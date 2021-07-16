package com.exchange.postgres.service;

import com.exchange.postgres.entity.Currency;
import com.exchange.postgres.entity.Trade;
import com.exchange.postgres.repository.CurrencyRepository;
import com.exchange.postgres.repository.OrderRepository;
import com.exchange.postgres.repository.TradeRepository;
import com.exchange.postgres.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExchangeService {
    private final CurrencyRepository currencyRepository;
    private final WalletRepository walletRepository;
    private final OrderRepository orderRepository;
    private final TradeRepository tradeRepository;

    public Mono<Trade> exchange(Currency currency) {
        log.info(">>> exchange process");

        // 1. 현재가 계산
        return currencyRepository.currentPrice(currency.getCurrency())
                .flatMap(currentPrice -> {
                    // 2. 현재가 반영(Currency - current_price)
                    currencyRepository.updateCurrentPrice(currentPrice.getCurrentPrice(), currentPrice.getCurrency()).subscribe();
                    // 3. 거래 진행
                    return trade(currentPrice, 1);
                });
    }

    // 3. 거래 진행
    public Mono<Trade> trade(Currency currency, int i) {
        log.info(">>> find trade target");
        // 1) 거래대상 order건 찾기
        return tradeRepository.findTradeOrder(currency.getCurrency(), currency.getCurrentPrice())
                .flatMap(tradeInfo -> {
                    log.info(">>> buyOrderId : " + tradeInfo.getBuyOrderId() + ", sellOrderId : " + tradeInfo.getSellOrderId() + ", stock : " + tradeInfo.getQuantity());
                    // 2) 조건에 맞는 매수, 매도자가 없을 경우 로직 종료
                    if(tradeInfo.getBuyOrderId() == null || tradeInfo.getSellOrderId() == null){
                            return Mono.just(new Trade());
//                        }
                    } else {
                        log.info(">>> success find trade target! try trade...");
                        // 3) 조건에 맞는 매수, 매도자가 있을 경우 : trade 테이블에 insert & 거래된만큼 stock량 줄이기
                        tradeRepository.updateSuccessOrder(tradeInfo.getBuyOrderId(), tradeInfo.getSellOrderId(), tradeInfo.getQuantity()).subscribe();
                        return tradeRepository.insertTrade(tradeInfo.getBuyOrderId(), tradeInfo.getSellOrderId(), tradeInfo.getQuantity());
                    }
                });
    }
}
