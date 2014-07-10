package com.mom.app;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
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

import com.mom.app.activity.LoginActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class ChangePasswordPBXActivity extends Activity {
	private String responseBody;
	String responseBodyhistory;
	private TextView accntbalresponse;
	String output, check;
	private String session_id;
	WebView wv;
	ListView listview;
	String flag;
	String[] pairs;
	ImageButton refresh;
	ImageButton number;
	TextView responseText, responseText1;
	private Button submitButton;
	private Button backButton;
	private EditText Confirm_Password;
	private EditText Old_Password;
	private EditText New_Password;
	private Button postButton;

	private EditText numField;
	Intent myintent = new Intent();
	Intent myintenttest = new Intent();

	String response_status;
	Helpz myHelpz = new Helpz();
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_changepassword);
		wv = (WebView) findViewById(R.id.resp);

		responseText = (TextView) findViewById(R.id.responseText);
		responseText1 = (TextView) findViewById(R.id.responseText1);

		accntbalresponse = (TextView) findViewById(R.id.AccountBal);

		this.Old_Password = (EditText) findViewById(R.id.OldPassword);
		Old_Password.setTypeface(Typeface.DEFAULT);
        Old_Password.setTransformationMethod(new PasswordTransformationMethod());
        
		this.New_Password = (EditText) findViewById(R.id.NewPassword);
		New_Password.setTypeface(Typeface.DEFAULT);
        New_Password.setTransformationMethod(new PasswordTransformationMethod());
        
		this.Confirm_Password = (EditText) findViewById(R.id.ConfirmPassword);
		Confirm_Password.setTypeface(Typeface.DEFAULT);
        Confirm_Password.setTransformationMethod(new PasswordTransformationMethod());
        
		this.backButton = (Button) findViewById(R.id.btn_back);

		this.submitButton = (Button) findViewById(R.id.btn_Submit);
		  getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.appsbg)); 
		submitButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int ValidationResult = validate();
				if (ValidationResult == 0) {
					rechargePost();
				}

				else {
					switch (ValidationResult) {

					case 1:
						responseText1.setVisibility(View.GONE);
						responseText.setVisibility(View.VISIBLE);
						responseText.setText("Enter Correct Old Password");
						Old_Password.setText("");
						New_Password.setText("");
						Confirm_Password.setText("");
						break;

					case 2:
						responseText1.setVisibility(View.GONE);
						responseText.setVisibility(View.VISIBLE);
						responseText.setText("The new Password cannot be empty");
						New_Password.setText("");
						Confirm_Password.setText("");

						break;

					

					case 4:
						responseText1.setVisibility(View.GONE);
						responseText.setVisibility(View.VISIBLE);
						responseText.setText("New Password and Confirm Password doesnot match");
						New_Password.setText("");
						Confirm_Password.setText("");
						break;
						
						
                    case 5:
                    	responseText1.setVisibility(View.GONE);
						responseText.setVisibility(View.VISIBLE);
						responseText.setText("Old Password and New Password cannot be same");
						Old_Password.setText("");
						New_Password.setText("");
						Confirm_Password.setText("");
						break;

					}

				}


			}

		});

		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				myintenttest = new Intent(ChangePasswordPBXActivity.this, InfoActivity.class);
				myintenttest.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(myintenttest);
				finish();
				
			}
		});

		AccountBalPost();
		// this.posting();
		myintent = new Intent(ChangePasswordPBXActivity.this, LoginActivity.class);

	}

	private void AccountBalPost() {

		
		
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://180.179.67.76/MobAppS/PbxMobApp.ashx");
//			try {
//
//				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
//				nameValuePairs.add(new BasicNameValuePair("RN", myHelpz
//						.GetMyLoginMobileNumber()));
//				nameValuePairs.add(new BasicNameValuePair("Service", "BL"));
//
//				httppost.addHeader("ua", "android");
//				final HttpParams httpParams = httpclient.getParams();
//				HttpConnectionParams.setConnectionTimeout(httpParams, 15000);
//				HttpConnectionParams.setSoTimeout(httpParams, 15000);
//				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//				HttpResponse response = httpclient.execute(httppost);
//				HttpEntity entity = response.getEntity();
//				responseBody = EntityUtils.toString(entity);
//				check = responseBody;
//				
//				Log.i("postData", response.getStatusLine().toString());
//				Log.i("info", responseBody);
//
//				accntbalresponse.setVisibility(View.VISIBLE);
//				accntbalresponse.setText("Bal: Rs." + check);
//
//			} catch (Exception e) {
//				Log.e("log_tag", "Error in http connection " + e.toString());
//				responseBody = "Timeout|Error in Http Connection";
//				// error="1";
//			}
			accntbalresponse.setVisibility(View.VISIBLE);
			accntbalresponse.setText("Bal: Rs." + myHelpz.GetRMNAccountBal().toString());
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
				Log.i(TAG,
						"Attribute Name"
								+ xmlpullparser1.getAttributeValue(null,
										"category"));

				break;

			case XmlPullParser.END_TAG:
				Log.i(TAG, "END_TAG" + xmlpullparser1.getName());

				break;

			case XmlPullParser.TEXT:
				Log.i(TAG, "TEXT");
				output = xmlpullparser1.getText();
				String newoutputrecharge = output;

				// //////// Toast.makeText(InfoActivity.this, newoutputrecharge,
				// Toast.LENGTH_LONG).show();

				accntbalresponse.setVisibility(View.VISIBLE);
				accntbalresponse.setText("Bal: Rs. " + newoutputrecharge);
				// response.setText("Bal: Rs. 100000000");

				break;

			}

		}

	}

	public void posting() {

		// rechargePost();

	}

	private void rechargePost() {

		Helpz myHelpez = new Helpz();
		
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://180.179.67.76/MobAppS/PbxMobApp.ashx");
			try {

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
				
				nameValuePairs.add(new BasicNameValuePair("OP",
						Old_Password.getText().toString()));
				nameValuePairs.add(new BasicNameValuePair("NP",
						New_Password.getText().toString()));
				nameValuePairs.add(new BasicNameValuePair("Service", "CP"));
				nameValuePairs.add(new BasicNameValuePair("RN",myHelpez.GetMyLoginMobileNumber()));

				final HttpParams httpParams = httpclient.getParams();
				HttpConnectionParams.setConnectionTimeout(httpParams, 15000);
				HttpConnectionParams.setSoTimeout(httpParams, 15000);
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				responseBody = EntityUtils.toString(entity);
				check = responseBody;
				if(check.equals("0")){
					responseText1.setVisibility(View.VISIBLE);
					responseText1.setText("Old Password is Incorrect");
					responseText.setVisibility(View.GONE);
					Old_Password.setText("");
					New_Password.setText("");
					Confirm_Password.setText("");	
				}
				else if(check.equals("1")){
					responseText1.setVisibility(View.VISIBLE);
					responseText1.setText("Password changed Successfully");
					responseText.setVisibility(View.GONE);
					Old_Password.setText("");
					New_Password.setText("");
					Confirm_Password.setText("");	
				}
				else{
					
				}
				Log.i("postData", response.getStatusLine().toString());
				Log.i("info", responseBody);

				

			} catch (Exception e) {
				Log.e("log_tag", "Error in http connection " + e.toString());
				responseBody = "Timeout|Error in Http Connection";
				// error="1";
			}
		

	}

	public class NewXmlPullParsing {

		protected XmlPullParser xmlpullparser;
		String outputrecharge;
		String TAG = "XmlPullParsing";

		public NewXmlPullParsing(InputStream is) {

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
				outputrecharge = xmlpullparser.getText();
				String newoutputrecharge = outputrecharge;
				String[] strArrayResponse = Split(newoutputrecharge, "~");
				int i = Integer.parseInt(strArrayResponse[0].toString());

			  
				responseText1.setVisibility(View.VISIBLE);
				responseText1.setText(strArrayResponse[1].toString());
				responseText.setVisibility(View.GONE);
				Old_Password.setText("");
				New_Password.setText("");
				Confirm_Password.setText("");
			//	myintenttest.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				break;

			}

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

	private int validate() {


		if (Old_Password.getText().length() >=1) {
			if (New_Password.getText().length() >=1) {
				
					
					if (New_Password.getText().toString().equals(Confirm_Password.getText().toString()))
					{
						if(!Old_Password.getText().toString().equals(New_Password.getText().toString()))
						{
						return 0;
					}
						else
							return 5;
					}
					else
						return 4;
				
			} else
				return 2;
		} else
			return 1;
	}

//	@Override
//	public void onUserLeaveHint() {
//		this.finish();
//
//	}

	public void onBackPressed() {
		myintenttest = new Intent(ChangePasswordPBXActivity.this, InfoActivity.class);
		myintenttest.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(myintenttest);
		this.finish();
		return;
	}

}
