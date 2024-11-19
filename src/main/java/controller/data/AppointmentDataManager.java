package controller.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import entity.Appointment;
import entity.Slot;

/**
 * Manages appointment data operations in the Hospital Management System (HMS).
 * This class handles all appointment-related data persistence and CRUD operations,
 * including loading from and saving to CSV files, filtering appointments, and
 * managing appointment details.
 * 
 * The class works closely with the SlotDataManager to maintain the relationship
 * between appointments and their associated time slots. It provides functionality
 * for patients to schedule, reschedule and cancel appointments, and for doctors
 * to manage their appointments.
 *
 * @author Group 7
 * @version 1.0
 */
public class AppointmentDataManager {

    private static final String APPOINTMENT_FILE = "src/main/resources/data/appointments.csv";

    private List<Appointment> appointments;
    private SlotDataManager slotDataManager;

    /**
     * Constructs a new AppointmentDataManager with an empty list of appointments.
     * Initializes the appointment list and sets up the connection with the
     * SlotDataManager for managing appointment time slots.
     *
     * @param slotDataManager The SlotDataManager instance to handle slot-related operations
     */
    public AppointmentDataManager(SlotDataManager slotDataManager) {
        this.appointments = new ArrayList<>();
        this.slotDataManager = slotDataManager;
    }

