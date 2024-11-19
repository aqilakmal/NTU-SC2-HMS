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
import java.util.Collections;

/**
 * Interface for administrator tasks in the Hospital Management System.
 */
public class AdministratorMenu {

    private AdministratorController administratorController;
    private Scanner scanner;

    /**
     * Constructor for AdministratorMenu.
     * 
     * @param AdministratorController The AdministratorController instance
     */
    public AdministratorMenu(AdministratorController administratorController) {
        this.administratorController = administratorController;
        this.scanner = new Scanner(System.in);
    }

    /**
     * [MAIN MENU] Displays the menu options available to the administrator.
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
     * [OPTION 1] Displays the staff management menu.
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
     * [OPTION 1.1] Views the list of staff.
     */
    private void viewStaffList() {
        try {
            ConsoleUtility.printHeader("VIEW STAFF LIST");
            List<User> staffList = administratorController.getFilteredStaffList(new HashMap<>());

            if (staffList.isEmpty()) {
                System.out.println("\nNo staff members found.");
                return;
            }

            displayStaffList(staffList, "Staff List");

        } catch (Exception e) {
            System.err.println("Error retrieving staff list: " + e.getMessage());
        }
    }

    /**
     * Displays the list of staff.
     * 
     * @param staffList The list of staff to display
     */
    private void displayStaffList(List<User> staffList, String title) {
        LinkedHashMap<String, TableBuilder.ColumnMapping> columnMapping = new LinkedHashMap<>();
        columnMapping.put("userID", new TableBuilder.ColumnMapping("User ID", null));
        columnMapping.put("name", new TableBuilder.ColumnMapping("Name", null));
        columnMapping.put("role", new TableBuilder.ColumnMapping("Role", null));
        columnMapping.put("dateOfBirth", new TableBuilder.ColumnMapping("Date of Birth", null));
        columnMapping.put("gender", new TableBuilder.ColumnMapping("Gender", null));
        columnMapping.put("contactNumber", new TableBuilder.ColumnMapping("Contact Number", null));
        columnMapping.put("emailAddress", new TableBuilder.ColumnMapping("Email Address", null));

        TableBuilder.createTable(title, staffList, columnMapping, 20);
    }

