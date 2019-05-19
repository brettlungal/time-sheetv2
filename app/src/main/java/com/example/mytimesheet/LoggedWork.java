package com.example.mytimesheet;

public class LoggedWork {

    private String date;
    private double hours;
    private String id;

    public LoggedWork(){
        date = "";
        hours = -1;
    }

    public LoggedWork(String id , String date, double hours){
        this.id = id;
        this.date = date;
        this.hours = hours;
    }

    public String getId(){ return id; }

    public String getDate(){
        return date;
    }

    public double getHours(){
        return hours;
    }


}
