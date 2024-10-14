package com.example.bookstore.controller;

import com.example.bookstore.service.AuthorService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test") // Use the 'test' profile where H2 in-memory database is set up
class AuthorControllerTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private AuthorService authorService;

    // Set up RestAssured to use the random port assigned during the test startup
    @BeforeEach
    void setUp() throws Exception {
        RestAssured.port = port;
    }


    /**
     * Test retrieving all authors.
     * The H2 database is pre-populated using 'schema-h2.sql' with authors.
     * This test verifies that the list of authors is correctly fetched.
     */
    @Test
    void shouldFindAllAuthors() {
        RestAssured.given()
                .auth().preemptive().basic("john_doe", "password123")
                .get("/api/v1/authors")
                .then()
                .statusCode(200)
                .body("[0].firstName", equalTo("Emily"))
                .body("[0].lastName", equalTo("Johnson"))
                .body("[1].firstName", equalTo("Jane"))
                .body("[1].lastName", equalTo("Smith"));
    }

    /**
     * Test retrieving an author by their ID when the author exists.
     * The ID is pre-defined in the 'schema-h2.sql' script.
     */
    @Test
    void shouldFindAuthorByIdWhenAuthorExist() {
        RestAssured.given()
                .auth().preemptive().basic("john_doe", "password123")
                .get("/api/v1/authors/{id}", 1)
                .then()
                .statusCode(200)
                .body("firstName", equalTo("John"))
                .body("lastName", equalTo("Doe"));
    }

    /**
     * Test retrieving an author by their ID when the author does not exist.
     * This verifies that the service returns a 404 status code.
     */
    @Test
    void shouldFindAuthorByIdWhenAuthorDoesNotExist() {
        RestAssured.given()
                .auth().preemptive().basic("john_doe", "password123")
                .get("/api/v1/authors/{id}", 10)
                .then()
                .statusCode(404);
    }

    /**
     * Test creating a new author.
     * This test verifies the creation of an author with valid data.
     */
    @Test
    void shouldCreateAuthor() {
        RestAssured.given()
                .auth().preemptive().basic("john_doe", "password123")
                .contentType(ContentType.JSON)
                .body("""
                            {
                                "firstName":"Jack",
                                "lastName":"Daniels"
                            }
                        """
                )
                .when()
                .post("/api/v1/authors")
                .then()
                .statusCode(200);
    }

    /**
     * Test updating an existing author by ID.
     * The author with the specified ID must already exist in the H2 database.
     */
    @Test
    void shouldUpdateAuthorWhenAuthorExist() {
        RestAssured.given()
                .auth().preemptive().basic("john_doe", "password123")
                .contentType(ContentType.JSON)
                .body("""
                            {
                                "firstName":"Michael_Updated",
                                "lastName":"Brown_Updated"
                            }
                        """
                )
                .when()
                .put("/api/v1/authors/{id}", 4)
                .then()
                .statusCode(200)
                .body("firstName", equalTo("Michael_Updated"))
                .body("lastName", equalTo("Brown_Updated"));
    }

    /**
     * Test updating a non-existent author by ID.
     * This verifies that the service returns a 404 status code.
     */
    @Test
    void shouldUpdateAuthorWhenAuthorDoesNotExist() {
        RestAssured.given()
                .auth().preemptive().basic("john_doe", "password123")
                .contentType(ContentType.JSON)
                .body("""
                            {
                                "firstName":"Michael_Updated",
                                "lastName":"Brown_Updated"
                            }
                        """
                )
                .when()
                .put("/api/v1/authors/{id}", 10)
                .then()
                .statusCode(404);
    }

    /**
     * Test deleting an existing author by ID.
     * The author with the specified ID must already exist in the H2 database.
     */
    @Test
    void shouldDeleteWhenAuthorExist() {
        RestAssured.given()
                .auth().preemptive().basic("john_doe", "password123")
                .delete("/api/v1/authors/{id}", 3)
                .then()
                .statusCode(200);
    }

    /**
     * Test deleting a non-existent author by ID.
     * This verifies that the service returns a 404 status code.
     */
    @Test
    void shouldDeleteWhenAuthorDoesNotExist() {
        RestAssured.given()
                .auth().preemptive().basic("john_doe", "password123")
                .delete("/api/v1/authors/{id}", 10)
                .then()
                .statusCode(404);
    }
}
