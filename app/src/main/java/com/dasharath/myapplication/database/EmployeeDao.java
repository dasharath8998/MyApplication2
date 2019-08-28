package com.dasharath.myapplication.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

/*
* Database Operation Query
* */

@Dao
public interface EmployeeDao{
    @Query("Select * from Employeedetail")
    public List<Employee> getAll();

    @Query("Select * from Employeedetail where id = :eId")
    public Employee getOneRecord(Long eId);

    @Query("Update employeedetail set empImg = :img, empName = :name, empEmail = :email, empBirthday = :birthdate, empGender = :gender, empTimestamp = :time where id = :eid")
    public int updateEmployee(Long eid , String img, String name, String email, String birthdate, String gender, Long time);

    @Insert
    public Long insertData(Employee employeeData);

    @Query("Delete from employeedetail where id = :eId")
    public void deleteRecord(Long eId);
}
