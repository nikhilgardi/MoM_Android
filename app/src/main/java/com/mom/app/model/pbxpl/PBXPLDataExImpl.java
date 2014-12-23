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
import com.mom.app.identifier.PinType;
import com.mom.app.model.AsyncDataEx;
import com.mom.app.model.AsyncListener;
import com.mom.app.model.AsyncResult;
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

    static PBXPLDataExImpl _instance;

    String _deviceRegId             = null;
    String _token                   = null;
    String _userName                = null;

    // String jsonStr =    "{\"Table\":[{\"PartyROWID\":92420,\"PartyRMN\":\"9769496026\",\"PartyName\":\"Akshay\",\"PartyGUID\":\"9163b4dd-f23d-41fd-99ab-7c0f57c9c7ed\",\"PartyEnum\":null,\"PartyTypeEnum\":16,\"userName\":\"Software\"}]}" ;

    private PBXPLDataExImpl(Context pContext){
        checkConnectivity(pContext);
        _applicationContext         = pContext;

        _deviceRegId                = GcmUtil.getInstance(pContext).getRegistrationId();
        _userName                   = EphemeralStorage.getInstance(_applicationContext).getString(
                AppConstants.PARAM_PBX_RMN, null
        );

    }

    private PBXPLDataExImpl(Context pContext, AsyncDataEx dataEx, boolean checkConnectivity){
        if(checkConnectivity) {
            checkConnectivity(pContext);
        }
        _applicationContext         = pContext;
        _deviceRegId                = GcmUtil.getInstance(pContext).getRegistrationId();
        _token                      = EphemeralStorage.getInstance(_applicationContext).getString(
                AppConstants.PARAM_PBX_TOKEN, null
        );
        _userName                   = EphemeralStorage.getInstance(_applicationContext).getString(
                AppConstants.PARAM_PBX_RMN, null
        );
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

    public static PBXPLDataExImpl getInstance(Context context, AsyncListener pListener, Methods method) throws MOMException{
        if(_instance == null){
            _instance               = new PBXPLDataExImpl(context);
        }

        _instance.setListener(pListener);

        if(!_instance.setToken() && method != Methods.LOGIN){
            throw new MOMException(AsyncResult.CODE.NOT_LOGGED_IN);
        }

        return _instance;
    }

    public static PBXPLDataExImpl getInstance(Context context, AsyncListener pListener) throws MOMException{
        return getInstance(context, pListener, Methods.LOGIN);
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
                case LOGIN:
                    boolean bSuccess = loginSuccessful(result);
                    _listener.onTaskSuccess(bSuccess, callback);
                    break;
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
                    }
                    break;

                case RECHARGE_DTH:
                    Log.i(_LOG, "TaskComplete: rechargeDTH method, result: " + result);
                    if (_listener != null) {
                        _listener.onTaskSuccess(getPaymentTransactionResult(result), Methods.RECHARGE_DTH);
                    }
                    break;
                case PAY_BILL:
                    Log.i(_LOG, "TaskComplete: payBill method, result: " + result);
                    if (_listener != null) {
                        _listener.onTaskSuccess(getPaymentTransactionResult(result), Methods.PAY_BILL);
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

       // String url				    = AppConstants.URL_PBX_PLATFORM_APP;
        String url				    = AppConstants.URL_PBX_PLATFORM_APPLIC;

        AsyncDataEx dataEx		    = new AsyncDataEx(this, request, url, Methods.RECHARGE_MOBILE);

        dataEx.execute(
                new BasicNameValuePair(AppConstants.PARAM_PBX_SERVICE, AppConstants.SVC_PBX_RECHARGE_MOBILE),
                new BasicNameValuePair(AppConstants.PARAM_PBX_RMN, EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.LOGGED_IN_USERNAME, null)),
                new BasicNameValuePair(AppConstants.PARAM_PBX_CUSTOMER_NUMBER, request.getConsumerId()),
                new BasicNameValuePair(AppConstants.PARAM_PBX_OPERTAORSHORTCODE , request.getOperator().getCode()),
                new BasicNameValuePair(AppConstants.PARAM_PBX_AMOUNT, String.valueOf(Math.round(request.getAmount()))),
                new BasicNameValuePair(AppConstants.PARAM_PBX_IDENTIFIER, MiscUtils.getRandomLongAsString()),
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
                new BasicNameValuePair(AppConstants.PARAM_PBX_IDENTIFIER, MiscUtils.getRandomLongAsString()),
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
                new BasicNameValuePair(AppConstants.PARAM_PBX_CUSTOMER_NUMBER, request.getConsumerId()),
                new BasicNameValuePair(AppConstants.PARAM_PBX_AMOUNT, String.valueOf(Math.round(request.getAmount()))),
                new BasicNameValuePair(AppConstants.PARAM_PBX_OPERTAORSHORTCODE , request.getOperator().getCode()),
                new BasicNameValuePair(AppConstants.PARAM_PBX_IDENTIFIER, MiscUtils.getRandomLongAsString()),
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

            Type type   = new TypeToken<ResponseBase<PaymentResponse>>() {
            }.getType();
            ResponseBase<PaymentResponse> responseBase = gson.fromJson(request.getRemoteResponse(), type);

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
    public void getBalance(){

        String url                  = AppConstants.URL_PBX_PLATFORM_APP;
        AsyncDataEx dataEx		    = new AsyncDataEx(this, new TransactionRequest(), url, Methods.GET_BALANCE);

        String userName             = EphemeralStorage.getInstance(_applicationContext).getString(
                AppConstants.LOGGED_IN_USERNAME, null
        );

        dataEx.execute(
                new BasicNameValuePair(AppConstants.PARAM_PBX_SERVICE, AppConstants.SVC_PBX_CHECK_BALANCE),
                new BasicNameValuePair(AppConstants.PARAM_PBX_RN, userName),
                new BasicNameValuePair(AppConstants.PARAM_PBX_IDENTIFIER, MiscUtils.getRandomLongAsString()),
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
        String url				= AppConstants.URL_PBX_PLATFORM_APP;
        Log.i(_LOG, "Calling Async login");
        AsyncDataEx dataEx		    = new AsyncDataEx(this, new TransactionRequest(), url, Methods.LOGIN);

        dataEx.execute(
                new BasicNameValuePair(AppConstants.PARAM_PBX_SERVICE, AppConstants.SVC_PBX_CHECK_LOGIN),
                new BasicNameValuePair(AppConstants.PARAM_PBX_USERNAME, username),
                new BasicNameValuePair(AppConstants.PARAM_PBX_PASSWORD, password),
                new BasicNameValuePair(AppConstants.PARAM_PBX_IDENTIFIER, MiscUtils.getRandomLongAsString()),
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

            }

        }catch(JsonSyntaxException jse){
            Log.e(_LOG, jse.getMessage());
            jse.printStackTrace();
        }
        return success;
    }





    public String getInternalBalTransfer(TransactionRequest request){
        String intenalbal = null;

        if(TextUtils.isEmpty(request.getRemoteResponse())){
            return null;
        }


        try {
            Gson gson   = new GsonBuilder().create();

            Type type   = new TypeToken<ResponseBase>() {
            }.getType();
            ResponseBase responseBase = gson.fromJson(request.getRemoteResponse(), type);

            intenalbal= responseBase.data.toString();
            Log.d(_LOG, "ResponseInternalBal: " + intenalbal);

            return intenalbal;
        }catch (Exception e){
            e.printStackTrace();
        }

        return intenalbal;
    }


    @Override
    public void getBillAmount(TransactionRequest request) {

    }

    @Override
    public void getTransactionHistory() {
        String userName             = EphemeralStorage.getInstance(_applicationContext).getString(
                AppConstants.PARAM_PBX_RMN, null
        );

        AsyncDataEx dataEx		    = new AsyncDataEx(
                this,
                new TransactionRequest(),
                AppConstants.URL_PBX_PLATFORM_APP,
                Methods.TRANSACTION_HISTORY
        );

        dataEx.execute(
                new BasicNameValuePair(AppConstants.PARAM_PBX_SERVICE, AppConstants.SVC_PBX_TRANSACTION_HISTORY),
                new BasicNameValuePair(AppConstants.PARAM_PBX_RMN, userName),
                new BasicNameValuePair(AppConstants.PARAM_PBX_IDENTIFIER, MiscUtils.getRandomLongAsString()),
                new BasicNameValuePair(AppConstants.PARAM_PBX_ORIGIN_ID, _deviceRegId),
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

        }catch (Exception e){
            e.printStackTrace();
        }

        return request;
    }

    public void getOperatorNames(){
        String userName             = EphemeralStorage.getInstance(_applicationContext).getString(
                AppConstants.LOGGED_IN_USERNAME, null
        );

        AsyncDataEx dataEx		    = new AsyncDataEx(
                this,
                new TransactionRequest(),
                AppConstants.URL_PBX_PLATFORM_APP,
                Methods.GET_OPERATOR_NAMES
        );

        dataEx.execute(
                new BasicNameValuePair(AppConstants.PARAM_PBX_SERVICE, AppConstants.SVC_PBX_OPERATOR_NAMES),
                new BasicNameValuePair(AppConstants.PARAM_OPERATOR_TYPE, AppConstants.PBX_OPERATOR_TYPE_DEFAULT),
                new BasicNameValuePair(AppConstants.PARAM_PBX_RMN, userName),
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

        return Arrays.asList(responseBase.data);
    }

    public void balanceTransfer(TransactionRequest request, String payTo){
        if(
                TextUtils.isEmpty(payTo) || request.getAmount() < 1 ){

            if(_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.BALANCE_TRANSFER);
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
    public  void lic(String policyNumber){

        if(
                TextUtils.isEmpty(policyNumber)){

            if(_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.LIC);
            }
            return;
        }

        String licrefno             = EphemeralStorage.getInstance(_applicationContext).getString(
                AppConstants.PARAM_LICREFNO, null
        );


        AsyncDataEx dataEx		    = new AsyncDataEx(
                this,
                new TransactionRequest<LicLifeResponse>(),
                AppConstants.URL_PBX_PLATFORM_APPLIC,
                Methods.LIC
        );

        dataEx.execute(
                new BasicNameValuePair(AppConstants.PARAM_SERVICE_NEW, AppConstants.SVC_PBX_LIC),
                new BasicNameValuePair(AppConstants.PARAM_LICREFNO, policyNumber),
                new BasicNameValuePair(AppConstants.PARAM_PBX_RMN , EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.LOGGED_IN_USERNAME, null))

        );

    }

    public  void licPayment(TransactionRequest<LicLifeResponse> request , String CustomerMobNo , String PolicyNo){

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
                AppConstants.URL_PBX_PLATFORM_APPLIC,
                Methods.PAY_LIC
        );

        dataEx.execute(
                new BasicNameValuePair(AppConstants.PARAM_SERVICE_NEW, AppConstants.PARAM_SERVICE_LIC),
                new BasicNameValuePair(AppConstants.PARAM_CUSTOMER_NUMBER_LIC, CustomerMobNo),
                new BasicNameValuePair(AppConstants.PARAM_POLICYNUMBER_LIC, PolicyNo),
                new BasicNameValuePair(AppConstants.PARAM_TRANSREFGUID_LIC , request.getCustom().getTransRefGUID()),
                new BasicNameValuePair(AppConstants.PARAM_TRANSINVGUID_LIC , request.getCustom().getTransInvGUID()),
                new BasicNameValuePair(AppConstants.PARAM_POLICYAMOUNT_LIC , Double.toString(request.getCustom().getTransInvAmount())),
                new BasicNameValuePair(AppConstants.PARAM_POLICYHOLDER_LIC , request.getCustom().getOLife().getParty().getFullName()),
                new BasicNameValuePair(AppConstants.PARAM_PBX_RMN , EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.LOGGED_IN_USERNAME, null))



        );

    }
    public void changePinTest(TransactionRequest request){

    }
}