    /**
     * [OPTION 1.2] Adds a new staff member.
     */
    private void addNewStaff() {
        try {
            ConsoleUtility.printHeader("ADD NEW STAFF");

            // Display current staff list first
            List<User> currentStaff = administratorController.getFilteredStaffList(new HashMap<>());
            if (!currentStaff.isEmpty()) {
                displayStaffList(currentStaff, "Current Staff List");
            }

            // Get new staff ID
            System.out.println("");
            String userID = ConsoleUtility.validateInput(
                    "Enter new staff ID (or press Enter to go back): ",
                    input -> {
                        if (input.isEmpty())
                            return true;
                        if (!ConsoleUtility.isValidID(input)) {
                            System.out.println("Invalid ID format.");
                            return false;
                        }
                        if (administratorController.getUserByID(input) != null) {
                            System.out.println("Staff ID already exists.");
                            return false;
                        }
                        return true;
                    });

            // If userID is empty, return to main menu
            if (userID.isEmpty()) {
                return;
            }

            // Get staff name
            String name = ConsoleUtility.validateInput("Enter staff name: ", ConsoleUtility::isValidName);

            // Get staff role
            User.UserRole role = ConsoleUtility.validateRole();

            // Get date of birth
            String dateOfBirth = ConsoleUtility.validateInput(
                    "Enter date of birth (YYYY-MM-DD): ",
                    ConsoleUtility::isValidDateFormat);

            // Get gender
            String genderInput = ConsoleUtility.validateInput(
                    "Select gender {1} Male {2} Female: ",
                    ConsoleUtility::isValidGender);
            String gender = genderInput.equals("1") ? "MALE" : "FEMALE";

            // Get contact number
            String contactNumber = ConsoleUtility.validateInput(
                    "Enter contact number (8 digits): ",
                    ConsoleUtility::isValidContactNumber);

            // Get email address
            String email = ConsoleUtility.validateInput(
                    "Enter email address: ",
                    ConsoleUtility::isValidEmail);

            // Get specialization if role is DOCTOR
            String specialization = "";
            if (role == User.UserRole.DOCTOR) {
                specialization = ConsoleUtility.validateInput(
                        "Enter doctor's specialization: ",
                        input -> {
                            if (input.isEmpty()) {
                                System.out.println("Specialization cannot be empty for doctors.");
                                return false;
                            }
                            if (!ConsoleUtility.isValidName(input)) {
                                System.out.println("Specialization must contain only letters and spaces.");
                                return false;
                            }
                            return true;
                        });
            }

            // Create new staff object based on role
            User newStaff;
            switch (role) {
                case DOCTOR:
                    newStaff = new Doctor(userID, "password", role, name, dateOfBirth, gender,
                            contactNumber, email, specialization);
                    break;
                case PHARMACIST:
                    newStaff = new Pharmacist(userID, "password", role, name, dateOfBirth,
                            gender, contactNumber, email);
                    break;
                case ADMINISTRATOR:
                    newStaff = new Administrator(userID, "password", role, name, dateOfBirth,
                            gender, contactNumber, email);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid role for staff member");
            }

            // Add new staff
            boolean success = administratorController.manageStaff(Administrator.StaffAction.ADD, newStaff);

            // If successful, display updated staff list
            if (success) {
                System.out.println("\nNew staff added successfully.");
                List<User> updatedStaff = administratorController.getFilteredStaffList(new HashMap<>());
                System.out.println("");
                displayStaffList(updatedStaff, "Updated Staff List");
            } else {
                System.out.println("Failed to add new staff.");
            }

        } catch (Exception e) {
            System.err.println("Error adding new staff: " + e.getMessage());
        }
    }

    /**
     * [OPTION 1.3] Updates the information of a staff member.
     */
    private void updateStaffInformation() {
        try {
            ConsoleUtility.printHeader("UPDATE STAFF INFORMATION");

            List<User> staffList = administratorController.getFilteredStaffList(new HashMap<>());
            if (staffList.isEmpty()) {
                System.out.println("\nNo staff members found.");
                return;
            }

            displayStaffList(staffList, "Staff List");

            System.out.println("");
            String userID = ConsoleUtility.validateInput(
                    "Enter staff ID to update (or press Enter to go back): ",
                    input -> {
                        if (input.isEmpty())
                            return true;
                        boolean exists = staffList.stream().anyMatch(s -> s.getUserID().equals(input));
                        if (!exists) {
                            System.out.println("Invalid ID, staff does not exist.");
                        }
                        return exists;
                    });

            if (userID.isEmpty()) {
                return;
            }

            User staffToUpdate = administratorController.getUserByID(userID);
            System.out.println("");
            displayStaffList(Collections.singletonList(staffToUpdate), "Staff to Update");

            // Get new name
            System.out.println("");
            String newName = ConsoleUtility.validateInput(
                    "Enter new name (or press Enter to skip): ",
                    input -> input.isEmpty() || ConsoleUtility.isValidName(input));

            String newDateOfBirth = ConsoleUtility.validateInput(
                    "Enter new date of birth (YYYY-MM-DD) (or press Enter to skip): ",
                    input -> input.isEmpty() || ConsoleUtility.isValidDateFormat(input));

            String newGender = "";
            if (ConsoleUtility.getConfirmation("Would you like to update gender?")) {
                System.out.println("Select gender: {1} Male {2} Female");
                String genderInput = ConsoleUtility.validateInput(
                        "Select gender {1} Male {2} Female: ",
                        ConsoleUtility::isValidGender);
                newGender = genderInput.equals("1") ? "MALE" : "FEMALE";
            }

            String newContactNumber = ConsoleUtility.validateInput(
                    "Enter new contact number (8 digits, or press Enter to skip): ",
                    input -> input.isEmpty() || ConsoleUtility.isValidContactNumber(input));

            String newEmail = ConsoleUtility.validateInput(
                    "Enter new email address (or press Enter to skip): ",
                    input -> input.isEmpty() || ConsoleUtility.isValidEmail(input));

            if (newName.isEmpty() && newDateOfBirth.isEmpty() && newGender.isEmpty() &&
                    newContactNumber.isEmpty() && newEmail.isEmpty()) {
                System.out.println("No changes made.");
                return;
            }

            if (!newName.isEmpty())
                staffToUpdate.updateName(newName);
            if (!newDateOfBirth.isEmpty())
                staffToUpdate.updateDateOfBirth(newDateOfBirth);
            if (!newGender.isEmpty())
                staffToUpdate.updateGender(newGender);
            if (!newContactNumber.isEmpty())
                staffToUpdate.updateContactNumber(newContactNumber);
            if (!newEmail.isEmpty())
                staffToUpdate.updateEmailAddress(newEmail);

            boolean success = administratorController.manageStaff(Administrator.StaffAction.UPDATE, staffToUpdate);

            if (success) {
                System.out.println("\nStaff information updated successfully.");
                System.out.println("");
                displayStaffList(Collections.singletonList(staffToUpdate), "Updated Staff List");
            } else {
                System.out.println("Failed to update staff information.");
            }

        } catch (Exception e) {
            System.err.println("Error updating staff information: " + e.getMessage());
        }
    }

