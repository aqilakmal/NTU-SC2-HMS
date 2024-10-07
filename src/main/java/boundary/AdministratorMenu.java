package boundary;

import controller.UserController;
import controller.StaffManagementController;
import entity.Administrator;
import entity.User;
import java.util.Scanner;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Interface for administrator tasks in the Hospital Management System.
 */
public class AdministratorMenu {
    
    private StaffManagementController staffManagementController;
    private UserController userController;
    private Scanner scanner;
    
    /**
     * Constructor for AdministratorMenu.
     * @param staffManagementController The StaffManagementController instance
     * @param userController The UserController instance
     */
    public AdministratorMenu(
        StaffManagementController staffManagementController,
        UserController userController
    ) {
        this.staffManagementController = staffManagementController;
        this.userController = userController;
        this.scanner = new Scanner(System.in);
    }
    
    /**
     * Displays the menu options available to the administrator.
     */
    public void displayMenu() {
        while (true) {
            System.out.println("\nAdministrator Menu:");
            System.out.println("1. Manage Staff");
            System.out.println("2. View Appointment Details");
            System.out.println("3. Manage Medication Inventory");
            System.out.println("4. Approve Replenishment Requests");
            System.out.println("5. Logout");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    manageStaffMenu();
                    break;
                case 2:
                    // Implement view appointment details
                    break;
                case 3:
                    // Implement manage medication inventory
                    break;
                case 4:
                    // Implement approve replenishment requests
                    break;
                case 5:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Displays the staff management menu.
     */
    private void manageStaffMenu() {
        while (true) {
            System.out.println("\nStaff Management Menu:");
            System.out.println("1. View Staff List");
            System.out.println("2. Add New Staff");
            System.out.println("3. Update Staff Information");
            System.out.println("4. Remove Staff");
            System.out.println("5. Return to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    viewStaffList();
                    break;
                case 2:
                    addNewStaff();
                    break;
                case 3:
                    updateStaffInformation();
                    break;
                case 4:
                    removeStaff();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Views the list of staff.
     */
    private void viewStaffList() {
        Map<String, String> filters = new HashMap<>();
        // You can add filter options here if needed
        List<User> staffList = staffManagementController.getFilteredStaffList(filters);
        displayStaffList(staffList);
    }

    /**
     * Displays the list of staff.
     * @param staffList The list of staff to display
     */
    private void displayStaffList(List<User> staffList) {
        System.out.println("Staff List:");
        System.out.println("---------------------------------------------------");
        System.out.printf("%-10s %-20s %-10s %-15s%n", "User ID", "Name", "Role", "Contact Number");
        System.out.println("---------------------------------------------------");
        for (User staff : staffList) {
            System.out.printf("%-10s %-20s %-10s %-15s%n", 
                staff.getUserID(), 
                staff.getName(), 
                staff.getRole(), 
                staff.getContactNumber());
        }
        System.out.println("---------------------------------------------------");
    }

    /**
     * Adds a new staff member.
     */
    private void addNewStaff() {
        // Implement logic to gather new staff information
        System.out.print("Enter new staff ID: ");
        String userID = scanner.nextLine();
        System.out.print("Enter staff name: ");
        String name = scanner.nextLine();
        System.out.print("Enter staff role (DOCTOR/PHARMACIST): ");
        User.UserRole role = User.UserRole.valueOf(scanner.nextLine().toUpperCase());

        User newStaff = new User(userID, "password", role, name, "", "", "", "");
        boolean success = staffManagementController.manageStaff(Administrator.StaffAction.ADD, newStaff);
        if (success) {
            System.out.println("New staff added successfully.");
        } else {
            System.out.println("Failed to add new staff.");
        }
    }

    /**
     * Updates the information of a staff member.
     */
    private void updateStaffInformation() {
        System.out.print("Enter staff ID to update: ");
        String userID = scanner.nextLine();
        User staffToUpdate = userController.getUserByID(userID);
        if (staffToUpdate == null) {
            System.out.println("Staff not found.");
            return;
        }

        System.out.print("Enter new name (or press enter to skip): ");
        String newName = scanner.nextLine();
        if (!newName.isEmpty()) {
            staffToUpdate.updateName(newName);
        }

        boolean success = staffManagementController.manageStaff(Administrator.StaffAction.UPDATE, staffToUpdate);
        if (success) {
            System.out.println("Staff information updated successfully.");
        } else {
            System.out.println("Failed to update staff information.");
        }
    }

    /**
     * Removes a staff member.
     */
    private void removeStaff() {
        System.out.print("Enter staff ID to remove: ");
        String userID = scanner.nextLine();
        User staffToRemove = userController.getUserByID(userID);
        if (staffToRemove == null) {
            System.out.println("Staff not found.");
            return;
        }

        boolean success = staffManagementController.manageStaff(Administrator.StaffAction.REMOVE, staffToRemove);
        if (success) {
            System.out.println("Staff removed successfully.");
        } else {
            System.out.println("Failed to remove staff.");
        }
    }
}