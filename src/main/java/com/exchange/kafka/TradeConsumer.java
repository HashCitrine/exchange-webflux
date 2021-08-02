package com.exchange.kafka;

import com.exchange.postgres.entity.Currency;
import com.exchange.postgres.service.ExchangeService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class TradeConsumer {

    private final KafkaProducer producer;
    private final ExchangeService exchangeService;

    @KafkaListener(topics = "submitOrder", groupId = "exchange")
    public void consume(String message) throws IOException {
        log.info("Consumed message : {}", message);

        Map<String, String> json = new Gson().fromJson(message, Map.class);
        String id = json.get("message");
        int interval = 1;

        // interval
        log.info(">>> start exchange");
        exchangeService.exchange(Long.parseLong(message))
//                .repeatWhen(longFlux -> Flux.interval(Duration.ofSeconds(interval)))
//                .take(5)
                .subscribe();
    }
}
