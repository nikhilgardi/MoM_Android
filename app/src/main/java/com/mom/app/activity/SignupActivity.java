package com.mom.app.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mom.app.R;
import com.mom.app.error.MOMException;
import com.mom.app.identifier.PlatformIdentifier;
import com.mom.app.model.AsyncDataEx;
import com.mom.app.model.AsyncListener;
import com.mom.app.model.AsyncResult;
import com.mom.app.model.IDataEx;
import com.mom.app.model.local.EphemeralStorage;
import com.mom.app.model.mompl.MoMPLDataExImpl;
import com.mom.app.model.pbxpl.PBXPLDataExImpl;
import com.mom.app.ui.TransactionRequest;
import com.mom.app.utils.AppConstants;

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
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mom.app.model.DataExImpl;


public class SignupActivity extends Activity implements AsyncListener<String> {
    String _LOG         = AppConstants.LOG_PREFIX + "LOGIN";
    private EditText et_dob, et_mobileNumber, et_name, et_emailId;
    private Calendar cal;
    private Button btn_login , btn_signUp;
    private int day;
    private int month;
    private int year;


    private ImageButton ib;
    TextView responseText , tv_signUp , lbl_login;
    String responseBody;
    public volatile boolean parsingComplete = true;
    private String registrationStatus ;
    private String temperature ;
    private String registeredCustomerID;
    private String errorMessage ;
    Intent myintent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ib = (ImageButton) findViewById(R.id.imageButton1);
        tv_signUp = (TextView) findViewById(R.id.tv_signUp);
        et_dob = (EditText) findViewById(R.id.et_dob);
        et_mobileNumber = (EditText) findViewById(R.id.et_mobileNumber);
        et_name = (EditText) findViewById(R.id.et_name);
        et_emailId = (EditText) findViewById(R.id.et_emailId);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_signUp = (Button) findViewById(R.id.BTN_signIn);

        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);




        et_dob.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                setDate();
                return false;
            }
        });




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }


    private int validate() {
        if (et_mobileNumber.getText().toString().length() < 10) {
            return 1;
        } else if (et_mobileNumber.getText().toString().length() > 10) {
            return 1;
        } else if (et_name.getText().toString().length() == 0) {
            return 2;
        } else if ((isEmailValid(et_emailId.getText().toString()))== 0) {
            return 3;
        }else if (et_dob.getText().toString().length() == 0) {
            return 4;
        }
        else {
            return 0;
        }
    }




            public void postSignUpData(View view) {

                if (validate() == 0) {
                    signUpDataEncrpyt();


                Log.i(_LOG, "Async login request sent");



            } else {
                switch (validate()) {


                    case 1:


                        tv_signUp.setText("");
                        tv_signUp.setVisibility(View.VISIBLE);
                        tv_signUp.setText(getResources().getString(R.string.prompt_Validity_mobile_number));
                        et_mobileNumber.setText("");
                        break;


                    case 2:


                        tv_signUp.setText("");
                        tv_signUp.setVisibility(View.VISIBLE);
                        tv_signUp.setText(getString(R.string.prompt_Validity_Name));
                        et_name.setText("");
                        break;

                    case 3:

                        tv_signUp.setText("");
                        tv_signUp.setVisibility(View.VISIBLE);
                        tv_signUp.setText(getString(R.string.prompt_Validity_Email_Address));
                        et_emailId.setText("");
                        break;

                    case 4:

                        tv_signUp.setText("");
                        tv_signUp.setVisibility(View.VISIBLE);
                        tv_signUp.setText(getString(R.string.prompt_Validity_DOB));
                        et_dob.setText("");
                        break;

                }
            }
        }

    public void postSignUpConsumer (String data){
        AsyncListener<String> listener = new AsyncListener<String>() {
            @Override
            public void onTaskSuccess(String result, DataExImpl.Methods callback) {
                switch (callback){
                    case SIGN_UP_CONSUMER:
                        Log.i(_LOG, "Check Response: " + result);

                }
            }

            @Override
            public void onTaskError(AsyncResult pResult, DataExImpl.Methods callback) {

            }
        };



        try {
            IDataEx dataEx;

            dataEx = new MoMPLDataExImpl(getApplicationContext(), listener);


            dataEx.signUpCustomerRegistration(data);
        }catch(Exception me){
            Log.e(_LOG, "Error getting dataex", me);

        }

    }

    public String ComposeData() {
        EditText et_mobileNumber = (EditText) findViewById(R.id.et_mobileNumber);
        String usermob = et_mobileNumber.getText().toString();

        EditText et_name = (EditText) findViewById(R.id.et_name);
        String name = et_name.getText().toString();

        EditText et_email = (EditText) findViewById(R.id.et_emailId);
        String email = et_email.getText().toString();

        EditText et_dob = (EditText) findViewById(R.id.et_dob);
        String dob = et_dob.getText().toString();


        int usermobLeng = usermob.length();
        int nameleng = name.length();
        int emailleng = email.length();
        int dobleng = dob.length();
        String.format("%03d", usermobLeng);
        String.format("%03d", nameleng);
        String.format("%03d", emailleng);
        String.format("%03d", dobleng);
        Log.i("Output", usermob);

        StringBuilder sb = new StringBuilder();


        sb.append("001" + String.format("%03d", usermobLeng) + usermob);
        sb.append("002" + String.format("%03d", nameleng) + name);
        sb.append("003" + String.format("%03d", emailleng) + email);
        sb.append("004" + String.format("%03d", dobleng) + dob);

        Log.i("tag", sb.toString());

        return sb.toString();
    }

    public void onTaskSuccess(String result, DataExImpl.Methods callback) {
        Log.i(_LOG, "User not of new PL");
        postSignUpConsumer(result);
    }

    @Override
    public void onTaskError(AsyncResult pResult, DataExImpl.Methods callback) {

    }

    public void setDate() {
        showDialog(999);
    }

    @Override
    protected Dialog onCreateDialog(int id) {

        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);

        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener
            = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {

            showDate(arg1 , arg2 + 1, arg3);
        }
    };

    private void showDate(int year , int month, int day) {
        et_dob.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
        Calendar userAge = new GregorianCalendar(year,month,day);
        Calendar minAdultAge = new GregorianCalendar();
        minAdultAge.add(Calendar.YEAR, -18);


        if (minAdultAge.before(userAge))
        {
            tv_signUp.setVisibility(View.VISIBLE);
            tv_signUp.setText(getResources().getString(R.string.prompt_Validity_DOB_Age));
        }
        else{
            tv_signUp.setVisibility(View.GONE);
        }

    }


    public void login(View view) {
        myintent = new Intent(SignupActivity.this, LoginActivity.class);
        myintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(myintent);
        finish();
    }

    public static int isEmailValid(String email) {
        int isValid = 0;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,3}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = 1;

        }
        return isValid;
    }


    public void signUpDataEncrpyt(){
        String Key = "f0rZHW8IXM8+YNYL7VptiOMr45m0VZ1yHhXD5zADpB4=";
        String url          = AppConstants.URL_NEW_PLATFORM_TXN_SIGNUP + AppConstants.SVC_NEW_METHOD_SIGN_UP_ENCRYPT_DATA;
        MoMPLDataExImpl dataEx      = new MoMPLDataExImpl(this , this);

        dataEx.signUpEncryptData(ComposeData(),Key);
    }

}




