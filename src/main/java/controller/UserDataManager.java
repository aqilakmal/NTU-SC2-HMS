package controller;

import entity.User;
import entity.Patient;
import entity.Doctor;
import entity.Pharmacist;
import entity.Administrator;
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
     * @param fileName The name of the CSV file to load data from.
     * @throws IOException If there's an error reading the file.
     */
    public void loadUsersFromCSV(String fileName) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            br.readLine(); // Skip header line

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length < 9) {
                    continue; // Skip invalid lines
                }

                String userID = values[0];
                String password = values[1];
                String role = values[2];
                String name = values[3];
                String dateOfBirth = values[4];
                String gender = values[5];
                String contactNumber = values[6];
                String emailAddress = values[7];
                String bloodType = values[8];
                String specialization = values.length > 9 ? values[9] : "";

                User user;
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
                        continue; // Skip unknown roles
                }
                users.add(user);
            }
        }
    }

    /**
     * Saves user data to a CSV file.
     * @param fileName The name of the CSV file to save data to.
     * @throws IOException If there's an error writing to the file.
     */
    public void saveUsersToCSV(String fileName) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
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
     * Retrieves the list of all users.
     * @return List of User objects.
     */
    public List<User> getUsers() {
        return new ArrayList<>(users); // Return a copy to preserve encapsulation
    }

    /**
     * Retrieves a user by their ID.
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
     * Adds a new user to the system.
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
     * Updates an existing user's information.
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
     * Removes a user from the system.
     * @param userID The ID of the user to remove.
     * @throws IllegalArgumentException If the user doesn't exist.
     */
    public void removeUser(String userID) throws IllegalArgumentException {
        if (!users.removeIf(user -> user.getUserID().equals(userID))) {
            throw new IllegalArgumentException("User with ID " + userID + " not found.");
        }
    }

    /**
     * Retrieves a filtered list of users based on specified criteria.
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
}