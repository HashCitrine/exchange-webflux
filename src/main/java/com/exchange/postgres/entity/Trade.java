package com.exchange.postgres.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table("trade")
public class Trade {
    @Id
    private Long tradeId;
    private LocalDateTime tradeDate;
    private Long buyOrderId;
    private Long sellOrderId;
    private Long quantity;
}
