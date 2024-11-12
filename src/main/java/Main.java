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
 * Main class for the Hospital Management System. This class serves as the entry
 * point for the application.
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
     * The main method that starts the Hospital Management System.
     * @param args Command line arguments (not used in this application)
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
     * Initialize all data managers and load data from CSV.
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
     * Initialize all controllers with the loaded data managers.
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
                authController
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
                authController
            );
            pharmacistController = new PharmacistController(
                medicationDataManager,
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
     * Displays the home screen menu and handles user input.
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
     * Handles the login process and redirects to appropriate menu based on user role.
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
     * Saves all data to CSV on exit.
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
