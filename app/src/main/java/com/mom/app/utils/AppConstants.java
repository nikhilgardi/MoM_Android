package com.mom.app.utils;

import java.util.HashMap;

public class AppConstants {
    public static final String APP_PREFERENCES = "AppPreferences";
    public static final String PACKAGE = "com.mom.app";
    public static final String LOG_PREFIX = "MOMAPP: ";
    public static final String UTF_8 = "utf-8";
    public static final String USER_BALANCE = "userBalance";
    public static final String RMN_BALANCE = "userBalance";
    public static final String USER_LANGUAGE = "userLanguage";
    public static final String KEY_LANGUAGE = PACKAGE + USER_LANGUAGE;
    public static final String ACTIVE_PLATFORM = "activePlatform";
    public static final String CURRENT_PLATFORM = "currentPlatform";


    /*
    Start: MoM Platform urls
     */
    public static final String URL_NEW_PLATFORM = "http://msvc.money-on-mobile.net/WebServiceV3Client.asmx";
    public static final String URL_NEW_PLATFORM_FORGOTPWD = "http://utilities.money-on-mobile.net/createuser/retailercreation.asmx";
    public static final String URL_NEW_PLATFORM_TXN = "http://msvc.money-on-mobile.net/WebServiceV3Trans.asmx";
    public static final String URL_NEW_PLATFORM_TXN_SIGNUP = "http://utilities.money-on-mobile.net/android_userservice/userservice.asmx";
    public static final String URL_NEW_PLATFORM_GET_BEST_BILL
            = "http://180.179.67.72/bestpayments/billInquiry.ashx";

    public static final String URL_NEW_PLATFORM_IMPS = "http://180.179.67.72/imps_appapi/IMPSService.asmx";
    //    public static final String URL_NEW_PLATFORM_GET_BEST_BILL
//            = "http://180.179.67.72/bestpayments/billInquiry.ashx?AccountNumber=";
    public static final String URL_NEW_PLATFORM_GET_RELIANCE_BILL
            = "http://180.179.67.72/RelianceEnergy/billEnquiry.ashx";
    public static final String URL_NEW_PLATFORM_GET_MGL_BILL
            = "http://180.179.67.72/mgl/billInquiry.aspx";

    //live url
    // public static final String URL_NEW_PL_DETAILS	        = "http://180.179.67.72/nokiaservice/DetailsByUserRMNCompID.aspx";
    public static final String URL_NEW_PL_HISTORY = "http://180.179.67.72/nokiaservice/Lastfivetransactions.aspx";

    public static final String URL_NEW_PL_DETAILS = "http://180.179.67.72/nokiaservice/GetUserDetailsByRMN.aspx";
    public static final String URL_NEW_BOOK_COMPLAINT_OPERATOR = "http://180.179.67.72/nokiaservice/GetComplaintTypes.aspx";
    public static final String URL_NEW_BOOK_COMPLAINT          = "http://180.179.67.72/nokiaservice/BookComplaint.aspx";
    public static final String URL_NEW_PAY_U_REQUEST           = "http://utilities.money-on-mobile.net/payu_serviceapp/Service1.asmx";


    //Start: MoM Platform Methods
    public static final String SVC_NEW_METHOD_LOGIN = "/getLoggedIn";
    public static final String SVC_NEW_METHOD_CHECK_TPIN = "/checkVallidTpin";
    public static final String SVC_NEW_METHOD_GET_BALANCE = "/getBalanceByCustomerId";
    public static final String SVC_NEW_METHOD_RECHARGE_MOBILE = "/DoMobRecharge";
    public static final String SVC_NEW_METHOD_RECHARGE_DTH = "/DoDTHRechargeV2";
    public static final String SVC_NEW_METHOD_BILL_PAYMENT = "/doBillPayment";
    public static final String SVC_NEW_METHOD_RETAILER_PAYMENT = "/doBalanceTransfer";
    public static final String SVC_NEW_METHOD_CHANGE_TPIN = "/ChangeT_Pin";
    public static final String SVC_NEW_METHOD_CHANGE_MPIN = "/ChangeM_Pin";
    public static final String SVC_NEW_METHOD_SIGN_UP_ENCRYPT_DATA = "/Encrypt";
    public static final String SVC_NEW_METHOD_SIGN_UP_CUSTOMER_REGISTRATION = "/CustomerRegistration";
    public static final String SVC_NEW_METHOD_IMPS_CUSTOMER_REGISTRATION = "/PbxIMPSApp.ashx";
    public static final String SVC_NEW_METHOD_IMPS_SUBMIT_PAYMENT = "/getSubmitPaymentDetails";
    public static final String SVC_NEW_METHOD_IMPS_CONFIRM_PAYMENT = "/getConfirmPaymentDetails";
    public static final String SVC_NEW_METHOD_IMPS_SERVICE_CHARGE_PAYMENT = "/getServiceCharge";
    public static final String SVC_NEW_METHOD_MOM_T_PIN = "/checkVallidTpin";
    public static final String SVC_NEW_METHOD_IMPS_AUTHENTICATION = "/GetIMPSUserStatus";
    public static final String SVC_NEW_METHOD_GET_PASSWORD_COUNT = "/getPasswordCount";
    public static final String SVC_NEW_METHOD_GET_FORGOTPASSWORD = "/getForgotPassword";
    public static final String SVC_NEW_METHOD_GET_PASSWORD_OPERATORS = "/ListOfOperators";
    public static final String SVC_NEW_METHOD_FLIPKART = "/DoFlipKart";
    public static final String SVC_NEW_METHOD_PAY_U     = "/PayuReqest";
    //End: MoM Platform Methods

    // MOM IMPS METHODS:

