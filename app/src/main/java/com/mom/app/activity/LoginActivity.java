package com.mom.app.activity;

import java.util.List;
import java.util.ArrayList;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.mom.app.R;
import com.mom.app.identifier.PlatformIdentifier;
import com.mom.app.model.AsyncListener;
import com.mom.app.model.AsyncResult;
import com.mom.app.model.DataExImpl;
import com.mom.app.model.IDataEx;
import com.mom.app.model.local.EphemeralStorage;
import com.mom.app.model.local.PersistentStorage;
import com.mom.app.model.mompl.MoMPLDataExImpl;
import com.mom.app.model.pbxpl.PBXPLDataExImpl;
import com.mom.app.ui.LanguageItem;
import com.mom.app.utils.AppConstants;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

public class LoginActivity extends Activity implements AsyncListener <String>{
    String _LOG         = AppConstants.LOG_PREFIX + "LOGIN";
    
            EditText _username, _password;

	private ProgressBar _pb;
	private PlatformIdentifier _currentPlatform;
	private Button _loginBtn;
	private TextView _tvMessage;
    private Spinner _languageSpinner;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setDefaultLocale();
        setContentView(R.layout.activity_login);

		_username 	        = (EditText) findViewById(R.id.et_un);
		_password 	        = (EditText) findViewById(R.id.et_pw);

        setupLanguageSelector();

		getProgressBar().setVisibility(View.GONE);

