package controller;

import entity.*;
import controller.data.*;
import java.util.List;

/**
 * Controller class for managing pharmacist-related operations in the Hospital Management System.
 */
public class PharmacistController {

    private MedicationDataManager medicationDataManager;
    private RequestDataManager requestDataManager;
    private AuthenticationController authController;

    /**
     * Constructor for PharmacistController.
     * 
     * @param medicationDataManager The MedicationDataManager instance
     * @param requestDataManager The RequestDataManager instance
     * @param authController The AuthenticationController instance
     */
    public PharmacistController(MedicationDataManager medicationDataManager, RequestDataManager requestDataManager, AuthenticationController authController) {
        this.medicationDataManager = medicationDataManager;
        this.requestDataManager = requestDataManager;
        this.authController = authController;
    }

    /**
     * Retrieves all medications in the inventory.
     * 
     * @return List of all Medication objects
     */
    public List<Medication> getAllMedications() {
        return medicationDataManager.getMedications();
    }

    /**
     * Retrieves a medication by its ID.
     * 
     * @param medicationID The ID of the medication to retrieve
     * @return The Medication object with the specified ID, or null if not found
     */
    public Medication getMedicationByID(String medicationID) {
        return medicationDataManager.getMedicationByID(medicationID);
    }

    /**
     * Submits a new replenishment request.
     * 
     * @param medicationID The ID of the medication to replenish
     * @param quantity The quantity requested
     * @return true if the request was successfully submitted, false otherwise
     */
    public boolean submitReplenishmentRequest(String medicationID, int quantity) {
        Pharmacist currentPharmacist = (Pharmacist) authController.getCurrentUser();
        String requestID = generateRequestID();
        Request newRequest = new Request(requestID, medicationID, quantity, Request.RequestStatus.PENDING, currentPharmacist.getUserID(), null);
        
        try {
            requestDataManager.addRequest(newRequest);
            return true;
        } catch (IllegalArgumentException e) {
            System.err.println("Error submitting replenishment request: " + e.getMessage());
            return false;
        }
    }

    /**
     * Generates a new unique request ID.
     * @return A new unique request ID
     */
    private String generateRequestID() {
        // Implementation depends on your ID generation strategy
        // This is a simple example and might need to be more robust in a real system
        return "R" + String.format("%02d", requestDataManager.getRequests().size() + 1);
    }
}
