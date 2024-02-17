package com.example.myfirstapp;

public class User {
    public String name,userName,phone,birthDate,profession;


    public User() {
        // Default constructor required for Firebase
    }

    public User(String name,String userName,String phone, String birthDate,String profession){
        this.name=name;
        this.userName=userName;
        this.phone=phone;
        this.birthDate=birthDate;
        this.profession=profession;
    }

}
