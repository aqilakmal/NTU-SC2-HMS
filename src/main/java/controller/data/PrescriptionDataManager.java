package controller.data;

import entity.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Manages prescription data operations including loading from and saving to CSV files,
 * as well as basic CRUD operations for prescriptions in the Hospital Management System.
 */
public class PrescriptionDataManager {

    private static final String PRESCRIPTION_FILE = "src/main/resources/data/prescriptions.csv";

    /**
     * List of prescriptions in the system.
     */
    private static List<Prescription> prescriptions;

    /**
     * Constructs a new PrescriptionDataManager with an empty list of prescriptions.
     */
    public PrescriptionDataManager() {
        prescriptions = new ArrayList<>();
    }

    /**
     * Loads prescription data from a CSV file.
     * @throws IOException If there's an error reading the file.
     */
    public void loadPrescriptionsFromCSV() throws IOException {
        prescriptions.clear(); // Clear existing prescriptions before loading
        System.out.println("\n[DEV] Loading: " + PRESCRIPTION_FILE);
        try (BufferedReader br = new BufferedReader(new FileReader(PRESCRIPTION_FILE))) {
            String line;
            br.readLine(); // Skip header line

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",", -1); // Use -1 to keep empty trailing fields
                if (values.length < 6) {
                    System.out.println("Skipping invalid line: " + line);
                    continue; // Skip invalid lines
                }

                String prescriptionID = values[0].trim();
                String appointmentID = values[1].trim();
                String medicationID = values[2].trim();
                int quantity = Integer.parseInt(values[3].trim());
                Prescription.PrescriptionStatus status = Prescription.PrescriptionStatus.valueOf(values[4].trim());
                String notes = values[5].trim();

                Prescription prescription = new Prescription(prescriptionID, appointmentID, medicationID, quantity, status, notes);
                prescriptions.add(prescription);
                System.out.println("[DEV] " + prescription); // Debug output
            }
        }
        System.out.println("[DEV] Total prescriptions loaded: " + prescriptions.size()); // Debug output
    }

    /**
     * Saves prescription data to a CSV file.
     * @throws IOException If there's an error writing to the file.
     */
    public void savePrescriptionsToCSV() throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(PRESCRIPTION_FILE))) {
            // Write header
            bw.write("prescriptionID,appointmentID,medicationID,quantity,status,notes");
            bw.newLine();

            for (Prescription prescription : prescriptions) {
                bw.write(String.format("%s,%s,%s,%d,%s,%s",
                    prescription.getPrescriptionID(),
                    prescription.getAppointmentID(),
                    prescription.getMedicationID(),
                    prescription.getQuantity(),
                    prescription.getStatus(),
                    prescription.getNotes()
                ));
                bw.newLine();
            }
        }
    }

    /**
     * [CREATE] Adds a new prescription to the system.
     * @param prescription The Prescription object to add.
     * @throws IllegalArgumentException If the prescription already exists.
     */
    public void addPrescription(Prescription prescription) throws IllegalArgumentException {
        if (getPrescriptionByID(prescription.getPrescriptionID()) != null) {
            throw new IllegalArgumentException("Prescription with ID " + prescription.getPrescriptionID() + " already exists.");
        }
        prescriptions.add(prescription);
    }

    /**
     * [READ] Retrieves the list of all prescriptions.
     * @return List of Prescription objects.
     */
    public List<Prescription> getPrescriptions() {
        return new ArrayList<>(prescriptions); // Return a copy to preserve encapsulation
    }

    /**
     * [READ] Retrieves a prescription by its ID.
     * @param prescriptionID The ID of the prescription to retrieve.
     * @return The Prescription object if found, null otherwise.
     */
    public Prescription getPrescriptionByID(String prescriptionID) {
        return prescriptions.stream()
                .filter(prescription -> prescription.getPrescriptionID().equals(prescriptionID))
                .findFirst()
                .orElse(null);
    }

    /**
     * [READ] Retrieves a filtered list of prescriptions based on specified criteria.
     * @param filters Map of filter criteria (e.g., status, appointmentID, etc.)
     * @return List of Prescription objects matching the filter criteria
     */
    public List<Prescription> getFilteredPrescriptions(Map<String, String> filters) {
        return prescriptions.stream()
            .filter(prescription -> filters.entrySet().stream()
                .allMatch(entry -> {
                    String key = entry.getKey().toLowerCase();
                    String value = entry.getValue().toLowerCase();
                    switch (key) {
                        case "status":
                            return prescription.getStatus().toString().toLowerCase().equals(value);
                        case "appointmentid":
                            return prescription.getAppointmentID().toLowerCase().equals(value);
                        case "medicationid":
                            return prescription.getMedicationID().toLowerCase().equals(value);
                        // Add more filter criteria as needed
                        default:
                            return true;
                    }
                }))
            .collect(Collectors.toList());
    }

    /**
     * [UPDATE] Updates an existing prescription's information.
     * @param updatedPrescription The Prescription object with updated information.
     * @throws IllegalArgumentException If the prescription doesn't exist.
     */
    public void updatePrescription(Prescription updatedPrescription) throws IllegalArgumentException {
        for (int i = 0; i < prescriptions.size(); i++) {
            if (prescriptions.get(i).getPrescriptionID().equals(updatedPrescription.getPrescriptionID())) {
                prescriptions.set(i, updatedPrescription);
                return;
            }
        }
        throw new IllegalArgumentException("Prescription with ID " + updatedPrescription.getPrescriptionID() + " not found.");
    }

    /**
     * [DELETE] Removes a prescription from the system.
     * @param prescriptionID The ID of the prescription to remove.
     * @throws IllegalArgumentException If the prescription doesn't exist.
     */
    public void removePrescription(String prescriptionID) throws IllegalArgumentException {
        if (!prescriptions.removeIf(prescription -> prescription.getPrescriptionID().equals(prescriptionID))) {
            throw new IllegalArgumentException("Prescription with ID " + prescriptionID + " not found.");
        }
    }
}
