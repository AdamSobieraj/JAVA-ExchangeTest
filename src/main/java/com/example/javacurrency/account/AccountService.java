package com.example.javacurrency.account;

import com.example.javacurrency.common.AccountRepository;
import com.example.javacurrency.exchange.CurrencyExchangeService;
import com.example.javacurrency.exchange.ExchangeRequest;
import lombok.RequiredArgsConstructor;
import java.util.UUID;

@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final CurrencyExchangeService currencyExchangeService;

    public UserAccount createAccount(UserAccount account) {
        if (accountRepository.containsUsername(account.getFirstName(), account.getLastName())) {
            throw new IllegalArgumentException("Username already exists");
        }

        UUID id = generateUniqueId();
        account.setUuid(id);
        accountRepository.addAccount(account);
        return account;
    }

    public UserAccount getAccountDetails(String firstName, String lastName) {
        return accountRepository.getAccountByFirstNameAndLastName(firstName, lastName);
    }

    public UserAccount getAccountDetailsUUID(String uuid) {
        return accountRepository.getAccountById(UUID.fromString(uuid));
    }

    public void deleteAccount(String firstName, String lastName) {
        accountRepository.removeAccountByFirstNameAndLastName(firstName, lastName);
    }

    public UserAccount updateAccount(UserAccount updatedAccount) {
        return accountRepository.updateAccount(updatedAccount);
    }

    private UUID generateUniqueId() {
        return UUID.randomUUID();
    }

    public UserAccount exchangeCurrency(AccExchangeCurrencyReq accExchangeCurrencyReq) {
        UserAccount account = accountRepository.getAccountById(accExchangeCurrencyReq.getUuid());

        if ( account != null && !account.getCurrency().equals(accExchangeCurrencyReq.getCurrency())) {

            ExchangeRequest exchangeRequest = new ExchangeRequest();
            exchangeRequest.setCurrency(accExchangeCurrencyReq.getCurrency());
            exchangeRequest.setAmount(account.getBalance());

            account.setBalance(currencyExchangeService.exchange(exchangeRequest).getResultAmount());
            account.setCurrency(accExchangeCurrencyReq.getCurrency());

            updateAccount(account);
        }

        return account;
    }

}
