package com.example.mahdi.dia_diary;

public class Users {
    String name;
    String country;
    String email;

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Users(String name, String country, String email) {
        this.name = name;
        this.country = country;
        this.email = email;
    }

    public Users() {
    }

    public String getName() {
        return name;
    }

}
