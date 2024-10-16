package com.example.bookstore.service;

import com.example.bookstore.exception.CurrencyFetchException;
import com.example.bookstore.model.CurrencyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "FeignFixerService", url = "${fixer.api.url}")
public interface FixerApiAdapter {

    @GetMapping
    ResponseEntity<CurrencyResponse> fetchCurrencyRates() throws CurrencyFetchException;
}
