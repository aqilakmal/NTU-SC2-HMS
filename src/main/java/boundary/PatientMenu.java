package boundary;

import controller.AppointmentController;
import controller.MedicalRecordController;
import entity.Patient;

/**
 * Interface for patient interactions in the Hospital Management System.
 */
public class PatientMenu {
    
    private AppointmentController appointmentController;
    private MedicalRecordController medicalRecordController;
    private Patient currentPatient;
    
    /**
     * Displays the menu options available to the patient.
     */
    public void displayMenu() {
        // TODO: Implement logic to display patient menu
    }
    
    /**
     * Handles the patient's menu selection.
     * @param selection The menu option selected by the patient
     */
    public void handleUserInput(int selection) {
        // TODO: Implement logic to handle patient's menu selection
    }
}