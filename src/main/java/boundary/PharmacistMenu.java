package boundary;

import controller.MenuController;
import controller.PrescriptionController;
import controller.InventoryController;
import entity.Pharmacist;

/**
 * Interface for pharmacist operations in the Hospital Management System.
 */
public class PharmacistMenu {
    
    private MenuController menuController;
    private PrescriptionController prescriptionController;
    private InventoryController inventoryController;
    private Pharmacist currentPharmacist;
    
    /**
     * Displays the menu options available to the pharmacist.
     */
    public void displayMenu() {
        // TODO: Implement logic to display pharmacist menu
    }
    
    /**
     * Handles the pharmacist's menu selection.
     * @param selection The menu option selected by the pharmacist
     */
    public void handleUserInput(int selection) {
        // TODO: Implement logic to handle pharmacist's menu selection
    }
}