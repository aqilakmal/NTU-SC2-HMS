package entity;

/**
 * Represents a request to replenish medication stock in the Hospital Management System.
 */
public class Request {

    /**
     * Enum representing the possible statuses of a replenishment request.
     */
    public enum RequestStatus {
        PENDING, APPROVED
    }
    
    /**
     * Unique identifier for the replenishment request.
     */
    private String requestID;
    
    /**
     * Unique identifier for the medication to be replenished.
     */
    private String medicationID;
    
    /**
     * The quantity of medication requested.
     */
    private int quantity;
    
    /**
     * The current status of the request.
     */
    private RequestStatus status;
    
    /**
     * The id of the pharmacist who created the request.
     */
    private String requestedBy;
    
    /**
     * The id of the administrator who approved the request (null if not yet approved).
     */
    private String approvedBy;
    
    /**
     * Constructor for Request.
     * @param requestID The unique identifier for the replenishment request
     * @param medication The medication to be replenished
     * @param quantity The quantity of medication requested
     * @param status The current status of the request
     * @param requestedBy The pharmacist who created the request
     * @param approvedBy The administrator who approved the request (null if not yet approved)
     */
    public Request(String requestID, String medicationID, int quantity, RequestStatus status, String requestedBy) {
        this.requestID = requestID;
        this.medicationID = medicationID;
        this.quantity = quantity;
        this.status = status;
        this.requestedBy = requestedBy;
        this.approvedBy = null;
    }
    
    /**
     * Get the unique identifier for the replenishment request.
     * @return The request ID
     */
    public String getRequestID() {
        return requestID;
    }
    
    /**
     * Get the medication to be replenished.
     * @return The medication object
     */
    public String getMedicationID() {
        return medicationID;
    }
    
    /**
     * Get the quantity of medication requested.
     * @return The quantity of medication
     */
    public int getQuantity() {
        return quantity;
    }
    
    /**
     * Get the current status of the request.
     * @return The status of the request
     */
    public RequestStatus getStatus() {
        return status;
    }
    
    /**
     * Get the pharmacist who created the request.
     * @return The pharmacist who created the request
     */
    public String getRequestedBy() {
        return requestedBy;
    }
    
    /**
     * Get the administrator who approved the request (null if not yet approved).
     * @return The administrator who approved the request, or null if not yet approved
     */
    public String getApprovedBy() {
        return approvedBy;
    }
    
    /**
     * Set the status of the request.
     * @param status The new status of the request
     */
    public void setStatus(RequestStatus status) {
        this.status = status;
    }
    
    /**
     * Set the administrator who approved the request.
     * @param approvedBy The administrator who approved the request
     */
    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }
}
