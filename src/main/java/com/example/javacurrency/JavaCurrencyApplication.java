package com.example.javacurrency;

import com.example.javacurrency.account.AccountController;
import com.example.javacurrency.account.AccountService;
import com.example.javacurrency.common.AccountRepository;
import com.example.javacurrency.exchange.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class JavaCurrencyApplication {

    @Value("${app.nbp.url}")
    private String nbpUrl;

    public static void main(String[] args) {
        SpringApplication.run(JavaCurrencyApplication.class, args);
    }

    @Bean
    public AccountController accountController() {
        return new AccountController(new AccountService(accountRepository(), currencyExchangeService()));
    }

    @Bean
    public AccountRepository accountRepository() {
        return new AccountRepository();
    }

    @Bean
    public AccountService accountService() {
        return new AccountService(accountRepository(), currencyExchangeService());
    }

    @Bean
    public ExchangeController exchangeController() {
        return new ExchangeController(currencyExchangeService());
    }

    @Bean
    public CurrencyExchangeService currencyExchangeService() {
        return new CurrencyExchangeService(exchangeRateService());
    }

    @Bean
    public ExchangeRateService exchangeRateService() {
        return new ExchangeRateService(restTemplate(), nbpUrl);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public PlnToUsdStrategy plnToUsdStrategy() {
        return new PlnToUsdStrategy(exchangeRateService());
    }

    @Bean
    public UsdToPlnStrategy usdToPlnStrategy() {
        return new UsdToPlnStrategy(exchangeRateService());
    }

}
