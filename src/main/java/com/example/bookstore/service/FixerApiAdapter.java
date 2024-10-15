package com.example.bookstore.service;

import com.example.bookstore.exception.CurrencyFetchException;
import com.example.bookstore.model.CurrencyResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Objects;

@Slf4j
@Service
public class FixerApiAdapter implements CurrencyRateService {

    // URL for the Fixer API, injected from application properties.
    @Value("${fixer.api.url}")
    private String apiUrl;

    private final RestClient restClient;

    public FixerApiAdapter() {
        this.restClient = RestClient.create();
    }

    /**
     * Fetches the latest currency exchange rates from the Fixer API.
     *
     * @return a {@link CurrencyResponse} object containing the fetched currency rates.
     * @throws CurrencyFetchException if there is an issue fetching the currency rates.
     */
    @Override
    @Cacheable(value = "currencyResponseCache", unless = "#result == null")
    public CurrencyResponse fetchCurrencyRates() {
        return restClient.get()
                .uri(apiUrl)
                .exchange((request, response) -> {
                    if (response.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(200))) {
                        log.info("Successfully fetched currency rates");
                        return Objects.requireNonNull(response.bodyTo(CurrencyResponse.class));
                    } else {
                        log.error("Failed to fetch currency rates: {}", response.bodyTo(String.class));
                        throw new CurrencyFetchException("Failed to fetch currency rates");
                    }
                });
    }
}
