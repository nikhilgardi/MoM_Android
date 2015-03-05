package com.mom.app.model;

import com.mom.app.identifier.PinType;
import com.mom.app.model.pbxpl.BeneficiaryResult;
import com.mom.app.model.pbxpl.ImpsCustomerRegistrationResult;
import com.mom.app.model.pbxpl.PaymentResponse;
import com.mom.app.model.pbxpl.lic.LicLifeResponse;
import com.mom.app.ui.TransactionRequest;

import java.util.HashMap;
import java.util.List;

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

    public void utilityPayBill(
            TransactionRequest<PaymentResponse> request,
            String psConsumerName,
            HashMap<String, String> psExtraParamsMap
    );

    public void getBillAmount(TransactionRequest request);

    public void getTransactionHistory();
    public void changePin(PinType pinType, String psOldPin, String psNewPin);
    public void changePassword(String psOldPin, String psNewPin);

    public abstract void balanceTransfer(TransactionRequest request, String payTo);
     public void signUpEncryptData(String composeData , String Key);

    public void signUpCustomerRegistration(String data);
<<<<<<< HEAD
    public void impsCustomerRegistrationStatus(String consumerNumber);
    public void impsCustomerRegistration(String consumerNumber , String consumerName , String consumerDOB , String EmailAddress);
    public void impsBeneficiaryList(String consumerNumber);
=======
    public void registerIMPSCustomer(TransactionRequest<ImpsCustomerRegistrationResult> request);
    public void getIMPSBeneficiaryList(TransactionRequest<List<BeneficiaryResult>> request);
>>>>>>> 198c7f77c4fc6efc3e62827ca177688d5171d5c7

    public  void lic(String lic , String customerMobNo);
    public void licPayment(TransactionRequest<LicLifeResponse> request , String CustomerMobNo , String PolicyNo , String fromDate);
}
