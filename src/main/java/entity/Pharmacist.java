package entity;

/**
 * Represents a pharmacist user in the Hospital Management System with specific responsibilities
 * for managing prescriptions and medication inventory.
 * 
 * Key responsibilities include:
 * - Viewing and fulfilling medication prescription orders from doctors
 * - Updating prescription status in Appointment Outcome Records
 * - Monitoring medication inventory and stock levels
 * - Submitting replenishment requests for low stock medications
 * 
 * The Pharmacist class extends the base User class to include role-specific
 * functionality required for pharmacy operations and medication management.
 *
 * @author Group 7
 * @version 1.0
 */
public class Pharmacist extends User {
    
    /**
     * Constructor for creating a new Pharmacist instance in the system.
     * Initializes a pharmacist with their personal information and credentials.
     * Validates and sets up the base user attributes through the parent User class
     * to create a pharmacist account with proper authentication and identification.
     *
     * @param userID The unique identifier for the pharmacist in the system
     * @param password The password for the pharmacist's account authentication
     * @param role The role of the user (should be UserRole.PHARMACIST)
     * @param name The full name of the pharmacist
     * @param dateOfBirth The date of birth of the pharmacist
     * @param gender The gender of the pharmacist
     * @param contactNumber The contact phone number for the pharmacist
     * @param emailAddress The email address for the pharmacist
     */
    public Pharmacist(String userID, String password, UserRole role, String name, String dateOfBirth, String gender, String contactNumber, String emailAddress) {
        super(userID, password, role, name, dateOfBirth, gender, contactNumber, emailAddress);
    }
}
