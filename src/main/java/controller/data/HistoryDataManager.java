package controller.data;

import entity.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Manages medical history data operations in the Hospital Management System (HMS).
 * This class handles all medical history-related data persistence and CRUD operations,
 * including loading from and saving to CSV files, filtering histories, and managing
 * history details.
 * 
 * The class provides functionality for doctors to record and update patient medical
 * histories, and for patients to view their own medical records. It maintains a
 * comprehensive record of diagnoses, treatments, and medical history details.
 *
 * @author Group 7
 * @version 1.0
 */
public class HistoryDataManager {

    private static final String HISTORY_FILE = "src/main/resources/data/history.csv";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * List of medical history records in the system.
     * Contains all patient medical histories loaded from CSV storage.
     */
    private static List<History> histories;

    /**
     * Constructs a new HistoryDataManager with an empty list of medical history records.
     * Initializes the histories list to store medical history data loaded from CSV storage.
     * This constructor is called during system initialization to prepare for history management.
     */
    public HistoryDataManager() {
        histories = new ArrayList<>();
    }

    /**
     * Loads medical history data from the CSV file into memory.
     * Clears any existing histories before loading new data.
     * Each line in the CSV file represents one medical history record with its complete details.
     * Prints debug information during the loading process.
     *
     * @throws IOException If there's an error reading the histories CSV file
     */
    public void loadHistoriesFromCSV() throws IOException {
        histories.clear(); // Clear existing histories before loading
        System.out.println("\n[DEV] Loading: " + HISTORY_FILE);
        try (BufferedReader br = new BufferedReader(new FileReader(HISTORY_FILE))) {
            String line;
            br.readLine(); // Skip header line

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",", -1); // Use -1 to keep empty trailing fields
                if (values.length < 5) {
                    System.out.println("Skipping invalid line: " + line);
                    continue; // Skip invalid lines
                }

                String historyID = values[0].trim();
                String patientID = values[1].trim();
                LocalDate diagnosisDate = LocalDate.parse(values[2].trim(), DATE_FORMATTER);
                String diagnosis = values[3].trim();
                String treatment = values[4].trim();

                History history = new History(historyID, patientID, diagnosisDate, diagnosis, treatment);
                histories.add(history);
                System.out.println("[DEV] " + history); // Debug output
            }
        }
        System.out.println("[DEV] Total histories loaded: " + histories.size()); // Debug output
    }

    /**
     * Saves all medical history records from memory back to the CSV file.
     * Writes each history record as a comma-separated line in the file.
     * Creates a new file if it doesn't exist, or overwrites the existing file.
     * Includes a header line with column names.
     *
     * @throws IOException If there's an error writing to the histories CSV file
     */
    public void saveHistoriesToCSV() throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(HISTORY_FILE))) {
            // Write header
            bw.write("historyID,patientID,diagnosisDate,diagnosis,treatment");
            bw.newLine();

            for (History history : histories) {
                StringBuilder sb = new StringBuilder();
                sb.append(history.getHistoryID()).append(",");
                sb.append(history.getPatientID()).append(",");
                sb.append(history.getDiagnosisDate().format(DATE_FORMATTER)).append(",");
                sb.append(history.getDiagnosis()).append(",");
                sb.append(history.getTreatment());
                
                bw.write(sb.toString());
                bw.newLine();
            }
        }
    }

    /**
     * Adds a new medical history record to the system.
     * Validates that no duplicate history ID exists before adding.
     * Used by doctors to create new medical history entries for patients.
     *
     * @param history The History object to add to the system
     * @throws IllegalArgumentException If a history record with the same ID already exists
     */
    public void addHistory(History history) throws IllegalArgumentException {
        if (getHistoryByID(history.getHistoryID()) != null) {
            throw new IllegalArgumentException("History record with ID " + history.getHistoryID() + " already exists.");
        }
        histories.add(history);
    }

    /**
     * Retrieves a copy of all medical history records in the system.
     * Returns a new list to preserve encapsulation of the internal histories list.
     * Used for displaying complete medical history records to authorized users.
     *
     * @return A new List containing all History objects in the system
     */
    public List<History> getHistories() {
        return new ArrayList<>(histories); // Return a copy to preserve encapsulation
    }

    /**
     * Retrieves a specific medical history record by its unique identifier.
     * Searches through all history records to find an exact ID match.
     * Used when accessing or updating a specific medical history entry.
     *
     * @param historyID The unique identifier of the history record to retrieve
     * @return The matching History object if found, null if no match exists
     */
    public History getHistoryByID(String historyID) {
        return histories.stream()
                .filter(history -> history.getHistoryID().equals(historyID))
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves all medical history records associated with a specific patient.
     * Filters the complete history list to find all records matching the patient ID.
     * Used to display a patient's complete medical history.
     *
     * @param patientID The unique identifier of the patient whose history to retrieve
     * @return List of History objects belonging to the specified patient
     */
    public List<History> getHistoriesByPatientID(String patientID) {
        return histories.stream()
                .filter(history -> history.getPatientID().equals(patientID))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a filtered list of medical history records based on specified criteria.
     * Supports filtering by patient ID, diagnosis, and other attributes.
     * Used for searching and filtering medical histories based on various parameters.
     *
     * @param filters Map containing filter criteria where key is the field name and value is the filter value
     * @return List of History objects matching all specified filter criteria
     */
    public List<History> getFilteredHistories(Map<String, String> filters) {
        return histories.stream()
            .filter(history -> filters.entrySet().stream()
                .allMatch(entry -> {
                    String key = entry.getKey().toLowerCase();
                    String value = entry.getValue().toLowerCase();
                    switch (key) {
                        case "patientid":
                            return history.getPatientID().toLowerCase().equals(value);
                        case "diagnosis":
                            return history.getDiagnosis().toLowerCase().contains(value);
                        // Add more filter criteria as needed
                        default:
                            return true;
                    }
                }))
            .collect(Collectors.toList());
    }

    /**
     * Updates an existing medical history record with new information.
     * Locates the existing record by ID and replaces it with the updated version.
     * Used by doctors to modify or correct existing medical history entries.
     *
     * @param updatedHistory The History object containing the updated information
     * @throws IllegalArgumentException If no history record with the specified ID exists
     */
    public void updateHistory(History updatedHistory) throws IllegalArgumentException {
        for (int i = 0; i < histories.size(); i++) {
            if (histories.get(i).getHistoryID().equals(updatedHistory.getHistoryID())) {
                histories.set(i, updatedHistory);
                return;
            }
        }
        throw new IllegalArgumentException("History record with ID " + updatedHistory.getHistoryID() + " not found.");
    }

    /**
     * Removes a medical history record from the system.
     * Searches for and removes the record matching the specified ID.
     * Used for removing incorrect or outdated medical history entries.
     *
     * @param historyID The unique identifier of the history record to remove
     * @throws IllegalArgumentException If no history record with the specified ID exists
     */
    public void removeHistory(String historyID) throws IllegalArgumentException {
        if (!histories.removeIf(history -> history.getHistoryID().equals(historyID))) {
            throw new IllegalArgumentException("History record with ID " + historyID + " not found.");
        }
    }
}
