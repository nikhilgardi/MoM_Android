package com.mom.app.model;

/**
 * Created by Pbx12 on 10/7/2014.
 */
public class Last5Transactions {


        public String transactionId;
        public int amount;
        public String transactionDate;
        public String customerNumber;
        public String operatorName;
        public int transactionStatus;

        public String toString(){
            return transactionId + ", " + amount + ", " + transactionDate;
        }

    }

