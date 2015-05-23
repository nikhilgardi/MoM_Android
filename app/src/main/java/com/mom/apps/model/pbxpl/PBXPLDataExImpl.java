package com.mom.apps.model.pbxpl;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.mom.apps.error.MOMException;
import com.mom.apps.gcm.GcmUtil;
import com.mom.apps.identifier.PBXOperatorDataType;
import com.mom.apps.identifier.PinType;
import com.mom.apps.model.AsyncDataEx;
import com.mom.apps.model.AsyncListener;
import com.mom.apps.model.AsyncResult;
import com.mom.apps.model.DataExImpl;
import com.mom.apps.model.Operator;
import com.mom.apps.model.Transaction;
import com.mom.apps.model.local.EphemeralStorage;

import com.mom.apps.model.pbxpl.lic.LicLife;
import com.mom.apps.model.pbxpl.lic.LicLifeResponse;

import com.mom.apps.model.pbxpl.lic.LicResponse;
import com.mom.apps.ui.TransactionRequest;
import com.mom.apps.utils.AppConstants;
import com.mom.apps.utils.MiscUtils;

import org.apache.http.message.BasicNameValuePair;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by vaibhavsinha on 7/6/14.
 */
public class PBXPLDataExImpl extends DataExImpl implements AsyncListener<TransactionRequest> {
    static String _LOG = AppConstants.LOG_PREFIX + "PBX_DATA";


    String _deviceRegId = null;
    String _token = null;
    String _userName = null;
    PBXOperatorDataType _opType = null;
    AsyncDataEx dataEx =null;

    // String jsonStr =    "{\"Table\":[{\"PartyROWID\":92420,\"PartyRMN\":\"9769496026\",\"PartyName\":\"Akshay\",\"PartyGUID\":\"9163b4dd-f23d-41fd-99ab-7c0f57c9c7ed\",\"PartyEnum\":null,\"PartyTypeEnum\":16,\"userName\":\"Software\"}]}" ;
//    public PBXPLDataExImpl(Context pContext, Methods method, AsyncListener pListener, boolean isBalance) throws MOMException {
//        checkConnectivity(pContext);
//        _applicationContext = pContext;
//
//        _deviceRegId = GcmUtil.getInstance(pContext).getRegistrationId();
//        _userName = EphemeralStorage.getInstance(_applicationContext).getString(
//                AppConstants.LOGGED_IN_USERNAME, null
//        );
//        if(!isBalance){
//            _listener = pListener;
//        }else{
//            _balance_listener = pListener;
//        }
//
//        _applicationContext = pContext;
//        _deviceRegId = GcmUtil.getInstance(pContext).getRegistrationId();
//        _token = EphemeralStorage.getInstance(pContext).getString(
//                AppConstants.PARAM_PBX_TOKEN, null
//        );
//        _userName = EphemeralStorage.getInstance(pContext).getString(
//                AppConstants.LOGGED_IN_USERNAME, null
//        );
//
//
//        if (method != Methods.LOGIN) {
//            if (TextUtils.isEmpty(_userName)) {
//                throw new IllegalStateException("username should not be empty here");
//            }
//
//            if (TextUtils.isEmpty(_deviceRegId)) {
//                throw new IllegalStateException("device id should not be empty here");
//            }
//
//            if (TextUtils.isEmpty(_token)) {
//                throw new MOMException(AsyncResult.CODE.NOT_LOGGED_IN);
//            }
//        }
//    }
//    public PBXPLDataExImpl(Context pContext, Methods method, AsyncListener pListener) throws MOMException {
//        this(pContext,method,pListener,false);
//    }
    public PBXPLDataExImpl(Context pContext, Methods method, AsyncListener pListener) throws MOMException {
        checkConnectivity(pContext);
        _applicationContext = pContext;

        _deviceRegId = GcmUtil.getInstance(pContext).getRegistrationId();
        _userName = EphemeralStorage.getInstance(_applicationContext).getString(
                AppConstants.LOGGED_IN_USERNAME, null
        );

        _listener = pListener;
        _applicationContext = pContext;
        _deviceRegId = GcmUtil.getInstance(pContext).getRegistrationId();
        _token = EphemeralStorage.getInstance(pContext).getString(
                AppConstants.PARAM_PBX_TOKEN, null
        );
        _userName = EphemeralStorage.getInstance(pContext).getString(
                AppConstants.LOGGED_IN_USERNAME, null
        );


        if (method != Methods.LOGIN) {
            if (TextUtils.isEmpty(_userName)) {
                throw new IllegalStateException("username should not be empty here");
            }

            if (TextUtils.isEmpty(_deviceRegId)) {
                throw new IllegalStateException("device id should not be empty here");
            }

            if (TextUtils.isEmpty(_token)) {
                throw new MOMException(AsyncResult.CODE.NOT_LOGGED_IN);
            }
        }
    }


