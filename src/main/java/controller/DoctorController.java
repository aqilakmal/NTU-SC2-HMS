package controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import controller.data.AppointmentDataManager;
import controller.data.HistoryDataManager;
import controller.data.MedicationDataManager;
import controller.data.OutcomeDataManager;
import controller.data.PrescriptionDataManager;
import controller.data.SlotDataManager;
import controller.data.UserDataManager;
import entity.Appointment;
import entity.Doctor;
import entity.History;
import entity.Medication;
import entity.Outcome;
import entity.Patient;
import entity.Prescription;
import entity.Slot;
import entity.User;

/**
 * Controller class for managing doctor-related operations in the Hospital Management System.
 * This class handles all doctor functionalities including managing appointments, patient records,
 * prescriptions, medical histories, and doctor schedules. It serves as an intermediary between
 * the doctor boundary class and various data managers.
 * 
 * The controller supports key doctor operations such as:
 * - Managing doctor schedules and appointment slots
 * - Viewing and managing patient medical records
 * - Writing prescriptions and recording appointment outcomes
 * - Accessing patient histories and medication information
 * 
 * @author Group 7
 * @version 1.0
 */
public class DoctorController {

    private UserDataManager userDataManager;
    private SlotDataManager slotDataManager;
    private AppointmentDataManager appointmentDataManager;
    private OutcomeDataManager outcomeDataManager;
    private PrescriptionDataManager prescriptionDataManager;
    private MedicationDataManager medicationDataManager;
    private HistoryDataManager historyDataManager;
    private AuthenticationController authController;

    /**
     * Constructor for DoctorController that initializes all required data managers.
     * This constructor sets up the controller with all necessary dependencies to manage
     * doctor operations including user data, appointments, medical histories, and prescriptions.
     *
     * @param userDataManager The data manager for handling user-related operations
     * @param slotDataManager The data manager for handling time slot operations
     * @param appointmentDataManager The data manager for handling appointment operations
     * @param historyDataManager The data manager for handling medical history operations
     * @param outcomeDataManager The data manager for handling appointment outcomes
     * @param medicationDataManager The data manager for handling medication data
     * @param prescriptionDataManager The data manager for handling prescription operations
     * @param authController The controller for handling authentication operations
     */
    public DoctorController(UserDataManager userDataManager, SlotDataManager slotDataManager,
            AppointmentDataManager appointmentDataManager, HistoryDataManager historyDataManager,
            OutcomeDataManager outcomeDataManager,
            MedicationDataManager medicationDataManager,
            PrescriptionDataManager prescriptionDataManager,
            AuthenticationController authController) {
        this.userDataManager = userDataManager;
        this.slotDataManager = slotDataManager;
        this.appointmentDataManager = appointmentDataManager;
        this.historyDataManager = historyDataManager;
        this.outcomeDataManager = outcomeDataManager;
        this.prescriptionDataManager = prescriptionDataManager;
        this.medicationDataManager = medicationDataManager;

        this.authController = authController;
    }

    /**
     * Retrieves a doctor by their ID from the system.
     * This method searches the user database for a doctor with the specified ID.
     * The returned user is cast to a Doctor object if found.
     *
     * @param userID The unique identifier of the doctor to retrieve
     * @return The Doctor object with the specified ID, or null if not found
     */
    public Doctor getDoctorByID(String userID) {
        return (Doctor) userDataManager.getUserByID(userID);
    }

    /**
     * Retrieves all doctors registered in the system.
     * This method filters all users in the system to return only those who are doctors.
     * The returned list contains all active doctor accounts in the system.
     *
     * @return List of all Doctor objects in the system
     */
    public List<Doctor> getAllDoctors() {
        return userDataManager.getUsers().stream()
                .filter(user -> user instanceof Doctor)
                .map(user -> (Doctor) user)
                .collect(Collectors.toList());
    }
    
