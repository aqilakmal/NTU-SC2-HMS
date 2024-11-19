package controller;

import entity.*;
import controller.data.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;

/**
 * Controller class for managing patient-related operations in the Hospital
 * Management System.
 */
public class PatientController {

    private AppointmentDataManager appointmentDataManager;
    private SlotDataManager slotDataManager;
    private DoctorController doctorController;
    private AuthenticationController authController;
    private HistoryDataManager historyDataManager;
    private OutcomeDataManager outcomeDataManager;
    private PrescriptionDataManager prescriptionDataManager;
    private MedicationDataManager medicationDataManager;
    private UserDataManager userDataManager;

    /**
     * Constructor for PatientController.
     * 
     * @param appointmentDataManager The AppointmentDataManager instance
     * @param slotDataManager        The SlotDataManager instance
     * @param doctorController       The DoctorController instance
     * @param authController         The AuthenticationController instance
     * @param historyDataManager     The historyDataManager instnace
     * @param outcomeDataManager     The OutcomeDataManager instance
     * @param prescriptionDataManager The PrescriptionDataManager instance
     * @param medicationDataManager   The MedicationDataManager instance
     * @param userDataManager         The UserDataManager instance
     */
    public PatientController(AppointmentDataManager appointmentDataManager, HistoryDataManager historyDataManager,
            SlotDataManager slotDataManager, DoctorController doctorController,
            AuthenticationController authController, OutcomeDataManager outcomeDataManager,
            PrescriptionDataManager prescriptionDataManager, MedicationDataManager medicationDataManager,
            UserDataManager userDataManager) {
        this.appointmentDataManager = appointmentDataManager;
        this.slotDataManager = slotDataManager;
        this.doctorController = doctorController;
        this.authController = authController;
        this.historyDataManager = historyDataManager;
        this.outcomeDataManager = outcomeDataManager;
        this.prescriptionDataManager = prescriptionDataManager;
        this.medicationDataManager = medicationDataManager;
        this.userDataManager = userDataManager;
    }

    /**
     * Schedules an appointment for the current patient.
     * 
     * This method schedules an appointment by creating a new appointment object
     * with the provided doctor and slot IDs.
     * It also updates the status of the selected slot to PENDING and stores the new
     * appointment in the data manager.
     * 
     * @param doctorID The ID of the selected doctor
     * @param slotID   The ID of the selected slot
     * @return true if the appointment was scheduled successfully, false otherwise
     */
    public boolean scheduleAppointment(String doctorID, String slotID) {
        try {
            // Generate a new appointment ID
            String appointmentID = generateAppointmentID();

            // Create a new appointment
            Appointment newAppointment = new Appointment(
                    appointmentID,
                    authController.getCurrentUser().getUserID(),
                    doctorID,
                    slotID,
                    Appointment.AppointmentStatus.REQUESTED,
                    "" // No outcome ID for a new appointment
            );

            // Add the appointment
            appointmentDataManager.addAppointment(newAppointment);

            // Update the slot status to PENDING
            Slot slot = slotDataManager.getSlotByID(slotID);
            if (slot != null) {
                slot.setStatus(Slot.SlotStatus.PENDING);
                slotDataManager.updateSlot(slot);
            }

            return true;
        } catch (Exception e) {
            System.err.println("Error scheduling appointment: " + e.getMessage());
            return false;
        }
    }

    /**
     * Generates a new unique appointment ID.
     * 
     * @return A new unique appointment ID
     */
    private String generateAppointmentID() {
        // Implementation depends on your ID generation strategy
        // This is a simple example and might need to be more robust in a real system
        return "A" + String.format("%02d", appointmentDataManager.getAllAppointments().size() + 1);
    }

    // FROM OTHER CONTROLLERS AND MANAGERS

    /**
     * Retrieves a slot by its ID.
     * 
     * @param slotID The ID of the slot to retrieve
     * @return The Slot object associated with the given slot ID
     */
    public Slot getSlotByID(String slotID) {
        return slotDataManager.getSlotByID(slotID);
    }

    /**
     * Retrieves a list of available slots for a given doctor.
     * 
     * @param doctorID The ID of the doctor to get available slots for
     * @return A list of Slot objects representing available slots for the doctor
     */
    public List<Slot> getAvailableSlotsForDoctor(String doctorID) {
        return doctorController.getAvailableSlotsForDoctor(doctorID);
    }

    /**
     * Retrieves the current patient's ID.
     * 
     * @return The ID of the current patient
     */
    public String getCurrentUserID() {
        return authController.getCurrentUser().getUserID();
    }

    /**
     * Retrieves a doctor by their ID.
     * 
     * @param doctorID The ID of the doctor to retrieve
     * @return The Doctor object associated with the given doctor ID
     */
    public Doctor getDoctorByID(String doctorID) {
        return doctorController.getDoctorByID(doctorID);
    }

    /**
     * Retrieves a list of all doctors.
     * 
     * @return A list of Doctor objects representing all doctors in the system
     */
    public List<Doctor> getAllDoctors() {
        return doctorController.getAllDoctors();
    }

    /**
     * Retrieves a patient by their ID.
     * 
     * @param patientID The ID of the patient to retrieve
     * @return The Patient object associated with the given patient ID
     */
    public Patient getPatientByID(String patientID) {
        return (Patient) userDataManager.getUserByID(patientID);
    }

    /**
     * Retrieves the medical history of the current patient.
     * 
     * @return A list of History objects representing the patient's medical history
     */
    public List<History> getPatientMedicalHistory() {
        return historyDataManager.getHistoriesByPatientID(authController.getCurrentUser().getUserID());
    }

