package com.mom.app.model;

import com.mom.app.error.MOMException;

import java.util.Date;

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
