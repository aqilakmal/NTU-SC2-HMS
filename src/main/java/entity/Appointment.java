package entity;

import java.time.LocalDateTime;

/**
 * Represents an appointment between a patient and a doctor in the Hospital Management System.
 */
public class Appointment {

    /**
     * Enum representing the possible statuses of an appointment.
     */
    public enum AppointmentStatus {
        REQUESTED, CONFIRMED, CANCELED, COMPLETED
    }
    
    /**
     * Unique identifier for the appointment.
     */
    private String appointmentID;
    
    /**
     * The id of the patient associated with this appointment.
     */
    private String patientID;
    
    /**
     * The id of the doctor associated with this appointment.
     */
    private String doctorID;
    
    /**
     * The date and time of the appointment.
     */
    private LocalDateTime dateTime;
    
    /**
     * The current status of the appointment.
     */
    private AppointmentStatus status;
    
    /**
     * The outcome record of the appointment (null if not completed).
     */
    private String outcomeID;
    
    /**
     * Constructor for Appointment.
     * @param appointmentID The unique identifier for the appointment
     * @param patientID The id of the patient associated with this appointment
     * @param doctorID The id of the doctor associated with this appointment
     * @param dateTime The date and time of the appointment
     * @param status The current status of the appointment
     * @param outcomeRecord The outcome record of the appointment (null if not completed)
     */
    public Appointment(String appointmentID, String patientID, String doctorID, LocalDateTime dateTime, AppointmentStatus status, String outcomeID) {
        this.appointmentID = appointmentID;
        this.patientID = patientID;
        this.doctorID = doctorID;
        this.dateTime = dateTime;
        this.status = status;
        this.outcomeID = outcomeID;
    }
    
    /**
     * Get the unique identifier for the appointment.
     * @return The appointment ID
     */
    public String getAppointmentID() {
        return appointmentID;
    }
    
    /**
     * Get the id of the patient associated with this appointment.
     * @return The patient ID
     */
    public String getPatientID() {
        return patientID;
    }
    
    /**
     * Get the id of the doctor associated with this appointment.
     * @return The doctor ID
     */
    public String getDoctorID() {
        return doctorID;
    }
    
    /**
     * Get the date and time of the appointment.
     * @return The date and time of the appointment
     */
    public LocalDateTime getDateTime() {
        return dateTime;
    }
    
    /**
     * Get the current status of the appointment.
     * @return The current status of the appointment
     */
    public AppointmentStatus getStatus() {
        return status;
    }
    
    /**
     * Get the outcome record of the appointment (null if not completed).
     * @return The outcome record of the appointment
     */
    public String getOutcomeID() {
        return outcomeID;
    }

    /**
     * Set the current status of the appointment.
     * @param status The new status of the appointment
     */
    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }
    
    /**
     * Set the outcome record of the appointment (null if not completed).
     * @param outcomeRecord The outcome record of the appointment
     */
    public void setOutcomeID(String outcomeID) {
        this.outcomeID = outcomeID;
    }

    @Override
    public String toString() {
        return "Appointment{" + appointmentID + "," + patientID + "," + doctorID + "," + dateTime + "," + status + "," + outcomeID + "}";
    }
}
