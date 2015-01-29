package com.mom.app.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mom.app.R;
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


public class SignupActivity extends Activity {

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
       // responseText = (TextView) findViewById(R.id.tv_response);
        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder() .permitAll().build();
        StrictMode.setThreadPolicy(policy);

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

        new GetLoginTask().onPostExecute("SignUpData");
    }
    private class GetLoginTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            return responseBody;
        }
        @Override
        protected void onPostExecute(String data) {



            if (validate() == 0) {

                HttpClient httpclient = new DefaultHttpClient();


                HttpPost httppost = new HttpPost("http://utilities.money-on-mobile.net/android_userservice/userservice.asmx/Encrypt");

                try {

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                    nameValuePairs.add(new BasicNameValuePair("PlainText", ComposeData()));
                    nameValuePairs.add(new BasicNameValuePair("Key", "f0rZHW8IXM8+YNYL7VptiOMr45m0VZ1yHhXD5zADpB4="));
                    final HttpParams httpParams = httpclient.getParams();
                    HttpConnectionParams.setConnectionTimeout(httpParams, 15000);
                    HttpConnectionParams.setSoTimeout(httpParams, 45000);
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();
                    responseBody = EntityUtils.toString(entity);
                    String check = responseBody;
                    Log.i("postData", response.getStatusLine().toString());
                    Log.i("postData", check);
                    InputStream in = new ByteArrayInputStream(responseBody.getBytes("UTF-8"));
                    new XmlPullParsing(in);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

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
    }
    private class GetLoginTaskCustomerRegistration extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            return responseBody;
        }

        @Override
        protected void onPostExecute(String data) {



                HttpClient httpclient = new DefaultHttpClient();


                HttpPost httppost = new HttpPost("http://utilities.money-on-mobile.net/android_userservice/userservice.asmx/CustomerRegistration");

                try {

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                    nameValuePairs.add(new BasicNameValuePair("Data", data));

                    final HttpParams httpParams = httpclient.getParams();
                    HttpConnectionParams.setConnectionTimeout(httpParams, 15000);
                    HttpConnectionParams.setSoTimeout(httpParams, 45000);
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();
                    responseBody = EntityUtils.toString(entity);
                    String check = responseBody;
                    Log.i("postData", response.getStatusLine().toString());
                    Log.i("DAta", data);
                    Log.i("postDataRegistration", check);


                    InputStream in = new ByteArrayInputStream(responseBody.getBytes("UTF-8"));
                    new XmlPullParsingRegistrationData(in);


                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        }
    public void postSignUpConsumer (String data){

        new GetLoginTaskCustomerRegistration().onPostExecute("Registration");
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









    public class XmlPullParsing {

        protected XmlPullParser xmlpullparser1;
        String output1;
        String TAG = "XmlPullParsing";

        public XmlPullParsing(InputStream is) {


            XmlPullParserFactory factory = null;
            try {
                factory = XmlPullParserFactory.newInstance();
            } catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            factory.setNamespaceAware(true);
            try {
                xmlpullparser1 = factory.newPullParser();
            } catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            try {
                xmlpullparser1.setInput(is, "UTF-8");
            } catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            int eventType = 0;
            try {
                eventType = xmlpullparser1.getEventType();
            } catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            while (eventType != XmlPullParser.END_DOCUMENT) {

                parseTag(eventType);
                try {
                    eventType = xmlpullparser1.next();
                } catch (XmlPullParserException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }


        }

        void parseTag(int event) {

            switch (event) {

                case XmlPullParser.START_DOCUMENT:
                    Log.i(TAG, "START_DOCUMENT");
                    break;

                case XmlPullParser.END_DOCUMENT:
                    Log.i(TAG, "END_DOCUMENT");
                    break;
                case XmlPullParser.START_TAG:
                    Log.i(TAG, "START_TAG" + xmlpullparser1.getName());
                    Log.i(TAG, "Attribute Name" + xmlpullparser1.getAttributeValue(null, "category"));

                    break;

                case XmlPullParser.END_TAG:
                    Log.i(TAG, "END_TAG" + xmlpullparser1.getName());

                    break;

                case XmlPullParser.TEXT:
                    Log.i(TAG, "TEXT");
                    String output = xmlpullparser1.getText();
                    String newoutputrecharge = output;
                  //  postSignUpConsumer(output);
                    new GetLoginTaskCustomerRegistration().onPostExecute(output);
                    Log.i("dataoutput", output);


                    break;

            }


        }

    }

    public class XmlPullParsingRegistrationData {

        protected XmlPullParser xmlpullparser1;
        String output1;
        String TAG = "XmlPullParsing";
        int event;
        String text=null;

        public XmlPullParsingRegistrationData(InputStream is) {


            XmlPullParserFactory factory = null;
            try {
                factory = XmlPullParserFactory.newInstance();
            } catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            factory.setNamespaceAware(true);
            try {
                xmlpullparser1 = factory.newPullParser();
            } catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            try {
                xmlpullparser1.setInput(is, "UTF-8");
            } catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            int eventType = 0;
            try {
                eventType = xmlpullparser1.getEventType();
            } catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            while (eventType != XmlPullParser.END_DOCUMENT) {

                parseTag(eventType);
                try {
                    eventType = xmlpullparser1.next();
                } catch (XmlPullParserException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }


        }

        void parseTag(int event) {
            try {
                event = xmlpullparser1.getEventType();
                while (event != XmlPullParser.END_DOCUMENT) {
                    String name=xmlpullparser1.getName();
                    switch (event){
                        case XmlPullParser.START_TAG:
                            break;
                        case XmlPullParser.TEXT:
                            text = xmlpullparser1.getText();
                            break;

                        case XmlPullParser.END_TAG:
                            if(name.equals("RegistrationStatus")){
                                registrationStatus = text;


                            }
                            else if(name.equals("RegisteredCustomerID")){
                                registeredCustomerID = text;

                            }
                            else if(name.equals("ErrorMessage")){
                                errorMessage = text;

                            }
                            else{
                            }


                            break;
                    }
                    Log.i("RegistrationStatus" , registrationStatus);
                    if(registrationStatus.equals("true")){
                        myintent = new Intent(SignupActivity.this, LoginActivity.class);
                        myintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        myintent.putExtra(AppConstants.SIGNUP_STATUS, 1);
                        startActivity(myintent);
                        finish();
                    }
                    else if(registrationStatus.equals("false")){

                        et_dob.setText("");
                        et_mobileNumber.setText("");
                        et_name.setText("");
                        et_emailId.setText("");
                      //  responseText.setVisibility(View.GONE);
                        btn_login.setVisibility(View.VISIBLE);
                        tv_signUp.setVisibility(View.VISIBLE);

                        tv_signUp.setText(getResources().getString(R.string.error_sinUp_msg));
                        ib.setVisibility(View.GONE);
                        et_dob.setVisibility(View.VISIBLE);
                        et_mobileNumber.setVisibility(View.VISIBLE);
                        et_name.setVisibility(View.VISIBLE);
                        et_emailId.setVisibility(View.VISIBLE);
                        btn_signUp.setVisibility(View.VISIBLE);


                    }
                    event = xmlpullparser1.next();



                }
                parsingComplete = false;
            } catch (Exception e) {
                e.printStackTrace();
            }


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




}




