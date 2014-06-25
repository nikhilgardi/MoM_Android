package com.example.moneyonmobileapplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
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
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener {

	EditText username, password;
	TextView response1, tv_user, tv_pass,response2;
	Button post;
	public static final String PREFERENCE_FILENAME = "AppPreferences";
	
	Intent myintent = new Intent();
	
	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	
	public String responseBody, response_status, response_message;
	protected String status;
	protected String message;
	protected String check;
	protected PublicKey pubkey;
	protected String test;
	Helpz myHelpez = new Helpz();
	protected XmlPullParser xmlpullparser;

	Button ok, back, exit;
	TextView result;
	SplitOutput splitoutput = new SplitOutput();

	String newoutput, output;
	String[] strArrayResponse;
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		username = (EditText) findViewById(R.id.et_un);
		password = (EditText) findViewById(R.id.et_pw);
		post = (Button) findViewById(R.id.btn_login);
		response1 = (TextView) findViewById(R.id.tv_response);
		response2 = (TextView) findViewById(R.id.tv_response1);
		tv_user = (TextView) findViewById(R.id.tv_username);
		tv_pass = (TextView) findViewById(R.id.tv_password);
		ok = (Button)findViewById(R.id.btn_login);
	    ok.setOnClickListener(this);
	
		Helpz myHelp = new Helpz();
	 Boolean booltest = myHelp.GetLogoutVariable();

	   if(booltest == false)
	    
	
		{  
			this.username.setVisibility(View.VISIBLE);
			this.password.setVisibility(View.VISIBLE);
			this.tv_pass.setVisibility(View.VISIBLE);
			this.tv_user.setVisibility(View.VISIBLE);
			this.post.setVisibility(View.VISIBLE);
			this.response1.setVisibility(View.VISIBLE);
			this.response2.setVisibility(View.VISIBLE);

	    }
	  
	    else if( booltest == true)
	    {
	    	this.username.setVisibility(View.INVISIBLE);
			this.password.setVisibility(View.INVISIBLE);
			this.tv_pass.setVisibility(View.INVISIBLE);
			this.tv_user.setVisibility(View.INVISIBLE);
			this.post.setVisibility(View.INVISIBLE);
			this.response1.setVisibility(View.INVISIBLE);
			this.response2.setVisibility(View.INVISIBLE);
			myintent = new Intent(LoginActivity.this,LoginActivity.class);
			startActivity(myintent);
			
	    }
		
	}

	private class GetLoginTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			// SharedPreferences appSettings =
			// getSharedPreferences(PREFERENCE_FILENAME, MODE_PRIVATE);

			// postLoginData();

			return responseBody;
		}
		
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

		}
		catch (Exception ex) {
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
	
	public void postLoginDataPBX() {
		
		

		try {
			
			myHelpez.SetMyLoginMobileNumber(username.getText().toString());

		}
		catch (Exception ex) {
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
			String[] strArrayResponse = Split(newoutput,"~");
			String[] abc = strArrayResponse;
			int i = Integer.parseInt(strArrayResponse[0].toString());
			
			switch (Integer.parseInt(strArrayResponse[0].toString())) {
			case 1:
				
				Toast.makeText(LoginActivity.this, strArrayResponse[0].toString(), Toast.LENGTH_LONG).show();
				response1.setVisibility(View.VISIBLE);
				response1.setText(strArrayResponse[1].toString());
				myintent = new Intent(LoginActivity.this,MainActivity1.class);
				startActivity(myintent);
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
										"category"));

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
		
		
					myintent = new Intent(LoginActivity.this,MainActivity1.class);
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
										"category"));

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
					
					Toast.makeText(LoginActivity.this, strArrayResponse[0].toString(), Toast.LENGTH_LONG).show();
					response1.setVisibility(View.VISIBLE);
					response1.setText(strArrayResponse[1].toString());
					myintent = new Intent(LoginActivity.this,MainActivity1.class);
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
							+ username + "&CompanyID=184");

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
		 if(username.getText().toString().trim().equals("")&& ((password.getText().toString().equals("")))){
			response1.setText("Missing Params");
			return false;
		 }
		if(username.getText().toString().trim().equals("")){
			response1.setText("Please enter MobileNumber");
			return false;
		}else if(password.getText().toString().equals("")){
			response1.setText("Please enter Password");
			return false;
		}else{
			nameValuePairs.add(new BasicNameValuePair("user", username.getText().toString()));
			nameValuePairs.add(new BasicNameValuePair("pass", password.getText().toString()));
		   return true;
		}
				
		}

	// @Override

	
	  public void onClick (View view) {
		  if(this.fillPostData()){
		  ok.setEnabled(false);
		  postLoginData();
		
	  }
	  }
	  
			
		}

