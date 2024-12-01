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

    public static final String FIRST_NAME = "John";
    public static final String LAST_NAME = "Doe";

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    private UserAccount mockUserAccount;

    @Mock
    private CurrencyExchangeService currencyExchangeService;
    private ExchangeResult exchangeResult;
    private AccExchangeCurrencyReq accExchangeCurrencyReq;

    @BeforeEach
    void setUp() {
        mockUserAccount = new UserAccount();
        mockUserAccount.setUuid(UUID.randomUUID());
        mockUserAccount.setEmail("test@example.com");
        mockUserAccount.setFirstName(FIRST_NAME);
        mockUserAccount.setLastName(LAST_NAME);
        mockUserAccount.setCurrency(Currency.USD);
        mockUserAccount.setBalance(BigDecimal.valueOf(100));

        exchangeResult = new ExchangeResult();
        exchangeResult.setFromCurrency(Currency.USD.getName());
        exchangeResult.setToCurrency(Currency.PLN.getName());
        exchangeResult.setAmount(new BigDecimal("100"));
        exchangeResult.setResultAmount(new BigDecimal("87.32"));
        exchangeResult.setExchangeRate(new BigDecimal("0.8732"));

        accExchangeCurrencyReq = new AccExchangeCurrencyReq();
        accExchangeCurrencyReq.setCurrency(Currency.USD);
        accExchangeCurrencyReq.setUuid(UUID.randomUUID());
    }

    @Test
    void testCreateAccountSuccess() {
        // Given
        // When
        UserAccount createdAccount = accountService.createAccount(mockUserAccount);

        // Then
        verify(accountRepository).containsUsername(FIRST_NAME, LAST_NAME);
        verify(accountRepository).addAccount(argThat(account ->
                account.getFirstName().equals(FIRST_NAME) && account.getLastName().equals("Doe")));

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
        when(accountRepository.getAccountByFirstNameAndLastName(FIRST_NAME, LAST_NAME)).thenReturn(mockUserAccount);

        // When
        UserAccount result = accountService.getAccountDetails(FIRST_NAME, LAST_NAME);

        // Assert
        verify(accountRepository).getAccountByFirstNameAndLastName(FIRST_NAME, LAST_NAME);
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
        // When
        accountService.deleteAccount(FIRST_NAME, LAST_NAME);

        //Then
        verify(accountRepository).removeAccountByFirstNameAndLastName(FIRST_NAME, LAST_NAME);
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
    void testExchangeCurrency() {
        // Given
        accExchangeCurrencyReq.setCurrency(Currency.PLN);
        when(accountRepository.getAccountById(any())).thenReturn(mockUserAccount);
        when(currencyExchangeService.exchange(any())).thenReturn(exchangeResult);

        // Then
        UserAccount result = accountService.exchangeCurrency(accExchangeCurrencyReq);

        //Then
        verify(accountRepository).updateAccount(mockUserAccount);
        assertEquals(BigDecimal.valueOf(87.32), result.getBalance());
    }

    @Test
    @SneakyThrows
    void testExchangeCurrencyNoUpdate() {
        // Given
        when(accountRepository.getAccountById(any())).thenReturn(mockUserAccount);

        // Then
        UserAccount result = accountService.exchangeCurrency(accExchangeCurrencyReq);

        // Then
        assertEquals(BigDecimal.valueOf(100), result.getBalance());
        verifyNoMoreInteractions(accountRepository);
    }
}