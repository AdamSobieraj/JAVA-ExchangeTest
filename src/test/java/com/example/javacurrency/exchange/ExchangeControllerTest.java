package com.example.javacurrency.exchange;

import com.example.javacurrency.common.Currency;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ExchangeControllerTest {

    private static final String BASE_URL = "/api/v1/exchange";

    @Mock
    private CurrencyExchangeService currencyExchangeService;

    @InjectMocks
    private ExchangeController exchangeController;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    ExchangeResult expectedResult;
    ExchangeRequest requestUSDtoPLN;
    ExchangeRequest requestPLNtoUSD;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(exchangeController).build();

        expectedResult = new ExchangeResult();

        expectedResult.setFromCurrency(Currency.USD.getName());
        expectedResult.setToCurrency(Currency.PLN.getName());
        expectedResult.setAmount(new BigDecimal("100"));
        expectedResult.setResultAmount(new BigDecimal("87.32"));
        expectedResult.setExchangeRate(new BigDecimal("0.8732"));

        requestUSDtoPLN = new ExchangeRequest();
        requestUSDtoPLN.setCurrencyFrom(Currency.USD.getName());
        requestUSDtoPLN.setCurrencyTo(Currency.PLN.getName());
        requestUSDtoPLN.setAmount(new BigDecimal("100"));

        requestPLNtoUSD = new ExchangeRequest();
        requestPLNtoUSD.setCurrencyFrom(Currency.PLN.getName());
        requestPLNtoUSD.setCurrencyTo(Currency.USD.getName());
        requestPLNtoUSD.setAmount(new BigDecimal("100"));
    }

    @Test
    @SneakyThrows
    void testExchangePlnToUsd() {
        // Given
        when(currencyExchangeService.exchangePlnToUsd(any())).thenReturn(expectedResult);

        // When
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/pln-to-usd")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestPLNtoUSD)))
                .andExpect(status().isOk())
                .andReturn();


        String responseContent = result.getResponse().getContentAsString();
        ExchangeResult actualResult = objectMapper.readValue(responseContent, ExchangeResult.class);

        // Then
        assertEquals(BigDecimal.valueOf(87.32), actualResult.getResultAmount());
        assertEquals(BigDecimal.valueOf(0.8732), actualResult.getExchangeRate());
        assertEquals(Currency.USD.getName(), actualResult.getFromCurrency());
        assertEquals(Currency.PLN.getName(), actualResult.getToCurrency());
    }

    @Test
    @SneakyThrows
    void testExchangeUsdToPln() {
        // Given
        when(currencyExchangeService.exchangeUsdToPln(any())).thenReturn(expectedResult);

        // When
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/usd-to-pln")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestUSDtoPLN))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        String responseContent = result.getResponse().getContentAsString();
        ExchangeResult actualResult = objectMapper.readValue(responseContent, ExchangeResult.class);

        assertEquals(BigDecimal.valueOf(87.32), actualResult.getResultAmount());
        assertEquals(BigDecimal.valueOf(0.8732), actualResult.getExchangeRate());
        assertEquals(Currency.USD.getName(), actualResult.getFromCurrency());
        assertEquals(Currency.PLN.getName(), actualResult.getToCurrency());
    }
}