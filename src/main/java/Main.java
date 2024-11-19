// Utility
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

// Boundary
import boundary.AdministratorMenu;
import boundary.DoctorMenu;
import boundary.LoginMenu;
import boundary.PatientMenu;
import boundary.PharmacistMenu;

// Controller
import controller.AdministratorController;
import controller.AuthenticationController;
import controller.DoctorController;
import controller.PatientController;
import controller.PharmacistController;

// Controller Data
import controller.data.AppointmentDataManager;
import controller.data.HistoryDataManager;
import controller.data.MedicationDataManager;
import controller.data.OutcomeDataManager;
import controller.data.PrescriptionDataManager;
import controller.data.RequestDataManager;
import controller.data.SlotDataManager;
import controller.data.UserDataManager;

// Entity
import entity.User;
/**
 * Main class for the Hospital Management System (HMS). This class serves as the entry
 * point for the application and orchestrates the initialization and coordination of 
 * all system components.
 * 
 * The class manages the complete lifecycle of the HMS application including:
 * - Data manager initialization and CSV data loading
 * - Controller initialization and dependency injection
 * - User authentication and role-based menu routing
 * - System shutdown and data persistence
 * 
 * The class implements a command-line interface for user interaction and handles
 * all potential runtime errors to ensure system stability.
 * 
 * @author Group 7
 * @version 1.0
 */
public class Main {

    private static Scanner scanner = new Scanner(System.in);
    private static LoginMenu loginMenu;
    private static AuthenticationController authController;
    private static AdministratorController administratorController;
    private static PatientController patientController;
    private static DoctorController doctorController;
    private static PharmacistController pharmacistController;
    private static AppointmentDataManager appointmentDataManager;
    private static UserDataManager userDataManager;
    private static MedicationDataManager medicationDataManager;
    private static PrescriptionDataManager prescriptionDataManager;
    private static OutcomeDataManager outcomeDataManager;
    private static SlotDataManager slotDataManager;
    private static HistoryDataManager historyDataManager;
    private static RequestDataManager requestDataManager;

    /**
     * The main entry point for the Hospital Management System.
     * 
     * Executes the following operations in sequence:
     * 1. Displays welcome message and initializes system components
     * 2. Loads all required data from CSV storage
     * 3. Sets up the user interface and authentication system
     * 4. Runs the main application loop
     * 5. Performs cleanup and data persistence on exit
     * 
     * The method implements comprehensive error handling to ensure
     * graceful system behavior even in case of failures.
     * 
     * @param args Command line arguments (not used in current implementation)
     */
    public static void main(String[] args) {
        System.out.println("Welcome to the Hospital Management System");

        // Initialize all data managers and load user data
        initializeDataManagers();
        
        // Initialize all controllers
        initializeControllers();

        // Initialize the LoginMenu & display the home screen
        loginMenu = new LoginMenu(authController);
        displayHomeScreen();

        // Save all data when the program is terminated
        saveDataOnExit();

        System.out.println("Thank you for using the Hospital Management System");
    }

    /**
     * Initializes all data managers and loads data from CSV storage.
     * 
     * Creates instances of all required data managers and loads their respective
     * data from CSV files. This includes user data, medication inventory,
     * appointment slots, medical histories, and other system records.
     * 
     * If any data loading operation fails, the error is logged but the system
     * continues to operate with empty data sets where necessary.
     */
    private static void initializeDataManagers() {
        userDataManager = new UserDataManager();
        medicationDataManager = new MedicationDataManager();
        slotDataManager = new SlotDataManager();
        appointmentDataManager = new AppointmentDataManager(slotDataManager);
        outcomeDataManager = new OutcomeDataManager();
        requestDataManager = new RequestDataManager();
        prescriptionDataManager = new PrescriptionDataManager();
        historyDataManager = new HistoryDataManager();

        try {
            userDataManager.loadUsersFromCSV();
            medicationDataManager.loadMedicationsFromCSV();
            slotDataManager.loadSlotsFromCSV();
            appointmentDataManager.loadAppointmentsFromCSV();
            outcomeDataManager.loadOutcomesFromCSV();
            requestDataManager.loadRequestsFromCSV();
            prescriptionDataManager.loadPrescriptionsFromCSV();
            historyDataManager.loadHistoriesFromCSV();
        } catch (IOException e) {
            System.err.println("Error loading data: " + e.getMessage());
        }
    }

