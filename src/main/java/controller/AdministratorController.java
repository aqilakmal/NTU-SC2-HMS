package controller;

import entity.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

import controller.data.*;

/**
 * Controller class for managing staff-related operations, appointments, medication inventory, and replenishment requests in the Hospital Management System.
 */
public class AdministratorController {

    private UserDataManager userDataManager;
    private AppointmentDataManager appointmentDataManager;
    private OutcomeDataManager outcomeDataManager;
    private MedicationDataManager medicationDataManager;
    private RequestDataManager requestDataManager;

    /**
     * Constructor for AdministratorController.
     * @param userDataManager The UserDataManager instance to manage user data.
     * @param appointmentDataManager The AppointmentDataManager instance to manage appointment data.
     * @param outcomeDataManager The OutcomeDataManager instance to manage outcome data.
     * @param medicationDataManager The MedicationDataManager instance to manage medication data.
     * @param requestDataManager The RequestDataManager instance to manage replenishment request data.
     */
    public AdministratorController(UserDataManager userDataManager, AppointmentDataManager appointmentDataManager, 
                                   OutcomeDataManager outcomeDataManager, MedicationDataManager medicationDataManager,
                                   RequestDataManager requestDataManager) {
        this.userDataManager = userDataManager;
        this.appointmentDataManager = appointmentDataManager;
        this.outcomeDataManager = outcomeDataManager;
        this.medicationDataManager = medicationDataManager;
        this.requestDataManager = requestDataManager;
    }

    /**
     * Manages hospital staff (doctors and pharmacists).
     * @param staffAction The action to perform (e.g., ADD, UPDATE, REMOVE)
     * @param staffData The data of the staff member to manage
     * @return boolean indicating whether the action was successful
     */
    public boolean manageStaff(Administrator.StaffAction staffAction, User staffData) {
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

    /**
     * Retrieves all appointments.
     * @return List of all Appointment objects.
     */
    public List<Appointment> getAllAppointments() {
        return appointmentDataManager.getAllAppointments();
    }

    /**
     * Retrieves all appointment outcomes.
     * @return List of all Outcome objects.
     */
    public List<Outcome> getAllOutcomes() {
        return outcomeDataManager.getAllOutcomes();
    }

    /**
     * Retrieves a filtered list of appointments based on specified criteria.
     * @param filters Map of filter criteria (e.g., status, date range, etc.)
     * @return List of Appointment objects matching the filter criteria
     */
    public List<Appointment> getFilteredAppointments(Map<String, String> filters) {
        return appointmentDataManager.getFilteredAppointments(filters);
    }

    /**
     * Retrieves an outcome by its ID.
     * @param outcomeID The ID of the outcome to retrieve
     * @return The Outcome object if found, null otherwise
     */
    public Outcome getOutcomeByID(String outcomeID) {
        return outcomeDataManager.getOutcomeByID(outcomeID);
    }

    /**
     * Retrieves all medications.
     * @return List of all Medication objects.
     */
    public List<Medication> getAllMedications() {
        return medicationDataManager.getMedications();
    }

    /**
     * Adds a new medication to the inventory.
     * @param medication The Medication object to add.
     * @return boolean indicating whether the action was successful.
     */
    public Medication getMedicationByID(String medicationID) {
        return medicationDataManager.getMedicationByID(medicationID);
    }

    public boolean addMedication(Medication medication) {
        try {
            medicationDataManager.addMedication(medication);
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Updates an existing medication's information.
     * @param updatedMedication The Medication object with updated information.
     * @return boolean indicating whether the action was successful.
     */
    public boolean updateMedication(Medication updatedMedication) {
        try {
            medicationDataManager.updateMedication(updatedMedication);
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Removes a medication from the inventory.
     * @param medicationID The ID of the medication to remove.
     * @return boolean indicating whether the action was successful.
     */
    public boolean removeMedication(String medicationID) {
        try {
            medicationDataManager.removeMedication(medicationID);
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves all pending replenishment requests.
     * @return List of pending Request objects.
     */
    public void updateStockLevel(String medicationID, int quantity) throws InventoryException {
        Medication medication = getMedicationByID(medicationID);
        if (medication == null) {
            throw new InventoryException("Medication with ID " + medicationID + " not found.");
        }
        if (quantity < 0) {
            throw new InventoryException("Stock level cannot be negative.");
        }
        medication.setStockLevel(quantity);
        updateMedication(medication);
    }

    public void updateLowStockAlert(String medicationID, int alertLevel) throws InventoryException {
        Medication medication = getMedicationByID(medicationID);
        if (medication == null) {
            throw new InventoryException("Medication with ID " + medicationID + " not found.");
        }
        if (alertLevel < 0) {
            throw new InventoryException("Low stock alert level cannot be negative.");
        }
        medication.setLowStockAlertLevel(alertLevel);
        updateMedication(medication);
    }

    public List<Medication> getLowStockMedications() {
        Map<String, String> filters = new HashMap<>();
        filters.put("lowStock", "true");
        return medicationDataManager.getFilteredMedications(filters).stream()
            .filter(med -> med.getStockLevel() < med.getLowStockAlertLevel())
            .collect(Collectors.toList());
    }

    // Replenishment request management methods
    public List<Request> getPendingRequests() {
        return requestDataManager.getPendingRequests();
    }

    /**
     * Approves a replenishment request and updates the medication stock.
     * @param requestID The ID of the request to approve.
     * @return boolean indicating whether the action was successful.
     */
    public boolean approveRequest(String requestID) {
        try {
            Request request = requestDataManager.getRequestByID(requestID);
            if (request == null || request.getStatus() != Request.RequestStatus.PENDING) {
                return false;
            }

            Medication medication = getMedicationByID(request.getMedicationID());
            if (medication == null) {
                return false;
            }

            medication.increaseStockLevel(request.getQuantity());
            updateMedication(medication);

            request.setStatus(Request.RequestStatus.APPROVED);
            requestDataManager.updateRequest(request);

            return true;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    /**
     * InventoryException extends Exception to handle exceptions related to inventory.
     */
    public class InventoryException extends Exception {
        public InventoryException(String message) {
            super(message);
        }

        public InventoryException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
