package com.mom.app.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mom.app.R;
import com.mom.app.identifier.PlatformIdentifier;
import com.mom.app.model.AsyncListener;
import com.mom.app.model.AsyncResult;
import com.mom.app.model.DataExImpl;
import com.mom.app.model.IDataEx;
import com.mom.app.model.local.EphemeralStorage;
import com.mom.app.model.mompl.MoMPLDataExImpl;
import com.mom.app.ui.TransactionRequest;
import com.mom.app.utils.AppConstants;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class ForgotPasswordActivity extends Activity  {
    String _LOG         = AppConstants.LOG_PREFIX + "FORGOT PASSWORD";
     private Button  btn_getPassword , btn_back;
     private EditText _etMobileNumber , _etAmount;
     private Spinner _spOperator;
     TextView _tvMessage ;

     ArrayAdapter<String> adapter;
     String responseBody;
     HashMap<String, String> operatorMap = new HashMap<String, String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder() .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        _etMobileNumber = (EditText) findViewById(R.id.et_mobileNumber);
        _etAmount       = (EditText) findViewById(R.id.amount);
        _spOperator     = (Spinner) findViewById(R.id.Operator);
        btn_back        = (Button) findViewById(R.id.btn_back);
        btn_getPassword = (Button) findViewById(R.id.btn_getPassword);
        getAllOperator();

        btn_getPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(view);

            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ForgotPasswordActivity.this ,LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }

        });

        String[] actions = new String[] {
                "Bookmark",
                "Subscribe",
                "Share"
        };
//        String[] sComplaintType = null;
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item, actions);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
     //   getMenuInflater().inflate(R.menu.menu_forgot_password, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void showMessage(int id){
        TextView response	= getMessageTextView();

        response.setVisibility(View.VISIBLE);
        response.setText(getString(id));
    }
    public void showMessageResponse(String psMsg){
        TextView response	= getMessageTextView();

        response.setVisibility(View.VISIBLE);
        response.requestFocus();
        response.setText(psMsg);
    }
    public TextView getMessageTextView(){
        if(_tvMessage == null){
            _tvMessage = (TextView) findViewById(R.id.msgDisplay);
        }
        return _tvMessage;
    }
    public static String[] Split(String strResponse, String delimiter) {
        StringBuffer token = new StringBuffer();
        Vector tokens = new Vector();
        // split
        char[] chars = strResponse.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (delimiter.indexOf(chars[i]) != -1) {
                // we bumbed into a delimiter
                if (token.length() > 0) {
                    tokens.addElement(token.toString());
                    token.setLength(0);
                }
            } else {
                token.append(chars[i]);
            }
        }
        // don't forget the "tail"...
        if (token.length() > 0) {
            tokens.addElement(token.toString());
        }
        // convert the vector into an array
        String[] splitArray = new String[tokens.size()];
        for (int i = 0; i < splitArray.length; i++) {
            splitArray[i] = (String) tokens.elementAt(i);
        }
        return splitArray;
    }
    public void validate(View view) {
        int nMinAmount          = 1;

        int nMinPhoneLength     = 10;
        int nMaxPhoneLength     = 10;


        int nPhoneLength        = _etMobileNumber.getText().toString().length();
        int nAmount             = 0;
        if(nPhoneLength < nMinPhoneLength || nPhoneLength > nMaxPhoneLength){
            if(nMinPhoneLength == nMaxPhoneLength) {
                showMessageResponse(String.format(getResources().getString(R.string.error_phone_length), nMinPhoneLength));
            }else{
                showMessageResponse(String.format(getResources().getString(R.string.error_phone_length_min_max), nMinPhoneLength, nMaxPhoneLength));
            }
            return;
        }
        if (_spOperator.getSelectedItemPosition() < 1){
            showMessageResponse(getResources().getString(R.string.prompt_select_operator));
            return;
        }

        try {
            nAmount             = Integer.parseInt(_etAmount.getText().toString());
        }catch (NumberFormatException nfe){
            nfe.printStackTrace();
            showMessageResponse(getResources().getString(R.string.prompt_numbers_only_amount));
            return;
        }
        if(nAmount < nMinAmount ){
            showMessageResponse(getResources().getString(R.string.prompt_numbers_only_amount));
            return;
        }

        checkPlatformAndLogin();

        getPasswordCountDetails(_etMobileNumber.getText().toString());
    }

    public  String getOperatorCode(String param) {
        return operatorMap.get(param);

    }

