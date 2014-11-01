package com.mom.app.ui;

import android.content.Context;
import android.text.TextUtils;
import android.text.format.DateUtils;

import com.mom.app.R;
import com.mom.app.identifier.TransactionType;
import com.mom.app.model.Operator;
import com.mom.app.utils.MiscUtils;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by vaibhavsinha on 10/9/14.
 */
public class TransactionRequest implements Serializable{
    public static enum RequestStatus{
        SUCCESSFUL (0), FAILED (-1), PENDING (-2);
        public int code;
        private RequestStatus(int code){
            this.code   = code;
        }

        public static RequestStatus getStatus(int code){
            switch (code){
                case 0:
                    return SUCCESSFUL;
                case -1:
                    return FAILED;
                case -2:
                    return PENDING;
            }

            return null;
        }
    }

    public Long id;
    String _type;
    float amount;
    String consumerId;
    String customerMobile;
    Operator operator;
    Date dateStarted;
    Date dateCompleted;
    RequestStatus status;
    boolean isCompleted;
    int responseCode;

    String remoteResponse;

    /**
     * This is only used in methods which are not operator or transactionType specific.
     * e.g. getBalance
     */
    public TransactionRequest(){
        this.id             = new Random().nextLong();
    }

    /**
     * @param type String of transaction type
     * @param amount amount of transaction
     */
    public TransactionRequest(String type, String consumerId, float amount){
        _type               = type;
        this.id             = MiscUtils.getRandomLong();
        this.consumerId     = consumerId;
        this.amount         = amount;
        this.dateStarted    = new Date();
    }

    public TransactionRequest(String type, String consumerId, String customerMobile, float amount, Operator operator){
        this.id             = MiscUtils.getRandomLong();
        _type               = type;
        this.consumerId     = consumerId;
        this.customerMobile = customerMobile;
        this.amount         = amount;
        this.operator       = operator;
        this.dateStarted    = new Date();
    }

    public String getDescription(){
        StringBuilder sb    = new StringBuilder(_type);

        if(operator != null){
            sb.append(" / ").append(operator.name);
        }

        if(!TextUtils.isEmpty(consumerId)) {
            sb.append(" (").append(consumerId).append(") ");
        }

        return sb.toString();
    }

    public float getAmount() {
        return amount;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public Operator getOperator() {
        return operator;
    }

    public Date getDateStarted() {
        return dateStarted;
    }

    public Date getDateCompleted() {
        return dateCompleted;
    }

    public void setDateCompleted(Date dateCompleted) {
        this.dateCompleted = dateCompleted;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public String getRemoteResponse() {
        return remoteResponse;
    }

    public void setRemoteResponse(String remoteResponse) {
        this.remoteResponse = remoteResponse;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }
}
