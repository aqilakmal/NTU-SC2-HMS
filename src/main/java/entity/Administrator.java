package entity;

/**
 * Represents an administrator managing staff, appointments, and inventory in the Hospital Management System.
 * Extends the User class.
 */
public class Administrator extends User {

    /**
     * Enum representing the possible actions for managing staff.
     */
    public enum StaffAction {
      ADD, UPDATE, REMOVE
    }

    /**
    * Enum representing the possible actions for managing inventory.
    */
    public enum InventoryAction {
      ADD, UPDATE, REMOVE, VIEW
    }

    /**
     * Constructor for Administrator.
     * @param userID The unique identifier for the user
     * @param password The password for the user
     * @param role The role of the user
     * @param name The name of the user
     * @param dateOfBirth The date of birth of the user
     * @param gender The gender of the user
     * @param contactNumber The contact number of the user
     * @param emailAddress The email address of the user
     */
    public Administrator(String userID, String password, UserRole role, String name, String dateOfBirth, String gender, String contactNumber, String emailAddress) {
        super(userID, password, role, name, dateOfBirth, gender, contactNumber, emailAddress);
    }
}