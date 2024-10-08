package boundary;

import controller.AppointmentController;
import controller.MedicalRecordController;
import entity.Doctor;

/**
 * Interface for doctor functionalities in the Hospital Management System.
 */
public class DoctorMenu {
    
    private AppointmentController appointmentController;
    private MedicalRecordController medicalRecordController;
    private Doctor currentDoctor;
    
    /**
     * Displays the menu options available to the doctor.
     */
    public void displayMenu() {
        // TODO: Implement logic to display doctor menu
    }
    
    /**
     * Handles the doctor's menu selection.
     * @param selection The menu option selected by the doctor
     */
    public void handleUserInput(int selection) {
        // TODO: Implement logic to handle doctor's menu selection
    }
}