package boundary;

import java.util.InputMismatchException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Collections;

import controller.PharmacistController;
import entity.Appointment;
import entity.Medication;
import entity.Outcome;
import entity.Prescription;
import entity.Slot;
import entity.User;
import utility.ConsoleUtility;
import utility.TableBuilder;

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
                    case 1:
                        viewAppointmentOutcomeRecord();
                        break;
                    case 2:
                        updatePrescriptionStatus();
                        break;
                    case 3:
                        viewMedicationInventory();
                        break;
                    case 4:
                        submitReplenishmentRequest();
                        break;
                    case 5:
                        System.out.println("Logging out and returning to home screen...");
                        return;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 5.");
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
    public void viewAppointmentOutcomeRecord() {
        try {
            while (true) {
                ConsoleUtility.printHeader("VIEW APPOINTMENT OUTCOME RECORD");

                // Get all completed appointments
                List<Appointment> completedAppointments = pharmacistController.getCompletedAppointments();
                if (completedAppointments.isEmpty()) {
                    System.out.println("No completed appointments found.");
                    return;
                }

                // Display completed appointments
                displayAppointmentList(completedAppointments, "COMPLETED APPOINTMENTS");

                // Get appointment ID from user
                System.out.println("");
                String appointmentID = ConsoleUtility.validateInput(
                    "Enter Appointment ID to view details (or press Enter to go back): ",
                    input -> {
                        boolean exists = completedAppointments.stream()
                            .anyMatch(a -> a.getAppointmentID().equals(input));
                        if (!exists && !input.isEmpty()) {
                            System.out.println("Invalid ID, appointment does not exist.");
                        }
                        return exists || input.isEmpty();
                    });

                if (appointmentID.isEmpty()) {
                    System.out.println("Returning to Main Menu...");
                    return;
                }

                // Get appointment details
                Map<String, Object> appointmentDetails = pharmacistController.getAppointmentDetails(appointmentID);
                if (appointmentDetails == null) {
                    System.out.println("Appointment not found or slot information is missing.");
                    continue;
                }

                Appointment selectedAppointment = (Appointment) appointmentDetails.get("appointment");
                Slot slot = pharmacistController.getSlotByID(selectedAppointment.getSlotID());
                User patient = pharmacistController.getPatientByID(selectedAppointment.getPatientID());
                User doctor = pharmacistController.getDoctorByID(selectedAppointment.getDoctorID());

                // Display detailed appointment information
                System.out.println("\nDETAILED APPOINTMENT INFORMATION");
                System.out.println("-".repeat(50));
                System.out.printf("Appointment ID: %s%n", selectedAppointment.getAppointmentID());
                System.out.printf("Status: %s%n", selectedAppointment.getStatus());
                
                System.out.println("\nPatient Information:");
                System.out.printf("Patient ID: %s%n", patient.getUserID());
                System.out.printf("Patient Name: %s%n", patient.getName());
                System.out.printf("Contact: %s%n", patient.getContactNumber());
                
                System.out.println("\nDoctor Information:");
                System.out.printf("Doctor ID: %s%n", doctor.getUserID());
                System.out.printf("Doctor Name: %s%n", doctor.getName());
                System.out.printf("Contact: %s%n", doctor.getContactNumber());
                
                System.out.println("\nSchedule Information:");
                System.out.printf("Date: %s%n", slot.getDate());
                System.out.printf("Time: %s - %s%n", slot.getStartTime(), slot.getEndTime());
                System.out.printf("Slot Status: %s%n", slot.getStatus());
                System.out.println("-".repeat(50));

                // Display outcome information if available
                String outcomeID = selectedAppointment.getOutcomeID();
                if (outcomeID != null && !outcomeID.isEmpty()) {
                    Outcome outcome = pharmacistController.getOutcomeByID(outcomeID);
                    if (outcome != null) {
                        System.out.println("\nOUTCOME INFORMATION");
                        System.out.println("-".repeat(50));
                        System.out.printf("Outcome ID: %s%n", outcome.getOutcomeID());
                        System.out.printf("Service Provided: %s%n", outcome.getServiceProvided());
                        System.out.println("\nConsultation Notes:");
                        System.out.println(outcome.getConsultationNotes());

                        // Display prescription information if available
                        String prescriptionID = outcome.getPrescriptionID();
                        if (prescriptionID != null && !prescriptionID.isEmpty() && !prescriptionID.equals("NIL")) {
                            Prescription prescription = pharmacistController.getPrescriptionByID(prescriptionID);
                            if (prescription != null) {
                                System.out.println("\nPRESCRIPTION DETAILS");
                                System.out.println("-".repeat(50));
                                System.out.printf("Prescription ID: %s%n", prescription.getPrescriptionID());
                                
                                // Get and display medication details
                                String[] medicationIDs = prescription.getMedicationID().split(";");
                                System.out.println("\nPrescribed Medications:");
                                for (String medicationID : medicationIDs) {
                                    Medication medication = pharmacistController.getMedicationByID(medicationID);
                                    if (medication != null) {
                                        System.out.printf("- %s (ID: %s)%n", medication.getName(), medicationID);
                                        System.out.printf("  Current Stock Level: %d%n", medication.getStockLevel());
                                    }
                                }

                                System.out.printf("Status: %s%n", prescription.getStatus());
                                System.out.printf("Notes: %s%n", prescription.getNotes());
                            }
                        } else {
                            System.out.println("No prescription was issued for this appointment.");
                        }
                    }
                } else {
                    System.out.println("No outcome record available for this appointment.");
                }
                System.out.println("-".repeat(50));
            }
        } catch (Exception e) {
            System.err.println("An error occurred while viewing appointment outcome: " + e.getMessage());
        }
    }

    /**
     * Displays the list of appointments.
     * 
     * @param appointments The list of appointments to display
     * @param title The title for the table
     */
    private void displayAppointmentList(List<Appointment> appointments, String title) {
        LinkedHashMap<String, TableBuilder.ColumnMapping> columnMapping = new LinkedHashMap<>();
        
        // Appointment ID column
        columnMapping.put("appointmentID", new TableBuilder.ColumnMapping("Appointment ID", null));
        
        // Patient column with name and ID
        columnMapping.put("patientID", new TableBuilder.ColumnMapping("Patient (ID)", 
                value -> {
                    String patientId = value.toString();
                    User patient = pharmacistController.getPatientByID(patientId);
                    return patient != null ? patient.getName() + " (" + patientId + ")" : "Unknown";
                }));
        
        // Doctor column with name and ID
        columnMapping.put("doctorID", new TableBuilder.ColumnMapping("Doctor (ID)",
                value -> {
                    String doctorId = value.toString();
                    User doctor = pharmacistController.getDoctorByID(doctorId);
                    return doctor != null ? doctor.getName() + " (" + doctorId + ")" : "Unknown";
                }));

        // Time column combining date and time from slot
        columnMapping.put("slotID", new TableBuilder.ColumnMapping("Schedule",
                value -> {
                    String slotId = value.toString();
                    Slot slot = pharmacistController.getSlotByID(slotId);
                    if (slot != null) {
                        return slot.getDate() + " " + slot.getStartTime() + "-" + slot.getEndTime();
                    }
                    return "Unknown";
                }));

        columnMapping.put("status", new TableBuilder.ColumnMapping("Status", null));

        TableBuilder.createTable(title, appointments, columnMapping, 25);
    }

    /**
     * [OPTION 2] Updates the prescription status.
     */
    private void updatePrescriptionStatus() {
        try {
            while (true) {
                ConsoleUtility.printHeader("UPDATE PRESCRIPTION STATUS");

                // Get pending prescriptions
                List<Prescription> pendingPrescriptions = pharmacistController.getPendingPrescriptions();
                if (pendingPrescriptions.isEmpty()) {
                    System.out.println("No pending prescriptions found.");
                    return;
                }

                // Display pending prescriptions
                displayPrescriptions(pendingPrescriptions, "PENDING PRESCRIPTIONS");

                // Get prescription ID from user
                System.out.println("");
                String prescriptionID = ConsoleUtility.validateInput(
                    "Enter Prescription ID to update (or press Enter to go back): ",
                    input -> {
                        boolean exists = pendingPrescriptions.stream()
                            .anyMatch(p -> p.getPrescriptionID().equals(input));
                        if (!exists && !input.isEmpty()) {
                            System.out.println("Invalid ID, prescription does not exist.");
                        }
                        return exists || input.isEmpty();
                    });

                if (prescriptionID.isEmpty()) {
                    System.out.println("Returning to Main Menu...");
                    return;
                }

                // Display current prescription details
                Prescription selectedPrescription = pharmacistController.getPrescriptionByID(prescriptionID);
                System.out.println("");
                displayPrescriptions(selectedPrescription, "SELECTED PRESCRIPTION");

                // Get medications for the prescription
                List<Medication> medications = pharmacistController.getMedicationsForPrescription(prescriptionID);
                displayMedication(medications, "MEDICATION DETAILS");

                // Get confirmation
                if (!ConsoleUtility.getConfirmation("\nDo you want to mark this prescription as dispensed?")) {
                    System.out.println("Operation cancelled.");
                    continue;
                }

                // Update status
                boolean success = pharmacistController.updatePrescriptionStatus(prescriptionID, 
                    Prescription.PrescriptionStatus.DISPENSED);
                
                if (success) {
                    System.out.println("\nPrescription status updated successfully.");
                    // Display updated prescription and medication
                    displayPrescriptions(pharmacistController.getPrescriptionByID(prescriptionID), 
                        "UPDATED PRESCRIPTION");
                    displayMedication(medications, "UPDATED MEDICATION INVENTORY");
                } else {
                    System.out.println("Failed to update prescription status.");
                }
            }
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    /**
     * [OPTION 3] Views the medication inventory.
     */
    public void viewMedicationInventory() {
        
        ConsoleUtility.printHeader("MEDICATION INVENTORY");

        // Get medications through controller
        List<Medication> medications = pharmacistController.getAllMedications();

        if (medications.isEmpty()) {
            System.out.println("No medications found in inventory.");
        } else {
            displayMedicationList(medications);
            System.out.println("Total Medications: " + medications.size());
        }
    }

    /**
     * [OPTION 4] Submits a replenishment request.
     */
    private void submitReplenishmentRequest() {
        try {
            ConsoleUtility.printHeader("SUBMIT REPLENISHMENT REQUEST");

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
                System.out
                        .println("Replenishment request submitted successfully. Waiting for administrator's approval.");
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
     * Displays a list of prescriptions in a table format.
     * 
     * @param prescriptions List of prescriptions to display
     * @param title The title for the table
     */
    private void displayPrescriptions(List<Prescription> prescriptions, String title) {
        LinkedHashMap<String, TableBuilder.ColumnMapping> columnMapping = new LinkedHashMap<>();
        columnMapping.put("prescriptionID", new TableBuilder.ColumnMapping("Prescription ID", null));
        columnMapping.put("appointmentID", new TableBuilder.ColumnMapping("Appointment ID", null));
        columnMapping.put("medicationID", new TableBuilder.ColumnMapping("Medication", 
            value -> {
                String medicationId = value.toString();
                Medication medication = pharmacistController.getMedicationByID(medicationId);
                return medication != null ? medication.getName() + " (" + medicationId + ")" : medicationId;
            }));
        columnMapping.put("quantity", new TableBuilder.ColumnMapping("Quantity", null));
        columnMapping.put("status", new TableBuilder.ColumnMapping("Status", null));
        columnMapping.put("notes", new TableBuilder.ColumnMapping("Notes", null));

        TableBuilder.createTable(title, prescriptions, columnMapping, 20);
    }

    /**
     * Displays a single prescription in a table format.
     * 
     * @param prescription The prescription to display
     * @param title The title for the table
     */
    private void displayPrescriptions(Prescription prescription, String title) {
        List<Prescription> prescriptionList = Collections.singletonList(prescription);
        displayPrescriptions(prescriptionList, title);
    }

    /**
     * Displays a list of medications in a table format.
     * 
     * @param medications List of medications to display
     * @param title The title for the table
     */
    private void displayMedication(List<Medication> medications, String title) {
        LinkedHashMap<String, TableBuilder.ColumnMapping> columnMapping = new LinkedHashMap<>();
        columnMapping.put("medicationID", new TableBuilder.ColumnMapping("Medication ID", null));
        columnMapping.put("name", new TableBuilder.ColumnMapping("Name", null));
        columnMapping.put("stockLevel", new TableBuilder.ColumnMapping("Stock Level", null));
        columnMapping.put("lowStockAlertLevel", new TableBuilder.ColumnMapping("Low Stock Alert Level", null));

        TableBuilder.createTable(title, medications, columnMapping, 20);
    }
}
