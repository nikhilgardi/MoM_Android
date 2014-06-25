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

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class InfoActivity extends ListActivity {
	private String responseBody;
	private String session_id;
	WebView wv;
	ListView lv;
	Button save,logout,relogout,mswipe,passchng,repasschng,back;
	EditText op,np,np1;
	TextView res,accntbalresponse;
	String url , check , output;
	String error="2";
	String flag="2";
	HashMap<String, String> responseMap = new HashMap<String, String>();
	String pairs[];
	ProgressDialog dialog;
	TableLayout tablelayout;
	Intent myintentlogin=new Intent();
	Intent reintent=new Intent();
	Intent myintenttest = new Intent();
	Intent swipeintent = new Intent();
	Intent mpinintent = new Intent();
	Intent tpinintent = new Intent();
	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	ArrayList<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>(2);
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//String[] values = new String[] { "My Info", "Change Recharge Password", "Change Password", "Recharge Logout","Logout"};
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		if(pref.getString("user_sessionMOM", "test").equals("MOM"))
			
		{
		String[] values = new String[] { "Account Balance", "Change M-Pin","Change T-Pin","Logout"};
		setContentView(R.layout.myinfo);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.row_layout,R.id.label, values);
		setListAdapter(adapter);
		}
		else if(pref.getString("user_sessionMOM", "test").equals("PBX"))
		{
			String[] values = new String[] {"Change Password","Logout"};
			setContentView(R.layout.myinfo);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.row_layout,R.id.label, values);
			setListAdapter(adapter);
		}
		
		wv = (WebView) findViewById(R.id.resp);
		lv = (ListView) findViewById(android.R.id.list);
		this.res = (TextView) findViewById(R.id.response);
		this.tablelayout = (TableLayout)findViewById(R.id.tableLayout1);
		//this.op = (EditText) findViewById(R.id.editText1);
		//this.np = (EditText) findViewById(R.id.editText2);
		//this.np1 = (EditText) findViewById(R.id.editText3);
		//this.save =(Button) findViewById(R.id.button4);
		//this.logout =(Button) findViewById(R.id.button5);
	//	this.relogout =(Button) findViewById(R.id.button3);
	//	this.passchng =(Button) findViewById(R.id.button1);
	//	this.repasschng =(Button) findViewById(R.id.button2);
		this.back =(Button) findViewById(R.id.button6);
		accntbalresponse = (TextView) findViewById(R.id.textViewaccntbal);
		//url= "https://recharge123.com/mobileapp/myinfo.php";
		myintentlogin = new Intent(InfoActivity.this,LoginActivity.class);
		reintent = new Intent(InfoActivity.this,TabLayoutActivity.class);
		swipeintent = new Intent(InfoActivity.this,LoginActivity.class);
		AccountBalPost();
		SaveSessionData();
