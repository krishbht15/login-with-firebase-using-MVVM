package com.example.fampayassignment;

public class UsersPojo {
    private String city,name,email,phone;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public UsersPojo(String city, String name, String email, String phone) {
        this.city = city;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }
}
