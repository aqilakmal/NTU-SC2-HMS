package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import controller.data.AppointmentDataManager;
import controller.data.MedicationDataManager;
import controller.data.OutcomeDataManager;
import controller.data.PrescriptionDataManager;
import controller.data.UserDataManager;
import controller.data.SlotDataManager;
import entity.Appointment;
import entity.Medication;
import entity.Outcome;
import entity.Prescription;
import entity.Slot;
import entity.User;
import entity.Patient;
import entity.Doctor;

/**
 * Controller class for managing pharmacist-related operations in the Hospital Management System.
 */
public class PharmacistController {

    private MedicationDataManager medicationDataManager;
    private PrescriptionDataManager prescriptionDataManager;
    private OutcomeDataManager outcomeDataManager;
    private AppointmentDataManager appointmentDataManager;
    private SlotDataManager slotDataManager;
    private UserDataManager userDataManager;

    /**
     * Constructor for PharmacistController.
     * 
     * @param medicationDataManager The MedicationDataManager instance
     * @param prescriptionDataManager The PrescriptionDataManager instance
     * @param outcomeDataManager The OutcomeDataManager instance
     * @param appointmentDataManager The AppointmentDataManager instance
     * @param slotDataManager The SlotDataManager instance
     * @param userDataManager The UserDataManager instance
     */
    public PharmacistController(MedicationDataManager medicationDataManager, 
                               PrescriptionDataManager prescriptionDataManager,
                               OutcomeDataManager outcomeDataManager,
                               AppointmentDataManager appointmentDataManager,
                               SlotDataManager slotDataManager,
                               UserDataManager userDataManager) {
        this.medicationDataManager = medicationDataManager;
        this.prescriptionDataManager = prescriptionDataManager;
        this.outcomeDataManager = outcomeDataManager;
        this.appointmentDataManager = appointmentDataManager;
        this.slotDataManager = slotDataManager;
        this.userDataManager = userDataManager;
    }

    /**
     * Gets all medications in the inventory.
     * 
     * @return List of all medications
     */
    public List<Medication> getAllMedications() {
        return medicationDataManager.getMedications();
    }

    /**
     * Gets a medication by its ID.
     * 
     * @param medicationID The ID of the medication to retrieve
     * @return The Medication object if found, null otherwise
     */
    public Medication getMedicationByID(String medicationID) {
        return medicationDataManager.getMedicationByID(medicationID);
    }

    /**
     * Gets the medications associated with a prescription.
     * 
     * @param prescriptionID The ID of the prescription
     * @return List of medications in the prescription
     */
    public List<Medication> getMedicationsForPrescription(String prescriptionID) {
        List<Medication> medications = new ArrayList<>();
        Prescription prescription = prescriptionDataManager.getPrescriptionByID(prescriptionID);
        
        if (prescription != null) {
            Medication medication = medicationDataManager.getMedicationByID(prescription.getMedicationID());
            if (medication != null) {
                medications.add(medication);
            }
        }
        
        return medications;
    }

    /**
     * Updates the status of a prescription.
     * 
     * @param prescriptionID The ID of the prescription to update
     * @param newStatus The new status to set
     * @return true if update was successful, false otherwise
     * @throws IOException If there's an error saving the updated status
     * @throws IllegalStateException If the prescription is already dispensed
     */
    public boolean updatePrescriptionStatus(String prescriptionID, Prescription.PrescriptionStatus newStatus) 
            throws IOException, IllegalStateException {
        Prescription prescription = prescriptionDataManager.getPrescriptionByID(prescriptionID);
        
        if (prescription == null) {
            return false;
        }

        // Check if prescription is already dispensed
        if (prescription.getStatus() == Prescription.PrescriptionStatus.DISPENSED) {
            throw new IllegalStateException("This prescription has already been dispensed.");
        }

        // Update the prescription status
        prescription.setStatus(newStatus);
        
        // If dispensing, update medication stock level
        if (newStatus == Prescription.PrescriptionStatus.DISPENSED) {
            Medication medication = medicationDataManager.getMedicationByID(prescription.getMedicationID());
            if (medication != null) {
                medication.decreaseStockLevel(prescription.getQuantity());
                medicationDataManager.updateMedication(medication);
            }
        }

        // Save the updated prescription
        prescriptionDataManager.updatePrescription(prescription);
        return true;
    }

