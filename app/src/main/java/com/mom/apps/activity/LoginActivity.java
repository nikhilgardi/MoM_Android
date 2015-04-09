package com.mom.apps.activity;

import java.util.List;
import java.util.ArrayList;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.mom.apps.R;
import com.mom.apps.error.MOMException;

import com.mom.apps.gcm.GcmUtil;
import com.mom.apps.identifier.PlatformIdentifier;
import com.mom.apps.model.AsyncListener;
import com.mom.apps.model.AsyncResult;
import com.mom.apps.model.DataExImpl;
import com.mom.apps.model.IDataEx;
import com.mom.apps.model.b2cpl.B2CPLDataExImpl;
import com.mom.apps.model.local.EphemeralStorage;
import com.mom.apps.model.local.PersistentStorage;
import com.mom.apps.model.mompl.MoMPLDataExImpl;
import com.mom.apps.model.pbxpl.PBXPLDataExImpl;
import com.mom.apps.ui.LanguageItem;
import com.mom.apps.utils.AppConstants;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
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

public class LoginActivity extends Activity implements AsyncListener <String>{
    String _LOG         = AppConstants.LOG_PREFIX + "LOGIN";
    

    static final int REQUEST_CODE_RECOVER_PLAY_SERVICES = 1001;
	private ProgressBar _pb;
	private PlatformIdentifier _currentPlatform;
	private Button _loginBtn , _SignUpBtn;
	private TextView _tvMessage ,_tvsignUp;
    private Spinner _languageSpinner;
    EditText _username, _password;
    boolean _spinnerCalledOnce = false;
    Intent myintent = new Intent();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        //MOHIT
        DataExImpl._listener = null;
      //  DataExImpl._balance_listener = null;

        setDefaultLocale();
        setContentView(R.layout.activity_login);

		_username 	        = (EditText) findViewById(R.id.et_un);
		_password 	        = (EditText) findViewById(R.id.et_pw);
        _tvsignUp = (TextView) findViewById(R.id.tv_signUp);
        _SignUpBtn          = (Button) findViewById(R.id.btn_signUp);
        setupLanguageSelector();
        getMessageTextView().setVisibility(View.GONE);
		getProgressBar().setVisibility(View.GONE);

        //
//		getWindow().setBackgroundDrawable(
//							getResources().getDrawable(R.drawable.appsbg)
//										);

        Intent intentObject = getIntent();
        int isRegistered = intentObject.getIntExtra(AppConstants.SIGNUP_STATUS , -1);
        Log.i("test" , String.valueOf(isRegistered));

        switch(isRegistered) {
            case -1:
                Log.i("test11" , String.valueOf(isRegistered));
                _tvsignUp.setVisibility(View.GONE);
                Log.i("test1" , String.valueOf(isRegistered));
                break;
            case 1:
                _tvsignUp.setVisibility(View.VISIBLE);
                Log.i("test12" , String.valueOf(isRegistered));
                _tvsignUp.setText(getResources().getString(R.string.singUp_Successful_msg));
                Toast.makeText(LoginActivity.this , "done" , Toast.LENGTH_LONG);
                Log.i("test14" , String.valueOf(isRegistered));
                break;


        }
        Log.i("test34" , String.valueOf(isRegistered));
	}

    @Override
    protected void onResume() {
        super.onResume();

        if(checkPlayServices()){
            GcmUtil.getInstance(this).registerDevice();
        }
    }

    private boolean checkPlayServices() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (status != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(status)) {
                showErrorDialog(status);
            } else {
                Toast.makeText(this, "This device is not supported.",
                        Toast.LENGTH_LONG).show();
                finish();
            }
            Log.d(_LOG, "Play services not available");
            return false;
        }
        Log.d(_LOG, "Play services available");
        return true;
    }

    void showErrorDialog(int code) {
        GooglePlayServicesUtil.getErrorDialog(code, this, REQUEST_CODE_RECOVER_PLAY_SERVICES).show();
    }

