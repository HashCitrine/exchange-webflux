package com.exchange.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class KafkaProducer {
    private static final String TOPIC = "tokenVerify";
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String message){
        log.info("Produce message : {}", message);
        kafkaTemplate.send(TOPIC, message);
    }
}
