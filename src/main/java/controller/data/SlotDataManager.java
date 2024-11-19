package controller.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import entity.Slot;

/**
 * Manages slot data operations in the Hospital Management System (HMS).
 * This class handles all appointment slot-related data persistence and CRUD operations,
 * including loading from and saving to CSV files, filtering slots, and managing slot availability.
 * 
 * The class provides functionality for doctors to set their availability slots,
 * patients to view and book available slots, and administrators to manage the
 * appointment scheduling system. It maintains a comprehensive record of all appointment
 * slots in the system.
 *
 * @author Group 7
 * @version 1.0
 */
public class SlotDataManager {

    private static final String SLOT_FILE = "src/main/resources/data/slots.csv";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * List of slots in the system.
     * Contains all appointment slots loaded from CSV storage.
     */
    private static List<Slot> slots;

    /**
     * Constructs a new SlotDataManager with an empty list of slots.
     * Initializes the slots list to store appointment slot data loaded from CSV storage.
     * This constructor is called during system initialization to prepare for slot management.
     */
    public SlotDataManager() {
        slots = new ArrayList<>();
    }

    /**
     * Loads slot data from the CSV file into memory.
     * Clears any existing slots before loading new data.
     * Each line in the CSV file represents one appointment slot with its complete details.
     * Prints debug information during the loading process.
     *
     * @throws IOException If there's an error reading the slots CSV file
     */
    public void loadSlotsFromCSV() throws IOException {
        slots.clear(); // Clear existing slots before loading
        System.out.println("\n[DEV] Loading: " + SLOT_FILE);
        try (BufferedReader br = new BufferedReader(new FileReader(SLOT_FILE))) {
            String line;
            br.readLine(); // Skip header line

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",", -1);
                if (values.length < 6) {
                    System.out.println("Skipping invalid line: " + line);
                    continue;
                }

                String slotID = values[0].trim();
                String doctorID = values[1].trim();
                LocalDate date = LocalDate.parse(values[2].trim(), DATE_FORMATTER);
                LocalTime startTime = LocalTime.parse(values[3].trim(), TIME_FORMATTER);
                LocalTime endTime = LocalTime.parse(values[4].trim(), TIME_FORMATTER);
                Slot.SlotStatus status = Slot.SlotStatus.valueOf(values[5].trim());

                Slot slot = new Slot(slotID, doctorID, date, startTime, endTime, status);
                slots.add(slot);
                System.out.println("[DEV] " + slot); // Debug output
            }
        }
        System.out.println("[DEV] Total slots loaded: " + slots.size()); // Debug output
    }

    /**
     * Saves all slot data from memory to the CSV file.
     * Writes the complete list of slots with their current states.
     * Creates a new file if it doesn't exist, or overwrites the existing file.
     *
     * @throws IOException If there's an error writing to the slots CSV file
     */
    public void saveSlotsToCSV() throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(SLOT_FILE))) {
            // Write header
            bw.write("slotID,doctorID,date,startTime,endTime,status");
            bw.newLine();

            for (Slot slot : slots) {
                StringBuilder sb = new StringBuilder();
                sb.append(slot.getSlotID()).append(",");
                sb.append(slot.getDoctorID()).append(",");
                sb.append(slot.getDate().format(DATE_FORMATTER)).append(",");
                sb.append(slot.getStartTime().format(TIME_FORMATTER)).append(",");
                sb.append(slot.getEndTime().format(TIME_FORMATTER)).append(",");
                sb.append(slot.getStatus());

                bw.write(sb.toString());
                bw.newLine();
            }
        }
    }

    /**
     * Adds a new appointment slot to the system.
     * Validates that no duplicate slot ID exists before adding.
     * Used by doctors to create new availability slots.
     *
     * @param slot The Slot object to add to the system
     * @throws IllegalArgumentException If a slot with the same ID already exists
     */
    public void addSlot(Slot slot) throws IllegalArgumentException {
        if (getSlotByID(slot.getSlotID()) != null) {
            throw new IllegalArgumentException("Slot with ID " + slot.getSlotID() + " already exists.");
        }
        slots.add(slot);
    }

    /**
     * Retrieves a copy of all slots in the system.
     * Returns a new list to preserve encapsulation of the internal slots list.
     * Used for displaying complete slot schedule to authorized users.
     *
     * @return A new List containing all Slot objects in the system
     */
    public List<Slot> getSlots() {
        return new ArrayList<>(slots); // Return a copy to preserve encapsulation
    }

    /**
     * Retrieves a specific slot by its unique identifier.
     * Searches through all slots to find an exact ID match.
     * Used when accessing or updating a specific slot entry.
     *
     * @param slotID The unique identifier of the slot to retrieve
     * @return The Slot object if found, null if no match exists
     */
    public Slot getSlotByID(String slotID) {
        return slots.stream()
                .filter(slot -> slot.getSlotID().equals(slotID))
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves a slot by matching both slot ID and doctor ID.
     * Ensures that the slot belongs to the specified doctor.
     * Used for doctor-specific slot management operations.
     *
     * @param slotID The unique identifier of the slot to retrieve
     * @param doctorID The doctor ID linked to retrieved slot
     * @return The Slot object if found, null if no match exists
     */
    public Slot getStatusByID(String slotID, String doctorID) {
        return slots.stream()
                .filter(slot -> slot.getSlotID().equals(slotID)
                && slot.getDoctorID().equals(doctorID))
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves a slot by matching slot ID, doctor ID, and slot status.
     * Provides fine-grained filtering for specific slot states.
     * Used for managing slot availability and booking status.
     *
     * @param slotID The unique identifier of the slot to retrieve
     * @param doctorID The doctor ID linked to retrieved slot
     * @param status The slot status to filter by
     * @return The Slot object if found, null if no match exists
     */
    public Slot getStatusByID(String slotID, String doctorID, Slot.SlotStatus status) {
        return slots.stream()
                .filter(slot -> slot.getSlotID().equals(slotID)
                && slot.getStatus() == status
                && slot.getDoctorID().equals(doctorID))
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves all available slots for a specific doctor on a given date.
     * Filters slots by doctor ID, date, and availability status.
     * Used by patients to view booking options for appointments.
     *
     * @param doctorID The ID of the doctor to find slots for
     * @param date The date to check for available slots
     * @return List of available Slot objects matching the criteria
     */
    public List<Slot> getAvailableSlots(String doctorID, LocalDate date) {
        return slots.stream()
                .filter(slot -> slot.getDoctorID().equals(doctorID)
                && slot.getDate().equals(date)
                && slot.getStatus() == Slot.SlotStatus.AVAILABLE)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    /**
     * Updates the information of an existing slot in the system.
     * Replaces the old slot data with the updated information.
     * Used for modifying slot details such as time or status.
     *
     * @param updatedSlot The Slot object containing the updated information
     * @throws IllegalArgumentException If the slot to update doesn't exist
     */
    public void updateSlot(Slot updatedSlot) throws IllegalArgumentException {
        for (int i = 0; i < slots.size(); i++) {
            if (slots.get(i).getSlotID().equals(updatedSlot.getSlotID())) {
                slots.set(i, updatedSlot);
                return;
            }
        }
        throw new IllegalArgumentException("Slot with ID " + updatedSlot.getSlotID() + " not found.");
    }

    /**
     * Removes a slot from the system permanently.
     * Deletes the slot with the specified ID if it exists.
     * Used for cleaning up cancelled or obsolete slots.
     *
     * @param slotID The ID of the slot to remove
     * @throws IllegalArgumentException If the slot to remove doesn't exist
     */
    public void removeSlot(String slotID) throws IllegalArgumentException {
        if (!slots.removeIf(slot -> slot.getSlotID().equals(slotID))) {
            throw new IllegalArgumentException("Slot with ID " + slotID + " not found.");
        }
    }
}
