package com.quma.app.controller;

import com.quma.app.common.response.ErrorResponse;
import com.quma.app.common.response.PollCameraResponse;
import com.quma.app.service.CameraService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/camera")
@RequiredArgsConstructor
public class CameraController {

    private final CameraService cameraService;

    @PostMapping
    public ResponseEntity<ErrorResponse> activateCamera(HttpServletRequest request, @RequestParam String mode) {
        String sessionId = request.getHeader("x-session-id");

        return ResponseEntity.ok(cameraService.activateCamera(mode, sessionId));
    }

    @GetMapping
    public ResponseEntity<PollCameraResponse> pollCameraResult(HttpServletRequest request, @RequestParam String mode) {
        String sessionId = request.getHeader("x-session-id");

        return ResponseEntity.ok(cameraService.pollCameraResult(mode, sessionId));
    }

}
