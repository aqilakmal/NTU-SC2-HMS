package controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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

    public Patient getPatientFromAppointment(Appointment appointment) {
        String patientID = appointment.getPatientID();
        Patient patient = getPatientByID(patientID);
        return patient;
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
     * Retrieves the patient associated with a given appointment.
     *
     * @param appointment The appointment to retrieve the patient from.
     * @return The Patient object if the patient is found and valid, otherwise
     * null.
     */
    public Patient getPatientByAppointment(Appointment appointment) {
        if (appointment == null) {
            return null; // Ensure the appointment is valid
        }

        String patientID = appointment.getPatientID();
        User user = userDataManager.getUserByID(patientID);

        if (user != null && user instanceof Patient) {
            return (Patient) user; // Explicitly cast to Patient
        } else {
            return null; // Return null if user is not a Patient or if user does not exist
        }
    }

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
        while (historyDataManager.getHistoryByID(uniqueID) != null) {
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
     * @param appointmentID appointment ID
     * @param medicationID medication ID
     * @param notes prescription notes
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
            System.err.println("Error adding prescription: " + e.getMessage());
            return false;
        }
    }

    /**
     * updates prescription.
     *
     * @param ds
     * @param appointmentID appointment ID
     * @param medicationID medication ID
     * @param notes prescription notes
     * @return true if the update was successful, false otherwise
     */
    //String prescriptionID, String appointmentID, String medicationID, int quantity, PrescriptionStatus status, String notes
    public Boolean updatePrescription(Prescription prescription, String medicationID, int quantity, String notes) {
        try {
            prescription.setMedicationID(medicationID);
            prescription.setQuantity(quantity);
            prescription.setNotes(notes);
            prescriptionDataManager.updatePrescription(prescription);
            return true;
        } catch (IllegalArgumentException e) {
            System.err.println("Error updating prescription: " + e.getMessage());
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
        while (prescriptionDataManager.getPrescriptionByID(uniqueID) != null) {
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
     * Removes a specific Slot
     *
     * @param SlotID The ID of the slot to remove
     * @return true if the removal was successful, false otherwise
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
     * Removes a specific Slot
     *
     * @param historyID The ID of the slot to remove
     * @return true if the removal was successful, false otherwise
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
     * Removes a specific Prescription
     *
     * @param prescriptionID The ID of the prescription to remove
     * @return true if the removal was successful, false otherwise
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
     * @param serviceProvided string of service provided to attach to outcome
     * @param prescriptionID string of medications provided to attach to outcome
     * @param consultationNotes string of consultation notes to attach to
     * outcome
     * @return true if the completion of appointment was successful, false
     * otherwise
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
     * updates an existing outcome record.
     *
     * @param outcome Outcome object to update
     * @param serviceProvided string of service provided to attach to outcome
     * @param prescriptionID string of medications provided to attach to outcome
     * @param consultationNotes string of consultation notes to attach to
     * outcome
     * @return true if the update was successful, false otherwise
     */
    public boolean updateOutcome(Outcome outcome, String serviceProvided, String prescriptionID, String consultationNotes) {
        try {
            // Update fields only if they are new or updated
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
     * Takes in the whole string of prescription ID, seperates them by semicolon
     * and count amount of medications
     *
     * @return A count of medications
     */
    public int countMedication(String prescriptionString) {
        if (prescriptionString == null || prescriptionString.trim().isEmpty()) {
            return 0;
        }
        String[] medications = prescriptionString.split(";");
        return medications.length;
    }

    /**
     * Generates a new unique Outcome ID.
     *
     * @return A new unique Outcome ID
     */
    private String generateOutcomeID() {
        // Implementation depends on your ID generation strategy
        // This is a simple example and might need to be more robust in a real system
        int count = 1;
        String uniqueID = "R" + String.format("%02d", outcomeDataManager.getAllOutcomes().size() + 1);
        while (outcomeDataManager.getOutcomeByID(uniqueID) != null) {
            uniqueID = "R" + String.format("%02d", count);
            count++;
        }
        return uniqueID;
    }

    /**
     * Sort Slots list
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
        while (slotDataManager.getSlotByID(uniqueID) != null) {
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
     * Checks if the given slot ID is valid.
     *
     * @param slotID The ID to validate
     * @return true if the ID is valid, by the current doctor, false otherwise
     */
    public boolean isValidDoctorSlotID(String slotID) {
        Doctor currentDoctor = (Doctor) authController.getCurrentUser();
        String doctorID = currentDoctor.getUserID();
        return slotDataManager.getStatusByID(slotID, doctorID) != null;
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

    /**
     * Checks if the given slot ID is valid & booked.
     *
     * @param slotID The ID to validate
     * @return true if the ID is valid, by the current doctor and, pending,
     * false otherwise
     */
    public boolean isValidCompletedSlotID(String slotID) {
        Doctor currentDoctor = (Doctor) authController.getCurrentUser();
        String doctorID = currentDoctor.getUserID();
        return slotDataManager.getStatusByID(slotID, doctorID, Slot.SlotStatus.COMPLETED) != null;
    }

    /**
     * Checks if the given appointment ID is valid & completed for the current
     * doctor.
     *
     * @param appointmentID The ID to validate
     * @return true if the appointment is valid, belongs to the current doctor,
     * and is completed, false otherwise
     */
    public boolean isValidCompletedAppointmentID(String appointmentID) {
        Doctor currentDoctor = (Doctor) authController.getCurrentUser();
        String doctorID = currentDoctor.getUserID();

        // Retrieve the appointment by ID
        Appointment appointment = appointmentDataManager.getAppointmentByID(appointmentID);

        // Check if the appointment is not null, belongs to the current doctor, and has COMPLETED status
        return appointment != null
                && appointment.getDoctorID().equals(doctorID)
                && appointment.getStatus() == Appointment.AppointmentStatus.COMPLETED;
    }

    /**
     * Checks if the given Medication ID is valid.
     *
     * @param medicationID The ID to validate
     * @return true if the ID is valid
     */
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
     * Retrieves an appointment record by its ID.
     *
     * @param appointmentID The ID of the appointment record to retrieve
     * @return The Appointment object with the specified ID, or null if not
     * found
     */
    public Appointment getAppointmentByID(String appointmentID) {
        return appointmentDataManager.getAppointmentByID(appointmentID);
    }

    /**
     * Retrieves an appointment record by its ID.
     *
     * @param appointmentID The ID of the appointment record to retrieve
     * @return The Appointment object with the specified ID, or null if not
     * found
     */
    public Outcome getOutcomeByID(String OutcomeID) {
        return outcomeDataManager.getOutcomeByID(OutcomeID);
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
     * Retrieves the outcome ID for a given appointment.
     *
     * @param appointment The appointment object to get the outcome ID from.
     * @return The outcome ID if it exists, otherwise null.
     */
    public String getOutcomeIDFromAppointment(Appointment appointment) {
        if (appointment == null) {
            return null;
        }
        return appointment.getOutcomeID();
    }

    /**
     * Retrieves the prescriptions for a given appointment ID.
     *
     * @param appointmentID The appointment ID to filter the prescription
     * database.
     * @return The list of prescriptions if they exist, otherwise an empty list.
     */
    public List<Prescription> getPrescriptionsByAppointmentID(String appointmentID) {
        return prescriptionDataManager.getPrescriptions().stream()
                .filter(prescription -> prescription.getAppointmentID().equals(appointmentID))
                .collect(Collectors.toList());
    }

    /**
     * Checks if the given prescription ID is valid and associated with an
     * appointment that belongs to the current doctor.
     *
     * @param prescriptionID The ID of the prescription to validate.
     * @return true if the prescription is valid, exists, and the associated
     * appointment belongs to the current doctor; false otherwise.
     */
    public boolean isValidPrescriptionID(String prescriptionID) {
        // Retrieve the current doctor
        Doctor currentDoctor = (Doctor) authController.getCurrentUser();
        String doctorID = currentDoctor.getUserID();

        // Retrieve the prescription using prescriptionID
        Prescription prescription = prescriptionDataManager.getPrescriptionByID(prescriptionID);
        if (prescription == null) {
            System.out.println("Prescription does not exist");
            return false; // Prescription does not exist
        }

        // Retrieve the appointment associated with the prescription
        String appointmentID = prescription.getAppointmentID();
        Appointment appointment = appointmentDataManager.getAppointmentByID(appointmentID);

        // Check if the appointment exists and is associated with the current doctor
        if (appointment == null || !appointment.getDoctorID().equals(doctorID)) {
            System.out.println("Appointment not associated with current doctor");
            return false; // Appointment does not exist or does not belong to the current doctor
        }

        // The prescription is valid and the appointment belongs to the current doctor
        return true;
    }

    /**
     * Retrieves a prescription by its ID.
     *
     * @param prescriptionID The ID of the prescription to retrieve.
     * @return The Prescription object if it exists, otherwise null.
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
     * Edits a specific medication in the prescription string.
     *
     * @param prescriptionString The original prescription string with
     * medications separated by semicolons.
     * @param index The index of the medication to be edited.
     * @param newMedication The new medication to replace the existing one at
     * the specified index.
     * @return The updated prescription string.
     * @throws IllegalArgumentException If the index is out of bounds.
     */
    public static String editMedicationInPrescriptionString(String prescriptionString, int index, String newMedication) {
        // Convert the prescription string to a list
        List<String> medications = new ArrayList<>(Arrays.asList(prescriptionString.split(";")));

        // Check if the provided index is valid
        if (index < 0 || index >= medications.size()) {
            throw new IllegalArgumentException("Index out of bounds for the medications list.");
        }

        // Update the medication at the specified index
        medications.set(index, newMedication);

        // Convert the list back to a semicolon-separated string
        return String.join(";", medications);
    }

}
