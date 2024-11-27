package com.example.javacurrency.common;

import com.example.javacurrency.account.UserAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AccountRepositoryTest {

    private AccountRepository accountRepository;
    private UserAccount mockUserAccount;

    @BeforeEach
    void setUp() {
        mockUserAccount = new UserAccount();
        mockUserAccount.setUuid(UUID.randomUUID());
        mockUserAccount.setEmail("test@example.com");
        mockUserAccount.setFirstName("John");
        mockUserAccount.setLastName("Doe");

        accountRepository = new AccountRepository();
        accountRepository.addAccount(mockUserAccount);
    }

    @Test
    void testAddAccount() {
        // Given
        // When
        accountRepository.addAccount(mockUserAccount);
        // Then
        assertTrue(accountRepository.getAllAccounts().contains(mockUserAccount));
    }

    @Test
    void testGetAllAccounts() {
        // Given
        accountRepository.addAccount(mockUserAccount);

        // Act
        List<UserAccount> allAccounts = accountRepository.getAllAccounts();

        // Assert
        assertEquals(1, allAccounts.size());
        assertTrue(allAccounts.contains(mockUserAccount));
    }

    @Test
    void testGetAccountById() {
        // Given
        accountRepository.addAccount(mockUserAccount);

        // When
        UserAccount result = accountRepository.getAccountById(mockUserAccount.getUuid());

        //Then
        assertNotNull(result);
        assertEquals(mockUserAccount.getUuid(), result.getUuid());
    }

    @Test
    void testContainsUsername() {
        // Given
        accountRepository.addAccount(mockUserAccount);

        // When
        // Then
        assertTrue(accountRepository.containsUsername("John", "Doe"));
        assertFalse(accountRepository.containsUsername("Jane", "Smith"));
    }

    @Test
    void testGetAccountByFirstNameAndLastName() {
        // Given
        accountRepository.addAccount(mockUserAccount);

        // When
        UserAccount result = accountRepository.getAccountByFirstNameAndLastName("John", "DOE");

        // Then
        assertNotNull(result);
        assertEquals(mockUserAccount.getUuid(), result.getUuid());
    }

    @Test
    void testRemoveAccountByFirstNameAndLastName() {
        // Given
        accountRepository.addAccount(mockUserAccount);

        // When
        accountRepository.removeAccountByFirstNameAndLastName("John", "Doe");

        // When
        assertFalse(accountRepository.getAllAccounts().contains(mockUserAccount));
    }

    @Test
    void testUpdateAccount() {
        // Given

        UserAccount updatedAccount = new UserAccount();
        updatedAccount.setFirstName("Updated John");
        updatedAccount.setEmail("updated@example.com");
        updatedAccount.setUuid(UUID.randomUUID());
        updatedAccount.setBalance(BigDecimal.valueOf(100.00));
        updatedAccount.setCurrency(Currency.USD);

        accountRepository.addAccount(updatedAccount);
        accountRepository.addAccount(mockUserAccount);

        // When
        UserAccount result = accountRepository.updateAccount(updatedAccount);

        // When
        assertNotNull(result);
        assertEquals(updatedAccount.getUuid(), result.getUuid());
        assertEquals(updatedAccount.getEmail(), result.getEmail());
        assertEquals(updatedAccount.getFirstName(), result.getFirstName());
        assertEquals(updatedAccount.getLastName(), result.getLastName());
        assertEquals(updatedAccount.getBalance(), result.getBalance(), String.valueOf(0.01));
        assertEquals(updatedAccount.getCurrency(), result.getCurrency());
    }

}