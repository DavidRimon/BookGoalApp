package com.example.david.bookgoalapp;

import android.util.Log;

import java.util.Objects;

/**
 * Created by David on 25-Jun-17.
 */

public class Time {
    private final String ERR_TAG = "Ttime class error";
    private int hours,minitues;

    public Time(int Hours, int Minitues) {
        this.hours = Hours;
        this.minitues = Minitues;
    }
    public Time(){}
    public Time(Time t) {
        this(t.hours,t.minitues);
    }
    public Time(String timeStr) throws Exception{
        try {
            this.hours = Integer.valueOf(timeStr.substring(0, timeStr.indexOf(':'))); //get the hours part
            this.minitues = Integer.valueOf(timeStr.substring(timeStr.indexOf(':')+1, timeStr.length())); //get the minutes part
        } catch (Exception e) {
            Log.d(ERR_TAG,e.getMessage());
            throw new Exception("Couldn't convert string to time");
        }
    }
    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Time)) return false;

        Time t = (Time) o;
        return this.hours == t.hours && this.minitues == t.minitues;
    }
    @Override
    public String toString() {
        //make sure to allways show 2-digit number time. (09:00)
        String textTimeToShow = "";
        if(hours < 10)
            textTimeToShow += "0";
        textTimeToShow += hours;
        textTimeToShow += ":";
        if(minitues < 10)
            textTimeToShow += "0";
        textTimeToShow += minitues;

        return textTimeToShow;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinitues() {
        return minitues;
    }

    public void setMinitues(int minitues) {
        this.minitues = minitues;
    }
}
