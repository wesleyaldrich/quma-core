package com.quma.app.common.dto;

import com.quma.app.entity.Ticket;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class TicketsByBookingDateDto {
    private LocalDate bookingDate;
    private List<Ticket> tickets;
}
