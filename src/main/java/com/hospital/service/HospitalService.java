package com.hospital.service;

import com.hospital.model.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HospitalService {

    private List<Doctor> doctors = new ArrayList<>();
    private List<Patient> patients = new ArrayList<>();
    private List<Appointment> appointments = new ArrayList<>();

    public HospitalService() {
        // Default Doctors
        doctors.add(new Doctor(1, "Dr. Smith", "Cardiology"));
        doctors.add(new Doctor(2, "Dr. John", "Neurology"));
        doctors.add(new Doctor(3, "Dr. Emily", "Orthopedics"));
    }

    public List<Doctor> getDoctors() {
        return doctors;
    }

    public List<Patient> getPatients() {
        return patients;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void addPatient(Patient patient) {
        patients.add(patient);
    }

    public void bookAppointment(Appointment appointment) {
        appointments.add(appointment);
    }

    public List<Appointment> getDoctorAppointments(String doctorName) {
        List<Appointment> result = new ArrayList<>();
        for (Appointment a : appointments) {
            if (a.getDoctorName().equals(doctorName)) {
                result.add(a);
            }
        }
        return result;
    }
}