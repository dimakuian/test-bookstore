package com.example.bookstore.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.*;

import com.example.bookstore.exception.ResourceNotFoundException;
import com.example.bookstore.model.BookResponse;
import com.example.bookstore.model.entity.Author;
import com.example.bookstore.model.entity.Book;
import com.example.bookstore.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    private Book book;
    private BookResponse bookResponse;
    private String jwtToken;

    @BeforeEach
    void setUp() throws Exception {
        book = new Book(1, "Effective Java", new Author(1, "Joshua", "Bloch"), 45.0);
        bookResponse = new BookResponse("Effective Java", "Joshua Bloch", new HashMap<>());

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"username\": \"john_doe\", \"password\": \"password123\" }"))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        jwtToken = response.substring(16, response.length() - 2);
    }

    @Test
    void getAllBooks() throws Exception {
        List<BookResponse> allBooks = Arrays.asList(bookResponse);
        given(bookService.findAllBooks()).willReturn(allBooks);

        mockMvc.perform(get("/api/v1/books").header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Effective Java"));
    }

    @Test
    void getBookById() throws Exception {
        given(bookService.findBookById(1)).willReturn(bookResponse);

        mockMvc.perform(get("/api/v1/books/{id}", 1)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Effective Java"));
    }

    @Test
    void createBook() throws Exception {
        given(bookService.saveBook(any(Book.class))).willReturn(book);

        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Effective Java"));
    }

    @Test
    void deleteBook() throws Exception {
        doNothing().when(bookService).deleteBook(1);

        mockMvc.perform(delete("/api/v1/books/{id}", 1)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken))
                .andExpect(status().isOk());
    }

    @Test
    void updateBook() throws Exception {
        given(bookService.updateBook(eq(1), any(Book.class))).willReturn(book);

        mockMvc.perform(put("/api/v1/books/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Effective Java"));
    }

    @Test
    void getBookByIdNotFound() throws Exception {
        given(bookService.findBookById(1)).willThrow(ResourceNotFoundException.class);

        mockMvc.perform(get("/api/v1/books/{id}", 1)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken))
                .andExpect(status().isNotFound());
    }
}