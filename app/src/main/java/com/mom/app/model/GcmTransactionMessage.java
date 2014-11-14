package com.mom.app.model;

/**
 * Created by vaibhavsinha on 10/29/14.
 */
public class GcmTransactionMessage {
    String identifier;
    String originTxnId;
    Integer status;
    String responseMessage;

    public GcmTransactionMessage(){

    }

    public String getIdentifier() {
        return identifier;
    }

    public String getOriginTxnId() {
        return originTxnId;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public void setOriginTxnId(String originTxnId) {
        this.originTxnId = originTxnId;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public Integer getStatus() {
        return status;
    }
}
