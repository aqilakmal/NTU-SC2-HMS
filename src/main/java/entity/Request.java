package entity;

/**
 * Represents a medication replenishment request in the Hospital Management System. Requests are created
 * by pharmacists when medication stock levels are low and need to be replenished.
 * 
 * Key components include:
 * - Unique request identifier
 * - Reference to the medication that needs replenishment
 * - Quantity requested
 * - Current request status (pending or approved)
 * - IDs of requesting pharmacist and approving administrator
 * 
 * The Request class supports the inventory management workflow by tracking replenishment
 * requests from creation through approval by administrators.
 *
 * @author Group 7
 * @version 1.0
 */
public class Request {

    /**
     * Enum representing the possible statuses of a replenishment request.
     * Used to track the approval state of requests in the system.
     * Supports PENDING (newly created) and APPROVED (fulfilled by administrator) states.
     */
    public enum RequestStatus {
        PENDING, APPROVED
    }
    
    /**
     * Unique identifier for the replenishment request.
     * Used to track and reference specific requests in the system.
     */
    private String requestID;
    
    /**
     * Unique identifier for the medication to be replenished.
     * References the specific medication that needs stock replenishment.
     */
    private String medicationID;
    
    /**
     * The quantity of medication requested.
     * Specifies the amount of medication stock to be replenished.
     */
    private int quantity;
    
    /**
     * The current status of the request.
     * Tracks whether the request has been approved by an administrator.
     */
    private RequestStatus status;
    
    /**
     * The id of the pharmacist who created the request.
     * References the pharmacist who initiated the replenishment request.
     */
    private String requestedBy;
    
    /**
     * The id of the administrator who approved the request.
     * References the administrator who approved the replenishment.
     * Will be null if request is not yet approved.
     */
    private String approvedBy;
    
    /**
     * Constructor for creating a new Request instance in the system.
     * Initializes a replenishment request with all required details including
     * medication reference, quantity needed, and tracking information.
     * Validates and sets up the request record for processing in the system.
     *
     * @param requestID The unique identifier for the replenishment request
     * @param medicationID The unique identifier for the medication to be replenished
     * @param quantity The quantity of medication requested
     * @param status The current status of the request
     * @param requestedBy The pharmacist who created the request
     * @param approvedBy The administrator who approved the request (null if not yet approved)
     */
    public Request(String requestID, String medicationID, int quantity, RequestStatus status, String requestedBy, String approvedBy) {
        this.requestID = requestID;
        this.medicationID = medicationID;
        this.quantity = quantity;
        this.status = status;
        this.requestedBy = requestedBy;
        this.approvedBy = approvedBy;
    }
    
    /**
     * Retrieves the unique identifier for this replenishment request.
     * This method provides access to the request's tracking ID which is used
     * throughout the system to reference and manage this specific request.
     *
     * @return The unique request ID string
     */
    public String getRequestID() {
        return requestID;
    }
    
    /**
     * Retrieves the identifier of the medication to be replenished.
     * This method provides access to the specific medication ID that
     * is associated with this replenishment request.
     *
     * @return The medication ID string
     */
    public String getMedicationID() {
        return medicationID;
    }
    
    /**
     * Retrieves the quantity of medication requested.
     * This method provides access to the amount of medication stock
     * that was requested for replenishment by the pharmacist.
     *
     * @return The requested quantity as an integer
     */
    public int getQuantity() {
        return quantity;
    }
    
    /**
     * Retrieves the current status of the replenishment request.
     * This method provides access to whether the request is still
     * pending or has been approved by an administrator.
     *
     * @return The current RequestStatus enum value
     */
    public RequestStatus getStatus() {
        return status;
    }
    
    /**
     * Retrieves the ID of the pharmacist who created the request.
     * This method provides access to the identifier of the pharmacist
     * who initiated this replenishment request in the system.
     *
     * @return The pharmacist's user ID string
     */
    public String getRequestedBy() {
        return requestedBy;
    }
    
    /**
     * Retrieves the ID of the administrator who approved the request.
     * This method provides access to the identifier of the administrator
     * who processed and approved this replenishment request.
     *
     * @return The administrator's user ID string, or null if not yet approved
     */
    public String getApprovedBy() {
        return approvedBy;
    }
    
    /**
     * Updates the status of the replenishment request.
     * This method allows administrators to change the request status
     * when processing and approving replenishment requests.
     *
     * @param status The new RequestStatus to set for this request
     */
    public void setStatus(RequestStatus status) {
        this.status = status;
    }
    
    /**
     * Updates the ID of the administrator who approved the request.
     * This method allows the system to record which administrator
     * processed and approved this replenishment request.
     *
     * @param approvedBy The user ID of the approving administrator
     */
    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    /**
     * Generates a string representation of the request.
     * This method creates a formatted string containing all relevant
     * request information for display or logging purposes.
     *
     * @return A string representation of the complete request record
     */
    @Override
    public String toString() {
        return "Request{" + requestID + "," + medicationID + "," + quantity + "," + status + "," + requestedBy + "," + approvedBy + "}";
    }
}
