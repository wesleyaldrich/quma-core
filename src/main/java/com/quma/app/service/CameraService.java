package com.quma.app.service;

import com.quma.app.common.constant.SessionStatus;
import com.quma.app.common.exception.BadParameterException;
import com.quma.app.common.response.ErrorResponse;
import com.quma.app.common.response.PollCameraResponse;
import com.quma.app.entity.Session;
import com.quma.app.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CameraService {

    private final SessionRepository sessionRepository;

    public ErrorResponse activateCamera(String mode, String sessionId) {
        /* Validations */
        validateMode(mode);
        if (sessionId == null || sessionId.isEmpty()) {
            throw new BadParameterException("Session id not found in header.");
        }

        try {
            if ("QR".equalsIgnoreCase(mode)) {
                generateSession(sessionId);

                /* TODO: Write to camera QR topic here. */

            }
            else if ("FR".equalsIgnoreCase(mode)) {
                /* Find the existing session */
                var sessionOptional = sessionRepository.findBySessionEpoch(sessionId);
                if (sessionOptional.isEmpty()) {
                    throw new BadParameterException("Session with the provided epoch doesn't exist in Core.");
                }

                /* Validate session status */
                var sessionStatus = sessionOptional.get().getStatus();
                switch (sessionStatus) {
                    case INITIATED:
                        throw new BadParameterException("Detected attempt to FR before ticket is identified!");
                    case IDENTIFIED:
                        /* Normal condition */
                        break;
                    case VERIFIED:
                        throw new BadParameterException("Customer is already verified!");
                }

                /* TODO: Write to camera FR topic here. */

            }
        } catch (Exception e) {
            /* TODO: Throw the exception that may occur. */

        }

        return ErrorResponse.builder()
                .errorMessage("Successfully activated camera in " + mode.toUpperCase() + " mode!")
                .build();
    }

    public PollCameraResponse pollCameraResult(String mode, String sessionId) {
        /* Validations */
        validateMode(mode);
        if (sessionId == null || sessionId.isEmpty()) {
            throw new BadParameterException("Session id not found in header.");
        }

        /* Find the existing session */
        var sessionOptional = sessionRepository.findBySessionEpoch(sessionId);
        if (sessionOptional.isEmpty()) {
            throw new BadParameterException("Session with the provided epoch doesn't exist in Core.");
        }

        var session = sessionOptional.get();

        if ("QR".equalsIgnoreCase(mode) && (session.getStatus() != SessionStatus.INITIATED)) {
            throw new BadParameterException("Unexpected use of poll to a session.");
        }
        if ("FR".equalsIgnoreCase(mode) && (session.getStatus() != SessionStatus.IDENTIFIED)) {
            throw new BadParameterException("Unexpected use of poll to a session.");
        }

        /* Return poll result */
        return PollCameraResponse.builder()
                .responded(session.isResponded())
                .validQr(session.isValidQr())
                .validFr(session.isValidFr())
                .build();
    }

    private void validateMode(String mode) {
        if (!("QR".equalsIgnoreCase(mode) || "FR".equalsIgnoreCase(mode))) {
            throw new BadParameterException("Unrecognized mode passed to Core.");
        }
    }

    private void generateSession(String sessionEpochString) {
        var newSession = Session.builder()
                .sessionEpoch(sessionEpochString)
                .build();

        sessionRepository.save(newSession);
    }

}