    public boolean setToken() {
        _token = EphemeralStorage.getInstance(_applicationContext).getString(
                AppConstants.PARAM_PBX_TOKEN, null
        );

        if (TextUtils.isEmpty(_token)) {
            return false;
        }

        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onTaskSuccess(TransactionRequest result, Methods callback) {
        if (_listener == null) {
            return;
        }

        Log.d(_LOG, "Result: " + result);
        try {
            switch (callback) {
                case GET_BALANCE:
                    try {
                        float balance = parseBalanceResult(result);
                        if(_listener!=null){
                            _listener.onTaskSuccess(balance, callback);
                        }
//                        if(_balance_listener!=null){
//                            _balance_listener.onTaskSuccess(balance, callback);
//                        }//yes


                    } catch (NumberFormatException nfe) {
                        onTaskError(new AsyncResult(AsyncResult.CODE.GENERAL_FAILURE), Methods.GET_BALANCE);
                        nfe.printStackTrace();
                    }
                    break;
                case GET_OPERATOR_NAMES:
                    if(_listener!=null){
                        _listener.onTaskSuccess(getOperatorNamesResult(result), callback);
                    }

                    break;
                case TRANSACTION_HISTORY:
                    if(_listener!=null){
                        _listener.onTaskSuccess(extractTransactionshistory(result), callback);
                    }

                    break;
                case RECHARGE_MOBILE:
                    Log.i(_LOG, "TaskComplete: rechargeMobile method, result: " + result);
                    if (_listener != null) {
                        _listener.onTaskSuccess(getPaymentTransactionResult(result), Methods.RECHARGE_MOBILE);
                        Log.i("Result", result.getConsumerId());
                    }
                    break;

                case RECHARGE_DTH:
                    Log.i(_LOG, "TaskComplete: rechargeDTH method, result: " + result);
                    if (_listener != null) {
                        _listener.onTaskSuccess(getPaymentTransactionResult(result), Methods.RECHARGE_DTH);
                        Log.i("ResultDTH", result.getConsumerId());
                    }
                    break;
                case PAY_BILL:
                    Log.i(_LOG, "TaskComplete: payBill method, result: " + result);
                    if (_listener != null) {
                        _listener.onTaskSuccess(getPaymentTransactionResult(result), Methods.PAY_BILL);
                        Log.i("ResultBill", result.getConsumerId());
                    }
                    break;

                case UTILITY_BILL_PAY:
                    Log.i(_LOG, "TaskComplete: payBill method, result: " + result);
                    if (_listener != null) {
                        _listener.onTaskSuccess(getPaymentTransactionResult(result), Methods.UTILITY_BILL_PAY);
                        Log.i("ResultUBPBill", result.getConsumerId());
                    }
                    break;
                case IMPS_CREATE_CUSTOMER_REGISTRATION:
                    Log.i(_LOG, "TaskComplete: Create Customer method, result: " + result);
                    if (_listener != null) {
                        _listener.onTaskSuccess(getIMPSCreateCustomerResponse(result), Methods.IMPS_CREATE_CUSTOMER_REGISTRATION);
                       // Log.i("ResultCreateCustomer", result.getConsumerId());
                    }
                    break;


                case IMPS_CUSTOMER_REGISTRATION:
                    Log.d(_LOG, "TaskComplete: Customer Registration method, result: " + result);
                    if (_listener != null) {

                        _listener.onTaskSuccess(getIMPSCustomerRegistrationResponse(result), callback);

                    }
                    break;

                case IMPS_AUTHENTICATION:
                    Log.d(_LOG, "TaskComplete: IMPS Authentication method, result: " + result);
                    if (_listener != null) {

                        _listener.onTaskSuccess(getIMPSAuthenticationResponse(result), callback);

                    }
                    break;

                case IMPS_CHECK_KYC:
                    Log.d(_LOG, "TaskComplete: Check KYC method, result: " + result);
                    if (_listener != null) {

                        _listener.onTaskSuccess(getIMPSCheckKYCResponse(result), callback);

                    }
                    break;


                case IMPS_BENEFICIARY_LIST:
                    Log.d(_LOG, "TaskComplete: BeneficiaryList method, result: " + result);
                    if (_listener != null) {
                        _listener.onTaskSuccess(getIMPSBeneficiaryListResponse(result), Methods.IMPS_BENEFICIARY_LIST);
                    }
                    break;

                case IMPS_ADD_BENEFICIARY:
                    Log.d(_LOG, "TaskComplete: AddBeneficiary method, result: " + result);
                    if(_listener != null){
                        _listener.onTaskSuccess(getIMPSAddBeneficiaryResponse(result), Methods.IMPS_ADD_BENEFICIARY);
                    }
                    break;

                case IMPS_BENEFICIARY_DETAILS:
                    Log.d(_LOG, "TaskComplete: AddBeneficiary method, result: " + result);
                    if(_listener != null){
                        _listener.onTaskSuccess(getIMPSBeneficiaryDetailsResponse(result), Methods.IMPS_BENEFICIARY_DETAILS);
                    }
                    break;
                case IMPS_BANK_NAME_LIST:
                    Log.d(_LOG, "TaskComplete: BankList method, result: " + result);
                    if (_listener != null) {
                        _listener.onTaskSuccess( getIMPSBankListResponse(result), Methods.IMPS_BANK_NAME_LIST);
                    }
                    break;
                case IMPS_STATE_NAME:
                    Log.d(_LOG, "TaskComplete: StateList method, result: " + result);
                    if (_listener != null) {
                        _listener.onTaskSuccess( getIMPSStateListResponse(result), Methods.IMPS_STATE_NAME);
                    }
                    break;

                case IMPS_CITY_NAME:
                    Log.d(_LOG, "TaskComplete: CityList method, result: " + result);
                    if (_listener != null) {
                        _listener.onTaskSuccess( getIMPSCityListResponse(result), Methods.IMPS_CITY_NAME);
                    }
                    break;
                case IMPS_BRANCH_NAME:
                    Log.d(_LOG, "TaskComplete: BranchList method, result: " + result);
                    if (_listener != null) {
                        _listener.onTaskSuccess( getIMPSBranchListResponse(result), Methods.IMPS_BRANCH_NAME);
                    }
                    break;

                case IMPS_VERIFY_PROCESS:
                    Log.d(_LOG, "TaskComplete:VerifyProcess method, result: " + result);
                    if (_listener != null) {
                        _listener.onTaskSuccess( getIMPSVerifyProcessResponse(result), Methods.IMPS_VERIFY_PROCESS);
                    }
                    break;

                case IMPS_VERIFY_PAYMENT:
                    Log.d(_LOG, "TaskComplete:VerifyPayment method, result: " + result);
                    if (_listener != null) {
                        _listener.onTaskSuccess( getIMPSVerifyPaymentResponse(result), Methods.IMPS_VERIFY_PAYMENT);
                    }
                    break;

                case IMPS_PAYMENT_PROCESS:
                    Log.d(_LOG, "TaskComplete:VerifyPayment method, result: " + result);
                    if (_listener != null) {
                        _listener.onTaskSuccess( getIMPSPaymentProcessResponse(result), Methods.IMPS_PAYMENT_PROCESS);
                    }
                    break;

                case IMPS_CONFIRM_PAYMENT:
                    Log.d(_LOG, "TaskComplete:VerifyPayment method, result: " + result);
                    if (_listener != null) {
                        _listener.onTaskSuccess( getIMPSConfirmPaymentListResponse(result), Methods.IMPS_CONFIRM_PAYMENT);
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
                case BALANCE_TRANSFER:
                    Log.d(_LOG, "TaskComplete: balanceTransfer method, result: " + result);

                    if (_listener != null) {
                        _listener.onTaskSuccess(getInternalBalTransfer(result), Methods.BALANCE_TRANSFER);
                    }
                    break;

                case CHANGE_PASSWORD:
                    Log.d(_LOG, "TaskComplete: changeMPin method, result: " + result);

                    if (_listener != null) {
                        _listener.onTaskSuccess(getChangePasswordResult(result), Methods.CHANGE_PASSWORD);
                    }

                    break;

            }
        } catch (MOMException me) {
            me.printStackTrace();
            _listener.onTaskError(new AsyncResult(AsyncResult.CODE.GENERAL_FAILURE), callback);
        }
    }

    @Override
    public void onTaskError(AsyncResult pResult, Methods callback) {
        if (_listener == null) {
            return;
        }

        _listener.onTaskError(pResult, callback);
    }

    @Override
    public void verifyTPin(String psTPin) {

    }

    @Override
    public void rechargeMobile(TransactionRequest<PaymentResponse> request, int rechargeType) {

        if (TextUtils.isEmpty(request.getConsumerId())) {

            if (_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.RECHARGE_MOBILE);
            }
            return;
        }

        String url = AppConstants.URL_PBX_PLATFORM_APP;


        AsyncDataEx dataEx = new AsyncDataEx(this, request, url, Methods.RECHARGE_MOBILE);

        dataEx.execute(
                new BasicNameValuePair(AppConstants.PARAM_PBX_SERVICE, AppConstants.SVC_PBX_RECHARGE_MOBILE),
                new BasicNameValuePair(AppConstants.PARAM_PBX_RMN, EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.LOGGED_IN_USERNAME, null)),
                new BasicNameValuePair(AppConstants.PARAM_PBX_CUSTOMER_NUMBER, request.getConsumerId()),
                new BasicNameValuePair(AppConstants.PARAM_PBX_OPERTAORSHORTCODE, request.getOperator().getCode()),
                new BasicNameValuePair(AppConstants.PARAM_PBX_AMOUNT, String.valueOf(Math.round(request.getAmount()))),
                new BasicNameValuePair(AppConstants.PARAM_PBX_IDENTIFIER, String.valueOf(request.getId())),
                new BasicNameValuePair(AppConstants.PARAM_PBX_ORIGIN_ID, _deviceRegId),
                new BasicNameValuePair(AppConstants.PARAM_PBX_CLIENT_TOKEN, _token)
        );
    }


    @Override
    public void rechargeDTH(TransactionRequest<PaymentResponse> request) {
        if (TextUtils.isEmpty(request.getConsumerId())) {

            if (_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.RECHARGE_DTH);
            }
        }

        String url = AppConstants.URL_PBX_PLATFORM_APP;


        AsyncDataEx dataEx = new AsyncDataEx(this, request, url, Methods.RECHARGE_DTH);

