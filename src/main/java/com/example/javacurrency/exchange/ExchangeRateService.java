package com.example.javacurrency.exchange;

import com.example.javacurrency.common.Currency;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@RequiredArgsConstructor
public class ExchangeRateService {

    private final RestTemplate restTemplate;
    private final String nbpUrl;

    public ExchangeRate getLatestExchangeRate() throws IOException {
        String response = restTemplate.getForObject(nbpUrl, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = response.replaceAll("^\\[|\\]$", "");
        ExchangeRateTable exchangeRateTable = objectMapper.readValue(jsonString, ExchangeRateTable.class);

        return exchangeRateTable.getRates().stream()
                .filter(entry -> entry.getCode().equals(Currency.USD.getCode()))
                .findFirst()
                .map(this::createExchangeRate)
                .orElseThrow(() -> new RuntimeException("Unable to fetch USD exchange rate"));
    }

    private ExchangeRate createExchangeRate(ExchangeRate currency) {

        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setCode(currency.getCode());
        exchangeRate.setCurrency(currency.getCode());
        exchangeRate.setMid(currency.getMid());
        return exchangeRate;
    }

}
