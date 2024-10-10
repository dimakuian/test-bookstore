package com.example.bookstore.model.mapper;

import com.example.bookstore.model.BookResponse;
import com.example.bookstore.model.entity.Author;
import com.example.bookstore.model.entity.Book;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BookMapperTest {

    @Test
    void testToBookResponse() {
        // Arrange
        Author author = new Author(1,"John", "Doe");
        Book book = new Book(1,"Java Basics", author, 100.0);
        Map<String, Double> currencyRates = new HashMap<>();
        currencyRates.put("USD", 1.2);
        currencyRates.put("UAH", 36.0);

        // Act
        BookResponse response = BookMapper.toBookResponse(book, currencyRates);

        // Assert
        assertEquals("Java Basics", response.getTitle());
        assertEquals("John Doe", response.getAuthor());
        assertEquals(100.0, response.getPrice().get("EUR"));
        assertEquals(120.0, response.getPrice().get("USD"));
        assertEquals(3600.0, response.getPrice().get("UAH"));
    }

    @Test
    void testToBookResponseList() {
        // Arrange
        Author author = new Author(1,"John", "Doe");
        Book book1 = new Book(1,"Java Basics", author, 100.0);
        Book book2 = new Book(1,"Advanced Java", author, 150.0);
        List<Book> books = Arrays.asList(book1, book2);
        Map<String, Double> currencyRates = new HashMap<>();
        currencyRates.put("USD", 1.2);
        currencyRates.put("UAH", 36.0);

        // Act
        List<BookResponse> responses = BookMapper.toBookResponseList(books, currencyRates);

        // Assert
        assertEquals(2, responses.size());
        assertEquals("Java Basics", responses.get(0).getTitle());
        assertEquals(100.0, responses.get(0).getPrice().get("EUR"));
        assertEquals("Advanced Java", responses.get(1).getTitle());
        assertEquals(150.0, responses.get(1).getPrice().get("EUR"));
    }

}