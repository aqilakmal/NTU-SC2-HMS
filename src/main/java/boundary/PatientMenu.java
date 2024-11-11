package boundary;

import controller.*;
import entity.*;
import utility.*;
import java.util.Scanner;
import java.util.List;
import java.util.InputMismatchException;
import java.util.LinkedHashMap;

/**
 * Interface for patient interactions in the Hospital Management System.
 */
public class PatientMenu {
    
    private PatientController patientController;
    private Scanner scanner;

    /**
     * Constructor for PatientMenu.
     * 
     * @param patientController The PatientController instance
     */
    public PatientMenu(PatientController patientController) {
        this.patientController = patientController;
        this.scanner = new Scanner(System.in);
    }

    /**
     * [MAIN MENU] Displays the menu options available to the patient.
     * Takes in the user input and navigate to the correct function.
     * Loops until the user logs out
     */
    public void displayMenu() {
        while (true) {
            try {
                ConsoleUtility.printHeader("PATIENT MENU");
                System.out.println("{1} View Medical Record"); // ok (include  alittle bit more)
                System.out.println("{2} Update Personal Information"); //ok
                System.out.println("{3} View Available Appointment Slots");//ok
                System.out.println("{4} Schedule an Appointment");//ok
                System.out.println("{5} Reschedule an Appointment"); //ok 
                System.out.println("{6} Cancel an Appointment");//ok 
                System.out.println("{7} View Scheduled Appointments"); // ok
                System.out.println("{8} View Past Appointment Outcome Records"); //ok
                System.out.println("{9} Logout"); //ok
                System.out.print("Enter your choice: ");

                /**
                 * choice takes in the user's input and navigates to the correct action choosen by the user
                 */
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1: viewMedicalRecord(); break;
                    case 2: updatePersonalInformation(); break;
                    case 3: viewAvailableAppointmentSlots(); break;
                    case 4: scheduleAppointment(); break;
                    case 5: rescheduleAppointment(); break;
                    case 6: cancelAppointment(); break;
                    case 7: viewScheduledAppointments(); break;
                    case 8: viewPastAppointmentOutcomeRecords(); break;
                    case 9: System.out.println("Logging out and returning to home screen..."); return;
                    default: System.out.println("Invalid choice. Please enter a number between 1 and 9.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear the invalid input
            } catch (Exception e) {
                System.err.println("An unexpected error occurred: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * [OPTION 1] Views the patient's medical record.
     * This method retrieves the patient's medical history data from the patient controller and passes it to 
       the display method for formatting and output.
     * Using a column mapping for a structured table view.
     * 
     *  @param history The list of objects representing the patient's medical history records.
     */
    private void viewMedicalRecord() {
        // TODO: Implement logic to view medical record
        System.out.println("");
        List<History> histories = patientController.getPatientMedicalHistory();
        displayMadicalRecords(histories);


    }

    private void displayMadicalRecords(List<History> history){
        LinkedHashMap<String, TableBuilder.ColumnMapping> columnMapping = new LinkedHashMap<>();
        columnMapping.put("HistoryID", new TableBuilder.ColumnMapping("historyID", null));
        columnMapping.put("Diagnosis", new TableBuilder.ColumnMapping("diagnosis", null));
        columnMapping.put("DiagnosisDate", new TableBuilder.ColumnMapping("DiagnosisDate", null));
        columnMapping.put("treatment", new TableBuilder.ColumnMapping("treatment", null));


        TableBuilder.createTable("Medical history", history, columnMapping, 20);
    }

    /**
     * [OPTION 2] Updates the patient's personal information.
     * 
     * This method allows the patient to choose between updating their password, contact number, or email address.
     * The appropriate update method in PatientController is called based on the user's choice.
     */
        public void updatePersonalInformation() {
            Scanner scanner = new Scanner(System.in);
    
            System.out.println("Select the information to update:");
            System.out.println("{1} Change password");
            System.out.println("{2} Change contact number");
            System.out.println("{3} Change email address");
    
            int updateChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
    
            switch (updateChoice) {
                case 1:
                    System.out.print("Enter new password: ");
                    String newPassword = scanner.nextLine();
                    patientController.updatePassword(newPassword);
                    System.out.println("Password updated successfully.");
                    break;
    
                case 2:
                    System.out.print("Enter new contact number: ");
                    String newContactNumber = scanner.nextLine();
                    patientController.updateContactNumber(newContactNumber);
                    System.out.println("Contact number updated successfully.");
                    break;
    
                case 3:
                    System.out.print("Enter new email address: ");
                    String newEmailAddress = scanner.nextLine();

                    // Assuming you have the userID stored or can retrieve it in the boundary
                    patientController.updateEmailAddress(newEmailAddress);
                    System.out.println("Email address updated successfully.");
    break;
    
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 3.");
            }
        }
    
    
    /**
     * [OPTION 3] Views available appointment slots.
     * 
     * The method first displays the available doctors and then prompt's the user to select a doctor 
       then retrieves the list of available slots for that doctor 
       and displays them
     */
    private void viewAvailableAppointmentSlots() {
        // TODO: Implement logic to view available appointment slots

       // Step 1: Display list of doctors
       List<Doctor> doctors = patientController.getAllDoctors();
       if (doctors.isEmpty()) {
           System.out.println("No doctors are currently available in the system.");
           return;
       }

       System.out.println("");
       displayDoctorList(doctors);

    // Step 2: Prompt user to select a doctor
    String selectedDoctorID = ConsoleUtility.validateInput("\nEnter the Doctor ID to view available slots: ",
            input -> doctors.stream().anyMatch(d -> d.getUserID().equals(input)));

    Doctor selectedDoctor = patientController.getDoctorByID(selectedDoctorID);

    if (selectedDoctor == null) {
        System.out.println("Invalid Doctor ID. Please try again.");
        return;
    }
    //displayDoctorList(doctors);
    // Step 3: Display available slots for the selected doctor
    List<Slot> availableSlots = patientController.getAvailableSlotsForDoctor(selectedDoctorID);
    if (availableSlots.isEmpty()) {
        System.out.println("\nNo available slots for the selected doctor.");
        return;
    }

    displayAvailableSlots(availableSlots);

    }

    /**
     * [OPTION 4] Schedules a new appointment.
     * 
     * Displays a list of doctors and prompts the user to select the doctor to fix an appointment with
     * After the user selects the doctor, then it displays the available time slots the selected doctor has and prompts the user to select a time slot to fix the appointment
     * Shows the appointment details and confirms with the patient
     * Upon pressing yes, schedules the appointment and waits for the doctor's approval
     */
    private void scheduleAppointment() {
        try {
            ConsoleUtility.printHeader("SCHEDULE AN APPOINTMENT", false);

            // Step 1: Display list of doctors
            List<Doctor> doctors = patientController.getAllDoctors();
            if (doctors.isEmpty()) {
                System.out.println("No doctors are currently available in the system.");
                return;
            }

            System.out.println("");
            displayDoctorList(doctors);

            // Step 2: Prompt user to select a doctor
            String selectedDoctorID = ConsoleUtility.validateInput("\nEnter the Doctor ID to schedule with: ",
                    input -> doctors.stream().anyMatch(d -> d.getUserID().equals(input)));

            Doctor selectedDoctor = patientController.getDoctorByID(selectedDoctorID);

            if (selectedDoctor == null) {
                System.out.println("Invalid Doctor ID. Please try again.");
                return;
            }

            // Step 3: Display available slots for the selected doctor
            List<Slot> availableSlots = patientController.getAvailableSlotsForDoctor(selectedDoctorID);
            if (availableSlots.isEmpty()) {
                System.out.println("\nNo available slots for the selected doctor.");
                return;
            }

            displayAvailableSlots(availableSlots);

            // Step 4: Prompt user to select a slot
            String selectedSlotID = ConsoleUtility.validateInput("\nEnter the Slot ID to schedule: ",
                    input -> availableSlots.stream().anyMatch(s -> s.getSlotID().equals(input)));

            Slot selectedSlot = patientController.getSlotByID(selectedSlotID);

            if (selectedSlot == null) {
                System.out.println("Invalid Slot ID. Please try again.");
                return;
            }

            // Step 5: Verify appointment information
            System.out.println("\nAppointment Details:");
            System.out.println("Doctor: " + selectedDoctor.getName());
            System.out.println("Date: " + selectedSlot.getDate());
            System.out.println("Time: " + selectedSlot.getStartTime() + " - " + selectedSlot.getEndTime());

            if (!ConsoleUtility.getConfirmation("Do you want to confirm this appointment?")) {
                System.out.println("Appointment scheduling cancelled.");
                return;
            }

            // Step 6: Schedule the appointment
            boolean success = patientController.scheduleAppointment(selectedDoctorID, selectedSlotID);
            if (success) {
                System.out.println("Appointment scheduled successfully. Waiting for doctor's approval.");
            } else {
                System.out.println("Failed to schedule the appointment. Please try again later.");
            }

        } catch (Exception e) {
            System.err.println("An error occurred while scheduling the appointment: " + e.getMessage());
        }
    }

    /**
     * Displays the list of doctors.
     * 
     * presents a table of available doctors with their IDs, names, and specializations.
     * 
     * @param doctors The list of doctors to display
     */
    private void displayDoctorList(List<Doctor> doctors) {
        LinkedHashMap<String, TableBuilder.ColumnMapping> columnMapping = new LinkedHashMap<>();
        columnMapping.put("userID", new TableBuilder.ColumnMapping("Doctor ID", null));
        columnMapping.put("name", new TableBuilder.ColumnMapping("Name", null));
        columnMapping.put("specialization", new TableBuilder.ColumnMapping("Specialization", null));

        TableBuilder.createTable("Available Doctors", doctors, columnMapping, 20);
    }

    /**
     * Displays the list of available slots.
     * 
     * presents a table of available time slots with their IDs, dates, start times, and end times.
     * 
     * @param slots The list of available slots to display
     */
    private void displayAvailableSlots(List<Slot> slots) {
        LinkedHashMap<String, TableBuilder.ColumnMapping> columnMapping = new LinkedHashMap<>();
        columnMapping.put("slotID", new TableBuilder.ColumnMapping("Slot ID", null));
        columnMapping.put("date", new TableBuilder.ColumnMapping("Date", null));
        columnMapping.put("startTime", new TableBuilder.ColumnMapping("Start Time", null));
        columnMapping.put("endTime", new TableBuilder.ColumnMapping("End Time", null));

        TableBuilder.createTable("Available Slots", slots, columnMapping, 15);
    }

    /**
     * [OPTION 5] Reschedules an existing appointment.
     * 
     * 
     * Displays the patient the scheduled(requested) appointments and  promta the user to enter the appointment to reschedule
     * Retrieves the appointment details
     * Displays available slots for the doctor and then prompts the user to select a new slot and validates the selection
     * After confirming, updates the appointment to the new slot, makes the old slot available
     */
    private void rescheduleAppointment() {

        List<Appointment> appointments = patientController.getreq(); // Fetch scheduled appointments
        displayreq(appointments); // Pass the appointments to the display method
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the Appointment ID to reschedule: ");
        String appointmentID = scanner.nextLine();
        
    
        // Retrieve the appointment from the data manager
        Appointment appointment = patientController.getAppointmentByID(appointmentID);

    
        if (appointment != null) {
            // Check if the appointment can be rescheduled
            if (appointment.getStatus() == Appointment.AppointmentStatus.CONFIRMED || 
                appointment.getStatus() == Appointment.AppointmentStatus.REQUESTED) {
    
                List<Slot> availableSlots = patientController.getAvailableSlotsForDoctor(appointment.getDoctorID());
                if (availableSlots.isEmpty()) {
                    System.out.println("\nNo available slots for the selected doctor.");
                    return;
                }
                
                // Display available slots
                displayAvailableSlots(availableSlots);
    
                // Ask the user to select a new slot
                System.out.print("\nEnter the Slot ID to reschedule to: ");
                String selectedSlotID = scanner.nextLine();
    
                // Validate the selected slot
                Slot selectedSlot = patientController.getSlotByID(selectedSlotID);
                if (selectedSlot == null || !availableSlots.contains(selectedSlot)) {
                    System.out.println("Invalid Slot ID. Please try again.");
                    return;
                }
    
                // Confirm appointment details
                System.out.println("\nNew Appointment Details:");
                System.out.println("Doctor: " + patientController.getDoctorByID(appointment.getDoctorID()).getName());
                System.out.println("Date: " + selectedSlot.getDate());
                System.out.println("Time: " + selectedSlot.getStartTime() + " - " + selectedSlot.getEndTime());
    
                // Confirm the rescheduling
                if (ConsoleUtility.getConfirmation("Do you want to confirm this rescheduling?")) {
                    // Update the appointment with the new slot and status
                   patientController.scheduleAppointment(patientController.getDoctorByID(appointment.getDoctorID()).getName(), selectedSlotID);  

                    Appointment selectedAppointment = patientController.getAppointmentByID(appointmentID);
                    Slot slot = patientController.getSlotByID(selectedAppointment.getSlotID());
                    if (slot != null) {
                        // Make the slot available for booking again
                        slot.setStatus(Slot.SlotStatus.AVAILABLE);
                    }
    
                    System.out.println("Appointment rescheduled successfully.");
                } else {
                    System.out.println("Appointment rescheduling cancelled.");
                }
            } else {
                System.out.println("Appointment " + appointmentID + " cannot be rescheduled as it is not in a reschedulable status.");
            }
        } else {
            System.out.println("Appointment with ID " + appointmentID + " not found.");
        }
    }


    private void displayreq(List<Appointment> appointments) {
        // Define the mapping of appointment attributes to table columns
        LinkedHashMap<String, TableBuilder.ColumnMapping> columnMapping = new LinkedHashMap<>();
        columnMapping.put("appointmentID", new TableBuilder.ColumnMapping("Appointment ID", null));
        columnMapping.put("patientID", new TableBuilder.ColumnMapping("Patient ID", null));
        columnMapping.put("doctorID", new TableBuilder.ColumnMapping("Doctor ID", null));
        columnMapping.put("status", new TableBuilder.ColumnMapping("Status", null));
    
        // Create and display the table using TableBuilder
        TableBuilder.createTable("Scheduled Appointments", appointments, columnMapping, 15); // Pass the appointments list
    }


    
    // Placeholder method for generating a new slot ID
    private String generateNewSlotID(String newDate, String newTime) {
        // Implement logic to generate a new slot ID based on the date and time
        return newDate + "T" + newTime; // Example format; adapt as needed*/
    }


    
    

    /**
     * [OPTION 6] Cancels an existing appointment.
     * 
     * Allows the patient to cancel an appointment by entering the AppointmentID.
     * If the appointment is confirmed or requested, it is updated to "CANCELLED" status, and the
       associated time slot is made available for booking again.
     */
    private void cancelAppointment() {
        // TODO: Implement logic to cancel an appointment

        Scanner scanner = new Scanner(System.in); // Scanner for user input
        System.out.print("Enter the Appointment ID to cancel: ");
        String appointmentID = scanner.nextLine(); // Read the appointment ID from the user
      
        // Retrieve the appointment from the data manager
        Appointment appointment = patientController.getAppointmentByID(appointmentID);
        //System.out.println(appointment.getAppointmentID());
    
        if (appointment != null) {
            // Check the current status of the appointment
            if (appointment.getStatus() == Appointment.AppointmentStatus.CONFIRMED || 
                appointment.getStatus() == Appointment.AppointmentStatus.REQUESTED) {
                // Update the appointment status to CANCELLED
                appointment.setStatus(Appointment.AppointmentStatus.CANCELLED);
                System.out.println("Appointment " + appointmentID + " has been cancelled.");
                Appointment selectedAppointment = patientController.getAppointmentByID(appointmentID);
                Slot slot = patientController.getSlotByID(selectedAppointment.getSlotID());
                if (slot != null) {
                    // Make the slot available for booking again
                    slot.setStatus(Slot.SlotStatus.AVAILABLE);
                }

            } else {
                System.out.println("Appointment " + appointmentID + " cannot be cancelled as it is not confirmed or requested.");
            }
        } else {
            System.out.println("Appointment with ID " + appointmentID + " not found.");
        }
        
        
    }

    /**
     * [OPTION 7] Views scheduled appointments.
     * 
     * Fetches a list of scheduled appointments for the patient, then displays the appointments in a table format
     */
private void viewScheduledAppointments() {
    System.out.println("");
    List<Appointment> appointments = patientController.getScheduledAppointments(); // Fetch scheduled appointments
    displayScheduledAppointments(appointments); // Pass the appointments to the display method
}

/**
 * Maps the attributes of each appointment to columns in a table, and then calls TableBuilder to generate and display the table
 * @param appointments A list of objects representing the scheduled appointments to display.
 */

private void displayScheduledAppointments(List<Appointment> appointments) {
    // Define the mapping of appointment attributes to table columns
    LinkedHashMap<String, TableBuilder.ColumnMapping> columnMapping = new LinkedHashMap<>();
    columnMapping.put("appointmentID", new TableBuilder.ColumnMapping("Appointment ID", null));
    columnMapping.put("patientID", new TableBuilder.ColumnMapping("Patient ID", null));
    columnMapping.put("doctorID", new TableBuilder.ColumnMapping("Doctor ID", null));
    columnMapping.put("status", new TableBuilder.ColumnMapping("Status", null));
    columnMapping.put("outcomeID", new TableBuilder.ColumnMapping("Outcome ID", null));

    // Create and display the table using TableBuilder
    TableBuilder.createTable("Scheduled Appointments", appointments, columnMapping, 15); // Pass the appointments list
}


    /**
     * [OPTION 8] Views past appointment outcome records.
     * 
     * Fetches a list of past appointmenr outcome records for the patient, then displays them in a table format
     */
    private void viewPastAppointmentOutcomeRecords() {
        // Fetch past appointments
        System.out.println("");
        List<Appointment> appointments = patientController.getPastAppointments(); // Fetch past appointments
        displayPastAppointments(appointments); // Pass the appointments to the display method
    }

    /**
     * Maps the attributes of each appointment to columns in a table, and then calls TableBuilder to generate and display the table
     * 
     * @param appointments A list of objects representing the past appointments to display.
     */
    
    private void displayPastAppointments(List<Appointment> appointments) {
        // Define the mapping of appointment attributes to table columns
        LinkedHashMap<String, TableBuilder.ColumnMapping> columnMapping = new LinkedHashMap<>();
        columnMapping.put("appointmentID", new TableBuilder.ColumnMapping("Appointment ID", null));
        columnMapping.put("patientID", new TableBuilder.ColumnMapping("Patient ID", null));
        columnMapping.put("doctorID", new TableBuilder.ColumnMapping("Doctor ID", null));
        columnMapping.put("status", new TableBuilder.ColumnMapping("Status", null));
    
        // Create and display the table using TableBuilder
        TableBuilder.createTable("Past Appointments", appointments, columnMapping, 15); // Pass the appointments list
    }
    
}


