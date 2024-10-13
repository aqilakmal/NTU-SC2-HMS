package boundary;

import controller.*;
import entity.*;
import utility.*;
import java.util.*;

/**
 * Interface for doctor functionalities in the Hospital Management System.
 */
public class DoctorMenu {
    
    private DoctorController doctorController;
    private Scanner scanner;

    /**
     * Constructor for DoctorMenu.
     * 
     * @param doctorController The DoctorController instance
     */
    public DoctorMenu(DoctorController doctorController) {
        this.doctorController = doctorController;
        this.scanner = new Scanner(System.in);
    }

    /**
     * [MAIN MENU] Displays the menu options available to the doctor.
     */
    public void displayMenu() {
        while (true) {
            try {
                ConsoleUtility.printHeader("DOCTOR MENU");
                System.out.println("{1} View Patient Medical Records");
                System.out.println("{2} Update Patient Medical Records");
                System.out.println("{3} View Personal Schedule");
                System.out.println("{4} Set Availability for Appointments");
                System.out.println("{5} Accept or Decline Appointment Requests");
                System.out.println("{6} View Upcoming Appointments");
                System.out.println("{7} Record Appointment Outcome");
                System.out.println("{8} Logout");
                System.out.print("Enter your choice: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1: viewPatientMedicalRecords(); break;
                    case 2: updatePatientMedicalRecords(); break;
                    case 3: viewPersonalSchedule(); break;
                    case 4: setAvailabilityForAppointments(); break;
                    case 5: manageAppointmentRequests(); break;
                    case 6: viewUpcomingAppointments(); break;
                    case 7: recordAppointmentOutcome(); break;
                    case 8: System.out.println("Logging out and returning to home screen..."); return;
                    default: System.out.println("Invalid choice. Please enter a number between 1 and 8.");
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
     * [OPTION 1] Views patient medical records.
     */
    private void viewPatientMedicalRecords() {
        ConsoleUtility.printHeader("VIEW PATIENT MEDICAL RECORDS");
        // TODO: Implement logic to view patient medical records
    }

    /**
     * [OPTION 2] Updates patient medical records.
     */
    private void updatePatientMedicalRecords() {
        try {
            ConsoleUtility.printHeader("UPDATE PATIENT MEDICAL RECORDS", false);
            List<Patient> patients = doctorController.getPatientsUnderCare();

            if (patients.isEmpty()) {
                System.out.println("\nYou currently have no patients under your care.");
                return;
            }

            // Display the list of patients under the doctor's care
            System.out.println();
            displayPatientList(patients);

            String patientID = ConsoleUtility.validateInput("\nEnter the Patient ID to update (or press Enter to go back): ", 
                input -> input.isEmpty() || doctorController.isValidPatientID(input));

            if (patientID.isEmpty()) {
                return;
            }

            List<History> medicalHistory = doctorController.getPatientMedicalHistory(patientID);

            if (medicalHistory.isEmpty()) {
                System.out.println("This patient has no medical history records.");
                if (ConsoleUtility.getConfirmation("Would you like to add a new record?")) {
                    addNewMedicalRecord(patientID);
                }
                return;
            }

            displayMedicalHistory(medicalHistory);

            String historyID = ConsoleUtility.validateInput("\nEnter the History ID to update (or press Enter to go back): ", 
                input -> input.isEmpty() || doctorController.isValidHistoryID(input));

            if (historyID.isEmpty()) {
                return;
            }

            History selectedHistory = doctorController.getHistoryByID(historyID);
            if (selectedHistory == null) {
                System.out.println("Error: Invalid History ID. Please try again.");
                return;
            }

            updateMedicalRecord(selectedHistory);
        } catch (Exception e) {
            System.err.println("An error occurred while updating medical records: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Displays the list of patients under the doctor's care.
     * @param patients The list of patients to display
     */
    private void displayPatientList(List<Patient> patients) {
        LinkedHashMap<String, TableBuilder.ColumnMapping> columnMapping = new LinkedHashMap<>();
        columnMapping.put("userID", new TableBuilder.ColumnMapping("Patient ID", null));
        columnMapping.put("name", new TableBuilder.ColumnMapping("Name", null));
        columnMapping.put("dateOfBirth", new TableBuilder.ColumnMapping("Date of Birth", null));
        columnMapping.put("gender", new TableBuilder.ColumnMapping("Gender", null));

        TableBuilder.createTable("Patients Under Care", patients, columnMapping, 20);
    }

    /**
     * Displays the medical history for a patient.
     * @param medicalHistory The list of medical history records to display
     */
    private void displayMedicalHistory(List<History> medicalHistory) {
        LinkedHashMap<String, TableBuilder.ColumnMapping> columnMapping = new LinkedHashMap<>();
        columnMapping.put("historyID", new TableBuilder.ColumnMapping("History ID", null));
        columnMapping.put("diagnosisDate", new TableBuilder.ColumnMapping("Date", null));
        columnMapping.put("diagnosis", new TableBuilder.ColumnMapping("Diagnosis", null));
        columnMapping.put("treatment", new TableBuilder.ColumnMapping("Treatment", null));

        TableBuilder.createTable("Medical History", medicalHistory, columnMapping, 20);
    }

    /**
     * Handles updating a specific medical record.
     * @param history The History object to update
     */
    private void updateMedicalRecord(History history) {
        System.out.println("\nCurrent Diagnosis: " + history.getDiagnosis());
        String newDiagnosis = ConsoleUtility.validateInput("Enter new diagnosis (or press Enter to keep current): ", 
            input -> input.isEmpty() || !input.trim().isEmpty());

        System.out.println("Current Treatment: " + history.getTreatment());
        String newTreatment = ConsoleUtility.validateInput("Enter new treatment (or press Enter to keep current): ", 
            input -> input.isEmpty() || !input.trim().isEmpty());

        if (newDiagnosis.isEmpty() && newTreatment.isEmpty()) {
            System.out.println("No changes made to the medical record.");
            return;
        }

        try {
            boolean updated = doctorController.updateMedicalHistory(history.getHistoryID(), 
                                                                    newDiagnosis.isEmpty() ? null : newDiagnosis, 
                                                                    newTreatment.isEmpty() ? null : newTreatment);

            if (updated) {
                System.out.println("Medical record updated successfully.");
            } else {
                System.out.println("Failed to update medical record. Please try again.");
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Error updating medical record: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred while updating the medical record: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles adding a new medical record for a patient.
     * @param patientID The ID of the patient
     */
    private void addNewMedicalRecord(String patientID) {
        try {
            String diagnosis = ConsoleUtility.validateInput("Enter diagnosis: ", input -> !input.trim().isEmpty());
            String treatment = ConsoleUtility.validateInput("Enter treatment: ", input -> !input.trim().isEmpty());

            boolean added = doctorController.addMedicalHistory(patientID, diagnosis, treatment);

            if (added) {
                System.out.println("New medical record added successfully.");
            } else {
                System.out.println("Failed to add new medical record. Please try again.");
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Error adding new medical record: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred while adding the new medical record: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * [OPTION 3] Views the doctor's personal schedule.
     */
    private void viewPersonalSchedule() {
        ConsoleUtility.printHeader("VIEW PERSONAL SCHEDULE");
        // TODO: Implement logic to view personal schedule
    }

    /**
     * [OPTION 4] Sets availability for appointments.
     */
    private void setAvailabilityForAppointments() {
        ConsoleUtility.printHeader("SET AVAILABILITY FOR APPOINTMENTS");
        // TODO: Implement logic to set availability for appointments
    }

    /**
     * [OPTION 5] Manages appointment requests.
     */
    private void manageAppointmentRequests() {
        ConsoleUtility.printHeader("MANAGE APPOINTMENT REQUESTS");
        // TODO: Implement logic to accept or decline appointment requests
    }

    /**
     * [OPTION 6] Views upcoming appointments.
     */
    private void viewUpcomingAppointments() {
        ConsoleUtility.printHeader("VIEW UPCOMING APPOINTMENTS");
        // TODO: Implement logic to view upcoming appointments
    }

    /**
     * [OPTION 7] Records appointment outcome.
     */
    private void recordAppointmentOutcome() {
        ConsoleUtility.printHeader("RECORD APPOINTMENT OUTCOME");
        // TODO: Implement logic to record appointment outcome
    }
}
