package com.example.imageflighttodo.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Project {
    private String title;
    private ArrayList<String> users; //String array of userUIDs
    private final Date date;
    private String uid;


    public Project(String title, String uid, ArrayList<String> users) {
        this.title = title;
        this.uid = uid;
        this.users = users;
        this.date =  Calendar.getInstance().getTime();
    }

    public String getUid() {
        return uid;
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

    public ArrayList<String> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<String> users) {
        this.users = users;
    }
}
