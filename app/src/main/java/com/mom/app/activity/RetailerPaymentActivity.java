package com.mom.app.activity;

import java.util.ArrayList;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mom.app.R;
import com.mom.app.identifier.ActivityIdentifier;
import com.mom.app.identifier.PlatformIdentifier;
import com.mom.app.model.AsyncListener;
import com.mom.app.model.AsyncResult;
import com.mom.app.model.DataExImpl;
import com.mom.app.model.local.EphemeralStorage;
import com.mom.app.utils.AppConstants;

public class RetailerPaymentActivity extends MOMActivityBase implements AsyncListener<String>{
    String _LOG         = AppConstants.LOG_PREFIX + "DTH RECHARGE";

    private EditText rechargeTargetPhone;
    private EditText amountField;


    String responseBody;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_payment);

        this.rechargeTargetPhone = (EditText) findViewById(R.id.rechargeTargetPhone);
        this.amountField         = (EditText) findViewById(R.id.amount);
        getProgressBar().setVisibility(View.GONE);
    }

    @Override
    public void onTaskSuccess(String result, DataExImpl.Methods callback) {
        Log.d(_LOG, "Called back");
        switch(callback){
            case RETAILER_PAYMENT:
                if(result == null){
                    Log.d(_LOG, "Obtained NULL  response");
                    showMessage(getResources().getString(R.string.error_recharge_failed));
                    return;
                }
                Log.d(_LOG, "Going to get new balance");
                getBalance();
                Log.d(_LOG, "Starting navigation to TxnMsg Activity");
                navigateToTransactionMessageActivity(ActivityIdentifier.RETAILER_PAYMENT, result);
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







    public void validateAndRecharge(View view) {


        int nMinAmount          = 10;

        int nMinPhoneLength     = 10;
        int nMaxPhoneLength     = 10;
        int nExactPhoneLength   = 10;

        int nPhoneLength        = rechargeTargetPhone.getText().toString().length();
        int nAmount             = 0;

        try {
            nAmount             = Integer.parseInt(amountField.getText().toString());
        }catch (NumberFormatException nfe){
            nfe.printStackTrace();
            showMessage(getResources().getString(R.string.prompt_numbers_only_amount));
            return;
        }



        if(nPhoneLength < nMinPhoneLength || nPhoneLength > nMaxPhoneLength){
            if(nMinPhoneLength == nMaxPhoneLength) {
                showMessage(String.format(getResources().getString(R.string.error_phone_length), nMinPhoneLength));
            }else{
                showMessage(String.format(getResources().getString(R.string.error_phone_length_min_max), nMinPhoneLength, nMaxPhoneLength));
            }
            return;
        }

        if(nAmount < nMinAmount ){
            showMessage(String.format(getResources().getString(R.string.error_amount_min_max), nMinAmount));
            return;
        }

        confirmRecharge();
    }

    private void startRecharge() {
        String[] strResponse1 = null;

        String sConsumerNumber          = rechargeTargetPhone.getText().toString();
        String sRechargeAmount          = amountField.getText().toString();


        if (_currentPlatform == PlatformIdentifier.NEW){
            getDataEx(this).retailerpayment(sConsumerNumber,Double.parseDouble(sRechargeAmount));
        }
        /*else if (_currentPlatform == PlatformIdentifier.PBX){
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(
                    "http://180.179.67.76/MobAppS/PbxMobApp.ashx");
            try {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                        5);
                nameValuePairs.add(new BasicNameValuePair("CustMobile", sConsumerNumber));
                nameValuePairs.add(new BasicNameValuePair("Amount", sRechargeAmount));
                nameValuePairs.add(new BasicNameValuePair("OP", sOperatorID));
                String sUserMobile  = EphemeralStorage.getInstance(this).getString(AppConstants.LOGGED_IN_USERNAME, null);
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

        }*/
        else {
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


    public void confirmRecharge() {
        getProgressBar().setVisibility(View.VISIBLE);


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                RetailerPaymentActivity.this);

        // Setting Dialog Title
        alertDialog.setTitle("Confirm Retailer Payment...");

        // Setting Dialog Message
        alertDialog.setMessage("Mobile Number:" + " "
                + rechargeTargetPhone.getText().toString() + "\n"+ "Amount:" + " " + "Rs." + " "
                + amountField.getText().toString());


        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ((AlertDialog) dialog).getButton(
                                AlertDialog.BUTTON1).setEnabled(false);
                        startRecharge();
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

}