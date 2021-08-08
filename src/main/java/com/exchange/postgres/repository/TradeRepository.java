package com.exchange.postgres.repository;

import com.exchange.postgres.entity.Currency;
import com.exchange.postgres.entity.Member;
import com.exchange.postgres.entity.Trade;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface TradeRepository extends R2dbcRepository<Trade, String> {

    @Query("SELECT Q1.order_id AS buy_order_id, Q2.order_id AS sell_order_id, " +
            "CASE WHEN Q1.stock > Q2.stock THEN Q2.stock ELSE Q1.stock END quantity " +
            "FROM (SELECT order_id, order_member, stock FROM \"order\" WHERE currency = $1 AND price = $2 AND \"order_type\" = 'BUY' AND stock > 0 ORDER BY order_date) Q1 " +
            ",(SELECT order_id, order_member, stock FROM \"order\" WHERE currency = $1 AND price = $2 AND \"order_type\" = 'SELL' AND stock > 0 ORDER BY order_date) Q2 " +
            "WHERE Q1.order_member != Q2.order_member " +
            "ORDER BY Q1.order_id, Q2.order_id " +
            "LIMIT 1")
    Mono<Trade> findTradeOrder(String currency, Long currentPrice);

    @Query("INSERT INTO \"trade\"(trade_date, buy_order_id, sell_order_id, quantity) " +
            "VALUES(now(), $1, $2, $3)")
    Mono<Trade> insertTrade(Long buyOrderId, Long sellOrderId, Long quantity);

    @Query("UPDATE \"order\" " +
            "SET stock = stock - $3, status = 'SUCS'" +
            "WHERE order_id = $1 OR order_id = $2")
    Mono<Trade> updateSuccessOrder(Long buyOrderId, Long sellOrderId, Long quantity);
}
