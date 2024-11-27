package com.example.javacurrency.exchange;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class ExchangeRateServiceTest {

    @InjectMocks
    private ExchangeRateService exchangeRateService;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @SneakyThrows
    void getLatestExchangeRate() {
        // Given
        when(restTemplate.getForObject(nullable(String.class), any())).thenReturn(jsonRes());

        // When
        ExchangeRate result = exchangeRateService.getLatestExchangeRate();

        // Then
        assertNotNull(result);
        assertEquals("USD", result.getCode());
        assertEquals(4.1073, result.getMid());
    }

    @Test
    void getLatestExchangeRateUSDNotFound() {
        // Given
        when(restTemplate.getForObject(anyString(), any())).thenReturn(jsonResNoUS());

        // When
        // Then
        assertThrows(RuntimeException.class, () -> exchangeRateService.getLatestExchangeRate());
    }

    private ExchangeRate createExchangeRate() {
        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setCode("USD");
        exchangeRate.setCurrency("US Dollar");
        exchangeRate.setMid(Double.parseDouble(("1.23")));
        return exchangeRate;
    }

    private String jsonRes() {
        return "[{\n" +
                "  \"table\": \"A\",\n" +
                "  \"no\": \"229/A/NBP/2024\",\n" +
                "  \"effectiveDate\": \"2024-11-26\",\n" +
                "  \"rates\": [\n" +
                "    {\n" +
                "      \"currency\": \"bat (Tajlandia)\",\n" +
                "      \"code\": \"THB\",\n" +
                "      \"mid\": 0.1184\n" +
                "    },\n" +
                "    {\n" +
                "      \"currency\": \"dolar amerykański\",\n" +
                "      \"code\": \"USD\",\n" +
                "      \"mid\": 4.1073\n" +
                "    },\n" +
                "    {\n" +
                "      \"currency\": \"dolar australijski\",\n" +
                "      \"code\": \"AUD\",\n" +
                "      \"mid\": 2.6637\n" +
                "    },\n" +
                "    {\n" +
                "      \"currency\": \"dolar Hongkongu\",\n" +
                "      \"code\": \"HKD\",\n" +
                "      \"mid\": 0.5278\n" +
                "    },\n" +
                "    {\n" +
                "      \"currency\": \"dolar kanadyjski\",\n" +
                "      \"code\": \"CAD\",\n" +
                "      \"mid\": 2.914\n" +
                "    },\n" +
                "    {\n" +
                "      \"currency\": \"dolar nowozelandzki\",\n" +
                "      \"code\": \"NZD\",\n" +
                "      \"mid\": 2.3998\n" +
                "    },\n" +
                "    {\n" +
                "      \"currency\": \"dolar singapurski\",\n" +
                "      \"code\": \"SGD\",\n" +
                "      \"mid\": 3.0478\n" +
                "    },\n" +
                "    {\n" +
                "      \"currency\": \"euro\",\n" +
                "      \"code\": \"EUR\",\n" +
                "      \"mid\": 4.3157\n" +
                "    },\n" +
                "    {\n" +
                "      \"currency\": \"lew (Bułgaria)\",\n" +
                "      \"code\": \"BGN\",\n" +
                "      \"mid\": 2.2066\n" +
                "    },\n" +
                "    {\n" +
                "      \"currency\": \"lira turecka\",\n" +
                "      \"code\": \"TRY\",\n" +
                "      \"mid\": 0.1187\n" +
                "    },\n" +
                "    {\n" +
                "      \"currency\": \"nowy izraelski szekel\",\n" +
                "      \"code\": \"ILS\",\n" +
                "      \"mid\": 1.1249\n" +
                "    },\n" +
                "    {\n" +
                "      \"currency\": \"peso chilijskie\",\n" +
                "      \"code\": \"CLP\",\n" +
                "      \"mid\": 0.00421\n" +
                "    },\n" +
                "    {\n" +
                "      \"currency\": \"peso filipińskie\",\n" +
                "      \"code\": \"PHP\",\n" +
                "      \"mid\": 0.0696\n" +
                "    },\n" +
                "    {\n" +
                "      \"currency\": \"peso meksykańskie\",\n" +
                "      \"code\": \"MXN\",\n" +
                "      \"mid\": 0.2002\n" +
                "    },\n" +
                "    {\n" +
                "      \"currency\": \"rand (Republika Południowej Afryki)\",\n" +
                "      \"code\": \"ZAR\",\n" +
                "      \"mid\": 0.227\n" +
                "    },\n" +
                "    {\n" +
                "      \"currency\": \"real (Brazylia)\",\n" +
                "      \"code\": \"BRL\",\n" +
                "      \"mid\": 0.7083\n" +
                "    },\n" +
                "    {\n" +
                "      \"currency\": \"yuan renminbi (Chiny)\",\n" +
                "      \"code\": \"CNY\",\n" +
                "      \"mid\": 0.5662\n" +
                "    },\n" +
                "    {\n" +
                "      \"currency\": \"SDR (MFW)\",\n" +
                "      \"code\": \"XDR\",\n" +
                "      \"mid\": 5.3877\n" +
                "    }\n" +
                "  ]\n" +
                "}]";
    }


    private String jsonResNoUS() {
        return "{\n" +
                "  \"rates\": {\n" +
                "    \"THB\": {\n" +
                "      \"currency\": \"bat (Tajlandia)\",\n" +
                "      \"code\": \"THB\",\n" +
                "      \"mid\": 0.1184\n" +
                "    },\n" +
                "    \"AUD\": {\n" +
                "      \"currency\": \"dolar australijski\",\n" +
                "      \"code\": \"AUD\",\n" +
                "      \"mid\": 2.6637\n" +
                "    },\n" +
                "    \"HKD\": {\n" +
                "      \"currency\": \"dolar Hongkongu\",\n" +
                "      \"code\": \"HKD\",\n" +
                "      \"mid\": 0.5278\n" +
                "    },\n" +
                "    \"CAD\": {\n" +
                "      \"currency\": \"dolar kanadyjski\",\n" +
                "      \"code\": \"CAD\",\n" +
                "      \"mid\": 2.9140\n" +
                "    },\n" +
                "    \"NZD\": {\n" +
                "      \"currency\": \"dolar nowozelandzki\",\n" +
                "      \"code\": \"NZD\",\n" +
                "      \"mid\": 2.3998\n" +
                "    },\n" +
                "    \"SGD\": {\n" +
                "      \"currency\": \"dolar singapurski\",\n" +
                "      \"code\": \"SGD\",\n" +
                "      \"mid\": 3.0478\n" +
                "    }\n" +
                "  }\n" +
                "}";

    }


}