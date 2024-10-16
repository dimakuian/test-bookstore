package com.example.bookstore.service;

import com.example.bookstore.exception.CurrencyFetchException;
import com.example.bookstore.model.CurrencyResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Service class for fetching currency rates using the Feign client.
 * Handles currency data retrieval and caching.
 */
@Slf4j
@Service
public class CurrencyRatesService {

    private final FixerApiAdapter fixerApiAdapter;

    @Autowired
    public CurrencyRatesService(FixerApiAdapter fixerApiAdapter) {
        this.fixerApiAdapter = fixerApiAdapter;
    }

    /**
     * Retrieves the latest currency rates by calling the external Fixer API via Feign client.
     * The result is cached to optimize subsequent calls.
     * If the fetch is successful, the currency rates are returned; otherwise, a CurrencyFetchException is thrown.
     *
     * <p>
     * This method is annotated with {@code @Cacheable}, which caches the result in the "currencyResponseCache"
     * unless the result is null. Cached results will be used in subsequent calls instead of fetching from the API again.
     * </p>
     *
     * @return CurrencyResponse object containing the latest currency rates.
     * @throws CurrencyFetchException if there is an error fetching the currency rates.
     */
    @Cacheable(value = "currencyResponseCache", unless = "#result == null")
    public CurrencyResponse getCurrencyRates() throws CurrencyFetchException {
        log.info("Attempting to fetch currency rates from Fixer API.");
        ResponseEntity<CurrencyResponse> responseEntity = fixerApiAdapter.fetchCurrencyRates();

        if (responseEntity != null && responseEntity.getBody() != null) {
            log.info("Successfully fetched currency rates.");
            return responseEntity.getBody();
        } else {
            log.error("Currency rates fetch failed: empty or null response.");
            throw new CurrencyFetchException("Failed to fetch currency rates.");
        }
    }
}
