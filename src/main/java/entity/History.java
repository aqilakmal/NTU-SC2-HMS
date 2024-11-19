package entity;

import java.time.LocalDate;

/**
 * Represents a medical history record in the Hospital Management System. Medical histories
 * track a patient's diagnoses, treatments, and medical timeline. This class serves as a core
 * entity for maintaining patient medical records and history tracking.
 * 
 * Key components include:
 * - Unique history record identifier
 * - Patient identification and linkage
 * - Diagnosis details and date
 * - Treatment information and plans
 * 
 * The History class provides a comprehensive view of a patient's medical background
 * and supports the doctor's decision making process by maintaining accurate
 * historical medical data.
 *
 * @author Group 7
 * @version 1.0
 */
public class History {

    /**
     * Unique identifier for the medical history record.
     * Used to track and reference specific history entries in the system.
     */
    private String historyID;
    
    /**
     * The id of the patient associated with this medical record.
     * References the patient whose medical history is being tracked.
     */
    private String patientID;
    
    /**
     * The date of the diagnosis.
     * Records when the medical condition was diagnosed.
     */
    private LocalDate diagnosisDate;

    /**
     * The diagnosis.
     * Contains the doctor's assessment and identified medical condition.
     */
    private String diagnosis;

    /**
     * The treatment.
     * Details the prescribed medical intervention and treatment plan.
     */
    private String treatment;

    /**
     * Constructor for creating a new History record in the system.
     * Initializes all required fields for tracking a patient's medical history entry.
     * Validates and sets up the core medical record data including patient reference,
     * diagnosis details, and treatment information.
     *
     * @param historyID The unique identifier for the history record
     * @param patientID The id of the patient associated with this medical record
     * @param diagnosisDate The date of the diagnosis
     * @param diagnosis The diagnosis details
     * @param treatment The prescribed treatment plan
     */
    public History(String historyID, String patientID, LocalDate diagnosisDate, String diagnosis, String treatment) {
        this.historyID = historyID;
        this.patientID = patientID;
        this.diagnosisDate = diagnosisDate;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
    }
    
    /**
     * Retrieves the unique identifier for this history record.
     * This method provides access to the history record's ID which is used
     * for tracking and referencing specific medical history entries.
     *
     * @return String representing the unique history record identifier
     */
    public String getHistoryID() {
        return historyID;
    }
    
    /**
     * Retrieves the patient identifier associated with this history record.
     * This method provides access to the ID of the patient whose medical
     * history is being tracked.
     *
     * @return String representing the patient's unique identifier
     */
    public String getPatientID() {
        return patientID;
    }

    /**
     * Retrieves the date when the diagnosis was made.
     * This method provides access to the timestamp of when the medical
     * condition was diagnosed and recorded.
     *
     * @return LocalDate representing the diagnosis date
     */
    public LocalDate getDiagnosisDate() {
        return diagnosisDate;
    }

    /**
     * Retrieves the diagnosis details for this medical record.
     * This method provides access to the doctor's assessment and
     * identified medical condition.
     *
     * @return String containing the detailed diagnosis
     */
    public String getDiagnosis() {
        return diagnosis;
    }

    /**
     * Retrieves the treatment plan for this medical record.
     * This method provides access to the prescribed medical intervention
     * and treatment details.
     *
     * @return String containing the detailed treatment plan
     */
    public String getTreatment() {
        return treatment;
    }

    /**
     * Updates the unique identifier for this history record.
     * This method allows system administrators to modify the history
     * record's tracking identifier if needed.
     *
     * @param historyID The new unique identifier to assign to this history record
     */
    public void setHistoryID(String historyID) {
        this.historyID = historyID;
    }

    /**
     * Updates the patient identifier for this history record.
     * This method allows system administrators to modify the patient
     * association if needed for record management.
     *
     * @param patientID The new patient identifier to associate with this record
     */
    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    /**
     * Updates the diagnosis date for this medical record.
     * This method allows doctors to modify the timestamp of when
     * the diagnosis was made if corrections are needed.
     *
     * @param diagnosisDate The new date to assign to this diagnosis
     */
    public void setDiagnosisDate(LocalDate diagnosisDate) {
        this.diagnosisDate = diagnosisDate;
    }

    /**
     * Updates the diagnosis details for this medical record.
     * This method allows doctors to modify the assessment and
     * medical condition details if updates are needed.
     *
     * @param diagnosis The new diagnosis details to record
     */
    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    /**
     * Updates the treatment plan for this medical record.
     * This method allows doctors to modify the prescribed medical
     * intervention and treatment details as needed.
     *
     * @param treatment The new treatment plan to record
     */
    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    /**
     * Generates a string representation of the history record.
     * This method creates a formatted string containing all relevant
     * medical history information for display or logging purposes.
     *
     * @return A string representation of the complete history record
     */
    @Override
    public String toString() {
        return "History{" + historyID + "," + patientID + "," + diagnosisDate + "," + diagnosis + "," + treatment + "}";
    }
}
