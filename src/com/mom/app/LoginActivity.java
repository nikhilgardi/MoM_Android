package com.mom.app;

import java.util.List;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Vector;

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
import java.security.*;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.mom.app.model.local.PersistentStorage;
import com.mom.app.ui.LanguageItem;
import com.mom.app.utils.AppConstants;

public class LoginActivity extends Activity implements OnClickListener {
    String _LOG = AppConstants.LOG_PREFIX + "LOGIN";

    EditText username, password;
    TextView response1, tv_user, tv_pass, response2;
    Button post;
    public static final String PREFERENCE_FILENAME = "AppPreferences";

    Intent myintent = new Intent();
    private Spinner _languageSpinner;
    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

    public String responseBody, response_status, response_message;
    protected String status;
    protected String message;
    protected String check;
    protected PublicKey pubkey;
    protected String test;
    Helpz myHelpez = new Helpz();
    protected XmlPullParser xmlpullparser;
    private Locale myLocale;
    Button ok, back, exit;
    TextView result;
    SplitOutput splitoutput = new SplitOutput();

    String newoutput, output;
    String[] strArrayResponse;
    boolean _spinnerCalledOnce  = false;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(_LOG, "Resource name: " + getResources().getResourceEntryName(2131165580));
        setDefaultLocale();
        setContentView(R.layout.main);

        setupLanguageSelector();

        username = (EditText) findViewById(R.id.et_un);
        password = (EditText) findViewById(R.id.et_pw);
        post = (Button) findViewById(R.id.btn_login);
        response1 = (TextView) findViewById(R.id.tv_response);
        response2 = (TextView) findViewById(R.id.tv_response1);
        tv_user = (TextView) findViewById(R.id.tv_username);
        tv_pass = (TextView) findViewById(R.id.tv_password);
        ok = (Button) findViewById(R.id.btn_login);

        ok.setOnClickListener(this);
        getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.appsbg));
        Helpz myHelp = new Helpz();
        Boolean booltest = myHelp.GetLogoutVariable();

        if (booltest == false)


        {
            this.username.setVisibility(View.VISIBLE);
            this.password.setVisibility(View.VISIBLE);
            this.tv_pass.setVisibility(View.VISIBLE);
            this.tv_user.setVisibility(View.VISIBLE);
            this.post.setVisibility(View.VISIBLE);
            this.response1.setVisibility(View.VISIBLE);
            this.response2.setVisibility(View.VISIBLE);
        } else if (booltest == true) {
            this.username.setVisibility(View.INVISIBLE);
            this.password.setVisibility(View.INVISIBLE);
            this.tv_pass.setVisibility(View.INVISIBLE);
            this.tv_user.setVisibility(View.INVISIBLE);
            this.post.setVisibility(View.INVISIBLE);
            this.response1.setVisibility(View.INVISIBLE);
            this.response2.setVisibility(View.INVISIBLE);
            myintent = new Intent(LoginActivity.this, LoginActivity.class);
            startActivity(myintent);

        }


    }

    public void setupLanguageSelector() {
        _languageSpinner = (Spinner) findViewById(R.id.selectLanguage);

        ArrayAdapter<LanguageItem> dataAdapter = new ArrayAdapter<LanguageItem>(this,
                android.R.layout.simple_spinner_item, LanguageItem.getLanguages(this));

        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        _languageSpinner.setAdapter(dataAdapter);


        _languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Object objSelection = adapterView.getItemAtPosition(i);
                Log.d(_LOG, "getting selection: " + objSelection);
                LanguageItem selection = (LanguageItem) objSelection;
                Log.d(_LOG, "Language: " + selection + ", id: " + selection.resourceId);
                setLocale(selection);

                if(_spinnerCalledOnce){
                    Intent intent   = getIntent();
                    finish();
                    startActivity(intent);
                }

                _spinnerCalledOnce  = true;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                setLocale(LanguageItem.getDefault(getApplicationContext()));
            }
        });

        LanguageItem item = getSelectedLanguageItem();

        if (item != null) {
            _languageSpinner.setSelection(dataAdapter.getPosition(item));
        }
    }

    private LanguageItem getSelectedLanguageItem() {
        int selectedLanguageId = PersistentStorage.getInstance(getApplicationContext()).getInt(AppConstants.USER_LANGUAGE, -1);
        if (selectedLanguageId != -1) {
            return LanguageItem.getLanguage(this, selectedLanguageId);
        }
        return null;
    }

    private void setDefaultLocale() {
        LanguageItem item = getSelectedLanguageItem();
        if(item == null){
            item            = LanguageItem.getDefault(this);
        }


        setLocale(item);
    }

    private void setLocale(LanguageItem item) {

        PersistentStorage.getInstance(getApplicationContext()).storeInt(AppConstants.USER_LANGUAGE, item.resourceId);
        Log.d(_LOG, "Setting locale to: " + item.code);

        Locale locale = new Locale(item.code);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(
                config,
                getBaseContext().getResources().getDisplayMetrics()
        );


    }


