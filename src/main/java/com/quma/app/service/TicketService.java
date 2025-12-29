package com.quma.app.service;

import com.google.zxing.WriterException;
import com.quma.app.common.constant.TrxType;
import com.quma.app.common.exception.BadParameterException;
import com.quma.app.common.dto.GenerateTicketDto;
import com.quma.app.common.response.ErrorResponse;
import com.quma.app.common.response.TicketListResponse;
import com.quma.app.entity.Ticket;
import com.quma.app.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketService {

    private final QrCodeService qrCodeService;
    private final CryptoService cryptoService;
    private final TicketRepository ticketRepository;

    @Value("${ticket.storage-path}")
    private String storagePath;

    public ErrorResponse generateTicket(GenerateTicketDto request, int width, int height) throws IOException, WriterException {
        String customerNo = request.getCustomerNo();
        String trxTypeString = request.getTrxTypeString();
        String bookingDateString = request.getBookingDateString();
        String branchName = request.getBranchName();
        var trxDetails = request.getTrxDetails();

        TrxType trxType;
        try {
            trxType = TrxType.valueOf(trxTypeString);
        } catch (IllegalArgumentException e) {
            throw new BadParameterException("The value of trxType is unrecognized.");
        }

        LocalDateTime bookingDate;
        try {
            bookingDate = LocalDateTime.parse(bookingDateString);
        } catch (DateTimeParseException e) {
            throw new BadParameterException("Booking date can't be parsed correctly based on ISO-8601.");
        }

        /* Create new ticket */
        String fileName = UUID.randomUUID() + ".png";
        String ticketUrl = "/tickets/images/" + fileName;
        Ticket ticket = Ticket.builder()
                .customerNo(customerNo)
                .trxType(trxType)
                .trxDetails(trxDetails)
                .bookingDate(bookingDate)
                .branchName(branchName)
                .url(ticketUrl)
                .build();

        ticketRepository.save(ticket);

        String ticketId = ticket.getId();
        String ticketIdEncrypted = cryptoService.encrypt(ticketId);
        String ticketIdDecrypted = cryptoService.decrypt(ticketIdEncrypted);

        log.info("ticketJson: {}", ticketId);
        log.info("ticketIdEncrypted: {}", ticketIdEncrypted);
        log.info("ticketIdDecrypted: {}", ticketIdDecrypted);

        /* Generate QR then save ticket */
        BufferedImage image = qrCodeService.generate(ticketIdEncrypted, width, height);
        save(image, fileName);

        return ErrorResponse.builder().errorMessage("Successfully created new ticket!").build();
    }

    private void save(BufferedImage image, String fileName) throws IOException {
        Path directory = Paths.get(storagePath);
        Files.createDirectories(directory);

        Path filePath = directory.resolve(fileName);
        ImageIO.write(image, "png", filePath.toFile());
    }

    public TicketListResponse getTicketList(String customerNo) {
        var activeReservations = ticketRepository.findGroupedByBookingDate(customerNo);

        return TicketListResponse.builder()
                .activeReservations(activeReservations)
                .build();
    }

    public ErrorResponse cancelTicket(String ticketId) {
        var targetedTicketOptional = ticketRepository.findById(ticketId);

        if (targetedTicketOptional.isEmpty()) {
            throw new BadParameterException("Ticket with the provided id not found!");
        }

        var targetedTicket = targetedTicketOptional.get();
        targetedTicket.setValid(false);
        ticketRepository.save(targetedTicket);

        return ErrorResponse.builder().errorMessage("Successfully cancelled ticket!").build();
    }

}
