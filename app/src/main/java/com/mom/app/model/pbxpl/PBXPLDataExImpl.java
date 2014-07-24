package com.mom.app.model.pbxpl;

import android.content.Context;
import android.util.Log;

import com.mom.app.model.AsyncDataEx;
import com.mom.app.model.AsyncListener;
import com.mom.app.model.DataExImpl;
import com.mom.app.model.local.LocalStorage;
import com.mom.app.model.xml.PullParser;
import com.mom.app.utils.MOMConstants;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.ByteArrayInputStream;
import java.util.HashMap;

/**
 * Created by vaibhavsinha on 7/6/14.
 */
public class PBXPLDataExImpl extends DataExImpl implements AsyncListener<String> {
    private String LOG_TAG     = "DATAEX_PBX";
    public PBXPLDataExImpl(AsyncListener pListener, Context context){
        this._listener              = pListener;
        this._applicationContext    = context;
    }

    @Override
    public void onTaskComplete(String result, Methods callback) {
        switch (callback){
            case LOGIN:
                boolean bSuccess    = loginSuccessful(result);
                if(_listener != null){
                    _listener.onTaskComplete((new Boolean(bSuccess)).toString(), null);
                }
                break;
            case GET_BALANCE:
                Log.d(LOG_TAG, "TaskComplete: getBalance method, result: " + result);
                float balance      = extractBalance(result);

                if(_listener != null) {
                    _listener.onTaskComplete(balance, Methods.GET_BALANCE);
                }
                break;
        }
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


    public void login(NameValuePair...params){
        String loginUrl				= MOMConstants.URL_PBX_PLATFORM;
        Log.i("PBXPL", "Calling Async login");

        AsyncDataEx dataEx		    = new AsyncDataEx(this, loginUrl, Methods.LOGIN);

        dataEx.execute(params);
    }

    public boolean loginSuccessful(String psResult){
        if("".equals(psResult)){
            return false;
        }

        try {
            PullParser parser   = new PullParser(new ByteArrayInputStream(psResult.getBytes()));
            String response     = parser.getTextResponse();

            Log.i("LoginResult", "Response: " + response);
            String[] strArrayResponse = response.split("~");

            int i = Integer.parseInt(strArrayResponse[0]);

            if(i == MOMConstants.PBX_PL_LOGIN_SUCCESS){
                Log.i("LoginResult", "Login successful");
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
    public void getBalance(){
        String url				= MOMConstants.URL_PBX_PLATFORM_Test;

        Log.d(LOG_TAG, "Creating dataEx for getBalancePBX call");
        AsyncDataEx dataEx		    = new AsyncDataEx(this, url, Methods.GET_BALANCE);

        Log.d(LOG_TAG, "Calling web service via async");

        dataEx.execute(
                new BasicNameValuePair(
                        MOMConstants.PARAM_PBX_LOGIN_MOBILENUMBER,
                        LocalStorage.getString(_applicationContext,"8976893713")
                ),
                new BasicNameValuePair(
                        MOMConstants.PARAM_PBX_SERVICE,
                        LocalStorage.getString(_applicationContext, "SL")
                )

        );
        Log.d(LOG_TAG, url);
        Log.d(LOG_TAG, "Called web service via async");
    }

    public float extractBalance(String psResult){
        if("".equals(psResult)){
            return 0;
        }

        try {
            PullParser parser   = new PullParser(new ByteArrayInputStream(psResult.getBytes()));
            String response     = parser.getTextResponse();

            Log.d(LOG_TAG, "Response: " + response);
            return Float.parseFloat(response);
        }catch (Exception e){
            e.printStackTrace();
        }

        return 0;
    }
}
