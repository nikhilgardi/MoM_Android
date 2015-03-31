package com.mom.apps.model;

/**
 * Created by vaibhavsinha on 7/14/14.
 */
public class Transaction implements ITransaction{
    public String transactionDate;
    public String subscriberId;
    public String transactionId;
    public String operator;
    public Float amount;
    public int balance;
    public String statusString;
    public boolean status;

    public String toString(){
        return transactionDate + ", " + amount + ", " + operator + ", " + statusString;
    }
}
