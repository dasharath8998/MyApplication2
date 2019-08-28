package com.dasharath.myapplication.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/*
 * Table fields
 * */

@Entity(tableName = "Employeedetail")
public class Employee {

    @NonNull
    @PrimaryKey
    Long id;

    @ColumnInfo(name = "empImg")
    String img;

    @ColumnInfo(name = "empName")
    String name;

    @ColumnInfo(name = "empEmail")
    String email;

    @ColumnInfo(name = "empBirthday")
    String birthday;

    @ColumnInfo(name = "empGender")
    String gender;

    @ColumnInfo(name = "empTimestamp")
    Long timestamp;


    public Employee(Long id, String img, String name, String email, String birthday, String gender, long timestamp) {
        this.id = id;
        this.img = img;
        this.name = name;
        this.email = email;
        this.birthday = birthday;
        this.gender = gender;
        this.timestamp = timestamp;
    }

    @NonNull
    public Long getId() {
        return id;
    }

    public String getImg() {
        return img;
    }

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

    public Long getTimestamp() {
        return timestamp;
    }
}

//@Entity
//public class Movies {
//    @NonNull
//    @PrimaryKey
//    private String movieId;
//    private String movieName;
//
//    public Movies() {
//    }
//
//    public String getMovieId() {
//        return movieId;
//    }
//
//    public void setMovieId(String movieId) {
//        this.movieId = movieId;
//    }
//
//    public String getMovieName() {
//        return movieName;
//    }
//
//    public void setMovieName(String movieName) {
//        this.movieName = movieName;
//    }
//}