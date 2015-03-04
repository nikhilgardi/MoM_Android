package com.mom.app.model.pbxpl;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.mom.app.error.MOMException;
import com.mom.app.gcm.GcmUtil;
import com.mom.app.identifier.ClassType;
import com.mom.app.identifier.PBXOperatorDataType;
import com.mom.app.identifier.PinType;
import com.mom.app.model.AsyncDataEx;
import com.mom.app.model.AsyncListener;
import com.mom.app.model.AsyncResult;
import com.mom.app.model.Beneficiary;
import com.mom.app.model.DataExImpl;
import com.mom.app.model.Operator;
import com.mom.app.model.Transaction;
import com.mom.app.model.local.EphemeralStorage;

import com.mom.app.model.pbxpl.lic.LicLife;
import com.mom.app.model.pbxpl.lic.LicLifeResponse;

import com.mom.app.model.pbxpl.lic.LicResponse;
import com.mom.app.ui.TransactionRequest;
import com.mom.app.utils.AppConstants;
import com.mom.app.utils.MiscUtils;

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
    static String _LOG              = AppConstants.LOG_PREFIX + "PBX_DATA";


    String _deviceRegId             = null;
    String _token                   = null;
    String _userName                = null;
    PBXOperatorDataType _opType     = null;


    // String jsonStr =    "{\"Table\":[{\"PartyROWID\":92420,\"PartyRMN\":\"9769496026\",\"PartyName\":\"Akshay\",\"PartyGUID\":\"9163b4dd-f23d-41fd-99ab-7c0f57c9c7ed\",\"PartyEnum\":null,\"PartyTypeEnum\":16,\"userName\":\"Software\"}]}" ;

    public PBXPLDataExImpl(Context pContext, Methods method, AsyncListener pListener) throws MOMException{
        checkConnectivity(pContext);
        _applicationContext         = pContext;

        _deviceRegId                = GcmUtil.getInstance(pContext).getRegistrationId();
        _userName                   = EphemeralStorage.getInstance(_applicationContext).getString(
                AppConstants.LOGGED_IN_USERNAME, null
        );

        _listener = pListener;
        _applicationContext = pContext;
        _deviceRegId            = GcmUtil.getInstance(pContext).getRegistrationId();
        _token                  = EphemeralStorage.getInstance(pContext).getString(
                AppConstants.PARAM_PBX_TOKEN, null
        );
        _userName               = EphemeralStorage.getInstance(pContext).getString(
                AppConstants.LOGGED_IN_USERNAME, null
        );


        if(method != Methods.LOGIN) {
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

    public boolean setToken(){
        _token                      = EphemeralStorage.getInstance(_applicationContext).getString(
                AppConstants.PARAM_PBX_TOKEN, null
        );

        if(TextUtils.isEmpty(_token)){
            return false;
        }

        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onTaskSuccess(TransactionRequest result, Methods callback) {
        if(_listener == null){
            return;
        }

        Log.d(_LOG, "Result: " + result);
        try {
            switch (callback) {
                case GET_BALANCE:
                    try {
                        float balance = parseBalanceResult(result);
                        _listener.onTaskSuccess(balance, callback);
                    } catch (NumberFormatException nfe) {
                        onTaskError(new AsyncResult(AsyncResult.CODE.GENERAL_FAILURE), Methods.GET_BALANCE);
                        nfe.printStackTrace();
                    }
                    break;
                case GET_OPERATOR_NAMES:
                    _listener.onTaskSuccess(getOperatorNamesResult(result), callback);
                    break;
                case TRANSACTION_HISTORY:
                    _listener.onTaskSuccess(extractTransactionshistory(result), callback);
                    break;
                case RECHARGE_MOBILE:
                    Log.i(_LOG, "TaskComplete: rechargeMobile method, result: " + result);
                    if (_listener != null) {
                        _listener.onTaskSuccess(getPaymentTransactionResult(result), Methods.RECHARGE_MOBILE);
                        Log.i("REsult" , result.getConsumerId());
                    }
                    break;

                case RECHARGE_DTH:
                    Log.i(_LOG, "TaskComplete: rechargeDTH method, result: " + result);
                    if (_listener != null) {
                        _listener.onTaskSuccess(getPaymentTransactionResult(result), Methods.RECHARGE_DTH);
                        Log.i("REsultDTH" , result.getConsumerId());
                    }
                    break;
                case PAY_BILL:
                    Log.i(_LOG, "TaskComplete: payBill method, result: " + result);
                    if (_listener != null) {
                        _listener.onTaskSuccess(getPaymentTransactionResult(result), Methods.PAY_BILL);
                        Log.i("REsultBill" , result.getConsumerId());
                    }
                    break;

                case UTILITY_BILL_PAY:
                    Log.i(_LOG, "TaskComplete: payBill method, result: " + result);
                    if (_listener != null) {
                        _listener.onTaskSuccess(getPaymentTransactionResult(result), Methods.UTILITY_BILL_PAY);
                        Log.i("REsultBill" , result.getConsumerId());
                    }
                    break;

                case IMPS_CUSTOMER_REGISTRATION:
                    Log.d(_LOG, "TaskComplete: Login method, result: " + result);
                    if (_listener != null) {
                       _listener.onTaskSuccess(getImpsCustomerRegistration(result), callback);
                    }
                    break;

                case IMPS_BENEFICIARY_LIST:
                    Log.d(_LOG, "TaskComplete: Login method, result: " + result);
                    if (_listener != null) {
                        _listener.onTaskSuccess(getImpsBeneficiaryList(result), Methods.IMPS_BENEFICIARY_LIST);
                    }
                    break;

                case LIC:
                    Log.d(_LOG, "TaskComplete: lic method, result: " + result);

                    if (_listener != null) {

                       _listener.onTaskSuccess(getPremiumAmount(result), Methods.LIC);
                     }

                     break;

                case PAY_LIC:
                    Log.d(_LOG, "TaskComplete: lic method, result: " + result);

                    if (_listener != null) {

                        _listener.onTaskSuccess(getLic(result), Methods.PAY_LIC);
                    }

                    break;
                case BALANCE_TRANSFER_PBX:
                    Log.d(_LOG, "TaskComplete: balanceTransfer method, result: " + result);

                    if (_listener != null) {
                        _listener.onTaskSuccess(getInternalBalTransfer(result), Methods.BALANCE_TRANSFER_PBX);
                    }
                    break;

                case CHANGE_PASSWORD:
                    Log.d(_LOG, "TaskComplete: changeMPin method, result: " + result);

                    if (_listener != null) {
                        _listener.onTaskSuccess(getChangePasswordResult(result), Methods.CHANGE_PASSWORD);
                    }

                    break;

            }
        }catch(MOMException me){
            me.printStackTrace();
            _listener.onTaskError(new AsyncResult(AsyncResult.CODE.GENERAL_FAILURE), callback);
        }
    }

    @Override
    public void onTaskError(AsyncResult pResult, Methods callback) {
        if(_listener == null){
            return;
        }

        _listener.onTaskError(pResult, callback);
    }

    @Override
    public void verifyTPin(String psTPin) {

    }

    @Override
    public void rechargeMobile(TransactionRequest<PaymentResponse> request, int rechargeType) {

        if(TextUtils.isEmpty(request.getConsumerId())){

            if(_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.RECHARGE_MOBILE);
            }
            return;
        }

        String url				    = AppConstants.URL_PBX_PLATFORM_APP;


        AsyncDataEx dataEx		    = new AsyncDataEx(this, request, url, Methods.RECHARGE_MOBILE);

        dataEx.execute(
                new BasicNameValuePair(AppConstants.PARAM_PBX_SERVICE, AppConstants.SVC_PBX_RECHARGE_MOBILE),
                new BasicNameValuePair(AppConstants.PARAM_PBX_RMN, EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.LOGGED_IN_USERNAME, null)),
                new BasicNameValuePair(AppConstants.PARAM_PBX_CUSTOMER_NUMBER, request.getConsumerId()),
                new BasicNameValuePair(AppConstants.PARAM_PBX_OPERTAORSHORTCODE , request.getOperator().getCode()),
                new BasicNameValuePair(AppConstants.PARAM_PBX_AMOUNT, String.valueOf(Math.round(request.getAmount()))),
                new BasicNameValuePair(AppConstants.PARAM_PBX_IDENTIFIER, String.valueOf(request.getId())),
                new BasicNameValuePair(AppConstants.PARAM_PBX_ORIGIN_ID, _deviceRegId),
                new BasicNameValuePair(AppConstants.PARAM_PBX_CLIENT_TOKEN, _token)
        );
    }



    @Override
    public void rechargeDTH(TransactionRequest<PaymentResponse> request){
        if(TextUtils.isEmpty(request.getConsumerId())){

            if(_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.RECHARGE_DTH);
            }
        }

        String url				    = AppConstants.URL_PBX_PLATFORM_APP;


        AsyncDataEx dataEx		    = new AsyncDataEx(this, request, url, Methods.RECHARGE_DTH);

        dataEx.execute(
                new BasicNameValuePair(AppConstants.PARAM_PBX_SERVICE, AppConstants.SVC_PBX_RECHARGE_DTH),
                new BasicNameValuePair(AppConstants.PARAM_PBX_RMN, _userName),
                new BasicNameValuePair(AppConstants.PARAM_PBX_CUSTOMER_NUMBER, request.getConsumerId()),
                new BasicNameValuePair(AppConstants.PARAM_PBX_AMOUNT, String.valueOf(Math.round(request.getAmount()))),
                new BasicNameValuePair(AppConstants.PARAM_PBX_OPERTAORSHORTCODE , request.getOperator().getCode()),
                new BasicNameValuePair(AppConstants.PARAM_PBX_IDENTIFIER, String.valueOf(request.getId())),
                new BasicNameValuePair(AppConstants.PARAM_PBX_ORIGIN_ID, _deviceRegId),
                new BasicNameValuePair(AppConstants.PARAM_PBX_CLIENT_TOKEN, _token)
        );
    }



    public void payBill(
            TransactionRequest<PaymentResponse> request,
            String psConsumerName,
            HashMap<String, String> pExtraParamsMap
    ){
        String url				    = AppConstants.URL_PBX_PLATFORM_APP ;

        AsyncDataEx dataEx		    = new AsyncDataEx(this, request, url, Methods.PAY_BILL);

        dataEx.execute(
                new BasicNameValuePair(AppConstants.PARAM_PBX_SERVICE, AppConstants.SVC_PBX_BILL_PAY),
                new BasicNameValuePair(AppConstants.PARAM_PBX_RMN, _userName),
                new BasicNameValuePair(AppConstants.PARAM_PBX_CUSTOMER_NUMBER, request.getCustomerMobile()),
                new BasicNameValuePair(AppConstants.PARAM_PBX_AMOUNT, String.valueOf(Math.round(request.getAmount()))),
                new BasicNameValuePair(AppConstants.PARAM_PBX_OPERTAORSHORTCODE , request.getOperator().getCode()),
                new BasicNameValuePair(AppConstants.PARAM_PBX_IDENTIFIER, String.valueOf(request.getId())),
                new BasicNameValuePair(AppConstants.PARAM_PBX_ORIGIN_ID, _deviceRegId),
                new BasicNameValuePair(AppConstants.PARAM_PBX_CLIENT_TOKEN, _token)
        );
    }


    public void utilityPayBill(
            TransactionRequest<PaymentResponse> request,
            String psConsumerName,
            HashMap<String, String> pExtraParamsMap
    ){
        String url				    = AppConstants.URL_PBX_PLATFORM_APP ;
        String operatorCode         = request.getOperator().code;
        AsyncDataEx dataEx		    = new AsyncDataEx(this, request, url, Methods.UTILITY_BILL_PAY);
        if (operatorCode.equals("CES")  && pExtraParamsMap != null){
            if(
                    !pExtraParamsMap.containsKey(AppConstants.PARAM_NEW_AC_MONTH)||
                            !pExtraParamsMap.containsKey(AppConstants.PARAM_NEW_DUE_DATE)||
                            !pExtraParamsMap.containsKey(AppConstants.PARAM_NEW_STUBTYPE)
                    ){
                Log.d("NEW_PL_DATA", "Parameters not sent for CESC");
                if(_listener != null) {
                    _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.LOGIN);
                }
                return;
            }

        }
        dataEx.execute(
                new BasicNameValuePair(AppConstants.PARAM_PBX_SERVICE, AppConstants.SVC_PBX_UTILITY_BILL_PAY),
                new BasicNameValuePair(AppConstants.PARAM_PBX_RMN, _userName),
                new BasicNameValuePair(AppConstants.PARAM_PBX_CUSTOMER_NUMBER, request.getCustomerMobile()),
                new BasicNameValuePair(AppConstants.PARAM_PBX_AMOUNT, String.valueOf(Math.round(request.getAmount()))),
                new BasicNameValuePair(AppConstants.PARAM_PBX_OPERTAORSHORTCODE , request.getOperator().getCode()),

                new BasicNameValuePair(AppConstants.PARAM_PBX_ACCOUNT_NUMBER , request.getConsumerId()),
                new BasicNameValuePair(AppConstants.PARAM_PBX_AC_MONTH , pExtraParamsMap.get(AppConstants.PARAM_PBX_AC_MONTH)),
                new BasicNameValuePair(AppConstants.PARAM_PBX_DUE_DATE , pExtraParamsMap.get(AppConstants.PARAM_PBX_DUE_DATE)),
                new BasicNameValuePair(AppConstants.PARAM_PBX_STUBTYPE , pExtraParamsMap.get(AppConstants.PARAM_PBX_STUBTYPE)),


                new BasicNameValuePair(AppConstants.PARAM_PBX_IDENTIFIER, String.valueOf(request.getId())),
                new BasicNameValuePair(AppConstants.PARAM_PBX_ORIGIN_ID, _deviceRegId),
                new BasicNameValuePair(AppConstants.PARAM_PBX_CLIENT_TOKEN, _token)
        );
    }

    public TransactionRequest getPaymentTransactionResult(TransactionRequest<PaymentResponse> request){

        if(TextUtils.isEmpty(request.getRemoteResponse())){
            Log.e(_LOG, "Null remote response received");
            return null;
        }

        try {
            Gson gson   = new GsonBuilder().create();

            Type type   = new TypeToken<ResponseBase<String>>() {
            }.getType();
            ResponseBase<String> responseBase = gson.fromJson(request.getRemoteResponse(), type);

            if(responseBase == null){
                Log.w(_LOG, "Null response?");
                return null;
            }

            Log.d(_LOG, "Response: " + responseBase.data);
            PaymentResponse response = new PaymentResponse();
            response.transactionId  = responseBase.data;

            request.setCustom(response);

            return request;
        }catch (Exception e){
            e.printStackTrace();
        }

        return request;
    }
    public TransactionRequest getImpsCustomerRegistration(TransactionRequest<ImpsCustomerRegistrationResult> request){


        if(TextUtils.isEmpty(request.getRemoteResponse())){
            Log.e(_LOG, "Null remote response received");
            return null;
        }
    //    boolean success = false;
        try {
            Gson gson   = new GsonBuilder().create();

            Type type   = new TypeToken<ResponseBase<ImpsCustomerRegistrationResult>>() {
            }.getType();
            ResponseBase<ImpsCustomerRegistrationResult> responseBase = gson.fromJson(request.getRemoteResponse(), type);

            if(responseBase == null){
                Log.w(_LOG, "Null response?");
                return null;
            }

            Log.d(_LOG, "Response: " + responseBase.data);
            ImpsCustomerRegistrationResult response = new ImpsCustomerRegistrationResult();
             response  = responseBase.data;
             request.setCustom(response);

                 EphemeralStorage.getInstance(_applicationContext).storeInt(
                        AppConstants.PARAM_PBX_IMPS_CUSTOMER_ID,
                        responseBase.data.customerID);
                Log.i("CustId" , String.valueOf(responseBase.data.customerID));

                EphemeralStorage.getInstance(_applicationContext).storeBoolean(
                        AppConstants.PARAM_PBX_IMPS_SERVICEALLOWED,
                        responseBase.data.isIMPSServiceAllowed);
                Log.i("ServiceAllowed" , String.valueOf(responseBase.data.isIMPSServiceAllowed));

                EphemeralStorage.getInstance(_applicationContext).storeBoolean(
                        AppConstants.PARAM_PBX_IMPS_ISREGISTERED,
                        responseBase.data.isRegistered);
                Log.i("Isreg" , String.valueOf(responseBase.data.isRegistered));





        }catch (Exception e){
            e.printStackTrace();
        }

        return request;
    }

        public  ArrayList<Beneficiary> getImpsBeneficiaryList(TransactionRequest request){
            Gson gson = new GsonBuilder().create();

            Type type   = new TypeToken<ResponseBase<BeneficiaryResult[]>>(){}.getType();

            ResponseBase<BeneficiaryResult[]> responseBase  = gson.fromJson(request.getRemoteResponse(), type);

            BeneficiaryResult[] beneficiarylist    = responseBase.data;
            ArrayList<Beneficiary> list     = new ArrayList<Beneficiary>();
            if(responseBase == null || responseBase.code != 0 || responseBase.data == null){
                return null;
            }

            for(BeneficiaryResult beneficiaryResult:responseBase.data){
                list.add(beneficiaryResult.getBeneficiary());
            }

            return list;
        }


    public List<BeneficiaryResult> getBeneficiaryListResponse(TransactionRequest request){
        Gson gson = new GsonBuilder().create();

        Type type   = new TypeToken<ResponseBase<BeneficiaryResult[]>>(){}.getType();

        ResponseBase<BeneficiaryResult[]> responseBase  = gson.fromJson(request.getRemoteResponse(), type);

        if(responseBase == null || responseBase.data == null){
            return null;
        }

        List<BeneficiaryResult> beneficiaryResult    = Arrays.asList(responseBase.data);

//        EphemeralStorage.getInstance(_applicationContext).storeObject(
//                AppConstants.PARAM_PBX_OPERATORS + _opType.id, beneficiaryResult
//        );
System.out.println("Beneficiary"+ beneficiaryResult.toString());
        return beneficiaryResult;
    }
    @Override
    public void getBalance(){

        String url                  = AppConstants.URL_PBX_PLATFORM_APP;
        TransactionRequest request  = new TransactionRequest();

        AsyncDataEx dataEx		    = new AsyncDataEx(this, request, url, Methods.GET_BALANCE);


        String userName             = EphemeralStorage.getInstance(_applicationContext).getString(
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

    public float parseBalanceResult(TransactionRequest request) throws MOMException{
        if(TextUtils.isEmpty(request.getRemoteResponse())){
            throw new MOMException();
        }
        try {
            Gson gson = new GsonBuilder().create();
            Balance balance = gson.fromJson(request.getRemoteResponse(), Balance.class);
            return balance.balance;
        }catch(JsonSyntaxException jse){
            jse.printStackTrace();
            throw new MOMException();
        }
    }

    @Override
    public void login(String username, String password){

        //String url				    = AppConstants.URL_PBX_PLATFORM_APPLIC;

        String url				    = AppConstants.URL_PBX_PLATFORM_APP;

        Log.i(_LOG, "Calling Async login");
        TransactionRequest request  = new TransactionRequest();

        AsyncDataEx dataEx		    = new AsyncDataEx(new AsyncListener<TransactionRequest>() {
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


    public boolean loginSuccessful(TransactionRequest pResult){
        if(pResult == null || TextUtils.isEmpty(pResult.getRemoteResponse())){
            return false;
        }

        boolean success = false;

        try {
            Gson gson   = new GsonBuilder().create();

            Type type   = new TypeToken<ResponseBase<LoginResult>>() {
            }.getType();

            ResponseBase<LoginResult> responseBase = gson.fromJson(pResult.getRemoteResponse(), type);

            LoginResult loginResult = responseBase.data;

            success     = (responseBase != null && responseBase.code == 0);

            if (success && responseBase.data != null ) {
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

                Log.i("Username" ,responseBase.data.userName);
                EphemeralStorage.getInstance(_applicationContext).storeString(
                        AppConstants.PARAM_MERCHANTID_LIC,
                        responseBase.data.merchantId);


                EphemeralStorage.getInstance(_applicationContext).storeInt(
                        AppConstants.PARAM_IsLIC,
                        responseBase.data.isLic);
                Log.i("isLic" , String.valueOf(responseBase.data.isLic));


            }

        }catch(JsonSyntaxException jse){
            Log.e(_LOG, jse.getMessage());
            jse.printStackTrace();
        }
        return success;
    }



     /*   public TransactionRequest getImpsCustomerRegistration(TransactionRequest request){


                if(request == null || TextUtils.isEmpty(request.getRemoteResponse())){
                    return null;
                }

                boolean success = false;

                try {
                    Gson gson   = new GsonBuilder().create();

                    Type type   = new TypeToken<ResponseBase<LoginResult>>() {
                    }.getType();

                    ResponseBase<ImpsCustomerRegistrationResult> responseBase = gson.fromJson(request.getRemoteResponse(), type);

                    ImpsCustomerRegistrationResult impsCustReg = responseBase.data;

                    success     = (responseBase != null && responseBase.code == 0);

                    if (success && responseBase.data != null ) {
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

                        Log.i("Username" ,responseBase.data.userName);
                        EphemeralStorage.getInstance(_applicationContext).storeString(
                                AppConstants.PARAM_MERCHANTID_LIC,
                                responseBase.data.merchantId);


                        EphemeralStorage.getInstance(_applicationContext).storeInt(
                                AppConstants.PARAM_IsLIC,
                                responseBase.data.isLic);
                        Log.i("isLic" , String.valueOf(responseBase.data.isLic));


                    }

                }catch(JsonSyntaxException jse){
                    Log.e(_LOG, jse.getMessage());
                    jse.printStackTrace();
                }
                return success;
            }*/


    public TransactionRequest getInternalBalTransfer(TransactionRequest request){

        if(TextUtils.isEmpty(request.getRemoteResponse())){
            Log.e(_LOG, "Null remote response received");
            return null;
        }

        try {
            Gson gson   = new GsonBuilder().create();

            Type type   = new TypeToken<ResponseBase<String>>() {
            }.getType();
            ResponseBase<String> responseBase = gson.fromJson(request.getRemoteResponse(), type);

            if(responseBase == null){
                Log.w(_LOG, "Null response?");
                return null;
            }

            Log.d(_LOG, "Response: " + responseBase.data);

            request.setCustom(responseBase.data);

            return request;
        }catch (Exception e){
            e.printStackTrace();
        }

        return request;
    }



       @Override
    public void getBillAmount(TransactionRequest request) {
    }

    @Override
    public void getTransactionHistory() {
        String userName             = EphemeralStorage.getInstance(_applicationContext).getString(
                AppConstants.PARAM_PBX_RMN, null
        );




        TransactionRequest request  = new TransactionRequest();


        AsyncDataEx dataEx		    = new AsyncDataEx(

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

    public ArrayList<Transaction> extractTransactionshistory(TransactionRequest request){
        Gson gson = new GsonBuilder().create();

        Type type   = new TypeToken<ResponseBase<PBXTransaction[]>>(){}.getType();

        ResponseBase<PBXTransaction[]> responseBase  = gson.fromJson(request.getRemoteResponse(), type);

        ArrayList<Transaction> list     = new ArrayList<Transaction>();

        if(responseBase == null || responseBase.code != 0 || responseBase.data == null){
            return list;
        }

        for(PBXTransaction transaction:responseBase.data){
            list.add(transaction.getTransaction());
        }

        return list;
    }

    @Override
    public  void changePassword( String psOldPin, String psNewPin){
        Log.i(_LOG, "Calling Async password");

        String userName             = EphemeralStorage.getInstance(_applicationContext).getString(
                AppConstants.LOGGED_IN_USERNAME, null
        );



        AsyncDataEx dataEx		    = new AsyncDataEx(
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

    private TransactionRequest getChangePasswordResult(TransactionRequest request){

        if(TextUtils.isEmpty(request.getRemoteResponse())){
            request.setStatus(TransactionRequest.RequestStatus.FAILED);
            return request;
        }


        try {
            Gson gson   = new GsonBuilder().create();

            Type type   = new TypeToken<ResponseBase<ChangePBXPassword>>() {}.getType();

            ResponseBase<ChangePBXPassword> responseBase = gson.fromJson(request.getRemoteResponse(), type);

            request.setResponseCode(responseBase.code);
            request.setRemoteResponse(responseBase.data.message);

        }catch (Exception e){
            e.printStackTrace();
        }

        return request;
    }

    public void getOperatorNames(PBXOperatorDataType type){
        _opType                     = type;



        AsyncDataEx dataEx		    = new AsyncDataEx(
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

    public List<Operator> getOperatorNamesResult(TransactionRequest request){
        Gson gson = new GsonBuilder().create();

        Type type   = new TypeToken<ResponseBase<Operator[]>>(){}.getType();

        ResponseBase<Operator[]> responseBase  = gson.fromJson(request.getRemoteResponse(), type);

        if(responseBase == null || responseBase.data == null){
            return null;
        }

        List<Operator> operators    = Arrays.asList(responseBase.data);

        EphemeralStorage.getInstance(_applicationContext).storeObject(
                AppConstants.PARAM_PBX_OPERATORS + _opType.code, operators
        );

        return operators;
    }

    public void balanceTransfer(TransactionRequest request, String payTo){
        if(
                TextUtils.isEmpty(payTo) || request.getAmount() < 1 ){

            if(_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.BALANCE_TRANSFER_PBX);
            }
            return;
        }

        String userName             = EphemeralStorage.getInstance(_applicationContext).getString(
                AppConstants.PARAM_PBX_RMN, null
        );


        AsyncDataEx dataEx		    = new AsyncDataEx(
                this,
                new TransactionRequest(),
                AppConstants.URL_PBX_PLATFORM_APP,
                Methods.BALANCE_TRANSFER_PBX
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

    public  void changePin(PinType pinType, String psOldPin, String psNewPin){

    }


    public TransactionRequest<LicLifeResponse> getPremiumAmount(TransactionRequest<LicLifeResponse> response) throws MOMException{

        if(TextUtils.isEmpty(response.getRemoteResponse())){
            throw new MOMException();
        }

        try {
            Gson gson   = new GsonBuilder().create();

            Type type   = new TypeToken<ResponseBase<LicResponse>>() {
            }.getType();

            ResponseBase<LicResponse> responseBase = gson.fromJson(response.getRemoteResponse(), type);


            if(
                    responseBase == null ||
                    responseBase.data == null ||
                    responseBase.data.TXLife == null ||
                    responseBase.data.TXLife.getTXLifeResponse() == null ||
                    responseBase.data.TXLife.getTXLifeResponse().getOLife() == null
                    ){
                throw new MOMException();
            }

            LicResponse licResponse = responseBase.data;
            LicLife txLife           = licResponse.TXLife;

            Log.i("Premium Amount" ,(txLife.getTXLifeResponse().getTransInvAmount().toString()));
            Log.i("Full Name" ,(txLife.getTXLifeResponse().getOLife().getParty().getFullName()));
            Log.i("From Unpaid" ,(txLife.getTXLifeResponse().getOLife().getPolicy().getFrUnpaidPremiumDate().toString()));
            Log.i("To Unpaid" ,(txLife.getTXLifeResponse().getOLife().getPolicy().getToUnpaidPremiumDate().toString()));

            response.setCustom(txLife.getTXLifeResponse());



            return response;

        }catch(JsonSyntaxException jse){
            Log.e(_LOG, jse.getMessage());
        }

        return null;
    }
    public TransactionRequest<LicLifeResponse> getLic(TransactionRequest<LicLifeResponse> response) throws MOMException{

        if(TextUtils.isEmpty(response.getRemoteResponse())){
            throw new MOMException();
        }

        try {
            Gson gson   = new GsonBuilder().create();

            Type type   = new TypeToken<ResponseBase<LicResponse>>() {
            }.getType();

            ResponseBase<LicResponse> responseBase = gson.fromJson(response.getRemoteResponse(), type);


            if(
                    responseBase == null ||
                            responseBase.data == null ||
                            responseBase.data.TXLife == null ||
                            responseBase.data.TXLife.getTXLifeResponse() == null
                    ){
                throw new MOMException();
            }

            LicResponse licResponse = responseBase.data;
            LicLife txLife           = licResponse.TXLife;

            response.setCustom(txLife.getTXLifeResponse());

            return response;

        }catch(JsonSyntaxException jse){
            Log.e(_LOG, jse.getMessage());
        }

        return null;
    }

    public String getPlaceHolder(String response){

        Log.i("Lic" , response);

        if(TextUtils.isEmpty(response)){
            return null;
        }

        try {
            Gson gson   = new GsonBuilder().create();

            Type type   = new TypeToken<ResponseBase<LicResponse>>() {
            }.getType();

            ResponseBase<LicResponse> responseBase = gson.fromJson(response, type);

            LicResponse licResponse = responseBase.data;
            LicLife txLife           = licResponse.TXLife;


            Log.i("PlaceHolder" , txLife.getTXLifeResponse().getOLife().getParty().getFullName().toString());
            return (txLife.getTXLifeResponse().getOLife().getParty().getFullName().toString());

        }catch(JsonSyntaxException jse){
            Log.e(_LOG, jse.getMessage());
                    }

        return null;
    }
    public  void lic(String policyNumber , String CustomerMobNo){

        if(
                TextUtils.isEmpty(policyNumber)){

            if(_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.LIC);
            }
            return;
        }


        TransactionRequest<LicLifeResponse> request = new TransactionRequest<LicLifeResponse>();

        AsyncDataEx dataEx		    = new AsyncDataEx(
                this,
                request,
                AppConstants.URL_PBX_PLATFORM_APP,
                Methods.LIC
        );

        dataEx.execute(
                new BasicNameValuePair(AppConstants.PARAM_SERVICE_NEW, AppConstants.SVC_PBX_LIC),
                new BasicNameValuePair(AppConstants.PARAM_LICREFNO, policyNumber),
                new BasicNameValuePair(AppConstants.PARAM_MERCHANTID_LIC , EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_MERCHANTID_LIC,null)),
                new BasicNameValuePair(AppConstants.PARAM_PBX_RMN , EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.LOGGED_IN_USERNAME, null)),
                new BasicNameValuePair(AppConstants.PARAM_CUSTOMER_NUMBER_LIC ,CustomerMobNo)

        );

    }

    public  void licPayment(TransactionRequest<LicLifeResponse> request , String CustomerMobNo , String PolicyNo , String UnpaidDate){

        if(
                TextUtils.isEmpty(request.getRemoteResponse())){

            if(_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.PAY_LIC);
            }
            return;
        }

        String licrefno             = EphemeralStorage.getInstance(_applicationContext).getString(
                AppConstants.PARAM_LICREFNO, null
        );


        AsyncDataEx dataEx		    = new AsyncDataEx(
                this,
                new TransactionRequest<LicLifeResponse>(),
                AppConstants.URL_PBX_PLATFORM_APP,
                Methods.PAY_LIC
        );

        dataEx.execute(
                new BasicNameValuePair(AppConstants.PARAM_SERVICE_NEW, AppConstants.PARAM_SERVICE_LIC),
                new BasicNameValuePair(AppConstants.PARAM_CUSTOMER_NUMBER_LIC, CustomerMobNo),
                new BasicNameValuePair(AppConstants.PARAM_PBX_USERID,EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_PBX_USERID, null) ),
                new BasicNameValuePair(AppConstants.PARAM_POLICYNUMBER_LIC, PolicyNo),
                new BasicNameValuePair(AppConstants.PARAM_TRANSREFGUID_LIC , request.getCustom().getTransRefGUID()),
                new BasicNameValuePair(AppConstants.PARAM_TRANSINVGUID_LIC , request.getCustom().getTransInvGUID()),
                new BasicNameValuePair(AppConstants.PARAM_POLICYAMOUNT_LIC , Double.toString(request.getCustom().getTransInvAmount())),
                new BasicNameValuePair(AppConstants.PARAM_POLICYHOLDER_LIC , request.getCustom().getOLife().getParty().getFullName()),
                new BasicNameValuePair(AppConstants.PARAM_FRUNPAIDPREMIUMDATE, request.getCustom().getOLife().getPolicy().getFrUnpaidPremiumDate()),
                new BasicNameValuePair(AppConstants.PARAM_PBX_RMN , EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.LOGGED_IN_USERNAME, null))



        );

    }

    public void impsCustomerRegistration(String sConsumerNumber){
        if(TextUtils.isEmpty(sConsumerNumber)){
            if(_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.IMPS_CUSTOMER_REGISTRATION);
            }
        }

        String Url				= AppConstants.URL_PBX_PLATFORM_IMPS + AppConstants.SVC_NEW_METHOD_IMPS_CUSTOMER_REGISTRATION;

        AsyncDataEx dataEx		    = new AsyncDataEx(this, new TransactionRequest(), Url, Methods.IMPS_CUSTOMER_REGISTRATION);

        dataEx.execute(

                new BasicNameValuePair(AppConstants.PARAM_PBX_SERVICE, AppConstants.PARAM_SERVICE_IMPS),
                new BasicNameValuePair(AppConstants.PARAM_PBX_CONSUMER_NUMBER, sConsumerNumber),
                new BasicNameValuePair(AppConstants.PARAM_PBX_ROWID,EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_PBX_USERID, null) ),
                new BasicNameValuePair(AppConstants.PARAM_PBX_RMN, EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.LOGGED_IN_USERNAME, null))
        );
    }


    public void impsBeneficiaryList(String sConsumerNumber){


        String Url				= AppConstants.URL_PBX_PLATFORM_IMPS + AppConstants.SVC_NEW_METHOD_IMPS_CUSTOMER_REGISTRATION;

        AsyncDataEx dataEx		    = new AsyncDataEx(this, new TransactionRequest(), Url, Methods.IMPS_BENEFICIARY_LIST);

        dataEx.execute(

                new BasicNameValuePair(AppConstants.PARAM_PBX_SERVICE, AppConstants.PARAM_SERVICE_IMPS_BENEFICIARY_LIST),
                new BasicNameValuePair(AppConstants.PARAM_PBX_IMPS_CUSTOMER_ID, String.valueOf(EphemeralStorage.getInstance(_applicationContext).getInt(AppConstants.PARAM_PBX_IMPS_CUSTOMER_ID, -1))),
                new BasicNameValuePair(AppConstants.PARAM_PBX_ROWID, String.valueOf(EphemeralStorage.getInstance(_applicationContext).getBoolean(AppConstants.PARAM_PBX_IMPS_ISREGISTERED, false))),
               // new BasicNameValuePair(AppConstants.PARAM_PBX_IMPS_CUSTOMER_ID,"40596" ),
               // new BasicNameValuePair(AppConstants.PARAM_PBX_ROWID,"true" ),
                new BasicNameValuePair(AppConstants.PARAM_PBX_RMN, sConsumerNumber)
        );
    }
    public void changePinTest(TransactionRequest request){

    }

    public void signUpEncryptData(String composeData , String Key){

    }
    public void signUpCustomerRegistration(String data){

    }
}