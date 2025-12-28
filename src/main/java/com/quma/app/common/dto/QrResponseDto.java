package com.quma.app.common.dto;

import com.quma.app.entity.Ticket;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QrResponseDto {
    private Ticket ticket;
}
