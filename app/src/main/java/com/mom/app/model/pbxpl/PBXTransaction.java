package com.mom.app.model.pbxpl;

import com.mom.app.model.ITransaction;
import com.mom.app.model.Transaction;

/**
 * Created by Pbx12 on 10/7/2014.
 */
public class PBXTransaction {

    public String transactionId;
    public float  amount;
    public int balance;
    public String transactionDate;
    public String subscriberId;
    public String operatorName;
    public String transactionStatus;

    public Transaction getTransaction(){
        Transaction transaction = new Transaction();
        transaction.transactionId   = transactionId;
        transaction.amount          = amount;
        transaction.balance         = balance;
        transaction.transactionDate = transactionDate;
        transaction.operator        = operatorName;
        transaction.subscriberId    = subscriberId;
        transaction.statusString    = transactionStatus ;

        return transaction;
    }
}