        dataEx.execute(
                new BasicNameValuePair(AppConstants.PARAM_PBX_SERVICE, AppConstants.SVC_PBX_RECHARGE_DTH),
                new BasicNameValuePair(AppConstants.PARAM_PBX_RMN, _userName),
                new BasicNameValuePair(AppConstants.PARAM_PBX_CUSTOMER_NUMBER, request.getConsumerId()),
                new BasicNameValuePair(AppConstants.PARAM_PBX_AMOUNT, String.valueOf(Math.round(request.getAmount()))),
                new BasicNameValuePair(AppConstants.PARAM_PBX_OPERTAORSHORTCODE, request.getOperator().getCode()),
                new BasicNameValuePair(AppConstants.PARAM_PBX_IDENTIFIER, String.valueOf(request.getId())),
                new BasicNameValuePair(AppConstants.PARAM_PBX_ORIGIN_ID, _deviceRegId),
                new BasicNameValuePair(AppConstants.PARAM_PBX_CLIENT_TOKEN, _token)
        );
    }


    public void payBill(
            TransactionRequest<PaymentResponse> request,
            String psConsumerName,
            HashMap<String, String> pExtraParamsMap
    ) {
        String url = AppConstants.URL_PBX_PLATFORM_APP;

        AsyncDataEx dataEx = new AsyncDataEx(this, request, url, Methods.PAY_BILL);

        dataEx.execute(
                new BasicNameValuePair(AppConstants.PARAM_PBX_SERVICE, AppConstants.SVC_PBX_BILL_PAY),
                new BasicNameValuePair(AppConstants.PARAM_PBX_RMN, _userName),
                new BasicNameValuePair(AppConstants.PARAM_PBX_CUSTOMER_NUMBER, request.getCustomerMobile()),
                new BasicNameValuePair(AppConstants.PARAM_PBX_AMOUNT, String.valueOf(Math.round(request.getAmount()))),
                new BasicNameValuePair(AppConstants.PARAM_PBX_OPERTAORSHORTCODE, request.getOperator().getCode()),
                new BasicNameValuePair(AppConstants.PARAM_PBX_IDENTIFIER, String.valueOf(request.getId())),
                new BasicNameValuePair(AppConstants.PARAM_PBX_ORIGIN_ID, _deviceRegId),
                new BasicNameValuePair(AppConstants.PARAM_PBX_CLIENT_TOKEN, _token)
        );
    }


    public void utilityPayBill(
            TransactionRequest<PaymentResponse> request,
            String psConsumerName,
            HashMap<String, String> pExtraParamsMap
    ) {
        String url = AppConstants.URL_PBX_PLATFORM_APP;
        String operatorCode = request.getOperator().code;
        Log.e("operatorCode" ,operatorCode);
        AsyncDataEx dataEx = new AsyncDataEx(this, request, url, Methods.UTILITY_BILL_PAY);
        if (operatorCode.equals("CES") && pExtraParamsMap != null) {
            if (
                    !pExtraParamsMap.containsKey(AppConstants.PARAM_PBX_AC_MONTH) ||
                            !pExtraParamsMap.containsKey(AppConstants.PARAM_PBX_DUE_DATE) ||
                            !pExtraParamsMap.containsKey(AppConstants.PARAM_PBX_STUBTYPE)
                    ) {
                Log.d("NEW_PL_DATA", "Parameters not sent for CESC");
                if (_listener != null) {
                    _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.UTILITY_BILL_PAY);
                }
                return;
            }

        }
        dataEx.execute(
                new BasicNameValuePair(AppConstants.PARAM_PBX_SERVICE, AppConstants.SVC_PBX_UTILITY_BILL_PAY),
                new BasicNameValuePair(AppConstants.PARAM_PBX_RMN, _userName),
                new BasicNameValuePair(AppConstants.PARAM_PBX_CONSUMER_NUMBER_UBP, request.getCustomerMobile()),
                new BasicNameValuePair(AppConstants.PARAM_PBX_AMOUNT, String.valueOf(Math.round(request.getAmount()))),
                new BasicNameValuePair(AppConstants.PARAM_PBX_OPERTAORSHORTCODE, request.getOperator().getCode()),

                new BasicNameValuePair(AppConstants.PARAM_PBX_ACCOUNT_NUMBER,  request.getCustomerMobile()),
                new BasicNameValuePair(AppConstants.PARAM_PBX_AC_MONTH, pExtraParamsMap.get(AppConstants.PARAM_PBX_AC_MONTH)),
                new BasicNameValuePair(AppConstants.PARAM_PBX_DUE_DATE, pExtraParamsMap.get(AppConstants.PARAM_PBX_DUE_DATE)),
                new BasicNameValuePair(AppConstants.PARAM_PBX_STUBTYPE, pExtraParamsMap.get(AppConstants.PARAM_PBX_STUBTYPE)),


                new BasicNameValuePair(AppConstants.PARAM_PBX_IDENTIFIER, String.valueOf(request.getId())),
                new BasicNameValuePair(AppConstants.PARAM_PBX_ORIGIN_ID, _deviceRegId),
                new BasicNameValuePair(AppConstants.PARAM_PBX_CLIENT_TOKEN, _token)
        );
    }

    public TransactionRequest getPaymentTransactionResult(TransactionRequest<PaymentResponse> request) {

        if (TextUtils.isEmpty(request.getRemoteResponse())) {
            Log.e(_LOG, "Null remote response received");
            return null;
        }

        try {
            Gson gson = new GsonBuilder().create();

            Type type = new TypeToken<ResponseBase<String>>() {
            }.getType();
            ResponseBase<String> responseBase = gson.fromJson(request.getRemoteResponse(), type);

            if (responseBase == null) {
                Log.w(_LOG, "Null response?");
                return null;
            }

            Log.d(_LOG, "Response: " + responseBase.data);
            PaymentResponse response = new PaymentResponse();
            response.transactionId = responseBase.data;

            request.setCustom(response);

            return request;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return request;
    }



    @Override
    public void getBalance() {

        String url = AppConstants.URL_PBX_PLATFORM_APP;
        TransactionRequest request = new TransactionRequest();

        AsyncDataEx dataEx = new AsyncDataEx(this, request, url, Methods.GET_BALANCE);


        String userName = EphemeralStorage.getInstance(_applicationContext).getString(
                AppConstants.LOGGED_IN_USERNAME, null
        );

        dataEx.execute(
                new BasicNameValuePair(AppConstants.PARAM_PBX_SERVICE, AppConstants.SVC_PBX_CHECK_BALANCE),
                new BasicNameValuePair(AppConstants.PARAM_PBX_RMN, userName),
                new BasicNameValuePair(AppConstants.PARAM_PBX_IDENTIFIER, String.valueOf(request.getId())),
                new BasicNameValuePair(AppConstants.PARAM_PBX_ORIGIN_ID, _deviceRegId),
                new BasicNameValuePair(AppConstants.PARAM_PBX_CLIENT_TOKEN, _token)
        );
    }



    public float parseBalanceResult(TransactionRequest request) throws MOMException {
        if (TextUtils.isEmpty(request.getRemoteResponse())) {
            throw new MOMException();
        }
        try {
            Gson gson = new GsonBuilder().create();
            Type type = new TypeToken<ResponseBase<Float>>() {
            }.getType();
            ResponseBase<Float> responseBase = gson.fromJson(request.getRemoteResponse(), type);
           // Balance balance = gson.fromJson(request.getRemoteResponse(), Balance.class);
           // return balance.balance;
            return responseBase.data;
        } catch (JsonSyntaxException jse) {
            jse.printStackTrace();
            throw new MOMException();
        }


    }

    @Override
    public void login(String username, String password) {

        //String url				    = AppConstants.URL_PBX_PLATFORM_APPLIC;

        String url = AppConstants.URL_PBX_PLATFORM_APP;

        Log.i(_LOG, "Calling Async login");
        TransactionRequest request = new TransactionRequest();

        AsyncDataEx dataEx = new AsyncDataEx(new AsyncListener<TransactionRequest>() {
            @Override
            public void onTaskSuccess(TransactionRequest result, Methods callback) {
                boolean bSuccess = loginSuccessful(result);
                _listener.onTaskSuccess(bSuccess, callback);
            }

            @Override
            public void onTaskError(AsyncResult pResult, Methods callback) {
                _listener.onTaskError(pResult, callback);
            }
        },
                request,
                url,
                Methods.LOGIN
        );

        dataEx.execute(
                new BasicNameValuePair(AppConstants.PARAM_PBX_SERVICE, AppConstants.SVC_PBX_CHECK_LOGIN),
                new BasicNameValuePair(AppConstants.PARAM_PBX_USERNAME, username),
                new BasicNameValuePair(AppConstants.PARAM_PBX_PASSWORD, password),
                new BasicNameValuePair(AppConstants.PARAM_PBX_IDENTIFIER, String.valueOf(request.getId())),
                new BasicNameValuePair(AppConstants.PARAM_PBX_ORIGIN_ID, _deviceRegId)
        );
    }








    public boolean loginSuccessful(TransactionRequest pResult) {
        if (pResult == null || TextUtils.isEmpty(pResult.getRemoteResponse())) {
            return false;
        }

        boolean success = false;

        try {
            Gson gson = new GsonBuilder().create();

            Type type = new TypeToken<ResponseBase<LoginResult>>() {
            }.getType();

            ResponseBase<LoginResult> responseBase = gson.fromJson(pResult.getRemoteResponse(), type);

            LoginResult loginResult = responseBase.data;

            success = (responseBase != null && responseBase.code == 0);

            if (success && responseBase.data != null) {
                //success = false;
                success = true;
            }

            if (success) {

                EphemeralStorage.getInstance(_applicationContext).storeString(
                        AppConstants.PARAM_PBX_USERID,
                        responseBase.data.userID);

                EphemeralStorage.getInstance(_applicationContext).storeString(
                        AppConstants.PARAM_PBX_RMN,
                        responseBase.data.rmn);

                EphemeralStorage.getInstance(_applicationContext).storeString(
                        AppConstants.PARAM_PBX_NAME,
                        responseBase.data.name);

                EphemeralStorage.getInstance(_applicationContext).storeString(
                        AppConstants.PARAM_PBX_TOKEN,
                        responseBase.data.token);

                EphemeralStorage.getInstance(_applicationContext).storeString(
                        AppConstants.PARAM_PBX_USERTYPE,
                        responseBase.data.userType);

                EphemeralStorage.getInstance(_applicationContext).storeString(
                        AppConstants.PARAM_PBX_USERNAME,
                        responseBase.data.userName);

                Log.i("Username", responseBase.data.userName);
                EphemeralStorage.getInstance(_applicationContext).storeString(
                        AppConstants.PARAM_MERCHANTID_LIC,
                        responseBase.data.merchantId);


                EphemeralStorage.getInstance(_applicationContext).storeInt(
                        AppConstants.PARAM_IsLIC,
                        responseBase.data.isLic);
                Log.i("isLic", String.valueOf(responseBase.data.isLic));


            }

        } catch (JsonSyntaxException jse) {
            Log.e(_LOG, jse.getMessage());
            jse.printStackTrace();
        }
        return success;
    }


    public TransactionRequest getInternalBalTransfer(TransactionRequest request) {

        if (TextUtils.isEmpty(request.getRemoteResponse())) {
            Log.e(_LOG, "Null remote response received");
            return null;
        }

        try {
            Gson gson = new GsonBuilder().create();

            Type type = new TypeToken<ResponseBase<String>>() {
            }.getType();
            ResponseBase<String> responseBase = gson.fromJson(request.getRemoteResponse(), type);

            if (responseBase == null) {
                Log.w(_LOG, "Null response?");
                return null;
            }

            Log.d(_LOG, "Response: " + responseBase.data);

            request.setRemoteResponse(responseBase.data);

            return request;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return request;
    }


    @Override
    public void getBillAmount(TransactionRequest request) {
    }

    @Override
    public void getTransactionHistory() {
        String userName = EphemeralStorage.getInstance(_applicationContext).getString(
                AppConstants.PARAM_PBX_RMN, null
        );


        TransactionRequest request = new TransactionRequest();


        AsyncDataEx dataEx = new AsyncDataEx(

                this,
                request,
                AppConstants.URL_PBX_PLATFORM_APP,
                Methods.TRANSACTION_HISTORY
        );


        dataEx.execute(
                new BasicNameValuePair(AppConstants.PARAM_PBX_SERVICE, AppConstants.SVC_PBX_TRANSACTION_HISTORY),
                new BasicNameValuePair(AppConstants.PARAM_PBX_RMN, userName),
                new BasicNameValuePair(AppConstants.PARAM_PBX_IDENTIFIER, String.valueOf(request.getId())),
                new BasicNameValuePair(AppConstants.PARAM_PBX_ORIGIN_ID, _deviceRegId),
                new BasicNameValuePair(AppConstants.PARAM_PBX_TOKEN, _token),
                new BasicNameValuePair(AppConstants.PARAM_PBX_CLIENT_TOKEN, _token)
        );
    }

    public ArrayList<Transaction> extractTransactionshistory(TransactionRequest request) {
        Gson gson = new GsonBuilder().create();

        Type type = new TypeToken<ResponseBase<PBXTransaction[]>>() {
        }.getType();

        ResponseBase<PBXTransaction[]> responseBase = gson.fromJson(request.getRemoteResponse(), type);

        ArrayList<Transaction> list = new ArrayList<Transaction>();

        if (responseBase == null || responseBase.code != 0 || responseBase.data == null) {
            return list;
        }

        for (PBXTransaction transaction : responseBase.data) {
            list.add(transaction.getTransaction());
        }

        return list;
    }

    @Override
    public void changePassword(String psOldPin, String psNewPin) {
        Log.i(_LOG, "Calling Async password");

        String userName = EphemeralStorage.getInstance(_applicationContext).getString(
                AppConstants.LOGGED_IN_USERNAME, null
        );


        AsyncDataEx dataEx = new AsyncDataEx(
                this,
                new TransactionRequest(),
                AppConstants.URL_PBX_PLATFORM_APP,
                Methods.CHANGE_PASSWORD
        );


        dataEx.execute(
                new BasicNameValuePair(AppConstants.PARAM_PBX_SERVICE, AppConstants.SVC_PBX_CHANGE_PASSWORD),
                new BasicNameValuePair(AppConstants.PARAM_PBX_NP, psNewPin),
                new BasicNameValuePair(AppConstants.PARAM_PBX_OP, psOldPin),
                new BasicNameValuePair(AppConstants.PARAM_PBX_RMN, userName),
                new BasicNameValuePair(AppConstants.PARAM_PBX_IDENTIFIER, MiscUtils.getRandomLongAsString()),
                new BasicNameValuePair(AppConstants.PARAM_PBX_ORIGIN_ID, _deviceRegId),
                new BasicNameValuePair(AppConstants.PARAM_PBX_TOKEN, _token),
                new BasicNameValuePair(AppConstants.PARAM_PBX_CLIENT_TOKEN, _token)

        );
    }

    private TransactionRequest getChangePasswordResult(TransactionRequest request) {

        if (TextUtils.isEmpty(request.getRemoteResponse())) {
            request.setStatus(TransactionRequest.RequestStatus.FAILED);
            return request;
        }


        try {
            Gson gson = new GsonBuilder().create();

            Type type = new TypeToken<ResponseBase<ChangePBXPassword>>() {
            }.getType();

            ResponseBase<ChangePBXPassword> responseBase = gson.fromJson(request.getRemoteResponse(), type);

            request.setResponseCode(responseBase.code);
            request.setRemoteResponse(responseBase.data.message);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return request;
    }

    public void getOperatorNames(PBXOperatorDataType type) {
        _opType = type;


        AsyncDataEx dataEx = new AsyncDataEx(
                this,
                new TransactionRequest(),
                AppConstants.URL_PBX_PLATFORM_APP,
                Methods.GET_OPERATOR_NAMES
        );

        dataEx.execute(
                new BasicNameValuePair(AppConstants.PARAM_PBX_SERVICE, AppConstants.SVC_PBX_OPERATOR_NAMES),
                new BasicNameValuePair(AppConstants.PARAM_OPERATOR_TYPE, String.valueOf(type.code)),
                new BasicNameValuePair(AppConstants.PARAM_PBX_RMN, _userName),
                new BasicNameValuePair(AppConstants.PARAM_PBX_IDENTIFIER, MiscUtils.getRandomLongAsString()),
                new BasicNameValuePair(AppConstants.PARAM_PBX_ORIGIN_ID, _deviceRegId),
                new BasicNameValuePair(AppConstants.PARAM_PBX_CLIENT_TOKEN, _token)
        );
    }

    public List<Operator> getOperatorNamesResult(TransactionRequest request) {
        Gson gson = new GsonBuilder().create();

        Type type = new TypeToken<ResponseBase<Operator[]>>() {
        }.getType();

        ResponseBase<Operator[]> responseBase = gson.fromJson(request.getRemoteResponse(), type);

        if (responseBase == null || responseBase.data == null) {
            return null;
        }

        List<Operator> operators = Arrays.asList(responseBase.data);

        EphemeralStorage.getInstance(_applicationContext).storeObject(
                AppConstants.PARAM_PBX_OPERATORS + _opType.code, operators
        );

        return operators;
    }

    public void balanceTransfer(TransactionRequest request, String payTo) {
        if (
                TextUtils.isEmpty(payTo) || request.getAmount() < 1) {


            // _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.BALANCE_TRANSFER_PBX);

            if (_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.BALANCE_TRANSFER);

            }
            return;
        }

        String userName = EphemeralStorage.getInstance(_applicationContext).getString(
                AppConstants.PARAM_PBX_RMN, null
        );


        AsyncDataEx dataEx = new AsyncDataEx(
                this,
                new TransactionRequest(),
                AppConstants.URL_PBX_PLATFORM_APP,
                Methods.BALANCE_TRANSFER
        );

        dataEx.execute(
                new BasicNameValuePair(AppConstants.PARAM_PBX_SERVICE, AppConstants.SVC_PBX_INTERNAL_BAL_TRANSFER),
                new BasicNameValuePair(AppConstants.PARAM_PBX_PARENT_RMN, userName),
                new BasicNameValuePair(AppConstants.PARAM_PBX_CHILD_RMN, payTo),
                new BasicNameValuePair(AppConstants.PARAM_PBX_AMOUNT, String.valueOf(Math.round(request.getAmount()))),
                new BasicNameValuePair(AppConstants.PARAM_PBX_IDENTIFIER, MiscUtils.getRandomLongAsString()),
                new BasicNameValuePair(AppConstants.PARAM_PBX_ORIGIN_ID, _deviceRegId),
                new BasicNameValuePair(AppConstants.PARAM_PBX_CLIENT_TOKEN, _token)
        );
    }

    public void changePin(PinType pinType, String psOldPin, String psNewPin) {

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

    public String getPlaceHolder(String response) {

        Log.i("Lic", response);

        if (TextUtils.isEmpty(response)) {
            return null;
        }

        try {
            Gson gson = new GsonBuilder().create();

            Type type = new TypeToken<ResponseBase<LicResponse>>() {
            }.getType();

            ResponseBase<LicResponse> responseBase = gson.fromJson(response, type);

            LicResponse licResponse = responseBase.data;
            LicLife txLife = licResponse.TXLife;


            Log.i("PlaceHolder", txLife.getTXLifeResponse().getOLife().getParty().getFullName().toString());
            return (txLife.getTXLifeResponse().getOLife().getParty().getFullName().toString());


        } catch (JsonSyntaxException jse) {
            Log.e(_LOG, jse.getMessage());
        }

        return null;
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


    public void registerIMPSCustomer(TransactionRequest<ImpsCustomerRegistrationResult> request) {
        if (request == null || TextUtils.isEmpty(request.getConsumerId())) {
            if (_listener != null) {

                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.IMPS_CUSTOMER_REGISTRATION);
            }
            return;
        }

        String Url = AppConstants.URL_PBX_PLATFORM_IMPS + AppConstants.SVC_NEW_METHOD_IMPS_CUSTOMER_REGISTRATION;

        AsyncDataEx dataEx = new AsyncDataEx(this, new TransactionRequest(), Url, Methods.IMPS_CUSTOMER_REGISTRATION);

        dataEx.execute(


                new BasicNameValuePair(AppConstants.PARAM_PBX_SERVICE, AppConstants.PARAM_SERVICE_IMPS_CUSTOMER_STATUS),
                new BasicNameValuePair(AppConstants.PARAM_PBX_CONSUMER_NUMBER_STATUS, request.getConsumerId()),
                new BasicNameValuePair(AppConstants.PARAM_PBX_ROWID, EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_PBX_USERID, null)),
                new BasicNameValuePair(AppConstants.PARAM_PBX_RMN, EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.LOGGED_IN_USERNAME, null))
        );
    }

    public void impsCustomerRegistration(TransactionRequest<ImpsCreateCustomerResult> request) {
        if (TextUtils.isEmpty(request.getConsumerId())) {
            if (_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.IMPS_CREATE_CUSTOMER_REGISTRATION);
            }
        }

        String Url = AppConstants.URL_PBX_PLATFORM_IMPS + AppConstants.SVC_NEW_METHOD_IMPS_CUSTOMER_REGISTRATION;

        AsyncDataEx dataEx = new AsyncDataEx(this, new TransactionRequest(), Url, Methods.IMPS_CREATE_CUSTOMER_REGISTRATION);

        dataEx.execute(

                new BasicNameValuePair(AppConstants.PARAM_PBX_SERVICE, AppConstants.PARAM_SERVICE_IMPS_CUSTOMER_REGISTRATION),
                new BasicNameValuePair(AppConstants.PARAM_PBX_CONSUMER_NUMBER, request.getConsumerNumber()),
                new BasicNameValuePair(AppConstants.PARAM_PBX_CONSUMER_NAME, request.getConsumerName()),
                new BasicNameValuePair(AppConstants.PARAM_PBX_CONSUMER_DOB, request.getConsumerDOB()),
                new BasicNameValuePair(AppConstants.PARAM_PBX_CONSUMER_EMAIL, request.getConsumerEmailAddress()),
                new BasicNameValuePair(AppConstants.PARAM_PBX_ROWID, EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_PBX_USERID, null)),
                new BasicNameValuePair(AppConstants.PARAM_PBX_RMN, EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.LOGGED_IN_USERNAME, null))
        );
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

            Log.d(_LOG, "Response: " + responseBase.data);
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



    public void impsAuthentication(TransactionRequest<ImpsAuthenticationResult> request) {
        if (TextUtils.isEmpty(request.getConsumerId())) {
            if (_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.IMPS_AUTHENTICATION);
            }
        }

        String Url = AppConstants.URL_PBX_PLATFORM_IMPS + AppConstants.SVC_NEW_METHOD_IMPS_CUSTOMER_REGISTRATION;

        AsyncDataEx dataEx = new AsyncDataEx(this, new TransactionRequest(), Url, Methods.IMPS_AUTHENTICATION);

        dataEx.execute(

                new BasicNameValuePair(AppConstants.PARAM_PBX_SERVICE, AppConstants.PARAM_SERVICE_IMPS_AUTHENTICATION),
                new BasicNameValuePair(AppConstants.PARAM_PBX_CLIENT_TYPE, AppConstants.PARAM_IMPS_CLIENT_TYPE),
                new BasicNameValuePair(AppConstants.PARAM_PBX_ROWID, EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_PBX_USERID, null)),
                new BasicNameValuePair(AppConstants.PARAM_PBX_RMN, EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.LOGGED_IN_USERNAME, null))
        );
    }


    public TransactionRequest getIMPSAuthenticationResponse(TransactionRequest<ImpsAuthenticationResult> request) {
        if (TextUtils.isEmpty(request.getRemoteResponse())) {
            Log.e(_LOG, "Null remote response received");
            return null;
        }

        try {
            Gson gson = new GsonBuilder().create();

            Type type = new TypeToken<ResponseBase<ImpsAuthenticationResult>>() {
            }.getType();

            ResponseBase<ImpsAuthenticationResult> responseBase = gson.fromJson(request.getRemoteResponse(), type);

            if (responseBase == null) {
                Log.w(_LOG, "Null response?");
                return null;
            }

            Log.d(_LOG, "Response: " + responseBase.data);
            ImpsAuthenticationResult response = responseBase.data;


            request.setCustom(response);


            EphemeralStorage.getInstance(_applicationContext).storeBoolean(
                    AppConstants.PARAM_PBX_IMPS_SERVICEALLOWED,
                    responseBase.data.isIMPSServiceAllowed);
            Log.i("ServiceAllowed", String.valueOf(responseBase.data.isIMPSServiceAllowed));

            EphemeralStorage.getInstance(_applicationContext).storeBoolean(
                    AppConstants.PARAM_PBX_IMPS_ISREGISTERED,
                    responseBase.data.isIMPSActive);
            Log.i("IsIMPSActive", String.valueOf(responseBase.data.isIMPSActive));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return request;
    }



    public void impsCheckKYC(TransactionRequest<ImpsCheckKYCResult> request , String sConsumerNumber) {
        if (TextUtils.isEmpty(sConsumerNumber)) {
            if (_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.IMPS_CHECK_KYC);
            }
        }

        String Url = AppConstants.URL_PBX_PLATFORM_IMPS + AppConstants.SVC_NEW_METHOD_IMPS_CUSTOMER_REGISTRATION;

        AsyncDataEx dataEx = new AsyncDataEx(this, new TransactionRequest(), Url, Methods.IMPS_CHECK_KYC);

        dataEx.execute(

                new BasicNameValuePair(AppConstants.PARAM_PBX_SERVICE, AppConstants.PARAM_SERVICE_IMPS_CHECK_KYC),
               // new BasicNameValuePair(AppConstants.PARAM_PBX_IMPS_KYC_CUSTOMER_ID, String.valueOf(EphemeralStorage.getInstance(_applicationContext).getInt(AppConstants.PARAM_PBX_IMPS_CUSTOMER_ID, -1))),
                new BasicNameValuePair(AppConstants.PARAM_PBX_IMPS_CUSTOMER_ID, String.valueOf(EphemeralStorage.getInstance(_applicationContext).getInt(AppConstants.PARAM_PBX_IMPS_CUSTOMER_ID, -1))),
                new BasicNameValuePair(AppConstants.PARAM_PBX_IMPS_CONSUMER_NUMBER, sConsumerNumber),
                new BasicNameValuePair(AppConstants.PARAM_PBX_ROWID, EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_PBX_USERID, null)),
                new BasicNameValuePair(AppConstants.PARAM_PBX_RMN, EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.LOGGED_IN_USERNAME, null))
        );
    }


    public TransactionRequest getIMPSCheckKYCResponse(TransactionRequest<ImpsCheckKYCResult> request) {
        if (TextUtils.isEmpty(request.getRemoteResponse())) {
            Log.e(_LOG, "Null remote response received");
            return null;
        }

        try {
            Gson gson = new GsonBuilder().create();

            Type type = new TypeToken<ResponseBase<ImpsCheckKYCResult>>() {
            }.getType();

            ResponseBase<ImpsCheckKYCResult> responseBase = gson.fromJson(request.getRemoteResponse(), type);

            if (responseBase == null) {
                Log.w(_LOG, "Null response?");
                return null;
            }

            Log.d(_LOG, "Response: " + responseBase.data);
            ImpsCheckKYCResult impsCheckKYCResult = responseBase.data;


            request.setCustom(impsCheckKYCResult);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return request;
    }


    public TransactionRequest getIMPSCreateCustomerResponse(TransactionRequest<ImpsCreateCustomerResult> request) {
        if (TextUtils.isEmpty(request.getRemoteResponse())) {
            Log.e(_LOG, "Null remote response received");
            return null;
        }

        try {
            Gson gson = new GsonBuilder().create();

            Type type = new TypeToken<ResponseBase<ImpsCreateCustomerResult>>() {
            }.getType();

            ResponseBase<ImpsCreateCustomerResult> responseBase = gson.fromJson(request.getRemoteResponse(), type);

            if (responseBase == null) {
                Log.w(_LOG, "Null response?");
                return null;
            }

            Log.d(_LOG, "Response: " + responseBase.data);
            ImpsCreateCustomerResult response = responseBase.data;


            request.setCustom(response);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return request;
    }


    public void getIMPSBeneficiaryList(TransactionRequest<List<BeneficiaryResult>> request) {
        if (request == null || TextUtils.isEmpty(request.getConsumerId())) {
            if (_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.IMPS_CUSTOMER_REGISTRATION);
            }
            return;
        }

        String url = AppConstants.URL_PBX_PLATFORM_IMPS + AppConstants.SVC_NEW_METHOD_IMPS_CUSTOMER_REGISTRATION;

        AsyncDataEx dataEx = new AsyncDataEx(this, new TransactionRequest(), url, Methods.IMPS_BENEFICIARY_LIST);

        dataEx.execute(
                new BasicNameValuePair(AppConstants.PARAM_PBX_SERVICE, AppConstants.PARAM_SERVICE_IMPS_BENEFICIARY_LIST),
                new BasicNameValuePair(AppConstants.PARAM_PBX_IMPS_CUSTOMER_ID, String.valueOf(EphemeralStorage.getInstance(_applicationContext).getInt(AppConstants.PARAM_PBX_IMPS_CUSTOMER_ID, -1))),
                new BasicNameValuePair(AppConstants.PARAM_PBX_ROWID, EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_PBX_USERID , null)),
              //  new BasicNameValuePair(AppConstants.PARAM_PBX_RMN, request.getConsumerId())
                new BasicNameValuePair(AppConstants.PARAM_PBX_RMN,  EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.LOGGED_IN_USERNAME, null))
        );
    }

    public TransactionRequest getIMPSBeneficiaryListResponse(TransactionRequest<List<BeneficiaryResult>> request) {
        Gson gson = new GsonBuilder().create();

        Type type = new TypeToken<ResponseBase<BeneficiaryResult[]>>() {
        }.getType();

        ResponseBase<BeneficiaryResult[]> responseBase = gson.fromJson(request.getRemoteResponse(), type);

        if (responseBase == null || responseBase.data == null) {
            return null;
        }

      //actual  List<BeneficiaryResult> beneficiaryList = Arrays.asList(responseBase.data);
        List<BeneficiaryResult> beneficiaryList = new ArrayList<BeneficiaryResult>(Arrays.asList(responseBase.data));

        request.setCustom(beneficiaryList);

        return request;
    }

    public void impsAddBeneficiary(TransactionRequest<ImpsAddBeneficiaryResult> request) {
        if (request == null ) {
            if (_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.IMPS_ADD_BENEFICIARY);
            }
            return;
        }

        String url = AppConstants.URL_PBX_PLATFORM_IMPS + AppConstants.SVC_NEW_METHOD_IMPS_CUSTOMER_REGISTRATION;

        AsyncDataEx dataEx = new AsyncDataEx(this, new TransactionRequest(), url, Methods.IMPS_ADD_BENEFICIARY);
