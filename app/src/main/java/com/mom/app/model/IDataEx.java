package com.mom.app.model;

import com.mom.app.identifier.PinType;

import org.apache.http.NameValuePair;

import java.util.HashMap;
import java.util.List;

/**
 * Created by vaibhavsinha on 7/5/14.
 */
public interface IDataEx {
    public void getBalance();

    public void login(String userName, String password);

    public void verifyTPin(String psTPin);

    public void rechargeMobile(String psConsumerNumber, double pdAmount, String psOperator, int pnRechargeType);

    public void rechargeDTH(String psSubscriberId, double pdAmount, String psOperator, String psCustomerMobile);

    public void payBill(
            String psSubscriberId,
            double pdAmount,
            String psOperatorId,
            String psCustomerMobile,
            String psConsumerName,
            HashMap<String, String> psExtraParamsMap
    );

    public void getBillAmount(String psOperatorId, String psSubscriberId);

    public void getTransactionHistory();

    public void changePin(PinType pinType, String psOldPin, String psNewPin);

    public abstract void retailerpayment(String psConsumerNumber, double pdAmount);
    public abstract void rechargeMobilePBX(
            String customerNumber,
            String psOperator,
            double pdAmount);

}