//	@Override
//	public void onUserLeaveHint() {
//		this.finish();
//	}

    public void onBackPressed() {

        this.finish();

    }

    public void postData(View v) {
        this.fillPostData();
    }

    public void postLoginData() {

        SaveSessionData();

        try {
            //Helpz myHelpez = (Helpz) Class.forName("Helpz").newInstance();
            myHelpez.SetMyLoginMobileNumber(username.getText().toString());

        } catch (Exception ex) {
        }
        // Create a new HttpClient and Post Header

        HttpClient httpclient = new DefaultHttpClient();

		/* login.php returns true if username and password is equal to saranga */
        HttpPost httppost = new HttpPost("http://msvc.money-on-mobile.net/WebServiceV3Client.asmx/getLoggedIn");

        try {
            // Add user name and password
            EditText uname = (EditText) findViewById(R.id.et_un);
            String username = uname.getText().toString();

            EditText pword = (EditText) findViewById(R.id.et_pw);
            String password = pword.getText().toString();

            String access = "abc";
            String Companyid = "184";

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
            nameValuePairs.add(new BasicNameValuePair("strUserRMN", username));
            nameValuePairs.add(new BasicNameValuePair("strPassword", password));
            nameValuePairs.add(new BasicNameValuePair("CompanyID", Companyid.toString()));
            nameValuePairs.add(new BasicNameValuePair("strAccessId", access));
            final HttpParams httpParams = httpclient.getParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 15000);
            HttpConnectionParams.setSoTimeout(httpParams, 45000);
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request

            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            this.responseBody = EntityUtils.toString(entity);
            String check = responseBody;
            Log.i("postData", response.getStatusLine().toString());
            Log.i("postData", this.responseBody);

            InputStream in = new ByteArrayInputStream(
                    this.responseBody.getBytes("UTF-8"));

            new XmlPullParsing(in);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    public void postLoginDataB2C() {

        SaveSessionDataB2C();

        try {
            //Helpz myHelpez = (Helpz) Class.forName("Helpz").newInstance();
            myHelpez.SetMyLoginMobileNumber(username.getText().toString());

        } catch (Exception ex) {
        }
        // Create a new HttpClient and Post Header

        HttpClient httpclient = new DefaultHttpClient();

		/* login.php returns true if username and password is equal to saranga */
        HttpPost httppost = new HttpPost("http://msvc.money-on-mobile.net/WebServiceV3Client.asmx/getLoggedIn");

        try {
            // Add user name and password
            EditText uname = (EditText) findViewById(R.id.et_un);
            String username = uname.getText().toString();

            EditText pword = (EditText) findViewById(R.id.et_pw);
            String password = pword.getText().toString();

            String access = "abc";
            String Companyid = "2365";

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
            nameValuePairs.add(new BasicNameValuePair("strUserRMN", username));
            nameValuePairs.add(new BasicNameValuePair("strPassword", password));
            nameValuePairs.add(new BasicNameValuePair("CompanyID", Companyid.toString()));
            nameValuePairs.add(new BasicNameValuePair("strAccessId", access));
            final HttpParams httpParams = httpclient.getParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 15000);
            HttpConnectionParams.setSoTimeout(httpParams, 45000);
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request

            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            this.responseBody = EntityUtils.toString(entity);
            String check = responseBody;
            Log.i("postData", response.getStatusLine().toString());
            Log.i("postData", this.responseBody);

            InputStream in = new ByteArrayInputStream(
                    this.responseBody.getBytes("UTF-8"));

            new XmlPullParsingB2C(in);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void postLoginDataPBX() {


        try {

            myHelpez.SetMyLoginMobileNumber(username.getText().toString());

        } catch (Exception ex) {
        }
        // Create a new HttpClient and Post Header

        HttpClient httpclient = new DefaultHttpClient();

		/* login.php returns true if username and password is equal to saranga */

        HttpPost httppost = new HttpPost("http://180.179.67.76/MobAppS/PbxMobApp.ashx");
        try {
            // Add user name and password
            EditText uname = (EditText) findViewById(R.id.et_un);
            String username = uname.getText().toString();

            EditText pword = (EditText) findViewById(R.id.et_pw);
            String password = pword.getText().toString();


            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            nameValuePairs.add(new BasicNameValuePair("UN", username));
            nameValuePairs.add(new BasicNameValuePair("Password", password));
            nameValuePairs.add(new BasicNameValuePair("Service", "CL"));


            final HttpParams httpParams = httpclient.getParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 15000);
            HttpConnectionParams.setSoTimeout(httpParams, 45000);
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request

            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            this.responseBody = EntityUtils.toString(entity);
            String check = responseBody;
            Log.i("postData", response.getStatusLine().toString());
            Log.i("postData", this.responseBody);

            output = check;
            String newoutput = output;
            String[] strArrayResponse = Split(newoutput, "~");
            String[] abc = strArrayResponse;
            int i = Integer.parseInt(strArrayResponse[0].toString());
            myHelpez.SetMyRoleID(strArrayResponse[2].toString());

            switch (Integer.parseInt(strArrayResponse[0].toString())) {
                case 1:

                    //Toast.makeText(LoginActivity.this, strArrayResponse[0].toString(), Toast.LENGTH_LONG).show();
                    //response1.setVisibility(View.VISIBLE);
                    //response1.setText(strArrayResponse[1].toString());
                    myintent = new Intent(LoginActivity.this, MainActivity1.class);
                    myintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(myintent);
                    finish();
                    break;

                default:

                    response1.setVisibility(View.VISIBLE);
                    response1.setText(strArrayResponse[1].toString());
                    this.username.setText("");
                    this.password.setText("");

                    ok.setEnabled(true);

                    break;


            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }

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

    public class XmlPullParsing {

        protected XmlPullParser xmlpullparser;
        String output;
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
                xmlpullparser = factory.newPullParser();
            } catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {
                xmlpullparser.setInput(is, "UTF-8");
            } catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            int eventType = 0;
            try {
                eventType = xmlpullparser.getEventType();
            } catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            while (eventType != XmlPullParser.END_DOCUMENT) {

                parseTag(eventType);
                try {
                    eventType = xmlpullparser.next();
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
                    Log.i(TAG, "START_TAG" + xmlpullparser.getName());
                    Log.i(TAG,
                            "Attribute Name"
                                    + xmlpullparser.getAttributeValue(null,
                                    "category")
                    );

                    break;

                case XmlPullParser.END_TAG:
                    Log.i(TAG, "END_TAG" + xmlpullparser.getName());

                    break;

                case XmlPullParser.TEXT:
                    Log.i(TAG, "TEXT");
                    output = xmlpullparser.getText();
                    String newoutput = output;
                    //	String[] strArrayResponse = Split(newoutput, "~");
                    String[] strArrayResponse = splitoutput.Split(newoutput, "~");
                    int i = Integer.parseInt(strArrayResponse[0].toString());

                    switch (Integer.parseInt(strArrayResponse[0].toString())) {
                        case 101:
                            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor prefEditor = pref.edit();
                            prefEditor.putString("user_sessionMOM", "MOM");
                            prefEditor.commit();


                            myintent = new Intent(LoginActivity.this, MainActivity1.class);
                            myintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(myintent);
                            finish();
                            break;

                        default:
//					SharedPreferences prefPBX = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//					SharedPreferences.Editor prefEditorPBX = prefPBX.edit();
//					prefEditorPBX.putString("user_sessionMOM", "PBX");  
//					prefEditorPBX.commit();
//					postLoginDataPBX();

                            SharedPreferences prefB2C = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor prefEditorB2C = prefB2C.edit();
                            prefEditorB2C.putString("user_sessionMOM", "B2C");
                            prefEditorB2C.commit();
                            postLoginDataB2C();

                            break;
                    }

            }

        }

    }

    public class XmlPullParsingB2C {

        protected XmlPullParser xmlpullparser;
        String output;
        String TAG = "XmlPullParsing";

        public XmlPullParsingB2C(InputStream is) {

            XmlPullParserFactory factory = null;
            try {
                factory = XmlPullParserFactory.newInstance();
            } catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            factory.setNamespaceAware(true);
            try {
                xmlpullparser = factory.newPullParser();
            } catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {
                xmlpullparser.setInput(is, "UTF-8");
            } catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            int eventType = 0;
            try {
                eventType = xmlpullparser.getEventType();
            } catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            while (eventType != XmlPullParser.END_DOCUMENT) {

                parseTag(eventType);
                try {
                    eventType = xmlpullparser.next();
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
                    Log.i(TAG, "START_TAG" + xmlpullparser.getName());
                    Log.i(TAG,
                            "Attribute Name"
                                    + xmlpullparser.getAttributeValue(null,
                                    "category")
                    );

                    break;

                case XmlPullParser.END_TAG:
                    Log.i(TAG, "END_TAG" + xmlpullparser.getName());

                    break;

                case XmlPullParser.TEXT:
                    Log.i(TAG, "TEXT");
                    output = xmlpullparser.getText();
                    String newoutput = output;
                    //	String[] strArrayResponse = Split(newoutput, "~");
                    String[] strArrayResponse = splitoutput.Split(newoutput, "~");
                    int i = Integer.parseInt(strArrayResponse[0].toString());

                    switch (Integer.parseInt(strArrayResponse[0].toString())) {
                        case 101:
//					SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//					SharedPreferences.Editor prefEditor = pref.edit();
//					prefEditor.putString("user_sessionMOM", "MOM");  
//					prefEditor.commit();


                            myintent = new Intent(LoginActivity.this, MainActivity1.class);
                            myintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(myintent);
                            finish();
                            break;

                        default:
                            SharedPreferences prefPBX = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor prefEditorPBX = prefPBX.edit();
                            prefEditorPBX.putString("user_sessionMOM", "PBX");
                            prefEditorPBX.commit();
                            postLoginDataPBX();

//					SharedPreferences prefB2C = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//					SharedPreferences.Editor prefEditorB2C = prefB2C.edit();
//					prefEditorB2C.putString("user_sessionMOM", "B2C");  
//					prefEditorB2C.commit();
//					postLoginDataB2C();

                            break;
                    }

            }

        }

    }


    public class XmlPullParsingPBX {

        protected XmlPullParser xmlpullparser;
        String output;
        String TAG = "XmlPullParsing";

        public XmlPullParsingPBX(InputStream is) {

            XmlPullParserFactory factory = null;
            try {
                factory = XmlPullParserFactory.newInstance();
            } catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            factory.setNamespaceAware(true);
            try {
                xmlpullparser = factory.newPullParser();
            } catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {
                xmlpullparser.setInput(is, "UTF-8");
            } catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            int eventType = 0;
            try {
                eventType = xmlpullparser.getEventType();
            } catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            while (eventType != XmlPullParser.END_DOCUMENT) {

                parseTag(eventType);
                try {
                    eventType = xmlpullparser.next();
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
                    Log.i(TAG, "START_TAG" + xmlpullparser.getName());
                    Log.i(TAG,
                            "Attribute Name"
                                    + xmlpullparser.getAttributeValue(null,
                                    "category")
                    );

                    break;

                case XmlPullParser.END_TAG:
                    Log.i(TAG, "END_TAG" + xmlpullparser.getName());

                    break;

                case XmlPullParser.TEXT:
                    Log.i(TAG, "TEXT");
                    output = xmlpullparser.getText();
                    String newoutput = output;
                    String[] strArrayResponse = Split(newoutput, "~");

                    int i = Integer.parseInt(strArrayResponse[0].toString());

                    switch (Integer.parseInt(strArrayResponse[0].toString())) {
                        case 1:

                            //	Toast.makeText(LoginActivity.this, strArrayResponse[0].toString(), Toast.LENGTH_LONG).show();
                            //	response1.setVisibility(View.VISIBLE);
                            //	response1.setText(strArrayResponse[1].toString());
                            myintent = new Intent(LoginActivity.this, MainActivity1.class);
                            startActivity(myintent);
                            break;

                        default:

                            response1.setVisibility(View.VISIBLE);
                            response1.setText(strArrayResponse[1].toString());
                            username.setText("");
                            password.setText("");
                            ok.setEnabled(true);

                            break;
                    }

            }

        }

    }


    public boolean SaveSessionData() {
        try {
            //Helpz myHelp = new Helpz();
            postLoginDetails();
            String[] strResponse1 = Split(check, "~");
            myHelpez.SetMyUserId(strResponse1[0]);
            myHelpez.SetMyCustomerId(strResponse1[1]);
            myHelpez.SetMyRechargeMobileNumber(strResponse1[2]);
            myHelpez.SetMyCompanyId(strResponse1[3]);
            myHelpez.SetMyRoleID(strResponse1[4]);
            myHelpez.SetMyUserAuthID(strResponse1[5]);
            myHelpez.SetMyUserWalletID(strResponse1[6]);
            myHelpez.SetMyUserStatus(strResponse1[7]);
            myHelpez.SetMyUserFranchID(strResponse1[8]);
            myHelpez.SetMyUserMastDist(strResponse1[9]);
            myHelpez.SetMyUserAreaDist(strResponse1[10]);
            myHelpez.SetMyUserVAS01(strResponse1[11]);
            myHelpez.SetMyUserVAS02(strResponse1[12]);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public void postLoginDetails() {

        // Create a new HttpClient and Post Header

        HttpClient httpclient = new DefaultHttpClient();


        try {
            // Add user name and password
            EditText uname = (EditText) findViewById(R.id.et_un);
            String username = uname.getText().toString();

            HttpPost httppost = new HttpPost(
                    "http://180.179.67.72/nokiaservice/DetailsByUserRMNCompID.aspx?UserRMN="
                            + username + "&CompanyID=184"
            );

            // Execute HTTP Post Request

            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            this.responseBody = EntityUtils.toString(entity);
            String check = responseBody;
            Log.i("postData", response.getStatusLine().toString());
            Log.i("postData", this.responseBody);

//			Helpz myHelp = new Helpz();
            try {

                String[] strResponse1 = Split(check, "~");
                myHelpez.SetMyUserId(strResponse1[0]);
                myHelpez.SetMyCustomerId(strResponse1[1]);
                myHelpez.SetMyRechargeMobileNumber(strResponse1[2]);
                myHelpez.SetMyCompanyId(strResponse1[3]);
                myHelpez.SetMyRoleID(strResponse1[4]);
                myHelpez.SetMyUserAuthID(strResponse1[5]);
                myHelpez.SetMyUserWalletID(strResponse1[6]);
                myHelpez.SetMyUserStatus(strResponse1[7]);
                myHelpez.SetMyUserFranchID(strResponse1[8]);
                myHelpez.SetMyUserMastDist(strResponse1[9]);
                myHelpez.SetMyUserAreaDist(strResponse1[10]);
                myHelpez.SetMyUserVAS01(strResponse1[11]);
                myHelpez.SetMyUserVAS02(strResponse1[12]);
            } catch (Exception ex) {

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public boolean SaveSessionDataB2C() {
        try {
            //Helpz myHelp = new Helpz();
            postLoginDetailsB2C();

            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public void postLoginDetailsB2C() {

        // Create a new HttpClient and Post Header

        HttpClient httpclient = new DefaultHttpClient();


        try {
            // Add user name and password
            EditText uname = (EditText) findViewById(R.id.et_un);
            String username = uname.getText().toString();

            HttpPost httppost = new HttpPost(
                    "http://180.179.67.72/nokiaservice/DetailsByUserRMNCompID.aspx?UserRMN="
                            + username + "&CompanyID=2365"
            );

            // Execute HTTP Post Request

            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            this.responseBody = EntityUtils.toString(entity);
            String check = responseBody;
            Log.i("postData", response.getStatusLine().toString());
            Log.i("postData", this.responseBody);

//			Helpz myHelp = new Helpz();
            try {

                String[] strResponse1 = Split(check, "~");
                myHelpez.SetMyUserId(strResponse1[0]);
                myHelpez.SetMyCustomerId(strResponse1[1]);
                myHelpez.SetMyRechargeMobileNumber(strResponse1[2]);
                myHelpez.SetMyCompanyId(strResponse1[3]);
                myHelpez.SetMyRoleID(strResponse1[4]);
                myHelpez.SetMyUserAuthID(strResponse1[5]);
                myHelpez.SetMyUserWalletID(strResponse1[6]);
                myHelpez.SetMyUserStatus(strResponse1[7]);
                myHelpez.SetMyUserFranchID(strResponse1[8]);
                myHelpez.SetMyUserMastDist(strResponse1[9]);
                myHelpez.SetMyUserAreaDist(strResponse1[10]);
                myHelpez.SetMyUserVAS01(strResponse1[11]);
                myHelpez.SetMyUserVAS02(strResponse1[12]);
            } catch (Exception ex) {

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private boolean fillPostData() {
        if (username.getText().toString().trim().equals("") && ((password.getText().toString().equals("")))) {
            response1.setText(getResources().getString(R.string.login_missing_params));
            return false;
        }
        if (username.getText().toString().trim().equals("")) {
            response1.setText(getResources().getString(R.string.login_username_required));
            return false;
        } else if (password.getText().toString().equals("")) {
            response1.setText(getResources().getString(R.string.login_pwd_required));
            return false;
        } else {
            nameValuePairs.add(new BasicNameValuePair("user", username.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("pass", password.getText().toString()));
            return true;
        }

    }

    // @Override


    public void onClick(View view) {
        if (this.fillPostData()) {
            ok.setEnabled(false);
            postLoginData();
            new GetLoginTask().onPostExecute("test");

        }
    }


    private class GetLoginTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {


            return responseBody;
        }

        @Override
        protected void onPostExecute(String result) {

            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

            if ((pref.getString("user_sessionMOM", "test").equals("MOM")) || (pref.getString("user_sessionMOM", "test").equals("B2C")))

            {


                try {

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
                    nameValuePairs.add(new BasicNameValuePair("OperatorID", myHelpez.GetMyCustomerId()));
                    nameValuePairs.add(new BasicNameValuePair("CompanyID", myHelpez.GetMyCompanyId()));
                    nameValuePairs.add(new BasicNameValuePair("strAccessID", GlobalVariables.AccessId));
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost("http://msvc.money-on-mobile.net/WebServiceV3Client.asmx/getBalanceByCustomerId");
                    httppost.addHeader("ua", "android");
                    final HttpParams httpParams = httpclient.getParams();
                    HttpConnectionParams.setConnectionTimeout(httpParams, 15000);
                    HttpConnectionParams.setSoTimeout(httpParams, 15000);
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();
                    responseBody = EntityUtils.toString(entity);
                    check = responseBody;
//					error="0";
                    Log.i("postData", response.getStatusLine().toString());
                    Log.i("info", responseBody);

                    InputStream in = new ByteArrayInputStream(responseBody.getBytes("UTF-8"));
                    new XmlPullParsingAccntBal(in);

                } catch (Exception e) {
                    Log.e("log_tag", "Error in http connection " + e.toString());
                    responseBody = "Timeout|Error in Http Connection";
//					error="1";
                }
            } else if (pref.getString("user_sessionMOM", "test").equals("PBX")) {

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://180.179.67.76/MobAppS/PbxMobApp.ashx");
                try {


                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                    nameValuePairs.add(new BasicNameValuePair("RN", myHelpez.GetMyLoginMobileNumber()));
                    nameValuePairs.add(new BasicNameValuePair("Service", "BL"));


                    httppost.addHeader("ua", "android");
                    final HttpParams httpParams = httpclient.getParams();
                    HttpConnectionParams.setConnectionTimeout(httpParams, 15000);
                    HttpConnectionParams.setSoTimeout(httpParams, 15000);
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();
                    responseBody = EntityUtils.toString(entity);
                    check = responseBody;
//					Double number = Double.valueOf(check);
//					DecimalFormat df = new DecimalFormat("#.00");
//					String newtestString = df.format(number);
                    //
//					String abc = newtestString;
                    // error="0";
                    Log.i("postData", response.getStatusLine().toString());
                    Log.i("info", responseBody);


                    myHelpez.SetRMNAccountBal(check);
                } catch (Exception e) {
                    Log.e("log_tag", "Error in http connection " + e.toString());
                    responseBody = "Timeout|Error in Http Connection";
                    // error="1";
                }
            } else {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            }
        }
    }

    public class XmlPullParsingAccntBal {

        protected XmlPullParser xmlpullparser1;
        String output1;
        String TAG = "XmlPullParsing";

        public XmlPullParsingAccntBal(InputStream is) {


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
                    output = xmlpullparser1.getText();
                    String newoutputrecharge = output;

                    //////////        Toast.makeText(InfoActivity.this, newoutputrecharge, Toast.LENGTH_LONG).show();


                    //response.setText("Bal: Rs. 100000000");
                    myHelpez.SetRMNAccountBal(newoutputrecharge);
                    break;

            }


        }

    }
}

