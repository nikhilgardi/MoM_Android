package com.mom.app.activity;

import java.util.List;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.mom.app.Helpz;
import com.mom.app.R;
import com.mom.app.identifier.PlatformIdentifier;
import com.mom.app.model.AsyncListener;
import com.mom.app.model.DataExImpl;
import com.mom.app.model.IDataEx;
import com.mom.app.model.local.LocalStorage;
import com.mom.app.model.newpl.NewPLDataExImpl;
import com.mom.app.model.pbxpl.PBXPLDataExImpl;
import com.mom.app.utils.MOMConstants;
import com.mom.app.utils.MiscUtils;

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
        setContentView(R.layout.login);

		if(isLoggedIn()){
			navigateToMain();
			return;
		}

		_username 	= (EditText) findViewById(R.id.et_un);
		_password 	= (EditText) findViewById(R.id.et_pw);
		getProgressBar().setVisibility(View.GONE);


		getWindow().setBackgroundDrawable(
							getResources().getDrawable(R.drawable.appsbg)
										);

	}

    public ProgressBar getProgressBar(){
        if(_pb == null){
            _pb			= (ProgressBar)findViewById(R.id.progressBarLogin);
        }
        return _pb;
    }

    public Button getLoginBtn(){
        if(_loginBtn == null){
            _loginBtn   = (Button)findViewById(R.id.btn_login);
        }
        return _loginBtn;
    }

    @Override
    public void onTaskComplete(String result, DataExImpl.Methods pMethod) {
        getProgressBar().setVisibility(View.GONE);
        Boolean bSuccess      = Boolean.valueOf(result);
        if(!bSuccess){
            setLoginFailed(getResources().getString(R.string.login_failed_msg_default));
            return;
        }

        switch (_currentPlatform){
            case NEW:
                LocalStorage.storeLocally(getApplicationContext(), "user_sessionMOM", "MOM");
            break;
            case PBX:
                LocalStorage.storeLocally(getApplicationContext(), "user_sessionMOM", "PBX");
            break;
        }

        LocalStorage.storeLocally(getApplicationContext(), MOMConstants.ACTIVE_PLATFORM, _currentPlatform.toString());
        LocalStorage.storeLocally(getApplicationContext(), MOMConstants.LOGGED_IN_USERNAME, _username.getText().toString());

        Helpz.SetMyLoginMobileNumber(_username.getText().toString());
        navigateToMain();
    }

	public boolean isLoggedIn(){
        return LocalStorage.getBoolean(getApplicationContext(), MOMConstants.PREF_IS_LOGGED_IN);
	}

	public void navigateToMain(){
        Log.i("navigate", "Going to MainActivity");
		Intent intent 						= new Intent(LoginActivity.this, DashboardActivity.class);
		
//		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Log.i("navigate", "Finishing LoginActivity");

		startActivity(intent);
	}

	
