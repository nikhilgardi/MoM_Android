
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

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity1 extends ListActivity {
	private String responseBody;
	private String session_id;
	private TextView responsetxt;
    
	Intent myintent = new Intent();
	Intent myintent1 = new Intent();
	Intent myintent2 = new Intent();
	Intent myintent3 = new Intent();
	Intent myintent4 = new Intent();
	Intent myintent5 = new Intent();
	Intent myintent6 = new Intent();
	Intent intentMain = new Intent();
	Intent swipeintent = new Intent();
	
	WebView wv;
	ListView lv;
	Button save,logout,relogout,passchng,repasschng,back;
	EditText op,np,np1;
	TextView res;
	String url , check , output;
	String error="2";
	String flag="2";
	HashMap<String, String> responseMap = new HashMap<String, String>();
	String pairs[];
	ProgressDialog dialog;
	Intent myintentlogin=new Intent();
	Intent reintent=new Intent();
	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	ArrayList<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>(2);
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//String[] values = new String[] { "My Info", "Change Recharge Password", "Change Password", "Recharge Logout","Logout"};
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		if(pref.getString("user_sessionMOM", "test").equals("MOM"))
			
		{
		String[] values = new String[] { "Mobile Recharge", "DTH Recharge","Bill Payment","Card Sale","History","Settings"};
		setContentView(R.layout.activity_main1);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.row_layout,R.id.label, values);
		setListAdapter(adapter);
		}
		else{
			String[] values = new String[] { "Mobile Recharge", "DTH Recharge","Bill Payment","Utility Bill Payment","History","Settings"};
			setContentView(R.layout.activity_main1);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.row_layout,R.id.label, values);
			setListAdapter(adapter);
		}
		
		wv = (WebView) findViewById(R.id.resp);
		lv = (ListView) findViewById(android.R.id.list);
		
		this.res = (TextView) findViewById(R.id.response);

		this.back =(Button) findViewById(R.id.button6);
		
		 intentMain =new Intent(this,MainActivity1.class);

		this.responsetxt = (TextView) findViewById(R.id.textView1);
		SaveSessionData();
		rechargePost();
	}
	
	
	private void rechargePost(){
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
				HttpPost httppost = new HttpPost("http://180.179.67.76/MobAppS/PbxMobApp.ashx");
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

				this.responsetxt.setVisibility(View.VISIBLE);
				this.responsetxt.setText("Bal: Rs." + check);

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
			    
			         responsetxt.setVisibility(View.VISIBLE);
			         responsetxt.setText("Bal: Rs. " + newoutputrecharge);
			         //response.setText("Bal: Rs. 100000000");
			        
			           
			        break;
		        
		        }
		        

		    }
		       
    }
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		String item = (String) getListAdapter().getItem(position);	    
	
	if(item.equals("Mobile Recharge")){
		myintent = new Intent(MainActivity1.this,HomeActivity.class);
		myintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(myintent);
		finish();
	}
	if(item.equals("DTH Recharge")){
		myintent1 = new Intent(MainActivity1.this,HomeActivity1.class);
		myintent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(myintent1);
		finish();
	}
	if(item.equals("Bill Payment")){
		myintent2 = new Intent(MainActivity1.this,HomeActivity2.class);
		myintent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(myintent2);
		finish();
	}
	if(item.equals("Card Sale")){
		
		swipeintent = new Intent(MainActivity1.this,MSwipeAndroidSDKListActivity1.class);
		swipeintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(swipeintent);		
		finish();
	}	
	
 /*  if(item.equals("Aadhar")){
		
		swipeintent = new Intent(MainActivity1.this,AadharListActivity.class);
		startActivity(swipeintent);		
		
	}	*/
	if(item.equals("Utility Bill Payment")){
		myintent6 = new Intent(MainActivity1.this,HomeBillActivity_PBX.class);
		myintent6.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(myintent6);
		finish();
	}
	
	if(item.equals("History")){
		
		myintent4 = new Intent(MainActivity1.this,HistoryActivity.class);
		myintent4.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(myintent4);
		finish();
	}

		
	if(item.equals("Settings")){
		
		
		myintent5 = new Intent(MainActivity1.this,InfoActivity.class);
		myintent5.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(myintent5);
		finish();
		
		
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
	
	public void logout(View v) {
		Helpz myhelp = new Helpz();
		//SharedPreferences appSettings = getSharedPreferences(LoginActivity.PREFERENCE_FILENAME, MODE_PRIVATE);
		//session_id = appSettings.getString("user_session","-1");
		//url="https://recharge123.com/mobileapp/logout.php";
	//	this.sendpost(url);
	//	SharedPreferences.Editor prefEditor = appSettings.edit();
	//	prefEditor.putString("user_session","null");  
	//	prefEditor.commit();
	    this.finish();
	    myhelp.SetLogoutVariable(false);
		startActivity(myintentlogin);
		//session_id = appSettings.getString("user_session","-1");
		//Log.i("logout",session_id );
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		}
//	@Override
//	public void onUserLeaveHint() {
//		//super.onUserLeaveHint();
////		Intent intent =new Intent(this,MainActivity1.class);
////		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////		startActivity(intent);
//        this.finish();
//		
//	}
	
	
	/*public void onBackPressed() {
		
		this.finish();
		
	}*/
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event)
//	{
//	    if ((keyCode == KeyEvent.KEYCODE_BACK))
//	    {
//	    	
//	        return true;
//	        
//	    }
//	    return super.onKeyDown(keyCode, event);
//	   
//	}
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
	if ((keyCode == KeyEvent.KEYCODE_BACK)) {
        System.out.println("KEYCODE_BACK");
        showDialog("BACK");
        return true;
    }
	return false;
	}
	
	void showDialog(String the_key){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity1.this);
		alertDialog.setMessage("You have pressed the BACK  button. Would you like to exit the app?")
              .setCancelable(true)
               .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   
                       dialog.cancel();
                        Helpz myhelp = new Helpz();
                        myhelp.SetLogoutVariable(false);
                        intentMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
               		    finish();
                   }
               })
               .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                   }
               });
        AlertDialog alert = alertDialog.create();
        alert.setTitle("Message");
        alert.show();
    }
	
	/*public boolean onKeyDown(int keyCode, KeyEvent event)  {
	    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.ECLAIR
	        && keyCode == KeyEvent.KEYCODE_BACK
	    && event.getRepeatCount() == 0) 
	    {
	        onBackPressed();
	    }
	    return super.onKeyDown(keyCode, event);
	}
	 
	@Override
	public void onBackPressed() {
	    // do what you want here
		finish();
	    return;
	}*/
}
	
