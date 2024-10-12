package entity;

/**
 * Represents a doctor with scheduling and patient management capabilities in the Hospital Management System.
 * Extends the User class.
 */
public class Doctor extends User {

    /**
     * Enum representing the possible decisions for an appointment request.
     */
    public enum Decision {
        ACCEPT, DECLINE
    }
    
    /**
     * Doctor's medical specialization.
     */
    private String specialization;

    /**
     * Constructor for Doctor.
     * @param userID The unique identifier for the user
     * @param password The password for the user
     * @param role The role of the user
     * @param name The name of the user
     * @param dateOfBirth The date of birth of the user
     * @param gender The gender of the user
     * @param contactNumber The contact number of the user
     * @param emailAddress The email address of the user
     * @param specialization The specialization of the doctor
     */
    public Doctor(String userID, String password, UserRole role, String name, String dateOfBirth, String gender, String contactNumber, String emailAddress, String specialization) {
        super(userID, password, role, name, dateOfBirth, gender, contactNumber, emailAddress);
        this.specialization = specialization;
    }

    /**
     * Get the doctor's specialization.
     * @return String representing the doctor's specialization
     */
    public String getSpecialization() {
        return specialization;
    }

    /**
     * Update the doctor's specialization.
     * @param newSpecialization The new specialization to set
     */
    public void updateSpecialization(String newSpecialization) {
        this.specialization = newSpecialization;
    }
}
