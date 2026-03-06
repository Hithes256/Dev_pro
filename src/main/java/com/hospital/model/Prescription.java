package com.hospital.model;
public class Prescription {
    private String patientName, doctorName, diagnosis, medicines, notes, date;
    public Prescription() {}
    public String getPatientName(){return patientName;} public void setPatientName(String v){patientName=v;}
    public String getDoctorName(){return doctorName;}  public void setDoctorName(String v){doctorName=v;}
    public String getDiagnosis(){return diagnosis;}    public void setDiagnosis(String v){diagnosis=v;}
    public String getMedicines(){return medicines;}    public void setMedicines(String v){medicines=v;}
    public String getNotes(){return notes;}            public void setNotes(String v){notes=v;}
    public String getDate(){return date;}              public void setDate(String v){date=v;}
}