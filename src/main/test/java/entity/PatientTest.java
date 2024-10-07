package main.test.java.entity;

import org.junit.Test;
import static org.junit.Assert.*;
import entity.Patient;

public class PatientTest {
    @Test
    public void testPatientCreation() {
        Patient patient = new Patient();
        // TODO: Implement test for Patient entity
        // For now, we'll just assert that the patient is not null
        assertNotNull(patient);
    }
}