package com.mom.app.model.mompl;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.mom.app.error.MOMException;
import com.mom.app.identifier.PinType;
import com.mom.app.model.AsyncDataEx;
import com.mom.app.model.AsyncListener;
import com.mom.app.model.AsyncResult;
import com.mom.app.model.DataExImpl;
import com.mom.app.model.Transaction;
import com.mom.app.model.local.EphemeralStorage;
import com.mom.app.model.pbxpl.lic.LicLifeResponse;
import com.mom.app.model.xml.PullParser;
import com.mom.app.ui.TransactionRequest;
import com.mom.app.utils.AppConstants;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by vaibhavsinha on 7/5/14.
 */
public class MoMPLDataExImpl extends DataExImpl implements AsyncListener<TransactionRequest>{

    private String _LOG = AppConstants.LOG_PREFIX + "DATAEX_NEW";
    private static MoMPLDataExImpl _instance;

    private String _operatorId      = null;

    private MoMPLDataExImpl(Context pContext){
        this._applicationContext    = pContext;

        checkConnectivity(pContext);
    }

    public static MoMPLDataExImpl getInstance(Context context, AsyncListener pListener){
        if(_instance == null){
            _instance               = new MoMPLDataExImpl(context);
        }

        _instance.setListener(pListener);
        return _instance;
    }

