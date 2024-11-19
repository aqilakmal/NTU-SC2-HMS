package entity;

/**
 * Represents an appointment between a patient and a doctor in the Hospital Management System.
 * This class manages appointment details including patient and doctor information, scheduling,
 * status tracking, and outcome records. It serves as a core entity for tracking medical 
 * consultations and maintaining appointment history.
 * 
 * The class supports the complete lifecycle of an appointment from initial request through
 * to completion, including status updates and outcome recording. It integrates with the
 * slot management system to handle scheduling and with the outcome system to track
 * medical records.
 *
 * @author Group 7
 * @version 1.0
 */
public class Appointment {

    /**
     * Enum representing the possible statuses of an appointment.
     * REQUESTED: Initial state when patient books an appointment
     * CONFIRMED: Doctor has accepted the appointment request
     * CANCELLED: Either patient or doctor has cancelled the appointment
     * COMPLETED: Appointment has been conducted and outcome recorded
     */
    public enum AppointmentStatus {
        REQUESTED, CONFIRMED, CANCELLED, COMPLETED,
    }
    
    /**
     * Unique identifier for the appointment.
     * Used to track and reference specific appointments throughout the system.
     */
    private String appointmentID;
    
    /**
     * The id of the patient associated with this appointment.
     * References the patient who requested/is attending the appointment.
     */
    private String patientID;
    
    /**
     * The id of the doctor associated with this appointment.
     * References the doctor who will conduct/conducted the consultation.
     */
    private String doctorID;
    
    /**
     * The id of the slot associated with this appointment.
     * References the specific time slot allocated for this appointment.
     */
    private String slotID;
    
    /**
     * The current status of the appointment.
     * Tracks the appointment's progress through the system workflow.
     */
    private AppointmentStatus status;
    
    /**
     * The outcome record of the appointment (null if not completed).
     * References the medical outcome record created after appointment completion.
     */
    private String outcomeID;
    
    /**
     * Constructor for creating a new Appointment instance.
     * Initializes all required fields for tracking an appointment in the system.
     * Validates and sets up the core appointment data including patient, doctor,
     * scheduling, and status information.
     *
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
     * Retrieves the unique identifier for this appointment.
     * This ID is used throughout the system to reference and track the appointment.
     * Essential for appointment management and record keeping.
     *
     * @return The appointment ID as a String
     */
    public String getAppointmentID() {
        return appointmentID;
    }
    
    /**
     * Retrieves the ID of the patient associated with this appointment.
     * Used to link the appointment to the patient's medical records and
     * for patient identification during the appointment process.
     *
     * @return The patient ID as a String
     */
    public String getPatientID() {
        return patientID;
    }
    
    /**
     * Retrieves the ID of the doctor assigned to this appointment.
     * Used to track doctor assignments and manage doctor schedules.
     * Essential for doctor availability and workload management.
     *
     * @return The doctor ID as a String
     */
    public String getDoctorID() {
        return doctorID;
    }
    
    /**
     * Retrieves the ID of the time slot reserved for this appointment.
     * Links the appointment to the hospital's scheduling system.
     * Used for time management and scheduling conflict prevention.
     *
     * @return The slot ID as a String
     */
    public String getSlotID() {
        return slotID;
    }

    /**
     * Updates the time slot assigned to this appointment.
     * Used when rescheduling appointments or resolving scheduling conflicts.
     * Ensures proper tracking of appointment timing changes.
     *
     * @param slotID The new slot ID to assign to this appointment
     */
    public void setSlotID(String slotID) {
        this.slotID = slotID;
    }
    
    /**
     * Retrieves the current status of the appointment.
     * Provides information about the appointment's progress through the system.
     * Essential for tracking appointment lifecycle and management.
     *
     * @return The current AppointmentStatus enum value
     */
    public AppointmentStatus getStatus() {
        return status;
    }
    
    /**
     * Retrieves the outcome ID associated with this appointment.
     * Links to the medical outcome record if the appointment is completed.
     * Returns null for appointments that haven't been completed.
     *
     * @return The outcome ID as a String, or null if not completed
     */
    public String getOutcomeID() {
        return outcomeID;
    }

    /**
     * Updates the status of the appointment.
     * Used to track the appointment's progress through the system workflow.
     * Critical for appointment lifecycle management and tracking.
     *
     * @param status The new AppointmentStatus to set for this appointment
     */
    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }
    
    /**
     * Sets the outcome ID for this appointment after completion.
     * Links the appointment to its medical outcome record.
     * Should only be called when the appointment is completed.
     *
     * @param outcomeID The outcome ID to associate with this appointment
     */
    public void setOutcomeID(String outcomeID) {
        this.outcomeID = outcomeID;
    }

    /**
     * Provides a string representation of the appointment.
     * Includes all key appointment details in a formatted string.
     * Useful for logging, debugging, and data display purposes.
     *
     * @return A string containing all appointment details
     */
    @Override
    public String toString() {
        return "Appointment{" + appointmentID + "," + patientID + "," + doctorID + "," + slotID + "," + status + "," + outcomeID + "}";
    }
}
