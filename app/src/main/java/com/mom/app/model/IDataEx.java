package com.mom.app.model;

import com.mom.app.identifier.PinType;
import com.mom.app.model.pbxpl.BankNameResult;
import com.mom.app.model.pbxpl.BeneficiaryResult;
import com.mom.app.model.pbxpl.BranchNameResult;
import com.mom.app.model.pbxpl.CityNameResult;
import com.mom.app.model.pbxpl.ImpsAddBeneficiaryResult;
import com.mom.app.model.pbxpl.ImpsAuthenticationResult;
import com.mom.app.model.pbxpl.ImpsBeneficiaryDetailsResult;
import com.mom.app.model.pbxpl.ImpsCheckKYCResult;
import com.mom.app.model.pbxpl.ImpsConfirmPaymentResult;
import com.mom.app.model.pbxpl.ImpsCreateCustomerResult;
import com.mom.app.model.pbxpl.ImpsCustomerRegistrationResult;
import com.mom.app.model.pbxpl.ImpsPaymentProcessResult;
import com.mom.app.model.pbxpl.ImpsVerifyPaymentResult;
import com.mom.app.model.pbxpl.ImpsVerifyProcessResult;
import com.mom.app.model.pbxpl.PaymentResponse;
import com.mom.app.model.pbxpl.StateNameResult;
import com.mom.app.model.pbxpl.lic.LicLifeResponse;
import com.mom.app.ui.TransactionRequest;

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
    public void getComplaintType();
    public void passwordCountDetails(String psMobileNumber);
    public void getAllOperators();
    public void getForgotPassword(String sRMN , String sOperator ,String sAmount);

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
    public void giftVoucher(Operator sOperator, String sOccasion ,String sDescription , String sSentTo , String sSenTFrom ,
                            String sAmount , String sMobileNumber ,String EmailId , int nRechargeType , int nDeliveryMethod );
    public void payURequest(String sMobileNumber ,String EmailId , String sAmount);
    public void bookComplaint(String sOperator , int TransactionId , String sComments);

    public void getBillAmount(TransactionRequest request);

    public void getTransactionHistory();
    public void changePin(PinType pinType, String psOldPin, String psNewPin);
    public void changePassword(String psOldPin, String psNewPin);

    public abstract void balanceTransfer(TransactionRequest request, String payTo);
     public void signUpEncryptData(String composeData , String Key);

    public void signUpCustomerRegistration(String data);

    public void impsCustomerRegistrationStatus(String consumerNumber);
    public void impsCustomerRegistration(TransactionRequest request , String sConsumerNumber);
    public void impsAuthentication(TransactionRequest<ImpsAuthenticationResult> request);
    public void impsBeneficiaryList(String consumerNumber);

    public void registerIMPSCustomer(TransactionRequest request);
    public void resetIPIN(TransactionRequest request , String sCustomerID);
    public void getIMPSBeneficiaryList(TransactionRequest request);
    public void impsRefundDetails(TransactionRequest request);
    public void impsAddBeneficiary(TransactionRequest request);

    public void impsBeneficiaryDetails(TransactionRequest request , String sBeneficiaryName);
    public void impsCheckKYC(TransactionRequest request , String sConsumerNumber);
    public void getIMPSBankName(TransactionRequest request);
    public void getIMPSStateName(TransactionRequest request , String sBankName);
    public void getIMPSCityName(TransactionRequest request , String sBankName , String sStateName);
    public void getIMPSBranchName(TransactionRequest request , String sBankName , String sStateName , String sCityName);
    public void impsVerifyProcess(TransactionRequest<ImpsVerifyProcessResult> request);
    public void impsVerifyPayment(TransactionRequest<ImpsVerifyPaymentResult> request ,String sOTP ,
                                  String sAccountNumber ,String sIFSCCodeString , String sCustomerNumber);

    public void impsPaymentProcess(TransactionRequest<ImpsPaymentProcessResult>request , String sAmount ,String sTxnNarration);
    public void impsMomPaymentProcess(TransactionRequest<ImpsPaymentProcessResult>request , String sAmount ,String sTxnNarration);
    public void impsMomIMPSServiceCharge(TransactionRequest request ,String sAmount);
    public void impsMomConfirmProcess(TransactionRequest request ,  String sOTP ,
                                      String sAccountNumber ,String sIFSCCode , String sCustomerNumber);
    public void impsMomConfirmPaymentProcess(TransactionRequest request, String sAmount ,String sTxnNarration ,
                                         String sIPin ,  String sClientTxnID);

    public void impsMomConfirmPaymentProcessByRefund(TransactionRequest request , String sReceiptID ,String sTxnDescription ,
                                                     String sIPin , String sClientTxnID ) ;
    public void impsConfirmPayment(TransactionRequest<List<ImpsConfirmPaymentResult>> request , String sOTP ,
    String sAccountNumber ,String sIFSCCode , String sCustomerNumber , String sAmount);

    public  void lic(String lic , String customerMobNo);
    public void licPayment(TransactionRequest<LicLifeResponse> request , String CustomerMobNo , String PolicyNo , String fromDate);
}
