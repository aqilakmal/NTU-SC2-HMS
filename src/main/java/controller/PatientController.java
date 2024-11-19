package controller;

import entity.*;
import controller.data.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;

/**
 * Controller class for managing patient-related operations in the Hospital Management System.
 * This class handles all patient interactions including appointment scheduling, medical record access,
 * and personal information management. It acts as an intermediary between the patient boundary 
 * and various data managers.
 *
 * @author Group 7
 * @version 1.0
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
     * Constructor for PatientController that initializes all required data managers and controllers.
     * This constructor sets up the necessary dependencies for managing patient operations
     * including appointments, medical records, and user data.
     * 
     * @param appointmentDataManager The data manager for handling appointment operations
     * @param historyDataManager The data manager for handling patient medical history
     * @param slotDataManager The data manager for handling appointment time slots
     * @param doctorController The controller for managing doctor-related operations
     * @param authController The controller for handling authentication and user sessions
     * @param outcomeDataManager The data manager for handling appointment outcomes
     * @param prescriptionDataManager The data manager for handling prescriptions
     * @param medicationDataManager The data manager for handling medications
     * @param userDataManager The data manager for handling user data
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
     * Schedules an appointment for the current patient with a specified doctor and time slot.
     * This method creates a new appointment with REQUESTED status and updates the selected
     * time slot to PENDING status. It handles the entire appointment creation process
     * including generating a unique appointment ID.
     * 
     * @param doctorID The ID of the doctor with whom the appointment is being scheduled
     * @param slotID The ID of the time slot selected for the appointment
     * @return true if the appointment was scheduled successfully, false if an error occurred
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
     * Generates a new unique appointment ID for creating appointments.
     * The ID is generated using a simple incremental strategy based on the current
     * number of appointments in the system. The format is "A" followed by a 
     * two-digit number.
     * 
     * @return A new unique appointment ID string
     */
    private String generateAppointmentID() {
        // Implementation depends on your ID generation strategy
        // This is a simple example and might need to be more robust in a real system
        return "A" + String.format("%02d", appointmentDataManager.getAllAppointments().size() + 1);
    }

    // FROM OTHER CONTROLLERS AND MANAGERS

    /**
     * Retrieves a specific appointment time slot using its ID.
     * This method provides access to detailed information about a specific
     * time slot including its status and associated doctor.
     * 
     * @param slotID The unique identifier of the slot to retrieve
     * @return The Slot object associated with the given slot ID, or null if not found
     */
    public Slot getSlotByID(String slotID) {
        return slotDataManager.getSlotByID(slotID);
    }

    /**
     * Retrieves all available appointment slots for a specific doctor.
     * This method filters through all slots to return only those that are
     * available for booking with the specified doctor.
     * 
     * @param doctorID The unique identifier of the doctor
     * @return A list of available Slot objects for the specified doctor
     */
    public List<Slot> getAvailableSlotsForDoctor(String doctorID) {
        return doctorController.getAvailableSlotsForDoctor(doctorID);
    }

    /**
     * Retrieves the ID of the currently logged-in patient.
     * This method provides access to the current user's ID for various
     * operations that require user identification.
     * 
     * @return The unique identifier of the current user
     */
    public String getCurrentUserID() {
        return authController.getCurrentUser().getUserID();
    }

    /**
     * Retrieves detailed information about a specific doctor.
     * This method provides access to a doctor's complete profile including
     * their specialization and contact information.
     * 
     * @param doctorID The unique identifier of the doctor to retrieve
     * @return The Doctor object containing the doctor's information
     */
    public Doctor getDoctorByID(String doctorID) {
        return doctorController.getDoctorByID(doctorID);
    }

    /**
     * Retrieves a list of all doctors in the system.
     * This method provides access to information about all available doctors
     * that patients can schedule appointments with.
     * 
     * @return A list of all Doctor objects in the system
     */
    public List<Doctor> getAllDoctors() {
        return doctorController.getAllDoctors();
    }

    /**
     * Retrieves detailed information about a specific patient.
     * This method provides access to a patient's complete profile including
     * their personal and medical information.
     * 
     * @param patientID The unique identifier of the patient to retrieve
     * @return The Patient object containing the patient's information
     */
    public Patient getPatientByID(String patientID) {
        return (Patient) userDataManager.getUserByID(patientID);
    }

    /**
     * Retrieves the complete medical history of the currently logged-in patient.
     * This method provides access to all historical medical records including
     * past diagnoses, treatments, and medications.
     * 
     * @return A list of History objects representing the patient's medical history
     */
    public List<History> getPatientMedicalHistory() {
        return historyDataManager.getHistoriesByPatientID(authController.getCurrentUser().getUserID());
    }

    /**
     * Retrieves filtered appointments for the current patient based on specified criteria.
     * This method allows filtering appointments by their status to view either
     * scheduled upcoming appointments or requested appointments awaiting confirmation.
     * 
     * @param type The type of appointments to retrieve: "SCHEDULED" for upcoming appointments,
     *             "REQUESTED" for pending appointment requests
     * @return A filtered list of appointments based on the specified type
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
     * Retrieves all past appointments for the current patient.
     * This method filters through all appointments to return only those that have
     * been either completed or cancelled, providing a historical view of
     * patient appointments.
     * 
     * @return A list of past appointments with status COMPLETED or CANCELLED
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
     * Retrieves detailed information about a specific appointment.
     * This method provides access to all appointment details including the
     * associated doctor, time slot, and current status.
     * 
     * @param appointmentID The unique identifier of the appointment to retrieve
     * @return The Appointment object if found, null if no such appointment exists
     */
    public Appointment getAppointmentByID(String appointmentID) {
        return appointmentDataManager.getAppointmentByID(appointmentID);
    }

    /**
     * Updates the email address for the currently logged-in patient.
     * This method validates and updates the patient's email address in their
     * user profile, maintaining current contact information.
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
     * Updates the password for the currently logged-in patient.
     * This method securely updates the patient's password, ensuring
     * proper authentication and account security.
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
     * Updates the contact number for the currently logged-in patient.
     * This method validates and updates the patient's contact number,
     * ensuring current contact information is maintained.
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
     * Retrieves the outcome record for a specific appointment.
     * This method provides access to the detailed medical outcome of a completed
     * appointment, including diagnosis and treatment information.
     * 
     * @param appointmentID The unique identifier of the appointment
     * @return The Outcome object if found, null if no outcome exists
     */
    public Outcome getOutcomeByAppointmentID(String appointmentID) {
        Appointment appointment = getAppointmentByID(appointmentID);
        if (appointment != null && appointment.getOutcomeID() != null) {
            return outcomeDataManager.getOutcomeByID(appointment.getOutcomeID());
        }
        return null;
    }

    /**
     * Retrieves detailed information about a specific prescription.
     * This method provides access to prescription details including
     * medications, dosage, and instructions.
     * 
     * @param prescriptionID The unique identifier of the prescription to retrieve
     * @return The Prescription object if found, null if not found
     */
    public Prescription getPrescriptionByID(String prescriptionID) {
        return prescriptionDataManager.getPrescriptionByID(prescriptionID);
    }

    /**
     * Retrieves detailed information about a specific medication.
     * This method provides access to medication details including
     * name, dosage, and usage instructions.
     * 
     * @param medicationID The unique identifier of the medication to retrieve
     * @return The Medication object if found, null if not found
     */
    public Medication getMedicationByID(String medicationID) {
        return medicationDataManager.getMedicationByID(medicationID);
    }

    /**
     * Retrieves detailed information about a specific user.
     * This method provides access to user profile information including
     * personal details and contact information.
     * 
     * @param userID The unique identifier of the user to retrieve
     * @return The User object if found, null if not found
     */
    public User getUserByID(String userID) {
        return userDataManager.getUserByID(userID);
    }

    /**
     * Retrieves an appointment using its associated outcome ID.
     * This method provides a way to find appointment details when only
     * the outcome ID is known.
     * 
     * @param outcomeID The unique identifier of the outcome to search for
     * @return The associated Appointment object if found, null if not found
     */
    public Appointment getAppointmentByOutcomeID(String outcomeID) {
        return appointmentDataManager.getAllAppointments().stream()
                .filter(appointment -> outcomeID.equals(appointment.getOutcomeID()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Reschedules an existing appointment to a new time slot.
     * This method handles the complete rescheduling process including updating
     * the status of both the old and new time slots, and resetting the
     * appointment status to REQUESTED.
     * 
     * @param appointment The appointment to be rescheduled
     * @param newSlotID The unique identifier of the new time slot
     * @return true if rescheduling was successful, false if an error occurred
     */
    public boolean rescheduleAppointment(Appointment appointment, String newSlotID) {
        try {
            // Get the old slot to make it available again
            Slot oldSlot = slotDataManager.getSlotByID(appointment.getSlotID());
            if (oldSlot != null) {
                oldSlot.setStatus(Slot.SlotStatus.AVAILABLE);
                slotDataManager.updateSlot(oldSlot);
            }

            // Get the new slot and mark it as pending
            Slot newSlot = slotDataManager.getSlotByID(newSlotID);
            if (newSlot != null) {
                newSlot.setStatus(Slot.SlotStatus.PENDING);
                slotDataManager.updateSlot(newSlot);
            }

            // Update the appointment with the new slot ID and reset status to REQUESTED
            appointment.setSlotID(newSlotID);
            appointment.setStatus(Appointment.AppointmentStatus.REQUESTED);
            appointmentDataManager.updateAppointment(appointment);

            return true;
        } catch (Exception e) {
            System.err.println("Error rescheduling appointment: " + e.getMessage());
            return false;
        }
    }
}
