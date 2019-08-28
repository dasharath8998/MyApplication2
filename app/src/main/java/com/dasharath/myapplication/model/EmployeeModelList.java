package com.dasharath.myapplication.model;

public class EmployeeModelList {

    String name, email, birthday, gender, image;

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getGender() {
        return gender;
    }

    public String getImage() {
        return image;
    }

    public EmployeeModelList(String name, String email, String birthday, String gender, String image) {
        this.name = name;
        this.email = email;
        this.birthday = birthday;
        this.gender = gender;
        this.image = image;
    }

}
