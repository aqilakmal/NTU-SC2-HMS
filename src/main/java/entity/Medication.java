package entity;

/**
 * Represents a medication in the inventory of the Hospital Management System.
 */
public class Medication {
    
    /**
     * Unique identifier for the medication.
     */
    private String medicationID;
    
    /**
     * Name of the medication.
     */
    private String name;
    
    /**
     * Current stock level of the medication.
     */
    private int stockLevel;
    
    /**
     * The stock level at which a low stock alert should be triggered.
     */
    private int lowStockAlertLevel;
    
    /**
     * Constructor for Medication.
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
     * Get the unique identifier for the medication.
     * @return The medication ID
     */
    public String getMedicationID() {
        return medicationID;
    }

    /**
     * Get the name of the medication.
     * @return The name of the medication
     */
    public String getName() {
        return name;
    }

    /**
     * Get the current stock level of the medication.
     * @return The stock level
     */
    public int getStockLevel() {
        return stockLevel;
    }

    /**
     * Get the stock level at which a low stock alert should be triggered.
     * @return The low stock alert level
     */
    public int getLowStockAlertLevel() {
        return lowStockAlertLevel;
    }

    /**
     * Set the current stock level of the medication.
     * @param stockLevel The new stock level
     */
    public void setStockLevel(int stockLevel) {
        this.stockLevel = stockLevel;
    }

    /**
     * Set the stock level at which a low stock alert should be triggered.
     * @param lowStockAlertLevel The new low stock alert level
     */
    public void setLowStockAlertLevel(int lowStockAlertLevel) {
        this.lowStockAlertLevel = lowStockAlertLevel;
    }

    /**
     * Increases the stock level of the medication by a specified amount.
     * @param amount The amount to increase the stock level by
     */
    public void increaseStockLevel(int amount) {
        this.stockLevel += amount;
    }

    /**
     * Decreases the stock level of the medication by a specified amount.
     * @param amount The amount to decrease the stock level by
     */
    public void decreaseStockLevel(int amount) {
        this.stockLevel -= amount;
    }

}