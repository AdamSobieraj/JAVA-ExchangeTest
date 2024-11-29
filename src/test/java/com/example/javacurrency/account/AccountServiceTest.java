package com.example.javacurrency.account;

import com.example.javacurrency.common.AccountRepository;
import com.example.javacurrency.common.Currency;
import com.example.javacurrency.exchange.CurrencyExchangeService;
import com.example.javacurrency.exchange.ExchangeResult;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    private UserAccount mockUserAccount;

    @Mock
    private CurrencyExchangeService currencyExchangeService;
    private ExchangeResult exchangeResult;

    @BeforeEach
    void setUp() {
        mockUserAccount = new UserAccount();
        mockUserAccount.setUuid(UUID.randomUUID());
        mockUserAccount.setEmail("test@example.com");
        mockUserAccount.setFirstName("John");
        mockUserAccount.setLastName("Doe");
        mockUserAccount.setCurrency(Currency.USD);

        exchangeResult = new ExchangeResult();

        exchangeResult.setFromCurrency(Currency.USD.getName());
        exchangeResult.setToCurrency(Currency.PLN.getName());
        exchangeResult.setAmount(new BigDecimal("100"));
        exchangeResult.setResultAmount(new BigDecimal("87.32"));
        exchangeResult.setExchangeRate(new BigDecimal("0.8732"));
    }

    @Test
    void testCreateAccountSuccess() {
        // Given
        // When
        UserAccount createdAccount = accountService.createAccount(mockUserAccount);

        // Then
        verify(accountRepository).containsUsername("John", "Doe");
        verify(accountRepository).addAccount(argThat(account ->
                account.getFirstName().equals("John") && account.getLastName().equals("Doe")));

        assertNotNull(createdAccount);
        assertEquals(mockUserAccount.getFirstName(), createdAccount.getFirstName());
        assertEquals(mockUserAccount.getLastName(), createdAccount.getLastName());
        assertNotNull(createdAccount.getUuid());
    }

    @Test
    void testCreateAccountException() {
        // Given
        // When
        doThrow(new IllegalArgumentException()).when(accountRepository).containsUsername(anyString(), anyString());

        // Then
        assertThrows(IllegalArgumentException.class, () ->
                accountService.createAccount(mockUserAccount));
        verify(accountRepository, never()).addAccount(any(UserAccount.class));
    }

    @Test
    @SneakyThrows
    void testGetAccountDetails() {
        // Given
        String firstName = "John";
        String lastName = "Doe";

        when(accountRepository.getAccountByFirstNameAndLastName(firstName, lastName)).thenReturn(mockUserAccount);

        // When
        UserAccount result = accountService.getAccountDetails(firstName, lastName);

        // Assert
        verify(accountRepository).getAccountByFirstNameAndLastName(firstName, lastName);
        assertEquals(mockUserAccount, result);
    }

    @Test
    @SneakyThrows
    void testGetAccountDetailsUUID() {
        // Given
        when(accountRepository.getAccountById(any())).thenReturn(mockUserAccount);

        // When
        UserAccount result = accountService.getAccountDetailsUUID(String.valueOf(mockUserAccount.getUuid()));

        // Assert
        verify(accountRepository).getAccountById(mockUserAccount.getUuid());
        assertEquals(mockUserAccount, result);
    }


    @Test
    void testDeleteAccount() {
        // Given
        String firstName = "John";
        String lastName = "Doe";

        // When
        accountService.deleteAccount(firstName, lastName);

        //Then
        verify(accountRepository).removeAccountByFirstNameAndLastName(firstName, lastName);
    }

    @Test
    @SneakyThrows
    void testUpdateAccountSuccess() {
        // Given
        UUID id = UUID.randomUUID();
        mockUserAccount.setUuid(id);

        when(accountRepository.updateAccount(any())).thenReturn(mockUserAccount);

        // When
        UserAccount result = accountService.updateAccount(mockUserAccount);

        // Assert
        verify(accountRepository).updateAccount(mockUserAccount);
        assertNotNull(result);
        assertEquals(id, result.getUuid());
    }

    @Test
    void testUpdateAccountAccountNotFound() {
        // Given
        UUID id = UUID.randomUUID();
        mockUserAccount.setUuid(id);

        // When
        when(accountRepository.updateAccount(mockUserAccount)).thenThrow(new IllegalArgumentException());

        // Then
        assertThrows(IllegalArgumentException.class, () ->
                accountService.updateAccount(mockUserAccount));

        verify(accountRepository).updateAccount(mockUserAccount);
    }

    @Test
    @SneakyThrows
    void testexchangeCurrency() {
        // Given
        UUID id = UUID.randomUUID();
        mockUserAccount.setUuid(id);

        when(accountRepository.getAccountById(any())).thenReturn(mockUserAccount);
        when(currencyExchangeService.exchange(any())).thenReturn(exchangeResult);

        // Then
        UserAccount result = accountService.exchangeCurrency(mockUserAccount.getUuid().toString());

        verify(accountRepository).updateAccount(mockUserAccount);
        assertEquals(BigDecimal.valueOf(87.32), result.getBalance());
    }
}