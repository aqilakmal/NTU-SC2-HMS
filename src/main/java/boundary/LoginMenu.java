package boundary;

import controller.*;
import entity.*;
import java.util.Scanner;

/**
 * Interface for user authentication in the Hospital Management System.
 */
public class LoginMenu {

    private AuthenticationController authController;
    private Scanner scanner;

    /**
     * Constructor for LoginMenu.
     * 
     * @param authController The AuthenticationController instance to handle
     *                       authentication
     */
    public LoginMenu(AuthenticationController authController) {
        this.authController = authController;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Displays the login screen and handles the login process.
     * 
     * @return Authenticated User object or null if login fails
     */
    public User displayLoginScreen() {
        System.out.println("\n<======= LOGIN SCREEN =======>\n");

        while (true) {
            int attempts = 0;
            while (attempts < 3) {
                try {
                    System.out.print("Enter your user ID: ");
                    String userID = scanner.nextLine().trim();

                    if (userID.isEmpty()) {
                        System.out.println("Error: User ID cannot be empty. Please try again.");
                        continue;
                    }

                    System.out.print("Enter your password: ");
                    String password = scanner.nextLine().trim();

                    if (password.isEmpty()) {
                        System.out.println("Error: Password cannot be empty. Please try again.");
                        continue;
                    }

                    User authenticatedUser = authController.login(userID, password);

                    if (authenticatedUser != null) {
                        System.out.println("\nLogin successful. Welcome, " + authenticatedUser.getName() + "!");
                        return authenticatedUser;
                    } else {
                        attempts++;
                        System.out.println("Invalid user ID or password. Attempts remaining: " + (3 - attempts));
                        System.out.println();
                    }
                } catch (Exception e) {
                    System.err.println("An error occurred during login: " + e.getMessage());
                    attempts++;
                }
            }

            System.out.print("Maximum attempts reached. Do you want to try again? (y/n): ");
            String retry = scanner.nextLine().trim().toLowerCase();
            if (!retry.equals("y")) {
                System.out.println("Returning to home screen...");
                return null;
            }
        }
    }

    /**
     * Prompts the user to change their password.
     * 
     * @param user The authenticated User object
     * @return true if password was changed successfully, false otherwise
     */
    public boolean changePassword(User user) {
        System.out.println("\nChange Password");
        int attempts = 0;
        while (attempts < 3) {
            try {
                System.out.print("Enter your current password: ");
                String oldPassword = scanner.nextLine().trim();

                if (oldPassword.isEmpty()) {
                    System.out.println("Error: Current password cannot be empty. Please try again.");
                    continue;
                }

                System.out.print("Enter your new password: ");
                String newPassword = scanner.nextLine().trim();

                if (newPassword.isEmpty()) {
                    System.out.println("Error: New password cannot be empty. Please try again.");
                    continue;
                }

                if (newPassword.equals(oldPassword)) {
                    System.out.println("Error: New password must be different from the current password.");
                    continue;
                }

                authController.changePassword(user.getUserID(), oldPassword, newPassword);
                System.out.println("Password changed successfully.");
                return true;
            } catch (AuthenticationController.AuthenticationException e) {
                attempts++;
                System.out.println("Failed to change password: " + e.getMessage());
                System.out.println("Attempts remaining: " + (3 - attempts));
            } catch (Exception e) {
                System.err.println("An unexpected error occurred: " + e.getMessage());
                attempts++;
            }
        }

        System.out.print("Maximum attempts reached. Do you want to try again? (y/n): ");
        String retry = scanner.nextLine().trim().toLowerCase();
        if (retry.equals("y")) {
            return changePassword(user);
        } else {
            return false;
        }
    }
}