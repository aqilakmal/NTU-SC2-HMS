package controller;

import entity.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

import controller.data.*;

/**
 * Controller class responsible for managing administrative operations in the Hospital Management System.
 * This class handles staff management, appointment oversight, medication inventory control,
 * and replenishment request processing. It serves as the central control point for all
 * administrator-specific functionalities.
 * 
 * The controller coordinates between various data managers to provide comprehensive 
 * administrative capabilities including:
 * - Staff management (adding, updating, removing staff members)
 * - Appointment and medical outcome oversight
 * - Medication inventory management
 * - Replenishment request processing
 * - System-wide data access and modification
 *
 * @author Group 7
 * @version 1.0
 */
public class AdministratorController {

    private UserDataManager userDataManager;
    private AppointmentDataManager appointmentDataManager;
    private OutcomeDataManager outcomeDataManager;
    private MedicationDataManager medicationDataManager;
    private RequestDataManager requestDataManager;
    private AuthenticationController authController;
    private PrescriptionDataManager prescriptionDataManager;
    private SlotDataManager slotDataManager;

    /**
     * Constructs a new AdministratorController with all required data managers and controllers.
     * Initializes the controller with necessary dependencies to manage hospital operations.
     * This constructor ensures all required components are properly injected for system functionality.
     *
     * @param userDataManager The manager handling user data operations
     * @param appointmentDataManager The manager handling appointment scheduling and management
     * @param outcomeDataManager The manager handling medical outcome records
     * @param medicationDataManager The manager handling medication inventory
     * @param requestDataManager The manager handling replenishment requests
     * @param authController The controller handling authentication operations
     * @param prescriptionDataManager The manager handling prescription records
     * @param slotDataManager The manager handling appointment time slots
     */
    public AdministratorController(UserDataManager userDataManager, AppointmentDataManager appointmentDataManager, 
                                   OutcomeDataManager outcomeDataManager, MedicationDataManager medicationDataManager,
                                   RequestDataManager requestDataManager, AuthenticationController authController,
                                   PrescriptionDataManager prescriptionDataManager, SlotDataManager slotDataManager) {
        this.userDataManager = userDataManager;
        this.appointmentDataManager = appointmentDataManager;
        this.outcomeDataManager = outcomeDataManager;
        this.medicationDataManager = medicationDataManager;
        this.requestDataManager = requestDataManager;
        this.authController = authController;
        this.prescriptionDataManager = prescriptionDataManager;
        this.slotDataManager = slotDataManager;
    }

