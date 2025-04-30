package com.example.workshop1.SQLite;


public class User {
    private String name;
    private String password;

    public User(String n,String p){name=n;password=p;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
