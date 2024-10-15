package controller;

import java.time.LocalDate;
import java.time.LocalTime;
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
 * Controller class for managing doctor-related operations in the Hospital
 * Management System.
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
     * [READ] Retrieves a doctor by their ID.
     *
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
                .filter(slot -> slot.getDoctorID().equals(doctorID)
                && slot.getStatus() == Slot.SlotStatus.AVAILABLE)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves available slots for current doctor.
     *
     * @return List of available Slot objects for the specified doctor
     */
    public List<Slot> getAvailableSlotsForDoctor() {
        Doctor currentDoctor = (Doctor) authController.getCurrentUser();
        return slotDataManager.getSlots().stream()
                .filter(slot -> slot.getDoctorID().equals(currentDoctor.getUserID())
                && slot.getStatus() == Slot.SlotStatus.AVAILABLE)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves non-available slots for current doctor.
     *
     * @return List of available Slot objects for the specified doctor
     */
    public List<Slot> getNonAvailableSlotsForDoctor() {
        Doctor currentDoctor = (Doctor) authController.getCurrentUser();
        return slotDataManager.getSlots().stream()
                .filter(slot -> slot.getDoctorID().equals(currentDoctor.getUserID())
                && slot.getStatus() != Slot.SlotStatus.AVAILABLE)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves booked slots for current doctor.
     *
     * @return List of available Slot objects for the specified doctor
     */
    public List<Slot> getBookedSlotsForDoctor() {
        Doctor currentDoctor = (Doctor) authController.getCurrentUser();
        return slotDataManager.getSlots().stream()
                .filter(slot -> slot.getDoctorID().equals(currentDoctor.getUserID())
                && slot.getStatus() == Slot.SlotStatus.BOOKED)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves pending slots for current doctor.
     *
     * @return List of available Slot objects for the specified doctor
     */
    public List<Slot> getPendingSlotsForDoctor() {
        Doctor currentDoctor = (Doctor) authController.getCurrentUser();
        return slotDataManager.getSlots().stream()
                .filter(slot -> slot.getDoctorID().equals(currentDoctor.getUserID())
                && slot.getStatus() == Slot.SlotStatus.PENDING)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all slots for current doctor.
     *
     * @return List of available Slot objects for the specified doctor
     */
    public List<Slot> getAllSlotsForDoctor() {
        Doctor currentDoctor = (Doctor) authController.getCurrentUser();
        return slotDataManager.getSlots().stream()
                .filter(slot -> slot.getDoctorID().equals(currentDoctor.getUserID()))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all Medication Information.
     *
     * @return List of all medication objects
     */
    public List<Medication> getAllMedication() {
        return medicationDataManager.getMedications();
    }

    /**
     * Retrieves a list of patients under the doctor's care.
     *
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
     * Retrieves a list of patients under the current doctor's care.
     *
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
     * Retrieves the medical history for a specific patient.
     *
     * @param patientID The ID of the patient
     * @return List of History objects for the patient
     */
    public List<History> getPatientMedicalHistory(String patientID) {
        return historyDataManager.getHistoriesByPatientID(patientID);
    }

    /**
     * Updates a specific medical history record.
     *
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
     *
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
     *
     * @return A new unique history ID
     */
    private String generateHistoryID() {
        // Implementation depends on your ID generation strategy
        // This is a simple example and might need to be more robust in a real system
        int count = 1;
        String uniqueID = "H" + String.format("%02d", historyDataManager.getHistories().size() + 1);
        while (historyDataManager.getHistoryByID(uniqueID) == null) {
            uniqueID = "H" + String.format("%02d", count);
            count++;
        }
        return uniqueID;
    }

    /**
     * Adds available slot for a doctor.
     *
     * @param date The date of available slot
     * @param startTime The start time of available slot
     * @param endTime The end time of available slot
     * @return true if the addition was successful, false otherwise
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
     * Adds new prescription.
     *
     * @param date The date of available slot
     * @param startTime The start time of available slot
     * @param endTime The end time of available slot
     * @return true if the addition was successful, false otherwise
     */
    //String prescriptionID, String appointmentID, String medicationID, int quantity, PrescriptionStatus status, String notes
    public Boolean addPrescription(String appointmentID, String medicationID, int quantity, String notes) {
        try {
            String prescriptionID = generatePrescriptionID();
            Prescription newPrescription = new Prescription(prescriptionID, appointmentID, medicationID, quantity, Prescription.PrescriptionStatus.PENDING, notes);
            prescriptionDataManager.addPrescription(newPrescription);
            return true;
        } catch (IllegalArgumentException e) {
            System.err.println("Error adding adding prescription: " + e.getMessage());
            return false;
        }
    }

    /**
     * Generates a new unique history ID.
     *
     * @return A new unique history ID
     */
    private String generatePrescriptionID() {
        // Implementation depends on your ID generation strategy
        // This is a simple example and might need to be more robust in a real system
        int count = 1;
        String uniqueID = "P" + String.format("%02d", prescriptionDataManager.getPrescriptions().size() + 1);
        while (historyDataManager.getHistoryByID(uniqueID) == null) {
            uniqueID = "P" + String.format("%02d", count);
            count++;
        }
        return uniqueID;
    }

    /**
     * Updates a specific medical history record.
     *
     * @param historyID The ID of the history record to update
     * @param newDiagnosis The new diagnosis (or null if unchanged)
     * @param newTreatment The new treatment (or null if unchanged)
     * @return true if the update was successful, false otherwise
     */
    public boolean updateSlot(Slot slot, LocalDate date, LocalTime newStartTime, LocalTime newEndTime) {
        try {
            slot.setDate(date);
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
     * Updates a specific medical history record.
     *
     * @param historyID The ID of the history record to update
     * @param newDiagnosis The new diagnosis (or null if unchanged)
     * @param newTreatment The new treatment (or null if unchanged)
     * @return true if the update was successful, false otherwise
     */
    public boolean removeSlot(String slotID) {
        try {
            slotDataManager.removeSlot(slotID);
            return true;
        } catch (IllegalArgumentException e) {
            System.err.println("Error updating avaialble slot: " + e.getMessage());
            return false;
        }
    }

    /**
     * Updates a slot and appointment record.
     *
     * @param accept decision to accept or reject
     * @param appointment Appointment object to update
     * @param slot Slot object to update
     * @return true if the update was successful, false otherwise
     */
    public boolean manageAppointment(Boolean accept, Appointment appointment, Slot slot) {
        try {
            if (accept) {
                slot.setStatus(Slot.SlotStatus.BOOKED);
                appointment.setStatus(Appointment.AppointmentStatus.CONFIRMED);

            } else {
                slot.setStatus(Slot.SlotStatus.REMOVED);
                appointment.setStatus(Appointment.AppointmentStatus.CANCELED);
            }
            slotDataManager.updateSlot(slot);
            appointmentDataManager.updateAppointment(appointment);
            return true;
        } catch (IllegalArgumentException e) {
            System.err.println("Error managing appointment: " + e.getMessage());
            return false;
        }
    }
//public Outcome(String outcomeID, String appointmentID, String serviceProvided, String prescriptionID, String consultationNotes)

    /**
     * Adds a new outcome record.
     *
     * @param appointment Appointment object to complete
     * @param slot Slot object to complete
     * @param serviceProvided
     * @param prescriptionID
     * @param consultationNotes
     * @return true if the update was successful, false otherwise
     */
    public boolean completeAppointment(Appointment appointment, Slot slot, String serviceProvided, String prescriptionID, String consultationNotes) {
        try {
            String outcomeID = generateOutcomeID();
            String appointmentID = appointment.getAppointmentID();

            Outcome newOutcome = new Outcome(outcomeID, appointmentID, serviceProvided, prescriptionID, consultationNotes);
            outcomeDataManager.addOutcome(newOutcome);
            slot.setStatus(Slot.SlotStatus.COMPLETED);
            appointment.setStatus(Appointment.AppointmentStatus.COMPLETED);
            slotDataManager.updateSlot(slot);
            appointmentDataManager.updateAppointment(appointment);
            return true;

        } catch (IllegalArgumentException e) {
            System.err.println("Error completing appointment: " + e.getMessage());
            return false;
        }
    }

    /**
     * Generates a new unique history ID.
     *
     * @return A new unique history ID
     */
    private String generateOutcomeID() {
        // Implementation depends on your ID generation strategy
        // This is a simple example and might need to be more robust in a real system
        int count = 1;
        String uniqueID = "R" + String.format("%02d", outcomeDataManager.getAllOutcomes().size() + 1);
        while (historyDataManager.getHistoryByID(uniqueID) == null) {
            uniqueID = "R" + String.format("%02d", count);
            count++;
        }
        return uniqueID;
    }

    /**
     * Generates a new unique slot ID.
     *
     * @return A new unique history ID
     */
    private String generateSlotID() {
        // Implementation depends on your ID ration strategy
        // This is a simple example and might need to be more robust in a real system
        int count = 1;
        String uniqueID = "S" + String.format("%02d", slotDataManager.getSlots().size() + 1);
        while (historyDataManager.getHistoryByID(uniqueID) == null) {
            uniqueID = "S" + String.format("%02d", count);
            count++;
        }
        return uniqueID;
    }

    /**
     * Checks if the given patient ID is valid and belongs to a patient under
     * the doctor's care.
     *
     * @param patientID The ID to validate
     * @return true if the ID is valid, false otherwise
     */
    public boolean isValidPatientID(String patientID) {
        return getPatientsUnderCare().stream()
                .anyMatch(patient -> patient.getUserID().equals(patientID));
    }

    /**
     * Retrieves a patient by their ID.
     *
     * @param patientID The ID of the patient to retrieve
     * @return The Patient object with the specified ID, or null if not found
     */
    public Patient getPatientByID(String patientID) {
        User user = userDataManager.getUserByID(patientID);
        return (user instanceof Patient) ? (Patient) user : null;
    }

    /**
     * Checks if the given history ID is valid.
     *
     * @param historyID The ID to validate
     * @return true if the ID is valid, false otherwise
     */
    public boolean isValidHistoryID(String historyID) {
        return historyDataManager.getHistoryByID(historyID) != null;
    }

    /**
     * Checks if the given slot ID is valid.
     *
     * @param slotID The ID to validate
     * @return true if the ID is valid, false otherwise
     */
    public boolean isValidSlotID(String slotID) {
        return slotDataManager.getSlotByID(slotID) != null;
    }

    /**
     * Checks if the given slot ID is valid & available.
     *
     * @param slotID The ID to validate
     * @return true if the ID is valid, by the current doctor and, available,
     * false otherwise
     */
    public boolean isValidAvailableSlotID(String slotID) {
        Doctor currentDoctor = (Doctor) authController.getCurrentUser();
        String doctorID = currentDoctor.getUserID();
        return slotDataManager.getStatusByID(slotID, doctorID, Slot.SlotStatus.AVAILABLE) != null;
    }

    /**
     * Checks if the given slot ID is valid & pending.
     *
     * @param slotID The ID to validate
     * @return true if the ID is valid, by the current doctor and, pending,
     * false otherwise
     */
    public boolean isValidPendingSlotID(String slotID) {
        Doctor currentDoctor = (Doctor) authController.getCurrentUser();
        String doctorID = currentDoctor.getUserID();
        return slotDataManager.getStatusByID(slotID, doctorID, Slot.SlotStatus.PENDING) != null;
    }

    /**
     * Checks if the given slot ID is valid & booked.
     *
     * @param slotID The ID to validate
     * @return true if the ID is valid, by the current doctor and, pending,
     * false otherwise
     */
    public boolean isValidBookedSlotID(String slotID) {
        Doctor currentDoctor = (Doctor) authController.getCurrentUser();
        String doctorID = currentDoctor.getUserID();
        return slotDataManager.getStatusByID(slotID, doctorID, Slot.SlotStatus.BOOKED) != null;
    }

    public boolean isValidMedicationID(String medicationID) {
        return medicationDataManager.getMedicationByID(medicationID) != null;
    }

    /**
     * Retrieves a history record by its ID.
     *
     * @param historyID The ID of the history record to retrieve
     * @return The History object with the specified ID, or null if not found
     */
    public History getHistoryByID(String historyID) {
        return historyDataManager.getHistoryByID(historyID);
    }

    /**
     * Retrieves a slot record by its ID.
     *
     * @param slotID The ID of the slot record to retrieve
     * @return The Slot object with the specified ID, or null if not found
     */
    public Slot getSlotByID(String slotID) {
        return slotDataManager.getSlotByID(slotID);
    }

    /**
     * Retrieves a slot record by its ID.
     *
     * @param slotID The ID of the slot record to retrieve
     * @return The Slot object with the specified ID, or null if not found
     */
    public Appointment getAppointmentBySlotID(String slotID) {
        return appointmentDataManager.getAppointmentBySlotID(slotID);
    }

    /**
     * Retrieves a slot record by its ID.
     *
     * @param slotID The ID of the slot record to retrieve
     * @return The Slot object with the specified ID, or null if not found
     */
    public Patient getPatientByAppointment(Appointment a) {
        String patientID = a.getPatientID();
        User user = userDataManager.getUserByID(patientID);
        return (user instanceof Patient) ? (Patient) user : null;
    }
}
