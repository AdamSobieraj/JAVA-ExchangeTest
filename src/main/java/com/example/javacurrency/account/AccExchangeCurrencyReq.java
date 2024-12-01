package com.example.javacurrency.account;

import com.example.javacurrency.common.Currency;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


import java.util.UUID;

@Data
public class AccExchangeCurrencyReq {

    @NotNull
    private UUID uuid;
    @NotNull
    private Currency currency;

}
