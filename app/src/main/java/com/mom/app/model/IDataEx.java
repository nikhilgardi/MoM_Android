package com.mom.app.model;

import com.mom.app.identifier.PinType;
import com.mom.app.model.pbxpl.PaymentResponse;
import com.mom.app.model.pbxpl.lic.LicLifeResponse;
import com.mom.app.ui.TransactionRequest;

import java.util.HashMap;

/**
 * Created by vaibhavsinha on 7/5/14.
 */
public interface IDataEx {
    
    public void getBalance();

    public void login(String userName, String password);

    public void verifyTPin(String psTPin);

    public void rechargeMobile(TransactionRequest<PaymentResponse> request, int pnRechargeType);

    public void rechargeDTH(TransactionRequest<PaymentResponse> request);

    public void payBill(
            TransactionRequest<PaymentResponse> request,
            String psConsumerName,
            HashMap<String, String> psExtraParamsMap
    );

    public void getBillAmount(TransactionRequest request);

    public void getTransactionHistory();
    public  void getOperatorNames();
    public void changePin(PinType pinType, String psOldPin, String psNewPin);
    public void changePinTest(TransactionRequest request);
    public void changePassword(String psOldPin, String psNewPin);

    public abstract void balanceTransfer(TransactionRequest request, String payTo);

    public  void lic(String lic);
    public void licPayment(TransactionRequest<LicLifeResponse> request , String CustomerMobNo , String PolicyNo);
}
