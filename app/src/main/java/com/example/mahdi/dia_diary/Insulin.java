package com.example.mahdi.dia_diary;

import java.util.Date;

public class Insulin {
    public String insulin_value;
    public Date time;

    public String getInsulin_value() {
        return insulin_value;
    }

    public Date getTime() {
        return time;
    }

    public Insulin() {
    }

    public Insulin(String gluc_value, Date time) {
        this.insulin_value = gluc_value;
        this.time = time;

    }
}
