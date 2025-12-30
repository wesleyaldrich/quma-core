package com.quma.app.common.exception;

public class BadMqttException extends RuntimeException {
    public BadMqttException(String message) {
        super(message);
    }
}
