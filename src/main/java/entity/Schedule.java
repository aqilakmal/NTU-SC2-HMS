package entity;

import java.util.List;

/**
 * Represents a doctor's availability and appointments in the Hospital Management System.
 */
public class Schedule {
    
    /**
     * The id of the doctor associated with this schedule.
     */
    private String doctorID;
    
    /**
     * List of available time slots in the schedule.
     */
    private List<TimeSlot> availableSlots;
    
    /**
     * List of booked appointments in the schedule.
     */
    private List<Appointment> bookedAppointments;

    /**
     * Constructor for Schedule.
     * @param doctorID The id of the doctor associated with this schedule.
     */
    public Schedule(String doctorID) {
        this.doctorID = doctorID;
    }

    /**
     * Get the id of the doctor associated with this schedule.
     * @return The doctor ID.
     */
    public String getDoctorID() {
        return doctorID;
    }

    /**
     * Get the list of available time slots in the schedule.
     * @return The list of available time slots.
     */
    public List<TimeSlot> getAvailableSlots() {
        return availableSlots;
    }

    /**
     * Get the list of booked appointments in the schedule.
     * @return The list of booked appointments.
     */
    public List<Appointment> getBookedAppointments() {
        return bookedAppointments;
    }
    
    /**
     * Set the id of the doctor associated with this schedule.
     * @param doctorID The doctor ID to set.
     */
    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }
    
    /**
     * Set the list of available time slots in the schedule.
     * @param availableSlots The list of available time slots to set.
     */
    public void setAvailableSlots(List<TimeSlot> availableSlots) {
        this.availableSlots = availableSlots;
    }

    /**
     * Set the list of booked appointments in the schedule.
     * @param bookedAppointments The list of booked appointments to set.
     */
    public void setBookedAppointments(List<Appointment> bookedAppointments) {
        this.bookedAppointments = bookedAppointments;
    }
}