    public static final String SVC_NEW_METHOD_IMPS_MOM_CUSTOMER_STATUS = "/getConsumerStatus";
    public static final String SVC_NEW_METHOD_IMPS_MOM_RESET_IPIN = "/ResetIPIN";
    public static final String SVC_NEW_METHOD_IMPS_MOM_REFUND_DETAILS = "/getRefundDetails";
    public static final String SVC_NEW_METHOD_IMPS_MOM_CHECK_KYC = "/getConsumerKYCLimitandStatus";
    public static final String SVC_NEW_METHOD_IMPS_MOM_CUSTOMER_REGISTRATION   = "/RegisterCustomer";
    public static final String SVC_NEW_METHOD_IMPS_MOM_CREATE_BENEFICIARY = "/getCreateBeneficiary";
    public static final String SVC_NEW_METHOD_IMPS_MOM_BENEFICIARY_LIST = "/getBeneficiaryList";
    public static final String SVC_NEW_METHOD_IMPS_MOM_BENEFICIARY_DETAILS = "/getBeneficiaryDetails";
    public static final String SVC_NEW_METHOD_IMPS_MOM_IFSC_BANKS = "/getIFSCBanks";
    public static final String SVC_NEW_METHOD_IMPS_MOM_IFSC_STATE_NAME = "/getIFSCStatesByBankName";
    public static final String SVC_NEW_METHOD_IMPS_MOM_IFSC_CITY_NAME = "/getIFSCCityByBankNameAndStateName";
    public static final String SVC_NEW_METHOD_IMPS_MOM_IFSC_BRANCH_NAME = "/getIFSCBranchByBankStateAndCityName";
    public static final String SVC_NEW_METHOD_IMPS_SUBMIT_PAYMENT_IPIN = "/SubmitPaymentByIPIN";
    public static final String SVC_NEW_METHOD_IMPS_SUBMIT_PAYMENT_IPIN_REFUND = "/RefundPaymentByIPIN";


    //END MOM IMPS METHODS

    //START: MOM IMPS PARAMS:
    public static final String PARAM_NEW_MOM_IMPS_CONSUMERID = "ConsumerID";
    public static final String PARAM_NEW_MOM_IMPS_CONSUMER_NAME = "ConsumerName";
    public static final String PARAM_NEW_MOM_IMPS_CONSUMER_EMAIL = "Email";
    public static final String PARAM_NEW_MOM_IMPS_CONSUMER_DOB = "DOB";
    public static final String PARAM_NEW_IMPS_BANK_NAME = "BankName";
    public static final String PARAM_NEW_IMPS_STATE_NAME = "StateName";
    public static final String PARAM_NEW_IMPS_CITY_NAME  ="CityName";
    public static final String PARAM_NEW_IMPS_ACCOUNT_NAME  ="AccountName";
    public static final String PARAM_NEW_IMPS_ACCOUNT_NUMBER  ="AccountNumber";
    public static final String PARAM_NEW_IMPS_IFSC_CODE  ="IFSCCode";
    public static final String PARAM_NEW_MOM_IMPS_TRANSACTION_NARRATION = "TransactionNarration";
    public static final String PARAM_NEW_MOM_IMPS_IPIN = "IPIN";



    //Start: PBX Platform Service
    // public static final String SVC_PBX_CHECK_LOGIN              = "CL";

    public static final String SVC_PBX_CHECK_LOGIN = "LOGIN";
    public static final String SVC_PBX_CHANGE_PASSWORD = "CP";
    public static final String SVC_PBX_CHECK_BALANCE = "BL";
    public static final String SVC_PBX_TRANSACTION_HISTORY = "LTH";
    public static final String SVC_PBX_OPERATOR_NAMES = "GO";
    public static final String SVC_PBX_INTERNAL_BAL_TRANSFER = "IBT";
    public static final String SVC_PBX_RECHARGE_MOBILE = "RECHARGEMOBILE";
    public static final String SVC_PBX_RECHARGE_DTH = "DTH";
    public static final String SVC_PBX_BILL_PAY = "BILLPAY";
    public static final String SVC_PBX_UTILITY_BILL_PAY = "UBP";
    public static final String SVC_PBX_LIC = "LICENQ";
    public static final String SVC_PBX_LIC_STATUS = "ISLIC";

    public static final String PARAM_SERVICE_LIC = "LICPAY";
    public static final String PARAM_SERVICE_IMPS_CUSTOMER_STATUS = "GETCUSTOMERSTATUS";
    public static final String PARAM_SERVICE_IMPS_CUSTOMER_REGISTRATION = "CONSUMERRESISTER";
    public static final String PARAM_SERVICE_IMPS_AUTHENTICATION = "IMPSAUTHENTICATION";
    public static final String PARAM_SERVICE_IMPS_CHECK_KYC = "CHECKKYC";
    public static final String PARAM_SERVICE_IMPS_BENEFICIARY_LIST = "BENEFICIARYLIST";
    public static final String PARAM_SERVICE_IMPS_ADD_BENEFICIARY = "NEWBENEFICIARYREGISTER";
    public static final String PARAM_SERVICE_IMPS_BANK_NAME = "GETBANKNAME";
    public static final String PARAM_SERVICE_IMPS_STATE_NAME = "GETSTATE";
    public static final String PARAM_SERVICE_IMPS_CITY_NAME = "GETCITY";
    public static final String PARAM_SERVICE_IMPS_BRANCH_NAME = "GETBRANCH";
    public static final String PARAM_SERVICE_IMPS_BENEFICIARY_DETAILS = "BENEFICIARYDETAILS";
    public static final String PARAM_SERVICE_IMPS_VERIFY_PROCESS = "VERIFYPROCESS";
    public static final String PARAM_SERVICE_IMPS_VERIFY_PAYMENT = "VERIFYPAYMENT";
    public static final String PARAM_SERVICE_IMPS_PAYMENT_PROCESS = "PAYMENTPROCESS";
    public static final String PARAM_SERVICE_IMPS_CONFIRM_PAYMENT = "CONFIRMPAYMENT";
    public static final String PARAM_IS_EXIST_ID_PBX = "-1";
    //End: PBX Platform Service

    public static final String PBX_OPERATOR_TYPE_DEFAULT = "1";
    //Start: PBX Platform URLs

    public static final String URL_PBX_PLATFORM_SERVICE = "http://test.pbx.momappworld.com/momspre/service.ashx";
    //  public static final String URL_PBX_PLATFORM_APP         = "http://test.pbx.momappworld.com/momspre/PbxMobApp.ashx";
    // public static final String URL_PBX_PLATFORM_APP         =  "http://api.pbx.momappworld.com/mobapps1/pbxmobapp.ashx";
    public static final String URL_PBX_PLATFORM_APPLIC = "http://test.api.pbx.momappworld.com/PbxMobApp.ashx";
    //    public static final String URL_PBX_PLATFORM_APP         = "http://172.16.1.102/MobAppS/PbxMobApp.ashx";
    // LOCAL IMPS URL
    //public static final String URL_PBX_PLATFORM_IMPS           =   "http://test.api.pbx.momappworld.com/PbxIMPSApp.ashx";
    // PRODUCTION IMPS PBX
    public static final String URL_PBX_PLATFORM_IMPS = "http://api.pbx.momappworld.com";
    // public static final String URL_PBX_PLATFORM_IMPS           =   "http://192.168.8.20/testapipbx/PbxIMPSApp.ashx";
    //-- production
    public static final String URL_PBX_PLATFORM_APP = "http://utilities.money-on-mobile.net/lic_service/PbxMobApp.ashx";
    public static final String URL_PBX_PLATFORM_APP_LIC = "http://utilities.money-on-mobile.net/lic_service/PbxMobApp.ashx?";
    // local pbx url
    //public static final String URL_PBX_PLATFORM_APP          =  "http://test.api.pbx.momappworld.com/pbxmobapp.ashx";
//local url
    //  public static final String URL_PBX_PLATFORM_APP          =  "http://192.168.8.20/testapipbx/pbxmobapp.ashx";
//   public static final String URL_PBX_PLATFORM_APP          =  "http://test.api.pbx.momappworld.com/pbxmobapp.ashx";


