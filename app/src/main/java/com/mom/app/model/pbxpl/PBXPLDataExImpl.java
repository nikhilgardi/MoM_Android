package com.mom.app.model.pbxpl;

import android.content.Context;
import android.support.annotation.NonNull;
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
import com.mom.app.model.Transaction;
import com.mom.app.model.local.EphemeralStorage;

import com.mom.app.model.Last5Transactions;

import com.mom.app.model.local.PersistentStorage;

import com.mom.app.utils.AppConstants;

import org.apache.http.message.BasicNameValuePair;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by vaibhavsinha on 7/6/14.
 */
public class PBXPLDataExImpl extends DataExImpl implements AsyncListener<String> {
    static String _LOG              = "PBX_DATA";
    static String LOG_TAG          = AppConstants.LOG_PREFIX + "PBX_DATA";
    private String _operatorId      = null;
    private static int _SUCCESS     = 200;
    String _deviceRegId             = null;

    // String jsonStr =    "{\"Table\":[{\"PartyROWID\":92420,\"PartyRMN\":\"9769496026\",\"PartyName\":\"Akshay\",\"PartyGUID\":\"9163b4dd-f23d-41fd-99ab-7c0f57c9c7ed\",\"PartyEnum\":null,\"PartyTypeEnum\":16,\"userName\":\"Software\"}]}" ;
    public PBXPLDataExImpl(Context pContext, AsyncListener pListener){
        this._listener              = pListener;
        this._applicationContext    = pContext;
        checkConnectivity(pContext);
        _deviceRegId                = PersistentStorage.getInstance(pContext).getString(GcmUtil.PROPERTY_REG_ID, "");
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
                    _listener.onTaskSuccess(getOperatorNamesResult(result), callback);
                    break;
                case TRANSACTION_HISTORY:
                  //  _listener.onTaskSuccess(extractTransactions(result), callback);
                    _listener.onTaskSuccess(extractTransactionshistory(result), callback);
                    Log.i("History" , result);
                    break;
                case MOBILE_RECHARGEPBX:
                    Log.d(LOG_TAG, "TaskComplete: rechargeMobile method, result: " + result);
                    Log.i(LOG_TAG, "TaskComplete: rechargeMobile method, result: " + result);
                    if (_listener != null) {
                        _listener.onTaskSuccess(getRechargeResult(result), Methods.MOBILE_RECHARGEPBX);
                    }
                    break;

                case RECHARGE_DTH:
                    Log.d(LOG_TAG, "TaskComplete: rechargeMobile method, result: " + result);
                    Log.i(LOG_TAG, "TaskComplete: rechargeDTH method, result: " + result);
                    if (_listener != null) {
                        _listener.onTaskSuccess(getRechargeResult(result), Methods.RECHARGE_DTH);
                    }
                    break;
                case PAY_BILL:
                    Log.d(LOG_TAG, "TaskComplete: payBill method, result: " + result);
                    Log.i(LOG_TAG, "TaskComplete: payBill method, result: " + result);
                    if (_listener != null) {
                        _listener.onTaskSuccess(getRechargeResult(result), Methods.PAY_BILL);
                    }
                    break;
                case BALANCE_TRANSFER:
                    Log.d(LOG_TAG, "TaskComplete: retailerpayment method, result: " + result);
                    Log.i(LOG_TAG, "TaskComplete: retailerpayment method, result: " + result);

                    if (_listener != null) {
                        _listener.onTaskSuccess(getInternalBalTransfer(result), Methods.BALANCE_TRANSFER);
                    }
                    break;

                case CHANGE_PASSWORD:
                    Log.d(LOG_TAG, "TaskComplete: changeMPin method, result: " + result);

                    result              = getchangePasswordResult(result);

                    if (_listener != null) {
                        _listener.onTaskSuccess(result, Methods.CHANGE_PASSWORD);
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
    public void rechargeMobilePBX(
                                   String psConsumerNumber,
                                   String psOperator,
                                   double pdAmount) {

        if(
                psConsumerNumber == null || "".equals(psConsumerNumber)
            //  || pdAmount < 1 || psOperator == null || "".equals(psOperator)
                ){

            if(_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.LOGIN);
            }
            return;
        }

        _operatorId                 = psOperator;

        String url				    = AppConstants.URL_PBX_PLATFORM_APP;

        AsyncDataEx dataEx		    = new AsyncDataEx(this, url, Methods.MOBILE_RECHARGEPBX);

        dataEx.execute(

                new BasicNameValuePair(AppConstants.PARAM_PBX_SERVICE, "RECHARGEMOBILE"),
                new BasicNameValuePair(AppConstants.PARAM_PBX_RMN, "9769496026"),
                new BasicNameValuePair("customerNumber" ,psConsumerNumber),
                new BasicNameValuePair("operatorShortCode" , "AIR"),
                new BasicNameValuePair("amount", String.valueOf(Math.round(pdAmount))),
                new BasicNameValuePair(AppConstants.PARAM_PBX_IDENTIFIER   , "1234"),
                new BasicNameValuePair(AppConstants.PARAM_PBX_ORIGINID , "5555"),
                new BasicNameValuePair(AppConstants.PARAM_PBX_CLIENTTOKEN , "4")


        );
    }

    @Override
    public void rechargeDTH(String psSubscriberId, double pdAmount, String psOperator, String psCustomerMobile){
        if(
                psSubscriberId == null || "".equals(psSubscriberId)
                       // || pdAmount < 1 || psOperator == null || "".equals(psOperator)
                ){

            if(_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.LOGIN);
            }
        }

        _operatorId                 = psOperator;

        String url				    = AppConstants.URL_PBX_PLATFORM_APP;

        AsyncDataEx dataEx		    = new AsyncDataEx(this, url, Methods.RECHARGE_DTH);

        dataEx.execute(
                new BasicNameValuePair(AppConstants.PARAM_PBX_SERVICE, "DTH"),
                new BasicNameValuePair(AppConstants.PARAM_PBX_RMN, "9769496026"),
                new BasicNameValuePair(AppConstants.PARAM_PBX_CUSTOMERNUMBER, psSubscriberId),
                new BasicNameValuePair(AppConstants.PARAM_PBX_AMOUNT, String.valueOf(Math.round(pdAmount))),
                new BasicNameValuePair(AppConstants.PARAM_PBX_OPERTAORSHORTCODE , "TSK"),
                new BasicNameValuePair(AppConstants.PARAM_PBX_IDENTIFIER   , "1234"),
                new BasicNameValuePair(AppConstants.PARAM_PBX_ORIGINID , "5555"),
                new BasicNameValuePair(AppConstants.PARAM_PBX_CLIENTTOKEN , "4")



        );
    }

