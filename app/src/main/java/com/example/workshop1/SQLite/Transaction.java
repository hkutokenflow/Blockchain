package com.example.workshop1.SQLite;

public class Transaction {
    public String datetime;
    public int source;
    public int destination;
    public int amount;
    public int eid;

    public Transaction(String dt,int s, int d, int a, int e) {
        this.datetime = dt;
        this.source = s;
        this.destination = d;
        this.amount = a;
        this.eid = e;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String dt) {
        this.datetime = dt;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int n) {
        this.source = n;
    }

    public int getDestination() {
        return destination;
    }

    public void setDestination(int n) {
        this.destination = n;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int n) {
        this.amount = n;
    }

    public int getEid() {
        return eid;
    }

    public void setEid(int e) { this.eid = e; }
}
