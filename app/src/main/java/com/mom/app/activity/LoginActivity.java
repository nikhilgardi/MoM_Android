package com.mom.app.activity;

import java.util.List;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.mom.app.R;
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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LoginActivity extends Activity implements AsyncListener <String>{

	EditText _username, _password;

	private ProgressBar _pb;
	private PlatformIdentifier _currentPlatform;
	private Button _loginBtn;
	private TextView _tvMessage;

	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

		_username 	= (EditText) findViewById(R.id.et_un);
		_password 	= (EditText) findViewById(R.id.et_pw);
		getProgressBar().setVisibility(View.GONE);

		getWindow().setBackgroundDrawable(
							getResources().getDrawable(R.drawable.appsbg)
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
            case LOGIN:
                Boolean bSuccess      = Boolean.valueOf(result);
                if(!bSuccess){
                    setLoginFailed(getResources().getString(R.string.login_failed_msg_default));
                    return;
                }

                switch (_currentPlatform){
                    case NEW:

                        break;
                    case PBX:
                        break;
                }
                EphemeralStorage.getInstance(this).storeLocally(MOMConstants.ACTIVE_PLATFORM, _currentPlatform.toString());
                EphemeralStorage.getInstance(this).storeLocally(MOMConstants.LOGGED_IN_USERNAME, _username.getText().toString());
                EphemeralStorage.getInstance(this).storeLocally(MOMConstants.IS_LOGGED_IN, true);

                navigateToMain();
                break;
            case CHECK_PLATFORM_DETAILS:
                Log.i("LOGIN", "Check result: " + result);

                if(result == null || result.trim().equals("")){
                    Log.i("LOGIN", "1. User not of new PL");
                    loginToPBXPL();
                    return;
                }

                String[] sArrDetails	= result.split("~");

                if(sArrDetails.length < 12){
                    Log.i("LOGIN", "2. User not of new PL");
                    loginToPBXPL();
                    return;
                }

                Context context     = getApplicationContext();
                EphemeralStorage.getInstance(this).storeLocally(MOMConstants.PARAM_NEW_USER_ID, sArrDetails[0]);

                EphemeralStorage.getInstance(this).storeLocally(MOMConstants.PARAM_NEW_CUSTOMER_ID, sArrDetails[1]);
                EphemeralStorage.getInstance(this).storeLocally(MOMConstants.PARAM_NEW_MOBILE_NUMBER, sArrDetails[2]);
                EphemeralStorage.getInstance(this).storeLocally(MOMConstants.PARAM_NEW_COMPANY_ID, sArrDetails[3]);
                EphemeralStorage.getInstance(this).storeLocally(MOMConstants.PARAM_NEW_ROLE_ID, sArrDetails[4]);
                EphemeralStorage.getInstance(this).storeLocally(MOMConstants.PARAM_NEW_USER_AUTH_ID, sArrDetails[5]);
                EphemeralStorage.getInstance(this).storeLocally(MOMConstants.PARAM_NEW_USER_WALLET_ID, sArrDetails[6]);
                EphemeralStorage.getInstance(this).storeLocally(MOMConstants.PARAM_NEW_USER_STATUS, sArrDetails[7]);
                EphemeralStorage.getInstance(this).storeLocally(MOMConstants.PARAM_NEW_USER_FRANCH_ID, sArrDetails[8]);
                EphemeralStorage.getInstance(this).storeLocally(MOMConstants.PARAM_NEW_USER_MAST_DIST, sArrDetails[9]);
                EphemeralStorage.getInstance(this).storeLocally(MOMConstants.PARAM_NEW_USER_AREA_DIST, sArrDetails[10]);
                EphemeralStorage.getInstance(this).storeLocally(MOMConstants.PARAM_NEW_USER_VAS01, sArrDetails[11]);
                EphemeralStorage.getInstance(this).storeLocally(MOMConstants.PARAM_NEW_USER_VAS02, sArrDetails[12]);


                loginToNewPL();
                break;
        }

    }

    @Override
    public void onTaskError(AsyncResult pResult, DataExImpl.Methods callback) {

    }

    public boolean isLoggedIn(){
        return EphemeralStorage.getInstance(this).getBoolean(MOMConstants.IS_LOGGED_IN, false);
	}

	public void navigateToMain(){
        Log.i("navigate", "Going to MainActivity");
		Intent intent 						= new Intent(LoginActivity.this, DashboardActivity.class);
		
        Log.i("navigate", "Finishing LoginActivity");

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
        Log.d("LOGIN", "Disable login button");
        
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
		list.add(new BasicNameValuePair(MOMConstants.PARAM_NEW_RMN, username));
		list.add(new BasicNameValuePair(MOMConstants.PARAM_NEW_COMPANY_ID, MOMConstants.NEW_PL_COMPANY_ID));

        NewPLDataExImpl dataEx      = new NewPLDataExImpl(this, this);
        dataEx.checkPlatform(
                new BasicNameValuePair(MOMConstants.PARAM_NEW_RMN, username),
                new BasicNameValuePair(MOMConstants.PARAM_NEW_COMPANY_ID, MOMConstants.NEW_PL_COMPANY_ID)
        );

	}
	
	public void loginToNewPL() {
		Log.i("LOGIN", "Going to login to New pl");
        _currentPlatform        = PlatformIdentifier.NEW;
		try {
			// Add user name and password
			EditText uname = (EditText) findViewById(R.id.et_un);
			String username = uname.getText().toString();

			EditText pword = (EditText) findViewById(R.id.et_pw);
			String password = pword.getText().toString();
            Log.i("LOGIN", "Firing NEWPL login async");
            IDataEx dataEx          = new NewPLDataExImpl(getApplicationContext(), this);

			dataEx.login(
                    new BasicNameValuePair("user", _username.getText().toString()),
                    new BasicNameValuePair("pass", _password.getText().toString()),
                    new BasicNameValuePair(MOMConstants.PARAM_NEW_USER, username),
                    new BasicNameValuePair(MOMConstants.PARAM_NEW_PWD, password),
                    new BasicNameValuePair(MOMConstants.PARAM_NEW_COMPANY_ID, MOMConstants.NEW_PL_COMPANY_ID),
                    new BasicNameValuePair(MOMConstants.PARAM_NEW_STR_ACCESS_ID, "abc")
            );

            Log.i("LOGIN", "Async login request sent to new pl");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void loginToPBXPL() {
        Log.i("LOGIN", "Going to login to PBXPL");
        _currentPlatform        = PlatformIdentifier.PBX;

		try {
			EditText uname = (EditText) findViewById(R.id.et_un);
			String username = uname.getText().toString();

			EditText pword = (EditText) findViewById(R.id.et_pw);
			String password = pword.getText().toString();

            IDataEx dataEx          = new PBXPLDataExImpl(this, getApplicationContext());

            Log.i("LOGIN", "Calling PBXPL for login");

            dataEx.login(
                    new BasicNameValuePair("UN", username),
                    new BasicNameValuePair("Password", password),
                    new BasicNameValuePair("Service", "CL"),
                    new BasicNameValuePair("user", _username.getText().toString()),
                    new BasicNameValuePair("pass", _password.getText().toString())
            );
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

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
