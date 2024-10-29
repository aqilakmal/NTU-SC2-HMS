package entity;

/**
 * Represents a medication prescribed to a patient in the Hospital Management
 * System.
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
     * Unique identifier for the appointment.
     */
    private String appointmentID;

    /**
     * Unique identifier for the medication prescribed.
     */
    private String medicationID;

    /**
     * Quantity of the medication prescribed.
     */
    private double quantity;

    /**
     * The current status of the prescription.
     */
    private PrescriptionStatus status;

    /**
     * Notes for the prescription.
     */
    private String notes;

    /**
     * Constructor for Prescription.
     *
     * @param prescriptionID The unique identifier for the prescription
     * @param appointmentID The unique identifier for the appointment
     * @param medicationID The unique identifier for the medication prescribed
     * @param quantity The quantity of the medication prescribed
     * @param status The current status of the prescription
     * @param notes The notes for the prescription
     */
    public Prescription(String prescriptionID, String appointmentID, String medicationID, double quantity, PrescriptionStatus status, String notes) {
        this.prescriptionID = prescriptionID;
        this.appointmentID = appointmentID;
        this.medicationID = medicationID;
        this.quantity = quantity;
        this.status = status;
        this.notes = notes;
    }

    /**
     * Get the unique identifier for the prescription.
     *
     * @return The prescription ID
     */
    public String getPrescriptionID() {
        return prescriptionID;
    }

    /**
     * Get the unique identifier for the appointment.
     *
     * @return The appointment ID
     */
    public String getAppointmentID() {
        return appointmentID;
    }

    /**
     * Get the unique identifier for the medication prescribed.
     *
     * @return The medication ID
     */
    public String getMedicationID() {
        return medicationID;
    }

    /**
     * Get the quantity of the medication prescribed.
     *
     * @return The quantity
     */
    public double getQuantity() {
        return quantity;
    }

    /**
     * Get the current status of the prescription.
     *
     * @return The prescription status
     */
    public PrescriptionStatus getStatus() {
        return status;
    }

    /**
     * Get the notes for the prescription.
     *
     * @return The notes
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Set the unique identifier for the prescription.
     *
     * @param prescriptionID The prescription ID to set
     */
    public void setPrescriptionID(String prescriptionID) {
        this.prescriptionID = prescriptionID;
    }

    /**
     * Set the unique identifier for the appointment.
     *
     * @param appointmentID The appointment ID to set
     */
    public void setAppointmentID(String appointmentID) {
        this.appointmentID = appointmentID;
    }

    /**
     * Set the unique identifier for the medication prescribed.
     *
     * @param medicationID The medication ID to set
     */
    public void setMedicationID(String medicationID) {
        this.medicationID = medicationID;
    }

    /**
     * Set the quantity of the medication prescribed.
     *
     * @param quantity The quantity to set
     */
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    /**
     * Set the current status of the prescription.
     *
     * @param status The prescription status to set
     */
    public void setStatus(PrescriptionStatus status) {
        this.status = status;
    }

    /**
     * Set the notes for the prescription.
     *
     * @param notes The notes to set
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "Prescription{" + prescriptionID + "," + appointmentID + "," + medicationID + "," + quantity + "," + status + "," + notes + "}";
    }
}
