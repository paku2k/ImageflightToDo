package com.example.imageflighttodo.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ToDoItem {
    private String title;
    private String description;
    private ArrayList<String> assignmentUsers;
    private Date date;


    public ToDoItem(String title, String description, ArrayList<String> assignment, Date date) {
        this.title = title;
        this.description = description;
        this.assignmentUsers = assignment;
        this.date = date;
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public ArrayList<String> getAssignmentUsers() {
        return assignmentUsers;
    }

    public void setAssignmentUsers(ArrayList<String> assignmentUsers) {
        this.assignmentUsers = assignmentUsers;
    }
}
