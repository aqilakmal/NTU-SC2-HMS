package controller;

import entity.User;
import controller.data.UserDataManager;

/**
 * Controller class responsible for managing user authentication and password management
 * in the Hospital Management System (HMS). This class handles user login validation,
 * session management, and password change operations.
 * 
 * The controller coordinates with the UserDataManager to verify credentials and 
 * maintain user sessions. It provides core security functionality including:
 * - User authentication and login management
 * - Current session tracking
 * - Password change validation and processing
 * - Custom authentication exception handling
 *
 * @author Group 7
 * @version 1.0
 */
public class AuthenticationController {

    /**
     * The currently authenticated user in the system.
     * Maintains the active user session after successful login.
     * Null when no user is logged in.
     */
    private User currentUser;

    /**
     * UserDataManager instance to manage user data operations.
     * Handles data persistence and CRUD operations for user records.
     * Used to validate credentials and update user information.
     */
    private UserDataManager userDataManager;
    
    /**
     * Constructs a new AuthenticationController with the specified UserDataManager.
     * Initializes the controller with necessary data access capabilities for
     * user authentication and management operations.
     *
     * @param userDataManager The UserDataManager instance to handle user data operations
     */
    public AuthenticationController(UserDataManager userDataManager) {
        this.userDataManager = userDataManager;
    }

    /**
     * Authenticates a user and establishes their session in the system.
     * Validates the provided credentials against stored user records.
     * Updates the current user session upon successful authentication.
     *
     * @param userID The user's unique identifier for authentication
     * @param password The user's password to verify
     * @return User object if authentication succeeds, null if credentials are invalid
     */
    public User login(String userID, String password) {
        User user = userDataManager.getUserByID(userID);
        if (user != null && user.getPassword().equals(password)) {
            currentUser = user;
            return user;
        }
        return null;
    }

    /**
     * Retrieves the currently authenticated user in the system.
     * Provides access to the active user session information.
     * Used to verify user authorization for protected operations.
     *
     * @return The currently authenticated User object, or null if no user is authenticated
     */
    public User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Changes the password for a specified user in the system.
     * Validates the old password before allowing the change.
     * Updates the user's password in persistent storage upon validation.
     *
     * @param userID The user's unique identifier
     * @param oldPassword The user's current password for verification
     * @param newPassword The new password to set for the user
     * @throws AuthenticationException if user not found, old password incorrect, or new password invalid
     */
    public void changePassword(String userID, String oldPassword, String newPassword) throws AuthenticationException {
        User user = userDataManager.getUserByID(userID);
        if (user == null) {
            throw new AuthenticationException("User not found");
        }
        if (!user.getPassword().equals(oldPassword)) {
            throw new AuthenticationException("Incorrect old password");
        }
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new AuthenticationException("New password cannot be empty");
        }
        user.updatePassword(newPassword);
        try {
            userDataManager.updateUser(user);
        } catch (IllegalArgumentException e) {
            throw new AuthenticationException(e.getMessage());
        }
    }

    /**
     * Custom exception class for handling authentication-related errors in the HMS.
     * Provides specific error handling for authentication failures, invalid credentials,
     * and password change errors. Extends the standard Exception class to maintain
     * proper exception hierarchy.
     */
    public class AuthenticationException extends Exception {
        
        /**
         * Constructs a new AuthenticationException with a specified error message.
         * Used for basic authentication errors where only a message is needed.
         * Provides detailed feedback about the nature of the authentication failure.
         *
         * @param message The detailed message describing the authentication error
         */
        public AuthenticationException(String message) {
            super(message);
        }

        /**
         * Constructs a new AuthenticationException with a message and underlying cause.
         * Used for complex authentication errors where both a message and cause are needed.
         * Provides complete error context for debugging and error handling.
         *
         * @param message The detailed message describing the authentication error
         * @param cause The underlying cause of the authentication failure
         */
        public AuthenticationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}