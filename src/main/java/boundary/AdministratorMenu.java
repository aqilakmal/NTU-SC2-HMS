package boundary;

import controller.*;
import entity.*;
import utility.*;
import java.util.Scanner;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.LinkedHashMap;
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
                ConsoleUtility.printHeader("ADMINISTRATOR MENU");
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
                ConsoleUtility.printHeader("STAFF MANAGEMENT MENU");
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
                ConsoleUtility.printHeader("ADD NEW STAFF");
                
                String userID = ConsoleUtility.validateInput("Enter new staff ID: ", ConsoleUtility::isValidID);
                String name = ConsoleUtility.validateInput("Enter staff name: ", ConsoleUtility::isValidName);
                User.UserRole role = ConsoleUtility.validateRole();
                String contactNumber = ConsoleUtility.validateInput("Enter contact number (8 digits): ", ConsoleUtility::isValidContactNumber);
                String email = ConsoleUtility.validateInput("Enter email address: ", ConsoleUtility::isValidEmail);

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

        if (ConsoleUtility.getConfirmation("Maximum attempts reached. Do you want to try again?")) {
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
            ConsoleUtility.printHeader("UPDATE STAFF INFORMATION");
            String userID = ConsoleUtility.validateInput("Enter staff ID to update: ", ConsoleUtility::isValidID);

            User staffToUpdate = userController.getUserByID(userID);
            if (staffToUpdate == null) {
                System.out.println("Staff not found.");
                return;
            }

            String newName = ConsoleUtility.validateInput("Enter new name (or press enter to skip): ", 
                input -> input.isEmpty() || ConsoleUtility.isValidName(input));
            if (!newName.isEmpty()) {
                staffToUpdate.updateName(newName);
            }

            String newContactNumber = ConsoleUtility.validateInput("Enter new contact number (8 digits, or press enter to skip): ", 
                input -> input.isEmpty() || ConsoleUtility.isValidContactNumber(input));
            if (!newContactNumber.isEmpty()) {
                staffToUpdate.updateContactNumber(newContactNumber);
            }

            String newEmail = ConsoleUtility.validateInput("Enter new email address (or press enter to skip): ", 
                input -> input.isEmpty() || ConsoleUtility.isValidEmail(input));
            if (!newEmail.isEmpty()) {
                staffToUpdate.updateEmailAddress(newEmail);
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
            ConsoleUtility.printHeader("REMOVE STAFF");
            String userID = ConsoleUtility.validateInput("Enter staff ID to remove: ", ConsoleUtility::isValidID);
            
            User staffToRemove = userController.getUserByID(userID);
            if (staffToRemove == null) {
                System.out.println("Staff not found.");
                return;
            }

            if (!ConsoleUtility.getConfirmation("Are you sure you want to remove this staff member?")) {
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
            ConsoleUtility.printHeader("VIEW APPOINTMENT DETAILS", false);
            List<Appointment> appointments = administratorController.getAllAppointments();

            if (appointments.isEmpty()) {
                System.out.println("No appointments found.");
                return;
            }

            System.out.println();
            displayAppointmentList(appointments);

            System.out.println("\n{1} View detailed appointment information");
            System.out.println("{2} Return to main menu");
            int choice = Integer.parseInt(ConsoleUtility.validateInput("Enter your choice: ", input -> input.matches("^[12]$")));

            switch (choice) {
                case 1:
                    viewDetailedAppointment(appointments);
                    break;
                case 2:
                    return;
                default:
                    System.out.println("Invalid choice. Please enter 1 or 2.");
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
        columnMapping.put("slotID", new TableBuilder.ColumnMapping("Slot ID", null));
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

        Map<String, Object> appointmentDetails = administratorController.getAppointmentDetails(appointmentID);
        if (appointmentDetails == null) {
            System.out.println("Appointment not found or slot information is missing.");
            return;
        }

        Appointment selectedAppointment = (Appointment) appointmentDetails.get("appointment");
        Slot slot = (Slot) appointmentDetails.get("slot");

        System.out.println("\nDetailed Appointment Information:");
        System.out.println("-".repeat(40));
        System.out.printf("Appointment ID: %s%n", selectedAppointment.getAppointmentID());
        System.out.printf("Patient ID: %s%n", selectedAppointment.getPatientID());
        System.out.printf("Doctor ID: %s%n", selectedAppointment.getDoctorID());
        System.out.printf("Date: %s%n", slot.getDate());
        System.out.printf("Time: %s - %s%n", slot.getStartTime(), slot.getEndTime());
        System.out.printf("Status: %s%n", selectedAppointment.getStatus());
        System.out.printf("Slot Status: %s%n", slot.getStatus());
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
                ConsoleUtility.printHeader("MANAGE MEDICATION INVENTORY");
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
            ConsoleUtility.printHeader("MEDICATION LIST", false);
            System.out.println();
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
            ConsoleUtility.printHeader("ADD MEDICATION");
            String medicationID = ConsoleUtility.validateInput("Enter medication ID: ", ConsoleUtility::isValidID);
            String name = ConsoleUtility.validateInput("Enter medication name: ", ConsoleUtility::isValidName);
            
            int stockLevel = Integer.parseInt(ConsoleUtility.validateInput("Enter initial stock level: ", ConsoleUtility::isValidInteger));
            if (stockLevel < 0) {
                System.out.println("Error: Stock level cannot be negative.");
                return;
            }

            int lowStockAlertLevel = Integer.parseInt(ConsoleUtility.validateInput("Enter low stock alert level: ", ConsoleUtility::isValidInteger));
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
            ConsoleUtility.printHeader("UPDATE MEDICATION INFORMATION");
            String medicationID = ConsoleUtility.validateInput("Enter medication ID to update: ", ConsoleUtility::isValidID);

            Medication medicationToUpdate = administratorController.getMedicationByID(medicationID);
            if (medicationToUpdate == null) {
                System.out.println("Medication not found.");
                return;
            }

            String newName = ConsoleUtility.validateInput("Enter new name (or press enter to skip): ", 
                input -> input.isEmpty() || ConsoleUtility.isValidName(input));
            if (!newName.isEmpty()) {
                medicationToUpdate.setName(newName);
            }

            String stockLevelInput = ConsoleUtility.validateInput("Enter new stock level (or press enter to skip): ", 
                input -> input.isEmpty() || ConsoleUtility.isValidInteger(input));
            if (!stockLevelInput.isEmpty()) {
                int newStockLevel = Integer.parseInt(stockLevelInput);
                if (newStockLevel < 0) {
                    System.out.println("Error: Stock level cannot be negative.");
                    return;
                }
                medicationToUpdate.setStockLevel(newStockLevel);
            }

            String alertLevelInput = ConsoleUtility.validateInput("Enter new low stock alert level (or press enter to skip): ", 
                input -> input.isEmpty() || ConsoleUtility.isValidInteger(input));
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
            ConsoleUtility.printHeader("REMOVE MEDICATION");
            String medicationID = ConsoleUtility.validateInput("Enter medication ID to remove: ", ConsoleUtility::isValidID);

            if (!ConsoleUtility.getConfirmation("Are you sure you want to remove this medication?")) {
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
            ConsoleUtility.printHeader("PENDING REPLENISHMENT REQUESTS", false);
            List<Request> pendingRequests = administratorController.getPendingRequests();

            if (pendingRequests.isEmpty()) {
                System.out.println("\n\nNo pending replenishment requests found.");
                return;
            }

            System.out.println();
            displayPendingRequests(pendingRequests);
            
            System.out.println("\n{1} Select replenishment ID to approve");
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