package com.example.bookstore.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

/**
 * Utility class for currency conversion.
 * This class provides methods to convert prices from a base currency to a target currency using given exchange rates.
 */
public class CurrencyConverter {

    /**
     * Converts a price from a base currency to a specified target currency using provided exchange rates.
     *
     * @param price The price in the base currency.
     * @param targetCurrency The currency code of the target currency.
     * @param rates A map containing currency codes as keys and their corresponding exchange rates as values.
     * @return The converted price rounded to two decimal places, or null if the target currency is not available in the rates map.
     */
    public static Double convertPrice(double price, String targetCurrency,  Map<String, Double> rates) {

        if (rates == null || !rates.containsKey(targetCurrency)) {
            return null; // Target currency not available
        }

        final Double rate = rates.get(targetCurrency);
        final BigDecimal bd = BigDecimal.valueOf(price * rate);

        return bd.setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
