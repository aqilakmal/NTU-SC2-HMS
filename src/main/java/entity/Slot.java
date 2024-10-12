package entity;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Represents a specific time slot in a schedule in the Hospital Management System.
 */
public class Slot {

    /**
     * Enum representing the possible statuses of a slot.
     */
    public enum SlotStatus {
        AVAILABLE,  // Slot is available for booking
        BOOKED,     // Slot is booked by a patient
        COMPLETED,  // Slot has been used for an appointment
        REMOVED     // Slot has been removed by the doctor (soft delete)
    }

    /**
     * Unique identifier for the slot.
     */
    private String slotID;
    
    /**
     * The id of the doctor associated with this slot.
     */
    private String doctorID;

    /**
     * The date of the slot
     */
    private LocalDate date;

    /**
     * The start time of the slot.
     */
    private LocalTime startTime;

    /**
     * The end time of the slot.
     */
    private LocalTime endTime;

    /**
     * Indicates the current status of the slot.
     */
    private SlotStatus status;

    /**
     * Constructor for Slot.
     * @param slotID The unique identifier for the slot.
     * @param doctorID The id of the doctor associated with this slot.
     * @param date The date of the slot.
     * @param startTime The start time of the slot.
     * @param endTime The end time of the slot.
     * @param status The current status of the slot.
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
     * Get the unique identifier for the slot.
     * @return The slot ID.
     */
    public String getSlotID() {
        return slotID;
    }

    /**
     * Get the id of the doctor associated with this slot.
     * @return The doctor ID.
     */
    public String getDoctorID() {
        return doctorID;
    }

    /**
     * Get the date of the slot.
     * @return The date.
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Get the start time of the slot.
     * @return The start time.
     */
    public LocalTime getStartTime() {
        return startTime;
    }

    /**
     * Get the end time of the slot.
     * @return The end time.
     */
    public LocalTime getEndTime() {
        return endTime;
    }

    /**
     * Get the current status of the slot.
     * @return The current status of the slot.
     */
    public SlotStatus getStatus() {
        return status;
    }

    /**
     * Set the unique identifier for the slot.
     * @param slotID The slot ID to set.
     */
    public void setSlotID(String slotID) {
        this.slotID = slotID;
    }

    /**
     * Set the id of the doctor associated with this slot.
     * @param doctorID The doctor ID to set.
     */
    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    /**
     * Set the date of the slot.
     * @param date The date to set.
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Set the start time of the slot.
     * @param startTime The start time to set.
     */
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    /**
     * Set the end time of the slot.
     * @param endTime The end time to set.
     */
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    /**
     * Set the current status of the slot.
     * @param status The current status to set.
     */
    public void setStatus(SlotStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Slot{" + slotID + "," + doctorID + "," + date + "," + startTime + "," + endTime + "," + status + "}";
    }

}