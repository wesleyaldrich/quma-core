package com.quma.app.service;

import com.quma.app.common.dto.FrResponseDto;
import com.quma.app.common.dto.QrResponseDto;
import com.quma.app.common.exception.BadParameterException;
import com.quma.app.common.response.ErrorResponse;
import com.quma.app.common.response.PollCameraResponse;
import com.quma.app.entity.Ticket;
import org.springframework.stereotype.Service;

@Service
public class CameraService {

    public ErrorResponse activateCamera(String mode) {
        if ("QR".equalsIgnoreCase(mode)) {
//            try {
//                /* TODO: Write to camera topic here. */
//            } catch (Exception e) {
//                /* TODO: Catch the exception that may occur. */
//            }

            return ErrorResponse.builder()
                    .errorMessage("Successfully activated camera in QR mode!")
                    .build();
        }
        else if ("FR".equalsIgnoreCase(mode)) {
//            try {
//                /* TODO: Write to camera topic here. */
//            } catch (Exception e) {
//                /* TODO: Catch the exception that may occur. */
//            }

            return ErrorResponse.builder()
                    .errorMessage("Successfully activated camera in FR mode!")
                    .build();
        }

        throw new BadParameterException("Unrecognized mode passed to Core.");
    }

    public PollCameraResponse pollCameraResult(String mode, String epochString) {
        if ("QR".equalsIgnoreCase(mode)) {
            /* Dummy line */
            return PollCameraResponse.builder()
                    .valid(true)
                    .build();
        }
        else if ("FR".equalsIgnoreCase(mode)) {
            /* Dummy line */
            return PollCameraResponse.builder()
                    .valid(true)
                    .build();
        }

        throw new BadParameterException("Unrecognized mode passed to Core.");
    }

    private QrResponseDto qrCameraResult() {


        return QrResponseDto.builder().build();
    }

    private FrResponseDto frCameraResult() {

        return FrResponseDto.builder().build();
    }



}
