package entity;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Represents a specific time slot in a schedule in the Hospital Management System.
 * Slots are used to track doctor availability and manage patient appointments.
 * 
 * Key components include:
 * - Unique slot identifier
 * - Associated doctor ID
 * - Date and time range of availability
 * - Current status (available, pending, booked, etc.)
 * 
 * The Slot class provides the foundation for the appointment scheduling system,
 * allowing doctors to set their availability and patients to book appointments
 * within those available time slots.
 *
 * @author Group 7
 * @version 1.0
 */
public class Slot {

    /**
     * Enum representing the possible statuses of a slot.
     * Used to track the current state of appointment slots in the system.
     * Supports multiple states to handle the complete lifecycle of a slot
     * from creation through completion or removal.
     */
    public enum SlotStatus {
        AVAILABLE,  // Slot is available for booking
        PENDING,    // Slot is pending for a doctor to confirm
        BOOKED,     // Slot is booked by a patient
        COMPLETED,  // Slot has been used for an appointment
        REMOVED     // Slot has been removed by the doctor (soft delete)
    }

    /**
     * Unique identifier for the slot.
     * Used to track and reference specific slots in the system.
     */
    private String slotID;
    
    /**
     * The id of the doctor associated with this slot.
     * References the specific doctor who will be available during this time slot.
     */
    private String doctorID;

    /**
     * The date of the slot.
     * Represents the calendar date when this slot is scheduled.
     */
    private LocalDate date;

    /**
     * The start time of the slot.
     * Represents the time when the doctor's availability begins.
     */
    private LocalTime startTime;

    /**
     * The end time of the slot.
     * Represents the time when the doctor's availability ends.
     */
    private LocalTime endTime;

    /**
     * Indicates the current status of the slot.
     * Tracks whether the slot is available, booked, or in another state.
     */
    private SlotStatus status;

    /**
     * Constructor for creating a new Slot instance in the system.
     * Initializes a time slot with all required details including doctor
     * availability and scheduling information. Validates and sets up the
     * slot record for appointment management.
     *
     * @param slotID The unique identifier for the slot
     * @param doctorID The id of the doctor associated with this slot
     * @param date The date of the slot
     * @param startTime The start time of the slot
     * @param endTime The end time of the slot
     * @param status The current status of the slot
     */
    public Slot(String slotID, String doctorID, LocalDate date, LocalTime startTime, LocalTime endTime, SlotStatus status) {
        this.slotID = slotID;
        this.doctorID = doctorID;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    /**
     * Retrieves the unique identifier for this slot.
     * This method provides access to the slot's tracking ID which is used
     * throughout the system to reference and manage this specific time slot.
     *
     * @return The unique slot ID string
     */
    public String getSlotID() {
        return slotID;
    }

    /**
     * Retrieves the identifier of the doctor associated with this slot.
     * This method provides access to the specific doctor ID that is
     * assigned to be available during this time slot.
     *
     * @return The doctor ID string
     */
    public String getDoctorID() {
        return doctorID;
    }

    /**
     * Retrieves the date when this slot is scheduled.
     * This method provides access to the calendar date when the doctor
     * will be available for appointments in this time slot.
     *
     * @return The scheduled date
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Retrieves the start time of the slot.
     * This method provides access to the specific time when the doctor's
     * availability begins for this appointment slot.
     *
     * @return The start time
     */
    public LocalTime getStartTime() {
        return startTime;
    }

    /**
     * Retrieves the end time of the slot.
     * This method provides access to the specific time when the doctor's
     * availability ends for this appointment slot.
     *
     * @return The end time
     */
    public LocalTime getEndTime() {
        return endTime;
    }

    /**
     * Retrieves the current status of the slot.
     * This method provides access to the current state of the slot,
     * indicating whether it is available, booked, or in another state.
     *
     * @return The current status of the slot
     */
    public SlotStatus getStatus() {
        return status;
    }

    /**
     * Updates the unique identifier for this slot.
     * This method allows the system to modify the tracking ID used
     * to reference this specific time slot.
     *
     * @param slotID The new slot ID to set
     */
    public void setSlotID(String slotID) {
        this.slotID = slotID;
    }

    /**
     * Updates the doctor associated with this slot.
     * This method allows the system to reassign the slot to a different
     * doctor when necessary for schedule management.
     *
     * @param doctorID The new doctor ID to set
     */
    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    /**
     * Updates the date of the slot.
     * This method allows the system to reschedule the slot to a
     * different calendar date when necessary.
     *
     * @param date The new date to set
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Updates the start time of the slot.
     * This method allows the system to modify when the doctor's
     * availability begins for this appointment slot.
     *
     * @param startTime The new start time to set
     */
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    /**
     * Updates the end time of the slot.
     * This method allows the system to modify when the doctor's
     * availability ends for this appointment slot.
     *
     * @param endTime The new end time to set
     */
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    /**
     * Updates the current status of the slot.
     * This method allows the system to track changes in the slot's state
     * as it moves through the appointment lifecycle.
     *
     * @param status The new status to set
     */
    public void setStatus(SlotStatus status) {
        this.status = status;
    }

    /**
     * Generates a string representation of the slot.
     * This method creates a formatted string containing all relevant
     * slot information for display or logging purposes.
     *
     * @return A string representation of the complete slot record
     */
    @Override
    public String toString() {
        return "Slot{" + slotID + "," + doctorID + "," + date + "," + startTime + "," + endTime + "," + status + "}";
    }

}