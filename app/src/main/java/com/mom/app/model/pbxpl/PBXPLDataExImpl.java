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
    static String LOG_TAG          = AppConstants.LOG_PREFIX + "PBX_DATA";
    private String _operatorId      = null;
    private static int _SUCCESS     = 200;
    // String jsonStr =    "{\"Table\":[{\"PartyROWID\":92420,\"PartyRMN\":\"9769496026\",\"PartyName\":\"Akshay\",\"PartyGUID\":\"9163b4dd-f23d-41fd-99ab-7c0f57c9c7ed\",\"PartyEnum\":null,\"PartyTypeEnum\":16,\"userName\":\"Software\"}]}" ;
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
                    //   boolean bSuccess = loginSuccessful(jsonStr);
                    _listener.onTaskSuccess(bSuccess, callback);
                    break;
//                case CHANGE_PIN:
//                    ResponseBase responseBase = changePinResult(result);
//                    String message = null;
//
//                    if (responseBase != null && responseBase.status != _SUCCESS) {
//                        message = responseBase.message;
//                    }
//                    _listener.onTaskSuccess(message, null);
//                    break;
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
                  //  _listener.onTaskSuccess(getOperatorNamesResult(result), callback);
                    break;
                case TRANSACTION_HISTORY:
                    _listener.onTaskSuccess(extractTransactions(result), callback);
                    Log.i("History" , result);
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
        if(
                psConsumerNumber == null || "".equals(psConsumerNumber) ||
                        pdAmount < 1 || psOperator == null || "".equals(psOperator)
                ){

            if(_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.LOGIN);
            }
            return;
        }

        _operatorId                 = psOperator;

        String url				    = AppConstants.URL_PBX_PLATFORM_APP;

        AsyncDataEx dataEx		    = new AsyncDataEx(this, url, Methods.RECHARGE_MOBILE);

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
                new BasicNameValuePair(AppConstants.PARAM_NEW_STR_MOBILE_NUMBER, psConsumerNumber),
                new BasicNameValuePair(AppConstants.PARAM_NEW_RECHARGE_AMOUNT, String.valueOf(Math.round(pdAmount))),
                new BasicNameValuePair(AppConstants.PARAM_NEW_OPERATOR, psOperator),
                new BasicNameValuePair(AppConstants.PARAM_NEW_INT_RECHARGE_TYPE, Integer.valueOf(pnRechargeType).toString())
        );
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
    public void login(String username, String password){
        String loginUrl				= AppConstants.URL_PBX_PLATFORM_APP;
        Log.i(_LOG, "Calling Async login");

        AsyncDataEx dataEx		    = new AsyncDataEx(this, loginUrl, Methods.LOGIN);

        dataEx.execute(
                new BasicNameValuePair(AppConstants.PARAM_PBX_SERVICE, AppConstants.SVC_PBX_CHECK_LOGIN),
                new BasicNameValuePair(AppConstants.PARAM_PBX_USERNAME, username),
                new BasicNameValuePair(AppConstants.PARAM_PBX_PASSWORD, password)
        );
    }


    public boolean loginSuccessful(String psResult){
        if(TextUtils.isEmpty(psResult)){
            return false;
        }

        boolean success = false;
        Log.i("Tes5t" , psResult);
        try {
            Gson gson   = new GsonBuilder().create();

            Type type   = new TypeToken<ResponseBase<LoginResult>>() {
            }.getType();
            ResponseBase<LoginResult> responseBase = gson.fromJson(psResult, type);
            LoginResult loginResult = new LoginResult();
            loginResult = responseBase.data;
            String jsonstring = loginResult.token;
            Log.i("ResultLogin" , jsonstring);
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
       // PBXTransaction pbxTransaction = new PBXTransaction();
        ArrayList<Transaction> list     = new ArrayList<Transaction>();
        //if(responseBase == null || responseBase.response == null || responseBase.response.Table == null){
            if(responseBase == null || responseBase.code != 0){
            return list;
        }

//        for(PBXTransaction pbxTransaction:list.){
//            list.add(pbxTransaction.getTransactionObject());
//        }
//        for(int i=0; i<list.size(); i++){
//            list.add(pbxTransaction.getTransactionObject());
//            System.out.println(list.get(i));
//        }
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

//    public String[] getOperatorNamesResult(String result){
//        Gson gson = new GsonBuilder().create();
//
//        Type type   = new TypeToken<ResponseBase<Party>>(){}.getType();
//
//        ResponseBase<Party> responseBase  = gson.fromJson(result, type);
//
////        if(responseBase == null || responseBase.response == null || responseBase.response.Table == null){
////            return new String[0];
////        }
//
//        Party[] parties     = responseBase.response.Table;
//        String[] operators  = new String[parties.length];
//
//        for(int i=0; i<parties.length; i++){
//            operators[i]    = parties[i].PartyName;
//        }
//
//        return operators;
//    }

    public void retailerpayment (String psConsumerNumber,double pdAmount ){

    }
}