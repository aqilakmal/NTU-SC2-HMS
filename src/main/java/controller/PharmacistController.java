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
import controller.data.RequestDataManager;
import entity.Appointment;
import entity.Medication;
import entity.Outcome;
import entity.Prescription;
import entity.Slot;
import entity.User;
import entity.Patient;
import entity.Doctor;
import entity.Request;

/**
 * Controller class for managing pharmacist-related operations in the Hospital Management System.
 * This class handles all pharmacist functionalities including prescription management,
 * medication inventory tracking, and replenishment request processing. It serves as an
 * intermediary between the pharmacist interface and the data layer.
 *
 * The controller provides methods for:
 * - Viewing and updating prescription statuses
 * - Monitoring medication inventory
 * - Submitting replenishment requests
 * - Accessing appointment and outcome records
 * - Retrieving user information
 *
 * @author Group 7
 * @version 1.0
 */
public class PharmacistController {

    private MedicationDataManager medicationDataManager;
    private PrescriptionDataManager prescriptionDataManager;
    private OutcomeDataManager outcomeDataManager;
    private AppointmentDataManager appointmentDataManager;
    private SlotDataManager slotDataManager;
    private UserDataManager userDataManager;
    private RequestDataManager requestDataManager;
    private AuthenticationController authController;

    /**
     * Constructor for PharmacistController.
     * Initializes all required data managers and controllers for pharmacist operations.
     * This constructor enables the controller to access and manage all necessary data
     * for pharmacist functionalities.
     * 
     * @param medicationDataManager The MedicationDataManager instance for managing medication inventory
     * @param prescriptionDataManager The PrescriptionDataManager instance for handling prescriptions
     * @param outcomeDataManager The OutcomeDataManager instance for accessing appointment outcomes
     * @param appointmentDataManager The AppointmentDataManager instance for appointment information
     * @param slotDataManager The SlotDataManager instance for managing time slots
     * @param userDataManager The UserDataManager instance for accessing user information
     * @param requestDataManager The RequestDataManager instance for handling replenishment requests
     * @param authController The AuthController instance for authentication services
     */
    public PharmacistController(MedicationDataManager medicationDataManager, 
                               PrescriptionDataManager prescriptionDataManager,
                               OutcomeDataManager outcomeDataManager,
                               AppointmentDataManager appointmentDataManager,
                               SlotDataManager slotDataManager,
                               UserDataManager userDataManager,
                               RequestDataManager requestDataManager,
                               AuthenticationController authController) {
        this.medicationDataManager = medicationDataManager;
        this.prescriptionDataManager = prescriptionDataManager;
        this.outcomeDataManager = outcomeDataManager;
        this.appointmentDataManager = appointmentDataManager;
        this.slotDataManager = slotDataManager;
        this.userDataManager = userDataManager;
        this.requestDataManager = requestDataManager;
        this.authController = authController;
    }

    /**
     * Gets all medications in the inventory.
     * Retrieves a complete list of all medications currently available in the hospital inventory.
     * This method provides pharmacists with access to the full medication catalog.
     * 
     * @return List of all medications in the inventory
     */
    public List<Medication> getAllMedications() {
        return medicationDataManager.getMedications();
    }

    /**
     * Gets a medication by its ID.
     * Retrieves detailed information about a specific medication using its unique identifier.
     * This method is useful for checking individual medication details and stock levels.
     * 
     * @param medicationID The ID of the medication to retrieve
     * @return The Medication object if found, null otherwise
     */
    public Medication getMedicationByID(String medicationID) {
        return medicationDataManager.getMedicationByID(medicationID);
    }

    /**
     * Gets the medications associated with a prescription.
     * Retrieves all medications listed in a specific prescription by its ID.
     * This method helps pharmacists verify medications before dispensing.
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
     * Changes the status of a prescription (e.g., from pending to dispensed) and updates
     * medication stock levels accordingly. This method includes validation checks to prevent
     * duplicate dispensing and maintains inventory accuracy.
     * 
     * @param prescriptionID The ID of the prescription to update
     * @param newStatus The new status to set for the prescription
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
     * Retrieves comprehensive information about a specific appointment, including
     * any associated medical outcomes if the appointment is completed.
     * 
     * @param appointmentID The ID of the appointment to retrieve details for
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
     * Gets all pending replenishment requests.
     * Retrieves a list of all medication replenishment requests that are currently
     * pending approval. This helps pharmacists track outstanding inventory requests.
     * 
     * @return List of pending replenishment requests
     */
    public List<Request> getPendingRequests() {
        return requestDataManager.getRequests().stream()
            .filter(r -> r.getStatus() == Request.RequestStatus.PENDING)
            .collect(Collectors.toList());
    }

    /**
     * Submits a replenishment request for a medication.
     * Creates and submits a new request to replenish medication stock when inventory
     * is running low. The request includes details about the medication and quantity needed.
     * 
     * @param medicationID The ID of the medication to replenish
     * @param quantity The quantity to request
     * @return true if request was submitted successfully, false otherwise
     */
    public boolean submitReplenishmentRequest(String medicationID, int quantity) {
        try {
            // Generate a new request ID (you may want to implement a proper ID generation strategy)
            String requestID = "R" + String.format("%02d", requestDataManager.getRequests().size() + 1);
            
            // Create new request
            Request request = new Request(
                requestID,
                medicationID,
                quantity,
                Request.RequestStatus.PENDING,
                authController.getCurrentUser().getUserID(),  // Assuming you have access to the current user
                ""  // No approver yet
            );
            
            // Save the request
            requestDataManager.addRequest(request);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Gets all completed appointments.
     * Retrieves a list of all appointments that have been marked as completed.
     * This allows pharmacists to access prescription information from completed consultations.
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
     * Retrieves a list of all prescriptions that are currently pending dispensation.
     * This helps pharmacists manage and prioritize prescription fulfillment.
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
     * Retrieves detailed information about a specific prescription using its unique identifier.
     * This method is essential for accessing individual prescription details.
     * 
     * @param prescriptionID The ID of the prescription to retrieve
     * @return The Prescription object if found, null otherwise
     */
    public Prescription getPrescriptionByID(String prescriptionID) {
        return prescriptionDataManager.getPrescriptionByID(prescriptionID);
    }

    /**
     * Gets a slot by its ID.
     * Retrieves information about a specific time slot using its unique identifier.
     * This helps in verifying appointment timing details.
     * 
     * @param slotID The ID of the slot to retrieve
     * @return The Slot object if found, null otherwise
     */
    public Slot getSlotByID(String slotID) {
        return slotDataManager.getSlotByID(slotID);
    }

    /**
     * Gets a patient by their ID.
     * Retrieves patient information using their unique identifier.
     * This method helps pharmacists verify patient details when dispensing medications.
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
     * Retrieves doctor information using their unique identifier.
     * This helps in verifying prescription sources and doctor details.
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
     * Retrieves detailed information about a specific appointment outcome.
     * This allows pharmacists to review diagnosis and prescription details.
     * 
     * @param outcomeID The ID of the outcome to retrieve
     * @return The Outcome object if found, null otherwise
     */
    public Outcome getOutcomeByID(String outcomeID) {
        return outcomeDataManager.getOutcomeByID(outcomeID);
    }
}
