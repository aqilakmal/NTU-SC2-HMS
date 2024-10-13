package controller.data;

import entity.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manages replenishment request data operations including loading from and saving to CSV files,
 * as well as basic CRUD operations for requests in the Hospital Management System.
 */
public class RequestDataManager {

    private static final String REQUEST_FILE = "src/main/resources/data/requests.csv";

    /**
     * List of replenishment requests in the system.
     */
    private static List<Request> requests;

    /**
     * Constructs a new RequestDataManager with an empty list of requests.
     */
    public RequestDataManager() {
        requests = new ArrayList<>();
    }

    /**
     * Loads request data from a CSV file.
     * @throws IOException If there's an error reading the file.
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
     * Saves request data to a CSV file.
     * @throws IOException If there's an error writing to the file.
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
     * [CREATE] Adds a new request to the system.
     * @param newRequest The new Request object to add.
     * @throws IllegalArgumentException If a request with the same ID already exists.
     */
    public void addRequest(Request newRequest) throws IllegalArgumentException {
        if (getRequestByID(newRequest.getRequestID()) != null) {
            throw new IllegalArgumentException("A request with ID " + newRequest.getRequestID() + " already exists.");
        }
        requests.add(newRequest);
    }

    /**
     * [READ] Retrieves the list of all requests.
     * @return List of Request objects.
     */
    public List<Request> getRequests() {
        return new ArrayList<>(requests); // Return a copy to preserve encapsulation
    }

    /**
     * [READ] Retrieves a request by its ID.
     * @param requestID The ID of the request to retrieve.
     * @return The Request object if found, null otherwise.
     */
    public Request getRequestByID(String requestID) {
        return requests.stream()
                .filter(request -> request.getRequestID().equals(requestID))
                .findFirst()
                .orElse(null);
    }

    /**
     * [READ] Retrieves a list of pending requests.
     * @return List of pending Request objects.
     */
    public List<Request> getPendingRequests() {
        return requests.stream()
                .filter(request -> request.getStatus() == Request.RequestStatus.PENDING)
                .collect(Collectors.toList());
    }

    /**
     * [UPDATE] Updates an existing request's information.
     * @param updatedRequest The Request object with updated information.
     * @throws IllegalArgumentException If the request doesn't exist.
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
