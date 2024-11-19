package utility;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.function.Function;

/**
 * Utility class for building and printing formatted tables with dynamic column widths.
 * This class provides functionality to create visually appealing ASCII tables from lists
 * of objects, with support for custom column mappings and value transformations.
 * 
 * The class handles dynamic sizing of columns, truncation of long content, proper padding,
 * and border characters to create professional-looking tables in the console output.
 * It supports accessing object properties through both getter methods and direct field access.
 * 
 * @author Group 7
 * @version 1.0
 */
public class TableBuilder {

    /**
     * Represents a mapping configuration for a table column.
     * This class defines how a column should be displayed in the table, including
     * its header name and an optional transformation function for the column values.
     * The callback function allows for custom formatting of the column data before display.
     */
    public static class ColumnMapping {
        private String columnName;
        private Function<Object, String> callback;

        /**
         * Creates a new column mapping with the specified header name and value transformer.
         * 
         * @param columnName The display name for the column header
         * @param callback Optional function to transform the column values before display
         */
        public ColumnMapping(String columnName, Function<Object, String> callback) {
            this.columnName = columnName;
            this.callback = callback;
        }

        /**
         * Returns the display name for this column.
         * 
         * @return The column's header name as it will appear in the table
         */
        public String getColumnName() {
            return columnName;
        }

        /**
         * Returns the transformation function for this column's values.
         * 
         * @return The function used to transform column values, or null if no transformation is needed
         */
        public Function<Object, String> getCallback() {
            return callback;
        }
    }

    /**
     * Creates and prints a formatted table to the console output.
     * This method handles the entire table generation process, including calculating column widths,
     * applying value transformations, and rendering the table with proper borders and padding.
     * The table automatically adjusts to accommodate the content while respecting the maximum
     * column width limit.
     * 
     * @param tableName The title to display at the top of the table
     * @param objects The list of objects whose properties will be displayed in the table
     * @param columnMapping The configuration mapping defining which properties to display and how
     * @param maxLenCol The maximum allowed width for any column in characters
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
     * Prints a single cell in the table with proper padding.
     * This method handles the formatting of individual cells, ensuring consistent spacing
     * and alignment within the table structure. It adds padding after the content to
     * maintain the specified column width.
     * 
     * @param content The text content to display in the cell
     * @param width The total width the cell should occupy, including padding
     */
    private static void printCell(String content, int width) {
        // Calculate padding after adding ellipsis if necessary
        int padding = Math.max(0, width - content.length());  // Ensure no negative padding
        System.out.print(content + " ".repeat(padding));  // Add spaces to match width
    }

    /**
     * Prints a horizontal line for table borders with specified corner characters.
     * This method generates the horizontal lines used for table borders and separators,
     * using the specified characters for the left and right corners. The line width
     * is calculated to match the table's total width.
     * 
     * @param totalWidth The total width of the line excluding corners
     * @param leftCorner The character to use for the left corner
     * @param rightCorner The character to use for the right corner
     */
    private static void printLine(int totalWidth, char leftCorner, char rightCorner) {
        System.out.print(leftCorner);
        for (int i = 0; i < totalWidth + 2; i++) {
            System.out.print("-");
        }
        System.out.println(rightCorner);
    }

    /**
     * Retrieves an attribute value from an object using reflection.
     * This method attempts to access object properties first through getter methods,
     * then through direct field access if no getter is available. It also applies
     * any specified transformation to the value before returning it as a string.
     * 
     * @param obj The object from which to retrieve the attribute value
     * @param attributeName The name of the attribute to retrieve
     * @param callback Optional transformation function to apply to the value
     * @return The attribute value as a string, possibly transformed by the callback
     * @throws Exception If the attribute cannot be accessed or transformed
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
