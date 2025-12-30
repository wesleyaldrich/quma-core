package com.quma.app.common.response;

import com.quma.app.entity.Ticket;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
public class FinalResultResponse extends QumaResponse {
    private Ticket ticket;
}
