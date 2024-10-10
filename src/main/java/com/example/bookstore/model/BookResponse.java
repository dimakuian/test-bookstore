package com.example.bookstore.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

/**
 * Represents the response data structure for book information.
 * This class is used to encapsulate details about a book, including its title, author, and pricing information.
 * It is designed to deserialize JSON data where specific properties are mapped to class fields using {@link JsonProperty} annotations.
 * This class is constructed with all necessary information at the time of creation, making it immutable thereafter.
 */
@Data
@AllArgsConstructor
public class BookResponse {

    @JsonProperty("title")
    private String title;

    @JsonProperty("author")
    private String author;

    @JsonProperty("price")
    private Map<String, Double> price;
}
