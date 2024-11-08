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

    //------------------------------------------Get-List Methods-------------------------------------------------
    /**
     * Retrieves a list of patients under the current doctor's care.
     *
     * @return List of Patient objects under the doctor's care
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
     * Retrieves a list of medical history for a specific patient.
     *
     * @param patientID The ID of the patient
     * @return List of History objects for the patient
     */
    public List<History> getPatientMedicalHistory(String patientID) {
        return historyDataManager.getHistoriesByPatientID(patientID);
    }

    /**
     * Retrieves all slots for current doctor.
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
     * Retrieves slots with the "AVAIALBLE" status for the current doctor.
     *
     * @return List of Available Slot objects for the current doctor
     */
    public List<Slot> getAvailableSlotsForDoctor() {
        Doctor currentDoctor = (Doctor) authController.getCurrentUser();
        return slotDataManager.getSlots().stream()
                .filter(slot -> slot.getDoctorID().equals(currentDoctor.getUserID())
                && slot.getStatus() == Slot.SlotStatus.AVAILABLE)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves slots with the "AVAIALBLE" status for a specific doctor.
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
     * Retrieves pending slots for current doctor.
     *
     * @return List of Pending Slot objects for the specified doctor
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
     *
     * @return List of Booked Slot objects for the specified doctor
     */
    public List<Slot> getBookedSlotsForDoctor() {
        Doctor currentDoctor = (Doctor) authController.getCurrentUser();
        return slotDataManager.getSlots().stream()
                .filter(slot -> slot.getDoctorID().equals(currentDoctor.getUserID())
                && slot.getStatus() == Slot.SlotStatus.BOOKED)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a list of all medication objects
     *
     * @return List of all medication objects
     */
    public List<Medication> getAllMedication() {
        return medicationDataManager.getMedications();
    }

    /**
     * Retrieves a list of completed appointments for the current doctor.
     *
     * @return List of completed Appointment objects for the specified doctor.
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
     * Retrieves a list of outcomes for completed appointments of the current
     * doctor.
     *
     * @return List of Outcome objects linked to completed appointments for the
     * current doctor.
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
     * Retrieves the prescriptions for a given appointment ID.
     *
     * @param appointmentID The appointment ID to filter the prescription
     * database.
     * @return The list of prescriptions if found; otherwise, return an empty
     * list.
     */
    public List<Prescription> getPrescriptionsByAppointmentID(String appointmentID) {
        return prescriptionDataManager.getPrescriptions().stream()
                .filter(prescription -> prescription.getAppointmentID().equals(appointmentID))
                .collect(Collectors.toList());
    }

    //------------------------------------------Get-Object Methods-------------------------------------------------
    /**
     * Retrieves a patient object by their ID.
     *
     * @param patientID The ID of the patient object to retrieve
     * @return The Patient object with the specified ID, or null if not found
     */
    public Patient getPatientByID(String patientID) {
        User user = userDataManager.getUserByID(patientID);
        return (user instanceof Patient) ? (Patient) user : null;
    }

    /**
     * Retrieves a history object by its ID.
     *
     * @param historyID The ID of the history object to retrieve
     * @return The History object with the specified ID, or null if not found
     */
    public History getHistoryByID(String historyID) {

        return historyDataManager.getHistoryByID(historyID);
    }

    /**
     * Retrieves a slot object by its ID.
     *
     * @param slotID The ID of the slot object to retrieve
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
     * Retrieves the patient associated with a given appointment.
     *
     * @param appointment The appointment object to retrieve the patient from.
     * @return The Patient object if patient is found, or null if not found
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
     *
     * @param appointmentID The ID of the appointment object to retrieve
     * @return The Appointment object with the specified ID, or null if not
     * found
     */
    public Appointment getAppointmentByID(String appointmentID) {
        return appointmentDataManager.getAppointmentByID(appointmentID);
    }

    /**
     * Retrieves the outcome ID for a given appointment.
     *
     * @param appointment The appointment object to get the outcome ID from.
     * @return The Outcome ID if it found, or null if not found.
     */
    public String getOutcomeIDFromAppointment(Appointment appointment) {
        if (appointment == null) {
            return null;
        }
        return appointment.getOutcomeID();
    }

    /**
     * Retrieves an appointment record by its ID.
     *
     * @param outcomeID The ID of the Outcome record to retrieve
     * @return The Outcome object with the specified ID, or null if not found.
     */
    public Outcome getOutcomeByID(String outcomeID) {
        return outcomeDataManager.getOutcomeByID(outcomeID);
    }

    /**
     * Retrieves the a patient object for a given appointment object.
     *
     * @param appointment The appointment object used to retrieve patient ID
     * @return The Patient object if found, or null if not found.
     */
    public Patient getPatientFromAppointment(Appointment appointment) {
        String patientID = appointment.getPatientID();
        Patient patient = getPatientByID(patientID);
        return patient;
    }

    /**
     * Retrieves a prescription by its ID.
     *
     * @param prescriptionID The ID of the prescription to retrieve.
     * @return The Prescription object if it found, or null if not found.
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

    //------------------------------------------ID-Generation Methods-------------------------------------------------
    /**
     * Generates a new unique history ID.
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
     *
     * @return A new unique history ID
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
     *
     * @return A new unique Outcome ID
     */
    private String generateOutcomeID() {
        int count = 1;
        String uniqueID = "R" + String.format("%02d", outcomeDataManager.getAllOutcomes().size() + 1);
        while (outcomeDataManager.getOutcomeByID(uniqueID) != null) {
            uniqueID = "R" + String.format("%02d", count);
            count++;
        }
        return uniqueID;
    }

    /**
     * Generates a new unique prescription ID
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

    //---------------------------------------------Validation Functions------------------------------------------------
    /**
     * Checks if given Patient ID is valid and belongs to a patient under the
     * doctor's care.
     *
     * @param patientID The Patient ID to validate and is under the current
     * doctor's care
     * @return true if Patient ID is valid and belongs to current doctor;
     * otherwise, return false
     */
    public boolean isValidPatientID(String patientID) {
        return getPatientsUnderCare().stream()
                .anyMatch(patient -> patient.getUserID().equals(patientID));
    }

    /**
     * Checks if given History ID belongs to a patient under the current
     * doctor's care
     *
     * @param patientID The Patient ID to validate and is under the current
     * doctor's care
     * @param historyID The History ID to validate
     * @return true if History ID is valid and belongs to current patient and
     * doctor, otherwise; return false
     */
    public boolean isPatientsHistory(String patientID, String historyID) {
        return getPatientMedicalHistory(patientID).stream()
                .anyMatch(history -> history.getHistoryID().equals(historyID));
    }

    /**
     * Checks if given Slot ID is valid.
     *
     * @param slotID The Slot ID to validate
     * @return true if Slot ID is valid, by the current doctor, otherwise;
     * return false
     */
    public boolean isValidDoctorSlotID(String slotID) {
        Doctor currentDoctor = (Doctor) authController.getCurrentUser();
        String doctorID = currentDoctor.getUserID();
        return slotDataManager.getStatusByID(slotID, doctorID) != null;
    }

    /**
     * Checks if given Slot ID is valid & available.
     *
     * @param slotID The Slot ID to validate
     * @return true if Slot ID is valid, by the current doctor and is available,
     * otherwise; return false
     */
    public boolean isValidAvailableSlotID(String slotID) {
        Doctor currentDoctor = (Doctor) authController.getCurrentUser();
        String doctorID = currentDoctor.getUserID();
        return slotDataManager.getStatusByID(slotID, doctorID, Slot.SlotStatus.AVAILABLE) != null;
    }

    /**
     * Checks if given Slot ID is valid & pending.
     *
     * @param slotID The Slot ID to validate
     * @return true if Slot ID is valid, by the current doctor and is pending,
     * otherwise; return false
     */
    public boolean isValidPendingSlotID(String slotID) {
        Doctor currentDoctor = (Doctor) authController.getCurrentUser();
        String doctorID = currentDoctor.getUserID();
        return slotDataManager.getStatusByID(slotID, doctorID, Slot.SlotStatus.PENDING) != null;
    }

    /**
     * Checks if given Slot ID is valid & booked.
     *
     * @param slotID The Slot ID to validate
     * @return true if Slot ID is valid, by the current doctor and is booked;
     * otherwise, return false
     */
    public boolean isValidBookedSlotID(String slotID) {
        Doctor currentDoctor = (Doctor) authController.getCurrentUser();
        String doctorID = currentDoctor.getUserID();
        return slotDataManager.getStatusByID(slotID, doctorID, Slot.SlotStatus.BOOKED) != null;
    }

    /**
     * Checks if given slot ID is valid & booked.
     *
     * @param slotID The ID to validate
     * @return true if Slot ID is valid, by the current doctor and, completed;
     * otherwise, return false
     */
    public boolean isValidCompletedSlotID(String slotID) {
        Doctor currentDoctor = (Doctor) authController.getCurrentUser();
        String doctorID = currentDoctor.getUserID();
        return slotDataManager.getStatusByID(slotID, doctorID, Slot.SlotStatus.COMPLETED) != null;
    }

    /**
     * Checks if given appointment ID is valid & completed for the current
     * doctor.
     *
     * @param appointmentID The ID to validate
     * @return true if Appointment ID is valid, belongs to the current doctor,
     * and is completed; otherwise, return false
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
     * Checks if given Medication ID is valid.
     *
     * @param medicationID The Medication ID to validate
     * @return true if Medication ID is valid; otherwise, return false
     */
    public boolean isValidMedicationID(String medicationID) {
        return medicationDataManager.getMedicationByID(medicationID) != null;
    }

    /**
     * Checks if given prescription ID is valid and associated with an
     * appointment that belongs to the current doctor.
     *
     * @param prescriptionID The Prescription ID to validate.
     * @return true if Prescription ID is valid and is associated with an
     * appointment the belongs to the current doctor; otherwise, return false
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
    //------------------------------------------Funtion 1-specific methods-------------------------------------------------
    //NIL

    //------------------------------------------Funtion 2-specific methods-------------------------------------------------
    /**
     * Adds a new medical history record for a patient.
     *
     * @param patientID The ID of the patient
     * @param diagnosis The diagnosis for new history record
     * @param treatment The treatment for new history record
     * @return true if addition was successful; otherwise, return false.
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
     *
     * @param historyID The ID of the history record to update
     * @param newDiagnosis The new diagnosis
     * @param newTreatment The new treatment
     * @return true if update was successful; otherwise, return false.
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
     * Removes a specific History
     *
     * @param historyID The ID of the history to remove
     * @return true if removal was successful; otherwise, return false.
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

    //------------------------------------------Funtion 3-specific methods-------------------------------------------------
    //NIL
    //------------------------------------------Funtion 4-specific methods-------------------------------------------------
    /**
     * Updates a specific slot object.
     *
     * @param slot The Slot object to update
     * @param newDate The new date
     * @param newStartTime The new start time
     * @param newEndTime The new end time
     * @return true if update was successful; otherwise, return false.
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
     *
     * @param date Date of available slot
     * @param startTime Start time of available slot
     * @param endTime End time of available slot
     * @return true if addition was successful; otherwise, return false.
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
     * Removes a specific Slot
     *
     * @param SlotID The ID of the slot to remove
     * @return true if removal was successful; otherwise, return false.
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

    //------------------------------------------Funtion 5-specific methods-------------------------------------------------
    /**
     * Updates a slot and appointment record.
     *
     * @param accept If true, sets slot status to "BOOKED" and appointment to
     * "CONFIRMED"; otherwise, sets slot to "REMOVED" and appointment to
     * "CANCELED"
     * @param appointment Appointment object to update
     * @param slot Slot object to update
     * @return true if update was successful; otherwise, return false.
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

    //------------------------------------------Funtion 6-specific methods-------------------------------------------------
    //NIL
    //------------------------------------------Funtion 7 & 8 methods-------------------------------------------------
    /**
     * Adds new prescription.
     *
     * @param appointmentID The appointment ID linked to this prescription
     * @param medicationID The medication ID used to prescribe medication
     * @param quantity Quantity of medication prescribed
     * @param notes Prescription notes, e.g. take 1 pill in the morning after
     * meal
     * @return true if addition was successful; otherwise, return false.
     */
    //String prescriptionID, String appointmentID, String medicationID, int quantity, PrescriptionStatus status, String notes
    public Boolean addPrescription(String appointmentID, String medicationID, int quantity, String notes) {
        try {
            String prescriptionID = generatePrescriptionID();
            Prescription newPrescription = new Prescription(prescriptionID, appointmentID, medicationID, quantity, Prescription.PrescriptionStatus.PENDING, notes);
            prescriptionDataManager.addPrescription(newPrescription);
            return true;
        } catch (IllegalArgumentException e) {
            System.err.println("Error adding prescription: " + e.getMessage());
            return false;
        }
    }

    /**
     * Adds a new outcome record.
     *
     * @param appointment Appointment object to complete
     * @param slot Slot object to complete
     * @param serviceProvided The service provided to create new outcome
     * @param prescriptionID The string of medications, seperated by semicolons,
     * to create new outcome
     * @param consultationNotes The consultation notes to create new outcome
     * @return true if completion of appointment was successful; otherwise,
     * return false.
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
     * Takes in the whole string of prescription ID, seperates them by semicolon
     * and count amount of medications
     *
     * @return the number of medications within the current prescription string
     */
    public int countMedication(String prescriptionString) {
        if (prescriptionString == null || prescriptionString.trim().isEmpty()) {
            return 0;
        }
        String[] medications = prescriptionString.split(";");
        return medications.length;
    }

    /**
     * Updates an exisitng prescription
     *
     * @param prescription The Prescription object to update
     * @param newMedicationID The new medication ID
     * @param newQuantity The new quantity
     * @param newPrescriptionNotes The new prescription notes
     * @return true if update was successful successful; otherwise, return
     * false.
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
     * Removes a prescription
     *
     * @param prescriptionID The ID of the prescription to remove
     * @return true if removal was successful; otherwise, return false.
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
     * Updates an existing outcome record.
     *
     * @param outcome The Outcome object to update
     * @param serviceProvided The new service provided
     * @param prescriptionID The new medications provided
     * @param consultationNotes The new consultation notes
     * @return true if update was successful; otherwise, return false.
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

    //------------------------------------------Other methods-------------------------------------------------
    /**
     * Sorts the Slots list by priority: "BOOKED," "PENDING," "AVAILABLE,"
     * "COMPLETED," and "REMOVED," then by status and start time.
     *
     * @return A sorted slots list
     */
    // Function to sort slots by Date, Status, and Start Time
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

    //----------------------------------------------------------------------------------------------------
}
