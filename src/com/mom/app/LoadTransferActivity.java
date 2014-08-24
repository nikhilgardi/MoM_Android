package com.mom.app;

import java.io.ByteArrayInputStream;
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

import com.mom.app.CreditSaleView.XmlPullParsing;

import com.mom.app.LoginActivity.XmlPullParsingAccntBal;

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoadTransferActivity extends Activity implements OnClickListener {

	private EditText numField_mob;
	private EditText amountField;
	private Button postButton;
	String responseBody;
	Intent reintent;
	TextView accountbal, responseText;
	Helpz myHelpz = new Helpz();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_load_transfer);
		init();
		getWindow().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.appsbg));
		AccountBalPost();

		postButton.setOnClickListener(this);
		reintent = new Intent(LoadTransferActivity.this, MainActivity1.class);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.load_transfer, menu);
		return true;
	}

	private void init() {
		this.numField_mob = (EditText) findViewById(R.id.NewPassword);
		this.amountField = (EditText) findViewById(R.id.ConfirmPassword);
		this.postButton = (Button) findViewById(R.id.btn_Submit);
		this.accountbal = (TextView) findViewById(R.id.AccountBal);
		this.responseText = (TextView) findViewById(R.id.responseText);
	}

	private void AccountBalPost() {

		String test = myHelpz.GetRMNAccountBal().toString();
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		if (pref.getString("user_sessionMOM", "test").equals("MOM"))

		{
			accountbal.setVisibility(View.VISIBLE);
			accountbal.setText(getResources().getString(R.string.lblBal)
					+ myHelpz.GetRMNAccountBal().toString());
			Log.i(getResources().getString(R.string.lblBal), test);
		} else if (pref.getString("user_sessionMOM", "test").equals("PBX"))

		{
			accountbal.setVisibility(View.VISIBLE);
			accountbal.setText(getResources().getString(R.string.lblBal)
					+ myHelpz.GetRMNAccountBal().toString());
		} else {
			Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG)
					.show();
		}
	}

	private class GetLoginTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {

			return responseBody;
		}

		@Override
		protected void onPostExecute(String result) {

			SharedPreferences pref = PreferenceManager
					.getDefaultSharedPreferences(getApplicationContext());
			if (pref.getString("user_sessionMOM", "test").equals("MOM")) {

				try {

					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
							3);
					nameValuePairs.add(new BasicNameValuePair("OperatorID",
							myHelpz.GetMyCustomerId()));
					nameValuePairs.add(new BasicNameValuePair("CompanyID",
							myHelpz.GetMyCompanyId()));
					nameValuePairs.add(new BasicNameValuePair("strAccessID",
							GlobalVariables.AccessId));
					HttpClient httpclient = new DefaultHttpClient();
					HttpPost httppost = new HttpPost(
							"http://msvc.money-on-mobile.net/WebServiceV3Client.asmx/getBalanceByCustomerId");
					httppost.addHeader("ua", "android");
					final HttpParams httpParams = httpclient.getParams();
					HttpConnectionParams
							.setConnectionTimeout(httpParams, 15000);
					HttpConnectionParams.setSoTimeout(httpParams, 15000);
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					HttpResponse response = httpclient.execute(httppost);
					HttpEntity entity = response.getEntity();
					responseBody = EntityUtils.toString(entity);
					String check = responseBody;
					// error="0";
					Log.i("postData", response.getStatusLine().toString());
					Log.i("info", responseBody);

					InputStream in = new ByteArrayInputStream(
							responseBody.getBytes("UTF-8"));
					new XmlPullParsingAccntBal(in);

				} catch (Exception e) {
					Log.e("log_tag", "Error in http connection " + e.toString());
					responseBody = "Timeout|Error in Http Connection";
					// error="1";
				}
			} else if (pref.getString("user_sessionMOM", "test").equals("PBX")) {

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
					HttpConnectionParams
							.setConnectionTimeout(httpParams, 15000);
					HttpConnectionParams.setSoTimeout(httpParams, 15000);
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					HttpResponse response = httpclient.execute(httppost);
					HttpEntity entity = response.getEntity();
					responseBody = EntityUtils.toString(entity);
					String check = responseBody;

					Log.i("postData", response.getStatusLine().toString());
					Log.i("info", responseBody);

					myHelpz.SetRMNAccountBal(check);
					accountbal.setVisibility(View.VISIBLE);
					accountbal.setText(getResources()
							.getString(R.string.lblBal)
							+ myHelpz.GetRMNAccountBal());
				} catch (Exception e) {
					Log.e("log_tag", "Error in http connection " + e.toString());
					responseBody = "Timeout|Error in Http Connection";
					// error="1";
				}
			} else {
				Toast.makeText(getApplicationContext(), "Error",
						Toast.LENGTH_LONG).show();
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
				String output = xmlpullparser1.getText();
				String newoutputrecharge = output;

				myHelpz.SetRMNAccountBal(newoutputrecharge);
				accountbal.setVisibility(View.VISIBLE);
				accountbal.setText(getResources().getString(R.string.lblBal)
						+ myHelpz.GetRMNAccountBal());
				break;

			}

		}

	}

	public void postLoginData() {
		Helpz myHelpz = new Helpz();

		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		
		if (pref.getString("user_sessionMOM", "test").equals("MOM")) {
			try {

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						6);
				nameValuePairs.add(new BasicNameValuePair("payer", myHelpz
						.GetMyUserId()));
				nameValuePairs.add(new BasicNameValuePair("Payee", numField_mob
						.getText().toString()));
				nameValuePairs.add(new BasicNameValuePair("TransferAmount",
						amountField.getText().toString()));
				nameValuePairs.add(new BasicNameValuePair("Transvaltype",
						"Refilled"));
				nameValuePairs.add(new BasicNameValuePair("strAccessID",
						GlobalVariables.AccessId));
				nameValuePairs.add(new BasicNameValuePair("CompanyID", myHelpz
						.GetMyCompanyId()));
				HttpClient httpclient = new DefaultHttpClient();

				HttpPost httppost = new HttpPost(
						"http://msvc.money-on-mobile.net/WebServiceV3Trans.asmx/doBalanceTransfer");
				httppost.addHeader("ua", "android");
				final HttpParams httpParams = httpclient.getParams();
				HttpConnectionParams.setConnectionTimeout(httpParams, 15000);
				HttpConnectionParams.setSoTimeout(httpParams, 15000);
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				responseBody = EntityUtils.toString(entity);
				String check = responseBody;
				// error="0";
				Log.i("postData", response.getStatusLine().toString());
				Log.i("info", responseBody);

				InputStream in = new ByteArrayInputStream(
						responseBody.getBytes("UTF-8"));
				new XmlPullParsing(in);

			} catch (Exception e) {
				Log.e("log_tag", "Error in http connection " + e.toString());
				responseBody = "Timeout|Error in Http Connection";
				// error="1";
			}
		} 
		
		else if (pref.getString("user_sessionMOM", "test").equals("PBX"))

		{
			 HttpClient httpclient = new DefaultHttpClient();
             HttpPost httppost = new HttpPost("http://180.179.67.76/MobAppS/PbxMobApp.ashx");
             try {


                 List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
                 nameValuePairs.add(new BasicNameValuePair("Parent", myHelpz.GetMyLoginMobileNumber()));
                 nameValuePairs.add(new BasicNameValuePair("Child",  numField_mob.getText().toString()));
                 nameValuePairs.add(new BasicNameValuePair("Amount", amountField.getText().toString()));
                 nameValuePairs.add(new BasicNameValuePair("Service", "IBT"));


                 httppost.addHeader("ua", "android");
                 final HttpParams httpParams = httpclient.getParams();
                 HttpConnectionParams.setConnectionTimeout(httpParams, 15000);
                 HttpConnectionParams.setSoTimeout(httpParams, 15000);
                 httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                 HttpResponse response = httpclient.execute(httppost);
                 HttpEntity entity = response.getEntity();
                 responseBody = EntityUtils.toString(entity);
                String  check = responseBody;

                 Log.i("postData", response.getStatusLine().toString());
                 Log.i("info", responseBody);
                 responseText.setVisibility(View.VISIBLE);
					responseText.setText(check);
					
					Log.i("Response",check);
					Toast.makeText(LoadTransferActivity.this,
							check, Toast.LENGTH_SHORT)
							.show();

                 
             } catch (Exception e) {
                 Log.e("log_tag", "Error in http connection " + e.toString());
                 responseBody = "Timeout|Error in Http Connection";
                 // error="1";
             }
         } 
		
		
		else {
			Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
		}
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
				String[] strArrayResponse = Split(newoutput, "~");
				int i = Integer.parseInt(strArrayResponse[0].toString());

				switch (Integer.parseInt(strArrayResponse[0].toString())) {
				case 0:
					
					
					responseText.setVisibility(View.VISIBLE);
					responseText.setText(strArrayResponse[1].toString());
					
					String test = strArrayResponse[1].toString();
					Log.i("Response", strArrayResponse[1].toString());
//					Toast.makeText(LoadTransferActivity.this,
//							strArrayResponse[1].toString(), Toast.LENGTH_SHORT)
//							.show();

					break;
				default:
					
					responseText.setVisibility(View.VISIBLE);
					responseText.setText(strArrayResponse[1].toString());
					Toast.makeText(LoadTransferActivity.this,
							strArrayResponse[1].toString(), Toast.LENGTH_SHORT)
							.show();

					break;
				}

			}

		}

	}

	private int validate() {
		if (numField_mob.getText().toString().length() < 10) {
			return 1;
		} else if (numField_mob.getText().toString().length() > 10) {
			return 1;
		} else if (amountField.getText().toString().length() == 0) {
			return 2;
		} else if (Integer.parseInt(amountField.getText().toString()) < 10) {
			return 2;
		} else {
			return 0;
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

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if (validate() == 0) {

			responseText.setText("");
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(
					LoadTransferActivity.this);

			// Setting Dialog Title
			alertDialog.setTitle(getResources().getString(
					R.string.AlertDialog_BillPayment));

			// Setting Dialog Message
			alertDialog.setMessage(getResources().getString(
					R.string.Lbl_MobileNumber)
					+ " "
					+ numField_mob.getText().toString()
					+ "\n"
					+ getResources().getString(R.string.Lbl_Amount)
					+ " "
					+ amountField.getText().toString());

			alertDialog.setPositiveButton(
					getResources().getString(R.string.Dialog_Yes),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							((AlertDialog) dialog).getButton(
									AlertDialog.BUTTON1).setEnabled(false);
							postLoginData();
							numField_mob.setText("");
							amountField.setText("");
							new GetLoginTask().onPostExecute("test");

						}
					});

			// Setting Negative "NO" Button
			alertDialog.setNegativeButton(
					getResources().getString(R.string.Dialog_No),
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
				responseText.setText("");
				responseText.setVisibility(View.VISIBLE);
				responseText.setText(getResources().getString(
						R.string.prompt_Validity_mobile_number));
				numField_mob.setText("");
				break;

			case 2:
				responseText.setText("");
				responseText.setVisibility(View.VISIBLE);
				responseText.setText(getResources().getString(
						R.string.prompt_Validity_amount));
				amountField.setText("");
				break;

			}
		}
	}

	public void backpost(View v) {

		// myintent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(reintent);
		this.finish();

	}

}
