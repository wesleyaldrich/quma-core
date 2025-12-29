package com.quma.app.entity;

import com.quma.app.common.constant.TrxDetail;
import com.quma.app.common.constant.TrxType;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Document(collection = "tickets")
@Data
@Builder
public class Ticket {

    @Id
    private String id;
    private String customerNo;
    private TrxType trxType;
    private HashMap<TrxDetail, Integer> trxDetails;
    private LocalDateTime bookingDate;
    private String branchName;
    private String url;

    /* False when cancelled */
    @Builder.Default
    private boolean valid = true;

    /* Timestamps */
    @CreatedDate
    private Instant createdAt;
    @LastModifiedDate
    private Instant updatedAt;
}
