package controller.data;

import entity.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Manages user data operations in the Hospital Management System (HMS).
 * This class handles all user-related data persistence and CRUD operations,
 * including loading from and saving to CSV files, filtering users, and managing
 * user details.
 * 
 * The class provides functionality for administrators to manage hospital staff,
 * supports user authentication during login, and maintains a comprehensive record
 * of all system users including patients, doctors, pharmacists and administrators.
 * It ensures data integrity and proper access control based on user roles.
 *
 * @author Group 7
 * @version 1.0
 */
public class UserDataManager {

    private static final String USER_FILE = "src/main/resources/data/users.csv";

    /**
     * List of users in the system.
     * Stores all user records loaded from CSV storage and maintains
     * the current state of users in memory.
     */
    private static List<User> users;

    /**
     * Constructs a new UserDataManager with an empty list of users.
     * Initializes the users list to store user data loaded from CSV storage.
     * This constructor is called during system initialization to prepare for user management.
     */
    public UserDataManager() {
        users = new ArrayList<>();
    }

    /**
     * Loads user data from the CSV file into memory.
     * Reads and parses each line of the CSV file to create appropriate User objects
     * based on their roles (Patient, Doctor, Pharmacist, Administrator).
     * Validates data format and skips invalid entries while logging them for review.
     * Clears existing users before loading to ensure data consistency.
     *
     * @throws IOException If there's an error reading from the users CSV file
     */
    public void loadUsersFromCSV() throws IOException {
        users.clear(); // Clear existing users before loading
        System.out.println("\n[DEV] Loading: " + USER_FILE);
        try (BufferedReader br = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            br.readLine(); // Skip header line

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",", -1); // Use -1 to keep empty trailing fields
                if (values.length < 8) {
                    System.out.println("Skipping invalid line: " + line);
                    continue; // Skip invalid lines
                }

                String userID = values[0].trim();
                String password = values[1].trim();
                String role = values[2].trim();
                String name = values[3].trim();
                String dateOfBirth = values[4].trim();
                String gender = values[5].trim();
                String contactNumber = values[6].trim();
                String emailAddress = values[7].trim();
                String bloodType = values.length > 8 ? values[8].trim() : "";
                String specialization = values.length > 9 ? values[9].trim() : "";

                User user;
                try {
                    switch (User.UserRole.valueOf(role.toUpperCase())) {
                        case PATIENT:
                            user = new Patient(userID, password, User.UserRole.PATIENT, name, dateOfBirth, gender, contactNumber, emailAddress, bloodType);
                            break;
                        case DOCTOR:
                            user = new Doctor(userID, password, User.UserRole.DOCTOR, name, dateOfBirth, gender, contactNumber, emailAddress, specialization);
                            break;
                        case PHARMACIST:
                            user = new Pharmacist(userID, password, User.UserRole.PHARMACIST, name, dateOfBirth, gender, contactNumber, emailAddress);
                            break;
                        case ADMINISTRATOR:
                            user = new Administrator(userID, password, User.UserRole.ADMINISTRATOR, name, dateOfBirth, gender, contactNumber, emailAddress);
                            break;
                        default:
                            System.out.println("Skipping unknown role: " + role);
                            continue; // Skip unknown roles
                    }
                    users.add(user);
                    System.out.println("[DEV] " + user); // Debug output
                } catch (IllegalArgumentException e) {
                    System.out.println("Error creating user with role " + role + ": " + e.getMessage());
                }
            }
        }
        System.out.println("[DEV] Total users loaded: " + users.size()); // Debug output
    }

    /**
     * Saves all user data from memory to the CSV file.
     * Writes the complete list of users to persistent storage, handling different user types
     * appropriately (Patient, Doctor, Pharmacist, Administrator).
     * Includes a header line with column names for data structure clarity.
     *
     * @throws IOException If there's an error writing to the users CSV file
     */
    public void saveUsersToCSV() throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(USER_FILE))) {
            // Write header
            bw.write("userID,password,role,name,dateOfBirth,gender,contactNumber,emailAddress,bloodType,specialization");
            bw.newLine();

            for (User user : users) {
                StringBuilder sb = new StringBuilder();
                sb.append(user.getUserID()).append(",");
                sb.append(user.getPassword()).append(",");
                sb.append(user.getRole()).append(",");
                sb.append(user.getName()).append(",");
                sb.append(user.getDateOfBirth()).append(",");
                sb.append(user.getGender()).append(",");
                sb.append(user.getContactNumber()).append(",");
                sb.append(user.getEmailAddress()).append(",");
                
                if (user instanceof Patient) {
                    sb.append(((Patient) user).getBloodType()).append(",");
                } else {
                    sb.append(","); // Empty bloodType for non-patients
                }
                
                if (user instanceof Doctor) {
                    sb.append(((Doctor) user).getSpecialization());
                } else {
                    sb.append(""); // Empty specialization for non-doctors
                }
                
                bw.write(sb.toString());
                bw.newLine();
            }
        }
    }

    /**
     * Adds a new user to the system's records.
     * Validates that no duplicate user ID exists before adding.
     * Used by administrators to create new user accounts in the system.
     *
     * @param user The User object containing the new user's details
     * @throws IllegalArgumentException If a user with the same ID already exists
     */
    public void addUser(User user) throws IllegalArgumentException {
        if (getUserByID(user.getUserID()) != null) {
            throw new IllegalArgumentException("User with ID " + user.getUserID() + " already exists.");
        }
        users.add(user);
    }

    /**
     * Retrieves a copy of the complete list of users in the system.
     * Returns a new ArrayList to preserve encapsulation of the internal users list.
     * Used by administrators to view and manage all system users.
     *
     * @return A new ArrayList containing all User objects in the system
     */
    public List<User> getUsers() {
        return new ArrayList<>(users); // Return a copy to preserve encapsulation
    }

    /**
     * Retrieves a specific user by their unique ID.
     * Used for user authentication and profile management.
     * Returns null if no user is found with the specified ID.
     *
     * @param userID The unique identifier of the user to retrieve
     * @return The User object if found, null otherwise
     */
    public User getUserByID(String userID) {
        return users.stream()
                .filter(user -> user.getUserID().equals(userID))
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves a filtered list of users based on specified criteria.
     * Supports filtering by role, gender, and other user attributes.
     * Used by administrators to search and manage users based on specific criteria.
     *
     * @param filters Map containing filter criteria where key is the attribute name and value is the filter value
     * @return List of User objects matching all specified filter criteria
     */
    public List<User> getFilteredUsers(Map<String, String> filters) {
        return users.stream()
            .filter(user -> filters.entrySet().stream()
                .allMatch(entry -> {
                    String key = entry.getKey().toLowerCase();
                    String value = entry.getValue().toLowerCase();
                    switch (key) {
                        case "role":
                            return user.getRole().toString().toLowerCase().equals(value);
                        case "gender":
                            return user.getGender().toLowerCase().equals(value);
                        // Add more filter criteria as needed
                        default:
                            return true;
                    }
                }))
            .collect(Collectors.toList());
    }

    /**
     * Updates an existing user's information in the system.
     * Validates the user exists before attempting the update.
     * Used to modify user details while maintaining data integrity.
     *
     * @param updatedUser The User object containing the updated information
     * @throws IllegalArgumentException If no user exists with the specified ID
     */
    public void updateUser(User updatedUser) throws IllegalArgumentException {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUserID().equals(updatedUser.getUserID())) {
                users.set(i, updatedUser);
                return;
            }
        }
        throw new IllegalArgumentException("User with ID " + updatedUser.getUserID() + " not found.");
    }

    /**
     * Removes a user from the system by their ID.
     * Validates the user exists before attempting removal.
     * Used by administrators to manage system users.
     *
     * @param userID The unique identifier of the user to remove
     * @throws IllegalArgumentException If no user exists with the specified ID
     */
    public void removeUser(String userID) throws IllegalArgumentException {
        if (!users.removeIf(user -> user.getUserID().equals(userID))) {
            throw new IllegalArgumentException("User with ID " + userID + " not found.");
        }
    }
}