    /**
     * Retrieves a list of patients under the current doctor's care.
     * This method filters appointments to find patients who have either confirmed or completed
     * appointments with the currently logged-in doctor.
     * Duplicate patient entries are removed from the final list.
     *
     * @return List of Patient objects under the current doctor's care
     */
    public List<Patient> getPatientsUnderCare() {
        Doctor currentDoctor = (Doctor) authController.getCurrentUser();

        // Get appointments that match either CONFIRMED or COMPLETED statuses
        List<String> patientIDs = appointmentDataManager.getFilteredAppointments(Map.of(
                "doctorID", currentDoctor.getUserID()
        )).stream()
                .filter(appointment
                        -> Arrays.asList(Appointment.AppointmentStatus.CONFIRMED, Appointment.AppointmentStatus.COMPLETED)
                        .contains(appointment.getStatus()))
                .map(Appointment::getPatientID)
                .distinct()
                .collect(Collectors.toList());

        // Convert patientIDs to Patient objects and filter only valid Patient users
        return patientIDs.stream()
                .map(userDataManager::getUserByID)
                .filter(user -> user instanceof Patient)
                .map(user -> (Patient) user)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a list of patients under a specific doctor's care.
     * This method filters appointments to find patients who have confirmed
     * appointments with the specified doctor.
     * Duplicate patient entries are removed from the final list.
     *
     * @param doctorID The unique identifier of the doctor
     * @return List of Patient objects under the specified doctor's care
     */
    public List<Patient> getPatientsUnderCare(String doctorID) {
        List<String> patientIDs = appointmentDataManager.getFilteredAppointments(Map.of(
                "doctorid", doctorID,
                "status", Appointment.AppointmentStatus.CONFIRMED.toString()
        )).stream().map(Appointment::getPatientID).distinct().collect(Collectors.toList());

        return patientIDs.stream()
                .map(userDataManager::getUserByID)
                .filter(user -> user instanceof Patient)
                .map(user -> (Patient) user)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a list of medical history records for a specific patient.
     * This method accesses the history database to retrieve all medical records
     * associated with the given patient ID.
     * The records are returned in chronological order.
     *
     * @param patientID The unique identifier of the patient
     * @return List of History objects containing the patient's medical records
     */
    public List<History> getPatientMedicalHistory(String patientID) {
        return historyDataManager.getHistoriesByPatientID(patientID);
    }

    /**
     * Retrieves all slots for current doctor.
     * This method filters all slots in the system to return only those
     * associated with the currently logged-in doctor.
     * Returns all slots regardless of their status.
     *
     * @return List of Slot objects for the current doctor
     */
    public List<Slot> getAllSlotsForDoctor() {
        Doctor currentDoctor = (Doctor) authController.getCurrentUser();
        return slotDataManager.getSlots().stream()
                .filter(slot -> slot.getDoctorID().equals(currentDoctor.getUserID()))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves slots with the "AVAILABLE" status for the current doctor.
     * This method filters all slots to return only available ones for the
     * currently logged-in doctor.
     * The slots are sorted by date and start time.
     *
     * @return List of Available Slot objects for the current doctor, ordered by date and start time
     */
    public List<Slot> getAvailableSlotsForDoctor() {
        Doctor currentDoctor = (Doctor) authController.getCurrentUser();
        return slotDataManager.getSlots().stream()
                .filter(slot -> slot.getDoctorID().equals(currentDoctor.getUserID())
                && slot.getStatus() == Slot.SlotStatus.AVAILABLE)
                .sorted(Comparator.comparing(Slot::getDate).thenComparing(Slot::getStartTime))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves slots with the "AVAILABLE" status for a specific doctor.
     * This method filters all slots to return only available ones for the
     * specified doctor.
     * The slots are sorted by date and start time.
     *
     * @param doctorID The unique identifier of the doctor
     * @return List of available Slot objects for the specified doctor, ordered by date and start time
     */
    public List<Slot> getAvailableSlotsForDoctor(String doctorID) {
        return slotDataManager.getSlots().stream()
                .filter(slot -> slot.getDoctorID().equals(doctorID)
                && slot.getStatus() == Slot.SlotStatus.AVAILABLE)
                .sorted(Comparator.comparing(Slot::getDate).thenComparing(Slot::getStartTime))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves pending slots for current doctor.
     * This method filters all slots to return only those with pending status
     * for the currently logged-in doctor.
     * Pending slots are those awaiting confirmation or action.
     *
     * @return List of Pending Slot objects for the current doctor
     */
    public List<Slot> getPendingSlotsForDoctor() {
        Doctor currentDoctor = (Doctor) authController.getCurrentUser();
        return slotDataManager.getSlots().stream()
                .filter(slot -> slot.getDoctorID().equals(currentDoctor.getUserID())
                && slot.getStatus() == Slot.SlotStatus.PENDING)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves booked slots for current doctor.
     * This method filters all slots to return only those with booked status
     * for the currently logged-in doctor.
     * Booked slots are those that have been confirmed for appointments.
     *
     * @return List of Booked Slot objects for the current doctor
     */
    public List<Slot> getBookedSlotsForDoctor() {
        Doctor currentDoctor = (Doctor) authController.getCurrentUser();
        return slotDataManager.getSlots().stream()
                .filter(slot -> slot.getDoctorID().equals(currentDoctor.getUserID())
                && slot.getStatus() == Slot.SlotStatus.BOOKED)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a list of all medications available in the system.
     * This method provides access to the complete medication database.
     * The list includes all active medications that can be prescribed.
     *
     * @return List of all Medication objects in the system
     */
    public List<Medication> getAllMedication() {
        return medicationDataManager.getMedications();
    }

    /**
     * Retrieves a list of completed appointments for the current doctor.
     * This method filters appointments to return only those that have been
     * marked as completed for the currently logged-in doctor.
     * Completed appointments are those where the consultation has finished.
     *
     * @return List of completed Appointment objects for the current doctor
     */
    public List<Appointment> getCompletedAppointmentsForDoctor() {
        Doctor currentDoctor = (Doctor) authController.getCurrentUser();

        // Get appointments that match COMPLETED status for the current doctor
        return appointmentDataManager.getFilteredAppointments(Map.of(
                "doctorID", currentDoctor.getUserID()
        )).stream()
                .filter(appointment -> appointment.getStatus() == Appointment.AppointmentStatus.COMPLETED)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a list of outcomes for completed appointments of the current doctor.
     * This method first gets all completed appointments for the current doctor,
     * then retrieves the associated outcomes.
     * Only valid outcomes with non-null IDs are included in the result.
     *
     * @return List of Outcome objects linked to completed appointments for the current doctor
     */
    public List<Outcome> getOutcomesForCompletedAppointmentsForDoctor() {
        // Get the list of completed appointments for the current doctor
        List<Appointment> completedAppointments = getCompletedAppointmentsForDoctor();

        // Use the outcome IDs to fetch the corresponding Outcome objects
        return completedAppointments.stream()
                .map(Appointment::getOutcomeID) // Get outcome ID from each completed appointment
                .filter(outcomeID -> outcomeID != null && !outcomeID.isEmpty()) // Filter out null or empty outcome IDs
                .map(outcomeDataManager::getOutcomeByID) // Get Outcome object by outcome ID
                .filter(outcome -> outcome != null) // Filter out any null outcomes
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the prescriptions for a given appointment.
     * This method filters the prescription database to find all prescriptions
     * associated with the specified appointment ID.
     * Returns an empty list if no prescriptions are found.
     *
     * @param appointmentID The unique identifier of the appointment
     * @return List of Prescription objects associated with the appointment
     */
    public List<Prescription> getPrescriptionsByAppointmentID(String appointmentID) {
        return prescriptionDataManager.getPrescriptions().stream()
                .filter(prescription -> prescription.getAppointmentID().equals(appointmentID))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a patient object by their ID.
     * This method searches the user database for a patient with the specified ID.
     * Returns null if the user is not found or is not a patient.
     *
     * @param patientID The unique identifier of the patient to retrieve
     * @return The Patient object if found and is a patient, null otherwise
     */
    public Patient getPatientByID(String patientID) {
        User user = userDataManager.getUserByID(patientID);
        return (user instanceof Patient) ? (Patient) user : null;
    }

    /**
     * Retrieves a history object by its ID.
     * This method accesses the history database to retrieve a specific
     * medical history record.
     * Returns null if the history record is not found.
     *
     * @param historyID The unique identifier of the history record to retrieve
     * @return The History object if found, null otherwise
     */
    public History getHistoryByID(String historyID) {
        return historyDataManager.getHistoryByID(historyID);
    }

    /**
     * Retrieves a slot object by its ID.
     * This method searches the slot database for a specific time slot.
     * Returns null if the slot is not found.
     *
     * @param slotID The unique identifier of the slot to retrieve
     * @return The Slot object if found, null otherwise
     */
    public Slot getSlotByID(String slotID) {
        return slotDataManager.getSlotByID(slotID);
    }

    /**
     * Retrieves an appointment by its associated slot ID.
     * This method searches the appointment database for an appointment
     * linked to the specified slot.
     * Returns null if no appointment is found for the slot.
     *
     * @param slotID The unique identifier of the slot
     * @return The Appointment object if found, null otherwise
     */
    public Appointment getAppointmentBySlotID(String slotID) {
        return appointmentDataManager.getAppointmentBySlotID(slotID);
    }

    /**
     * Retrieves the patient associated with a given appointment.
     * This method extracts the patient ID from the appointment and
     * retrieves the corresponding patient record.
     * Performs validation to ensure the user is a patient.
     *
     * @param appointment The appointment object to retrieve the patient from
     * @return The Patient object if found and valid, null otherwise
     */
    public Patient getPatientByAppointment(Appointment appointment) {
        if (appointment == null) {
            return null; // Ensure the appointment is valid
        }

        String patientID = appointment.getPatientID();
        User user = userDataManager.getUserByID(patientID);

        if (user != null && user instanceof Patient) {
            return (Patient) user;
        } else {
            return null; // Return null if user is not a Patient or if user does not found
        }
    }

    /**
     * Retrieves an appointment object by its ID.
     * This method searches the appointment database for a specific appointment.
     * Returns null if the appointment is not found.
     *
     * @param appointmentID The unique identifier of the appointment to retrieve
     * @return The Appointment object if found, null otherwise
     */
    public Appointment getAppointmentByID(String appointmentID) {
        return appointmentDataManager.getAppointmentByID(appointmentID);
    }

    /**
     * Retrieves the outcome ID associated with an appointment.
     * This method extracts the outcome ID from the given appointment object.
     * Returns null if the appointment is invalid or has no outcome.
     *
     * @param appointment The appointment object to get the outcome ID from
     * @return The outcome ID if found, null otherwise
     */
    public String getOutcomeIDFromAppointment(Appointment appointment) {
        if (appointment == null) {
            return null;
        }
        return appointment.getOutcomeID();
    }

    /**
     * Retrieves an outcome record by its ID.
     * This method searches the outcome database for a specific outcome record.
     * Returns null if the outcome is not found.
     *
     * @param outcomeID The unique identifier of the outcome to retrieve
     * @return The Outcome object if found, null otherwise
     */
    public Outcome getOutcomeByID(String outcomeID) {
        return outcomeDataManager.getOutcomeByID(outcomeID);
    }

    /**
     * Retrieves a patient object from an appointment.
     * This method extracts the patient ID from the appointment and
     * retrieves the corresponding patient record.
     * Returns null if the patient is not found.
     *
     * @param appointment The appointment object used to retrieve patient ID
     * @return The Patient object if found, null otherwise
     */
    public Patient getPatientFromAppointment(Appointment appointment) {
        String patientID = appointment.getPatientID();
        Patient patient = getPatientByID(patientID);
        return patient;
    }

    /**
     * Retrieves a prescription by its ID.
     * This method searches the prescription database for a specific prescription.
     * Includes validation for null or empty prescription IDs.
     *
     * @param prescriptionID The unique identifier of the prescription to retrieve
     * @return The Prescription object if found, null otherwise
     */
    public Prescription getPrescriptionByID(String prescriptionID) {
        if (prescriptionID == null || prescriptionID.isEmpty()) {
            System.out.println("Invalid prescription ID provided.");
            return null;
        }

        // Retrieve the prescription from prescriptionDataManager
        Prescription prescription = prescriptionDataManager.getPrescriptionByID(prescriptionID);

        if (prescription == null) {
            System.out.println("Prescription not found with ID: " + prescriptionID);
        }

        return prescription;
    }

    /**
     * Generates a new unique history ID.
     * This method creates a sequential ID for new history records.
     * Ensures uniqueness by checking against existing IDs.
     *
     * @return A new unique history ID
     */
    private String generateHistoryID() {
        int count = 1;
        String uniqueID = "H" + String.format("%02d", historyDataManager.getHistories().size() + 1);
        while (historyDataManager.getHistoryByID(uniqueID) != null) {
            uniqueID = "H" + String.format("%02d", count);
            count++;
        }
        return uniqueID;
    }

    /**
     * Generates a new unique slot ID.
     * This method creates a sequential ID for new time slots.
     * Ensures uniqueness by checking against existing IDs.
     *
     * @return A new unique slot ID
     */
    private String generateSlotID() {
        int count = 1;
        String uniqueID = "S" + String.format("%02d", slotDataManager.getSlots().size() + 1);
        while (slotDataManager.getSlotByID(uniqueID) != null) {
            uniqueID = "S" + String.format("%02d", count);
            count++;
        }
        return uniqueID;
    }

    /**
     * Generates a new unique Outcome ID.
     * This method creates a sequential ID for new appointment outcomes.
     * Ensures uniqueness by checking against existing IDs.
     *
     * @return A new unique outcome ID
     */
    private String generateOutcomeID() {
        int count = 1;
        String uniqueID = "O" + String.format("%02d", outcomeDataManager.getAllOutcomes().size() + 1);
        while (outcomeDataManager.getOutcomeByID(uniqueID) != null) {
            uniqueID = "O" + String.format("%02d", count);
            count++;
        }
        return uniqueID;
    }

    /**
     * Generates a new unique prescription ID.
     * This method creates a sequential ID for new prescriptions.
     * Ensures uniqueness by checking against existing IDs.
     *
     * @return A new unique prescription ID
     */
    private String generatePrescriptionID() {
        int count = 1;
        String uniqueID = "P" + String.format("%02d", prescriptionDataManager.getPrescriptions().size() + 1);
        while (prescriptionDataManager.getPrescriptionByID(uniqueID) != null) {
            uniqueID = "P" + String.format("%02d", count);
            count++;
        }
        return uniqueID;
    }

    /**
     * Validates if a patient ID belongs to a patient under the doctor's care.
     * This method checks if the patient exists and is currently under the
     * care of the logged-in doctor.
     * Used for access control and data validation.
     *
     * @param patientID The patient ID to validate
     * @return true if patient is under current doctor's care, false otherwise
     */
    public boolean isValidPatientID(String patientID) {
        return getPatientsUnderCare().stream()
                .anyMatch(patient -> patient.getUserID().equals(patientID));
    }

    /**
     * Validates if a history record belongs to a patient under the doctor's care.
     * This method checks if the history record exists and is associated with
     * the specified patient who is under the current doctor's care.
     * Used for access control and data validation to ensure proper access rights.
     *
     * @param patientID The ID of the patient to validate
     * @param historyID The ID of the history record to validate
     * @return true if history belongs to the patient under current doctor's care, false otherwise
     */
    public boolean isPatientsHistory(String patientID, String historyID) {
        return getPatientMedicalHistory(patientID).stream()
                .anyMatch(history -> history.getHistoryID().equals(historyID));
    }

    /**
     * Checks if given Slot ID is valid for the current doctor.
     * Validates that the slot exists and belongs to the currently logged in doctor.
     * Used for access control and data validation.
     *
     * @param slotID The Slot ID to validate
     * @return true if Slot ID is valid and belongs to the current doctor, false otherwise
     */
    public boolean isValidDoctorSlotID(String slotID) {
        Doctor currentDoctor = (Doctor) authController.getCurrentUser();
        String doctorID = currentDoctor.getUserID();
        return slotDataManager.getStatusByID(slotID, doctorID) != null;
    }

    /**
     * Checks if given Slot ID is valid and has an available status.
     * Validates that the slot exists, belongs to the current doctor, and has not been booked.
     * Used for scheduling new appointments.
     *
     * @param slotID The Slot ID to validate
     * @return true if Slot ID is valid, belongs to current doctor and is available, false otherwise
     */
    public boolean isValidAvailableSlotID(String slotID) {
        Doctor currentDoctor = (Doctor) authController.getCurrentUser();
        String doctorID = currentDoctor.getUserID();
        return slotDataManager.getStatusByID(slotID, doctorID, Slot.SlotStatus.AVAILABLE) != null;
    }

    /**
     * Checks if given Slot ID is valid and has a pending status.
     * Validates that the slot exists, belongs to the current doctor, and is pending confirmation.
     * Used for managing appointment requests.
     *
     * @param slotID The Slot ID to validate
     * @return true if Slot ID is valid, belongs to current doctor and is pending, false otherwise
     */
    public boolean isValidPendingSlotID(String slotID) {
        Doctor currentDoctor = (Doctor) authController.getCurrentUser();
        String doctorID = currentDoctor.getUserID();
        return slotDataManager.getStatusByID(slotID, doctorID, Slot.SlotStatus.PENDING) != null;
    }

    /**
     * Checks if given Slot ID is valid and has a booked status.
     * Validates that the slot exists, belongs to the current doctor, and has been booked.
     * Used for managing confirmed appointments.
     *
     * @param slotID The Slot ID to validate
     * @return true if Slot ID is valid, belongs to current doctor and is booked, false otherwise
     */
    public boolean isValidBookedSlotID(String slotID) {
        Doctor currentDoctor = (Doctor) authController.getCurrentUser();
        String doctorID = currentDoctor.getUserID();
        return slotDataManager.getStatusByID(slotID, doctorID, Slot.SlotStatus.BOOKED) != null;
    }

    /**
     * Checks if given slot ID is valid and has a completed status.
     * Validates that the slot exists, belongs to the current doctor, and has been completed.
     * Used for managing appointment history and records.
     *
     * @param slotID The ID to validate
     * @return true if Slot ID is valid, belongs to current doctor and is completed, false otherwise
     */
    public boolean isValidCompletedSlotID(String slotID) {
        Doctor currentDoctor = (Doctor) authController.getCurrentUser();
        String doctorID = currentDoctor.getUserID();
        return slotDataManager.getStatusByID(slotID, doctorID, Slot.SlotStatus.COMPLETED) != null;
    }

    /**
     * Checks if given appointment ID is valid and completed for the current doctor.
     * Validates that the appointment exists, belongs to the current doctor, and has been completed.
     * Used for managing appointment records and outcomes.
     *
     * @param appointmentID The ID to validate
     * @return true if Appointment ID is valid, belongs to current doctor and is completed, false otherwise
     */
    public boolean isValidCompletedAppointmentID(String appointmentID) {
        Doctor currentDoctor = (Doctor) authController.getCurrentUser();
        String doctorID = currentDoctor.getUserID();

        // Retrieve the appointment by ID
        Appointment appointment = appointmentDataManager.getAppointmentByID(appointmentID);

        // Check if appointment is not null, belongs to the current doctor, and has COMPLETED status
        return appointment != null
                && appointment.getDoctorID().equals(doctorID)
                && appointment.getStatus() == Appointment.AppointmentStatus.COMPLETED;
    }

    /**
     * Checks if given Medication ID exists in the system.
     * Validates that the medication exists in the database.
     * Used for prescription management and validation.
     *
     * @param medicationID The Medication ID to validate
     * @return true if Medication ID exists in the system, false otherwise
     */
    public boolean isValidMedicationID(String medicationID) {
        return medicationDataManager.getMedicationByID(medicationID) != null;
    }

    /**
     * Checks if given prescription ID is valid and associated with current doctor.
     * Validates that the prescription exists and belongs to an appointment under the current doctor.
     * Used for prescription management and access control.
     *
     * @param prescriptionID The Prescription ID to validate
     * @return true if prescription exists and belongs to current doctor's appointment, false otherwise
     */
    public boolean isValidPrescriptionID(String prescriptionID) {
        // Retrieve the current doctor
        Doctor currentDoctor = (Doctor) authController.getCurrentUser();
        String doctorID = currentDoctor.getUserID();

        // Retrieve the prescription using prescriptionID
        Prescription prescription = prescriptionDataManager.getPrescriptionByID(prescriptionID);
        if (prescription == null) {
            System.out.println("Prescription not found");
            return false; // Prescription not found
        }

        // Retrieve the appointment associated with the prescription
        String appointmentID = prescription.getAppointmentID();
        Appointment appointment = appointmentDataManager.getAppointmentByID(appointmentID);

        // Check if appointment is found and is associated with the current doctor
        if (appointment == null || !appointment.getDoctorID().equals(doctorID)) {
            System.out.println("Appointment not associated with current doctor");
            return false; // Appointment not found or does not belong to the current doctor
        }

        // The prescription is valid and the appointment belongs to the current doctor
        return true;
    }

    /**
     * Adds a new medical history record for a patient.
     * Creates a new history record with the provided diagnosis and treatment information.
     * Generates a unique history ID and sets the current date.
     *
     * @param patientID The ID of the patient
     * @param diagnosis The diagnosis for new history record
     * @param treatment The treatment for new history record
     * @return true if addition was successful, false otherwise
     */
    public boolean addMedicalHistory(String patientID, String diagnosis, String treatment) {
        try {
            String historyID = generateHistoryID();
            History newHistory = new History(historyID, patientID, LocalDate.now(), diagnosis, treatment);
            historyDataManager.addHistory(newHistory);
            return true;
        } catch (IllegalArgumentException e) {
            System.err.println("Error adding medical history: " + e.getMessage());
            return false;
        }
    }

    /**
     * Updates a specific medical history record.
     * Modifies the diagnosis and/or treatment information for an existing history record.
     * Only updates fields that contain new non-empty values.
     *
     * @param historyID The ID of the history record to update
     * @param newDiagnosis The new diagnosis
     * @param newTreatment The new treatment
     * @return true if update was successful, false otherwise
     */
    public boolean updateMedicalHistory(String historyID, String newDiagnosis, String newTreatment) {
        try {
            History history = historyDataManager.getHistoryByID(historyID);
            if (history == null) {
                return false;
            }

            if (newDiagnosis != null && !newDiagnosis.trim().isEmpty()) {
                history.setDiagnosis(newDiagnosis.trim());
            }
            if (newTreatment != null && !newTreatment.trim().isEmpty()) {
                history.setTreatment(newTreatment.trim());
            }

            historyDataManager.updateHistory(history);
            return true;
        } catch (IllegalArgumentException e) {
            System.err.println("Error updating medical history: " + e.getMessage());
            return false;
        }
    }

    /**
     * Removes a specific medical history record.
     * Deletes the history record with the given ID from the system.
     * Used for managing patient medical records.
     *
     * @param historyID The ID of the history to remove
     * @return true if removal was successful, false otherwise
     */
    public boolean removeHistory(String historyID) {
        try {
            historyDataManager.removeHistory(historyID);
            return true;
        } catch (IllegalArgumentException e) {
            System.err.println("Error removing history: " + e.getMessage());
            return false;
        }
    }

    /**
     * Updates a specific slot's schedule details.
     * Modifies the date and time information for an existing slot.
     * Updates all specified fields in the slot object.
     *
     * @param slot The Slot object to update
     * @param newDate The new date
     * @param newStartTime The new start time
     * @param newEndTime The new end time
     * @return true if update was successful, false otherwise
     */
    public boolean updateSlot(Slot slot, LocalDate newDate, LocalTime newStartTime, LocalTime newEndTime) {
        try {
            slot.setDate(newDate);
            slot.setStartTime(newStartTime);
            slot.setEndTime(newEndTime);

            slotDataManager.updateSlot(slot);
            return true;
        } catch (IllegalArgumentException e) {
            System.err.println("Error updating avaialble slot: " + e.getMessage());
            return false;
        }
    }

    /**
     * Adds an available slot for the current doctor.
     * Creates a new slot with the specified schedule and marks it as available.
     * Generates a unique slot ID for the new slot.
     *
     * @param date Date of available slot
     * @param startTime Start time of available slot
     * @param endTime End time of available slot
     * @return true if addition was successful, false otherwise
     */
    public boolean addAvailableSlot(LocalDate date, LocalTime startTime, LocalTime endTime) {
        try {
            Doctor currentDoctor = (Doctor) authController.getCurrentUser();
            String doctorID = currentDoctor.getUserID();
            String slotID = generateSlotID();
            Slot newSlot = new Slot(slotID, doctorID, date, startTime, endTime, Slot.SlotStatus.AVAILABLE);
            slotDataManager.addSlot(newSlot);
            return true;
        } catch (IllegalArgumentException e) {
            System.err.println("Error adding available slot: " + e.getMessage());
            return false;
        }
    }

    /**
     * Removes a specific slot from the system.
     * Deletes the slot with the given ID from the schedule.
     * Used for managing doctor availability.
     *
     * @param slotID The ID of the slot to remove
     * @return true if removal was successful, false otherwise
     */
    public boolean removeSlot(String slotID) {
        try {
            slotDataManager.removeSlot(slotID);
            return true;
        } catch (IllegalArgumentException e) {
            System.err.println("Error removing avaialble slot: " + e.getMessage());
            return false;
        }
    }

    /**
     * Creates a new prescription for a medication.
     * Generates a new prescription with the specified details and sets initial status as pending.
     * Associates the prescription with an appointment and medication.
     *
     * @param appointmentID The appointment ID this prescription belongs to
     * @param medicationID The medication being prescribed
     * @param quantity Quantity of medication to prescribe
     * @param notes Instructions for taking the medication
     * @return true if prescription was created successfully, false otherwise
     */
    public Boolean createPrescription(String appointmentID, String medicationID, int quantity, String notes) {
        try {
            String prescriptionID = generatePrescriptionID();
            Prescription newPrescription = new Prescription(
                prescriptionID, 
                appointmentID, 
                medicationID, 
                quantity, 
                Prescription.PrescriptionStatus.PENDING, 
                notes
            );
            prescriptionDataManager.addPrescription(newPrescription);
            return true;
        } catch (IllegalArgumentException e) {
            System.err.println("Error creating prescription: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves a medication by its ID.
     * Fetches detailed medication information from the database.
     * Used for prescription management and medication information display.
     *
     * @param medicationID The ID of the medication to retrieve
     * @return The Medication object if found, null otherwise
     */
    public Medication getMedicationByID(String medicationID) {
        return medicationDataManager.getMedicationByID(medicationID);
    }

    /**
     * Adds a new outcome record for a completed appointment.
     * Creates an outcome record with the provided details and updates appointment status.
     * Also updates the associated slot status to completed.
     *
     * @param appointment Appointment object to complete
     * @param slot Slot object to complete
     * @param serviceProvided The service provided to create new outcome
     * @param prescriptionID The string of medications, separated by semicolons
     * @param consultationNotes The consultation notes to create new outcome
     * @return true if completion of appointment was successful, false otherwise
     */
    public boolean completeAppointment(Appointment appointment, Slot slot, String serviceProvided, String prescriptionID, String consultationNotes) {
        try {
            String outcomeID = generateOutcomeID();
            String appointmentID = appointment.getAppointmentID();

            Outcome newOutcome = new Outcome(outcomeID, appointmentID, serviceProvided, prescriptionID, consultationNotes);
            outcomeDataManager.addOutcome(newOutcome);
            slot.setStatus(Slot.SlotStatus.COMPLETED);
            appointment.setStatus(Appointment.AppointmentStatus.COMPLETED);
            appointment.setOutcomeID(outcomeID);

            slotDataManager.updateSlot(slot);
            appointmentDataManager.updateAppointment(appointment);
            return true;

        } catch (IllegalArgumentException e) {
            System.err.println("Error completing appointment: " + e.getMessage());
            return false;
        }
    }

    /**
     * Counts the number of medications in a prescription string.
     * Parses the semicolon-separated string of prescription IDs.
     * Used for prescription management and validation.
     *
     * @param prescriptionString The string containing prescription IDs separated by semicolons
     * @return the number of medications within the prescription string
     */
    public int countMedication(String prescriptionString) {
        if (prescriptionString == null || prescriptionString.trim().isEmpty()) {
            return 0;
        }
        String[] medications = prescriptionString.split(";");
        return medications.length;
    }

    /**
     * Updates an existing prescription with new details.
     * Modifies medication, quantity, and notes for a prescription.
     * Used for managing and updating prescription information.
     *
     * @param prescription The Prescription object to update
     * @param newMedicationID The new medication ID
     * @param newQuantity The new quantity
     * @param newPrescriptionNotes The new prescription notes
     * @return true if update was successful, false otherwise
     */
    public Boolean updatePrescription(Prescription prescription, String newMedicationID, int newQuantity, String newPrescriptionNotes) {
        try {
            prescription.setMedicationID(newMedicationID);
            prescription.setQuantity(newQuantity);
            prescription.setNotes(newPrescriptionNotes);
            prescriptionDataManager.updatePrescription(prescription);
            return true;
        } catch (IllegalArgumentException e) {
            System.err.println("Error updating prescription: " + e.getMessage());
            return false;
        }
    }

    /**
     * Removes a prescription from the system.
     * Deletes the prescription with the given ID.
     * Used for managing prescriptions and medical records.
     *
     * @param prescriptionID The ID of the prescription to remove
     * @return true if removal was successful, false otherwise
     */
    public boolean removePrescription(String prescriptionID) {
        try {
            prescriptionDataManager.removePrescription(prescriptionID);
            return true;
        } catch (IllegalArgumentException e) {
            System.err.println("Error removing prescription: " + e.getMessage());
            return false;
        }
    }

    /**
     * Updates an existing outcome record with new information.
     * Modifies service, prescription, and consultation details for an outcome.
     * Used for managing appointment outcomes and medical records.
     *
     * @param outcome The Outcome object to update
     * @param serviceProvided The new service provided
     * @param prescriptionID The new medications provided
     * @param consultationNotes The new consultation notes
     * @return true if update was successful, false otherwise
     */
    public boolean updateOutcome(Outcome outcome, String serviceProvided, String prescriptionID, String consultationNotes) {
        try {
            // Update fields only ify are new or updated
            outcome.setServiceProvided(serviceProvided);
            outcome.setPrescriptionID(prescriptionID);
            outcome.setConsultationNotes(consultationNotes);
            outcomeDataManager.updateOutcome(outcome); // Persist changes in the data manager
            return true;

        } catch (IllegalArgumentException e) {
            System.err.println("Error updating outcomes: " + e.getMessage());
            return false;
        }
    }

    /**
     * Sorts a list of slots by priority and time.
     * Orders slots by status priority (BOOKED, PENDING, AVAILABLE, COMPLETED, REMOVED),
     * then by date and start time within each status group.
     *
     * @param slots The list of slots to sort
     * @return A sorted list of slots
     */
    public static List<Slot> sortSlots(List<Slot> slots) {
        slots.sort(
                Comparator.comparing((Slot slot) -> {
                    // Set custom priority for each status to achieve desired grouping
                    switch (slot.getStatus()) {
                        case BOOKED:
                            return 1;     // BOOKED has the highest priority for sorting
                        case PENDING:
                            return 2;    // PENDING follows BOOKED
                        case AVAILABLE:
                            return 3;  // AVAILABLE is after BOOKED and PENDING
                        case COMPLETED:
                            return 4;  // COMPLETED follows AVAILABLE
                        case REMOVED:
                            return 5;    // REMOVED has the lowest priority
                        default:
                            return Integer.MAX_VALUE;
                    }
                })
                        // Then sort by date (oldest first) for each status group
                        .thenComparing(Slot::getDate)
                        // Then sort by start time (earliest first) for each date group
                        .thenComparing(Slot::getStartTime)
        );
        return slots; // Return the sorted list
    }

    /**
     * Retrieves pending appointments for the current doctor.
     * Filters appointments to show only those with REQUESTED status.
     * Used for managing incoming appointment requests.
     *
     * @return List of appointments with REQUESTED status for the current doctor
     */
    public List<Appointment> getPendingAppointmentsForDoctor() {
        Doctor currentDoctor = (Doctor) authController.getCurrentUser();
        return appointmentDataManager.getFilteredAppointments(Map.of(
                "doctorID", currentDoctor.getUserID()
        )).stream()
        .filter(appointment -> appointment.getStatus() == Appointment.AppointmentStatus.REQUESTED)
        .collect(Collectors.toList());
    }

    /**
     * Retrieves confirmed appointments for the current doctor.
     * Filters appointments to show only those with CONFIRMED status.
     * Used for managing upcoming appointments.
     *
     * @return List of appointments with CONFIRMED status for the current doctor
     */
    public List<Appointment> getConfirmedAppointmentsForDoctor() {
        Doctor currentDoctor = (Doctor) authController.getCurrentUser();
        return appointmentDataManager.getFilteredAppointments(Map.of(
                "doctorID", currentDoctor.getUserID()
        )).stream()
        .filter(appointment -> appointment.getStatus() == Appointment.AppointmentStatus.CONFIRMED)
        .collect(Collectors.toList());
    }

    /**
     * Updates an appointment and its associated slot status.
     * Handles both acceptance and rejection of appointment requests.
     * Updates both appointment and slot statuses accordingly.
     *
     * @param accept If true, confirms appointment and books slot; if false, cancels both
     * @param appointment Appointment object to update
     * @param slot Slot object to update
     * @return true if update was successful, false otherwise
     */
    public boolean manageAppointment(Boolean accept, Appointment appointment, Slot slot) {
        try {
            if (accept) {
                appointment.setStatus(Appointment.AppointmentStatus.CONFIRMED);
                slot.setStatus(Slot.SlotStatus.BOOKED);
            } else {
                appointment.setStatus(Appointment.AppointmentStatus.CANCELLED);
                slot.setStatus(Slot.SlotStatus.REMOVED);
            }
            
            appointmentDataManager.updateAppointment(appointment);
            slotDataManager.updateSlot(slot);
            return true;
        } catch (IllegalArgumentException e) {
            System.err.println("Error managing appointment: " + e.getMessage());
            return false;
        }
    }

}
