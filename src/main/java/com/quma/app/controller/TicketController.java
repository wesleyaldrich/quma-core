package com.quma.app.controller;

import com.google.zxing.WriterException;
import com.quma.app.common.response.TicketResponse;
import com.quma.app.service.TicketService;
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

    @GetMapping("/dummy")
    public ResponseEntity<TicketResponse> getTicketDummy(@RequestParam int width, @RequestParam int height)
            throws WriterException, IOException {

        return ResponseEntity.ok(ticketService.getTicketDummy(width, height));
    }

    @GetMapping(value = "/images/{fileName}", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getTicketImage(@PathVariable String fileName) throws IOException {
        Path filePath = Paths.get(storagePath).resolve(fileName);
        return Files.readAllBytes(filePath);
    }

}
