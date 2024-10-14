package com.example.bookstore.service;

import com.example.bookstore.model.entity.Author;
import com.example.bookstore.exception.ResourceNotFoundException;
import com.example.bookstore.repository.AuthorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing author data.
 */
@Slf4j
@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    /**
     * Constructs an AuthorService with the necessary author repository.
     *
     * @param authorRepository The repository for author data operations.
     */
    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    /**
     * Retrieves all authors from the repository.
     *
     * @return A list of all authors.
     */
    public List<Author> findAllAuthors() {
        log.info("Fetching all authors");
        return authorRepository.findAll();
    }

    /**
     * Finds an author by their ID.
     *
     * @param id The ID of the author to find.
     * @return The found author, or null if no author is found.
     */
    public Author findAuthorById(Integer id) {
        log.info("Fetching author by ID: {}", id);
        return authorRepository.findById(id).orElse(null);
    }

    /**
     * Saves a new author to the repository.
     *
     * @param author The author to save.
     * @return The saved author.
     */
    public Author saveAuthor(Author author) {
        log.info("Saving new author: {}", author);
        return authorRepository.save(author);
    }

    /**
     * Deletes an author by their ID.
     *
     * @param id The ID of the author to delete.
     */
    public void deleteAuthor(Integer id) {
        log.info("Deleting author with ID: {}", id);
        if (authorRepository.existsById(id)){
            authorRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Author not found with id " + id);
        }
    }

    /**
     * Updates an existing author identified by ID with new data.
     *
     * @param id            The ID of the author to update.
     * @param authorDetails The new author data to apply.
     * @return The updated author.
     * @throws ResourceNotFoundException if no author is found with the given ID.
     */
    public Author updateAuthor(Integer id, Author authorDetails) {
        log.info("Updating author with ID: {}", id);
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id " + id));
        author.setFirstName(authorDetails.getFirstName());
        author.setLastName(authorDetails.getLastName());
        return authorRepository.save(author);
    }
}