    //End: PBX Platform URLs

    //Start: Bundle Messages
    public static final String BUNDLE_NEXT_SCREEN = PACKAGE + "NextScreen";
    public static final String BUNDLE_TRANSACTION_REQUEST = PACKAGE + "Transaction";
    public static final String BUNDLE_PROGRESS = PACKAGE + "Progress";
    public static final String BUNDLE_MESSAGE_CATEGORY = PACKAGE + "MessageCat";

    //End: Bundle Messages
    //Start: PBX Platform Methods

    //End: PBX Platform Methods

    //START: FONT RELATED
    public static final String FONT_DEFAULT_TEXT_STYLE = "regular";

    //END: FONT RELATED

    public static final float ERROR_BALANCE = -1000000;
    public static final int DEFAULT_INT = Integer.MIN_VALUE;

    //PIN RELATED
    public static final String PBX_CHANGE_PASSWORD = "Pbx-Pin";
    public static final String M_PIN = "M-Pin";
    public static final String T_PIN = "T-Pin";
    public static final String PIN = "Pin";
    //End get bill information

    //START: External Android constants
    public static String EXTERNAL_TEMP_IMAGES = "temp_images";
    //END: External Android constants

    //Get Bill information parameter
    public static final String PARAM_NEW_ACCOUNT_NUMBER = "AccountNumber";
    public static final String PARAM_NEW_CANUMBER = "CANumber";

    public static final String PARAM_NEW_USER = "strUserRMN";
    public static final String PARAM_NEW_PWD = "strPassword";
    public static final String PARAM_NEW_RMN = "UserRMN";
    public static final String PARAM_NEW_COMPANY_ID = "CompanyID";
    public static final String PARAM_NEW_OPERATOR_ID = "OperatorID";
    public static final String PARAM_NEW_STR_ACCESS_ID = "strAccessID";
    public static final String PARAM_NEW_STR_ACCESS_ID_SMALL_D
            = "strAccessId";
    public static final String PARAM_NEW_RETAILER_ID = "RetailerID";
    public static final String PARAM_NEW_USER_ID = "UserID";
    public static final String PARAM_NEW_USER_ID_LIC = "userId";
    public static final String PARAM_NEW_CUSTOMER_ID = "CustomerID";
    public static final String PARAM_NEW_MOBILE_NUMBER = "MobileNumber";
    public static final String PARAM_NEW_STR_MOBILE_NUMBER = "strMobileNumber";

// GIFT VOUCHER PARAMETERS
    public static final String PARAM_FLIP_KART_STATUS          = "StatusCode";
    public static final String PARAM_FLIP_KART_TRANSACTION_ID  = "TransactionID";
    public static final String PARAM_FLIP_KART_MOBILENUMBER    = "MobileNumber";
    public static final String PARAM_FLIP_KART_AMOUNT          = "Amount";
    public static final String PARAM_FLIP_KART_VOUCHER         = "Voucher";
    public static final String PARAM_FLIP_KART_PIN             = "Pin";
    public static final String PARAM_FLIP_KART_MESSAGE         = "Message";

 // PAY U PARAMETERS

    public static final String PARAM_NEW_STR_NAME                  = "Name";
    public static final String PARAM_NEW_STR_EMAIL                 = "Email";
    public static final String PARAM_NEW_STR_AMOUNT                = "Amount";
    public static final String PARAM_NEW_STR_MOBILE_NUMBER_PAY_U   = "Mobileno";


    public static final String PARAM_NEW_STR_OCCASION        = "Occasion";
    public static final String PARAM_NEW_STR_DESCRIPTION     = "Description";
    public static final String PARAM_NEW_STR_SENT_TO         = "SentTo";
    public static final String PARAM_NEW_STR_SENT_FROM       = "SentFrom";
    public static final String PARAM_NEW_STR_DELIVERY_METHOD = "DeliveryMethod";
    public static final String PARAM_NEW_STR_EMAIL_ID        = "EmailID";


    // WALLET UPDATE PARAMETERS

    public static final String PARAM_NEW_STR_COMPLAINT_TYPE        = "ComplaintType";
    public static final String PARAM_NEW_STR_TRANSACTION_ID        = "TransactionID";
    public static final String PARAM_NEW_STR_COMMENTS              = "Comments";
    public static final String PARAM_NEW_STR_COMPLAINT_BOOKED_BY   = "ComplentBookedBy";

   // public static final String PARAM_NEW_STR_COMPLAINTBOOKEDBY   = "ComplaintBookedBy";



    public static final String PARAM_NEW_RECHARGE_AMOUNT = "strRechargeAmount";
    public static final String PARAM_NEW_FORGOT_PWD_AMOUNT = "Amount";
    public static final String PARAM_NEW_OPERATOR = "strOperator";
    public static final String PARAM_NEW_INT_RECHARGE_TYPE = "intRechargeType";
    public static final String PARAM_NEW_INT_CUSTOMER_ID = "intCustomerId";
    public static final String PARAM_NEW_CUSTOMER_NUMBER = "CustomerNumber";
    public static final String PARAM_NEW_CONSUMER_NUMBER = "ConsumerNumber";
    public static final String PARAM_NEW_INT_OPERATOR_ID_BILL_PAY
            = "intOperatorIDBillPay";
    public static final String PARAM_NEW_DEC_BILL_AMOUNT = "DecBillAmount";
    public static final String PARAM_NEW_STR_CUSTOMER_NUMBER
            = "strCustomerNumber";
    public static final String PARAM_NEW_COMPANY_ID_CAMEL_CASE
            = "companyId";
    public static final String PARAM_NEW_STR_PAYER = "payer";
    public static final String PARAM_NEW_STR_PAYEE = "Payee";
    public static final String PARAM_NEW_STR_TRANSFERAMOUNT = "TransferAmount";
    public static final String PARAM_NEW_STR_TRANSVALUETPE = "Transvaltype";
    public static final String PARAM_NEW_STR_TRANSVALUE = "Refilled";

