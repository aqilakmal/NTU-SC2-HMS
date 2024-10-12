package boundary;

import controller.*;
import entity.*;
import utility.*;
import java.util.Scanner;
import java.util.List;
import java.util.InputMismatchException;

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
        // TODO: Implement logic to view patient medical records
    }

    /**
     * [OPTION 2] Updates patient medical records.
     */
    private void updatePatientMedicalRecords() {
        // TODO: Implement logic to update patient medical records
    }

    /**
     * [OPTION 3] Views the doctor's personal schedule.
     */
    private void viewPersonalSchedule() {
        // TODO: Implement logic to view personal schedule
    }

    /**
     * [OPTION 4] Sets availability for appointments.
     */
    private void setAvailabilityForAppointments() {
        // TODO: Implement logic to set availability for appointments
    }

    /**
     * [OPTION 5] Manages appointment requests.
     */
    private void manageAppointmentRequests() {
        // TODO: Implement logic to accept or decline appointment requests
    }

    /**
     * [OPTION 6] Views upcoming appointments.
     */
    private void viewUpcomingAppointments() {
        // TODO: Implement logic to view upcoming appointments
    }

    /**
     * [OPTION 7] Records appointment outcome.
     */
    private void recordAppointmentOutcome() {
        // TODO: Implement logic to record appointment outcome
    }
}
