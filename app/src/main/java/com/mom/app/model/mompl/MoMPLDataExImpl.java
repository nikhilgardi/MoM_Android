package com.mom.app.model.mompl;

import android.content.Context;
import android.renderscript.Sampler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.internal.is;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.mom.app.error.MOMException;
import com.mom.app.identifier.PinType;
import com.mom.app.model.AsyncDataEx;
import com.mom.app.model.AsyncListener;
import com.mom.app.model.AsyncResult;
import com.mom.app.model.DataExImpl;
import com.mom.app.model.Operator;
import com.mom.app.model.Transaction;
import com.mom.app.model.local.EphemeralStorage;
import com.mom.app.model.pbxpl.BankNameResult;
import com.mom.app.model.pbxpl.BeneficiaryResult;
import com.mom.app.model.pbxpl.BranchNameResult;
import com.mom.app.model.pbxpl.CityNameResult;
import com.mom.app.model.pbxpl.ImpsAddBeneficiaryResult;
import com.mom.app.model.pbxpl.ImpsBeneficiaryDetailsResult;
import com.mom.app.model.pbxpl.ImpsCheckKYCResult;
import com.mom.app.model.pbxpl.ImpsConfirmPaymentResult;
import com.mom.app.model.pbxpl.ImpsCreateCustomerResult;
import com.mom.app.model.pbxpl.ImpsCustomerRegistrationResult;
import com.mom.app.model.pbxpl.ImpsPaymentProcessResult;
import com.mom.app.model.pbxpl.ImpsVerifyPaymentResult;
import com.mom.app.model.pbxpl.ImpsVerifyProcessResult;
import com.mom.app.model.pbxpl.PaymentResponse;
import com.mom.app.model.pbxpl.ResponseBase;
import com.mom.app.model.pbxpl.StateNameResult;
import com.mom.app.model.pbxpl.lic.LicLife;
import com.mom.app.model.pbxpl.lic.LicLifeResponse;
import com.mom.app.model.pbxpl.lic.LicResponse;
import com.mom.app.model.xml.PullParser;
import com.mom.app.model.xml.PullParserBeneficiaryDetailsHandler;
import com.mom.app.model.xml.PullParserConfirmPaymentHandler;
import com.mom.app.model.xml.PullParserConfirmPaymentRefundResponseHandler;
import com.mom.app.model.xml.PullParserCreateBeneficiaryHandler;
import com.mom.app.model.xml.PullParserCustomerRegistrationHandler;
import com.mom.app.model.xml.PullParserHandler;

import com.mom.app.model.xml.PullParserHandlerTest;
import com.mom.app.model.xml.PullParserRefundResponseHandler;
import com.mom.app.ui.TransactionRequest;
import com.mom.app.utils.AppConstants;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by vaibhavsinha on 7/5/14.
 */
public class MoMPLDataExImpl extends DataExImpl implements AsyncListener<TransactionRequest>{

    private String _LOG = AppConstants.LOG_PREFIX + "DATAEX_NEW";
    private static MoMPLDataExImpl _instance;

    private String _operatorId      = null;
    private String PostingStatus ;
    private String sessionID ;
    private String errorMessage ;
    private String statusCode;
    private String errorCode;
    private String receiptID;
    private String currentBalance;
    AsyncDataEx dataEx =null;

    public volatile boolean parsingComplete = true;

  //  TransactionRequest transactionRequest = new TransactionRequest();
//
//    public MoMPLDataExImpl(Context pContext, AsyncListener pListener, boolean isBalance){
//        _applicationContext    = pContext;
//        if(!isBalance){
//            _listener   = pListener;
//        }else{
//            _balance_listener = pListener;
//        }
//
//        checkConnectivity(pContext);
//
//    }
//    public MoMPLDataExImpl(Context pContext, AsyncListener pListener){
//        this(pContext,pListener,false);
//    }

    public MoMPLDataExImpl(Context pContext, AsyncListener pListener){
        _applicationContext    = pContext;
        _listener   = pListener;
        checkConnectivity(pContext);

    }