    public static final String PARAM_NEW_ROLE_ID = "RoleID";
    public static final String PARAM_NEW_USER_AUTH_ID = "UserAuthID";
    public static final String PARAM_NEW_USER_WALLET_ID = "UserWalletID";
    public static final String PARAM_NEW_USER_STATUS = "UserStatus";
    public static final String PARAM_NEW_ROLE_ID_RETAILER = "RetailerID";

    public static final String PARAM_NEW_USER_FRANCH_ID = "UserFranchID";
    public static final String PARAM_NEW_USER_MAST_DIST = "UserMastDist";
    public static final String PARAM_NEW_USER_AREA_DIST = "UserAreaDist";
    public static final String PARAM_NEW_USER_VAS01 = "UserVAS01";
    public static final String PARAM_NEW_USER_VAS02 = "UserVAS02";
    public static final String PARAM_NEW_TPIN = "strTPassword";

    public static final String PARAM_NEW_RELIANCE_SBE_NBE = "relianceSBENBE";
    public static final String PARAM_NEW_SPECIAL_OPERATOR = "specialOperator";
    public static final String PARAM_NEW_DUE_DATE = "dueDate";
    public static final String PARAM_NEW_AC_MONTH = "acmonth";
    public static final String PARAM_NEW_SD_CODE = "sdcode";
    public static final String PARAM_NEW_SOP = "sop";
    public static final String PARAM_NEW_FSA = "fsa";
    public static final String PARAM_NEW_STUBTYPE = "stubtype";
    public static final String PARAM_NEW_SPECIAL_OPERATOR_NBE = "specialOperatorNBE";
    public static final String PARAM_NEW_SPECIAL_OPERATOR_SBE = "specialOperatorSBE";
    public static final String PARAM_NEW_FIRST_NAME = "FirstNameSBE_NBE";

    public static final String PARAM_NEW_STR_PASSWORD = "strPassword";
    public static final String PARAM_NEW_STR_NEW_PASSWORD = "strNewPassword";
    public static final String PARAM_NEW_IMPS_AMOUNT = "Amount";
    public static final String PARAM_NEW_IMPS_PASSWORD = "Password";
    public static final String PARAM_NEW_IMPS_ConsumerID = "ConsumerID";
    public static final String PARAM_NEW_IMPS_ACCOUNT_ALIAS = "AccountAlias";
    public static final String PARAM_NEW_IMPS_PAYMENT_BENEFICIARY_ID = "BeneficiaryID";
    public static final String PARAM_NEW_IMPS_PAYMENT_REMITTANCE_AMOUNT = "RemittanceAmount";
    public static final String PARAM_NEW_IMPS_TRANSACTION_NARRATION = "Narration";
    public static final String PARAM_NEW_IMPS_CLIENTTxnID = "ClientTransactionID";
    public static final String PARAM_NEW_IMPS_SERVICE_CHARGE = "ServiceCharge";


    //Start: PBX Platform Params
    public static final String PARAM_PBX_SERVICE = "service";
    public static final String PARAM_PBX_USERNAME = "username";
    public static final String PARAM_PBX_PASSWORD = "password";
    public static final String PARAM_PBX_IDENTIFIER = "identifier";
    public static final String PARAM_PBX_ORIGIN_ID = "originId";
    public static final String PARAM_PBX_CLIENT_TOKEN = "clientToken";
    public static final String PARAM_PBX_CONSUMER_NUMBER_STATUS = "ConsumerNumber";
    public static final String PARAM_PBX_CONSUMER_NUMBER = "CustomerMobileNumber";
    public static final String PARAM_PBX_CONSUMER_NUMBER_UBP = "customerMobileNumber";
    public static final String PARAM_PBX_CONSUMER_NAME = "CustomerName";
    public static final String PARAM_PBX_CONSUMER_DOB = "CustomerDOB";
    public static final String PARAM_PBX_CONSUMER_EMAIL = "CustomerEmail";
    public static final String PARAM_PBX_ROWID = "partyRowId";
    public static final String PARAM_PBX_CLIENT_TYPE = "ClientType";

    public static final String PARAM_PBX_OPERATORS = "pbxOperators";
    public static final String PARAM_PBX_OP = "oldPassword";
    public static final String PARAM_PBX_NP = "newPassword";
    public static final String PARAM_PBX_RN = "RN";
    public static final String PARAM_PBX_USERID = "userID";
    public static final String PARAM_PBX_RMN = "rmn";
    public static final String PARAM_OPERATOR_TYPE = "ot";
    public static final String PARAM_PBX_NAME = "name";
    public static final String PARAM_PBX_TOKEN = "token";
    public static final String PARAM_PBX_CUSTOMER_NUMBER = "customerNumber";
    public static final String PARAM_PBX_OPERTAORSHORTCODE = "operatorShortCode";
    public static final String PARAM_PBX_AMOUNT = "amount";
    public static final String PARAM_PBX_USERTYPE = "userType";
    public static final String PARAM_PBX_USERNAMELOGIN = "userName";
    public static final String PARAM_PBX_LICAMOUNT = "premiumAmount";
    public static final String PARAM_PBX_IMPS_CONSUMER_NUMBER = "consumerNumber";
    public static final String PARAM_PBX_IMPS_CUSTOMER_ID = "customerID";
    public static final String PARAM_PBX_IMPS_KYC_CUSTOMER_ID = "customerId";

    public static final String PARAM_PBX_IMPS_ISBENEFICIAYSTATUS = "isIMPSBeneficiaryStatus";
    public static final String PARAM_PBX_IMPS_POSTINGSTATUS = "isIMPSPostingStatus";
    public static final String PARAM_PBX_IMPS_SESSIONID = "SessionID";
    public static final String PARAM_PBX_IMPS_SERVICEALLOWED = "PBXisIMPSServiceAllowed";
    public static final String PARAM_PBX_IMPS_ISREGISTERED = "isRegistered";
    public static final String PARAM_PBX_IMPS_BENEFICIARY_CUSTOMER_ID = "customerID";
    public static final String PARAM_PBX_IMPS_OTP = "OTP";
    public static final String PARAMS_PBX_IMPS_SESSION_ID = "SessionID";

