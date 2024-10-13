package controller;

import entity.*;
import controller.data.*;
import java.util.List;
/**
 * Controller class for managing patient-related operations in the Hospital Management System.
 */
public class PatientController {

    private AppointmentDataManager appointmentDataManager;
    private SlotDataManager slotDataManager;
    private DoctorController doctorController;
    private AuthenticationController authController;

    /**
     * Constructor for PatientController.
     * 
     * @param appointmentDataManager The AppointmentDataManager instance
     * @param slotDataManager The SlotDataManager instance
     * @param doctorController The DoctorController instance
     * @param authController The AuthenticationController instance
     */
    public PatientController(AppointmentDataManager appointmentDataManager,
            SlotDataManager slotDataManager, DoctorController doctorController, AuthenticationController authController) {
        this.appointmentDataManager = appointmentDataManager;
        this.slotDataManager = slotDataManager;
        this.doctorController = doctorController;
        this.authController = authController;
    }

    /**
     * Schedules an appointment for the current patient.
     * 
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

    public Slot getSlotByID(String slotID) {
        return slotDataManager.getSlotByID(slotID);
    }

    public List<Slot> getAvailableSlotsForDoctor(String doctorID) {
        return doctorController.getAvailableSlotsForDoctor(doctorID);
    }

    public Doctor getDoctorByID(String doctorID) {
        return doctorController.getDoctorByID(doctorID);
    }

    public List<Doctor> getAllDoctors() {
        return doctorController.getAllDoctors();
    }
}
