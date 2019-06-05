package com.volunteam.components;

import java.io.Serializable;

public class Date implements Serializable {

    public String day;
    public String month;
    public String year;

    public Date() {}

    public Date(String day, String month, String year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public static Date max(Date d1, Date d2){
        if(Integer.parseInt(d1.year)!=Integer.parseInt(d2.year)){
            if(Integer.parseInt(d1.year)>Integer.parseInt(d2.year)) {
                return d1;
            }
            else {
                return d2;
            }
        }
        if(Integer.parseInt(d1.year)!=Integer.parseInt(d2.year)){
            if(Integer.parseInt(d1.year)>Integer.parseInt(d2.year)){
                return d1;
            }
            else {
                return d2;
            }
        }
        if(Integer.parseInt(d1.month)!=Integer.parseInt(d2.month)){
            if(Integer.parseInt(d1.month)>Integer.parseInt(d2.month)){
                return d1;
            }
            else{
                return d2;
            }
        }
        return null;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
