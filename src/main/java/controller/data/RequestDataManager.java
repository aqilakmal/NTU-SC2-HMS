package controller.data;

import entity.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manages replenishment request data operations in the Hospital Management System (HMS).
 * This class handles all request-related data persistence and CRUD operations,
 * including loading from and saving to CSV files, filtering requests, and managing
 * request statuses.
 * 
 * The class provides functionality for pharmacists to submit replenishment requests,
 * administrators to approve requests, and tracking request history. It maintains a
 * comprehensive record of all medication replenishment requests in the system.
 *
 * @author Group 7
 * @version 1.0
 */
public class RequestDataManager {

    private static final String REQUEST_FILE = "src/main/resources/data/requests.csv";

    /**
     * List of replenishment requests in the system.
     * Stores all request records loaded from CSV storage and maintains
     * the current state of requests in memory.
     */
    private static List<Request> requests;

    /**
     * Constructs a new RequestDataManager with an empty list of requests.
     * Initializes the requests list to store request data loaded from CSV storage.
     * This constructor is called during system initialization to prepare for request management.
     */
    public RequestDataManager() {
        requests = new ArrayList<>();
    }

    /**
     * Loads request data from the CSV file into memory.
     * Reads and parses each line of the CSV file to create Request objects.
     * Validates data format and skips invalid entries while logging them for review.
     * Clears existing requests before loading to ensure data consistency.
     *
     * @throws IOException If there's an error reading from the requests CSV file
     */
    public void loadRequestsFromCSV() throws IOException {
        requests.clear(); // Clear existing requests before loading
        System.out.println("\n[DEV] Loading: " + REQUEST_FILE);
        try (BufferedReader br = new BufferedReader(new FileReader(REQUEST_FILE))) {
            String line;
            br.readLine(); // Skip header line

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",", -1); // Use -1 to keep empty trailing fields
                if (values.length < 5) {
                    System.out.println("Skipping invalid line: " + line);
                    continue; // Skip invalid lines
                }

                String requestID = values[0].trim();
                String medicationID = values[1].trim();
                int quantity = Integer.parseInt(values[2].trim());
                Request.RequestStatus status = Request.RequestStatus.valueOf(values[3].trim());
                String requestedBy = values[4].trim();
                String approvedBy = values.length > 5 ? values[5].trim() : "";

                Request request = new Request(
                    requestID,
                    medicationID,
                    quantity,
                    status,
                    requestedBy,
                    approvedBy
                );
                requests.add(request);
                System.out.println("[DEV] " + request); // Debug output
            }
        }
        System.out.println("[DEV] Total requests loaded: " + requests.size()); // Debug output
    }

    /**
     * Saves all request data from memory to the CSV file.
     * Writes the complete list of requests to persistent storage.
     * Includes a header line with column names for data structure clarity.
     *
     * @throws IOException If there's an error writing to the requests CSV file
     */
    public void saveRequestsToCSV() throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(REQUEST_FILE))) {
            // Write header
            bw.write("requestID,medicationID,quantity,requestedBy,status,approvedBy");
            bw.newLine();

            for (Request request : requests) {
                StringBuilder sb = new StringBuilder();
                sb.append(request.getRequestID()).append(",");
                sb.append(request.getMedicationID()).append(",");
                sb.append(request.getQuantity()).append(",");
                sb.append(request.getStatus()).append(",");
                sb.append(request.getRequestedBy()).append(",");
                sb.append(request.getApprovedBy());
                
                bw.write(sb.toString());
                bw.newLine();
            }
        }
    }

    /**
     * Adds a new replenishment request to the system's records.
     * Validates that no duplicate request ID exists before adding.
     * Used by pharmacists to create new medication replenishment requests.
     *
     * @param newRequest The Request object containing the new request details
     * @throws IllegalArgumentException If a request with the same ID already exists
     */
    public void addRequest(Request newRequest) throws IllegalArgumentException {
        if (getRequestByID(newRequest.getRequestID()) != null) {
            throw new IllegalArgumentException("A request with ID " + newRequest.getRequestID() + " already exists.");
        }
        requests.add(newRequest);
    }

    /**
     * Retrieves a copy of all requests in the system.
     * Returns a new list to preserve encapsulation of the internal requests list.
     * Used for displaying complete request history to authorized users.
     *
     * @return A new List containing all Request objects in the system
     */
    public List<Request> getRequests() {
        return new ArrayList<>(requests); // Return a copy to preserve encapsulation
    }

    /**
     * Retrieves a specific request by its unique identifier.
     * Searches through all requests to find an exact ID match.
     * Used when accessing or updating a specific request entry.
     *
     * @param requestID The unique identifier of the request to retrieve
     * @return The Request object if found, null if no match exists
     */
    public Request getRequestByID(String requestID) {
        return requests.stream()
                .filter(request -> request.getRequestID().equals(requestID))
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves all pending replenishment requests in the system.
     * Filters the requests list to return only requests with PENDING status.
     * Used by administrators to view requests requiring approval.
     *
     * @return List of Request objects with PENDING status
     */
    public List<Request> getPendingRequests() {
        return requests.stream()
                .filter(request -> request.getStatus() == Request.RequestStatus.PENDING)
                .collect(Collectors.toList());
    }

    /**
     * Updates the information of an existing request in the system.
     * Locates the request by ID and replaces it with the updated version.
     * Used when administrators approve requests or when request status changes.
     *
     * @param updatedRequest The Request object containing the updated information
     * @throws IllegalArgumentException If no request exists with the given ID
     */
    public void updateRequest(Request updatedRequest) throws IllegalArgumentException {
        for (int i = 0; i < requests.size(); i++) {
            if (requests.get(i).getRequestID().equals(updatedRequest.getRequestID())) {
                requests.set(i, updatedRequest);
                return;
            }
        }
        throw new IllegalArgumentException("Request with ID " + updatedRequest.getRequestID() + " not found.");
    }
}