    @SuppressWarnings("unchecked")
    @Override
    public void onTaskSuccess(TransactionRequest result, Methods callback) {
        Log.d(_LOG, "onTaskSuccess");
        if(_listener == null){
            Log.e(_LOG, "No listener!");
            return;
        }

        try {
            /*
            Since all these transactions are synchronous, set them as completed.
             */
            result.setCompleted(true);
            result.setDateCompleted(new Date());

            switch (callback) {
                case CHECK_PLATFORM_DETAILS:
                    Log.d(_LOG, "TaskComplete: Check platform, result: " + result);
                    if (_listener != null) {
                        _listener.onTaskSuccess(result.getRemoteResponse(), callback);
                    }
                    break;
                case CHECK_LIC:
                    Log.d(_LOG, "TaskComplete: Check Lic, result: " + result);
                    if (_listener != null) {
                        _listener.onTaskSuccess(result.getRemoteResponse(), callback);
                    }
                    break;
//                case CHECK_LIC:
//                    Log.d(_LOG, "TaskComplete: Check LIC Info method, result: " + result);
//                    if (_listener != null) {
//
//                        TransactionRequest<String> response = getLICInfo(result, Methods.CHECK_LIC);
//                        _listener.onTaskSuccess(response, callback);
//                    }
//                    break;
                case GET_COMPLAINT_TYPE:
                    Log.d(_LOG, "TaskComplete: getComplaintType method, result: " + result);


//                    if (_listener != null) {
//
//                        TransactionRequest<String> response = getComplaintTypeResponse(result , Methods.GET_COMPLAINT_TYPE);
//                        _listener.onTaskSuccess(response, callback);
//                    }

                    if (_listener != null) {
                        _listener.onTaskSuccess(result.getRemoteResponse(), callback);
                    }
                    break;

                case GET_BOOK_COMPLAINT:
                    Log.d(_LOG, "TaskComplete:GetBookComplaint, result: " + result);
                    if (_listener != null) {
                        _listener.onTaskSuccess(result.getRemoteResponse(), callback);
                    }
                    break;
                case LOGIN:
                    Log.d(_LOG, "TaskComplete: Login method, result: " + result);
                    if (_listener != null) {
                       _listener.onTaskSuccess(loginSuccessful(result), callback);


                    }
                    break;
                case VERIFY_TPIN:
                    Log.d(_LOG, "TaskComplete: verifyTPin method, result: " + result);
                   // boolean bTPinSuccess = tpinVerified(result.getRemoteResponse());

                    if (_listener != null) {
                      //  _listener.onTaskSuccess((new Boolean(bTPinSuccess)), callback);
                     //   _listener.onTaskSuccess(tpinVerified(result), callback);
                        TransactionRequest<String> response = getVerifyTpin(result , Methods.VERIFY_TPIN);
                        _listener.onTaskSuccess(response, callback);
                    }
                    break;


                case PASSWORD_COUNT_DETAILS:
                    Log.d(_LOG, "TaskComplete: RMN CountDetails method, result: " + result);
                     if (_listener != null) {

                        TransactionRequest<String> response = getRMNCountDetails(result , Methods.PASSWORD_COUNT_DETAILS);
                        _listener.onTaskSuccess(response, callback);
                    }
                    break;
                case GET_ALL_OPERATORS_FORGOTPWD:
                    Log.d(_LOG, "TaskComplete: RMN CountDetails method, result: " + result);
                    if (_listener != null) {

                        TransactionRequest<String> response = getAllOperatorResult(result , Methods.GET_ALL_OPERATORS_FORGOTPWD);
                        _listener.onTaskSuccess(response, callback);
                    }
                    break;
                case GET_FORGOT_PASSWORD:
                    Log.d(_LOG, "TaskComplete: RMN CountDetails method, result: " + result);
                    if (_listener != null) {

                        TransactionRequest<String> response = getForgotPasswordResult(result , Methods.GET_FORGOT_PASSWORD);
                        _listener.onTaskSuccess(response, callback);
                    }
                    break;


                case GET_BALANCE:
                    Log.d(_LOG, "TaskComplete: getBalance method, result: " + result);
                    float balance = extractBalance(result);

                    if (_listener != null) {
                        _listener.onTaskSuccess(balance, Methods.GET_BALANCE);
                    }


//                    if(_balance_listener!=null){
//                        _balance_listener.onTaskSuccess(balance, callback);
//                    }


                    break;
                case SIGN_UP_ENCRYPT_DATA:
                    Log.d(_LOG, "TaskComplete: signUpEncrypt method, result: " + result);
                    if (_listener != null) {
                        TransactionRequest<String> response = getPaymentResult(result, Methods.SIGN_UP_ENCRYPT_DATA);
                        _listener.onTaskSuccess(response, Methods.SIGN_UP_ENCRYPT_DATA);

                    }
                    break;

                case SIGN_UP_CONSUMER:
                    Log.d(_LOG, "TaskComplete: signUpConsumer method, result: " + result);
                    if (_listener != null) {
                     TransactionRequest<String> response = getSignUpResult(result, Methods.SIGN_UP_CONSUMER);
                     _listener.onTaskSuccess(response, Methods.SIGN_UP_CONSUMER);

                    }
                    break;

                case RECHARGE_MOBILE:
                    Log.d(_LOG, "TaskComplete: rechargeMobile method, result: " + result);
                    if (_listener != null) {
                        TransactionRequest<String> response = getPaymentResult(result, Methods.RECHARGE_MOBILE);
                        _listener.onTaskSuccess(response, Methods.RECHARGE_MOBILE);
                    }
                    break;
                case RECHARGE_DTH:
                    Log.d(_LOG, "TaskComplete: rechargeDTH method, result: " + result);
                    if (_listener != null) {
                        TransactionRequest<String> response = getPaymentResult(result , Methods.RECHARGE_DTH);
                        _listener.onTaskSuccess(response, Methods.RECHARGE_DTH);
                    }
                    break;
                case PAY_BILL:
                    Log.d(_LOG, "TaskComplete: payBill method, result: " + result);

                    if (_listener != null) {
                        TransactionRequest<String> response = getPaymentResult(result , Methods.PAY_BILL);
                        _listener.onTaskSuccess(response, Methods.PAY_BILL);
                    }
                    break;

                case LIC:
                    Log.d(_LOG, "TaskComplete: lic method, result: " + result);

                    if (_listener != null) {

                        _listener.onTaskSuccess(getPremiumAmount(result), Methods.LIC);
                    }

                    break;

                case PAY_LIC:
                    Log.d(_LOG, "TaskComplete: Pay lic method, result: " + result);

                    if (_listener != null) {

                        _listener.onTaskSuccess(getLic(result), Methods.PAY_LIC);
                    }

                    break;

                case IMPS_AUTHENTICATION_MOM:
                    Log.i(_LOG, "TaskComplete: IMPS Authentication method, result: " + result);
                    if (_listener != null) {
                        TransactionRequest<String> response = getPaymentResult(result , Methods.IMPS_AUTHENTICATION_MOM);
                        _listener.onTaskSuccess(response, Methods.IMPS_AUTHENTICATION_MOM);
                    }
                    break;

                case IMPS_CREATE_CUSTOMER_REGISTRATION:
                    Log.i(_LOG, "TaskComplete: Create Customer method, result: " + result);
                    if (_listener != null) {
//                        _listener.onTaskSuccess(getIMPSCreateCustomerResponse(result), Methods.IMPS_CREATE_CUSTOMER_REGISTRATION);
                        TransactionRequest<String> response = getIMPSCreateCustomerResponse(result , Methods.IMPS_CREATE_CUSTOMER_REGISTRATION);
                        _listener.onTaskSuccess(response, Methods.IMPS_CREATE_CUSTOMER_REGISTRATION);
                    }
                    break;

//                case IMPS_CUSTOMER_REGISTRATION:
//                    Log.d(_LOG, "TaskComplete: Customer Registration method, result: " + result);
//                    if (_listener != null) {
//
//                        _listener.onTaskSuccess(getIMPSCustomerRegistrationResponse(result), callback);
//
//                    }
//                    break;

                case IMPS_CUSTOMER_REGISTRATION:
                    Log.d(_LOG, "TaskComplete: Customer Registration method, result: " + result);
                    if (_listener != null) {

                        if (_listener != null) {
                            TransactionRequest<String> response = getMOMIMPSCustomerStatusResult(result , Methods.IMPS_CUSTOMER_REGISTRATION);
                            _listener.onTaskSuccess(response, Methods.IMPS_CUSTOMER_REGISTRATION);
                        }
                        break;

                    }
                    break;
                case IMPS_CHECK_KYC:
                    Log.d(_LOG, "TaskComplete: Check KYC method, result: " + result);
                    if (_listener != null) {
                        TransactionRequest<String> response = getIMPSCheckKYCResponse(result , Methods.IMPS_CHECK_KYC);
                        _listener.onTaskSuccess(response, Methods.IMPS_CHECK_KYC);

                    }
                    break;
                case IMPS_RESET_IPIN:
                    Log.d(_LOG, "TaskComplete: ResetIPIN method result: " + result);
                    if(_listener !=null){
                        TransactionRequest<String> response = getResetIPinResponse(result , Methods.IMPS_RESET_IPIN);
                        _listener.onTaskSuccess(response, Methods.IMPS_RESET_IPIN);
                    }
                    break;

                case IMPS_BENEFICIARY_LIST:
                    Log.d(_LOG, "TaskComplete: BeneficiaryList method, result: " + result);
                    if (_listener != null) {
                        TransactionRequest<String> response = getIMPSBeneficiaryListResponse(result , Methods.IMPS_BENEFICIARY_LIST);
                        _listener.onTaskSuccess(response, Methods.IMPS_BENEFICIARY_LIST);

                    }
                    break;
                case IMPS_REFUND_DETAILS:
                    Log.d(_LOG, "TaskComplete: BeneficiaryList method, result: " + result);
                    if (_listener != null) {
                        TransactionRequest<String> response = getIMPSRefundListResponse(result , Methods.IMPS_REFUND_DETAILS);
                        _listener.onTaskSuccess(response, Methods.IMPS_REFUND_DETAILS);

                    }
                    break;


                case IMPS_ADD_BENEFICIARY:
                    Log.d(_LOG, "TaskComplete: AddBeneficiary method, result: " + result);
                    if(_listener != null){
                        TransactionRequest<String> response = getIMPSAddBeneficiaryResponse(result , Methods.IMPS_ADD_BENEFICIARY);
                        _listener.onTaskSuccess(response, Methods.IMPS_ADD_BENEFICIARY);
                    }
                    break;

                case IMPS_BENEFICIARY_DETAILS:
                    Log.d(_LOG, "TaskComplete: AddBeneficiary method, result: " + result);
                    if(_listener != null){
                        TransactionRequest<String> response = getIMPSBeneficiaryDetailsResponse(result, Methods.IMPS_BENEFICIARY_DETAILS);
                        _listener.onTaskSuccess(response, Methods.IMPS_BENEFICIARY_DETAILS);

                    }
                    break;
                case IMPS_BANK_NAME_LIST:
                    Log.d(_LOG, "TaskComplete: BankList method, result: " + result);
                    if (_listener != null) {
                        TransactionRequest<String> response = getIMPSBankListResponse(result, Methods.IMPS_BANK_NAME_LIST);
                        _listener.onTaskSuccess( response, Methods.IMPS_BANK_NAME_LIST);
                    }
                    break;
                case IMPS_STATE_NAME:
                    Log.d(_LOG, "TaskComplete: StateList method, result: " + result);
                    if (_listener != null) {
                        TransactionRequest<String> response = getIMPSStateListResponse(result, Methods.IMPS_STATE_NAME);
                        _listener.onTaskSuccess(response , Methods.IMPS_STATE_NAME);
                    }
                    break;

                case IMPS_CITY_NAME:
                    Log.d(_LOG, "TaskComplete: CityList method, result: " + result);
                    if (_listener != null) {
                        TransactionRequest<String> response = getIMPSCityListResponse(result, Methods.IMPS_CITY_NAME);
                        _listener.onTaskSuccess( response , Methods.IMPS_CITY_NAME);
                    }
                    break;
                case IMPS_BRANCH_NAME:
                    Log.d(_LOG, "TaskComplete: BranchList method, result: " + result);
                    if (_listener != null) {
                        TransactionRequest<String> response = getIMPSBranchListResponse(result, Methods.IMPS_BRANCH_NAME);
                        _listener.onTaskSuccess( response , Methods.IMPS_BRANCH_NAME);
                    }
                    break;

                case IMPS_MOM_SEVICE_CHARGE_PROCESS:
                    Log.d(_LOG, "TaskComplete: MOM Service Charge method, result: " + result);

                    if (_listener != null) {
                        TransactionRequest<String> response = getPaymentResult(result , Methods.IMPS_MOM_SEVICE_CHARGE_PROCESS);
                        _listener.onTaskSuccess(response, Methods.IMPS_MOM_SEVICE_CHARGE_PROCESS);
                    }
                    break;

                case IMPS_MOM_SUBMIT_PROCESS:
                    Log.d(_LOG, "TaskComplete:SubmitPayment method, result: " + result);
                    if (_listener != null) {
                        TransactionRequest<String> response = getPaymentIMPSResult(result , Methods.IMPS_MOM_SUBMIT_PROCESS);

                        _listener.onTaskSuccess(response, Methods.IMPS_MOM_SUBMIT_PROCESS);

                    }
                    break;
                case IMPS_MOM_CONFIRM_PROCESS:
                    Log.d(_LOG, "TaskComplete:SubmitPayment method, result: " + result);
                    if (_listener != null) {
                        TransactionRequest<String> response = getConfirmIMPSResult(result , Methods.IMPS_MOM_CONFIRM_PROCESS);

                        _listener.onTaskSuccess(response, Methods.IMPS_MOM_CONFIRM_PROCESS);

                    }
                    break;
                case IMPS_MOM_CONFIRM_PROCESS_ByREFUND:
                    Log.d(_LOG, "TaskComplete:SubmitPayment method, result: " + result);
                    if (_listener != null) {
                        TransactionRequest<String> response = getConfirmIMPSRefundResult(result, Methods.IMPS_MOM_CONFIRM_PROCESS_ByREFUND);

                        _listener.onTaskSuccess(response, Methods.IMPS_MOM_CONFIRM_PROCESS_ByREFUND);

                    }
                    break;


                case BALANCE_TRANSFER:
                    Log.d(_LOG, "TaskComplete: balanceTransfer method, result: " + result);

                    if (_listener != null) {
                        TransactionRequest<String> response = getPaymentResult(result , Methods.BALANCE_TRANSFER);
                        _listener.onTaskSuccess(response, Methods.BALANCE_TRANSFER);
                    }
                    break;


                case GET_BILL_AMOUNT:
                    Log.d(_LOG, "TaskComplete: getBillAmount method, result: " + result);
                    float billAmount = extractBillAmount(result.getRemoteResponse() , result.getOperator().code);
                    Log.i(_LOG, "TaskComplete: getBillAmount method1, result1: " + billAmount);

                    if (_listener != null) {
                        _listener.onTaskSuccess(billAmount, Methods.GET_BILL_AMOUNT);
                    }
                    break;
                case TRANSACTION_HISTORY:
                    Log.d(_LOG, "TaskComplete: getTransactionHistory method, result: " + result);
                    if (_listener != null) {
                        _listener.onTaskSuccess(extractTransactions(result.getRemoteResponse()), Methods.TRANSACTION_HISTORY);
                    }
                    break;
                case CHANGE_PIN:
                    Log.d(_LOG, "TaskComplete: changeMPin method, result: " + result);

                    if (_listener != null) {

                        Log.d(_LOG, "TaskComplete: getTransactionHistory method, result: " + result);
                        if (_listener != null) {
                            _listener.onTaskSuccess(extractChangePinResponse(result.getRemoteResponse()), Methods.CHANGE_PIN);
                        }
                    }
                case GIFT_VOUCHER:
                    Log.d(_LOG, "TaskComplete: GiftVoucher method, result: " + result);

                    if (_listener != null) {

                        Log.d(_LOG, "TaskComplete: getGiftVoucher method, result: " + result);
                        if (_listener != null) {
                            _listener.onTaskSuccess(extractGiftVoucherResponse(result.getRemoteResponse()), Methods.GIFT_VOUCHER);
                        }
                    }
                    break;

             }
        }catch (Exception e){
            e.printStackTrace();
            if(_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.GENERAL_FAILURE), callback);
            }
        }
    }

    @Override
    public void onTaskError(AsyncResult pResult, Methods callback) {
        if(_listener != null) {

            _listener.onTaskError(pResult, callback);
        }
    }

    public void checkPlatform(NameValuePair...params){
        if(params == null || params.length < 1){
            if(_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.LOGIN);
            }
        }

        String url                  = AppConstants.URL_NEW_PL_DETAILS;
        AsyncDataEx dataEx          = new AsyncDataEx(
                this,
                new TransactionRequest(),
                url,
                Methods.CHECK_PLATFORM_DETAILS,
                AsyncDataEx.HttpMethod.GET
        );
        dataEx.execute(params);
    }
    public void checkLic(NameValuePair...params){
        if(params == null || params.length < 1){
            if(_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.CHECK_LIC);
            }
        }

        String url                  = AppConstants.URL_PBX_PLATFORM_APP;
        AsyncDataEx dataEx          = new AsyncDataEx(
                this,
                new TransactionRequest(),
                url,
                Methods.CHECK_LIC,
                AsyncDataEx.HttpMethod.GET
        );
        dataEx.execute (params);
    }
    public void login(String userName, String password) {
        String sCompanyId = null;
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
            if (_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.LOGIN);
            }
        }



        String loginUrl = AppConstants.URL_NEW_PLATFORM + AppConstants.SVC_NEW_METHOD_LOGIN;

        AsyncDataEx dataEx = new AsyncDataEx(this, new TransactionRequest(), loginUrl, Methods.LOGIN);

        dataEx.execute(

                new BasicNameValuePair("user", userName),
                new BasicNameValuePair("pass", password),
                new BasicNameValuePair(AppConstants.PARAM_NEW_USER, userName),
                new BasicNameValuePair(AppConstants.PARAM_NEW_PWD, password),
                new BasicNameValuePair(AppConstants.PARAM_NEW_COMPANY_ID, AppConstants.NEW_PL_COMPANY_ID),
               //new BasicNameValuePair(AppConstants.PARAM_NEW_COMPANY_ID, sCompanyId),
                new BasicNameValuePair(AppConstants.PARAM_NEW_STR_ACCESS_ID, "abc")
        );

    }

    public boolean loginSuccessful(TransactionRequest pResult){
        if("".equals(pResult.getRemoteResponse())){
            return false;
        }

        try {
            PullParser parser   = new PullParser(new ByteArrayInputStream(pResult.getRemoteResponse().getBytes()));
            String response     = parser.getTextResponse();

            Log.d(_LOG, "Response: " + response);
            String[] strArrayResponse = response.split("~");

            int i = Integer.parseInt(strArrayResponse[0]);

            if(i == AppConstants.NEW_PL_LOGIN_SUCCESS) {
                Log.d(_LOG, "Login successful");
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }
//    public String SignUp(TransactionRequest pResult){
//        String response = null;
//        if("".equals(pResult.getRemoteResponse())){
//
//        }
//
//        try {
////            PullParser parser   = new PullParser(new ByteArrayInputStream(pResult.getRemoteResponse().getBytes()));
////            response     = parser.getTextResponse();
//            InputStream in = new ByteArrayInputStream(pResult.getRemoteResponse().getBytes("UTF-8"));
//            new PullParserHandler(in);
//            Log.d(_LOG, "Response: " + response);
//            String responseResult = EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_SUBMIT_POSTING_STATUS, null);;
//            Log.d(_LOG, "NewResponse:" + responseResult);
//            pResult.setRemoteResponse(responseResult);
//            Log.d(_LOG, "Response: " + response);
//            Log.d(_LOG, "Responsetest: " + response);
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//        return  pResult;
//    }

    public String SignUpConsumer(TransactionRequest pResult){
        String response = null;
        String responseRegistrationStatus = null;
        String responseErrorMessage = null;
        if("".equals(pResult.getRemoteResponse())){

        }

        try {
            PullParser parser   = new PullParser(new ByteArrayInputStream(pResult.getRemoteResponse().getBytes()), true);
            responseRegistrationStatus = parser.getTextResponse();


            Log.d(_LOG, "ResponseCOnsumer: " + responseRegistrationStatus + responseErrorMessage);


        }catch (Exception e){
            e.printStackTrace();
        }

        return  responseRegistrationStatus;
    }
    public void getBalance(){
        String url				= AppConstants.URL_NEW_PLATFORM + AppConstants.SVC_NEW_METHOD_GET_BALANCE;
        Log.e("url", url);
        Log.e("BalCust", String.valueOf(Integer.parseInt(EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_CUSTOMER_ID, null))));

        Log.d(_LOG, "Creating dataEx for getBalance call");

        AsyncDataEx dataEx		    = new AsyncDataEx(this, new TransactionRequest(), url, Methods.GET_BALANCE);

        Log.d(_LOG, "Calling web service via async");

        dataEx.execute(
                new BasicNameValuePair(
                        AppConstants.PARAM_NEW_OPERATOR_ID,
                        EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_CUSTOMER_ID, null)
                ),
                new BasicNameValuePair(
                        AppConstants.PARAM_NEW_COMPANY_ID,
                        EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_COMPANY_ID, null)
                ),
                new BasicNameValuePair(
                        AppConstants.PARAM_NEW_STR_ACCESS_ID_SMALL_D,
                        "haduiy23d"
                )
        );

        Log.d(_LOG, "Called web service via async");
    }

    public float extractBalance(TransactionRequest pResult) throws MOMException{
        if(TextUtils.isEmpty(pResult.getRemoteResponse())){
            throw new MOMException();
        }

        try {
            PullParser parser   = new PullParser(new ByteArrayInputStream(pResult.getRemoteResponse().getBytes()));
            String response     = parser.getTextResponse();

            Log.d(_LOG, "Response: " + response);
            return Float.parseFloat(response);
        }catch (Exception e){
            e.printStackTrace();
        }

        return 0;
    }
    @Override
    public void cancelAsynctASK(){

        dataEx = new AsyncDataEx(this);
        dataEx.cancel(true);

    }

    public void verifyTPin(String psTPin){
        if(psTPin == null || "".equals(psTPin)){
            if(_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.VERIFY_TPIN);
            }
        }

        String url				= AppConstants.URL_NEW_PLATFORM + AppConstants.SVC_NEW_METHOD_CHECK_TPIN;

         dataEx		    = new AsyncDataEx(this, new TransactionRequest(), url, Methods.VERIFY_TPIN);

        dataEx.execute(
                new BasicNameValuePair(AppConstants.PARAM_NEW_TPIN, psTPin),
                new BasicNameValuePair(
                        AppConstants.PARAM_NEW_CUSTOMER_ID,
                        EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_CUSTOMER_ID, null)
                ),
                new BasicNameValuePair(
                        AppConstants.PARAM_NEW_COMPANY_ID,
                        EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_COMPANY_ID, null)
                ),
                new BasicNameValuePair(AppConstants.PARAM_NEW_STR_ACCESS_ID_SMALL_D, "KJHASFD")
                );
    }
    // Forgot Password Parameters
    public void passwordCountDetails(String psCountDetails){
        if(psCountDetails == null || "".equals(psCountDetails)){
            if(_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.PASSWORD_COUNT_DETAILS);
            }
        }

        String url				= AppConstants.URL_NEW_PLATFORM_FORGOTPWD + AppConstants.SVC_NEW_METHOD_GET_PASSWORD_COUNT;

        dataEx		    = new AsyncDataEx(this, new TransactionRequest(), url, Methods.PASSWORD_COUNT_DETAILS);

        dataEx.execute(
                new BasicNameValuePair(AppConstants.PARAM_NEW_MOBILE_NUMBER, psCountDetails)

        );
    }
    public void getForgotPassword(String sRMN , String sOperator ,String sAmount){
        if(sRMN == null || "".equals(sRMN)){
            if(_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.PASSWORD_COUNT_DETAILS);
            }
        }

        String url				= AppConstants.URL_NEW_PLATFORM_FORGOTPWD + AppConstants.SVC_NEW_METHOD_GET_FORGOTPASSWORD;

        dataEx		    = new AsyncDataEx(this, new TransactionRequest(), url, Methods.PASSWORD_COUNT_DETAILS);

        dataEx.execute(
                new BasicNameValuePair(AppConstants.PARAM_NEW_RMN, sRMN),
                new BasicNameValuePair(AppConstants.PARAM_NEW_OPERATOR_ID, sOperator),
                new BasicNameValuePair(AppConstants.PARAM_NEW_FORGOT_PWD_AMOUNT, sAmount),
                new BasicNameValuePair(AppConstants.PARAM_NEW_COMPANY_ID, EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_COMPANY_ID, null))

        );
    }
    public void getAllOperators(){



        String url				= AppConstants.URL_NEW_PLATFORM_FORGOTPWD + AppConstants.SVC_NEW_METHOD_GET_PASSWORD_OPERATORS;

        dataEx		            = new AsyncDataEx(this, new TransactionRequest(), url, Methods.GET_ALL_OPERATORS_FORGOTPWD);
        dataEx.execute(
                new BasicNameValuePair(AppConstants.PARAM_NEW_MOBILE_NUMBER, "")

        );

    }

    public void payURequest(String sConsumerNumber, String sEmailId, String sRechargeAmount){

       if(("".equals(sConsumerNumber)) ||("".equals(sEmailId)) || ("".equals(sRechargeAmount))){
           if(_listener != null) {
               _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.GET_PAY_U);
           }
       }
        String url				= AppConstants.URL_NEW_PAY_U_REQUEST + AppConstants.SVC_NEW_METHOD_PAY_U; ;

        dataEx		    = new AsyncDataEx(this, new TransactionRequest(), url, Methods.GET_PAY_U );
        dataEx.execute(
                new BasicNameValuePair(AppConstants.PARAM_NEW_STR_NAME, "Akanskha"),
                new BasicNameValuePair(AppConstants.PARAM_NEW_STR_EMAIL, sEmailId),
                new BasicNameValuePair(AppConstants.PARAM_NEW_STR_AMOUNT, sRechargeAmount),
                new BasicNameValuePair(AppConstants.PARAM_NEW_STR_MOBILE_NUMBER_PAY_U, sConsumerNumber)
        );


    }
    public void getComplaintType(){


        String url				= AppConstants.URL_NEW_BOOK_COMPLAINT_OPERATOR ;

        dataEx		    = new AsyncDataEx(this, new TransactionRequest(), url, Methods.GET_COMPLAINT_TYPE );
        dataEx.execute(
                new BasicNameValuePair(AppConstants.PARAM_NEW_MOBILE_NUMBER, "")
        );


    }

    public void bookComplaint( String sOperator , int sTransactionId , String sComment){

//        if(_listener != null) {
//            _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.GET_BOOK_COMPLAINT);
//
//        }

        String sUserId          = EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_USER_ID, null);
        String url				= AppConstants.URL_NEW_BOOK_COMPLAINT ;

        dataEx		    = new AsyncDataEx(this, new TransactionRequest(), url, Methods.GET_BOOK_COMPLAINT , AsyncDataEx.HttpMethod.GET );
        dataEx.execute(
                new BasicNameValuePair(AppConstants.PARAM_NEW_STR_COMPLAINT_TYPE,sOperator),
                new BasicNameValuePair(AppConstants.PARAM_NEW_STR_TRANSACTION_ID, String.valueOf(sTransactionId)),
                new BasicNameValuePair(AppConstants.PARAM_NEW_STR_COMMENTS, sComment),
                new BasicNameValuePair(AppConstants.PARAM_NEW_STR_COMPLAINT_BOOKED_BY, sUserId)

        );


    }


    static public String urlEncode(String sUrl) {
        int i = 0;
        String urlOK = "";
        while (i < sUrl.length()) {
            if (sUrl.charAt(i) == '<') {
                urlOK = urlOK + "%3C";
            } else if (sUrl.charAt(i) == '/') {
                urlOK = urlOK + "%2F";
            } else if (sUrl.charAt(i) == '>') {
                urlOK = urlOK + "%3E";
            } else if (sUrl.charAt(i) == ' ') {
                urlOK = urlOK + "%20";
            } else if (sUrl.charAt(i) == ':') {
                urlOK = urlOK + "%3A";
            } else if (sUrl.charAt(i) == '-') {
                urlOK = urlOK + "%2D";
            } else {
                urlOK = urlOK + sUrl.charAt(i);
            }
            i++;
        }
        return (urlOK);
    }


