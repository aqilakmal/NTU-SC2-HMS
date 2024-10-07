package entity;

/**
 * Represents a medication prescribed to a patient in the Hospital Management System.
 */
public class Prescription {

    /**
     * Enum representing the possible statuses of a prescription.
     */
    public enum PrescriptionStatus {
        PENDING, DISPENSED
    }
    
    /**
     * Unique identifier for the prescription.
     */
    private String prescriptionID;
    
    /**
     * The medication prescribed.
     */
    private Medication medication;
    
    /**
     * The current status of the prescription.
     */
    private PrescriptionStatus status;
    
    /**
     * Constructor for Prescription.
     * @param prescriptionID The unique identifier for the prescription
     * @param medication The medication prescribed
     * @param status The current status of the prescription
     */
    public Prescription(String prescriptionID, Medication medication, PrescriptionStatus status) {
        this.prescriptionID = prescriptionID;
        this.medication = medication;
        this.status = status;
    }
    
    /**
     * Get the unique identifier for the prescription.
     * @return The prescription ID
     */
    public String getPrescriptionID() {
        return prescriptionID;
    }
    
    /**
     * Get the medication prescribed.
     * @return The medication object
     */
    public Medication getMedication() {
        return medication;
    }
    
    /**
     * Get the current status of the prescription.
     * @return The prescription status
     */
    public PrescriptionStatus getStatus() {
        return status;
    }

    /**
     * Set the unique identifier for the prescription.
     * @param prescriptionID The prescription ID to set
     */
    public void setPrescriptionID(String prescriptionID) {
        this.prescriptionID = prescriptionID;
    }

    /**
     * Set the medication prescribed.
     * @param medication The medication to set
     */
    public void setMedication(Medication medication) {
        this.medication = medication;
    }
    
    /**
     * Set the status of the prescription.
     * @param status The new status of the prescription
     */
    public void setStatus(PrescriptionStatus status) {
        this.status = status;
    }
}
