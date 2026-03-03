package com.hospital.controller;

import com.hospital.model.Patient;
import com.hospital.service.PatientService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService service;

    public PatientController(PatientService service) {
        this.service = service;
    }

    @GetMapping
    public List<Patient> getPatients() {
        return service.getAllPatients();
    }

    @PostMapping
    public String addPatient(@RequestBody Patient patient) {
        service.addPatient(patient);
        return "Patient added successfully!";
    }
}