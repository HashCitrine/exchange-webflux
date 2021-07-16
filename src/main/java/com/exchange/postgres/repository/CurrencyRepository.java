package com.exchange.postgres.repository;

import com.exchange.postgres.entity.Currency;
import com.exchange.postgres.entity.Order;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CurrencyRepository extends R2dbcRepository<Currency, String> {

    @Query("SELECT 'test' AS currency, (Q1.price * Q1.stock + Q2.price * Q2.stock) / (Q1.stock + Q2.stock) AS current_price " +
            "FROM (select A.price, SUM(A.stock) AS stock " +
                "FROM (SELECT MAX(price) as price, SUM(stock) as stock  FROM \"order\" WHERE stock > 0 AND order_type = 'BUY' AND currency = $1 GROUP BY stock ORDER BY price desc LIMIT 1) A " +
                "GROUP BY A.price) Q1 " +
                ",(SELECT B.price, SUM(B.stock) AS stock " +
                "FROM (SELECT MIN(price) as price, SUM(stock) as stock FROM \"order\" WHERE stock > 0 and order_type = 'SELL' AND currency = $1 GROUP BY stock ORDER BY price LIMIT 1) B " +
                "GROUP BY B.price) Q2")
    Mono<Currency> currentPrice(String currency);

    @Query("UPDATE currency SET current_price = $1 WHERE currency = $2")
    Mono<Object> updateCurrentPrice(Long currentPrice, String currency);
}
