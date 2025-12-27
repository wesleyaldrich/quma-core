package com.quma.app.common.response;

import com.quma.app.common.dto.TicketsByBookingDateDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
public class TicketListResponse extends QumaResponse {
    private List<TicketsByBookingDateDto> activeReservations;
}
