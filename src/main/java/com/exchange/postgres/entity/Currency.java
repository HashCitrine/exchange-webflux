package com.exchange.postgres.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("currency")
public class Currency {

    @Id
    private String currency;

    private String currencyKr;

    private String currencyAbbr;

    private Long currentPrice;

    private Long previousPrice;

    private Long transactionPrice;
}
