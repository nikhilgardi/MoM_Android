package com.mom.app.model.newpl;

import android.content.Context;
import android.util.Log;

import com.mom.app.error.MOMException;
import com.mom.app.identifier.PinType;
import com.mom.app.model.AsyncDataEx;
import com.mom.app.model.AsyncListener;
import com.mom.app.model.AsyncResult;
import com.mom.app.model.DataExImpl;
import com.mom.app.model.local.EphemeralStorage;
import com.mom.app.model.local.LocalStorage;
import com.mom.app.model.xml.PullParser;
import com.mom.app.utils.ConnectionUtil;
import com.mom.app.utils.MOMConstants;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.ByteArrayInputStream;
import java.util.HashMap;

/**
 * Created by vaibhavsinha on 7/5/14.
 */
public class NewPLDataExImpl extends DataExImpl implements AsyncListener<String>{

    private String LOG_TAG          = "DATAEX_NEW";
    private String _operatorId      = null;

    public NewPLDataExImpl(Context pContext, AsyncListener pListener){
        super.SOAP_ADDRESS = "http://msvc.money-on-mobile.net/WebServiceV3Client.asmx";
        this._applicationContext    = pContext;
        this._listener              = pListener;

        checkConnectivity(pContext);
    }


    @Override
    public void onTaskSuccess(String result, Methods callback) {
        Log.d(LOG_TAG, "onTaskSuccess");
        try {
            switch (callback) {
                case CHECK_PLATFORM_DETAILS:
                    Log.d(LOG_TAG, "TaskComplete: Check platform, result: " + result);
                    if (_listener != null) {
                        _listener.onTaskSuccess(result, callback);
                    }
                    break;
                case LOGIN:
                    Log.d(LOG_TAG, "TaskComplete: Login method, result: " + result);
                    boolean bLoginSuccess = loginSuccessful(result);
                    if (_listener != null) {
                        _listener.onTaskSuccess((new Boolean(bLoginSuccess)).toString(), callback);
                    }
                    break;
                case VERIFY_TPIN:
                    Log.d(LOG_TAG, "TaskComplete: verifyTPin method, result: " + result);
                    boolean bTPinSuccess = tpinVerified(result);
                    if (_listener != null) {
                        _listener.onTaskSuccess((new Boolean(bTPinSuccess)).toString(), callback);
                    }
                    break;
                case GET_BALANCE:
                    Log.d(LOG_TAG, "TaskComplete: getBalance method, result: " + result);
                    float balance = extractBalance(result);

                    if (_listener != null) {
                        _listener.onTaskSuccess(balance, Methods.GET_BALANCE);
                    }

                    break;
                case RECHARGE_MOBILE:
                    Log.d(LOG_TAG, "TaskComplete: rechargeMobile method, result: " + result);
                    if (_listener != null) {
                        _listener.onTaskSuccess(getRechargeResult(result), Methods.RECHARGE_MOBILE);
                    }
                    break;
                case RECHARGE_DTH:
                    Log.d(LOG_TAG, "TaskComplete: rechargeDTH method, result: " + result);
                    if (_listener != null) {
                        _listener.onTaskSuccess(getRechargeResult(result), Methods.RECHARGE_DTH);
                    }
                    break;
                case PAY_BILL:
                    Log.d(LOG_TAG, "TaskComplete: payBill method, result: " + result);

                    if (_listener != null) {
                        _listener.onTaskSuccess(getRechargeResult(result), Methods.PAY_BILL);
                    }
                    break;
                case GET_BILL_AMOUNT:
                    Log.d(LOG_TAG, "TaskComplete: getBillAmount method, result: " + result);
                    float billAmount = extractBillAmount(result);

                    if (_listener != null) {
                        _listener.onTaskSuccess(billAmount, Methods.GET_BILL_AMOUNT);
                    }
                    break;
                case TRANSACTION_HISTORY:
                    Log.d(LOG_TAG, "TaskComplete: getTransactionHistory method, result: " + result);
                    if (_listener != null) {
                        _listener.onTaskSuccess(result, Methods.TRANSACTION_HISTORY);
                    }
                    break;
                case CHANGE_PIN:
                    Log.d(LOG_TAG, "TaskComplete: changeMPin method, result: " + result);

                    result              = extractChangePinResponse(result);

                    if (_listener != null) {
                        _listener.onTaskSuccess(result, Methods.CHANGE_PIN);
                    }

                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
            if(_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.GENERAL_FAILURE), callback);
            }
        }
    }

    @Override
    public void onTaskError(AsyncResult pResult, Methods callback) {
        if(_listener != null) {
            _listener.onTaskError(pResult, Methods.GET_BILL_AMOUNT);
        }
    }

    public void checkPlatform(NameValuePair...params){
        if(params == null || params.length < 1){
            if(_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.LOGIN);
            }
        }

        String url                  = MOMConstants.URL_NEW_PL_DETAILS;
        AsyncDataEx dataEx          = new AsyncDataEx(this, url, Methods.CHECK_PLATFORM_DETAILS, AsyncDataEx.HttpMethod.GET);
        dataEx.execute(params);
    }

    public void login(NameValuePair...params){
        if(params == null || params.length < 1){
            if(_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.LOGIN);
            }
        }
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
                        EphemeralStorage.getInstance(_applicationContext).getString(MOMConstants.PARAM_NEW_CUSTOMER_ID, null)
                ),
                new BasicNameValuePair(
                        MOMConstants.PARAM_NEW_COMPANY_ID,
                        EphemeralStorage.getInstance(_applicationContext).getString(MOMConstants.PARAM_NEW_COMPANY_ID, null)
                ),
                new BasicNameValuePair(
                        MOMConstants.PARAM_NEW_STR_ACCESS_ID_SMALL_D,
                        "haduiy23d"
                )
        );

        Log.d(LOG_TAG, "Called web service via async");
    }

    public float extractBalance(String psResult) throws MOMException{
        if(psResult == null || "".equals(psResult.trim())){
            throw new MOMException();
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
        if(psTPin == null || "".equals(psTPin)){
            if(_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.LOGIN);
            }
        }

        String url				= MOMConstants.URL_NEW_PLATFORM + MOMConstants.SVC_NEW_METHOD_CHECK_TPIN;

        AsyncDataEx dataEx		    = new AsyncDataEx(this, url, Methods.VERIFY_TPIN);

        dataEx.execute(
                new BasicNameValuePair(MOMConstants.PARAM_NEW_TPIN, psTPin),
                new BasicNameValuePair(
                        MOMConstants.PARAM_NEW_CUSTOMER_ID,
                        EphemeralStorage.getInstance(_applicationContext).getString(MOMConstants.PARAM_NEW_CUSTOMER_ID, null)
                ),
                new BasicNameValuePair(
                        MOMConstants.PARAM_NEW_COMPANY_ID,
                        EphemeralStorage.getInstance(_applicationContext).getString(MOMConstants.PARAM_NEW_COMPANY_ID, null)
                ),
                new BasicNameValuePair(MOMConstants.PARAM_NEW_STR_ACCESS_ID_SMALL_D, "KJHASFD")
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

        String url				    = MOMConstants.URL_NEW_PLATFORM_TXN + MOMConstants.SVC_NEW_METHOD_RECHARGE_MOBILE;

        AsyncDataEx dataEx		    = new AsyncDataEx(this, url, Methods.RECHARGE_MOBILE);

        dataEx.execute(
                new BasicNameValuePair(
                        MOMConstants.PARAM_NEW_INT_CUSTOMER_ID,
                        EphemeralStorage.getInstance(_applicationContext).getString(MOMConstants.PARAM_NEW_CUSTOMER_ID, null)
                ),
                new BasicNameValuePair(
                        MOMConstants.PARAM_NEW_COMPANY_ID,
                        EphemeralStorage.getInstance(_applicationContext).getString(MOMConstants.PARAM_NEW_COMPANY_ID, null)
                ),
                new BasicNameValuePair(MOMConstants.PARAM_NEW_STR_ACCESS_ID, "KJHASFD"),
                new BasicNameValuePair(MOMConstants.PARAM_NEW_STR_MOBILE_NUMBER, psConsumerNumber),
                new BasicNameValuePair(MOMConstants.PARAM_NEW_RECHARGE_AMOUNT, Double.valueOf(pdAmount).toString()),
                new BasicNameValuePair(MOMConstants.PARAM_NEW_OPERATOR, psOperator),
                new BasicNameValuePair(MOMConstants.PARAM_NEW_INT_RECHARGE_TYPE, Integer.valueOf(pnRechargeType).toString())
        );
    }

    public String getRechargeResult(String psResult) throws MOMException{
        if(psResult == null || "".equals(psResult.trim())){
            throw new MOMException();
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
        if(
                psSubscriberId == null || "".equals(psSubscriberId) ||
                pdAmount < 1 || psOperator == null || "".equals(psOperator)
                ){

            if(_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.LOGIN);
            }
        }

        _operatorId                 = psOperator;

        String url				    = MOMConstants.URL_NEW_PLATFORM_TXN + MOMConstants.SVC_NEW_METHOD_RECHARGE_DTH;

        AsyncDataEx dataEx		    = new AsyncDataEx(this, url, Methods.RECHARGE_DTH);

        dataEx.execute(
                new BasicNameValuePair(
                        MOMConstants.PARAM_NEW_INT_CUSTOMER_ID,
                        EphemeralStorage.getInstance(_applicationContext).getString(MOMConstants.PARAM_NEW_CUSTOMER_ID, null)
                ),
                new BasicNameValuePair(
                        MOMConstants.PARAM_NEW_COMPANY_ID,
                        EphemeralStorage.getInstance(_applicationContext).getString(MOMConstants.PARAM_NEW_COMPANY_ID, null)
                ),
                new BasicNameValuePair(MOMConstants.PARAM_NEW_STR_ACCESS_ID, "Test"),
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
                        HashMap<String, String> pExtraParamsMap
                    ){

        _operatorId                 = psOperatorId;

        String url				    = MOMConstants.URL_NEW_PLATFORM_TXN + MOMConstants.SVC_NEW_METHOD_BILL_PAYMENT;

        AsyncDataEx dataEx		    = new AsyncDataEx(this, url, Methods.PAY_BILL);

        String strCustomerNumber    = psSubscriberId;

        String sUserId              = EphemeralStorage.getInstance(_applicationContext).getString(MOMConstants.PARAM_NEW_USER_ID, null);

        if (
                MOMConstants.OPERATOR_ID_BEST_ELECTRICITY.equals(psOperatorId) ||
                MOMConstants.OPERATOR_ID_RELIANCE_ENERGY.equals(psOperatorId) ||
                MOMConstants.OPERATOR_ID_MAHANAGAR_GAS.equals(psOperatorId)){

            strCustomerNumber   = psSubscriberId + "|" + psCustomerMobile
                                + "|" + sUserId;

        } else if (MOMConstants.OPERATOR_ID_SBE.equals(psOperatorId) && pExtraParamsMap != null) {
            if(
                    !pExtraParamsMap.containsKey(MOMConstants.PARAM_NEW_RELIANCE_SBE_NBE) ||
                    !pExtraParamsMap.containsKey(MOMConstants.PARAM_NEW_SPECIAL_OPERATOR)
                ){

                Log.d("NEW_PL_DATA", "Parameters not sent for SBE");
                if(_listener != null) {
                    _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.LOGIN);
                }
                return;
            }
            strCustomerNumber   = "SBE|" + psSubscriberId + "|" + psConsumerName + "|"
                                + "|" + pExtraParamsMap.get(MOMConstants.PARAM_NEW_RELIANCE_SBE_NBE)
                                + "|" + pExtraParamsMap.get(MOMConstants.PARAM_NEW_SPECIAL_OPERATOR);

        } else if (MOMConstants.OPERATOR_ID_NBE.equals(psOperatorId) && pExtraParamsMap != null) {
            if(
                    !pExtraParamsMap.containsKey(MOMConstants.PARAM_NEW_RELIANCE_SBE_NBE) ||
                            !pExtraParamsMap.containsKey(MOMConstants.PARAM_NEW_SPECIAL_OPERATOR_NBE)
                ){

                Log.d("NEW_PL_DATA", "Parameters not sent for NBE");

                if(_listener != null) {
                    _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.LOGIN);
                }

                return;
            }

            strCustomerNumber   = "NBE|" + psSubscriberId + "|" + psConsumerName + "|"
                    + "|" + pExtraParamsMap.get(MOMConstants.PARAM_NEW_RELIANCE_SBE_NBE)
                    + "|" + pExtraParamsMap.get(MOMConstants.PARAM_NEW_SPECIAL_OPERATOR_NBE);

        }

        Log.d("NEW_PL_DATA", "Going to pay bill for strCustomerNumber: " + strCustomerNumber);

        dataEx.execute(
                new BasicNameValuePair(
                        MOMConstants.PARAM_NEW_INT_CUSTOMER_ID,
                        EphemeralStorage.getInstance(_applicationContext).getString(MOMConstants.PARAM_NEW_CUSTOMER_ID, null)
                ),
                new BasicNameValuePair(
                        MOMConstants.PARAM_NEW_COMPANY_ID_CAMEL_CASE,
                        EphemeralStorage.getInstance(_applicationContext).getString(MOMConstants.PARAM_NEW_COMPANY_ID, null)
                ),
                new BasicNameValuePair(MOMConstants.PARAM_NEW_STR_ACCESS_ID, "KJHASFD"),
                new BasicNameValuePair(MOMConstants.PARAM_NEW_STR_CUSTOMER_NUMBER, strCustomerNumber),
                new BasicNameValuePair(MOMConstants.PARAM_NEW_DEC_BILL_AMOUNT, String.valueOf(pdAmount)),
                new BasicNameValuePair(MOMConstants.PARAM_NEW_INT_OPERATOR_ID_BILL_PAY, psOperatorId)
        );
    }

    public void getBillAmount(String psOperatorId, String psSubscriberId){

        if(
                psSubscriberId == null || "".equals(psSubscriberId) ||
                psOperatorId == null || "".equals(psOperatorId)
                ){

            if(_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.LOGIN);
            }
        }

        _operatorId             = psOperatorId;

        Log.d("BILL_AMOUNT", "going to get bill amount for operator: " + psOperatorId + ", subscriber: " + psSubscriberId);

        String url				= MOMConstants.URL_NEW_PLATFORM_GET_BEST_BILL;

        AsyncDataEx dataEx		= new AsyncDataEx(this, url, Methods.GET_BILL_AMOUNT);
        String sParameterName   = MOMConstants.PARAM_NEW_CANUMBER;

        if(MOMConstants.OPERATOR_ID_BEST_ELECTRICITY.equals(psOperatorId)){
            sParameterName      = MOMConstants.PARAM_NEW_ACCOUNT_NUMBER;
        }else if(MOMConstants.OPERATOR_ID_RELIANCE_ENERGY.equals(psOperatorId)) {
            url                 = MOMConstants.URL_NEW_PLATFORM_GET_RELIANCE_BILL;
        }else if(MOMConstants.OPERATOR_ID_MAHANAGAR_GAS.equals(psOperatorId)) {
            url                 = MOMConstants.URL_NEW_PLATFORM_GET_MGL_BILL;
        }else{
            Log.e("BILL_AMOUNT", "Bill information cannot be checked for this operator");
            onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.GET_BILL_AMOUNT);
            return;
        }

        dataEx.execute(new BasicNameValuePair(sParameterName, psSubscriberId));
    }

    public float extractBillAmount(String psResult) throws MOMException{
        if(psResult == null || "".equals(psResult.trim()) || _operatorId == null){
            throw new MOMException();
        }

        String[] sArrResponse       = psResult.split("~");
        String sBill                = null;

        if(_operatorId.equals(MOMConstants.OPERATOR_ID_BEST_ELECTRICITY) && sArrResponse.length > 15){
            sBill                   = sArrResponse[16];
        }else if(_operatorId.equals(MOMConstants.OPERATOR_ID_RELIANCE_ENERGY) && sArrResponse.length > 2){
            sBill                   = sArrResponse[3];
        }else if(_operatorId.equals(MOMConstants.OPERATOR_ID_MAHANAGAR_GAS) && sArrResponse.length > 3){
            sBill                   = sArrResponse[4];
        }

        if(sBill == null || "".equals(sBill.trim())){
            throw new MOMException();
        }

        Log.d("BILL", "Bill: " + sBill);

        return Float.valueOf(sBill);
    }

    @Override
    public void getTransactionHistory() {
        String url				= MOMConstants.URL_NEW_PL_HISTORY;

        AsyncDataEx dataEx		= new AsyncDataEx(this, url, Methods.TRANSACTION_HISTORY);
        String sUserId          = EphemeralStorage.getInstance(_applicationContext).getString(MOMConstants.PARAM_NEW_USER_ID, null);

        dataEx.execute(new BasicNameValuePair(MOMConstants.PARAM_NEW_USER_ID, sUserId));
    }

    @Override
    public void changePin(PinType pinType, String psOldPin, String psNewPin) {

        String method           = pinType == PinType.M_PIN
                                    ? MOMConstants.SVC_NEW_METHOD_CHANGE_MPIN
                                    : MOMConstants.SVC_NEW_METHOD_CHANGE_TPIN;

        String url				= MOMConstants.URL_NEW_PLATFORM + method;

        AsyncDataEx dataEx		= new AsyncDataEx(this, url, Methods.CHANGE_PIN);
        String sUserId          = EphemeralStorage.getInstance(_applicationContext).getString(MOMConstants.PARAM_NEW_USER_ID, null);

        dataEx.execute(
                new BasicNameValuePair(
                        MOMConstants.PARAM_NEW_CUSTOMER_ID,
                        EphemeralStorage.getInstance(_applicationContext).getString(MOMConstants.PARAM_NEW_CUSTOMER_ID, null)
                ),
                new BasicNameValuePair(
                        MOMConstants.PARAM_NEW_COMPANY_ID,
                        EphemeralStorage.getInstance(_applicationContext).getString(MOMConstants.PARAM_NEW_COMPANY_ID, null)
                ),
                new BasicNameValuePair(MOMConstants.PARAM_NEW_STR_ACCESS_ID, "KJHASFD"),
                new BasicNameValuePair(MOMConstants.PARAM_NEW_STR_PASSWORD, psOldPin),
                new BasicNameValuePair(MOMConstants.PARAM_NEW_STR_NEW_PASSWORD, psNewPin)

        );
    }

    public String extractChangePinResponse(String psResult) throws MOMException{
            if(psResult == null || "".equals(psResult.trim())){
                throw new MOMException();
            }

            try {
                PullParser parser   = new PullParser(new ByteArrayInputStream(psResult.getBytes()));
                String response     = parser.getTextResponse();

                Log.d(LOG_TAG, "Response: " + response);
                String[] strArrayResponse = response.split("~");
                if(strArrayResponse.length < 2){
                    throw new MOMException();
                }

                return strArrayResponse[1];
            }catch (Exception e){
                e.printStackTrace();
            }

            return "";
    }
}