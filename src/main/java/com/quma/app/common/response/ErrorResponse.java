package com.quma.app.common.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
public class ErrorResponse extends QumaResponse {
    private String errorMessage;
}
