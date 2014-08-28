package com.mom.app.model.pbxpl;

import android.content.Context;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.mom.app.identifier.PinType;
import com.mom.app.model.AsyncDataEx;
import com.mom.app.model.AsyncListener;
import com.mom.app.model.AsyncResult;
import com.mom.app.model.DataExImpl;
import com.mom.app.model.xml.PullParser;
import com.mom.app.utils.AppConstants;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.NameValuePair;

import java.io.ByteArrayInputStream;
import java.util.HashMap;

/**
 * Created by vaibhavsinha on 7/6/14.
 */
public class PBXPLDataExImpl extends DataExImpl implements AsyncListener<String> {

    private String LOG_TAG          = AppConstants.LOG_PREFIXPBX + "DATAEX_PBX";
    public PBXPLDataExImpl(Context pContext, AsyncListener pListener){
        this._listener              = pListener;
        this._applicationContext    = pContext;
        checkConnectivity(pContext);
    }

    @Override
    public void onTaskSuccess(String result, Methods callback) {
        switch (callback){
            case LOGIN:
                boolean bSuccess    = loginSuccessful(result);
                if(_listener != null){
                    _listener.onTaskSuccess((new Boolean(bSuccess)).toString(), callback);
                }
                break;

            case GET_BALANCE:
                Log.d(LOG_TAG, "TaskComplete: getBalance method, result: " + result);
                //float balance = extractBalance(result);
                 Balance balance      = extractJsonBalance();
                Log.d(LOG_TAG, "TaskCompleteNew: resultbal: " + balance);
                if (_listener != null) {
                    _listener.onTaskSuccess(balance, Methods.GET_BALANCE);
                }

                break;
        }
    }

    @Override
    public void onTaskError(AsyncResult pResult, Methods callback) {

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
        String url				= AppConstants.URL_PBX_PLATFORM_Test;

        Log.d(LOG_TAG, "Creating dataEx for getBalancePBX call");
        AsyncDataEx dataEx		    = new AsyncDataEx(this, url, Methods.GET_BALANCE);

        Log.d(LOG_TAG, "Calling web service via async");

        dataEx.execute(
//                new BasicNameValuePair(
//                        MOMConstants.PARAM_PBX_LOGIN_MOBILENUMBER,
//                        LocalStorage.getString(_applicationContext,"8976893713")
//                ),
//                new BasicNameValuePair(
//                        MOMConstants.PARAM_PBX_SERVICE,
//                        LocalStorage.getString(_applicationContext, "SL")
                new BasicNameValuePair("RN","8976893713"),
                new BasicNameValuePair("Service", "SL")



        );
        Log.d(LOG_TAG, url);
        Log.d(LOG_TAG, "Called web service via async");
    }

    public void login(NameValuePair...params){
        String loginUrl				= AppConstants.URL_PBX_PLATFORM;
        Log.i("PBXPL", "Calling Async login");

        AsyncDataEx dataEx		    = new AsyncDataEx(this, loginUrl, Methods.LOGIN);

        dataEx.execute(params);
    }

    public boolean loginSuccessful(String psResult){
        if("".equals(psResult)){
            return false;
        }

        try {
           // PullParser parser   = new PullParser(new ByteArrayInputStream(psResult.getBytes()));
          //  String response     = parser.getTextResponse();

         //   Log.i("LoginResult", "Response: " + response);
          //  String[] strArrayResponse = response.split("~");
            Log.i("LoginResult", "Response: " + psResult);
            String[] strArrayResponse = psResult.split("~");
            int i = Integer.parseInt(strArrayResponse[0]);

            if(i == AppConstants.PBX_PL_LOGIN_SUCCESS){
                Log.i("LoginResult", "Login successful"+ i);
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public void payBill(String psSubscriberId, double pdAmount, String psOperatorId, String psCustomerMobile, String psConsumerName, HashMap<String, String> psExtraParamsMap) {

    }

    @Override
    public void getBillAmount(String psOperatorId, String psSubscriberId) {

    }

    @Override
    public void getTransactionHistory() {

    }

    @Override
    public void changePin(PinType pinType, String psOldPin, String psNewPin) {

    }



    public float extractBalance(String psResult){
        if("".equals(psResult)){
            return 0;
        }

        try {
            PullParser parser   = new PullParser(new ByteArrayInputStream(psResult.getBytes()));
            String response     = parser.getTextResponse();

            Log.d(LOG_TAG, "Response: " + response);
//            Log.d(LOG_TAG, "Response: " + psResult);
            return Float.parseFloat(response);
//            return Float.parseFloat(psResult);

        }catch (Exception e){
            e.printStackTrace();
        }

        return 0;
    }


    public Balance extractJsonBalance(){
        String json = "{balance: 10, status: \"success\"}";
        return new GsonBuilder().create().fromJson(json, Balance.class);

    }
}
