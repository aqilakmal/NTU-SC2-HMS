package entity;

/**
 * Represents an appointment outcome record in the Hospital Management System. Outcomes track
 * the results and details of completed patient appointments with doctors.
 * 
 * Key components include:
 * - Unique outcome record identifier
 * - Reference to the associated appointment
 * - Service type provided during visit
 * - Associated prescription details
 * - Doctor's consultation notes
 * 
 * The Outcome class provides a comprehensive record of appointment results and supports
 * the tracking of patient care delivery and prescription management within the hospital system.
 *
 * @author Group 7
 * @version 1.0
 */
public class Outcome {

    /**
     * Unique identifier for the outcome record.
     * Used to track and reference specific appointment outcomes in the system.
     */
    private String outcomeID;
    
    /**
     * The ID of the appointment associated with this outcome record.
     * References the specific appointment for which this outcome was recorded.
     */
    private String appointmentID;
    
    /**
     * The type of service provided during the appointment.
     * Records the medical service delivered (e.g., consultation, X-ray, blood test).
     */
    private String serviceProvided;
    
    /**
     * The ID of the prescription associated with this outcome.
     * References any medications prescribed during the appointment.
     */
    private String prescriptionID;
    
    /**
     * Notes from the consultation.
     * Contains the doctor's detailed observations and recommendations from the visit.
     */
    private String consultationNotes;
    
    /**
     * Constructor for creating a new Outcome record in the system.
     * Initializes all required fields for tracking an appointment's results.
     * Validates and sets up the core outcome data including service details,
     * prescription information, and consultation notes.
     *
     * @param outcomeID The unique identifier for the outcome record
     * @param appointmentID The ID of the appointment associated with this outcome record
     * @param serviceProvided The type of service provided during the appointment
     * @param prescriptionID The ID of the prescription associated with this outcome
     * @param consultationNotes The notes from the consultation
     */
    public Outcome(String outcomeID, String appointmentID, String serviceProvided, String prescriptionID, String consultationNotes) {
        this.outcomeID = outcomeID;
        this.appointmentID = appointmentID;
        this.serviceProvided = serviceProvided;
        this.prescriptionID = prescriptionID;
        this.consultationNotes = consultationNotes;
    }
    
    /**
     * Retrieves the unique identifier for this outcome record.
     * This method provides access to the outcome record's ID which is used
     * for tracking and referencing specific appointment results in the system.
     *
     * @return The outcome ID
     */
    public String getOutcomeID() {
        return outcomeID;
    }
    
    /**
     * Retrieves the ID of the appointment associated with this outcome.
     * This method provides access to the reference ID that links this outcome
     * to its corresponding appointment record in the system.
     *
     * @return The appointment ID
     */
    public String getAppointmentID() {
        return appointmentID;
    }
    
    /**
     * Retrieves the type of service provided during the appointment.
     * This method provides access to the medical service category that was
     * delivered during the appointment (e.g., consultation, X-ray).
     *
     * @return The type of service provided
     */
    public String getServiceProvided() {
        return serviceProvided;
    }
    
    /**
     * Retrieves the ID of the prescription associated with this outcome.
     * This method provides access to the reference ID for any medications
     * that were prescribed during the appointment.
     *
     * @return The prescription ID
     */
    public String getPrescriptionID() {
        return prescriptionID;
    }
    
    /**
     * Retrieves the consultation notes from the appointment.
     * This method provides access to the doctor's detailed observations,
     * recommendations, and other important medical notes from the visit.
     *
     * @return The consultation notes
     */
    public String getConsultationNotes() {
        return consultationNotes;
    }

    /**
     * Updates the unique identifier for this outcome record.
     * This method allows administrators to modify the outcome's ID
     * if corrections or updates are needed in the system.
     *
     * @param outcomeID The new outcome ID to assign
     */
    public void setOutcomeID(String outcomeID) {
        this.outcomeID = outcomeID;
    }

    /**
     * Updates the appointment ID associated with this outcome.
     * This method allows administrators to modify the appointment
     * reference if corrections are needed to maintain proper records.
     *
     * @param appointmentID The new appointment ID to associate
     */
    public void setAppointmentID(String appointmentID) {
        this.appointmentID = appointmentID;
    }

    /**
     * Updates the type of service provided during the appointment.
     * This method allows doctors to modify the service category
     * if corrections or additional services were provided.
     *
     * @param serviceProvided The new service type to record
     */
    public void setServiceProvided(String serviceProvided) {
        this.serviceProvided = serviceProvided;
    }
    
    /**
     * Updates the prescription ID associated with this outcome.
     * This method allows doctors to modify the prescription reference
     * if changes or updates to the prescribed medications are needed.
     *
     * @param prescriptionID The new prescription ID to associate
     */
    public void setPrescriptionID(String prescriptionID) {
        this.prescriptionID = prescriptionID;
    }
    
    /**
     * Updates the consultation notes for this outcome.
     * This method allows doctors to modify their observations and
     * recommendations if additional information needs to be recorded.
     *
     * @param consultationNotes The new consultation notes to record
     */
    public void setConsultationNotes(String consultationNotes) {
        this.consultationNotes = consultationNotes;
    }

    /**
     * Generates a string representation of the outcome record.
     * This method creates a formatted string containing all relevant
     * outcome information for display or logging purposes.
     *
     * @return A string representation of the complete outcome record
     */
    @Override
    public String toString() {
        return "Outcome{" + outcomeID + "," + appointmentID + "," + serviceProvided + "," + prescriptionID + "," + consultationNotes + "}";
    }
}