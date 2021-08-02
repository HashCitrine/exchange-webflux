package com.exchange.api;

import com.exchange.kafka.KafkaProducer;
import com.exchange.postgres.entity.Currency;
import com.exchange.postgres.entity.Order;
import com.exchange.postgres.entity.Trade;
import com.exchange.postgres.repository.CurrencyRepository;
import com.exchange.postgres.repository.MemberRepository;
import com.exchange.postgres.repository.OrderRepository;
import com.exchange.postgres.service.ExchangeService;
import com.exchange.postgres.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
@Log4j2
@RequiredArgsConstructor
public class TestController {

    private final MemberRepository memberRepository;
    private final ExchangeService exchangeService;
    private final CurrencyRepository currencyRepository;
    private final MemberService memberService;
    private final KafkaProducer producer;
    private final OrderRepository orderRepository;

    @PostMapping("/exchange")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Flux<Trade> exchange(@RequestBody Order order, @RequestParam("token") String token) {
        // 토큰 검증 생략
        log.info("token : " + token);

        log.info(">>> start exchange");
        log.info("orderId : {}", order.getOrderId());

//        return exchangeService.exchange(currency);
//        return exchangeService3.exchange(currency).repeat();
//        Long time = currency.getTransactionPrice();
        return exchangeService.exchange(order.getOrderId()).repeatWhen(longFlux -> Flux.interval(Duration.ofSeconds(1)));
    }

    @PostMapping("/exchange/{interval}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Flux<Trade> exchangeInterval(@RequestBody Order order, @PathVariable("interval") Long interval, @RequestParam("token") String token) {
        // 토큰 검증 생략
        log.info("token : " + token);
        
        log.info(">>> start exchange");
        log.info("orderId : {}", order.getOrderId());
        log.debug("interval : {}", interval);

//        return exchangeService.exchange(currency);
//        return exchangeService3.exchange(currency).repeat();
//        Long time = currency.getTransactionPrice();
        exchangeService.exchange(order.getOrderId())
                .repeatWhen(longFlux -> Flux.interval(Duration.ofSeconds(interval)))

                .subscribe();

        return Flux.just(new Trade());
    }

    @PostMapping("/test/kafka")
    public Mono<String> kafkaTest(@RequestParam("token") String token) {
        log.debug("debug test");
        log.info("info test");
        producer.sendMessage(token);
        return Mono.just("");
    }

    /*@PostMapping("/trade")
    public Mono<Trade> exchangeTest(@RequestBody Currency currency) {
        log.info("currency : " + currency.toString());
        return exchangeService.trade(currency, 0);
    }

    @GetMapping("/test/order")
    public Mono<Order> orderTest() {
        return orderRepository.testOrder(1L);
    }*/

}
