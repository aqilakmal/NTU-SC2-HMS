package boundary;

import controller.*;
import entity.*;
import utility.*;
import java.util.Scanner;
import java.util.List;
import java.util.InputMismatchException;
import java.util.LinkedHashMap;

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
        try {
            ConsoleUtility.printHeader("SUBMIT REPLENISHMENT REQUEST", false);

            // Step 1: Display list of medications
            List<Medication> medications = pharmacistController.getAllMedications();
            if (medications.isEmpty()) {
                System.out.println("No medications are currently available in the system.");
                return;
            }

            System.out.println("");
            displayMedicationList(medications);

            // Step 2: Prompt user to select a medication
            String selectedMedicationID = ConsoleUtility.validateInput("\nEnter the Medication ID to replenish: ",
                    input -> medications.stream().anyMatch(m -> m.getMedicationID().equals(input)));

            Medication selectedMedication = pharmacistController.getMedicationByID(selectedMedicationID);

            if (selectedMedication == null) {
                System.out.println("Invalid Medication ID. Please try again.");
                return;
            }

            // Step 3: Prompt user to enter quantity
            int quantity = Integer.parseInt(ConsoleUtility.validateInput("Enter the quantity to replenish: ",
                    input -> {
                        try {
                            int value = Integer.parseInt(input);
                            return value > 0;
                        } catch (NumberFormatException e) {
                            return false;
                        }
                    }));

            // Step 4: Verify replenishment request information
            System.out.println("\nReplenishment Request Details:");
            System.out.println("Medication: " + selectedMedication.getName());
            System.out.println("Current Stock: " + selectedMedication.getStockLevel());
            System.out.println("Requested Quantity: " + quantity);

            if (!ConsoleUtility.getConfirmation("Do you want to submit this replenishment request?")) {
                System.out.println("Replenishment request submission cancelled.");
                return;
            }

            // Step 5: Submit the replenishment request
            boolean success = pharmacistController.submitReplenishmentRequest(selectedMedicationID, quantity);
            if (success) {
                System.out.println("Replenishment request submitted successfully. Waiting for administrator's approval.");
            } else {
                System.out.println("Failed to submit the replenishment request. Please try again later.");
            }

        } catch (Exception e) {
            System.err.println("An error occurred while submitting the replenishment request: " + e.getMessage());
        }
    }

    /**
     * Displays the list of medications.
     * 
     * @param medications The list of medications to display
     */
    private void displayMedicationList(List<Medication> medications) {
        LinkedHashMap<String, TableBuilder.ColumnMapping> columnMapping = new LinkedHashMap<>();
        columnMapping.put("medicationID", new TableBuilder.ColumnMapping("Medication ID", null));
        columnMapping.put("name", new TableBuilder.ColumnMapping("Name", null));
        columnMapping.put("stockLevel", new TableBuilder.ColumnMapping("Stock Level", null));
        columnMapping.put("lowStockAlertLevel", new TableBuilder.ColumnMapping("Low Stock Alert Level", null));

        TableBuilder.createTable("Medication Inventory", medications, columnMapping, 20);
    }
}