    public static final String PARAM_PBX_IMPS_BENEFICIARY_ID = "PBXBeneficiaryID";
    public static final String PARAM_PBX_IMPS_BANK_NAME = "BankName";
    public static final String PARAM_PBX_IMPS_STATE_NAME = "StateName";
    public static final String PARAM_PBX_IMPS_CITY_NAME = "CityName";
    public static final String PARAM_PBX_IMPS_BENEFICIARY_ACCOUNT_NAME = "PBXBeneficiaryAccountName";
    public static final String PARAM_PBX_IMPS_ACCOUNT_NAME = "accountName";
    public static final String PARAM_PBX_IMPS_CUSTOMER_NUMBER = "CustomerNumber";
    public static final String PARAM_PBX_IMPS_IFSC_CODE = "ifscCode";
    public static final String PARAM_PBX_IMPS_VERIFY_IFSC_CODE = "iFSCCode";
    public static final String PARAM_PBX_IMPS_ACCOUNT_NUMBER = "accountNumber";
    public static final String PARAM_PBX_IMPS_BENEFICIARY_NAME = "beneficiaryName";
    public static final String PARAM_PBX_IMPS_BENEFICIARY_MOBILE_NUMBER = "PBXbeneficiaryMobileNumber";
    public static final String PARAM_PBX_IMPS_REMITTANCE_AMOUNT = "RemittanceAmount";
    public static final String PARAM_PBX_IMPS_TRANSACTION_NARRATION = "PbxTransactionNarration";
    public static final String PARAM_PBX_IMPS_TRANSACTION_NARRATION_DESC = "testingPBXIMPS";
    public static final String PARAM_IMPS_CLIENT_TYPE = "4";

    // IMPS PAYMENT PROCESS
    public static final String PARAM_PBX_IMPS_PAYMENT_REMITTANCE_AMOUNT = "remittanceAmount";
    public static final String PARAM_PBX_IMPS_PAYMENT_BENEFICIARY_ID = "beneficiaryID";
    public static final String PARAM_NEW_IMPS_PAYMENT_REFUND_RECEIPT_ID = "RefundTransactionID";

    //  IMPS CONFIRM PAYMENT

    public static final String PARAM_PBX_IMPS_CONFIRM_PAYMENT_REMITTANCE_AMOUNT = "paymentAmount";

    public static final String PARAM_NEW_SUBMIT_POSTING_STATUS = "SubmitPosting Status";
    public static final String PARAM_NEW_SUBMIT_SESSION_ID = "SubmitSession ID";
    public static final String PARAM_NEW_SUBMIT_ERROR_MSG = "SubmitErrorMessage";
    public static final String PARAM_NEW_SUBMIT_STATUS_CODE = "SubmitStatusCode";
    public static final String PARAM_NEW_SUBMIT_ERROR_CODE = "SubmitErrorCode";
    public static final String PARAM_NEW_SUBMIT_RECEIPT_ID = "SubmitReceiptID";
    public static final String PARAM_NEW_SUBMIT_CURRENT_BALANCE = "SubmitCurrentBalance";
    public static final String PARAM_NEW_SIGNUP_STATUS = "SignUpRegistrationStatus";
    public static final String PARAM_NEW_FORGOT_PASSWORD_CODE = "ForgotPwdCode";
    public static final String PARAM_NEW_FORGOT_PASSWORD_MESSAGE = "ForgotPwdMessage";
    public static final String PARAM_NEW_MOM_CUSTOMER_STATUS_IS_REGISTERED = "CustomerStatusIsRegistered";
//    public static final String PARAM_NEW_MOM_IMPS_CUSTOMER_STATUS_CUSTOMER_ID = "CustomerID";
    public static final String PARAM_NEW_MOM_IMPS_CUSTOMER_STATUS_CUSTOMER_ID = "IMPSCustomerStatusCustomerID";
    public static final String PARAM_NEW_MOM_REGISTER_CUSTOMER_REGISTRATION_STATUS = "CustomerRegistrationStatus";
    public static final String PARAM_NEW_MOM_REGISTER_CUSTOMER_CUSTOMER_ID = "RegisteredCustomerID";
    public static final String PARAM_NEW_MOM_REGISTER_CUSTOMER_ERROR_MSG = "CustomerRegistrationErrorMessage";
    public static final String PARAM_NEW_MOM_REGISTER_IFSC_BANK_NAMES_LIST = "BankName";
    public static final String PARAM_NEW_MOM_REGISTER_IFSC_STATE_NAMES_LIST = "StateName";
    public static final String PARAM_NEW_MOM_REGISTER_IFSC_CITY_NAMES_LIST = "CityName";

// IMPS REFUND RESPONSE STRINGS

    public static final String PARAM_NEW_MOM_IMPS_REFUND_RECEIPT_ID = "RefundReceiptID";
    public static final String PARAM_NEW_MOM_IMPS_REFUND_TRANSACTION_DESCRIPTION = "RefundTransactionDescription";
    public static final String PARAM_NEW_MOM_IMPS_REFUND_AMOUNT = "RefundAmount";
    public static final String PARAM_MOM_IMPS_REFUND_RECEIPT_ID_RESPONSE = "IMPSRefundReceiptID";


    public static final String PARAM_NEW_MOM_IMPS_BENEFICIARY_ID = "IMPSBeneficiaryID";
    public static final String PARAM_NEW_MOM_IMPS_BENEFICIARY_ACCOUNT_ALIAS= "BeneficiaryAccountAlias";
    public static final String PARAM_NEW_MOM_IMPS_BENEFICIARY_ACCOUNT_NAME = "BeneficiaryAccountName";
    public static final String PARAM_NEW_MOM_IMPS_BENEFICIARY_ACCOUNT_NUMBER = "BeneficiaryAccountNumber";
    public static final String PARAM_NEW_MOM_IMPS_BENEFICIARY_IFSC_CODE = "BeneficiaryIFSCCode";
    public static final String PARAM_NEW_MOM_IMPS_BENEFICIARY_MOBILE_NUMBER = "BeneficiaryMobileNumber";
    public static final String PARAM_NEW_MOM_IMPS_BENEFICIARY_VERIFICATION_STATUS = "BeneficiaryVerificationStatus";

