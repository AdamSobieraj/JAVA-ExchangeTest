package com.example.javacurrency.account;

import com.example.javacurrency.common.AccountRepository;
import com.example.javacurrency.common.Currency;
import com.example.javacurrency.exchange.CurrencyExchangeService;
import com.example.javacurrency.exchange.ExchangeRequest;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
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

    public UserAccount getAccountDetails(String firstName, String lastName) throws IOException {
        return accountRepository.getAccountByFirstNameAndLastName(firstName, lastName);
    }

    public UserAccount getAccountDetailsUUID(String uuid) throws IOException {
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

    public UserAccount exchangeCurrency(String uuid) throws IOException {
        UserAccount account = accountRepository.getAccountById(UUID.fromString(uuid));

        ExchangeRequest exchangeRequest = new ExchangeRequest();
        exchangeRequest.setCurrency(account.getCurrency());
        exchangeRequest.setAmount(account.getBalance());

        account.setBalance(currencyExchangeService.exchange(exchangeRequest).getResultAmount());
        account.setCurrency(account.getCurrency());


        updateAccount(account);

        return account;
    }

}
