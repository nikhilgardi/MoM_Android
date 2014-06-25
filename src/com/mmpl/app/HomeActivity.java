package com.mmpl.app;

import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
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
import com.mmpl.app.HistoryActivity.XmlPullParsing;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.opengl.Visibility;
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
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class HomeActivity extends Activity implements OnClickListener {
	// private Button reButton;
	private Button testButton;
	private Button passButton;
	private Button backButton;
	private EditText passField;
	private EditText numField_mob;
	private EditText amountField;
	private Button postButton, secondback;
	private Button newButton, newButtontest;
	private String session_id, flag;
	TableLayout tablelayout;
	ProgressDialog dialog;
	TextView tv_secpass, accountbal;
	ImageView image;
	Spinner operatorSpinner, operatorSpinner1, operatorSpinner2;
	private Spinner spinner1;
	CheckBox responseCheckbox, respcheckbox;
	RadioButton rb, rb1, rb2;
	String[] pairs;
	HashMap<String, String> responseMap = new HashMap<String, String>();
	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	String responseBody;
	String response_status;
	String check;
	TextView responseText, secondback_responseText, responseText1;
	boolean checkbox;
	String Topup = "";
	private String response_message;
	String newoutput, output;
	String newoutputrecharge;
	String[] strArrayResponse;

	int i;
	Intent myintent1 = new Intent();
	Intent reintent = new Intent();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		// SharedPreferences appSettings =
		// getSharedPreferences(LoginActivity.PREFERENCE_FILENAME,
		// MODE_PRIVATE);

		init();

		postButton.setOnClickListener(this);

		addListenerOnSpinnerItemSelection();

		myintent1 = new Intent(HomeActivity.this, LoginActivity.class);
		reintent = new Intent(HomeActivity.this, MainActivity1.class);
	}

	/*
	 * private class GetRechargeTask extends AsyncTask<Void, Void, String> {
	 * 
	 * @Override protected String doInBackground(Void... params) {
	 * 
	 * // SharedPreferences appSettings =
	 * getSharedPreferences(LoginActivity.PREFERENCE_FILENAME, MODE_PRIVATE);
	 * //sendPost(); //rechargePost();
	 * 
	 * 
	 * 
	 * return responseBody; }
	 * 
	 * @Override protected void onPostExecute(String result) {
	 * 
	 * sendPost();
	 * 
	 * 
	 * } }
	 */
	private void init() {
		// this.tv_secpass = (TextView) findViewById(R.id.secpass);
		this.responseText = (TextView) findViewById(R.id.responseText);
		this.responseText1 = (TextView) findViewById(R.id.responseText1);
		this.secondback_responseText = (TextView) findViewById(R.id.secondresponseText);
		this.passButton = (Button) findViewById(R.id.btn_reclogin);
		this.backButton = (Button) findViewById(R.id.btn_firstback);
		this.newButton = (Button) findViewById(R.id.new_recharge);
		this.passField = (EditText) findViewById(R.id.repass);
		this.numField_mob = (EditText) findViewById(R.id.number);
		this.postButton = (Button) findViewById(R.id.btn_recharge);
		this.operatorSpinner = (Spinner) findViewById(R.id.Operator);
		this.secondback = (Button) findViewById(R.id.btn_secondback);

		this.amountField = (EditText) findViewById(R.id.amount);

		// this.image= (ImageView) findViewById(R.id.img_resp);

		this.rb = (RadioButton) findViewById(R.id.RadioButton01);
		this.rb1 = (RadioButton) findViewById(R.id.RadioButton02);
		this.rb2 = (RadioButton) findViewById(R.id.RadioButton03);
		this.accountbal = (TextView) findViewById(R.id.AccountBal);

		this.tablelayout = (TableLayout) findViewById(R.id.tableLayout1);
		AccountBalPost();
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		if (pref.getString("user_sessionMOM", "test").equals("MOM"))

		{
			passButton.setVisibility(View.VISIBLE);

			passField.setVisibility(View.VISIBLE);
			backButton.setVisibility(View.VISIBLE);
		} else if (pref.getString("user_sessionMOM", "test").equals("PBX")) {
			passButton.setVisibility(View.GONE);
			passField.setVisibility(View.GONE);
			backButton.setVisibility(View.GONE);
			numField_mob.setVisibility(View.VISIBLE);
			postButton.setVisibility(View.VISIBLE);
			secondback.setVisibility(View.VISIBLE);
			operatorSpinner.setVisibility(View.VISIBLE);
			amountField.setVisibility(View.VISIBLE);
		}

		String[] strOperators = getAllOperators();

		// Get_OperatorID("Airtel");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, strOperators);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		operatorSpinner.setAdapter(dataAdapter);

	}

	private void AccountBalPost() {

		Helpz myHelpz = new Helpz();

		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		if (pref.getString("user_sessionMOM", "test").equals("MOM"))

		{

			try {

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						3);
				nameValuePairs.add(new BasicNameValuePair("OperatorID", myHelpz
						.GetMyCustomerId()));
				nameValuePairs.add(new BasicNameValuePair("CompanyID", myHelpz
						.GetMyCompanyId()));
				nameValuePairs.add(new BasicNameValuePair("strAccessID",
						GlobalVariables.AccessId));
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"http://msvc.money-on-mobile.net/WebServiceV3Client.asmx/getBalanceByCustomerId");
				httppost.addHeader("ua", "android");
				final HttpParams httpParams = httpclient.getParams();
				HttpConnectionParams.setConnectionTimeout(httpParams, 15000);
				HttpConnectionParams.setSoTimeout(httpParams, 15000);
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				responseBody = EntityUtils.toString(entity);
				check = responseBody;
				// error="0";
				Log.i("postData", response.getStatusLine().toString());
				Log.i("info", responseBody);

				InputStream in = new ByteArrayInputStream(
						responseBody.getBytes("UTF-8"));
				new XmlPullParsingAccnt(in);

			} catch (Exception e) {
				Log.e("log_tag", "Error in http connection " + e.toString());
				responseBody = "Timeout|Error in Http Connection";
				// error="1";
			}
		}

		else if (pref.getString("user_sessionMOM", "test").equals("PBX"))

		{

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://180.179.67.76/MobAppS/PbxMobApp.ashx");
			try {

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						2);
				nameValuePairs.add(new BasicNameValuePair("RN", myHelpz
						.GetMyLoginMobileNumber()));
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
				// Double number = Double.valueOf(check);
				// DecimalFormat df = new DecimalFormat("#.00");
				// String newtestString = df.format(number);
				//
				// String abc = newtestString;
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
		} else {
			Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG)
					.show();
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

	public String[] getAllOperators() {
		String[] strResponse1 = null;
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		if (pref.getString("user_sessionMOM", "test").equals("MOM"))

		{
			HttpClient httpclient = new DefaultHttpClient();
			try {

				HttpPost httppost = new HttpPost(
						"http://180.179.67.72/nokiaservice/getProductByProductType.aspx?OperatorType=1");
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				this.responseBody = EntityUtils.toString(entity);
				String strOperators = responseBody;
				Log.i("postData", response.getStatusLine().toString());
				Log.i("postData", this.responseBody);

				String[] strArrOperators = Split(strOperators, "~");

				strResponse1 = strArrOperators;

			} catch (Exception ex) {
				String[] strResponse = { "NO ITEM TO DISPLAY" };
				strResponse1 = strResponse;
			}
		} else if (pref.getString("user_sessionMOM", "test").equals("PBX")) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://180.179.67.76/MobAppS/PbxMobApp.ashx");
			try {

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						2);
				nameValuePairs.add(new BasicNameValuePair("OT", "1"));
				nameValuePairs.add(new BasicNameValuePair("Service", "ON"));

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
		} else {
			Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG)
					.show();
		}

		return strResponse1;
	}

	private String Get_OperatorID(String strOperatorName) {

		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		if (pref.getString("user_sessionMOM", "test").equals("MOM"))

		{
			HttpClient httpclient = new DefaultHttpClient();
			try {

				HttpPost httppostnew = new HttpPost(
						"http://180.179.67.72/nokiaservice/getOperatorIdByOperatorName.aspx?OperatorName="
								+ urlEncode(strOperatorName.toString()));
				HttpResponse response = httpclient.execute(httppostnew);
				HttpEntity entity = response.getEntity();
				this.responseBody = EntityUtils.toString(entity);
				String strResponse = responseBody;
				Log.i("postData", response.getStatusLine().toString());
				Log.i("postData", this.responseBody);

				return strResponse;

			} catch (Exception ex) {
				return "0";
			}
		} else if (pref.getString("user_sessionMOM", "test").equals("PBX")) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://180.179.67.76/MobAppS/PbxMobApp.ashx");
			try {

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						3);
				nameValuePairs.add(new BasicNameValuePair("SN", strOperatorName
						.toString()));
				nameValuePairs.add(new BasicNameValuePair("Service", "OSN"));
				nameValuePairs.add(new BasicNameValuePair("OT", "1"));

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
			} catch (Exception ex) {
				return "0";
			}
		} else {
			return "0";
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

	public void rechargeLogin(View v) {
		if (this.fillData()) {
			// flag="1";
			// this.dialog = new ProgressDialog(this);
			// this.dialog.setMessage("Loading..");
			// this.dialog.show();
			// this.dialog.setCancelable(false);
			// GetRechargeTask task1 = new GetRechargeTask();
			// task1.execute();
			sendPost();

			// this.sendPost();
			// this.parseResponse();
			// this.setResponse();
			// rechargecheck();
		}
	}

	/*
	 * private int validate1() {
	 * 
	 * if (numField_mob.getText().toString().length() < 10) { return 1; } else
	 * if (operatorSpinner.getSelectedItemPosition() < 1) { return 2; } else if
	 * (amountField.getText().toString().length() == 0) { return 3; } else if
	 * (Integer.parseInt(amountField.getText().toString()) < 10) { return 3; }
	 * else if (Integer.parseInt(amountField.getText().toString()) > 10000) {
	 * return 3; //} else if ((responseCheckbox.isChecked() == false)) { //
	 * return 4; }else if ((rb.isChecked() == false) && (rb1.isChecked() ==
	 * false) && (rb2.isChecked() == false)) { return 4; }else { return 0; } }
	 */

	private int validate() {

		Helpz myHelpez = new Helpz();
		try {
			myHelpez.SetMyRechargeOperator(operatorSpinner.getSelectedItem()
					.toString());
		} catch (Exception ex) {

		}

		String Operatorid = (Get_OperatorID(myHelpez.GetMyRechargeOperator()
				.toString()));
		// if( (operatorSpinner.getSelectedItemPosition() == 3) ||
		// (operatorSpinner.getSelectedItemPosition() == 8)) {
		if ((Operatorid.equals("5")) || (Operatorid.equals("23"))) {
			if (operatorSpinner.getSelectedItemPosition() < 1) {
				return 1;
			} else if (numField_mob.getText().toString().length() < 10) {
				return 2;
			} else if (amountField.getText().toString().length() == 0) {
				return 3;
			} else if (Integer.parseInt(amountField.getText().toString()) < 10) {
				return 3;
			} else if (Integer.parseInt(amountField.getText().toString()) > 10000) {
				return 3;
				/*
				 * } else if ((responseCheckbox.isChecked() == false)) { return
				 * 4;
				 */
				// }else if ((rb.isChecked() == false) && (rb1.isChecked() ==
				// false)){
				// return 4;
			} else {
				return 0;
			}

		}
		// else if ((operatorSpinner.getSelectedItemPosition() == 14) ||
		// (operatorSpinner.getSelectedItemPosition() == 16)) {
		else if ((Operatorid.equals("16")) || (Operatorid.equals("30"))) {
			if (operatorSpinner.getSelectedItemPosition() < 1) {
				return 1;
			} else if (numField_mob.getText().toString().length() < 10) {
				return 2;
			} else if (amountField.getText().toString().length() == 0) {
				return 3;
			} else if (Integer.parseInt(amountField.getText().toString()) < 10) {
				return 3;
			} else if (Integer.parseInt(amountField.getText().toString()) > 10000) {
				return 3;
				/*
				 * } else if ((responseCheckbox.isChecked() == false)) { return
				 * 4;
				 */
				// }else if ((rb.isChecked() == false) && (rb2.isChecked() ==
				// false)){
				// return 4;
			} else {
				return 0;
			}
		} else {
			if (operatorSpinner.getSelectedItemPosition() < 1) {
				return 1;
			} else if (numField_mob.getText().toString().length() < 10) {
				return 2;
			} else if (amountField.getText().toString().length() == 0) {
				return 3;
			} else if (Integer.parseInt(amountField.getText().toString()) < 10) {
				return 3;
			} else if (Integer.parseInt(amountField.getText().toString()) > 10000) {
				return 3;
			} else {
				return 0;
			}
		}
	}

	private void rechargecheck() {
		// TODO Auto-generated method stub
		// if (this.response_status.equals("1")) {
		this.passButton.setVisibility(View.GONE);
		// this.tv_secpass.setVisibility(View.GONE);
		this.passField.setVisibility(View.GONE);
		this.responseText.setVisibility(View.GONE);
		this.backButton.setVisibility(View.GONE);
		this.numField_mob.setVisibility(View.VISIBLE);
		this.postButton.setVisibility(View.VISIBLE);
		this.operatorSpinner.setVisibility(View.VISIBLE);
		this.secondback.setVisibility(View.VISIBLE);

		this.amountField.setVisibility(View.VISIBLE);
		// this.responseCheckbox.setVisibility(View.VISIBLE);
		// // this.rb.setVisibility(View.VISIBLE);
		// // this.rb1.setVisibility(View.VISIBLE);
		// // this.rb2.setVisibility(View.VISIBLE);

		// this.operatorSpinner1.setVisibility(View.VISIBLE);

		// }
		/*
		 * if(this.response_status.equals("0")){
		 * this.responseText.setVisibility(View.VISIBLE);
		 * responseText.setText("Incorrect Pass/Missing Params"); }
		 * if(this.response_status.equals("l")){ startActivity(myintent1); }
		 */
	}

	private void newrechargecheck() {

		this.numField_mob.setVisibility(View.GONE);
		this.postButton.setVisibility(View.GONE);
		this.operatorSpinner.setVisibility(View.GONE);
		this.amountField.setVisibility(View.GONE);
		this.responseCheckbox.setVisibility(View.GONE);
		this.image.setVisibility(View.VISIBLE);
		this.responseText.setVisibility(View.VISIBLE);
		this.newButton.setVisibility(View.VISIBLE);
		image.setImageResource(R.drawable.success);
		this.responseText.setText(this.newoutputrecharge);
		// //////// Toast.makeText(HomeActivity.this, this.newoutputrecharge,
		// Toast.LENGTH_SHORT).show();

	}

	/*
	 * public void rechargeData(View v) { //flag="0"; this.dialog = new
	 * ProgressDialog(this); this.dialog.setMessage("Connecting..");
	 * this.dialog.show(); this.dialog.setCancelable(false); GetRechargeTask
	 * task = new GetRechargeTask(); task.execute(); }
	 */
	public void newRecharge(View v) {

		this.resetLayout();
	}

	private void resetLayout() {
		AccountBalPost();
		tablelayout.setVisibility(View.VISIBLE);
		this.operatorSpinner.setSelection(0);
		this.numField_mob.setText("");
		this.amountField.setText("");
		this.numField_mob.setVisibility(View.VISIBLE);
		this.postButton.setVisibility(View.VISIBLE);
		this.operatorSpinner.setVisibility(View.VISIBLE);
		this.amountField.setVisibility(View.VISIBLE);
		this.secondback.setVisibility(View.VISIBLE);
		this.newButton.setVisibility(View.GONE);
		this.responseText.setVisibility(View.GONE);
		this.responseText1.setVisibility(View.GONE);

	}

	public void backpost(View v) {

		this.finish();

		startActivity(reintent);

	}

	public void Backpost1(View v) {

		this.finish();

		startActivity(reintent);

	}

	private void rechargePost() {
		String[] strResponse1 = null;
		Helpz myHelpez = new Helpz();
		try {

			myHelpez.SetMyRechargeMobileNumber(numField_mob.getText()
					.toString());
			myHelpez.SetMyRechargeAmount(amountField.getText().toString());
			myHelpez.SetMyRechargeOperator(operatorSpinner.getSelectedItem()
					.toString());

			/*
			 * if (responseCheckbox.isChecked()== true ) {
			 * myHelpez.SetMyRechargeType("0"); }else {
			 * myHelpez.SetMyRechargeType("0"); }
			 */

			if (rb.isChecked() == true) {
				myHelpez.SetMyRechargeType("0");
			} else if (rb1.isChecked() == true) {
				myHelpez.SetMyRechargeType("1");
			} else if (rb2.isChecked() == true) {
				myHelpez.SetMyRechargeType("2");
			} else {
				myHelpez.SetMyRechargeType("0");
			}

		} catch (Exception ex) {
		}
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		if (pref.getString("user_sessionMOM", "test").equals("MOM"))

		{
			/*
			 * try {
			 * 
			 * myHelpez.SetMyRechargeMobileNumber(numField_mob.getText()
			 * .toString());
			 * myHelpez.SetMyRechargeAmount(amountField.getText().toString());
			 * myHelpez.SetMyRechargeOperator(operatorSpinner.getSelectedItem()
			 * .toString());
			 * 
			 * /* if (responseCheckbox.isChecked()== true ) {
			 * myHelpez.SetMyRechargeType("0"); }else {
			 * myHelpez.SetMyRechargeType("0"); }
			 */

			/*
			 * if (rb.isChecked() == true) { myHelpez.SetMyRechargeType("0"); }
			 * else if (rb1.isChecked() == true) {
			 * myHelpez.SetMyRechargeType("1"); } else if (rb2.isChecked() ==
			 * true) { myHelpez.SetMyRechargeType("2"); } else {
			 * myHelpez.SetMyRechargeType("0"); }
			 * 
			 * } catch (Exception ex) { }
			 */
			try {

				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"http://msvc.money-on-mobile.net/WebServiceV3Trans.asmx/DoMobRecharge");

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						7);
				nameValuePairs.add(new BasicNameValuePair("strMobileNumber",
						myHelpez.GetMyRechargeMobileNumber()));
				nameValuePairs.add(new BasicNameValuePair("strRechargeAmount",
						myHelpez.GetMyRechargeAmount()));
				nameValuePairs.add(new BasicNameValuePair("strOperator",
						Get_OperatorID(myHelpez.GetMyRechargeOperator())
								.toString()));
				nameValuePairs.add(new BasicNameValuePair("intRechargeType",
						myHelpez.GetMyRechargeType()));
				nameValuePairs.add(new BasicNameValuePair("intCustomerId",
						myHelpez.GetMyCustomerId()));
				nameValuePairs.add(new BasicNameValuePair("strAccessID",
						GlobalVariables.AccessId));
				nameValuePairs.add(new BasicNameValuePair("CompanyID", myHelpez
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
		} else if (pref.getString("user_sessionMOM", "test").equals("PBX"))

		{
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://180.179.67.76/MobAppS/PbxMobApp.ashx");
			try {

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						5);
				nameValuePairs.add(new BasicNameValuePair("CustMobile",
						myHelpez.GetMyRechargeMobileNumber()));
				nameValuePairs.add(new BasicNameValuePair("Amount", myHelpez
						.GetMyRechargeAmount()));
				nameValuePairs.add(new BasicNameValuePair("OP", Get_OperatorID(
						myHelpez.GetMyRechargeOperator()).toString()));
				nameValuePairs.add(new BasicNameValuePair("RN", myHelpez
						.GetMyLoginMobileNumber()));
				nameValuePairs.add(new BasicNameValuePair("Service", "RM"));

				final HttpParams httpParams = httpclient.getParams();
			//	HttpConnectionParams.setConnectionTimeout(httpParams, 15000);
			//	HttpConnectionParams.setSoTimeout(httpParams, 15000);
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
							+ "\n" + "Message:" + strResponse1[1].toString()
							 + "\n" + "Amount :" + strResponse1[3].toString());
					       
					sb.append("\n");
					sb.append("\n");

					String a = sb.toString();

					responseText1.setVisibility(View.VISIBLE);
					newButton.setVisibility(View.VISIBLE);
					responseText1.setText(a);
					numField_mob.setVisibility(View.GONE);
					postButton.setVisibility(View.GONE);
					operatorSpinner.setVisibility(View.GONE);
					amountField.setVisibility(View.GONE);

					backButton.setVisibility(View.GONE);
					secondback.setVisibility(View.GONE);
					rb.setVisibility(View.GONE);
					rb1.setVisibility(View.GONE);
					rb2.setVisibility(View.GONE);
					tablelayout.setVisibility(View.VISIBLE);
					image.setVisibility(View.INVISIBLE);
					responseText.setVisibility(View.GONE);

					Log.i("postData~", response.getStatusLine().toString());
					Log.i("info", a);
				}

				else {
					responseText1.setVisibility(View.VISIBLE);
					newButton.setVisibility(View.VISIBLE);
					responseText1.setText(check);
					numField_mob.setVisibility(View.GONE);
					postButton.setVisibility(View.GONE);
					operatorSpinner.setVisibility(View.GONE);
					amountField.setVisibility(View.GONE);

					backButton.setVisibility(View.GONE);
					secondback.setVisibility(View.GONE);
					rb.setVisibility(View.GONE);
					rb1.setVisibility(View.GONE);
					rb2.setVisibility(View.GONE);
					tablelayout.setVisibility(View.VISIBLE);
					image.setVisibility(View.INVISIBLE);
					responseText.setVisibility(View.GONE);

					Log.i("postDataWithout", response.getStatusLine()
							.toString());
					Log.i("info", responseBody);
				}
			} catch (Exception e) {
				Log.e("log_tagTESTabcd",
						"Error in http connection " + e.toString());
				responseBody = "Timeout|Error in Http Connection";

			}

		} else {
			Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG)
					.show();
		}

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

					
					passButton.setVisibility(View.GONE);
					// tv_secpass.setVisibility(View.GONE);
					passField.setVisibility(View.GONE);
					backButton.setVisibility(View.GONE);
					responseText.setVisibility(View.GONE);

					numField_mob.setVisibility(View.VISIBLE);
					postButton.setVisibility(View.VISIBLE);
					secondback.setVisibility(View.VISIBLE);
					operatorSpinner.setVisibility(View.VISIBLE);
					amountField.setVisibility(View.VISIBLE);
					myintent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					
					break;
				default:

					

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

				Toast.makeText(HomeActivity.this, newoutputrecharge,
						Toast.LENGTH_LONG).show();

				responseText1.setVisibility(View.VISIBLE);
				responseText1.setText(newoutputrecharge);
				newButton.setVisibility(View.VISIBLE);

				// newrechargecheck();
				numField_mob.setVisibility(View.GONE);
				postButton.setVisibility(View.GONE);
				operatorSpinner.setVisibility(View.GONE);
				amountField.setVisibility(View.GONE);
				// responseCheckbox.setVisibility(View.GONE);
				backButton.setVisibility(View.GONE);
				secondback.setVisibility(View.GONE);
				rb.setVisibility(View.GONE);
				rb1.setVisibility(View.GONE);
				rb2.setVisibility(View.GONE);
				tablelayout.setVisibility(View.VISIBLE);
				image.setVisibility(View.INVISIBLE);
				responseText.setVisibility(View.GONE);
				// responseText1.setVisibility(View.VISIBLE);
				// newButton.setVisibility(View.VISIBLE);
				// image.setImageResource(R.drawable.success);
				// responseText.setText(newoutputrecharge);
				// responseText1.setText(newoutputrecharge);

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
			
			return true;
		}
	}

	

	
