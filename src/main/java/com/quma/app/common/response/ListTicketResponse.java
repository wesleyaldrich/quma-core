package com.quma.app.common.response;

import com.quma.app.entity.Ticket;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
public class ListTicketResponse extends QumaResponse {
    private List<Ticket> list_ticket;
}
