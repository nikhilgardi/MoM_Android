package com.mom.app.identifier;

import com.mom.app.R;

/**
* Created by vaibhavsinha on 10/15/14.
*/
public enum TransactionType {
    MOBILE (R.string.action_mobileRecharge),
    DTH (R.string.action_dthRecharge),
    BILL_PAYMENT (R.string.action_billPayment),
    UTILITY_BILL_PAYMENT(R.string.action_utilityBill),
    LIC (R.string.action_lIC),
    BALANCE_TRANSFER(R.string.action_balanceTransfer),
    IMPS_CREATE_CUSTOMER_REGISTRATION(R.string.action_IMPSCreateCustomerRegistration),
    IMPS_CHECK_KYC(R.string.action_IMPSCheckKYC),
    IMPS_CUSTOMER_REGISTRATION(R.string.action_IMPSCustomerRegistration),
    IMPS_BENEFICIARY_LIST(R.string.action_IMPSBeneficiaryList),
    IMPS_ADD_BENEFICIARY(R.string.action_IMPSAddBeneficiary),
    IMPS_BENEFICIARY_DETAILS(R.string.action_IMPSBeneficiaryDetails),
    IMPS_BANK_NAME_LIST(R.string.action_IMPSBankName),
    IMPS_STATE_NAME(R.string.action_IMPSStateName),
    IMPS_CITY_NAME(R.string.action_IMPSCityName),
    IMPS_BRANCH_NAME(R.string.action_IMPSBranchName),
    IMPS_VERIFY_PROCESS(R.string.action_IMPSVerifyProcess),
    IMPS_VERIFY_PAYMENT(R.string.action_IMPSVerifyPayment),
    IMPS_PAYMENT_PROCESS(R.string.action_IMPSPaymentProcess),
    IMPS_CONFIRM_PAYMENT(R.string.action_IMPSConfirmPayment),
    IMPS_MOM_SUBMIT_PROCESS(R.string.action_IMPSMOMSubmitPayment),
    IMPS_MOM_CONFIRM_PROCESS(R.string.action_IMPSMOMConfirmPayment),
    IMPS_MOM_SUBMIT_PAY_PROCESS(R.string.action_IMPSMOMSubmitPayPayment),
    IMPS_AUTHENTICATION(R.string.action_IMPSAuthentication),
    IMPS_AUTHENTICATION_MOM(R.string.action_IMPSAuthenticationMOM),
    MOM_T_PIN(R.string.action_MOMTPin),
    SIGN_UP_CONSUMER(R.string.action_SignUp);


    public int transactionTypeStringId;

    TransactionType(int id){
        this.transactionTypeStringId = id;
    }
}
