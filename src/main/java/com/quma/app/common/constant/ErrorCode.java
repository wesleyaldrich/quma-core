package com.quma.app.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    SUCCESS("QMA-1"),
    BAD_REQUEST("QMA-2"),
    BAD_CODE("QMA-3"),
    MQTT_FAULT("QMA-4"),
    BAD_CUSTOMER("QMA-4"),
    UNEXPECTED_ERROR("QMA-0");

    private final String code;
}
