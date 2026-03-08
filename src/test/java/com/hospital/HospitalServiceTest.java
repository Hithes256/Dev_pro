package com.hospital;

import com.hospital.model.Appointment;
import com.hospital.model.Patient;
import com.hospital.model.Prescription;
import com.hospital.service.HospitalService;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.*;

public class HospitalServiceTest {

    private HospitalService service;

    // Runs before EVERY test — gives each test a clean fresh service
    @BeforeMethod
    public void setUp() {
        service = new HospitalService();
    }

    // ── DOCTOR TESTS ──────────────────────────────────────────────────────────

    @Test(description = "Service should pre-load 3 default doctors on startup")
    public void testDefaultDoctorsLoaded() {
        assertEquals(service.getDoctors().size(), 3);
    }

    @Test(description = "Default doctors must be Smith, John and Emily")
    public void testDoctorNamesAreCorrect() {
        List<String> names = service.getDoctors()
                .stream().map(d -> d.getName()).toList();

        assertTrue(names.contains("Smith"));
        assertTrue(names.contains("John"));
        assertTrue(names.contains("Emily"));
    }

    // ── PATIENT TESTS ─────────────────────────────────────────────────────────

    @Test(description = "A new patient should be saved successfully")
    public void testAddPatient() {
        Patient p = new Patient();
        p.setName("Alice");
        p.setEmail("alice@gmail.com");
        service.addPatient(p);

        assertEquals(service.getPatients().size(), 1);
        assertEquals(service.getPatients().get(0).getName(), "Alice");
    }

    @Test(description = "Two different patients should both be stored")
    public void testAddMultiplePatients() {
        Patient p1 = new Patient(); p1.setName("Alice"); p1.setEmail("alice@gmail.com");
        Patient p2 = new Patient(); p2.setName("Bob");   p2.setEmail("bob@gmail.com");

        service.addPatient(p1);
        service.addPatient(p2);

        assertEquals(service.getPatients().size(), 2);
    }

    @Test(description = "Adding the same patient name twice should be ignored")
    public void testDuplicatePatientNotAdded() {
        Patient p1 = new Patient(); p1.setName("Alice"); p1.setEmail("alice@gmail.com");
        Patient p2 = new Patient(); p2.setName("Alice"); p2.setEmail("alice2@gmail.com");

        service.addPatient(p1);
        service.addPatient(p2);  // should be rejected

        assertEquals(service.getPatients().size(), 1);
    }

    @Test(description = "Patient ID should be auto-assigned starting from 1")
    public void testPatientIdAutoAssigned() {
        Patient p = new Patient();
        p.setName("Charlie");
        p.setEmail("charlie@gmail.com");
        service.addPatient(p);

        assertEquals(service.getPatients().get(0).getId(), 1);
    }

    @Test(description = "Each new patient should receive a unique incremented ID")
    public void testPatientIdIncrementsCorrectly() {
        Patient p1 = new Patient(); p1.setName("Alice"); p1.setEmail("a@gmail.com");
        Patient p2 = new Patient(); p2.setName("Bob");   p2.setEmail("b@gmail.com");

        service.addPatient(p1);
        service.addPatient(p2);

        assertEquals(service.getPatients().get(0).getId(), 1);
        assertEquals(service.getPatients().get(1).getId(), 2);
    }

    // ── APPOINTMENT TESTS ─────────────────────────────────────────────────────

    @Test(description = "A booked appointment should appear in the appointments list")
    public void testBookAppointment() {
        service.bookAppointment(makeAppt("Alice", "Smith", "2025-12-01", "active"));
        assertEquals(service.getAppointments().size(), 1);
    }

    @Test(description = "Appointment status should remain active after booking")
    public void testAppointmentStatusIsActive() {
        service.bookAppointment(makeAppt("Alice", "Smith", "2025-12-01", "active"));
        assertEquals(service.getAppointments().get(0).getStatus(), "active");
    }

    @Test(description = "getDoctorAppointments should return only appointments for that doctor")
    public void testGetDoctorAppointments() {
        service.bookAppointment(makeAppt("Alice", "Smith", "2025-12-01", "active"));
        service.bookAppointment(makeAppt("Bob",   "John",  "2025-12-02", "active"));

        List<Appointment> result = service.getDoctorAppointments("Smith");

        assertEquals(result.size(), 1);
        assertEquals(result.get(0).getPatientName(), "Alice");
    }

