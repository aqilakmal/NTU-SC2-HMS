package utility;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
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
            System.out.println("Invalid input. Please try again.");
        }
    }

    /**
     * Prompts the user to enter a valid double value, continuously until a
     * valid double is entered.
     *
     * @param prompt The prompt to display to the user.
     * @return The validated double value.
     */
    public static double validateDoubleInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            // Check if the input can be parsed as a double
            if (isValidDouble(input)) {
                return Double.parseDouble(input);
            }
            System.out.println("Invalid input. Please enter a valid number.");
        }
    }

    /**
     * Checks if the input string is a valid double.
     *
     * @param input The input string to check.
     * @return True if the input can be parsed as a double, false otherwise.
     */
    /**
     * Checks if the input string is a valid double.
     *
     * @param input The input string to check.
     * @return True if the input can be parsed as a double, false otherwise.
     */
    public static boolean isValidDouble(String input) {
        try {
            Double.parseDouble(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Validates any ID (user, medication, appointment, etc.).
     *
     * @param id The ID to validate
     * @return true if the ID is valid, false otherwise
     */
    public static boolean isValidID(String id) {
        return !id.isEmpty() && id.matches("^[a-zA-Z0-9]+$");
    }

    /**
     * Validates the name.
     *
     * @param name The name to validate
     * @return true if the name is valid, false otherwise
     */
    public static boolean isValidName(String name) {
        return !name.isEmpty() && name.matches("^[a-zA-Z\\s]+$");
    }

    /**
     * Validates the contact number.
     *
     * @param contactNumber The contact number to validate
     * @return true if the contact number is valid, false otherwise
     */
    public static boolean isValidContactNumber(String contactNumber) {
        return contactNumber.matches("^\\d{8}$");
    }

    /**
     * Validates the email address.
     *
     * @param email The email address to validate
     * @return true if the email address is valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
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
            return false;
        }
    }

    public static int validateInteger(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int input = scanner.nextInt();  // Try to read an integer
                scanner.nextLine();
                return input;  // If successful, return the input
            } catch (InputMismatchException e) {
                // If input is not an integer, catch the exception
                System.out.println("Invalid input. Please enter a valid integer.");
                scanner.next();  // Clear the invalid input from the scanner
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
                System.out.println("Error: Invalid role. Please enter either 1 for DOCTOR or 2 for PHARMACIST.");
            }
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
            System.out.println("Invalid input. Please enter 'y' for yes or 'n' for no.");
        }
    }
}
