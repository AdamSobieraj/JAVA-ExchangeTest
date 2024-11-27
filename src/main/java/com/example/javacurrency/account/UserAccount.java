package com.example.javacurrency.account;

import com.example.javacurrency.common.Currency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class UserAccount {

    UUID uuid;
    @NotBlank
    private String email;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotNull
    private BigDecimal balance;
    @NotNull
    private Currency currency;

}
