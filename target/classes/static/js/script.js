document.addEventListener("DOMContentLoaded", function () {

    // Initialize doctors only once
    if (!localStorage.getItem("doctors")) {
        const doctors = [
            { name: "John" },
            { name: "Smith" },
            { name: "David" }
        ];
        localStorage.setItem("doctors", JSON.stringify(doctors));
        localStorage.setItem("appointments", JSON.stringify([]));
    }

    // LOGIN PAGE
    const loginBtn = document.getElementById("loginBtn");
    if (loginBtn) {
        loginBtn.addEventListener("click", function () {
            const name = document.getElementById("username").value.trim();
            const role = document.getElementById("role").value;

            if (name === "") {
                alert("Enter your name");
                return;
            }

            localStorage.setItem("currentUser", name);
            localStorage.setItem("role", role);

            if (role === "Patient") {
                window.location.href = "patient-dashboard.html";
            } else {
                window.location.href = "doctor-dashboard.html";
            }
        });
    }

    // PATIENT DASHBOARD
    if (window.location.pathname.includes("patient-dashboard.html")) {

        const patientName = localStorage.getItem("currentUser");
        document.getElementById("patientName").innerText = patientName;

        const doctors = JSON.parse(localStorage.getItem("doctors")) || [];
        const doctorSelect = document.getElementById("doctorSelect");

        doctorSelect.innerHTML = "";

        doctors.forEach(doc => {
            const option = document.createElement("option");
            option.value = doc.name;
            option.textContent = "Dr. " + doc.name;
            doctorSelect.appendChild(option);
        });

        showPatientAppointments();

        const bookBtn = document.getElementById("bookBtn");
        bookBtn.addEventListener("click", bookAppointment);
    }

    // DOCTOR DASHBOARD
    if (window.location.pathname.includes("doctor-dashboard.html")) {

        const doctorName = localStorage.getItem("currentUser");
        document.getElementById("doctorName").innerText = doctorName;

        showDoctorAppointments();
    }

});

// BOOK APPOINTMENT
function bookAppointment() {

    const doctor = document.getElementById("doctorSelect").value;
    const date = document.getElementById("appointmentDate").value;
    const patient = localStorage.getItem("currentUser");

    if (!doctor || !date) {
        alert("Select doctor and date");
        return;
    }

    const appointments = JSON.parse(localStorage.getItem("appointments")) || [];
    appointments.push({ doctor, patient, date });

    localStorage.setItem("appointments", JSON.stringify(appointments));

    alert("Appointment booked successfully!");
    showPatientAppointments();
}

// SHOW PATIENT APPOINTMENTS
function showPatientAppointments() {

    const list = document.getElementById("patientAppointments");
    if (!list) return;

    list.innerHTML = "";

    const appointments = JSON.parse(localStorage.getItem("appointments")) || [];
    const patient = localStorage.getItem("currentUser");

    appointments
        .filter(app => app.patient === patient)
        .forEach(app => {
            const li = document.createElement("li");
            li.textContent = "Dr. " + app.doctor + " on " + app.date;
            list.appendChild(li);
        });
}

// SHOW DOCTOR APPOINTMENTS
function showDoctorAppointments() {

    const list = document.getElementById("doctorAppointments");
    if (!list) return;

    list.innerHTML = "";

    const appointments = JSON.parse(localStorage.getItem("appointments")) || [];
    const doctor = localStorage.getItem("currentUser");

    appointments
        .filter(app => app.doctor === doctor)
        .forEach(app => {
            const li = document.createElement("li");
            li.textContent = app.patient + " on " + app.date;
            list.appendChild(li);
        });
}

// LOGOUT
function logout() {
    localStorage.removeItem("currentUser");
    localStorage.removeItem("role");
    window.location.href = "login.html";
}