package entity;

import java.util.List;

/**
 * Represents a pharmacist managing prescriptions and medication inventory in the Hospital Management System.
 * Extends the User class.
 */
public class Pharmacist extends User {
    
    /**
     * Constructor for Pharmacist.
     * @param userID The unique identifier for the user
     * @param password The password for the user
     * @param role The role of the user
     * @param name The name of the user
     * @param dateOfBirth The date of birth of the user
     * @param gender The gender of the user
     * @param contactNumber The contact number of the user
     * @param emailAddress The email address of the user
     */
    public Pharmacist(String userID, String password, UserRole role, String name, String dateOfBirth, String gender, String contactNumber, String emailAddress) {
        super(userID, password, role, name, dateOfBirth, gender, contactNumber, emailAddress);
    }

    /**
     * Retrieves all current prescriptions.
     * @return List of Prescription objects
     */
    public List<Prescription> viewPrescriptions() {
        // TODO: Implement logic to retrieve and return prescriptions
        return null;
    }
    
    /**
     * Updates the status of a specific prescription.
     * @param prescriptionID The ID of the prescription to update
     * @param status The new status to set
     * @return boolean indicating whether the update was successful
     */
    public boolean updatePrescriptionStatus(String prescriptionID, Prescription.PrescriptionStatus status) {
        // TODO: Implement logic to update prescription status
        return false;
    }
    
    /**
     * Monitors the current medication inventory.
     * @return Inventory object representing the current state of the medication inventory
     */
    public Medication monitorInventory() {
        // TODO: Implement logic to retrieve and return inventory status
        return null;
    }
    
    /**
     * Submits a request to replenish a specific medication.
     * @param medicationID The ID of the medication to replenish
     * @param quantity The quantity to request
     * @return boolean indicating whether the request was successfully submitted
     */
    public boolean submitReplenishmentRequest(String medicationID, int quantity) {
        // TODO: Implement logic to submit replenishment request
        return false;
    }

}
