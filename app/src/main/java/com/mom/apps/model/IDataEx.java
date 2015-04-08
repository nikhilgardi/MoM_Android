package com.mom.apps.model;

import com.mom.apps.identifier.PinType;
import com.mom.apps.model.pbxpl.BankNameResult;
import com.mom.apps.model.pbxpl.BeneficiaryResult;
import com.mom.apps.model.pbxpl.BranchNameResult;
import com.mom.apps.model.pbxpl.CityNameResult;
import com.mom.apps.model.pbxpl.ImpsAddBeneficiaryResult;
import com.mom.apps.model.pbxpl.ImpsAuthenticationResult;
import com.mom.apps.model.pbxpl.ImpsBeneficiaryDetailsResult;
import com.mom.apps.model.pbxpl.ImpsCheckKYCResult;
import com.mom.apps.model.pbxpl.ImpsConfirmPaymentResult;
import com.mom.apps.model.pbxpl.ImpsCreateCustomerResult;
import com.mom.apps.model.pbxpl.ImpsCustomerRegistrationResult;
import com.mom.apps.model.pbxpl.ImpsPaymentProcessResult;
import com.mom.apps.model.pbxpl.ImpsVerifyPaymentResult;
import com.mom.apps.model.pbxpl.ImpsVerifyProcessResult;
import com.mom.apps.model.pbxpl.PaymentResponse;
import com.mom.apps.model.pbxpl.StateNameResult;
import com.mom.apps.model.pbxpl.lic.LicLifeResponse;
import com.mom.apps.ui.TransactionRequest;

import java.util.HashMap;
import java.util.List;

/**
 * Created by vaibhavsinha on 7/5/14.
 */
public interface IDataEx {
    
    public void getBalance();
    public void cancelAsynctASK();

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

    public void impsCustomerRegistrationStatus(String consumerNumber);
    public void impsCustomerRegistration(TransactionRequest<ImpsCreateCustomerResult> request);
    public void impsAuthentication(TransactionRequest<ImpsAuthenticationResult> request);
    public void impsBeneficiaryList(String consumerNumber);

    public void registerIMPSCustomer(TransactionRequest<ImpsCustomerRegistrationResult> request);
    public void getIMPSBeneficiaryList(TransactionRequest<List<BeneficiaryResult>> request);
    public void impsAddBeneficiary(TransactionRequest<ImpsAddBeneficiaryResult> request);

    public void impsBeneficiaryDetails(TransactionRequest<ImpsBeneficiaryDetailsResult> request , String sBeneficiaryName);
    public void impsCheckKYC(TransactionRequest<ImpsCheckKYCResult> request , String sConsumerNumber);
    public void getIMPSBankName(TransactionRequest<List<BankNameResult>> request);
    public void getIMPSStateName(TransactionRequest<List<StateNameResult>> request , String sBankName);
    public void getIMPSCityName(TransactionRequest<List<CityNameResult>> request , String sBankName , String sStateName);
    public void getIMPSBranchName(TransactionRequest<List<BranchNameResult>> request , String sBankName , String sStateName , String sCityName);
    public void impsVerifyProcess(TransactionRequest<ImpsVerifyProcessResult> request);
    public void impsVerifyPayment(TransactionRequest<ImpsVerifyPaymentResult> request ,String sOTP ,
                                  String sAccountNumber ,String sIFSCCodeString , String sCustomerNumber);

    public void impsPaymentProcess(TransactionRequest<ImpsPaymentProcessResult>request , String sAmount ,String sTxnNarration);
    public void impsMomPaymentProcess(TransactionRequest<ImpsPaymentProcessResult>request , String sAmount ,String sTxnNarration);
    public void impsMomConfirmProcess(TransactionRequest request ,  String sOTP ,
                                      String sAccountNumber ,String sIFSCCode , String sCustomerNumber);

    public void impsConfirmPayment(TransactionRequest<List<ImpsConfirmPaymentResult>> request , String sOTP ,
    String sAccountNumber ,String sIFSCCode , String sCustomerNumber , String sAmount);

    public  void lic(String lic , String customerMobNo);
    public void licPayment(TransactionRequest<LicLifeResponse> request , String CustomerMobNo , String PolicyNo , String fromDate);
}
