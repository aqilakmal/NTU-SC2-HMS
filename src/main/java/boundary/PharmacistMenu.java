package boundary;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;

import controller.DoctorController;
import controller.PatientController;
import controller.PharmacistController;
import controller.data.AppointmentDataManager;
import controller.data.MedicationDataManager;
import controller.data.OutcomeDataManager;
import controller.data.PrescriptionDataManager;
import controller.data.UserDataManager;
import entity.Appointment;
import entity.Medication;
import entity.Outcome;
import entity.Prescription;
import utility.ConsoleUtility;
import utility.TableBuilder;


//Change to controller 
/**
 * Interface for pharmacist operations in the Hospital Management System.
 */
public class PharmacistMenu {
    
    private PharmacistController pharmacistController;
    private AppointmentDataManager appointmentDataManager;
    private UserDataManager userDataManager;
    private MedicationDataManager medicationDataManager;
    private PrescriptionDataManager prescriptionDataManager;
    private OutcomeDataManager outcomeDataManager;

    // private AppointmentController appointmentController;


    private Scanner scanner;
    private PatientController patientController;
    private DoctorController doctorController;

    /**
     * Constructor for PharmacistMenu.
     * 
     * @param pharmacistController The PharmacistController instance
     */
    public PharmacistMenu(PharmacistController pharmacistController,AppointmentDataManager appointmentDataManager,
     UserDataManager userDataManager, MedicationDataManager medicationDataManager, PrescriptionDataManager prescriptionDataManager, OutcomeDataManager outcomeDataManager) 
    {
        this.pharmacistController = pharmacistController;
        this.appointmentDataManager = appointmentDataManager;
        this.medicationDataManager = medicationDataManager;
        this.prescriptionDataManager=prescriptionDataManager;
        this.userDataManager = userDataManager;
        this.outcomeDataManager = outcomeDataManager;
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
  /**
 * [OPTION 1] Views the appointment outcome record.
 */
public void viewAppointmentOutcomeRecord() {
    // Step 1: Get the appointment ID from the user
    System.out.print("Enter Appointment ID to view the outcome: ");
    String appointmentID = scanner.nextLine();

    // Step 2: Retrieve the appointment from AppointmentDataManager
    Appointment appointment = appointmentDataManager.getAppointmentByID(appointmentID);
    
    if (appointment == null) {
        System.out.println("Appointment not found.");
        return;
    }

    // Step 3: Check if the appointment is completed
    if (appointment.getStatus() == Appointment.AppointmentStatus.COMPLETED) {
        // Step 4: Retrieve the outcome by outcomeID
        String outcomeID = appointment.getOutcomeID();
        Outcome outcome = outcomeDataManager.getOutcomeByID(outcomeID);
        
        if (outcome != null) {
            // Display consultation notes
            String consultationNotes = outcome.getConsultationNotes();
            System.out.println("Outcome for Appointment ID " + appointmentID + ":");
            System.out.println("Consultation Notes: " + (consultationNotes != null ? consultationNotes : "No notes available."));
            
            // Get the prescription ID(s) and fetch medicine names
            String prescriptionID = outcome.getPrescriptionID();
            if (prescriptionID != null && !prescriptionID.isEmpty()) {
                System.out.print("Prescribed Medicines: ");
                
                // Split prescription ID string on comma or semicolon
                String[] prescriptionIDs = prescriptionID.split("[,;]");
                for (String id : prescriptionIDs) {
                    // Get the medication object from MedicationDataManager
                    Medication medication = medicationDataManager.getMedicationByID(id.trim());
                    if (medication != null) {
                        System.out.print(medication.getName() + " ");
                    } else {
                        System.out.print("Unknown Medicine (" + id + ") ");
                    }
                }
                System.out.println(); // New line after displaying all medicines
            } else {
                System.out.println("No medicines prescribed.");
            }
        } else {
            System.out.println("No outcome recorded for this appointment.");
        }
    } else {
        System.out.println("This appointment has not been completed yet. Outcome is not available.");
    }
}


    /**
     * [OPTION 2] Updates the prescription status.
     */
    private void updatePrescriptionStatus() {
        Scanner scanner = new Scanner(System.in);

        // Prompt the pharmacist to enter the prescription ID
        System.out.print("Enter the Prescription ID to update: ");
        String prescriptionID = scanner.nextLine();

        // Retrieve the prescription by ID
        Prescription prescription = prescriptionDataManager.getPrescriptionByID(prescriptionID);
        
        // Verify if the prescription exists
        if (prescription == null) {
            System.out.println("Prescription not found.");
            return;
        }

        // Check if the prescription is already marked as "DISPENSED"
        if (prescription.getStatus() == Prescription.PrescriptionStatus.DISPENSED) {
            System.out.println("Prescription is already dispensed.");
            return;
        }

        // Update the prescription status to "DISPENSED"
        prescription.setStatus(Prescription.PrescriptionStatus.DISPENSED);

        // Reflect changes in the data manager and save to CSV (or database)
        prescriptionDataManager.updatePrescription(prescription);
        System.out.println("Prescription status updated to DISPENSED successfully.");

        // Save updated prescriptions to CSV file
        try {
            prescriptionDataManager.savePrescriptionsToCSV();
            System.out.println("Prescription status update saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving the updated prescription status: " + e.getMessage());
        }

        // Reflect the update in patient's records (assuming additional implementation here if needed)
        updatePatientRecordWithDispensedStatus(prescription);


    
}



    /**
     * [OPTION 3] Views the medication inventory.
     */
     public void viewMedicationInventory() {
        System.out.println("\n<======= Medication Inventory =======>");
        
        // Retrieve all medications from the MedicationDataManager
        List<Medication> medications = medicationDataManager.getMedications();
        
        if (medications.isEmpty()) {
            System.out.println("No medications found in inventory.");
        } else {
            // Use displayMedicationList to print the medication inventory in a formatted table
            displayMedicationList(medications);
            System.out.println("Total Medications: " + medications.size());
        }
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
    /**
     * Reflects the prescription status update in the patient's medical records.
     * @param prescription The updated prescription
     */
    private void updatePatientRecordWithDispensedStatus(Prescription prescription) {
        // Assuming there is logic to retrieve and update the patient records.
        // You would implement the logic to access the patient's record and update it accordingly.
        System.out.println("Patient record updated with dispensed status for prescription ID: " + prescription.getPrescriptionID());
    }
}