//    public void checkPlatformAndLogin() {
//
//        AsyncListener<TransactionRequest> listener = new AsyncListener<TransactionRequest>() {
//            @Override
//            public void onTaskSuccess(TransactionRequest result, DataExImpl.Methods callback) {
//
//                String[] sArrDetails = result.getRemoteResponse().split("~");
//
//
//                if(TextUtils.isEmpty(result.getRemoteResponse())) {
//                    Log.i(_LOG, "User not of new PL");
//
//
//
//
//                    return;
//                }
//
//
//
//                EphemeralStorage.getInstance(getApplicationContext()).storeString(AppConstants.PARAM_NEW_USER_ID, sArrDetails[0]);
//
//                EphemeralStorage.getInstance(getApplicationContext()).storeString(AppConstants.PARAM_NEW_CUSTOMER_ID, sArrDetails[1]);
//                EphemeralStorage.getInstance(getApplicationContext()).storeString(AppConstants.PARAM_NEW_MOBILE_NUMBER, sArrDetails[2]);
//                EphemeralStorage.getInstance(getApplicationContext()).storeString(AppConstants.PARAM_NEW_COMPANY_ID, sArrDetails[3]);
//                EphemeralStorage.getInstance(getApplicationContext()).storeString(AppConstants.NEW_B2C_COMPANY_ID, sArrDetails[3]);
//                Log.i("testCID", EphemeralStorage.getInstance(getApplicationContext()).getString(AppConstants.PARAM_NEW_COMPANY_ID, null));
//
//
//                Log.i(_LOG, EphemeralStorage.getInstance(getApplicationContext()).getString(AppConstants.PARAM_NEW_CUSTOMER_ID, null));
//
//
//
//            }
//
//            @Override
//            public void onTaskError(AsyncResult pResult, DataExImpl.Methods callback) {
//
//            }
//        };
//
//        String username = _etMobileNumber.getText().toString();
//
//        List<NameValuePair> list = new ArrayList<NameValuePair>();
//        list.add(new BasicNameValuePair(AppConstants.PARAM_NEW_RMN, username));
//        list.add(new BasicNameValuePair(AppConstants.PARAM_NEW_COMPANY_ID, AppConstants.NEW_PL_COMPANY_ID));
//
//        try {
//            MoMPLDataExImpl dataEx      = new MoMPLDataExImpl(getApplicationContext(), listener);
//
//            dataEx.checkPlatform(
//                    new BasicNameValuePair(AppConstants.PARAM_NEW_RMN, username)
//            );
//        }catch(Exception me){
//            Log.e(_LOG, "Error getting dataex", me);
//            showMessage(R.string.error_init_app);
//        }
//
//
//
//
//    }

    public void checkPlatformAndLogin(){



            HttpClient httpclient = new DefaultHttpClient();


            try {


                String username = _etMobileNumber.getText().toString();

                HttpPost httppost = new HttpPost("http://180.179.67.72/nokiaservice/GetUserDetailsByRMN.aspx?UserRMN="
                        + username);

                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                this.responseBody = EntityUtils.toString(entity);
                String check = responseBody;
                Log.i("postDatadetails", response.getStatusLine().toString());
                Log.i("postDataPltDetails", this.responseBody);
                String[] sArrDetails = check.split("~");
                EphemeralStorage.getInstance(getApplicationContext()).storeString(AppConstants.PARAM_NEW_USER_ID, sArrDetails[0]);

                EphemeralStorage.getInstance(getApplicationContext()).storeString(AppConstants.PARAM_NEW_CUSTOMER_ID, sArrDetails[1]);
                EphemeralStorage.getInstance(getApplicationContext()).storeString(AppConstants.PARAM_NEW_MOBILE_NUMBER, sArrDetails[2]);
                EphemeralStorage.getInstance(getApplicationContext()).storeString(AppConstants.PARAM_NEW_COMPANY_ID, sArrDetails[3]);
                EphemeralStorage.getInstance(getApplicationContext()).storeString(AppConstants.NEW_B2C_COMPANY_ID, sArrDetails[3]);
                Log.i("testCID", EphemeralStorage.getInstance(getApplicationContext()).getString(AppConstants.PARAM_NEW_COMPANY_ID, null));


                Log.i(_LOG, EphemeralStorage.getInstance(getApplicationContext()).getString(AppConstants.PARAM_NEW_CUSTOMER_ID, null));




            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }



    public void getAllOperator(){
        Log.i(_LOG, "Going to GetOperator");


        final Context context   = this;

        AsyncListener<TransactionRequest> listener = new AsyncListener<TransactionRequest>() {
            @Override
            public void onTaskSuccess(TransactionRequest result, DataExImpl.Methods callback) {
                String[] strArrOperators = Split(result.getRemoteResponse(), "~");

                int mylength = strArrOperators.length;
                operatorMap = new HashMap<String, String>();
                operatorMap.put("--Select Operator--","-1");
                List<String> list = new ArrayList<String>();
                list.add("--"+getResources().getString(R.string.prompt_spinner_select_operator)+"--");
                for ( int i=0 ; i< mylength ; i++){

                    String[] strArrStatename = Split(strArrOperators[i] , "|" );
                    operatorMap.put(strArrStatename[1], strArrStatename[0]);
                    list.add(strArrStatename[1]);

                }

                adapter = new ArrayAdapter<String>(ForgotPasswordActivity.this,
                        android.R.layout.simple_spinner_item, list);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                _spOperator.setAdapter(adapter);


            }

            @Override
            public void onTaskError(AsyncResult pResult, DataExImpl.Methods callback) {

            }
        };
        IDataEx dataEx;

        try {
            dataEx = new MoMPLDataExImpl(getApplicationContext(), listener);
            dataEx.getAllOperators();
        }catch(Exception me){
            Log.e(_LOG, "Error getting dataex", me);
            showMessage(R.string.error_init_app);
        }

        Log.i(_LOG, "Async Count request sent");
    }

    public void getPasswordCountDetails(String sMobileNumber){
        Log.i(_LOG, "Going to login");


        final Context context   = this;

        AsyncListener<TransactionRequest> listener = new AsyncListener<TransactionRequest>() {
            @Override
            public void onTaskSuccess(TransactionRequest result, DataExImpl.Methods callback) {

              if(EphemeralStorage.getInstance(getApplicationContext()).getInt(AppConstants.PARAM_NEW_FORGOT_PASSWORD_CODE ,-1) == 1){

                Log.e("REsponse" ,String.valueOf(EphemeralStorage.getInstance(getApplicationContext()).getInt(AppConstants.PARAM_NEW_FORGOT_PASSWORD_CODE, -1)));
                  Toast.makeText(getApplicationContext(),"Validate", Toast.LENGTH_LONG).show();
                  getPassword();
              }
                else{
                  showMessageResponse(EphemeralStorage.getInstance(getApplicationContext()).getString(AppConstants.PARAM_NEW_FORGOT_PASSWORD_MESSAGE ,null));
                  _etMobileNumber.setText("");
                  _spOperator.setSelection(0);
                  _etAmount.setText("");
              }
            }

            @Override
            public void onTaskError(AsyncResult pResult, DataExImpl.Methods callback) {

            }
        };
        IDataEx dataEx;

        try {
            dataEx = new MoMPLDataExImpl(getApplicationContext(), listener);
            dataEx.passwordCountDetails(sMobileNumber);
        }catch(Exception me){
            Log.e(_LOG, "Error getting dataex", me);
            showMessage(R.string.error_init_app);
        }

        Log.i(_LOG, "Async Count request sent");
    }

    public void getPassword(){
        Log.i(_LOG, "Going to GetPassword");

        final Context context   = this;

        AsyncListener<TransactionRequest> listener = new AsyncListener<TransactionRequest>() {
            @Override
            public void onTaskSuccess(TransactionRequest result, DataExImpl.Methods callback) {

             showMessageResponse(result.getRemoteResponse());
                _etMobileNumber.setText("");
                _spOperator.setSelection(0);
                _etAmount.setText("");


            }

            @Override
            public void onTaskError(AsyncResult pResult, DataExImpl.Methods callback) {

            }
        };
        IDataEx dataEx;

        String sRmn      = _etMobileNumber.getText().toString();
        String sOperator = getOperatorCode(_spOperator.getSelectedItem().toString());
        String sAmount   = _etAmount.getText().toString();
        try {
            dataEx = new MoMPLDataExImpl(getApplicationContext(), listener);
            dataEx.getForgotPassword(sRmn , sOperator ,sAmount);
        }catch(Exception me){
            Log.e(_LOG, "Error getting dataex", me);
            showMessage(R.string.error_init_app);
        }

        Log.i(_LOG, "Async Count request sent");
    }


    public void onBackPressed() {


        finish();

    }


}
