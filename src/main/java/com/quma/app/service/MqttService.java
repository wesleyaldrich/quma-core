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
    private Double minimumConfidence;

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
        String ticketIdDecrypted = null;
        try {
            ticketIdDecrypted = cryptoService.decrypt(ticketId);
        } catch (IllegalStateException e) {
            log.error("Attempt on scanning invalid QR code on sessionEpoch={}.", sessionEpoch);
            return;
        }
        Optional<Ticket> ticketOptional = ticketRepository.findById(ticketIdDecrypted);
        if (ticketOptional.isEmpty()) {
            log.error("Couldn't find any ticket with ticketIdDecrypted={} when processing QR.", ticketIdDecrypted);
            return;
        }

        /* Determine validity */
        boolean valid = true;
        String reason = null;
        LocalDateTime now = LocalDateTime.now();
        Ticket ticket = ticketOptional.get();

        if (!ticket.isValid()) {
            valid = false;
            reason = "Ticket was cancelled by the customer.";
            log.info(reason);
        }
        else if (now.isBefore(ticket.getBookingDate())) {
            valid = false;
            reason = "Scanning before the promised booking date.";
            log.info(reason);
        }
        else if (now.isAfter(ticket.getBookingDate().plusMinutes(ticketLifetime))) {
            valid = false;
            reason = "Expired ticket.";
            log.info(reason);
        }

        /* Update the session */
        session.setStatus(SessionStatus.IDENTIFIED);
        session.setTicketId(ticket.getId());
        session.setCustomerNo(ticket.getCustomerNo());
        session.setValid(valid);
        session.setReason(reason);
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
        String reason = (valid)? null: "Face doesn't match!";

        /* Update the session */
        session.setStatus(SessionStatus.VERIFIED);
        session.setValid(valid);
        session.setReason(reason);
        session.setResponded(true);

        sessionRepository.save(session);
    }

}
