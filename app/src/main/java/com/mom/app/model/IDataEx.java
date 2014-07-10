package com.mom.app.model;

import org.apache.http.NameValuePair;

import java.util.HashMap;
import java.util.List;

/**
 * Created by vaibhavsinha on 7/5/14.
 */
public interface IDataEx {
    public void getBalance();
    public void login(NameValuePair...params);
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
}
