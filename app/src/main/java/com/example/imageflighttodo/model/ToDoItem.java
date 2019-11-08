package com.example.imageflighttodo.model;

import java.util.Calendar;
import java.util.Date;

public class ToDoItem {
    private String title;
    private String description;
    private int assignment;
    private final Date date;


    public ToDoItem(String title, String description, int assignment) {
        this.title = title;
        this.description = description;
        this.assignment = assignment;
        this.date =  Calendar.getInstance().getTime();
    }


    public Date getDate() {
        return date;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAssignment() {
        return assignment;
    }

    public void setAssignment(int assignment) {
        this.assignment = assignment;
    }
}