    /**
     * Retrieves filtered appointments for the current patient based on the specified type.
     * 
     * @param type The type of appointments to retrieve:
     *             "SCHEDULED" - Returns appointments that are not CANCELLED or COMPLETED
     *             "REQUESTED" - Returns only REQUESTED appointments
     * @return List of filtered appointments based on the specified type
     */
    public List<Appointment> getFilteredAppointments(String type) {
        String patientID = authController.getCurrentUser().getUserID();

        // Create a Map to hold filter criteria
        Map<String, String> filters = new HashMap<>();
        filters.put("patientID", patientID); // Add the patientID filter

        // Pass the filters map to the getFilteredAppointments method
        List<Appointment> allAppointments = appointmentDataManager.getFilteredAppointments(filters);

        // Filter appointments based on type parameter
        if (type.equals("SCHEDULED")) {
            return allAppointments.stream()
                    .filter(appointment -> appointment.getStatus() != Appointment.AppointmentStatus.CANCELLED &&
                            appointment.getStatus() != Appointment.AppointmentStatus.COMPLETED)
                    .collect(Collectors.toList());
        } else if (type.equals("REQUESTED")) {
            return allAppointments.stream()
                    .filter(appointment -> appointment.getStatus() == Appointment.AppointmentStatus.REQUESTED)
                    .collect(Collectors.toList());
        }

        return allAppointments;
    }

    /**
     * Retrieves the list of past appointments for the current patient.
     * Filters appointments to include only those with status COMPLETED or
     * CANCELLED.
     * 
     * @return list of past appointments with status COMPLETED or CANCELLED
     */
    public List<Appointment> getPastAppointments() {
        String patientID = authController.getCurrentUser().getUserID();

        // Create a Map to hold filter criteria
        Map<String, String> filters = new HashMap<>();
        filters.put("patientID", patientID); // Add the patientID filter

        // Retrieve appointments based on the patient ID
        List<Appointment> allAppointments = appointmentDataManager.getFilteredAppointments(filters);

        // Filter to include only appointments with status COMPLETED or CANCELLED
        return allAppointments.stream()
                .filter(appointment -> appointment.getStatus() == Appointment.AppointmentStatus.COMPLETED ||
                        appointment.getStatus() == Appointment.AppointmentStatus.CANCELLED)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves an appointment by its unique ID.
     * 
     * @param appointmentID The ID of the appointment to retrieve
     * @return The appointment with the specified ID, or null if no such appointment
     *         exists
     */
    public Appointment getAppointmentByID(String appointmentID) {
        return appointmentDataManager.getAppointmentByID(appointmentID);
    }

    /**
     * Updates the email address for the currently logged-in user.
     * 
     * @param newEmailAddress The new email address to set for the current user
     */
    public void updateEmailAddress(String newEmailAddress) {
        User currentUser = authController.getCurrentUser();
        if (currentUser != null) {
            currentUser.updateEmailAddress(newEmailAddress);
            System.out.println("Email address updated successfully.");
        } else {
            System.out.println("No user is currently logged in.");
        }
    }

    /**
     * Updates the password for the currently logged-in user.
     * 
     * @param newPassword The new password to set for the current user
     */

    public void updatePassword(String newPassword) {
        User currentUser = authController.getCurrentUser();
        if (currentUser != null) {
            currentUser.updatePassword(newPassword);
            System.out.println("Password address updated successfully.");
        } else {
            System.out.println("No user is currently logged in.");
        }
    }

    /**
     * Updates the contact number for the currently logged-in user.
     * 
     * @param newContact The new contact number to set for the current user
     */
    public void updateContactNumber(String newContact) {
        User currentUser = authController.getCurrentUser();
        if (currentUser != null) {
            currentUser.updateContactNumber(newContact);
            System.out.println("Contact number address updated successfully.");
        } else {
            System.out.println("No user is currently logged in.");
        }
    }

    /**
     * Retrieves the outcome record for a specific appointment
     * @param appointmentID The ID of the appointment to get the outcome for
     * @return The Outcome object if found, null otherwise
     */
    public Outcome getOutcomeByAppointmentID(String appointmentID) {
        Appointment appointment = getAppointmentByID(appointmentID);
        if (appointment != null && appointment.getOutcomeID() != null) {
            return outcomeDataManager.getOutcomeByID(appointment.getOutcomeID());
        }
        return null;
    }

    /**
     * Retrieves a prescription by its ID
     * @param prescriptionID The ID of the prescription to retrieve
     * @return The Prescription object if found, null otherwise
     */
    public Prescription getPrescriptionByID(String prescriptionID) {
        return prescriptionDataManager.getPrescriptionByID(prescriptionID);
    }

    /**
     * Retrieves a medication by its ID
     * @param medicationID The ID of the medication to retrieve
     * @return The Medication object if found, null otherwise
     */
    public Medication getMedicationByID(String medicationID) {
        return medicationDataManager.getMedicationByID(medicationID);
    }

    /**
     * Retrieves a user by their ID
     * @param userID The ID of the user to retrieve
     * @return The User object if found, null otherwise
     */
    public User getUserByID(String userID) {
        return userDataManager.getUserByID(userID);
    }

    /**
     * Retrieves an appointment by its outcome ID
     * @param outcomeID The ID of the outcome to find the appointment for
     * @return The Appointment object if found, null otherwise
     */
    public Appointment getAppointmentByOutcomeID(String outcomeID) {
        return appointmentDataManager.getAllAppointments().stream()
                .filter(appointment -> outcomeID.equals(appointment.getOutcomeID()))
                .findFirst()
                .orElse(null);
    }
}
