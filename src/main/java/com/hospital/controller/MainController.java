package com.hospital.controller;
import com.hospital.model.*;
import com.hospital.service.HospitalService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class MainController {
    private final HospitalService service;
    public MainController(HospitalService service) { this.service = service; }

    // DOCTORS
    @GetMapping("/doctors")
    public List<Doctor> getDoctors() { return service.getDoctors(); }

    // PATIENTS
    @PostMapping("/patients")
    public void addPatient(@RequestBody Patient patient) { service.addPatient(patient); }

    @GetMapping("/patients")
    public List<Patient> getPatients() { return service.getPatients(); }

    // APPOINTMENTS
    @PostMapping("/appointments")
    public void bookAppointment(@RequestBody Appointment appointment) { service.bookAppointment(appointment); }

    @GetMapping("/appointments")
    public List<Appointment> getAppointments() { return service.getAppointments(); }

    // Active appointments for a specific doctor
    @GetMapping("/doctorAppointments/{doctorName}")
    public List<Appointment> getDoctorAppointments(@PathVariable String doctorName) {
        return service.getDoctorAppointments(doctorName);
    }

    // Active appointments for a specific patient
    @GetMapping("/patientAppointments/{patientName}")
    public List<Appointment> getPatientAppointments(@PathVariable String patientName) {
        return service.getPatientAppointments(patientName);
    }

    // PRESCRIPTIONS
    @PostMapping("/prescriptions")
    public void issuePrescription(@RequestBody Prescription prescription) {
        service.issuePrescription(prescription);
    }

    @GetMapping("/prescriptions")
    public List<Prescription> getPrescriptions() { return service.getPrescriptions(); }

    @GetMapping("/doctorPrescriptions/{doctorName}")
    public List<Prescription> getDoctorPrescriptions(@PathVariable String doctorName) {
        return service.getDoctorPrescriptions(doctorName);
    }

    @GetMapping("/patientPrescriptions/{patientName}")
    public List<Prescription> getPatientPrescriptions(@PathVariable String patientName) {
        return service.getPatientPrescriptions(patientName);
    }
}