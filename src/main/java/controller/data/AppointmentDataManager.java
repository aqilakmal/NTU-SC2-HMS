package controller.data;

import entity.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Manages appointment data operations including loading from and saving to CSV files,
 * as well as basic CRUD operations for appointments in the Hospital Management System.
 */
public class AppointmentDataManager {

    private static final String APPOINTMENT_FILE = "src/main/resources/data/appointments.csv";

    private List<Appointment> appointments;

    /**
     * Constructs a new AppointmentDataManager with an empty list of appointments.
     */
    public AppointmentDataManager() {
        this.appointments = new ArrayList<>();
    }

    /**
     * Loads appointment data from CSV file.
     * @throws IOException If there's an error reading the file.
     */
    public void loadAppointmentsFromCSV() throws IOException {
        appointments.clear();

        System.out.println("\n[DEV] Loading appointments: " + APPOINTMENT_FILE);
        try (BufferedReader br = new BufferedReader(new FileReader(APPOINTMENT_FILE))) {
            String line;
            br.readLine(); // Skip header line
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",", -1); // Use -1 to keep empty trailing fields
                if (values.length >= 5) {
                    Appointment appointment = new Appointment(
                        values[0].trim(), // appointmentID
                        values[1].trim(), // patientID
                        values[2].trim(), // doctorID
                        LocalDateTime.parse(values[3].trim()), // dateTime
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
     * @throws IOException If there's an error writing to the file.
     */
    public void saveAppointmentsToCSV() throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(APPOINTMENT_FILE))) {
            bw.write("appointmentID,patientID,doctorID,appointmentDateTime,status");
            bw.newLine();
            for (Appointment appointment : appointments) {
                bw.write(String.format("%s,%s,%s,%s,%s",
                    appointment.getAppointmentID(),
                    appointment.getPatientID(),
                    appointment.getDoctorID(),
                    appointment.getDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                    appointment.getStatus()
                ));
                bw.newLine();
            }
        }
    }

    /**
     * Retrieves all appointments.
     * @return List of all Appointment objects.
     */
    public List<Appointment> getAllAppointments() {
        return new ArrayList<>(appointments);
    }

    /**
     * Retrieves a filtered list of appointments based on specified criteria.
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
}