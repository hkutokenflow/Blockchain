package com.example.workshop1.SQLite;

import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String password;
    private String name;
    private String type;
    private String walletAddress;
    private int balance;


    public User(String username, String password, String name, String type, String walletAddress) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.type = type;
        this.walletAddress = walletAddress;
        this.balance = 0;
    }

    public User(String n, String p, String name, String type, String walletAddress, int balance) {
        this.username = n;
        this.password = p;
        this.name = name;
        this.type = type;
        this.walletAddress = walletAddress;
        this.balance = balance;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getWalletAddress() {
        return walletAddress;
    }

    public void setWalletAddress(String walletAddress) {
        this.walletAddress = walletAddress;
    }
}
