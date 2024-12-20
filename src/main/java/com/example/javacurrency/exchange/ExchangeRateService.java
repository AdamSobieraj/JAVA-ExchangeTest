package com.example.javacurrency.exchange;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ExchangeRateService {

    private final RestTemplate restTemplate;
    private final String nbpUrl;
    private final ConcurrentHashMap<String, ExchangeRate> exchangeRates = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler;

    public ExchangeRateService(RestTemplate restTemplate, String nbpUrl) {
        this.restTemplate = restTemplate;
        this.nbpUrl = nbpUrl;
        this.scheduler = Executors.newScheduledThreadPool(1);

        // Aktualizacja kursów przy uruchomieniu programu
        updateExchangeRates();

        // Rozpocznij regularne aktualizacje kursów
        scheduleUpdate();
    }

    private void updateExchangeRates() {
        try {

            log.info("Updating exchange rates");

            ResponseEntity<String> response = restTemplate.getForEntity(nbpUrl, String.class);
            ObjectMapper objectMapper = new ObjectMapper();

            String jsonString = response.getBody().replaceAll("^\\[|\\]$", "");
            ExchangeRateTable exchangeRateTable = objectMapper.readValue(jsonString, ExchangeRateTable.class);

            exchangeRates.clear();
            exchangeRateTable.getRates().forEach(entry -> {
                ExchangeRate currency = createExchangeRate(entry);
                exchangeRates.put(currency.getCode(), currency);
            });

        } catch (IOException e) {
            throw new RuntimeException("Failed to update exchange rates", e);
        }
    }

    private ExchangeRate createExchangeRate(ExchangeRate currency) {
        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setCode(currency.getCode());
        exchangeRate.setCurrency(currency.getCode());
        exchangeRate.setMid(currency.getMid());
        return exchangeRate;
    }

    public ExchangeRate getLatestExchangeRate(String currencyCode) {
        return exchangeRates.getOrDefault(currencyCode, null);
    }

    private void scheduleUpdate() {
        scheduler.scheduleAtFixedRate(this::updateExchangeRates, 0, 24, TimeUnit.HOURS);
    }

}
