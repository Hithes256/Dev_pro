package com.hospital.service;
import com.hospital.model.*;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HospitalService {
    private final List<Doctor>       doctors       = new ArrayList<>();
    private final List<Patient>      patients      = new ArrayList<>();
    private final List<Appointment>  appointments  = new ArrayList<>();
    private final List<Prescription> prescriptions = new ArrayList<>();
    private int patientId = 1;

    public HospitalService() {
        // Doctor name = exactly what doctor types at login
        // Frontend shows "Dr. Smith" in dropdown but stores value="Smith"
        // So doctorName in appointment = "Smith" (the login name)
        doctors.add(new Doctor(1, "Smith", "Cardiology"));
        doctors.add(new Doctor(2, "John",  "Neurology"));
        doctors.add(new Doctor(3, "Emily", "Orthopedics"));
    }

    public List<Doctor> getDoctors() { return doctors; }

    public List<Patient> getPatients() { return patients; }
    public void addPatient(Patient p) {
        boolean exists = patients.stream().anyMatch(x -> x.getName().equalsIgnoreCase(p.getName()));
        if (!exists) { p.setId(patientId++); patients.add(p); }
    }

    public List<Appointment> getAppointments() { return appointments; }
    public void bookAppointment(Appointment a) {
        a.setStatus("active");
        appointments.add(a);
    }

    public List<Appointment> getDoctorAppointments(String doctorName) {
        return appointments.stream()
            .filter(a -> a.getDoctorName().equalsIgnoreCase(doctorName) && "active".equals(a.getStatus()))
            .collect(Collectors.toList());
    }

    public List<Appointment> getPatientAppointments(String patientName) {
        return appointments.stream()
            .filter(a -> a.getPatientName().equalsIgnoreCase(patientName) && "active".equals(a.getStatus()))
            .collect(Collectors.toList());
    }

    public List<Prescription> getPrescriptions() { return prescriptions; }
    public void issuePrescription(Prescription rx) {
        prescriptions.add(rx);
        appointments.stream()
            .filter(a -> a.getPatientName().equalsIgnoreCase(rx.getPatientName())
                      && a.getDoctorName().equalsIgnoreCase(rx.getDoctorName())
                      && "active".equals(a.getStatus()))
            .findFirst()
            .ifPresent(a -> a.setStatus("done"));
    }

    public List<Prescription> getDoctorPrescriptions(String doctorName) {
        return prescriptions.stream()
            .filter(p -> p.getDoctorName().equalsIgnoreCase(doctorName))
            .collect(Collectors.toList());
    }

    public List<Prescription> getPatientPrescriptions(String patientName) {
        return prescriptions.stream()
            .filter(p -> p.getPatientName().equalsIgnoreCase(patientName))
            .collect(Collectors.toList());
    }
}