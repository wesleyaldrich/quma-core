package com.quma.app.common.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TicketResponse {
    private String ticketUrl;
}
