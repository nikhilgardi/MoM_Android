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
import com.mom.app.model.local.EphemeralStorage;

import com.mom.app.model.local.PersistentStorage;

import com.mom.app.ui.TransactionRequest;
import com.mom.app.utils.AppConstants;

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


    // String jsonStr =    "{\"Table\":[{\"PartyROWID\":92420,\"PartyRMN\":\"9769496026\",\"PartyName\":\"Akshay\",\"PartyGUID\":\"9163b4dd-f23d-41fd-99ab-7c0f57c9c7ed\",\"PartyEnum\":null,\"PartyTypeEnum\":16,\"userName\":\"Software\"}]}" ;

    public PBXPLDataExImpl(Context pContext, AsyncListener pListener){
        this._listener              = pListener;
        this._applicationContext    = pContext;
        checkConnectivity(pContext);
        _deviceRegId                = PersistentStorage.getInstance(pContext).getString(GcmUtil.PROPERTY_REG_ID, "");
    }

    public PBXPLDataExImpl(Context pContext, AsyncListener pListener, AsyncDataEx dataEx, boolean checkConnectivity){
        this._listener              = pListener;
        this._applicationContext    = pContext;
        _deviceRegId                = PersistentStorage.getInstance(pContext).getString(GcmUtil.PROPERTY_REG_ID, "");

        if(checkConnectivity) {
            checkConnectivity(pContext);
        }
    }

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
                    //   boolean bSuccess = loginSuccessful(jsonStr);
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
                  //  _listener.onTaskSuccess(extractTransactions(result), callback);
                    _listener.onTaskSuccess(extractTransactionshistory(result), callback);
                    break;
                case RECHARGE_MOBILE:
                    Log.i(_LOG, "TaskComplete: rechargeMobile method, result: " + result);
                    if (_listener != null) {
                        _listener.onTaskSuccess(getRechargeResult(result), Methods.RECHARGE_MOBILE);
                    }
                    break;

                case RECHARGE_DTH:
                    Log.i(_LOG, "TaskComplete: rechargeDTH method, result: " + result);
                    if (_listener != null) {
                        _listener.onTaskSuccess(getRechargeResult(result), Methods.RECHARGE_DTH);
                    }
                    break;
                case PAY_BILL:
                    Log.i(_LOG, "TaskComplete: payBill method, result: " + result);
                    if (_listener != null) {
                        _listener.onTaskSuccess(getRechargeResult(result), Methods.PAY_BILL);
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
    public void rechargeMobile(TransactionRequest request, int rechargeType) {

        if(
                request.getConsumerId() == null || "".equals(request.getConsumerId())
            //  || pdAmount < 1 || psOperator == null || "".equals(psOperator)
                ){

            if(_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.LOGIN);
            }
            return;
        }

        String url				    = AppConstants.URL_PBX_PLATFORM_APP;

        AsyncDataEx dataEx		    = new AsyncDataEx(this, request, url, Methods.RECHARGE_MOBILE);

        dataEx.execute(

                new BasicNameValuePair(AppConstants.PARAM_PBX_SERVICE, "RECHARGEMOBILE"),
                new BasicNameValuePair(AppConstants.PARAM_PBX_RMN, "9769496026"),
                new BasicNameValuePair(AppConstants.PARAM_PBX_CUSTOMERNUMBER, request.getConsumerId()),
                new BasicNameValuePair("operatorShortCode" , "AIR"),
                new BasicNameValuePair("amount", String.valueOf(Math.round(request.getAmount()))),
                new BasicNameValuePair(AppConstants.PARAM_PBX_IDENTIFIER   , "1234"),
                new BasicNameValuePair(AppConstants.PARAM_PBX_ORIGINID , "5555"),
                new BasicNameValuePair(AppConstants.PARAM_PBX_CLIENTTOKEN , "4")
        );
    }

    @Override
    public void rechargeDTH(TransactionRequest request){
        if(
                request.getConsumerId() == null || "".equals(request.getConsumerId())
                       // || pdAmount < 1 || psOperator == null || "".equals(psOperator)
                ){

            if(_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.LOGIN);
            }
        }

        String url				    = AppConstants.URL_PBX_PLATFORM_APP;
        AsyncDataEx dataEx		    = new AsyncDataEx(this, request, url, Methods.RECHARGE_DTH);

        dataEx.execute(
                new BasicNameValuePair(AppConstants.PARAM_PBX_SERVICE, "DTH"),
                new BasicNameValuePair(AppConstants.PARAM_PBX_RMN, "9769496026"),
                new BasicNameValuePair(AppConstants.PARAM_PBX_CUSTOMERNUMBER, request.getConsumerId()),
                new BasicNameValuePair(AppConstants.PARAM_PBX_AMOUNT, String.valueOf(Math.round(request.getAmount()))),
                new BasicNameValuePair(AppConstants.PARAM_PBX_OPERTAORSHORTCODE , "TSK"),
                new BasicNameValuePair(AppConstants.PARAM_PBX_IDENTIFIER   , "1234"),
                new BasicNameValuePair(AppConstants.PARAM_PBX_ORIGINID , "5555"),
                new BasicNameValuePair(AppConstants.PARAM_PBX_CLIENTTOKEN , "4")
        );
    }

    public void payBill(
            TransactionRequest request,
            String psConsumerName,
            HashMap<String, String> pExtraParamsMap
    ){


        String url				    = AppConstants.URL_PBX_PLATFORM_APP ;
        AsyncDataEx dataEx		    = new AsyncDataEx(this, request, url, Methods.PAY_BILL);

        dataEx.execute(
                new BasicNameValuePair(AppConstants.PARAM_PBX_SERVICE, "BILLPAY"),
                new BasicNameValuePair(AppConstants.PARAM_PBX_RMN, "9769496026"),
                new BasicNameValuePair(AppConstants.PARAM_PBX_CUSTOMERNUMBER, request.getConsumerId()),
                new BasicNameValuePair(AppConstants.PARAM_PBX_AMOUNT, String.valueOf(Math.round(request.getAmount()))),
                new BasicNameValuePair(AppConstants.PARAM_PBX_OPERTAORSHORTCODE , "BAI"),
                new BasicNameValuePair(AppConstants.PARAM_PBX_IDENTIFIER   , "1234"),
                new BasicNameValuePair(AppConstants.PARAM_PBX_ORIGINID , "5555"),
                new BasicNameValuePair(AppConstants.PARAM_PBX_CLIENTTOKEN , "4")
        );
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
                new BasicNameValuePair(AppConstants.PARAM_PBX_RN, userName)
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
                new BasicNameValuePair(AppConstants.PARAM_PBX_PASSWORD, password)
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
                String jsonstring1 = loginResult.userID;
                Log.i("ResultLogin1" , jsonstring1);
                EphemeralStorage.getInstance(_applicationContext).storeString(
                        AppConstants.PARAM_PBX_RMN,
                        responseBase.data.rmn);
                String jsonstring2 = loginResult.rmn;
                Log.i("ResultLogin2" , jsonstring2);
                EphemeralStorage.getInstance(_applicationContext).storeString(
                        AppConstants.PARAM_PBX_NAME,
                        responseBase.data.name);
                String jsonstring3 = loginResult.name;
                Log.i("ResultLogin3" , jsonstring3);
                EphemeralStorage.getInstance(_applicationContext).storeString(
                        AppConstants.PARAM_PBX_TOKEN,
                        responseBase.data.token);
                String jsonstring4 = loginResult.token;
                Log.i("ResultLogin4" , jsonstring4);
                EphemeralStorage.getInstance(_applicationContext).storeString(
                        AppConstants.PARAM_PBX_USERTYPE,
                        responseBase.data.userType);
                String jsonstring5 = loginResult.userType;
                Log.i("ResultLogin5" , jsonstring5);
                EphemeralStorage.getInstance(_applicationContext).storeString(
                        AppConstants.PARAM_PBX_USERNAMELOGIN,
                        responseBase.data.userName);
                String jsonstring6 = loginResult.userName;
                Log.i("ResultLogin6" , jsonstring6);
            }

        }catch(JsonSyntaxException jse){
            Log.e(_LOG, jse.getMessage());
            jse.printStackTrace();
        }
        return success;
    }
    public String getRechargeResult(TransactionRequest request){
        String response = null;

        if(TextUtils.isEmpty(request.getRemoteResponse())){
            return null;
        }


        try {
            Gson gson   = new GsonBuilder().create();

            Type type   = new TypeToken<ResponseBase<RechargeResponse>>() {
            }.getType();
            ResponseBase<RechargeResponse> responseBase = gson.fromJson(request.getRemoteResponse(), type);
            RechargeResponse rechargeResponse = responseBase.data;

            String response1   = rechargeResponse.transactionId;
            Log.d(_LOG, "Response: " + response1);
            String response2   = Double.toString(rechargeResponse.amount);
            Log.d(_LOG, "Response1: " + response2);
            String response3   = Double.toString(rechargeResponse.Balance);
            Log.d(_LOG, "Response2: " + response3);
             response = "Transaction Id: " + response1 + " " + "Amount" + response2 + " " + "Balance" + " " + response3;
            return response;
        }catch (Exception e){
            e.printStackTrace();
        }

        return response;
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
            Log.i(_LOG, "ResponseInternalBal: " + intenalbal);

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

        String token                = EphemeralStorage.getInstance(_applicationContext).getString(
                AppConstants.PARAM_PBX_TOKEN, null
        );
        AsyncDataEx dataEx		    = new AsyncDataEx(
                this,
                new TransactionRequest(),
                AppConstants.URL_PBX_PLATFORM_APP,
                Methods.RECHARGE_MOBILE
        );

        dataEx.execute(
                new BasicNameValuePair(AppConstants.PARAM_PBX_SERVICE, AppConstants.SVC_PBX_TRANSACTION_HISTORY),
                new BasicNameValuePair(AppConstants.PARAM_PBX_RMN, userName),
                new BasicNameValuePair(AppConstants.PARAM_PBX_TOKEN, token),
                new BasicNameValuePair(AppConstants.PARAM_PBX_IDENTIFIER, "1234"),
                new BasicNameValuePair(AppConstants.PARAM_PBX_ORIGINID, "5555"),
                new BasicNameValuePair(AppConstants.PARAM_PBX_CLIENTTOKEN, "4")
        );
    }

    public ArrayList<PBXTransaction> extractTransactionshistory(TransactionRequest request){
        Gson gson = new GsonBuilder().create();

        Type type   = new TypeToken<ResponseBase<PBXTransaction[]>>(){}.getType();

        ResponseBase<PBXTransaction> responseBase  = gson.fromJson(request.getRemoteResponse(), type);

        ArrayList<PBXTransaction> list     = new ArrayList<PBXTransaction>();
        if(responseBase == null || responseBase.code != 0){
            return list;
        }

        return list;
    }
    @Override
    public  void changePassword( String psOldPin, String psNewPin){
        Log.i(_LOG, "Calling Async password");

        String userName             = EphemeralStorage.getInstance(_applicationContext).getString(
                AppConstants.LOGGED_IN_USERNAME, null
        );

        String token                = EphemeralStorage.getInstance(_applicationContext).getString(
                AppConstants.PARAM_PBX_TOKEN, null
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
                new BasicNameValuePair(AppConstants.PARAM_PBX_TOKEN, token),
                new BasicNameValuePair(AppConstants.PARAM_PBX_RMN, userName),
                new BasicNameValuePair(AppConstants.PARAM_PBX_IDENTIFIER, "1234"),
                new BasicNameValuePair(AppConstants.PARAM_PBX_ORIGINID, "5555"),
                new BasicNameValuePair(AppConstants.PARAM_PBX_CLIENTTOKEN, "4")
        );
    }

    public String getChangePasswordResult(TransactionRequest request){
        String changepwd = null;

        if(TextUtils.isEmpty(request.getRemoteResponse())){
            return null;
        }


        try {
            Gson gson   = new GsonBuilder().create();

            Type type   = new TypeToken<ResponseBase<ChangePBXPassword>>() {
            }.getType();
            ResponseBase<ChangePBXPassword> responseBase = gson.fromJson(request.getRemoteResponse(), type);
            ChangePBXPassword changepassword = new ChangePBXPassword();
            changepassword= responseBase.data;

            String response1   = Double.toString(changepassword.status);
            Log.d(_LOG, "Response: " + response1);
            Log.i(_LOG, "Response: " + response1);

            String response2   = changepassword.message;
            Log.d(_LOG, "Response1: " + response2);
            Log.i(_LOG, "Response1: " + response2);


            changepwd = "Status: " + response1 + " " + "Message" + response2;
            Log.i(_LOG, "Response: " + changepwd);
            return changepwd;

        }catch (Exception e){
            e.printStackTrace();
        }

        return changepwd;
       // Gson gson = new GsonBuilder().create();
       // return gson.fromJson(psResult, ResponseBase.class);
    }

    public void getOperatorNames(){
        String url                  = AppConstants.URL_PBX_PLATFORM_APP;
        String userName             = EphemeralStorage.getInstance(_applicationContext).getString(
                AppConstants.LOGGED_IN_USERNAME, null
        );

        String token                = EphemeralStorage.getInstance(_applicationContext).getString(
                AppConstants.PARAM_PBX_TOKEN, null
        );
        AsyncDataEx dataEx		    = new AsyncDataEx(
                this,
                new TransactionRequest(),
                AppConstants.URL_PBX_PLATFORM_APP,
                Methods.GET_OPERATOR_NAMES
        );


        dataEx.execute(
                new BasicNameValuePair(AppConstants.PARAM_PBX_SERVICE, AppConstants.SVC_PBX_OPERATOR_NAMES),
                new BasicNameValuePair(AppConstants.PARAM_OPERATOR_TYPE, "1"),
                new BasicNameValuePair(AppConstants.PARAM_PBX_RMN, userName),
                new BasicNameValuePair(AppConstants.PARAM_PBX_TOKEN, token),
                new BasicNameValuePair(AppConstants.PARAM_PBX_IDENTIFIER, "1234"),
                new BasicNameValuePair(AppConstants.PARAM_PBX_ORIGINID, "5555"),
                new BasicNameValuePair(AppConstants.PARAM_PBX_CLIENTTOKEN, "4")
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

    public void balanceTransfer(TransactionRequest request, String payTo)
    {

        if(
                payTo == null || "".equals(payTo) || request.getAmount() < 1 ){

            if(_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.LOGIN);
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
                new BasicNameValuePair(AppConstants.PARAM_PBX_IDENTIFIER, "1234"),
                new BasicNameValuePair(AppConstants.PARAM_PBX_ORIGINID, "5555"),
                new BasicNameValuePair(AppConstants.PARAM_PBX_CLIENTTOKEN, "4")
        );
    }
    public void rechargeMobile(
            String psConsumerNumber,
            double pdAmount,
            String psOperator,
            int pnRechargeType
    ){

    }

    public  void changePin(PinType pinType, String psOldPin, String psNewPin){

    }


}