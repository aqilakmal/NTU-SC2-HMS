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
 * Manages appointment data operations including loading from and saving to CSV
 * files, as well as basic CRUD operations for appointments in the Hospital
 * Management System.
 */
public class AppointmentDataManager {

    private static final String APPOINTMENT_FILE = "src/main/resources/data/appointments.csv";

    private List<Appointment> appointments;
    private SlotDataManager slotDataManager;

    /**
     * Constructs a new AppointmentDataManager with an empty list of
     * appointments.
     */
    public AppointmentDataManager(SlotDataManager slotDataManager) {
        this.appointments = new ArrayList<>();
        this.slotDataManager = slotDataManager;
    }

    /**
     * Loads appointment data from CSV file.
     *
     * @throws IOException If there's an error reading the file.
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
     * Saves appointment data to CSV file.
     *
     * @throws IOException If there's an error writing to the file.
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
     * [CREATE] Adds a new appointment to the list.
     *
     * @param appointment The Appointment object to add.
     */
    public void addAppointment(Appointment appointment) throws IllegalArgumentException {
        if (getAppointmentByID(appointment.getAppointmentID()) != null) {
            throw new IllegalArgumentException("Appointment with ID " + appointment.getAppointmentID() + " already exists.");
        }
        appointments.add(appointment);
    }

    /**
     * [READ] Retrieves an appointment by its ID.
     *
     * @param appointmentID The ID of the appointment to retrieve.
     * @return The Appointment object with the specified ID, or null if not
     * found.
     */
    public Appointment getAppointmentByID(String appointmentID) {
        return appointments.stream().filter(a -> a.getAppointmentID().equals(appointmentID)).findFirst().orElse(null);
    }

    /**
     * [READ] Retrieves an appointment by its ID.
     *
     * @param slotID The ID of the appointment to retrieve.
     * @return The Appointment object with the specified ID, or null if not
     * found.
     */
    public Appointment getAppointmentBySlotID(String slotID) {
        return appointments.stream().filter(a -> a.getSlotID().equals(slotID)).findFirst().orElse(null);
    }

    /**
     * [READ] Retrieves all appointments.
     *
     * @return List of all Appointment objects.
     */
    public List<Appointment> getAllAppointments() {
        return new ArrayList<>(appointments);
    }

    /**
     * [READ] Retrieves a filtered list of appointments based on specified
     * criteria.
     *
     * @param filters Map of filter criteria (e.g., status, date range, etc.)
     * @return List of Appointment objects matching the filter criteria
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
     * [READ] Retrieves appointment details including slot information.
     *
     * @param appointmentID The ID of the appointment to retrieve.
     * @return A map containing appointment and slot details, or null if not
     * found.
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
     * [UPDATE] Updates an existing slot's information.
     *
     * @param updatedSlot The Slot object with updated information.
     * @throws IllegalArgumentException If the slot doesn't exist.
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

    public List<Appointment> getAppointmentsByPatientID(String patientID) {
        throw new UnsupportedOperationException("Unimplemented method 'getAppointmentsByPatientID'");
    }
}
