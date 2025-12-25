package com.quma.app.entity;

import com.quma.app.common.constant.TrxType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "tickets")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {

    @Id
    private String id;
    private String customerNo;
    private TrxType trxType;
    private LocalDateTime bookingDate;
    private String url;

    /* False when cancelled */
    @Builder.Default
    private boolean valid = true;

/*    *//* Timestamps *//*
    @CreatedDate
    private Instant createdAt;
    @LastModifiedDate
    private Instant updatedAt;*/
}
