package com.exchange.postgres.entity;

import com.exchange.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
//@NoArgsConstructor
//@AllArgsConstructor
@Table("bankstatement")
public class Bankstatement {
    @Id
    private Long transactionId;
    private LocalDateTime transactionDate;
    private String memberId;
    private Constants.TRANSACTION_TYPE transactionType;
    private Long krw;

}
