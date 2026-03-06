package com.hospital.model;
public class Appointment {
    private String patientName;
    private String doctorName;
    private String date;
    private String status;
    public Appointment() {}
    public Appointment(String p, String d, String dt) { patientName=p; doctorName=d; date=dt; status="active"; }
    public String getPatientName(){return patientName;} public void setPatientName(String v){patientName=v;}
    public String getDoctorName(){return doctorName;}  public void setDoctorName(String v){doctorName=v;}
    public String getDate(){return date;}              public void setDate(String v){date=v;}
    public String getStatus(){return status;}          public void setStatus(String v){status=v;}
}