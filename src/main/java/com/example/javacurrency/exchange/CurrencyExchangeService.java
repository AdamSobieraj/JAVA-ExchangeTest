package com.example.javacurrency.exchange;

import com.example.javacurrency.common.Currency;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

@RequiredArgsConstructor
public class CurrencyExchangeService {

    private final ExchangeRateService exchangeRateService;

    public ExchangeResult exchangePlnToUsd(BigDecimal amount) throws IOException {
        ExchangeRate exchangeRate = exchangeRateService.getLatestExchangeRate();

        BigDecimal resultAmount = amount.multiply(BigDecimal.valueOf(exchangeRate.getMid()));

        ExchangeResult result = new ExchangeResult();
        result.setFromCurrency(Currency.PLN.getName());
        result.setToCurrency(Currency.USD.getName());
        result.setAmount(amount);
        result.setResultAmount(resultAmount);
        result.setExchangeRate(BigDecimal.valueOf(exchangeRate.getMid()));

        return result;
    }

    public ExchangeResult exchangeUsdToPln(BigDecimal amount) throws IOException {
        ExchangeRate exchangeRate = exchangeRateService.getLatestExchangeRate();

        BigDecimal resultAmount = amount.divide(BigDecimal.valueOf(exchangeRate.getMid()), 4, RoundingMode.HALF_UP);

        ExchangeResult result = new ExchangeResult();
        result.setFromCurrency(Currency.USD.getName());
        result.setToCurrency(Currency.PLN.getName());
        result.setAmount(amount);
        result.setResultAmount(resultAmount);
        result.setExchangeRate(BigDecimal.valueOf(exchangeRate.getMid()));

        return result;
    }

}
