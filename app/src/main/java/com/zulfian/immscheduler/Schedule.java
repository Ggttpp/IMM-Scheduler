package com.zulfian.immscheduler;

public class Schedule {
    private String id, name, location, note, date, hour;

    public Schedule(){

    }

    public Schedule(String id, String name, String location, String note, String date, String hour ) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.note = note;
        this.date = date;
        this.hour = hour;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getNote() {
        return note;
    }

    public String getDate() {
        return date;
    }

    public String getHour() {
        return hour;
    }
}
