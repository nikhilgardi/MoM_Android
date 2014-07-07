package com.mom.app;

import java.util.List;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

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

public class LoginActivity extends Activity implements AsyncListener {

	EditText _username, _password;
//	TextView response1, tv_user, tv_pass,response2;
	Button post;
//	public static final String PREFERENCE_FILENAME = "AppPreferences";
	
//	Intent myintent = new Intent();
	
//	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	
	public String _responseBody, response_status, response_message;
	private ProgressBar _pb;
	private PlatformIdentifier _currentPlatform;
//	protected String status;
//	protected String message;
//	protected String check;
//	protected PublicKey pubkey;
//	protected String test;
//	Helpz myHelpez = new Helpz();
//	protected XmlPullParser xmlpullparser;
	private Button _loginBtn;
	
//	Button ok, back, exit;
	TextView result;
	SplitOutput splitoutput = new SplitOutput();

//	String newoutput, output;
//	String[] strArrayResponse;
	
	
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
		_pb			= (ProgressBar)findViewById(R.id.progressBarLogin);

        if(_pb != null) {
            _pb.setVisibility(View.GONE);
        }
		
//		post = (Button) findViewById(R.id.btn_login);
//		response1 = (TextView) findViewById(R.id.tv_response);
//		response2 = (TextView) findViewById(R.id.tv_response1);
//		tv_user = (TextView) findViewById(R.id.tv_username);
//		tv_pass = (TextView) findViewById(R.id.tv_password);
		
//		ok = (Button) findViewById(R.id.btn_login);
		
//		ok.setOnClickListener(this);

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
        Helpz.SetMyLoginMobileNumber(_username.getText().toString());
        navigateToMain();
    }

	public boolean isLoggedIn(){
        return LocalStorage.getBoolean(getApplicationContext(), MOMConstants.PREF_IS_LOGGED_IN);
	}

	public void navigateToMain(){
        Log.i("navigate", "Going to MainActivity");
		Intent intent 						= new Intent(LoginActivity.this, MainActivity.class);
		
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
//		getProgressBar().setVisibility(View.VISIBLE);

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
//		SaveSessionData();
		
		// Create a new HttpClient and Post Header

//		String loginUrl				= MOMConstants.URL_NEW_PLATFORM + MOMConstants.SVC_METHOD_LOGIN_NEW;
		
		try {
			// Add user name and password
			EditText uname = (EditText) findViewById(R.id.et_un);
			String username = uname.getText().toString();

			EditText pword = (EditText) findViewById(R.id.et_pw);
			String password = pword.getText().toString();
			
//			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
			
//			nameValuePairs.add(new BasicNameValuePair("user", _username.getText().toString()));
//			nameValuePairs.add(new BasicNameValuePair("pass", _password.getText().toString()));
			
//			nameValuePairs.add(new BasicNameValuePair(MOMConstants.PARAM_NEW_USER, username));
//			nameValuePairs.add(new BasicNameValuePair(MOMConstants.PARAM_NEW_PWD, password));
//			nameValuePairs.add(new BasicNameValuePair(MOMConstants.PARAM_NEW_COMPANY_ID, MOMConstants.NEW_PL_COMPANY_ID));
//			nameValuePairs.add(new BasicNameValuePair(MOMConstants.PARAM_NEW_ACCESS_ID, "abc"));
			
//			this._responseBody	= MiscUtils.getHttpResponse(loginUrl, nameValuePairs);

//			InputStream in = new ByteArrayInputStream(this._responseBody.getBytes("UTF-8"));

//			new XmlPullParsing(in);
            Log.i("LOGIN", "Firing NEWPL login async");
//			AsyncDataEx	dataEx		= new AsyncDataEx(this, loginUrl, PlatformIdentifier.NEW);
            IDataEx dataEx          = new NewPLDataExImpl(this, getApplicationContext());

			dataEx.login(
                    new BasicNameValuePair("user", _username.getText().toString()),
                    new BasicNameValuePair("pass", _password.getText().toString()),
                    new BasicNameValuePair(MOMConstants.PARAM_NEW_USER, username),
                    new BasicNameValuePair(MOMConstants.PARAM_NEW_PWD, password),
                    new BasicNameValuePair(MOMConstants.PARAM_NEW_COMPANY_ID, MOMConstants.NEW_PL_COMPANY_ID),
                    new BasicNameValuePair(MOMConstants.PARAM_NEW_ACCESS_ID, "abc")
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
//			Helpz.SetMyLoginMobileNumber(_username.getText().toString());
			
			// Add user name and password
			EditText uname = (EditText) findViewById(R.id.et_un);
			String username = uname.getText().toString();

			EditText pword = (EditText) findViewById(R.id.et_pw);
			String password = pword.getText().toString();

            IDataEx dataEx          = new PBXPLDataExImpl(this);

            Log.i("LOGIN", "Calling PBXPL for login");

            dataEx.login(
                    new BasicNameValuePair("UN", username),
                    new BasicNameValuePair("Password", password),
                    new BasicNameValuePair("Service", "CL"),
                    new BasicNameValuePair("user", _username.getText().toString()),
                    new BasicNameValuePair("pass", _password.getText().toString())
            );

//			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//			nameValuePairs.add(new BasicNameValuePair("UN", username));
//			nameValuePairs.add(new BasicNameValuePair("Password", password));
//			nameValuePairs.add(new BasicNameValuePair("Service", "CL"));
//			nameValuePairs.add(new BasicNameValuePair("user", _username.getText().toString()));
//			nameValuePairs.add(new BasicNameValuePair("pass", _password.getText().toString()));
			
//			_responseBody					= MiscUtils.getHttpPostResponse(MOMConstants.URL_PBX_PLATFORM, nameValuePairs);
//
//			if(_responseBody == null || _responseBody.trim().equals("")){
//				setLoginFailed(getResources().getString(R.string.login_failed_msg_default));
//				return;
//			}
//
//			String[] sArrResponse			= _responseBody.split("~");
//
//			if(Integer.parseInt(sArrResponse[0]) != 1){
//				if(sArrResponse.length > 1){
//					setLoginFailed(sArrResponse[1]);
//					return;
//				}
//			}else{
//				setLoginFailed(getResources().getString(R.string.login_failed_msg_default));
//				return;
//			}
//			navigateToMain();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}


	public void showMessage(String psMsg){
		TextView response	= (TextView) findViewById(R.id.tv_response);
		
		response.setVisibility(View.VISIBLE);
		response.setText(psMsg);
	}
	
	public void setLoginFailed(String psMsg){
		showMessage(psMsg);
		
        _username.setText("");
        _password.setText("");
		
		getLoginBtn().setEnabled(true);
	}
	

//	public class XmlPullParsing {
//
//		protected XmlPullParser xmlpullparser;
//		String output;
//		String TAG = "XmlPullParsing";
//
//		public XmlPullParsing(InputStream is) {
//
//			XmlPullParserFactory factory = null;
//			try {
//				factory = XmlPullParserFactory.newInstance();
//			} catch (XmlPullParserException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			factory.setNamespaceAware(true);
//			try {
//				xmlpullparser = factory.newPullParser();
//			} catch (XmlPullParserException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//			try {
//				xmlpullparser.setInput(is, "UTF-8");
//			} catch (XmlPullParserException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//			int eventType = 0;
//			try {
//				eventType = xmlpullparser.getEventType();
//			} catch (XmlPullParserException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			while (eventType != XmlPullParser.END_DOCUMENT) {
//
//				parseTag(eventType);
//				try {
//					eventType = xmlpullparser.next();
//				} catch (XmlPullParserException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//
//		}
//
//		void parseTag(int event) {
//
//			switch (event) {
//
//			case XmlPullParser.START_DOCUMENT:
//				Log.i(TAG, "START_DOCUMENT");
//				break;
//
//			case XmlPullParser.END_DOCUMENT:
//				Log.i(TAG, "END_DOCUMENT");
//				break;
//			case XmlPullParser.START_TAG:
//				Log.i(TAG, "START_TAG" + xmlpullparser.getName());
//				Log.i(TAG,
//						"Attribute Name"
//								+ xmlpullparser.getAttributeValue(null,
//										"category"));
//
//				break;
//
//			case XmlPullParser.END_TAG:
//				Log.i(TAG, "END_TAG" + xmlpullparser.getName());
//
//				break;
//
//			case XmlPullParser.TEXT:
//				Log.i(TAG, "TEXT");
////				output = xmlpullparser.getText();
//				String newoutput = xmlpullparser.getText();
//                Log.i(TAG, "Response: " + newoutput);
//			//	String[] strArrayResponse = Split(newoutput, "~");
//				String[] strArrayResponse = newoutput.split("~");
//
//				int i = Integer.parseInt(strArrayResponse[0]);
//
//				switch (i) {
//                    case 101:
//                        Log.i(TAG, "Login successful");
//                        LocalStorage.storeLocally(getApplicationContext(), MOMConstants.ACTIVE_PLATFORM, PlatformIdentifier.NEW.toString());
//                        LocalStorage.storeLocally(getApplicationContext(), "user_sessionMOM", "MOM");
//
//                        Helpz.SetMyLoginMobileNumber(_username.getText().toString());
//
//                        navigateToMain();
//                        break;
//
//                    default:
//                        SharedPreferences prefPBX = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                        SharedPreferences.Editor prefEditorPBX = prefPBX.edit();
//                        prefEditorPBX.putString("user_sessionMOM", "PBX");
//                        prefEditorPBX.commit();
//                        postLoginDataPBX();
//                        break;
//				}
//
//			}
//
//		}
//
//	}

	
//	public class XmlPullParsingPBX {
//
//		protected XmlPullParser xmlpullparser;
//		String output;
//		String TAG = "XmlPullParsing";
//
//		public XmlPullParsingPBX(InputStream is) {
//
//			XmlPullParserFactory factory = null;
//			try {
//				factory = XmlPullParserFactory.newInstance();
//			} catch (XmlPullParserException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			factory.setNamespaceAware(true);
//			try {
//				xmlpullparser = factory.newPullParser();
//			} catch (XmlPullParserException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//			try {
//				xmlpullparser.setInput(is, "UTF-8");
//			} catch (XmlPullParserException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//			int eventType = 0;
//			try {
//				eventType = xmlpullparser.getEventType();
//			} catch (XmlPullParserException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			while (eventType != XmlPullParser.END_DOCUMENT) {
//
//				parseTag(eventType);
//				try {
//					eventType = xmlpullparser.next();
//				} catch (XmlPullParserException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//
//		}
//
//		void parseTag(int event) {
//
//			switch (event) {
//
//			case XmlPullParser.START_DOCUMENT:
//				Log.i(TAG, "START_DOCUMENT");
//				break;
//
//			case XmlPullParser.END_DOCUMENT:
//				Log.i(TAG, "END_DOCUMENT");
//				break;
//			case XmlPullParser.START_TAG:
//				Log.i(TAG, "START_TAG" + xmlpullparser.getName());
//				Log.i(TAG,
//						"Attribute Name"
//								+ xmlpullparser.getAttributeValue(null,
//										"category"));
//
//				break;
//
//			case XmlPullParser.END_TAG:
//				Log.i(TAG, "END_TAG" + xmlpullparser.getName());
//
//				break;
//
//			case XmlPullParser.TEXT:
//				Log.i(TAG, "TEXT");
//				output = xmlpullparser.getText();
//				String newoutput = output;
//				String[] strArrayResponse = newoutput.split("~");
//
//				int i = Integer.parseInt(strArrayResponse[0].toString());
//
//				switch (Integer.parseInt(strArrayResponse[0].toString())) {
//				case 1:
//
//				//	Toast.makeText(LoginActivity.this, strArrayResponse[0].toString(), Toast.LENGTH_LONG).show();
//				//	response1.setVisibility(View.VISIBLE);
//				//	response1.setText(strArrayResponse[1].toString());
//					navigateToMain();
//					break;
//
//				default:
//					setLoginFailed(strArrayResponse[1]);
//
//					break;
//				}
//
//			}
//
//		}
//
//	}
	
	
	
	
//	public boolean SaveSessionData() {
//		try {
//			//Helpz myHelp = new Helpz();
//			postLoginDetails();
//			String[] strResponse1 = check.split("~");
//			
//			myHelpez.SetMyUserId(strResponse1[0]);
//			myHelpez.SetMyCustomerId(strResponse1[1]);
//			myHelpez.SetMyRechargeMobileNumber(strResponse1[2]);
//			myHelpez.SetMyCompanyId(strResponse1[3]);
//			myHelpez.SetMyRoleID(strResponse1[4]);
//			myHelpez.SetMyUserAuthID(strResponse1[5]);
//			myHelpez.SetMyUserWalletID(strResponse1[6]);
//			myHelpez.SetMyUserStatus(strResponse1[7]);
//			myHelpez.SetMyUserFranchID(strResponse1[8]);
//			myHelpez.SetMyUserMastDist(strResponse1[9]);
//			myHelpez.SetMyUserAreaDist(strResponse1[10]);
//			myHelpez.SetMyUserVAS01(strResponse1[11]);
//			myHelpez.SetMyUserVAS02(strResponse1[12]);
//			return true;
//		} catch (Exception ex) {
//			return false;
//		}
//	}

	
	
//	public void postLoginDetails() {
//
//		// Create a new HttpClient and Post Header
//
//		HttpClient httpclient = new DefaultHttpClient();
//
//		
//		try {
//			// Add user name and password
//			EditText uname = (EditText) findViewById(R.id.et_un);
//			String username = uname.getText().toString();
//
//			HttpPost httppost = new HttpPost(
//					"http://180.179.67.72/nokiaservice/DetailsByUserRMNCompID.aspx?UserRMN="
//							+ username + "&CompanyID=184");
//
//			// Execute HTTP Post Request
//
//			HttpResponse response = httpclient.execute(httppost);
//			HttpEntity entity = response.getEntity();
//			this._responseBody = EntityUtils.toString(entity);
//			String check = _responseBody;
//			Log.i("postData", response.getStatusLine().toString());
//			Log.i("postData", this._responseBody);
//
////			Helpz myHelp = new Helpz();
//			try {
//
//				String[] strResponse1 = check.split("~");
//				myHelpez.SetMyUserId(strResponse1[0]);
//				myHelpez.SetMyCustomerId(strResponse1[1]);
//				myHelpez.SetMyRechargeMobileNumber(strResponse1[2]);
//				myHelpez.SetMyCompanyId(strResponse1[3]);
//				myHelpez.SetMyRoleID(strResponse1[4]);
//				myHelpez.SetMyUserAuthID(strResponse1[5]);
//				myHelpez.SetMyUserWalletID(strResponse1[6]);
//				myHelpez.SetMyUserStatus(strResponse1[7]);
//				myHelpez.SetMyUserFranchID(strResponse1[8]);
//				myHelpez.SetMyUserMastDist(strResponse1[9]);
//				myHelpez.SetMyUserAreaDist(strResponse1[10]);
//				myHelpez.SetMyUserVAS01(strResponse1[11]);
//				myHelpez.SetMyUserVAS02(strResponse1[12]);
//			} catch (Exception ex) {
//
//			}
//
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//
//	}

	

	// @Override

//	public void onClick(View view) {
//		if (areLoginCredentialsPresent()) {
//			ok.setEnabled(false);
//			loginToNewPL();
//			new GetLoginTask().onPostExecute("test");
//		}
//	}

//	private class GetLoginTask extends AsyncTask<Void, Void, String> {
//
//		@Override
//		protected String doInBackground(Void... params) {
//
//			return _responseBody;
//		}
//
//		@Override
//		protected void onPostExecute(String result) {
//
//			SharedPreferences pref = PreferenceManager
//					.getDefaultSharedPreferences(getApplicationContext());
//
//			if (pref.getString("user_sessionMOM", "test").equals("MOM"))
//
//			{
//
//				try {
//
//					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
//							3);
//					nameValuePairs.add(new BasicNameValuePair("OperatorID",
//							myHelpez.GetMyCustomerId()));
//					nameValuePairs.add(new BasicNameValuePair("CompanyID",
//							myHelpez.GetMyCompanyId()));
//					nameValuePairs.add(new BasicNameValuePair("strAccessID",
//							GlobalVariables.AccessId));
//					HttpClient httpclient = new DefaultHttpClient();
//					HttpPost httppost = new HttpPost(
//							"http://msvc.money-on-mobile.net/WebServiceV3Client.asmx/getBalanceByCustomerId");
//					httppost.addHeader("ua", "android");
//					final HttpParams httpParams = httpclient.getParams();
//					HttpConnectionParams
//							.setConnectionTimeout(httpParams, 15000);
//					HttpConnectionParams.setSoTimeout(httpParams, 15000);
//					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//					HttpResponse response = httpclient.execute(httppost);
//					HttpEntity entity = response.getEntity();
//					_responseBody = EntityUtils.toString(entity);
//					check = _responseBody;
//					// error="0";
//					Log.i("postData", response.getStatusLine().toString());
//					Log.i("info", _responseBody);
//
//					InputStream in = new ByteArrayInputStream(
//							_responseBody.getBytes("UTF-8"));
//					new XmlPullParsingAccntBal(in);
//
//				} catch (Exception e) {
//					Log.e("log_tag", "Error in http connection " + e.toString());
//					_responseBody = "Timeout|Error in Http Connection";
//					// error="1";
//				}
//			} else if (pref.getString("user_sessionMOM", "test").equals("PBX")) {
//
//				HttpClient httpclient = new DefaultHttpClient();
//				HttpPost httppost = new HttpPost(
//						"http://180.179.67.76/MobAppS/PbxMobApp.ashx");
//				try {
//
//					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
//							2);
//					nameValuePairs.add(new BasicNameValuePair("RN", myHelpez
//							.GetMyLoginMobileNumber()));
//					nameValuePairs.add(new BasicNameValuePair("Service", "BL"));
//
//					httppost.addHeader("ua", "android");
//					final HttpParams httpParams = httpclient.getParams();
//					HttpConnectionParams
//							.setConnectionTimeout(httpParams, 15000);
//					HttpConnectionParams.setSoTimeout(httpParams, 15000);
//					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//					HttpResponse response = httpclient.execute(httppost);
//					HttpEntity entity = response.getEntity();
//					_responseBody = EntityUtils.toString(entity);
//					check = _responseBody;
//					// Double number = Double.valueOf(check);
//					// DecimalFormat df = new DecimalFormat("#.00");
//					// String newtestString = df.format(number);
//					//
//					// String abc = newtestString;
//					// error="0";
//					Log.i("postData", response.getStatusLine().toString());
//					Log.i("info", _responseBody);
//
//					myHelpez.SetRMNAccountBal(check);
//				} catch (Exception e) {
//					Log.e("log_tag", "Error in http connection " + e.toString());
//					_responseBody = "Timeout|Error in Http Connection";
//					// error="1";
//				}
//			}
//
//			else {
//				Toast.makeText(getApplicationContext(), "Error",
//						Toast.LENGTH_LONG).show();
//			}
//		}
//	}

//	public class XmlPullParsingAccntBal {
//
//		protected XmlPullParser xmlpullparser1;
//		String output1;
//		String TAG = "XmlPullParsing";
//
//		public XmlPullParsingAccntBal(InputStream is) {
//
//			XmlPullParserFactory factory = null;
//			try {
//				factory = XmlPullParserFactory.newInstance();
//			} catch (XmlPullParserException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			factory.setNamespaceAware(true);
//			try {
//				xmlpullparser1 = factory.newPullParser();
//			} catch (XmlPullParserException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//			try {
//				xmlpullparser1.setInput(is, "UTF-8");
//			} catch (XmlPullParserException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//			int eventType = 0;
//			try {
//				eventType = xmlpullparser1.getEventType();
//			} catch (XmlPullParserException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			while (eventType != XmlPullParser.END_DOCUMENT) {
//
//				parseTag(eventType);
//				try {
//					eventType = xmlpullparser1.next();
//				} catch (XmlPullParserException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//
//		}
//
//		void parseTag(int event) {
//
//			switch (event) {
//
//			case XmlPullParser.START_DOCUMENT:
//				Log.i(TAG, "START_DOCUMENT");
//				break;
//
//			case XmlPullParser.END_DOCUMENT:
//				Log.i(TAG, "END_DOCUMENT");
//				break;
//			case XmlPullParser.START_TAG:
//				Log.i(TAG, "START_TAG" + xmlpullparser1.getName());
//				Log.i(TAG,
//						"Attribute Name"
//								+ xmlpullparser1.getAttributeValue(null,
//										"category"));
//
//				break;
//
//			case XmlPullParser.END_TAG:
//				Log.i(TAG, "END_TAG" + xmlpullparser1.getName());
//
//				break;
//
//			case XmlPullParser.TEXT:
//				Log.i(TAG, "TEXT");
//				output = xmlpullparser1.getText();
//				String newoutputrecharge = output;
//
//				// //////// Toast.makeText(InfoActivity.this, newoutputrecharge,
//				// Toast.LENGTH_LONG).show();
//
//				// response.setText("Bal: Rs. 100000000");
//				myHelpez.SetRMNAccountBal(newoutputrecharge);
//				break;
//
//			}
//
//		}
//
//	}




	

}
