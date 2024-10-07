package controller;

import entity.Prescription;
import java.util.List;

/**
 * Handles prescription status updates in the Hospital Management System.
 */
public class PrescriptionController {
    
    /**
     * Retrieves all prescriptions in the system.
     * @return List of Prescription objects
     */
    public List<Prescription> getPrescriptions() {
        // TODO: Implement logic to retrieve all prescriptions
        return null;
    }
    
    /**
     * Updates the status of a specific prescription.
     * @param prescriptionID The unique identifier of the prescription
     * @param status The new status to set for the prescription
     * @throws PrescriptionException if the status update is invalid
     */
    public void updatePrescriptionStatus(String prescriptionID, Prescription.PrescriptionStatus status) throws PrescriptionException {
        // TODO: Implement logic to update prescription status
    }

    /**
     * PrescriptionException extends Exception to handle exceptions related to prescriptions.
     */
    public class PrescriptionException extends Exception {
      
        /**
         * Constructor for PrescriptionException with a message.
         * @param message The message to display when the exception is thrown.
         */
        public PrescriptionException(String message) {
            super(message);
        }

        /**
         * Constructor for PrescriptionException with a message and a cause.
         * @param message The message to display when the exception is thrown.
         * @param cause The cause of the exception.
         */
        public PrescriptionException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}