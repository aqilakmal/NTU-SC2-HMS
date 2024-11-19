package entity;

/**
 * Represents a medication in the inventory of the Hospital Management System. Medications are managed
 * by pharmacists and administrators to track stock levels and fulfill prescriptions.
 * 
 * Key responsibilities include:
 * - Tracking medication inventory levels and low stock alerts
 * - Supporting prescription fulfillment by pharmacists
 * - Enabling inventory management by administrators
 * - Maintaining medication details like name and ID
 * 
 * The Medication class provides core functionality for medication inventory tracking
 * and stock level management within the hospital system.
 *
 * @author Group 7
 * @version 1.0
 */
public class Medication {
    
    /**
     * Unique identifier for the medication.
     * Used to track and reference specific medications in the system.
     */
    private String medicationID;
    
    /**
     * Name of the medication.
     * The standard name used to identify the medication in prescriptions and inventory.
     */
    private String name;
    
    /**
     * Current stock level of the medication.
     * Tracks the current quantity available in inventory.
     */
    private int stockLevel;
    
    /**
     * The stock level at which a low stock alert should be triggered.
     * Used to notify pharmacists when replenishment is needed.
     */
    private int lowStockAlertLevel;
    
    /**
     * Constructor for creating a new Medication instance in the system.
     * Initializes all required fields for tracking a medication in inventory.
     * Validates and sets up the core medication data including identifier,
     * name, and stock tracking information.
     *
     * @param medicationID The unique identifier for the medication
     * @param name The name of the medication
     * @param stockLevel The current stock level of the medication
     * @param lowStockAlertLevel The stock level at which a low stock alert should be triggered
     */
    public Medication(String medicationID, String name, int stockLevel, int lowStockAlertLevel) {
        this.medicationID = medicationID;
        this.name = name;
        this.stockLevel = stockLevel;
        this.lowStockAlertLevel = lowStockAlertLevel;
    }
    
    /**
     * Retrieves the unique identifier for this medication.
     * This method provides access to the medication's ID which is used
     * for tracking and referencing specific medications in prescriptions
     * and inventory management.
     *
     * @return The medication ID
     */
    public String getMedicationID() {
        return medicationID;
    }

    /**
     * Retrieves the name of this medication.
     * This method provides access to the standard name used to identify
     * the medication in prescriptions, inventory listings, and medical
     * records.
     *
     * @return The name of the medication
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the current stock level of this medication.
     * This method provides access to the current quantity available
     * in inventory for prescription fulfillment and inventory
     * management purposes.
     *
     * @return The stock level
     */
    public int getStockLevel() {
        return stockLevel;
    }

    /**
     * Retrieves the low stock alert level for this medication.
     * This method provides access to the threshold quantity that
     * triggers low stock alerts for pharmacists to request
     * inventory replenishment.
     *
     * @return The low stock alert level
     */
    public int getLowStockAlertLevel() {
        return lowStockAlertLevel;
    }

    /**
     * Updates the name of this medication.
     * This method allows administrators to modify the standard name
     * used to identify the medication if corrections or updates
     * are needed.
     *
     * @param name The new name of the medication
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Updates the current stock level of this medication.
     * This method allows pharmacists and administrators to directly
     * modify the quantity available in inventory when performing
     * manual stock adjustments.
     *
     * @param stockLevel The new stock level
     */
    public void setStockLevel(int stockLevel) {
        this.stockLevel = stockLevel;
    }

    /**
     * Updates the low stock alert level for this medication.
     * This method allows administrators to modify the threshold
     * quantity that triggers inventory replenishment alerts
     * based on usage patterns.
     *
     * @param lowStockAlertLevel The new low stock alert level
     */
    public void setLowStockAlertLevel(int lowStockAlertLevel) {
        this.lowStockAlertLevel = lowStockAlertLevel;
    }

    /**
     * Increases the stock level by the specified amount.
     * This method is used when receiving new inventory shipments
     * or making positive inventory adjustments. Updates the
     * current stock level by adding the specified amount.
     *
     * @param amount The amount to increase the stock level by
     */
    public void increaseStockLevel(int amount) {
        this.stockLevel += amount;
    }

    /**
     * Decreases the stock level by the specified amount.
     * This method is used when dispensing medications for prescriptions
     * or making negative inventory adjustments. Updates the current
     * stock level by subtracting the specified amount.
     *
     * @param amount The amount to decrease the stock level by
     */
    public void decreaseStockLevel(int amount) {
        this.stockLevel -= amount;
    }

    /**
     * Generates a string representation of the medication.
     * This method creates a formatted string containing all relevant
     * medication information for display or logging purposes.
     *
     * @return A string representation of the complete medication record
     */
    @Override
    public String toString() {
        return "Medication{" + medicationID + "," + name + "," + stockLevel + "," + lowStockAlertLevel + "}";
    }
}