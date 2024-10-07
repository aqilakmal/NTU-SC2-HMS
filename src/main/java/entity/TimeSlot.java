package entity;

import java.time.LocalDateTime;

/**
 * Represents a specific time slot in a schedule in the Hospital Management System.
 */
public class TimeSlot {
    
    /**
     * The start time of the time slot.
     */
    private LocalDateTime startTime;
    
    /**
     * The end time of the time slot.
     */
    private LocalDateTime endTime;
    
    /**
     * Indicates whether the time slot is available.
     */
    private boolean isAvailable;

    /**
     * Constructor for TimeSlot.
     * @param startTime The start time of the time slot.
     * @param endTime The end time of the time slot.
     * @param isAvailable Indicates whether the time slot is available.
     */
    public TimeSlot(LocalDateTime startTime, LocalDateTime endTime, boolean isAvailable) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.isAvailable = isAvailable;
    }

    /**
     * Get the start time of the time slot.
     * @return The start time.
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }
    
    /**
     * Get the end time of the time slot.
     * @return The end time.
     */
    public LocalDateTime getEndTime() {
        return endTime;
    }
    
    /**
     * Get the availability status of the time slot.
     * @return True if the time slot is available, false otherwise.
     */
    public boolean isAvailable() {
        return isAvailable;
    }
    
    /**
     * Set the availability status of the time slot.
     * @param isAvailable The availability status to set.
     */
    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
}