    public static final String PARAM_NEW_MOM_REGISTER_GET_BENEFICIARY_LIST_RESULT = "GetBeneficiaryListResult";
    public static final String PARAM_NEW_MOM_REGISTER_GET_BENEFICIARY_LIST_RESULT_DETAIL = "GetBeneficiaryListResultDetail";
    public static final String PARAM_NEW_MOM_CUSTOMER_STATUS_IS_IMPS_AUTHORISED = "CustomerStatusIsIMPSServiceAllowed";
    public static final String PARAM_NEW_MOM_REGISTER_GET_BRANCH_LIST_RESULT = "GetBranchListResult";
    public static final String PARAM_NEW_MOM_REGISTER_GET_BRANCH_LIST_RESULT_DETAIL = "GetBranchListResultDetail";
    public static final String PARAM_NEW_MOM_CHECK_KYC_AVAILABLE_LIMIT = "CheckKYCAvailableLimit";
    public static final String PARAM_NEW_MOM_CHECK_KYC_isFullKYC = "CheckKYCisFullKYC";

    public static final String PARAM_NEW_MOM_IMPS_ADD_BENEFICIARY_REGISTRATION_STATUS = "AddBeneficiaryRegistrationStatus";
    public static final String PARAM_NEW_MOM_IMPS_ADD_BENEFICIARY_BENEFICIARY_ID = "AddBeneficiaryBeneficiaryID";
    public static final String PARAM_NEW_MOM_IMPS_ADD_BENEFICIARY_ERROR_MESSAGE = "AddBeneficiaryErrorMessage";

    public static final String PARAM_NEW_MOM_IMPS_CONFIRM_PAYMENT_STATUS_BY_REFUND = "RefundPaymentStatus";
    public static final String PARAM_NEW_MOM_IMPS_CONFIRM_RECEIPT_ID_BY_REFUND = "RefundReceiptID";
    public static final String PARAM_NEW_MOM_IMPS_CONFIRM_PAYMENT_ERROR_CODE_BY_REFUND = "RefundErrorCode";
    public static final String PARAM_NEW_MOM_IMPS_CONFIRM_PAYMENT_ERROR_MESSAGE_BY_REFUND = "RefundErrorMessage";
    public static final String PARAM_NEW_MOM_IMPS_CONFIRM_CLIENT_TXN_ID_BY_REFUND = "RefundClientTransactionID";
    public static final String PARAM_NEW_MOM_IMPS_CONFIRM_TxnAMOUNT_ID_BY_REFUND = "RefundTransactionAmount";


    public static final String PARAM_NEW_MOM_IMPS_CONFIRM_PAYMENT_STATUS = "ConfirmPaymentStatus";
    public static final String PARAM_NEW_MOM_IMPS_CONFIRM_PAYMENT_ERROR_CODE = "ConfirmErrorCode";
    public static final String PARAM_NEW_MOM_IMPS_CONFIRM_PAYMENT_ERROR_MESSAGE = "ConfirmErrorMessage";
    public static final String PARAM_NEW_MOM_IMPS_CONFIRM_CLIENT_TXN_ID = "ConfirmClientTransactionID";
    public static final String PARAM_NEW_MOM_IMPS_CONFIRM_RECEIPT_ID = "ConfirmReceiptID";


    public static final String PARAM_NEW_MOM_REFUND_RECEIPT_ID_MAP= "RefundReceiptTxnDesc";
    public static final String PARAM_NEW_MOM_REFUND_AMOUNT_MAP= "RefundAmountTxnDesc";
    public static final String PARAM_NEW_MOM_REFUND_TRANSACTION_DESCRIPTION_LIST= "RefundTxnDescList";

    public static final String PARAM_PBX_ACCOUNT_NUMBER = "account";
    public static final String PARAM_PBX_DUE_DATE = "dueDate";
    public static final String PARAM_PBX_AC_MONTH = "acMonth";
    public static final String PARAM_PBX_STUBTYPE = "stubType";

    public static final String PARAM_PBX_PARENT_RMN = "ParentRmn";
    public static final String PARAM_PBX_CHILD_RMN = "childRmn";
    public static final String PARAM_LICREFNO = "licRefNo";
    public static final String PARAM_CUSTOMER_NUMBER_LIC = "CustMobNo";
    public static final String PARAM_POLICYNUMBER_LIC = "policyNo";
    public static final String PARAM_TRANSREFGUID_LIC = "transRefGuid";
    public static final String PARAM_TRANSINVGUID_LIC = "transInvGuid";
    public static final String PARAM_POLICYAMOUNT_LIC = "policyAmount";
    public static final String PARAM_POLICYHOLDER_LIC = "policyHolder";
    public static final String PARAM_FRUNPAIDPREMIUMDATE = "UnpaidPremiumDate";
    public static final String PARAM_MERCHANTID_LIC = "merchantId";
    public static final String PARAM_IsLIC = "isLic";

    public static final String PARAM_SERVICE_NEW = "Service";

    public static final String PARAM_SERVICE_NEW_LIC_STATUS = "Service=";
    public static final String PARAM_SERVICE_NEW_LIC_STATUS_USERID = "&userID=";

    //End: PBX Platform Params

    public static final String PARAM_GCM_PAYLOAD = "payload";
    public static final int TIMEOUT_CONNECTION = 15000;
    public static final int TIMEOUT_SOCKET = 45000;
    public static final int STANDARD_MOBILE_NUMBER_LENGTH = 10;

    public static final String IS_LOGGED_IN = "isLoggedIn";
    public static final String LOGGED_IN_USERNAME = "username";
    public static final String LOGGED_IN_PASSWORD = "password";

    public static final String NEW_PL_COMPANY_ID = "184";
    public static final String NEW_B2C_COMPANY_ID = "2365";
    public static final int NEW_PL_LOGIN_SUCCESS = 101;
    public static final int NEW_PL_TPIN_VERIFIED = 101;

    public static final String INTENT_MESSAGE = PACKAGE + "MESSAGE";
    public static final String INTENT_MESSAGE_DEST = PACKAGE + "MESSAGE_DEST";
    public static final String INTENT_MESSAGE_ORIGIN = PACKAGE + "MESSAGE_ORIGIN";
    public static final String INTENT_GCM = PACKAGE + "intentGcm";
    public static final String INTENT_NETWORK = PACKAGE + "intentNetwork";
    public static final String SIGNUP_STATUS = PACKAGE + "MESSAGE_SignUp";

    public static String NETWORK_STATUS_CONNECTED = "networkStatusConnected";

    public static final String OPERATOR_ID_FLIPKART = "86";

    public static final String OPERATOR_ID_AIRCEL = "1";
    public static final String OPERATOR_ID_AIRTEL = "2";
    public static final String OPERATOR_ID_BSNL = "5";
    public static final String OPERATOR_ID_DATACOMM = "33";
    public static final String OPERATOR_ID_IDEA = "28";
    public static final String OPERATOR_ID_LOOP = "9";
    public static final String OPERATOR_ID_MOM_CARD = "46";
    public static final String OPERATOR_ID_MTNL = "23";
    public static final String OPERATOR_ID_MTS = "10";

