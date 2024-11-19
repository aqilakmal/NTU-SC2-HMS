package entity;

/**
 * Represents an administrator user in the Hospital Management System. Administrators are responsible for
 * managing hospital staff (doctors and pharmacists), monitoring appointments, and managing medication inventory.
 * 
 * Key responsibilities include:
 * - Staff management (adding, updating, removing staff members)
 * - Viewing real-time appointment schedules and details
 * - Managing medication inventory levels
 * - Approving medication replenishment requests from pharmacists
 * - Monitoring system operations and maintaining data integrity
 * 
 * The Administrator class extends the base User class and provides additional functionality
 * specific to administrative tasks within the hospital system.
 *
 * @author Group 7
 * @version 1.0
 */
public class Administrator extends User {

    /**
     * Enum representing the possible actions for managing staff members in the system.
     * Used to track and validate administrative operations on hospital staff records.
     * Includes options for adding new staff, updating existing records, and removing staff members.
     */
    public enum StaffAction {
      ADD, UPDATE, REMOVE
    }

    /**
     * Enum representing the possible actions for managing medication inventory.
     * Used to track and validate administrative operations on the hospital's medication stock.
     * Includes options for adding new medications, updating stock levels, removing medications,
     * and viewing current inventory status.
     */
    public enum InventoryAction {
      ADD, UPDATE, REMOVE, VIEW
    }

    /**
     * Constructor for creating a new Administrator user in the system.
     * Initializes an administrator with their personal information and system credentials.
     * The constructor validates and sets all required fields for the administrator profile
     * by calling the parent User class constructor.
     *
     * @param userID The unique identifier for the administrator in the system
     * @param password The administrator's login password
     * @param role The user role (should be UserRole.ADMINISTRATOR)
     * @param name The administrator's full name
     * @param dateOfBirth The administrator's date of birth
     * @param gender The administrator's gender
     * @param contactNumber The administrator's contact phone number
     * @param emailAddress The administrator's email address
     */
    public Administrator(String userID, String password, UserRole role, String name, String dateOfBirth, String gender, String contactNumber, String emailAddress) {
        super(userID, password, role, name, dateOfBirth, gender, contactNumber, emailAddress);
    }
}