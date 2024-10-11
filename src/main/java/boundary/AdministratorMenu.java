package boundary;

import controller.*;
import entity.*;
import utility.*;
import java.util.Scanner;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.time.LocalDateTime;
/**
 * Interface for administrator tasks in the Hospital Management System.
 */
public class AdministratorMenu {
    
    private AdministratorController administratorController;
    private UserController userController;
    private Scanner scanner;
    
    /**
     * Constructor for AdministratorMenu.
     * @param AdministratorController The AdministratorController instance
     * @param userController The UserController instance
     */
    public AdministratorMenu(
        AdministratorController administratorController,
        UserController userController
    ) {
        this.administratorController = administratorController;
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
                        viewAppointmentDetails();
                        break;
                    case 3:
                        manageMedicationInventory();
                        break;
                    case 4:
                        approveReplenishmentRequests();
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
            List<User> staffList = administratorController.getFilteredStaffList(filters);
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
        LinkedHashMap<String, TableBuilder.ColumnMapping> columnMapping = new LinkedHashMap<>();
        columnMapping.put("userID", new TableBuilder.ColumnMapping("User ID", null));
        columnMapping.put("name", new TableBuilder.ColumnMapping("Name", null));
        columnMapping.put("role", new TableBuilder.ColumnMapping("Role", null));
        columnMapping.put("contactNumber", new TableBuilder.ColumnMapping("Contact Number", null));

        TableBuilder.createTable("Staff List", staffList, columnMapping, 20);
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
                boolean success = administratorController.manageStaff(Administrator.StaffAction.ADD, newStaff);
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

            boolean success = administratorController.manageStaff(Administrator.StaffAction.UPDATE, staffToUpdate);
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

            boolean success = administratorController.manageStaff(Administrator.StaffAction.REMOVE, staffToRemove);
            if (success) {
                System.out.println("Staff removed successfully.");
            } else {
                System.out.println("Failed to remove staff.");
            }
        } catch (Exception e) {
            System.err.println("Error removing staff: " + e.getMessage());
        }
    }

    /**
     * Views the details of all appointments.
     */
    private void viewAppointmentDetails() {
        while (true) {
            List<Appointment> appointments = administratorController.getAllAppointments();

            if (appointments.isEmpty()) {
                System.out.println("No appointments found.");
                return;
            }

            displayAppointmentList(appointments);

            System.out.println("Options:");
            System.out.println("{1} View detailed appointment information");
            System.out.println("{2} Return to main menu");
            System.out.print("Enter your choice: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        viewDetailedAppointment(appointments);
                        break;
                    case 2:
                        return;
                    default:
                        System.out.println("Invalid choice. Please enter 1 or 2.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear the invalid input
            }
        }
    }

    /**
     * Displays the list of appointments.
     * @param appointments The list of appointments to display
     */
    private void displayAppointmentList(List<Appointment> appointments) {
        LinkedHashMap<String, TableBuilder.ColumnMapping> columnMapping = new LinkedHashMap<>();
        columnMapping.put("appointmentID", new TableBuilder.ColumnMapping("Appointment ID", null));
        columnMapping.put("patientID", new TableBuilder.ColumnMapping("Patient ID", null));
        columnMapping.put("doctorID", new TableBuilder.ColumnMapping("Doctor ID", null));
        columnMapping.put("dateTime", new TableBuilder.ColumnMapping("Date/Time", 
            (val) -> ((LocalDateTime) val).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
        columnMapping.put("status", new TableBuilder.ColumnMapping("Status", null));

        TableBuilder.createTable("Appointment List", appointments, columnMapping, 20);
    }

    /**
     * Views the details of a specific appointment, including its outcome if available.
     * @param appointments The list of all appointments
     */
    private void viewDetailedAppointment(List<Appointment> appointments) {
        System.out.print("Enter the Appointment ID to view details: ");
        String appointmentID = scanner.nextLine().trim();

        Appointment selectedAppointment = appointments.stream()
            .filter(a -> a.getAppointmentID().equals(appointmentID))
            .findFirst()
            .orElse(null);

        if (selectedAppointment == null) {
            System.out.println("Appointment not found.");
            return;
        }

        System.out.println("Detailed Appointment Information:");
        System.out.println("-".repeat(40));
        System.out.printf("Appointment ID: %s%n", selectedAppointment.getAppointmentID());
        System.out.printf("Patient ID: %s%n", selectedAppointment.getPatientID());
        System.out.printf("Doctor ID: %s%n", selectedAppointment.getDoctorID());
        System.out.printf("Date/Time: %s%n", selectedAppointment.getDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        System.out.printf("Status: %s%n", selectedAppointment.getStatus());
        System.out.println("-".repeat(40));

        String outcomeID = selectedAppointment.getOutcomeID();
        if (outcomeID != null && !outcomeID.isEmpty()) {
            Outcome outcome = administratorController.getOutcomeByID(outcomeID);
            if (outcome != null) {
                System.out.println("Outcome Information:");
                System.out.println("-".repeat(40));
                System.out.printf("Outcome ID: %s%n", outcome.getOutcomeID());
                System.out.printf("Service Provided: %s%n", outcome.getServiceProvided());
                System.out.printf("Prescription ID: %s%n", outcome.getPrescriptionID());
                System.out.printf("Consultation Notes: %s%n", outcome.getConsultationNotes());
                System.out.println("-".repeat(40));
            } else {
                System.out.println("Outcome information not found for this appointment.");
            }
        } else {
            System.out.println("No outcome information available for this appointment.");
        }
    }

    /**
     * Manages the medication inventory.
     */
    private void manageMedicationInventory() {
        while (true) {
            try {
                System.out.println("\nManage Medication Inventory:");
                System.out.println("{1} View Medications");
                System.out.println("{2} Add Medication");
                System.out.println("{3} Update Medication Information");
                System.out.println("{4} Remove Medication");
                System.out.println("{5} Back to Admin Menu");
                System.out.print("Enter your choice: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        viewMedications();
                        break;
                    case 2:
                        addMedication();
                        break;
                    case 3:
                        updateMedicationInformation();
                        break;
                    case 4:
                        removeMedication();
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
     * Displays the list of medications.
     */
    private void viewMedications() {
        List<Medication> medications = administratorController.getAllMedications();
        if (medications.isEmpty()) {
            System.out.println("No medications found.");
        } else {
            LinkedHashMap<String, TableBuilder.ColumnMapping> columnMapping = new LinkedHashMap<>();
            columnMapping.put("medicationID", new TableBuilder.ColumnMapping("ID", null));
            columnMapping.put("name", new TableBuilder.ColumnMapping("Name", null));
            columnMapping.put("stockLevel", new TableBuilder.ColumnMapping("Stock Level", null));
            columnMapping.put("lowStockAlertLevel", new TableBuilder.ColumnMapping("Low Stock Alert Level", null));

            TableBuilder.createTable("Medication List", medications, columnMapping, 30);
        }
    }

    /**
     * Adds a new medication to the inventory.
     */
    private void addMedication() {
        try {
            System.out.print("Enter medication ID: ");
            String medicationID = scanner.nextLine().trim();
            if (medicationID.isEmpty()) {
                System.out.println("Error: Medication ID cannot be empty.");
                return;
            }

            System.out.print("Enter medication name: ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("Error: Medication name cannot be empty.");
                return;
            }

            System.out.print("Enter initial stock level: ");
            int stockLevel = Integer.parseInt(scanner.nextLine().trim());
            if (stockLevel < 0) {
                System.out.println("Error: Stock level cannot be negative.");
                return;
            }

            System.out.print("Enter low stock alert level: ");
            int lowStockAlertLevel = Integer.parseInt(scanner.nextLine().trim());
            if (lowStockAlertLevel < 0) {
                System.out.println("Error: Low stock alert level cannot be negative.");
                return;
            }

            Medication newMedication = new Medication(medicationID, name, stockLevel, lowStockAlertLevel);
            boolean success = administratorController.addMedication(newMedication);
            if (success) {
                System.out.println("Medication added successfully.");
            } else {
                System.out.println("Failed to add medication. It may already exist.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid number format. Please enter valid integers for stock levels.");
        } catch (Exception e) {
            System.err.println("Error adding medication: " + e.getMessage());
        }
    }

    /**
     * Updates the information of an existing medication.
     */
    private void updateMedicationInformation() {
        try {
            System.out.print("Enter medication ID to update: ");
            String medicationID = scanner.nextLine().trim();
            if (medicationID.isEmpty()) {
                System.out.println("Error: Medication ID cannot be empty.");
                return;
            }

            Medication medicationToUpdate = administratorController.getMedicationByID(medicationID);
            if (medicationToUpdate == null) {
                System.out.println("Medication not found.");
                return;
            }

            System.out.print("Enter new name (or press enter to skip): ");
            String newName = scanner.nextLine().trim();
            if (!newName.isEmpty()) {
                medicationToUpdate.setName(newName);
            }

            System.out.print("Enter new stock level (or press enter to skip): ");
            String stockLevelInput = scanner.nextLine().trim();
            if (!stockLevelInput.isEmpty()) {
                int newStockLevel = Integer.parseInt(stockLevelInput);
                if (newStockLevel < 0) {
                    System.out.println("Error: Stock level cannot be negative.");
                    return;
                }
                medicationToUpdate.setStockLevel(newStockLevel);
            }

            System.out.print("Enter new low stock alert level (or press enter to skip): ");
            String alertLevelInput = scanner.nextLine().trim();
            if (!alertLevelInput.isEmpty()) {
                int newAlertLevel = Integer.parseInt(alertLevelInput);
                if (newAlertLevel < 0) {
                    System.out.println("Error: Low stock alert level cannot be negative.");
                    return;
                }
                medicationToUpdate.setLowStockAlertLevel(newAlertLevel);
            }

            boolean success = administratorController.updateMedication(medicationToUpdate);
            if (success) {
                System.out.println("Medication information updated successfully.");
            } else {
                System.out.println("Failed to update medication information.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid number format. Please enter valid integers for stock levels.");
        } catch (Exception e) {
            System.err.println("Error updating medication information: " + e.getMessage());
        }
    }

    /**
     * Removes a medication from the inventory.
     */
    private void removeMedication() {
        try {
            System.out.print("Enter medication ID to remove: ");
            String medicationID = scanner.nextLine().trim();
            if (medicationID.isEmpty()) {
                System.out.println("Error: Medication ID cannot be empty.");
                return;
            }

            System.out.print("Are you sure you want to remove this medication? (y/n): ");
            String confirm = scanner.nextLine().trim().toLowerCase();
            if (!confirm.equals("y")) {
                System.out.println("Medication removal cancelled.");
                return;
            }

            boolean success = administratorController.removeMedication(medicationID);
            if (success) {
                System.out.println("Medication removed successfully.");
            } else {
                System.out.println("Failed to remove medication. It may not exist.");
            }
        } catch (Exception e) {
            System.err.println("Error removing medication: " + e.getMessage());
        }
    }

    /**
     * Handles the approval of replenishment requests.
     */
    private void approveReplenishmentRequests() {
        while (true) {
            List<Request> pendingRequests = administratorController.getPendingRequests();

            if (pendingRequests.isEmpty()) {
                System.out.println("No pending replenishment requests found.");
                return;
            }

            displayPendingRequests(pendingRequests);

            System.out.println("Options:");
            System.out.println("{1} Select replenishment ID to approve");
            System.out.println("{2} Return to main menu");
            System.out.print("Enter your choice: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        approveRequest(pendingRequests);
                        break;
                    case 2:
                        return;
                    default:
                        System.out.println("Invalid choice. Please enter 1 or 2.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear the invalid input
            }
        }
    }

    /**
     * Displays the list of pending replenishment requests.
     * @param pendingRequests The list of pending requests to display
     */
    private void displayPendingRequests(List<Request> pendingRequests) {
        LinkedHashMap<String, TableBuilder.ColumnMapping> columnMapping = new LinkedHashMap<>();
        columnMapping.put("requestID", new TableBuilder.ColumnMapping("Request ID", null));
        columnMapping.put("medicationID", new TableBuilder.ColumnMapping("Medication ID", null));
        columnMapping.put("quantity", new TableBuilder.ColumnMapping("Quantity", null));
        columnMapping.put("requestedBy", new TableBuilder.ColumnMapping("Requested By", null));
        columnMapping.put("status", new TableBuilder.ColumnMapping("Status", null));

        TableBuilder.createTable("Pending Replenishment Requests", pendingRequests, columnMapping, 15);
    }

    /**
     * Approves a selected replenishment request.
     * @param pendingRequests The list of pending requests
     */
    private void approveRequest(List<Request> pendingRequests) {
        System.out.print("Enter the Request ID to approve: ");
        String requestID = scanner.nextLine().trim();

        Request selectedRequest = pendingRequests.stream()
            .filter(r -> r.getRequestID().equals(requestID))
            .findFirst()
            .orElse(null);

        if (selectedRequest == null) {
            System.out.println("Request not found.");
            return;
        }

        boolean success = administratorController.approveRequest(requestID);
        if (success) {
            System.out.println("Request approved successfully. Medication stock has been updated.");
        } else {
            System.out.println("Failed to approve request. Please try again.");
        }
    }
}