		getWindow().setBackgroundDrawable(
							getResources().getDrawable(R.drawable.appsbg)
										);
	}

    public void setupLanguageSelector(){
        _languageSpinner    = (Spinner) findViewById(R.id.selectLanguage);

        ArrayAdapter<LanguageItem> dataAdapter = new ArrayAdapter<LanguageItem>(this,
                android.R.layout.simple_spinner_item, LanguageItem.getLanguages(this));

        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        _languageSpinner.setAdapter(dataAdapter);

        _languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Object objSelection    = adapterView.getItemAtPosition(i);
                Log.d(_LOG, "getting selection: " + objSelection);
                LanguageItem selection    = (LanguageItem) objSelection;
                Log.d(_LOG, "Language: " + selection + ", id: " + selection.resourceId);
                setLocale(selection);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                setLocale(LanguageItem.getDefault(getApplicationContext()));
            }
        });

        LanguageItem item       = getSelectedLanguageItem();
        if(item != null){
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

    public Button getLoginBtn(){
        if(_loginBtn == null){
            _loginBtn   = (Button)findViewById(R.id.btnLogin);
        }
        return _loginBtn;
    }

    @Override
    public void onTaskSuccess(String result, DataExImpl.Methods pMethod) {
        getProgressBar().setVisibility(View.GONE);
        switch (pMethod){

            case CHECK_PLATFORM_DETAILS:
                Log.i(_LOG, "Check result: " + result);

                if(result == null || result.trim().equals("")){
                    Log.i(_LOG, "1. User not of new PL");
                    login(PlatformIdentifier.PBX);
                    return;
                }

                String[] sArrDetails	= result.split("~");

                if(sArrDetails.length < 12){
                    Log.i(_LOG, "2. User not of new PL");
                    login(PlatformIdentifier.PBX);
                    return;
                }

                EphemeralStorage.getInstance(this).storeString(AppConstants.PARAM_NEW_USER_ID, sArrDetails[0]);

                EphemeralStorage.getInstance(this).storeString(AppConstants.PARAM_NEW_CUSTOMER_ID, sArrDetails[1]);
                EphemeralStorage.getInstance(this).storeString(AppConstants.PARAM_NEW_MOBILE_NUMBER, sArrDetails[2]);
                EphemeralStorage.getInstance(this).storeString(AppConstants.PARAM_NEW_COMPANY_ID, sArrDetails[3]);
                EphemeralStorage.getInstance(this).storeString(AppConstants.PARAM_NEW_ROLE_ID, sArrDetails[4]);
                EphemeralStorage.getInstance(this).storeString(AppConstants.PARAM_NEW_USER_AUTH_ID, sArrDetails[5]);
                EphemeralStorage.getInstance(this).storeString(AppConstants.PARAM_NEW_USER_WALLET_ID, sArrDetails[6]);
                EphemeralStorage.getInstance(this).storeString(AppConstants.PARAM_NEW_USER_STATUS, sArrDetails[7]);
                EphemeralStorage.getInstance(this).storeString(AppConstants.PARAM_NEW_USER_FRANCH_ID, sArrDetails[8]);
                EphemeralStorage.getInstance(this).storeString(AppConstants.PARAM_NEW_USER_MAST_DIST, sArrDetails[9]);
                EphemeralStorage.getInstance(this).storeString(AppConstants.PARAM_NEW_USER_AREA_DIST, sArrDetails[10]);
                EphemeralStorage.getInstance(this).storeString(AppConstants.PARAM_NEW_USER_VAS01, sArrDetails[11]);
                EphemeralStorage.getInstance(this).storeString(AppConstants.PARAM_NEW_USER_VAS02, sArrDetails[12]);


                login(PlatformIdentifier.NEW);
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
		Intent intent 						= new Intent(LoginActivity.this, DashboardActivity.class);
		
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
        
		getProgressBar().setVisibility(View.VISIBLE);

		
		checkPlatformAndLogin();
	}

	private boolean areLoginCredentialsPresent() {

		if (_username.getText().toString().trim().equals("")) {
//			response.setText(R.string.login_username_required);
            _username.setError(getResources().getString(R.string.error_mobile_required));
			return false;
		} else if (_password.getText().toString().equals("")) {
//			response.setText(R.string.login_pwd_required);
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
                new BasicNameValuePair(AppConstants.PARAM_NEW_RMN, username),
                new BasicNameValuePair(AppConstants.PARAM_NEW_COMPANY_ID, AppConstants.NEW_PL_COMPANY_ID)
        );

	}
	
	public void login(PlatformIdentifier platform) {
		Log.i(_LOG, "Going to login");
        _currentPlatform        = platform;

        final Context context   = this;

        AsyncListener<Boolean> listener = new AsyncListener<Boolean>() {
            @Override
            public void onTaskSuccess(Boolean result, DataExImpl.Methods callback) {
                getProgressBar().setVisibility(View.GONE);

                if(!result){
                    setLoginFailed(getResources().getString(R.string.login_failed_msg_default));
                    return;
                }

                switch (_currentPlatform){
                    case NEW:

                        break;
                    case PBX:
                        break;
                }

                EphemeralStorage.getInstance(context).storeString(AppConstants.ACTIVE_PLATFORM, _currentPlatform.toString());
                EphemeralStorage.getInstance(context).storeString(AppConstants.LOGGED_IN_USERNAME, _username.getText().toString());
                EphemeralStorage.getInstance(context).storeBoolean(AppConstants.IS_LOGGED_IN, true);

                navigateToMain();
            }

            @Override
            public void onTaskError(AsyncResult pResult, DataExImpl.Methods callback) {
                getProgressBar().setVisibility(View.GONE);
                setLoginFailed(getResources().getString(R.string.login_failed_msg_default));
            }
        };

        IDataEx dataEx          = null;
        if(platform == PlatformIdentifier.NEW) {
            dataEx              = new MoMPLDataExImpl(getApplicationContext(), listener);
        }else{
            dataEx              = new PBXPLDataExImpl(getApplicationContext(), listener);
        }

        dataEx.login(_username.getText().toString(), _password.getText().toString());

        Log.i(_LOG, "Async login request sent");

	}
//
//	public void loginToPBXPL() {
//        Log.i(_LOG, "Going to login to PBXPL");
//        _currentPlatform        = PlatformIdentifier.PBX;
//
//		try {
//			EditText uname = (EditText) findViewById(R.id.et_un);
//			String username = uname.getText().toString();
//
//			EditText pword = (EditText) findViewById(R.id.et_pw);
//			String password = pword.getText().toString();
//
//            IDataEx dataEx          = new PBXPLDataExImpl(this, getApplicationContext());
//
//            Log.i(_LOG, "Calling PBXPL for login");
//
//            dataEx.login(username, password);
//
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//	}

    public TextView getMessageTextView(){
        if(_tvMessage == null){
            _tvMessage = (TextView) findViewById(R.id.msgDisplay);
        }
        return _tvMessage;
    }

	public void showMessage(String psMsg){
		TextView response	= getMessageTextView();
		
		response.setVisibility(View.VISIBLE);
		response.setText(psMsg);
	}
	
	public void setLoginFailed(String psMsg){
		showMessage(psMsg);
		
        _username.setText("");
        _password.setText("");
		
		getLoginBtn().setEnabled(true);
	}
}
