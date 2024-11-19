package entity;

/**
 * Represents a medication prescription record in the Hospital Management System. Prescriptions track
 * medications ordered by doctors for patients during appointments.
 * 
 * Key components include:
 * - Unique prescription identifier
 * - Reference to the associated appointment
 * - Medication details and quantity prescribed
 * - Current status (pending or dispensed)
 * - Additional notes and instructions
 * 
 * The Prescription class provides a comprehensive record of medication orders and supports
 * the tracking of prescription fulfillment by pharmacists within the hospital system.
 *
 * @author Group 7
 * @version 1.0
 */
public class Prescription {

    /**
     * Enum representing the possible statuses of a prescription.
     * Used to track the fulfillment state of prescriptions in the system.
     * Supports PENDING (newly created) and DISPENSED (fulfilled by pharmacist) states.
     */
    public enum PrescriptionStatus {
        PENDING, DISPENSED
    }

    /**
     * Unique identifier for the prescription.
     * Used to track and reference specific prescriptions in the system.
     */
    private String prescriptionID;

    /**
     * Unique identifier for the appointment.
     * References the specific appointment during which this prescription was created.
     */
    private String appointmentID;

    /**
     * Unique identifier for the medication prescribed.
     * References the specific medication that was prescribed to the patient.
     */
    private String medicationID;

    /**
     * Quantity of the medication prescribed.
     * Specifies the amount of medication to be dispensed to the patient.
     */
    private int quantity;

    /**
     * The current status of the prescription.
     * Tracks whether the prescription has been fulfilled by the pharmacy.
     */
    private PrescriptionStatus status;

    /**
     * Notes for the prescription.
     * Contains additional instructions or information about the prescription.
     */
    private String notes;

    /**
     * Constructor for creating a new Prescription instance in the system.
     * Initializes a prescription with all required details including medication,
     * quantity, and status. Validates and sets up the prescription record for
     * tracking in the hospital system.
     *
     * @param prescriptionID The unique identifier for the prescription
     * @param appointmentID The unique identifier for the appointment
     * @param medicationID The unique identifier for the medication prescribed
     * @param quantity The quantity of the medication prescribed
     * @param status The current status of the prescription
     * @param notes The notes for the prescription
     */
    public Prescription(String prescriptionID, String appointmentID, String medicationID, int quantity, PrescriptionStatus status, String notes) {
        this.prescriptionID = prescriptionID;
        this.appointmentID = appointmentID;
        this.medicationID = medicationID;
        this.quantity = quantity;
        this.status = status;
        this.notes = notes;
    }

    /**
     * Retrieves the unique identifier for this prescription.
     * This method provides access to the prescription's ID which is used
     * to track and reference the prescription throughout the system.
     *
     * @return The unique prescription identifier
     */
    public String getPrescriptionID() {
        return prescriptionID;
    }

    /**
     * Retrieves the appointment identifier associated with this prescription.
     * This method provides access to the ID of the appointment during which
     * the prescription was created, enabling tracking of prescription origin.
     *
     * @return The associated appointment identifier
     */
    public String getAppointmentID() {
        return appointmentID;
    }

    /**
     * Retrieves the medication identifier for this prescription.
     * This method provides access to the ID of the specific medication
     * that was prescribed, enabling lookup of medication details.
     *
     * @return The prescribed medication identifier
     */
    public String getMedicationID() {
        return medicationID;
    }

    /**
     * Retrieves the prescribed quantity of medication.
     * This method provides access to the amount of medication that
     * should be dispensed to the patient by the pharmacy.
     *
     * @return The quantity of medication prescribed
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Retrieves the current status of the prescription.
     * This method provides access to whether the prescription has been
     * fulfilled by the pharmacy or is still pending dispensation.
     *
     * @return The current prescription status
     */
    public PrescriptionStatus getStatus() {
        return status;
    }

    /**
     * Retrieves the notes associated with this prescription.
     * This method provides access to any additional instructions or
     * information recorded by the prescribing doctor.
     *
     * @return The prescription notes
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Updates the unique identifier for this prescription.
     * This method allows system administrators to modify the prescription ID
     * if corrections or updates are needed in the record.
     *
     * @param prescriptionID The new prescription ID to set
     */
    public void setPrescriptionID(String prescriptionID) {
        this.prescriptionID = prescriptionID;
    }

    /**
     * Updates the appointment identifier for this prescription.
     * This method allows system administrators to modify the associated
     * appointment reference if corrections are needed.
     *
     * @param appointmentID The new appointment ID to set
     */
    public void setAppointmentID(String appointmentID) {
        this.appointmentID = appointmentID;
    }

    /**
     * Updates the medication identifier for this prescription.
     * This method allows doctors to modify the prescribed medication
     * if changes to the prescription are needed.
     *
     * @param medicationID The new medication ID to set
     */
    public void setMedicationID(String medicationID) {
        this.medicationID = medicationID;
    }

    /**
     * Updates the prescribed quantity of medication.
     * This method allows doctors to modify the amount of medication
     * that should be dispensed to the patient.
     *
     * @param quantity The new quantity to set
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Updates the status of the prescription.
     * This method allows pharmacists to update the fulfillment status
     * when dispensing medications to patients.
     *
     * @param status The new prescription status to set
     */
    public void setStatus(PrescriptionStatus status) {
        this.status = status;
    }

    /**
     * Updates the notes for this prescription.
     * This method allows doctors to modify the instructions or
     * additional information about the prescribed medication.
     *
     * @param notes The new notes to set
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Generates a string representation of the prescription.
     * This method creates a formatted string containing all relevant
     * prescription information for display or logging purposes.
     *
     * @return A string representation of the complete prescription record
     */
    @Override
    public String toString() {
        return "Prescription{" + prescriptionID + "," + appointmentID + "," + medicationID + "," + quantity + "," + status + "," + notes + "}";
    }
}
