package com.example.bookstore.service;

import com.example.bookstore.exception.CurrencyFetchException;
import com.example.bookstore.model.CurrencyResponse;

public interface FeignFixerApiAdapter extends CurrencyRateService {


    @Override
    CurrencyResponse fetchCurrencyRates() throws CurrencyFetchException;
}
