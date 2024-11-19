package entity;

/**
 * Represents a generic user in the Hospital Management System (HMS). This class serves as the
 * base entity for all user types in the system, encapsulating common attributes and behaviors
 * shared across different roles (Patient, Doctor, Pharmacist, Administrator).
 * 
 * The User class manages core user information including:
 * - Authentication credentials (ID and password)
 * - Role-based access control
 * - Personal information (name, DOB, gender)
 * - Contact details (phone, email)
 * 
 * This class provides the foundation for user management and authentication in the HMS,
 * supporting role-specific access control and user profile management.
 *
 * @author Group 7
 * @version 1.0
 */
public class User {

    /**
     * Enum representing the user's role in the system. Used for role-based access control
     * to determine permissions and available functionality. Each user must be assigned
     * exactly one role that defines their capabilities within the HMS.
     */
    public enum UserRole {
        PATIENT, DOCTOR, PHARMACIST, ADMINISTRATOR
    }
    
    /**
     * Unique identifier for the user. Used as the primary key for user records and
     * for authentication purposes. Cannot be modified after user creation.
     */
    private String userID;
    
    /**
     * User's password for authentication. Initially set to "password" for new users,
     * requiring a password change on first login for security purposes.
     */
    private String password;
    
    /**
     * Enum representing the user's role in the system. Determines the user's access level
     * and available functionality within the HMS. Used for role-based menu routing
     * and permission validation.
     */
    private UserRole role;

    /**
     * User's full name. Used for display and identification purposes throughout
     * the system interface and reports.
     */
    private String name;

    /**
     * User's date of birth. Stored as a string in a standardized format for
     * age calculation and demographic reporting.
     */
    private String dateOfBirth;
    
    /**
     * User's gender. Used for demographic information and reporting purposes
     * within the HMS.
     */
    private String gender;

    /**
     * User's contact number. Used for communication purposes and notifications
     * regarding appointments or system updates.
     */
    private String contactNumber;
    
    /**
     * User's email address. Used as an alternative contact method and for
     * system notifications or communications.
     */
    private String emailAddress;

    /**
     * Constructor for creating a new User instance in the system. Initializes all required
     * user attributes including authentication credentials, role assignment, and personal
     * information. Validates and sets up the user record for system access.
     *
     * @param userID The unique identifier for the user
     * @param password The password for the user
     * @param role The role of the user
     * @param name The name of the user
     * @param dateOfBirth The date of birth of the user
     * @param gender The gender of the user
     * @param contactNumber The contact number of the user
     * @param emailAddress The email address of the user
     */
    public User(String userID, String password, UserRole role, String name, String dateOfBirth, String gender, String contactNumber, String emailAddress) {
        this.userID = userID;
        this.password = password;
        this.role = role;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.contactNumber = contactNumber;
        this.emailAddress = emailAddress;
    }

    /**
     * Retrieves the unique identifier for this user. This method provides access to
     * the user's ID which is used throughout the system for authentication and
     * record management purposes.
     *
     * @return String representing the user's ID
     */
    public String getUserID() {
        return userID;
    }

    /**
     * Retrieves the user's password. This method provides access to the encrypted
     * password string used for authentication validation during login attempts.
     * The password is initially set to "password" for new users.
     *
     * @return String representing the user's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Retrieves the user's assigned role in the system. This method provides access
     * to the role enum which determines the user's permissions and available
     * functionality within the HMS.
     *
     * @return UserRole enum representing the user's role
     */
    public UserRole getRole() {
        return role;
    }

    /**
     * Retrieves the user's full name. This method provides access to the user's
     * name which is used for display purposes throughout the system interface
     * and in generated reports.
     *
     * @return String representing the user's name
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the user's date of birth. This method provides access to the user's
     * birth date which is used for age calculation and demographic reporting
     * purposes in the system.
     *
     * @return String representing the user's date of birth
     */
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Retrieves the user's gender. This method provides access to the user's
     * gender information which is used for demographic reporting and
     * statistical analysis.
     *
     * @return String representing the user's gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * Retrieves the user's contact number. This method provides access to the
     * phone number used for system communications and notifications regarding
     * appointments or updates.
     *
     * @return String representing the user's contact number
     */
    public String getContactNumber() {
        return contactNumber;
    }

    /**
     * Retrieves the user's email address. This method provides access to the
     * email address used for system communications and as an alternative
     * contact method.
     *
     * @return String representing the user's email address
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Updates the user's password in the system. This method is called during
     * first-time login password changes and when users request password updates
     * for security purposes.
     *
     * @param newPassword The new password to set for the user
     */
    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    /**
     * Updates the user's role in the system. This method allows administrators
     * to modify user permissions by changing their assigned role, affecting
     * their available functionality.
     *
     * @param newRole The new role to assign to the user
     */
    public void updateRole(UserRole newRole) {
        this.role = newRole;
    }

    /**
     * Updates the user's name in the system. This method allows for correction
     * or updates to the user's displayed name in case of changes or
     * corrections needed.
     *
     * @param newName The new name to set for the user
     */
    public void updateName(String newName) {
        this.name = newName;
    }

    /**
     * Updates the user's date of birth in the system. This method allows for
     * correction of birth date information if errors are found in the
     * user's record.
     *
     * @param newDateOfBirth The new date of birth to set for the user
     */
    public void updateDateOfBirth(String newDateOfBirth) {
        this.dateOfBirth = newDateOfBirth;
    }

    /**
     * Updates the user's gender in the system. This method allows for
     * modification of gender information if updates or corrections
     * are needed.
     *
     * @param newGender The new gender to set for the user
     */
    public void updateGender(String newGender) {
        this.gender = newGender;
    }

    /**
     * Updates the user's contact number in the system. This method allows users
     * to modify their phone contact information when changes occur or
     * corrections are needed.
     *
     * @param newContactNumber The new contact number to set for the user
     */
    public void updateContactNumber(String newContactNumber) {
        this.contactNumber = newContactNumber;
    }

    /**
     * Updates the user's email address in the system. This method allows users
     * to modify their email contact information when changes occur or
     * corrections are needed.
     *
     * @param newEmailAddress The new email address to set for the user
     */
    public void updateEmailAddress(String newEmailAddress) {
        this.emailAddress = newEmailAddress;
    }

    /**
     * Generates a string representation of the user. This method creates a
     * formatted string containing key user information for display or
     * logging purposes.
     *
     * @return A string representation of the user record
     */
    @Override
    public String toString() {
        return "User{" + userID + "," + role + "," + name + "}";
    }
}
