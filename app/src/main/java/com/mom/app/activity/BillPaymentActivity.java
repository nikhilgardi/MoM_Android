package com.mom.app.activity;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
import com.mom.app.model.DataExImpl;
import com.mom.app.model.IDataEx;
import com.mom.app.model.local.LocalStorage;
import com.mom.app.model.newpl.NewPLDataExImpl;
import com.mom.app.model.pbxpl.PBXPLDataExImpl;
import com.mom.app.utils.MOMConstants;

public class BillPaymentActivity extends TransactionActivityBase implements AsyncListener<String>{

    PlatformIdentifier _currentPlatform;
    IDataEx _dataEx     = null;

    ProgressBar _pb;

    Button _getBillAmount;
    private EditText _etSubscriberId;
    private EditText amountField;
    private EditText _etCustomerNumber;

    //	private Button postButton, secondback;
//	private Button newButton;
//	TableLayout tablelayout;
    TextView accountbal;
    Spinner operatorSpinner;



    String responseBody;

    TextView responseText;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_payment);
        _currentPlatform        = IdentifierUtils.getPlatformIdentifier(getApplicationContext());
        _getBillAmount          = (Button) findViewById(R.id.btn_GetBillAmount);

        this.operatorSpinner    = (Spinner) findViewById(R.id.Operator);
        this.operatorSpinner.setOnItemSelectedListener(new OperatorSelectedListener());

        this.responseText       = (TextView) findViewById(R.id.msgDisplay);
        this._etSubscriberId    = (EditText) findViewById(R.id.subscriberId);
        this.amountField        = (EditText) findViewById(R.id.amount);
        this.accountbal         = (TextView) findViewById(R.id.AccountBal);
        this._etCustomerNumber  = (EditText) findViewById(R.id.number);
        getAllOperators();

        getProgressBar().setVisibility(View.GONE);
    }

    public void setDataEx(IDataEx pDataEx){
        this._dataEx    = pDataEx;
    }

    public IDataEx getDataEx(){
        if(_dataEx == null){
            if(_currentPlatform == PlatformIdentifier.NEW){
                _dataEx     = new NewPLDataExImpl(getApplicationContext(), this);
            }else{
                _dataEx     = new PBXPLDataExImpl(this, getApplicationContext());
            }
        }

        return _dataEx;
    }

    public void showMessage(String psMsg){
        TextView response	= getMessageTextView();

        response.setVisibility(View.VISIBLE);
        response.setText(psMsg);
    }

    public TextView getMessageTextView(){
        if(responseText == null){
            responseText    = (TextView) findViewById(R.id.msgDisplay);
        }
        return responseText;
    }

    public ProgressBar getProgressBar(){
        if(_pb == null){
            _pb			= (ProgressBar)findViewById(R.id.progressBar);
        }
        return _pb;
    }

    @Override
    public void onTaskComplete(String result, DataExImpl.Methods callback) {
        Log.d("TASK_C_MAIN", "Called back");
        switch(callback){
            case RECHARGE_DTH:
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
    protected void showBalance(float pfBalance) {
        Locale lIndia       = new Locale("en", "IN");
        NumberFormat form   = NumberFormat.getCurrencyInstance(lIndia);
        String sBalance      = form.format(pfBalance);
        accountbal.setText("Balance: " + sBalance);
    }



    public String[] getAllOperators() {
        String[] strResponse1 = null;
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());

        if (_currentPlatform == PlatformIdentifier.NEW)
        {
            try {
                String[] strOperators = new String[] {
                        "Select Service Provider",
                        "AIRTEL DIGITAL" , "BIG TV" , "DISH" ,"SUN DIRECT" ,"TATA SKY", "VIDEOCON DTH"
                };

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

    public void getBillAmount(View view){
        Log.d("BIL_PAY", "Get Bill Amount called");

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

        int nSubscriberLength   = _etSubscriberId.getText().toString().length();
        int nAmount             = 0;

        try {
            nAmount             = Integer.parseInt(amountField.getText().toString());
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

        if(nSubscriberLength < nMinLength || nSubscriberLength > nMaxLength){
            if(nMinLength == nMaxLength){
                showMessage(String.format(getResources().getString(R.string.error_subscriber_id_length), nMinLength));
            }else{
                showMessage(String.format(getResources().getString(R.string.error_subscriber_id_min_max), nMinLength, nMaxLength));
            }
            return;
        }

        if(nAmount < nMinAmount || nAmount > nMaxAmount){
            showMessage(String.format(getResources().getString(R.string.error_amount_min_max), nMinAmount, nMaxAmount));
            return;
        }

        String sCustomerNumber          = _etCustomerNumber.getText().toString().trim();

        if("".equals(sCustomerNumber)) {
            showMessage(getResources().getString(R.string.error_customer_phone_required));
            return;
        }else if(sCustomerNumber.length() != MOMConstants.STANDARD_MOBILE_NUMBER_LENGTH){
            showMessage(String.format(getResources().getString(R.string.error_phone_length), MOMConstants.STANDARD_MOBILE_NUMBER_LENGTH));
            return;
        }

        confirmPayment();
    }

    private void startPayment() {
        String[] strResponse1           = null;

        String sSubscriberId            = _etSubscriberId.getText().toString();
        String sRechargeAmount          = amountField.getText().toString();
        String sOperatorID              = getOperatorId(operatorSpinner.getSelectedItem().toString());
        String sCustomerNumber          = _etCustomerNumber.getText().toString().trim();


        int nRechargeType               = 0;

//        SharedPreferences pref = PreferenceManager
//                .getDefaultSharedPreferences(getApplicationContext());

        if (_currentPlatform == PlatformIdentifier.NEW){

            getDataEx().rechargeDTH(sSubscriberId, Double.parseDouble(sRechargeAmount), sOperatorID, sCustomerNumber);

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
                String sUserMobile  = LocalStorage.getString(getApplicationContext(), MOMConstants.LOGGED_IN_USERNAME);
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



    public void goBack(View view) {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.finish();
        return;
    }


    public void confirmPayment() {
        getProgressBar().setVisibility(View.VISIBLE);


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                BillPaymentActivity.this);

        // Setting Dialog Title
        alertDialog.setTitle("Confirm MobileRecharge...");

        // Setting Dialog Message
        alertDialog.setMessage("Subscriber ID:" + " "
                + _etSubscriberId.getText().toString() + "\n" + "Operator:"
                + " " + operatorSpinner.getSelectedItem().toString() + "\n"
                + "Amount:" + " " + "Rs." + " "
                + amountField.getText().toString());

        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ((AlertDialog) dialog).getButton(
                                AlertDialog.BUTTON1).setEnabled(false);
                        startPayment();
//							new GetLoginTask().onPostExecute("test");

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
        _etSubscriberId.setVisibility(View.GONE);
        _getBillAmount.setVisibility(View.GONE);
    }

    public class OperatorSelectedListener implements AdapterView.OnItemSelectedListener{
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            String sConsumerNumber          = getResources().getString(R.string.lblConsumerNumber);
            String sAccountNumber           = getResources().getString(R.string.lblAccountNumber);
            String sMobileNumber            = getResources().getString(R.string.lblCustomerMobile);

            String sHintDisplay             = sMobileNumber;

            String sOperator                = operatorSpinner.getSelectedItem().toString();
            String sOperatorId              = getOperatorId(sOperator);

            if(sOperatorId == null){
                return;
            }

            hideRetrieveBillFields();

            if(MOMConstants.OPERATOR_ID_NBE.equals(sOperatorId) || MOMConstants.OPERATOR_ID_SBE.equals(sOperatorId)){
                sHintDisplay                = sConsumerNumber;
            }else if(
                    MOMConstants.OPERATOR_ID_RELIANCE_ENERGY.equals(sOperatorId) ||
                    MOMConstants.OPERATOR_ID_BEST_ELECTRICITY.equals(sOperatorId) ||
                    MOMConstants.OPERATOR_ID_MAHANAGAR_GAS.equals(sOperatorId)){
                showRetrieveBillFields();
            }

            _etSubscriberId.setHint(sHintDisplay);
        }

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        }
    }

}