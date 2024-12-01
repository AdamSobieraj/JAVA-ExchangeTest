package com.example.javacurrency.common;

import com.example.javacurrency.account.UserAccount;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AccountRepository {

    private final Map<UUID, UserAccount> accounts = new HashMap<>();

    public void addAccount(UserAccount account) {
        accounts.put(account.getUuid(), account);
    }

    public List<UserAccount> getAllAccounts() {
        return new ArrayList<>(accounts.values());
    }

    public UserAccount getAccountById(UUID id) {
        if (accounts.isEmpty()) {
            return null;
        } else {
            return accounts.values().stream()
                    .filter(acc -> acc.getUuid().equals(id))
                    .findFirst()
                    .orElse(null);
        }
    }

    public boolean containsUsername(String firstName, String lastName) {
        return accounts.values().stream().anyMatch(acc ->
                acc.getFirstName().equals(firstName) && acc.getLastName().equals(lastName));
    }

    public UserAccount getAccountByFirstNameAndLastName(String firstName, String lastName) throws IllegalArgumentException {
        if (accounts.isEmpty()) {
            return null;
        } else {
            return accounts.values().stream()
                    .filter(acc -> acc.getFirstName().equalsIgnoreCase(firstName) &&
                            acc.getLastName().equalsIgnoreCase(lastName))
                    .findFirst()
                    .orElse(null);
        }
    }

    public void removeAccountByFirstNameAndLastName(String firstName, String lastName) {
        accounts.values().removeIf(acc ->
                acc.getFirstName().equalsIgnoreCase(firstName) &&
                        acc.getLastName().equalsIgnoreCase(lastName)
        );
    }

    public UserAccount updateAccount(UserAccount updatedAccount) {
        UserAccount existingAccount = accounts.values().stream()
                .filter(acc -> acc.getUuid().equals(updatedAccount.getUuid()))
                .findFirst()
                .orElse(null);

        if (existingAccount != null) {

            existingAccount.setEmail(updatedAccount.getEmail());
            existingAccount.setFirstName(updatedAccount.getFirstName());
            existingAccount.setLastName(updatedAccount.getLastName());
            existingAccount.setBalance(updatedAccount.getBalance());
            existingAccount.setCurrency(updatedAccount.getCurrency());

            accounts.put(existingAccount.getUuid(), existingAccount);
            return existingAccount;
        } else {
            return null;
        }
    }

}
