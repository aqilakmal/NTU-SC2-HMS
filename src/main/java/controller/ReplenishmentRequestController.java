package controller;

import entity.ReplenishmentRequest;
import java.util.List;

/**
 * Manages medication replenishment requests in the Hospital Management System.
 */
public class ReplenishmentRequestController {
    
    /**
     * Submits a new replenishment request.
     * @param request The ReplenishmentRequest object to submit
     * @throws ReplenishmentRequestException if the request is invalid or cannot be submitted
     */
    public void submitRequest(ReplenishmentRequest request) throws ReplenishmentRequestException {
        // TODO: Implement logic to submit replenishment request
    }
    
    /**
     * Approves a pending replenishment request.
     * @param requestID The unique identifier of the request to approve
     * @param administratorID The unique identifier of the administrator approving the request
     * @throws ReplenishmentRequestException if the request doesn't exist, is not pending, or the administrator is invalid
     */
    public void approveRequest(String requestID, String administratorID) throws ReplenishmentRequestException {
        // TODO: Implement logic to approve replenishment request
    }
    
    /**
     * Retrieves all pending replenishment requests.
     * @return List of pending ReplenishmentRequest objects
     */
    public List<ReplenishmentRequest> getPendingRequests() {
        // TODO: Implement logic to retrieve pending requests
        return null;
    }

    /**
     * ReplenishmentRequestException extends Exception to handle exceptions related to replenishment requests.
     */
    public class ReplenishmentRequestException extends Exception {
      
        /**
         * Constructor for ReplenishmentRequestException with a message.
         * @param message The message to display when the exception is thrown.
         */
        public ReplenishmentRequestException(String message) {
            super(message);
        }

        /**
         * Constructor for ReplenishmentRequestException with a message and a cause.
         * @param message The message to display when the exception is thrown.
         * @param cause The cause of the exception.
         */
        public ReplenishmentRequestException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}