package com.example.javacurrency.exchange;

import com.example.javacurrency.common.Currency;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class CurrencyExchangeServiceTest {

    @Mock
    private ExchangeRateService exchangeRateService;

    private CurrencyExchangeService currencyExchangeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        currencyExchangeService = new CurrencyExchangeService(exchangeRateService);
    }

    @Test
    @SneakyThrows
    void testExchangePlnToUsd() {
        // Given
        BigDecimal amount = new BigDecimal("100");
        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setMid(Double.parseDouble("3.5"));
        when(exchangeRateService.getLatestExchangeRate(any())).thenReturn(exchangeRate);

        ExchangeRequest exchangeRequest = new ExchangeRequest();
        exchangeRequest.setAmount(amount);
        exchangeRequest.setCurrency(Currency.USD);

        // When
        ExchangeResult result = currencyExchangeService.exchange(exchangeRequest);

        // Then
        assertNotNull(result);
        assertEquals(Currency.PLN.getName(), result.getFromCurrency());
        assertEquals(Currency.USD.getName(), result.getToCurrency());
        assertEquals(amount, result.getAmount());
        assertEquals(new BigDecimal("350.0"), result.getResultAmount());
        assertEquals(new BigDecimal("3.5"), result.getExchangeRate());

        verify(exchangeRateService).getLatestExchangeRate(Currency.USD.getCode());
        verifyNoMoreInteractions(exchangeRateService);
    }

    @Test
    @SneakyThrows
    void testExchangeUsdToPln() {
        // Given
        BigDecimal amount = new BigDecimal("100");
        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setMid(Double.parseDouble("3.5"));
        when(exchangeRateService.getLatestExchangeRate(any())).thenReturn(exchangeRate);

        ExchangeRequest exchangeRequest = new ExchangeRequest();
        exchangeRequest.setAmount(amount);
        exchangeRequest.setCurrency(Currency.PLN);

        // When
        ExchangeResult result = currencyExchangeService.exchange(exchangeRequest);

        // Then
        assertNotNull(result);
        assertEquals(Currency.USD.getName(), result.getFromCurrency());
        assertEquals(Currency.PLN.getName(), result.getToCurrency());
        assertEquals(amount, result.getAmount());
        assertEquals(new BigDecimal("28.5714"), result.getResultAmount());
        assertEquals(new BigDecimal("3.5"), result.getExchangeRate());

        verify(exchangeRateService).getLatestExchangeRate(Currency.USD.getCode());
        verifyNoMoreInteractions(exchangeRateService);
    }
}