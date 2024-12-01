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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class UsdToPlnStrategyTest {

    public static final String MID = "3.5";
    public static final String AMOUNT = "100";
    public static final String RESULT_AMOUNT = "28.5714";

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
        BigDecimal amount = new BigDecimal(AMOUNT);
        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setMid(Double.parseDouble(MID));

        when(exchangeRateService.getLatestExchangeRate(Currency.USD.getCode())).thenReturn(exchangeRate);

        // When
        ExchangeResult result = usdToPlnStrategy.execute(amount);

        // Then
        assertNotNull(result);
        assertEquals(Currency.USD.getName(), result.getFromCurrency());
        assertEquals(Currency.PLN.getName(), result.getToCurrency());
        assertEquals(amount, result.getAmount());
        assertEquals(new BigDecimal(RESULT_AMOUNT), result.getResultAmount());
        assertEquals(new BigDecimal(MID), result.getExchangeRate());

        verify(exchangeRateService).getLatestExchangeRate(Currency.USD.getCode());
        verifyNoMoreInteractions(exchangeRateService);
    }
}