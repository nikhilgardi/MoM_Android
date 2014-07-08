package com.mom.app.model.newpl;

import android.content.Context;
import android.util.Log;

import com.mom.app.model.AsyncDataEx;
import com.mom.app.model.AsyncListener;
import com.mom.app.model.DataExImpl;
import com.mom.app.model.local.LocalStorage;
import com.mom.app.model.xml.PullParser;
import com.mom.app.utils.MOMConstants;
import com.mom.app.utils.MiscUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.ksoap2.serialization.SoapObject;

import java.io.ByteArrayInputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by vaibhavsinha on 7/5/14.
 */
public class NewPLDataExImpl extends DataExImpl implements AsyncListener{

    private String LOG_TAG     = "DATAEX_NEW";

    public NewPLDataExImpl(AsyncListener pListener, Context pContext){
        super.SOAP_ADDRESS = "http://msvc.money-on-mobile.net/WebServiceV3Client.asmx";
        this._applicationContext    = pContext;
        this._listener              = pListener;
    }

//    public NewPLDataExImpl(AsyncListener pListener){
//        this._listener  = pListener;
//    }

    @Override
    public void onTaskComplete(String result, Methods callback) {
        Log.d(LOG_TAG, "onTaskComplete");
        switch (callback){
            case LOGIN:
                Log.d(LOG_TAG, "TaskComplete: Login method, result: " + result);
                boolean bLoginSuccess    = loginSuccessful(result);
                if(_listener != null){
                    _listener.onTaskComplete((new Boolean(bLoginSuccess)).toString(), null);
                }
            break;
            case VERIFY_TPIN:
                Log.d(LOG_TAG, "TaskComplete: verifyTPin method, result: " + result);
                boolean bTPinSuccess    = tpinVerified(result);
                if(_listener != null){
                    _listener.onTaskComplete((new Boolean(bTPinSuccess)).toString(), null);
                }
                break;
            case GET_BALANCE:
                Log.d(LOG_TAG, "TaskComplete: getBalance method, result: " + result);
                double balance      = extractBalance(result);
                Locale lIndia       = new Locale("en", "IN");
                NumberFormat form   = NumberFormat.getCurrencyInstance(lIndia);

                String sBalance      = form.format(balance);

                if(_listener != null) {
                    _listener.onTaskComplete(sBalance, Methods.GET_BALANCE);
                }
            break;
        }
    }

//    public double getBalance(){
//        HashMap<String, String> params  = new HashMap<String, String>();
//        params.put(MOMConstants.PARAM_NEW_OPERATOR_ID, LocalStorage.getString(_applicationContext, MOMConstants.PARAM_NEW_OPERATOR_ID));
//        params.put(MOMConstants.PARAM_NEW_COMPANY_ID, LocalStorage.getString(_applicationContext, MOMConstants.PARAM_NEW_COMPANY_ID));
//        params.put(MOMConstants.PARAM_NEW_ACCESS_ID, "kjasd876");
//
//        SoapObject soapObject = getResponse("getBalanceByCustomerId", params);
//
//        if(soapObject == null){
//            Log.i("NewPLDataEx", "No SOAP response");
//            return 0.;
//        }
//
//        String sBalance = soapObject.getPrimitivePropertySafelyAsString("getBalanceByCustomerIdResult");
//
//        if(sBalance != null && !sBalance.trim().equals("")){
//            return Double.parseDouble(sBalance);
//        }
//
//        return 0.;
//    }

    public void login(NameValuePair...params){
        String loginUrl				= MOMConstants.URL_NEW_PLATFORM + MOMConstants.SVC_NEW_METHOD_LOGIN;

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

            Log.d(LOG_TAG, "Response: " + response);
            String[] strArrayResponse = response.split("~");

            int i = Integer.parseInt(strArrayResponse[0]);

            if(i == MOMConstants.NEW_PL_LOGIN_SUCCESS){
                Log.d(LOG_TAG, "Login successful");
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    public void getBalance(){
        String url				= MOMConstants.URL_NEW_PLATFORM + MOMConstants.SVC_NEW_METHOD_GET_BALANCE;

        Log.d(LOG_TAG, "Creating dataEx for getBalance call");
        AsyncDataEx dataEx		    = new AsyncDataEx(this, url, Methods.GET_BALANCE);

        Log.d(LOG_TAG, "Calling web service via async");

        dataEx.execute(
                new BasicNameValuePair(
                        MOMConstants.PARAM_NEW_OPERATOR_ID,
                        LocalStorage.getString(_applicationContext, MOMConstants.PARAM_NEW_CUSTOMER_ID)
                ),
                new BasicNameValuePair(
                        MOMConstants.PARAM_NEW_COMPANY_ID,
                        LocalStorage.getString(_applicationContext, MOMConstants.PARAM_NEW_COMPANY_ID)
                ),
                new BasicNameValuePair(
                        MOMConstants.PARAM_NEW_ACCESS_ID,
                        "haduiy23d"
                )
        );

        Log.d(LOG_TAG, "Called web service via async");
    }

    public double extractBalance(String psResult){
        if("".equals(psResult)){
            return 0.;
        }

        try {
            PullParser parser   = new PullParser(new ByteArrayInputStream(psResult.getBytes()));
            String response     = parser.getTextResponse();

            Log.d(LOG_TAG, "Response: " + response);
            return Double.parseDouble(response);
        }catch (Exception e){
            e.printStackTrace();
        }

        return 0.;
    }

    public void verifyTPin(String psTPin){
        String loginUrl				= MOMConstants.URL_NEW_PLATFORM + MOMConstants.SVC_NEW_METHOD_CHECK_TPIN;

        AsyncDataEx dataEx		    = new AsyncDataEx(this, loginUrl, Methods.VERIFY_TPIN);

        dataEx.execute(
                new BasicNameValuePair(MOMConstants.PARAM_NEW_TPIN, psTPin),
                new BasicNameValuePair(
                        MOMConstants.PARAM_NEW_CUSTOMER_ID,
                        LocalStorage.getString(_applicationContext, MOMConstants.PARAM_NEW_CUSTOMER_ID)
                ),
                new BasicNameValuePair(
                        MOMConstants.PARAM_NEW_COMPANY_ID,
                        LocalStorage.getString(_applicationContext, MOMConstants.PARAM_NEW_COMPANY_ID)
                ),
                new BasicNameValuePair(MOMConstants.PARAM_NEW_ACCESS_ID, "KJHASFD")
                );
    }

    public boolean tpinVerified(String psResult){
        if("".equals(psResult)){
            return false;
        }

        try {
            PullParser parser   = new PullParser(new ByteArrayInputStream(psResult.getBytes()));
            String response     = parser.getTextResponse();

            Log.d(LOG_TAG, "Response: " + response);
            String[] strArrayResponse = response.split("~");

            int i = Integer.parseInt(strArrayResponse[0]);

            if(i == MOMConstants.NEW_PL_TPIN_VERIFIED){
                Log.d(LOG_TAG, "TPin verified");
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }
}