    public void payBill(
            String psSubscriberId,
            double pdAmount,
            String psOperatorId,
            String psCustomerMobile,
            String psConsumerName,
            HashMap<String, String> pExtraParamsMap
    ){

        _operatorId                 = psOperatorId;

        String url				    = AppConstants.URL_PBX_PLATFORM_APP ;

        AsyncDataEx dataEx		    = new AsyncDataEx(this, url, Methods.PAY_BILL);

        String strCustomerNumber    = psSubscriberId;



        Log.d("NEW_PL_DATA", "Going to pay bill for strCustomerNumber: " + strCustomerNumber);

        dataEx.execute(

                new BasicNameValuePair(AppConstants.PARAM_PBX_SERVICE, "BILLPAY"),
                new BasicNameValuePair(AppConstants.PARAM_PBX_RMN, "9769496026"),
                new BasicNameValuePair(AppConstants.PARAM_PBX_CUSTOMERNUMBER, strCustomerNumber),
                new BasicNameValuePair(AppConstants.PARAM_PBX_AMOUNT, String.valueOf(Math.round(pdAmount))),
                new BasicNameValuePair(AppConstants.PARAM_PBX_OPERTAORSHORTCODE , "BAI"),
                new BasicNameValuePair(AppConstants.PARAM_PBX_IDENTIFIER   , "1234"),
                new BasicNameValuePair(AppConstants.PARAM_PBX_ORIGINID , "5555"),
                new BasicNameValuePair(AppConstants.PARAM_PBX_CLIENTTOKEN , "4")
        );
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
      //      Log.i("ResultLogin" , jsonstring);
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
    public String getRechargeResult(String psResult){
        String response = null;
        if(TextUtils.isEmpty(psResult)){
            return null;
        }


        Log.i("Tes5t" , psResult);
        try {
            Gson gson   = new GsonBuilder().create();

            Type type   = new TypeToken<ResponseBase<RechargeResponse>>() {
            }.getType();
            ResponseBase<RechargeResponse> responseBase = gson.fromJson(psResult, type);
            RechargeResponse rechargeResponse = new RechargeResponse();
            rechargeResponse = responseBase.data;


            String response1   = rechargeResponse.transactionId;
            Log.d(LOG_TAG, "Response: " + response1);
            String response2   = Double.toString(rechargeResponse.amount);
            Log.d(LOG_TAG, "Response1: " + response2);
            String response3   = Double.toString(rechargeResponse.Balance);
            Log.d(LOG_TAG, "Response2: " + response3);
             response = "Transaction Id: " + response1 + " " + "Amount" + response2 + " " + "Balance" + " " + response3;
            return response;
        }catch (Exception e){
            e.printStackTrace();
        }

        return response;
    }
    public String getInternalBalTransfer(String psResult){
        String intenalbal = null;
        if(TextUtils.isEmpty(psResult)){
            return null;
        }


        Log.i("Tes5t" , psResult);
        try {
            Gson gson   = new GsonBuilder().create();

            Type type   = new TypeToken<ResponseBase>() {
            }.getType();
            ResponseBase responseBase = gson.fromJson(psResult, type);

            intenalbal= responseBase.data.toString();
            Log.d(LOG_TAG, "ResponseInternalBal: " + intenalbal);
            Log.i(LOG_TAG, "ResponseInternalBal: " + intenalbal);

            return intenalbal;
        }catch (Exception e){
            e.printStackTrace();
        }

        return intenalbal;
    }


    @Override
    public void getBillAmount(String psOperatorId, String psSubscriberId) {

    }

    @Override
    public void getTransactionHistory() {
        String userName             = EphemeralStorage.getInstance(_applicationContext).getString(
                AppConstants.PARAM_PBX_RMN, null
        );

        String token                = EphemeralStorage.getInstance(_applicationContext).getString(
                AppConstants.PARAM_PBX_TOKEN, null
        );

        AsyncDataEx dataEx		    = new AsyncDataEx(this, AppConstants.URL_PBX_PLATFORM_APP, Methods.TRANSACTION_HISTORY);

        dataEx.execute(
                new BasicNameValuePair(AppConstants.PARAM_PBX_SERVICE, AppConstants.SVC_PBX_TRANSACTION_HISTORY),
                new BasicNameValuePair(AppConstants.PARAM_PBX_RMN, userName),
                new BasicNameValuePair(AppConstants.PARAM_PBX_TOKEN, token),
                new BasicNameValuePair(AppConstants.PARAM_PBX_IDENTIFIER, "1234"),
                new BasicNameValuePair(AppConstants.PARAM_PBX_ORIGINID, "5555"),
                new BasicNameValuePair(AppConstants.PARAM_PBX_CLIENTTOKEN, "4")
        );
    }

    public ArrayList<Transaction> extractTransactions(String result){
        Gson gson = new GsonBuilder().create();

        Type type   = new TypeToken<ResponseBase<PBXTransaction>>(){}.getType();

        ResponseBase<PBXTransaction> responseBase  = gson.fromJson(result, type);

        ArrayList<Transaction> list     = new ArrayList<Transaction>();
        //if(responseBase == null || responseBase.response == null || responseBase.response.Table == null){
            if(responseBase == null || responseBase.code != 0){
            return list;
        }

//        for(PBXTransaction pbxTransaction:responseBase){
//            list.add(pbxTransaction.getTransactionObject());
//        }
//        for(int i=0; i<list.size(); i++){
//            list.add(pbxTransaction.getTransactionObject());
//            System.out.println(list.get(i));
//            Log.i("test" , list.get(i).toString());
//        }
        return list;
    }
    public ArrayList<PBXLast5Transactions> extractTransactionshistory(String result){
        Gson gson = new GsonBuilder().create();

        Type type   = new TypeToken<ResponseBase<PBXLast5Transactions[]>>(){}.getType();

        ResponseBase<PBXLast5Transactions> responseBase  = gson.fromJson(result, type);
        PBXLast5Transactions last5Transactions = new PBXLast5Transactions();
        PBXLast5Transactions[] p = gson.fromJson(responseBase.data.toString(), type);

        ArrayList<PBXLast5Transactions> list     = new ArrayList<PBXLast5Transactions>();
        //if(responseBase == null || responseBase.response == null || responseBase.response.Table == null){
        if(responseBase == null || responseBase.code != 0){
            return list;
        }

//        for(PBXLast5Transactions pbxTransactionlast:last5Transaction){
//
//            //list.add(pbxTransactionlast.getTransactionHistory());
//        }
//        for(int i=0; i<list.size(); i++){
//            list.add(pbxTransactionlast.getTransactionHistory());
//            System.out.println(list.get(i));
//            Log.i("test" , list.get(i).toString());
//        }
        return list;
    }
    @Override
    public  void changePassword( String psOldPin, String psNewPin){
        String loginUrl				= AppConstants.URL_PBX_PLATFORM_APP;

        Log.i(_LOG, "Calling Async password");

        String userName             = EphemeralStorage.getInstance(_applicationContext).getString(
                AppConstants.LOGGED_IN_USERNAME, null
        );

        String token                = EphemeralStorage.getInstance(_applicationContext).getString(
                AppConstants.PARAM_PBX_TOKEN, null
        );

        AsyncDataEx dataEx		    = new AsyncDataEx(this, loginUrl, Methods.CHANGE_PASSWORD);

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

    public String getchangePasswordResult(String psResult){
        String changepwd = null;

        if(TextUtils.isEmpty(psResult)){
            return null;
        }


        Log.i("Tes5t" , psResult);
        try {
            Gson gson   = new GsonBuilder().create();

            Type type   = new TypeToken<ResponseBase<ChangePBXPassword>>() {
            }.getType();
            ResponseBase<ChangePBXPassword> responseBase = gson.fromJson(psResult, type);
            ChangePBXPassword changepassword = new ChangePBXPassword();
            changepassword= responseBase.data;

            String response1   = Double.toString(changepassword.status);
            Log.d(LOG_TAG, "Response: " + response1);
            Log.i(LOG_TAG, "Response: " + response1);

            String response2   = changepassword.message;
            Log.d(LOG_TAG, "Response1: " + response2);
            Log.i(LOG_TAG, "Response1: " + response2);


            changepwd = "Status: " + response1 + " " + "Message" + response2;
            Log.i(LOG_TAG, "Response: " + changepwd);
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

        AsyncDataEx dataEx		    = new AsyncDataEx(this, url, Methods.GET_OPERATOR_NAMES);
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

//    public List getOperatorNamesResult(String result){
//        Gson gson = new GsonBuilder().create();
//
//        Type type   = new TypeToken<ResponseBase<Party>>(){}.getType();
//
//        ResponseBase<Party> responseBase  = gson.fromJson(result, type);
//       ArrayList <PartyName> partyname = new ArrayList<PartyName>();
//
//
//
//
//        if(responseBase == null || responseBase.data == null){
//            return list ;
//        }
//Party.OperatorList party = new Gson().fromJson(result , Party.OperatorList.class);
//        party = responseBase.data;
//        String test= party.toString();
//        Log.i("OperatorTest" , test);
////        Party[] parties     = responseBas;
//      List operators  = null;
////
////        for(int i=0; i<parties.length; i++){
////            operators[i]    = parties[i].PartyName;
////        }
//
//        return operators;
//    }
        public List getOperatorNamesResult(String result){
        Gson gson = new GsonBuilder().create();

        Type type   = new TypeToken<ResponseBase<Party>>(){}.getType();

        ResponseBase<Party> responseBase  = gson.fromJson(result, type);

          //  Party<Party.OperatorList> myParty=new Party<Party.operatorList>();
            Party party = new Gson().fromJson(result,Party.class);

            Log.i("TEstOPerator" , responseBase.data.toString());
        if(responseBase == null || responseBase.data == null){
            return null;
        }

//        Party[] parties     = responseBase.response.Table;
//        String[] operators  = new String[parties.length];
//
//        for(int i=0; i<parties.length; i++){
//            operators[i]    = parties[i].PartyName;
//        }

        return null;
    }
    public void retailerpayment (String psConsumerNumber,double pdAmount )
    {

        if(
                psConsumerNumber == null || "".equals(psConsumerNumber) ||pdAmount < 1 ){

            if(_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.LOGIN);
            }
            return;
        }
        String userName             = EphemeralStorage.getInstance(_applicationContext).getString(
                AppConstants.PARAM_PBX_RMN, null
        );


        String url				    = AppConstants.URL_PBX_PLATFORM_APP;
        AsyncDataEx dataEx		    = new AsyncDataEx(this, url, Methods.BALANCE_TRANSFER);

        dataEx.execute(
                new BasicNameValuePair(AppConstants.PARAM_PBX_SERVICE, AppConstants.SVC_PBX_INTERNAL_BAL_TRANSFER),
                new BasicNameValuePair(AppConstants.PARAM_PBX_PARENT_RMN, userName),
                new BasicNameValuePair(AppConstants.PARAM_PBX_CHILD_RMN, psConsumerNumber),
                new BasicNameValuePair(AppConstants.PARAM_PBX_AMOUNT, String.valueOf(Math.round(pdAmount))),
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