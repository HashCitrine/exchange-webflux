package com.exchange.postgres.entity;

import com.exchange.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("order")
public class Order {
    @Id
    private Long orderId;
    private Date orderDate;
    private String orderMember;
    private String currency;
    private Constants.ROLE role;
}
