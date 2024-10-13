package boundary;

import controller.*;
import entity.*;
import utility.*;
import java.util.Scanner;
import java.util.List;
import java.util.InputMismatchException;
import java.util.LinkedHashMap;

/**
 * Interface for patient interactions in the Hospital Management System.
 */
public class PatientMenu {
    
    private PatientController patientController;
    private Scanner scanner;

    /**
     * Constructor for PatientMenu.
     * 
     * @param patientController The PatientController instance
     */
    public PatientMenu(PatientController patientController) {
        this.patientController = patientController;
        this.scanner = new Scanner(System.in);
    }

    /**
     * [MAIN MENU] Displays the menu options available to the patient.
     */
    public void displayMenu() {
        while (true) {
            try {
                ConsoleUtility.printHeader("PATIENT MENU");
                System.out.println("{1} View Medical Record");
                System.out.println("{2} Update Personal Information");
                System.out.println("{3} View Available Appointment Slots");
                System.out.println("{4} Schedule an Appointment");
                System.out.println("{5} Reschedule an Appointment");
                System.out.println("{6} Cancel an Appointment");
                System.out.println("{7} View Scheduled Appointments");
                System.out.println("{8} View Past Appointment Outcome Records");
                System.out.println("{9} Logout");
                System.out.print("Enter your choice: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1: viewMedicalRecord(); break;
                    case 2: updatePersonalInformation(); break;
                    case 3: viewAvailableAppointmentSlots(); break;
                    case 4: scheduleAppointment(); break;
                    case 5: rescheduleAppointment(); break;
                    case 6: cancelAppointment(); break;
                    case 7: viewScheduledAppointments(); break;
                    case 8: viewPastAppointmentOutcomeRecords(); break;
                    case 9: System.out.println("Logging out and returning to home screen..."); return;
                    default: System.out.println("Invalid choice. Please enter a number between 1 and 9.");
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
     * [OPTION 1] Views the patient's medical record.
     */
    private void viewMedicalRecord() {
        // TODO: Implement logic to view medical record
    }

    /**
     * [OPTION 2] Updates the patient's personal information.
     */
    private void updatePersonalInformation() {
        // TODO: Implement logic to update personal information
    }

    /**
     * [OPTION 3] Views available appointment slots.
     */
    private void viewAvailableAppointmentSlots() {
        // TODO: Implement logic to view available appointment slots
    }

    /**
     * [OPTION 4] Schedules a new appointment.
     */
    private void scheduleAppointment() {
        try {
            ConsoleUtility.printHeader("SCHEDULE AN APPOINTMENT", false);

            // Step 1: Display list of doctors
            List<Doctor> doctors = patientController.getAllDoctors();
            if (doctors.isEmpty()) {
                System.out.println("No doctors are currently available in the system.");
                return;
            }

            System.out.println("");
            displayDoctorList(doctors);

            // Step 2: Prompt user to select a doctor
            String selectedDoctorID = ConsoleUtility.validateInput("\nEnter the Doctor ID to schedule with: ",
                    input -> doctors.stream().anyMatch(d -> d.getUserID().equals(input)));

            Doctor selectedDoctor = patientController.getDoctorByID(selectedDoctorID);

            if (selectedDoctor == null) {
                System.out.println("Invalid Doctor ID. Please try again.");
                return;
            }

            // Step 3: Display available slots for the selected doctor
            List<Slot> availableSlots = patientController.getAvailableSlotsForDoctor(selectedDoctorID);
            if (availableSlots.isEmpty()) {
                System.out.println("\nNo available slots for the selected doctor.");
                return;
            }

            displayAvailableSlots(availableSlots);

            // Step 4: Prompt user to select a slot
            String selectedSlotID = ConsoleUtility.validateInput("\nEnter the Slot ID to schedule: ",
                    input -> availableSlots.stream().anyMatch(s -> s.getSlotID().equals(input)));

            Slot selectedSlot = patientController.getSlotByID(selectedSlotID);

            if (selectedSlot == null) {
                System.out.println("Invalid Slot ID. Please try again.");
                return;
            }

            // Step 5: Verify appointment information
            System.out.println("\nAppointment Details:");
            System.out.println("Doctor: " + selectedDoctor.getName());
            System.out.println("Date: " + selectedSlot.getDate());
            System.out.println("Time: " + selectedSlot.getStartTime() + " - " + selectedSlot.getEndTime());

            if (!ConsoleUtility.getConfirmation("Do you want to confirm this appointment?")) {
                System.out.println("Appointment scheduling cancelled.");
                return;
            }

            // Step 6: Schedule the appointment
            boolean success = patientController.scheduleAppointment(selectedDoctorID, selectedSlotID);
            if (success) {
                System.out.println("Appointment scheduled successfully. Waiting for doctor's approval.");
            } else {
                System.out.println("Failed to schedule the appointment. Please try again later.");
            }

        } catch (Exception e) {
            System.err.println("An error occurred while scheduling the appointment: " + e.getMessage());
        }
    }

    /**
     * Displays the list of doctors.
     * 
     * @param doctors The list of doctors to display
     */
    private void displayDoctorList(List<Doctor> doctors) {
        LinkedHashMap<String, TableBuilder.ColumnMapping> columnMapping = new LinkedHashMap<>();
        columnMapping.put("userID", new TableBuilder.ColumnMapping("Doctor ID", null));
        columnMapping.put("name", new TableBuilder.ColumnMapping("Name", null));
        columnMapping.put("specialization", new TableBuilder.ColumnMapping("Specialization", null));

        TableBuilder.createTable("Available Doctors", doctors, columnMapping, 20);
    }

    /**
     * Displays the list of available slots.
     * 
     * @param slots The list of available slots to display
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
     * [OPTION 5] Reschedules an existing appointment.
     */
    private void rescheduleAppointment() {
        // TODO: Implement logic to reschedule an appointment
    }

    /**
     * [OPTION 6] Cancels an existing appointment.
     */
    private void cancelAppointment() {
        // TODO: Implement logic to cancel an appointment
    }

    /**
     * [OPTION 7] Views scheduled appointments.
     */
    private void viewScheduledAppointments() {
        // TODO: Implement logic to view scheduled appointments
    }

    /**
     * [OPTION 8] Views past appointment outcome records.
     */
    private void viewPastAppointmentOutcomeRecords() {
        // TODO: Implement logic to view past appointment outcome records
    }
}