    /**
     * Manages hospital staff members by performing specified administrative actions.
     * Handles the addition, update, and removal of staff members in the system.
     * Includes error handling for invalid operations and data validation.
     *
     * @param staffAction The administrative action to perform (ADD, UPDATE, or REMOVE)
     * @param staffData The staff member data to be managed
     * @return boolean indicating whether the operation was successful
     */
    public boolean manageStaff(Administrator.StaffAction staffAction, User staffData) {
        try {
            switch (staffAction) {
                case ADD: userDataManager.addUser(staffData); break;
                case UPDATE: userDataManager.updateUser(staffData); break;
                case REMOVE: userDataManager.removeUser(staffData.getUserID()); break;
                default: return false;
            }
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves a user from the system by their unique identifier.
     * Searches the user database for a matching user ID.
     * Returns null if no user is found with the specified ID.
     *
     * @param userID The unique identifier of the user to retrieve
     * @return The User object if found, null otherwise
     */
    public User getUserByID(String userID) {
        return userDataManager.getUserByID(userID);
    }

    /**
     * Retrieves a filtered list of staff members based on specified criteria.
     * Applies the provided filters to the user database to find matching staff members.
     * Supports filtering by various attributes such as role, gender, and other parameters.
     *
     * @param filters Map of filter criteria to apply to the staff search
     * @return List of User objects matching the specified filter criteria
     */
    public List<User> getFilteredStaffList(Map<String, String> filters) {
        return userDataManager.getFilteredUsers(filters);
    }

    /**
     * Retrieves all appointments currently in the system.
     * Provides a comprehensive list of all scheduled appointments.
     * Includes both upcoming and past appointments.
     *
     * @return List of all Appointment objects in the system
     */
    public List<Appointment> getAllAppointments() {
        return appointmentDataManager.getAllAppointments();
    }

    /**
     * Retrieves all medical outcomes recorded in the system.
     * Provides access to the complete history of appointment outcomes.
     * Includes all medical results and treatment records.
     *
     * @return List of all Outcome objects in the system
     */
    public List<Outcome> getAllOutcomes() {
        return outcomeDataManager.getAllOutcomes();
    }

    /**
     * Retrieves a filtered list of appointments based on specified criteria.
     * Applies provided filters to find appointments matching specific conditions.
     * Supports filtering by status, date range, and other relevant parameters.
     *
     * @param filters Map of filter criteria to apply to the appointment search
     * @return List of Appointment objects matching the filter criteria
     */
    public List<Appointment> getFilteredAppointments(Map<String, String> filters) {
        return appointmentDataManager.getFilteredAppointments(filters);
    }

    /**
     * Retrieves a specific medical outcome record by its identifier.
     * Searches the outcome database for a matching outcome ID.
     * Returns null if no outcome is found with the specified ID.
     *
     * @param outcomeID The unique identifier of the outcome to retrieve
     * @return The Outcome object if found, null otherwise
     */
    public Outcome getOutcomeByID(String outcomeID) {
        return outcomeDataManager.getOutcomeByID(outcomeID);
    }

    /**
     * Retrieves all medications currently in the hospital inventory.
     * Provides a complete list of all medications available in the system.
     * Includes both in-stock and out-of-stock medications.
     *
     * @return List of all Medication objects in the inventory
     */
    public List<Medication> getAllMedications() {
        return medicationDataManager.getMedications();
    }

    /**
     * Retrieves a specific medication by its identifier.
     * Searches the medication inventory for a matching medication ID.
     * Returns null if no medication is found with the specified ID.
     *
     * @param medicationID The unique identifier of the medication to retrieve
     * @return The Medication object if found, null otherwise
     */
    public Medication getMedicationByID(String medicationID) {
        return medicationDataManager.getMedicationByID(medicationID);
    }

    /**
     * Adds a new medication to the hospital inventory.
     * Validates the medication data before adding to the system.
     * Handles potential errors during the addition process.
     *
     * @param medication The Medication object to add to the inventory
     * @return boolean indicating whether the addition was successful
     */
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
     * Updates an existing medication's information in the inventory.
     * Validates the updated medication data before applying changes.
     * Handles potential errors during the update process.
     *
     * @param updatedMedication The Medication object containing updated information
     * @return boolean indicating whether the update was successful
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
     * Removes a medication from the hospital inventory.
     * Validates the medication ID and handles the removal process.
     * Ensures proper cleanup of related records.
     *
     * @param medicationID The unique identifier of the medication to remove
     * @return boolean indicating whether the removal was successful
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
     * Updates the stock level of a specific medication.
     * Validates the new quantity and ensures it's non-negative.
     * Updates the medication's stock level in the inventory.
     *
     * @param medicationID The unique identifier of the medication to update
     * @param quantity The new stock level to set
     * @throws InventoryException if the medication is not found or quantity is invalid
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

    /**
     * Updates the low stock alert level for a specific medication.
     * Validates the new alert level and ensures it's non-negative.
     * Sets the threshold at which low stock warnings are triggered.
     *
     * @param medicationID The unique identifier of the medication to update
     * @param alertLevel The new low stock alert level to set
     * @throws InventoryException if the medication is not found or alert level is invalid
     */
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

    /**
     * Retrieves a list of medications that are currently below their low stock alert levels.
     * Filters the medication inventory to identify items needing replenishment.
     * Helps in proactive inventory management.
     *
     * @return List of Medication objects that are below their alert levels
     */
    public List<Medication> getLowStockMedications() {
        Map<String, String> filters = new HashMap<>();
        filters.put("lowStock", "true");
        return medicationDataManager.getFilteredMedications(filters).stream()
            .filter(med -> med.getStockLevel() < med.getLowStockAlertLevel())
            .collect(Collectors.toList());
    }

    /**
     * Retrieves all pending replenishment requests in the system.
     * Provides access to requests awaiting administrative approval.
     * Helps manage inventory replenishment workflow.
     *
     * @return List of pending Request objects
     */
    public List<Request> getPendingRequests() {
        return requestDataManager.getPendingRequests();
    }

    /**
     * Processes and approves a replenishment request.
     * Updates medication stock levels upon approval.
     * Records the approving administrator's information.
     *
     * @param requestID The unique identifier of the request to approve
     * @return boolean indicating whether the approval was successful
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

            request.setApprovedBy(authController.getCurrentUser().getUserID());

            requestDataManager.updateRequest(request);

            return true;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves detailed information about a specific appointment.
     * Includes associated slot information and other relevant details.
     * Returns null if the appointment is not found.
     *
     * @param appointmentID The unique identifier of the appointment to retrieve
     * @return Map containing appointment and slot details, or null if not found
     */
    public Map<String, Object> getAppointmentDetails(String appointmentID) {
        return appointmentDataManager.getAppointmentDetails(appointmentID);
    }

    /**
     * Retrieves a specific prescription by its identifier.
     * Searches the prescription database for a matching prescription ID.
     * Returns null if no prescription is found with the specified ID.
     *
     * @param prescriptionID The unique identifier of the prescription to retrieve
     * @return The Prescription object if found, null otherwise
     */
    public Prescription getPrescriptionByID(String prescriptionID) {
        return prescriptionDataManager.getPrescriptionByID(prescriptionID);
    }

    /**
     * Custom exception class for handling inventory-related errors.
     * Provides specific error handling for inventory management operations.
     * Extends the standard Exception class with additional constructors.
     */
    public class InventoryException extends Exception {
        /**
         * Constructs a new InventoryException with the specified message.
         *
         * @param message The error message describing the exception
         */
        public InventoryException(String message) {
            super(message);
        }

        /**
         * Constructs a new InventoryException with the specified message and cause.
         *
         * @param message The error message describing the exception
         * @param cause The cause of the exception
         */
        public InventoryException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * Retrieves a specific appointment slot by its identifier.
     * Searches the slot database for a matching slot ID.
     * Returns null if no slot is found with the specified ID.
     *
     * @param slotID The unique identifier of the slot to retrieve
     * @return The Slot object if found, null otherwise
     */
    public Slot getSlotByID(String slotID) {
        return slotDataManager.getSlotByID(slotID);
    }
}