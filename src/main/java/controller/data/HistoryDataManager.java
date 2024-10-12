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
 * Manages medical history data operations including loading from and saving to CSV files,
 * as well as basic CRUD operations for medical history records in the Hospital Management System.
 */
public class HistoryDataManager {

    private static final String HISTORY_FILE = "src/main/resources/data/history.csv";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * List of medical history records in the system.
     */
    private static List<History> histories;

    /**
     * Constructs a new HistoryDataManager with an empty list of medical history records.
     */
    public HistoryDataManager() {
        histories = new ArrayList<>();
    }

    /**
     * Loads medical history data from a CSV file.
     * @throws IOException If there's an error reading the file.
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
     * Saves medical history data to a CSV file.
     * @throws IOException If there's an error writing to the file.
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
     * [CREATE] Adds a new medical history record to the system.
     * @param history The History object to add.
     * @throws IllegalArgumentException If the history record already exists.
     */
    public void addHistory(History history) throws IllegalArgumentException {
        if (getHistoryByID(history.getHistoryID()) != null) {
            throw new IllegalArgumentException("History record with ID " + history.getHistoryID() + " already exists.");
        }
        histories.add(history);
    }

    /**
     * [READ] Retrieves the list of all medical history records.
     * @return List of History objects.
     */
    public List<History> getHistories() {
        return new ArrayList<>(histories); // Return a copy to preserve encapsulation
    }

    /**
     * [READ] Retrieves a medical history record by its ID.
     * @param historyID The ID of the history record to retrieve.
     * @return The History object if found, null otherwise.
     */
    public History getHistoryByID(String historyID) {
        return histories.stream()
                .filter(history -> history.getHistoryID().equals(historyID))
                .findFirst()
                .orElse(null);
    }

    /**
     * [READ] Retrieves all medical history records for a specific patient.
     * @param patientID The ID of the patient.
     * @return List of History objects for the specified patient.
     */
    public List<History> getHistoriesByPatientID(String patientID) {
        return histories.stream()
                .filter(history -> history.getPatientID().equals(patientID))
                .collect(Collectors.toList());
    }

    /**
     * [READ] Retrieves a filtered list of medical history records based on specified criteria.
     * @param filters Map of filter criteria (e.g., patientID, diagnosisDate range, etc.)
     * @return List of History objects matching the filter criteria
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
     * [UPDATE] Updates an existing medical history record's information.
     * @param updatedHistory The History object with updated information.
     * @throws IllegalArgumentException If the history record doesn't exist.
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
     * [DELETE] Removes a medical history record from the system.
     * @param historyID The ID of the history record to remove.
     * @throws IllegalArgumentException If the history record doesn't exist.
     */
    public void removeHistory(String historyID) throws IllegalArgumentException {
        if (!histories.removeIf(history -> history.getHistoryID().equals(historyID))) {
            throw new IllegalArgumentException("History record with ID " + historyID + " not found.");
        }
    }
}
