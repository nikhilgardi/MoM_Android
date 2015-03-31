package com.mom.apps.model;

/**
 * Created by akanksharaina
 */
public class ConfirmPayment implements ITransaction{
    public int     status;
    public String  errorMessage;
    public String  recieptId;
    public Long    transactionId;

    public String toString(){
        return errorMessage + ", " + recieptId + ", " + transactionId + ", " + status;
    }


}
