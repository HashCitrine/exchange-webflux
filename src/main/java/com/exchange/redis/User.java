package com.exchange.redis;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
//@AllArgsConstructor
public class User implements Serializable {

    private int id;
    private String fullName;

}