//		this.dialog = new ProgressDialog(this);
//		this.dialog.setMessage("Fetching Info");
//		this.dialog.show();
//		this.dialog.setCancelable(false);
//		GetInfoTask task = new GetInfoTask();
//		task.execute();
	}
	
	public void logout(View v) {
		Helpz myhelp = new Helpz();
		
	    this.finish();
	    myhelp.SetLogoutVariable(false);
		startActivity(myintentlogin);
		
		
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
			    new AccntBalXmlPullParsing(in);	
			 
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
	public class AccntBalXmlPullParsing {

		  protected XmlPullParser xmlpullparser1;
		   String output1;
		  String TAG="XmlPullParsing";
		    public AccntBalXmlPullParsing(InputStream is) {
		    	  

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
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		String item = (String) getListAdapter().getItem(position);
		
		if(item.equals("Account Balance")){
			
			
			rechargePost();
			SaveSessionData(); 
			
			this.back.setVisibility(View.VISIBLE);
			lv.setVisibility(View.GONE);
			
		}
		
		
//		if(item.equals("MSwipe")){
//			swipeintent = new Intent(InfoActivity.this,MSwipeAndroidSDKListActivity.class);
//			startActivity(swipeintent);
//		}
		
//		if(item.equals("Recharge Logout")){
//			
//			
//			
//			LogoutTask task2= new LogoutTask();
//			task2.execute();
//			
//			
//			
//
//		}
		if(item.equals("Logout")){
			
			
			
			LogoutTask1 task3= new LogoutTask1();
			task3.execute();
			
			
	
		}
		
		if (item.equals("Change M-Pin")) {

			mpinintent = new Intent(InfoActivity.this, MPinActivity.class);
			startActivity(mpinintent);

		}
		
		if (item.equals("Change Password")) {

			mpinintent = new Intent(InfoActivity.this, ChangePasswordPBXActivity.class);
			startActivity(mpinintent);

		}

		if (item.equals("Change T-Pin")) {

			tpinintent = new Intent(InfoActivity.this, TPinActivity.class);
			startActivity(tpinintent);

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
	public void rechargeLogout(View v) {
	
		
		this.finish();
		
		startActivity(reintent);

		
		
		
	}
	
	
	/*public void mswipe(View v) {
		////	SharedPreferences appSettings = getSharedPreferences(LoginActivity.PREFERENCE_FILENAME, MODE_PRIVATE);
		////	session_id = appSettings.getString("user_session","-1");
		////	url="https://recharge123.com/mobileapp/logout_recharge.php";
		////	this.sendpost(url);
			
			this.finish();
			
			startActivity(swipeintent);
		////	Log.i("logout",session_id );
			
			
			
		}*/
	
	
	
//	public void passPost(View v) {
//		
//		this.dialog = new ProgressDialog(this);
//		this.dialog.setMessage("Posting");
//		this.dialog.show();
//		this.dialog.setCancelable(false);
//		PostTask task1 = new PostTask();
//		task1.execute();
//	}
	public void back(View v) {
		res.setText("");
		////this.op.setText("");
		////this.np.setText("");
		////this.np1.setText("");
		resetlayout();
	}
	private void setlayout() {
		// TODO Auto-generated method stub
		this.wv.setVisibility(View.GONE);
		
		this.back.setVisibility(View.VISIBLE);
		this.op.setVisibility(View.VISIBLE);
		this.np.setVisibility(View.VISIBLE);
		this.np1.setVisibility(View.VISIBLE);
		this.save.setVisibility(View.VISIBLE);
		this.logout.setVisibility(View.GONE);
		this.relogout.setVisibility(View.GONE);
		this.res.setVisibility(View.VISIBLE);
		this.passchng.setVisibility(View.GONE);
		this.repasschng.setVisibility(View.GONE);
	}
	

	/*private class GetInfoTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			//SharedPreferences appSettings = getSharedPreferences(LoginActivity.PREFERENCE_FILENAME, MODE_PRIVATE);
			//session_id = appSettings.getString("user_session","-1");
			//responseBody="";
			rechargePost();
			return responseBody;
		}*/
		
		

		
		 
		
	////	@Override
		////protected void onPostExecute(String result) {
			/*parseResponse();
			result= pairs[2];
			dialog.dismiss();
			wv.setVisibility(View.VISIBLE);
			lv.setVisibility(View.GONE);
			back.setVisibility(View.VISIBLE);
			passchng.setVisibility(View.GONE);
			repasschng.setVisibility(View.GONE);
			logout.setVisibility(View.GONE);
			relogout.setVisibility(View.GONE);
			wv.loadDataWithBaseURL(null, result,"text/html", "utf-8", null);*/
		////}
	////}
	
	private void rechargePost(){
		 
		 Helpz myHelpz = new Helpz();
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
			 tablelayout.setVisibility(View.VISIBLE);
			         res.setVisibility(View.VISIBLE);
			         res.setText("Your Current Balance is Rs" + newoutputrecharge);
			        
			           
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
	
	
	private class LogoutTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			
			
			return error;
		}

		@Override
		protected void onPostExecute(String result) {
			
			
			rechargeLogout(relogout);
		}
	}
	
	
	private class LogoutTask1 extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			
		////	logout(logout);			
			return error;
		}

		@Override
		protected void onPostExecute(String result) {
			logout(logout);	
		}
	}
	
	
	
	
	
	
	
	
	

	private void resetlayout() {
		
		this.res.setVisibility(View.GONE);
		this.back.setVisibility(View.GONE);
		tablelayout.setVisibility(View.VISIBLE);
		lv.setVisibility(View.VISIBLE);

	}
	
	
public void onBackPressed() {
		
		myintenttest = new Intent(InfoActivity.this, MainActivity1.class);
		myintenttest.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(myintenttest);
		
		return;
	}

//@Override
//public void onUserLeaveHint() {
//	this.finish();
//}


}