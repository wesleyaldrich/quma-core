package com.quma.app.entity;

import com.quma.app.common.constant.SessionStatus;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "sessions")
@Data
@Builder
public class Session {
    @Id
    private String id;

    private String sessionEpoch;

    @Builder.Default
    private SessionStatus status = SessionStatus.INITIATED;

    /* Should only be filled when SessionStatus.IDENTIFIED */
    private String ticketId;
    private String customerNo;

    /* To handle poll logic */
    @Builder.Default
    private boolean responded = false;
    private boolean valid;
    private String reason;

    /* Timestamps */
    @CreatedDate
    private Instant createdAt;
    @LastModifiedDate
    private Instant updatedAt;
}
