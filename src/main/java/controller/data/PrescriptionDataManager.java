package controller.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import entity.Prescription;

/**
 * Manages prescription data operations in the Hospital Management System (HMS).
 * This class handles all prescription-related data persistence and CRUD operations,
 * including loading from and saving to CSV files, filtering prescriptions, and managing
 * prescription details.
 * 
 * The class provides functionality for doctors to create prescriptions during appointments,
 * pharmacists to update prescription status when dispensing medications, and administrators
 * to track prescription records. It maintains a comprehensive record of all prescriptions
 * for tracking patient medications and pharmacy inventory management.
 *
 * @author Group 7
 * @version 1.0
 */
public class PrescriptionDataManager {

    private static final String PRESCRIPTION_FILE = "src/main/resources/data/prescriptions.csv";

    /**
     * List of prescriptions in the system.
     * Stores all prescription records loaded from CSV storage and maintains
     * the current state of prescriptions in memory.
     */
    private static List<Prescription> prescriptions;

    /**
     * Constructs a new PrescriptionDataManager with an empty list of prescriptions.
     * Initializes the prescriptions list to store prescription data loaded from CSV storage.
     * This constructor is called during system initialization to prepare for prescription management.
     */
    public PrescriptionDataManager() {
        prescriptions = new ArrayList<>();
    }

    /**
     * Loads prescription data from the CSV file into memory.
     * Reads and parses each line of the CSV file to create Prescription objects.
     * Validates data format and skips invalid entries while logging them for review.
     * Clears existing prescriptions before loading to ensure data consistency.
     *
     * @throws IOException If there's an error reading from the prescriptions CSV file
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
     * Saves all prescription data from memory to the CSV file.
     * Writes the complete list of prescriptions to persistent storage.
     * Includes a header line with column names for data structure clarity.
     *
     * @throws IOException If there's an error writing to the prescriptions CSV file
     */
    public void savePrescriptionsToCSV() throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(PRESCRIPTION_FILE))) {
            // Write header
            bw.write("prescriptionID,appointmentID,medicationID,quantity,status,notes");
            bw.newLine();

            for (Prescription prescription : prescriptions) {
                // Changed from %.2f to %d since quantity is an integer
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
     * Adds a new prescription to the system's records.
     * Validates that no duplicate prescription ID exists before adding.
     * Used by doctors to create new prescriptions during appointments.
     *
     * @param prescription The Prescription object containing the new prescription details
     * @throws IllegalArgumentException If a prescription with the same ID already exists
     */
    public void addPrescription(Prescription prescription) throws IllegalArgumentException {
        if (getPrescriptionByID(prescription.getPrescriptionID()) != null) {
            throw new IllegalArgumentException("Prescription with ID " + prescription.getPrescriptionID() + " already exists.");
        }
        prescriptions.add(prescription);
    }

    /**
     * Retrieves a copy of all prescription records in the system.
     * Returns a new list to preserve encapsulation of the internal prescriptions list.
     * Used for displaying complete prescription records to authorized users.
     *
     * @return A new List containing all Prescription objects in the system
     */
    public List<Prescription> getPrescriptions() {
        return new ArrayList<>(prescriptions); // Return a copy to preserve encapsulation
    }

    /**
     * Retrieves a specific prescription record by its unique identifier.
     * Searches through all prescription records to find an exact ID match.
     * Returns null if no matching prescription is found.
     *
     * @param prescriptionID The unique identifier of the prescription to retrieve
     * @return The matching Prescription object if found, null otherwise
     */
    public Prescription getPrescriptionByID(String prescriptionID) {
        return prescriptions.stream()
                .filter(prescription -> prescription.getPrescriptionID().equals(prescriptionID))
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves prescriptions matching specified filter criteria.
     * Supports filtering by status, appointment ID, and medication ID.
     * Case-insensitive matching is used for all string comparisons.
     *
     * @param filters Map containing filter criteria where key is the field name and value is the filter value
     * @return List of Prescription objects matching all specified filter criteria
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
     * Updates an existing prescription's information in the system.
     * Locates the existing prescription by ID and replaces it with the updated version.
     * Used by pharmacists to update prescription status when dispensing medications.
     *
     * @param updatedPrescription The Prescription object containing the updated information
     * @throws IllegalArgumentException If no prescription with the specified ID exists
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
     * Removes a prescription from the system's records.
     * Searches for and removes the prescription matching the specified ID.
     * Used for removing cancelled or erroneous prescription entries.
     *
     * @param prescriptionID The unique identifier of the prescription to remove
     * @throws IllegalArgumentException If no prescription with the specified ID exists
     */
    public void removePrescription(String prescriptionID) throws IllegalArgumentException {
        if (!prescriptions.removeIf(prescription -> prescription.getPrescriptionID().equals(prescriptionID))) {
            throw new IllegalArgumentException("Prescription with ID " + prescriptionID + " not found.");
        }
    }
}