//Not USED//
//    public Integer tpinVerified(TransactionRequest psResult){
//        if("".equals(psResult)){
//            return 0;
//        }
//
//        try {
//            PullParser parser   = new PullParser(new ByteArrayInputStream(psResult.getRemoteResponse().getBytes()));
//            String response     = parser.getTextResponse();
//
//            Log.d(_LOG, "Response: " + response);
//            String[] strArrayResponse = response.split("~");
//
//            int i = Integer.parseInt(strArrayResponse[0]);
//
//
//            if(i == AppConstants.NEW_PL_TPIN_VERIFIED){
//                Log.d(_LOG, "TPin verified");
//                return 101;
//            }
//            else{
//                return Integer.valueOf(strArrayResponse[1]);
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//        return 0;
//    }


    public void signUpEncryptData(String composeData , String key)
    {
        Log.i("testparams" , composeData +"\n" + key);
        String url          = AppConstants.URL_NEW_PLATFORM_TXN_SIGNUP + AppConstants.SVC_NEW_METHOD_SIGN_UP_ENCRYPT_DATA;
        AsyncDataEx dataEx	= new AsyncDataEx(this, new TransactionRequest(),url,Methods.SIGN_UP_ENCRYPT_DATA);


        dataEx.execute(
                new BasicNameValuePair("PlainText", composeData),
                new BasicNameValuePair("Key", "f0rZHW8IXM8+YNYL7VptiOMr45m0VZ1yHhXD5zADpB4=")

        );
    }
    public void signUpCustomerRegistration(String data)
    {
        Log.i("testparams123" , data);
        String url          = AppConstants.URL_NEW_PLATFORM_TXN_SIGNUP + AppConstants.SVC_NEW_METHOD_SIGN_UP_CUSTOMER_REGISTRATION;
        AsyncDataEx dataEx	= new AsyncDataEx(this, new TransactionRequest(), url, Methods.SIGN_UP_CONSUMER);


        dataEx.execute(
                new BasicNameValuePair("Data", data)

        );
    }

    public void signStringomerRegistration(String data)
    {
        Log.i("testparams" , data);
        String url          = AppConstants.URL_NEW_PLATFORM_TXN_SIGNUP + AppConstants.SVC_NEW_METHOD_SIGN_UP_CUSTOMER_REGISTRATION;
        AsyncDataEx dataEx	= new AsyncDataEx(this, new TransactionRequest(), url, Methods.SIGN_UP_CONSUMER);


        dataEx.execute(
                new BasicNameValuePair("Data", data)

        );
    }
    public void rechargeMobile(TransactionRequest<PaymentResponse> request, int pnRechargeType){

        if(
            request == null || TextUtils.isEmpty(request.getConsumerId()) ||
            request.getAmount() < 1 || request.getOperator() == null
            ){

            if(_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.RECHARGE_MOBILE);
            }
            return;
        }

        String url				    = AppConstants.URL_NEW_PLATFORM_TXN + AppConstants.SVC_NEW_METHOD_RECHARGE_MOBILE;

        AsyncDataEx dataEx		    = new AsyncDataEx(this, request, url, Methods.RECHARGE_MOBILE);

        dataEx.execute(
                new BasicNameValuePair(
                        AppConstants.PARAM_NEW_INT_CUSTOMER_ID,
                        EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_CUSTOMER_ID, null)
                ),
                new BasicNameValuePair(
                        AppConstants.PARAM_NEW_COMPANY_ID,
                        EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_COMPANY_ID, null)
                ),

                new BasicNameValuePair(AppConstants.PARAM_NEW_STR_ACCESS_ID, "Test"),
                new BasicNameValuePair(AppConstants.PARAM_NEW_STR_MOBILE_NUMBER, request.getConsumerId()),
                new BasicNameValuePair(AppConstants.PARAM_NEW_RECHARGE_AMOUNT, String.valueOf(Math.round(request.getAmount()))),
                new BasicNameValuePair(AppConstants.PARAM_NEW_OPERATOR, request.getOperator().code),
                new BasicNameValuePair(AppConstants.PARAM_NEW_INT_RECHARGE_TYPE, Integer.valueOf(pnRechargeType).toString())
        );
    }


    public void giftVoucher(Operator operator , String sDescription ,String sOccasion ,String sSentTo,
                            String sSentFrom ,String sEmailId,String sConsumerNumber ,String sRechargeAmount ,
                            int nRechargeType , int nDeliveryMethod) {

        int amount = Integer.parseInt(sRechargeAmount);
        if(
                sDescription == null || TextUtils.isEmpty(sDescription) ||TextUtils.isEmpty(sOccasion) ||
                        TextUtils.isEmpty(sSentTo) ||
                        TextUtils.isEmpty(sSentFrom) ||
                        TextUtils.isEmpty(sEmailId) ||
                        TextUtils.isEmpty(sConsumerNumber) ||
                        amount < 1 || operator == null
                ){

            if(_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.GIFT_VOUCHER);
            }
            return;
        }

        String url				    = AppConstants.URL_NEW_PLATFORM_TXN + AppConstants.SVC_NEW_METHOD_FLIPKART;

        AsyncDataEx dataEx		    = new AsyncDataEx(this,  new TransactionRequest(), url, Methods.GIFT_VOUCHER);

        dataEx.execute(
                new BasicNameValuePair(
                        AppConstants.PARAM_NEW_INT_CUSTOMER_ID,
                        EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_CUSTOMER_ID, null)
                ),
                new BasicNameValuePair(
                        AppConstants.PARAM_NEW_COMPANY_ID,
                        EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_COMPANY_ID, null)
                ),

                new BasicNameValuePair(AppConstants.PARAM_NEW_STR_ACCESS_ID, "Test"),
                new BasicNameValuePair(AppConstants.PARAM_NEW_STR_MOBILE_NUMBER, sConsumerNumber),
                new BasicNameValuePair(AppConstants.PARAM_NEW_RECHARGE_AMOUNT, sRechargeAmount),
                new BasicNameValuePair(AppConstants.PARAM_NEW_OPERATOR, operator.getCode()),
                new BasicNameValuePair(AppConstants.PARAM_NEW_INT_RECHARGE_TYPE, Integer.valueOf(nRechargeType).toString()),
                new BasicNameValuePair(AppConstants.PARAM_NEW_STR_OCCASION, sOccasion),
                new BasicNameValuePair(AppConstants.PARAM_NEW_STR_DESCRIPTION, sDescription),
                new BasicNameValuePair(AppConstants.PARAM_NEW_STR_SENT_TO, sSentTo),
                new BasicNameValuePair(AppConstants.PARAM_NEW_STR_SENT_FROM, sSentFrom),
                new BasicNameValuePair(AppConstants.PARAM_NEW_STR_DELIVERY_METHOD, String.valueOf(nDeliveryMethod)),
                new BasicNameValuePair(AppConstants.PARAM_NEW_STR_EMAIL_ID, sEmailId)

        );
    }


    public TransactionRequest<String> getPaymentResult(
            TransactionRequest<String> pResult , Methods callback
    ) throws MOMException{

        if(pResult == null || "".equals(pResult.getRemoteResponse().trim())){
            throw new MOMException();
        }

        try {
            PullParser parser   = new PullParser(new ByteArrayInputStream(pResult.getRemoteResponse().getBytes()));
            String response     = parser.getTextResponse();
            Log.d(_LOG, "Response: " + response);
            String responseResult = response.toLowerCase();
            Log.d(_LOG, "NewResponse:" + responseResult);
            pResult.setRemoteResponse(responseResult);

            if(response.contains("~"))
            {
                String[] strArrayResponse = response.split("~");
                int i = Integer.parseInt(strArrayResponse[0].toString());

                switch (i) {
                    case 0:

                        String test = strArrayResponse[1].toString();
                        Log.i("Response", strArrayResponse[1].toString());
                        pResult.setRemoteResponse(test);

                        break;
                    default:


                        Log.i("Response", strArrayResponse[1].toString());
                        pResult.setRemoteResponse(strArrayResponse[1].toString());
                        break;
                }
            }
         else {
                if(responseResult.contains("success")){
                    pResult.setStatus(TransactionRequest.RequestStatus.SUCCESSFUL);

                }
                else if(responseResult.contains("failed")){
                    pResult.setStatus(TransactionRequest.RequestStatus.FAILED);
                }
                else if(responseResult.contains("pending")){
                    pResult.setStatus(TransactionRequest.RequestStatus.PENDING);
                }
                else if(responseResult.contains("could not process transaction")){
                    pResult.setStatus(TransactionRequest.RequestStatus.FAILED);
                }
                else if(responseResult.contains("no  productconfiguration  users")){
                    pResult.setStatus(TransactionRequest.RequestStatus.FAILED);
                }
                else{
                    pResult.setStatus(TransactionRequest.RequestStatus.PENDING);
                }

            }



            return pResult;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
    public TransactionRequest<String> getAllOperatorResult(
            TransactionRequest<String> pResult , Methods callback
    ) throws MOMException{

        if(pResult == null || "".equals(pResult.getRemoteResponse().trim())){
            throw new MOMException();
        }

        try {
            PullParser parser   = new PullParser(new ByteArrayInputStream(pResult.getRemoteResponse().getBytes()));
            String response     = parser.getTextResponse();
            Log.d(_LOG, "Response: " + response);
            pResult.setRemoteResponse(response);
            return pResult;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }


    public TransactionRequest<String> getVerifyTpin(
            TransactionRequest<String> pResult , Methods callback
    ) throws MOMException{

        if(pResult == null || "".equals(pResult.getRemoteResponse().trim())){
            throw new MOMException();
        }

        try {
            PullParser parser   = new PullParser(new ByteArrayInputStream(pResult.getRemoteResponse().getBytes()));
            String response     = parser.getTextResponse();



                String[] strArrayResponse = response.split("~");
                int i = Integer.parseInt(strArrayResponse[0].toString());

                switch (i) {
                    case 101:

                        String test = strArrayResponse[1].toString();
                        Log.i("Response", strArrayResponse[1].toString());
                        pResult.setRemoteResponse(strArrayResponse[0].toString());

                        break;
                    default:


                        Log.i("Response", strArrayResponse[1].toString());
                        pResult.setRemoteResponse(strArrayResponse[1].toString());
                        break;
                }





            return pResult;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }


//    public TransactionRequest<String> getComplaintTypeResponse(
//            TransactionRequest<String> pResult , Methods callback
//    ) throws MOMException{
//
//        if(pResult == null || "".equals(pResult.getRemoteResponse().trim())){
//            throw new MOMException();
//        }
//
//
//
//            return pResult;
//
//
//
//    }
       public TransactionRequest<String> getLICInfo(
        TransactionRequest<String> pResult , Methods callback
    ) throws MOMException{



    try {
       Log.e("response" , pResult.getRemoteResponse());





        return pResult;
    }catch (Exception e){
        e.printStackTrace();
    }

    return null;
}

    public TransactionRequest<String> getRMNCountDetails(
            TransactionRequest<String> pResult , Methods callback
    ) throws MOMException{

        if(pResult == null || "".equals(pResult.getRemoteResponse().trim())){
            throw new MOMException();
        }

        try {
            InputStream in = new ByteArrayInputStream(pResult.getRemoteResponse().getBytes("UTF-8"));
            new PullParserHandler(in);



            String responseResultTest ="Code:"+ " "+ EphemeralStorage.getInstance(_applicationContext).getInt(AppConstants.PARAM_NEW_FORGOT_PASSWORD_CODE, -1) + " "
                    +"\n" + " " + "Message: " + " "+ EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_FORGOT_PASSWORD_MESSAGE, null);
            Log.d(_LOG, "NewResponse:" + responseResultTest);
            String responseResult = EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_FORGOT_PASSWORD_MESSAGE ,null);
            pResult.setRemoteResponse(responseResult);






            return pResult;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public TransactionRequest<String> getForgotPasswordResult(
            TransactionRequest<String> pResult , Methods callback
    ) throws MOMException{

        if(pResult == null || "".equals(pResult.getRemoteResponse().trim())){
            throw new MOMException();
        }

        try {
            InputStream in = new ByteArrayInputStream(pResult.getRemoteResponse().getBytes("UTF-8"));
            new PullParserHandler(in);



            String responseResultTest ="Code:"+ " "+ EphemeralStorage.getInstance(_applicationContext).getInt(AppConstants.PARAM_NEW_FORGOT_PASSWORD_CODE, -1) + " "
                    +"\n" + " " + "Message: " + " "+ EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_FORGOT_PASSWORD_MESSAGE, null);
            Log.d(_LOG, "NewResponse:" + responseResultTest);
            String responseResult = EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_FORGOT_PASSWORD_MESSAGE ,null);
            pResult.setRemoteResponse(responseResult);






            return pResult;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
    public TransactionRequest<String> getPaymentIMPSResult(
            TransactionRequest<String> pResult , Methods callback
    ) throws MOMException{
        String response = null;
        String responseRegistrationStatus = null;
        String responseErrorMessage = null;
        if(pResult == null || "".equals(pResult.getRemoteResponse().trim())){
            throw new MOMException();
        }

        try {
            InputStream in = new ByteArrayInputStream(pResult.getRemoteResponse().getBytes("UTF-8"));
            new PullParserHandler(in);


            Log.d(_LOG, "Response: " + response);
            String responseResult = EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_SUBMIT_POSTING_STATUS, null);;
            Log.d(_LOG, "NewResponse:" + responseResult);
            pResult.setRemoteResponse(responseResult);


            return pResult;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }



    public TransactionRequest<String> getMOMIMPSCustomerStatusResult(
            TransactionRequest<String> pResult , Methods callback
    ) throws MOMException{
        String response = null;
        String responseRegistrationStatus = null;
        String responseErrorMessage = null;
        if(pResult == null || "".equals(pResult.getRemoteResponse().trim())){
            throw new MOMException();
        }

        try {
            InputStream in = new ByteArrayInputStream(pResult.getRemoteResponse().getBytes("UTF-8"));
            new PullParserHandler(in);


            Log.d(_LOG, "ResponseMOMIMPSCustStatus: " + pResult);
            String responseResult = EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_MOM_CUSTOMER_STATUS_IS_REGISTERED, null);;
            Log.d(_LOG, "NewResponseResponseMOMIMPSCustStatus:" + responseResult);
            pResult.setRemoteResponse(responseResult);


            return pResult;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public TransactionRequest<String> getIMPSCreateCustomerResponse(
            TransactionRequest<String> pResult , Methods callback
    ) throws MOMException{
        String response = null;
        String responseRegistrationStatus = null;
        String responseErrorMessage = null;
        if(pResult == null || "".equals(pResult.getRemoteResponse().trim())){
            throw new MOMException();
        }

        try {
            InputStream in = new ByteArrayInputStream(pResult.getRemoteResponse().getBytes("UTF-8"));
            new PullParserCustomerRegistrationHandler(in);


            Log.d(_LOG, "ResponseMOMIMPSCustRegistrationStatus: " + pResult);
            String responseResult = EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_MOM_REGISTER_CUSTOMER_ERROR_MSG, null);
            Log.d(_LOG, "NewResponseResponseMOMIMPSCustRegistrationStatus:" + responseResult);
            pResult.setRemoteResponse(responseResult);


            return pResult;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
    public TransactionRequest<String> getSignUpResult(
            TransactionRequest<String> pResult , Methods callback
    ) throws MOMException{
        String response = null;
        String responseRegistrationStatus = null;
        String responseErrorMessage = null;
        if(pResult == null || "".equals(pResult.getRemoteResponse().trim())){
            throw new MOMException();
        }

        try {
            InputStream in = new ByteArrayInputStream(pResult.getRemoteResponse().getBytes("UTF-8"));
            new PullParserHandler(in);


            Log.d(_LOG, "Response: " + response);
            String responseResult = EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_SIGNUP_STATUS, null);;
            Log.d(_LOG, "NewResponse:" + responseResult);
            pResult.setRemoteResponse(responseResult);


            return pResult;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }


    public TransactionRequest<String> getConfirmIMPSResult(
            TransactionRequest<String> pResult , Methods callback
    ) throws MOMException{
        String response = null;
        String responseRegistrationStatus = null;
        String responseErrorMessage = null;
        if(pResult == null || "".equals(pResult.getRemoteResponse().trim())){
            throw new MOMException();
        }

        try {
            InputStream in = new ByteArrayInputStream(pResult.getRemoteResponse().getBytes("UTF-8"));
            new PullParserConfirmPaymentHandler(in);


            Log.d(_LOG, "Response: " + response);
            String responseResult ="Message:"+ " "+ EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_MOM_IMPS_CONFIRM_PAYMENT_ERROR_MESSAGE, null) + " "
                    +"\n" + " " + "Receipt Id: " + " "+ EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_MOM_IMPS_CONFIRM_RECEIPT_ID, null);
            Log.d(_LOG, "NewResponse:" + responseResult);
            pResult.setRemoteResponse(responseResult);


            return pResult;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public TransactionRequest<String> getConfirmIMPSRefundResult(
            TransactionRequest<String> pResult , Methods callback
    ) throws MOMException{
        String response = null;

        if(pResult == null || "".equals(pResult.getRemoteResponse().trim())){
            throw new MOMException();
        }

        try {
            InputStream in = new ByteArrayInputStream(pResult.getRemoteResponse().getBytes("UTF-8"));
            new PullParserConfirmPaymentRefundResponseHandler(in);



            String responseResult ="Message:"+ " "+ EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_MOM_IMPS_CONFIRM_PAYMENT_ERROR_MESSAGE_BY_REFUND, null) + " "
                    +"\n" + " " + "Receipt Id: " + " "+ EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_MOM_IMPS_CONFIRM_RECEIPT_ID_BY_REFUND, null);
            Log.d(_LOG, "NewResponse:" + responseResult);
            pResult.setRemoteResponse(responseResult);


            return pResult;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public void rechargeDTH(TransactionRequest<PaymentResponse> request){
        if(
                TextUtils.isEmpty(request.getConsumerId()) ||
                request.getAmount() < 1 || request.getOperator() == null
                ){

            if(_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.RECHARGE_DTH);
            }
        }


        String url				    = AppConstants.URL_NEW_PLATFORM_TXN + AppConstants.SVC_NEW_METHOD_RECHARGE_DTH;

        AsyncDataEx dataEx		    = new AsyncDataEx(this, request, url, Methods.RECHARGE_DTH);

        dataEx.execute(
                new BasicNameValuePair(
                        AppConstants.PARAM_NEW_INT_CUSTOMER_ID,
                        EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_CUSTOMER_ID, null)
                ),
                new BasicNameValuePair(
                        AppConstants.PARAM_NEW_COMPANY_ID,
                        EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_COMPANY_ID, null)
                ),
                new BasicNameValuePair(AppConstants.PARAM_NEW_STR_ACCESS_ID, "Test"),
                new BasicNameValuePair(AppConstants.PARAM_NEW_STR_MOBILE_NUMBER, request.getConsumerId()),
                new BasicNameValuePair(AppConstants.PARAM_NEW_RECHARGE_AMOUNT, String.valueOf(Math.round(request.getAmount()))),
                new BasicNameValuePair(AppConstants.PARAM_NEW_OPERATOR, request.getOperator().code),
                new BasicNameValuePair(AppConstants.PARAM_NEW_CUSTOMER_NUMBER, request.getCustomerMobile())
        );
    }

    public void payBill(
                        TransactionRequest<PaymentResponse> request,
                        String psConsumerName ,
                        HashMap<String, String> pExtraParamsMap
                    ){


        String url				    = AppConstants.URL_NEW_PLATFORM_TXN + AppConstants.SVC_NEW_METHOD_BILL_PAYMENT;

        AsyncDataEx dataEx		    = new AsyncDataEx(this, request, url, Methods.PAY_BILL);

        String sUserId              = EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_USER_ID, null);

        String operatorCode         = request.getOperator().code;
        Log.e("Code" , operatorCode);
        String strCustomerNumber    = request.getCustomerMobile();


        if (
                AppConstants.OPERATOR_ID_BEST_ELECTRICITY.equals(operatorCode) ||
                AppConstants.OPERATOR_ID_RELIANCE_ENERGY.equals(operatorCode)  ){

            strCustomerNumber   = request.getConsumerId() + "|" + request.getCustomerMobile()
                                + "|" + sUserId;

        }

        else if(AppConstants.OPERATOR_ID_MAHANAGAR_GAS.equals(operatorCode)){
            strCustomerNumber   = request.getConsumerId();
        }

       else if (
                AppConstants.OPERATOR_ID_DHBVN_HARYANA.equals(operatorCode) ||
                        AppConstants.OPERATOR_ID_BSES_RAJDHANI.equals(operatorCode) ||
                        AppConstants.OPERATOR_ID_BSES_YAMUNA.equals(operatorCode)||
                        AppConstants.OPERATOR_ID_INDRAPRASTHA_GAS.equals(operatorCode)||
                        AppConstants.OPERATOR_ID_BESCOM_BANGALURU.equals(operatorCode)||
                        AppConstants.OPERATOR_ID_CESCOM_MYSORE.equals(operatorCode)){

            strCustomerNumber   = request.getConsumerId() + "|" + request.getCustomerMobile();


        }
        else if (AppConstants.OPERATOR_ID_DELHI_JAL_BOARD.equals(operatorCode) && pExtraParamsMap!= null){
            if(
                    !pExtraParamsMap.containsKey(AppConstants.PARAM_NEW_DUE_DATE)

                    ){

                Log.d("NEW_PL_DATA", "Parameters not sent for DJB");

                if(_listener != null) {
                    _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.PAY_BILL);
                }

                return;
            }

            strCustomerNumber   = request.getConsumerId() + "|" + request.getCustomerMobile() +"|"+ pExtraParamsMap.get(AppConstants.PARAM_NEW_DUE_DATE);
        }

        else if (AppConstants.OPERATOR_ID_CESC_LIMITED.equals(operatorCode)  && pExtraParamsMap != null){
            if(
                    !pExtraParamsMap.containsKey(AppConstants.PARAM_NEW_AC_MONTH)||
                            !pExtraParamsMap.containsKey(AppConstants.PARAM_NEW_DUE_DATE)||
                            !pExtraParamsMap.containsKey(AppConstants.PARAM_NEW_STUBTYPE)
                    ){
                Log.d("NEW_PL_DATA", "Parameters not sent for CESC");
            if(_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.PAY_BILL);
            }
            return;
        }
            strCustomerNumber   = request.getConsumerId() + "|" + request.getCustomerMobile()
                    +"|"+ pExtraParamsMap.get(AppConstants.PARAM_NEW_AC_MONTH)
                    + "|" +pExtraParamsMap.get(AppConstants.PARAM_NEW_DUE_DATE)
                    + "|" + pExtraParamsMap.get(AppConstants.PARAM_NEW_STUBTYPE);

        }
        else if(AppConstants.OPERATOR_ID_BSES_RAJDHANI.equals(operatorCode) ||
                AppConstants.OPERATOR_ID_BESCOM_BANGALURU.equals(operatorCode)||
                AppConstants.OPERATOR_ID_CESCOM_MYSORE.equals(operatorCode)||
                AppConstants.OPERATOR_ID_DHBVN_HARYANA.equals(operatorCode)||
                AppConstants.OPERATOR_ID_TATA_POWER_DELHI.equals(operatorCode)||
                AppConstants.OPERATOR_ID_INDRAPRASTHA_GAS.equals(operatorCode)){

            strCustomerNumber = request.getConsumerId() + "|" + request.getCustomerMobile();
            Log.i("ParametersBIll" , strCustomerNumber.toString());
        }

        else if (AppConstants.OPERATOR_ID_UHBVN_HARYANA.equals(operatorCode) && pExtraParamsMap != null) {
            if(
                    !pExtraParamsMap.containsKey(AppConstants.PARAM_NEW_SD_CODE) ||
                    !pExtraParamsMap.containsKey(AppConstants.PARAM_NEW_SOP)||
                    !pExtraParamsMap.containsKey(AppConstants.PARAM_NEW_FSA)

                    ){

                Log.d("NEW_PL_DATA", "Parameters not sent for UHBVN");
                if(_listener != null) {
                    _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.PAY_BILL);
                }
                return;
            }
             strCustomerNumber   = request.getConsumerId() + "|" + request.getCustomerMobile()
                    +"|"+ pExtraParamsMap.get(AppConstants.PARAM_NEW_SD_CODE)
                    + "|" +pExtraParamsMap.get(AppConstants.PARAM_NEW_SOP)
                    + "|" + pExtraParamsMap.get(AppConstants.PARAM_NEW_FSA);

        }

        else if (AppConstants.OPERATOR_ID_SBE.equals(operatorCode) && pExtraParamsMap != null) {
            if(
                    !pExtraParamsMap.containsKey(AppConstants.PARAM_NEW_ACCOUNT_NUMBER) ||
                    !pExtraParamsMap.containsKey(AppConstants.PARAM_NEW_SPECIAL_OPERATOR_SBE)
                ){

                Log.d("NEW_PL_DATA", "Parameters not sent for SBE");
                if(_listener != null) {
                    _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.PAY_BILL);
                }
                return;
            }
            strCustomerNumber   = "SBE" +"|" + request.getCustomerMobile() + "|" + psConsumerName
                                + "|" + pExtraParamsMap.get(AppConstants.PARAM_NEW_ACCOUNT_NUMBER)
                                + "|" + pExtraParamsMap.get(AppConstants.PARAM_NEW_SPECIAL_OPERATOR_NBE);

            Log.d("NEW_PL_DATA_SBE", "Going to pay bill for strCustomerNumber: " + strCustomerNumber);

        } else if (AppConstants.OPERATOR_ID_NBE.equals(operatorCode) && pExtraParamsMap != null) {
            if(
                    !pExtraParamsMap.containsKey(AppConstants.PARAM_NEW_ACCOUNT_NUMBER) ||
                            !pExtraParamsMap.containsKey(AppConstants.PARAM_NEW_SPECIAL_OPERATOR_NBE)
                ){

                Log.d("NEW_PL_DATA", "Parameters not sent for NBE");

                if(_listener != null) {
                    _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.PAY_BILL);
                }

                return;
            }

            strCustomerNumber   = "NBE" + "|" + request.getCustomerMobile() + "|" + psConsumerName
                    + "|" + pExtraParamsMap.get(AppConstants.PARAM_NEW_ACCOUNT_NUMBER)
                    + "|" + pExtraParamsMap.get(AppConstants.PARAM_NEW_SPECIAL_OPERATOR_NBE);

        }

        Log.d("NEW_PL_DATA_NBE", "Going to pay bill for strCustomerNumber: " + strCustomerNumber);

        dataEx.execute(
                new BasicNameValuePair(
                        AppConstants.PARAM_NEW_INT_CUSTOMER_ID,
                        EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_CUSTOMER_ID, null)
                ),
                new BasicNameValuePair(
                        AppConstants.PARAM_NEW_COMPANY_ID_CAMEL_CASE,
                        EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_COMPANY_ID, null)
                ),
                new BasicNameValuePair(AppConstants.PARAM_NEW_STR_ACCESS_ID, "KJHASFD"),
                new BasicNameValuePair(AppConstants.PARAM_NEW_STR_CUSTOMER_NUMBER, strCustomerNumber),
                new BasicNameValuePair(AppConstants.PARAM_NEW_DEC_BILL_AMOUNT, String.valueOf(Math.round(request.getAmount()))),
                new BasicNameValuePair(AppConstants.PARAM_NEW_INT_OPERATOR_ID_BILL_PAY, operatorCode)
        );
    }









    public void balanceTransfer(TransactionRequest request, String payTo)
    {
        if(
                TextUtils.isEmpty(request.getConsumerId()) || request.getAmount() < 1 ||
                payTo == null || payTo.trim().equals("")
                ){
            if(_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.BALANCE_TRANSFER);
            }
            return;
        }


        String url          = AppConstants.URL_NEW_PLATFORM_TXN + AppConstants.SVC_NEW_METHOD_RETAILER_PAYMENT;
        String sUserId      = EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_USER_ID, null);
        AsyncDataEx dataEx	= new AsyncDataEx(this, request, url, Methods.BALANCE_TRANSFER);

        dataEx.execute(
                new BasicNameValuePair(AppConstants.PARAM_NEW_STR_PAYER, sUserId),
                new BasicNameValuePair(AppConstants.PARAM_NEW_STR_PAYEE, payTo),
                new BasicNameValuePair(AppConstants.PARAM_NEW_STR_TRANSFERAMOUNT, String.valueOf(Math.round(request.getAmount()))),
                new BasicNameValuePair(AppConstants.PARAM_NEW_STR_TRANSVALUETPE, AppConstants.PARAM_NEW_STR_TRANSVALUE),
                new BasicNameValuePair(AppConstants.PARAM_NEW_STR_ACCESS_ID, "Test"),
        new BasicNameValuePair(
                AppConstants.PARAM_NEW_COMPANY_ID,
                EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_COMPANY_ID, null)
        )
        );
    }

    public void getBillAmount(TransactionRequest request){

        if(
                request.getConsumerId() == null || "".equals(request.getConsumerId()) ||
                request.getOperator() == null
                ){

            if(_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.GET_BILL_AMOUNT);
            }
        }

        String operatorId             = request.getOperator().code;

        Log.d("BILL_AMOUNT", "going to get bill amount for operator: " + operatorId + ", subscriber: " + request.getConsumerId());

        String url;


        String sParameterName   = AppConstants.PARAM_NEW_CANUMBER;


        if(AppConstants.OPERATOR_ID_BEST_ELECTRICITY.equals(operatorId)){
            url				= AppConstants.URL_NEW_PLATFORM_GET_BEST_BILL;

            sParameterName      = AppConstants.PARAM_NEW_ACCOUNT_NUMBER;
        }else if(AppConstants.OPERATOR_ID_RELIANCE_ENERGY.equals(operatorId)) {
            url                 = AppConstants.URL_NEW_PLATFORM_GET_RELIANCE_BILL;

        }else if(AppConstants.OPERATOR_ID_MAHANAGAR_GAS.equals(operatorId)) {
            url                 = AppConstants.URL_NEW_PLATFORM_GET_MGL_BILL;

        }else{
            Log.e("BILL_AMOUNT", "Bill information cannot be checked for this operator");
            onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.GET_BILL_AMOUNT);
            return;
        }
        Log.i("Params" , sParameterName+ request.getConsumerId().toString());
        AsyncDataEx dataEx		= new AsyncDataEx(this, request, url, Methods.GET_BILL_AMOUNT ,AsyncDataEx.HttpMethod.GET);
        dataEx.execute(new BasicNameValuePair(sParameterName, request.getConsumerId()));
    }

    public float extractBillAmount(String psResult , String _operatorId) throws MOMException{
        Log.i("AMount" , psResult);
        if(psResult == null || "".equals(psResult.trim()) || _operatorId == null){
            throw new MOMException();
        }

        String[] sArrResponse       = psResult.split("~");
        String sBill                = null;

        if(_operatorId.equals(AppConstants.OPERATOR_ID_BEST_ELECTRICITY) && sArrResponse.length > 15){
            sBill                   = sArrResponse[22];
            Log.i("BESTAmount" , sBill);
        }else if(_operatorId.equals(AppConstants.OPERATOR_ID_RELIANCE_ENERGY) && sArrResponse.length > 2){
            sBill                   = sArrResponse[3];
        }else if(_operatorId.equals(AppConstants.OPERATOR_ID_MAHANAGAR_GAS) && sArrResponse.length > 3){
            sBill                   = sArrResponse[4];
        }

        if(sBill == null || "".equals(sBill.trim())){
            throw new MOMException();
        }

        Log.d("BILL", "Bill: " + sBill);

        return Float.valueOf(sBill);
    }

    @Override
    public void getTransactionHistory() {
        String url				= AppConstants.URL_NEW_PL_HISTORY;

        AsyncDataEx dataEx		= new AsyncDataEx(this, new TransactionRequest(), url, Methods.TRANSACTION_HISTORY , AsyncDataEx.HttpMethod.GET);
        String sUserId          = EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_USER_ID, null);

        dataEx.execute(new BasicNameValuePair(AppConstants.PARAM_NEW_USER_ID, sUserId));
    }

    public ArrayList<Transaction> extractTransactions(String result) throws MOMException{
        String[] sArrTxn            = result.split("\\|");
        ArrayList<Transaction> transactions  = new ArrayList<Transaction>();

        int i                       = 0;

        for(String sTransaction:sArrTxn) {
            String[] sArr = sTransaction.split("~");

            if (sArr.length < 6) {
                Log.e(_LOG, "Invalid request response");
                continue;
            }

            Transaction transaction         = new Transaction();
            transaction.transactionDate     = sArr[0];
            transaction.transactionId       = sArr[1];
            transaction.subscriberId        = sArr[2];
            try {
                transaction.amount = Float.parseFloat(sArr[3]);
            }catch(NumberFormatException nfe){
                Log.w(_LOG, "Invalid amount response");
            }
            transaction.operator            = sArr[4];
            transaction.statusString        = sArr[5];

            transactions.add(transaction);
        }

        return transactions;
    }

    @Override
    public void changePin(PinType pinType, String psOldPin, String psNewPin) {

        String method             = pinType == PinType.M_PIN
                                    ? AppConstants.SVC_NEW_METHOD_CHANGE_MPIN
                                    : AppConstants.SVC_NEW_METHOD_CHANGE_TPIN;

            String url				= AppConstants.URL_NEW_PLATFORM + method;
        Log.i("method" , url);

        AsyncDataEx dataEx		= new AsyncDataEx(this, new TransactionRequest(), url, Methods.CHANGE_PIN);
        String sUserId          = EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_USER_ID, null);

        dataEx.execute(
                new BasicNameValuePair(
                        AppConstants.PARAM_NEW_CUSTOMER_ID,
                        EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_CUSTOMER_ID, null)
                ),
                new BasicNameValuePair(
                        AppConstants.PARAM_NEW_COMPANY_ID,
                        EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_COMPANY_ID, null)
                ),
                new BasicNameValuePair(AppConstants.PARAM_NEW_STR_ACCESS_ID, "KJHASFD"),
                new BasicNameValuePair(AppConstants.PARAM_NEW_STR_PASSWORD, psOldPin),
                new BasicNameValuePair(AppConstants.PARAM_NEW_STR_NEW_PASSWORD, psNewPin)

        );
    }



    public void changePinTest(TransactionRequest request) {

        String method             = request.getPin() == PinType.M_PIN
                ? AppConstants.SVC_NEW_METHOD_CHANGE_MPIN
                : AppConstants.SVC_NEW_METHOD_CHANGE_TPIN;

        String url				= AppConstants.URL_NEW_PLATFORM + method;
        Log.i("method" , url);

        AsyncDataEx dataEx		= new AsyncDataEx(this, new TransactionRequest(), url, Methods.CHANGE_PIN);
        String sUserId          = EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_USER_ID, null);

        dataEx.execute(
                new BasicNameValuePair(
                        AppConstants.PARAM_NEW_CUSTOMER_ID,
                        EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_CUSTOMER_ID, null)
                ),
                new BasicNameValuePair(
                        AppConstants.PARAM_NEW_COMPANY_ID,
                        EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_COMPANY_ID, null)
                ),
                new BasicNameValuePair(AppConstants.PARAM_NEW_STR_ACCESS_ID, "KJHASFD"),
                new BasicNameValuePair(AppConstants.PARAM_NEW_STR_PASSWORD, request.getOldPin()),
                new BasicNameValuePair(AppConstants.PARAM_NEW_STR_NEW_PASSWORD, request.getNewPin())

        );
    }

    public TransactionRequest extractChangePinResponse(String psResult) throws MOMException{
        if(psResult == null || "".equals(psResult.trim())){
            throw new MOMException();
        }
        TransactionRequest transactionRequest = new TransactionRequest();
        try {
            PullParser parser   = new PullParser(new ByteArrayInputStream(psResult.getBytes()));
            String response     = parser.getTextResponse();

            Log.d(_LOG, "Response: " + response);
            String[] strArrayResponse = response.split("~");
            if(strArrayResponse.length < 2){
                throw new MOMException();
            }
            transactionRequest.setResponseCode(Integer.parseInt(strArrayResponse[0]));
            transactionRequest.setRemoteResponse(strArrayResponse[1]);
            return transactionRequest;

        }catch (Exception e){
            e.printStackTrace();
        }

        return transactionRequest;
    }

    public TransactionRequest extractGiftVoucherResponse(String psResult) throws MOMException{
        if(psResult == null || "".equals(psResult.trim())){
            throw new MOMException();
        }
        TransactionRequest transactionRequest = new TransactionRequest();
        try {
            PullParser parser   = new PullParser(new ByteArrayInputStream(psResult.getBytes()));
            String response     = parser.getTextResponse();

            Log.d(_LOG, "Response: " + response);

           transactionRequest.setRemoteResponse(response);
            return transactionRequest;

        }catch (Exception e){
            e.printStackTrace();
        }

        return transactionRequest;
    }

    public  void rechargeMobilePBX(
            String customerNumber,
            String psOperator,
            double pdAmount)
    {

    }
    public void getOperatorNames(){

    }

    public  void changePassword( String psOldPin, String psNewPin){

    }

    public void lic(String policyNumber, String CustomerMobNo) {

        if (
                TextUtils.isEmpty(policyNumber)) {

            if (_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.LIC);
            }
            return;
        }


        TransactionRequest<LicLifeResponse> request = new TransactionRequest<LicLifeResponse>();

        AsyncDataEx dataEx = new AsyncDataEx(
                this,
                request,
                AppConstants.URL_PBX_PLATFORM_APP,
                Methods.LIC
        );

        dataEx.execute(
                new BasicNameValuePair(AppConstants.PARAM_SERVICE_NEW, AppConstants.SVC_PBX_LIC),
                new BasicNameValuePair(AppConstants.PARAM_LICREFNO, policyNumber),
                new BasicNameValuePair(AppConstants.PARAM_MERCHANTID_LIC, EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_MERCHANTID_LIC, null)),
                new BasicNameValuePair(AppConstants.PARAM_PBX_RMN, EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.LOGGED_IN_USERNAME, null)),
                new BasicNameValuePair(AppConstants.PARAM_CUSTOMER_NUMBER_LIC, CustomerMobNo)

        );

    }

    public void licPayment(TransactionRequest<LicLifeResponse> request, String CustomerMobNo, String PolicyNo, String UnpaidDate) {

        if (
                TextUtils.isEmpty(request.getRemoteResponse())) {

            if (_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.PAY_LIC);
            }
            return;
        }

        String licrefno = EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_LICREFNO, null);


        AsyncDataEx dataEx = new AsyncDataEx(
                this,
                new TransactionRequest<LicLifeResponse>(),
                AppConstants.URL_PBX_PLATFORM_APP,
                Methods.PAY_LIC
        );

        dataEx.execute(
                new BasicNameValuePair(AppConstants.PARAM_SERVICE_NEW, AppConstants.PARAM_SERVICE_LIC),
                new BasicNameValuePair(AppConstants.PARAM_CUSTOMER_NUMBER_LIC, CustomerMobNo),
                new BasicNameValuePair(AppConstants.PARAM_PBX_USERID, EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_PBX_USERID, null)),
                new BasicNameValuePair(AppConstants.PARAM_POLICYNUMBER_LIC, PolicyNo),
                new BasicNameValuePair(AppConstants.PARAM_TRANSREFGUID_LIC, request.getCustom().getTransRefGUID()),
                new BasicNameValuePair(AppConstants.PARAM_TRANSINVGUID_LIC, request.getCustom().getTransInvGUID()),
                new BasicNameValuePair(AppConstants.PARAM_POLICYAMOUNT_LIC, Double.toString(request.getCustom().getTransInvAmount())),
                new BasicNameValuePair(AppConstants.PARAM_POLICYHOLDER_LIC, request.getCustom().getOLife().getParty().getFullName()),
                new BasicNameValuePair(AppConstants.PARAM_FRUNPAIDPREMIUMDATE, request.getCustom().getOLife().getPolicy().getFrUnpaidPremiumDate()),

                new BasicNameValuePair(AppConstants.PARAM_PBX_RMN, EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.LOGGED_IN_USERNAME, null))


        );

    }

    public void utilityPayBill(
            TransactionRequest<PaymentResponse> request,
            String psConsumerName,
            HashMap<String, String> pExtraParamsMap
    ){

    }
    public void impsBeneficiaryList(String consumerNumber){

    }

     public void impsCustomerRegistrationStatus(String consumerNumber){

    }

    public void impsCustomerRegistration(TransactionRequest request ,String sConsumerNumber) {
        if( (TextUtils.isEmpty(request.getConsumerId())) || ("".equals(sConsumerNumber))) {
            if (_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.IMPS_CREATE_CUSTOMER_REGISTRATION);
            }
        }


        String Url = AppConstants.URL_NEW_PLATFORM_IMPS + AppConstants.SVC_NEW_METHOD_IMPS_MOM_CUSTOMER_REGISTRATION;
        String sUserId              = EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_USER_ID, null);
        AsyncDataEx dataEx = new AsyncDataEx(this, new TransactionRequest(), Url, Methods.IMPS_CREATE_CUSTOMER_REGISTRATION);

        dataEx.execute(
                new BasicNameValuePair(AppConstants.PARAM_NEW_RETAILER_ID,sUserId),
                new BasicNameValuePair(AppConstants.PARAM_NEW_IMPS_PASSWORD,  EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.LOGGED_IN_PASSWORD,null)),
                new BasicNameValuePair(AppConstants.PARAM_NEW_COMPANY_ID, AppConstants.NEW_PL_COMPANY_ID),
                new BasicNameValuePair(AppConstants.PARAM_NEW_CONSUMER_NUMBER ,sConsumerNumber),
                new BasicNameValuePair(AppConstants.PARAM_NEW_MOM_IMPS_CONSUMER_NAME, request.getConsumerName()),
                new BasicNameValuePair(AppConstants.PARAM_NEW_MOM_IMPS_CONSUMER_DOB, request.getConsumerDOB()),
                new BasicNameValuePair(AppConstants.PARAM_NEW_MOM_IMPS_CONSUMER_EMAIL, request.getConsumerEmailAddress())

        );
    }