    /**
     * [OPTION 1.4] Removes a staff member.
     */
    private void removeStaff() {
        try {
            ConsoleUtility.printHeader("REMOVE STAFF");

            // Get the list of staff
            List<User> staffList = administratorController.getFilteredStaffList(new HashMap<>());
            if (staffList.isEmpty()) {
                System.out.println("No staff members found.");
                return;
            }

            // Display the list of staff
            displayStaffList(staffList, "Staff List");

            // Get the staff ID to remove
            System.out.println("");
            String userID = ConsoleUtility.validateInput("Enter staff ID to remove: ", input -> {
                boolean exists = staffList.stream().anyMatch(s -> s.getUserID().equals(input));
                if (!exists) {
                    System.out.println("Invalid ID, staff does not exist.");
                }
                return exists;
            });

            User staffToRemove = administratorController.getUserByID(userID);
            if (staffToRemove == null) {
                System.out.println("Staff not found.");
                return;
            }

            // Display the staff to be removed
            System.out.println("");
            displayStaffList(Collections.singletonList(staffToRemove), "Staff to Remove");

            if (!ConsoleUtility.getConfirmation("\nAre you sure you want to remove this staff member?")) {
                System.out.println("Staff removal cancelled.");
                return;
            }

            boolean success = administratorController.manageStaff(Administrator.StaffAction.REMOVE, staffToRemove);
            if (success) {
                System.out.println("\nStaff removed successfully.");

                // Display updated staff list
                List<User> updatedStaff = administratorController.getFilteredStaffList(new HashMap<>());
                if (!updatedStaff.isEmpty()) {
                    System.out.println("");
                    displayStaffList(updatedStaff, "Updated Staff List");
                } else {
                    System.out.println("No staff members remaining.");
                }
            } else {
                System.out.println("Failed to remove staff.");
            }
        } catch (Exception e) {
            System.err.println("Error removing staff: " + e.getMessage());
        }
    }

