package utility;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.function.Function;

/**
 * Utility class for building and printing tables with dynamic column widths.
 * 
 * @example
 * List<Person> people = Arrays.asList(
 *     new Person("John", "Smith", 28, LocalDateTime.now()),
 *     new Person("Alexander", "Williams", 33, LocalDateTime.now().minusDays(1)),
 *     new Person("Emily", "Johnson", 25, LocalDateTime.now().plusDays(1))
 * );
 *
 * // Define column mappings with optional value transformations
 * LinkedHashMap<String, TableBuilder.ColumnMapping> columnMapping = new LinkedHashMap<>();
 * columnMapping.put("firstName", new TableBuilder.ColumnMapping("First Name", null));
 * columnMapping.put("lastName", new TableBuilder.ColumnMapping("Last Name", null));
 * columnMapping.put("dateTime", new TableBuilder.ColumnMapping("Date/Time", (val) -> ((LocalDateTime) val).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
 * columnMapping.put("age", new TableBuilder.ColumnMapping("Age", null));
 *
 * // Generate and print table
 * TableBuilder.createTable("Person Table", people, columnMapping, 20);
 */
public class TableBuilder {

    /**
     * Represents a mapping for a column in the table.
     */
    public static class ColumnMapping {
        private String columnName;
        private Function<Object, String> callback;

        public ColumnMapping(String columnName, Function<Object, String> callback) {
            this.columnName = columnName;
            this.callback = callback;
        }

        public String getColumnName() {
            return columnName;
        }

        public Function<Object, String> getCallback() {
            return callback;
        }
    }

    /**
     * Creates and prints a table with dynamic column widths.
     * @param tableName The name of the table
     * @param objects The list of objects to display in the table
     * @param columnMapping The mapping of column names to their respective callbacks
     * @param maxLenCol The maximum length for the columns
     */
    public static void createTable(String tableName, List<?> objects, LinkedHashMap<String, ColumnMapping> columnMapping, int maxLenCol) {
        // Determine the maximum width for each column (either from data or header)
        int[] colWidths = new int[columnMapping.size()];
        String[] headers = new String[columnMapping.size()];

        int index = 0;
        for (Map.Entry<String, ColumnMapping> entry : columnMapping.entrySet()) {
            headers[index] = entry.getValue().getColumnName();
            colWidths[index] = Math.min(maxLenCol, headers[index].length()); // Initialize with header length
            index++;
        }

        // Calculate column widths based on data
        for (Object obj : objects) {
            index = 0;
            for (Map.Entry<String, ColumnMapping> entry : columnMapping.entrySet()) {
                try {
                    String attributeName = entry.getKey();
                    String stringValue = getAttributeValue(obj, attributeName, entry.getValue().getCallback());
                    int valueLength = stringValue != null ? stringValue.length() : 0;
                    if (valueLength > maxLenCol) {
                        valueLength = maxLenCol - 3; // For ellipsis
                    }
                    colWidths[index] = Math.max(colWidths[index], Math.min(maxLenCol, valueLength));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                index++;
            }
        }

        // Add space for padding
        for (int i = 0; i < colWidths.length; i++) {
            colWidths[i] += 2; // Add space padding on both sides
        }

        // Calculate total width for the whole table (sum of column widths + spaces between columns)
        int totalWidth = 0;
        for (int width : colWidths) {
            totalWidth += width;
        }
        totalWidth += colWidths.length - 1; // Add space between columns

        // Print the table name with a line above and below
        System.out.println();
        printLine(totalWidth - 1, '┌', '┐');
        System.out.println("│ " + tableName + " ".repeat(Math.max(0, totalWidth - tableName.length())) + "│");
        printLine(totalWidth - 1, '├', '┤');

        // Print the header
        index = 0;
        System.out.print("│ ");
        for (String header : headers) {
            printCell(header, colWidths[index]);
            if (index < colWidths.length - 1) System.out.print(" "); // Add space between columns
            index++;
        }
        System.out.println("│");

        printLine(totalWidth - 1, '├', '┤');

        // Print the rows
        for (Object obj : objects) {
            index = 0;
            System.out.print("│ ");
            for (Map.Entry<String, ColumnMapping> entry : columnMapping.entrySet()) {
                try {
                    String attributeName = entry.getKey();
                    String stringValue = getAttributeValue(obj, attributeName, entry.getValue().getCallback());
                    
                    // Handle truncation and ellipsis
                    if (stringValue.length() > maxLenCol) {
                        stringValue = stringValue.substring(0, maxLenCol - 4) + "...";  // Truncate with ellipsis
                    }
                    
                    printCell(stringValue, colWidths[index]);
                    if (index < colWidths.length - 1) System.out.print(" "); // Add space between columns
                } catch (Exception e) {
                    e.printStackTrace();
                }
                index++;
            }
            System.out.println("│");
        }

        printLine(totalWidth - 1, '└', '┘');
    }

    /**
     * Prints a cell with proper padding.
     * @param content The content of the cell
     * @param width The width of the cell
     */
    private static void printCell(String content, int width) {
        // Calculate padding after adding ellipsis if necessary
        int padding = Math.max(0, width - content.length());  // Ensure no negative padding
        System.out.print(content + " ".repeat(padding));  // Add spaces to match width
    }

    /**
     * Prints a line with the specified characters.
     * @param totalWidth The total width of the line
     * @param leftCorner The character for the left corner
     * @param rightCorner The character for the right corner
     */
    private static void printLine(int totalWidth, char leftCorner, char rightCorner) {
        System.out.print(leftCorner);
        for (int i = 0; i < totalWidth + 2; i++) {
            System.out.print("-");
        }
        System.out.println(rightCorner);
    }

    /**
     * Gets the attribute value via getter method or direct field access.
     * @param obj The object to get the attribute value from
     * @param attributeName The name of the attribute
     * @param callback The callback to apply to the attribute value
     * @return The attribute value as a string
     * @throws Exception If there's an error getting the attribute value
     */
    private static String getAttributeValue(Object obj, String attributeName, Function<Object, String> callback) throws Exception {
        // Capitalize the first letter of the attribute to find getter method
        String capitalized = attributeName.substring(0, 1).toUpperCase() + attributeName.substring(1);
        String getterName = "get" + capitalized;

        Object value;
        try {
            // Try to find a getter method
            Method getterMethod = obj.getClass().getMethod(getterName);
            value = getterMethod.invoke(obj);
        } catch (NoSuchMethodException e) {
            // If no getter is found, fallback to accessing the field directly
            Field field = obj.getClass().getDeclaredField(attributeName);
            field.setAccessible(true); // Make the field accessible if it's private
            value = field.get(obj);
        }

        // If a callback is provided, apply it
        if (callback != null) {
            return callback.apply(value);
        } else {
            return value != null ? value.toString() : "";
        }
    }
}
