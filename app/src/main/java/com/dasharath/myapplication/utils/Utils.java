package com.dasharath.myapplication.utils;

import android.app.DatePickerDialog;
import android.content.Context;
import android.text.format.DateFormat;
import android.widget.DatePicker;

import com.dasharath.myapplication.listeners.DateSetListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utils {

    public static String EMPLOYEE_ID = "EmployeeID";

    public static void openCalendar(final Context context, final int cYear, final int cMonth, final int cDay, final DateSetListener dateSetListener){
        DatePickerDialog dpd  = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day){
                dateSetListener.onDateSelected(year,month,day);
            }
        },cYear,cMonth,cDay);

        dpd.getDatePicker().setMaxDate(System.currentTimeMillis());
        dpd.show();
    }

    public static String getCalculatedAge(int year, int monthOfYear, int dayOfMonth){
        Calendar c = Calendar.getInstance();
        int cYear = c.get(Calendar.YEAR);
        int cMonth = c.get(Calendar.MONTH)+1;
        int cDay = c.get(Calendar.DAY_OF_MONTH);

        String age = "";
        if(cMonth < monthOfYear) {

            age = (cYear - year - 1) +" Years, ";
            if(dayOfMonth < cDay) {
                age+= (12 - (monthOfYear - cMonth))+ " Months, ";
                age+= (cDay - dayOfMonth)+" Days";
            }else{
                age+= (11 - (monthOfYear - cMonth)) + " Months, ";
                age+= 31 - (dayOfMonth - cDay)+" Days";
            }

        }else if(cMonth > monthOfYear) {

            age+= (cYear - year) + " Years, ";
            if(dayOfMonth < cDay) {
                age+= (cMonth - monthOfYear) + " Months, ";
                age+= (cDay - dayOfMonth)+" Days";
            }else{
                age+= (cMonth - monthOfYear - 1) + " Months, ";
                age+= 31 - (dayOfMonth - cDay)+" Days";
            }

        }else{
            age+= (cYear - year) + " Years, ";

            if(dayOfMonth < cDay) {
                age+= "0 Months, ";
                age+= (cDay - dayOfMonth)+" Days";
            }else{
                age+="11 Months, ";
                age+= 31 - (dayOfMonth - cDay)+" Days";
            }
        }
        return age;
    }

    public static String stringToDate(String date,String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date parseDate = sdf.parse(date);
        int day = Integer.parseInt(DateFormat.format("dd",parseDate).toString());
        int month = Integer.parseInt(DateFormat.format("mm",parseDate).toString());
        int year = Integer.parseInt(DateFormat.format("yyyy",parseDate).toString());

        return getCalculatedAge(year,month-1,day);
    }
}
