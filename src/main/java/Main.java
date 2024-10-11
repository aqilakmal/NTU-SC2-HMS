import boundary.*;
import controller.*;
import controller.data.*;
import entity.*;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.io.IOException;

/**
 * Main class for the Hospital Management System.
 * This class serves as the entry point for the application.
 */
public class Main {

	private static Scanner scanner = new Scanner(System.in);
	private static LoginMenu loginMenu;
	private static AuthenticationController authController;
	private static UserController userController;
	private static AdministratorController AdministratorController;

	/**
	 * The main method that starts the Hospital Management System.
	 * @param args Command line arguments (not used in this application)
	 */
	public static void main(String[] args) {
		System.out.println("Welcome to the Hospital Management System");

		// Initialize all data managers and load user data
		UserDataManager userDataManager = new UserDataManager();
		MedicationDataManager medicationDataManager = new MedicationDataManager();
		AppointmentDataManager appointmentDataManager = new AppointmentDataManager();
		OutcomeDataManager outcomeDataManager = new OutcomeDataManager();
		RequestDataManager requestDataManager = new RequestDataManager();
		
		// Load system data from CSV files
		try {
			userDataManager.loadUsersFromCSV();
			medicationDataManager.loadMedicationsFromCSV();
			appointmentDataManager.loadAppointmentsFromCSV();
			outcomeDataManager.loadOutcomesFromCSV();
			requestDataManager.loadRequestsFromCSV();
		} catch (IOException e) {
			System.err.println("Error loading data: " + e.getMessage());
		}

		// Initialize all controllers
		try {
			userController = new UserController(userDataManager);
			authController = new AuthenticationController(userController);
			AdministratorController = new AdministratorController(
				userDataManager,
				appointmentDataManager,
				outcomeDataManager,
				medicationDataManager,
				requestDataManager
			);
		} catch (Exception e) {
			System.err.println("Critical Error: Failed to initialize controllers. The system cannot start.");
			System.err.println("Error details: " + e.getMessage());
			e.printStackTrace();
			return;
		}

		// Initialize the LoginMenu
		loginMenu = new LoginMenu(authController);

		// Display the home screen menu
		displayHomeScreen();

		// Save all data when the program is terminated
		try {
			userDataManager.saveUsersToCSV();
			medicationDataManager.saveMedicationsToCSV();
			appointmentDataManager.saveAppointmentsToCSV();
			outcomeDataManager.saveOutcomesToCSV();
			requestDataManager.saveRequestsToCSV();
		} catch (IOException e) {
			System.err.println("Error saving data to CSV: " + e.getMessage());
		}

		System.out.println("Thank you for using the Hospital Management System");
	}

	/**
	 * Displays the home screen menu and handles user input.
	 */
	private static void displayHomeScreen() {
		while (true) {
			try {
				System.out.println("\nHospital Management System - Home Screen");
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
						AdministratorMenu adminUI = new AdministratorMenu(AdministratorController, userController);
						adminUI.displayMenu();
						break;
					default:
						System.out.println("Error: Invalid user role. Please contact system administrator.");
						break;
				}
			}
		} catch (Exception e) {
			System.err.println("An error occurred during login: " + e.getMessage());
			e.printStackTrace();
		}
	}
}