package entity;

/**
 * Represents a patient with personal and medical information in the Hospital Management System.
 * Extends the User class.
 */
public class Patient extends User {

    /**
     * Enum representing the possible gender options.
     */
    public enum Gender {
        MALE, FEMALE, OTHER
    }
    
    /**
     * Patient's blood type.
     */
    private String bloodType;
    
    /**
     * Patient's medical record.
     */
    private MedicalRecord medicalRecord;

    /**
     * Constructor for Patient.
     * @param userID The unique identifier for the user
     * @param password The password for the user
     * @param role The role of the user
     * @param name The name of the user
     * @param dateOfBirth The date of birth of the user
     * @param gender The gender of the user
     * @param contactNumber The contact number of the user
     * @param emailAddress The email address of the user
     * @param bloodType The blood type of the patient
     */
    public Patient(String userID, String password, UserRole role, String name, String dateOfBirth, String gender, String contactNumber, String emailAddress, String bloodType) {
        super(userID, password, role, name, dateOfBirth, gender, contactNumber, emailAddress);
        this.bloodType = bloodType;
    }

    /**
     * Get the patient's blood type.
     * @return String representing the patient's blood type
     */
    public String getBloodType() {
        return bloodType;
    }
    
    /**
     * Get the patient's medical record.
     * @return MedicalRecord object containing the patient's medical information
     */
    public MedicalRecord getMedicalRecord() {
        return medicalRecord;
    }

    /**
     * Updates the patient's blood type.
     * @param newBloodType The new blood type to set
     */
    public void updateBloodType(String newBloodType) {
        this.bloodType = newBloodType;
    }
    
    /**
     * Updates the patient's medical record.
     * @param newMedicalRecord The new medical record to set
     */
    public void updateMedicalRecord(MedicalRecord newMedicalRecord) {
        this.medicalRecord = newMedicalRecord;
    }
    
    // TODO: Other patient-specific methods
}
