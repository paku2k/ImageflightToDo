package com.example.imageflighttodo.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Project {
    private String title;
    private ArrayList<String> users; //String array of userUIDs
    private Date date;
    private String uid;


    public Project(String title, String uid, ArrayList<String> users, Date date) {
        this.title = title;
        this.uid = uid;
        this.users = users;
        this.date =  date;
    }

    public String getUid() {
        return uid;
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

    public ArrayList<String> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<String> users) {
        this.users = users;
    }
}
