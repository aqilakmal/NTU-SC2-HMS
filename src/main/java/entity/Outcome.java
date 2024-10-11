package entity;

/**
 * Records the outcome of an appointment in the Hospital Management System.
 */
public class Outcome {

    /**
     * The unique identifier for the outcome record.
     */
    private String outcomeID;
    
    /**
     * The ID of the appointment associated with this outcome record.
     */
    private String appointmentID;
    
    /**
     * The type of service provided during the appointment.
     */
    private String serviceProvided;
    
    /**
     * The ID of the prescription associated with this outcome.
     */
    private String prescriptionID;
    
    /**
     * Notes from the consultation.
     */
    private String consultationNotes;
    
    /**
     * Constructor for Outcome.
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
     * Get the unique identifier for the outcome record.
     * @return The outcome ID
     */
    public String getOutcomeID() {
        return outcomeID;
    }
    
    /**
     * Get the ID of the appointment associated with this outcome record.
     * @return The appointment ID
     */
    public String getAppointmentID() {
        return appointmentID;
    }
    
    /**
     * Get the type of service provided during the appointment.
     * @return The type of service provided
     */
    public String getServiceProvided() {
        return serviceProvided;
    }
    
    /**
     * Get the ID of the prescription associated with this outcome.
     * @return The prescription ID
     */
    public String getPrescriptionID() {
        return prescriptionID;
    }
    
    /**
     * Get the notes from the consultation.
     * @return The consultation notes
     */
    public String getConsultationNotes() {
        return consultationNotes;
    }

    /**
     * Set the unique identifier for the outcome record.
     * @param outcomeID The outcome ID
     */
    public void setOutcomeID(String outcomeID) {
        this.outcomeID = outcomeID;
    }

    /**
     * Set the ID of the appointment associated with this outcome record.
     * @param appointmentID The appointment ID
     */
    public void setAppointmentID(String appointmentID) {
        this.appointmentID = appointmentID;
    }

    /**
     * Set the type of service provided during the appointment.
     * @param serviceProvided The type of service provided
     */
    public void setServiceProvided(String serviceProvided) {
        this.serviceProvided = serviceProvided;
    }
    
    /**
     * Set the ID of the prescription associated with this outcome.
     * @param prescriptionID The prescription ID
     */
    public void setPrescriptionID(String prescriptionID) {
        this.prescriptionID = prescriptionID;
    }
    
    /**
     * Set the notes from the consultation.
     * @param consultationNotes The consultation notes
     */
    public void setConsultationNotes(String consultationNotes) {
        this.consultationNotes = consultationNotes;
    }

    @Override
    public String toString() {
        return "Outcome{" + outcomeID + "," + appointmentID + "," + serviceProvided + "," + prescriptionID + "," + consultationNotes + "}";
    }
}