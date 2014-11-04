package com.mom.app.model.pbxpl.lic;

import com.mom.app.model.ITransaction;

/**
 * Created by vaibhavsinha on 7/14/14.
 */
public class LicLifeResponsePayment implements ITransaction {

    public String TransRefGUID;
    public String TransInvGUID;
    public String TransExeDate;
    public String PymtTrsfrStatus;
    public String TransReceiptID;

    public String getTransReceiptID() {
        return TransReceiptID;
    }

    public void setTransReceiptID(String transReceiptID) {
        TransReceiptID = transReceiptID;
    }



    public String getPymtTrsfrStatus() {
        return PymtTrsfrStatus;
    }

    public void setPymtTrsfrStatus(String pymtTrsfrStatus) {
        PymtTrsfrStatus = pymtTrsfrStatus;
    }

    public String getTransExeDate() {

        return TransExeDate;
    }

    public void setTransExeDate(String transExeDate) {
        TransExeDate = transExeDate;
    }

    public String getTransRefGUID() {
        return TransRefGUID;
    }

    public String getTransInvGUID() {
        return TransInvGUID;
    }

    public void setTransRefGUID(String transRefGUID) {
        TransRefGUID = transRefGUID;
    }

    public void setTransInvGUID(String transInvGUID) {
        TransInvGUID = transInvGUID;
    }
}
