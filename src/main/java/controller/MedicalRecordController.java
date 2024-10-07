package controller;

import entity.MedicalRecord;

/**
 * Manages access and updates to medical records in the Hospital Management System.
 */
public class MedicalRecordController {
    
    /**
     * Retrieves the medical record for a specific patient.
     * @param patientID The unique identifier of the patient
     * @return MedicalRecord object for the patient
     * @throws MedicalRecordException if the record cannot be found or accessed
     */
    public MedicalRecord getMedicalRecord(String patientID) throws MedicalRecordException {
        // TODO: Implement logic to retrieve medical record
        return null;
    }
    
    /**
     * Updates the medical record for a specific patient.
     * @param patientID The unique identifier of the patient
     * @param updates The new medical data to be added to the record
     * @throws MedicalRecordException if the update is invalid or unauthorized
     */
    public void updateMedicalRecord(String patientID, MedicalRecord updates) throws MedicalRecordException {
        // TODO: Implement logic to update medical record
    }

    /**
     * MedicalRecordException extends Exception to handle exceptions related to medical records.
     */
    public class MedicalRecordException extends Exception {
      
        /**
         * Constructor for MedicalRecordException with a message.
         * @param message The message to display when the exception is thrown.
         */
        public MedicalRecordException(String message) {
            super(message);
        }

        /**
         * Constructor for MedicalRecordException with a message and a cause.
         * @param message The message to display when the exception is thrown.
         * @param cause The cause of the exception.
         */
        public MedicalRecordException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}