//	@Override
//	public void onUserLeaveHint() {
//		this.finish();
//	}

	public void onBackPressed() {
        finish();
	}
	
	public void startLogin(View v) {
		getLoginBtn().setEnabled(false);
		getProgressBar().setVisibility(View.VISIBLE);

		if(!areLoginCredentialsPresent()){
			return;
		}
		
		if(isNewPLLogin()){
			loginToNewPL();
		}else{
			loginToPBXPL();
		}
	}

	private boolean areLoginCredentialsPresent() {
		TextView response	= (TextView) findViewById(R.id.tv_response);
		
		if (
			_username.getText().toString().trim().equals("")
			&& ((_password.getText().toString().equals("")))
			){
			
			response.setVisibility(View.VISIBLE);
			response.setText(R.string.login_username_pwd_required);
			
			return false;
		}else if (_username.getText().toString().trim().equals("")) {
			response.setText(R.string.login_username_required);
			return false;
		} else if (_password.getText().toString().equals("")) {
			response.setText(R.string.login_pwd_required);
			return false;
		}
		
		return true;
	}
	
	public boolean isNewPLLogin(){
		EditText uname 				= (EditText) findViewById(R.id.et_un);
		String username 			= uname.getText().toString();
		
		List<NameValuePair> list	= new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair(MOMConstants.PARAM_NEW_RMN, username));
		list.add(new BasicNameValuePair(MOMConstants.PARAM_NEW_COMPANY_ID, MOMConstants.NEW_PL_COMPANY_ID));
		
		String response	= MiscUtils.getHttpGetResponse(
                MOMConstants.URL_NEW_PL_DETAILS + "?" + MOMConstants.PARAM_NEW_RMN + "=" + username + "&"
                        + MOMConstants.PARAM_NEW_COMPANY_ID + "=" + MOMConstants.NEW_PL_COMPANY_ID
        );

        Log.i("LOGIN", "Check result: " + response);

		if(response == null || response.trim().equals("")){
            Log.i("LOGIN", "1. User not of new PL");
			return false;
		}
		
		String[] sArrDetails	= response.split("~");
		
		if(sArrDetails.length < 12){
            Log.i("LOGIN", "2. User not of new PL");
			return false;
		}

		Context context     = getApplicationContext();
        LocalStorage.storeLocally(context, MOMConstants.PARAM_NEW_USER_ID, sArrDetails[0]);
        LocalStorage.storeLocally(context, MOMConstants.PARAM_NEW_CUSTOMER_ID, sArrDetails[1]);
        LocalStorage.storeLocally(context, MOMConstants.PARAM_NEW_MOBILE_NUMBER, sArrDetails[2]);
        LocalStorage.storeLocally(context, MOMConstants.PARAM_NEW_COMPANY_ID, sArrDetails[3]);
        LocalStorage.storeLocally(context, MOMConstants.PARAM_NEW_ROLE_ID, sArrDetails[4]);
        LocalStorage.storeLocally(context, MOMConstants.PARAM_NEW_USER_AUTH_ID, sArrDetails[5]);
        LocalStorage.storeLocally(context, MOMConstants.PARAM_NEW_USER_WALLET_ID, sArrDetails[6]);
        LocalStorage.storeLocally(context, MOMConstants.PARAM_NEW_USER_STATUS, sArrDetails[7]);
        LocalStorage.storeLocally(context, MOMConstants.PARAM_NEW_USER_FRANCH_ID, sArrDetails[8]);
        LocalStorage.storeLocally(context, MOMConstants.PARAM_NEW_USER_MAST_DIST, sArrDetails[9]);
        LocalStorage.storeLocally(context, MOMConstants.PARAM_NEW_USER_AREA_DIST, sArrDetails[10]);
        LocalStorage.storeLocally(context, MOMConstants.PARAM_NEW_USER_VAS01, sArrDetails[11]);
        LocalStorage.storeLocally(context, MOMConstants.PARAM_NEW_USER_VAS02, sArrDetails[12]);

        Helpz.SetMyUserId(sArrDetails[0]);
		Helpz.SetMyCustomerId(sArrDetails[1]);
		Helpz.SetMyRechargeMobileNumber(sArrDetails[2]);
		Helpz.SetMyCompanyId(sArrDetails[3]);
		Helpz.SetMyRoleID(sArrDetails[4]);
		Helpz.SetMyUserAuthID(sArrDetails[5]);
		Helpz.SetMyUserWalletID(sArrDetails[6]);
		Helpz.SetMyUserStatus(sArrDetails[7]);
		Helpz.SetMyUserFranchID(sArrDetails[8]);
		Helpz.SetMyUserMastDist(sArrDetails[9]);
		Helpz.SetMyUserAreaDist(sArrDetails[10]);
		Helpz.SetMyUserVAS01(sArrDetails[11]);
		Helpz.SetMyUserVAS02(sArrDetails[12]);
		
		return true;
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
            _tvMessage = (TextView) findViewById(R.id.tv_response);
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