    @Override
    public void onTaskSuccess(TransactionRequest result, Methods callback) {
        Log.d(_LOG, "onTaskSuccess");
        try {
            result.setCompleted(true);
            result.setDateCompleted(new Date());

            switch (callback) {
                case CHECK_PLATFORM_DETAILS:
                    Log.d(_LOG, "TaskComplete: Check platform, result: " + result);
                    if (_listener != null) {
                        _listener.onTaskSuccess(result.getRemoteResponse(), callback);
                    }
                    break;
                case LOGIN:
                    Log.d(_LOG, "TaskComplete: Login method, result: " + result);
                    if (_listener != null) {
                        _listener.onTaskSuccess(loginSuccessful(result), callback);
                    }
                    break;
                case VERIFY_TPIN:
                    Log.d(_LOG, "TaskComplete: verifyTPin method, result: " + result);
                    boolean bTPinSuccess = tpinVerified(result.getRemoteResponse());
                    if (_listener != null) {
                        _listener.onTaskSuccess((new Boolean(bTPinSuccess)).toString(), callback);
                    }
                    break;
                case GET_BALANCE:
                    Log.d(_LOG, "TaskComplete: getBalance method, result: " + result);
                    float balance = extractBalance(result);

                    if (_listener != null) {
                        _listener.onTaskSuccess(balance, Methods.GET_BALANCE);
                    }

                    break;
                case RECHARGE_MOBILE:
                    Log.d(_LOG, "TaskComplete: rechargeMobile method, result: " + result);
                    if (_listener != null) {
                        _listener.onTaskSuccess(getRechargeResult(result), Methods.RECHARGE_MOBILE);
                    }
                    break;
                case RECHARGE_DTH:
                    Log.d(_LOG, "TaskComplete: rechargeDTH method, result: " + result);
                    if (_listener != null) {
                        _listener.onTaskSuccess(getRechargeResult(result), Methods.RECHARGE_DTH);
                    }
                    break;
                case PAY_BILL:
                    Log.d(_LOG, "TaskComplete: payBill method, result: " + result);

                    if (_listener != null) {
                        _listener.onTaskSuccess(getRechargeResult(result), Methods.PAY_BILL);
                    }
                    break;
                case BALANCE_TRANSFER:
                    Log.d(_LOG, "TaskComplete: balanceTransfer method, result: " + result);

                    if (_listener != null) {
                        _listener.onTaskSuccess(getRechargeResult(result), Methods.BALANCE_TRANSFER);
                    }
                    break;
                case GET_BILL_AMOUNT:
                    Log.d(_LOG, "TaskComplete: getBillAmount method, result: " + result);
                    float billAmount = extractBillAmount(result.getRemoteResponse());

                    if (_listener != null) {
                        _listener.onTaskSuccess(billAmount, Methods.GET_BILL_AMOUNT);
                    }
                    break;
                case TRANSACTION_HISTORY:
                    Log.d(_LOG, "TaskComplete: getTransactionHistory method, result: " + result);
                    if (_listener != null) {
                        _listener.onTaskSuccess(extractTransactions(result.getRemoteResponse()), Methods.TRANSACTION_HISTORY);
                    }
                    break;
                case CHANGE_PIN:
                    Log.d(_LOG, "TaskComplete: changeMPin method, result: " + result);


                    if (_listener != null) {
                        _listener.onTaskSuccess(
                                extractChangePinResponse(result.getRemoteResponse()),
                                Methods.CHANGE_PIN
                        );
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

        String url                  = AppConstants.URL_NEW_PL_DETAILS;
        AsyncDataEx dataEx          = new AsyncDataEx(
                this,
                new TransactionRequest(),
                url,
                Methods.CHECK_PLATFORM_DETAILS,
                AsyncDataEx.HttpMethod.GET
        );
        dataEx.execute(params);
    }

    public void login(String userName, String password){
        if(TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)){
            if(_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.LOGIN);
            }
        }

        String loginUrl				= AppConstants.URL_NEW_PLATFORM + AppConstants.SVC_NEW_METHOD_LOGIN;

        AsyncDataEx dataEx		    = new AsyncDataEx(this, new TransactionRequest(), loginUrl, Methods.LOGIN);

        dataEx.execute(
                new BasicNameValuePair("user", userName),
                new BasicNameValuePair("pass", password),
                new BasicNameValuePair(AppConstants.PARAM_NEW_USER, userName),
                new BasicNameValuePair(AppConstants.PARAM_NEW_PWD, password),
                new BasicNameValuePair(AppConstants.PARAM_NEW_COMPANY_ID, AppConstants.NEW_PL_COMPANY_ID),
                new BasicNameValuePair(AppConstants.PARAM_NEW_STR_ACCESS_ID, "abc")
        );
    }

    public boolean loginSuccessful(TransactionRequest pResult){
        if("".equals(pResult.getRemoteResponse())){
            return false;
        }

        try {
            PullParser parser   = new PullParser(new ByteArrayInputStream(pResult.getRemoteResponse().getBytes()));
            String response     = parser.getTextResponse();

            Log.d(_LOG, "Response: " + response);
            String[] strArrayResponse = response.split("~");

            int i = Integer.parseInt(strArrayResponse[0]);

            if(i == AppConstants.NEW_PL_LOGIN_SUCCESS) {
                Log.d(_LOG, "Login successful");
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    public void getBalance(){
        String url				= AppConstants.URL_NEW_PLATFORM + AppConstants.SVC_NEW_METHOD_GET_BALANCE;

        Log.d(_LOG, "Creating dataEx for getBalance call");

        AsyncDataEx dataEx		    = new AsyncDataEx(this, new TransactionRequest(), url, Methods.GET_BALANCE);

        Log.d(_LOG, "Calling web service via async");

        dataEx.execute(
                new BasicNameValuePair(
                        AppConstants.PARAM_NEW_OPERATOR_ID,
                        EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_CUSTOMER_ID, null)
                ),
                new BasicNameValuePair(
                        AppConstants.PARAM_NEW_COMPANY_ID,
                        EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_COMPANY_ID, null)
                ),
                new BasicNameValuePair(
                        AppConstants.PARAM_NEW_STR_ACCESS_ID_SMALL_D,
                        "haduiy23d"
                )
        );

        Log.d(_LOG, "Called web service via async");
    }

    public float extractBalance(TransactionRequest pResult) throws MOMException{
        if(TextUtils.isEmpty(pResult.getRemoteResponse())){
            throw new MOMException();
        }

        try {
            PullParser parser   = new PullParser(new ByteArrayInputStream(pResult.getRemoteResponse().getBytes()));
            String response     = parser.getTextResponse();

            Log.d(_LOG, "Response: " + response);
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

        String url				= AppConstants.URL_NEW_PLATFORM + AppConstants.SVC_NEW_METHOD_CHECK_TPIN;

        AsyncDataEx dataEx		    = new AsyncDataEx(this, new TransactionRequest(), url, Methods.VERIFY_TPIN);

        dataEx.execute(
                new BasicNameValuePair(AppConstants.PARAM_NEW_TPIN, psTPin),
                new BasicNameValuePair(
                        AppConstants.PARAM_NEW_CUSTOMER_ID,
                        EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_CUSTOMER_ID, null)
                ),
                new BasicNameValuePair(
                        AppConstants.PARAM_NEW_COMPANY_ID,
                        EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_COMPANY_ID, null)
                ),
                new BasicNameValuePair(AppConstants.PARAM_NEW_STR_ACCESS_ID_SMALL_D, "KJHASFD")
                );
    }

    public boolean tpinVerified(String psResult){
        if("".equals(psResult)){
            return false;
        }

        try {
            PullParser parser   = new PullParser(new ByteArrayInputStream(psResult.getBytes()));
            String response     = parser.getTextResponse();

            Log.d(_LOG, "Response: " + response);
            String[] strArrayResponse = response.split("~");

            int i = Integer.parseInt(strArrayResponse[0]);

            if(i == AppConstants.NEW_PL_TPIN_VERIFIED){
                Log.d(_LOG, "TPin verified");
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }


    public void rechargeMobile(TransactionRequest request, int pnRechargeType){

        if(
            request == null || TextUtils.isEmpty(request.getConsumerId()) ||
            request.getAmount() < 1 || request.getOperator() == null
            ){

            if(_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.LOGIN);
            }
            return;
        }

        String url				    = AppConstants.URL_NEW_PLATFORM_TXN + AppConstants.SVC_NEW_METHOD_RECHARGE_MOBILE;

        AsyncDataEx dataEx		    = new AsyncDataEx(this, request, url, Methods.RECHARGE_MOBILE);

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
                new BasicNameValuePair(AppConstants.PARAM_NEW_STR_MOBILE_NUMBER, request.getConsumerId()),
                new BasicNameValuePair(AppConstants.PARAM_NEW_RECHARGE_AMOUNT, String.valueOf(Math.round(request.getAmount()))),
                new BasicNameValuePair(AppConstants.PARAM_NEW_OPERATOR, request.getOperator().code),
                new BasicNameValuePair(AppConstants.PARAM_NEW_INT_RECHARGE_TYPE, Integer.valueOf(pnRechargeType).toString())
        );
    }

    public TransactionRequest getRechargeResult(TransactionRequest pResult) throws MOMException{
        if(pResult == null || "".equals(pResult.getRemoteResponse().trim())){
            throw new MOMException();
        }

        try {
            PullParser parser   = new PullParser(new ByteArrayInputStream(pResult.getRemoteResponse().getBytes()));
            String response     = parser.getTextResponse();

            Log.d(_LOG, "Response: " + response);
            pResult.setRemoteResponse(response);
            return pResult;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public void rechargeDTH(TransactionRequest request){
        if(
                TextUtils.isEmpty(request.getConsumerId()) ||
                request.getAmount() < 1 || request.getOperator() == null
                ){

            if(_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.LOGIN);
            }
        }


        String url				    = AppConstants.URL_NEW_PLATFORM_TXN + AppConstants.SVC_NEW_METHOD_RECHARGE_DTH;

        AsyncDataEx dataEx		    = new AsyncDataEx(this, request, url, Methods.RECHARGE_DTH);

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
                new BasicNameValuePair(AppConstants.PARAM_NEW_STR_MOBILE_NUMBER, request.getConsumerId()),
                new BasicNameValuePair(AppConstants.PARAM_NEW_RECHARGE_AMOUNT, String.valueOf(Math.round(request.getAmount()))),
                new BasicNameValuePair(AppConstants.PARAM_NEW_OPERATOR, request.getOperator().code),
                new BasicNameValuePair(AppConstants.PARAM_NEW_CUSTOMER_NUMBER, request.getCustomerMobile())
        );
    }

    public void payBill(
                        TransactionRequest request,
                        String psConsumerName,
                        HashMap<String, String> pExtraParamsMap
                    ){


        String url				    = AppConstants.URL_NEW_PLATFORM_TXN + AppConstants.SVC_NEW_METHOD_BILL_PAYMENT;

        AsyncDataEx dataEx		    = new AsyncDataEx(this, request, url, Methods.PAY_BILL);

        String sUserId              = EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_USER_ID, null);

        String operatorCode         = request.getOperator().code;
        String strCustomerNumber    = request.getConsumerId();

        if (
                AppConstants.OPERATOR_ID_BEST_ELECTRICITY.equals(operatorCode) ||
                AppConstants.OPERATOR_ID_RELIANCE_ENERGY.equals(operatorCode) ||
                AppConstants.OPERATOR_ID_MAHANAGAR_GAS.equals(operatorCode)){

            strCustomerNumber   = strCustomerNumber + "|" + request.getCustomerMobile()
                                + "|" + sUserId;

        } else if (AppConstants.OPERATOR_ID_SBE.equals(operatorCode) && pExtraParamsMap != null) {
            if(
                    !pExtraParamsMap.containsKey(AppConstants.PARAM_NEW_RELIANCE_SBE_NBE) ||
                    !pExtraParamsMap.containsKey(AppConstants.PARAM_NEW_SPECIAL_OPERATOR)
                ){

                Log.d("NEW_PL_DATA", "Parameters not sent for SBE");
                if(_listener != null) {
                    _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.LOGIN);
                }
                return;
            }
            strCustomerNumber   = "SBE|" + request.getConsumerId() + "|" + psConsumerName + "|"
                                + "|" + pExtraParamsMap.get(AppConstants.PARAM_NEW_RELIANCE_SBE_NBE)
                                + "|" + pExtraParamsMap.get(AppConstants.PARAM_NEW_SPECIAL_OPERATOR);

        } else if (AppConstants.OPERATOR_ID_NBE.equals(operatorCode) && pExtraParamsMap != null) {
            if(
                    !pExtraParamsMap.containsKey(AppConstants.PARAM_NEW_RELIANCE_SBE_NBE) ||
                            !pExtraParamsMap.containsKey(AppConstants.PARAM_NEW_SPECIAL_OPERATOR_NBE)
                ){

                Log.d("NEW_PL_DATA", "Parameters not sent for NBE");

                if(_listener != null) {
                    _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.LOGIN);
                }

                return;
            }

