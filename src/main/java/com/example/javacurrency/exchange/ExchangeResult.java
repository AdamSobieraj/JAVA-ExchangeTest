package com.example.javacurrency.exchange;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExchangeResult {
    private String fromCurrency;
    private String toCurrency;
    private BigDecimal amount;
    private BigDecimal resultAmount;
    private BigDecimal exchangeRate;
}
