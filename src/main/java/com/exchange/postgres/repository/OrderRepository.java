package com.exchange.postgres.repository;

import com.exchange.Constants;
import com.exchange.postgres.entity.Currency;
import com.exchange.postgres.entity.Member;
import com.exchange.postgres.entity.Order;
import com.exchange.postgres.entity.Trade;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface OrderRepository extends R2dbcRepository<Order, String> {

    @Query("select order_type from \"order\" Where order_id = $1")
    Mono<Order> testOrder(Long orderId);
}
