package entity;

import java.time.LocalDate;

/**
 * Contains a patient's medical history in the Hospital Management System.
 */
public class History {

    /**
     * Unique identifier for the medical history record.
     */
    private String historyID;
    
    /**
     * The id of the patient associated with this medical record.
     */
    private String patientID;
    
    /**
     * The date of the diagnosis.
     */
    private LocalDate diagnosisDate;

    /**
     * The diagnosis.
     */
    private String diagnosis;

    /**
     * The treatment.
     */
    private String treatment;

    /**
     * Constructor for History.
     * @param patientID The id of the patient associated with this medical record
     * @param diagnosisDate The date of the diagnosis
     * @param diagnosis The diagnosis
     * @param treatment The treatment
     */
    public History(String historyID, String patientID, LocalDate diagnosisDate, String diagnosis, String treatment) {
        this.historyID = historyID;
        this.patientID = patientID;
        this.diagnosisDate = diagnosisDate;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
    }
    
    /**
     * Get the history ID.
     * @return The history ID
     */
    public String getHistoryID() {
        return historyID;
    }
    
    /**
     * Get the patient ID.
     * @return The patient ID
     */
    public String getPatientID() {
        return patientID;
    }

    /**
     * Get the diagnosis date.
     * @return The diagnosis date
     */
    public LocalDate getDiagnosisDate() {
        return diagnosisDate;
    }

    /**
     * Get the diagnosis.
     * @return The diagnosis
     */
    public String getDiagnosis() {
        return diagnosis;
    }

    /**
     * Get the treatment.
     * @return The treatment
     */
    public String getTreatment() {
        return treatment;
    }

    /**
     * Set the history ID.
     * @param historyID The history ID
     */
    public void setHistoryID(String historyID) {
        this.historyID = historyID;
    }

    /**
     * Set the patient ID.
     * @param patientID The patient ID
     */
    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    /**
     * Set the diagnosis date.
     * @param diagnosisDate The diagnosis date
     */
    public void setDiagnosisDate(LocalDate diagnosisDate) {
        this.diagnosisDate = diagnosisDate;
    }

    /**
     * Set the diagnosis.
     * @param diagnosis The diagnosis
     */
    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    /**
     * Set the treatment.
     * @param treatment The treatment
     */
    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    /**
     * Returns a string representation of the history.  
     * @return A string representation of the history
     */
    @Override
    public String toString() {
        return "History{" + historyID + "," + patientID + "," + diagnosisDate + "," + diagnosis + "," + treatment + "}";
    }
}
