package boundary;

import controller.*;
import entity.*;
import utility.*;
import java.util.Scanner;
import java.util.List;
import java.util.InputMismatchException;
import java.util.LinkedHashMap;
import java.util.Collections;

/**
 * Interface for patient interactions in the Hospital Management System.
 * This class provides a menu-driven interface for patients to interact with the system.
 * It handles operations like viewing medical records, scheduling appointments, and updating personal information.
 *
 * @author Group 7
 * @version 1.0
 */
public class PatientMenu {

    private PatientController patientController;
    private Scanner scanner;

    /**
     * Constructor for PatientMenu.
     * Initializes the menu with a PatientController instance and creates a scanner for user input.
     * This allows the menu to interact with the business logic layer through the controller.
     * 
     * @param patientController The PatientController instance to handle business logic
     */
    public PatientMenu(PatientController patientController) {
        this.patientController = patientController;
        this.scanner = new Scanner(System.in);
    }

    /**
     * [MAIN MENU] Displays the main menu options available to the patient and handles user input.
     * Takes in the user input and navigates to the correct function based on selection.
     * Provides error handling for invalid inputs and continues to loop until user logs out.
     * The menu includes options for viewing records, managing appointments, and updating information.
     */
    public void displayMenu() {
        while (true) {
            try {
                ConsoleUtility.printHeader("PATIENT MENU");
                System.out.println("{1} View Medical Record"); // ok (include alittle bit more)
                System.out.println("{2} Update Personal Information"); // ok
                System.out.println("{3} View Available Appointment Slots");// ok
                System.out.println("{4} Schedule an Appointment");// ok
                System.out.println("{5} Reschedule an Appointment"); // ok
                System.out.println("{6} Cancel an Appointment");// ok
                System.out.println("{7} View Scheduled Appointments"); // ok
                System.out.println("{8} View Past Appointment Outcome Records"); // ok
                System.out.println("{9} Logout"); // ok
                System.out.print("Enter your choice: ");

                /**
                 * choice takes in the user's input and navigates to the correct action choosen
                 * by the user
                 */
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        viewMedicalRecord();
                        break;
                    case 2:
                        updatePersonalInformation();
                        break;
                    case 3:
                        viewAvailableAppointmentSlots();
                        break;
                    case 4:
                        scheduleAppointment();
                        break;
                    case 5:
                        rescheduleAppointment();
                        break;
                    case 6:
                        cancelAppointment();
                        break;
                    case 7:
                        viewScheduledAppointments();
                        break;
                    case 8:
                        viewPastAppointmentOutcomeRecords();
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
     * [OPTION 1] Views and displays the patient's complete medical record.
     * Retrieves the current patient's information and medical history from the controller.
     * Displays both patient details and medical history in a formatted table view.
     */
    private void viewMedicalRecord() {
        ConsoleUtility.printHeader("VIEW MEDICAL RECORD");
        
        // Get current patient
        Patient currentPatient = (Patient) patientController.getPatientByID(patientController.getCurrentUserID());
        
        // Display patient details
        displayPatientInfo(currentPatient, "PATIENT INFORMATION");
        
        // Get and display medical history
        List<History> histories = patientController.getPatientMedicalHistory();
        System.out.println("");
        displayMadicalRecords(histories);
    }

    /**
     * Displays the patient's medical history records in a formatted table.
     * Creates a table with columns for History ID, Diagnosis, Diagnosis Date, and Treatment.
     * Uses TableBuilder utility to format and display the data in a readable manner.
     * 
     * @param history The list of History objects representing the patient's medical history records
     */
    private void displayMadicalRecords(List<History> history) {
        LinkedHashMap<String, TableBuilder.ColumnMapping> columnMapping = new LinkedHashMap<>();
        columnMapping.put("historyID", new TableBuilder.ColumnMapping("History ID", null));
        columnMapping.put("diagnosis", new TableBuilder.ColumnMapping("Diagnosis", null));
        columnMapping.put("diagnosisDate", new TableBuilder.ColumnMapping("Diagnosis Date", null));
        columnMapping.put("treatment", new TableBuilder.ColumnMapping("Treatment", null));

        TableBuilder.createTable("Medical History", history, columnMapping, 20);
    }

    /**
     * Displays patient information in a formatted table view.
     * Creates a table with columns for patient details including ID, name, DOB, gender, etc.
     * Uses TableBuilder utility to ensure consistent and readable formatting.
     * 
     * @param patient The patient whose information to display
     * @param title The title for the information table
     */
    private void displayPatientInfo(Patient patient, String title) {
        LinkedHashMap<String, TableBuilder.ColumnMapping> columnMapping = new LinkedHashMap<>();
        columnMapping.put("userID", new TableBuilder.ColumnMapping("Patient ID", null));
        columnMapping.put("name", new TableBuilder.ColumnMapping("Name", null));
        columnMapping.put("dateOfBirth", new TableBuilder.ColumnMapping("Date of Birth", null));
        columnMapping.put("gender", new TableBuilder.ColumnMapping("Gender", null));
        columnMapping.put("contactNumber", new TableBuilder.ColumnMapping("Contact Number", null));
        columnMapping.put("emailAddress", new TableBuilder.ColumnMapping("Email Address", null));
        columnMapping.put("bloodType", new TableBuilder.ColumnMapping("Blood Type", null));

        List<Patient> patientList = Collections.singletonList(patient);
        TableBuilder.createTable(title, patientList, columnMapping, 20);
    }

    /**
     * [OPTION 2] Allows patients to update their personal information.
     * Provides options to change password, contact number, and email address.
     * Displays current information before and after updates for verification.
     */
    private void updatePersonalInformation() {
        while (true) {
            ConsoleUtility.printHeader("UPDATE PERSONAL INFORMATION");
            
            // Get current patient and display information in table format
            Patient currentPatient = (Patient) patientController.getPatientByID(patientController.getCurrentUserID());
            displayPatientInfo(currentPatient, "CURRENT INFORMATION");

            System.out.println("\nSelect the information to update:");
            System.out.println("{1} Change password");
            System.out.println("{2} Change contact number");
            System.out.println("{3} Change email address");
            System.out.println("{4} Back to main menu");
            
            int updateChoice = ConsoleUtility.validateIntegerInput("Enter your choice: ");

            switch (updateChoice) {
                case 1:
                    String newPassword = ConsoleUtility.validateInput("Enter new password: ",
                            ConsoleUtility::isValidBasicInput);
                    patientController.updatePassword(newPassword);
                    System.out.println("Password updated successfully.");
                    break;

                case 2:
                    String newContactNumber = ConsoleUtility.validateInput("Enter new contact number: ",
                            ConsoleUtility::isValidContactNumber);
                    patientController.updateContactNumber(newContactNumber);
                    
                    // Show updated information
                    System.out.println("");
                    Patient updatedPatient = (Patient) patientController.getPatientByID(patientController.getCurrentUserID());
                    displayPatientInfo(updatedPatient, "UPDATED INFORMATION");
                    break;

                case 3:
                    String newEmailAddress = ConsoleUtility.validateInput("Enter new email address: ",
                            ConsoleUtility::isValidEmail);
                    patientController.updateEmailAddress(newEmailAddress);
                    
                    // Show updated information
                    System.out.println("");
                    Patient updatedPatientEmail = (Patient) patientController.getPatientByID(patientController.getCurrentUserID());
                    displayPatientInfo(updatedPatientEmail, "UPDATED INFORMATION");
                    break;

                case 4:
                    System.out.println("\nReturning to Main Menu...");
                    return;

                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 4.");
            }
        }
    }

    /**
     * [OPTION 3] Displays all available appointment slots for doctors.
     * Shows a list of doctors and their available time slots.
     * Allows patients to view slots for a specific doctor by selecting their ID.
     */
    private void viewAvailableAppointmentSlots() {
        ConsoleUtility.printHeader("VIEW AVAILABLE APPOINTMENT SLOTS");

        // Get all doctors
        List<Doctor> doctors = patientController.getAllDoctors();

        // Check if there are any doctors in the system
        if (doctors.isEmpty()) {
            System.out.println("No doctors are currently available in the system.");
            return;
        }

        // Display the list of doctors
        displayDoctorList(doctors);
        System.out.println("");

        // Prompt the user to select a doctor
        String selectedDoctorID = ConsoleUtility.validateInput("Enter the Doctor ID to view available slots: ",
                input -> {
                    boolean exists = doctors.stream().anyMatch(d -> d.getUserID().equals(input));
                    if (!exists) {
                        System.out.println("Invalid ID, doctor does not exist.");
                    }
                    return exists;
                });

        // Get the doctor object based on the selected doctor ID
        Doctor selectedDoctor = patientController.getDoctorByID(selectedDoctorID);

        // Check if the doctor ID is valid
        if (selectedDoctor == null) {
            System.out.println("Invalid Doctor ID. Please try again.");
            return;
        }

        // Get the available slots for the selected doctor
        List<Slot> availableSlots = patientController.getAvailableSlotsForDoctor(selectedDoctorID);

        // Check if there are any available slots for the selected doctor
        if (availableSlots.isEmpty()) {
            System.out.println("\nNo available slots for the selected doctor.");
            return;
        }

        // Display available slots
        System.out.println("");
        displayAvailableSlots(availableSlots);
    }

    /**
     * Displays a list of doctors in a formatted table view.
     * Shows doctor information including ID, name, specialization, and contact details.
     * Uses TableBuilder utility to create a consistent and readable format.
     * 
     * @param doctors The list of Doctor objects to display
     */
    private void displayDoctorList(List<Doctor> doctors) {
        LinkedHashMap<String, TableBuilder.ColumnMapping> columnMapping = new LinkedHashMap<>();
        columnMapping.put("userID", new TableBuilder.ColumnMapping("Doctor ID", null));
        columnMapping.put("name", new TableBuilder.ColumnMapping("Name", null));
        columnMapping.put("specialization", new TableBuilder.ColumnMapping("Specialization", null));
        columnMapping.put("contactNumber", new TableBuilder.ColumnMapping("Contact Number", null));
        columnMapping.put("emailAddress", new TableBuilder.ColumnMapping("Email Address", null));

        TableBuilder.createTable("Available Doctors", doctors, columnMapping, 20);
    }

    /**
     * Displays available appointment slots in a formatted table view.
     * Shows slot information including ID, date, and time details.
     * Uses TableBuilder utility to create a consistent and readable format.
     * 
     * @param slots The list of Slot objects to display
     */
    private void displayAvailableSlots(List<Slot> slots) {
        LinkedHashMap<String, TableBuilder.ColumnMapping> columnMapping = new LinkedHashMap<>();
        columnMapping.put("slotID", new TableBuilder.ColumnMapping("Slot ID", null));
        columnMapping.put("date", new TableBuilder.ColumnMapping("Date", null));
        columnMapping.put("startTime", new TableBuilder.ColumnMapping("Start Time", null));
        columnMapping.put("endTime", new TableBuilder.ColumnMapping("End Time", null));

        TableBuilder.createTable("Available Slots", slots, columnMapping, 15);
    }

    /**
     * [OPTION 4] Handles the process of scheduling a new appointment.
     * Guides the user through selecting a doctor and available time slot.
     * Confirms appointment details before finalizing the scheduling.
     * Displays updated appointment information after successful scheduling.
     */
    private void scheduleAppointment() {
        ConsoleUtility.printHeader("SCHEDULE AN APPOINTMENT");

        // Get all doctors
        List<Doctor> doctors = patientController.getAllDoctors();
        if (doctors.isEmpty()) {
            System.out.println("No doctors are currently available in the system.");
            return;
        }

        // Display doctor list
        displayDoctorList(doctors);

        // Prompt the user to select a doctor
        System.out.println("");
        String selectedDoctorID = ConsoleUtility.validateInput(
                "Enter the Doctor ID to schedule with (or press Enter to go back): ",
                input -> {
                    boolean exists = doctors.stream().anyMatch(d -> d.getUserID().equals(input));
                    if (!exists) {
                        if (input.isEmpty()) {
                            return true;
                        }
                        System.out.println("Invalid ID, doctor does not exist.");
                    }
                    return exists || input.isEmpty();
                });

        if (selectedDoctorID.isEmpty()) {
            System.out.println("\nReturning to Main Menu...");
            return;
        }

        // Get available slots for selected doctor
        List<Slot> availableSlots = patientController.getAvailableSlotsForDoctor(selectedDoctorID);
        if (availableSlots.isEmpty()) {
            System.out.println("\nNo available slots for the selected doctor.");
            return;
        }

        // Display available slots
        System.out.println("");
        displayAvailableSlots(availableSlots);

        // Prompt for slot selection
        System.out.println("");
        String selectedSlotID = ConsoleUtility.validateInput(
                "Enter the Slot ID to schedule (or press Enter to go back): ",
                input -> {
                    boolean exists = availableSlots.stream().anyMatch(s -> s.getSlotID().equals(input));
                    if (!exists) {
                        if (input.isEmpty()) {
                            return true;
                        }
                        System.out.println("Invalid ID, slot does not exist.");
                    }
                    return exists || input.isEmpty();
                });

        if (selectedSlotID.isEmpty()) {
            System.out.println("\nReturning to Main Menu...");
            return;
        }

        // Get selected objects
        Doctor selectedDoctor = patientController.getDoctorByID(selectedDoctorID);
        Slot selectedSlot = patientController.getSlotByID(selectedSlotID);

        // Verify appointment information
        System.out.println("\nAppointment Details:");
        System.out.println("-".repeat(50));
        System.out.printf("Doctor: %s%n", selectedDoctor.getName());
        System.out.printf("Date: %s%n", selectedSlot.getDate());
        System.out.printf("Time: %s - %s%n", selectedSlot.getStartTime(), selectedSlot.getEndTime());
        System.out.println("-".repeat(50));

        if (!ConsoleUtility.getConfirmation("\nDo you want to confirm this appointment?")) {
            System.out.println("Appointment scheduling cancelled.");
            return;
        }

        // Schedule the appointment
        boolean success = patientController.scheduleAppointment(selectedDoctorID, selectedSlotID);
        if (success) {
            System.out.println("Appointment scheduled successfully. Waiting for doctor's approval.");
            
            // Display updated appointment information
            List<Appointment> updatedAppointments = patientController.getFilteredAppointments("REQUESTED");
            System.out.println("");
            displayAppointments(updatedAppointments);
        } else {
            System.out.println("Failed to schedule the appointment. Please try again later.");
        }
    }

    /**
     * [OPTION 5] Handles the process of rescheduling an existing appointment.
     * Allows patients to select a confirmed appointment and choose a new time slot.
     * Verifies the new appointment details before confirming the change.
     * Updates and displays the rescheduled appointment information.
     */
    private void rescheduleAppointment() {
        ConsoleUtility.printHeader("RESCHEDULE AN APPOINTMENT");

        // Fetch scheduled appointments
        List<Appointment> appointments = patientController.getFilteredAppointments("REQUESTED");

        // Display scheduled appointments
        displayAppointments(appointments);

        // Prompt the user to select an appointment to reschedule
        System.out.println("");
        String appointmentID = ConsoleUtility.validateInput("Enter the Appointment ID to reschedule: ",
                input -> {
                    boolean exists = appointments.stream().anyMatch(a -> a.getAppointmentID().equals(input));
                    if (!exists) {
                        System.out.println("Invalid Appointment ID, appointment does not exist.");
                    }
                    return exists;
                });

        // Retrieve the appointment from the data manager
        Appointment appointment = patientController.getAppointmentByID(appointmentID);

        // Check if the appointment ID is valid
        if (appointment == null) {
            System.out.println("Invalid Appointment ID, appointment does not exist.");
            return;
        }

        // Check if the appointment is in a reschedulable status
        if (appointment.getStatus() != Appointment.AppointmentStatus.CONFIRMED &&
                appointment.getStatus() != Appointment.AppointmentStatus.REQUESTED) {
            System.out.println("Appointment cannot be rescheduled as it is not in a reschedulable status.");
            return;
        }

        // Get the available slots for the selected doctor
        List<Slot> availableSlots = patientController.getAvailableSlotsForDoctor(appointment.getDoctorID());
        if (availableSlots.isEmpty()) {
            System.out.println("\nNo available slots for the selected doctor.");
            return;
        }

        // Display available slots
        System.out.println("");
        displayAvailableSlots(availableSlots);

        // Ask the user to select a new slot
        System.out.println("");
        String selectedSlotID = ConsoleUtility.validateInput("Enter the Slot ID to reschedule to: ",
                input -> {
                    boolean exists = availableSlots.stream().anyMatch(s -> s.getSlotID().equals(input));
                    if (!exists) {
                        System.out.println("Invalid Slot ID, slot does not exist.");
                    }
                    return exists;
                });

        // Validate the selected slot
        Slot selectedSlot = patientController.getSlotByID(selectedSlotID);
        if (selectedSlot == null || !availableSlots.contains(selectedSlot)) {
            System.out.println("Invalid Slot ID. Please try again.");
            return;
        }

        // Confirm appointment details
        System.out.println("\nNew Appointment Details:");
        System.out.println("Doctor: " + patientController.getDoctorByID(appointment.getDoctorID()).getName());
        System.out.println("Date: " + selectedSlot.getDate());
        System.out.println("Time: " + selectedSlot.getStartTime() + " - " + selectedSlot.getEndTime());

        // Check if the user wants to confirm the rescheduling
        if (!ConsoleUtility.getConfirmation("\nDo you want to confirm this rescheduling?")) {
            System.out.println("Rescheduling cancelled.");
            return;
        }

        // Reschedule the appointment
        boolean success = patientController.rescheduleAppointment(appointment, selectedSlotID);
        if (success) {
            System.out.println("Appointment rescheduled successfully. Waiting for doctor's approval.");
            
            // Display updated appointment information
            List<Appointment> updatedAppointments = patientController.getFilteredAppointments("REQUESTED");
            System.out.println("");
            displayAppointments(updatedAppointments);
        } else {
            System.out.println("Failed to reschedule the appointment. Please try again later.");
        }
    }

    /**
     * Displays appointments in a formatted table view.
     * Shows comprehensive appointment information including IDs, patient and doctor details, schedule, and status.
     * Uses TableBuilder utility to create a consistent and readable format.
     * 
     * @param appointments The list of Appointment objects to display
     */
    private void displayAppointments(List<Appointment> appointments) {
        LinkedHashMap<String, TableBuilder.ColumnMapping> columnMapping = new LinkedHashMap<>();

        // Appointment ID column
        columnMapping.put("appointmentID", new TableBuilder.ColumnMapping("Appointment ID", null));

        // Patient column with name and ID
        columnMapping.put("patientID", new TableBuilder.ColumnMapping("Patient (ID)",
                value -> {
                    String patientId = value.toString();
                    User patient = patientController.getUserByID(patientId);
                    return patient != null ? patient.getName() + " (" + patientId + ")" : "Unknown";
                }));

        // Doctor column with name and ID
        columnMapping.put("doctorID", new TableBuilder.ColumnMapping("Doctor (ID)",
                value -> {
                    String doctorId = value.toString();
                    Doctor doctor = patientController.getDoctorByID(doctorId);
                    return doctor != null ? doctor.getName() + " (" + doctorId + ")" : "Unknown";
                }));

        // Time column combining date and time from slot
        columnMapping.put("slotID", new TableBuilder.ColumnMapping("Schedule",
                value -> {
                    String slotId = value.toString();
                    Slot slot = patientController.getSlotByID(slotId);
                    if (slot != null) {
                        return slot.getDate() + " " + slot.getStartTime() + "-" + slot.getEndTime();
                    }
                    return "Unknown";
                }));

        columnMapping.put("status", new TableBuilder.ColumnMapping("Status", null));

        TableBuilder.createTable("Appointment List", appointments, columnMapping, 25);
    }

    /**
     * [OPTION 6] Handles the process of canceling an existing appointment.
     * Displays a list of scheduled appointments and allows the patient to select one to cancel.
     * Validates the appointment status and updates both appointment and slot status upon cancellation.
     * Provides appropriate feedback messages throughout the cancellation process.
     */
    private void cancelAppointment() {
        ConsoleUtility.printHeader("CANCEL AN APPOINTMENT");

        // Fetch scheduled appointments
        List<Appointment> appointments = patientController.getFilteredAppointments("SCHEDULED");

        // Display scheduled appointments
        displayAppointments(appointments);

        // Prompt the user to select an appointment to cancel
        System.out.println("");
        String appointmentID = ConsoleUtility.validateInput("Enter the Appointment ID to cancel: ",
                input -> {
                    boolean exists = appointments.stream()
                            .anyMatch(a -> a.getAppointmentID().equals(input));
                    if (!exists) {
                        System.out.println("Invalid Appointment ID, appointment does not exist.");
                    }
                    return exists;
                });

        // Retrieve the appointment object based on the selected appointment ID
        Appointment appointment = patientController.getAppointmentByID(appointmentID);

        // Check if the appointment object is valid
        if (appointment == null) {
            System.out.println("Invalid Appointment ID, appointment does not exist.");
            return;
        }

        // Check if the appointment is in a cancellable status
        if (appointment.getStatus() != Appointment.AppointmentStatus.CONFIRMED &&
                appointment.getStatus() != Appointment.AppointmentStatus.REQUESTED) {
            System.out.println("Appointment cannot be cancelled as it is not confirmed or requested.");
            return;
        }

        // Update the appointment status to CANCELLED
        appointment.setStatus(Appointment.AppointmentStatus.CANCELLED);
        System.out.println("Appointment " + appointmentID + " has been cancelled.");

        // Make the slot available for booking again
        Slot slot = patientController.getSlotByID(appointment.getSlotID());
        if (slot != null) {
            slot.setStatus(Slot.SlotStatus.AVAILABLE);
        }
    }

    /**
     * [OPTION 7] Displays all currently scheduled appointments for the patient.
     * Retrieves and shows a list of appointments that are in the SCHEDULED status.
     * Uses the displayAppointments method to present the information in a formatted table.
     */
    private void viewScheduledAppointments() {
        ConsoleUtility.printHeader("VIEW SCHEDULED APPOINTMENTS");
        List<Appointment> appointments = patientController.getFilteredAppointments("SCHEDULED"); // Fetch scheduled
        displayAppointments(appointments); // Pass the appointments to the display method
    }

    /**
     * [OPTION 8] Allows patients to view detailed outcome records of past appointments.
     * Displays a list of completed appointments and prompts the user to select one for detailed viewing.
     * Shows comprehensive information including appointment details, outcome, and any prescriptions.
     * Provides an option to return to the main menu without selecting an appointment.
     */
    private void viewPastAppointmentOutcomeRecords() {
        ConsoleUtility.printHeader("VIEW PAST APPOINTMENT OUTCOME RECORDS");

        // Get and display past appointments
        List<Appointment> appointments = patientController.getPastAppointments();
        if (appointments.isEmpty()) {
            System.out.println("No past appointments found.");
            return;
        }

        displayAppointments(appointments);

        // Prompt user to select an appointment
        System.out.println("");
        String appointmentID = ConsoleUtility.validateInput(
                "Enter the Appointment ID to view outcome (or press Enter to go back): ",
                input -> {
                    boolean exists = appointments.stream()
                            .anyMatch(a -> a.getAppointmentID().equals(input) && a.getOutcomeID() != null);
                    if (!exists) {
                        if (input.isEmpty()) {
                            return true;
                        }
                        System.out.println("Invalid ID, appointment does not exist or has no outcome.");
                    }
                    return exists || input.isEmpty();
                });

        if (appointmentID.isEmpty()) {
            System.out.println("\nReturning to Main Menu...");
            return;
        }

        // Display the outcome for the selected appointment
        Outcome outcome = patientController.getOutcomeByAppointmentID(appointmentID);
        if (outcome == null) {
            System.out.println("No outcome record found for this appointment.");
            return;
        }

        displayOutcome(outcome);
    }

    /**
     * Displays detailed outcome information for a specific appointment in a formatted view.
     * Shows comprehensive details including appointment info, patient and doctor details,
     * schedule information, outcome details, and prescription information if available.
     * Uses formatted output to present the information in clearly separated sections.
     * 
     * @param outcome The Outcome object containing the appointment outcome details to display
     */
    private void displayOutcome(Outcome outcome) {
        // Get the appointment associated with this outcome
        Appointment appointment = patientController.getAppointmentByOutcomeID(outcome.getOutcomeID());
        if (appointment == null) {
            System.out.println("Error: Could not find appointment details.");
            return;
        }

        // Get slot and user information
        Slot slot = patientController.getSlotByID(appointment.getSlotID());
        User patient = patientController.getUserByID(appointment.getPatientID());
        Doctor doctor = patientController.getDoctorByID(appointment.getDoctorID());

        System.out.println("\nDETAILED APPOINTMENT INFORMATION");
        System.out.println("-".repeat(50));
        System.out.printf("Appointment ID: %s%n", appointment.getAppointmentID());
        System.out.printf("Status: %s%n", appointment.getStatus());

        System.out.println("\nPatient Information:");
        System.out.printf("Patient ID: %s%n", patient.getUserID());
        System.out.printf("Patient Name: %s%n", patient.getName());
        System.out.printf("Contact: %s%n", patient.getContactNumber());

        System.out.println("\nDoctor Information:");
        System.out.printf("Doctor ID: %s%n", doctor.getUserID());
        System.out.printf("Doctor Name: %s%n", doctor.getName());
        System.out.printf("Contact: %s%n", doctor.getContactNumber());

        System.out.println("\nSchedule Information:");
        if (slot != null) {
            System.out.printf("Date: %s%n", slot.getDate());
            System.out.printf("Time: %s - %s%n", slot.getStartTime(), slot.getEndTime());
            System.out.printf("Slot Status: %s%n", slot.getStatus());
        } else {
            System.out.println("Slot information not available");
        }
        System.out.println("-".repeat(50));

        System.out.println("\nOUTCOME INFORMATION");
        System.out.println("-".repeat(50));
        System.out.printf("Outcome ID: %s%n", outcome.getOutcomeID());
        System.out.printf("Service Provided: %s%n", outcome.getServiceProvided());
        System.out.println("\nConsultation Notes:");
        System.out.println(outcome.getConsultationNotes());

        // Display prescription information if available
        String prescriptionID = outcome.getPrescriptionID();
        if (prescriptionID != null && !prescriptionID.isEmpty()) {
            Prescription prescription = patientController.getPrescriptionByID(prescriptionID);
            if (prescription != null) {
                System.out.println("\nPRESCRIPTION DETAILS");
                System.out.println("-".repeat(50));
                System.out.printf("Prescription ID: %s%n", prescription.getPrescriptionID());

                // Get and display medication details
                String[] medicationIDs = prescription.getMedicationID().split(";");
                System.out.println("\nPrescribed Medications:");
                for (String medicationID : medicationIDs) {
                    Medication medication = patientController.getMedicationByID(medicationID);
                    if (medication != null) {
                        System.out.printf("- %s (ID: %s)%n", medication.getName(), medicationID);
                    }
                }

                System.out.printf("Status: %s%n", prescription.getStatus());
                System.out.printf("Notes: %s%n", prescription.getNotes());
            }
        } else {
            System.out.println("\nNo prescription was issued for this appointment.");
        }
        System.out.println("-".repeat(50));
    }

}
