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
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

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

    @BeforeEach
    void setUp() throws Exception {
        book = new Book(1, "Effective Java", new Author(1, "Joshua", "Bloch"), 45.0);
        bookResponse = new BookResponse("Effective Java", "Joshua Bloch", new HashMap<>());
        setAuthentication("ROLE_WRITER");
    }

    @Test
    void getAllBooks() throws Exception {
        Page<BookResponse> allBooks = new PageImpl<>(Collections.singletonList(bookResponse), PageRequest.of(0, 5, Sort.by("id")), 1);

        given(bookService.findAllBooks(Mockito.any())).willReturn(allBooks);

        mockMvc.perform(get("/api/v1/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.[0].title").value("Effective Java"));
    }

    @Test
    void getBookById() throws Exception {
        given(bookService.findBookById(1)).willReturn(bookResponse);

        mockMvc.perform(get("/api/v1/books/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Effective Java"));
    }

    @Test
    void createBook() throws Exception {
        given(bookService.saveBook(any(Book.class))).willReturn(book);

        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Effective Java"));
    }

    @Test
    void deleteBook() throws Exception {
        doNothing().when(bookService).deleteBook(1);

        mockMvc.perform(delete("/api/v1/books/{id}", 1))
                .andExpect(status().isOk());
    }

    @Test
    void updateBook() throws Exception {
        given(bookService.updateBook(eq(1), any(Book.class))).willReturn(book);

        mockMvc.perform(put("/api/v1/books/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Effective Java"));
    }

    @Test
    void getBookByIdNotFound() throws Exception {
        given(bookService.findBookById(1)).willThrow(ResourceNotFoundException.class);

        mockMvc.perform(get("/api/v1/books/{id}", 1))
                .andExpect(status().isNotFound());
    }

    private void setAuthentication(String... roles) {
        List<SimpleGrantedAuthority> authorities = Stream.of(roles)
                .map(SimpleGrantedAuthority::new)
                .toList();
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken("test_user", "", authorities));
    }
}