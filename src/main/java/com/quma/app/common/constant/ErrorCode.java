package com.quma.app.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    SUCCESS("QMA-1"),
    BAD_REQUEST("QMA-2"),
    UNAUTHORIZED("QMA-3"),
    UNEXPECTED_ERROR("QMA-0");

    private final String code;
}
