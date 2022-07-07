package com.example.desihand;

public class userdetails
{
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    String name;
    String email;
    String city;

    public userdetails() {
    }

    String address;
    String pass;
    String number;

    public userdetails(String name, String email, String city, String address, String pass, String number) {
        this.name = name;
        this.email = email;
        this.city = city;
        this.address = address;
        this.pass = pass;
        this.number = number;
    }
}
