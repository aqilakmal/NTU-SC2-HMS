# Hospital Management System (HMS)

## SCSC Group 7 Team Members
- Sethi Kavya
- Tan Yong Huat
- Saravanakaleeswaran Arun Karthick
- Muhammad Faqih Akmal

## Project Overview
The Hospital Management System (HMS) is a robust Java-based CLI application designed to automate and streamline hospital operations. The system facilitates efficient management of hospital resources, enhances patient care, and streamlines administrative processes.

### Key Features
- Multi-role user authentication system
- Patient record management
- Appointment scheduling and management
- Medication inventory control
- Staff management
- Prescription tracking

## System Architecture
HMS follows the B-C-E (Boundary-Controller-Entity) architecture pattern:
- **Boundary**: Handles user interface and input/output
- **Controller**: Manages business logic and data flow
- **Entity**: Represents system data models
- **Data Managers**: Handles data persistence and retrieval

## User Roles and Capabilities

### 1. Patient
- View personal medical records
- Update contact information
- Schedule/reschedule/cancel appointments
- View appointment history and outcomes

### 2. Doctor
- Access patient medical records
- Update patient diagnoses and treatments
- Manage appointment schedule
- Record appointment outcomes

### 3. Pharmacist
- Process medication prescriptions
- Monitor medication inventory
- Submit stock replenishment requests
- Update prescription status

### 4. Administrator
- Manage hospital staff
- Monitor appointment system
- Control medication inventory
- Process replenishment requests

## Developer Setup Guide

### Prerequisites
- Java Development Kit (JDK) 21
- Apache Maven 3.8+
- Git

### Installation Steps
1. Clone the repository:
   ```bash
   git clone [repository-url]
   cd HospitalManagementSystem
   ```

2. Build the project:
   ```bash
   mvn clean install
   ```

3. Run the application:
   ```bash
   ./run.sh
   ```
   or
   ```bash
   mvn compile exec:java
   ```

### Project Structure

```plaintext
HospitalManagementSystem/
├── src/
│ ├── main/
│ │ ├── java/
│ │ │ ├── boundary/ # User interface classes
│ │ │ ├── controller/ # Business logic
│ │ │ ├── entity/ # Data models
│ │ │ └── utility/ # Helper classes
│ │ └── resources/
│ │ └── data/ # CSV data files
│ └── test/
│ └── java/ # Test classes
├── pom.xml # Maven configuration
└── run.sh # Run script
```