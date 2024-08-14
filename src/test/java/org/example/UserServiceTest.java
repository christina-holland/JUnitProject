package org.example;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mockito;

public class UserServiceTest {

    private UserService userService;
    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        userService = new UserService();
        user1 = new User("johnDoe", "password123", "john@example.com");
        user2 = new User("janeDoe", "password456", "jane@example.com");

        userService.registerUser(user1);
    }

    @AfterEach
    void tearDown() {
        // Clean up resources if needed
    }

    @Test
    void testRegisterUser_Positive() {
        User newUser = new User("newUser", "password", "newuser@example.com");
        boolean result = userService.registerUser(newUser);
        assertTrue(result, "User registration should succeed if the username is unique.");
    }

    @Test
    void testRegisterUser_Negative() {
        boolean result = userService.registerUser(user1);
        assertFalse(result, "User registration should fail if the username is already taken.");
    }

    @Test
    void testRegisterUser_EdgeCase() {
        User userWithEmptyUsername = new User("", "password", "emptyuser@example.com");
        boolean result = userService.registerUser(userWithEmptyUsername);
        assertTrue(result, "User registration should succeed if the username is empty (as long as it's unique).");
    }

    @Test
    void testLoginUser_Positive() {
        User result = userService.loginUser("johnDoe", "password123");
        assertNotNull(result, "Login should succeed with correct username and password.");
        assertEquals(user1, result, "Logged in user should be 'johnDoe'.");
    }

    @Test
    void testLoginUser_Negative() {
        User result = userService.loginUser("johnDoe", "wrongPassword");
        assertNull(result, "Login should fail with incorrect password.");
    }

    @Test
    void testLoginUser_EdgeCase() {
        User result = userService.loginUser("nonexistentUser", "password");
        assertNull(result, "Login should fail with a non-existent username.");
    }

    @Test
    void testUpdateUserProfile_Positive() {
        boolean result = userService.updateUserProfile(user1, "johnNew", "newPassword", "newemail@example.com");
        assertTrue(result, "User profile update should succeed with a unique new username.");
        assertEquals("johnNew", user1.getUsername(), "Username should be updated.");
        assertEquals("newPassword", user1.getPassword(), "Password should be updated.");
        assertEquals("newemail@example.com", user1.getEmail(), "Email should be updated.");
    }

    @Test
    void testUpdateUserProfile_Negative() {
        userService.registerUser(user2); // Register a second user
        boolean result = userService.updateUserProfile(user1, "janeDoe", "newPassword", "newemail@example.com");
        assertFalse(result, "User profile update should fail if the new username is already taken.");
    }

    @Test
    void testUpdateUserProfile_EdgeCase() {
        boolean result = userService.updateUserProfile(user1, "", "", "");
        assertTrue(result, "User profile update should succeed even with empty new values if the username is unique.");
        assertEquals("", user1.getUsername(), "Username should be updated to empty.");
        assertEquals("", user1.getPassword(), "Password should be updated to empty.");
        assertEquals("", user1.getEmail(), "Email should be updated to empty.");
    }
}
