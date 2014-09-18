package com.mom.app.model;

import com.mom.app.error.MOMException;

import java.util.Date;

/**
 * Created by vaibhavsinha on 7/14/14.
 */
public class Transaction {
    public String transactionDate;
    public String subscriberId;
    public String transactionId;
    public String operator;
    public String amount;
    public String status;

    public String toString(){
        return transactionDate + ", " + amount + ", " + operator;
    }

}
