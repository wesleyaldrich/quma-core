package com.quma.app.service;

import com.google.zxing.WriterException;
import com.quma.app.common.constant.ErrorCode;
import com.quma.app.common.response.TicketResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@AllArgsConstructor
public class TicketService {

    private final QrCodeService qrCodeService;

    public TicketResponse getTicketDummy(int width, int height) throws WriterException, IOException {
        var dummyTicket = qrCodeService.generate("This is a dummy text", width, height);

        return TicketResponse.builder()
                .ticketUrl(dummyTicket)
                .build();
    }
}
