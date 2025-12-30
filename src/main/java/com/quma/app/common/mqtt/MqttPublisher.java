package com.quma.app.common.mqtt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

import com.quma.app.common.exception.BadMqttException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MqttPublisher {

    private final MessageChannel mqttOutboundChannel;

    @Value("${mqtt.topic.cam-mode}")
    private String topicCamMode;

    public void publish(String payload) {
        try {
            Message<String> message = MessageBuilder
                    .withPayload(payload)
                    .setHeader(MqttHeaders.TOPIC, topicCamMode)
                    .setHeader(MqttHeaders.QOS, 1)
                    .build();

            mqttOutboundChannel.send(message);

        } catch (Exception e) {
            throw new BadMqttException("Failed to publish message");
        }
    }
}
