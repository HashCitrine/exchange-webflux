package com.exchange.api;

import com.exchange.postgres.entity.Currency;
import com.exchange.postgres.entity.Trade;
import com.exchange.postgres.repository.CurrencyRepository;
import com.exchange.postgres.repository.MemberRepository;
import com.exchange.postgres.service.ExchangeService1;
import com.exchange.postgres.service.ExchangeService2;
import com.exchange.postgres.service.ExchangeService;
import com.exchange.postgres.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
@Log4j2
@RequiredArgsConstructor
public class TestController {

    private MemberRepository memberRepository;
//    private ExchangeService1 exchangeService1;
//    private ExchangeService2 exchangeService2;
    private ExchangeService exchangeService;
    private CurrencyRepository currencyRepository;
    private MemberService memberService;

    @GetMapping("index")
    public Mono<String> test() {
        Mono<String>  message = Mono.just("hello, world");
        return message;
    }

    @PostMapping("/exchange")
    public Mono<Trade> exchange(@RequestBody Currency currency) {
        log.info(">>> start exchange");
        log.info("currency : " + currency.toString());

//        return exchangeService.exchange(currency);
//        return exchangeService2.exchange(currency);
        return null;
    }

    @PostMapping("/exchange/3")
    public Flux<Trade> exchange3(@RequestBody Currency currency) {
        log.info(">>> start exchange3");
        log.info("currency : " + currency.toString());

//        return exchangeService.exchange(currency);
//        return exchangeService3.exchange(currency).repeat();
        return exchangeService.exchange(currency).repeatWhen(longFlux -> Flux.interval(Duration.ofSeconds(1)));
    }


    /*@PostMapping("/trade")
    public Mono<Trade> exchangeTest(@RequestBody Currency currency) {
        log.info("currency : " + currency.toString());
        return exchangeService.trade(currency, 0);
    }*/


}
