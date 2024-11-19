package utility;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.function.Predicate;

import entity.User;

/**
 * Utility class providing console input/output operations and validation functions for the Hospital Management System.
 * This class centralizes all console interaction logic and input validation to ensure consistent user experience
 * and data integrity across the application. It handles various types of user inputs including basic text,
 * dates, times, numbers and specialized hospital data formats.
 * 
 * The class implements comprehensive input validation for all data types used in the system:
 * - Basic text input validation
 * - ID format validation for users, medications, appointments etc.
 * - Date and time format validation
 * - Numeric input validation
 * - Contact information validation
 * - Role-specific input validation
 * 
 * @author Group 7
 * @version 1.0
 */
public class ConsoleUtility {

    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Validates user input using a provided validation function.
     * This method continuously prompts the user for input until valid input is received.
     * The validation is performed using a provided predicate function that defines the validation rules.
     * Invalid inputs result in a retry prompt to the user.
     *
     * @param prompt The text prompt to display to the user requesting input
     * @param validationFunction A predicate function that returns true if input is valid
     * @return The validated user input string
     */
    public static String validateInput(String prompt, Predicate<String> validationFunction) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (validationFunction.test(input)) {
                return input;
            }
            System.out.println("Please try again.\n");
        }
    }

    /**
     * Validates and parses integer input from the user.
     * This method continuously prompts the user until a valid integer value is entered.
     * It handles number format exceptions and provides appropriate error messages.
     * The method ensures that only valid integer values are accepted.
     *
     * @param prompt The text prompt to display to the user requesting the integer input
     * @return The validated integer value
     */
    public static int validateIntegerInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            // Check if the input can be parsed as an integer
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid Input: Input must be a valid integer number.");
            }
        }
    }

    /**
     * Validates and returns a staff role selection from the user.
     * This method handles the role selection process for staff members.
     * It continuously prompts until a valid role selection is made.
     * Only allows selection of DOCTOR or PHARMACIST roles.
     *
     * @return The validated User.UserRole enum value
     */
    public static User.UserRole validateRole() {
        while (true) {
            System.out.print("Enter staff role (Enter {1} DOCTOR or {2} PHARMACIST): ");
            String roleInput = scanner.nextLine().trim();
            try {
                switch (roleInput) {
                    case "1":
                        return User.UserRole.DOCTOR;
                    case "2":
                        return User.UserRole.PHARMACIST;
                    default:
                        throw new IllegalArgumentException();
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid Input: Please enter either 1 for DOCTOR or 2 for PHARMACIST.");
            }
        }
    }

    /**
     * Validates basic text input ensuring it doesn't exceed maximum length.
     * This method checks if the input string length is within acceptable limits.
     * It is used for general purpose text input validation where specific format
     * requirements don't apply.
     *
     * @param input The string to validate
     * @return true if the input is valid, false otherwise
     */
    public static boolean isValidBasicInput(String input) {
        if (input.length() > 32) {
            System.out.println("Invalid Input: Input must not exceed 32 characters.");
            return false;
        }
        return true;
    }

    /**
     * Validates identifier strings used throughout the system.
     * This method ensures that IDs conform to the system's standardized format.
     * It checks for empty strings and validates that only alphanumeric characters are used.
     * Used for validating user IDs, medication IDs, appointment IDs, etc.
     *
     * @param id The identifier string to validate
     * @return true if the ID is valid, false otherwise
     */
    public static boolean isValidID(String id) {
        if (id.isEmpty()) {
            System.out.println("Invalid Input: ID cannot be empty.");
            return false;
        }
        if (!id.matches("^[a-zA-Z0-9]+$")) {
            System.out.println("Invalid Input: ID must contain only letters and numbers.");
            return false;
        }
        return true;
    }

    /**
     * Validates name strings for users in the system.
     * This method ensures that names contain only letters and spaces.
     * It checks for empty strings and validates the character set used.
     * Used for validating patient names, staff names, etc.
     *
     * @param name The name string to validate
     * @return true if the name is valid, false otherwise
     */
    public static boolean isValidName(String name) {
        if (name.isEmpty()) {
            System.out.println("Invalid Input: Name cannot be empty.");
            return false;
        }
        if (!name.matches("^[a-zA-Z\\s]+$")) {
            System.out.println("Invalid Input: Name must contain only letters and spaces.");
            return false;
        }
        return true;
    }

    /**
     * Validates contact numbers according to system requirements.
     * This method ensures that contact numbers are exactly 8 digits long.
     * It checks that the input contains only numeric characters.
     * Used for validating phone numbers of patients and staff.
     *
     * @param contactNumber The contact number string to validate
     * @return true if the contact number is valid, false otherwise
     */
    public static boolean isValidContactNumber(String contactNumber) {
        if (!contactNumber.matches("^\\d{8}$")) {
            System.out.println("Invalid Input: Contact number must be exactly 8 digits.");
            return false;
        }
        return true;
    }

    /**
     * Validates email addresses according to basic email format rules.
     * This method checks if the email follows a standard email format.
     * It ensures the presence of @ symbol and proper domain format.
     * Used for validating email addresses of patients and staff.
     *
     * @param email The email address string to validate
     * @return true if the email is valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            System.out.println("Invalid Input: Invalid email format.");
            return false;
        }
        return true;
    }

    /**
     * Validates date strings and ensures they can be parsed to LocalDate.
     * This method checks if the date string can be properly parsed.
     * It uses the ISO local date format (YYYY-MM-DD).
     * Used for validating appointment dates, birth dates, etc.
     *
     * @param dateStr The date string to validate
     * @return true if the date is valid, false otherwise
     */
    public static boolean isValidDate(String dateStr) {
        try {
            LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
            return true;
        } catch (DateTimeParseException e) {
            System.out.println("Invalid Input: Invalid date format. Use YYYY-MM-DD format.");
            return false;
        }
    }

    /**
     * Validates time strings and ensures they can be parsed to LocalTime.
     * This method checks if the time string can be properly parsed.
     * It uses the ISO local time format (HH:MM:SS).
     * Used for validating appointment times, operating hours, etc.
     *
     * @param timeStr The time string to validate
     * @return true if the time is valid, false otherwise
     */
    public static boolean isValidTime(String timeStr) {
        try {
            LocalTime.parse(timeStr, DateTimeFormatter.ISO_LOCAL_TIME);
            return true;
        } catch (DateTimeParseException e) {
            System.out.println("Invalid Input: Invalid time format. Use HH:MM:SS format.");
            return false;
        }
    }

    /**
     * Validates if a string can be parsed to an integer.
     * This method attempts to parse the input string to an integer.
     * It provides appropriate error messages for invalid inputs.
     * Used for validating numeric inputs throughout the system.
     *
     * @param input The string to validate as an integer
     * @return true if the input is a valid integer, false otherwise
     */
    public static boolean isValidInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            System.out.println("Invalid Input: Input must be a valid integer number.");
            return false;
        }
    }

    /**
     * Prints a formatted header for menus and sections.
     * This method creates consistent header formatting throughout the application.
     * It allows control over whether a new line is added after the header.
     * Used for creating visual separation between different sections of the interface.
     *
     * @param header The header text to display
     * @param addNewLine Whether to add a new line after the header
     */
    public static void printHeader(String header, boolean addNewLine) {
        String headerText = "\n<======= " + header + " =======>";
        if (addNewLine) {
            System.out.println(headerText + "\n");
        } else {
            System.out.print(headerText);
        }
    }

    /**
     * Prints a formatted header with a new line after it.
     * This is a convenience method that calls printHeader with addNewLine set to true.
     * It provides a simpler interface for the common case of adding a new line after the header.
     *
     * @param header The header text to display
     */
    public static void printHeader(String header) {
        printHeader(header, true);
    }

    /**
     * Gets confirmation from the user for important actions.
     * This method prompts the user for a yes/no response.
     * It continuously prompts until a valid response is received.
     * Used for confirming critical actions like deletions or updates.
     *
     * @param prompt The prompt message to display to the user
     * @return true if user confirms (enters 'y'), false if user denies (enters 'n')
     */
    public static boolean getConfirmation(String prompt) {
        while (true) {
            System.out.print(prompt + " (y/n): ");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("y")) {
                return true;
            } else if (input.equals("n")) {
                return false;
            }
            System.out.println("Invalid Input: Please enter 'y' for yes or 'n' for no.");
        }
    }

    /**
     * Validates gender input according to system requirements.
     * This method ensures gender is specified as either 1 (MALE) or 2 (FEMALE).
     * It provides clear error messages for invalid inputs.
     * Used for validating gender input in user profiles.
     *
     * @param gender The gender input string to validate
     * @return true if the gender is valid, false otherwise
     */
    public static boolean isValidGender(String gender) {
        if (!gender.matches("^[12]$")) {
            System.out.println("Invalid Input: Please enter 1 for MALE or 2 for FEMALE.");
            return false;
        }
        return true;
    }

    /**
     * Validates date format and ensures it represents a valid date.
     * This method checks both the format (YYYY-MM-DD) and validity of the date.
     * It ensures that the date string represents an actual calendar date.
     * Used for validating dates throughout the system where strict format is required.
     *
     * @param date The date string to validate
     * @return true if the date format and value are valid, false otherwise
     */
    public static boolean isValidDateFormat(String date) {
        if (!date.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
            System.out.println("Invalid Input: Date must be in YYYY-MM-DD format.");
            return false;
        }
        try {
            LocalDate.parse(date);
            return true;
        } catch (DateTimeParseException e) {
            System.out.println("Invalid Input: Please enter a valid date.");
            return false;
        }
    }
}
