package com.exchange.postgres.entity;

import com.exchange.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("order")
@Builder
public class Order {
    @Id
    private Long orderId;
    private LocalDateTime orderDate;
    private String orderMember;
    private String currency;
    private Constants.ORDER_TYPE orderType;
    private Long price;
    private Long quantity;
    private Long stock;
}
