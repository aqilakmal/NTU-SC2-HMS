package controller;

import entity.*;
import controller.data.*;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.util.Map;

/**
 * Controller class for managing doctor-related operations in the Hospital Management System.
 */
public class DoctorController {

    private UserDataManager userDataManager;
    private SlotDataManager slotDataManager;
    private AppointmentDataManager appointmentDataManager;
    private HistoryDataManager historyDataManager;
    private AuthenticationController authController;

    /**
     * Constructor for DoctorController.
     * 
     * @param userDataManager The UserDataManager instance
     * @param slotDataManager The SlotDataManager instance
     * @param appointmentDataManager The AppointmentDataManager instance
     * @param historyDataManager The HistoryDataManager instance
     * @param authController The AuthenticationController instance
     */
    public DoctorController(UserDataManager userDataManager, SlotDataManager slotDataManager, 
                            AppointmentDataManager appointmentDataManager, HistoryDataManager historyDataManager,
                            AuthenticationController authController) {
        this.userDataManager = userDataManager;
        this.slotDataManager = slotDataManager;
        this.appointmentDataManager = appointmentDataManager;
        this.historyDataManager = historyDataManager;
        this.authController = authController;
    }

    /**
     * [READ] Retrieves a doctor by their ID.
     * @param userID The ID of the doctor to retrieve.
     * @return The Doctor object with the specified ID, or null if not found.
     */
    public Doctor getDoctorByID(String userID) {
        return (Doctor) userDataManager.getUserByID(userID);
    }

    /**
     * Retrieves all doctors in the system.
     * 
     * @return List of all Doctor objects
     */
    public List<Doctor> getAllDoctors() {
        return userDataManager.getUsers().stream()
                .filter(user -> user instanceof Doctor)
                .map(user -> (Doctor) user)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves available slots for a specific doctor.
     * 
     * @param doctorID The ID of the doctor
     * @return List of available Slot objects for the specified doctor
     */
    public List<Slot> getAvailableSlotsForDoctor(String doctorID) {
        return slotDataManager.getSlots().stream()
                .filter(slot -> slot.getDoctorID().equals(doctorID) &&
                                slot.getStatus() == Slot.SlotStatus.AVAILABLE)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a list of patients under the doctor's care.
     * @param doctorID The ID of the doctor
     * @return List of Patient objects under the doctor's care
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
     * Retrieves the medical history for a specific patient.
     * @param patientID The ID of the patient
     * @return List of History objects for the patient
     */
    public List<History> getPatientMedicalHistory(String patientID) {
        return historyDataManager.getHistoriesByPatientID(patientID);
    }

    /**
     * Updates a specific medical history record.
     * @param historyID The ID of the history record to update
     * @param newDiagnosis The new diagnosis (or null if unchanged)
     * @param newTreatment The new treatment (or null if unchanged)
     * @return true if the update was successful, false otherwise
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
     * Adds a new medical history record for a patient.
     * @param patientID The ID of the patient
     * @param diagnosis The diagnosis
     * @param treatment The treatment
     * @return true if the addition was successful, false otherwise
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
     * Generates a new unique history ID.
     * @return A new unique history ID
     */
    private String generateHistoryID() {
        // Implementation depends on your ID generation strategy
        // This is a simple example and might need to be more robust in a real system
        return "H" + String.format("%02d", historyDataManager.getHistories().size() + 1);
    }

    /**
     * Retrieves a list of patients under the current doctor's care.
     * @return List of Patient objects under the doctor's care
     */
    public List<Patient> getPatientsUnderCare() {
        Doctor currentDoctor = (Doctor) authController.getCurrentUser();
        List<String> patientIDs = appointmentDataManager.getFilteredAppointments(Map.of(
            "doctorID", currentDoctor.getUserID(),
            "status", Appointment.AppointmentStatus.CONFIRMED.toString()
        )).stream().map(Appointment::getPatientID).distinct().collect(Collectors.toList());

        return patientIDs.stream()
                .map(userDataManager::getUserByID)
                .filter(user -> user instanceof Patient)
                .map(user -> (Patient) user)
                .collect(Collectors.toList());
    }

    /**
     * Checks if the given patient ID is valid and belongs to a patient under the doctor's care.
     * @param patientID The ID to validate
     * @return true if the ID is valid, false otherwise
     */
    public boolean isValidPatientID(String patientID) {
        return getPatientsUnderCare().stream()
                .anyMatch(patient -> patient.getUserID().equals(patientID));
    }

    /**
     * Retrieves a patient by their ID.
     * @param patientID The ID of the patient to retrieve
     * @return The Patient object with the specified ID, or null if not found
     */
    public Patient getPatientByID(String patientID) {
        User user = userDataManager.getUserByID(patientID);
        return (user instanceof Patient) ? (Patient) user : null;
    }

    /**
     * Checks if the given history ID is valid.
     * @param historyID The ID to validate
     * @return true if the ID is valid, false otherwise
     */
    public boolean isValidHistoryID(String historyID) {
        return historyDataManager.getHistoryByID(historyID) != null;
    }

    /**
     * Retrieves a history record by its ID.
     * @param historyID The ID of the history record to retrieve
     * @return The History object with the specified ID, or null if not found
     */
    public History getHistoryByID(String historyID) {
        return historyDataManager.getHistoryByID(historyID);
    }
}