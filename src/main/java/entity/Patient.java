package entity;

/**
 * Represents a patient with personal and medical information in the Hospital Management System.
 * Patients are users who can schedule appointments, view their medical records, and manage their
 * personal information.
 * 
 * Key responsibilities include:
 * - Storing patient-specific medical information like blood type
 * - Maintaining reference to patient's medical history
 * - Supporting updates to patient information
 * - Providing access to medical records and history
 * 
 * The Patient class extends the base User class to include additional medical-specific
 * attributes and functionality required for patient management.
 *
 * @author Group 7
 * @version 1.0
 */
public class Patient extends User {

    /**
     * Enum representing the possible gender options for patients.
     * Used to standardize and validate gender values in the system.
     * Supports MALE, FEMALE and OTHER options for inclusive patient records.
     */
    public enum Gender {
        MALE, FEMALE, OTHER
    }
    
    /**
     * Patient's blood type information.
     * Stores the patient's blood group (e.g., A+, B-, O+, AB+) which is
     * critical medical information needed for treatments and procedures.
     */
    private String bloodType;
    
    /**
     * Patient's complete medical record.
     * Contains the patient's full medical history including past diagnoses,
     * treatments, prescriptions and other relevant medical information.
     */
    private History History;

    /**
     * Constructor for creating a new Patient instance in the system.
     * Initializes a patient with their personal information and medical details.
     * Validates and sets up both the base user attributes through the parent class
     * and patient-specific attributes like blood type.
     *
     * @param userID The unique identifier for the user
     * @param password The password for the user's account
     * @param role The role of the user in the system
     * @param name The full name of the user
     * @param dateOfBirth The date of birth of the user
     * @param gender The gender of the user
     * @param contactNumber The contact phone number of the user
     * @param emailAddress The email address of the user
     * @param bloodType The blood type of the patient
     */
    public Patient(String userID, String password, UserRole role, String name, String dateOfBirth, String gender, String contactNumber, String emailAddress, String bloodType) {
        super(userID, password, role, name, dateOfBirth, gender, contactNumber, emailAddress);
        this.bloodType = bloodType;
    }

    /**
     * Retrieves the patient's blood type information.
     * This method provides access to the patient's blood group which is
     * essential medical information required for various medical procedures
     * and emergency situations.
     *
     * @return String representing the patient's blood type
     */
    public String getBloodType() {
        return bloodType;
    }
    
    /**
     * Retrieves the patient's complete medical record.
     * This method provides access to the patient's full medical history
     * including past diagnoses, treatments, prescriptions and other
     * relevant medical information tracked by the system.
     *
     * @return History object containing the patient's medical information
     */
    public History getHistory() {
        return History;
    }

    /**
     * Updates the patient's blood type information.
     * This method allows authorized users to modify the patient's blood type
     * if corrections or updates are needed in the medical record. Should only
     * be used by medical professionals with proper authorization.
     *
     * @param newBloodType The new blood type to set for the patient
     */
    public void updateBloodType(String newBloodType) {
        this.bloodType = newBloodType;
    }
    
    /**
     * Updates the patient's medical record information.
     * This method allows medical professionals to update the patient's complete
     * medical history with new diagnoses, treatments, or other relevant medical
     * information that needs to be recorded in the system.
     *
     * @param newHistory The new medical record to set for the patient
     */
    public void updateHistory(History newHistory) {
        this.History = newHistory;
    }
}
