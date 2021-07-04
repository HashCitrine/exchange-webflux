package com.exchange.postgres.entity;

import com.exchange.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("member")
public class Member {
    @Id
    private String memberId;
    private String password;
    private Constants.ROLE role;
    private Constants.YN useYn;
    private LocalDateTime regDate;
    private LocalDateTime uptDate;


}
