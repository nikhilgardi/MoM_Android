package com.mom.app.model.pbxpl;

import com.mom.app.model.Transaction;

/**
 * Created by vaibhavsinha on 9/17/14.
 */
public class PBXTransaction {
    public String TXNDATE;
    public String TXNID;
    public String TXNAmount;
    public String Operator;
    public String Status;

    public Transaction getTransactionObject(){
        Transaction transaction     = new Transaction();
        transaction.operator        = Operator;
        transaction.transactionDate = TXNDATE;
        transaction.transactionId   = TXNID;
        transaction.amount          = TXNAmount;
        transaction.status          = Status;

        return transaction;
    }
}
