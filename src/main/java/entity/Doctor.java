package entity;

import java.util.List;

/**
 * Represents a doctor with scheduling and patient management capabilities in the Hospital Management System.
 * Extends the User class.
 */
public class Doctor extends User {

    /**
     * Enum representing the possible decisions for an appointment request.
     */
    public enum Decision {
        ACCEPT, DECLINE
    }
    
    /**
     * Doctor's medical specialization.
     */
    private String specialization;
    
    /**
     * Doctor's work schedule.
     */
    private Schedule schedule;
    
    /**
     * List of patients assigned to this doctor.
     */
    private List<Patient> assignedPatients;

    /**
     * Constructor for Doctor.
     * @param userID The unique identifier for the user
     * @param password The password for the user
     * @param role The role of the user
     * @param name The name of the user
     * @param dateOfBirth The date of birth of the user
     * @param gender The gender of the user
     * @param contactNumber The contact number of the user
     * @param emailAddress The email address of the user
     * @param specialization The specialization of the doctor
     */
    public Doctor(String userID, String password, UserRole role, String name, String dateOfBirth, String gender, String contactNumber, String emailAddress, String specialization) {
        super(userID, password, role, name, dateOfBirth, gender, contactNumber, emailAddress);
        this.specialization = specialization;
    }

    /**
     * Get the doctor's specialization.
     * @return String representing the doctor's specialization
     */
    public String getSpecialization() {
        return specialization;
    }

    /**
     * Get the doctor's schedule.
     * @return Schedule object containing the doctor's schedule
     */
    public Schedule getSchedule() {
        return schedule;
    }
    
    /**
     * Get the list of patients assigned to this doctor.
     * @return List of Patient objects assigned to the doctor
     */
    public List<Patient> getAssignedPatients() {
        return assignedPatients;
    }

    /**
     * Update the doctor's specialization.
     * @param newSpecialization The new specialization to set
     */
    public void updateSpecialization(String newSpecialization) {
        this.specialization = newSpecialization;
    }

    /**
     * Update the doctor's schedule.
     * @param newSchedule The new schedule to set
     */
    public void updateSchedule(Schedule newSchedule) {
        this.schedule = newSchedule;
    }

    /**
     * Update the list of patients assigned to this doctor.
     * @param newAssignedPatients The new list of Patient objects to assign to the doctor
     */
    public void updateAssignedPatients(List<Patient> newAssignedPatients) {
        this.assignedPatients = newAssignedPatients;
    }

    /**
     * Retrieves the medical record for a specific patient.
     * @param patientID The ID of the patient whose record is to be viewed
     * @return MedicalRecord object for the specified patient
     */
    public MedicalRecord viewPatientMedicalRecord(String patientID) {
        // TODO: Implement logic to retrieve and return patient's medical record
        return null;
    }
    
    /**
     * Updates the medical record for a specific patient.
     * @param patientID The ID of the patient whose record is to be updated
     * @param updates The new medical data to be added to the record
     * @return boolean indicating whether the update was successful
     */
    public boolean updatePatientMedicalRecord(String patientID, MedicalRecord updates) {
        // TODO: Implement logic to update patient's medical record
        return false;
    }
    
    /**
     * Sets the doctor's availability for appointments.
     * @param availability The new schedule to set
     * @return boolean indicating whether the schedule was successfully set
     */
    public boolean setAvailability(Schedule availability) {
        // TODO: Implement logic to set doctor's availability
        return false;
    }
    
    /**
     * Responds to an appointment request.
     * @param appointmentID The ID of the appointment to respond to
     * @param decision The doctor's decision (ACCEPT or DECLINE)
     * @return boolean indicating whether the response was successfully recorded
     */
    public boolean respondToAppointmentRequest(String appointmentID, Decision decision) {
        // TODO: Implement logic to respond to appointment request
        return false;
    }
    
    /**
     * Records the outcome of a completed appointment.
     * @param appointmentID The ID of the completed appointment
     * @param outcome The outcome record of the appointment
     * @return boolean indicating whether the outcome was successfully recorded
     */
    public boolean recordAppointmentOutcome(String appointmentID, AppointmentOutcomeRecord outcome) {
        // TODO: Implement logic to record appointment outcome
        return false;
    }
    
    // TODO: Add getters and setters for attributes
}