            strCustomerNumber   = "NBE|" + request.getConsumerId() + "|" + psConsumerName + "|"
                    + "|" + pExtraParamsMap.get(AppConstants.PARAM_NEW_RELIANCE_SBE_NBE)
                    + "|" + pExtraParamsMap.get(AppConstants.PARAM_NEW_SPECIAL_OPERATOR_NBE);

        }

        Log.d("NEW_PL_DATA", "Going to pay bill for strCustomerNumber: " + strCustomerNumber);

        dataEx.execute(
                new BasicNameValuePair(
                        AppConstants.PARAM_NEW_INT_CUSTOMER_ID,
                        EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_CUSTOMER_ID, null)
                ),
                new BasicNameValuePair(
                        AppConstants.PARAM_NEW_COMPANY_ID_CAMEL_CASE,
                        EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_COMPANY_ID, null)
                ),
                new BasicNameValuePair(AppConstants.PARAM_NEW_STR_ACCESS_ID, "KJHASFD"),
                new BasicNameValuePair(AppConstants.PARAM_NEW_STR_CUSTOMER_NUMBER, strCustomerNumber),
                new BasicNameValuePair(AppConstants.PARAM_NEW_DEC_BILL_AMOUNT, String.valueOf(Math.round(request.getAmount()))),
                new BasicNameValuePair(AppConstants.PARAM_NEW_INT_OPERATOR_ID_BILL_PAY, operatorCode)
        );
    }

    public void balanceTransfer(TransactionRequest request, String payTo)
    {
        if(
                TextUtils.isEmpty(request.getConsumerId()) || request.getAmount() < 1 ||
                payTo == null || payTo.trim().equals("")
                ){
            if(_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.BALANCE_TRANSFER);
            }
            return;
        }


        String url          = AppConstants.URL_NEW_PLATFORM_TXN + AppConstants.SVC_NEW_METHOD_RETAILER_PAYMENT;
        String sUserId      = EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_USER_ID, null);
        AsyncDataEx dataEx	= new AsyncDataEx(this, request, url, Methods.BALANCE_TRANSFER);

        dataEx.execute(
                new BasicNameValuePair(AppConstants.PARAM_NEW_STR_PAYER, sUserId),
                new BasicNameValuePair(AppConstants.PARAM_NEW_STR_PAYEE, payTo),
                new BasicNameValuePair(AppConstants.PARAM_NEW_STR_TRANSFERAMOUNT, String.valueOf(Math.round(request.getAmount()))),
                new BasicNameValuePair(AppConstants.PARAM_NEW_STR_TRANSVALUETPE, AppConstants.PARAM_NEW_STR_TRANSVALUE),
                new BasicNameValuePair(AppConstants.PARAM_NEW_STR_ACCESS_ID, "Test"),
        new BasicNameValuePair(
                AppConstants.PARAM_NEW_COMPANY_ID,
                EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_COMPANY_ID, null)
        )
        );
    }

    public void getBillAmount(TransactionRequest request){

        if(
                request.getConsumerId() == null || "".equals(request.getConsumerId()) ||
                request.getOperator() == null
                ){

            if(_listener != null) {
                _listener.onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.LOGIN);
            }
        }

        String operatorId             = request.getOperator().code;

        Log.d("BILL_AMOUNT", "going to get bill amount for operator: " + operatorId + ", subscriber: " + request.getConsumerId());

        String url				= AppConstants.URL_NEW_PLATFORM_GET_BEST_BILL;

        AsyncDataEx dataEx		= new AsyncDataEx(this, request, url, Methods.GET_BILL_AMOUNT);
        String sParameterName   = AppConstants.PARAM_NEW_CANUMBER;

        if(AppConstants.OPERATOR_ID_BEST_ELECTRICITY.equals(operatorId)){
            sParameterName      = AppConstants.PARAM_NEW_ACCOUNT_NUMBER;
        }else if(AppConstants.OPERATOR_ID_RELIANCE_ENERGY.equals(operatorId)) {
            url                 = AppConstants.URL_NEW_PLATFORM_GET_RELIANCE_BILL;
        }else if(AppConstants.OPERATOR_ID_MAHANAGAR_GAS.equals(operatorId)) {
            url                 = AppConstants.URL_NEW_PLATFORM_GET_MGL_BILL;
        }else{
            Log.e("BILL_AMOUNT", "Bill information cannot be checked for this operator");
            onTaskError(new AsyncResult(AsyncResult.CODE.INVALID_PARAMETERS), Methods.GET_BILL_AMOUNT);
            return;
        }

        dataEx.execute(new BasicNameValuePair(sParameterName, request.getConsumerId()));
    }

    public float extractBillAmount(String psResult) throws MOMException{
        if(psResult == null || "".equals(psResult.trim()) || _operatorId == null){
            throw new MOMException();
        }

        String[] sArrResponse       = psResult.split("~");
        String sBill                = null;

        if(_operatorId.equals(AppConstants.OPERATOR_ID_BEST_ELECTRICITY) && sArrResponse.length > 15){
            sBill                   = sArrResponse[16];
        }else if(_operatorId.equals(AppConstants.OPERATOR_ID_RELIANCE_ENERGY) && sArrResponse.length > 2){
            sBill                   = sArrResponse[3];
        }else if(_operatorId.equals(AppConstants.OPERATOR_ID_MAHANAGAR_GAS) && sArrResponse.length > 3){
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
        String url				= AppConstants.URL_NEW_PL_HISTORY;

        AsyncDataEx dataEx		= new AsyncDataEx(this, new TransactionRequest(), url, Methods.TRANSACTION_HISTORY);
        String sUserId          = EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_USER_ID, null);

        dataEx.execute(new BasicNameValuePair(AppConstants.PARAM_NEW_USER_ID, sUserId));
    }

    public ArrayList<Transaction> extractTransactions(String result) throws MOMException{
        String[] sArrTxn            = result.split("\\|");
        ArrayList<Transaction> transactions  = new ArrayList<Transaction>();

        int i                       = 0;

        for(String sTransaction:sArrTxn) {
            String[] sArr = sTransaction.split("~");

            if (sArr.length < 6) {
                Log.e(_LOG, "Invalid request response");
                continue;
            }

            Transaction transaction         = new Transaction();
            transaction.transactionDate     = sArr[0];
            transaction.transactionId       = sArr[1];
            transaction.subscriberId        = sArr[2];
            try {
                transaction.amount = Float.parseFloat(sArr[3]);
            }catch(NumberFormatException nfe){
                Log.w(_LOG, "Invalid amount response");
            }
            transaction.operator            = sArr[4];
            transaction.statusString        = sArr[5];

            transactions.add(transaction);
        }

        return transactions;
    }

    @Override
    public void changePin(PinType pinType, String psOldPin, String psNewPin) {

        String method           = pinType == PinType.M_PIN
                                    ? AppConstants.SVC_NEW_METHOD_CHANGE_MPIN
                                    : AppConstants.SVC_NEW_METHOD_CHANGE_TPIN;

        String url				= AppConstants.URL_NEW_PLATFORM + method;

        AsyncDataEx dataEx		= new AsyncDataEx(this, new TransactionRequest(), url, Methods.CHANGE_PIN);
        String sUserId          = EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_USER_ID, null);

        dataEx.execute(
                new BasicNameValuePair(
                        AppConstants.PARAM_NEW_CUSTOMER_ID,
                        EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_CUSTOMER_ID, null)
                ),
                new BasicNameValuePair(
                        AppConstants.PARAM_NEW_COMPANY_ID,
                        EphemeralStorage.getInstance(_applicationContext).getString(AppConstants.PARAM_NEW_COMPANY_ID, null)
                ),
                new BasicNameValuePair(AppConstants.PARAM_NEW_STR_ACCESS_ID, "KJHASFD"),
                new BasicNameValuePair(AppConstants.PARAM_NEW_STR_PASSWORD, psOldPin),
                new BasicNameValuePair(AppConstants.PARAM_NEW_STR_NEW_PASSWORD, psNewPin)

        );
    }

    public String extractChangePinResponse(String psResult) throws MOMException{
            if(psResult == null || "".equals(psResult.trim())){
                throw new MOMException();
            }

            try {
                PullParser parser   = new PullParser(new ByteArrayInputStream(psResult.getBytes()));
                String response     = parser.getTextResponse();

                Log.d(_LOG, "Response: " + response);
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

    public  void rechargeMobilePBX(
            String customerNumber,
            String psOperator,
            double pdAmount)
    {

    }
    public void getOperatorNames(){

    }

    public  void changePassword( String psOldPin, String psNewPin){

    }

    public  void lic(String lic){

    }

    public void licPayment(TransactionRequest<LicLifeResponse> request , String CustomerMobNo , String PolicyNo){

    }
}