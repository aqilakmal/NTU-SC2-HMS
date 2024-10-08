package entity;

/**
 * An abstract class representing a generic user in the Hospital Management System.
 * It encapsulates common attributes and methods shared by all user types.
 */
public class User {

    /**
     * Enum representing the user's role in the system.
     */
    public enum UserRole {
        PATIENT, DOCTOR, PHARMACIST, ADMINISTRATOR
    }
    
    /**
     * Unique identifier for the user.
     */
    private String userID;
    
    /**
     * User's password for authentication.
     */
    private String password;
    
    /**
     * Enum representing the user's role in the system.
     */
    private UserRole role;

    /**
     * User's full name.
     */
    private String name;

    /**
     * User's date of birth.
     */
    private String dateOfBirth;
    
    /**
     * User's gender.
     */
    private String gender;

    /**
     * User's contact number.
     */
    private String contactNumber;
    
    /**
     * User's email address.
     */
    private String emailAddress;

    /**
     * Constructor for User.
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
     * Get the user's ID.
     * @return String representing the user's ID
     */
    public String getUserID() {
        return userID;
    }

    /**
     * Get the user's password.
     * @return String representing the user's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Get the user's role.
     * @return UserRole enum representing the user's role
     */
    public UserRole getRole() {
        return role;
    }

    /**
     * Get the user's name.
     * @return String representing the user's name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the user's date of birth.
     * @return String representing the user's date of birth
     */
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Get the user's gender.
     * @return String representing the user's gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * Get the user's contact number.
     * @return String representing the user's contact number
     */
    public String getContactNumber() {
        return contactNumber;
    }

    /**
     * Get the user's email address.
     * @return String representing the user's email address
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Updates the user's password.
     * @param newPassword The new password to set
     */
    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    /**
     * Updates the user's role.
     * @param newRole The new role to set
     */
    public void updateRole(UserRole newRole) {
        this.role = newRole;
    }

    /**
     * Updates the user's name.
     * @param newName The new name to set
     */
    public void updateName(String newName) {
        this.name = newName;
    }

    /**
     * Updates the user's date of birth.
     * @param newDateOfBirth The new date of birth to set
     */
    public void updateDateOfBirth(String newDateOfBirth) {
        this.dateOfBirth = newDateOfBirth;
    }

    /**
     * Updates the user's gender.
     * @param newGender The new gender to set
     */
    public void updateGender(String newGender) {
        this.gender = newGender;
    }

    /**
     * Updates the user's contact number.
     * @param newContactNumber The new contact number to set
     */
    public void updateContactNumber(String newContactNumber) {
        this.contactNumber = newContactNumber;
    }

    /**
     * Updates the user's email address.
     * @param newEmailAddress The new email address to set
     */
    public void updateEmailAddress(String newEmailAddress) {
        this.emailAddress = newEmailAddress;
    }

    @Override
    public String toString() {
        return "User{" +
                "userID='" + userID + '\'' +
                ", role=" + role +
                ", name='" + name + '\'' +
                '}';
    }
}
