package boundary;

import controller.UserController;
import controller.StaffManagementController;
import entity.Administrator;
import entity.User;
import java.util.Scanner;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.InputMismatchException;

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
            try {
                System.out.println("\nAdministrator Menu:");
                System.out.println("{1} Manage Staff");
                System.out.println("{2} View Appointment Details");
                System.out.println("{3} Manage Medication Inventory");
                System.out.println("{4} Approve Replenishment Requests");
                System.out.println("{5} Logout");
                System.out.print("Enter your choice: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        manageStaffMenu();
                        break;
                    case 2:
                        // Implement view appointment details
                        System.out.println("View Appointment Details - Not implemented yet.");
                        break;
                    case 3:
                        // Implement manage medication inventory
                        System.out.println("Manage Medication Inventory - Not implemented yet.");
                        break;
                    case 4:
                        // Implement approve replenishment requests
                        System.out.println("Approve Replenishment Requests - Not implemented yet.");
                        break;
                    case 5:
                        System.out.println("Logging out and returning to home screen...");
                        return;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 5.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear the invalid input
            } catch (Exception e) {
                System.err.println("An unexpected error occurred: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Displays the staff management menu.
     */
    private void manageStaffMenu() {
        while (true) {
            try {
                System.out.println("\nStaff Management Menu:");
                System.out.println("{1} View Staff List");
                System.out.println("{2} Add New Staff");
                System.out.println("{3} Update Staff Information");
                System.out.println("{4} Remove Staff");
                System.out.println("{5} Return to Main Menu");
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
                        System.out.println("Invalid choice. Please enter a number between 1 and 5.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear the invalid input
            } catch (Exception e) {
                System.err.println("An unexpected error occurred: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Views the list of staff.
     */
    private void viewStaffList() {
        try {
            Map<String, String> filters = new HashMap<>();
            // You can add filter options here if needed
            List<User> staffList = staffManagementController.getFilteredStaffList(filters);
            if (staffList.isEmpty()) {
                System.out.println("No staff members found.");
            } else {
                displayStaffList(staffList);
            }
        } catch (Exception e) {
            System.err.println("Error retrieving staff list: " + e.getMessage());
        }
    }

    /**
     * Displays the list of staff.
     * @param staffList The list of staff to display
     */
    private void displayStaffList(List<User> staffList) {
        System.out.println("\nStaff List:");
        // System.out.println("------------------------------------------------------------------------");
        System.out.println("-".repeat(70));
        System.out.printf("%-10s %-20s %-20s %-15s%n", "User ID", "Name", "Role", "Contact Number");
        System.out.println("-".repeat(70));
        for (User staff : staffList) {
            System.out.printf("%-10s %-20s %-20s %-15s%n", 
                staff.getUserID(), 
                staff.getName(), 
                staff.getRole(), 
                staff.getContactNumber());
        }
        System.out.println("-".repeat(70));
    }

    /**
     * Adds a new staff member.
     */
    private void addNewStaff() {
        int attempts = 0;
        while (attempts < 3) {
            try {
                System.out.print("Enter new staff ID: ");
                String userID = scanner.nextLine().trim();
                if (userID.isEmpty()) {
                    System.out.println("Error: Staff ID cannot be empty.");
                    attempts++;
                    continue;
                }

                System.out.print("Enter staff name: ");
                String name = scanner.nextLine().trim();
                if (name.isEmpty()) {
                    System.out.println("Error: Staff name cannot be empty.");
                    attempts++;
                    continue;
                }

                System.out.print("Enter staff role (1 for DOCTOR, 2 for PHARMACIST): ");
                String roleInput = scanner.nextLine().trim();
                User.UserRole role;
                try {
                    switch (roleInput) {
                        case "1":
                            role = User.UserRole.DOCTOR;
                            break;
                        case "2":
                            role = User.UserRole.PHARMACIST;
                            break;
                        default:
                            throw new IllegalArgumentException();
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Error: Invalid role. Please enter either 1 for DOCTOR or 2 for PHARMACIST.");
                    attempts++;
                    continue;
                }

                System.out.print("Enter contact number: ");
                String contactNumber = scanner.nextLine().trim();
                if (contactNumber.isEmpty()) {
                    System.out.println("Error: Contact number cannot be empty.");
                    attempts++;
                    continue;
                }

                System.out.print("Enter email address: ");
                String email = scanner.nextLine().trim();
                if (email.isEmpty()) {
                    System.out.println("Error: Email address cannot be empty.");
                    attempts++;
                    continue;
                }

                User newStaff = new User(userID, "password", role, name, "", "", contactNumber, email);
                boolean success = staffManagementController.manageStaff(Administrator.StaffAction.ADD, newStaff);
                if (success) {
                    System.out.println("New staff added successfully.");
                    return;
                } else {
                    System.out.println("Failed to add new staff. The staff ID may already exist.");
                    attempts++;
                }
            } catch (Exception e) {
                System.err.println("Error adding new staff: " + e.getMessage());
                attempts++;
            }

            if (attempts < 3) {
                System.out.println("Attempts remaining: " + (3 - attempts));
            }
        }

        System.out.print("Maximum attempts reached. Do you want to try again? (y/n): ");
        String retry = scanner.nextLine().trim().toLowerCase();
        if (retry.equals("y")) {
            addNewStaff();
        } else {
            System.out.println("Returning to the menu...");
        }
    }

    /**
     * Updates the information of a staff member.
     */
    private void updateStaffInformation() {
        try {
            System.out.print("Enter staff ID to update: ");
            String userID = scanner.nextLine().trim();
            if (userID.isEmpty()) {
                System.out.println("Error: Staff ID cannot be empty.");
                return;
            }

            User staffToUpdate = userController.getUserByID(userID);
            if (staffToUpdate == null) {
                System.out.println("Staff not found.");
                return;
            }

            System.out.print("Enter new name (or press enter to skip): ");
            String newName = scanner.nextLine().trim();
            if (!newName.isEmpty()) {
                staffToUpdate.updateName(newName);
            }

            boolean success = staffManagementController.manageStaff(Administrator.StaffAction.UPDATE, staffToUpdate);
            if (success) {
                System.out.println("Staff information updated successfully.");
            } else {
                System.out.println("Failed to update staff information.");
            }
        } catch (Exception e) {
            System.err.println("Error updating staff information: " + e.getMessage());
        }
    }

    /**
     * Removes a staff member.
     */
    private void removeStaff() {
        try {
            System.out.print("Enter staff ID to remove: ");
            String userID = scanner.nextLine().trim();
            if (userID.isEmpty()) {
                System.out.println("Error: Staff ID cannot be empty.");
                return;
            }

            User staffToRemove = userController.getUserByID(userID);
            if (staffToRemove == null) {
                System.out.println("Staff not found.");
                return;
            }

            System.out.print("Are you sure you want to remove this staff member? (y/n): ");
            String confirm = scanner.nextLine().trim().toLowerCase();
            if (!confirm.equals("y")) {
                System.out.println("Staff removal cancelled.");
                return;
            }

            boolean success = staffManagementController.manageStaff(Administrator.StaffAction.REMOVE, staffToRemove);
            if (success) {
                System.out.println("Staff removed successfully.");
            } else {
                System.out.println("Failed to remove staff.");
            }
        } catch (Exception e) {
            System.err.println("Error removing staff: " + e.getMessage());
        }
    }
}