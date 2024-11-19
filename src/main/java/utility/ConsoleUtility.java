package utility;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.function.Predicate;

import entity.User;

/**
 * Utility class for console operations, including input validation, formatting,
 * and common I/O operations.
 */
public class ConsoleUtility {

    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Validates user input based on a given validation function.
     *
     * @param prompt The prompt to display to the user
     * @param validationFunction The function to validate the input
     * @return The validated input
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
     * Prompts the user to enter a valid integer value continuously until a
     * valid integer is entered.
     *
     * @param prompt The prompt to display to the user.
     * @return The validated double value.
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
     * Validates and returns the user role.
     *
     * @return The validated User.UserRole
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
     * Basic Input (No greater than 32 characters)
     * 
     * @param input The input to validate
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
     * Validates any ID (user, medication, appointment, etc.).
     *
     * @param id The ID to validate
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
     * Validates the name.
     *
     * @param name The name to validate
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
     * Validates the contact number.
     *
     * @param contactNumber The contact number to validate
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
     * Validates the email address.
     *
     * @param email The email address to validate
     * @return true if the email address is valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            System.out.println("Invalid Input: Invalid email format.");
            return false;
        }
        return true;
    }

    /**
     * Validates a date string.
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
     * Validates a time string.
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
     * Validates an integer input.
     *
     * @param input The input to validate
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
     * Prints a header for a menu or section.
     *
     * @param header The header text to print
     * @param addNewLine Whether to add a new line at the end (default is true)
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
     * Prints a header for a menu or section with a new line at the end.
     *
     * @param header The header text to print
     */
    public static void printHeader(String header) {
        printHeader(header, true);
    }

    /**
     * Gets a confirmed yes/no input from the user.
     *
     * @param prompt The prompt to display to the user
     * @return true if the user confirms, false otherwise
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
     * Validates the gender input.
     * @param gender The gender to validate
     * @return true if the gender is valid (MALE or FEMALE), false otherwise
     */
    public static boolean isValidGender(String gender) {
        if (!gender.matches("^[12]$")) {
            System.out.println("Invalid Input: Please enter 1 for MALE or 2 for FEMALE.");
            return false;
        }
        return true;
    }

    /**
     * Validates the date format (YYYY-MM-DD).
     * @param date The date string to validate
     * @return true if the date is valid, false otherwise
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
