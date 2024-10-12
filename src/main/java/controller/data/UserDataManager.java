package controller.data;

import entity.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Manages user data operations including loading from and saving to CSV files,
 * as well as basic CRUD operations for users in the Hospital Management System.
 */
public class UserDataManager {

    private static final String USER_FILE = "src/main/resources/data/users.csv";

    /**
     * List of users in the system.
     */
    private static List<User> users;

    /**
     * Constructs a new UserDataManager with an empty list of users.
     */
    public UserDataManager() {
        users = new ArrayList<>();
    }

    /**
     * Loads user data from a CSV file.
     * @throws IOException If there's an error reading the file.
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
     * Saves user data to a CSV file.
     * @throws IOException If there's an error writing to the file.
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
     * [CREATE] Adds a new user to the system.
     * @param user The User object to add.
     * @throws IllegalArgumentException If the user already exists.
     */
    public void addUser(User user) throws IllegalArgumentException {
        if (getUserByID(user.getUserID()) != null) {
            throw new IllegalArgumentException("User with ID " + user.getUserID() + " already exists.");
        }
        users.add(user);
    }

    /**
     * [READ] Retrieves the list of all users.
     * @return List of User objects.
     */
    public List<User> getUsers() {
        return new ArrayList<>(users); // Return a copy to preserve encapsulation
    }

    /**
     * [READ] Retrieves a user by their ID.
     * @param userID The ID of the user to retrieve.
     * @return The User object if found, null otherwise.
     */
    public User getUserByID(String userID) {
        return users.stream()
                .filter(user -> user.getUserID().equals(userID))
                .findFirst()
                .orElse(null);
    }

    /**
     * [READ] Retrieves a filtered list of users based on specified criteria.
     * @param filters Map of filter criteria (e.g., role, gender, etc.)
     * @return List of User objects matching the filter criteria
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
     * [UPDATE] Updates an existing user's information.
     * @param updatedUser The User object with updated information.
     * @throws IllegalArgumentException If the user doesn't exist.
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
     * [DELETE] Removes a user from the system.
     * @param userID The ID of the user to remove.
     * @throws IllegalArgumentException If the user doesn't exist.
     */
    public void removeUser(String userID) throws IllegalArgumentException {
        if (!users.removeIf(user -> user.getUserID().equals(userID))) {
            throw new IllegalArgumentException("User with ID " + userID + " not found.");
        }
    }
}