    public static final String OPERATOR_ID_QUE = "72";
    public static final String OPERATOR_ID_RELIANCE_CDMA = "12";
    public static final String OPERATOR_ID_RELIANCE_GSM = "32";
    public static final String OPERATOR_ID_STEL = "29";
    public static final String OPERATOR_ID_TATA = "15";

    public static final String OPERATOR_ID_TATA_DOCOMO = "16";
    public static final String OPERATOR_ID_TATA_WALKY = "37";
    public static final String OPERATOR_ID_UNINOR = "30";
    public static final String OPERATOR_ID_VIRGIN = "19";
    public static final String OPERATOR_ID_VODAFONE = "25";

    public static final String OPERATOR_ID_AIRTEL_DIGITAL = "21";
    public static final String OPERATOR_ID_BIG_TV = "27";
    public static final String OPERATOR_ID_DISH = "7";
    public static final String OPERATOR_ID_SUN_DIRECT = "14";
    public static final String OPERATOR_ID_TATA_SKY = "24";

    public static final String OPERATOR_ID_VIDEOCON_DTH = "18";
    public static final String OPERATOR_ID_AIRCEL_BILL = "39";
    public static final String OPERATOR_ID_AIRTEL_BILL = "3";
    public static final String OPERATOR_ID_AIRTEL_LAND_LINE = "68";
    public static final String OPERATOR_ID_BESCOM_BANGALURU = "61";
    public static final String OPERATOR_ID_BEST_ELECTRICITY = "55";
    public static final String OPERATOR_ID_BSES_RAJDHANI = "58";
    public static final String OPERATOR_ID_BSES_YAMUNA = "70";
    public static final String OPERATOR_ID_BSNL_BILL_PAY = "6";
    public static final String OPERATOR_ID_CELLONE_BILL_PAY = "56";
    public static final String OPERATOR_ID_CESC_LIMITED = "57";

    public static final String OPERATOR_ID_CESCOM_MYSORE = "62";
    public static final String OPERATOR_ID_DHBVN_HARYANA = "59";
    public static final String OPERATOR_ID_DELHI_JAL_BOARD = "74";
    public static final String OPERATOR_ID_IDEA_BILL = "35";
    public static final String OPERATOR_ID_INDRAPRASTHA_GAS = "63";
    public static final String OPERATOR_ID_MAHANAGAR_GAS = "45";
    public static final String OPERATOR_ID_NBE = "49";
    public static final String OPERATOR_ID_RELIANCE_BILL_GSM
            = "36";
    public static final String OPERATOR_ID_RELIANCE_BILL_CDMA
            = "11";
    public static final String OPERATOR_ID_RELIANCE_ENERGY = "51";
    public static final String OPERATOR_ID_SBE = "50";
    public static final String OPERATOR_ID_TATA_BILL = "42";
    public static final String OPERATOR_ID_TATA_POWER_DELHI = "67";
    public static final String OPERATOR_ID_TIKONA_BILL = "44";
    public static final String OPERATOR_ID_UHBVN_HARYANA = "60";
    public static final String OPERATOR_ID_VODAFONE_BILL = "20";


    public static HashMap<String, String> OPERATOR_PBX = new HashMap<String, String>();

    static {
        OPERATOR_PBX.put("CEL", "AIRCEL");
        OPERATOR_PBX.put("AIR", "AIRTEL");
        OPERATOR_PBX.put("BST", "BSNL");
        OPERATOR_PBX.put("BSV", "BSNL VALIDITY");
        OPERATOR_PBX.put("DTC", "DATACOMM");
        OPERATOR_PBX.put("IDE", "IDEA");
        OPERATOR_PBX.put("ACT", "MOM CARD ACTIVATION");
        OPERATOR_PBX.put("MOM", "MOM CARD REFILL");
        OPERATOR_PBX.put("MTS", "MTS");
        OPERATOR_PBX.put("REL", "RELIANCE CDMA");
        OPERATOR_PBX.put("RGM", "RELIANCE GSM");
        OPERATOR_PBX.put("TID", "TATA");
        OPERATOR_PBX.put("DCM", "TATA DOCOMO");
        OPERATOR_PBX.put("SDC", "TATA DOCOMO SPECIAL RECHARGE");
        OPERATOR_PBX.put("TWT", "TATA WALKY");
        OPERATOR_PBX.put("UNI", "UNINOR");
        OPERATOR_PBX.put("UNS", "UNINOR SPECIAL RECHARGE");
        OPERATOR_PBX.put("VIN", "VIDEOCON MOBILE TOPUP");
        OPERATOR_PBX.put("VIR", "VIRGIN");
        OPERATOR_PBX.put("VOD", "VODAFONE");

        OPERATOR_PBX.put("ADG", "AIRTEL DTH");
        OPERATOR_PBX.put("DSH", "DISH TV");
        OPERATOR_PBX.put("BIG", "RELIANCE BIGTV");
        OPERATOR_PBX.put("SUN", "SUN DIRECT");
        OPERATOR_PBX.put("TSK", "TATA SKY");
        OPERATOR_PBX.put("D2H", "VIDEOCON D2H");

        OPERATOR_PBX.put("BAI", "AIRTEL BILL");
        OPERATOR_PBX.put("BAC", "AIRCEL BILL");
        OPERATOR_PBX.put("BLA", "AIRTELLANDLINE");
        OPERATOR_PBX.put("BLL", "BSNL BILL");
        OPERATOR_PBX.put("BID", "IDEA BILL");
        OPERATOR_PBX.put("BRC", "RELIANCE BILL");
        OPERATOR_PBX.put("BRG", "RELIANCE BILL GSM");
        OPERATOR_PBX.put("BTA", "TATA BILL");
        OPERATOR_PBX.put("BVO", "VODAFONE BILL");

        OPERATOR_PBX.put("CES", "CALCUTTA ELECTRIC SUPPLY CORPORATION");

    }

    public static HashMap<String, String> OPERATOR_NEW = new HashMap<String, String>();

