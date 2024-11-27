package com.example.javacurrency.account;

import com.example.javacurrency.common.Currency;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    private static final String BASE_URL = "/api/v1/accounts";

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private UserAccount mockUserAccount;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();

        mockUserAccount = new UserAccount();
        mockUserAccount.setUuid(UUID.randomUUID());
        mockUserAccount.setEmail("test@example.com");
        mockUserAccount.setFirstName("John");
        mockUserAccount.setLastName("Doe");
        mockUserAccount.setBalance(BigDecimal.valueOf(100));
        mockUserAccount.setCurrency(Currency.PLN);

    }

    @Test
    @SneakyThrows
    void testCreateAccount() {

        when(accountService.createAccount(any(UserAccount.class))).thenReturn(mockUserAccount);

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockUserAccount)))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void testGetAccountDetails() {

        when(accountService.getAccountDetails(any(), any())).thenReturn(mockUserAccount);

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/getaccount/test/John"))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void testGetAccountDetailsByUUID() {

        when(accountService.getAccountDetailsUUID(any())).thenReturn(mockUserAccount);

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/getaccountuuid/" + mockUserAccount.getUuid()))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void testUpdateAccount() {

        when(accountService.updateAccount(any(UserAccount.class))).thenReturn(mockUserAccount);

        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockUserAccount)))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void testExchange() {
        // Given
        when(accountService.exchangeCurrency(any())).thenReturn(mockUserAccount);

        // When
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/exchange/" + mockUserAccount.getUuid())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        UserAccount actualResult = objectMapper.readValue(responseContent, UserAccount.class);

        // Then
        assertNotNull(actualResult);
    }
}