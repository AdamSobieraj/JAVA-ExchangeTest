package com.example.javacurrency.exchange;

import com.example.javacurrency.common.Currency;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExchangeRequest {

    @NotNull
    private BigDecimal amount;
    @NotNull
    private Currency currency;
}
