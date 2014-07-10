package com.mom.app.model.newpl;

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
 * Created by vaibhavsinha on 7/5/14.
 */
public class NewPLDataExImpl extends DataExImpl implements AsyncListener<String>{

    private String LOG_TAG     = "DATAEX_NEW";

    public NewPLDataExImpl(Context pContext, AsyncListener pListener){
        super.SOAP_ADDRESS = "http://msvc.money-on-mobile.net/WebServiceV3Client.asmx";
        this._applicationContext    = pContext;
        this._listener              = pListener;
    }


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
                float balance      = extractBalance(result);

                if(_listener != null) {
                    _listener.onTaskComplete(balance, Methods.GET_BALANCE);
                }
            break;
            case RECHARGE_MOBILE:
                Log.d(LOG_TAG, "TaskComplete: rechargeMobile method, result: " + result);
                if(_listener != null) {
                    _listener.onTaskComplete(getRechargeResult(result), Methods.RECHARGE_MOBILE);
                }
            break;
            case RECHARGE_DTH:
                Log.d(LOG_TAG, "TaskComplete: rechargeDTH method, result: " + result);
                if(_listener != null) {
                    _listener.onTaskComplete(getRechargeResult(result), Methods.RECHARGE_DTH);
                }
            break;
            case PAY_BILL:
                Log.d(LOG_TAG, "TaskComplete: payBill method, result: " + result);

                if(_listener != null) {
                    _listener.onTaskComplete(getRechargeResult(result), Methods.PAY_BILL);
                }
            break;
            case GET_BILL_AMOUNT:
                Log.d(LOG_TAG, "TaskComplete: getBillAmount method, result: " + result);
                float billAmount      = extractBalance(result);

