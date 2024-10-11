package controller.data;

import entity.Medication;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Manages medication data operations including loading from and saving to CSV files,
 * as well as basic CRUD operations for medications in the Hospital Management System.
 */
public class MedicationDataManager {

    private static final String MEDICATION_FILE = "src/main/resources/data/medications.csv";

    /**
     * List of medications in the system.
     */
    private static List<Medication> medications;

    /**
     * Constructs a new MedicationDataManager with an empty list of medications.
     */
    public MedicationDataManager() {
        medications = new ArrayList<>();
    }

    /**
     * Loads medication data from a CSV file.
     * @throws IOException If there's an error reading the file.
     */
    public void loadMedicationsFromCSV() throws IOException {
        medications.clear(); // Clear existing medications before loading
        System.out.println("\n[DEV] Loading medications: " + MEDICATION_FILE);
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
     * Saves medication data to a CSV file.
     * @param fileName The name of the CSV file to save data to.
     * @throws IOException If there's an error writing to the file.
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
     * [CREATE] Adds a new medication to the system.
     * @param medication The Medication object to add.
     * @throws IllegalArgumentException If the medication already exists.
     */
    public void addMedication(Medication medication) throws IllegalArgumentException {
        if (getMedicationByID(medication.getMedicationID()) != null) {
            throw new IllegalArgumentException("Medication with ID " + medication.getMedicationID() + " already exists.");
        }
        medications.add(medication);
    }

    /**
     * [READ] Retrieves the list of all medications.
     * @return List of Medication objects.
     */
    public List<Medication> getMedications() {
        return new ArrayList<>(medications); // Return a copy to preserve encapsulation
    }

    /**
     * [READ] Retrieves a medication by its ID.
     * @param medicationID The ID of the medication to retrieve.
     * @return The Medication object if found, null otherwise.
     */
    public Medication getMedicationByID(String medicationID) {
        return medications.stream()
                .filter(medication -> medication.getMedicationID().equals(medicationID))
                .findFirst()
                .orElse(null);
    }

    /**
     * [READ] Retrieves a filtered list of medications based on specified criteria.
     * @param filters Map of filter criteria (e.g., name, stockLevel, etc.)
     * @return List of Medication objects matching the filter criteria
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
     * [UPDATE] Updates an existing medication's information.
     * @param updatedMedication The Medication object with updated information.
     * @throws IllegalArgumentException If the medication doesn't exist.
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
     * [DELETE] Removes a medication from the system.
     * @param medicationID The ID of the medication to remove.
     * @throws IllegalArgumentException If the medication doesn't exist.
     */
    public void removeMedication(String medicationID) throws IllegalArgumentException {
        if (!medications.removeIf(medication -> medication.getMedicationID().equals(medicationID))) {
            throw new IllegalArgumentException("Medication with ID " + medicationID + " not found.");
        }
    }
}