//    public TransactionRequest getIMPSCreateCustomerResponse(TransactionRequest<ImpsCreateCustomerResult> request) {
//        if (TextUtils.isEmpty(request.getRemoteResponse())) {
//            Log.e(_LOG, "Null remote response received");
//            return null;
//        }
//
//        try {
//            Gson gson = new GsonBuilder().create();
//
//            Type type = new TypeToken<ResponseBase<ImpsCreateCustomerResult>>() {
//            }.getType();
//
//            ResponseBase<ImpsCreateCustomerResult> responseBase = gson.fromJson(request.getRemoteResponse(), type);
//
//            if (responseBase == null) {
//                Log.w(_LOG, "Null response?");
//                return null;
//            }
//
//            Log.d(_LOG, "NEW_PL_DATA_CreateCustomerResponse: " + responseBase.data);
//            ImpsCreateCustomerResult response = responseBase.data;
//
//
//            request.setCustom(response);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return request;
//    }

    public void registerIMPSCustomer(TransactionRequest request) {
        if (request == null || TextUtils.isEmpty(request.getConsumerId())) {
            if (_listener != null) {

                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.IMPS_CUSTOMER_REGISTRATION);
            }
            return;
        }

        String Url = AppConstants.URL_NEW_PLATFORM_IMPS + AppConstants.SVC_NEW_METHOD_IMPS_MOM_CUSTOMER_STATUS;
        String sUserId              = EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_USER_ID, null);
        AsyncDataEx dataEx = new AsyncDataEx(this, new TransactionRequest(), Url, Methods.IMPS_CUSTOMER_REGISTRATION);

        dataEx.execute(




                new BasicNameValuePair(AppConstants.PARAM_NEW_RETAILER_ID, sUserId),
                new BasicNameValuePair(AppConstants.PARAM_NEW_IMPS_PASSWORD,  EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.LOGGED_IN_PASSWORD,null)),
                new BasicNameValuePair(AppConstants.PARAM_NEW_COMPANY_ID, AppConstants.NEW_PL_COMPANY_ID),
                new BasicNameValuePair(AppConstants.PARAM_NEW_MOBILE_NUMBER, request.getConsumerId())
        );
    }

    public void resetIPIN(TransactionRequest request , String sCustomerID) {
        if (request == null || ("".equals(sCustomerID))) {
            if (_listener != null) {

                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.IMPS_RESET_IPIN);
            }
            return;
        }

        String Url = AppConstants.URL_NEW_PLATFORM_IMPS + AppConstants.SVC_NEW_METHOD_IMPS_MOM_RESET_IPIN;

        AsyncDataEx dataEx = new AsyncDataEx(this, new TransactionRequest(), Url, Methods.IMPS_RESET_IPIN);

        dataEx.execute(

                new BasicNameValuePair(AppConstants.PARAM_NEW_CUSTOMER_ID ,sCustomerID)
        );
    }

    public TransactionRequest<String> getResetIPinResponse(
            TransactionRequest<String> pResult , Methods callback
    ) throws MOMException{

        if(pResult == null || "".equals(pResult.getRemoteResponse().trim())){
            throw new MOMException();
        }

        try {
            PullParser parser   = new PullParser(new ByteArrayInputStream(pResult.getRemoteResponse().getBytes()));
            String response     = parser.getTextResponse();
            Log.d(_LOG, "Response: " + response);
            pResult.setRemoteResponse(response);
            return pResult;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public TransactionRequest getIMPSCustomerRegistrationResponse(TransactionRequest<ImpsCustomerRegistrationResult> request) {
        if (TextUtils.isEmpty(request.getRemoteResponse())) {
            Log.e(_LOG, "Null remote response received");
            return null;
        }

        try {
            Gson gson = new GsonBuilder().create();

            Type type = new TypeToken<ResponseBase<ImpsCustomerRegistrationResult>>() {
            }.getType();

            ResponseBase<ImpsCustomerRegistrationResult> responseBase = gson.fromJson(request.getRemoteResponse(), type);

            if (responseBase == null) {
                Log.w(_LOG, "Null response?");
                return null;
            }

            Log.d(_LOG, "NEW_PL_DATA_CustomerRegResponse: " + responseBase.data);
            ImpsCustomerRegistrationResult response = responseBase.data;


            request.setCustom(response);

            EphemeralStorage.getInstance(_applicationContext).storeInt(
                    AppConstants.PARAM_PBX_IMPS_CUSTOMER_ID,
                    responseBase.data.customerID);
            Log.i("CustomerId", String.valueOf(responseBase.data.customerID));

            EphemeralStorage.getInstance(_applicationContext).storeBoolean(
                    AppConstants.PARAM_PBX_IMPS_SERVICEALLOWED,
                    responseBase.data.isIMPSServiceAllowed);
            Log.i("ServiceAllowed", String.valueOf(responseBase.data.isIMPSServiceAllowed));

            EphemeralStorage.getInstance(_applicationContext).storeBoolean(
                    AppConstants.PARAM_PBX_IMPS_ISREGISTERED,
                    responseBase.data.isRegistered);
            Log.i("IsRegisteredUser", String.valueOf(responseBase.data.isRegistered));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return request;
    }
//    public void impsCheckKYC(TransactionRequest<ImpsCheckKYCResult> request , String sConsumerNumber) {
//        if (TextUtils.isEmpty(sConsumerNumber)) {
//            if (_listener != null) {
//                Log.e("Check KYC" ,"Error");
//                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.IMPS_CHECK_KYC);
//            }
//
//        }
//
//        String Url = AppConstants.URL_PBX_PLATFORM_IMPS + AppConstants.SVC_NEW_METHOD_IMPS_CUSTOMER_REGISTRATION;
//
//        AsyncDataEx dataEx = new AsyncDataEx(this, new TransactionRequest(), Url, Methods.IMPS_CHECK_KYC);
//
//        dataEx.execute(
//
//                new BasicNameValuePair(AppConstants.PARAM_PBX_SERVICE, AppConstants.PARAM_SERVICE_IMPS_CHECK_KYC),
//                // new BasicNameValuePair(AppConstants.PARAM_PBX_IMPS_KYC_CUSTOMER_ID, String.valueOf(EphemeralStorage.getInstance(_applicationContext).getInt(AppConstants.PARAM_PBX_IMPS_CUSTOMER_ID, -1))),
//                new BasicNameValuePair(AppConstants.PARAM_PBX_IMPS_CUSTOMER_ID, String.valueOf(EphemeralStorage.getInstance(_applicationContext).getInt(AppConstants.PARAM_PBX_IMPS_CUSTOMER_ID, -1))),
//                new BasicNameValuePair(AppConstants.PARAM_PBX_IMPS_CONSUMER_NUMBER, sConsumerNumber),
//                new BasicNameValuePair(AppConstants.PARAM_PBX_ROWID, EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_PBX_USERID, null)),
//                new BasicNameValuePair(AppConstants.PARAM_PBX_RMN, EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.LOGGED_IN_USERNAME, null))
//        );
//    }

    public void impsCheckKYC(TransactionRequest request , String sConsumerNumber) {
        if (TextUtils.isEmpty(sConsumerNumber)) {
            if (_listener != null) {
                Log.e("Check KYC" ,"Error");
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.IMPS_CHECK_KYC);
            }

        }
      //  Toast.makeText(_applicationContext ,String.valueOf(EphemeralStorage.getInstance(_applicationContext).getInt(AppConstants.PARAM_NEW_MOM_IMPS_CUSTOMER_STATUS_CUSTOMER_ID, -1)),Toast.LENGTH_LONG).show();
        String Url = AppConstants.URL_NEW_PLATFORM_IMPS + AppConstants.SVC_NEW_METHOD_IMPS_MOM_CHECK_KYC;
        String sUserId              = EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_USER_ID, null);
        AsyncDataEx dataEx = new AsyncDataEx(this, new TransactionRequest(), Url, Methods.IMPS_CHECK_KYC);
     //   Toast.makeText(_applicationContext ,String.valueOf(EphemeralStorage.getInstance(_applicationContext).getInt(AppConstants.PARAM_NEW_MOM_IMPS_CUSTOMER_STATUS_CUSTOMER_ID, -1)),Toast.LENGTH_LONG).show();
        dataEx.execute(

                new BasicNameValuePair(AppConstants.PARAM_NEW_RETAILER_ID, sUserId),
                new BasicNameValuePair(AppConstants.PARAM_NEW_IMPS_PASSWORD,  EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.LOGGED_IN_PASSWORD,null)),
                new BasicNameValuePair(AppConstants.PARAM_NEW_COMPANY_ID, AppConstants.NEW_PL_COMPANY_ID),
                new BasicNameValuePair(AppConstants.PARAM_NEW_MOM_IMPS_CONSUMERID, String.valueOf(EphemeralStorage.getInstance(_applicationContext).getInt(AppConstants.PARAM_NEW_MOM_IMPS_CUSTOMER_STATUS_CUSTOMER_ID, -1)))
                        );
    }

    public TransactionRequest<String> getIMPSCheckKYCResponse(
            TransactionRequest<String> pResult , Methods callback
    ) throws MOMException{
        String response = null;
        String responseRegistrationStatus = null;
        String responseErrorMessage = null;
        if(pResult == null || "".equals(pResult.getRemoteResponse().trim())){
            throw new MOMException();
        }

        try {
            InputStream in = new ByteArrayInputStream(pResult.getRemoteResponse().getBytes("UTF-8"));
            new PullParserHandler(in);


            Log.d(_LOG, "ResponseMOMIMPSCheckKYCStatus: " + pResult);
            String responseResult =   EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_MOM_CHECK_KYC_AVAILABLE_LIMIT, null);
            Log.d(_LOG, "NewResponseMOMIMPSCheckKYCStatus:" + responseResult);
            pResult.setRemoteResponse(responseResult);


            return pResult;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }


    public void impsRefundDetails(TransactionRequest request) {
        if (request == null) {
            if (_listener != null) {
                Log.e("Check KYC" ,"Error");
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.IMPS_REFUND_DETAILS);
            }

        }

        String Url = AppConstants.URL_NEW_PLATFORM_IMPS + AppConstants.SVC_NEW_METHOD_IMPS_MOM_REFUND_DETAILS;
        String sUserId              = EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_USER_ID, null);
        AsyncDataEx dataEx = new AsyncDataEx(this, new TransactionRequest(), Url, Methods.IMPS_REFUND_DETAILS);
      //  Toast.makeText(_applicationContext ,String.valueOf(EphemeralStorage.getInstance(_applicationContext).getInt(AppConstants.PARAM_NEW_MOM_IMPS_CUSTOMER_STATUS_CUSTOMER_ID, -1)),Toast.LENGTH_LONG).show();
        dataEx.execute(

                new BasicNameValuePair(AppConstants.PARAM_NEW_RETAILER_ID, sUserId),
                new BasicNameValuePair(AppConstants.PARAM_NEW_IMPS_PASSWORD,  EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.LOGGED_IN_PASSWORD,null)),
                new BasicNameValuePair(AppConstants.PARAM_NEW_COMPANY_ID, AppConstants.NEW_PL_COMPANY_ID),
                new BasicNameValuePair(AppConstants.PARAM_NEW_CUSTOMER_ID, String.valueOf(EphemeralStorage.getInstance(_applicationContext).getInt(AppConstants.PARAM_NEW_MOM_IMPS_CUSTOMER_STATUS_CUSTOMER_ID, -1)))
        );
    }

    public TransactionRequest<String> getIMPSRefundListResponse(
            TransactionRequest<String> pResult , Methods callback
    ) throws MOMException{

        if(pResult == null || "".equals(pResult.getRemoteResponse().trim())){
            throw new MOMException();
        }

        try {
            InputStream in = new ByteArrayInputStream(pResult.getRemoteResponse().getBytes("UTF-8"));
            new PullParserRefundResponseHandler(in);


            Log.d(_LOG, "ResponseMOMIMPSIMPSRefundListResponse: " + pResult);
            List<String> responseResult =  EphemeralStorage.getInstance(_applicationContext).getRefundTransactionDescriptionList();
            Log.d(_LOG, "NewResponseMOMIMPSIMPSRefundListResponse:" + responseResult);



            return pResult;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

//    public TransactionRequest getIMPSCheckKYCResponse(TransactionRequest<ImpsCheckKYCResult> request) {
//        if (TextUtils.isEmpty(request.getRemoteResponse())) {
//            Log.e(_LOG, "Null remote response received");
//            return null;
//        }
//
//        try {
//            Gson gson = new GsonBuilder().create();
//
//            Type type = new TypeToken<ResponseBase<ImpsCheckKYCResult>>() {
//            }.getType();
//
//            ResponseBase<ImpsCheckKYCResult> responseBase = gson.fromJson(request.getRemoteResponse(), type);
//
//            if (responseBase == null) {
//                Log.w(_LOG, "Null response?");
//                return null;
//            }
//
//            Log.d(_LOG, "NEW_PL_DATA_KYCResponse: " + responseBase.data);
//            ImpsCheckKYCResult impsCheckKYCResult = responseBase.data;
//
//
//            request.setCustom(impsCheckKYCResult);
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return request;
//    }

    public void impsAddBeneficiary(TransactionRequest request) {
        if (request == null ) {
            if (_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.IMPS_ADD_BENEFICIARY);
            }
            return;
        }

        String url = AppConstants.URL_NEW_PLATFORM_IMPS + AppConstants.SVC_NEW_METHOD_IMPS_MOM_CREATE_BENEFICIARY;

        String sUserId              = EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_USER_ID, null);
      //  Toast.makeText(_applicationContext ,String.valueOf(EphemeralStorage.getInstance(_applicationContext).getInt(AppConstants.PARAM_NEW_MOM_IMPS_CUSTOMER_STATUS_CUSTOMER_ID, -1)),Toast.LENGTH_LONG).show();
        AsyncDataEx dataEx = new AsyncDataEx(this, new TransactionRequest(), url, Methods.IMPS_ADD_BENEFICIARY);
// CHANGE PARAMS
        dataEx.execute(
                new BasicNameValuePair(AppConstants.PARAM_NEW_RETAILER_ID, sUserId),
                new BasicNameValuePair(AppConstants.PARAM_NEW_IMPS_PASSWORD,  EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.LOGGED_IN_PASSWORD,null)),
                new BasicNameValuePair(AppConstants.PARAM_NEW_COMPANY_ID, AppConstants.NEW_PL_COMPANY_ID),
                new BasicNameValuePair(AppConstants.PARAM_NEW_MOM_IMPS_CONSUMERID, String.valueOf(EphemeralStorage.getInstance(_applicationContext).getInt(AppConstants.PARAM_NEW_MOM_IMPS_CUSTOMER_STATUS_CUSTOMER_ID, -1))),
                new BasicNameValuePair(AppConstants.PARAM_NEW_IMPS_ACCOUNT_NAME, request.getBeneficiaryName()),
                new BasicNameValuePair(AppConstants.PARAM_NEW_IMPS_ACCOUNT_NUMBER,request.getAccountNumber()),
                new BasicNameValuePair(AppConstants.PARAM_NEW_MOM_IMPS_BENEFICIARY_MOBILE_NUMBER, request.getBeneficiaryMobNo()),
                new BasicNameValuePair(AppConstants.PARAM_NEW_IMPS_IFSC_CODE, request.getIfscCode())

        );
    }

    public TransactionRequest<String> getIMPSAddBeneficiaryResponse(
            TransactionRequest<String> pResult , Methods callback
    ) throws MOMException{
        String response = null;
        String responseRegistrationStatus = null;
        String responseErrorMessage = null;
        if(pResult == null || "".equals(pResult.getRemoteResponse().trim())){
            throw new MOMException();
        }

        try {
            InputStream in = new ByteArrayInputStream(pResult.getRemoteResponse().getBytes("UTF-8"));
            new PullParserCreateBeneficiaryHandler(in);


            Log.d(_LOG, "NEW_PL_DATA_Add_BeneficiaryResponse: " + pResult);
            String responseResult =   EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_MOM_IMPS_ADD_BENEFICIARY_ERROR_MESSAGE, null);
            Log.d(_LOG, "NEW_PL_DATA_Add_BeneficiaryResponse:" + responseResult);
            pResult.setRemoteResponse(responseResult);


            return pResult;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

//    public TransactionRequest getIMPSAddBeneficiaryResponse(TransactionRequest<ImpsAddBeneficiaryResult> request) {
//        if (TextUtils.isEmpty(request.getRemoteResponse())) {
//            Log.e(_LOG, "Null remote response received");
//            return null;
//        }
//
//        try {
//            Gson gson = new GsonBuilder().create();
//
//            Type type = new TypeToken<ResponseBase<ImpsAddBeneficiaryResult>>() {
//            }.getType();
//
//            ResponseBase<ImpsAddBeneficiaryResult> responseBase = gson.fromJson(request.getRemoteResponse(), type);
//
//            if (responseBase == null) {
//                Log.w(_LOG, "Null response?");
//                return null;
//            }
//
//            Log.d(_LOG, "NEW_PL_DATA_Add_BeneficiaryResponse: " + responseBase.data);
//            ImpsAddBeneficiaryResult response = responseBase.data;
//            request.setCustom(response);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return request;
//    }
//    public void getIMPSBeneficiaryList(TransactionRequest<List<BeneficiaryResult>> request) {
       public void getIMPSBeneficiaryList(TransactionRequest request) {
        if (request == null || TextUtils.isEmpty(request.getConsumerId())) {
            if (_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.IMPS_BENEFICIARY_LIST);
            }
            return;
        }

        String Url = AppConstants.URL_NEW_PLATFORM_IMPS + AppConstants.SVC_NEW_METHOD_IMPS_MOM_BENEFICIARY_LIST;
        String sUserId              = EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_USER_ID, null);
         //  Toast.makeText(_applicationContext ,String.valueOf(EphemeralStorage.getInstance(_applicationContext).getInt(AppConstants.PARAM_NEW_MOM_IMPS_CUSTOMER_STATUS_CUSTOMER_ID, -1)),Toast.LENGTH_LONG).show();
        AsyncDataEx dataEx = new AsyncDataEx(this, new TransactionRequest(),Url , Methods.IMPS_BENEFICIARY_LIST);

        dataEx.execute(

                new BasicNameValuePair(AppConstants.PARAM_NEW_RETAILER_ID, sUserId),
                new BasicNameValuePair(AppConstants.PARAM_NEW_IMPS_PASSWORD, EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.LOGGED_IN_PASSWORD, null)),
                new BasicNameValuePair(AppConstants.PARAM_NEW_COMPANY_ID, AppConstants.NEW_PL_COMPANY_ID),
                new BasicNameValuePair(AppConstants.PARAM_NEW_MOM_IMPS_CONSUMERID, String.valueOf(EphemeralStorage.getInstance(_applicationContext).getInt(AppConstants.PARAM_NEW_MOM_IMPS_CUSTOMER_STATUS_CUSTOMER_ID, -1)))
        );
    }

