package controller;

import entity.Medication;
import java.util.List;

/**
 * Manages medication inventory in the Hospital Management System.
 */
public class InventoryController {
    
    /**
     * Retrieves the current inventory of medications.
     * @return List of Medication objects
     */
    public List<Medication> viewInventory() {
        // TODO: Implement logic to retrieve all medications in inventory
        return null;
    }
    
    /**
     * Updates the stock level of a specific medication.
     * @param medicationID The unique identifier of the medication
     * @param quantity The new quantity to set
     * @throws InventoryException if the medication doesn't exist or the quantity is invalid
     */
    public void updateStockLevel(String medicationID, int quantity) throws InventoryException {
        // TODO: Implement logic to update stock level
    }
    
    /**
     * Adds a new medication to the inventory.
     * @param medication The Medication object to add
     * @throws InventoryException if the medication already exists or is invalid
     */
    public void addMedication(Medication medication) throws InventoryException {
        // TODO: Implement logic to add new medication
    }
    
    /**
     * Removes a medication from the inventory.
     * @param medicationID The unique identifier of the medication to remove
     * @throws InventoryException if the medication doesn't exist or cannot be removed
     */
    public void removeMedication(String medicationID) throws InventoryException {
        // TODO: Implement logic to remove medication
    }
    
    /**
     * Updates the low stock alert level for a specific medication.
     * @param medicationID The unique identifier of the medication
     * @param alertLevel The new alert level to set
     * @throws InventoryException if the medication doesn't exist or the alert level is invalid
     */
    public void updateLowStockAlert(String medicationID, int alertLevel) throws InventoryException {
        // TODO: Implement logic to update low stock alert level
    }
    
    /**
     * Retrieves all medications that are currently below their low stock alert level.
     * @return List of Medication objects that are low in stock
     */
    public List<Medication> getLowStockMedications() {
        // TODO: Implement logic to retrieve low stock medications
        return null;
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