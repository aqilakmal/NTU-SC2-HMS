package controller;


import entity.*;
import controller.data.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.HashMap;
/**
 * Controller class for managing patient-related operations in the Hospital Management System.
 */
public class PatientController {

    private AppointmentDataManager appointmentDataManager;
    private SlotDataManager slotDataManager;
    private DoctorController doctorController;
    private AuthenticationController authController;
    private HistoryDataManager historyDataManager; 

    /**
     * Constructor for PatientController.
     * 
     * @param appointmentDataManager The AppointmentDataManager instance
     * @param slotDataManager The SlotDataManager instance
     * @param doctorController The DoctorController instance
     * @param authController The AuthenticationController instance
     * @param historyDataManager The historyDataManager instnace
     */
    public PatientController(AppointmentDataManager appointmentDataManager, HistoryDataManager historyDataManager,
            SlotDataManager slotDataManager, DoctorController doctorController, AuthenticationController authController) {
        this.appointmentDataManager = appointmentDataManager;
        this.slotDataManager = slotDataManager;
        this.doctorController = doctorController;
        this.authController = authController;
        this.historyDataManager = historyDataManager;
    }

    

    /**
     * Schedules an appointment for the current patient.
     * 
     * This method schedules an appointment by creating a new appointment object with the provided doctor and slot IDs.
     * It also updates the status of the selected slot to PENDING and stores the new appointment in the data manager.
     * @param doctorID The ID of the selected doctor
     * @param slotID The ID of the selected slot
     * @return true if the appointment was scheduled successfully, false otherwise
     */
    public boolean scheduleAppointment(String doctorID, String slotID) {
        try {
            // Generate a new appointment ID
            String appointmentID = generateAppointmentID();

            // LOG
            System.out.println(authController.getCurrentUser().getUserID());

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
        System.out.println("Controller: " + slotID);
        System.out.println(slotDataManager.getSlotByID(slotID));
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
     * Retrieves the medical history of the current patient.
     * 
     * @return A list of History objects representing the patient's medical history
     */
    public List<History> getPatientMedicalHistory() {
        return historyDataManager.getHistoriesByPatientID(authController.getCurrentUser().getUserID());
    }
    

/**
 * Retrieves the list of scheduled appointments for the current patient.
 * Filters out appoinments with status  CANCELLED or COMPELTED
 * @return the remaing appointment i.e. either CONFIRMED or REQUESTED
 */
    public List<Appointment> getScheduledAppointments() {
        String patientID = authController.getCurrentUser().getUserID();
        
        // Create a Map to hold filter criteria
        Map<String, String> filters = new HashMap<>();
        filters.put("patientID", patientID); // Add the patientID filter
    
        // Pass the filters map to the getFilteredAppointments method
        List<Appointment> allAppointments = appointmentDataManager.getFilteredAppointments(filters);
    
        // Filter out appointments with status CANCELED or COMPLETED
        return allAppointments.stream()
                .filter(appointment -> appointment.getStatus() != Appointment.AppointmentStatus.CANCELLED &&
                                       appointment.getStatus() != Appointment.AppointmentStatus.COMPLETED)
                .collect(Collectors.toList());
    }
/**
 * Retrieves the list of erquested appointments for the current patient.
 * Filters out appoinments and returns only appointments with the status REQUESTED
 * @return list of appointments with status REQUESTED
 */

    public List<Appointment> getreq() {
        String patientID = authController.getCurrentUser().getUserID();
        
        // Create a Map to hold filter criteria
        Map<String, String> filters = new HashMap<>();
        filters.put("patientID", patientID); // Add the patientID filter
    
        // Pass the filters map to the getFilteredAppointments method
        List<Appointment> allAppointments = appointmentDataManager.getFilteredAppointments(filters);
    
        // Filter out appointments with status CANCELED or COMPLETED
        return allAppointments.stream()
                .filter(appointment -> appointment.getStatus() == Appointment.AppointmentStatus.REQUESTED
                                       )
                .collect(Collectors.toList());
    }
/**
 * Retrieves the list of past appointments for the current patient.
 * Filters appointments to include only those with status COMPLETED or CANCELLED.
 * @return list of past appointments with status COMPLETED or CANCELLED 
 */

    public List<Appointment> getPastAppointments() {
        String patientID = authController.getCurrentUser().getUserID();
        
        // Create a Map to hold filter criteria
        Map<String, String> filters = new HashMap<>();
        filters.put("patientID", patientID); // Add the patientID filter
    
        // Retrieve appointments based on the patient ID
        List<Appointment> allAppointments = appointmentDataManager.getFilteredAppointments(filters);
    
        // Filter to include only appointments with status COMPLETED or CANCELED
        return allAppointments.stream()
                .filter(appointment -> appointment.getStatus() == Appointment.AppointmentStatus.COMPLETED ||
                                       appointment.getStatus() == Appointment.AppointmentStatus.CANCELLED)
                .collect(Collectors.toList());
    }
    

/**
 * Retrieves an appointment by its unique ID.
 * @param appointmentID The ID of the appointment to retrieve
 * @return  The appointment with the specified ID, or null if no such appointment exists
 */
    public Appointment getAppointmentByID(String appointmentID) {
        return appointmentDataManager.getAppointmentByID(appointmentID);
    }
  
/**
 * Updates the email address for the currently logged-in user.
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
 * @param newPassword The new password to set for the current user
 */

    public void updatePassword(String newEPassword) {
        User currentUser = authController.getCurrentUser();
        if (currentUser != null) {
            currentUser.updatePassword(newEPassword);
            System.out.println("Password address updated successfully.");
        } else {
            System.out.println("No user is currently logged in.");
        }
    }
/**
 * Updates the contact number for the currently logged-in user.
 *  @param newContact The new contact number to set for the current user
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
    

   
}