//    public TransactionRequest getIMPSBeneficiaryListResponse(TransactionRequest<List<BeneficiaryResult>> request) {
//        Gson gson = new GsonBuilder().create();
//
//        Type type = new TypeToken<ResponseBase<BeneficiaryResult[]>>() {
//        }.getType();
//
//        ResponseBase<BeneficiaryResult[]> responseBase = gson.fromJson(request.getRemoteResponse(), type);
//
//        if (responseBase == null || responseBase.data == null) {
//            return null;
//        }
//
//        //actual  List<BeneficiaryResult> beneficiaryList = Arrays.asList(responseBase.data);
//        List<BeneficiaryResult> beneficiaryList = new ArrayList<BeneficiaryResult>(Arrays.asList(responseBase.data));
//
//        request.setCustom(beneficiaryList);
//        Log.d(_LOG, "NEW_PL_DATA_BeneficiaryListResponse: ");
//        return request;
//    }
    public TransactionRequest<String> getIMPSBeneficiaryListResponse(
            TransactionRequest<String> pResult , Methods callback
    ) throws MOMException{
        String response = null;
        String responseRegistrationStatus = null;
        String responseErrorMessage = null;
        if(pResult == null || "".equals(pResult.getRemoteResponse().trim())){
            throw new MOMException();
        }

        try {
            InputStream in = new ByteArrayInputStream(pResult.getRemoteResponse().getBytes("UTF-8"));
            new PullParserHandler(in);


            Log.d(_LOG, "ResponseMOMIMPSIMPSBeneficiaryListResponse: " + pResult);
            List<String> responseResult =  EphemeralStorage.getInstance(_applicationContext).getBeneficiaryListDetail();
            Log.d(_LOG, "NewResponseMOMIMPSIMPSBeneficiaryListResponse:" + responseResult);



            return pResult;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

//    public void impsBeneficiaryDetails(TransactionRequest<ImpsBeneficiaryDetailsResult> request , String sBeneficiaryName) {
//        if (request == null || TextUtils.isEmpty(sBeneficiaryName)) {
//            if (_listener != null) {
//                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.IMPS_BENEFICIARY_DETAILS);
//            }
//            return;
//        }
//
//        String url = AppConstants.URL_PBX_PLATFORM_IMPS + AppConstants.SVC_NEW_METHOD_IMPS_CUSTOMER_REGISTRATION;
//
//        AsyncDataEx dataEx = new AsyncDataEx(this, new TransactionRequest(), url, Methods.IMPS_BENEFICIARY_DETAILS);
//// PARAMS
//        dataEx.execute(
//                new BasicNameValuePair(AppConstants.PARAM_PBX_SERVICE, AppConstants.PARAM_SERVICE_IMPS_BENEFICIARY_DETAILS),
//                new BasicNameValuePair(AppConstants.PARAM_PBX_IMPS_BENEFICIARY_ACCOUNT_NAME,sBeneficiaryName),
//                new BasicNameValuePair(AppConstants.PARAM_PBX_IMPS_BENEFICIARY_CUSTOMER_ID, String.valueOf(EphemeralStorage.getInstance(_applicationContext).getInt(AppConstants.PARAM_PBX_IMPS_CUSTOMER_ID, -1))),
//                new BasicNameValuePair(AppConstants.PARAM_PBX_ROWID, EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_PBX_USERID, null)),
//                new BasicNameValuePair(AppConstants.PARAM_PBX_RMN, EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.LOGGED_IN_USERNAME, null))
//
//
//        );
//    }
public void impsBeneficiaryDetails(TransactionRequest request , String sBeneficiaryName) {
    if (request == null || TextUtils.isEmpty(sBeneficiaryName)) {
        if (_listener != null) {
            _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.IMPS_BENEFICIARY_DETAILS);
        }
        return;
    }
   // Toast.makeText(_applicationContext ,String.valueOf(EphemeralStorage.getInstance(_applicationContext).getInt(AppConstants.PARAM_NEW_MOM_IMPS_CUSTOMER_STATUS_CUSTOMER_ID, -1)),Toast.LENGTH_LONG).show();
    String url       = AppConstants.URL_NEW_PLATFORM_IMPS + AppConstants.SVC_NEW_METHOD_IMPS_MOM_BENEFICIARY_DETAILS;
    String sUserId   = EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_USER_ID, null);
    AsyncDataEx dataEx = new AsyncDataEx(this, new TransactionRequest(), url, Methods.IMPS_BENEFICIARY_DETAILS);
// PARAMS
    dataEx.execute(
            new BasicNameValuePair(AppConstants.PARAM_NEW_RETAILER_ID, sUserId),
            new BasicNameValuePair(AppConstants.PARAM_NEW_IMPS_PASSWORD, EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.LOGGED_IN_PASSWORD, null)),
            new BasicNameValuePair(AppConstants.PARAM_NEW_COMPANY_ID, AppConstants.NEW_PL_COMPANY_ID),
            new BasicNameValuePair(AppConstants.PARAM_NEW_MOM_IMPS_CONSUMERID, String.valueOf(EphemeralStorage.getInstance(_applicationContext).getInt(AppConstants.PARAM_NEW_MOM_IMPS_CUSTOMER_STATUS_CUSTOMER_ID, -1))),
            new BasicNameValuePair(AppConstants.PARAM_NEW_IMPS_ACCOUNT_ALIAS , sBeneficiaryName)

    );
}

    public TransactionRequest<String> getIMPSBeneficiaryDetailsResponse(
            TransactionRequest<String> pResult , Methods callback
    ) throws MOMException{
        String response = null;
        String responseRegistrationStatus = null;
        String responseErrorMessage = null;
        if(pResult == null || "".equals(pResult.getRemoteResponse().trim())){
            throw new MOMException();
        }

        try {
            InputStream in = new ByteArrayInputStream(pResult.getRemoteResponse().getBytes("UTF-8"));
            new PullParserBeneficiaryDetailsHandler(in);


            Log.d(_LOG, "ResponseMOMIMPSBeneficiaryAliasName: " + pResult);
            String responseResult =   EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_MOM_IMPS_BENEFICIARY_ACCOUNT_NAME, null);
            Log.d(_LOG, "NewResponseMOMIMPSBeneficiaryAliasName:" + responseResult);
            pResult.setRemoteResponse(responseResult);


            return pResult;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
    public void getIMPSBankName(TransactionRequest request) {
        if (request == null ) {
            if (_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.IMPS_BANK_NAME_LIST);
            }
            return;
        }

        String url = AppConstants.URL_NEW_PLATFORM_IMPS + AppConstants.SVC_NEW_METHOD_IMPS_MOM_IFSC_BANKS;
        String sUserId              = EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_USER_ID, null);
        AsyncDataEx dataEx = new AsyncDataEx(this, new TransactionRequest(), url, Methods.IMPS_BANK_NAME_LIST);

        dataEx.execute(
                new BasicNameValuePair(AppConstants.PARAM_NEW_RETAILER_ID, sUserId),
                new BasicNameValuePair(AppConstants.PARAM_NEW_IMPS_PASSWORD, EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.LOGGED_IN_PASSWORD, null)),
                new BasicNameValuePair(AppConstants.PARAM_NEW_COMPANY_ID, AppConstants.NEW_PL_COMPANY_ID)
        );
    }
    public TransactionRequest<String> getIMPSBankListResponse(
            TransactionRequest<String> pResult , Methods callback
    ) throws MOMException{
        String response = null;
        String responseRegistrationStatus = null;
        String responseErrorMessage = null;
        if(pResult == null || "".equals(pResult.getRemoteResponse().trim())){
            throw new MOMException();
        }

        try {
            InputStream in = new ByteArrayInputStream(pResult.getRemoteResponse().getBytes("UTF-8"));
            new PullParserHandler(in);


            Log.d(_LOG, "ResponseMOMIMPSBank: " + pResult);



            return pResult;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
//    public TransactionRequest getIMPSBankListResponse(TransactionRequest<List<BankNameResult>> request) {
//        Gson gson = new GsonBuilder().create();
//
//        Type type = new TypeToken<ResponseBase<BankNameResult[]>>() {
//        }.getType();
//
//        ResponseBase<BankNameResult[]> responseBase = gson.fromJson(request.getRemoteResponse(), type);
//
//        if (responseBase == null || responseBase.data == null) {
//            return null;
//        }
//
//        List<BankNameResult> bankNameResultList = Arrays.asList(responseBase.data);
//
//        // List<BankNameResult> bankNameResultList = new ArrayList<BankNameResult>(Arrays.asList(responseBase.data));
//        request.setCustom(bankNameResultList);
//
//        Log.d(_LOG, "NEW_PL_DATA_BeneficiaryBankListResponse: ");
//        return request;
//    }

    public void getIMPSStateName(TransactionRequest request , String sBankName) {
        if((request == null ) || ("".equals(sBankName))) {
            if (_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.IMPS_STATE_NAME);
            }
            return;
        }


        String url = AppConstants.URL_NEW_PLATFORM_IMPS + AppConstants.SVC_NEW_METHOD_IMPS_MOM_IFSC_STATE_NAME;
        String sUserId              = EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_USER_ID, null);
        AsyncDataEx dataEx = new AsyncDataEx(this, new TransactionRequest(), url, Methods.IMPS_STATE_NAME);

        dataEx.execute(
                new BasicNameValuePair(AppConstants.PARAM_NEW_RETAILER_ID, sUserId),
                new BasicNameValuePair(AppConstants.PARAM_NEW_IMPS_PASSWORD, EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.LOGGED_IN_PASSWORD, null)),
                new BasicNameValuePair(AppConstants.PARAM_NEW_COMPANY_ID, AppConstants.NEW_PL_COMPANY_ID),
                new BasicNameValuePair(AppConstants.PARAM_NEW_IMPS_BANK_NAME, sBankName)


        );
    }
    public TransactionRequest<String> getIMPSStateListResponse(
            TransactionRequest<String> pResult , Methods callback
    ) throws MOMException{
        String response = null;
        String responseRegistrationStatus = null;
        String responseErrorMessage = null;
        if(pResult == null || "".equals(pResult.getRemoteResponse().trim())){
            throw new MOMException();
        }

        try {
            InputStream in = new ByteArrayInputStream(pResult.getRemoteResponse().getBytes("UTF-8"));
            new PullParserHandler(in);


            Log.d(_LOG, "ResponseMOMIMPSStateList: " + pResult);

            Log.d(_LOG, "NEW_PL_DATA_BeneficiaryStateListResponse: ");


            return pResult;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
//    public TransactionRequest getIMPSStateListResponse(TransactionRequest<List<StateNameResult>> request) {
//        Gson gson = new GsonBuilder().create();
//
//        Type type = new TypeToken<ResponseBase<StateNameResult[]>>() {
//        }.getType();
//
//        ResponseBase<StateNameResult[]> responseBase = gson.fromJson(request.getRemoteResponse(), type);
//
//        if (responseBase == null || responseBase.data == null) {
//            return null;
//        }
//
//        List<StateNameResult> stateNameResultList = Arrays.asList(responseBase.data);
//        // List<StateNameResult> stateNameResultList = new ArrayList<StateNameResult>(Arrays.asList(responseBase.data));
//        request.setCustom(stateNameResultList);
//        Log.d(_LOG, "NEW_PL_DATA_BeneficiaryStateListResponse: ");
//        return request;
//    }


    public void getIMPSCityName(TransactionRequest request , String sBankName , String sStateName) {
        if ((request == null ) || ("".equals(sBankName)) || (("".equals(sStateName)))) {
            if (_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.IMPS_CITY_NAME);
            }
            return;
        }


        String url = AppConstants.URL_NEW_PLATFORM_IMPS + AppConstants.SVC_NEW_METHOD_IMPS_MOM_IFSC_CITY_NAME;
        String sUserId              = EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_USER_ID, null);
        AsyncDataEx dataEx = new AsyncDataEx(this, new TransactionRequest(), url, Methods.IMPS_CITY_NAME);

        dataEx.execute(
                new BasicNameValuePair(AppConstants.PARAM_NEW_RETAILER_ID, sUserId),
                new BasicNameValuePair(AppConstants.PARAM_NEW_IMPS_PASSWORD, EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.LOGGED_IN_PASSWORD, null)),
                new BasicNameValuePair(AppConstants.PARAM_NEW_COMPANY_ID, AppConstants.NEW_PL_COMPANY_ID),
                new BasicNameValuePair(AppConstants.PARAM_NEW_IMPS_BANK_NAME, sBankName),
                new BasicNameValuePair(AppConstants.PARAM_NEW_IMPS_STATE_NAME, sStateName)


        );
    }
    public TransactionRequest<String> getIMPSCityListResponse(
            TransactionRequest<String> pResult , Methods callback
    ) throws MOMException{
        String response = null;
        String responseRegistrationStatus = null;
        String responseErrorMessage = null;
        if(pResult == null || "".equals(pResult.getRemoteResponse().trim())){
            throw new MOMException();
        }

        try {
            InputStream in = new ByteArrayInputStream(pResult.getRemoteResponse().getBytes("UTF-8"));
            new PullParserHandler(in);


            Log.d(_LOG, "ResponseMOMIMPSCityList: " + pResult);

            Log.d(_LOG, "NEW_PL_DATA_BeneficiaryCityListResponse: ");


            return pResult;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
//    public TransactionRequest getIMPSCityListResponse(TransactionRequest<List<CityNameResult>> request) {
//        Gson gson = new GsonBuilder().create();
//
//        Type type = new TypeToken<ResponseBase<CityNameResult[]>>() {
//        }.getType();
//
//        ResponseBase<CityNameResult[]> responseBase = gson.fromJson(request.getRemoteResponse(), type);
//
//        if (responseBase == null || responseBase.data == null) {
//            return null;
//        }
//
//        List<CityNameResult> cityNameResultList = Arrays.asList(responseBase.data);
//        // List<CityNameResult> cityNameResultList = new ArrayList<CityNameResult>(Arrays.asList(responseBase.data));
//        request.setCustom(cityNameResultList);
//        Log.d(_LOG, "NEW_PL_DATA_BeneficiaryCityListResponse: ");
//
//        return request;
//    }

    public void getIMPSBranchName(TransactionRequest request , String sBankName , String sStateName ,String sCityName) {
        if ((request == null ) || ("".equals(sBankName)) || ("".equals(sStateName))  ||  ("".equals(sCityName)) ) {
            if (_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.IMPS_BRANCH_NAME);
            }
            return;
        }

        String url = AppConstants.URL_NEW_PLATFORM_IMPS + AppConstants.SVC_NEW_METHOD_IMPS_MOM_IFSC_BRANCH_NAME;
        String sUserId              = EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_USER_ID, null);

        AsyncDataEx dataEx = new AsyncDataEx(this, new TransactionRequest(), url, Methods.IMPS_BRANCH_NAME);

        dataEx.execute(
                new BasicNameValuePair(AppConstants.PARAM_NEW_RETAILER_ID, sUserId),
                new BasicNameValuePair(AppConstants.PARAM_NEW_IMPS_PASSWORD, EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.LOGGED_IN_PASSWORD, null)),
                new BasicNameValuePair(AppConstants.PARAM_NEW_COMPANY_ID, AppConstants.NEW_PL_COMPANY_ID),
                new BasicNameValuePair(AppConstants.PARAM_NEW_IMPS_BANK_NAME, sBankName),
                new BasicNameValuePair(AppConstants.PARAM_NEW_IMPS_STATE_NAME, sStateName),
                new BasicNameValuePair(AppConstants.PARAM_NEW_IMPS_CITY_NAME, sCityName)
                      );
    }
    public TransactionRequest<String> getIMPSBranchListResponse(
            TransactionRequest<String> pResult , Methods callback
    ) throws MOMException{
        String response = null;
        String responseRegistrationStatus = null;
        String responseErrorMessage = null;
        if(pResult == null || "".equals(pResult.getRemoteResponse().trim())){
            throw new MOMException();
        }

        try {
            InputStream in = new ByteArrayInputStream(pResult.getRemoteResponse().getBytes("UTF-8"));
            new PullParserHandler(in);


            Log.d(_LOG, "ResponseMOMIMPSBranchList: " + pResult);

            Log.d(_LOG, "NEW_PL_DATA_BeneficiaryBranchListResponse: ");


            return pResult;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
//    public TransactionRequest getIMPSBranchListResponse(TransactionRequest<List<BranchNameResult>> request) {
//        Gson gson = new GsonBuilder().create();
//
//        Type type = new TypeToken<ResponseBase<BranchNameResult[]>>() {
//        }.getType();
//
//        ResponseBase<BranchNameResult[]> responseBase = gson.fromJson(request.getRemoteResponse(), type);
//
//        if (responseBase == null || responseBase.data == null) {
//            return null;
//        }
//
//        List<BranchNameResult> branchNameResultList = Arrays.asList(responseBase.data);
//        //List<BranchNameResult> branchNameResultList = new ArrayList<BranchNameResult>(Arrays.asList(responseBase.data));
//        request.setCustom(branchNameResultList);
//
//        Log.d(_LOG, "NEW_PL_DATA_BeneficiaryBranchListResponse: ");
//
//
//        return request;
//    }
    public void impsPaymentProcess(TransactionRequest<ImpsPaymentProcessResult> request , String sAmount ,String sTxnDescription) {
        if ((request == null) || ("".equals(sAmount)) || ("".equals(sTxnDescription))) {
            if (_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.IMPS_PAYMENT_PROCESS);
            }
        }
        String sUserId              = EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_USER_ID, null);
        String Url = AppConstants.URL_NEW_PLATFORM_IMPS + AppConstants.SVC_NEW_METHOD_IMPS_SUBMIT_PAYMENT;

        AsyncDataEx dataEx = new AsyncDataEx(this, new TransactionRequest(), Url, Methods.IMPS_PAYMENT_PROCESS);

        dataEx.execute(

                new BasicNameValuePair(AppConstants.PARAM_NEW_USER_ID, sUserId),
                new BasicNameValuePair(AppConstants.PARAM_NEW_IMPS_PASSWORD,  EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.LOGGED_IN_PASSWORD,null)),
                new BasicNameValuePair(AppConstants.PARAM_NEW_COMPANY_ID, AppConstants.NEW_PL_COMPANY_ID),
                new BasicNameValuePair(AppConstants.PARAM_NEW_IMPS_ConsumerID, String.valueOf(EphemeralStorage.getInstance(_applicationContext).getInt(AppConstants.PARAM_PBX_IMPS_CUSTOMER_ID, -1))),
                new BasicNameValuePair(AppConstants.PARAM_NEW_IMPS_PAYMENT_BENEFICIARY_ID, EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_PBX_IMPS_BENEFICIARY_ID,null)),
                new BasicNameValuePair(AppConstants.PARAM_NEW_IMPS_PAYMENT_REMITTANCE_AMOUNT, sAmount),
                new BasicNameValuePair(AppConstants.PARAM_NEW_IMPS_TRANSACTION_NARRATION, sTxnDescription)

        );
    }

    public void impsMomPaymentProcess(TransactionRequest<ImpsPaymentProcessResult> request , String sAmount ,String sTxnDescription) {
        if ((request == null) || ("".equals(sAmount)) || ("".equals(sTxnDescription))) {
            if (_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.IMPS_MOM_SUBMIT_PROCESS);
            }
        }
        String sUserId              = EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_USER_ID, null);
        String Url = AppConstants.URL_NEW_PLATFORM_IMPS + AppConstants.SVC_NEW_METHOD_IMPS_SUBMIT_PAYMENT;

        AsyncDataEx dataEx = new AsyncDataEx(this, new TransactionRequest(), Url, Methods.IMPS_MOM_SUBMIT_PROCESS);

        dataEx.execute(

                new BasicNameValuePair(AppConstants.PARAM_NEW_USER_ID, sUserId),
                new BasicNameValuePair(AppConstants.PARAM_NEW_IMPS_PASSWORD,  EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.LOGGED_IN_PASSWORD,null)),
                new BasicNameValuePair(AppConstants.PARAM_NEW_COMPANY_ID, AppConstants.NEW_PL_COMPANY_ID),
                new BasicNameValuePair(AppConstants.PARAM_NEW_IMPS_ConsumerID, String.valueOf(EphemeralStorage.getInstance(_applicationContext).getInt(AppConstants.PARAM_PBX_IMPS_CUSTOMER_ID, -1))),
                new BasicNameValuePair(AppConstants.PARAM_NEW_IMPS_PAYMENT_BENEFICIARY_ID, EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_PBX_IMPS_BENEFICIARY_ID,null)),
                new BasicNameValuePair(AppConstants.PARAM_NEW_IMPS_PAYMENT_REMITTANCE_AMOUNT, sAmount),
                new BasicNameValuePair(AppConstants.PARAM_NEW_IMPS_TRANSACTION_NARRATION, sTxnDescription)

        );
    }

    public void tpinMomPaymentProcess(TransactionRequest request , String sAmount ,String sTxnDescription) {
        if (request == null) {
            if (_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.MOM_T_PIN);
            }
        }
        String sUserId              = EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_USER_ID, null);
        String Url = AppConstants.URL_NEW_PLATFORM_IMPS + AppConstants.SVC_NEW_METHOD_MOM_T_PIN;

        AsyncDataEx dataEx = new AsyncDataEx(this, new TransactionRequest(), Url, Methods.MOM_T_PIN);

        dataEx.execute(

                new BasicNameValuePair(AppConstants.PARAM_NEW_USER_ID, sUserId),
                new BasicNameValuePair(AppConstants.PARAM_NEW_IMPS_PASSWORD,  EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.LOGGED_IN_PASSWORD,null)),
                new BasicNameValuePair(AppConstants.PARAM_NEW_COMPANY_ID, AppConstants.NEW_PL_COMPANY_ID),
                new BasicNameValuePair(AppConstants.PARAM_NEW_IMPS_ConsumerID, String.valueOf(EphemeralStorage.getInstance(_applicationContext).getInt(AppConstants.PARAM_PBX_IMPS_CUSTOMER_ID, -1))),
                new BasicNameValuePair(AppConstants.PARAM_NEW_IMPS_PAYMENT_BENEFICIARY_ID, EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_PBX_IMPS_BENEFICIARY_ID,null)),
                new BasicNameValuePair(AppConstants.PARAM_NEW_IMPS_PAYMENT_REMITTANCE_AMOUNT, sAmount),
                new BasicNameValuePair(AppConstants.PARAM_NEW_IMPS_TRANSACTION_NARRATION, sTxnDescription)

        );
    }

    public void impsVerifyProcess(TransactionRequest<ImpsVerifyProcessResult> request){

    }

    public void impsVerifyPayment(TransactionRequest<ImpsVerifyPaymentResult> request ,String sOTP ,
                                  String sAccountNumber ,String sIFSCCodeString , String sCustomerNumber){

    }