    @Test(description = "getPatientAppointments should return only that patient's appointments")
    public void testGetPatientAppointments() {
        service.bookAppointment(makeAppt("Alice", "Smith", "2025-12-01", "active"));
        service.bookAppointment(makeAppt("Bob",   "Smith", "2025-12-02", "active"));

        List<Appointment> result = service.getPatientAppointments("Alice");

        assertEquals(result.size(), 1);
        assertEquals(result.get(0).getDoctorName(), "Smith");
    }

    @Test(description = "Doctor should not see appointments booked with another doctor")
    public void testDoctorCannotSeeOtherDoctorAppointments() {
        service.bookAppointment(makeAppt("Alice", "Smith", "2025-12-01", "active"));
        service.bookAppointment(makeAppt("Bob",   "Emily", "2025-12-02", "active"));

        assertEquals(service.getDoctorAppointments("Smith").size(), 1);
        assertEquals(service.getDoctorAppointments("Emily").size(), 1);
    }

    @Test(description = "Patient with no appointments should return an empty list not null")
    public void testUnknownPatientReturnsEmptyAppointments() {
        List<Appointment> result = service.getPatientAppointments("Nobody");
        assertNotNull(result);
        assertEquals(result.size(), 0);
    }

    // ── PRESCRIPTION TESTS ────────────────────────────────────────────────────

    @Test(description = "Issuing a prescription should save it to the prescriptions list")
    public void testIssuePrescription() {
        service.bookAppointment(makeAppt("Alice", "Smith", "2025-12-01", "active"));
        service.issuePrescription(makeRx("Alice", "Smith", "Hypertension", "Lisinopril 10mg", "2025-12-01"));

        assertEquals(service.getPrescriptions().size(), 1);
    }

    @Test(description = "After prescription is issued the appointment status must change to done")
    public void testPrescriptionMarksAppointmentDone() {
        service.bookAppointment(makeAppt("Alice", "Smith", "2025-12-01", "active"));
        service.issuePrescription(makeRx("Alice", "Smith", "Hypertension", "Lisinopril 10mg", "2025-12-01"));

        assertEquals(service.getAppointments().get(0).getStatus(), "done");
    }

    @Test(description = "getDoctorPrescriptions should return only that doctor's prescriptions")
    public void testGetDoctorPrescriptions() {
        service.issuePrescription(makeRx("Alice", "Smith", "Flu",   "Paracetamol", "2025-12-01"));
        service.issuePrescription(makeRx("Bob",   "John",  "Fever", "Ibuprofen",   "2025-12-02"));

        List<Prescription> result = service.getDoctorPrescriptions("Smith");

        assertEquals(result.size(), 1);
        assertEquals(result.get(0).getPatientName(), "Alice");
    }

    @Test(description = "getPatientPrescriptions should return only that patient's prescriptions")
    public void testGetPatientPrescriptions() {
        service.issuePrescription(makeRx("Alice", "Smith", "Flu", "Paracetamol 500mg", "2025-12-01"));

        List<Prescription> result = service.getPatientPrescriptions("Alice");

        assertEquals(result.size(), 1);
        assertEquals(result.get(0).getDiagnosis(), "Flu");
    }

    @Test(description = "Doctor with zero prescriptions should get an empty list not null")
    public void testDoctorWithNoPrescriptionsReturnsEmpty() {
        List<Prescription> result = service.getDoctorPrescriptions("John");
        assertNotNull(result);
        assertEquals(result.size(), 0);
    }

    @Test(description = "Prescription issued by Smith should not appear under John")
    public void testPrescriptionsNotSharedBetweenDoctors() {
        service.issuePrescription(makeRx("Alice", "Smith", "Hypertension", "Lisinopril", "2025-12-01"));

        assertEquals(service.getDoctorPrescriptions("Smith").size(), 1);
        assertEquals(service.getDoctorPrescriptions("John").size(),  0);
    }

    // ── HELPER METHODS ────────────────────────────────────────────────────────

    private Appointment makeAppt(String patient, String doctor, String date, String status) {
        Appointment a = new Appointment();
        a.setPatientName(patient);
        a.setDoctorName(doctor);
        a.setDate(date);
        a.setStatus(status);
        return a;
    }

    private Prescription makeRx(String patient, String doctor,
                                 String diagnosis, String medicines, String date) {
        Prescription rx = new Prescription();
        rx.setPatientName(patient);
        rx.setDoctorName(doctor);
        rx.setDiagnosis(diagnosis);
        rx.setMedicines(medicines);
        rx.setDate(date);
        return rx;
    }
}