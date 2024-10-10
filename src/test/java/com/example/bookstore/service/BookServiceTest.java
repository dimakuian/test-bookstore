package com.example.bookstore.service;

import com.example.bookstore.exception.ResourceNotFoundException;
import com.example.bookstore.model.BookResponse;
import com.example.bookstore.model.CurrencyResponse;
import com.example.bookstore.model.entity.Author;
import com.example.bookstore.model.entity.Book;
import com.example.bookstore.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private FixerApiAdapter fixerApiAdapter;

    @InjectMocks
    private BookService bookService;

    @Test
    void testFindAllBooks() {
        // Arrange
        Book book1 = new Book(1, "Book One", new Author(1, "Author", "One"), 10.0);
        Book book2 = new Book(2, "Book Two", new Author(1, "Author", "Two"), 15.0);
        List<Book> books = Arrays.asList(book1, book2);
        when(bookRepository.findAll()).thenReturn(books);

        CurrencyResponse currencyResponse = new CurrencyResponse();
        currencyResponse.setRates(new HashMap<>() {{
            put("USD", 1.1);
            put("EUR", 0.9);
        }});
        when(fixerApiAdapter.fetchCurrencyRates()).thenReturn(currencyResponse);

        // Act
        List<BookResponse> bookResponses = bookService.findAllBooks();

        // Assert
        assertEquals(2, bookResponses.size());
        assertEquals("Book One", bookResponses.getFirst().getTitle());
        assertEquals("Author One", bookResponses.getFirst().getAuthor());
        assertNotNull(bookResponses.getFirst().getPrice());
        assertTrue(bookResponses.getFirst().getPrice().containsKey("USD"));
        assertTrue(bookResponses.getFirst().getPrice().containsKey("EUR"));
    }

    @Test
    void testFindBookById_Found() {
        // Arrange
        Integer bookId = 1;
        Book book = new Book(bookId, "Effective Java", new Author(1, "Joshua", "Bloch"), 40.0);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        CurrencyResponse currencyResponse = new CurrencyResponse();
        currencyResponse.setRates(new HashMap<>() {{
            put("USD", 1.1);
            put("EUR", 0.9);
        }});
        when(fixerApiAdapter.fetchCurrencyRates()).thenReturn(currencyResponse);

        // Act
        BookResponse bookResponse = bookService.findBookById(bookId);

        // Assert
        assertNotNull(bookResponse);
        assertEquals("Effective Java", bookResponse.getTitle());
        assertEquals("Joshua Bloch", bookResponse.getAuthor());
        assertTrue(bookResponse.getPrice().containsKey("USD"));
        assertTrue(bookResponse.getPrice().containsKey("EUR"));
    }

    @Test
    void testFindBookById_NotFound() {
        // Arrange
        Integer bookId = 99;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> bookService.findBookById(bookId));
    }

    @Test
    void testSaveBook() {
        // Arrange
        Book bookToSave = new Book(null, "Clean Code", new Author(1, "Robert C.", "Martin"), 25.0);
        Book savedBook = new Book(1, "Clean Code", new Author(1, "Robert C.", "Martin"), 25.0); // Assume the repository assigns ID 1
        when(bookRepository.save(bookToSave)).thenReturn(savedBook);

        // Act
        Book result = bookService.saveBook(bookToSave);

        // Assert
        assertNotNull(result);
        assertEquals(Integer.valueOf(1), result.getId());

        // Verify that the repository's save method was called exactly once with the book to save
        verify(bookRepository, times(1)).save(bookToSave);
    }

    @Test
    void testUpdateBook_Found() {
        // Arrange
        Integer bookId = 1;
        Book existingBook = new Book(bookId, "Original Title", new Author(1, "Original", "Author"), 20.0);
        Book updatedBook = new Book(bookId, "Updated Title", new Author(1, "Updated", "Author"), 25.0);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(existingBook)).thenReturn(existingBook);

        // Act
        Book result = bookService.updateBook(bookId, updatedBook);

        // Assert
        assertNotNull(result);
        assertEquals(bookId, result.getId());
        assertEquals("Updated Title", result.getTitle());
        assertEquals(new Author(1, "Updated", "Author"), result.getAuthor());
        assertEquals(25.0, result.getPrice());

        // Verify that the repository's save method was called exactly once with the updated book
        verify(bookRepository, times(1)).save(existingBook);
    }

    @Test
    void testUpdateBook_NotFound() {
        // Arrange
        Integer bookId = 99;
        Book updatedBook = new Book(bookId, "Updated Title", new Author(1, "Updated", "Author"), 25.0);
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> bookService.updateBook(bookId, updatedBook));
    }

    @Test
    void testDeleteBook() {
        // Arrange
        Integer bookId = 1;

        // Act
        bookService.deleteBook(bookId);

        // Assert
        // Verify that the repository's deleteById method was called exactly once with the correct ID
        verify(bookRepository, times(1)).deleteById(bookId);
    }
}