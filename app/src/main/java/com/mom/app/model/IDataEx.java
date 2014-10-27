package com.mom.app.model;

import com.mom.app.identifier.PinType;
import com.mom.app.ui.TransactionRequest;

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

    public void rechargeMobile(TransactionRequest request, int pnRechargeType);

    public void rechargeDTH(TransactionRequest request);

    public void payBill(
            TransactionRequest request,
            String psConsumerName,
            HashMap<String, String> psExtraParamsMap
    );

    public void getBillAmount(TransactionRequest request);

    public void getTransactionHistory();
    public  void getOperatorNames();
    public void changePin(PinType pinType, String psOldPin, String psNewPin);
    public void changePassword(String psOldPin, String psNewPin);

    public abstract void balanceTransfer(TransactionRequest request, String payTo);
}
