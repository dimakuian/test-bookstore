package com.example.bookstore.service;

import com.example.bookstore.exception.CurrencyFetchException;
import com.example.bookstore.model.CurrencyResponse;

/**
 * Interface for fetching currency exchange rates from an external API.
 */
public interface CurrencyRateService {

    /**
     * Retrieves the latest currency exchange rates.
     *
     * @return a {@link CurrencyResponse} containing the rates.
     * @throws CurrencyFetchException if the rates cannot be fetched or parsed.
     */
    CurrencyResponse fetchCurrencyRates() throws CurrencyFetchException;
}