    static {
        OPERATOR_NEW.put(AppConstants.OPERATOR_ID_AIRCEL, "AIRCEL");
        OPERATOR_NEW.put(AppConstants.OPERATOR_ID_AIRTEL, "AIRTEL");
        OPERATOR_NEW.put(AppConstants.OPERATOR_ID_BSNL, "BSNL");
        OPERATOR_NEW.put(AppConstants.OPERATOR_ID_DATACOMM, "DATACOMM");
        OPERATOR_NEW.put(AppConstants.OPERATOR_ID_IDEA, "IDEA");
        OPERATOR_NEW.put(AppConstants.OPERATOR_ID_LOOP, "LOOP");

        OPERATOR_NEW.put(AppConstants.OPERATOR_ID_MOM_CARD, "MOM CARD REFILL");
        OPERATOR_NEW.put(AppConstants.OPERATOR_ID_MTNL, "MTNL");
        OPERATOR_NEW.put(AppConstants.OPERATOR_ID_MTS, "MTS");

        OPERATOR_NEW.put(AppConstants.OPERATOR_ID_QUE, "QUE MOBILE");
        OPERATOR_NEW.put(AppConstants.OPERATOR_ID_RELIANCE_CDMA, "RELIANCE CDMA");
        OPERATOR_NEW.put(AppConstants.OPERATOR_ID_RELIANCE_GSM, "RELIANCE GSM");
        OPERATOR_NEW.put(AppConstants.OPERATOR_ID_STEL, "STEL");
        OPERATOR_NEW.put(AppConstants.OPERATOR_ID_TATA, "TATA");
        OPERATOR_NEW.put(AppConstants.OPERATOR_ID_TATA_DOCOMO, "TATA DOCOMO");
        OPERATOR_NEW.put(AppConstants.OPERATOR_ID_TATA_WALKY, "TATA WALKY");
        OPERATOR_NEW.put(AppConstants.OPERATOR_ID_UNINOR, "UNINOR");

        OPERATOR_NEW.put(AppConstants.OPERATOR_ID_VIRGIN, "VIRGIN");
        OPERATOR_NEW.put(AppConstants.OPERATOR_ID_VODAFONE, "VODAFONE");

        OPERATOR_NEW.put(AppConstants.OPERATOR_ID_AIRTEL_DIGITAL, "AIRTEL DIGITAL");
        OPERATOR_NEW.put(AppConstants.OPERATOR_ID_BIG_TV, "BIG TV");
        OPERATOR_NEW.put(AppConstants.OPERATOR_ID_DISH, "DISH");
        OPERATOR_NEW.put(AppConstants.OPERATOR_ID_SUN_DIRECT, "SUN DIRECT");

        OPERATOR_NEW.put(AppConstants.OPERATOR_ID_TATA_SKY, "TATA SKY");
        OPERATOR_NEW.put(AppConstants.OPERATOR_ID_VIDEOCON_DTH, "VIDEOCON DTH");

//Bill Payment
        OPERATOR_NEW.put(AppConstants.OPERATOR_ID_AIRCEL_BILL, "AIRCEL BILL");
        OPERATOR_NEW.put(AppConstants.OPERATOR_ID_AIRTEL_BILL, "AIRTEL BILL");
        OPERATOR_NEW.put(AppConstants.OPERATOR_ID_AIRTEL_LAND_LINE, "AIRTEL LAND LINE");

        OPERATOR_NEW.put(AppConstants.OPERATOR_ID_BESCOM_BANGALURU, "BESCOM BANGALURU");
        OPERATOR_NEW.put(AppConstants.OPERATOR_ID_BEST_ELECTRICITY, "BEST ELECTRICITY BILL");
        OPERATOR_NEW.put(AppConstants.OPERATOR_ID_BSES_RAJDHANI, "BSES RAJDHANI");
        OPERATOR_NEW.put(AppConstants.OPERATOR_ID_BSES_YAMUNA, "BSES YAMUNA");
        OPERATOR_NEW.put(AppConstants.OPERATOR_ID_BSNL_BILL_PAY, "BSNL BILL PAY");
        OPERATOR_NEW.put(AppConstants.OPERATOR_ID_CELLONE_BILL_PAY, "CELLONE BILL PAY");
        OPERATOR_NEW.put(AppConstants.OPERATOR_ID_CESC_LIMITED, "CESC LIMITED");
        OPERATOR_NEW.put(AppConstants.OPERATOR_ID_CESCOM_MYSORE, "CESCOM MYSORE");
        OPERATOR_NEW.put(AppConstants.OPERATOR_ID_DHBVN_HARYANA, "DHBVN HARYANA");
        OPERATOR_NEW.put(AppConstants.OPERATOR_ID_DELHI_JAL_BOARD, "DELHI JAL BOARD");
        OPERATOR_NEW.put(AppConstants.OPERATOR_ID_IDEA_BILL, "IDEA BILL");
        OPERATOR_NEW.put(AppConstants.OPERATOR_ID_INDRAPRASTHA_GAS, "INDRAPRASTH GAS");
        OPERATOR_NEW.put(AppConstants.OPERATOR_ID_MAHANAGAR_GAS, "MAHANAGAR GAS BILL");
        OPERATOR_NEW.put(AppConstants.OPERATOR_ID_NBE, "NORTH BIHAR ELECTRICITY");
        OPERATOR_NEW.put(AppConstants.OPERATOR_ID_RELIANCE_BILL_GSM, "RELIANCE BILL GSM");
        OPERATOR_NEW.put(AppConstants.OPERATOR_ID_RELIANCE_BILL_CDMA, "RELIANCE CDMA BILL");
        OPERATOR_NEW.put(AppConstants.OPERATOR_ID_RELIANCE_ENERGY, "RELIANCE ENERGY BILL");
        OPERATOR_NEW.put(AppConstants.OPERATOR_ID_SBE, "SOUTH BIHAR ELECTRICITY");
        OPERATOR_NEW.put(AppConstants.OPERATOR_ID_TATA_BILL, "TATA BILL");
        OPERATOR_NEW.put(AppConstants.OPERATOR_ID_TATA_POWER_DELHI, "TATA POWER DELHI");
        OPERATOR_NEW.put(AppConstants.OPERATOR_ID_TIKONA_BILL, "TIKONA BILL PAYMENT");
        OPERATOR_NEW.put(AppConstants.OPERATOR_ID_UHBVN_HARYANA, "UHBVN HARYANA");
        OPERATOR_NEW.put(AppConstants.OPERATOR_ID_VODAFONE_BILL, "VODAFONE BILL");
    }

    public static HashMap<String, String> OPERATOR_GIFT_VOUCHER = new HashMap<String, String>();

        static {
            OPERATOR_GIFT_VOUCHER.put(AppConstants.OPERATOR_ID_FLIPKART, "FLIPKART");
    }



}
