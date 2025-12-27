package com.quma.app.common.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GenerateTicketRequest {
    private String customerNo;
    private String trxTypeString;
    private String bookingDateString;
    private String branchName;
}
