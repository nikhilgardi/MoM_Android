package com.mom.app.model.pbxpl.lic;

import com.mom.app.model.ITransaction;


public class LicLifeResponse implements ITransaction {
    LicOLife OLife;
    public Float TransInvAmount;
    public String TransType;
    public String TransRefGUID;
    public String TransInvGUID;
    public String TransExeDate;
    public String TransReceiptID;
    public String PymtTrsfrStatus;

    public Float getTransInvAmount() {
        return TransInvAmount;
    }

    public void setTransInvAmount(Float transInvAmount) {
        TransInvAmount = transInvAmount;
    }

    public LicOLife getOLife() {
        return OLife;
    }

    public void setOLife(LicOLife OLife) {
        this.OLife = OLife;
    }

    public String getTransType() {
        return TransType;
    }

    public String getTransRefGUID() {
        return TransRefGUID;
    }

    public String getTransInvGUID() {
        return TransInvGUID;
    }

    public void setTransType(String transType) {
        TransType = transType;
    }

    public void setTransRefGUID(String transRefGUID) {
        TransRefGUID = transRefGUID;
    }

    public void setTransInvGUID(String transInvGUID) {
        TransInvGUID = transInvGUID;
    }

    public String getTransExeDate() {
        return TransExeDate;
    }

    public String getTransReceiptID() {
        return TransReceiptID;
    }

    public String getPymtTrsfrStatus() {
        return PymtTrsfrStatus;
    }

    public void setTransExeDate(String transExeDate) {
        TransExeDate = transExeDate;
    }

    public void setTransReceiptID(String transReceiptID) {
        TransReceiptID = transReceiptID;
    }

    public void setPymtTrsfrStatus(String pymtTrsfrStatus) {
        PymtTrsfrStatus = pymtTrsfrStatus;
    }

}
