package com.exchange.postgres.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("wallet")
public class Wallet {

    @Id
    private String memberId;

    private String currency;

    private Long quantity;

    private Long averagePrice;
}
