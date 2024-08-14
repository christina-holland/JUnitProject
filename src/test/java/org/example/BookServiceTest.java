package org.example;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class BookServiceTest {

    private BookService bookService;
    private User user;
    private Book book1;
    private Book book2;

    @BeforeEach
    void setUp() {
        bookService = new BookService();
        user = new User("johnDoe", "password123", "john@example.com");

        book1 = new Book("1984", "George Orwell", "Dystopian", 9.99);
        book2 = new Book("To Kill a Mockingbird", "Harper Lee", "Fiction", 14.99);

        bookService.addBook(book1);
        bookService.addBook(book2);
    }

    @AfterEach
    void tearDown() {
        // Clean up resources if needed
    }

    @Test
    void testSearchBook_ByTitle_Positive() {
        List<Book> results = bookService.searchBook("1984");
        assertEquals(1, results.size(), "Search by title should return 1 result.");
        assertEquals(book1, results.get(0), "The returned book should be '1984'.");
    }

    @Test
    void testSearchBook_ByAuthor_Negative() {
        List<Book> results = bookService.searchBook("Nonexistent Author");
        assertTrue(results.isEmpty(), "Search by non-existent author should return no results.");
    }

    @Test
    void testSearchBook_ByGenre_EdgeCase() {
        List<Book> results = bookService.searchBook("");
        assertTrue(results.contains(book1) && results.contains(book2), "Search with empty keyword should return all books.");
    }

    @Test
    void testPurchaseBook_Positive() {
        boolean result = bookService.purchaseBook(user, book1);
        assertTrue(result, "Purchase should succeed if the book is in the database.");
    }

    @Test
    void testPurchaseBook_Negative() {
        Book newBook = new Book("New Book", "New Author", "New Genre", 19.99);
        boolean result = bookService.purchaseBook(user, newBook);
        assertFalse(result, "Purchase should fail if the book is not in the database.");
    }

    @Test
    void testPurchaseBook_EdgeCase() {
        boolean result = bookService.purchaseBook(user, null);
        assertFalse(result, "Purchase should fail if the book is null.");
    }

    @Test
    void testAddBookReview_Positive() {
        user.setPurchasedBooks(new ArrayList<>(List.of(book1)));
        boolean result = bookService.addBookReview(user, book1, "Great book!");
        assertTrue(result, "Adding a review should succeed if the user has purchased the book.");
        assertTrue(book1.getReviews().contains("Great book!"), "The review should be added to the book.");
    }

    @Test
    void testAddBookReview_Negative() {
        boolean result = bookService.addBookReview(user, book1, "Great book!");
        assertFalse(result, "Adding a review should fail if the user has not purchased the book.");
    }

    @Test
    void testAddBookReview_EdgeCase() {
        user.setPurchasedBooks(new ArrayList<>());
        boolean result = bookService.addBookReview(user, null, "Great book!");
        assertFalse(result, "Adding a review should fail if the book is null.");
    }

    @Test
    void testAddBook_Positive() {
        Book newBook = new Book("New Book", "New Author", "New Genre", 19.99);
        boolean result = bookService.addBook(newBook);
        assertTrue(result, "Adding a new book should succeed.");
        assertTrue(bookService.searchBook("New Book").contains(newBook), "The new book should be in the database.");
    }

    @Test
    void testAddBook_Negative() {
        boolean result = bookService.addBook(book1);
        assertFalse(result, "Adding a book that already exists should fail.");
    }

    @Test
    void testAddBook_EdgeCase() {
        boolean result = bookService.addBook(null);
        assertFalse(result, "Adding a null book should fail.");
    }

    @Test
    void testRemoveBook_Positive() {
        boolean result = bookService.removeBook(book1);
        assertTrue(result, "Removing a book should succeed if it is in the database.");
        assertFalse(bookService.searchBook("1984").contains(book1), "The book should no longer be in the database.");
    }

    @Test
    void testRemoveBook_Negative() {
        Book newBook = new Book("Nonexistent Book", "Unknown Author", "Unknown Genre", 0.0);
        boolean result = bookService.removeBook(newBook);
        assertFalse(result, "Removing a book that is not in the database should fail.");
    }

    @Test
    void testRemoveBook_EdgeCase() {
        boolean result = bookService.removeBook(null);
        assertFalse(result, "Removing a null book should fail.");
    }
}
