package com.example.javacurrency.exchange;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Validated
@RestController
@RequestMapping("/api/v1/exchange")
@RequiredArgsConstructor
public class ExchangeController {

    private final CurrencyExchangeService currencyExchangeService;

    @PostMapping("/pln-to-usd")
    public ResponseEntity<ExchangeResult> exchangePlnToUsd(@Valid @RequestBody ExchangeRequest request) throws IOException {
        ExchangeResult result = currencyExchangeService.exchangePlnToUsd(request.getAmount());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/usd-to-pln")
    public ResponseEntity<ExchangeResult> exchangeUsdToPln(@Valid @RequestBody ExchangeRequest request) throws IOException {
        ExchangeResult result = currencyExchangeService.exchangeUsdToPln(request.getAmount());
        return ResponseEntity.ok(result);
    }

}
