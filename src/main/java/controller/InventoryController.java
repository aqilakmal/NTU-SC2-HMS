package controller;

import entity.Medication;
import controller.data.*;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * Manages medication inventory in the Hospital Management System.
 */
public class InventoryController {


    /**
     * MedicationDataManager instance to manage medication data.
     */
    private MedicationDataManager medicationDataManager;

    /**
     * Constructor for InventoryController.
     * Initializes the MedicationDataManager and loads medication data.
     */
    public InventoryController() {
        medicationDataManager = new MedicationDataManager();
    }
    
    /**
     * Retrieves the current inventory of medications.
     * @return List of Medication objects
     */
    public List<Medication> viewInventory() {
        return medicationDataManager.getMedications();
    }
    
    /**
     * Updates the stock level of a specific medication.
     * @param medicationID The unique identifier of the medication
     * @param quantity The new quantity to set
     * @throws InventoryException if the medication doesn't exist or the quantity is invalid
     */
    public void updateStockLevel(String medicationID, int quantity) throws InventoryException {
        Medication medication = medicationDataManager.getMedicationByID(medicationID);
        if (medication == null) {
            throw new InventoryException("Medication with ID " + medicationID + " not found.");
        }
        if (quantity < 0) {
            throw new InventoryException("Stock level cannot be negative.");
        }
        medication.setStockLevel(quantity);
        medicationDataManager.updateMedication(medication);
    }
    
    /**
     * Adds a new medication to the inventory.
     * @param medication The Medication object to add
     * @throws InventoryException if the medication already exists or is invalid
     */
    public void addMedication(Medication medication) throws InventoryException {
        try {
            medicationDataManager.addMedication(medication);
        } catch (IllegalArgumentException e) {
            throw new InventoryException(e.getMessage());
        }
    }
    
    /**
     * Removes a medication from the inventory.
     * @param medicationID The unique identifier of the medication to remove
     * @throws InventoryException if the medication doesn't exist or cannot be removed
     */
    public void removeMedication(String medicationID) throws InventoryException {
        try {
            medicationDataManager.removeMedication(medicationID);
        } catch (IllegalArgumentException e) {
            throw new InventoryException(e.getMessage());
        }
    }

    /**
     * Updates the low stock alert level for a specific medication.
     * @param medicationID The unique identifier of the medication
     * @param alertLevel The new alert level to set
     * @throws InventoryException if the medication doesn't exist or the alert level is invalid
     */
    public void updateLowStockAlert(String medicationID, int alertLevel) throws InventoryException {
        Medication medication = medicationDataManager.getMedicationByID(medicationID);
        if (medication == null) {
            throw new InventoryException("Medication with ID " + medicationID + " not found.");
        }
        if (alertLevel < 0) {
            throw new InventoryException("Low stock alert level cannot be negative.");
        }
        medication.setLowStockAlertLevel(alertLevel);
        try {
            medicationDataManager.updateMedication(medication);
        } catch (IllegalArgumentException e) {
            throw new InventoryException(e.getMessage());
        }
    }
    
    /**
     * Retrieves all medications that are currently below their low stock alert level.
     * @return List of Medication objects that are low in stock
     */
    public List<Medication> getLowStockMedications() {
        Map<String, String> filters = new HashMap<>();
        filters.put("lowStock", "true");
        return medicationDataManager.getFilteredMedications(filters).stream()
            .filter(med -> med.getStockLevel() < med.getLowStockAlertLevel())
            .collect(Collectors.toList());
    }

    /**
     * InventoryException extends Exception to handle exceptions related to inventory.
     */
    public class InventoryException extends Exception {
      
        /**
         * Constructor for InventoryException with a message.
         * @param message The message to display when the exception is thrown.
         */
        public InventoryException(String message) {
            super(message);
        }

        /**
         * Constructor for InventoryException with a message and a cause.
         * @param message The message to display when the exception is thrown.
         * @param cause The cause of the exception.
         */
        public InventoryException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}