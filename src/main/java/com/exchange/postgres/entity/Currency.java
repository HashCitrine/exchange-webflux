package com.exchange.postgres.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("currency")
@Builder
public class Currency {

    @Id
    private String currency;

    private String currencyKr;

    private String currencyAbbr;

    private Long currentPrice;

    private Long previousPrice;

    private Long transactionPrice;
}
