package com.exchange.postgres.repository;

import com.exchange.postgres.entity.Wallet;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends R2dbcRepository<Wallet, String> {

}