    /**
     * Loads appointment data from the CSV file into memory.
     * Clears any existing appointments before loading new data.
     * Each line in the CSV file represents one appointment with its complete details.
     * Prints debug information during the loading process.
     *
     * @throws IOException If there's an error reading the appointments CSV file
     */
    public void loadAppointmentsFromCSV() throws IOException {
        appointments.clear();

        System.out.println("\n[DEV] Loading: " + APPOINTMENT_FILE);
        try (BufferedReader br = new BufferedReader(new FileReader(APPOINTMENT_FILE))) {
            String line;
            br.readLine(); // Skip header line
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",", -1);
                if (values.length >= 5) {
                    Appointment appointment = new Appointment(
                            values[0].trim(), // appointmentID
                            values[1].trim(), // patientID
                            values[2].trim(), // doctorID
                            values[3].trim(), // slotID
                            Appointment.AppointmentStatus.valueOf(values[4].trim()), // status
                            values.length > 5 ? values[5].trim() : "" // outcomeID
                    );
                    appointments.add(appointment);
                    System.out.println("[DEV] " + appointment); // Debug output
                }
            }
        }
        System.out.println("[DEV] Loaded " + appointments.size() + " appointments."); // Debug output
    }

    /**
     * Saves all appointment data from memory to the CSV file.
     * Writes appointments in a structured format with headers.
     * Each appointment is written as a comma-separated line containing all
     * appointment details including ID, patient, doctor, slot, status and outcome.
     *
     * @throws IOException If there's an error writing to the appointments CSV file
     */
    public void saveAppointmentsToCSV() throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(APPOINTMENT_FILE))) {
            bw.write("appointmentID,patientID,doctorID,slotID,status,outcomeID");
            bw.newLine();
            for (Appointment appointment : appointments) {
                bw.write(String.format("%s,%s,%s,%s,%s,%s",
                        appointment.getAppointmentID(),
                        appointment.getPatientID(),
                        appointment.getDoctorID(),
                        appointment.getSlotID(),
                        appointment.getStatus(),
                        appointment.getOutcomeID()
                ));
                bw.newLine();
            }
        }
    }

    /**
     * Adds a new appointment to the system.
     * Validates that no duplicate appointment ID exists before adding.
     * Throws an exception if attempting to add an appointment with an existing ID.
     *
     * @param appointment The Appointment object to add to the system
     * @throws IllegalArgumentException If an appointment with the same ID already exists
     */
    public void addAppointment(Appointment appointment) throws IllegalArgumentException {
        if (getAppointmentByID(appointment.getAppointmentID()) != null) {
            throw new IllegalArgumentException("Appointment with ID " + appointment.getAppointmentID() + " already exists.");
        }
        appointments.add(appointment);
    }

    /**
     * Retrieves an appointment by its unique identifier.
     * Searches through all appointments to find an exact ID match.
     * Returns null if no matching appointment is found.
     *
     * @param appointmentID The unique identifier of the appointment to retrieve
     * @return The matching Appointment object, or null if not found
     */
    public Appointment getAppointmentByID(String appointmentID) {
        return appointments.stream().filter(a -> a.getAppointmentID().equals(appointmentID)).findFirst().orElse(null);
    }

    /**
     * Retrieves an appointment by its associated slot ID.
     * Searches through all appointments to find one matching the given slot.
     * Returns null if no appointment is found for the specified slot.
     *
     * @param slotID The ID of the time slot to search for
     * @return The Appointment object associated with the slot, or null if not found
     */
    public Appointment getAppointmentBySlotID(String slotID) {
        return appointments.stream().filter(a -> a.getSlotID().equals(slotID)).findFirst().orElse(null);
    }

    /**
     * Retrieves all appointments currently in the system.
     * Returns a new ArrayList to prevent external modification of the internal list.
     * Provides a complete view of all appointments regardless of their status.
     *
     * @return A new List containing all Appointment objects
     */
    public List<Appointment> getAllAppointments() {
        return new ArrayList<>(appointments);
    }

    /**
     * Retrieves appointments based on specified filter criteria.
     * Supports filtering by status, patient ID, doctor ID, and other attributes.
     * Returns an empty list if no appointments match the filter criteria.
     *
     * @param filters Map of filter criteria where key is the filter type and value is the filter value
     * @return List of Appointment objects matching all specified filter criteria
     */
    public List<Appointment> getFilteredAppointments(Map<String, String> filters) {
        return appointments.stream()
                .filter(appointment -> filters.entrySet().stream()
                .allMatch(entry -> {
                    String key = entry.getKey().toLowerCase();
                    String value = entry.getValue().toLowerCase();
                    switch (key) {
                        case "status":
                            return appointment.getStatus().toString().toLowerCase().equals(value);
                        case "patientid":
                            return appointment.getPatientID().toLowerCase().equals(value);
                        case "doctorid":
                            return appointment.getDoctorID().toLowerCase().equals(value);
                        // Add more filter criteria as needed
                        default:
                            return true;
                    }
                }))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves detailed information about a specific appointment.
     * Includes both the appointment details and its associated time slot information.
     * Returns null if either the appointment or its associated slot is not found.
     *
     * @param appointmentID The unique identifier of the appointment to retrieve details for
     * @return Map containing the appointment and slot objects, or null if not found
     */
    public Map<String, Object> getAppointmentDetails(String appointmentID) {
        Appointment appointment = appointments.stream()
                .filter(a -> a.getAppointmentID().equals(appointmentID))
                .findFirst()
                .orElse(null);

        if (appointment == null) {
            return null;
        }

        Slot slot = slotDataManager.getSlotByID(appointment.getSlotID());
        if (slot == null) {
            return null;
        }

        Map<String, Object> details = new HashMap<>();
        details.put("appointment", appointment);
        details.put("slot", slot);
        return details;
    }

    /**
     * Updates an existing appointment's information in the system.
     * Replaces the old appointment data with the updated version.
     * Maintains the same appointment ID while updating other fields.
     *
     * @param updatedAppointment The Appointment object containing the updated information
     * @throws IllegalArgumentException If no appointment with the specified ID exists
     */
    public void updateAppointment(Appointment updatedAppointment) throws IllegalArgumentException {
        for (int i = 0; i < appointments.size(); i++) {
            if (appointments.get(i).getAppointmentID().equals(updatedAppointment.getAppointmentID())) {
                appointments.set(i, updatedAppointment);
                return;
            }
        }
        throw new IllegalArgumentException("Slot with ID " + updatedAppointment.getAppointmentID() + " not found.");
    }

    /**
     * Retrieves all appointments associated with a specific patient.
     * Currently unimplemented and throws an UnsupportedOperationException.
     * Will be implemented in future versions to support patient appointment history.
     *
     * @param patientID The unique identifier of the patient
     * @return List of appointments associated with the specified patient
     * @throws UnsupportedOperationException As this method is not yet implemented
     */
    public List<Appointment> getAppointmentsByPatientID(String patientID) {
        throw new UnsupportedOperationException("Unimplemented method 'getAppointmentsByPatientID'");
    }
}
