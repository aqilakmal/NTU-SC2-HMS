package entity;

/**
 * Represents an appointment between a patient and a doctor in the Hospital Management System.
 */
public class Appointment {

    /**
     * Enum representing the possible statuses of an appointment.
     */
    public enum AppointmentStatus {
        REQUESTED, CONFIRMED, CANCELLED, COMPLETED,
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
     * The id of the slot associated with this appointment.
     */
    private String slotID;
    
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
     * @param slotID The id of the slot associated with this appointment
     * @param status The current status of the appointment
     * @param outcomeID The outcome ID of the appointment (null if not completed)
     */
    public Appointment(String appointmentID, String patientID, String doctorID, String slotID, AppointmentStatus status, String outcomeID) {
        this.appointmentID = appointmentID;
        this.patientID = patientID;
        this.doctorID = doctorID;
        this.slotID = slotID;
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
     * Get the id of the slot associated with this appointment.
     * @return The slot ID
     */
    public String getSlotID() {
        return slotID;
    }

    public void setSlotID(String slotID) {
        this.slotID = slotID;
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
        return "Appointment{" + appointmentID + "," + patientID + "," + doctorID + "," + slotID + "," + status + "," + outcomeID + "}";
    }
}

