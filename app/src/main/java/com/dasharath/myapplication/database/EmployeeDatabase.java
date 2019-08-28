package com.dasharath.myapplication.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Employee.class},version = 1)
public abstract class EmployeeDatabase extends RoomDatabase {
    public abstract EmployeeDao employeeDao();

    private static EmployeeDatabase INSTANCE;

    public static EmployeeDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context, EmployeeDatabase.class, "empDb").build();
        }
        return (EmployeeDatabase) INSTANCE;
    }
}