                if(_listener != null) {
                    _listener.onTaskComplete(billAmount, Methods.GET_BILL_AMOUNT);
                }
            break;
        }
    }

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
                        MOMConstants.PARAM_NEW_STR_ACCESS_ID,
                        "haduiy23d"
                )
        );

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

    public void verifyTPin(String psTPin){
        String url				= MOMConstants.URL_NEW_PLATFORM + MOMConstants.SVC_NEW_METHOD_CHECK_TPIN;

        AsyncDataEx dataEx		    = new AsyncDataEx(this, url, Methods.VERIFY_TPIN);

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
                new BasicNameValuePair(MOMConstants.PARAM_NEW_STR_ACCESS_ID, "KJHASFD")
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


    public void rechargeMobile(String psConsumerNumber, double pdAmount, String psOperator, int pnRechargeType){

        String url				= MOMConstants.URL_NEW_PLATFORM_TXN + MOMConstants.SVC_NEW_METHOD_RECHARGE_MOBILE;

        AsyncDataEx dataEx		    = new AsyncDataEx(this, url, Methods.RECHARGE_MOBILE);

        dataEx.execute(
                new BasicNameValuePair(
                        MOMConstants.PARAM_NEW_INT_CUSTOMER_ID,
                        LocalStorage.getString(_applicationContext, MOMConstants.PARAM_NEW_CUSTOMER_ID)
                ),
                new BasicNameValuePair(
                        MOMConstants.PARAM_NEW_COMPANY_ID,
                        LocalStorage.getString(_applicationContext, MOMConstants.PARAM_NEW_COMPANY_ID)
                ),
                new BasicNameValuePair(MOMConstants.PARAM_NEW_STR_ACCESS_ID, "KJHASFD"),
                new BasicNameValuePair(MOMConstants.PARAM_NEW_STR_MOBILE_NUMBER, psConsumerNumber),
                new BasicNameValuePair(MOMConstants.PARAM_NEW_RECHARGE_AMOUNT, Double.valueOf(pdAmount).toString()),
                new BasicNameValuePair(MOMConstants.PARAM_NEW_OPERATOR, psOperator),
                new BasicNameValuePair(MOMConstants.PARAM_NEW_INT_RECHARGE_TYPE, Integer.valueOf(pnRechargeType).toString())
        );
    }

    public String getRechargeResult(String psResult){
        if("".equals(psResult)){
            return null;
        }

        try {
            PullParser parser   = new PullParser(new ByteArrayInputStream(psResult.getBytes()));
            String response     = parser.getTextResponse();

            Log.d(LOG_TAG, "Response: " + response);
            return response;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public void rechargeDTH(String psSubscriberId, double pdAmount, String psOperator, String psCustomerMobile){
        String url				= MOMConstants.URL_NEW_PLATFORM_TXN + MOMConstants.SVC_NEW_METHOD_RECHARGE_DTH;

        AsyncDataEx dataEx		    = new AsyncDataEx(this, url, Methods.RECHARGE_DTH);

        dataEx.execute(
                new BasicNameValuePair(
                        MOMConstants.PARAM_NEW_INT_CUSTOMER_ID,
                        LocalStorage.getString(_applicationContext, MOMConstants.PARAM_NEW_CUSTOMER_ID)
                ),
                new BasicNameValuePair(
                        MOMConstants.PARAM_NEW_COMPANY_ID,
                        LocalStorage.getString(_applicationContext, MOMConstants.PARAM_NEW_COMPANY_ID)
                ),
                new BasicNameValuePair(MOMConstants.PARAM_NEW_STR_ACCESS_ID, "KJHASFD"),
                new BasicNameValuePair(MOMConstants.PARAM_NEW_STR_MOBILE_NUMBER, psSubscriberId),
                new BasicNameValuePair(MOMConstants.PARAM_NEW_RECHARGE_AMOUNT, String.valueOf(pdAmount)),
                new BasicNameValuePair(MOMConstants.PARAM_NEW_OPERATOR, psOperator),
                new BasicNameValuePair(MOMConstants.PARAM_NEW_CUSTOMER_NUMBER, psCustomerMobile)
        );
    }

    public void payBill(
                        String psSubscriberId,
                        double pdAmount,
                        String psOperatorId,
                        String psCustomerMobile,
                        String psConsumerName,
                        HashMap<String, String> psExtraParamsMap
                    ){

        String url				= MOMConstants.URL_NEW_PLATFORM_TXN + MOMConstants.SVC_NEW_METHOD_BILL_PAYMENT;

        AsyncDataEx dataEx		    = new AsyncDataEx(this, url, Methods.PAY_BILL);

        String strCustomerNumber    = psSubscriberId;

        String sUserId              = LocalStorage.getString(_applicationContext, MOMConstants.PARAM_NEW_USER_ID);

        if (
                MOMConstants.OPERATOR_ID_BEST_ELECTRICITY.equals(psOperatorId) ||
                MOMConstants.OPERATOR_ID_RELIANCE_ENERGY.equals(psOperatorId) ||
                MOMConstants.OPERATOR_ID_MAHANAGAR_GAS.equals(psOperatorId)){

            strCustomerNumber   = psSubscriberId + "|" + psCustomerMobile
                                + "|" + sUserId;

        } else if (MOMConstants.OPERATOR_ID_SBE.equals(psOperatorId)) {

            strCustomerNumber   = "SBE|" + psSubscriberId + "|" + psConsumerName + "|"
                                + "|" + psExtraParamsMap.get(MOMConstants.PARAM_NEW_RELIANCE_SBE_NBE)
                                + "|" + psExtraParamsMap.get(MOMConstants.PARAM_NEW_SPECIAL_OPERATOR);

        } else if (MOMConstants.OPERATOR_ID_NBE.equals(psOperatorId)) {
            strCustomerNumber   = "NBE|" + psSubscriberId + "|" + psConsumerName + "|"
                    + "|" + psExtraParamsMap.get(MOMConstants.PARAM_NEW_RELIANCE_SBE_NBE)
                    + "|" + psExtraParamsMap.get(MOMConstants.PARAM_NEW_SPECIAL_OPERATOR_NBE);

        }

        Log.d("NEW_PL_DATA", "Going to pay bill for strCustomerNumber: " + strCustomerNumber);

        dataEx.execute(
                new BasicNameValuePair(
                        MOMConstants.PARAM_NEW_INT_CUSTOMER_ID,
                        LocalStorage.getString(_applicationContext, MOMConstants.PARAM_NEW_CUSTOMER_ID)
                ),
                new BasicNameValuePair(
                        MOMConstants.PARAM_NEW_COMPANY_ID_CAMEL_CASE,
                        LocalStorage.getString(_applicationContext, MOMConstants.PARAM_NEW_COMPANY_ID)
                ),
                new BasicNameValuePair(MOMConstants.PARAM_NEW_STR_ACCESS_ID, "KJHASFD"),
                new BasicNameValuePair(MOMConstants.PARAM_NEW_STR_CUSTOMER_NUMBER, strCustomerNumber),
                new BasicNameValuePair(MOMConstants.PARAM_NEW_DEC_BILL_AMOUNT, String.valueOf(pdAmount)),
                new BasicNameValuePair(MOMConstants.PARAM_NEW_INT_OPERATOR_ID_BILL_PAY, psOperatorId)
        );
    }

    public void getBillAmount(String psOperatorId, String psSubscriberId){

        String url				= MOMConstants.URL_NEW_PLATFORM_GET_BILL;

        AsyncDataEx dataEx		    = new AsyncDataEx(this, url, Methods.PAY_BILL);

    }
}