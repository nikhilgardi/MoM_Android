package com.mom.app.model;

/**
 * Created by vaibhavsinha on 7/14/14.
 */
public class LicLifeResponse implements ITransaction {

    public Float TransInvAmount;
    public String TransType;

    public Float getTransInvAmount() {
        return TransInvAmount;
    }

    public void setTransInvAmount(Float transInvAmount) {
        TransInvAmount = transInvAmount;
    }



    public String toString(){
        return  Double.toString(TransInvAmount) ;
    }
}
