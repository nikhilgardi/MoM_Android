package com.mom.app;

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

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mom.app.utils.MOMConstants;

public class HistoryActivity extends Activity {
	private String responseBody;
	String responseBodyhistory;
	private TextView accntbalresponse;
	String output ,check ;
	private String session_id;
	WebView wv;
	ListView listview;
	String flag;
	String[] pairs;
	ImageButton refresh;
	ImageButton number;
	TextView res, responsetest;
	int RowCount = 0;

	// String url;
	private EditText numField;
	Intent myintent = new Intent();
	Intent myintenttest = new Intent();
	ProgressDialog dialog;
	String response_status;
	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	ArrayList<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>(2);
	// ArrayList<NameValuePair> nameValuePairs2 = new
	// ArrayList<NameValuePair>(2);
	HashMap<String, String> responseMap = new HashMap<String, String>();
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.history_new);
		wv = (WebView) findViewById(R.id.resp);
		refresh = (ImageButton) findViewById(R.id.btn_search);
	
		res = (TextView) findViewById(R.id.textView1);
		responsetest = (TextView) findViewById(R.id.textView1);
		accntbalresponse = (TextView) findViewById(R.id.textViewaccntbal);
		  getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.appsbg)); 
		//new GetLoginTask().onPostExecute("http://msvc.money-on-mobile.net/WebServiceV3Client.asmx/getBalanceByCustomerId");
		new GetLoginTask().onPostExecute("test");
	//	AccountBalPost(); 
		this.posting();
		myintent = new Intent(HistoryActivity.this, LoginActivity.class);
	
	}

	private class GetLoginTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			
			
			return responseBody;
		}
	
	
	@Override
	protected void onPostExecute(String result) {
		Helpz myHelpz = new Helpz();
		 SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			
			if(pref.getString("user_sessionMOM", "test").equals("MOM"))
				
				{
				accntbalresponse.setVisibility(View.VISIBLE);
				accntbalresponse.setText("Bal: Rs." + myHelpz.GetRMNAccountBal().toString());

				}
			
			else if(pref.getString("user_sessionMOM", "test").equals("PBX"))
				
				
			{
				accntbalresponse.setVisibility(View.VISIBLE);
				accntbalresponse.setText("Bal: Rs." + myHelpz.GetRMNAccountBal().toString());

			}
			else{
				Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
			}
	}
	}

	
	private void AccountBalPost(){
		 
		 Helpz myHelpz = new Helpz();
		 SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			
			if(pref.getString("user_sessionMOM", "test").equals("MOM"))
				
				{
			try {
			 	 
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
	            nameValuePairs.add(new BasicNameValuePair("OperatorID",myHelpz.GetMyCustomerId()));
	            nameValuePairs.add(new BasicNameValuePair("CompanyID", myHelpz.GetMyCompanyId()));
				nameValuePairs.add(new BasicNameValuePair("strAccessID",GlobalVariables.AccessId));
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost("http://msvc.money-on-mobile.net/WebServiceV3Client.asmx/getBalanceByCustomerId");
				httppost.addHeader("ua", "android");
				final HttpParams httpParams = httpclient.getParams();
				HttpConnectionParams.setConnectionTimeout(httpParams, 15000);
				HttpConnectionParams.setSoTimeout(httpParams, 15000);
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				responseBody = EntityUtils.toString(entity);
				 check = responseBody;
//				error="0";
				Log.i("postData", response.getStatusLine().toString());
				Log.i("info", responseBody);
				
				InputStream in = new ByteArrayInputStream(responseBody.getBytes("UTF-8"));
			    new XmlPullParsing(in);	
			 
			} catch (Exception e) {
				Log.e("log_tag", "Error in http connection " + e.toString());
				responseBody= "Timeout|Error in Http Connection";
//				error="1";
			}
				}
			
			else if(pref.getString("user_sessionMOM", "test").equals("PBX"))
				
				
			{
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
//					Double number = Double.valueOf(check);
//					DecimalFormat df = new DecimalFormat("#.00");
//					String newtestString = df.format(number);
//
//					String abc = newtestString;
					// error="0";
					Log.i("postData", response.getStatusLine().toString());
					Log.i("info", responseBody);

					accntbalresponse.setVisibility(View.VISIBLE);
					accntbalresponse.setText("Bal: Rs." + check);

				} catch (Exception e) {
					Log.e("log_tag", "Error in http connection " + e.toString());
					responseBody = "Timeout|Error in Http Connection";
					// error="1";
				}
			}
			else{
				Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
			}
	}
	

	public class XmlPullParsing {

		  protected XmlPullParser xmlpullparser1;
		   String output1;
		  String TAG="XmlPullParsing";
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

		    void parseTag(int event){

		        switch (event) {

		        case XmlPullParser.START_DOCUMENT:
		            Log.i(TAG,"START_DOCUMENT");
		            break;

		        case XmlPullParser.END_DOCUMENT:
		            Log.i(TAG,"END_DOCUMENT");
		            break;
		        case XmlPullParser.START_TAG: 
		            Log.i(TAG,"START_TAG"+xmlpullparser1.getName());
		            Log.i(TAG,"Attribute Name"+xmlpullparser1.getAttributeValue(null,"category"));

		            break;

		        case XmlPullParser.END_TAG: 
		            Log.i(TAG,"END_TAG"+xmlpullparser1.getName());

		            break;

		        case XmlPullParser.TEXT:
		            Log.i(TAG,"TEXT");
		            output = xmlpullparser1.getText();
		            String newoutputrecharge = output;
		           		                
			//////////        Toast.makeText(InfoActivity.this, newoutputrecharge, Toast.LENGTH_LONG).show();
			    
		            accntbalresponse.setVisibility(View.VISIBLE);
		            accntbalresponse.setText("Bal: Rs. " + newoutputrecharge);
			         //response.setText("Bal: Rs. 100000000");
			        
			           
			        break;
		        
		        }
		        

		    }
		       
   }
	public void posting() {
		// SharedPreferences appSettings =
		// getSharedPreferences(LoginActivity.PREFERENCE_FILENAME,
		// MODE_PRIVATE);
		// session_id = appSettings.getString("user_session","-1");
		// responseBody="";
		// nameValuePairs1.add(new BasicNameValuePair("session_id",session_id));
		// flag="1";
		// this.dialog = new ProgressDialog(this);
		// this.dialog.setMessage("Fetching History");
		// this.dialog.show();
		// this.dialog.setCancelable(false);
		// url= "http://app.recharge123.com/history.php";
		SaveSessionData();
		rechargePost();
		//GetHistoryTask task = new GetHistoryTask();
		//task.execute();
	}

	public void numberSearch(View v) {
		SharedPreferences appSettings = getSharedPreferences(
				MOMConstants.APP_PREFERENCES, MODE_PRIVATE);
		// session_id = appSettings.getString("user_session","-1");
		responseBody = "";
		// nameValuePairs.add(new BasicNameValuePair("session_id",session_id));
		// nameValuePairs.add(new
		// BasicNameValuePair("customer_num",numField.getText().toString()));
		// flag="0";
		this.dialog = new ProgressDialog(this);
		this.dialog.setMessage("Fetching Number History");
		this.dialog.show();
		this.dialog.setCancelable(false);
///////////	GetHistoryTask task = new GetHistoryTask();
///////////	task.execute();
	}

	//private class GetHistoryTask extends AsyncTask<Void, Void, String> {

