package controller.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import entity.Outcome;

/**
 * Manages outcome data operations in the Hospital Management System (HMS).
 * This class handles all appointment outcome-related data persistence and CRUD operations,
 * including loading from and saving to CSV files, filtering outcomes, and managing
 * outcome details.
 * 
 * The class provides functionality for doctors to record appointment outcomes,
 * including services provided, prescriptions issued, and consultation notes.
 * It maintains a comprehensive record of all appointment outcomes for tracking
 * patient care and hospital services.
 *
 * @author Group 7
 * @version 1.0
 */
public class OutcomeDataManager {

    private static final String OUTCOME_FILE = "src/main/resources/data/outcomes.csv";

    private List<Outcome> outcomes;

    /**
     * Constructs a new OutcomeDataManager with an empty list of outcomes.
     * Initializes the outcomes list to store outcome data loaded from CSV storage.
     * This constructor is called during system initialization to prepare for outcome management.
     */
    public OutcomeDataManager() {
        this.outcomes = new ArrayList<>();
    }

    /**
     * Loads outcome data from the CSV file into memory.
     * Clears any existing outcomes before loading new data.
     * Each line in the CSV file represents one outcome record with its complete details.
     * Prints debug information during the loading process.
     *
     * @throws IOException If there's an error reading the outcomes CSV file
     */
    public void loadOutcomesFromCSV() throws IOException {
        outcomes.clear();

        System.out.println("\n[DEV] Loading: " + OUTCOME_FILE);
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
                            values[4].trim() // consultationNotes
                    );
                    outcomes.add(outcome);
                    System.out.println("[DEV] " + outcome); // Debug output
                }
            }
        }
        System.out.println("[DEV] Loaded " + outcomes.size() + " outcomes."); // Debug output
    }

    /**
     * Saves all outcome data from memory to the CSV file.
     * Writes the complete list of outcomes to persistent storage.
     * Includes a header line with column names for data structure clarity.
     *
     * @throws IOException If there's an error writing to the outcomes CSV file
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
     * Retrieves a copy of all outcome records in the system.
     * Returns a new list to preserve encapsulation of the internal outcomes list.
     * Used for displaying complete outcome records to authorized users.
     *
     * @return A new List containing all Outcome objects in the system
     */
    public List<Outcome> getAllOutcomes() {
        return new ArrayList<>(outcomes);
    }

    /**
     * Retrieves a specific outcome record by its unique identifier.
     * Searches through all outcome records to find an exact ID match.
     * Used when accessing or updating a specific appointment outcome entry.
     *
     * @param outcomeID The unique identifier of the outcome record to retrieve
     * @return The matching Outcome object if found, null if no match exists
     */
    public Outcome getOutcomeByID(String outcomeID) {
        return outcomes.stream()
                .filter(outcome -> outcome.getOutcomeID().equals(outcomeID))
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves an outcome record associated with a specific appointment.
     * Filters the complete outcomes list to find the record matching the appointment ID.
     * Used to access outcome details for a particular appointment.
     * 
     * @param appointmentID The unique identifier of the appointment whose outcome to retrieve
     * @return The matching Outcome object if found, null if no match exists
     */
    public Outcome getOutcomeByAppointmentID(String appointmentID) {
        return outcomes.stream()
                .filter(outcome -> outcome.getAppointmentID().equals(appointmentID))
                .findFirst()
                .orElse(null);
    }

    /**
     * Adds a new outcome record to the system.
     * Validates that no duplicate outcome ID exists before adding.
     * Used by doctors to create new outcome entries after appointments.
     * 
     * @param outcome The Outcome object to add to the system
     * @throws IllegalArgumentException If an outcome record with the same ID already exists
     */
    public void addOutcome(Outcome outcome) throws IllegalArgumentException {
        if (getOutcomeByID(outcome.getOutcomeID()) != null) {
            throw new IllegalArgumentException("Outcome record with ID " + outcome.getOutcomeID() + " already exists.");
        }
        outcomes.add(outcome);
    }

    /**
     * Updates an existing outcome record with new information.
     * Locates the existing record by ID and replaces it with the updated version.
     * Used by doctors to modify or correct existing outcome entries.
     *
     * @param updatedOutcome The Outcome object containing the updated information
     * @throws IllegalArgumentException If no outcome record with the specified ID exists
     */
    public void updateOutcome(Outcome updatedOutcome) throws IllegalArgumentException {
        for (int i = 0; i < outcomes.size(); i++) {
            if (outcomes.get(i).getOutcomeID().equals(updatedOutcome.getOutcomeID())) {
                // Use the list `outcomes` to update the specific Outcome object at index `i`
                outcomes.set(i, updatedOutcome);
                return; // Exit the method once the outcome has been updated
            }
        }
        throw new IllegalArgumentException("Outcome with ID " + updatedOutcome.getOutcomeID() + " not found.");
    }
}
