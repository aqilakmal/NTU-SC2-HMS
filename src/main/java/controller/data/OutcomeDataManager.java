package controller.data;

import entity.Outcome;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages outcome data operations including loading from and saving to CSV files,
 * as well as basic CRUD operations for outcomes in the Hospital Management System.
 */
public class OutcomeDataManager {

    private static final String OUTCOME_FILE = "src/main/resources/data/outcomes.csv";

    private List<Outcome> outcomes;

    /**
     * Constructs a new OutcomeDataManager with an empty list of outcomes.
     */
    public OutcomeDataManager() {
        this.outcomes = new ArrayList<>();
    }

    /**
     * Loads outcome data from CSV file.
     * @throws IOException If there's an error reading the file.
     */
    public void loadOutcomesFromCSV() throws IOException {
        outcomes.clear();

        System.out.println("\n[DEV] Loading outcomes: " + OUTCOME_FILE);
        try (BufferedReader br = new BufferedReader(new FileReader(OUTCOME_FILE))) {
            String line;
            br.readLine(); // Skip header line
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",", -1); // Use -1 to keep empty trailing fields
                if (values.length >= 5) {
                    Outcome outcome = new Outcome(
                        values[0].trim(), // outcomeID
                        values[1].trim(), // appointmentID
                        values[2].trim(), // serviceProvided
                        values[3].trim(), // prescriptionID
                        values[4].trim()  // consultationNotes
                    );
                    outcomes.add(outcome);
                    System.out.println("[DEV] " + outcome); // Debug output
                }
            }
        }
        System.out.println("[DEV] Loaded " + outcomes.size() + " outcomes."); // Debug output
    }

    /**
     * Saves outcome data to CSV file.
     * @throws IOException If there's an error writing to the file.
     */
    public void saveOutcomesToCSV() throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(OUTCOME_FILE))) {
            bw.write("outcomeID,appointmentID,serviceProvided,prescriptionID,consultationNotes");
            bw.newLine();
            for (Outcome outcome : outcomes) {
                bw.write(String.format("%s,%s,%s,%s,%s",
                    outcome.getOutcomeID(),
                    outcome.getAppointmentID(),
                    outcome.getServiceProvided(),
                    outcome.getPrescriptionID(),
                    outcome.getConsultationNotes()
                ));
                bw.newLine();
            }
        }
    }

    /**
     * Retrieves all outcomes.
     * @return List of all Outcome objects.
     */
    public List<Outcome> getAllOutcomes() {
        return new ArrayList<>(outcomes);
    }

    /**
     * Retrieves an outcome by its ID.
     * @param outcomeID The ID of the outcome to retrieve
     * @return The Outcome object if found, null otherwise
     */
    public Outcome getOutcomeByID(String outcomeID) {
        return outcomes.stream()
            .filter(outcome -> outcome.getOutcomeID().equals(outcomeID))
            .findFirst()
            .orElse(null);
    }
}