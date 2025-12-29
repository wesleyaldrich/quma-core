package com.quma.app.service;

import com.quma.app.common.constant.SessionStatus;
import com.quma.app.entity.Session;
import com.quma.app.entity.Ticket;
import com.quma.app.repository.SessionRepository;
import com.quma.app.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MqttService {

    private final SessionRepository sessionRepository;
    private final TicketRepository ticketRepository;
    private final CryptoService cryptoService;

    @Value("${fr.minimum-confidence-level}")
    private Integer minimumConfidence;

    @Value("${ticket.ttl}")
    private Integer ticketLifetime;

    public void processMqttQrMessage(String sessionEpoch, String ticketId) {
        /* Check for invalid sessionEpoch */
        if (sessionEpoch == null || sessionEpoch.isEmpty()) {
            log.error("Value of null or empty in sessionEpoch passed to processMqttQrMessage!");
            return;
        }

        /* Find the session */
        Optional<Session> sessionOptional = sessionRepository.findBySessionEpoch(sessionEpoch);
        if (sessionOptional.isEmpty()) {
            log.error("Couldn't find any session with sessionEpoch={} when processing QR.", sessionEpoch);
            return;
        }

        Session session = sessionOptional.get();

        /* Illegal state */
        if (session.getStatus() != SessionStatus.INITIATED) {
            log.error("Consumed unexpected QR data to sessionEpoch={}", sessionEpoch);
            return;
        }

        /* Decrypt and find the ticket */
        String ticketIdDecrypted = cryptoService.decrypt(ticketId);
        Optional<Ticket> ticketOptional = ticketRepository.findById(ticketIdDecrypted);
        if (ticketOptional.isEmpty()) {
            log.error("Couldn't find any ticket with ticketIdDecrypted={} when processing QR.", ticketIdDecrypted);
            return;
        }

        /* Determine validity */
        boolean valid = true;
        LocalDateTime now = LocalDateTime.now();
        Ticket ticket = ticketOptional.get();

        if (!ticket.isValid()) {
            log.info("Ticket was cancelled by the customer.");
            valid = false;
        }
        else if (now.isBefore(ticket.getBookingDate())) {
            log.info("Ticket is scanned before the promised bookingDate.");
            valid = false;
        }
        else if (now.isAfter(ticket.getBookingDate().plusMinutes(ticketLifetime))) {
            log.info("Ticket is expired.");
            valid = false;
        }

        /* Update the session */
        session.setStatus(SessionStatus.IDENTIFIED);
        session.setTicketId(ticket.getId());
        session.setCustomerNo(ticket.getCustomerNo());
        session.setValid(valid);
        session.setResponded(true);

        sessionRepository.save(session);
    }

    public void processMqttFrMessage(String sessionEpoch, int confidenceRate) {
        /* Check for invalid sessionEpoch */
        if (sessionEpoch == null || sessionEpoch.isEmpty()) {
            log.error("Value of null or empty in sessionEpoch passed to processMqttFrMessage!");
            return;
        }

        /* Find the session */
        Optional<Session> sessionOptional = sessionRepository.findBySessionEpoch(sessionEpoch);
        if (sessionOptional.isEmpty()) {
            log.error("Couldn't find any session with sessionEpoch={} when processing FR.", sessionEpoch);
            return;
        }

        Session session = sessionOptional.get();

        /* Illegal state */
        if (session.getStatus() != SessionStatus.IDENTIFIED) {
            log.error("Consumed unexpected FR data to sessionEpoch={}", sessionEpoch);
            return;
        }

        /* Determine validity */
        boolean valid = (confidenceRate >= minimumConfidence);

        /* Update the session */
        session.setStatus(SessionStatus.VERIFIED);
        session.setValid(valid);
        session.setResponded(true);

        sessionRepository.save(session);
    }

}
