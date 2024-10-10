package com.example.bookstore.utils;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CurrencyConverterTest {

    @Test
    public void testConvertPrice_ValidRate() {
        // Setup
        double price = 100.0;
        String targetCurrency = "EUR";
        Map<String, Double> rates = new HashMap<>();
        rates.put("EUR", 1.1);

        // Execute
        Double convertedPrice = CurrencyConverter.convertPrice(price, targetCurrency, rates);

        // Verify
        Double expectedPrice = BigDecimal.valueOf(price * rates.get(targetCurrency))
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
        assertEquals(expectedPrice, convertedPrice, "The converted price should match the expected value.");
    }

    @Test
    public void testConvertPrice_InvalidCurrency() {
        // Setup
        double price = 100.0;
        String targetCurrency = "JPY";
        Map<String, Double> rates = new HashMap<>();
        rates.put("EUR", 0.85);

        // Execute
        Double convertedPrice = CurrencyConverter.convertPrice(price, targetCurrency, rates);

        // Verify
        assertNull(convertedPrice, "The converted price should be null for an invalid currency.");
    }

    @Test
    public void testConvertPrice_NullRates() {
        // Setup
        double price = 100.0;
        String targetCurrency = "EUR";

        // Execute
        Double convertedPrice = CurrencyConverter.convertPrice(price, targetCurrency, null);

        // Verify
        assertNull(convertedPrice, "The converted price should be null when rates map is null.");
    }
}