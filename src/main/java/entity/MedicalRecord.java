package entity;

import java.util.List;

/**
 * Contains a patient's medical history in the Hospital Management System.
 */
public class MedicalRecord {
    
    /**
     * The id of the patient associated with this medical record.
     */
    private String patientID;
    
    /**
     * List of diagnoses in the patient's medical history.
     */
    private List<String> diagnoses;
    
    /**
     * List of treatments in the patient's medical history.
     */
    private List<String> treatments;
    
    /**
     * List of prescriptions in the patient's medical history.
     */
    private List<Prescription> prescriptions;

    /**
     * Constructor for MedicalRecord.
     * @param patient The patient associated with this medical record
     * @param diagnoses List of diagnoses in the patient's medical history
     * @param treatments List of treatments in the patient's medical history
     * @param prescriptions List of prescriptions in the patient's medical history
     */
    public MedicalRecord(String patientID, List<String> diagnoses, List<String> treatments, List<Prescription> prescriptions) {
        this.patientID = patientID;
        this.diagnoses = diagnoses;
        this.treatments = treatments;
        this.prescriptions = prescriptions;
    }
    
    /**
     * Get the patient associated with this medical record.
     * @return The patient
     */
    public String getPatientID() {
        return patientID;
    }

    /**
     * Get the list of diagnoses in the patient's medical history.
     * @return The list of diagnoses
     */
    public List<String> getDiagnoses() {
        return diagnoses;
    }
    
    /**
     * Get the list of treatments in the patient's medical history.
     * @return The list of treatments
     */
    public List<String> getTreatments() {
        return treatments;
    }
    
    /**
     * Get the list of prescriptions in the patient's medical history.
     * @return The list of prescriptions
     */
    public List<Prescription> getPrescriptions() {
        return prescriptions;
    }

    /**
     * Adds a new diagnosis to the medical record.
     * @param diagnosis The diagnosis to add
     */
    public void addDiagnosis(String diagnosis) {
        diagnoses.add(diagnosis);
    }
    
    /**
     * Adds a new treatment to the medical record.
     * @param treatment The treatment to add
     */
    public void addTreatment(String treatment) {
        treatments.add(treatment);
    }
    
    /**
     * Adds a new prescription to the medical record.
     * @param prescription The prescription to add
     */
    public void addPrescription(Prescription prescription) {
        prescriptions.add(prescription);
    }
    
}
