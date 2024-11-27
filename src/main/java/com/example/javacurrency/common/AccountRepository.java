package com.example.javacurrency.common;

import com.example.javacurrency.account.UserAccount;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class AccountRepository {

    private final Set<UserAccount> accounts = new HashSet<>();

    public void addAccount(UserAccount account) {
        accounts.add(account);
    }

    public List<UserAccount> getAllAccounts() {
        return new ArrayList<>(accounts);
    }

    public UserAccount getAccountById(UUID id) {
        return accounts.stream()
                .filter(acc -> acc.getUuid().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
    }

    public boolean containsUsername(String firstName, String lastName) {
        return accounts.stream().anyMatch(acc ->
                acc.getFirstName().equals(firstName) && acc.getLastName().equals(lastName));
    }

    public UserAccount getAccountByFirstNameAndLastName(String firstName, String lastName) {
        return accounts.stream()
                .filter(acc -> acc.getFirstName().equalsIgnoreCase(firstName) &&
                        acc.getLastName().equalsIgnoreCase(lastName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
    }

    public void removeAccountByFirstNameAndLastName(String firstName, String lastName) {
        accounts.removeIf(acc ->
                acc.getFirstName().equalsIgnoreCase(firstName) &&
                        acc.getLastName().equalsIgnoreCase(lastName)
        );
    }

    public UserAccount updateAccount(UserAccount updatedAccount) {
        UserAccount existingAccount = accounts.stream()
                .filter(acc -> acc.getUuid().equals(updatedAccount.getUuid()))
                .findFirst()
                .orElse(null);

        if (existingAccount != null) {

            existingAccount.setEmail(updatedAccount.getEmail());
            existingAccount.setFirstName(updatedAccount.getFirstName());
            existingAccount.setLastName(updatedAccount.getLastName());
            existingAccount.setBalance(updatedAccount.getBalance());
            existingAccount.setCurrency(updatedAccount.getCurrency());

            accounts.remove(existingAccount);
            accounts.add(existingAccount);
            return existingAccount;
        } else {
            throw new IllegalArgumentException("Account not found for UUID: " + updatedAccount.getUuid().toString());
        }
    }

}
