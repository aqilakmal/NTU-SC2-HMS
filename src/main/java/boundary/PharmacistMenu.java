package boundary;

import controller.*;
import entity.*;
import utility.*;
import java.util.Scanner;
import java.util.List;
import java.util.InputMismatchException;

/**
 * Interface for pharmacist operations in the Hospital Management System.
 */
public class PharmacistMenu {
    
    private PharmacistController pharmacistController;
    private Scanner scanner;

    /**
     * Constructor for PharmacistMenu.
     * 
     * @param pharmacistController The PharmacistController instance
     */
    public PharmacistMenu(PharmacistController pharmacistController) {
        this.pharmacistController = pharmacistController;
        this.scanner = new Scanner(System.in);
    }

    /**
     * [MAIN MENU] Displays the menu options available to the pharmacist.
     */
    public void displayMenu() {
        while (true) {
            try {
                ConsoleUtility.printHeader("PHARMACIST MENU");
                System.out.println("{1} View Appointment Outcome Record");
                System.out.println("{2} Update Prescription Status");
                System.out.println("{3} View Medication Inventory");
                System.out.println("{4} Submit Replenishment Request");
                System.out.println("{5} Logout");
                System.out.print("Enter your choice: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1: viewAppointmentOutcomeRecord(); break;
                    case 2: updatePrescriptionStatus(); break;
                    case 3: viewMedicationInventory(); break;
                    case 4: submitReplenishmentRequest(); break;
                    case 5: System.out.println("Logging out and returning to home screen..."); return;
                    default: System.out.println("Invalid choice. Please enter a number between 1 and 5.");
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
     * [OPTION 1] Views the appointment outcome record.
     */
    private void viewAppointmentOutcomeRecord() {
        // TODO: Implement logic to view appointment outcome record
    }

    /**
     * [OPTION 2] Updates the prescription status.
     */
    private void updatePrescriptionStatus() {
        // TODO: Implement logic to update prescription status
    }

    /**
     * [OPTION 3] Views the medication inventory.
     */
    private void viewMedicationInventory() {
        // TODO: Implement logic to view medication inventory
    }

    /**
     * [OPTION 4] Submits a replenishment request.
     */
    private void submitReplenishmentRequest() {
        // TODO: Implement logic to submit replenishment request
    }
}
