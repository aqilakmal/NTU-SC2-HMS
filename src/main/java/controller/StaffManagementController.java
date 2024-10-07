package controller;

import entity.User;
import entity.Administrator.StaffAction;
import java.util.List;
import java.util.Map;

/**
 * Controller class for managing staff-related operations in the Hospital Management System.
 */
public class StaffManagementController {

    private UserDataManager userDataManager;

    /**
     * Constructor for StaffManagementController.
     * @param userDataManager The UserDataManager instance to manage user data.
     */
    public StaffManagementController(UserDataManager userDataManager) {
        this.userDataManager = userDataManager;
    }

    /**
     * Manages hospital staff (doctors and pharmacists).
     * @param staffAction The action to perform (e.g., ADD, UPDATE, REMOVE)
     * @param staffData The data of the staff member to manage
     * @return boolean indicating whether the action was successful
     */
    public boolean manageStaff(StaffAction staffAction, User staffData) {
        try {
            switch (staffAction) {
                case ADD:
                    userDataManager.addUser(staffData);
                    break;
                case UPDATE:
                    userDataManager.updateUser(staffData);
                    break;
                case REMOVE:
                    userDataManager.removeUser(staffData.getUserID());
                    break;
                default:
                    return false;
            }
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves a list of staff members based on specified filters.
     * @param filters Map of filter criteria (e.g., role, gender, etc.)
     * @return List of User objects matching the filter criteria
     */
    public List<User> getFilteredStaffList(Map<String, String> filters) {
        return userDataManager.getFilteredUsers(filters);
    }
}