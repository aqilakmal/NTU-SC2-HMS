package controller;

import entity.*;
import java.util.List;

/**
 * Manages appointment-related operations in the Hospital Management System.
 */
public class AppointmentController {
    
    /**
     * Retrieves available time slots for a specific doctor.
     * @param doctorID The unique identifier of the doctor
     * @return List of available TimeSlot objects
     */
    public List<TimeSlot> viewAvailableSlots(String doctorID) {
        // TODO: Implement logic to retrieve available slots
        return null;
    }
    
    /**
     * Schedules a new appointment.
     * @param patientID The unique identifier of the patient
     * @param doctorID The unique identifier of the doctor
     * @param slot The selected time slot for the appointment
     * @throws AppointmentException if the slot is not available or there's a scheduling conflict
     */
    public void scheduleAppointment(String patientID, String doctorID, TimeSlot slot) throws AppointmentException {
        // TODO: Implement appointment scheduling logic
    }
    
    /**
     * Reschedules an existing appointment.
     * @param appointmentID The unique identifier of the appointment to reschedule
     * @param newSlot The new time slot for the appointment
     * @throws AppointmentException if the new slot is not available or there's a scheduling conflict
     */
    public void rescheduleAppointment(String appointmentID, TimeSlot newSlot) throws AppointmentException {
        // TODO: Implement appointment rescheduling logic
    }
    
    /**
     * Cancels an existing appointment.
     * @param appointmentID The unique identifier of the appointment to cancel
     * @throws AppointmentException if the appointment cannot be cancelled
     */
    public void cancelAppointment(String appointmentID) throws AppointmentException {
        // TODO: Implement appointment cancellation logic
    }
    
    /**
     * Updates the status of an appointment.
     * @param appointmentID The unique identifier of the appointment
     * @param status The new status to set for the appointment
     * @throws AppointmentException if the status update is invalid
     */
    public void updateAppointmentStatus(String appointmentID, Appointment.AppointmentStatus status) throws AppointmentException {
        // TODO: Implement appointment status update logic
    }
    
    /**
     * Retrieves appointments for a specific user (patient or doctor).
     * @param userID The unique identifier of the user
     * @return List of Appointment objects for the user
     */
    public List<Appointment> getAppointmentsForUser(String userID) {
        // TODO: Implement logic to retrieve appointments for a user
        return null;
    }

    /**
     * AppointmentException extends Exception to handle exceptions related to appointments.
     */
    public class AppointmentException extends Exception {
      
        /**
         * Constructor for AppointmentException with a message.
         * @param message The message to display when the exception is thrown.
         */
        public AppointmentException(String message) {
              super(message);
          }

        /**
         * Constructor for AppointmentException with a message and a cause.
         * @param message The message to display when the exception is thrown.
         * @param cause The cause of the exception.
         */
        public AppointmentException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}