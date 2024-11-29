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

class UsdToPlnStrategyTest {

    @Mock
    private ExchangeRateService exchangeRateService;

    private UsdToPlnStrategy usdToPlnStrategy;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usdToPlnStrategy = new UsdToPlnStrategy(exchangeRateService);
    }

    @Test
    @SneakyThrows
    void testExchangeUsdToPln() {
        // Given
        BigDecimal amount = new BigDecimal("100");
        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setMid(Double.parseDouble("3.5"));

        when(exchangeRateService.getLatestExchangeRate(Currency.USD.getCode())).thenReturn(exchangeRate);

        // When
        ExchangeResult result = usdToPlnStrategy.execute(amount);

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