package controller;

import entity.User;

/**
 * Handles user authentication and password management in the Hospital Management System.
 */
public class AuthenticationController {

    /**
     * UserController instance to manage user data.
     */
    private UserController userController;
    
    /**
     * Constructor for AuthenticationController.
     * @param userController UserController instance to manage user data.
     */
    public AuthenticationController(UserController userController) {
        this.userController = userController;
    }

    /**
     * Authenticates a user and logs them into the system.
     * @param userID The user's unique identifier
     * @param password The user's password
     * @return User object if authentication is successful, null otherwise
     */
    public User login(String userID, String password) {
        User user = userController.getUserByID(userID);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
    
    /**
     * Changes the password for a user.
     * @param userID The user's unique identifier
     * @param oldPassword The user's current password
     * @param newPassword The new password to set
     * @throws AuthenticationException if the old password is incorrect or the new password is invalid
     */
    public void changePassword(String userID, String oldPassword, String newPassword) throws AuthenticationException {
        User user = userController.getUserByID(userID);
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
            userController.updateUser(user);
        } catch (UserController.UserException e) {
            throw new AuthenticationException(e.getMessage());
        }
    }

    /**
     * AuthenticationException extends Exception to handle exceptions related to authentication.
     */
    public class AuthenticationException extends Exception {
        
        /**
         * Constructor for AuthenticationException with a message.
         * @param message The message to display when the exception is thrown.
         */
        public AuthenticationException(String message) {
            super(message);
        }

        /**
         * Constructor for AuthenticationException with a message and a cause.
         * @param message The message to display when the exception is thrown.
         * @param cause The cause of the exception.
         */
        public AuthenticationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}