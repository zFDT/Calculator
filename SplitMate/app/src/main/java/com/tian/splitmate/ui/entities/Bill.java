package com.tian.splitmate.ui.entities;


public class Bill {
    private int Bill_ID;
    private String Bill_Name;
    private String Bill_Type;
    private String Bill_Date;
    private Double Bill_Money;
    private String Bill_content;
    private String User_Name;

    public Bill() {
    }

    public Bill(int bill_ID, String bill_Name, String bill_Type, String bill_Date, Double bill_Money, String bill_content, String user_Name) {
        Bill_ID = bill_ID;
        Bill_Name = bill_Name;
        Bill_Type = bill_Type;
        Bill_Date = bill_Date;
        Bill_Money = bill_Money;
        Bill_content = bill_content;
        User_Name = user_Name;
    }

    public int getBill_ID() {
        return Bill_ID;
    }

    public void setBill_ID(int bill_ID) {
        Bill_ID = bill_ID;
    }

    public String getBill_Name() {
        return Bill_Name;
    }

    public void setBill_Name(String bill_Name) {
        Bill_Name = bill_Name;
    }

    public String getBill_Type() {
        return Bill_Type;
    }

    public void setBill_Type(String bill_Type) {
        Bill_Type = bill_Type;
    }

    public String getBill_Date() {
        return Bill_Date;
    }

    public void setBill_Date(String bill_Date) {
        Bill_Date = bill_Date;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "Bill_ID=" + Bill_ID +
                ", Bill_Name='" + Bill_Name + '\'' +
                ", Bill_Type='" + Bill_Type + '\'' +
                ", Bill_Date='" + Bill_Date + '\'' +
                ", Bill_Money=" + Bill_Money +
                ", Bill_content='" + Bill_content + '\'' +
                ", User_Name='" + User_Name + '\'' +
                '}';
    }

    public Double getBill_Money() {
        return Bill_Money;
    }

    public void setBill_Money(Double bill_Money) {
        Bill_Money = bill_Money;
    }

    public String getBill_content() {
        return Bill_content;
    }

    public void setBill_content(String bill_content) {
        Bill_content = bill_content;
    }

    public String getUser_Name() {
        return User_Name;
    }

    public void setUser_Name(String user_Name) {
        User_Name = user_Name;
    }


}
