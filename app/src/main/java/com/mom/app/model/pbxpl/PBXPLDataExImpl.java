package com.mom.app.model.pbxpl;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

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
import com.mom.app.model.Transaction;
import com.mom.app.model.local.EphemeralStorage;
import com.mom.app.utils.AppConstants;

import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by vaibhavsinha on 7/6/14.
 */
public class PBXPLDataExImpl extends DataExImpl implements AsyncListener<String> {
    static String _LOG              = "PBX_DATA";

    private static int _SUCCESS     = 200;

    public PBXPLDataExImpl(Context pContext, AsyncListener pListener){
        this._listener              = pListener;
        this._applicationContext    = pContext;
        checkConnectivity(pContext);
    }

    @Override
    public void onTaskSuccess(String result, Methods callback) {
        if(_listener == null){
            return;
        }

        Log.d(_LOG, "Result: " + result);
        try {
            switch (callback) {
                case LOGIN:
                    boolean bSuccess = loginSuccessful(result);
                    _listener.onTaskSuccess(bSuccess, null);
                    break;
                case CHANGE_PIN:
                    ResponseBase responseBase = changePinResult(result);
                    String message = null;

                    if (responseBase != null && responseBase.status != _SUCCESS) {
                        message = responseBase.message;
                    }
                    _listener.onTaskSuccess(message, null);
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
                    _listener.onTaskSuccess(extractTransactions(result), callback);
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
    public void rechargeMobile(String psConsumerNumber, double pdAmount, String psOperator, int pnRechargeType) {

    }

    @Override
    public void rechargeDTH(String psSubscriberId, double pdAmount, String psOperator, String psCustomerMobile) {

    }

    @Override
    public void getBalance(){
        String url                  = AppConstants.URL_PBX_PLATFORM_APP;
        AsyncDataEx dataEx		    = new AsyncDataEx(this, url, Methods.GET_BALANCE);
        String userName             = EphemeralStorage.getInstance(_applicationContext).getString(
                AppConstants.LOGGED_IN_USERNAME, null
        );

        dataEx.execute(
                new BasicNameValuePair(AppConstants.PARAM_PBX_SERVICE, AppConstants.SVC_PBX_CHECK_BALANCE),
                new BasicNameValuePair(AppConstants.PARAM_PBX_RN, userName)
        );
    }

    public float parseBalanceResult(String result) throws MOMException{
        if(TextUtils.isEmpty(result)){
            throw new MOMException();
        }
        try {
            Gson gson = new GsonBuilder().create();
            Balance balance = gson.fromJson(result, Balance.class);
            return balance.balance;
        }catch(JsonSyntaxException jse){
            jse.printStackTrace();
            throw new MOMException();
        }
    }

    @Override
    public void login(String userName, String password){
        String loginUrl				= AppConstants.URL_PBX_PLATFORM_APP;
        Log.i(_LOG, "Calling Async login");

        AsyncDataEx dataEx		    = new AsyncDataEx(this, loginUrl, Methods.LOGIN);

        dataEx.execute(
            new BasicNameValuePair(AppConstants.PARAM_PBX_SERVICE, AppConstants.SVC_PBX_CHECK_LOGIN),
            new BasicNameValuePair(AppConstants.PARAM_PBX_USERNAME, userName),
            new BasicNameValuePair(AppConstants.PARAM_PBX_PASSWORD, password)
        );
    }

    public boolean loginSuccessful(String psResult){
        if(TextUtils.isEmpty(psResult)){
            return false;
        }

        boolean success = false;

        try {
            Gson gson   = new GsonBuilder().create();

            Type type   = new TypeToken<ResponseBase<LoginResult>>() {
            }.getType();

            ResponseBase<LoginResult> responseBase = gson.fromJson(psResult, type);

            success     = (responseBase != null && responseBase.status == 200);

            if (success && responseBase.response.Table != null && responseBase.response.Table.length < 1) {
                success = false;
            }

            if (success) {
                EphemeralStorage.getInstance(_applicationContext).storeString(
                        AppConstants.PARAM_PBX_TOKEN,
                        responseBase.response.Table[0].PartyGUID
                );
            }
        }catch(JsonSyntaxException jse){
            Log.e(_LOG, jse.getMessage());
            jse.printStackTrace();
        }
        return success;
    }

    @Override
    public void payBill(String psSubscriberId, double pdAmount, String psOperatorId, String psCustomerMobile, String psConsumerName, HashMap<String, String> psExtraParamsMap) {

    }

    @Override
    public void getBillAmount(String psOperatorId, String psSubscriberId) {

    }

    @Override
    public void getTransactionHistory() {
        String userName             = EphemeralStorage.getInstance(_applicationContext).getString(
                AppConstants.LOGGED_IN_USERNAME, null
        );

        String token                = EphemeralStorage.getInstance(_applicationContext).getString(
                AppConstants.PARAM_PBX_TOKEN, null
        );

        AsyncDataEx dataEx		    = new AsyncDataEx(this, AppConstants.URL_PBX_PLATFORM_APP, Methods.TRANSACTION_HISTORY);

        dataEx.execute(
                new BasicNameValuePair(AppConstants.PARAM_PBX_SERVICE, AppConstants.SVC_PBX_TRANSACTION_HISTORY),
                new BasicNameValuePair(AppConstants.PARAM_PBX_USERNAME, userName),
                new BasicNameValuePair(AppConstants.PARAM_PBX_TOKEN, token)
        );
    }

    public ArrayList<Transaction> extractTransactions(String result){
        Gson gson = new GsonBuilder().create();

        Type type   = new TypeToken<ResponseBase<PBXTransaction>>(){}.getType();

        ResponseBase<PBXTransaction> responseBase  = gson.fromJson(result, type);

        ArrayList<Transaction> list     = new ArrayList<Transaction>();
        if(responseBase == null || responseBase.response == null || responseBase.response.Table == null){
            return list;
        }

        for(PBXTransaction pbxTransaction:responseBase.response.Table){
            list.add(pbxTransaction.getTransactionObject());
        }

        return list;
    }

    @Override
    public void changePin(PinType pinType, String psOldPin, String psNewPin) {
        String loginUrl				= AppConstants.URL_PBX_PLATFORM_APP;

        Log.i(_LOG, "Calling Async password");

        String userName             = EphemeralStorage.getInstance(_applicationContext).getString(
                AppConstants.LOGGED_IN_USERNAME, null
        );

        String token                = EphemeralStorage.getInstance(_applicationContext).getString(
                AppConstants.PARAM_PBX_TOKEN, null
        );

        AsyncDataEx dataEx		    = new AsyncDataEx(this, loginUrl, Methods.CHANGE_PIN);

        dataEx.execute(
                new BasicNameValuePair(AppConstants.PARAM_PBX_SERVICE, AppConstants.SVC_PBX_CHANGE_PASSWORD),
                new BasicNameValuePair(AppConstants.PARAM_PBX_NP, psNewPin),
                new BasicNameValuePair(AppConstants.PARAM_PBX_OP, psOldPin),
                new BasicNameValuePair(AppConstants.PARAM_PBX_RN, userName),
                new BasicNameValuePair(AppConstants.PARAM_PBX_TOKEN, token)
        );
    }

    public ResponseBase changePinResult(String psResult){
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(psResult, ResponseBase.class);
    }

    public void getOperatorNames(){
        String url                  = AppConstants.URL_PBX_PLATFORM_APP;
        String userName             = EphemeralStorage.getInstance(_applicationContext).getString(
                AppConstants.LOGGED_IN_USERNAME, null
        );

        String token                = EphemeralStorage.getInstance(_applicationContext).getString(
                AppConstants.PARAM_PBX_TOKEN, null
        );

        AsyncDataEx dataEx		    = new AsyncDataEx(this, url, Methods.GET_OPERATOR_NAMES);
    }

    public String[] getOperatorNamesResult(String result){
        Gson gson = new GsonBuilder().create();

        Type type   = new TypeToken<ResponseBase<Party>>(){}.getType();

        ResponseBase<Party> responseBase  = gson.fromJson(result, type);

        if(responseBase == null || responseBase.response == null || responseBase.response.Table == null){
            return new String[0];
        }

        Party[] parties     = responseBase.response.Table;
        String[] operators  = new String[parties.length];

        for(int i=0; i<parties.length; i++){
            operators[i]    = parties[i].PartyName;
        }

        return operators;
    }
}