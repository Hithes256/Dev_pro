package com.hospital.controller;

import com.hospital.model.*;
import com.hospital.service.HospitalService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class MainController {

    private final HospitalService service;

    public MainController(HospitalService service) {
        this.service = service;
    }

    @GetMapping("/doctors")
    public List<Doctor> getDoctors() {
        return service.getDoctors();
    }

    @PostMapping("/patients")
    public void addPatient(@RequestBody Patient patient) {
        service.addPatient(patient);
    }

    @GetMapping("/patients")
    public List<Patient> getPatients() {
        return service.getPatients();
    }

    @PostMapping("/appointments")
    public void bookAppointment(@RequestBody Appointment appointment) {
        service.bookAppointment(appointment);
    }

    @GetMapping("/appointments")
    public List<Appointment> getAppointments() {
        return service.getAppointments();
    }

    @GetMapping("/doctorAppointments/{doctorName}")
    public List<Appointment> getDoctorAppointments(@PathVariable String doctorName) {
        return service.getDoctorAppointments(doctorName);
    }
}