//
//	@Override
//	public void onUserLeaveHint() {
//		
//		this.finish();
//	}

	@Override
	public void onBackPressed() {
				return;
	}

	@Override
	public void onClick(View view) {
		if (validate() == 0) {
			secondback_responseText.setText(" ");

			AlertDialog.Builder alertDialog = new AlertDialog.Builder(
					HomeActivity.this);
			
			// Setting Dialog Title
			alertDialog.setTitle("Confirm MobileRecharge...");

			// Setting Dialog Message
			alertDialog.setMessage("Mobile Number:" + " "
					+ numField_mob.getText().toString() + "\n" + "Operator:"
					+ " " + operatorSpinner.getSelectedItem().toString() + "\n"
					+ "Amount:" + " " + "Rs." + " "
					+ amountField.getText().toString());

			
			
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
						public void onClick(DialogInterface dialog, int which1) {
							
							dialog.cancel();

						}
					});

			
			alertDialog.show();

			// TODO Auto-generated method stub

		} else {
			switch (validate()) {
			case 1:

				secondback_responseText.setVisibility(View.VISIBLE);
				secondback_responseText.setText("Select Service Provider");
				operatorSpinner.setSelection(0);
				numField_mob.setText("");
				amountField.setText("");

				break;

			case 2:

				secondback_responseText.setVisibility(View.VISIBLE);
				secondback_responseText.setText("Invalid MobileNumber");
				numField_mob.setText("");
				break;

			case 3:
				secondback_responseText.setVisibility(View.VISIBLE);
				secondback_responseText.setText("Invalid Amount");
				amountField.setText("");
				break;

			/*
			 * case 4: responseText.setVisibility(View.VISIBLE);
			 * responseText.setText("Select CheckBox"); break;
			 */
			case 4:
				secondback_responseText.setVisibility(View.VISIBLE);
				secondback_responseText.setText("Select radio button");
				break;
			}
		}
	}

	public class CustomOnItemSelectedListener implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			
			Helpz myHelpez = new Helpz();
			try {
				myHelpez.SetMyRechargeOperator(operatorSpinner
						.getSelectedItem().toString());
			} catch (Exception ex) {

			}

			String Operatorid = (Get_OperatorID(myHelpez
					.GetMyRechargeOperator().toString()));

			if (Operatorid.equals("5")) {
				numField_mob.setText("");
				amountField.setText("");

				
				rb.setChecked(true);
				rb1.setChecked(false);
				rb.setVisibility(view.VISIBLE);
				rb1.setVisibility(view.VISIBLE);
				rb2.setVisibility(view.GONE);
			}
			// else if (pos == 8) {
			else if (Operatorid.equals("23")) {
				numField_mob.setText("");
				amountField.setText("");
				rb.setChecked(true);
				rb1.setChecked(false);
				rb.setVisibility(view.VISIBLE);
				rb1.setVisibility(view.VISIBLE);
				rb2.setVisibility(view.GONE);
			}
			// else if (pos == 14) {
			else if (Operatorid.equals("16")) {
				numField_mob.setText("");
				amountField.setText("");
				rb.setChecked(true);
				rb2.setChecked(false);
				rb.setVisibility(view.VISIBLE);
				rb2.setVisibility(view.VISIBLE);
				rb1.setVisibility(view.GONE);
			}
			// else if (pos == 16) {
			else if (Operatorid.equals("30")) {
				numField_mob.setText("");
				amountField.setText("");
				rb.setChecked(true);
				rb2.setChecked(false);
				rb.setVisibility(view.VISIBLE);
				rb2.setVisibility(view.VISIBLE);
				rb1.setVisibility(view.GONE);
			} else {
				numField_mob.setText("");
				amountField.setText("");
				rb.setVisibility(view.GONE);
				rb1.setVisibility(view.GONE);
				rb2.setVisibility(view.GONE);
			}

		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
		}

	}

}