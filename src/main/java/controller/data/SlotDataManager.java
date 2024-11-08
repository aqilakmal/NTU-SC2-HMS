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
 * Manages slot data operations including loading from and saving to CSV files,
 * as well as basic CRUD operations for slots in the Hospital Management System.
 */
public class SlotDataManager {

    private static final String SLOT_FILE = "src/main/resources/data/slots.csv";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * List of slots in the system.
     */
    private static List<Slot> slots;

    /**
     * Constructs a new SlotDataManager with an empty list of slots.
     */
    public SlotDataManager() {
        slots = new ArrayList<>();
    }

    /**
     * Loads slot data from a CSV file.
     *
     * @throws IOException If there's an error reading the file.
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
     * Saves slot data to a CSV file.
     *
     * @throws IOException If there's an error writing to the file.
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
     * [CREATE] Adds a new slot to the system.
     *
     * @param slot The Slot object to add.
     * @throws IllegalArgumentException If the slot already exists.
     */
    public void addSlot(Slot slot) throws IllegalArgumentException {
        if (getSlotByID(slot.getSlotID()) != null) {
            throw new IllegalArgumentException("Slot with ID " + slot.getSlotID() + " already exists.");
        }
        slots.add(slot);
    }

    /**
     * [READ] Retrieves the list of all slots.
     *
     * @return List of Slot objects.
     */
    public List<Slot> getSlots() {
        return new ArrayList<>(slots); // Return a copy to preserve encapsulation
    }

    /**
     * [READ] Retrieves a slot by its ID.
     *
     * @param slotID The ID of the slot to retrieve.
     * @return The Slot object if found, null otherwise.
     */
    public Slot getSlotByID(String slotID) {
        return slots.stream()
                .filter(slot -> slot.getSlotID().equals(slotID))
                .findFirst()
                .orElse(null);
    }

    /**
     * [READ] Retrieves a slot by its ID.
     *
     * @param slotID The ID of the slot to retrieve.
     * @param doctorID The doctor ID linked to retrieved slot.
     * @return The Slot object if found, null otherwise.
     */
    public Slot getStatusByID(String slotID, String doctorID) {
        return slots.stream()
                .filter(slot -> slot.getSlotID().equals(slotID)
                && slot.getDoctorID().equals(doctorID))
                .findFirst()
                .orElse(null);
    }

    /**
     * [READ] Retrieve slot by Slot ID, Doctor ID and Status
     *
     * @param slotID The ID of the slot to retrieve.
     * @param doctorID The doctor ID linked to retrieved slot.
     * @param status Slot Status to filter by
     * @return The Slot object if found, null otherwise.
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
     * [READ] Retrieves available slots for a specific doctor on a given date.
     *
     * @param doctorID The ID of the doctor.
     * @param date The date to check for available slots.
     * @return List of available Slot objects.
     */
    public List<Slot> getAvailableSlots(String doctorID, LocalDate date) {
        return slots.stream()
                .filter(slot -> slot.getDoctorID().equals(doctorID)
                && slot.getDate().equals(date)
                && slot.getStatus() == Slot.SlotStatus.AVAILABLE)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    /**
     * [UPDATE] Updates an existing slot's information.
     *
     * @param updatedSlot The Slot object with updated information.
     * @throws IllegalArgumentException If the slot doesn't exist.
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
     * [DELETE] Removes a slot from the system.
     *
     * @param slotID The ID of the slot to remove.
     * @throws IllegalArgumentException If the slot doesn't exist.
     */
    public void removeSlot(String slotID) throws IllegalArgumentException {
        if (!slots.removeIf(slot -> slot.getSlotID().equals(slotID))) {
            throw new IllegalArgumentException("Slot with ID " + slotID + " not found.");
        }
    }
}
