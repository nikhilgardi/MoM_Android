package com.mom.app.ui;

import android.content.Context;
import android.text.TextUtils;
import android.text.format.DateUtils;

import com.mom.app.R;
import com.mom.app.identifier.PinType;
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
public class TransactionRequest<T> implements Serializable{
    public static enum RequestStatus{
        PENDING (1), SUCCESSFUL (0), FAILED (-1), REPEAT_RECHARGE(-2),
        INVALID_NUMBER(-3), INVALID_AMOUNT(-4), INVALID_SYNTAX(-5),
        NOT_AUTHORIZED(-6), NOT_REGISTERED(-7);

        public int code;
        private RequestStatus(int code){
            this.code   = code;
        }

        public static RequestStatus getStatus(int code){
            switch (code){
                case 1:
                    return PENDING;
                case 0:
                    return SUCCESSFUL;
                case -1:
                    return FAILED;
                case -2:
                    return REPEAT_RECHARGE;
                case -3:
                    return INVALID_NUMBER;
                case -4:
                    return INVALID_SYNTAX;
                case -6:
                    return NOT_AUTHORIZED;
                case -7:
                    return NOT_REGISTERED;
            }

            return null;
        }
    }

    public Long id;
    String _type;
    float amount;
    String placeholder;
    String consumerId;
    String oldPin;
    String newPin;
    PinType pinType;
    String NewConfirmPin;
    String customerMobile;
    Operator operator;
    Date dateStarted;
    Date dateCompleted;
    RequestStatus status = RequestStatus.PENDING;
    boolean isCompleted = false;
    int responseCode;

    String remoteResponse;

    T custom;
String remoteId;
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

    public TransactionRequest(PinType pinType , String oldPin, String NewPin ){

        this.pinType            = pinType;
        this.oldPin             = oldPin;
        this.newPin             = NewPin ;
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
    public TransactionRequest(String consumerId){

        this.consumerId     = consumerId;
        this.dateStarted    = new Date();
    }

    public TransactionRequest(String type, String consumerId){
        _type               = type;
        this.id             = MiscUtils.getRandomLong();
        this.consumerId     = consumerId;
        this.dateStarted    = new Date();
    }

    public Long getId() {
        return id;
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
    public String getPlaceHolder()
    {
        return placeholder;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public String getConsumerId() {
        return consumerId;
    }
    public void setPin(PinType pin) {
        this.pinType = pinType;
    }

    public PinType getPin() {
        return pinType;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }
    public String getOldPin() {
        return oldPin;
    }
    public String getNewPin() {
        return newPin;
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

    public boolean setStatus(int code){
        RequestStatus statusFromCode    = RequestStatus.getStatus(code);
        if(statusFromCode != null){
            setStatus(statusFromCode);
            return true;
        }
        return false;
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

    public void setAmount(float amount) {
        this.amount = amount;
    }
    public void setPlaceHolder(String placeholder) {
        this.placeholder = placeholder;
    }

    public T getCustom() {
        return custom;
    }

    public void setCustom(T custom) {
        this.custom = custom;
    }
}