///////////	@Override
	//	protected String doInBackground(Void... params) {

			// TODO Auto-generated method stub
			// SharedPreferences appSettings =
			// getSharedPreferences(LoginActivity.PREFERENCE_FILENAME,
			// MODE_PRIVATE);

			///////////rechargePost();
			// session_id = appSettings.getString("user_session","-1");
			// responseBody="";
			/*
			 * try { //nameValuePairs.add(new
			 * BasicNameValuePair("session_id",session_id)); HttpClient
			 * httpclient = new DefaultHttpClient(); HttpPost httppost = new
			 * HttpPost( "https://recharge123.com/mobileapp/history.php");
			 * httppost.addHeader("ua", "android"); final HttpParams httpParams
			 * = httpclient.getParams();
			 * HttpConnectionParams.setConnectionTimeout(httpParams, 15000);
			 * HttpConnectionParams.setSoTimeout(httpParams, 15000);
			 * if(flag.equals("0")){ httppost.setEntity(new
			 * UrlEncodedFormEntity(nameValuePairs)); } if(flag.equals("1")){
			 * httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs1)); }
			 * HttpResponse response = httpclient.execute(httppost); HttpEntity
			 * entity = response.getEntity(); responseBody =
			 * EntityUtils.toString(entity); Log.i("postData",
			 * response.getStatusLine().toString()); Log.i("history",
			 * responseBody); } catch (Exception e) { responseBody =
			 * "Timeout|Error in Http Connection"; Log.e("log_tag",
			 * "Error in http connection " +responseBody+ e.toString()); }
			 */

