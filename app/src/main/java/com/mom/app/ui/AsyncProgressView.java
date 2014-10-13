package com.mom.app.ui;

import android.app.Activity;
import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mom.app.R;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by vaibhavsinha on 10/9/14.
 */
public class AsyncProgressView {
    public enum TransactionType{
        MOBILE (R.string.action_mobileRecharge),
        DTH (R.string.action_dthRecharge),
        BILL_PAYMENT (R.string.action_billPayment);

        public int transactionTypeStringId;

        private TransactionType(int id){
            this.transactionTypeStringId = id;
        }
    }

    Context _context;
    TransactionType _type;

    float amount;
    String consumerId;
    String operator;
    Date dateStarted;
    Date dateCompleted;
    boolean isSuccessful;
    boolean isCompleted;

    public AsyncProgressView(Context context, TransactionType type, String consumerId, float amount, String operator){
        _context            = context;
        _type               = type;
        this.consumerId     = consumerId;
        this.amount         = amount;
        this.operator       = operator;
        this.dateStarted    = new Date();
    }

    public String getDescription(){
        StringBuilder sb    = new StringBuilder();
        sb.append(
                DateUtils.formatSameDayTime(
                        dateStarted.getTime(),
                        new Date().getTime(),
                        DateFormat.SHORT,
                        DateFormat.SHORT
                ))
                .append(_context.getString(_type.transactionTypeStringId))
                .append(": ").append(consumerId)
                .append(" (").append(operator).append(")")
                .append(", ").append(_context.getString(R.string.lblAmount))
                .append(": ").append(String.valueOf(amount));

        return sb.toString();
    }

    public float getAmount() {
        return amount;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public String getOperator() {
        return operator;
    }

    public Date getDateStarted() {
        return dateStarted;
    }

    public Date getDateCompleted() {
        return dateCompleted;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public boolean isCompleted() {
        return isCompleted;
    }
}
