package com.mom.app.model.pbxpl;

import com.mom.app.model.ITransaction;
import com.mom.app.model.Transaction;

/**
 * Created by Pbx12 on 10/7/2014.
 */
public class PBXTransaction {

    public String transactionId;
    public float  amount;
    public int bal;
    public String transactionDate;
    public String customerNumber;
    public String operatorName;
    public int transactionStatus;

    public Transaction getTransaction(){
        Transaction transaction = new Transaction();
        transaction.transactionId   = transactionId;
        transaction.amount          = amount;
        transaction.transactionDate = transactionDate;
        transaction.operator        = operatorName;
        transaction.subscriberId    = customerNumber;
        transaction.status          = transactionStatus > 0;

        return transaction;
    }
}
