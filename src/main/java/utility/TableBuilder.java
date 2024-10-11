package utility;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.function.Function;

public class TableBuilder {

    // ColumnMapping class to store both column name and optional callback (transformation function)
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
        printLine(totalWidth);
        System.out.println(tableName);
        printLine(totalWidth);

        // Print the header
        index = 0;
        for (String header : headers) {
            printCell(header, colWidths[index]);
            if (index < colWidths.length - 1) System.out.print(" "); // Add space between columns
            index++;
        }
        System.out.println();

        // Print a line under the headers
        printLine(totalWidth);

        // Print the rows
        for (Object obj : objects) {
            index = 0;
            for (Map.Entry<String, ColumnMapping> entry : columnMapping.entrySet()) {
                try {
                    String attributeName = entry.getKey();
                    String stringValue = getAttributeValue(obj, attributeName, entry.getValue().getCallback());
                    if (stringValue.length() > maxLenCol) {
                        stringValue = stringValue.substring(0, maxLenCol - 3) + "..."; // Truncate with ellipsis
                    }
                    printCell(stringValue, colWidths[index]);
                    if (index < colWidths.length - 1) System.out.print(" "); // Add space between columns
                } catch (Exception e) {
                    e.printStackTrace();
                }
                index++;
            }
            System.out.println();
        }

        // Print the final line after the table
        printLine(totalWidth);
    }

    // Helper method to print cell content with proper padding
    private static void printCell(String content, int width) {
        System.out.printf("%-" + width + "s", content);
    }

    // Helper method to print line separator
    private static void printLine(int totalWidth) {
        for (int i = 0; i < totalWidth; i++) {
            System.out.print("-");
        }
        System.out.println();
    }

    // Helper method to get attribute value via getter method or direct field access
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

/*

Example usage:

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        List<Person> people = Arrays.asList(
            new Person("John", "Smith", 28, LocalDateTime.now()),
            new Person("Alexander", "Williams", 33, LocalDateTime.now().minusDays(1)),
            new Person("Emily", "Johnson", 25, LocalDateTime.now().plusDays(1))
        );

        // Specify which attributes to show in the table and their headers with optional callbacks
        LinkedHashMap<String, TableBuilder.ColumnMapping> columnMapping = new LinkedHashMap<>();
        columnMapping.put("firstName", new TableBuilder.ColumnMapping("First Name", null)); // No transformation
        columnMapping.put("lastName", new TableBuilder.ColumnMapping("Last Name", null)); // No transformation
        columnMapping.put("dateTime", new TableBuilder.ColumnMapping("Date/Time", 
            (val) -> ((LocalDateTime) val).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))); // Callback to format date
        columnMapping.put("age", new TableBuilder.ColumnMapping("Age", null)); // No transformation

        // Max column width for the table
        int maxLenCol = 20;

        // Generate and print the table with a title
        TableBuilder.createTable("Person Table", people, columnMapping, maxLenCol);
    }
}

*/