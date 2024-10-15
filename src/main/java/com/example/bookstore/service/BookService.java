package com.example.bookstore.service;

import com.example.bookstore.exception.ResourceNotFoundException;
import com.example.bookstore.model.BookResponse;
import com.example.bookstore.model.CurrencyResponse;
import com.example.bookstore.model.entity.Book;
import com.example.bookstore.model.mapper.BookMapper;
import com.example.bookstore.repository.BookRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * Service class for managing books.
 */
@Slf4j
@Service
public class BookService {

    private final BookRepository bookRepository;
    private final CurrencyRateService rateService;

    @Autowired
    public BookService(BookRepository bookRepository, CurrencyRateService rateService) {
        this.bookRepository = bookRepository;
        this.rateService = rateService;
    }

    /**
     * Retrieves all books and converts them to BookResponse DTOs with current currency rates.
     *
     * @return A page of BookResponse DTOs.
     */
    public Page<BookResponse> findAllBooks(PageRequest pageRequest) {
        log.info("Fetching all books with current currency rates");
        final CurrencyResponse currencyResponse = rateService.fetchCurrencyRates();
        return BookMapper.toBookResponseList(bookRepository.findAll(pageRequest), currencyResponse.getRates());
    }

    /**
     * Finds a book by its ID and converts it to a BookResponse DTO.
     * This method retrieves a book from the repository by its ID and uses the current currency rates
     * to convert the book's price into different currencies, encapsulating the result in a BookResponse DTO.
     *
     * @param id The ID of the book to find.
     * @return A BookResponse DTO representing the book with converted currency values.
     * @throws ResourceNotFoundException if no book is found with the given ID, indicating that the book retrieval operation cannot proceed.
     */
    public BookResponse findBookById(Integer id) {
        log.info("Fetching book by ID: {}", id);
        CurrencyResponse currencyResponse = rateService.fetchCurrencyRates();
        return bookRepository.findById(id)
                .map(book -> BookMapper.toBookResponse(book, currencyResponse.getRates()))
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id" + id));
    }

    /**
     * Saves a new book to the repository.
     *
     * @param book The book to save.
     * @return The saved book.
     */
    @Transactional
    public Book saveBook(Book book) {
        log.info("Saving new book: {}", book);
        return bookRepository.save(book);
    }

    /**
     * Updates an existing book identified by ID with new data.
     * This method fetches a book by its ID and updates its properties with the provided book data.
     *
     * @param id          The ID of the book to update.
     * @param updatedBook The new book data to apply. This includes title, author, and price.
     * @return The updated book.
     * @throws ResourceNotFoundException if no book is found with the given ID, indicating that the update operation cannot proceed.
     */
    @Transactional
    public Book updateBook(Integer id, Book updatedBook) {
        log.info("Updating book with ID: {}", id);
        return bookRepository.findById(id).map(book -> {
            book.setTitle(updatedBook.getTitle());
            book.setAuthor(updatedBook.getAuthor());
            book.setPrice(updatedBook.getPrice());
            log.info("Updated book: {}", book);
            return bookRepository.save(book);
        }).orElseThrow(() -> new ResourceNotFoundException("Book not found with id" + id));
    }

    /**
     * Deletes a book from the repository by its ID.
     *
     * @param id The ID of the book to delete.
     */
    @Transactional
    public void deleteBook(Integer id) {
        bookRepository.deleteById(id);
    }

}
