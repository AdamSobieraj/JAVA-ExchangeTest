package com.example.javacurrency.exchange;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExchangeRequest {

    @NotBlank
    private String currencyFrom;
    @NotBlank
    private String currencyTo;
    @NotNull
    private BigDecimal amount;
}