//    public void setupLanguageSelector(){
//        _languageSpinner    = (Spinner) findViewById(R.id.selectLanguage);
//
//        ArrayAdapter<LanguageItem> dataAdapter = new ArrayAdapter<LanguageItem>(this,
//                android.R.layout.simple_spinner_item, LanguageItem.getLanguages(this));
//
//        dataAdapter
//                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        _languageSpinner.setAdapter(dataAdapter);
//
//        _languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Object objSelection    = adapterView.getItemAtPosition(i);
//                Log.d(_LOG, "getting selection: " + objSelection);
//                LanguageItem selection    = (LanguageItem) objSelection;
//                Log.d(_LOG, "Language: " + selection + ", id: " + selection.resourceId);
//                setLocale(selection);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//                setLocale(LanguageItem.getDefault(getApplicationContext()));
//            }
//        });
//
//        LanguageItem item       = getSelectedLanguageItem();
//        if(item != null){
//            _languageSpinner.setSelection(dataAdapter.getPosition(item));
//        }
//    }

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

    private LanguageItem getSelectedLanguageItem(){
        int selectedLanguageId  = PersistentStorage.getInstance(getApplicationContext()).getInt(AppConstants.USER_LANGUAGE, -1);
        if(selectedLanguageId != -1){
            return LanguageItem.getLanguage(this, selectedLanguageId);
        }
        return null;
    }

    private void setDefaultLocale(){
        LanguageItem item       = getSelectedLanguageItem();
        if(item != null){
            setLocale(item);
        }
    }

    private void setLocale(LanguageItem item){

        PersistentStorage.getInstance(getApplicationContext()).storeInt(AppConstants.USER_LANGUAGE, item.resourceId);
        Log.d(_LOG, "Setting locale to: " + item.code);

        Locale locale           = new Locale(item.code);
        Locale.setDefault(locale);
        Configuration config    = new Configuration();
        config.locale           = locale;
        getBaseContext().getResources().updateConfiguration(
                config,
                getBaseContext().getResources().getDisplayMetrics()
        );


    }

    public ProgressBar getProgressBar(){
        if(_pb == null){
            _pb			= (ProgressBar)findViewById(R.id.progressBar);
        }
        return _pb;
    }

    public void showProgressBar(){
        getProgressBar().setVisibility(View.VISIBLE);
    }

    public void hideProgressBar(){
        getProgressBar().setVisibility(View.GONE);
    }

    public Button getLoginBtn(){
        if(_loginBtn == null){
            _loginBtn   = (Button)findViewById(R.id.btnLogin);
        }
        return _loginBtn;
    }

    @Override
    public void onTaskSuccess(String result, DataExImpl.Methods pMethod) {
        switch (pMethod){
            case CHECK_PLATFORM_DETAILS:
                Log.i(_LOG, "Check result: " + result);

                //TESTING. TODO: Remove this
//              result = null;
                //TESTING

                   if(TextUtils.isEmpty(result)) {
                        Log.i(_LOG, "User not of new PL");
                        login(PlatformIdentifier.PBX);
                        return;
                    }


                String[] sArrDetails	= result.split("~");
                Log.i("Array", sArrDetails[1]);
                if(sArrDetails.length < 13){
                Log.i(_LOG, "2. User not of new PL");
                login(PlatformIdentifier.PBX);
                EphemeralStorage.getInstance(this).storeString(AppConstants.PARAM_NEW_USER_ID, sArrDetails[0]);

                return;
            }

                if(sArrDetails[3].equals("184")){
                    login(PlatformIdentifier.MOM);

                }

                else if(sArrDetails[3].equals("2365")){
                    login(PlatformIdentifier.B2C);
                }



                EphemeralStorage.getInstance(this).storeString(AppConstants.PARAM_NEW_USER_ID, sArrDetails[0]);

                EphemeralStorage.getInstance(this).storeString(AppConstants.PARAM_NEW_CUSTOMER_ID, sArrDetails[1]);
                EphemeralStorage.getInstance(this).storeString(AppConstants.PARAM_NEW_MOBILE_NUMBER, sArrDetails[2]);
                EphemeralStorage.getInstance(this).storeString(AppConstants.PARAM_NEW_COMPANY_ID, sArrDetails[3]);
                EphemeralStorage.getInstance(this).storeString(AppConstants.NEW_B2C_COMPANY_ID, sArrDetails[3]);
                Log.i("testCID" , EphemeralStorage.getInstance(this).getString(AppConstants.PARAM_NEW_COMPANY_ID , null));
                EphemeralStorage.getInstance(this).storeString(AppConstants.PARAM_NEW_ROLE_ID, sArrDetails[4]);
                EphemeralStorage.getInstance(this).storeString(AppConstants.PARAM_NEW_USER_AUTH_ID, sArrDetails[5]);
                EphemeralStorage.getInstance(this).storeString(AppConstants.PARAM_NEW_USER_WALLET_ID, sArrDetails[6]);
                EphemeralStorage.getInstance(this).storeString(AppConstants.PARAM_NEW_USER_STATUS, sArrDetails[7]);
                EphemeralStorage.getInstance(this).storeString(AppConstants.PARAM_NEW_USER_FRANCH_ID, sArrDetails[8]);
                EphemeralStorage.getInstance(this).storeString(AppConstants.PARAM_NEW_USER_MAST_DIST, sArrDetails[9]);
                EphemeralStorage.getInstance(this).storeString(AppConstants.PARAM_NEW_USER_AREA_DIST, sArrDetails[10]);
                EphemeralStorage.getInstance(this).storeString(AppConstants.PARAM_NEW_USER_VAS01, sArrDetails[11]);
                EphemeralStorage.getInstance(this).storeString(AppConstants.PARAM_NEW_USER_VAS02, sArrDetails[12]);

                Log.i(_LOG, EphemeralStorage.getInstance(this).getString(AppConstants.PARAM_NEW_CUSTOMER_ID , null));

               // login(PlatformIdentifier.MOM);
                break;
        }
    }

    @Override
    public void onTaskError(AsyncResult pResult, DataExImpl.Methods callback) {

    }

    public boolean isLoggedIn(){
        return EphemeralStorage.getInstance(this).getBoolean(AppConstants.IS_LOGGED_IN, false);
	}

	public void navigateToMain(){
        Log.i(_LOG, "Going to MainActivity");
		Intent intent 						= new Intent(LoginActivity.this, BaseActivity.class);
        Log.i(_LOG, "Finishing LoginActivity");

		startActivity(intent);
	}


	public void onBackPressed() {
        finish();
	}
	
	public void startLogin(View v) {
        if(!areLoginCredentialsPresent()){
            return;
        }


		getLoginBtn().setEnabled(false);
        Log.d(_LOG, "Disable login button");
        
		showProgressBar();
		
		checkPlatformAndLogin();
	}

	private boolean areLoginCredentialsPresent() {
        int nMinLength          = 10;
        int nMaxLength          = 10;
        int nUserNumberLength   = _username.getText().toString().trim().length();

		if (_username.getText().toString().trim().equals("")) {

            _username.setError(getResources().getString(R.string.error_mobile_required));
			return false;
		}
        else if(nUserNumberLength < nMinLength || nUserNumberLength > nMaxLength){
            if(nMinLength == nMaxLength){
                _username.setError(String.format(getResources().getString(R.string.error_phone_length), nMinLength));

            }else{
                _username.setError(String.format(getResources().getString(R.string.error_phone_length_min_max), nMinLength, nMaxLength));

            }
            return false;
      }
      else if (_password.getText().toString().equals("")) {

            _password.setError(getResources().getString(R.string.login_pwd_required));
			return false;
		}

		return true;
	}


	
	public void checkPlatformAndLogin(){
		EditText uname 				= (EditText) findViewById(R.id.et_un);
		String username 			= uname.getText().toString();
		
		List<NameValuePair> list	= new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair(AppConstants.PARAM_NEW_RMN, username));
		list.add(new BasicNameValuePair(AppConstants.PARAM_NEW_COMPANY_ID, AppConstants.NEW_PL_COMPANY_ID));

        MoMPLDataExImpl dataEx      = new MoMPLDataExImpl(this, this);

        dataEx.checkPlatform(
                new BasicNameValuePair(AppConstants.PARAM_NEW_RMN, username)
              //  new BasicNameValuePair(AppConstants.PARAM_NEW_COMPANY_ID, AppConstants.NEW_PL_COMPANY_ID)
        );

	}


    public void checkPlatformAndLoginB2C(){
        EditText uname 				= (EditText) findViewById(R.id.et_un);
        String username 			= uname.getText().toString();

        List<NameValuePair> list	= new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair(AppConstants.PARAM_NEW_RMN, username));
        list.add(new BasicNameValuePair(AppConstants.PARAM_NEW_COMPANY_ID, AppConstants.NEW_B2C_COMPANY_ID));

        B2CPLDataExImpl dataEx      = new B2CPLDataExImpl(this, this);

        dataEx.checkPlatform(
                new BasicNameValuePair(AppConstants.PARAM_NEW_RMN, username),
                new BasicNameValuePair(AppConstants.PARAM_NEW_COMPANY_ID, AppConstants.NEW_B2C_COMPANY_ID)
        );

    }
	
	public void login(PlatformIdentifier platform) {
		Log.i(_LOG, "Going to login");
        _currentPlatform        = platform;

        Log.i("_currentPlatform" , platform.toString());

        final Context context   = this;

        AsyncListener<Boolean> listener = new AsyncListener<Boolean>() {
            @Override
            public void onTaskSuccess(Boolean result, DataExImpl.Methods callback) {
                String username = _username.getText().toString();
                String password = _password.getText().toString();
                hideProgressBar();
                hideMessage();
                if (!result) {
                    setLoginFailed(R.string.login_failed_msg_default);
                    return;
                }

//                switch (_currentPlatform){
//                    case MOM:
//
//                        break;
//                    case PBX:
//                        break;
//                }

                getLoginBtn().setEnabled(true);
                _username.setText(null);
                _password.setText(null);
                Log.d(_LOG, "Enable login button");
                EphemeralStorage.getInstance(context).storeObject(AppConstants.ACTIVE_PLATFORM, _currentPlatform);
                EphemeralStorage.getInstance(context).storeString(AppConstants.LOGGED_IN_USERNAME, username);
                EphemeralStorage.getInstance(context).storeString(AppConstants.LOGGED_IN_PASSWORD, password);
                Log.i("RMN", username);
                EphemeralStorage.getInstance(context).storeBoolean(AppConstants.IS_LOGGED_IN, true);

                // temporary
                navigateToMain();

            }

            @Override
            public void onTaskError(AsyncResult pResult, DataExImpl.Methods callback) {
                getProgressBar().setVisibility(View.GONE);
                setLoginFailed(R.string.login_failed_msg_default);
            }
        };

        IDataEx dataEx;

        try {
            if (platform == PlatformIdentifier.MOM) {
                dataEx = new MoMPLDataExImpl(getApplicationContext(), listener);
            }else if(platform == PlatformIdentifier.B2C){
                dataEx = new B2CPLDataExImpl(getApplicationContext() , listener);
            }else {
                dataEx = new PBXPLDataExImpl(getApplicationContext(), DataExImpl.Methods.LOGIN, listener);
            }

            dataEx.login(_username.getText().toString(), _password.getText().toString());
        }catch(MOMException me){
            Log.e(_LOG, "Error getting dataex", me);
            showMessage(R.string.error_init_app);
        }

        Log.i(_LOG, "Async login request sent");
	}

    public TextView getMessageTextView(){
        if(_tvMessage == null){
            _tvMessage = (TextView) findViewById(R.id.msgDisplay);
        }
        return _tvMessage;
    }



	public void showMessage(int id){
		TextView response	= getMessageTextView();
		
		response.setVisibility(View.VISIBLE);
		response.setText(getString(id));
	}
	
	public void setLoginFailed(int id){
		showMessage(id);
        _password.setText(null);

		getLoginBtn().setEnabled(true);
	}

    public void hideMessage(){
            getMessageTextView().setVisibility(View.GONE);
        }


    public void signUp(View view) {
        myintent = new Intent(LoginActivity.this, SignupActivity.class);
        myintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(myintent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DataExImpl._listener = null;
       // DataExImpl._balance_listener = null;
    }
}
