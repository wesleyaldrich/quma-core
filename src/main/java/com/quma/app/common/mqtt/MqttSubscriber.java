package com.quma.app.common.mqtt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.quma.app.service.MqttService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MqttSubscriber {

    private final MqttService mqttService;

    @Value("${mqtt.topic.qr-out}")
    private String topicQrOut;

    @Value("${mqtt.topic.fr-out}")
    private String topicFrOut;

    @ServiceActivator(inputChannel = "mqttInboundChannel")
    public void onMessage(Message<String> message) {

        String topic = message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC, String.class);

        String payload = message.getPayload();

        if (topicFrOut.equals(topic)) {
            handleQr(payload);
        }
        else if (topicQrOut.equals(topic)) {
            handleFr(payload);
        }
    }

    private void handleQr(String payload) {
        String[] parts = payload.split("#");

        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid FR payload: " + payload);
        }

        String ticketId = parts[0];
        String sessionId = parts[1];

        mqttService.processMqttQrMessage(sessionId, ticketId);
    }

    private void handleFr(String payload) {
        String[] parts = payload.split("#");

        if (parts.length != 4) {
            throw new IllegalArgumentException("Invalid FR payload: " + payload);
        }

        // boolean matched = Boolean.parseBoolean(parts[0]); not used
        int confidence = (int) Float.parseFloat(parts[1]);
        String sessionId = parts[2];
        // String userId = parts[3]; not used

        mqttService.processMqttFrMessage(sessionId, confidence);
    }
}
