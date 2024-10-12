package boundary;

import controller.*;
import entity.*;
import utility.*;
import java.util.Scanner;
import java.util.List;
import java.util.InputMismatchException;

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
        // TODO: Implement logic to schedule an appointment
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
