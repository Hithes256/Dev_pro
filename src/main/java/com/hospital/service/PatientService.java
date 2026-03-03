package com.hospital.service;

import com.hospital.model.Patient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PatientService {

    private List<Patient> patients = new ArrayList<>();

    public PatientService() {
        patients.add(new Patient(1L, "John", "Fever"));
        patients.add(new Patient(2L, "Alice", "Cold"));
    }

    public List<Patient> getAllPatients() {
        return patients;
    }

    public void addPatient(Patient patient) {
        patients.add(patient);
    }
}