// CHANGE PARAMS
        dataEx.execute(
                new BasicNameValuePair(AppConstants.PARAM_PBX_SERVICE, AppConstants.PARAM_SERVICE_IMPS_ADD_BENEFICIARY),
                new BasicNameValuePair(AppConstants.PARAM_PBX_IMPS_CUSTOMER_ID, String.valueOf(EphemeralStorage.getInstance(_applicationContext).getInt(AppConstants.PARAM_PBX_IMPS_CUSTOMER_ID, -1))),
                new BasicNameValuePair(AppConstants.PARAM_PBX_IMPS_IFSC_CODE, request.getIfscCode()),
                new BasicNameValuePair(AppConstants.PARAM_PBX_IMPS_ACCOUNT_NUMBER,request.getAccountNumber()),
                new BasicNameValuePair(AppConstants.PARAM_PBX_IMPS_BENEFICIARY_NAME, request.getBeneficiaryName()),
                new BasicNameValuePair(AppConstants.PARAM_PBX_IMPS_BENEFICIARY_MOBILE_NUMBER, request.getBeneficiaryMobNo()),
                new BasicNameValuePair(AppConstants.PARAM_PBX_ROWID, EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_PBX_USERID , null)),
                new BasicNameValuePair(AppConstants.PARAM_PBX_RMN, EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.LOGGED_IN_USERNAME, null))
        );
    }

   public TransactionRequest getIMPSAddBeneficiaryResponse(TransactionRequest<ImpsAddBeneficiaryResult> request) {
        if (TextUtils.isEmpty(request.getRemoteResponse())) {
            Log.e(_LOG, "Null remote response received");
            return null;
        }

        try {
            Gson gson = new GsonBuilder().create();

            Type type = new TypeToken<ResponseBase<ImpsAddBeneficiaryResult>>() {
            }.getType();

            ResponseBase<ImpsAddBeneficiaryResult> responseBase = gson.fromJson(request.getRemoteResponse(), type);

            if (responseBase == null) {
                Log.w(_LOG, "Null response?");
                return null;
            }

            Log.d(_LOG, "Response: " + responseBase.data);
            ImpsAddBeneficiaryResult response = responseBase.data;
            request.setCustom(response);

            } catch (Exception e) {
            e.printStackTrace();
        }

        return request;
    }


    public void impsBeneficiaryDetails(TransactionRequest<ImpsBeneficiaryDetailsResult> request , String sBeneficiaryName) {
        if (request == null || TextUtils.isEmpty(sBeneficiaryName)) {
            if (_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.IMPS_BENEFICIARY_DETAILS);
            }
            return;
        }

        String url = AppConstants.URL_PBX_PLATFORM_IMPS + AppConstants.SVC_NEW_METHOD_IMPS_CUSTOMER_REGISTRATION;

        AsyncDataEx dataEx = new AsyncDataEx(this, new TransactionRequest(), url, Methods.IMPS_BENEFICIARY_DETAILS);
