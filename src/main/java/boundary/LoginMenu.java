package boundary;

import controller.AuthenticationController;
import entity.User;
import java.util.Scanner;

/**
 * Interface for user authentication in the Hospital Management System.
 */
public class LoginMenu {
    
    private AuthenticationController authController;
    private Scanner scanner;
    
    /**
     * Constructor for LoginMenu.
     * @param authController The AuthenticationController instance to handle authentication
     */
    public LoginMenu(AuthenticationController authController) {
        this.authController = authController;
        this.scanner = new Scanner(System.in);
    }
    
    /**
     * Displays the login screen and handles the login process.
     * @return Authenticated User object or null if login fails
     */
    public User displayLoginScreen() {
        System.out.println("Welcome to the Hospital Management System");
        System.out.println("Please log in to continue");
        
        while (true) {
            System.out.print("Enter your user ID: ");
            String userID = scanner.nextLine();
            
            System.out.print("Enter your password: ");
            String password = scanner.nextLine();
            
            User authenticatedUser = authController.login(userID, password);
            
            if (authenticatedUser != null) {
                System.out.println("Login successful. Welcome, " + authenticatedUser.getName() + "!");
                return authenticatedUser;
            } else {
                System.out.println("Invalid user ID or password. Please try again.");
                System.out.print("Do you want to try again? (y/n): ");
                String retry = scanner.nextLine();
                if (!retry.equalsIgnoreCase("y")) {
                    return null;
                }
            }
        }
    }
    
    /**
     * Prompts the user to change their password.
     * @param user The authenticated User object
     */
    public void changePassword(User user) {
        System.out.println("\nChange Password");
        System.out.print("Enter your current password: ");
        String oldPassword = scanner.nextLine();
        
        System.out.print("Enter your new password: ");
        String newPassword = scanner.nextLine();
        
        try {
            authController.changePassword(user.getUserID(), oldPassword, newPassword);
            System.out.println("Password changed successfully.");
        } catch (AuthenticationController.AuthenticationException e) {
            System.out.println("Failed to change password: " + e.getMessage());
        }
    }
}