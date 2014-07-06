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
import org.ksoap2.serialization.SoapObject;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.List;

/**
 * Created by vaibhavsinha on 7/5/14.
 */
public class NewPLDataExImpl extends DataExImpl implements AsyncListener{
    public NewPLDataExImpl(String psWSUrl, Context pContext){
        SOAP_ADDRESS = "http://msvc.money-on-mobile.net/WebServiceV3Client.asmx";
        this._applicationContext    = pContext;
    }

    public NewPLDataExImpl(AsyncListener pListener){
        this._listener  = pListener;
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
        }
    }

    public double getBalance(){
        HashMap<String, String> params  = new HashMap<String, String>();
        params.put(MOMConstants.PARAM_NEW_OPERATOR_ID, LocalStorage.getString(_applicationContext, MOMConstants.PARAM_NEW_OPERATOR_ID));
        params.put(MOMConstants.PARAM_NEW_COMPANY_ID, LocalStorage.getString(_applicationContext, MOMConstants.PARAM_NEW_COMPANY_ID));
        params.put(MOMConstants.PARAM_NEW_ACCESS_ID, "kjasd876");

        SoapObject soapObject = getResponse("getBalanceByCustomerId", params);

        if(soapObject == null){
            Log.i("NewPLDataEx", "No SOAP response");
            return 0.;
        }

        String sBalance = soapObject.getPrimitivePropertySafelyAsString("getBalanceByCustomerIdResult");

        if(sBalance != null && !sBalance.trim().equals("")){
            return Double.parseDouble(sBalance);
        }

        return 0.;
    }

    public void login(NameValuePair...params){
        String loginUrl				= MOMConstants.URL_NEW_PLATFORM + MOMConstants.SVC_METHOD_LOGIN_NEW;

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

            if(i == MOMConstants.NEW_PL_LOGIN_SUCCESS){
                Log.i("LoginResult", "Login successful");
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }
}