    /**
     * Gets appointment details including the associated outcome.
     * 
     * @param appointmentID The ID of the appointment
     * @return Map containing appointment and outcome details
     */
    public Map<String, Object> getAppointmentDetails(String appointmentID) {
        Map<String, Object> details = new HashMap<>();
        
        Appointment appointment = appointmentDataManager.getAppointmentByID(appointmentID);
        if (appointment != null) {
            details.put("appointment", appointment);
            
            // Get the outcome if appointment is completed
            if (appointment.getStatus() == Appointment.AppointmentStatus.COMPLETED) {
                Outcome outcome = outcomeDataManager.getOutcomeByAppointmentID(appointmentID);
                details.put("outcome", outcome);
            }
        }
        
        return details;
    }

    /**
     * Submits a replenishment request for a medication.
     * 
     * @param medicationID The ID of the medication to replenish
     * @param quantity The quantity to request
     * @return true if request was submitted successfully, false otherwise
     */
    public boolean submitReplenishmentRequest(String medicationID, int quantity) {
        // Implementation for replenishment request
        // This would typically involve creating a replenishment request record
        // and notifying administrators
        return true; // Placeholder return
    }

    /**
     * Gets all completed appointments.
     * 
     * @return List of completed appointments
     */
    public List<Appointment> getCompletedAppointments() {
        return appointmentDataManager.getAllAppointments().stream()
            .filter(a -> a.getStatus() == Appointment.AppointmentStatus.COMPLETED)
            .collect(Collectors.toList());
    }

    /**
     * Gets all pending prescriptions.
     * 
     * @return List of pending prescriptions
     */
    public List<Prescription> getPendingPrescriptions() {
        return prescriptionDataManager.getPrescriptions().stream()
            .filter(p -> p.getStatus() == Prescription.PrescriptionStatus.PENDING)
            .collect(Collectors.toList());
    }

    /**
     * Gets a prescription by its ID.
     * 
     * @param prescriptionID The ID of the prescription to retrieve
     * @return The Prescription object if found, null otherwise
     */
    public Prescription getPrescriptionByID(String prescriptionID) {
        return prescriptionDataManager.getPrescriptionByID(prescriptionID);
    }

    /**
     * Gets a slot by its ID.
     * 
     * @param slotID The ID of the slot to retrieve
     * @return The Slot object if found, null otherwise
     */
    public Slot getSlotByID(String slotID) {
        return slotDataManager.getSlotByID(slotID);
    }

    /**
     * Gets a patient by their ID.
     * 
     * @param patientID The ID of the patient to retrieve
     * @return The Patient object if found, null otherwise
     */
    public Patient getPatientByID(String patientID) {
        User user = userDataManager.getUserByID(patientID);
        return (user instanceof Patient) ? (Patient) user : null;
    }

    /**
     * Gets a doctor by their ID.
     * 
     * @param doctorID The ID of the doctor to retrieve
     * @return The Doctor object if found, null otherwise
     */
    public Doctor getDoctorByID(String doctorID) {
        User user = userDataManager.getUserByID(doctorID);
        return (user instanceof Doctor) ? (Doctor) user : null;
    }

    /**
     * Gets an outcome by its ID.
     * 
     * @param outcomeID The ID of the outcome to retrieve
     * @return The Outcome object if found, null otherwise
     */
    public Outcome getOutcomeByID(String outcomeID) {
        return outcomeDataManager.getOutcomeByID(outcomeID);
    }
}
