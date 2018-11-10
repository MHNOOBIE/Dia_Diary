package com.example.mahdi.dia_diary;

import java.util.Date;

public class Glucose {
    public String gluc_value;
    public Date time;
    public String type;

    public Glucose() {
    }

    public Glucose(String gluc_value, Date time, String type) {
        this.gluc_value = gluc_value;
        this.time = time;
        this.type = type;
    }
}
