package com.example.bookstore.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Map;

/**
 * Represents the response structure for currency exchange rate data retrieved from an external API.
 * This class is used to deserialize JSON responses that provide details about current currency exchange rates.
 * It is annotated to ignore unknown properties to ensure flexibility and resilience in case the API response changes.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrencyResponse {

    private boolean success;
    private long timestamp;
    private String base;
    private String date;
    private Map<String, Double> rates;
}
