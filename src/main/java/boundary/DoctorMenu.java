package boundary;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;

import controller.DoctorController;
import entity.Appointment;
import entity.History;
import entity.Medication;
import entity.Outcome;
import entity.Patient;
import entity.Prescription;
import entity.Slot;
import entity.Doctor;
import utility.ConsoleUtility;
import utility.TableBuilder;

/**
 * DoctorMenu provides the user interface for doctor functionalities in the Hospital Management System.
 * This class handles all doctor-related operations including managing appointments, medical records,
 * prescriptions and patient information.
 * 
 * @author Group 7
 * @version 1.0
 */
public class DoctorMenu {

    private DoctorController doctorController;
    private Scanner scanner;

    /**
     * Constructor for DoctorMenu that initializes the doctor interface.
     * Creates a new DoctorMenu instance with the specified controller.
     * Sets up the scanner for user input handling.
     *
     * @param doctorController The DoctorController instance to handle business logic
     */
    public DoctorMenu(DoctorController doctorController) {
        this.doctorController = doctorController;
        this.scanner = new Scanner(System.in);
    }

    /**
     * [MAIN MENU] Displays the main menu options available to doctors and handles user input.
     * Provides access to various doctor functions like viewing records, managing appointments,
     * and handling patient information.
     * Includes error handling for invalid inputs and unexpected exceptions.
     */
    public void displayMenu() {
        while (true) {
            try {
                ConsoleUtility.printHeader("DOCTOR MENU");
                System.out.println("{1} View Patient Medical Records");
                System.out.println("{2} Update Patient Medical Records");
                System.out.println("{3} View Personal Schedule");
                System.out.println("{4} Manage Availability for Appointments");
                System.out.println("{5} Accept or Decline Appointment Requests");
                System.out.println("{6} View Upcoming Appointments");
                System.out.println("{7} Record Appointment Outcome");
                System.out.println("{8} View and Update Outcomes");
                System.out.println("{9} Logout");

                int choice = ConsoleUtility.validateIntegerInput("Enter your choice: ");

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
                        updateAppointmentOutcome();
                        break;
                    case 9:
                        System.out.println("Logging out and returning to home screen...");
                        return;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 9.");
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
     * [OPTION 1] Allows doctors to view medical records for patients under their care.
     * Displays a list of patients and their medical histories.
     * Provides functionality to select specific patients and view their detailed records.
     */
    private void viewPatientMedicalRecords() {
        try {
            String name;
            Patient selectedPatient;

            while (true) {
                ConsoleUtility.printHeader("VIEW PATIENT MEDICAL RECORDS");
                final List<Patient> patients = doctorController.getPatientsUnderCare();

                if (patients.isEmpty()) {
                    System.out.println("\nYou currently have no patients under your care.");
                    return;
                }

                displayPatients(patients, "Patients Under Your Care");

                // Prompt the user to select a patient
                System.out.println("");
                String patientID = ConsoleUtility.validateInput(
                        "Enter the Patient ID to view their medical records (or press Enter to go back): ",
                        input -> {
                            boolean exists = patients.stream().anyMatch(p -> p.getUserID().equals(input));
                            if (!exists) {
                                if (input.isEmpty()) {
                                    return true;
                                }
                                System.out.println("Invalid ID, patient does not exist.");
                            }
                            return exists;
                        });

                if (patientID.isEmpty()) {
                    return;
                }

                selectedPatient = doctorController.getPatientByID(patientID);
                name = selectedPatient.getName();
                List<History> medicalHistory = doctorController.getPatientMedicalHistory(patientID);

                if (medicalHistory.isEmpty()) {
                    System.out.println("This patient, " + name + ", has no medical history records.");
                    continue;
                }

                System.out.println();
                displayMedicalHistory(medicalHistory, name + "'s Medical History");
            }

        } catch (Exception e) {
            System.err.println("An error occurred while viewing medical records: " + e.getMessage());
            e.printStackTrace();
        }

    }

    /**
     * [OPTION 2] Enables doctors to update patient medical records including adding new records,
     * modifying existing ones, and removing records.
     * Provides a comprehensive interface for managing patient medical histories.
     * Includes validation and error handling for all record management operations.
     */
    private void updatePatientMedicalRecords() {
        try {
            String name;
            Patient selectedPatient;
            List<History> medicalHistory;

            // Display the list of patients under the doctor's care
            while (true) {

                ConsoleUtility.printHeader("UPDATE PATIENT MEDICAL RECORDS");
                final List<Patient> patients = doctorController.getPatientsUnderCare();

                if (patients.isEmpty()) {
                    System.out.println("\nYou currently have no patients under your care.");
                    return;
                }

                displayPatients(patients, "Patients Under Your Care");

                // Prompt the user to select a patient
                System.out.println("");
                String patientID = ConsoleUtility.validateInput(
                        "Enter the Patient ID to update (or press Enter to go back): ",
                        input -> {
                            boolean exists = patients.stream().anyMatch(p -> p.getUserID().equals(input));
                            if (!exists) {
                                if (input.isEmpty()) {
                                    return true;
                                }
                                System.out.println("Invalid ID, patient does not exist.");
                            }
                            return exists;
                        });

                if (patientID.isEmpty()) {
                    return;
                }

                selectedPatient = doctorController.getPatientByID(patientID);
                name = selectedPatient.getName();

                // Loop to update medical history
                while (true) {

                    medicalHistory = doctorController.getPatientMedicalHistory(patientID);
                    if (medicalHistory.isEmpty()) {
                        System.out.println("This patient, " + name + ", has no medical history records.");

                        if (ConsoleUtility.getConfirmation("Would you like to add a new record?")) {
                            addNewMedicalRecord(patientID);
                            continue;
                        }
                        break;
                    }

                    System.out.println("");
                    displayMedicalHistory(medicalHistory, name + "'s Medical History");
                    ConsoleUtility.printHeader("AVAILABLE MEDICAL HISTORY FUNCTIONS");
                    System.out.println("{1} Update History");
                    System.out.println("{2} Add History");
                    System.out.println("{3} Remove History");
                    System.out.println("{4} Return to Main Menu");

                    int choice = ConsoleUtility.validateIntegerInput("Enter your choice: ");

                    switch (choice) {
                        case 1:
                            updateMedicalRecord(medicalHistory, selectedPatient);
                            break;
                        case 2:
                            addNewMedicalRecord(patientID);
                            break;
                        case 3:
                            removeHistory(medicalHistory, selectedPatient);
                            break;
                        case 4:
                            System.out.println("Returning to Main Menu...");
                            return;
                        default:
                            System.out.println("Invalid choice. Please enter a number between 1 and 4.");
                    }

                }
            }
        } catch (Exception e) {
            System.err.println("An error occurred while updating medical records: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * [OPTION 2.1] Updates a specific medical record for a patient.
     * Allows modification of diagnosis and treatment information.
     * Includes validation and confirmation steps for the update process.
     *
     * @param medicalHistory List of medical history records for the patient
     * @param patient The patient whose record is being updated
     */
    private void updateMedicalRecord(List<History> medicalHistory, Patient patient) {
        String name = patient.getName();
        History selectedHistory;

        // Loop to update medical history
        while (true) {

            ConsoleUtility.printHeader("UPDATE PATIENT MEDICAL RECORDS");

            displayMedicalHistory(medicalHistory, name + "'s Medical History");

            System.out.println("");
            String historyID = ConsoleUtility.validateInput(
                    "Enter the History ID to update (or press Enter to go back): ",
                    input -> {
                        boolean exists = medicalHistory.stream().anyMatch(h -> h.getHistoryID().equals(input));
                        if (!exists) {
                            if (input.isEmpty()) {
                                return true;
                            }
                            System.out.println("Invalid ID, history does not exist.");
                        }
                        return exists;
                    });

            if (historyID.isEmpty()) {
                return;
            }

            selectedHistory = doctorController.getHistoryByID(historyID);

            if (selectedHistory == null) {
                System.out.println("Error: Invalid History ID. Please try again.");
                continue;
            }

            System.out.println("\nCurrent Diagnosis: " + selectedHistory.getDiagnosis());
            String newDiagnosis = ConsoleUtility.validateInput("Enter new diagnosis (or press Enter to keep current): ",
                    input -> input.isEmpty() || !input.trim().isEmpty());

            System.out.println("\nCurrent Treatment: " + selectedHistory.getTreatment());
            String newTreatment = ConsoleUtility.validateInput("Enter new treatment (or press Enter to keep current): ",
                    input -> input.isEmpty() || !input.trim().isEmpty());

            if (newDiagnosis.isEmpty() && newTreatment.isEmpty()) {
                System.out.println("No changes made to the medical record.");
                return;
            }

            try {
                boolean updated = doctorController.updateMedicalHistory(selectedHistory.getHistoryID(),
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
    }

    /**
     * [OPTION 2.2] Adds a new medical record for a specified patient.
     * Collects diagnosis and treatment information for the new record.
     * Validates input and confirms successful addition of the record.
     *
     * @param patientID The ID of the patient receiving the new medical record
     */
    private void addNewMedicalRecord(String patientID) {

        ConsoleUtility.printHeader("ADD NEW MEDICAL RECORD");

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
     * [OPTION 2.3] Removes a medical history record for a patient.
     * Displays available records and handles the removal process.
     * Includes validation and confirmation steps for the removal.
     *
     * @param medicalHistory List of medical history records to choose from
     * @param patient The patient whose history record is being removed
     */
    private void removeHistory(List<History> medicalHistory, Patient patient) {

        ConsoleUtility.printHeader("REMOVE MEDICAL RECORD");

        try {
            // Display the list of medications and prescriptions
            displayMedicalHistory(medicalHistory, patient.getName() + "'s Medical History");

            // Prompt the user to select a history
            System.out.println("");
            String historyID = ConsoleUtility.validateInput(
                    "Enter the History ID to remove (or press Enter to go back): ",
                    input -> {
                        boolean exists = medicalHistory.stream().anyMatch(h -> h.getHistoryID().equals(input));
                        if (!exists) {
                            if (input.isEmpty()) {
                                return true;
                            }
                            System.out.println("Invalid ID, history does not exist.");
                        }
                        return exists;
                    });

            if (historyID.isEmpty()) {
                return;
            }

            History selectedHistory = doctorController.getHistoryByID(historyID);
            if (selectedHistory == null) {
                System.out.println("Error: Invalid History ID. Please try again.");
                return;
            }

            boolean removed = doctorController.removeHistory(historyID);

            if (removed) {
                System.out.println("History removed successfully.");
            } else {
                System.out.println("Failed to remove histoty. Please try again.");
            }

        } catch (IllegalArgumentException e) {
            System.err.println("Error removing slot: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred while removing slot: " + e.getMessage());
            e.printStackTrace();
        }

    }

    /**
     * [OPTION 3] Displays the doctor's personal schedule and appointment details.
     * Shows all slots and their status, with additional details for booked slots.
     * Allows viewing of patient information for booked appointments.
     */
    private void viewPersonalSchedule() {
        try {
            List<Slot> slots = doctorController.getAllSlotsForDoctor();
            Appointment selectedAppointment;
            Patient selectedPatient;
            String patientID;
            List<History> medicalHistory;

            // Loop to view personal schedule
            while (true) {

                ConsoleUtility.printHeader("VIEW PERSONAL SCHEDULE");

                if (slots.isEmpty()) {
                    System.out.println("\nYou currently have no slots.");
                    System.out.println("\nReturning to Main Menu...");
                    return;
                }

                displaySlots(slots, "CURRENT SLOTS");

                // Prompt the user to select a slot
                System.out.println("");
                String slotID = ConsoleUtility.validateInput(
                        "Enter an available Slot ID to view its details (or press Enter to go back): ",
                        input -> {
                            boolean exists = slots.stream().anyMatch(s -> s.getSlotID().equals(input));
                            if (!exists) {
                                if (input.isEmpty()) {
                                    return true;
                                }
                                System.out.println("Invalid ID, slot does not exist.");
                            }
                            return exists;
                        });

                Slot selectedSlot = doctorController.getSlotByID(slotID);

                if (slotID.isEmpty()) {
                    System.out.println("\nReturning to Main Menu...");
                    return;
                }

                if (selectedSlot == null) {
                    System.out.println("Error: Invalid Slot ID. Please try again.");
                    continue;
                }

                System.out.println("");
                displaySlots(selectedSlot, "Slot Details");

                Slot.SlotStatus aSlotStatus = selectedSlot.getStatus();

                if (aSlotStatus != Slot.SlotStatus.AVAILABLE) {
                    selectedAppointment = doctorController.getAppointmentBySlotID(slotID);
                    selectedPatient = doctorController.getPatientByAppointment(selectedAppointment);
                    patientID = selectedPatient.getUserID();
                    medicalHistory = doctorController.getPatientMedicalHistory(patientID);
                    displayAppointments(Collections.singletonList(selectedAppointment), "Appointment Details");
                    displayPatients(selectedPatient, "PATIENT'S DETAILS");

                    if (medicalHistory.isEmpty()) {
                        System.out.println("This patient has no medical history records.");
                        continue;
                    }

                    displayMedicalHistory(medicalHistory, "PATIENT'S MEDICAL RECORDS");

                }

            }

        } catch (Exception e) {
            System.err.println("An error occurred while viewing schedule: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * [OPTION 4] Manages the doctor's availability for appointments.
     * Allows adding, removing, and updating available time slots.
     * Displays current availability and handles slot management operations.
     */
    private void manageAvailabilityForAppointments() {

        try {
            List<Slot> slots;
            List<Slot> availableSlots;

            // Display all slots and then available slots
            while (true) {
                ConsoleUtility.printHeader("MANAGE AVAILABILITY FOR APPOINTMENTS");

                // refresh slots after every run to keep the slots updated
                slots = doctorController.getAllSlotsForDoctor();
                if (slots.isEmpty()) {
                    System.out.println("\nYou currently have no slots.");
                } else {
                    displaySlots(slots, "ALL CURRENT SLOTS");
                }

                availableSlots = doctorController.getAvailableSlotsForDoctor();

                if (availableSlots.isEmpty()) {
                    System.out.println("\nYou currently have no available slots.");
                } else {
                    displaySlots(availableSlots, "AVAILABLE SLOTS");
                }

                ConsoleUtility.printHeader("AVAILABLE SLOT FUNCTIONS");
                System.out.println("{1} Update Avaiable Slot");
                System.out.println("{2} Add Available Slot");
                System.out.println("{3} Remove Available Slot");
                System.out.println("{4} Return to Main Menu");

                int choice = ConsoleUtility.validateIntegerInput("Enter your choice: ");

                switch (choice) {
                    case 1:
                        updateSlot(availableSlots);
                        break;
                    case 2:
                        addNewSlot();
                        break;
                    case 3:
                        removeSlot(availableSlots);
                        break;
                    case 4:
                        System.out.println("Returning to Main Menu...");
                        return;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 4.");
                }
            }
        } catch (Exception e) {
            System.err.println("An error occurred while updating avaialble slots: " + e.getMessage());
            e.printStackTrace();
        }

    }

    /**
     * [OPTION 4.1] Updates an existing available time slot.
     * Allows modification of date and time information for the slot.
     * Includes validation for time conflicts and date formatting.
     *
     * @param availableSlots List of currently available slots that can be updated
     */
    private void updateSlot(List<Slot> availableSlots) {

        if (availableSlots.isEmpty()) {
            System.out.println("\nYou have no available slots to update.");
            return;
        }

        while (true) {
            try {

                ConsoleUtility.printHeader("UPDATE AVAILABLE SLOT");

                // Display the list of available slots
                displaySlots(availableSlots, "AVAILABLE SLOTS");

                // Prompt the user to select a slot
                System.out.println("");
                String slotID = ConsoleUtility.validateInput(
                        "Enter an available Slot ID to update (or press Enter to go back): ",
                        input -> {
                            boolean exists = availableSlots.stream().anyMatch(s -> s.getSlotID().equals(input));
                            if (!exists) {
                                if (input.isEmpty()) {
                                    return true;
                                }
                                System.out.println("Invalid ID, slot does not exist.");
                            }
                            return exists;
                        });

                if (slotID.isEmpty()) {
                    return;
                }

                Slot selectedSlot = doctorController.getSlotByID(slotID);
                if (selectedSlot == null) {
                    System.out.println("Error: Invalid Slot ID. Please try again.");
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
                        });

                LocalDate date = dateInput.isEmpty() ? selectedSlot.getDate() : LocalDate.parse(dateInput);

                System.out.println("\nCurrent Start Time: " + selectedSlot.getStartTime());
                String startTimeInput = ConsoleUtility.validateInput(
                        "Enter start time (HH:MM) or press Enter to use the current value ("
                                + selectedSlot.getStartTime() + "): ",
                        input -> input.isEmpty() || ConsoleUtility.isValidTime(input));

                LocalTime startTime = startTimeInput.isEmpty() ? selectedSlot.getStartTime()
                        : LocalTime.parse(startTimeInput);

                System.out.println("\nCurrent End Time: " + selectedSlot.getEndTime());
                String endTimeInput = ConsoleUtility.validateInput(
                        "Enter end time (HH:MM) or press Enter to use the current value (" + selectedSlot.getEndTime()
                                + "): ",
                        input -> input.isEmpty() || ConsoleUtility.isValidTime(input));

                LocalTime endTime = endTimeInput.isEmpty() ? selectedSlot.getEndTime() : LocalTime.parse(endTimeInput);

                if (!startTime.isBefore(endTime)) {
                    throw new IllegalArgumentException("Start time must be before End time.");
                }

                boolean updated = doctorController.updateSlot(selectedSlot, date, startTime, endTime);

                if (updated) {
                    System.out.println("Available slot updated successfully.");
                    return;
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
    }

    /**
     * [OPTION 4.2] Adds a new available time slot for appointments.
     * Collects date and time information for the new slot.
     * Validates input to ensure proper formatting and logical time sequence.
     */
    private void addNewSlot() {
        while (true) {
            try {

                ConsoleUtility.printHeader("ADD NEW AVAILABLE SLOT");

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
                        }));

                LocalTime startTime = LocalTime.parse(ConsoleUtility.validateInput(
                        "Enter start time (HH:MM): ", ConsoleUtility::isValidTime));

                LocalTime endTime = LocalTime.parse(ConsoleUtility.validateInput(
                        "Enter end time (HH:MM): ", ConsoleUtility::isValidTime));

                if (!startTime.isBefore(endTime)) {
                    throw new IllegalArgumentException("Start time must be before End time.");
                }

                boolean added = doctorController.addAvailableSlot(date, startTime, endTime);

                if (added) {
                    System.out.println("Available slot added successfully.");
                    return;
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
    }

    /**
     * [OPTION 4.3] Removes an available time slot from the doctor's schedule.
     * Displays available slots and handles the removal process.
     * Includes validation and confirmation steps.
     *
     * @param availableSlots List of available slots that can be removed
     */
    private void removeSlot(List<Slot> availableSlots) {

        if (availableSlots.isEmpty()) {
            System.out.println("\nYou have no available slots to remove.");
            return;
        }

        try {
            ConsoleUtility.printHeader("REMOVE AVAILABLE SLOT");
            displaySlots(availableSlots, "AVAILABLE SLOTS");

            // Prompt the user to select a slot
            System.out.println("");
            String slotID = ConsoleUtility.validateInput(
                    "Enter an available Slot ID to remove (or press Enter to go back): ",
                    input -> {
                        boolean exists = availableSlots.stream().anyMatch(s -> s.getSlotID().equals(input));
                        if (!exists) {
                            if (input.isEmpty()) {
                                return true;
                            }
                            System.out.println("Invalid ID, slot does not exist.");
                        }
                        return exists;
                    });

            if (slotID.isEmpty()) {
                return;
            }

            Slot selectedSlot = doctorController.getSlotByID(slotID);

            if (selectedSlot == null) {
                System.out.println("Error: Invalid Slot ID. Please try again.");
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
     * [OPTION 5] Manages appointment requests from patients by displaying pending requests and allowing the doctor to accept or decline them.
     * The method shows comprehensive patient information and medical history to help the doctor make informed decisions.
     * For each request, displays:
     * - Patient details including medical history
     * - Appointment date and time
     * - Option to accept or decline the request
     */
    private void manageAppointmentRequests() {
        try {
            // Main loop for managing appointment requests
            while (true) {
                ConsoleUtility.printHeader("MANAGE APPOINTMENT REQUESTS");

                // Get all pending appointments for the doctor
                List<Appointment> pendingAppointments = doctorController.getPendingAppointmentsForDoctor();

                // If there are no pending appointments, return to the main menu
                if (pendingAppointments.isEmpty()) {
                    System.out.println("You currently have no pending appointment requests.");
                    System.out.println("Returning to Main Menu...");
                    return;
                }

                // Display the list of pending appointments
                displayAppointments(pendingAppointments, "PENDING APPOINTMENT REQUESTS");

                // Prompt the user to select an appointment
                System.out.println("");
                String appointmentID = ConsoleUtility.validateInput(
                        "Enter an Appointment ID to accept or decline (or press Enter to go back): ",
                        input -> {
                            boolean exists = pendingAppointments.stream()
                                .anyMatch(a -> a.getAppointmentID().equals(input));
                            if (!exists) {
                                if (input.isEmpty()) {
                                    return true;
                                }
                                System.out.println("Invalid ID, appointment does not exist.");
                            }
                            return exists;
                        });

                if (appointmentID.isEmpty()) {
                    System.out.println("Returning to Main Menu...");
                    return;
                }

                // Get the selected appointment
                Appointment selectedAppointment = doctorController.getAppointmentByID(appointmentID);
                if (selectedAppointment == null) {
                    System.out.println("Error: Invalid Appointment ID. Please try again.");
                    continue;
                }

                // Get the selected slot
                Slot selectedSlot = doctorController.getSlotByID(selectedAppointment.getSlotID());

                // Get the selected patient
                Patient selectedPatient = doctorController.getPatientByAppointment(selectedAppointment);
                String patientID = selectedPatient.getUserID();

                // Display the selected patient's details
                System.out.println("");
                displayPatients(selectedPatient, "REQUESTING PATIENT'S DETAILS");

                // Get the selected patient's medical history
                List<History> medicalHistory = doctorController.getPatientMedicalHistory(patientID);

                // If the patient has no medical history records, display a message
                if (medicalHistory.isEmpty()) {
                    System.out.println("This patient has no medical history records.");
                } else {
                    displayMedicalHistory(medicalHistory, "REQUESTING PATIENT'S MEDICAL RECORDS");
                }

                // Display the selected appointment
                displayAppointments(Collections.singletonList(selectedAppointment), "SELECTED APPOINTMENT REQUEST");

                // Manage the selected appointment
                manageAppointment(selectedAppointment, selectedSlot);
            }
        } catch (Exception e) {
            System.err.println("An error occurred while managing appointment requests: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * [OPTION 5.1] Processes the doctor's decision to accept or decline a specific appointment request.
     * Validates the doctor's input and updates the appointment status accordingly.
     * Provides feedback on the success or failure of the operation.
     *
     * @param appointment The appointment object containing the request details to be processed
     * @param slot The time slot associated with the appointment request
     */
    private void manageAppointment(Appointment appointment, Slot slot) {

        try {
            Boolean accept;

            // Get user confirmation
            System.out.println("");
            String acceptString = ConsoleUtility.validateInput(
                    "Would you like to accept this appointment? (press Enter to go back) [y/n]: ",
                    input -> input.isEmpty() || input.equalsIgnoreCase("y") || input.equalsIgnoreCase("n"));

            acceptString = acceptString.toLowerCase();

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
     * [OPTION 6] Displays and manages the doctor's upcoming confirmed appointments.
     * Shows a list of all confirmed appointments with their details including:
     * - Patient information
     * - Appointment date and time
     * - Medical history of the patient
     * Allows the doctor to select and view detailed information for specific appointments.
     */
    private void viewUpcomingAppointments() {
        try {
            // Main loop for viewing upcoming appointments
            while (true) {
                ConsoleUtility.printHeader("VIEW UPCOMING APPOINTMENTS");

                // Get all confirmed appointments for the doctor
                List<Appointment> confirmedAppointments = doctorController.getConfirmedAppointmentsForDoctor();

                // If there are no confirmed appointments, return to the main menu
                if (confirmedAppointments.isEmpty()) {
                    System.out.println("You currently have no upcoming appointments.");
                    System.out.println("Returning to Main Menu...");
                    return;
                }

                // Display the list of confirmed appointments
                displayAppointments(confirmedAppointments, "UPCOMING APPOINTMENTS");

                // Prompt the user to select an appointment
                System.out.println("");
                String appointmentID = ConsoleUtility.validateInput(
                        "Enter an Appointment ID to view details (or press Enter to go back): ",
                        input -> {
                            boolean exists = confirmedAppointments.stream()
                                .anyMatch(a -> a.getAppointmentID().equals(input));
                            if (!exists) {
                                if (input.isEmpty()) {
                                    return true;
                                }
                                System.out.println("Invalid ID, appointment does not exist.");
                            }
                            return exists;
                        });

                if (appointmentID.isEmpty()) {
                    System.out.println("Returning to Main Menu...");
                    return;
                }

                // Get the selected appointment
                Appointment selectedAppointment = doctorController.getAppointmentByID(appointmentID);
                if (selectedAppointment == null) {
                    System.out.println("Error: Invalid Appointment ID. Please try again.");
                    continue;
                }

                // Get the selected patient
                Patient selectedPatient = doctorController.getPatientByAppointment(selectedAppointment);
                String patientID = selectedPatient.getUserID();

                // Get the selected patient's medical history
                List<History> medicalHistory = doctorController.getPatientMedicalHistory(patientID);

                // Display detailed information
                System.out.println("");
                displayAppointments(Collections.singletonList(selectedAppointment), "APPOINTMENT DETAILS");
                displayPatients(selectedPatient, "PATIENT'S DETAILS");

                if (medicalHistory.isEmpty()) {
                    System.out.println("This patient has no medical history records.");
                } else {
                    displayMedicalHistory(medicalHistory, "PATIENT'S MEDICAL RECORDS");
                }
            }
        } catch (Exception e) {
            System.err.println("An error occurred while viewing upcoming appointments: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * [OPTION 7] Manages the process of recording outcomes for confirmed appointments.
     * Allows doctors to:
     * - Select a confirmed appointment
     * - View patient details and medical history
     * - Record services provided
     * - Add consultation notes
     * - Prescribe medications if needed
     */
    private void recordAppointmentOutcome() {
        try {
            // Main loop for recording appointment outcomes
            while (true) {
                ConsoleUtility.printHeader("RECORD APPOINTMENT OUTCOME");

                // Get all confirmed appointments for the doctor
                List<Appointment> confirmedAppointments = doctorController.getConfirmedAppointmentsForDoctor();

                // If there are no confirmed appointments, return to the main menu
                if (confirmedAppointments.isEmpty()) {
                    System.out.println("You currently have no appointments to complete.");
                    System.out.println("Returning to Main Menu...");
                    return;
                }

                // Display the list of confirmed appointments
                displayAppointments(confirmedAppointments, "SELECT AN APPOINTMENT TO COMPLETE");

                // Prompt the user to select an appointment
                System.out.println("");
                String appointmentID = ConsoleUtility.validateInput(
                        "Enter an Appointment ID to complete (or press Enter to go back): ",
                        input -> {
                            boolean exists = confirmedAppointments.stream()
                                .anyMatch(a -> a.getAppointmentID().equals(input));
                            if (!exists) {
                                if (input.isEmpty()) {
                                    return true;
                                }
                                System.out.println("Invalid ID, appointment does not exist.");
                            }
                            return exists;
                        });

                if (appointmentID.isEmpty()) {
                    System.out.println("\nReturning to Main Menu...");
                    return;
                }

                // Get the selected appointment
                Appointment selectedAppointment = doctorController.getAppointmentByID(appointmentID);
                if (selectedAppointment == null) {
                    System.out.println("Error: Invalid Appointment ID. Please try again.");
                    continue;
                }

                // Get the selected slot
                Slot selectedSlot = doctorController.getSlotByID(selectedAppointment.getSlotID());

                // Get the selected patient
                Patient selectedPatient = doctorController.getPatientByAppointment(selectedAppointment);

                // Display the selected patient's details
                System.out.println("");
                displayPatients(selectedPatient, "PATIENT'S DETAILS");

                // Get and display the selected patient's medical history
                List<History> medicalHistory = doctorController.getPatientMedicalHistory(selectedPatient.getUserID());
                if (!medicalHistory.isEmpty()) {
                    displayMedicalHistory(medicalHistory, "PATIENT'S MEDICAL RECORDS");
                }

                // Display the selected appointment
                displayAppointments(Collections.singletonList(selectedAppointment), "SELECTED APPOINTMENT");

                // Complete the selected appointment
                completeAppointment(selectedAppointment, selectedSlot);
            }
        } catch (Exception e) {
            System.err.println("An error occurred while recording appointment outcome: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Records the completion of a booked appointment by capturing:
     * - Service provided during the appointment
     * - Consultation notes
     * - Any prescribed medications
     * Updates the appointment status and creates associated records.
     *
     * @param appointment The appointment object to be completed
     * @param slot The time slot associated with the appointment
     */
    private void completeAppointment(Appointment appointment, Slot slot) {
        try {

            // Get user input for service provided
            System.out.println("");
            String serviceProvided = ConsoleUtility.validateInput("Enter service provided: ",
                    input -> !input.trim().isEmpty());

            // Get user input for consultation notes
            String consultationNotes = ConsoleUtility.validateInput("Enter consultation notes: ",
                    input -> !input.trim().isEmpty());

            // Get user confirmation for prescriptions
            Boolean prescriptionsBoolean = ConsoleUtility
                    .getConfirmation("Does the patient require any prescriptions?");
            String prescriptionString = "";

            // If the patient requires prescriptions, add them
            if (prescriptionsBoolean) {
                prescriptionString = addPrescription(appointment, prescriptionString, 0);
            } else {
                prescriptionString = "NIL";
            }

            // Complete the selected appointment
            Boolean success = doctorController.completeAppointment(appointment, slot, serviceProvided,
                    prescriptionString, consultationNotes);

            // If the appointment is successfully completed, display a success message
            if (success) {
                System.out.println("Appointment Sucessfully Completed.");

                // Display the outcome of the completed appointment
                System.out.println("");
                displayOutcome(doctorController.getOutcomeByID(appointment.getOutcomeID()),
                        "OUTCOME OF COMPLETED APPOINTMENT");
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

    /**
     * Manages the process of adding prescriptions to an appointment outcome.
     * Allows doctors to:
     * - View available medications
     * - Select medications to prescribe
     * - Specify quantity and instructions for each prescription
     * - Add multiple prescriptions for a single appointment
     *
     * @param appointment The appointment to add prescriptions to
     * @param medicationIDString The current string of medication IDs (semicolon separated)
     * @param count Number of medications currently prescribed
     * @return Updated string of medication IDs in format "M01;M02;M03"
     */
    private String addPrescription(Appointment appointment, String medicationIDString, int count) {
        try {
            String appointmentID = appointment.getAppointmentID();
            boolean isFirstVisit = true;
            int quantity;
            String notes;
            String medicationID;
            Boolean success;

            // Store list of medication IDs
            List<String> medicationList = new ArrayList<>();
            if (!medicationIDString.isEmpty() && !medicationIDString.equals("NIL")) {
                medicationList = new ArrayList<>(Arrays.asList(medicationIDString.split(";")));
            }

            do {
                List<Medication> medications = doctorController.getAllMedication();

                if (isFirstVisit) {
                    System.out.println("");
                    displayMedication(medications, "AVAILABLE MEDICATIONS");
                    isFirstVisit = false;
                }

                System.out.println("");
                medicationID = ConsoleUtility.validateInput(
                        "Enter a Medication ID to prescribe (press Enter to finish prescribing) [e.g. M01]: ",
                        input -> {
                            boolean exists = medications.stream()
                                .anyMatch(m -> m.getMedicationID().equals(input));
                            if (!exists) {
                                if (input.isEmpty()) {
                                    return true;
                                }
                                System.out.println("Invalid Medication ID. Please try again.");
                            }
                            return exists;
                        });

                if (medicationID.isEmpty() && count == 0) {
                    medicationIDString = "NIL";
                    break;
                } else if (medicationID.isEmpty()) {
                    break;
                }

                // Check if this medication has already been prescribed
                if (medicationList.contains(medicationID)) {
                    System.out.println("This medication has already been prescribed for this appointment.");
                    continue;
                }

                // Add medication ID to tracking list
                medicationList.add(medicationID);

                // Update the medication ID string
                if (medicationIDString.equals("NIL")) {
                    medicationIDString = medicationID;
                } else if (medicationIDString.isEmpty()) {
                    medicationIDString = medicationID;
                } else {
                    medicationIDString = medicationIDString + ";" + medicationID;
                }

                // Get prescription details
                quantity = ConsoleUtility.validateIntegerInput("Enter quantity to prescribe: ");
                notes = ConsoleUtility.validateInput(
                    "Enter prescription instructions (e.g., 'Take twice daily after meals'): ", 
                    input -> !input.trim().isEmpty()
                );

                // Create new prescription
                success = doctorController.createPrescription(appointmentID, medicationID, quantity, notes);

                if (success) {
                    System.out.println("Medication successfully prescribed.");
                } else {
                    System.out.println("Error creating prescription.");
                }

                count++;
            } while (true);

        } catch (IllegalArgumentException e) {
            System.err.println("Error adding prescription: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred while adding prescription: " + e.getMessage());
            e.printStackTrace();
        }

        return medicationIDString;
    }

    /**
     * [OPTION 8] View and update appointment outcomes for completed appointments.
     * Allows doctors to review and modify the outcomes of their completed appointments,
     * including updating service provided, consultation notes, and managing prescriptions.
     * Displays relevant patient information and current outcome details before updates.
     */
    private void updateAppointmentOutcome() {
        try {
            List<Medication> medication = doctorController.getAllMedication();
            List<Outcome> outcomes;
            List<Patient> patients;

            while (true) {

                ConsoleUtility.printHeader("VIEW AND UPDATE APPOINTMENT OUTCOME");

                // Get all completed appointments for the current doctor
                List<Appointment> completedAppointment = doctorController.getCompletedAppointmentsForDoctor();

                // Get all outcomes for the completed appointments
                outcomes = doctorController.getOutcomesForCompletedAppointmentsForDoctor();

                // Get all patients under the current doctor's care
                patients = doctorController.getPatientsUnderCare();

                // If there are no completed appointments, display a message and return to the main menu
                if (completedAppointment.isEmpty()) {
                    System.out.println("You currently have no completed appointments to view or update.");
                    System.out.println("Returning to Main Menu...");
                    return;
                }

                // Display the list of patients under the doctor's care
                displayPatients(patients, "PATIENT INFO");

                // Display the list of medications
                displayMedication(medication, "LIST OF MEDICATIONS");

                // Display the list of outcomes
                displayOutcome(outcomes, "OUTCOMES");

                // Display the list of completed appointments
                displayAppointments(completedAppointment, "SELECT A COMPLETED APPOINTMENT OUTCOME TO UPDATE");

                // Get user input for the appointment ID to update
                System.out.println("");
                String appointmentID = ConsoleUtility.validateInput(
                        "Enter an Appointment ID to update (or press Enter to go back)[e.g. A01]: ",
                        input -> {
                            boolean exists = completedAppointment.stream().anyMatch(a -> a.getAppointmentID().equals(input));
                            if (!exists) {
                                if (input.isEmpty()) {
                                    return true;
                                }
                                System.out.println("Invalid ID, appointment does not exist.");
                            }
                            return exists;
                        });

                if (appointmentID.isEmpty()) {
                    System.out.println("\nReturning to Main Menu...");
                    return;
                }

                // Update outcome path
                updateOutcome(appointmentID);
            }

        } catch (Exception e) {
            System.err.println("An error occurred while viewing upcoming appointments: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Updates an existing outcome record for a completed appointment.
     * Allows modification of service provided, consultation notes, and prescription details.
     * Displays current outcome information before allowing updates and validates all input.
     * 
     * @param appointmentID The unique identifier of the appointment whose outcome needs to be updated
     */
    private void updateOutcome(String appointmentID) {
        // Retrieve the selected appointment, outcome, and prescriptions
        Appointment selectedAppointment = doctorController.getAppointmentByID(appointmentID);
        String outcomeID = doctorController.getOutcomeIDFromAppointment(selectedAppointment);
        Outcome selectedOutcome = doctorController.getOutcomeByID(outcomeID);
        List<Prescription> prescriptions = doctorController.getPrescriptionsByAppointmentID(appointmentID);
        Patient patient = doctorController.getPatientFromAppointment(selectedAppointment);

        if (selectedOutcome == null) {
            System.out.println(
                    "Error: This appointment does not have an outcome. Please complete this appointment first.");
            return;
        }

        try {
            // Display current patient and outcome information
            System.out.println("");
            displayPatients(patient, "PATIENT INFO");
            displayOutcome(selectedOutcome, "OUTCOME INFO");

            // Update the outcome details: service provided and consultation notes
            System.out.println("\nCurrent Service Provided: " + selectedOutcome.getServiceProvided());
            String newServiceProvided = ConsoleUtility.validateInput(
                    "Enter new service provided (or press Enter to keep current): ",
                    input -> input.isEmpty() || !input.trim().isEmpty());

            // If the input is empty, keep the current value
            if (newServiceProvided.isEmpty()) {
                newServiceProvided = selectedOutcome.getServiceProvided();
            }

            System.out.println("\nCurrent Consultation Notes: " + selectedOutcome.getConsultationNotes());
            String newConsultationNotes = ConsoleUtility.validateInput(
                    "Enter new consultation notes (or press Enter to keep current): ",
                    input -> input.isEmpty() || !input.trim().isEmpty());

            // If the input is empty, keep the current value
            if (newConsultationNotes.isEmpty()) {
                newConsultationNotes = selectedOutcome.getConsultationNotes();
            }

            // Display current prescriptions and update if needed
            System.out.println("");
            displayPrescriptions(prescriptions, "CURRENT PRESCRIPTIONS PROVIDED FOR THIS APPOINTMENT");
            Boolean prescriptionsBoolean = ConsoleUtility
                    .getConfirmation("\nDoes the patient require any changes to the prescriptions?");
            String prescriptionString = selectedOutcome.getPrescriptionID();

            if (prescriptionsBoolean) {
                prescriptionString = managePrescriptions(appointmentID, prescriptionString);
            }

            // Update outcome with new or existing values
            Boolean success = doctorController.updateOutcome(selectedOutcome, newServiceProvided, prescriptionString,
                    newConsultationNotes);
            if (success) {
                System.out.println("Outcome Successfully Updated.");
            } else {
                System.out.println("Error updating outcomes.");
            }

        } catch (IllegalArgumentException e) {
            System.err.println("Error updating outcomes: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred while updating outcomes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * [OPTION 8.1] Manages prescriptions linked to a specific appointment.
     * Provides functionality to add new prescriptions or remove existing ones.
     * Displays current prescriptions and available medications before modifications.
     * 
     * @param appointmentID The unique identifier of the appointment to manage prescriptions for
     * @param prescriptionString The current semicolon-separated string of medication IDs
     * @return The updated prescription string after modifications
     */
    private String managePrescriptions(String appointmentID, String prescriptionString) {
        List<Prescription> prescriptions;
        List<Medication> medication = doctorController.getAllMedication();
        Appointment appointment = doctorController.getAppointmentByID(appointmentID);
        int count = doctorController.countMedication(prescriptionString);

        // Manage prescriptions associated with the appointment
        while (true) {
            ConsoleUtility.printHeader("MANAGE PRESCRIPTIONS PROVIDED FOR THIS APPOINTMENT");
            prescriptions = doctorController.getPrescriptionsByAppointmentID(appointmentID);

            displayMedication(medication, "LIST OF MEDICATIONS");

            if (prescriptions.isEmpty()) {
                System.out.println("You currently have no prescriptions.");
            } else {
                displayPrescriptions(prescriptions, "PRESCRIPTIONS PROVIDED FOR THIS APPOINTMENT");
            }

            ConsoleUtility.printHeader("AVAILABLE PRESCRIPTIONS FUNCTIONS");
            System.out.println("{1} Add Prescription");
            System.out.println("{2} Remove Prescription");
            System.out.println("{3} Complete Updating of Outcome");

            int choice = ConsoleUtility.validateIntegerInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    prescriptionString = addPrescription(appointment, prescriptionString, count);
                    break;
                case 2:
                    if (!prescriptions.isEmpty()) {
                        prescriptionString = removePrescription(medication, prescriptions, prescriptionString);
                    } else {
                        System.out.println("\nYou have no prescription to remove.");
                    }
                    break;
                case 3:
                    System.out.println("Completing the update process for the outcome.");
                    return prescriptionString; // Exit the loop and method
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 4.");
            }
        }
    }

    /**
     * Removes a prescription from an appointment and updates the prescription string.
     * Displays available medications and current prescriptions before removal.
     * Validates prescription ID input and handles the removal process.
     * 
     * @param medication List of all available medications in the system
     * @param prescriptions List of current prescriptions for the appointment
     * @param prescriptionString Current semicolon-separated string of medication IDs
     * @return Updated prescription string after removal
     */
    private String removePrescription(List<Medication> medication, List<Prescription> prescriptions,
            String prescriptionString) {
        try {
            // Display the list of medications and prescriptions
            System.out.println("");
            displayMedication(medication, "LIST OF MEDICATIONS");
            displayPrescriptions(prescriptions, "PRESCRIPTIONS");

            // Get prescription ID from user
            String prescriptionID = ConsoleUtility.validateInput(
                    "Enter a prescription ID to remove (or press Enter to go back): ",
                    input -> {
                        boolean exists = prescriptions.stream().anyMatch(p -> p.getPrescriptionID().equals(input));
                        if (!exists) {
                            if (input.isEmpty()) {
                                return true;
                            }
                            System.out.println("Invalid ID, prescription does not exist.");
                        }
                        return exists;
                    });

            if (prescriptionID.isEmpty()) {
                return prescriptionString; // User chose to go back without removing anything
            }

            // Retrieve the selected prescription
            Prescription selectedPrescription = doctorController.getPrescriptionByID(prescriptionID);
            if (selectedPrescription == null) {
                System.out.println("Error: Invalid Prescription ID. Please try again.");
                return prescriptionString;
            }

            // Remove the prescription from the system
            boolean removed = doctorController.removePrescription(prescriptionID);

            if (removed) {
                System.out.println("Prescription removed successfully.");

                // Remove the medication ID from prescriptionString
                String medicationIDToRemove = selectedPrescription.getMedicationID();

                // creates an array to store IDs and manage medication IDs
                List<String> prescriptionList = new ArrayList<>(Arrays.asList(prescriptionString.split(";")));

                // Remove the medication ID from the list
                prescriptionList.remove(medicationIDToRemove);

                // Update the prescriptionString
                prescriptionString = String.join(";", prescriptionList);

            } else {
                System.out.println("Failed to remove prescription. Please try again.");
            }

        } catch (IllegalArgumentException e) {
            System.err.println("Error removing prescription: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred while removing prescription: " + e.getMessage());
            e.printStackTrace();
        }

        // Return the updated prescription string
        return prescriptionString;
    }

    /**
     * Creates and displays a formatted table showing patient information.
     * Shows Patient ID, Name, Date of Birth, Gender, Contact Number, Email Address, and Blood Type.
     * Handles multiple patient records in a list format.
     * 
     * @param patients List of Patient objects to display
     * @param name Title of the table to be displayed
     */
    private void displayPatients(List<Patient> patients, String name) {
        LinkedHashMap<String, TableBuilder.ColumnMapping> columnMapping = new LinkedHashMap<>();
        columnMapping.put("userID", new TableBuilder.ColumnMapping("Patient ID", null));
        columnMapping.put("name", new TableBuilder.ColumnMapping("Name", null));
        columnMapping.put("dateOfBirth", new TableBuilder.ColumnMapping("Date of Birth", null));
        columnMapping.put("gender", new TableBuilder.ColumnMapping("Gender", null));
        columnMapping.put("contactNumber", new TableBuilder.ColumnMapping("Contact Number", null));
        columnMapping.put("emailAddress", new TableBuilder.ColumnMapping("Email Address", null));
        columnMapping.put("bloodType", new TableBuilder.ColumnMapping("Blood Type", null));

        TableBuilder.createTable(name, patients, columnMapping, 20);
    }

    /**
     * Creates and displays a formatted table showing a single patient's information.
     * Shows Patient ID, Name, Date of Birth, Gender, Contact Number, Email Address, and Blood Type.
     * Formats data for a single patient record.
     * 
     * @param patient Single Patient object to display
     * @param name Title of the table to be displayed
     */
    private void displayPatients(Patient patient, String name) {
        LinkedHashMap<String, TableBuilder.ColumnMapping> columnMapping = new LinkedHashMap<>();
        columnMapping.put("userID", new TableBuilder.ColumnMapping("Patient ID", null));
        columnMapping.put("name", new TableBuilder.ColumnMapping("Name", null));
        columnMapping.put("dateOfBirth", new TableBuilder.ColumnMapping("Date of Birth", null));
        columnMapping.put("gender", new TableBuilder.ColumnMapping("Gender", null));
        columnMapping.put("contactNumber", new TableBuilder.ColumnMapping("Contact Number", null));
        columnMapping.put("emailAddress", new TableBuilder.ColumnMapping("Email Address", null));
        columnMapping.put("bloodType", new TableBuilder.ColumnMapping("Blood Type", null));

        List<Patient> patientList = Collections.singletonList(patient);

        TableBuilder.createTable(name, patientList, columnMapping, 20);
    }

    /**
     * Creates and displays a formatted table showing medical history information.
     * Shows History ID, Date, Diagnosis, and Treatment details.
     * Formats data for multiple medical history records.
     * 
     * @param medicalHistory List of History objects to display
     * @param name Title of the table to be displayed
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
     * Creates and displays a formatted table showing appointment slots.
     * Shows Slot ID, Date, Start Time, End Time, and Status.
     * Sorts slots by date, start time, and status before display.
     * 
     * @param slots List of Slot objects to display
     * @param name Title of the table to be displayed
     */
    public static void displaySlots(List<Slot> slots, String name) {
        // Sort the slots by Date, Start Time, and Status
        List<Slot> sortedSlots = DoctorController.sortSlots(slots);

        // Display the sorted slots
        LinkedHashMap<String, TableBuilder.ColumnMapping> columnMapping = new LinkedHashMap<>();
        columnMapping.put("slotID", new TableBuilder.ColumnMapping("Slot ID", null));
        columnMapping.put("date", new TableBuilder.ColumnMapping("Date", null));
        columnMapping.put("startTime", new TableBuilder.ColumnMapping("Start Time", null));
        columnMapping.put("endTime", new TableBuilder.ColumnMapping("End Time", null));
        columnMapping.put("status", new TableBuilder.ColumnMapping("Status", null));

        TableBuilder.createTable(name, sortedSlots, columnMapping, 20);
    }

    /**
     * Creates and displays a formatted table showing a single appointment slot.
     * Shows Slot ID, Date, Start Time, End Time, and Status.
     * Formats data for a single slot record.
     * 
     * @param slot Single Slot object to display
     * @param name Title of the table to be displayed
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
     * Creates and displays a formatted table showing appointment information.
     * Shows Appointment ID, Patient details, Doctor details, Schedule, Status, and Outcome ID.
     * Includes name lookups for patient and doctor IDs.
     * 
     * @param appointments List of Appointment objects to display
     * @param name Title of the table to be displayed
     */
    private void displayAppointments(List<Appointment> appointments, String name) {
        LinkedHashMap<String, TableBuilder.ColumnMapping> columnMapping = new LinkedHashMap<>();

        // Appointment ID column
        columnMapping.put("appointmentID", new TableBuilder.ColumnMapping("Appointment ID", null));

        // Patient column with name and ID
        columnMapping.put("patientID", new TableBuilder.ColumnMapping("Patient (ID)",
                value -> {
                    String patientId = value.toString();
                    Patient patient = doctorController.getPatientByID(patientId);
                    return patient != null ? patient.getName() + " (" + patientId + ")" : "Unknown";
                }));

        // Doctor column with name and ID
        columnMapping.put("doctorID", new TableBuilder.ColumnMapping("Doctor (ID)",
                value -> {
                    String doctorId = value.toString();
                    Doctor doctor = doctorController.getDoctorByID(doctorId);
                    return doctor != null ? doctor.getName() + " (" + doctorId + ")" : "Unknown";
                }));

        // Time column combining date and time from slot
        columnMapping.put("slotID", new TableBuilder.ColumnMapping("Schedule",
                value -> {
                    String slotId = value.toString();
                    Slot slot = doctorController.getSlotByID(slotId);
                    if (slot != null) {
                        return slot.getDate() + " " + slot.getStartTime() + "-" + slot.getEndTime();
                    }
                    return "Unknown";
                }));

        columnMapping.put("status", new TableBuilder.ColumnMapping("Status", null));
        columnMapping.put("outcomeID", new TableBuilder.ColumnMapping("Outcome ID", null));

        TableBuilder.createTable(name, appointments, columnMapping, 25);
    }

    /**
     * Creates and displays a formatted table showing outcome information.
     * Shows Outcome ID, Appointment ID, Service Provided, Medications, and Consultation Notes.
     * Formats data for multiple outcome records.
     * 
     * @param outcomes List of Outcome objects to display
     * @param name Title of the table to be displayed
     */
    private void displayOutcome(List<Outcome> outcomes, String name) {
        LinkedHashMap<String, TableBuilder.ColumnMapping> columnMapping = new LinkedHashMap<>();
        columnMapping.put("outcomeID", new TableBuilder.ColumnMapping("Outcome ID", null));
        columnMapping.put("appointmentID", new TableBuilder.ColumnMapping("Appointment ID", null));
        columnMapping.put("serviceProvided", new TableBuilder.ColumnMapping("Service Provided", null));
        columnMapping.put("prescriptionID", new TableBuilder.ColumnMapping("Medication(s)", null));
        columnMapping.put("consultationNotes", new TableBuilder.ColumnMapping("Consulatation Notes", null));

        TableBuilder.createTable(name, outcomes, columnMapping, 20);
    }

    /**
     * Creates and displays a formatted table showing a single outcome record.
     * Shows Outcome ID, Appointment ID, Service Provided, Medications, and Consultation Notes.
     * Formats data for a single outcome record.
     * 
     * @param outcome Single Outcome object to display
     * @param name Title of the table to be displayed
     */
    private void displayOutcome(Outcome outcome, String name) {
        LinkedHashMap<String, TableBuilder.ColumnMapping> columnMapping = new LinkedHashMap<>();
        columnMapping.put("outcomeID", new TableBuilder.ColumnMapping("Outcome ID", null));
        columnMapping.put("appointmentID", new TableBuilder.ColumnMapping("Appointment ID", null));
        columnMapping.put("serviceProvided", new TableBuilder.ColumnMapping("Service Provided", null));
        columnMapping.put("prescriptionID", new TableBuilder.ColumnMapping("Medication(s)", null));
        columnMapping.put("consultationNotes", new TableBuilder.ColumnMapping("Consulatation Notes", null));

        List<Outcome> outcomeList = Collections.singletonList(outcome);

        TableBuilder.createTable(name, outcomeList, columnMapping, 20);
    }

    /**
     * Creates and displays a formatted table showing prescription information.
     * Shows Prescription ID, Appointment ID, Medication ID, Quantity, Status, and Notes.
     * Formats data for multiple prescription records.
     * 
     * @param prescriptions List of Prescription objects to display
     * @param name Title of the table to be displayed
     */
    private void displayPrescriptions(List<Prescription> prescriptions, String name) {
        LinkedHashMap<String, TableBuilder.ColumnMapping> columnMapping = new LinkedHashMap<>();
        // prescriptionID,appointmentID,medicationID,quantity,status,notes
        columnMapping.put("prescriptionID", new TableBuilder.ColumnMapping("Prescription ID", null));
        columnMapping.put("appointmentID", new TableBuilder.ColumnMapping("Appointment ID", null));
        columnMapping.put("medicationID", new TableBuilder.ColumnMapping("Medication ID", null));
        columnMapping.put("quantity", new TableBuilder.ColumnMapping("Quantity Provided", null));
        columnMapping.put("status", new TableBuilder.ColumnMapping("Prescription Status", null));
        columnMapping.put("notes", new TableBuilder.ColumnMapping("Prescription Notes", null));

        TableBuilder.createTable(name, prescriptions, columnMapping, 20);
    }

    /**
     * Creates and displays a formatted table showing medication information.
     * Shows Medication ID, Name, and Quantity Remaining in stock.
     * Formats data for multiple medication records.
     * 
     * @param medication List of Medication objects to display
     * @param name Title of the table to be displayed
     */
    private void displayMedication(List<Medication> medication, String name) {
        LinkedHashMap<String, TableBuilder.ColumnMapping> columnMapping = new LinkedHashMap<>();
        columnMapping.put("medicationID", new TableBuilder.ColumnMapping("Medication ID", null));
        columnMapping.put("name", new TableBuilder.ColumnMapping("Medication Name", null));
        columnMapping.put("stockLevel", new TableBuilder.ColumnMapping("Quantity Remaining", null));

        TableBuilder.createTable(name, medication, columnMapping, 20);
    }

}
