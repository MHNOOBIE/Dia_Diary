package com.example.mahdi.dia_diary;

public class Doctor {
    public String doctor_id, email, hospital, name;

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDoctor_id() {
        return doctor_id;
    }

    public String getEmail() {
        return email;
    }

    public String getHospital() {
        return hospital;
    }

    public String getName() {
        return name;
    }

    public Doctor() {
    }


    public Doctor(String doctor_id, String email, String hospital, String name) {
        this.doctor_id = doctor_id;
        this.email = email;
        this.hospital = hospital;
        this.name = name;
    }
}