// PARAMS
        dataEx.execute(
                new BasicNameValuePair(AppConstants.PARAM_PBX_SERVICE, AppConstants.PARAM_SERVICE_IMPS_BENEFICIARY_DETAILS),
                new BasicNameValuePair(AppConstants.PARAM_PBX_IMPS_BENEFICIARY_ACCOUNT_NAME,sBeneficiaryName),
                new BasicNameValuePair(AppConstants.PARAM_PBX_IMPS_BENEFICIARY_CUSTOMER_ID, String.valueOf(EphemeralStorage.getInstance(_applicationContext).getInt(AppConstants.PARAM_PBX_IMPS_CUSTOMER_ID, -1))),
                new BasicNameValuePair(AppConstants.PARAM_PBX_ROWID, EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_PBX_USERID, null)),
                new BasicNameValuePair(AppConstants.PARAM_PBX_RMN, EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.LOGGED_IN_USERNAME, null))


        );
    }

    public TransactionRequest getIMPSBeneficiaryDetailsResponse(TransactionRequest<ImpsBeneficiaryDetailsResult> request) {
        if (TextUtils.isEmpty(request.getRemoteResponse())) {
            Log.e(_LOG, "Null remote response received");
            return null;
        }

        try {
            Gson gson = new GsonBuilder().create();

            Type type = new TypeToken<ResponseBase<ImpsBeneficiaryDetailsResult>>() {
            }.getType();

            ResponseBase<ImpsBeneficiaryDetailsResult> responseBase = gson.fromJson(request.getRemoteResponse(), type);

            if (responseBase == null) {
                Log.w(_LOG, "Null response?");
                return null;
            }

            Log.d(_LOG, "Response: " + responseBase.data);
            ImpsBeneficiaryDetailsResult response = responseBase.data;
            request.setCustom(response);

            EphemeralStorage.getInstance(_applicationContext).storeBoolean(
                    AppConstants.PARAM_PBX_IMPS_ISBENEFICIAYSTATUS,
                    responseBase.data.IsBeneficiaryVerified);
            Log.i("BeneficiaryStatus", String.valueOf(responseBase.data.IsBeneficiaryVerified));


        } catch (Exception e) {
            e.printStackTrace();
        }

        return request;
    }

    public void getIMPSBankName(TransactionRequest<List<BankNameResult>> request) {
        if (request == null ) {
            if (_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.IMPS_BANK_NAME_LIST);
            }
            return;
        }

        String url = AppConstants.URL_PBX_PLATFORM_IMPS + AppConstants.SVC_NEW_METHOD_IMPS_CUSTOMER_REGISTRATION;

        AsyncDataEx dataEx = new AsyncDataEx(this, new TransactionRequest(), url, Methods.IMPS_BANK_NAME_LIST);

        dataEx.execute(
                new BasicNameValuePair(AppConstants.PARAM_PBX_SERVICE, AppConstants.PARAM_SERVICE_IMPS_BANK_NAME),
                new BasicNameValuePair(AppConstants.PARAM_PBX_ROWID, EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_PBX_USERID , null)),
                new BasicNameValuePair(AppConstants.PARAM_PBX_RMN, EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.LOGGED_IN_USERNAME, null))
        );
    }

    public TransactionRequest getIMPSBankListResponse(TransactionRequest<List<BankNameResult>> request) {
        Gson gson = new GsonBuilder().create();

        Type type = new TypeToken<ResponseBase<BankNameResult[]>>() {
        }.getType();

        ResponseBase<BankNameResult[]> responseBase = gson.fromJson(request.getRemoteResponse(), type);

        if (responseBase == null || responseBase.data == null) {
            return null;
        }

        List<BankNameResult> bankNameResultList = Arrays.asList(responseBase.data);

       // List<BankNameResult> bankNameResultList = new ArrayList<BankNameResult>(Arrays.asList(responseBase.data));
        request.setCustom(bankNameResultList);

        return request;
    }

    public void getIMPSStateName(TransactionRequest<List<StateNameResult>> request , String sBankName) {
        if (request == null ) {
            if (_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.IMPS_STATE_NAME);
            }
            return;
        }

        String url = AppConstants.URL_PBX_PLATFORM_IMPS + AppConstants.SVC_NEW_METHOD_IMPS_CUSTOMER_REGISTRATION;

        AsyncDataEx dataEx = new AsyncDataEx(this, new TransactionRequest(), url, Methods.IMPS_STATE_NAME);

        dataEx.execute(
                new BasicNameValuePair(AppConstants.PARAM_PBX_SERVICE, AppConstants.PARAM_SERVICE_IMPS_STATE_NAME),
                new BasicNameValuePair(AppConstants.PARAM_PBX_IMPS_BANK_NAME, sBankName),
                new BasicNameValuePair(AppConstants.PARAM_PBX_ROWID, EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_PBX_USERID , null)),
                new BasicNameValuePair(AppConstants.PARAM_PBX_RMN, EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.LOGGED_IN_USERNAME, null))
        );
    }

    public TransactionRequest getIMPSStateListResponse(TransactionRequest<List<StateNameResult>> request) {
        Gson gson = new GsonBuilder().create();

        Type type = new TypeToken<ResponseBase<StateNameResult[]>>() {
        }.getType();

        ResponseBase<StateNameResult[]> responseBase = gson.fromJson(request.getRemoteResponse(), type);

        if (responseBase == null || responseBase.data == null) {
            return null;
        }

        List<StateNameResult> stateNameResultList = Arrays.asList(responseBase.data);
       // List<StateNameResult> stateNameResultList = new ArrayList<StateNameResult>(Arrays.asList(responseBase.data));
        request.setCustom(stateNameResultList);

        return request;
    }


    public void getIMPSCityName(TransactionRequest<List<CityNameResult>> request , String sBankName , String sStateName) {
        if (request == null ) {
            if (_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.IMPS_CITY_NAME);
            }
            return;
        }

        String url = AppConstants.URL_PBX_PLATFORM_IMPS + AppConstants.SVC_NEW_METHOD_IMPS_CUSTOMER_REGISTRATION;

        AsyncDataEx dataEx = new AsyncDataEx(this, new TransactionRequest(), url, Methods.IMPS_CITY_NAME);

        dataEx.execute(
                new BasicNameValuePair(AppConstants.PARAM_PBX_SERVICE, AppConstants.PARAM_SERVICE_IMPS_CITY_NAME),
                new BasicNameValuePair(AppConstants.PARAM_PBX_IMPS_BANK_NAME, sBankName),
                new BasicNameValuePair(AppConstants.PARAM_PBX_IMPS_STATE_NAME, sStateName),
                new BasicNameValuePair(AppConstants.PARAM_PBX_ROWID, EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_PBX_USERID , null)),
                new BasicNameValuePair(AppConstants.PARAM_PBX_RMN, EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.LOGGED_IN_USERNAME, null))
        );
    }

    public TransactionRequest getIMPSCityListResponse(TransactionRequest<List<CityNameResult>> request) {
        Gson gson = new GsonBuilder().create();

        Type type = new TypeToken<ResponseBase<CityNameResult[]>>() {
        }.getType();

        ResponseBase<CityNameResult[]> responseBase = gson.fromJson(request.getRemoteResponse(), type);

        if (responseBase == null || responseBase.data == null) {
            return null;
        }

        List<CityNameResult> cityNameResultList = Arrays.asList(responseBase.data);
       // List<CityNameResult> cityNameResultList = new ArrayList<CityNameResult>(Arrays.asList(responseBase.data));
        request.setCustom(cityNameResultList);


        return request;
    }

    public void getIMPSBranchName(TransactionRequest<List<BranchNameResult>> request , String sBankName , String sStateName ,String sCityName) {
        if (request == null ) {
            if (_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.IMPS_BRANCH_NAME);
            }
            return;
        }

        String url = AppConstants.URL_PBX_PLATFORM_IMPS + AppConstants.SVC_NEW_METHOD_IMPS_CUSTOMER_REGISTRATION;

        AsyncDataEx dataEx = new AsyncDataEx(this, new TransactionRequest(), url, Methods.IMPS_BRANCH_NAME);

        dataEx.execute(
                new BasicNameValuePair(AppConstants.PARAM_PBX_SERVICE, AppConstants.PARAM_SERVICE_IMPS_BRANCH_NAME),
                new BasicNameValuePair(AppConstants.PARAM_PBX_IMPS_BANK_NAME, sBankName),
                new BasicNameValuePair(AppConstants.PARAM_PBX_IMPS_STATE_NAME, sStateName),
                new BasicNameValuePair(AppConstants.PARAM_PBX_IMPS_CITY_NAME, sCityName),
                new BasicNameValuePair(AppConstants.PARAM_PBX_ROWID, EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_PBX_USERID , null)),
                new BasicNameValuePair(AppConstants.PARAM_PBX_RMN, EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.LOGGED_IN_USERNAME, null))
        );
    }

    public TransactionRequest getIMPSBranchListResponse(TransactionRequest<List<BranchNameResult>> request) {
        Gson gson = new GsonBuilder().create();

        Type type = new TypeToken<ResponseBase<BranchNameResult[]>>() {
        }.getType();

        ResponseBase<BranchNameResult[]> responseBase = gson.fromJson(request.getRemoteResponse(), type);

        if (responseBase == null || responseBase.data == null) {
            return null;
        }

        List<BranchNameResult> branchNameResultList = Arrays.asList(responseBase.data);
        //List<BranchNameResult> branchNameResultList = new ArrayList<BranchNameResult>(Arrays.asList(responseBase.data));
        request.setCustom(branchNameResultList);


        return request;
    }

    public void impsVerifyProcess(TransactionRequest<ImpsVerifyProcessResult> request) {
        if (request == null) {
            if (_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.IMPS_VERIFY_PROCESS);
            }
        }

        String Url = AppConstants.URL_PBX_PLATFORM_IMPS + AppConstants.SVC_NEW_METHOD_IMPS_CUSTOMER_REGISTRATION;

        AsyncDataEx dataEx = new AsyncDataEx(this, new TransactionRequest(), Url, Methods.IMPS_VERIFY_PROCESS);

        dataEx.execute(

                new BasicNameValuePair(AppConstants.PARAM_PBX_SERVICE, AppConstants.PARAM_SERVICE_IMPS_VERIFY_PROCESS),
                new BasicNameValuePair(AppConstants.PARAM_PBX_IMPS_BENEFICIARY_ID, EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_PBX_IMPS_BENEFICIARY_ID,null)),
                new BasicNameValuePair(AppConstants.PARAM_PBX_IMPS_BENEFICIARY_CUSTOMER_ID, String.valueOf(EphemeralStorage.getInstance(_applicationContext).getInt(AppConstants.PARAM_PBX_IMPS_CUSTOMER_ID, -1))),
                new BasicNameValuePair(AppConstants.PARAM_PBX_IMPS_TRANSACTION_NARRATION, AppConstants.PARAM_PBX_IMPS_TRANSACTION_NARRATION_DESC),
                new BasicNameValuePair(AppConstants.PARAM_PBX_ROWID, EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_PBX_USERID, null)),
                new BasicNameValuePair(AppConstants.PARAM_PBX_RMN, EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.LOGGED_IN_USERNAME, null))
        );
    }


    public TransactionRequest getIMPSVerifyProcessResponse(TransactionRequest<ImpsVerifyProcessResult> request) {
        if (TextUtils.isEmpty(request.getRemoteResponse())) {
            Log.e(_LOG, "Null remote response received");
            return null;
        }

        try {
            Gson gson = new GsonBuilder().create();

            Type type = new TypeToken<ResponseBase<ImpsVerifyProcessResult>>() {
            }.getType();

            ResponseBase<ImpsVerifyProcessResult> responseBase = gson.fromJson(request.getRemoteResponse(), type);

            if (responseBase == null) {
                Log.w(_LOG, "Null response?");
                return null;
            }

            Log.d(_LOG, "Response: " + responseBase.data);
            ImpsVerifyProcessResult impsVerifyProcessResult = responseBase.data;


            request.setCustom(impsVerifyProcessResult);



            EphemeralStorage.getInstance(_applicationContext).storeBoolean(
                    AppConstants.PARAM_PBX_IMPS_POSTINGSTATUS,
                    responseBase.data.PostingStatus);
            Log.i("PostingStatus", String.valueOf(responseBase.data.PostingStatus));

            EphemeralStorage.getInstance(_applicationContext).storeString(
                    AppConstants.PARAM_PBX_IMPS_SESSIONID,
                    responseBase.data.SessionID);
            Log.i("SessionID", String.valueOf(responseBase.data.SessionID));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return request;
    }

    public void impsVerifyPayment(TransactionRequest<ImpsVerifyPaymentResult> request , String sOTP ,
                               String sAccountNumber ,String sIFSCCode , String sCustomerNumber) {
        if (request == null) {
            if (_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.IMPS_VERIFY_PAYMENT);
            }
        }

        String Url = AppConstants.URL_PBX_PLATFORM_IMPS + AppConstants.SVC_NEW_METHOD_IMPS_CUSTOMER_REGISTRATION;

        AsyncDataEx dataEx = new AsyncDataEx(this, new TransactionRequest(), Url, Methods.IMPS_VERIFY_PAYMENT);

        dataEx.execute(

                new BasicNameValuePair(AppConstants.PARAM_PBX_SERVICE, AppConstants.PARAM_SERVICE_IMPS_VERIFY_PAYMENT),
                new BasicNameValuePair(AppConstants.PARAM_PBX_IMPS_ACCOUNT_NAME, EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_PBX_IMPS_BENEFICIARY_ACCOUNT_NAME,null)),
                new BasicNameValuePair(AppConstants.PARAM_PBX_IMPS_ACCOUNT_NUMBER , sAccountNumber),
                new BasicNameValuePair(AppConstants.PARAM_PBX_IMPS_VERIFY_IFSC_CODE, sIFSCCode),
                new BasicNameValuePair(AppConstants.PARAM_PBX_IMPS_CUSTOMER_NUMBER,sCustomerNumber),
                new BasicNameValuePair(AppConstants.PARAM_PBX_IMPS_BENEFICIARY_ID, EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_PBX_IMPS_BENEFICIARY_ID,null)),
                new BasicNameValuePair(AppConstants.PARAM_PBX_IMPS_BENEFICIARY_CUSTOMER_ID, String.valueOf(EphemeralStorage.getInstance(_applicationContext).getInt(AppConstants.PARAM_PBX_IMPS_CUSTOMER_ID, -1))),
                new BasicNameValuePair(AppConstants.PARAM_PBX_IMPS_OTP, sOTP),
                new BasicNameValuePair(AppConstants.PARAMS_PBX_IMPS_SESSION_ID ,EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_PBX_IMPS_SESSIONID , null)),
                new BasicNameValuePair(AppConstants.PARAM_PBX_ROWID, EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_PBX_USERID, null)),
                new BasicNameValuePair(AppConstants.PARAM_PBX_RMN, EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.LOGGED_IN_USERNAME, null))
        );
    }


    public TransactionRequest getIMPSVerifyPaymentResponse(TransactionRequest<ImpsVerifyPaymentResult> request) {
        if (TextUtils.isEmpty(request.getRemoteResponse())) {
            Log.e(_LOG, "Null remote response received");
            return null;
        }

        try {
            Gson gson = new GsonBuilder().create();

            Type type = new TypeToken<ResponseBase<ImpsVerifyPaymentResult>>() {
            }.getType();

            ResponseBase<ImpsVerifyPaymentResult> responseBase = gson.fromJson(request.getRemoteResponse(), type);

            if (responseBase == null) {
                Log.w(_LOG, "Null response?");
                return null;
            }
            if(responseBase.code== -1){
                request.setResponseCode(-1);
                return request;
            }

            Log.d(_LOG, "Response: " + responseBase.data);
            ImpsVerifyPaymentResult impsVerifyPaymentResult = responseBase.data;


            request.setCustom(impsVerifyPaymentResult);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return request;
    }

    // PAYMENT PROCESS


    public void impsPaymentProcess(TransactionRequest<ImpsPaymentProcessResult> request , String sAmount ,String sTxnDescription) {
        if (request == null) {
            if (_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.IMPS_PAYMENT_PROCESS);
            }
        }

        String Url = AppConstants.URL_PBX_PLATFORM_IMPS + AppConstants.SVC_NEW_METHOD_IMPS_CUSTOMER_REGISTRATION;

        AsyncDataEx dataEx = new AsyncDataEx(this, new TransactionRequest(), Url, Methods.IMPS_PAYMENT_PROCESS);

        dataEx.execute(

                new BasicNameValuePair(AppConstants.PARAM_PBX_SERVICE, AppConstants.PARAM_SERVICE_IMPS_PAYMENT_PROCESS),
                new BasicNameValuePair(AppConstants.PARAM_PBX_IMPS_PAYMENT_REMITTANCE_AMOUNT, sAmount),
                new BasicNameValuePair(AppConstants.PARAM_PBX_IMPS_PAYMENT_BENEFICIARY_ID, EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_PBX_IMPS_BENEFICIARY_ID,null)),
                new BasicNameValuePair(AppConstants.PARAM_PBX_IMPS_BENEFICIARY_CUSTOMER_ID, String.valueOf(EphemeralStorage.getInstance(_applicationContext).getInt(AppConstants.PARAM_PBX_IMPS_CUSTOMER_ID, -1))),
                new BasicNameValuePair(AppConstants.PARAM_PBX_IMPS_TRANSACTION_NARRATION, sTxnDescription),
                new BasicNameValuePair(AppConstants.PARAM_PBX_ROWID, EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_PBX_USERID, null)),
                new BasicNameValuePair(AppConstants.PARAM_PBX_RMN, EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.LOGGED_IN_USERNAME, null))
        );
    }

    public TransactionRequest getIMPSPaymentProcessResponse(TransactionRequest<ImpsPaymentProcessResult> request) {
        if (TextUtils.isEmpty(request.getRemoteResponse())) {
            Log.e(_LOG, "Null remote response received");
            return null;
        }

        try {
            Gson gson = new GsonBuilder().create();

            Type type = new TypeToken<ResponseBase<ImpsPaymentProcessResult>>() {
            }.getType();

            ResponseBase<ImpsPaymentProcessResult> responseBase = gson.fromJson(request.getRemoteResponse(), type);

            if (responseBase == null) {
                Log.w(_LOG, "Null response?");
                return null;
            }

            Log.d(_LOG, "Response: " + responseBase.data);
            ImpsPaymentProcessResult impsPaymentProcessResult = responseBase.data;


            request.setCustom(impsPaymentProcessResult);



            EphemeralStorage.getInstance(_applicationContext).storeBoolean(
                    AppConstants.PARAM_PBX_IMPS_POSTINGSTATUS,
                    responseBase.data.PostingStatus);
            Log.i("PostingStatus", String.valueOf(responseBase.data.PostingStatus));

            EphemeralStorage.getInstance(_applicationContext).storeString(
                    AppConstants.PARAM_PBX_IMPS_SESSIONID,
                    responseBase.data.SessionID);
            Log.i("SessionID", String.valueOf(responseBase.data.SessionID));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return request;
    }



   //PAYMENT CONFIRM


    public void impsConfirmPayment(TransactionRequest<List<ImpsConfirmPaymentResult>> request , String sOTP ,
                                   String sAccountNumber ,String sIFSCCode , String sCustomerNumber , String sAmount) {
        if (request == null ) {
            if (_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.IMPS_CONFIRM_PAYMENT);
            }
            return;
        }

        String url = AppConstants.URL_PBX_PLATFORM_IMPS + AppConstants.SVC_NEW_METHOD_IMPS_CUSTOMER_REGISTRATION;

        AsyncDataEx dataEx = new AsyncDataEx(this, new TransactionRequest(), url, Methods.IMPS_CONFIRM_PAYMENT);

        dataEx.execute(
                new BasicNameValuePair(AppConstants.PARAM_PBX_SERVICE, AppConstants.PARAM_SERVICE_IMPS_CONFIRM_PAYMENT),
                new BasicNameValuePair(AppConstants.PARAM_PBX_IMPS_ACCOUNT_NAME, EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_PBX_IMPS_BENEFICIARY_ACCOUNT_NAME,null)),
                new BasicNameValuePair(AppConstants.PARAM_PBX_IMPS_ACCOUNT_NUMBER , sAccountNumber),
                new BasicNameValuePair(AppConstants.PARAM_PBX_IMPS_VERIFY_IFSC_CODE, sIFSCCode),
                new BasicNameValuePair(AppConstants.PARAM_PBX_IMPS_CUSTOMER_NUMBER,sCustomerNumber),
                new BasicNameValuePair(AppConstants.PARAM_PBX_IMPS_BENEFICIARY_ID, EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_PBX_IMPS_BENEFICIARY_ID,null)),
                new BasicNameValuePair(AppConstants.PARAM_PBX_IMPS_BENEFICIARY_CUSTOMER_ID, String.valueOf(EphemeralStorage.getInstance(_applicationContext).getInt(AppConstants.PARAM_PBX_IMPS_CUSTOMER_ID, -1))),
                new BasicNameValuePair(AppConstants.PARAM_PBX_IMPS_OTP, sOTP),
                new BasicNameValuePair(AppConstants.PARAM_PBX_IMPS_CONFIRM_PAYMENT_REMITTANCE_AMOUNT, sAmount),
                new BasicNameValuePair(AppConstants.PARAMS_PBX_IMPS_SESSION_ID ,EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_PBX_IMPS_SESSIONID , null)),
                new BasicNameValuePair(AppConstants.PARAM_PBX_ROWID, EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_PBX_USERID, null)),
                new BasicNameValuePair(AppConstants.PARAM_PBX_RMN, EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.LOGGED_IN_USERNAME, null))

        );
    }

    public TransactionRequest getIMPSConfirmPaymentListResponse(TransactionRequest<List<ImpsConfirmPaymentResult>> request) {

        try {
            Gson gson = new GsonBuilder().create();

            Type type = new TypeToken<ResponseBase<ImpsConfirmPaymentResult[]>>() {
            }.getType();

            ResponseBase<ImpsConfirmPaymentResult[]> responseBase = gson.fromJson(request.getRemoteResponse(), type);
            request.setResponseCode(responseBase.code);
            Log.e("ErrorPayment123", String.valueOf(request.getResponseCode()));
            if (responseBase == null) {
                return null;
            }
            if(responseBase.data == null){
                return null;
            }
            if (responseBase.code == -1) {
                request.setResponseCode(-1);
                return request;
            }

            List<ImpsConfirmPaymentResult> impsConfirmPaymentResultList = Arrays.asList(responseBase.data);
            // List<ImpsConfirmPaymentResult> impsConfirmPaymentResultList = new ArrayList<ImpsConfirmPaymentResult>(Arrays.asList(responseBase.data));

            request.setCustom(impsConfirmPaymentResultList);
        }
        catch(NullPointerException npe){
            npe.printStackTrace();
        }
        return request;
    }





    public void signUpEncryptData(String composeData, String Key) {

    }

    public void signUpCustomerRegistration(String data) {

    }

    public void impsCustomerRegistrationStatus(String consumerNumber) {

    }

    public void impsBeneficiaryList(String consumerNumber) {

    }

    public void impsMomPaymentProcess(TransactionRequest<ImpsPaymentProcessResult>request , String sAmount ,String sTxnNarration){

    }
    public void impsMomConfirmProcess(TransactionRequest request ,  String sOTP ,
                                      String sAccountNumber ,String sIFSCCode , String sCustomerNumber){

    }
    public void cancelAsynctASK(){
        dataEx = new AsyncDataEx(this);
        dataEx.cancel(true);
    }
}