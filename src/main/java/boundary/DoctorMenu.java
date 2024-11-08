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
     * [OPTION 1] Views patient medical records.
     */
    private void viewPatientMedicalRecords() {
        try {
            String name;
            Patient selectedPatient;
            List<Patient> patients; // Display the list of patients under the doctor's care
            while (true) {
                ConsoleUtility.printHeader("VIEW PATIENT MEDICAL RECORDS");
                patients = doctorController.getPatientsUnderCare();
                if (patients.isEmpty()) {
                    System.out.println("\nYou currently have no patients under your care.");
                    return;
                }
                displayPatients(patients, "Patients Under Your Care");

                String patientID = ConsoleUtility.validateInput("\nEnter the Patient ID to view their medical records (or press Enter to go back): ",
                        input -> input.isEmpty() || doctorController.isValidPatientID(input));

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

                displayMedicalHistory(medicalHistory, name + "'s Medical History");
            }

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
            String name;
            Patient selectedPatient;
            List<Patient> patients;
            List<History> medicalHistory;
            // Display the list of patients under the doctor's care
            while (true) {
                ConsoleUtility.printHeader("UPDATE PATIENT MEDICAL RECORDS", false);
                patients = doctorController.getPatientsUnderCare();
                if (patients.isEmpty()) {
                    System.out.println("\nYou currently have no patients under your care.");
                    return;
                }
                System.out.println();
                displayPatients(patients, "Patients Under Your Care");

                String patientID = ConsoleUtility.validateInput("\nEnter the Patient ID to update (or press Enter to go back): ",
                        input -> input.isEmpty() || doctorController.isValidPatientID(input));

                if (patientID.isEmpty()) {
                    return;
                }
                selectedPatient = doctorController.getPatientByID(patientID);
                name = selectedPatient.getName();

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
     * Handles updating a specific medical record.
     *
     * @param history The History object to update
     */
    private void updateMedicalRecord(List<History> medicalHistory, Patient patient) {
        String name = patient.getName();
        String patientID = patient.getUserID();
        History selectedHistory;
        while (true) {
            medicalHistory = doctorController.getPatientMedicalHistory(patientID);
            displayMedicalHistory(medicalHistory, name + "'s Medical History");
            String historyID = ConsoleUtility.validateInput("\nEnter the History ID to update (or press Enter to go back): ",
                    input -> input.isEmpty() || doctorController.isPatientsHistory(patient.getUserID(), input));

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

            System.out.println("Current Treatment: " + selectedHistory.getTreatment());
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
     * Handles removing prescriptions for a doctor.
     *
     * @param medication List of available medications.
     * @param prescriptions List of existing prescriptions.
     * @param prescriptionString String containing medication IDs separated by
     * semicolons.
     * @return Updated prescriptionString after removal.
     */
    private void removeHistory(List<History> medicalHistory, Patient patient) {
        String name = patient.getName();
        String patientID = patient.getUserID();

        try {
            // Display the list of medications and prescriptions
            displayMedicalHistory(medicalHistory, name + "'s Medical History");
            String historyID = ConsoleUtility.validateInput("\nEnter the History ID to remove (or press Enter to go back): ",
                    input -> input.isEmpty() || doctorController.isPatientsHistory(patientID, input));

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
     * [OPTION 3] Views the doctor's personal slot schedule and view the details
     * of a selected slot.
     */
    private void viewPersonalSchedule() {
        try {
            List<Slot> slots = doctorController.getAllSlotsForDoctor();
            Appointment selectedAppointment;
            Patient selectedPatient;
            String patientID;
            List<History> medicalHistory;

            while (true) {

                ConsoleUtility.printHeader("VIEW PERSONAL SCHEDULE");

                if (slots.isEmpty()) {
                    System.out.println("\nYou currently have no slots.");
                    System.out.println("\nReturning to Main Menu...");
                    return;
                }

                displaySlots(slots, "CURRENT SLOTS");
                System.out.println();

                String slotID = ConsoleUtility.validateInput("\nEnter an available Slot ID to view its details (or press Enter to go back): ",
                        input -> input.isEmpty() || doctorController.isValidDoctorSlotID(input));

                Slot selectedSlot = doctorController.getSlotByID(slotID);

                if (slotID.isEmpty()) {
                    System.out.println("\nReturning to Main Menu...");
                    return;
                }

                if (selectedSlot == null) {
                    System.out.println("Error: Invalid Slot ID. Please try again.");
                    continue;
                }

                displaySlots(selectedSlot, "Slot Details");

                Slot.SlotStatus aSlotStatus = selectedSlot.getStatus();

                if (aSlotStatus != Slot.SlotStatus.AVAILABLE) {
                    selectedAppointment = doctorController.getAppointmentBySlotID(slotID);
                    selectedPatient = doctorController.getPatientByAppointment(selectedAppointment);
                    patientID = selectedPatient.getUserID();
                    medicalHistory = doctorController.getPatientMedicalHistory(patientID);
                    displayAppointments(selectedAppointment, "Appointment Details");
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
     * [OPTION 4] Add, remove or update available slots for appointments.
     */
    private void manageAvailabilityForAppointments() {

        try {
            List<Slot> slots;
            List<Slot> availableSlots;
            Boolean availableSlotsBoolean;

            //Display all slots and then available slots
            while (true) {
                ConsoleUtility.printHeader("MANAGE AVAILABILITY FOR APPOINTMENTS");
                //refresh slots after every run to keep the slots updated
                slots = doctorController.getAllSlotsForDoctor();
                if (slots.isEmpty()) {
                    System.out.println("\nYou currently have no slots.");
                } else {
                    displaySlots(slots, "ALL CURRENT SLOTS");

                }
                availableSlots = doctorController.getAvailableSlotsForDoctor();
                availableSlotsBoolean = true;
                if (availableSlots.isEmpty()) {
                    availableSlotsBoolean = false;
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
                        if (availableSlotsBoolean) {
                            updateSlot(availableSlots);
                        } else {
                            System.out.println("\nYou have no available slots to update");
                        }
                        break;
                    case 2:
                        addNewSlot();
                        break;
                    case 3:
                        if (availableSlotsBoolean) {
                            removeSlot(availableSlots);
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
            }
        } catch (Exception e) {
            System.err.println("An error occurred while updating avaialble slots: " + e.getMessage());
            e.printStackTrace();
        }

    }

    /**
     * Handles update slots for doctor.
     */
    private void updateSlot(List<Slot> availableSlots) {
        while (true) {
            try {
                displaySlots(availableSlots, "AVAILABLE SLOTS");
                String slotID = ConsoleUtility.validateInput("\nEnter an available Slot ID to update (or press Enter to go back): ",
                        input -> input.isEmpty() || doctorController.isValidAvailableSlotID(input));

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
     * Handles adding a new slot for a doctor.
     */
    private void addNewSlot() {
        while (true) {
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

    private void removeSlot(List<Slot> availableSlots) {
        try {
            displaySlots(availableSlots, "AVAILABLE SLOTS");
            String slotID = ConsoleUtility.validateInput("\nEnter an available Slot ID to remove (or press Enter to go back): ",
                    input -> input.isEmpty() || doctorController.isValidAvailableSlotID(input));

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
     * [OPTION 5] Accept or Decline appointments/slots with REQUESTED/PENDING
     * status.
     */
    private void manageAppointmentRequests() {
        try {

            List<Slot> pendingSlots;

            while (true) {
                ConsoleUtility.printHeader("MANAGE APPOINTMENT REQUESTS");
                pendingSlots = doctorController.getPendingSlotsForDoctor();
                if (pendingSlots.isEmpty()) {
                    System.out.println("\nYou currently have no pending appointments.");
                    System.out.println("\nReturning to Main Menu...");
                    return;
                }

                displaySlots(pendingSlots, "PENDING APPOINTMENTS");

                String slotID = ConsoleUtility.validateInput("\nEnter an pending Slot ID to accept or decline (or press Enter to go back)[e.g. S01]: ",
                        input -> input.isEmpty() || doctorController.isValidPendingSlotID(input));
                if (slotID.isEmpty()) {
                    System.out.println("\nReturning to Main Menu...");
                    return;
                }
                Slot selectedSlot = doctorController.getSlotByID(slotID);
                if (selectedSlot == null) {
                    System.out.println("Error: Invalid Slot ID. Please try again.");
                    continue;
                }

                Appointment selectedAppointment = doctorController.getAppointmentBySlotID(slotID);
                Patient selectedPatient = doctorController.getPatientByAppointment(selectedAppointment);
                String patientID = selectedPatient.getUserID();
                System.out.println();
                displayPatients(selectedPatient, "REQUESTING PATIENT'S DETAILS");

                List<History> medicalHistory = doctorController.getPatientMedicalHistory(patientID);

                if (medicalHistory.isEmpty()) {
                    System.out.println("This patient has no medical history records.");
                } else {
                    displayMedicalHistory(medicalHistory, "REQUESTING PATIENT'S MEDICAL RECORDS");
                }

                displaySlots(selectedSlot, "SELECTED APPOINTMENT");

                manageAppointment(selectedAppointment, selectedSlot);
            }

        } catch (Exception e) {
            System.err.println("An error occurred while viewing pending appointments: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Accept or Decline a selected appointment/slot
     *
     * @param appointment The appointment to reject or accept
     * @param slot The slot to reject or accept
     */
    private void manageAppointment(Appointment appointment, Slot slot) {

        try {
            Boolean accept;
            String acceptString = ConsoleUtility.validateInput("\nWould you like to accept this appointment?(press Enter to go back) [y/n]: ",
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
     * [OPTION 6] Views upcoming appointments. Similar to [OPTION 3] View
     * Personal Schedule, but only slots with BOOKED status
     */
    private void viewUpcomingAppointments() {
        try {

            List<Slot> bookedSlots;

            while (true) {
                ConsoleUtility.printHeader("VIEW UPCOMING APPOINTMENTS");
                bookedSlots = doctorController.getBookedSlotsForDoctor();
                if (bookedSlots.isEmpty()) {
                    System.out.println("\nYou currently have no upcoming appointments.");
                    System.out.println("\nReturning to Main Menu...");
                    return;
                }
                displaySlots(bookedSlots, "UPCOMING APPOINTMENT");
                String slotID = ConsoleUtility.validateInput("\nEnter an upcoming Slot ID to view its details (or press Enter to go back)[e.g. S01]: ",
                        input -> input.isEmpty() || doctorController.isValidBookedSlotID(input));
                if (slotID.isEmpty()) {
                    System.out.println("\nReturning to Main Menu...");
                    return;
                }

                Slot selectedSlot = doctorController.getSlotByID(slotID);

                if (selectedSlot == null) {
                    System.out.println("Error: Invalid Slot ID. Please try again.");
                    continue;
                }

                Appointment selectedAppointment = doctorController.getAppointmentBySlotID(slotID);
                Patient selectedPatient = doctorController.getPatientByAppointment(selectedAppointment);
                String patientID = selectedPatient.getUserID();
                List<History> medicalHistory = doctorController.getPatientMedicalHistory(patientID);

                displaySlots(selectedSlot, "Slot Details");
                displayAppointments(selectedAppointment, "Appointment Details");
                displayPatients(selectedPatient, "PATIENT'S DETAILS");

                if (medicalHistory.isEmpty()) {
                    System.out.println("This patient has no medical history records.");
                    continue;
                }

                displayMedicalHistory(medicalHistory, "PATIENT'S MEDICAL RECORDS");
            }

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

            List<Slot> bookedSlots;

            while (true) {
                ConsoleUtility.printHeader("RECORD APPOINTMENT OUTCOME");

                bookedSlots = doctorController.getBookedSlotsForDoctor();

                if (bookedSlots.isEmpty()) {
                    System.out.println("\nYou currently have no more booked appointments to record and complete.");
                    System.out.println("\nReturning to Main Menu...");
                    return;
                }
                displaySlots(bookedSlots, "SELECT A BOOKED APPOINTMENT TO COMPLETE");

                String slotID = ConsoleUtility.validateInput("\nEnter an Slot ID (Appointment ID) to complete (or press Enter to go back)[e.g. S01]: ",
                        input -> input.isEmpty() || doctorController.isValidBookedSlotID(input));

                if (slotID.isEmpty()) {
                    System.out.println("\nReturning to Main Menu...");
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
                displayPatients(selectedPatient, "PATIENT'S DETAILS");

                List<History> medicalHistory = doctorController.getPatientMedicalHistory(patientID);

                if (medicalHistory.isEmpty()) {
                    System.out.println("This patient has no medical history records.");
                    return;
                }

                displayMedicalHistory(medicalHistory, "PATIENT'S MEDICAL RECORDS");

                displaySlots(selectedSlot, "SELECTED APPOINTMENT");

                completeAppointment(selectedAppointment, selectedSlot);
            }

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
            String consultationNotes = ConsoleUtility.validateInput("\nEnter consultation notes: ", input -> !input.trim().isEmpty());
            Boolean prescriptionsBoolean = ConsoleUtility.getConfirmation("\nDoes the patient require any prescriptions?");
            String prescriptionString = "";
            if (prescriptionsBoolean) {
                prescriptionString = addPrescription(appointment, prescriptionString, 0);
            } else {
                prescriptionString = "NIL";
            }

            Boolean success = doctorController.completeAppointment(appointment, slot, serviceProvided, prescriptionString, consultationNotes);
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

    /**
     * Creates a Prescription object and returns a string of prescriptionID. For
     * example, "M01;M02;M02"
     *
     * @param appointment The accompanying appointment
     * @param prescriptionID The prescription ID to manage (perhaps using
     * prescription ID instead of medication ID might be clearer)
     * @param count number of medications in current prescription ID, 0 if it is
     * a new appointment record
     * @return the updated prescription ID
     */
    private String addPrescription(Appointment appointment, String prescriptionID, int count) {
        try {
            String appointmentID = appointment.getAppointmentID();
            int quantity;
            String notes;
            String medicationID;
            Boolean success;
            //we use an array list to split and store the medications
            List<String> prescriptionList = new ArrayList<>();

            List<Medication> medication = doctorController.getAllMedication();

            // Initialize prescriptionList if prescriptionID is not empty or "NIL"
            if (!prescriptionID.isEmpty() && !prescriptionID.equals("NIL")) {
                prescriptionList = new ArrayList<>(Arrays.asList(prescriptionID.split(";")));
            }

            do {
                // Display list of medications to the user
                displayMedication(medication, "LIST OF MEDICATIONS");

                // Prompt user to enter medication ID
                medicationID = ConsoleUtility.validateInput(
                        "\nEnter a Medication ID to prescribe to the patient (press Enter to finish prescription) [e.g. M01]: ",
                        input -> input.isEmpty() || doctorController.isValidMedicationID(input));

                // If the input is empty and no medication has been prescribed yet, set prescriptionID to "NIL"
                if (medicationID.isEmpty() && count == 0) {
                    prescriptionID = "NIL";
                    break;
                } else if (medicationID.isEmpty()) {
                    break; // Finish adding prescriptions if user presses Enter
                }

                // Ensure the medicationID is not already in the prescription list
                if (prescriptionList.contains(medicationID)) {
                    System.out.println("This medication has already been prescribed. Please enter a different medication.");
                    continue; // Skip to the next iteration of the loop
                }

                // Add the medicationID to the list to ensure no duplicates
                prescriptionList.add(medicationID);

                // Update the prescriptionID string with the new medication
                if (prescriptionID.equals("NIL")) {
                    prescriptionID = medicationID; // Replace "NIL" with the first medication
                } else if (prescriptionID.isEmpty()) {
                    prescriptionID = medicationID; // First medication, no need for a semicolon
                } else {
                    prescriptionID = prescriptionID + ";" + medicationID; // Subsequent medications
                }

                // Prompt user to enter quantity and notes for the medication
                quantity = ConsoleUtility.validateIntegerInput("Enter quantity: ");
                notes = ConsoleUtility.validateInput("Enter prescription notes: ", input -> !input.trim().isEmpty());

                // Add the prescription to the system through doctorController
                success = doctorController.addPrescription(appointmentID, medicationID, quantity, notes);

                if (success) {
                    System.out.println("Medication successfully added to prescription.");
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
     * [OPTION 8] View and Update Appointment Outcome.
     */
    private void updateAppointmentOutcome() {
        try {
            List<Medication> medication = doctorController.getAllMedication();
            List<Appointment> completedAppointment;
            List<Outcome> outcomes;
            List<Patient> patients; // Display the list of patients under the doctor's care

            while (true) {
                ConsoleUtility.printHeader("VIEW AND UPDATE APPOINTMENT OUTCOME");

                completedAppointment = doctorController.getCompletedAppointmentsForDoctor();
                outcomes = doctorController.getOutcomesForCompletedAppointmentsForDoctor();
                patients = doctorController.getPatientsUnderCare();

                if (completedAppointment.isEmpty()) {
                    System.out.println("\nYou currently have no completed appointments to view or update.");
                    System.out.println("\nReturning to Main Menu...");
                    return;
                }

                displayPatients(patients, "Patients Under Care");
                displayMedication(medication, "LIST OF MEDICATIONS");
                displayOutcome(outcomes, "OUTCOMES");
                displayAppointments(completedAppointment, "SELECT A COMPLETED APPOINTMENT OUTCOME TO UPDATE");

                String appointmentID = ConsoleUtility.validateInput("\nEnter an Appointment ID to update (or press Enter to go back)[e.g. A01]: ",
                        input -> input.isEmpty() || doctorController.isValidCompletedAppointmentID(input));

                if (appointmentID.isEmpty()) {
                    System.out.println("\nReturning to Main Menu...");
                    return;
                }

                updateOutcome(appointmentID);
            }

        } catch (Exception e) {
            System.err.println("An error occurred while viewing upcoming appointments: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Updates an Outcome object.
     *
     * @param appointmentID The appointmentID linked to the outcome to update
     */
    private void updateOutcome(String appointmentID) {
        // Retrieve the selected appointment, outcome, and prescriptions
        Appointment selectedAppointment = doctorController.getAppointmentByID(appointmentID);
        String outcomeID = doctorController.getOutcomeIDFromAppointment(selectedAppointment);
        Outcome selectedOutcome = doctorController.getOutcomeByID(outcomeID);
        List<Prescription> prescriptions = doctorController.getPrescriptionsByAppointmentID(appointmentID);
        Patient patient = doctorController.getPatientFromAppointment(selectedAppointment);

        if (selectedOutcome == null) {
            System.out.println("Error: This appointment does not have an outcome. Please complete this appointment first.");
            return;
        }

        try {
            // Display current patient and outcome information
            displayPatients(patient, "Details of the Affected patient");
            displayOutcome(selectedOutcome, "Details of the current outcome");

            // Update the outcome details: service provided and consultation notes
            System.out.println("\nCurrent Service Provided: " + selectedOutcome.getServiceProvided());
            String newServiceProvided = ConsoleUtility.validateInput(
                    "Enter new service provided (or press Enter to keep current): ",
                    input -> input.isEmpty() || !input.trim().isEmpty());

            // If the input is empty, keep the current value
            if (newServiceProvided.isEmpty()) {
                newServiceProvided = selectedOutcome.getServiceProvided();
            }

            System.out.println("Current Consultation Notes: " + selectedOutcome.getConsultationNotes());
            String newConsultationNotes = ConsoleUtility.validateInput(
                    "Enter new consultation notes (or press Enter to keep current): ",
                    input -> input.isEmpty() || !input.trim().isEmpty());

            // If the input is empty, keep the current value
            if (newConsultationNotes.isEmpty()) {
                newConsultationNotes = selectedOutcome.getConsultationNotes();
            }

            // Display current prescriptions and update if needed
            displayPrescriptions(prescriptions, "CURRENT PRESCRIPTIONS PROVIDED FOR THIS APPOINTMENT");
            Boolean prescriptionsBoolean = ConsoleUtility.getConfirmation("\nDoes the patient require any changes to the prescriptions?");
            String prescriptionString = selectedOutcome.getPrescriptionID();

            if (prescriptionsBoolean) {
                prescriptionString = managePrescriptions(appointmentID, prescriptionString);
            }

            // Update outcome with new or existing values
            Boolean success = doctorController.updateOutcome(selectedOutcome, newServiceProvided, prescriptionString, newConsultationNotes);
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
     * Manages a Prescription object linked to an appointment ID
     *
     * @param appointmentID The ID of the appointment linked to the Prescription
     * object
     * @param prescriptionString The current string of medication. For example,
     * "M01;M02;M02"
     * @return the updated prescription ID used to update outcome
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
                System.out.println("\nYou currently have no prescriptions.");
            } else {
                displayPrescriptions(prescriptions, "PRESCRIPTIONS PROVIDED FOR THIS APPOINTMENT");
            }

            ConsoleUtility.printHeader("AVAILABLE PRESCRIPTIONS FUNCTIONS");
            System.out.println("{1} Update Prescription");
            System.out.println("{2} Add Prescription");
            System.out.println("{3} Remove Prescription");
            System.out.println("{4} Complete Updating of Outcome");

            int choice = ConsoleUtility.validateIntegerInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    if (!prescriptions.isEmpty()) {
                        prescriptionString = updatePrescription(medication, prescriptions, prescriptionString);
                    } else {
                        System.out.println("\nYou have no prescription to update.");
                    }
                    break;
                case 2:
                    prescriptionString = addPrescription(appointment, prescriptionString, count);
                    break;
                case 3:
                    if (!prescriptions.isEmpty()) {
                        prescriptionString = removePrescription(medication, prescriptions, prescriptionString);
                    } else {
                        System.out.println("\nYou have no prescription to remove.");
                    }
                    break;
                case 4:
                    System.out.println("Completing the update process for the outcome.");
                    return prescriptionString; // Exit the loop and method
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 4.");
            }
        }
    }

    /**
     * Handles updating prescriptions for a doctor.
     *
     * @param medication List of available medications.
     * @param prescriptions List of existing prescriptions.
     * @param prescriptionString String containing medication IDs separated by
     * semicolons.
     * @return updated prescriptionString.
     */
    private String updatePrescription(List<Medication> medication, List<Prescription> prescriptions, String prescriptionString) {
        try {
            // Display the available medications and prescriptions
            displayMedication(medication, "LIST OF MEDICATIONS");
            displayPrescriptions(prescriptions, "PRESCRIPTIONS");

            // Get prescription ID from user
            String prescriptionID = ConsoleUtility.validateInput(
                    "\nEnter a prescription ID to update (or press Enter to go back): ",
                    input -> input.isEmpty() || doctorController.isValidPrescriptionID(input));

            if (prescriptionID.isEmpty()) {
                return prescriptionString; // User chose to go back without updating
            }

            // Retrieve the selected prescription
            Prescription selectedPrescription = doctorController.getPrescriptionByID(prescriptionID);
            if (selectedPrescription == null) {
                System.out.println("Error: Invalid Prescription ID. Please try again.");
                return prescriptionString;
            }

            // Extract the prescriptionString into a List of medication IDs
            List<String> prescriptionList = new ArrayList<>();
            if (!prescriptionString.isEmpty() && !prescriptionString.equals("NIL")) {
                prescriptionList = new ArrayList<>(Arrays.asList(prescriptionString.split(";")));
            }

            // Get current details of the selected prescription
            String currentMedicationID = selectedPrescription.getMedicationID();
            int currentQuantity = selectedPrescription.getQuantity();
            String currentNotes = selectedPrescription.getNotes();

            // Display current details and prompt for updates
            displayPrescriptions(selectedPrescription, "SELECTED PRESCRIPTION");
            displayMedication(medication, "LIST OF MEDICATIONS");
            System.out.println("\nCurrent Medication: " + currentMedicationID);
            String newMedicationID = ConsoleUtility.validateInput(
                    "Enter new Medication ID to update (or press Enter to keep current): ",
                    input -> input.isEmpty() || doctorController.isValidMedicationID(input));

            System.out.println("\nCurrent Quantity: " + currentQuantity);
            String quantityInput = ConsoleUtility.validateInput(
                    "Enter new quantity (or press Enter to keep current): ",
                    input -> input.isEmpty() || ConsoleUtility.isValidInteger(input));

            int newQuantity;
            if (quantityInput.isEmpty()) {
                newQuantity = currentQuantity; // Keep current value
            } else {
                try {
                    newQuantity = Integer.parseInt(quantityInput); // Parse to int
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid integer.");
                    newQuantity = currentQuantity; // or some default value
                }
            }

            System.out.println("Current Prescription Notes: " + currentNotes);
            String newNotes = ConsoleUtility.validateInput(
                    "Enter new prescription notes (or press Enter to keep current): ",
                    input -> input.isEmpty() || !input.trim().isEmpty());

            if (newMedicationID.isEmpty() && quantityInput.isEmpty() && newNotes.isEmpty()) {
                System.out.println("No changes made to the prescription.");
                return prescriptionString; // Return the original prescription string if no updates are made
            }

            // Update prescription list only if a new medication ID is provided and it's different from the current
            if (!newMedicationID.isEmpty() && !newMedicationID.equals(currentMedicationID)) {
                if (prescriptionList.contains(newMedicationID)) {
                    System.out.println("This medication has already been prescribed. Please enter a different medication.");
                    return prescriptionString; // Prevent duplicate medication and return original string
                }
                // Replace the old medication ID with the new one
                int index = prescriptionList.indexOf(currentMedicationID);
                if (index != -1) {
                    prescriptionList.set(index, newMedicationID);
                }
            }

            // Update the prescriptionString with the new values from the list and recreates it in the form of medication ID seperated by semicolons ("M01;M02;M02")
            prescriptionString = String.join(";", prescriptionList);

            // Update the prescription in the system
            boolean updated = doctorController.updatePrescription(selectedPrescription, newMedicationID.isEmpty() ? currentMedicationID : newMedicationID, newQuantity, newNotes.isEmpty() ? currentNotes : newNotes);

            if (updated) {
                System.out.println("Prescription updated successfully.");
            } else {
                System.out.println("Failed to update prescription. Please try again.");
            }

        } catch (IllegalArgumentException e) {
            System.err.println("Error updating prescription: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred while updating prescription: " + e.getMessage());
            e.printStackTrace();
        }

        // Return the updated prescription string
        return prescriptionString;
    }

    /**
     * Removes prescriptions and returns the updated prescription string
     *
     * @param medication List of available medications.
     * @param prescriptions List of existing prescriptions.
     * @param prescriptionString String containing medication IDs separated by
     * semicolons.
     * @return updated prescriptionString after removal.
     */
    private String removePrescription(List<Medication> medication, List<Prescription> prescriptions, String prescriptionString) {
        try {
            // Display the list of medications and prescriptions
            displayMedication(medication, "LIST OF MEDICATIONS");
            displayPrescriptions(prescriptions, "PRESCRIPTIONS");

            // Get prescription ID from user
            String prescriptionID = ConsoleUtility.validateInput(
                    "\nEnter a prescription ID to remove (or press Enter to go back): ",
                    input -> input.isEmpty() || doctorController.isValidPrescriptionID(input));

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

                //creates an array to store IDs and manage medication IDs
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

    //-------------------Display Functions--------------------
    /**
     * 1) Creates a table with the details to display: Patient ID, Name, Date of
     * Birth, Gender,Contact Number, Email Adress, Blood Type.<p>
     * 2) Displays a list of patients and their details.
     *
     * @param patients List of Patients Objects to display
     * @param name Name of table to be displayed
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
     * 1) Creates a table with the details to display: Patient ID, Name, Date of
     * Birth, Gender,Contact Number, Email Adress, Blood Type.<p>
     * 2) Displays a selected patient's details.
     *
     * @param patients Patient Object to display
     * @param name Name of table to be displayed
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
     * 1) Creates a table with the details to display: History ID, Date,
     * Diagnosis, Treatment.<p>
     * 2) Displays a list of medical histories and their details.
     *
     * @param medicalHistory List of History Objects to display
     * @param name Name of table to be displayed
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
     * 1) Creates a table with the details to display: Slot ID, Date, Start
     * Time, End Time, Status.<p>
     * 2) Sort the slots by Date, Start Time, and Status.<p>
     * 3) Displays a list of sorted slots and their details.
     *
     * @param slots List of Slot Objects to display
     * @param name Name of table to be displayed
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
     * 1) Creates a table with the details to display: Slot ID, Date, Start
     * Time, End Time, Status.<p>
     * 2) Displays a selected slot's details.
     *
     * @param slots Slot Object to display
     * @param name Name of table to be displayed
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
     * 1) Creates a table with the details to display: Appointment ID, Patient
     * ID, Doctor ID, Slot ID, Status, Outcome ID.<p>
     * 2) Displays a list of appointments and their details.
     *
     * @param appointments List of Appointment Objects to display
     * @param name Name of table to be displayed
     */
    private void displayAppointments(List<Appointment> appointments, String name) {
        LinkedHashMap<String, TableBuilder.ColumnMapping> columnMapping = new LinkedHashMap<>();
        columnMapping.put("appointmentID", new TableBuilder.ColumnMapping("Appointment ID", null));
        columnMapping.put("patientID", new TableBuilder.ColumnMapping("Patient ID", null));
        columnMapping.put("doctorID", new TableBuilder.ColumnMapping("Doctor ID", null));
        columnMapping.put("slotID", new TableBuilder.ColumnMapping("Slot ID", null));
        columnMapping.put("status", new TableBuilder.ColumnMapping("Status", null));
        columnMapping.put("outcomeID", new TableBuilder.ColumnMapping("Outcome ID", null));

        TableBuilder.createTable(name, appointments, columnMapping, 20);
    }

    /**
     * 1) Creates a table with the details to display: Appointment ID, Patient
     * ID, Doctor ID, Slot ID, Status, Outcome ID.<p>
     * 2) Displays a selected appointment's details.
     *
     * @param appointments List of Appointment Objects to display
     * @param name Name of table to be displayed
     */
    private void displayAppointments(Appointment appointment, String name) {
        LinkedHashMap<String, TableBuilder.ColumnMapping> columnMapping = new LinkedHashMap<>();
        columnMapping.put("appointmentID", new TableBuilder.ColumnMapping("Appointment ID", null));
        columnMapping.put("patientID", new TableBuilder.ColumnMapping("Patient ID", null));
        columnMapping.put("doctorID", new TableBuilder.ColumnMapping("Doctor ID", null));
        columnMapping.put("slotID", new TableBuilder.ColumnMapping("Slot ID", null));
        columnMapping.put("status", new TableBuilder.ColumnMapping("Status", null));
        columnMapping.put("outcomeID", new TableBuilder.ColumnMapping("Outcome ID", null));

        List<Appointment> appointmentList = Collections.singletonList(appointment);

        TableBuilder.createTable(name, appointmentList, columnMapping, 20);
    }

    /**
     * 1) Creates a table with the details to display: Outcome ID, Appointment
     * ID, Service Provided, Medication(s), Consulatation Notes.<p>
     * 2) Displays a list of outcomes and their details.
     *
     * @param outcomes List of Outcome Objects to display
     * @param name Name of table to be displayed
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
     * 1) Creates a table with the details to display: Outcome ID, Appointment
     * ID, Service Provided, Medication(s), Consulatation Notes.<p>
     * 2) Displays a selected outcome's details.
     *
     * @param outcome Outcome Object to display
     * @param name Name of table to be displayed
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
     * /**
     * 1) Creates a table with the details to display: Prescription ID,
     * Appointment ID, Medication ID, Quantity Provided, Prescription Status,
     * Prescription Notes.<p>
     * 2) Displays a list of medications and their details.
     *
     * @param prescriptions List of Prescription Objects to display
     * @param name Name of table to be displayed
     */
    private void displayPrescriptions(List<Prescription> prescriptions, String name) {
        LinkedHashMap<String, TableBuilder.ColumnMapping> columnMapping = new LinkedHashMap<>();
        //prescriptionID,appointmentID,medicationID,quantity,status,notes
        columnMapping.put("prescriptionID", new TableBuilder.ColumnMapping("Prescription ID", null));
        columnMapping.put("appointmentID", new TableBuilder.ColumnMapping("Appointment ID", null));
        columnMapping.put("medicationID", new TableBuilder.ColumnMapping("Medication ID", null));
        columnMapping.put("quantity", new TableBuilder.ColumnMapping("Quantity Provided", null));
        columnMapping.put("status", new TableBuilder.ColumnMapping("Prescription Status", null));
        columnMapping.put("notes", new TableBuilder.ColumnMapping("Prescription Notes", null));

        TableBuilder.createTable(name, prescriptions, columnMapping, 20);
    }

    /**
     * 1) Creates a table with the details to display: Prescription ID,
     * Appointment ID, Medication ID, Quantity Provided, Prescription Status,
     * Prescription Notes.<p>
     * 2) Displays a selected prescription's details.
     *
     * @param prescription Prescription Object to display
     * @param name Name of table to be displayed
     */
    private void displayPrescriptions(Prescription prescription, String name) {
        LinkedHashMap<String, TableBuilder.ColumnMapping> columnMapping = new LinkedHashMap<>();
        //prescriptionID,appointmentID,medicationID,quantity,status,notes
        columnMapping.put("prescriptionID", new TableBuilder.ColumnMapping("Prescription ID", null));
        columnMapping.put("appointmentID", new TableBuilder.ColumnMapping("Appointment ID", null));
        columnMapping.put("medicationID", new TableBuilder.ColumnMapping("Medication ID", null));
        columnMapping.put("quantity", new TableBuilder.ColumnMapping("Quantity Provided", null));
        columnMapping.put("status", new TableBuilder.ColumnMapping("Prescription Status", null));
        columnMapping.put("notes", new TableBuilder.ColumnMapping("Prescription Notes", null));

        List<Prescription> prescriptionList = Collections.singletonList(prescription);

        TableBuilder.createTable(name, prescriptionList, columnMapping, 20);
    }

    /**
     * 1) Creates a table with the details to display: Medication ID, Medication
     * Name, Quantity Remaining.<p>
     * 2) Displays a list of medications and their details.
     *
     * @param medication List of Medication Objects to display
     * @param name Name of table to be displayed
     */
    private void displayMedication(List<Medication> medication, String name) {
        LinkedHashMap<String, TableBuilder.ColumnMapping> columnMapping = new LinkedHashMap<>();
        columnMapping.put("medicationID", new TableBuilder.ColumnMapping("Medication ID", null));
        columnMapping.put("name", new TableBuilder.ColumnMapping("Medication Name", null));
        columnMapping.put("stockLevel", new TableBuilder.ColumnMapping("Quantity Remaining", null));

        TableBuilder.createTable(name, medication, columnMapping, 20);
    }

}