//    public void impsMomPaymentProcess(TransactionRequest request , String sAmount ,String sTxnDescription ,
//                                      String sOTP , String sClientTxnID) {
//        if ((request == null) || ("".equals(sAmount)) || ("".equals(sTxnDescription))) {
//            if (_listener != null) {
//                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.IMPS_MOM_SUBMIT_PROCESS);
//            }
//        }
//        String sUserId              = EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_USER_ID, null);
//        String Url = AppConstants.URL_NEW_PLATFORM_IMPS + AppConstants.SVC_NEW_METHOD_IMPS_SUBMIT_PAYMENT;
//
//        AsyncDataEx dataEx = new AsyncDataEx(this, new TransactionRequest(), Url, Methods.IMPS_MOM_SUBMIT_PROCESS);
//
//        dataEx.execute(
//
//                new BasicNameValuePair(AppConstants.PARAM_NEW_RETAILER_ID, sUserId),
//                new BasicNameValuePair(AppConstants.PARAM_NEW_IMPS_PASSWORD,  EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.LOGGED_IN_PASSWORD,null)),
//                new BasicNameValuePair(AppConstants.PARAM_NEW_COMPANY_ID, AppConstants.NEW_PL_COMPANY_ID),
//                new BasicNameValuePair(AppConstants.PARAM_NEW_IMPS_ConsumerID, String.valueOf(EphemeralStorage.getInstance(_applicationContext).getInt(AppConstants.PARAM_PBX_IMPS_CUSTOMER_ID, -1))),
//                new BasicNameValuePair(AppConstants.PARAM_NEW_IMPS_PAYMENT_BENEFICIARY_ID, EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_PBX_IMPS_BENEFICIARY_ID,null)),
//                new BasicNameValuePair(AppConstants.PARAM_NEW_IMPS_PAYMENT_REMITTANCE_AMOUNT, sAmount),
//                new BasicNameValuePair(AppConstants.PARAM_NEW_IMPS_TRANSACTION_NARRATION, sTxnDescription),
//                new BasicNameValuePair(AppConstants.PARAM_PBX_IMPS_OTP, sOTP),
//                new BasicNameValuePair(AppConstants.PARAM_NEW_IMPS_CLIENTTxnID, sClientTxnID)
//
//        );
//    }

    public void impsMomConfirmProcess(TransactionRequest request , String sOTP ,
                                     String sAccountNumber ,String sClientTxnID , String sCustomerNumber) {
    if ((request == null)|| ("".equals(sOTP)) || ("".equals(sAccountNumber)) ||
            ("".equals(sClientTxnID)) || ("".equals(sCustomerNumber))){
        if (_listener != null) {
            _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.IMPS_MOM_CONFIRM_PROCESS);
        }
    }
    String sUserId              = EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_USER_ID, null);
    String Url = AppConstants.URL_NEW_PLATFORM_IMPS + AppConstants.SVC_NEW_METHOD_IMPS_CONFIRM_PAYMENT;

    AsyncDataEx dataEx = new AsyncDataEx(this, new TransactionRequest(), Url, Methods.IMPS_MOM_CONFIRM_PROCESS);

    dataEx.execute(

            new BasicNameValuePair(AppConstants.PARAM_NEW_RETAILER_ID, sUserId),
            new BasicNameValuePair(AppConstants.PARAM_NEW_IMPS_PASSWORD,  EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.LOGGED_IN_PASSWORD,null)),
            new BasicNameValuePair(AppConstants.PARAM_NEW_COMPANY_ID, AppConstants.NEW_PL_COMPANY_ID),
            new BasicNameValuePair(AppConstants.PARAM_PBX_IMPS_SESSIONID, EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_SUBMIT_SESSION_ID, null)),
            new BasicNameValuePair(AppConstants.PARAM_PBX_IMPS_OTP, sOTP),
            new BasicNameValuePair(AppConstants.PARAM_NEW_IMPS_CLIENTTxnID, sClientTxnID)


    );
}

    public void impsMomConfirmPaymentProcess(TransactionRequest request , String sAmount ,String sTxnDescription ,
                                          String sIPin , String sClientTxnID ) {
        if ((request == null)|| ("".equals(sIPin)) || ("".equals(sAmount)) ||
                ("".equals(sClientTxnID)) || ("".equals(sTxnDescription))){
            if (_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.IMPS_MOM_CONFIRM_PROCESS);
            }
        }
        String sUserId              = EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_USER_ID, null);
        String Url = AppConstants.URL_NEW_PLATFORM_IMPS + AppConstants.SVC_NEW_METHOD_IMPS_SUBMIT_PAYMENT_IPIN;
     //   Toast.makeText(_applicationContext ,String.valueOf(EphemeralStorage.getInstance(_applicationContext).getInt(AppConstants.PARAM_NEW_MOM_IMPS_CUSTOMER_STATUS_CUSTOMER_ID, -1)),Toast.LENGTH_LONG).show();
        AsyncDataEx dataEx = new AsyncDataEx(this, new TransactionRequest(), Url, Methods.IMPS_MOM_CONFIRM_PROCESS);
        String custID = String.valueOf(EphemeralStorage.getInstance(_applicationContext).getInt(AppConstants.PARAM_NEW_MOM_IMPS_CUSTOMER_STATUS_CUSTOMER_ID, -1));
        Log.e("CustID" , custID);
        Log.e("BenefID" ,EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_MOM_IMPS_BENEFICIARY_ID,null));
        Log.e("Pwd", EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.LOGGED_IN_PASSWORD,null));
        dataEx.execute(

                new BasicNameValuePair(AppConstants.PARAM_NEW_RETAILER_ID, sUserId),
                new BasicNameValuePair(AppConstants.PARAM_NEW_IMPS_PASSWORD,  EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.LOGGED_IN_PASSWORD,null)),
                new BasicNameValuePair(AppConstants.PARAM_NEW_COMPANY_ID, AppConstants.NEW_PL_COMPANY_ID),
                new BasicNameValuePair(AppConstants.PARAM_NEW_CUSTOMER_ID, String.valueOf(EphemeralStorage.getInstance(_applicationContext).getInt(AppConstants.PARAM_NEW_MOM_IMPS_CUSTOMER_STATUS_CUSTOMER_ID, -1))),
                new BasicNameValuePair(AppConstants.PARAM_NEW_IMPS_PAYMENT_BENEFICIARY_ID, EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_MOM_IMPS_BENEFICIARY_ID,null)),
                new BasicNameValuePair(AppConstants.PARAM_NEW_IMPS_PAYMENT_REMITTANCE_AMOUNT, sAmount),
                new BasicNameValuePair(AppConstants.PARAM_NEW_MOM_IMPS_TRANSACTION_NARRATION, sTxnDescription),
                new BasicNameValuePair(AppConstants.PARAM_NEW_MOM_IMPS_IPIN, sIPin),
                new BasicNameValuePair(AppConstants.PARAM_NEW_IMPS_CLIENTTxnID, sClientTxnID)


        );
    }

    public void impsMomConfirmPaymentProcessByRefund(TransactionRequest request , String sReceiptID ,String sTxnDescription ,
                                             String sIPin , String sClientTxnID ) {
        if ((request == null)|| ("".equals(sIPin)) || ("".equals(sReceiptID)) ||
                ("".equals(sClientTxnID)) || ("".equals(sTxnDescription))){
            if (_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.IMPS_MOM_CONFIRM_PROCESS_ByREFUND);
            }
        }
        String sUserId              = EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_USER_ID, null);
        String Url = AppConstants.URL_NEW_PLATFORM_IMPS + AppConstants.SVC_NEW_METHOD_IMPS_SUBMIT_PAYMENT_IPIN_REFUND;

        AsyncDataEx dataEx = new AsyncDataEx(this, new TransactionRequest(), Url, Methods.IMPS_MOM_CONFIRM_PROCESS_ByREFUND);
        String custID = String.valueOf(EphemeralStorage.getInstance(_applicationContext).getInt(AppConstants.PARAM_NEW_MOM_IMPS_CUSTOMER_STATUS_CUSTOMER_ID, -1));
        Log.e("CustID" , custID);
        Log.e("BenefID" ,EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_MOM_IMPS_BENEFICIARY_ID,null));
        Log.e("Pwd", EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.LOGGED_IN_PASSWORD,null));
        dataEx.execute(

                new BasicNameValuePair(AppConstants.PARAM_NEW_RETAILER_ID, sUserId),
                new BasicNameValuePair(AppConstants.PARAM_NEW_IMPS_PASSWORD,  EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.LOGGED_IN_PASSWORD,null)),
                new BasicNameValuePair(AppConstants.PARAM_NEW_COMPANY_ID, AppConstants.NEW_PL_COMPANY_ID),
                new BasicNameValuePair(AppConstants.PARAM_NEW_CUSTOMER_ID, String.valueOf(EphemeralStorage.getInstance(_applicationContext).getInt(AppConstants.PARAM_NEW_MOM_IMPS_CUSTOMER_STATUS_CUSTOMER_ID, -1))),
                new BasicNameValuePair(AppConstants.PARAM_NEW_IMPS_PAYMENT_BENEFICIARY_ID, EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_MOM_IMPS_BENEFICIARY_ID,null)),
                new BasicNameValuePair(AppConstants.PARAM_NEW_IMPS_PAYMENT_REFUND_RECEIPT_ID, sReceiptID),
                new BasicNameValuePair(AppConstants.PARAM_NEW_MOM_IMPS_TRANSACTION_NARRATION, sTxnDescription),
                new BasicNameValuePair(AppConstants.PARAM_NEW_MOM_IMPS_IPIN, sIPin),
                new BasicNameValuePair(AppConstants.PARAM_NEW_IMPS_CLIENTTxnID, sClientTxnID)


        );
    }


    public void impsMomIMPSServiceCharge(TransactionRequest request , String sAmount) {
        if ((request == null)|| ("".equals(sAmount))){
            if (_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.IMPS_MOM_SEVICE_CHARGE_PROCESS);
            }
        }
        String sUserId              = EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_USER_ID, null);
        String Url = AppConstants.URL_NEW_PLATFORM_IMPS + AppConstants.SVC_NEW_METHOD_IMPS_SERVICE_CHARGE_PAYMENT;

        AsyncDataEx dataEx = new AsyncDataEx(this, new TransactionRequest(), Url, Methods.IMPS_MOM_SEVICE_CHARGE_PROCESS);

        dataEx.execute(

                new BasicNameValuePair(AppConstants.PARAM_NEW_RETAILER_ID, sUserId),
                new BasicNameValuePair(AppConstants.PARAM_NEW_IMPS_AMOUNT, sAmount)
        );
    }
    public void impsConfirmPayment (TransactionRequest<List<ImpsConfirmPaymentResult>> request , String sOTP ,
                                    String sAccountNumber ,String sIFSCCode , String sCustomerNumber , String sAmount){

    }
    public void impsAuthentication(TransactionRequest request){
        if (request == null) {
            if (_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.IMPS_AUTHENTICATION_MOM);
            }
        }
        String sUserId              = EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_USER_ID, null);
        String Url = AppConstants.URL_NEW_PLATFORM_IMPS + AppConstants.SVC_NEW_METHOD_IMPS_AUTHENTICATION;

        AsyncDataEx dataEx = new AsyncDataEx(this, new TransactionRequest(), Url, Methods.IMPS_AUTHENTICATION_MOM);

        dataEx.execute(

                new BasicNameValuePair(AppConstants.PARAM_NEW_RETAILER_ID, sUserId)


        );

    }
    public TransactionRequest<LicLifeResponse> getLic(TransactionRequest<LicLifeResponse> response) throws MOMException {

        if (TextUtils.isEmpty(response.getRemoteResponse())) {
            throw new MOMException();
        }

        try {
            Gson gson = new GsonBuilder().create();

            Type type = new TypeToken<ResponseBase<LicResponse>>() {
            }.getType();

            ResponseBase<LicResponse> responseBase = gson.fromJson(response.getRemoteResponse(), type);


            if (
                    responseBase == null ||
                            responseBase.data == null ||
                            responseBase.data.TXLife == null ||
                            responseBase.data.TXLife.getTXLifeResponse() == null
                    ) {
                throw new MOMException();
            }

            LicResponse licResponse = responseBase.data;
            LicLife txLife = licResponse.TXLife;

            response.setCustom(txLife.getTXLifeResponse());

            return response;

        } catch (JsonSyntaxException jse) {
            Log.e(_LOG, jse.getMessage());
        }

        return null;
    }

    public TransactionRequest<LicLifeResponse> getPremiumAmount(TransactionRequest<LicLifeResponse> response) throws MOMException {

        if (TextUtils.isEmpty(response.getRemoteResponse())) {
            throw new MOMException();
        }

        try {
            Gson gson = new GsonBuilder().create();

            Type type = new TypeToken<ResponseBase<LicResponse>>() {
            }.getType();

            ResponseBase<LicResponse> responseBase = gson.fromJson(response.getRemoteResponse(), type);


            if (
                    responseBase == null ||
                            responseBase.data == null ||
                            responseBase.data.TXLife == null ||
                            responseBase.data.TXLife.getTXLifeResponse() == null ||
                            responseBase.data.TXLife.getTXLifeResponse().getOLife() == null
                    ) {
                throw new MOMException();
            }

            LicResponse licResponse = responseBase.data;
            LicLife txLife = licResponse.TXLife;

            Log.i("Premium Amount", (txLife.getTXLifeResponse().getTransInvAmount().toString()));
            Log.i("Full Name", (txLife.getTXLifeResponse().getOLife().getParty().getFullName()));
            Log.i("From Unpaid", (txLife.getTXLifeResponse().getOLife().getPolicy().getFrUnpaidPremiumDate().toString()));
            Log.i("To Unpaid", (txLife.getTXLifeResponse().getOLife().getPolicy().getToUnpaidPremiumDate().toString()));

            response.setCustom(txLife.getTXLifeResponse());


            return response;

        } catch (JsonSyntaxException jse) {
            Log.e(_LOG, jse.getMessage());
        }

        return null;
    }

}