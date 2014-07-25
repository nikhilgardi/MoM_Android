package com.mom.app.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mom.app.R;
import com.mom.app.identifier.ActivityIdentifier;
import com.mom.app.identifier.IdentifierUtils;
import com.mom.app.identifier.PlatformIdentifier;
import com.mom.app.model.AsyncListener;
import com.mom.app.model.AsyncResult;
import com.mom.app.model.DataExImpl;
import com.mom.app.model.IDataEx;
import com.mom.app.model.local.EphemeralStorage;
import com.mom.app.model.local.LocalStorage;
import com.mom.app.model.newpl.NewPLDataExImpl;
import com.mom.app.model.pbxpl.PBXPLDataExImpl;
import com.mom.app.utils.MOMConstants;

public class BillPaymentActivity extends MOMActivityBase implements AsyncListener<String>{



    Button _getBillAmount;
    private EditText _etSubscriberId;
    private EditText amountField;
    private EditText _etCustomerNumber;
    private TextView _billMsgDisplay;

    Spinner operatorSpinner;
    String responseBody;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_payment);

        _getBillAmount          = (Button) findViewById(R.id.btn_GetBillAmount);

        this.operatorSpinner    = (Spinner) findViewById(R.id.Operator);
        this.operatorSpinner.setOnItemSelectedListener(new OperatorSelectedListener());

        this._etSubscriberId    = (EditText) findViewById(R.id.subscriberId);
        this.amountField        = (EditText) findViewById(R.id.amount);
        this._etCustomerNumber  = (EditText) findViewById(R.id.number);
        getAllOperators();

        getProgressBar().setVisibility(View.GONE);
    }

    @Override
    public void onTaskSuccess(String result, DataExImpl.Methods callback) {
        Log.d("TASK_C_MAIN", "Called back");
        switch(callback){
            case PAY_BILL:
                if(result == null){
                    Log.d("TASK_COMPLETE", "Obtained NULL bill payment response");
                    showMessage(getResources().getString(R.string.error_recharge_failed));
                    return;
                }
                Log.d("TASK_COMPLETE", "Going to get new balance");
                getBalanceAsync();
                Log.d("TASK_COMPLETE", "Starting navigation to TxnMsg Activity");
                navigateToTransactionMessageActivity(ActivityIdentifier.BILL_PAYMENT, result);
                break;
        }

        getProgressBar().setVisibility(View.GONE);
    }

    @Override
    public void onTaskError(AsyncResult pResult, DataExImpl.Methods callback) {

    }

    @Override
    protected void showBalance(float pfBalance) {

    }



    public String[] getAllOperators() {
        String[] strResponse1 = null;
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());

        if (_currentPlatform == PlatformIdentifier.NEW)
        {
            try {
                String[] strOperators = new String[] {"Select Service Provider","AIRCEL BILL" , "AIRTEL BILL", "AIRTEL LAND LINE" ,"BESCOM BANGALURU",
                        "BEST ELECTRICITY BILL" ,"BSES RAJDHANI" ,"BSNL BILL PAY" ,"CELLONE BILL PAY"," CESC LIMITED" ,"CESCOM MYSORE",
                        "DHBVN HARYANA", "IDEA BILL" ,"INDRAPRASTH GAS" , "MAHANAGAR GAS BILL","NORTH BIHAR ELECTRICITY",
                        "RELIANCE BILL GSM" ,"RELIANCE CDMA BILL", "RELIANCE ENERGY BILL", "SOUTH BIHAR ELECTRICITY","TATA BILL",
                        "TATA POWER DELHI", "TIKONA BILL PAYMENT","UHBVN HARYANA","VODAFONE BILL"};

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
                        this, android.R.layout.simple_spinner_item,
                        strOperators);
                dataAdapter
                        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                operatorSpinner.setAdapter(dataAdapter);

            } catch (Exception ex) {
                String[] strResponse = { "NO ITEM TO DISPLAY" };
                strResponse1 = strResponse;
            }

        } else if (_currentPlatform == PlatformIdentifier.PBX) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(
                    "http://180.179.67.76/MobAppS/PbxMobApp.ashx");
            try {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                        2);
                nameValuePairs.add(new BasicNameValuePair("OT", "2"));
                nameValuePairs.add(new BasicNameValuePair("Service", "ON"));

                httppost.addHeader("ua", "android");
                final HttpParams httpParams = httpclient.getParams();
                HttpConnectionParams.setConnectionTimeout(httpParams, 15000);
                HttpConnectionParams.setSoTimeout(httpParams, 15000);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                responseBody = EntityUtils.toString(entity);
                String strOperators = responseBody;
                Log.i("postData", response.getStatusLine().toString());
                Log.i("postData", this.responseBody);
                String[] strArrOperators = strOperators.split("\\|");

                strResponse1 = strArrOperators;

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
                        this, android.R.layout.simple_spinner_item,
                        strResponse1);
                dataAdapter
                        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                operatorSpinner.setAdapter(dataAdapter);

            } catch (Exception ex) {
                String[] strResponse = { "NO ITEM TO DISPLAY" };
                strResponse1 = strResponse;
            }
        } else {
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG)
                    .show();
        }

        return strResponse1;

    }

    private String getOperatorId(String strOperatorName) {

        if (_currentPlatform == PlatformIdentifier.NEW)
        {
            String id      = MOMConstants.OPERATOR_NEW.get(strOperatorName);
            if(id == null){
                return "-1";
            }

            return id;

        } else if (_currentPlatform == PlatformIdentifier.PBX) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(
                    "http://180.179.67.76/MobAppS/PbxMobApp.ashx");
            try {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                        3);
                nameValuePairs.add(new BasicNameValuePair("SN", strOperatorName));
                nameValuePairs.add(new BasicNameValuePair("Service", "OSN"));
                nameValuePairs.add(new BasicNameValuePair("OT", "1"));

                httppost.addHeader("ua", "android");

                final HttpParams httpParams = httpclient.getParams();
                HttpConnectionParams.setConnectionTimeout(httpParams, 15000);
                HttpConnectionParams.setSoTimeout(httpParams, 15000);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                responseBody = EntityUtils.toString(entity);

                return responseBody;
            } catch (Exception ex) {
                return "-1";
            }
        } else {
            return "-1";
        }
    }

    public void showBillMessage(String psMsg){
        if(_billMsgDisplay == null){
            _billMsgDisplay = (TextView) findViewById(R.id.billMsgDisplay);
        }
        _billMsgDisplay.setVisibility(View.VISIBLE);
        _billMsgDisplay.setText(psMsg);
    }

    public void getBillAmount(View view){
        Log.d("BIL_PAY", "Get Bill Amount called");
        String sSubscriberId            = _etSubscriberId.getText().toString();
        String sOperatorID              = getOperatorId(operatorSpinner.getSelectedItem().toString());

        IDataEx dataEx  = new NewPLDataExImpl(getApplicationContext(), new AsyncListener<Float>() {
            @Override
            public void onTaskSuccess(Float result, DataExImpl.Methods callback) {
                Log.e("BILL_PAY", "Obtainined bill amount: " + result);
                int bill                = Math.round(result);
                amountField.setText(String.valueOf(bill));
                amountField.setBackgroundColor(getResources().getColor(R.color.green));
            }

            @Override
            public void onTaskError(AsyncResult pResult, DataExImpl.Methods callback) {
                Log.e("BILL_PAY", "Error obtaining bill amount");
                amountField.setBackgroundColor(getResources().getColor(R.color.red));
                showBillMessage(getResources().getString(R.string.error_obtaining_bill_amount));
            }
        });

        dataEx.getBillAmount(sOperatorID, sSubscriberId);
        Log.d("BIL_PAY", "Get Bill Amount finished");
    }


    public void validateAndPay(View view) {
        if (operatorSpinner.getSelectedItemPosition() < 1){
            showMessage(getResources().getString(R.string.prompt_select_operator));
            return;
        }

        String sOperator        = operatorSpinner.getSelectedItem().toString();
        String sOperatorId      = getOperatorId(sOperator);

        int nMinAmount          = 10;
        int nMaxAmount          = 10000;
        int nMinLength          = 10;
        int nMaxLength          = 10;

        int nCustomerNumberLength
                                = _etCustomerNumber.getText().toString().trim().length();
        int nAmount             = 0;

        try {
            nAmount             = Integer.parseInt(amountField.getText().toString().trim());
        }catch (NumberFormatException nfe){
            nfe.printStackTrace();
            showMessage(getResources().getString(R.string.prompt_numbers_only_amount));
            return;
        }

        if(
                sOperatorId.equals(MOMConstants.OPERATOR_ID_AIRTEL_BILL) ||
                        sOperatorId.equals("BAI")
                ){

            nMinAmount      = 50;
            nMinLength      = 10;
            nMaxLength      = 11;
        }else if(
                sOperatorId.equals(MOMConstants.OPERATOR_ID_AIRCEL_BILL) ||
                sOperatorId.equals(MOMConstants.OPERATOR_ID_BSNL_BILL_PAY) ||
                sOperatorId.equals(MOMConstants.OPERATOR_ID_IDEA_BILL) ||
                sOperatorId.equals(MOMConstants.OPERATOR_ID_RELIANCE_BILL_GSM) ||
                sOperatorId.equals(MOMConstants.OPERATOR_ID_RELIANCE_BILL_CDMA) ||
                sOperatorId.equals(MOMConstants.OPERATOR_ID_TATA_BILL) ||
                sOperatorId.equals(MOMConstants.OPERATOR_ID_VODAFONE_BILL) ||
                sOperatorId.equals("BAC") ||
                sOperatorId.equals("BLL") ||
                sOperatorId.equals("BID") ||
                sOperatorId.equals("BRG") ||
                sOperatorId.equals("BRC") ||
                sOperatorId.equals("BTA") ||
                sOperatorId.equals("BVO") ||
                sOperatorId.equals("BRC")
                ){

            nMinAmount      = 50;
        }

        if(nCustomerNumberLength < nMinLength || nCustomerNumberLength > nMaxLength){
            if(nMinLength == nMaxLength){
                showMessage(String.format(getResources().getString(R.string.error_phone_length), nMinLength));
            }else{
                showMessage(String.format(getResources().getString(R.string.error_phone_length_min_max), nMinLength, nMaxLength));
            }
            return;
        }

        if(nAmount < nMinAmount || nAmount > nMaxAmount){
            showMessage(String.format(getResources().getString(R.string.error_amount_min_max), nMinAmount, nMaxAmount));
            return;
        }

        if(
                MOMConstants.OPERATOR_ID_RELIANCE_ENERGY.equals(sOperatorId) ||
                        MOMConstants.OPERATOR_ID_BEST_ELECTRICITY.equals(sOperatorId) ||
                        MOMConstants.OPERATOR_ID_MAHANAGAR_GAS.equals(sOperatorId)){

            String sSubscriberId          = _etSubscriberId.getText().toString().trim();

            if("".equals(sSubscriberId)) {
                showMessage(getResources().getString(R.string.error_subscriber_id_length));
                return;
            }
        }


        confirmPayment();
    }

    private void startPayment() {
        getProgressBar().setVisibility(View.VISIBLE);

        String[] strResponse1           = null;

        String sSubscriberId            = _etSubscriberId.getText().toString();
        String sRechargeAmount          = amountField.getText().toString();
        String sOperatorID              = getOperatorId(operatorSpinner.getSelectedItem().toString());
        String sCustomerNumber          = _etCustomerNumber.getText().toString().trim();
        String sCustomerName            = "";

        int nRechargeType               = 0;

//        SharedPreferences pref = PreferenceManager
//                .getDefaultSharedPreferences(getApplicationContext());

        if (_currentPlatform == PlatformIdentifier.NEW){
            HashMap<String, String> map     = new HashMap<String, String>();

            map.put(MOMConstants.PARAM_NEW_RELIANCE_SBE_NBE, "");
            map.put(MOMConstants.PARAM_NEW_SPECIAL_OPERATOR_NBE, "");

            getDataEx(this).payBill(sSubscriberId, Double.parseDouble(sRechargeAmount), sOperatorID, sCustomerNumber, sCustomerName, map);

        } else if (_currentPlatform == PlatformIdentifier.PBX){
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(
                    "http://180.179.67.76/MobAppS/PbxMobApp.ashx");
            try {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                        5);
                nameValuePairs.add(new BasicNameValuePair("CustMobile", sSubscriberId));
                nameValuePairs.add(new BasicNameValuePair("Amount", sRechargeAmount));
                nameValuePairs.add(new BasicNameValuePair("OP", sOperatorID));
                String sUserMobile  = EphemeralStorage.getInstance(this).getString(MOMConstants.LOGGED_IN_USERNAME, "");
                nameValuePairs.add(new BasicNameValuePair("RN", sUserMobile));
                nameValuePairs.add(new BasicNameValuePair("Service", "RM"));

                final HttpParams httpParams = httpclient.getParams();
                // HttpConnectionParams.setConnectionTimeout(httpParams, 15000);
                // HttpConnectionParams.setSoTimeout(httpParams, 15000);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                responseBody = EntityUtils.toString(entity);


                if (responseBody.contains("~")) {
                    strResponse1 = responseBody.split("~");

                    StringBuilder sb = new StringBuilder();
                    sb.append("StatusCode : " + strResponse1[0].toString()
                            + "\n" + "TransId : " + strResponse1[2].toString()
                            + "\n" + "Message:" + strResponse1[1].toString()
                            + "\n" + "Balance:" + strResponse1[3].toString());

                    sb.append("\n");
                    sb.append("\n");

                    String a = sb.toString();

                    showMessage(a);

                    Log.i("postData~", response.getStatusLine().toString());
                    Log.i("info", a);
                }else {
                    showMessage(responseBody);
                }
            } catch (Exception e) {
                Log.e("log_tagTESTabcd",
                        "Error in http connection " + e.toString());
            }

        } else {
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG)
                    .show();
        }

    }



    public void confirmPayment() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                BillPaymentActivity.this);

        // Setting Dialog Title
        alertDialog.setTitle("Confirm MobileRecharge...");

        String sMsg     = "Operator: "
                        + operatorSpinner.getSelectedItem() + "\n"
                        + "Amount:" + " " + "Rs." + " "
                        + amountField.getText() + "\n"
                        + "Phone: " + _etCustomerNumber.getText();

        String sOperator                = operatorSpinner.getSelectedItem().toString();

        String sOperatorId              = getOperatorId(sOperator);

        if(
                MOMConstants.OPERATOR_ID_RELIANCE_ENERGY.equals(sOperatorId) ||
                MOMConstants.OPERATOR_ID_BEST_ELECTRICITY.equals(sOperatorId) ||
                MOMConstants.OPERATOR_ID_MAHANAGAR_GAS.equals(sOperatorId)
                ){

            sMsg        = getResources().getString(R.string.lblConsumerNumber) + ": "
                        + _etSubscriberId.getText() + "\n" + sMsg;
        }

        // Setting Dialog Message
        alertDialog.setMessage(sMsg);

        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ((AlertDialog) dialog).getButton(
                                AlertDialog.BUTTON1).setEnabled(false);
                        startPayment();

                    }
                });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which1) {

                        dialog.cancel();

                    }
                });

        alertDialog.show();
    }

    public void showRetrieveBillFields(){
        _etSubscriberId.setVisibility(View.VISIBLE);
        _getBillAmount.setVisibility(View.VISIBLE);
    }

    public void hideRetrieveBillFields(){
        amountField.setBackgroundColor(getResources().getColor(R.color.white));
       _etSubscriberId.setText("");
        _etSubscriberId.setVisibility(View.GONE);
        _getBillAmount.setVisibility(View.GONE);
    }

    public class OperatorSelectedListener implements AdapterView.OnItemSelectedListener{
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            String sConsumerNumber          = getResources().getString(R.string.lblConsumerNumber);
            String sAccountNumber           = getResources().getString(R.string.lblAccountNumber);
            String sMobileNumber            = getResources().getString(R.string.lblCustomerMobile);

            String sHintDisplay             = sConsumerNumber;

            showBillMessage("");

            String sOperator                = operatorSpinner.getSelectedItem().toString();

            Log.d("ITEM_SELECT", "Operator selected: " + sOperator);
            String sOperatorId              = getOperatorId(sOperator);

            if(sOperatorId == null){
                Log.d("ITEM_SELECT", "Could not find operator id, doing nothing");
                return;
            }

            hideRetrieveBillFields();

            if(MOMConstants.OPERATOR_ID_NBE.equals(sOperatorId) || MOMConstants.OPERATOR_ID_SBE.equals(sOperatorId)){
                sHintDisplay                = sConsumerNumber;
            }else if(
                    MOMConstants.OPERATOR_ID_RELIANCE_ENERGY.equals(sOperatorId) ||
                            MOMConstants.OPERATOR_ID_BEST_ELECTRICITY.equals(sOperatorId) ||
                            MOMConstants.OPERATOR_ID_MAHANAGAR_GAS.equals(sOperatorId)){

                sHintDisplay                = sAccountNumber;

                showRetrieveBillFields();
            }

            _etSubscriberId.setHint(sHintDisplay);
        }
    }

}