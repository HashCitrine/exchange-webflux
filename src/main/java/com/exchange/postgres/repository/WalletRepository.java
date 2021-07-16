package com.exchange.postgres.repository;

import com.exchange.postgres.entity.Trade;
import com.exchange.postgres.entity.Wallet;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface WalletRepository extends R2dbcRepository<Wallet, String> {

    @Query("select 1")
    void updateBuyOrderWallet(Long tradeId);

    @Query("select 1")
    void updateSellOrderWallet(Long sellOrderId);
}
