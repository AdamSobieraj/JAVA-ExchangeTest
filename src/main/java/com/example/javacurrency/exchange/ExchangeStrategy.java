package com.example.javacurrency.exchange;

import java.math.BigDecimal;

public interface ExchangeStrategy {
    ExchangeResult execute(BigDecimal amount);
}
