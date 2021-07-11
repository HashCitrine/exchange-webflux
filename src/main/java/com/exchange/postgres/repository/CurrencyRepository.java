package com.exchange.postgres.repository;

import com.exchange.postgres.entity.Currency;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends R2dbcRepository<Currency, String> {

}
