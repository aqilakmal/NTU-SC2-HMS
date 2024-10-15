package boundary;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;

import controller.DoctorController;
import entity.Appointment;
import entity.History;
import entity.Medication;
import entity.Patient;
import entity.Slot;
import utility.ConsoleUtility;
import utility.TableBuilder;

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
                System.out.println("{4} Manage Availability for Appointments (Add, Remove, Update)");
                System.out.println("{5} Accept or Decline Appointment Requests");
                System.out.println("{6} View Upcoming Appointments");
                System.out.println("{7} Record Appointment Outcome");
                System.out.println("{8} Logout");
                System.out.print("Enter your choice: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        viewPatientMedicalRecords();
                        break;
                    case 2:
                        updatePatientMedicalRecords();
                        break;
                    case 3:
                        viewPersonalSchedule();
                        break;
                    case 4:
                        manageAvailabilityForAppointments();
                        break;
                    case 5:
                        manageAppointmentRequests();
                        break;
                    case 6:
                        viewUpcomingAppointments();
                        break;
                    case 7:
                        recordAppointmentOutcome();
                        break;
                    case 8:
                        System.out.println("Logging out and returning to home screen...");
                        return;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 8.");
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
        try {

            List<Patient> patients = doctorController.getPatientsUnderCare();
            if (patients.isEmpty()) {
                System.out.println("\nYou currently have no patients under your care.");
                return;
            }
            // Display the list of patients under the doctor's care
            ConsoleUtility.printHeader("VIEW PATIENT MEDICAL RECORDS");
            System.out.println();
            displayPatientList(patients);

            String patientID = ConsoleUtility.validateInput("\nEnter the Patient ID to view their medical records (or press Enter to go back): ",
                    input -> input.isEmpty() || doctorController.isValidPatientID(input));

            if (patientID.isEmpty()) {
                return;
            }

            List<History> medicalHistory = doctorController.getPatientMedicalHistory(patientID);

            if (medicalHistory.isEmpty()) {
                System.out.println("This patient has no medical history records.");
                return;
            }

            displayMedicalHistory(medicalHistory, "Medical History");

        } catch (Exception e) {
            System.err.println("An error occurred while viewing medical records: " + e.getMessage());
            e.printStackTrace();
        }

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

            displayMedicalHistory(medicalHistory, "Medical History");

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
     *
     * @param patients The list of patients to display
     */
    //contactNumber,emailAddress,bloodType
    private void displayPatientList(List<Patient> patients) {
        LinkedHashMap<String, TableBuilder.ColumnMapping> columnMapping = new LinkedHashMap<>();
        columnMapping.put("userID", new TableBuilder.ColumnMapping("Patient ID", null));
        columnMapping.put("name", new TableBuilder.ColumnMapping("Name", null));
        columnMapping.put("dateOfBirth", new TableBuilder.ColumnMapping("Date of Birth", null));
        columnMapping.put("gender", new TableBuilder.ColumnMapping("Gender", null));
        columnMapping.put("contactNumber", new TableBuilder.ColumnMapping("Contact Number", null));
        columnMapping.put("emailAddress", new TableBuilder.ColumnMapping("Email Address", null));
        columnMapping.put("bloodType", new TableBuilder.ColumnMapping("Blood Type", null));

        TableBuilder.createTable("Patients Under Care", patients, columnMapping, 20);
    }

    /**
     * Displays the medical history for a patient.
     *
     * @param medicalHistory The list of medical history records to display
     */
    private void displayMedicalHistory(List<History> medicalHistory, String name) {
        LinkedHashMap<String, TableBuilder.ColumnMapping> columnMapping = new LinkedHashMap<>();
        columnMapping.put("historyID", new TableBuilder.ColumnMapping("History ID", null));
        columnMapping.put("diagnosisDate", new TableBuilder.ColumnMapping("Date", null));
        columnMapping.put("diagnosis", new TableBuilder.ColumnMapping("Diagnosis", null));
        columnMapping.put("treatment", new TableBuilder.ColumnMapping("Treatment", null));

        TableBuilder.createTable(name, medicalHistory, columnMapping, 20);
    }

    /**
     * Handles updating a specific medical record.
     *
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
     *
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
        try {
            List<Slot> slots = doctorController.getAllSlotsForDoctor();
            ConsoleUtility.printHeader("VIEW PERSONAL SCHEDULE");
            if (slots.isEmpty()) {
                System.out.println("\nYou currently have no slots.");
                return;
            }
            displaySlots(slots, "CURRENT SLOTS");

        } catch (Exception e) {
            System.err.println("An error occurred while viewing avaialble slots: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Displays the list of slots for the current doctor.
     *
     * @param slots The list of slots to display
     * @param name The name of the table
     */
    private void displaySlots(List<Slot> slots, String name) {
        LinkedHashMap<String, TableBuilder.ColumnMapping> columnMapping = new LinkedHashMap<>();
        columnMapping.put("slotID", new TableBuilder.ColumnMapping("Slot ID", null));
        columnMapping.put("date", new TableBuilder.ColumnMapping("Date", null));
        columnMapping.put("startTime", new TableBuilder.ColumnMapping("Start Time", null));
        columnMapping.put("endTime", new TableBuilder.ColumnMapping("End Time", null));
        columnMapping.put("status", new TableBuilder.ColumnMapping("Status", null));

        TableBuilder.createTable(name, slots, columnMapping, 20);
    }

    /**
     * [OPTION 4] Sets availability for appointments.
     */
    private void manageAvailabilityForAppointments() {
        try {
            ConsoleUtility.printHeader("MANAGE AVAILABILITY FOR APPOINTMENTS");
            List<Slot> slots = doctorController.getAllSlotsForDoctor();
            List<Slot> availableSlots = doctorController.getAvailableSlotsForDoctor();
            Boolean availableSlotsBoolean = true;

            //Display all slots and then available slots
            if (slots.isEmpty()) {
                System.out.println("\nYou currently have no slots.");
            } else {
                displaySlots(slots, "CURRENT SLOTS");
                if (availableSlots.isEmpty()) {
                    availableSlotsBoolean = false;
                    System.out.println("\nYou currently have no available slots.");
                } else {
                    displaySlots(availableSlots, "AVAILABLE SLOTS");
                }
            }

            ConsoleUtility.printHeader("AVIALABLE SLOT FUNCTIONS");
            System.out.println("{1} Update Avaiable Slot");
            System.out.println("{2} Add Available Slot");
            System.out.println("{3} Remove Available Slot");
            System.out.println("{4} Return to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    if (availableSlotsBoolean) {
                        updateSlot();
                    } else {
                        System.out.println("\nYou have no available slots to update");
                    }
                    break;
                case 2:
                    addNewSlot();
                    break;
                case 3:
                    if (availableSlotsBoolean) {
                        removeSlot();
                    } else {
                        System.out.println("\nYou have no available slots to remove.");
                    }
                    break;
                case 4:
                    System.out.println("Returning to Main Menu...");
                    return;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 4.");
            }

        } catch (Exception e) {
            System.err.println("An error occurred while updating avaialble slots: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles update slots for doctor.
     */
    private void updateSlot() {
        try {
            String slotID = ConsoleUtility.validateInput("\nEnter an available Slot ID to update (or press Enter to go back): ",
                    input -> input.isEmpty() || doctorController.isValidAvailableSlotID(input));

            Slot selectedSlot = doctorController.getSlotByID(slotID);
            if (selectedSlot == null) {
                System.out.println("Error: Invalid Slot ID. Please try again.");
                return;
            }

            if (slotID.isEmpty()) {
                return;
            }
            System.out.println("\nCurrent Date: " + selectedSlot.getDate());
            String dateInput = ConsoleUtility.validateInput(
                    "Enter date (YYYY-MM-DD) or press Enter to use the current value (" + selectedSlot.getDate() + "): ",
                    input -> {
                        if (input.isEmpty()) {
                            return true; // Accept empty input to use the default value
                        }
                        try {
                            LocalDate.parse(input); // Try to parse the date
                            return true;
                        } catch (DateTimeParseException e) {
                            System.out.println("Invalid date format. Please use YYYY-MM-DD.");
                            return false;
                        }
                    }
            );

            LocalDate date = dateInput.isEmpty() ? selectedSlot.getDate() : LocalDate.parse(dateInput);

            System.out.println("\nCurrent Start Time: " + selectedSlot.getStartTime());
            String startTimeInput = ConsoleUtility.validateInput(
                    "Enter start time (HH:MM) or press Enter to use the current value (" + selectedSlot.getStartTime() + "): ",
                    input -> input.isEmpty() || ConsoleUtility.isValidTime(input)
            );

            LocalTime startTime = startTimeInput.isEmpty() ? selectedSlot.getStartTime() : LocalTime.parse(startTimeInput);

            System.out.println("\nCurrent End Time: " + selectedSlot.getEndTime());
            String endTimeInput = ConsoleUtility.validateInput(
                    "Enter end time (HH:MM) or press Enter to use the current value (" + selectedSlot.getEndTime() + "): ",
                    input -> input.isEmpty() || ConsoleUtility.isValidTime(input)
            );

            LocalTime endTime = endTimeInput.isEmpty() ? selectedSlot.getEndTime() : LocalTime.parse(endTimeInput);

            if (!startTime.isBefore(endTime)) {
                throw new IllegalArgumentException("Start time must be before End time.");
            }

            boolean updated = doctorController.updateSlot(selectedSlot, date, startTime, endTime);

            if (updated) {
                System.out.println("Available slot updated successfully.");
            } else {
                System.out.println("Failed to update available slot. Please try again.");
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Error updating available slot: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred while updating available slot: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles adding a new slot for a doctor.
     */
    private void addNewSlot() {
        try {
            // Prompt and validate the date and time inputs
            LocalDate date = LocalDate.parse(ConsoleUtility.validateInput(
                    "Enter date (YYYY-MM-DD): ", input -> {
                        try {
                            LocalDate.parse(input);
                            return true;
                        } catch (DateTimeParseException e) {
                            System.out.println("Invalid date format. Please enter a valid date (YYYY-MM-DD).");
                            return false;
                        }
                    }
            ));

            LocalTime startTime = LocalTime.parse(ConsoleUtility.validateInput(
                    "Enter start time (HH:MM): ", ConsoleUtility::isValidTime
            ));

            LocalTime endTime = LocalTime.parse(ConsoleUtility.validateInput(
                    "Enter end time (HH:MM): ", ConsoleUtility::isValidTime
            ));

            if (!startTime.isBefore(endTime)) {
                throw new IllegalArgumentException("Start time must be before End time.");
            }

            boolean added = doctorController.addAvailableSlot(date, startTime, endTime);

            if (added) {
                System.out.println("Available slot added successfully.");
            } else {
                System.out.println("Failed to add available slot. Please try again.");
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Error adding available slot: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred while adding available slot: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void removeSlot() {
        try {
            String slotID = ConsoleUtility.validateInput("\nEnter an available Slot ID to remove (or press Enter to go back): ",
                    input -> input.isEmpty() || doctorController.isValidAvailableSlotID(input));

            Slot selectedSlot = doctorController.getSlotByID(slotID);

            if (selectedSlot == null) {
                System.out.println("Error: Invalid Slot ID. Please try again.");
                return;
            }

            if (slotID.isEmpty()) {
                return;
            }

            boolean removed = doctorController.removeSlot(slotID);

            if (removed) {
                System.out.println("Slot removed successfully.");
            } else {
                System.out.println("Failed to remove slot. Please try again.");
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Error removing slot: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred while removing slot: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * [OPTION 5] Manages appointment requests.
     */
    private void manageAppointmentRequests() {
        try {
            ConsoleUtility.printHeader("MANAGE APPOINTMENT REQUESTS");
            List<Slot> pendingSlots = doctorController.getPendingSlotsForDoctor();

            if (pendingSlots.isEmpty()) {
                System.out.println("\nYou currently have no pending appointments.");
                return;
            }
            displaySlots(pendingSlots, "PENDING APPOINTMENTS");

            String slotID = ConsoleUtility.validateInput("\nEnter an Slot ID (Appointment ID) to accept (or press Enter to go back)[e.g. S01]: ",
                    input -> input.isEmpty() || doctorController.isValidPendingSlotID(input));
            if (slotID.isEmpty()) {
                return;
            }
            Slot selectedSlot = doctorController.getSlotByID(slotID);
            if (selectedSlot == null) {
                System.out.println("Error: Invalid Slot ID. Please try again.");
                return;
            }

            Appointment selectedAppointment = doctorController.getAppointmentBySlotID(slotID);
            Patient selectedPatient = doctorController.getPatientByAppointment(selectedAppointment);
            String patientID = selectedPatient.getUserID();
            System.out.println();
            displayPatientList(selectedPatient, "REQUESTING PATIENT'S DETAILS");

            List<History> medicalHistory = doctorController.getPatientMedicalHistory(patientID);

            if (medicalHistory.isEmpty()) {
                System.out.println("This patient has no medical history records.");
                return;
            }

            displayMedicalHistory(medicalHistory, "REQUESTING PATIENT'S MEDICAL RECORDS");

            displaySlots(selectedSlot, "SELECTED APPOINTMENT");

            manageAppointment(selectedAppointment, selectedSlot);

        } catch (Exception e) {
            System.err.println("An error occurred while viewing pending appointments: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Displays the a selected slot for the current doctor.
     *
     * @param slot The slot to display
     * @param name The name of the table
     */
    private void displaySlots(Slot slot, String name) {
        LinkedHashMap<String, TableBuilder.ColumnMapping> columnMapping = new LinkedHashMap<>();
        columnMapping.put("slotID", new TableBuilder.ColumnMapping("Slot ID", null));
        columnMapping.put("date", new TableBuilder.ColumnMapping("Date", null));
        columnMapping.put("startTime", new TableBuilder.ColumnMapping("Start Time", null));
        columnMapping.put("endTime", new TableBuilder.ColumnMapping("End Time", null));
        columnMapping.put("status", new TableBuilder.ColumnMapping("Status", null));

        List<Slot> slotList = Collections.singletonList(slot);

        TableBuilder.createTable(name, slotList, columnMapping, 20);
    }

    /**
     * Display the details of the patient requesting the appointment
     *
     * @param patients The patient requesting the appointment
     */
    private void displayPatientList(Patient patients, String name) {
        LinkedHashMap<String, TableBuilder.ColumnMapping> columnMapping = new LinkedHashMap<>();
        columnMapping.put("userID", new TableBuilder.ColumnMapping("Patient ID", null));
        columnMapping.put("name", new TableBuilder.ColumnMapping("Name", null));
        columnMapping.put("dateOfBirth", new TableBuilder.ColumnMapping("Date of Birth", null));
        columnMapping.put("gender", new TableBuilder.ColumnMapping("Gender", null));
        columnMapping.put("contactNumber", new TableBuilder.ColumnMapping("Contact Number", null));
        columnMapping.put("emailAddress", new TableBuilder.ColumnMapping("Email Address", null));
        columnMapping.put("bloodType", new TableBuilder.ColumnMapping("Blood Type", null));

        List<Patient> patientList = Collections.singletonList(patients);
        TableBuilder.createTable(name, patientList, columnMapping, 20);
    }

    /**
     * Managages a selected appointment fo rthe current doctor
     *
     * @param appointment The accompanying appointment
     * @param slot The selected slot
     */
    private void manageAppointment(Appointment appointment, Slot slot) {

        try {
            Boolean accept;
            String acceptString = ConsoleUtility.validateInput("\nWould you like to accept this appointment?(press Enter to go back) [y/n]: ",
                    input -> input.isEmpty() || ConsoleUtility.getConfirmation(""));

            if (acceptString.isEmpty()) {
                return;
            } else if (acceptString.equals("y")) {
                accept = true;
            } else if (acceptString.equals("n")) {
                accept = false;
            } else {
                return;
            }

            Boolean success = doctorController.manageAppointment(accept, appointment, slot);

            if (accept && success) {
                System.out.println("Appointment Sucessfully Accepted.");
            } else if (accept == false && success) {
                System.out.println("Appointment Sucessfully Rejected.");
            } else {
                System.out.println("Error managing appointment.");
            }

        } catch (IllegalArgumentException e) {
            System.err.println("Error managing appointments: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred while managing appointments: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * [OPTION 6] Views upcoming appointments.
     */
    private void viewUpcomingAppointments() {
        try {
            ConsoleUtility.printHeader("VIEW UPCOMING APPOINTMENTS");
            List<Slot> bookedSlots = doctorController.getBookedSlotsForDoctor();

            if (bookedSlots.isEmpty()) {
                System.out.println("\nYou currently have no upcoming appointments.");
                return;
            }
            displaySlots(bookedSlots, "UPCOMING APPOINTMENT");

        } catch (Exception e) {
            System.err.println("An error occurred while viewing upcoming appointments: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * [OPTION 7] Records appointment outcome.
     */
    private void recordAppointmentOutcome() {
        try {
            ConsoleUtility.printHeader("RECORD APPOINTMENT OUTCOME");
            List<Slot> bookedSlots = doctorController.getBookedSlotsForDoctor();

            if (bookedSlots.isEmpty()) {
                System.out.println("\nYou currently have no booked appointments to complete.");
                return;
            }
            displaySlots(bookedSlots, "SELECT A BOOKED APPOINTMENT TO COMPLETE");

            String slotID = ConsoleUtility.validateInput("\nEnter an Slot ID (Appointment ID) to complete (or press Enter to go back)[e.g. S01]: ",
                    input -> input.isEmpty() || doctorController.isValidBookedSlotID(input));

            if (slotID.isEmpty()) {
                return;
            }

            Slot selectedSlot = doctorController.getSlotByID(slotID);
            if (selectedSlot == null) {
                System.out.println("Error: Invalid Slot ID. Please try again.");
                return;
            }

            Appointment selectedAppointment = doctorController.getAppointmentBySlotID(slotID);
            Patient selectedPatient = doctorController.getPatientByAppointment(selectedAppointment);
            String patientID = selectedPatient.getUserID();
            System.out.println();
            displayPatientList(selectedPatient, "PATIENT'S DETAILS");

            List<History> medicalHistory = doctorController.getPatientMedicalHistory(patientID);

            if (medicalHistory.isEmpty()) {
                System.out.println("This patient has no medical history records.");
                return;
            }

            displayMedicalHistory(medicalHistory, "PATIENT'S MEDICAL RECORDS");

            displaySlots(selectedSlot, "SELECTED APPOINTMENT");

            completeAppointment(selectedAppointment, selectedSlot);

        } catch (Exception e) {
            System.err.println("An error occurred while viewing upcoming appointments: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Completes a booked appointment for the current doctor
     *
     * @param appointment The accompanying appointment
     * @param slot The selected booked slot
     */
    private void completeAppointment(Appointment appointment, Slot slot) {
        try {
            String serviceProvided = ConsoleUtility.validateInput("Enter service provided: ", input -> !input.trim().isEmpty());
            Boolean prescriptionsBoolean = ConsoleUtility.getConfirmation("\nDoes the patient require any prescriptions?");
            String prescriptionID = "";
            if (prescriptionsBoolean) {
                prescriptionID = addPrescription(appointment, prescriptionID);
            } else {
                prescriptionID = "NIL";
            }
            String consultationNotes = ConsoleUtility.validateInput("\nEnter consultation notes: ", input -> !input.trim().isEmpty());
            Boolean success = doctorController.completeAppointment(appointment, slot, serviceProvided, prescriptionID, consultationNotes);
            if (success) {
                System.out.println("Appointment Sucessfully Completed.");
            } else {
                System.out.println("Error completing appointment.");
            }

        } catch (IllegalArgumentException e) {
            System.err.println("Error completing appointments: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred while completing appointments: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String addPrescription(Appointment appointment, String prescriptionID) {
        try {
            String appointmentID = appointment.getAppointmentID();
            int count = 1;
            int quantity;
            String notes;
            Boolean success;

            List<Medication> medication = doctorController.getAllMedication();
            displayMedication(medication, "LIST OF MEDICATIONS");
            do {
                String medicationID = ConsoleUtility.validateInput("\nEnter an Medication ID to prescribe to the patient(press Enter to finish prescription)[e.g. M01]: ",
                        input -> input.isEmpty() || doctorController.isValidMedicationID(input));
                //need to account for 1/2 tablets
                if (medicationID.isEmpty() && count == 1) {
                    prescriptionID = "NIL";
                    break;
                } else if (medicationID.isEmpty()) {
                    break;
                }
                if (count == 1) {
                    prescriptionID = prescriptionID + medicationID;
                } else {
                    prescriptionID = prescriptionID + ";" + medicationID;
                }
                quantity = ConsoleUtility.validateInteger("Enter quantity: ");
                notes = ConsoleUtility.validateInput("Enter prescription notes: ", input -> !input.trim().isEmpty());
                success = doctorController.addPrescription(appointmentID, medicationID, quantity, notes);

                if (success) {
                    System.out.println("Medication sucessfully added to prescription.");
                } else {
                    System.out.println("Error adding prescription.");
                }

                count++;
            } while (true);
        } catch (IllegalArgumentException e) {
            System.err.println("Error adding prescription: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred while adding prescription: " + e.getMessage());
            e.printStackTrace();
        }
        return prescriptionID;
    }

    /**
     * Displays a list of medication.
     *
     * @param prescription The slot to display
     * @param name The name of the table
     */
    //medicationID,medicationName,stockLevel,lowStockAlertLevel
    private void displayMedication(List<Medication> medication, String name) {
        LinkedHashMap<String, TableBuilder.ColumnMapping> columnMapping = new LinkedHashMap<>();
        columnMapping.put("medicationID", new TableBuilder.ColumnMapping("Medication ID", null));
        columnMapping.put("name", new TableBuilder.ColumnMapping("Medication Name", null));
        columnMapping.put("stockLevel", new TableBuilder.ColumnMapping("Quantity Remaining", null));

        TableBuilder.createTable(name, medication, columnMapping, 20);
    }
}
