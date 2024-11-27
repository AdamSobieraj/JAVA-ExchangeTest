package com.example.javacurrency.common;

import lombok.Getter;

@Getter
public enum Currency {
    PLN("PLN", "Polish Złoty"),
    USD("USD", "United States Dollar");

    private final String code;
    private final String name;

    Currency(String code, String name) {
        this.code = code;
        this.name = name;
    }

}
