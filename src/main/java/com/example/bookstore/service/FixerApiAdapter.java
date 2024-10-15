package com.example.bookstore.service;

import com.example.bookstore.exception.CurrencyFetchException;
import com.example.bookstore.model.CurrencyResponse;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "FeignFixerService", url = "${fixer.api.url}")
public interface FixerApiAdapter extends CurrencyRateService {

    @GetMapping
    @Cacheable(value = "currencyResponseCache", unless = "#result == null")
    @Override
    CurrencyResponse fetchCurrencyRates() throws CurrencyFetchException;
}