///////////return responseBody;
///////////}

///////////		@Override
///////////protected void onPostExecute(String result) {

			/*
			 * parseResponse(); if(pairs[1].equals("Error in Connection")){
			 * result=pairs[1]; } if(pairs[1].equals("status=0")){
			 * result="Internal Error"; } if(pairs[1].equals("1")){
			 * result="History:"+"\n"; for (int i=2;i<pairs.length;i++){ result=
			 * result+pairs[i]; } if(pairs[1].equals("status=l")){ myintent =
			 * new Intent(HistoryActivity.this,LoginActivity.class);
			 * SharedPreferences appSettings =
			 * getSharedPreferences(LoginActivity.PREFERENCE_FILENAME,
			 * MODE_PRIVATE); session_id="null"; SharedPreferences.Editor
			 * prefEditor = appSettings.edit();
			 * prefEditor.putString("user_session", session_id);
			 * prefEditor.commit(); startActivity(myintent); } }
			 */
			// dialog.dismiss();
			// wv.setVisibility(View.VISIBLE);
			// wv.loadDataWithBaseURL(null, result,"text/html", "utf-8", null);
///////////	}
///////////}

	/**
	 * 
	 */
	private void rechargePost() {
		String[] strResponse1 = null;
		String[] strResponse2 = null;
		String[] testLastValue = null;
		Helpz myHelpz = new Helpz();
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		
		if(pref.getString("user_sessionMOM", "test").equals("MOM"))
			
			{

		
		try {

			HttpClient httpclient = new DefaultHttpClient();

			HttpPost httppostnew = new HttpPost(
					"http://180.179.67.72/nokiaservice/Lastfivetransactions.aspx?UserID="
							+ myHelpz.GetMyUserId());
			final HttpParams httpParams = httpclient.getParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, 15000);
			HttpConnectionParams.setSoTimeout(httpParams, 15000);
			HttpResponse response = httpclient.execute(httppostnew);
			HttpEntity entity = response.getEntity();
			responseBodyhistory = EntityUtils.toString(entity);
			String strResponse = responseBodyhistory;
			String Mama = "";

		

			/*strResponse1 = Split(strResponse, "$$");
			StringBuilder sb = new StringBuilder();
			ArrayList stringValue = new ArrayList();
			int i = 0;
			for (String s : strResponse1) {
				String[] LastValue = Split(s, "~");
				i++;
				//sb.append(i + ")" + "TransDate : "+ LastValue[0].toString() + "," +"\n"+"RequestId : "+ LastValue[1].toString() + "," +"\n"+ "CustomerNo :"+ LastValue[2].toString() + "," +"\n"+"Amount :"+ LastValue[3].toString() + "," +"\n"+"Operator: " + LastValue[4].toString() + "," +"\n"+"Status :"+ LastValue[5].toString());
	if(LastValue[2].toString().contains("|"))		
	{
		
		testLastValue = Split(LastValue[2], "|");
		
		   sb.append(i + ")" + "TransDate : "+ LastValue[0].toString() +"\n" + "Consumer Number :" + testLastValue[0].toString()+"\n"+ "Mobile Number :" + testLastValue[1].toString() + "\n"+ "Userid :" + testLastValue[2].toString() + "\n"+ "TransId : "+ LastValue[1].toString()+"\n"+"Operator: " + LastValue[4].toString() +"\n"+"Amount :"+ LastValue[3].toString()+"\n"+"Status :"+ LastValue[5].toString());			
				
				sb.append("\n");
				sb.append("\n");
	}
	else{
		sb.append(i + ")" + "TransDate : "+ LastValue[0].toString() +"\n"+"CustomerNo :"+ LastValue[2].toString() +"\n"+ "TransId : "+ LastValue[1].toString()+"\n"+"Operator: " + LastValue[4].toString() +"\n"+"Amount :"+ LastValue[3].toString()+"\n"+"Status :"+ LastValue[5].toString());			
		
		sb.append("\n");
		sb.append("\n");
	}
				
			}*/
			
			strResponse1 = Split(strResponse, "|");
			StringBuilder sb = new StringBuilder();
			ArrayList stringValue = new ArrayList();
			int i = 0;
			for (String s : strResponse1) {
				String[] LastValue = Split(s, "~");
				i++;
				//sb.append(i + ")" + "TransDate : "+ LastValue[0].toString() + "," +"\n"+"RequestId : "+ LastValue[1].toString() + "," +"\n"+ "CustomerNo :"+ LastValue[2].toString() + "," +"\n"+"Amount :"+ LastValue[3].toString() + "," +"\n"+"Operator: " + LastValue[4].toString() + "," +"\n"+"Status :"+ LastValue[5].toString());
				sb.append(i + ")" + "TransDate : "+ LastValue[0].toString() +"\n"+"SubscriberID/Mobile Number :"+ LastValue[2].toString() +"\n"+ "TransId : "+ LastValue[1].toString()+"\n"+"Operator: " + LastValue[4].toString() +"\n"+"Amount :"+ LastValue[3].toString()+"\n"+"Status :"+ LastValue[5].toString());			
				sb.append("\n");
				sb.append("\n");
			
			}
			String a = sb.toString();

			
			responsetest.setVisibility(View.VISIBLE);
			responsetest.setText(a);

		/////////	Toast.makeText(HistoryActivity.this, RowCount, Toast.LENGTH_LONG).show();
			Log.i("postData", response.getStatusLine().toString());
			Log.i("postData", responseBodyhistory);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
			}
		
		else if(pref.getString("user_sessionMOM", "test").equals("PBX"))
			
			
		{
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://180.179.67.76/MobAppS/PbxMobApp.ashx");
			try {
				
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
				nameValuePairs.add(new BasicNameValuePair("RN", myHelpz.GetMyLoginMobileNumber()));
			//	nameValuePairs.add(new BasicNameValuePair("RN", "9308743449"));
				nameValuePairs.add(new BasicNameValuePair("Service", "LTH"));
				
				
				httppost.addHeader("ua", "android");
				final HttpParams httpParams = httpclient.getParams();
				HttpConnectionParams.setConnectionTimeout(httpParams, 15000);
				HttpConnectionParams.setSoTimeout(httpParams, 15000);
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				responseBody = EntityUtils.toString(entity);
				String strResponse = responseBody;
			
				if(!strResponse.equals("<string/>"))
				{
					
					
				
				strResponse1 = Split(strResponse, "|");
				StringBuilder sb = new StringBuilder();
				ArrayList stringValue = new ArrayList();
				int i = 0;
				for (String s : strResponse1) {
					String[] LastValue = Split(s, "~");
					i++;
					String newoutput = LastValue[3].toString();
					Double number = Double.valueOf(newoutput);
					DecimalFormat df = new DecimalFormat("#.00");
				    String	newtestString = df.format(number);

					String abc = newtestString;
					sb.append(i + ")" + "TransDate : "+ LastValue[0].toString() +"\n"+"SubscriberID/Mobile Number :"+ LastValue[2].toString() +"\n"+ "TransId : "+ LastValue[1].toString()+"\n"+"Operator: " + LastValue[4].toString() +"\n"+"Amount :"+ newtestString+"\n"+"Status :"+ LastValue[5].toString());			
					
					sb.append("\n");
					sb.append("\n");
				
				}
				String a = sb.toString();
				
				responsetest.setVisibility(View.VISIBLE);
				responsetest.setText(a);
				myintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		
				Log.i("postData", response.getStatusLine().toString());
				Log.i("postData", responseBodyhistory);
			}
				else{
					responsetest.setText("No Transactions Found");
				}
		}
			 catch (Exception e) {
					Log.e("log_tag", "Error in http connection " + e.toString());
					responseBody = "Timeout|Error in Http Connection";
					
				}
			}
		
		
		else{
				Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
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

	private void parseResponse() {
		pairs = this.responseBody.split("[|]");
		for (int i = 0; i < pairs.length; i++) {
			Log.i("parser" + i, pairs[i]);
		}
	}
	
	public boolean SaveSessionData() {
		try {
			Helpz myHelp = new Helpz();
			myHelp.SetLogoutVariable(true);
			return true;
		} catch (Exception ex) {
			return false;
		}
		
		}
//	
//	@Override
//	protected void onPause() {
//	    super.onPause();
//
//	    SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
//	    Editor editor = prefs.edit();
//	    editor.putString("lastActivity", getClass().getName());
//	    editor.commit();
//	}
//	@Override
//	 protected void onDestroy() {
//		super.onDestroy();
//		
//		android.os.Process.killProcess(android.os.Process.myPid());
//	}

	public void refreshHistory(View v) {
		
		this.rechargePost();
	}

//	@Override
//	public void onUserLeaveHint() {
//		this.finish();
//		
//	}
	@Override
	public void onBackPressed() {
		
		myintenttest = new Intent(HistoryActivity.this, MainActivity.class);
		myintenttest.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(myintenttest);
		finish();
		return;
	}


}
