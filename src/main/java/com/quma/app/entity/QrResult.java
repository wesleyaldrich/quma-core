package com.quma.app.entity;

import com.quma.app.common.constant.TrxType;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDateTime;

@Document(collection = "qr_results")
@Data
@Builder
public class QrResult {

    @Id
    private String id;
    private String customerNo;
    private TrxType trxType;
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