    /**
     * [OPTION 2] Views the details of all appointments.
     */
    private void viewAppointmentDetails() {
        while (true) {
            ConsoleUtility.printHeader("VIEW APPOINTMENT DETAILS");
            List<Appointment> appointments = administratorController.getAllAppointments();

            if (appointments.isEmpty()) {
                System.out.println("No appointments found.");
                return;
            }

            displayAppointmentList(appointments);

            System.out.println("\n{1} View detailed appointment information");
            System.out.println("{2} Return to main menu");
            int choice = Integer
                    .parseInt(ConsoleUtility.validateInput("Enter your choice: ", input -> input.matches("^[12]$")));

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
     * 
     * @param appointments The list of appointments to display
     */
    private void displayAppointmentList(List<Appointment> appointments) {
        LinkedHashMap<String, TableBuilder.ColumnMapping> columnMapping = new LinkedHashMap<>();

        // Appointment ID column
        columnMapping.put("appointmentID", new TableBuilder.ColumnMapping("Appointment ID", null));

        // Patient column with name and ID
        columnMapping.put("patientID", new TableBuilder.ColumnMapping("Patient (ID)",
                value -> {
                    String patientId = value.toString();
                    User patient = administratorController.getUserByID(patientId);
                    return patient != null ? patient.getName() + " (" + patientId + ")" : "Unknown";
                }));

        // Doctor column with name and ID
        columnMapping.put("doctorID", new TableBuilder.ColumnMapping("Doctor (ID)",
                value -> {
                    String doctorId = value.toString();
                    User doctor = administratorController.getUserByID(doctorId);
                    return doctor != null ? doctor.getName() + " (" + doctorId + ")" : "Unknown";
                }));

        // Time column combining date and time from slot
        columnMapping.put("slotID", new TableBuilder.ColumnMapping("Schedule",
                value -> {
                    String slotId = value.toString();
                    Slot slot = administratorController.getSlotByID(slotId);
                    if (slot != null) {
                        return slot.getDate() + " " + slot.getStartTime() + "-" + slot.getEndTime();
                    }
                    return "Unknown";
                }));

        columnMapping.put("status", new TableBuilder.ColumnMapping("Status", null));

        TableBuilder.createTable("Appointment List", appointments, columnMapping, 25);
    }

    /**
     * [OPTION 2.1] Views the details of a specific appointment, including its
     * outcome if available.
     * 
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
        User patient = administratorController.getUserByID(selectedAppointment.getPatientID());
        User doctor = administratorController.getUserByID(selectedAppointment.getDoctorID());

        System.out.println("\nDETAILED APPOINTMENT INFORMATION");
        System.out.println("-".repeat(50));
        System.out.printf("Appointment ID: %s%n", selectedAppointment.getAppointmentID());
        System.out.printf("Status: %s%n", selectedAppointment.getStatus());
        System.out.println("\nPatient Information:");
        System.out.printf("Patient ID: %s%n", patient.getUserID());
        System.out.printf("Patient Name: %s%n", patient.getName());
        System.out.printf("Contact: %s%n", patient.getContactNumber());
        System.out.println("\nDoctor Information:");
        System.out.printf("Doctor ID: %s%n", doctor.getUserID());
        System.out.printf("Doctor Name: %s%n", doctor.getName());
        System.out.printf("Contact: %s%n", doctor.getContactNumber());
        System.out.println("\nSchedule Information:");
        System.out.printf("Date: %s%n", slot.getDate());
        System.out.printf("Time: %s - %s%n", slot.getStartTime(), slot.getEndTime());
        System.out.printf("Slot Status: %s%n", slot.getStatus());
        System.out.println("-".repeat(50));

        String outcomeID = selectedAppointment.getOutcomeID();
        if (outcomeID != null && !outcomeID.isEmpty()) {
            Outcome outcome = administratorController.getOutcomeByID(outcomeID);
            if (outcome != null) {
                System.out.println("\nOUTCOME INFORMATION");
                System.out.println("-".repeat(50));
                System.out.printf("Outcome ID: %s%n", outcome.getOutcomeID());
                System.out.printf("Service Provided: %s%n", outcome.getServiceProvided());
                System.out.println("\nConsultation Notes:");
                System.out.println(outcome.getConsultationNotes());

                // Display prescription information if available
                String prescriptionID = outcome.getPrescriptionID();
                if (prescriptionID != null && !prescriptionID.isEmpty()) {
                    Prescription prescription = administratorController.getPrescriptionByID(prescriptionID);
                    if (prescription != null) {
                        System.out.println("\nPRESCRIPTION DETAILS");
                        System.out.println("-".repeat(50));
                        System.out.printf("Prescription ID: %s%n", prescription.getPrescriptionID());

                        // Get and display medication details
                        String[] medicationIDs = prescription.getMedicationID().split(";");
                        System.out.println("\nPrescribed Medications:");
                        for (String medicationID : medicationIDs) {
                            Medication medication = administratorController.getMedicationByID(medicationID);
                            if (medication != null) {
                                System.out.printf("- %s (ID: %s)%n", medication.getName(), medicationID);
                                System.out.printf("  Current Stock Level: %d%n", medication.getStockLevel());
                            }
                        }

                        System.out.printf("Status: %s%n", prescription.getStatus());
                        System.out.printf("Notes: %s%n", prescription.getNotes());
                    }
                } else {
                    System.out.println("\nNo prescription was issued for this appointment.");
                }
            }
        } else {
            System.out.println("\nNo outcome record available for this appointment.");
        }
        System.out.println("-".repeat(50));
    }

    /**
     * [OPTION 3] Manages the medication inventory.
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
     * [OPTION 3.1] Displays the list of medications.
     */
    private void viewMedications() {
        List<Medication> medications = administratorController.getAllMedications();
        if (medications.isEmpty()) {
            System.out.println("No medications found.");
        } else {
            System.out.println();
            ConsoleUtility.printHeader("MEDICATION LIST");
            displayMedicationList(medications, "Current Medication List");
        }
    }

    /**
     * Displays the list of medications.
     */
    private void displayMedicationList(List<Medication> medications, String title) {
        LinkedHashMap<String, TableBuilder.ColumnMapping> columnMapping = new LinkedHashMap<>();
        columnMapping.put("medicationID", new TableBuilder.ColumnMapping("ID", null));
        columnMapping.put("name", new TableBuilder.ColumnMapping("Name", null));
        columnMapping.put("stockLevel", new TableBuilder.ColumnMapping("Stock Level", null));
        columnMapping.put("lowStockAlertLevel", new TableBuilder.ColumnMapping("Low Stock Alert Level", null));

        TableBuilder.createTable(title, medications, columnMapping, 30);
    }

    /**
     * [OPTION 3.2] Adds a new medication to the inventory.
     */
    private void addMedication() {
        try {
            ConsoleUtility.printHeader("ADD MEDICATION");
            
            // Display current medication list first
            List<Medication> currentMedications = administratorController.getAllMedications();
            if (!currentMedications.isEmpty()) {
                displayMedicationList(currentMedications, "Current Medication List");
            }
            
            System.out.println("");
            String medicationID = ConsoleUtility.validateInput(
                    "Enter medication ID (or press Enter to go back): ",
                    input -> {
                        if (input.isEmpty()) 
                            return true;
                        if (!ConsoleUtility.isValidID(input)) {
                            System.out.println("Invalid ID format.");
                            return false;
                        }
                        if (administratorController.getMedicationByID(input) != null) {
                            System.out.println("Medication ID already exists.");
                            return false;
                        }
                        return true;
                    });

            // If medicationID is empty, return to main menu
            if (medicationID.isEmpty()) {
                return;
            }

            // Get medication name
            String name = ConsoleUtility.validateInput("Enter medication name: ", ConsoleUtility::isValidName);

            int stockLevel = Integer.parseInt(
                    ConsoleUtility.validateInput("Enter initial stock level: ", ConsoleUtility::isValidInteger));
            if (stockLevel < 0) {
                System.out.println("Error: Stock level cannot be negative.");
                return;
            }

            int lowStockAlertLevel = Integer.parseInt(
                    ConsoleUtility.validateInput("Enter low stock alert level: ", ConsoleUtility::isValidInteger));
            if (lowStockAlertLevel < 0) {
                System.out.println("Error: Low stock alert level cannot be negative.");
                return;
            }

            Medication newMedication = new Medication(medicationID, name, stockLevel, lowStockAlertLevel);
            boolean success = administratorController.addMedication(newMedication);
            if (success) {
                System.out.println("\nMedication added successfully.");
                // Display updated medication list
                List<Medication> updatedMedications = administratorController.getAllMedications();
                System.out.println("");
                displayMedicationList(updatedMedications, "Updated Medication List");
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
     * [OPTION 3.3] Updates the information of an existing medication.
     */
    private void updateMedicationInformation() {
        try {
            ConsoleUtility.printHeader("UPDATE MEDICATION INFORMATION");

            // Get all medications
            List<Medication> medications = administratorController.getAllMedications();

            // Display the list of medications before updating
            displayMedicationList(medications, "Current Medication List");
            
            // Get the medication ID to update
            System.out.println("");
            String medicationID = ConsoleUtility.validateInput("Enter medication ID to update: ",
                    input -> {
                        boolean exists = medications.stream().anyMatch(m -> m.getMedicationID().equals(input));
                        if (!exists) {
                            System.out.println("Invalid ID, medication does not exist.");
                        }
                        return exists;
                    });

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

            String alertLevelInput = ConsoleUtility.validateInput(
                    "Enter new low stock alert level (or press enter to skip): ",
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
                System.out.println("\nMedication information updated successfully.");
                // Display updated list
                List<Medication> updatedMedications = administratorController.getAllMedications();
                System.out.println("");
                displayMedicationList(updatedMedications, "Updated Medication List");
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
     * [OPTION 3.4] Removes a medication from the inventory.
     */
    private void removeMedication() {
        try {
            ConsoleUtility.printHeader("REMOVE MEDICATION");

            List<Medication> medications = administratorController.getAllMedications();
            if (medications.isEmpty()) {
                System.out.println("\nNo medications found.");
                return;
            }

            displayMedicationList(medications, "Current Medication List");
            
            // Get the medication ID to remove
            System.out.println("");
            String medicationID = ConsoleUtility.validateInput(
                    "Enter medication ID to remove (or press Enter to go back): ",
                    input -> {
                        if (input.isEmpty())
                            return true;
                        boolean exists = medications.stream().anyMatch(m -> m.getMedicationID().equals(input));
                        if (!exists) {
                            System.out.println("Invalid ID, medication does not exist.");
                        }
                        return exists;
                    });

            if (medicationID.isEmpty()) {
                return;
            }

            Medication medicationToRemove = administratorController.getMedicationByID(medicationID);
            System.out.println("");
            displayMedicationList(Collections.singletonList(medicationToRemove), "Medication to Remove");
            
            if (!ConsoleUtility.getConfirmation("\nAre you sure you want to remove this medication?")) {
                System.out.println("Medication removal cancelled.");
                return;
            }

            boolean success = administratorController.removeMedication(medicationID);

            if (success) {
                System.out.println("\nMedication removed successfully.");
                // Display updated medication list
                List<Medication> updatedMedications = administratorController.getAllMedications();
                if (!updatedMedications.isEmpty()) {
                    System.out.println("");
                    displayMedicationList(updatedMedications, "Updated Medication List");
                }
            } else {
                System.out.println("Failed to remove medication.");
            }

        } catch (Exception e) {
            System.err.println("Error removing medication: " + e.getMessage());
        }
    }

    /**
     * [OPTION 4] Handles the approval of replenishment requests.
     */
    private void approveReplenishmentRequests() {
        while (true) {
            ConsoleUtility.printHeader("PENDING REPLENISHMENT REQUESTS");
            List<Request> pendingRequests = administratorController.getPendingRequests();

            if (pendingRequests.isEmpty()) {
                System.out.println("No pending replenishment requests found.");
                return;
            }

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
     * 
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
     * [OPTION 4.1] Approves a selected replenishment request.
     * 
     * @param pendingRequests The list of pending requests
     */
    private void approveRequest(List<Request> pendingRequests) {
        String requestID = ConsoleUtility.validateInput("Enter the Request ID to approve: ",
                input -> {
                    boolean exists = pendingRequests.stream().anyMatch(r -> r.getRequestID().equals(input));
                    if (!exists) {
                        System.out.println("Invalid ID, request does not exist.");
                    }
                    return exists;
                });

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
