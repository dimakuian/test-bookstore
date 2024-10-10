package com.example.bookstore.model.mapper;

import com.example.bookstore.model.BookResponse;
import com.example.bookstore.model.entity.Author;
import com.example.bookstore.model.entity.Book;
import com.example.bookstore.utils.CurrencyConverter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Mapper class for converting Book entity to BookResponse DTO.
 */
public class BookMapper {

    /**
     * Converts a Book entity to a BookResponse DTO.
     *
     * @param book          The book entity.
     * @param currencyRates The map of currency rates.
     * @return A BookResponse DTO with prices in multiple currencies.
     */
    public static BookResponse toBookResponse(Book book, Map<String, Double> currencyRates) {
        Map<String, Double> prices = new HashMap<>();
        prices.put("EUR", book.getPrice()); // Assuming EUR is the base currency

        // Convert price to other currencies
        convertAndPutPrice(prices, book.getPrice(), "USD", currencyRates);
        convertAndPutPrice(prices, book.getPrice(), "UAH", currencyRates);

        Author author = book.getAuthor();
        String fullName = author != null ? String.join(" ", author.getFirstName(), author.getLastName()) : "Unknown Author";

        return new BookResponse(book.getTitle(), fullName, prices);
    }

    /**
     * Converts a list of Book entities to a list of BookResponse DTOs.
     *
     * @param books         The list of book entities.
     * @param currencyRates The map of currency rates.
     * @return A list of BookResponse DTOs.
     */
    public static List<BookResponse> toBookResponseList(List<Book> books, Map<String, Double> currencyRates) {
        return books.stream()
                .map(book -> toBookResponse(book, currencyRates))
                .collect(Collectors.toList());
    }


    /**
     * Helper method to convert price and put it in the prices map if the target currency rate is available.
     *
     * @param prices   The map where the converted prices are put.
     * @param price    The price in the base currency.
     * @param currency The target currency.
     * @param rates    The map of currency rates.
     */
    private static void convertAndPutPrice(Map<String, Double> prices, double price, String currency, Map<String, Double> rates) {
        Optional<Double> convertedPrice = Optional.ofNullable(CurrencyConverter.convertPrice(price, currency, rates));
        convertedPrice.ifPresent(aDouble -> prices.put(currency, aDouble));
    }
}
