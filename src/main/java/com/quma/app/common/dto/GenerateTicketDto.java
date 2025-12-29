package com.quma.app.common.dto;

import com.quma.app.common.constant.TrxDetail;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
@Builder
public class GenerateTicketDto {
    private String customerNo;
    private String bookingDateString;
    private String branchName;
    private String trxTypeString;
    private HashMap<TrxDetail, Integer> trxDetails;
}
