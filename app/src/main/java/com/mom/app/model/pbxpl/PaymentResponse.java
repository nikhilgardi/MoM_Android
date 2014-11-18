package com.mom.app.model.pbxpl;

/**
 * Created by Pbx12 on 10/8/2014.
 */
public class PaymentResponse {

    public String transactionId;
    public float amount;
    public float balance;

    public String toString(){
        return "id: " + transactionId + ", amount: " + amount + ", balance: " + balance;
    }
}
