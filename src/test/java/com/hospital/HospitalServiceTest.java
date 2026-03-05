package com.hospital;

import com.hospital.model.Patient;
import com.hospital.service.HospitalService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HospitalServiceTest {

    @Test
    void testAddPatient() {
        HospitalService service = new HospitalService();

        Patient patient = new Patient(1, "John", "john@gmail.com");
        service.addPatient(patient);

        assertEquals(1, service.getPatients().size());
        assertEquals("John", service.getPatients().get(0).getName());
    }

    @Test
    void testDefaultDoctorsLoaded() {
        HospitalService service = new HospitalService();

        assertEquals(3, service.getDoctors().size());
    }
}