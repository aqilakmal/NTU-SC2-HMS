package controller.data;

import entity.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Manages medication data operations in the Hospital Management System (HMS).
 * This class handles all medication-related data persistence and CRUD operations,
 * including loading from and saving to CSV files, filtering medications, and managing
 * inventory levels.
 * 
 * The class provides functionality for pharmacists to monitor medication stock levels,
 * administrators to manage the medication inventory, and doctors to view available
 * medications for prescriptions. It maintains a comprehensive record of all medications
 * in the system.
 *
 * @author Group 7
 * @version 1.0
 */
public class MedicationDataManager {

    private static final String MEDICATION_FILE = "src/main/resources/data/medications.csv";

    /**
     * List of medications in the system.
     * Contains all medications loaded from CSV storage.
     */
    private static List<Medication> medications;

    /**
     * Constructs a new MedicationDataManager with an empty list of medications.
     * Initializes the medications list to store medication data loaded from CSV storage.
     * This constructor is called during system initialization to prepare for medication management.
     */
    public MedicationDataManager() {
        medications = new ArrayList<>();
    }

    /**
     * Loads medication data from the CSV file into memory.
     * Clears any existing medications before loading new data.
     * Each line in the CSV file represents one medication with its complete details.
     * Prints debug information during the loading process.
     *
     * @throws IOException If there's an error reading the medications CSV file
     */
    public void loadMedicationsFromCSV() throws IOException {
        medications.clear(); // Clear existing medications before loading
        System.out.println("\n[DEV] Loading: " + MEDICATION_FILE);
        try (BufferedReader br = new BufferedReader(new FileReader(MEDICATION_FILE))) {
            String line;
            br.readLine(); // Skip header line

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",", -1); // Use -1 to keep empty trailing fields
                if (values.length < 4) {
                    System.out.println("Skipping invalid line: " + line);
                    continue; // Skip invalid lines
                }

                String medicationID = values[0].trim();
                String name = values[1].trim();
                int stockLevel = values[2].trim().isEmpty() ? 0 : Integer.parseInt(values[2].trim());
                int lowStockAlertLevel = values[3].trim().isEmpty() ? 0 : Integer.parseInt(values[3].trim());

                Medication medication = new Medication(medicationID, name, stockLevel, lowStockAlertLevel);
                medications.add(medication);
                System.out.println("[DEV] " + medication); // Debug output
            }
        }
        System.out.println("[DEV] Total medications loaded: " + medications.size()); // Debug output
    }

    /**
     * Saves the current state of all medications to the CSV file.
     * Writes medication data in a structured format with headers.
     * Ensures data persistence by saving all medication attributes.
     *
     * @throws IOException If there's an error writing to the medications CSV file
     */
    public void saveMedicationsToCSV() throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(MEDICATION_FILE))) {
            // Write header
            bw.write("medicationID,medicationName,stockLevel,lowStockAlertLevel");
            bw.newLine();

            for (Medication medication : medications) {
                StringBuilder sb = new StringBuilder();
                sb.append(medication.getMedicationID()).append(",");
                sb.append(medication.getName()).append(",");
                sb.append(medication.getStockLevel()).append(",");
                sb.append(medication.getLowStockAlertLevel());
                
                bw.write(sb.toString());
                bw.newLine();
            }
        }
    }

    /**
     * Adds a new medication to the system's inventory.
     * Validates that no duplicate medication ID exists before adding.
     * Used by administrators to add new medications to the system.
     *
     * @param medication The Medication object to add to the system
     * @throws IllegalArgumentException If a medication with the same ID already exists
     */
    public void addMedication(Medication medication) throws IllegalArgumentException {
        if (getMedicationByID(medication.getMedicationID()) != null) {
            throw new IllegalArgumentException("Medication with ID " + medication.getMedicationID() + " already exists.");
        }
        medications.add(medication);
    }

    /**
     * Retrieves a copy of all medications in the system.
     * Returns a new list to preserve encapsulation of the internal medications list.
     * Used for displaying complete medication inventory to authorized users.
     *
     * @return A new List containing all Medication objects in the system
     */
    public List<Medication> getMedications() {
        return new ArrayList<>(medications); // Return a copy to preserve encapsulation
    }

    /**
     * Retrieves a specific medication by its unique identifier.
     * Searches through all medications to find an exact ID match.
     * Used when accessing or updating a specific medication entry.
     *
     * @param medicationID The unique identifier of the medication to retrieve
     * @return The Medication object if found, null if no match exists
     */
    public Medication getMedicationByID(String medicationID) {
        return medications.stream()
                .filter(medication -> medication.getMedicationID().equals(medicationID))
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves a filtered list of medications based on specified criteria.
     * Supports filtering by medication name, stock level, and other attributes.
     * Used for searching and filtering medications based on various parameters.
     *
     * @param filters Map containing filter criteria where key is the field name and value is the filter value
     * @return List of Medication objects matching all specified filter criteria
     */
    public List<Medication> getFilteredMedications(Map<String, String> filters) {
        return medications.stream()
            .filter(medication -> filters.entrySet().stream()
                .allMatch(entry -> {
                    String key = entry.getKey().toLowerCase();
                    String value = entry.getValue().toLowerCase();
                    switch (key) {
                        case "name":
                            return medication.getName().toLowerCase().contains(value);
                        case "stocklevel":
                            return Integer.toString(medication.getStockLevel()).equals(value);
                        // Add more filter criteria as needed
                        default:
                            return true;
                    }
                }))
            .collect(Collectors.toList());
    }

    /**
     * Updates an existing medication's information in the system.
     * Locates the existing medication by ID and replaces it with the updated version.
     * Used by administrators to modify medication details or update stock levels.
     *
     * @param updatedMedication The Medication object containing the updated information
     * @throws IllegalArgumentException If no medication with the specified ID exists
     */
    public void updateMedication(Medication updatedMedication) throws IllegalArgumentException {
        for (int i = 0; i < medications.size(); i++) {
            if (medications.get(i).getMedicationID().equals(updatedMedication.getMedicationID())) {
                medications.set(i, updatedMedication);
                return;
            }
        }
        throw new IllegalArgumentException("Medication with ID " + updatedMedication.getMedicationID() + " not found.");
    }

    /**
     * Removes a medication from the system's inventory.
     * Searches for and removes the medication matching the specified ID.
     * Used for removing discontinued or obsolete medications from the system.
     *
     * @param medicationID The unique identifier of the medication to remove
     * @throws IllegalArgumentException If no medication with the specified ID exists
     */
    public void removeMedication(String medicationID) throws IllegalArgumentException {
        if (!medications.removeIf(medication -> medication.getMedicationID().equals(medicationID))) {
            throw new IllegalArgumentException("Medication with ID " + medicationID + " not found.");
        }
    }
}