package com.quma.app.common.response;

import com.quma.app.common.constant.ErrorCode;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public abstract class QumaResponse {
    @Builder.Default
    private String errorCode = ErrorCode.SUCCESS.getCode();
}
