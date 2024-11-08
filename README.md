# TODOs

#### System

* ✅ Function to import and save program user data to csv

#### Admin

* ✅ Manage Staff
* ✅ View Appointment Details
* ✅ Manage Medication Inventory
* ✅ Approve Replenistment Requests
* ✅ Logout

#### Faqih Todos:

* Go through code and reorganise
* Implement todo functions for the rest of the classes
* Setup testing (optional)

#### Yong Huat's Todos:

Completed
* Fix Accept/Decline at (y/n) prompt
* Print Slots then prompt user
* After completing appointment, provide Outcome ID
* When complete action, dont go back to main menu but the action menu
* In upcoming appointments, choose which appointment to view the details of appointment
* Order by date, slots
* Edit History such that doctor can access past patients as well
* View and update appointment outcomes
* Function 2: Allow doctor to add History

Not Completed


Doctor Actions 
Test Case 9: View Patient Medical Records 
● Doctor views medical records of patients under their care. 
● Verify that the patient's medical record is displayed, including all relevant medical history. 
Tested: 
>Passed normal inputs (input a valid patient ID to view details of a patient)
>Tried inputing invalid Patient IDs and it prompts to input a valid ID

Test Case 10: Update Patient Medical Records 
● Doctor adds a new diagnosis and treatment plan to a patient's medical record. 
● Verify that the medical record is updated successfully, reflecting the new information.
Tested:
>Passed normal inputs (input a valid patient ID and choose to add, update or remove history)
>Tried inputing invalid Patient IDs and it prompts to input a valid ID
>Tried inputing invalid History IDs and it prompts to input a valid ID
>Removing all histories, adding histories & updating histories

Test Case 11: View Personal Schedule 
● Doctor views their personal appointment schedule. 
● Verify that the system displays the doctor's upcoming appointments and availability slots.
Tested:
>Passed normal inputs (input a valid slot ID to view details of a slot)
>Tried inputing invalid Slot IDs and it prompts to input a valid ID

Test Case 12: Set Availability for Appointments 
● Doctor sets or updates their availability for patient appointments. 
● Verify that the doctor's availability is updated, and patients can see the new slots when scheduling appointments. 
Tested:
>Passed normal inputs (choose whether to add, update or remove slots, then selecting the slot to update/remove or add a new slot)
>Tried inputing invalid Slot IDs and it prompts to input a valid ID
>Passed invalid date format, time format and when end time is before start time
>Removing all slots, adding slots & updating slots

Test Case 13: Accept or Decline Appointment Requests 
● Doctor accepts or declines an appointment request from a patient. 
● Verify that the appointment status changes to "confirmed" when accepted or “cancelled” when declined , and the patient is able to see the updated status of the appointment. 
Tested:
>Passed normal inputs(input valid a slot ID and then input 'y' or 'n')
>Tried inputing invalid Slot IDs and it prompts to input a valid ID

Test Case 14: View Upcoming Appointments 
● Doctor views all upcoming confirmed appointments. 
● Verify that the system displays a list of all upcoming appointments with patient details and appointment times.
Tested:
>Passed normal inputs (input a valid slot ID to view details of a booked slot)
>Tried inputing invalid Slot IDs and it prompts to input a valid ID

Test Case 15: Record Appointment Outcome 
● Doctor records the outcome of a completed appointment. 
● Verify that the appointment outcome is recorded, and relevant updates are visible to the patient under “View Past Appointment Outcome Records”.
Tested:
>Passed normal inputs (input a valid slot ID then inputing service provided, consultation and prescriptions)
>Tried inputing invalid Slot IDs and it prompts to input a valid ID
>Tried inputing invalid Medication IDs and it prompts to input a valid ID
>Tried inputing non-integer quantity and it prompts to input an integer

Test Case: View and Update Appointment Outcome
● Doctor updates the outcome of a completed appointment. 
● Verify that the appointment outcome is recorded, and relevant updates are visible to the patient under “View Past Appointment Outcome Records”.
Tested:
>Passed normal inputs (input a valid appointment ID then inputing service provided, consultation and prescriptions)
>Tried inputing invalid Appointment IDs and it prompts to input a valid ID
>Tried inputing invalid Medication IDs and it prompts to input a valid ID
>Tried inputing non-integer quantity and it prompts to input an integer