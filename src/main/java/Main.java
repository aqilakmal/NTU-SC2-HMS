package main.java;

import boundary.*;
import controller.*;
import entity.*;

/**
 * Main class for the Hospital Management System.
 * This class serves as the entry point for the application.
 */
public class Main {

    /**
     * The main method that starts the Hospital Management System.
     * @param args Command line arguments (not used in this application)
     */
    public static void main(String[] args) {
        System.out.println("Welcome to the Hospital Management System");

        // Initialize UserDataManager and load user data
        UserDataManager userDataManager = new UserDataManager();
        try {
            userDataManager.loadUsersFromCSV("users.csv");
        } catch (Exception e) {
            System.out.println("Error loading user data: " + e.getMessage());
            return;
        }

        // Initialize controllers
        UserController userController = new UserController(userDataManager);
        AuthenticationController authController = new AuthenticationController(userDataManager, userController);
        StaffManagementController staffManagementController = new StaffManagementController(userDataManager);

        // Initialize the LoginMenu
        LoginMenu loginMenu = new LoginMenu(authController);

        // Display login screen and handle user authentication
        User authenticatedUser = loginMenu.displayLoginScreen();

        if (authenticatedUser != null) {
            // Check if it's the user's first login (using default password)
            if (authenticatedUser.getPassword().equals("password")) {
                System.out.println("This is your first login. You must change your password.");
                loginMenu.changePassword(authenticatedUser);
            }

            // Based on the authenticated user's role, initialize appropriate UI
            switch (authenticatedUser.getRole()) {
                case PATIENT:
                    PatientMenu patientUI = new PatientMenu();
                    patientUI.displayMenu();
                    break;
                case DOCTOR:
                    DoctorMenu doctorUI = new DoctorMenu();
                    doctorUI.displayMenu();
                    break;
                case PHARMACIST:
                    PharmacistMenu pharmacistUI = new PharmacistMenu();
                    pharmacistUI.displayMenu();
                    break;
                case ADMINISTRATOR:
                    AdministratorMenu adminUI = new AdministratorMenu(staffManagementController, userController);
                    adminUI.displayMenu();
                    break;
                default:
                    System.out.println("Invalid user role");
                    break;
            }
        } else {
            System.out.println("Authentication failed. Exiting system.");
        }

        System.out.println("Thank you for using the Hospital Management System");
    }
}
