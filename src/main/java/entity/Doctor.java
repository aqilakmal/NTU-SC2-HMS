package entity;

/**
 * Represents a doctor user in the Hospital Management System. Doctors are responsible for
 * managing patient appointments, medical records, and providing healthcare services.
 * 
 * Key responsibilities include:
 * - Managing personal schedule and appointment availability
 * - Accepting or declining appointment requests from patients
 * - Recording appointment outcomes and diagnoses
 * - Prescribing medications and treatments
 * - Updating patient medical records
 * 
 * The Doctor class extends the base User class and provides additional functionality
 * specific to doctor operations within the hospital system.
 *
 * @author Group 7
 * @version 1.0
 */
public class Doctor extends User {

    /**
     * Enum representing the possible decisions a doctor can make for an appointment request.
     * Used to track and validate doctor responses to patient appointment requests.
     * ACCEPT indicates the doctor has agreed to the appointment.
     * DECLINE indicates the doctor has rejected the appointment request.
     */
    public enum Decision {
        ACCEPT, DECLINE
    }
    
    /**
     * The medical specialization of the doctor.
     * Represents the doctor's area of expertise and qualification
     * (e.g., Cardiology, Pediatrics, Orthopedics, etc.).
     */
    private String specialization;

    /**
     * Constructor for creating a new Doctor user in the system.
     * Initializes a doctor with their personal information, system credentials,
     * and medical specialization. The constructor validates and sets all required
     * fields for the doctor profile by calling the parent User class constructor.
     *
     * @param userID The unique identifier for the doctor in the system
     * @param password The doctor's login password
     * @param role The user role (should be UserRole.DOCTOR)
     * @param name The doctor's full name
     * @param dateOfBirth The doctor's date of birth
     * @param gender The doctor's gender
     * @param contactNumber The doctor's contact phone number
     * @param emailAddress The doctor's email address
     * @param specialization The doctor's medical specialization
     */
    public Doctor(String userID, String password, UserRole role, String name, String dateOfBirth, String gender, String contactNumber, String emailAddress, String specialization) {
        super(userID, password, role, name, dateOfBirth, gender, contactNumber, emailAddress);
        this.specialization = specialization;
    }

    /**
     * Retrieves the doctor's medical specialization.
     * This method provides access to the doctor's area of medical expertise,
     * which is used for appointment scheduling and patient care assignment.
     *
     * @return String representing the doctor's medical specialization
     */
    public String getSpecialization() {
        return specialization;
    }

    /**
     * Updates the doctor's medical specialization.
     * This method allows administrators to modify a doctor's area of expertise
     * when their qualifications or role changes within the hospital.
     *
     * @param newSpecialization The new medical specialization to assign to the doctor
     */
    public void updateSpecialization(String newSpecialization) {
        this.specialization = newSpecialization;
    }
}
