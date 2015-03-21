package com.mom.app.model.pbxpl;

import com.mom.app.model.Beneficiary;
import com.mom.app.model.ConfirmPayment;
import com.mom.app.model.ITransaction;

/**
 * Created by vaibhavsinha on 10/13/14.
 */
public class ImpsConfirmPaymentResult implements ITransaction {
    public int status;
    public String errorMessage;
    public String recieptId;
    public Long transactionId;

    public ImpsConfirmPaymentResult(){}
    public ImpsConfirmPaymentResult(int status , String errorMessage ,String recieptId , Long transactionId){
        this.status   = status;
        this.errorMessage   = errorMessage;
        this.recieptId   = recieptId;
        this.transactionId   = transactionId;

    }

    public int getStatus() {
        return status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getRecieptId() {
        return recieptId;
    }
    public Long getTransactionId() {
        return transactionId;
    }


    public String toString(){
        return errorMessage;
    }


    public ConfirmPayment getConfirm (){
        ConfirmPayment confirmPayment = new ConfirmPayment();
        confirmPayment.status   = status;
        confirmPayment.transactionId = transactionId;
        confirmPayment.recieptId     =recieptId;
        confirmPayment.errorMessage =errorMessage;

        return confirmPayment;
    }
}
