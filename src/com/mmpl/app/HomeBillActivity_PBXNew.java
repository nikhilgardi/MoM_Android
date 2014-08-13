package com.mmpl.app;

import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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

import com.mmpl.app.R;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class HomeBillActivity_PBXNew extends Activity implements OnClickListener {
	// private Button reButton;
	private Button passButton;
	private EditText passField;
	private EditText numField_bill, first_name, last_name, mobile_number,
			accountnumber;
	private EditText amountField;
	private Button postButtonpayment, testButton, submitbutton, backButton,
			secondback, backButton1, lastbackButton, lastpaymentButton;
	private Button newButton;
	private Button firstbackButton;
	private Button GetBillAmount;
	private EditText consumernumber;
	private String session_id, flag;
	TableLayout tablelayout;
	ProgressDialog dialog;
	TextView tv_secpass, accountbal;
	ImageView image;
	Spinner operatorSpinner, operatorSpinner1, operatorSpinner2,
			spl_OperatorSBE, spl_OperatorNBE;
	private Spinner spinner1;
	CheckBox responseCheckbox, respcheckbox;
	String[] pairs;
	HashMap<String, String> responseMap = new HashMap<String, String>();
	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	String responseBody;
	String response_status;
	String responseBodyhistory;
	String check;
	TextView responseText, responseText1, secondback_responseText,
			third_responseText, last_responseText, ResultResponse_responseText;
	boolean checkbox;
	String Topup = "";
	private String response_message;
	String newoutput, output;
	String newoutputrecharge;
	String[] strArrayResponse;
	Intent reintent = new Intent();
	int i;
	Intent myintent3 = new Intent();
	Helpz myHelpez = new Helpz();
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// //setContentView(R.layout.recharge);
		setContentView(R.layout.activity_home_bill_activity__pbx);
		// SharedPreferences appSettings =
		// getSharedPreferences(LoginActivity.PREFERENCE_FILENAME,MODE_PRIVATE);

		init();

		postButtonpayment.setOnClickListener(this);

		addListenerOnSpinnerItemSelection();
		addListenerOnSpinnerItemSelectionSBE();
		addListenerOnSpinnerItemSelectionNBE();
		myintent3 = new Intent(HomeBillActivity_PBXNew.this, LoginActivity.class);
		reintent = new Intent(HomeBillActivity_PBXNew.this, MainActivity1.class);

	}

	
	private void init() {
		
		this.responseText = (TextView) findViewById(R.id.responseText);
		this.responseText1 = (TextView) findViewById(R.id.responseText1);
		this.secondback_responseText = (TextView) findViewById(R.id.secondresponseText);
		this.third_responseText = (TextView) findViewById(R.id.third_responseText);
		this.passButton = (Button) findViewById(R.id.btn_reclogin);
		this.newButton = (Button) findViewById(R.id.new_recharge);
		this.passField = (EditText) findViewById(R.id.repass);
		this.numField_bill = (EditText) findViewById(R.id.number);
		this.postButtonpayment = (Button) findViewById(R.id.btn_recharge);
		this.firstbackButton = (Button) findViewById(R.id.btn_firstback);
		this.testButton = (Button) findViewById(R.id.btn_check);
		this.operatorSpinner = (Spinner) findViewById(R.id.Operator);
		this.consumernumber = (EditText) findViewById(R.id.consumernumber);
		this.GetBillAmount = (Button) findViewById(R.id.btn_GetBillAmount);
		this.accountbal = (TextView) findViewById(R.id.AccountBal);
		this.amountField = (EditText) findViewById(R.id.amount);
		this.spl_OperatorSBE = (Spinner) findViewById(R.id.Spl_OperatorSBE);
		this.spl_OperatorNBE = (Spinner) findViewById(R.id.Spl_OperatorNBE);
		this.first_name = (EditText) findViewById(R.id.first_name);
		this.last_name = (EditText) findViewById(R.id.last_name);
		this.mobile_number = (EditText) findViewById(R.id.mobile_number);
		this.accountnumber = (EditText) findViewById(R.id.spl_accountnumber);
		// this.submitbutton = (Button)findViewById(R.id.btn_splreclogin);
		// this.backButton = (Button)findViewById(R.id.btn_splback);
		this.secondback = (Button) findViewById(R.id.btn_secondback);
		this.backButton1 = (Button) findViewById(R.id.btn_back1);
		this.lastbackButton = (Button) findViewById(R.id.btn_splback);
		this.lastpaymentButton = (Button) findViewById(R.id.btn_splreclogin);
		// this.consumernumber = (EditText)findViewById(R.id.consumernumber);
		this.accountbal = (TextView) findViewById(R.id.AccountBal);
		this.tablelayout = (TableLayout) findViewById(R.id.tableLayout1);

		this.image = (ImageView) findViewById(R.id.img_resp);
		this.last_responseText = (TextView) findViewById(R.id.last_responseText);
		this.ResultResponse_responseText = (TextView) findViewById(R.id.ResultResponse_responseText);

		AccountBalPost();
		
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		if(pref.getString("user_sessionMOM", "test").equals("MOM"))
			
		{
			passButton.setVisibility(View.VISIBLE);
			
			passField.setVisibility(View.VISIBLE);
			firstbackButton.setVisibility(View.VISIBLE);
		}
		else if(pref.getString("user_sessionMOM", "test").equals("PBX"))
		{
           rechargecheck();
		}
		
				
		String[] strOperators = getAllOperators();
		/*
		 * String[] actions = new String[] { "Bookmark", "Subscribe", "Share" };
		 */

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, strOperators);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		operatorSpinner.setAdapter(dataAdapter);

	}

	private void AccountBalPost() {

		Helpz myHelpz = new Helpz();

		
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://180.179.67.76/MobAppS/PbxMobApp.ashx");
			try {
				
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
				nameValuePairs.add(new BasicNameValuePair("RN", myHelpz.GetMyLoginMobileNumber()));
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
//				Double number = Double.valueOf(check);
//				DecimalFormat df = new DecimalFormat("#.00");
//				String newtestString = df.format(number);
//
//				String abc = newtestString;
				// error="0";
				Log.i("postData", response.getStatusLine().toString());
				Log.i("info", responseBody);

				accountbal.setVisibility(View.VISIBLE);
				accountbal.setText("Bal: Rs." + check);

			} catch (Exception e) {
				Log.e("log_tag", "Error in http connection " + e.toString());
				responseBody = "Timeout|Error in Http Connection";
				// error="1";
			}	
	}

	public class XmlPullParsingAccnt {

		protected XmlPullParser xmlpullparser1;
		String output1;
		String TAG = "XmlPullParsing";

		public XmlPullParsingAccnt(InputStream is) {

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

				accountbal.setVisibility(View.VISIBLE);
				accountbal.setText("Bal: Rs." + newoutputrecharge);

				break;

			}

		}

	}

	public void addListenerOnSpinnerItemSelection() {

		operatorSpinner
				.setOnItemSelectedListener(new CustomOnItemSelectedListener());
	}

	public void addListenerOnSpinnerItemSelectionNBE() {

		spl_OperatorNBE
				.setOnItemSelectedListener(new CustomOnItemSelectedListenerNBE());
	}

	public void addListenerOnSpinnerItemSelectionSBE() {

		spl_OperatorSBE
				.setOnItemSelectedListener(new CustomOnItemSelectedListenerSBE());
	}

	public String[] getAllOperators() {
		String[] strResponse1 = null;
		

		
			 HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost("http://180.179.67.76/MobAppS/PbxMobApp.ashx");
			try {

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
				nameValuePairs.add(new BasicNameValuePair("OT","6"));
				nameValuePairs.add(new BasicNameValuePair("Service","ON"));
				
				
				
				httppost.addHeader("ua", "android");
				final HttpParams httpParams = httpclient.getParams();
				HttpConnectionParams.setConnectionTimeout(httpParams, 15000);
				HttpConnectionParams.setSoTimeout(httpParams, 15000);
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				responseBody = EntityUtils.toString(entity);
				String strOperators = responseBody;
				Log.i("postData", response.getStatusLine().toString());
				Log.i("postData", this.responseBody);
				String[] strArrOperators = Split(strOperators, "|");

				strResponse1 = strArrOperators;

			} catch (Exception ex) {
				String[] strResponse = { "NO ITEM TO DISPLAY" };
				strResponse1 = strResponse;
			}
		
		

		return strResponse1;
	}

	private String Get_OperatorID(String strOperatorName) {

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://180.179.67.76/MobAppS/PbxMobApp.ashx");
			try {
				
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
				nameValuePairs.add(new BasicNameValuePair("SN", strOperatorName.toString()));
				nameValuePairs.add(new BasicNameValuePair("Service", "OSN"));
				nameValuePairs.add(new BasicNameValuePair("OT", "6"));
				
				
				httppost.addHeader("ua", "android");
				final HttpParams httpParams = httpclient.getParams();
				HttpConnectionParams.setConnectionTimeout(httpParams, 15000);
				HttpConnectionParams.setSoTimeout(httpParams, 15000);
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				responseBody = EntityUtils.toString(entity);
				check = responseBody;
				  return check;
		}
			catch (Exception ex) {
				return "0";
			}
				
		
	}

	public void rechargeLogin(View v) {
		if (this.fillData()) {
			// flag="1";
			// this.dialog = new ProgressDialog(this);
			// this.dialog.setMessage("Loading..");
			// this.dialog.show();
			// this.dialog.setCancelable(false);

			// // GetRechargeTask task1 = new GetRechargeTask();
			// // task1.execute();

			sendPost();

			// this.sendPost();
			// this.parseResponse();
			// this.setResponse();
			// /rechargecheck();
		}
	}

	private int validate() {


		try {
			myHelpez.SetMyRechargeOperator(operatorSpinner.getSelectedItem()
					.toString());
		} catch (Exception ex) {

		}

		String Operatorid = (Get_OperatorID(myHelpez.GetMyRechargeOperator()
				.toString()));
		// if((operatorSpinner.getSelectedItemPosition() ==
		// 8)||(operatorSpinner.getSelectedItemPosition() ==12)){
		if ((Operatorid.equals("NBE")) || (Operatorid.equals("SBE"))) {

			if (operatorSpinner.getSelectedItemPosition() < 1) {
				return 1;
			} else if (consumernumber.getText().toString().length() == 0) {
				return 4;
			} else if (amountField.getText().toString().length() == 0) {
				return 3;
			} else if (Integer.parseInt(amountField.getText().toString()) < 10) {
				return 3;
			} else if (Integer.parseInt(amountField.getText().toString()) > 10000) {
				return 3;
			}

			else {
				return 0;
			}
		}

		// else if((operatorSpinner.getSelectedItemPosition() ==
		// 3)||(operatorSpinner.getSelectedItemPosition() ==11))
		else if ((Operatorid.equals("REM")) || (Operatorid.equals("BES"))
				|| (Operatorid.equals("MGB"))) {
			if (operatorSpinner.getSelectedItemPosition() < 1) {
				return 5;
			} else if (consumernumber.getText().toString().length() == 0) {
				return 4;

			} else if (numField_bill.getText().toString().length() < 10) {
				return 2;
			} else if (amountField.getText().toString().length() == 0) {
				return 3;
			} else if (Integer.parseInt(amountField.getText().toString()) < 10) {
				return 3;
			} else if (Integer.parseInt(amountField.getText().toString()) > 10000) {
				return 3;
			}

			else {
				return 0;
			}
		} else {
			if (operatorSpinner.getSelectedItemPosition() < 1) {
				return 1;
			} else if (numField_bill.getText().toString().length() < 10) {
				return 2;
			} else if (amountField.getText().toString().length() == 0) {
				return 3;
			} else if (Integer.parseInt(amountField.getText().toString()) < 10) {
				return 3;
			} else if (Integer.parseInt(amountField.getText().toString()) > 10000) {
				return 3;
			}

			else {
				return 0;
			}
		}
	}

	private void rechargecheck() {
		// TODO Auto-generated method stub
		// if (this.response_status.equals("1")) {

		this.passButton.setVisibility(View.GONE);
		// this.tv_secpass.setVisibility(View.GONE);
		this.firstbackButton.setVisibility(View.GONE);
		this.passField.setVisibility(View.GONE);
		this.responseText.setVisibility(View.GONE);
		this.consumernumber.setVisibility(View.GONE);
		this.numField_bill.setVisibility(View.VISIBLE);
		this.postButtonpayment.setVisibility(View.VISIBLE);
		this.operatorSpinner.setVisibility(View.VISIBLE);
		this.secondback.setVisibility(View.VISIBLE);

		this.amountField.setVisibility(View.VISIBLE);

		// this.operatorSpinner1.setVisibility(View.VISIBLE);

		// }
		/*
		 * if(this.response_status.equals("0")){
		 * this.responseText.setVisibility(View.VISIBLE);
		 * responseText.setText("Incorrect Pass/Missing Params"); }
		 * if(this.response_status.equals("l")){ startActivity(myintent1); }
		 */
	}

	public void backpost(View v) {

		this.finish();

		startActivity(reintent);

	}

	public void Backpost1(View v) {

		this.finish();

		startActivity(reintent);

	}

	public void backpost1(View v) {

		this.finish();

		startActivity(reintent);

	}

	public void backpost2(View v) {

		this.finish();

		startActivity(reintent);

	}

	private void newrechargecheck() {

		this.numField_bill.setVisibility(View.GONE);
		this.postButtonpayment.setVisibility(View.GONE);
		this.operatorSpinner.setVisibility(View.GONE);
		this.amountField.setVisibility(View.GONE);

		this.image.setVisibility(View.VISIBLE);
		this.responseText.setVisibility(View.VISIBLE);
		this.newButton.setVisibility(View.VISIBLE);
	
		this.responseText.setText(this.newoutputrecharge);
		// /////// Toast.makeText(HomeActivity2.this, this.newoutputrecharge,
		// Toast.LENGTH_SHORT).show();

	}

	/*
	 * public void rechargeData(View v) { //flag="0"; this.dialog = new
	 * ProgressDialog(this); this.dialog.setMessage("Connecting..");
	 * this.dialog.show(); this.dialog.setCancelable(false); GetRechargeTask
	 * task = new GetRechargeTask(); task.execute(); }
	 */
	public void testData(View view1) {
		
		try {
			//myHelpez.SetMyRechargeOperator(operatorSpinner.getSelectedItem().toString());
		    myHelpez.SetConsumerNumber(consumernumber.getText().toString());
			myHelpez.SetMyRechargeAmount(amountField.getText().toString());
			//myHelpez.SetMyRechargeMobileNumber(numField_bill.getText().toString());

		} catch (Exception ex) {
		}
		String Operatorid = (Get_OperatorID(myHelpez.GetMyRechargeOperator()
				.toString()));

		if ((validate() == 0) && (Operatorid.equals("NBE"))) {
		
			
			spl_OperatorNBE.setVisibility(View.VISIBLE);
			first_name.setVisibility(View.VISIBLE);
			last_name.setVisibility(View.VISIBLE);
			mobile_number.setVisibility(View.VISIBLE);
			accountnumber.setVisibility(View.VISIBLE);
			lastbackButton.setVisibility(View.VISIBLE);
			lastpaymentButton.setVisibility(View.VISIBLE);
			secondback_responseText.setVisibility(View.GONE);
			third_responseText.setVisibility(View.GONE);
			testButton.setVisibility(View.GONE);
			backButton1.setVisibility(View.GONE);
			consumernumber.setVisibility(View.GONE);
			numField_bill.setVisibility(View.GONE);
			postButtonpayment.setVisibility(View.GONE);
			operatorSpinner.setVisibility(View.GONE);
			amountField.setVisibility(View.GONE);
		} else if ((validate() == 0) && (Operatorid.equals("SBE"))) {
			
			spl_OperatorSBE.setVisibility(View.VISIBLE);
			first_name.setVisibility(View.VISIBLE);
			last_name.setVisibility(View.VISIBLE);
			mobile_number.setVisibility(View.VISIBLE);
			accountnumber.setVisibility(View.VISIBLE);
			lastbackButton.setVisibility(View.VISIBLE);
			lastpaymentButton.setVisibility(View.VISIBLE);
			secondback_responseText.setVisibility(View.GONE);
			third_responseText.setVisibility(View.GONE);
			testButton.setVisibility(View.GONE);
			backButton1.setVisibility(View.GONE);
			consumernumber.setVisibility(View.GONE);
			numField_bill.setVisibility(View.GONE);
			postButtonpayment.setVisibility(View.GONE);
			operatorSpinner.setVisibility(View.GONE);
			amountField.setVisibility(View.GONE);

		} else {
			switch (validate()) {
			case 1:
				third_responseText.setVisibility(View.VISIBLE);
				third_responseText.setText("Select Service Provider");
				operatorSpinner.setSelection(0);
				consumernumber.setText("");
				amountField.setText("");

				break;
			case 4:
				third_responseText.setVisibility(View.VISIBLE);
				third_responseText.setText("Select Consumer Number");

				consumernumber.setText("");
				break;

			case 3:
				third_responseText.setVisibility(View.VISIBLE);
				third_responseText.setText("Invalid Amount");
				amountField.setText("");

				break;
			}
		}
	}

	public void newRecharge(View v) {
		this.resetLayout();
	}

	private void resetLayout() {
		AccountBalPost();
		tablelayout.setVisibility(View.VISIBLE);
		this.consumernumber.setText("");
		this.numField_bill.setText("");
		this.amountField.setText("");
		this.operatorSpinner.setSelection(0);
		this.spl_OperatorSBE.setSelection(0);
		this.spl_OperatorNBE.setSelection(0);
		this.first_name.setText("");
		this.last_name.setText("");
		this.mobile_number.setText("");
		this.accountnumber.setText("");
		this.numField_bill.setVisibility(View.VISIBLE);
		this.postButtonpayment.setVisibility(View.VISIBLE);
		this.operatorSpinner.setVisibility(View.VISIBLE);
		this.amountField.setVisibility(View.VISIBLE);
		this.secondback.setVisibility(View.VISIBLE);
		this.newButton.setVisibility(View.GONE);
		this.image.setVisibility(View.GONE);
		this.responseText.setVisibility(View.GONE);
		this.responseText1.setVisibility(View.GONE);
		this.ResultResponse_responseText.setVisibility(View.GONE);

	}

	private void rechargePost() {
		String[] strResponse1 = null;
		// Helpz myHelpez = new Helpz();
		try {
			myHelpez.SetMyRechargeOperator(operatorSpinner.getSelectedItem()
					.toString());
			myHelpez.SetConsumerName(first_name.getText().toString());
			myHelpez.SetMySpecialOperator(spl_OperatorSBE.getSelectedItem()
					.toString());
			myHelpez.SetMySpecialOperatorNBE(spl_OperatorNBE.getSelectedItem()
					.toString());
			
			myHelpez.SetMyREL_SBE_NBE(mobile_number.getText().toString());
			myHelpez.SetMyRechargeMobileNumber(numField_bill.getText().toString());
			
			String Operatorid = (Get_OperatorID(myHelpez.GetMyRechargeOperator()
					.toString()));
			
			if (!(Operatorid.equals("NBE") || Operatorid.equals("SBE")))
			{				
			myHelpez.SetMyRechargeAmount(amountField.getText().toString());
		    myHelpez.SetConsumerNumber(consumernumber.getText().toString());
			}
			

			if (responseCheckbox.isChecked() == true) {
				myHelpez.SetMyRechargeType("0");
			} else {
				myHelpez.SetMyRechargeType("0");
			}
		} catch (Exception ex) {
		}
SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		
		if(pref.getString("user_sessionMOM", "test").equals("MOM"))
			
			{
		try {

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://msvc.money-on-mobile.net/WebServiceV3Trans.asmx/doBillPayment");

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(6);
			String Operatorid = (Get_OperatorID(myHelpez
					.GetMyRechargeOperator().toString()));
			// if(operatorSpinner.getSelectedItemPosition()== 3)
			if (Operatorid.equals("BES"))

			{
				nameValuePairs.add(new BasicNameValuePair("strCustomerNumber",
						myHelpez.GetConsumerNumber() + "|"
								+ myHelpez.GetMyRechargeMobileNumber() + "|"
								+ myHelpez.GetMyUserId()));
			}

			// else if(operatorSpinner.getSelectedItemPosition()== 11)
			else if (Operatorid.equals("REM")) {
				nameValuePairs.add(new BasicNameValuePair("strCustomerNumber",
						myHelpez.GetConsumerNumber() + "|"
								+ myHelpez.GetMyRechargeMobileNumber() + "|"
								+ myHelpez.GetMyUserId()));
			}

			// else if(operatorSpinner.getSelectedItemPosition()== 12)
			else if (Operatorid.equals("SBE")) {
				nameValuePairs.add(new BasicNameValuePair("strCustomerNumber",
						"SBE" + "|" + myHelpez.GetConsumerNumber() + "|"
								+ myHelpez.GetConsumerName() + "|"
								+ myHelpez.GetMyREL_SBE_NBE() + "|"
								+ myHelpez.GetMySpecialOperator()));

			}
			// else if(operatorSpinner.getSelectedItemPosition()== 8)
			else if (Operatorid.equals("NBE")) {
				nameValuePairs.add(new BasicNameValuePair("strCustomerNumber",
						"NBE" + "|" + myHelpez.GetConsumerNumber() + "|"
								+ myHelpez.GetConsumerName() + "|"
								+ myHelpez.GetMyREL_SBE_NBE() + "|"
								+ myHelpez.GetMySpecialOperatorNBE()));

			} else if (Operatorid.equals("MGB")) {
				nameValuePairs.add(new BasicNameValuePair("strCustomerNumber",
						myHelpez.GetConsumerNumber() + "|"
								+ myHelpez.GetMyRechargeMobileNumber() + "|"
								+ myHelpez.GetMyUserId()));
			} else {
				nameValuePairs.add(new BasicNameValuePair("strCustomerNumber",
						myHelpez.GetMyRechargeMobileNumber()));

			}
			nameValuePairs
					.add(new BasicNameValuePair("intOperatorIDBillPay",
							Get_OperatorID(myHelpez.GetMyRechargeOperator())
									.toString()));
			nameValuePairs.add(new BasicNameValuePair("DecBillAmount", myHelpez
					.GetMyRechargeAmount()));
			nameValuePairs.add(new BasicNameValuePair("intCustomerId", myHelpez
					.GetMyCustomerId()));
			nameValuePairs.add(new BasicNameValuePair("strAccessID",
					GlobalVariables.AccessId));
			nameValuePairs.add(new BasicNameValuePair("companyId", myHelpez
					.GetMyCompanyId()));
			final HttpParams httpParams = httpclient.getParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, 180000);
			HttpConnectionParams.setSoTimeout(httpParams, 180000);

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request

			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			this.responseBody = EntityUtils.toString(entity);

			check = responseBody;
			Log.i("postData", response.getStatusLine().toString());
			Log.i("postData", this.responseBody);

			/*
			 * this.numField.setVisibility(View.GONE);
			 * this.postButton.setVisibility(View.GONE);
			 * this.operatorSpinner.setVisibility(View.GONE);
			 * this.amountField.setVisibility(View.GONE);
			 * this.responseCheckbox.setVisibility(View.GONE);
			 * this.image.setVisibility(View.VISIBLE);
			 * this.responseText.setVisibility(View.VISIBLE);
			 * this.newButton.setVisibility(View.VISIBLE);
			 * image.setImageResource(R.drawable.success);
			 * this.responseText.setText(check);
			 * Toast.makeText(HomeActivity.this, check,
			 * Toast.LENGTH_SHORT).show();
			 */

			InputStream in = new ByteArrayInputStream(
					this.responseBody.getBytes("UTF-8"));
			new NewXmlPullParsing(in);

			/*
			 * this.numField.setVisibility(View.GONE);
			 * this.postButton.setVisibility(View.GONE);
			 * this.operatorSpinner.setVisibility(View.GONE);
			 * this.amountField.setVisibility(View.GONE);
			 * this.responseCheckbox.setVisibility(View.GONE);
			 * this.image.setVisibility(View.VISIBLE);
			 * this.responseText.setVisibility(View.VISIBLE);
			 * this.newButton.setVisibility(View.VISIBLE);
			 * image.setImageResource(R.drawable.success);
			 * this.responseText.setText(check);
			 * Toast.makeText(HomeActivity.this, check,
			 * Toast.LENGTH_SHORT).show();
			 */

		} catch (Exception ex) {
		}
			}
		else if(pref.getString("user_sessionMOM", "test").equals("PBX"))
					
				{
					HttpClient httpclient = new DefaultHttpClient();
					HttpPost httppost = new HttpPost(
							"http://180.179.67.76/MobAppS/PbxMobApp.ashx");
					try {
					 	 
						List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
						nameValuePairs.add(new BasicNameValuePair("CustMobile",myHelpez.GetMyRechargeMobileNumber()));
						nameValuePairs.add(new BasicNameValuePair("Amount",myHelpez.GetMyRechargeAmount()));
						nameValuePairs.add(new BasicNameValuePair("OP", Get_OperatorID(myHelpez.GetMyRechargeOperator()).toString()));
						nameValuePairs.add(new BasicNameValuePair("RN", myHelpez.GetMyLoginMobileNumber()));
						nameValuePairs.add(new BasicNameValuePair("Service", "RM"));
						
						final HttpParams httpParams = httpclient.getParams();
						HttpConnectionParams.setConnectionTimeout(httpParams, 15000);
						HttpConnectionParams.setSoTimeout(httpParams, 15000);
						httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
						HttpResponse response = httpclient.execute(httppost);
						HttpEntity entity = response.getEntity();
						responseBody = EntityUtils.toString(entity);
						 check = responseBody;
						 if (check.contains("~")) {
								strResponse1 = Split(check, "~");

								StringBuilder sb = new StringBuilder();

								sb.append("StatusCode : " + strResponse1[0].toString()
										+ "\n" + "TransId : " + strResponse1[2].toString()
										+ "\n" + "Message:" + strResponse1[1].toString());
								sb.append("\n");
								sb.append("\n");

								String a = sb.toString();
						    numField_bill.setVisibility(View.GONE);
							consumernumber.setVisibility(View.GONE);
							postButtonpayment.setVisibility(View.GONE);
							operatorSpinner.setVisibility(View.GONE);
							amountField.setVisibility(View.GONE);
							secondback.setVisibility(View.GONE);
							image.setVisibility(View.INVISIBLE);
							tablelayout.setVisibility(View.VISIBLE);
							GetBillAmount.setVisibility(View.GONE);

							spl_OperatorSBE.setVisibility(View.GONE);
							spl_OperatorNBE.setVisibility(View.GONE);
							first_name.setVisibility(View.GONE);
							last_name.setVisibility(View.GONE);
							mobile_number.setVisibility(View.GONE);
							accountnumber.setVisibility(View.GONE);
							lastbackButton.setVisibility(View.GONE);
							lastpaymentButton.setVisibility(View.GONE);
							last_responseText.setVisibility(View.GONE);

						
							responseText1.setVisibility(View.VISIBLE);
							newButton.setVisibility(View.VISIBLE);
						
							responseText1.setText(a);
				    	  

						Log.i("postDatatest", response.getStatusLine().toString());
						Log.i("infotest", responseBody);
						 }
						 
						 else{
							    numField_bill.setVisibility(View.GONE);
								consumernumber.setVisibility(View.GONE);
								postButtonpayment.setVisibility(View.GONE);
								operatorSpinner.setVisibility(View.GONE);
								amountField.setVisibility(View.GONE);
								secondback.setVisibility(View.GONE);
								image.setVisibility(View.INVISIBLE);
								tablelayout.setVisibility(View.VISIBLE);
								GetBillAmount.setVisibility(View.GONE);

								spl_OperatorSBE.setVisibility(View.GONE);
								spl_OperatorNBE.setVisibility(View.GONE);
								first_name.setVisibility(View.GONE);
								last_name.setVisibility(View.GONE);
								mobile_number.setVisibility(View.GONE);
								accountnumber.setVisibility(View.GONE);
								lastbackButton.setVisibility(View.GONE);
								lastpaymentButton.setVisibility(View.GONE);
								last_responseText.setVisibility(View.GONE);

								
								responseText1.setVisibility(View.VISIBLE);
								newButton.setVisibility(View.VISIBLE);
								
								responseText1.setText(check);
						 }
						 
					//	InputStream in = new ByteArrayInputStream(responseBody.getBytes("UTF-8"));
					//    new NewXmlPullParsing(in);	
					 
					} catch (Exception e) {
						Log.e("log_tag", "Error in http connection " + e.toString());
						responseBody= "Timeout|Error in Http Connection";

					}
				}
				else{
					Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
				}
	}

	static public String urlEncode(String sUrl) {
		int i = 0;
		String urlOK = "";
		while (i < sUrl.length()) {
			if (sUrl.charAt(i) == '<') {
				urlOK = urlOK + "%3C";
			} else if (sUrl.charAt(i) == '/') {
				urlOK = urlOK + "%2F";
			} else if (sUrl.charAt(i) == '>') {
				urlOK = urlOK + "%3E";
			} else if (sUrl.charAt(i) == ' ') {
				urlOK = urlOK + "%20";
			} else if (sUrl.charAt(i) == ':') {
				urlOK = urlOK + "%3A";
			} else if (sUrl.charAt(i) == '-') {
				urlOK = urlOK + "%2D";
			} else {
				urlOK = urlOK + sUrl.charAt(i);
			}
			i++;
		}
		return (urlOK);
	}

	private void fillParams() {
		// TODO Auto-generated method stub
		this.checkbox = responseCheckbox.isChecked();
		if (checkbox == true) {
			Topup = "on";
		} else {
			Topup = "null";
		}
		// nameValuePairs.add(new BasicNameValuePair("session_id",
		// this.session_id));
		nameValuePairs.add(new BasicNameValuePair("customer_num",
				((EditText) findViewById(R.id.number)).getText().toString()));
		nameValuePairs.add(new BasicNameValuePair("amount", amountField
				.getText().toString()));
		nameValuePairs.add(new BasicNameValuePair("operator", operatorSpinner
				.getSelectedItem().toString()));
		// nameValuePairs.add(new BasicNameValuePair("operatorDTH",
		// operatorSpinner1.getSelectedItem().toString()));
		nameValuePairs.add(new BasicNameValuePair("topup", this.Topup));

	}

	private void sendPost() {

		Helpz myHelpz = new Helpz();
		try {

			HttpClient httpclient = new DefaultHttpClient();
			// HttpPost httppost = new
			// HttpPost("http://msvc.money-on-mobile.net/WebServiceV3Client.asmx/checkVallidTpin?CustomerID="
			// + myHelpz.GetMyCustomerId() + "&strTPassword=" +
			// passField.getText() + "&CompanyID=" + myHelpz.GetMyCompanyId() +
			// "&strAccessID=" + GlobalVariables.AccessId.toString());

			HttpPost httppost = new HttpPost(
					"http://msvc.money-on-mobile.net/WebServiceV3Client.asmx/checkVallidTpin");

			// Add password

			EditText pword = (EditText) findViewById(R.id.repass);
			String password = pword.getText().toString();

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
			nameValuePairs.add(new BasicNameValuePair("CustomerID", myHelpz
					.GetMyCustomerId()));
			nameValuePairs
					.add(new BasicNameValuePair("strTPassword", password));
			nameValuePairs.add(new BasicNameValuePair("CompanyID", myHelpz
					.GetMyCompanyId()));
			nameValuePairs.add(new BasicNameValuePair("strAccessId",
					GlobalVariables.AccessId.toString()));

			httppost.addHeader("ua", "android");
			final HttpParams httpParams = httpclient.getParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, 15000);
			HttpConnectionParams.setSoTimeout(httpParams, 15000);
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			this.responseBody = EntityUtils.toString(entity);
			String check = responseBody;
			String newcheck = check;
			Log.i("postData", response.getStatusLine().toString());

			Log.i("history", this.responseBody);
			InputStream in = new ByteArrayInputStream(
					this.responseBody.getBytes("UTF-8"));

			new XmlPullParsing(in);

		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection " + e.toString());
			this.responseBody = "null";
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
				String newoutput = output;
				String[] strArrayResponse = Split(newoutput, "~");
				int i = Integer.parseInt(strArrayResponse[0].toString());
				switch (Integer.parseInt(strArrayResponse[0].toString())) {
				case 101:

					// Toast.makeText(MainActivity.this, "Sucessful Login",
					// Toast.LENGTH_SHORT).show();
					// /////// Toast.makeText(HomeActivity2.this,
					// strArrayResponse[1].toString(),
					// Toast.LENGTH_LONG).show();

					// startActivity(myintent1);
					rechargecheck();
					// login();

					// Log.i("valuee==============================",""+output);
					break;
				default:

					// ////// Toast.makeText(HomeActivity2.this,
					// strArrayResponse[1].toString(),
					// Toast.LENGTH_LONG).show();
					responseText.setVisibility(View.VISIBLE);
					responseText.setText(strArrayResponse[1].toString());
					passField.setText("");

					break;
				}

			}

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

				// /////// Toast.makeText(HomeActivity2.this, newoutputrecharge,
				// Toast.LENGTH_LONG).show();

				// newrechargecheck();
				numField_bill.setVisibility(View.GONE);
				consumernumber.setVisibility(View.GONE);
				postButtonpayment.setVisibility(View.GONE);
				operatorSpinner.setVisibility(View.GONE);
				amountField.setVisibility(View.GONE);
				secondback.setVisibility(View.GONE);
				image.setVisibility(View.INVISIBLE);
				tablelayout.setVisibility(View.VISIBLE);
				GetBillAmount.setVisibility(View.GONE);

				spl_OperatorSBE.setVisibility(View.GONE);
				spl_OperatorNBE.setVisibility(View.GONE);
				first_name.setVisibility(View.GONE);
				last_name.setVisibility(View.GONE);
				mobile_number.setVisibility(View.GONE);
				accountnumber.setVisibility(View.GONE);
				lastbackButton.setVisibility(View.GONE);
				lastpaymentButton.setVisibility(View.GONE);
				last_responseText.setVisibility(View.GONE);

				// ResultResponse_responseText.setVisibility(View.VISIBLE);
				responseText1.setVisibility(View.VISIBLE);
				newButton.setVisibility(View.VISIBLE);
				
				responseText1.setText(newoutputrecharge);
				// ResultResponse_responseText.setText(newoutputrecharge);

				break;

			}

		}

	}

	private boolean fillData() {
		// TODO Auto-generated method stub

		if (passField.getText().toString().trim().equals("")) {
			this.responseText.setVisibility(View.VISIBLE);
			responseText.setText("Please enter your Password");
			return false;
		} else {
			nameValuePairs.add(new BasicNameValuePair("recharge_key", passField
					.getText().toString()));
			// nameValuePairs.add(new BasicNameValuePair("session_id",
			// this.session_id));
			return true;
		}
	}

	private void parseResponse() {
		// TODO Auto-generated method stub
		this.pairs = this.responseBody.split("[|]");
		for (int i = 0; i < pairs.length; i++) {
			Log.i("postData", pairs[i]);
			String[] temp = pairs[i].split("=");
			if (temp.length == 2) {
				this.responseMap.put(temp[0].trim(), temp[1].trim());
			}
		}
	}

	private void setResponse() {
		// TODO Auto-generated method stub
		if (this.responseMap.containsKey("status")) {
			this.response_status = this.responseMap.get("status");
			Log.i("postData", this.response_status);
		} else
			this.response_status = "2";

		if (this.responseMap.containsKey("message"))
			this.response_message = this.responseMap.get("message");
		else
			this.response_message = "Error in logging";
	}

	@Override
	public void onUserLeaveHint() {
		this.finish();
	}

	@Override
	public void onBackPressed() {
		return;
	}

	@Override
	public void onClick(View view) {
		//Helpz myHelpez = new Helpz();
		/*try {
			myHelpez.SetMyRechargeOperator(operatorSpinner.getSelectedItem()
					.toString());
		} catch (Exception ex) {
		}*/
		String Operatorid = (Get_OperatorID(operatorSpinner.getSelectedItem().toString()));
		if ((validate() == 0) && (Operatorid.equals("REM"))) {

			secondback_responseText.setText(" ");

			AlertDialog.Builder alertDialog = new AlertDialog.Builder(
					HomeBillActivity_PBXNew.this);

			// Setting Dialog Title
			alertDialog.setTitle("Confirm Payment...");

			// Setting Dialog Message
			alertDialog.setMessage("ConsumerNumber:" + " "
					+ consumernumber.getText().toString() + "\n"
					+ "MobileNumber:" + " "
					+ numField_bill.getText().toString() + "\n" + "Operator:"
					+ " " + operatorSpinner.getSelectedItem().toString() + "\n"
					+ "Amount:" + " " + "Rs." + " "
					+ amountField.getText().toString());

			// Setting Icon to Dialog
			// // alertDialog.setIcon(R.drawable.delete);

			// Setting Positive "Yes" Button
			alertDialog.setPositiveButton("YES",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							((AlertDialog) dialog).getButton(
									AlertDialog.BUTTON1).setEnabled(false);
							rechargePost();

						}
					});

			// Setting Negative "NO" Button
			alertDialog.setNegativeButton("NO",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {

							dialog.cancel();

						}
					});

			// Showing Alert Message
			alertDialog.show();

			// TODO Auto-generated method stub

		} else if ((validate() == 0) && (Operatorid.equals("BES"))) {
			secondback_responseText.setText(" ");

			AlertDialog.Builder alertDialog = new AlertDialog.Builder(
					HomeBillActivity_PBXNew.this);

			// Setting Dialog Title
			alertDialog.setTitle("Confirm Payment1...");

			// Setting Dialog Message
			alertDialog.setMessage("ConsumerNumber:" + " "
					+ consumernumber.getText().toString() + "\n"
					+ "MobileNumber:" + " "
					+ numField_bill.getText().toString() + "\n" + "Operator:"
					+ " " + operatorSpinner.getSelectedItem().toString() + "\n"
					+ "Amount:" + " " + "Rs." + " "
					+ amountField.getText().toString());

			// Setting Icon to Dialog
			// // alertDialog.setIcon(R.drawable.delete);

			// Setting Positive "Yes" Button
			alertDialog.setPositiveButton("YES",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							((AlertDialog) dialog).getButton(
									AlertDialog.BUTTON1).setEnabled(false);
							rechargePost();

						}
					});

			// Setting Negative "NO" Button
			alertDialog.setNegativeButton("NO",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {

							dialog.cancel();

						}
					});

			// Showing Alert Message
			alertDialog.show();
		}

		else if ((validate() == 0) && (Operatorid.equals("MGB"))) {
			secondback_responseText.setText(" ");

			AlertDialog.Builder alertDialog = new AlertDialog.Builder(
					HomeBillActivity_PBXNew.this);

			// Setting Dialog Title
			alertDialog.setTitle("Confirm Payment2...");

			// Setting Dialog Message
			alertDialog.setMessage("ConsumerNumber:" + " "
					+ consumernumber.getText().toString() + "\n"
					+ "MobileNumber:" + " "
					+ numField_bill.getText().toString() + "\n" + "Operator:"
					+ " " + operatorSpinner.getSelectedItem().toString() + "\n"
					+ "Amount:" + " " + "Rs." + " "
					+ amountField.getText().toString());

			// Setting Icon to Dialog
			// // alertDialog.setIcon(R.drawable.delete);

			// Setting Positive "Yes" Button
			alertDialog.setPositiveButton("YES",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							((AlertDialog) dialog).getButton(
									AlertDialog.BUTTON1).setEnabled(false);
							rechargePost();

						}
					});

			// Setting Negative "NO" Button
			alertDialog.setNegativeButton("NO",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {

							dialog.cancel();

						}
					});

			// Showing Alert Message
			alertDialog.show();
		}

		else if (validate() == 0) {
			secondback_responseText.setText(" ");

			AlertDialog.Builder alertDialog = new AlertDialog.Builder(
					HomeBillActivity_PBXNew.this);

			// Setting Dialog Title
			alertDialog.setTitle("Confirm Payment...");

			// Setting Dialog Message
			alertDialog.setMessage("MobileNumber:" + " "
					+ numField_bill.getText().toString() + "\n" + "Operator:"
					+ " " + operatorSpinner.getSelectedItem().toString() + "\n"
					+ "Amount:" + " " + "Rs." + " "
					+ amountField.getText().toString());

			// Setting Icon to Dialog
			// // alertDialog.setIcon(R.drawable.delete);

			// Setting Positive "Yes" Button
			alertDialog.setPositiveButton("YES",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							((AlertDialog) dialog).getButton(
									AlertDialog.BUTTON1).setEnabled(false);
							rechargePost();

						}
					});

			// Setting Negative "NO" Button
			alertDialog.setNegativeButton("NO",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {

							dialog.cancel();

						}
					});

			// Showing Alert Message
			alertDialog.show();
		}

		else {
			switch (validate()) {
			case 1:
				secondback_responseText.setVisibility(View.VISIBLE);
				secondback_responseText.setText("Select Service Provider");
				operatorSpinner.setSelection(0);
				numField_bill.setText("");
				amountField.setText("");

				break;
			case 4:
				secondback_responseText.setVisibility(View.VISIBLE);
				secondback_responseText.setText("Select Consumer Number");

				consumernumber.setText("");
				break;
			case 2:
				secondback_responseText.setVisibility(View.VISIBLE);
				secondback_responseText.setText("Invalid MobileNumber");
				numField_bill.setText("");
				break;

			case 3:
				secondback_responseText.setVisibility(View.VISIBLE);
				secondback_responseText.setText("Invalid Amount");
				amountField.setText("");

				break;

			case 5:
				secondback_responseText.setVisibility(View.VISIBLE);
				secondback_responseText.setText("Select Service Provider");
				operatorSpinner.setSelection(0);
				consumernumber.setText("");
				numField_bill.setText("");
				amountField.setText("");
			}
		}
	}

	public class CustomOnItemSelectedListener implements OnItemSelectedListener {
		//Helpz myHelpez = new Helpz();

		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {

			try {
				myHelpez.SetMyRechargeOperator(operatorSpinner
						.getSelectedItem().toString());
			} catch (Exception ex) {

			}

			String Operatorid = (Get_OperatorID(myHelpez
					.GetMyRechargeOperator().toString()));
			// switch(Operatorid){
			if ((Operatorid.equals("NBE")) || (Operatorid.equals("SBE"))) {
				consumernumber.setText("");
				amountField.setText("");
				first_name.setText("");
				last_name.setText("");
				mobile_number.setText("");
				accountnumber.setText("");

				consumernumber.setVisibility(view.VISIBLE);
				testButton.setVisibility(view.VISIBLE);
				backButton1.setVisibility(view.VISIBLE);

				secondback_responseText.setVisibility(view.GONE);
				secondback.setVisibility(view.GONE);
				numField_bill.setVisibility(View.GONE);
				GetBillAmount.setVisibility(view.GONE);
				postButtonpayment.setVisibility(View.GONE);

			}

			else if ((Operatorid.equals("REM")) || (Operatorid.equals("BES"))
					|| (Operatorid.equals("MGB"))) {
				numField_bill.setText("");
				amountField.setText("");
				consumernumber.setText("");
				secondback_responseText.setText("");
				consumernumber.setVisibility(view.VISIBLE);
				GetBillAmount.setVisibility(view.VISIBLE);
				testButton.setVisibility(view.GONE);
				backButton1.setVisibility(view.GONE);
				numField_bill.setVisibility(View.VISIBLE);
				postButtonpayment.setVisibility(view.VISIBLE);
				operatorSpinner.setVisibility(View.VISIBLE);
				secondback.setVisibility(view.VISIBLE);
				amountField.setVisibility(View.VISIBLE);

			}

			else {
				consumernumber.setText("");
				numField_bill.setText("");
				amountField.setText("");
				secondback_responseText.setText("");
				consumernumber.setVisibility(view.GONE);
				testButton.setVisibility(view.GONE);
				backButton1.setVisibility(view.GONE);
				numField_bill.setVisibility(View.VISIBLE);
				GetBillAmount.setVisibility(view.GONE);
				postButtonpayment.setVisibility(view.VISIBLE);
				operatorSpinner.setVisibility(View.VISIBLE);
				secondback.setVisibility(view.VISIBLE);
				amountField.setVisibility(View.VISIBLE);

			}

		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}
	}

	public class CustomOnItemSelectedListenerNBE implements
			OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {

			consumernumber.setText("");
			amountField.setText("");
			first_name.setText("");
			last_name.setText("");
			mobile_number.setText("");
			accountnumber.setText("");

		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
		}
	}

	public class CustomOnItemSelectedListenerSBE implements
			OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {

			consumernumber.setText("");
			amountField.setText("");
			first_name.setText("");
			last_name.setText("");
			mobile_number.setText("");
			accountnumber.setText("");

		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
		}
	}

	public void SBE_NBERecharge(View view) {
		//Helpz myHelpez = new Helpz();
		/*try {
			myHelpez.SetMyRechargeOperator(operatorSpinner.getSelectedItem()
					.toString());

		} catch (Exception ex) {
		}*/
		String Operatorid = (Get_OperatorID(myHelpez.GetMyRechargeOperator()
				.toString()));

		if ((validatenew() == 0) && (Operatorid.equals("NBE"))) {
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(
					HomeBillActivity_PBXNew.this);
			alertDialog.setTitle("Confirm Payment...");
			alertDialog.setMessage("ConsumerNumber:" + " "
					+ myHelpez.GetConsumerNumber() + "\n" + "MobileNumber:"
					+ " " + mobile_number.getText().toString() + "\n"
					+ "Operator:" + " "
					+ spl_OperatorNBE.getSelectedItem().toString() + "\n"
					+ "Amount:" + " " + "Rs." + " "
					+ myHelpez.GetMyRechargeAmount());
			alertDialog.setPositiveButton("YES",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							((AlertDialog) dialog).getButton(
									AlertDialog.BUTTON1).setEnabled(false);
							rechargePost();

						}
					});

			// Setting Negative "NO" Button
			alertDialog.setNegativeButton("NO",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {

							dialog.cancel();

						}
					});

			// Showing Alert Message
			alertDialog.show();

		}

		else if ((validatenew() == 0) && (Operatorid.equals("SBE"))) {
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(
					HomeBillActivity_PBXNew.this);
			alertDialog.setTitle("Confirm Payment...");
			alertDialog.setMessage("ConsumerNumber:" + " "
					+ myHelpez.GetConsumerNumber() + "\n" + "MobileNumber:"
					+ " " + mobile_number.getText().toString() + "\n"
					+ "Operator:" + " "
					+ spl_OperatorSBE.getSelectedItem().toString() + "\n"
					+ "Amount:" + " " + "Rs." + " "
					+ myHelpez.GetMyRechargeAmount());
			alertDialog.setPositiveButton("YES",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							((AlertDialog) dialog).getButton(
									AlertDialog.BUTTON1).setEnabled(false);
							rechargePost();

						}
					});

			// Setting Negative "NO" Button
			alertDialog.setNegativeButton("NO",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {

							dialog.cancel();

						}
					});

			// Showing Alert Message
			alertDialog.show();

		} else {
			switch (validatenew()) {
			case 1:
				last_responseText.setVisibility(View.VISIBLE);
				last_responseText.setText("Select Service Provider");
				spl_OperatorSBE.setSelection(0);
				first_name.setText("");
				last_name.setText("");
				mobile_number.setText("");
				accountnumber.setText("");

				break;
			case 2:
				last_responseText.setVisibility(View.VISIBLE);
				last_responseText.setText("Enter First Name");
				first_name.setText("");
				last_name.setText("");
				mobile_number.setText("");
				accountnumber.setText("");
				break;

			case 3:
				last_responseText.setVisibility(View.VISIBLE);
				last_responseText.setText("Enter Last Name");
				last_name.setText("");
				mobile_number.setText("");
				accountnumber.setText("");
				break;
			case 4:
				last_responseText.setVisibility(View.VISIBLE);
				last_responseText.setText("Invalid MobileNumber");
				mobile_number.setText("");
				accountnumber.setText("");
				break;

			case 5:
				last_responseText.setVisibility(View.VISIBLE);
				last_responseText.setText("Invalid Account Number");
				accountnumber.setText("");
				break;
			case 6:
				last_responseText.setVisibility(View.VISIBLE);
				last_responseText.setText("Select NorthService Provider");
				spl_OperatorNBE.setSelection(0);
				first_name.setText("");
				last_name.setText("");
				mobile_number.setText("");
				accountnumber.setText("");
				break;
			}
		}
	}

	private int validatenew() {

		//Helpz myHelpez = new Helpz();
	/*	try {
			myHelpez.SetMyRechargeOperator(operatorSpinner.getSelectedItem()
					.toString());
		} catch (Exception ex) {

		}*/

		String Operatorid = (Get_OperatorID(myHelpez.GetMyRechargeOperator()
				.toString()));

		if (Operatorid.equals("NBE"))

		{

			if (spl_OperatorNBE.getSelectedItemPosition() < 1) {
				return 6;
			} else if (first_name.getText().toString().equals("")) {
				return 2;

			} else if (last_name.getText().toString().equals("")) {
				return 3;

			} else if (mobile_number.getText().toString().length() < 10) {
				return 4;
			} 
			//else if (accountnumber.getText().toString().length() == 0) {
			//	return 5;
			//} 
			else {
				return 0;
			}
		} else if (Operatorid.equals("SBE")) {
			if (spl_OperatorSBE.getSelectedItemPosition() < 1) {
				return 1;
			} else if (first_name.getText().toString().equals("")) {
				return 2;

			} else if (last_name.getText().toString().equals("")) {
				return 3;

			} else if (mobile_number.getText().toString().length() < 10) {
				return 4;
			} 
			//else if (accountnumber.getText().toString().length() == 0) {
			//	return 5;
			//}
			else {
				return 0;
			}
		}

		else {
			return 0;
		}

	}

	public void GetbillamountData(View view) {
		GetbillData();
	}

	private void GetbillData() {

		//Helpz myHelpz = new Helpz();
		String[] strResponse1 = null;
		String[] strResponse2 = null;
		
		//String Operatorid = (Get_OperatorID(myHelpez.GetMyRechargeOperator()
			//	.toString()));
		String Operatorid = Get_OperatorID(operatorSpinner.getSelectedItem().toString());
		if (Operatorid.equals("BES")) {
			try {

				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"http://180.179.67.76/MobAppS/PbxMobApp.ashx");
				
				 	 
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
					nameValuePairs.add(new BasicNameValuePair("AccountNo",consumernumber.getText().toString()));
					nameValuePairs.add(new BasicNameValuePair("OP", Get_OperatorID(myHelpez.GetMyRechargeOperator()).toString()));
					nameValuePairs.add(new BasicNameValuePair("RN", myHelpez.GetMyLoginMobileNumber()));
					nameValuePairs.add(new BasicNameValuePair("Service", "BillENQ"));
					
					final HttpParams httpParams = httpclient.getParams();
					HttpConnectionParams.setConnectionTimeout(httpParams, 15000);
					HttpConnectionParams.setSoTimeout(httpParams, 15000);
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					HttpResponse response = httpclient.execute(httppost);
					HttpEntity entity = response.getEntity();
					responseBody = EntityUtils.toString(entity);
					 check = responseBody;

				// Toast.makeText(HomeActivity2.this, strResponse,
				// Toast.LENGTH_LONG).show();

				strResponse1 = Split(check, "~");

				String[] LastValue = strResponse1;

				strResponse2 = Split(LastValue[16].toString(), ".");
				String[] LastValueNew = strResponse2;

				this.amountField.setText(LastValueNew[0].toString());
				// String set = LastValue[16].toString();
				// Integer getlastnode = set.indexOf(".00");
				// set.substring(0, (set-getlastnode));

				// this.amountField.setText(set);

				Log.i("postData", response.getStatusLine().toString());
				Log.i("postData", responseBodyhistory);
				Log.i("postData1", LastValueNew[0].toString());
				String abc = LastValueNew[0].toString();

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else if (Operatorid.equals("REM")) {
			try {

				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"http://180.179.67.76/MobAppS/PbxMobApp.ashx");
				
				 	 
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
					nameValuePairs.add(new BasicNameValuePair("AccountNo",consumernumber.getText().toString()));
					nameValuePairs.add(new BasicNameValuePair("OP", Get_OperatorID(myHelpez.GetMyRechargeOperator()).toString()));
					nameValuePairs.add(new BasicNameValuePair("RN", myHelpez.GetMyLoginMobileNumber()));
					nameValuePairs.add(new BasicNameValuePair("Service", "BillENQ"));
					
					final HttpParams httpParams = httpclient.getParams();
					HttpConnectionParams.setConnectionTimeout(httpParams, 15000);
					HttpConnectionParams.setSoTimeout(httpParams, 15000);
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					HttpResponse response = httpclient.execute(httppost);
					HttpEntity entity = response.getEntity();
					 responseBody = EntityUtils.toString(entity);
					 check = responseBody;

				// Toast.makeText(HomeActivity2.this, strResponse,
				// Toast.LENGTH_LONG).show();

				strResponse1 = Split(check, "~");

				String[] LastValue = strResponse1;

				strResponse2 = Split(LastValue[3].toString(), ".");
				String[] LastValueNew = strResponse2;

				this.amountField.setText(LastValueNew[0].toString());

				Log.i("postData", response.getStatusLine().toString());
				Log.i("postData", responseBodyhistory);

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		else if (Operatorid.equals("MGB")) {
			try {

				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"http://180.179.67.76/MobAppS/PbxMobApp.ashx");
				
				 	 
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
					nameValuePairs.add(new BasicNameValuePair("AccountNo",consumernumber.getText().toString()));
					nameValuePairs.add(new BasicNameValuePair("OP", Get_OperatorID(myHelpez.GetMyRechargeOperator()).toString()));
					nameValuePairs.add(new BasicNameValuePair("RN", myHelpez.GetMyLoginMobileNumber()));
					nameValuePairs.add(new BasicNameValuePair("Service", "BillENQ"));
					
					final HttpParams httpParams = httpclient.getParams();
					//HttpConnectionParams.setConnectionTimeout(httpParams, 15000);
					//HttpConnectionParams.setSoTimeout(httpParams, 15000);
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					HttpResponse response = httpclient.execute(httppost);
					HttpEntity entity = response.getEntity();
					responseBody = EntityUtils.toString(entity);
					 check = responseBody;
					 


				// Toast.makeText(HomeActivity2.this, strResponse,
				// Toast.LENGTH_LONG).show();

				strResponse1 = Split(check, "~");

				String[] LastValue = strResponse1;

				strResponse2 = Split(LastValue[4].toString(), ".");
				String[] LastValueNew = strResponse2;

				this.amountField.setText(LastValueNew[0].toString());

				Log.i("postData", response.getStatusLine().toString());
				Log.i("postData", responseBodyhistory);

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		
		}
		else {

		}
		
	}

}