    /**
     * Initializes all system controllers with their required dependencies.
     * 
     * Creates and configures controller instances for each user role:
     * - Authentication controller for login management
     * - Administrator controller for system management
     * - Doctor controller for medical operations
     * - Patient controller for appointment management
     * - Pharmacist controller for medication management
     * 
     * If controller initialization fails, the system will terminate with
     * an error message as controllers are essential for operation.
     */
    private static void initializeControllers() {
        try {
            authController = new AuthenticationController(userDataManager);
            administratorController = new AdministratorController(
                userDataManager,
                appointmentDataManager,
                outcomeDataManager,
                medicationDataManager,
                requestDataManager,
                authController,
                prescriptionDataManager,
                slotDataManager
            );
            doctorController = new DoctorController(
                userDataManager,
                slotDataManager,
                appointmentDataManager,
                historyDataManager,
                outcomeDataManager,
                medicationDataManager,
                prescriptionDataManager,
                authController
            );
            patientController = new PatientController(
                appointmentDataManager,
                historyDataManager,
                slotDataManager,
                doctorController,
                authController,
                outcomeDataManager,
                prescriptionDataManager,
                medicationDataManager,
                userDataManager
            );
            pharmacistController = new PharmacistController(
                medicationDataManager,
                prescriptionDataManager,
                outcomeDataManager,
                appointmentDataManager,
                slotDataManager,
                userDataManager,
                requestDataManager,
                authController
            );
        } catch (Exception e) {
            System.err.println("Critical Error: Failed to initialize controllers. The system cannot start.");
            System.err.println("Error details: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Manages the main application loop and user interface.
     * 
     * Displays the main menu with login and exit options, handles user input,
     * and manages the flow of control. Implements input validation and error
     * handling for all user interactions.
     * 
     * The loop continues until the user chooses to exit the system or
     * an unrecoverable error occurs.
     */
    private static void displayHomeScreen() {
        while (true) {
            try {
                System.out.println("\n<======= HOSPITAL MANAGEMENT SYSTEM =======>\n");
                System.out.println("{1} Log in");
                System.out.println("{2} Exit");
                System.out.print("Enter your choice: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        handleLogin();
                        break;
                    case 2:
                        System.out.println("Exiting the Hospital Management System. Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid choice. Please enter 1 or 2.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear the invalid input
            } catch (Exception e) {
                System.err.println("An unexpected error occurred: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Manages the user authentication process and role-based menu routing.
     * 
     * Handles the following operations:
     * 1. User credential validation
     * 2. First-time login password change requirement
     * 3. Role-based menu initialization and display
     * 
     * Implements comprehensive error handling for authentication failures
     * and invalid role assignments.
     */
    private static void handleLogin() {
        try {
            User authenticatedUser = loginMenu.displayLoginScreen();

            if (authenticatedUser != null) {

                if (authenticatedUser.getPassword().equals("password")) {
                    System.out.println("This is your first login. You must change your password.");
                    boolean passwordChanged = loginMenu.changePassword(authenticatedUser);
                    if (!passwordChanged) {
                        System.out.println("Password change cancelled. Logging out...");
                        return;
                    }
                }

                // Based on the authenticated user's role, initialize appropriate UI
                switch (authenticatedUser.getRole()) {
                    case PATIENT:
                        new PatientMenu(patientController).displayMenu();
                        break;
                    case DOCTOR:
                        new DoctorMenu(doctorController).displayMenu();
                        break;
                    case PHARMACIST:
                        new PharmacistMenu(pharmacistController).displayMenu();
                        break;
                    case ADMINISTRATOR:
                        new AdministratorMenu(administratorController).displayMenu();
                        break;
                    default:
                        System.out.println("Error: Invalid user role. Please contact system administrator.");
                        break;
                }
            } else {
                System.out.println("Login failed. Please try again.");
            }
        } catch (Exception e) {
            System.err.println("An error occurred during login: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Performs system shutdown operations and data persistence.
     * 
     * Saves all system data to CSV storage including:
     * - User records
     * - Medication inventory
     * - Appointment schedules
     * - Medical histories
     * - System configurations
     * 
     * Logs any errors that occur during the save operation but continues
     * with the shutdown process.
     */
    private static void saveDataOnExit() {
        try {
            userDataManager.saveUsersToCSV();
            medicationDataManager.saveMedicationsToCSV();
            slotDataManager.saveSlotsToCSV();
            appointmentDataManager.saveAppointmentsToCSV();
            outcomeDataManager.saveOutcomesToCSV();
            requestDataManager.saveRequestsToCSV();
            prescriptionDataManager.savePrescriptionsToCSV();
            historyDataManager.saveHistoriesToCSV();
        } catch (IOException e) {
            System.err.println("Error saving data to CSV: " + e.getMessage());
        }
    }
}
