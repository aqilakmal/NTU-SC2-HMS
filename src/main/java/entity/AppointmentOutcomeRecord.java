package entity;

import java.util.List;

/**
 * Records the outcome of an appointment in the Hospital Management System.
 */
public class AppointmentOutcomeRecord {
    
    /**
     * The  appointment associated with this outcome record.
     */
    private Appointment appointment;
    
    /**
     * The type of service provided during the appointment.
     */
    private String serviceProvided;
    
    /**
     * List of medications prescribed during the appointment.
     */
    private List<Prescription> prescribedMedications;
    
    /**
     * Notes from the consultation.
     */
    private String consultationNotes;
    
    /**
     * Constructor for AppointmentOutcomeRecord.
     * @param appointment The appointment associated with this outcome record
     * @param serviceProvided The type of service provided during the appointment
     * @param prescribedMedications The list of medications prescribed during the appointment
     * @param consultationNotes The notes from the consultation
     */
    public AppointmentOutcomeRecord(Appointment appointment, String serviceProvided, List<Prescription> prescribedMedications, String consultationNotes) {
        this.appointment = appointment;
        this.serviceProvided = serviceProvided;
        this.prescribedMedications = prescribedMedications;
        this.consultationNotes = consultationNotes;
    }
    
    /**
     * Get the appointment associated with this outcome record.
     * @return The appointment associated with this outcome record
     */
    public Appointment getAppointment() {
        return appointment;
    }
    
    /**
     * Get the type of service provided during the appointment.
     * @return The type of service provided during the appointment
     */
    public String getServiceProvided() {
        return serviceProvided;
    }
    
    /**
     * Get the list of medications prescribed during the appointment.
     * @return The list of medications prescribed during the appointment
     */
    public List<Prescription> getPrescribedMedications() {
        return prescribedMedications;
    }
    
    /**
     * Get the notes from the consultation.
     * @return The notes from the consultation
     */
    public String getConsultationNotes() {
        return consultationNotes;
    }

    /**
     * Set the appointment associated with this outcome record.
     * @param appointment The appointment associated with this outcome record
     */
    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    /**
     * Set the type of service provided during the appointment.
     * @param serviceProvided The type of service provided during the appointment
     */
    public void setServiceProvided(String serviceProvided) {
        this.serviceProvided = serviceProvided;
    }
    
    /**
     * Set the list of medications prescribed during the appointment.
     * @param prescribedMedications The list of medications prescribed during the appointment
     */
    public void setPrescribedMedications(List<Prescription> prescribedMedications) {
        this.prescribedMedications = prescribedMedications;
    }
    
    /**
     * Set the notes from the consultation.
     * @param consultationNotes The notes from the consultation
     */
    public void setConsultationNotes(String consultationNotes) {
        this.consultationNotes = consultationNotes;
    }
}