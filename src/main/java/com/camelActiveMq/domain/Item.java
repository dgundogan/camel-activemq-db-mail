package com.camelActiveMq.domain;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@ToString
public class Item {
    private String transactionType;
    private String skuu;
    private String itemDescription;
    private BigDecimal price;
}
