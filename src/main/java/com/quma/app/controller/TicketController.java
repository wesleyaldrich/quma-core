package com.quma.app.controller;

import com.google.zxing.WriterException;
import com.quma.app.common.dto.GenerateTicketDto;
import com.quma.app.common.request.GenerateTicketRequest;
import com.quma.app.common.response.ErrorResponse;
import com.quma.app.common.response.TicketListResponse;
import com.quma.app.service.TicketService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @Value("${ticket.storage-path}")
    private String storagePath;

    @GetMapping(value = "/images/{fileName}", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getTicketImage(@PathVariable String fileName) throws IOException {
        Path filePath = Paths.get(storagePath).resolve(fileName);
        return Files.readAllBytes(filePath);
    }

    @PostMapping
    public ResponseEntity<ErrorResponse> generateTicket(
            HttpServletRequest request,
            @RequestBody GenerateTicketRequest requestBody,
            @RequestParam int width,
            @RequestParam int height
    ) throws IOException, WriterException {

        String customerNo = request.getHeader("x-customer-no");
        String bookingDateString = requestBody.getBookingDate();
        String branchName = requestBody.getBranchName();
        String trxTypeString = requestBody.getTrxType();
        var trxDetails = requestBody.getTrxDetails();

        var generateTicketDto = GenerateTicketDto.builder()
                .customerNo(customerNo)
                .bookingDateString(bookingDateString)
                .branchName(branchName)
                .trxTypeString(trxTypeString)
                .trxDetails(trxDetails)
                .build();

        return ResponseEntity.ok(ticketService.generateTicket(generateTicketDto, width, height));
    }

    @GetMapping
    public ResponseEntity<TicketListResponse> getTicketList(HttpServletRequest request) {
        return ResponseEntity.ok(ticketService.getTicketList(request.getHeader("x-customer-no")));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ErrorResponse> cancelTicket(@PathVariable String id) {
        return ResponseEntity.ok(ticketService.cancelTicket(id));
    }

}
