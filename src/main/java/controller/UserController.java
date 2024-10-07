package controller;

import entity.User;
import java.util.List;
import java.util.Map;

/**
 * Manages hospital staff and patient accounts in the Hospital Management System.
 */
public class UserController {

    private UserDataManager userDataManager;

    /**
     * Constructor for UserController.
     * @param userDataManager The UserDataManager instance to manage user data.
     */
    public UserController(UserDataManager userDataManager) {
        this.userDataManager = userDataManager;
    }

    /**
     * Retrieves a user by their ID.
     * @param userID The ID of the user to retrieve.
     * @return The User object if found, null otherwise.
     */
    public User getUserByID(String userID) {
        return userDataManager.getUserByID(userID);
    }
    
    /**
     * Adds a new user to the system.
     * @param user The User object to add
     * @throws UserException if the user already exists or is invalid
     */
    public void addUser(User user) throws UserException {
        // TODO: Implement logic to add new user
    }
    
    /**
     * Updates an existing user's information.
     * @param user The User object with updated information
     * @throws UserException if the user doesn't exist or the update is invalid
     */
    public void updateUser(User user) throws UserException {
        // TODO: Implement logic to update user information
    }
    
    /**
     * Removes a user from the system.
     * @param userID The unique identifier of the user to remove
     * @throws UserException if the user doesn't exist or cannot be removed
     */
    public void removeUser(String userID) throws UserException {
        // TODO: Implement logic to remove user
    }
    
    /**
     * Retrieves users based on specified filters.
     * @param filter The filter to apply when retrieving users
     * @return List of User objects matching the filter
     */
    public List<User> getUsers(Map<String, Object> filter) {
        // TODO: Implement logic to retrieve filtered users
        return null;
    }

    /**
     * UserException extends Exception to handle exceptions related to user management.
     */
    public class UserException extends Exception {
      
        /**
         * Constructor for UserException with a message.
         * @param message The message to display when the exception is thrown.
         */
        public UserException(String message) {
            super(message);
        }

        /**
         * Constructor for UserException with a message and a cause.
         * @param message The message to display when the exception is thrown.
         * @param cause The cause of the exception.
         */
        public UserException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}