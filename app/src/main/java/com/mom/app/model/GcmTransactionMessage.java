package com.mom.app.model;

/**
 * Created by vaibhavsinha on 10/29/14.
 */
public class GcmTransactionMessage {
    String identifier;
    String originTxnId;
    String payload;

    public GcmTransactionMessage(){

    }

    public String getIdentifier() {
        return identifier;
    }

    public String getOriginTxnId() {
        return originTxnId;
    }

    public String getPayload() {
        return payload;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public void setOriginTxnId(String originTxnId) {
        this.originTxnId = originTxnId;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
