package com.mom.app;



import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
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

public class HomeActivity1 extends Activity implements OnClickListener {
	//private Button reButton;
	private Button passButton;
	private EditText passField;
	private EditText numField_SubscriberId;
	private EditText amountField;
	private EditText CustomerNumber;
	private Button postButton ,backButton ,secondback;
	private Button newButton;
	private String session_id,flag;
	TableLayout tablelayout;
	ProgressDialog dialog;
	TextView tv_secpass;
	ImageView image;
	Spinner operatorSpinner,operatorSpinner1, operatorSpinner2;
	private Spinner spinner1;
	CheckBox responseCheckbox , respcheckbox;
	String[] pairs;
	HashMap<String, String> responseMap = new HashMap<String, String>();
	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	String responseBody;
	String response_status;
	String check;
	TextView responseText , secondback_responseText ,accountbal , responseText1;
	boolean checkbox;
	String Topup="";
	private String response_message;
	String newoutput , output;
	String newoutputrecharge;
	String[] strArrayResponse ;
	
	 int i;
	Intent myintent2=new Intent();
	Intent reintent=new Intent();
	 Helpz myHelpz = new Helpz();
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		////setContentView(R.layout.recharge);
		setContentView(R.layout.home1);
		//SharedPreferences appSettings = getSharedPreferences(LoginActivity.PREFERENCE_FILENAME, MODE_PRIVATE);
		
		init();
		
		
	
		 postButton.setOnClickListener(this);
		 addListenerOnSpinnerItemSelection();
         
		myintent2 = new Intent(HomeActivity1.this,LoginActivity.class);
		reintent = new Intent(HomeActivity1.this,MainActivity.class);
	}

/*	private class GetRechargeTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			
			//	SharedPreferences appSettings = getSharedPreferences(LoginActivity.PREFERENCE_FILENAME, MODE_PRIVATE);
				//sendPost();
				//rechargePost();
		////	rechargecheck();
				
				
			return responseBody;
		}
		@Override
		protected void onPostExecute(String result) {
	
			sendPost();
			
		
		}
	

	}*/
	
	

	private void init() {
		
		this.responseText = (TextView) findViewById(R.id.responseText);
		this.responseText1 = (TextView) findViewById(R.id.responseText1);
		this.secondback_responseText = (TextView) findViewById(R.id.secondresponseText);
		this.passButton = (Button) findViewById(R.id.btn_reclogin);
		this.backButton =(Button)findViewById(R.id.btn_firstback);
		this.newButton = (Button) findViewById(R.id.new_recharge);
		this.passField = (EditText) findViewById(R.id.repass);
		this.numField_SubscriberId = (EditText) findViewById(R.id.number);
		this.postButton = (Button) findViewById(R.id.btn_recharge);
		this.operatorSpinner = (Spinner) findViewById(R.id.Operator);
		this.secondback = (Button)findViewById(R.id.btn_secondback);
	    this.amountField = (EditText) findViewById(R.id.amount);
		this.CustomerNumber = (EditText)findViewById(R.id.CustomerNumber);
		this.accountbal = (TextView) findViewById(R.id.AccountBal);
		this.image= (ImageView) findViewById(R.id.img_resp);
		this.tablelayout = (TableLayout)findViewById(R.id.tableLayout1);
		  getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.appsbg)); 
		 AccountBalPost(); 
		
	SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
     if(pref.getString("user_sessionMOM", "test").equals("MOM"))
			
		{
			passButton.setVisibility(View.VISIBLE);
			
			passField.setVisibility(View.VISIBLE);
			backButton.setVisibility(View.VISIBLE);
		}
		else if(pref.getString("user_sessionMOM", "test").equals("PBX"))
		{
			rechargecheck();
		}
     
		 getAllOperators();
		 
		
	}
	
	private class GetLoginTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			
			
			return responseBody;
		}
		

		@Override
		protected void onPostExecute(String result) {
		 
		
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

				Log.i("postData", response.getStatusLine().toString());
				Log.i("info", responseBody);
				
				InputStream in = new ByteArrayInputStream(responseBody.getBytes("UTF-8"));
			    new XmlPullParsingAccnt(in);	
			 
			} catch (Exception e) {
				Log.e("log_tag", "Error in http connection " + e.toString());
				responseBody= "Timeout|Error in Http Connection";

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
//					DecimalFormat df = new DecimalFormat("#.##");
//					String newtestString = df.format(number);
					///String strDouble = String.format("%.2f", check); 

			//		String abc = strDouble;
					// error="0";
					Log.i("postData", response.getStatusLine().toString());
					Log.i("info", responseBody);

					
					    myHelpz.SetRMNAccountBal(check);
						accountbal.setVisibility(View.VISIBLE);
						accountbal.setText("Bal: Rs." + myHelpz.GetRMNAccountBal());

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
	}
	private void AccountBalPost(){
		 
		 Helpz myHelpz = new Helpz();
		 SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			
			if(pref.getString("user_sessionMOM", "test").equals("MOM"))
				
				{
				accountbal.setVisibility(View.VISIBLE);
				accountbal.setText("Bal: Rs." + myHelpz.GetRMNAccountBal().toString());

				}
			else if(pref.getString("user_sessionMOM", "test").equals("PBX"))
				
				
			{
				accountbal.setVisibility(View.VISIBLE);
				accountbal.setText("Bal: Rs." + myHelpz.GetRMNAccountBal().toString());

			}
			else{
				Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
			}
			
	}
	public class XmlPullParsingAccnt {

		  protected XmlPullParser xmlpullparser1;
		   String output1;
		  String TAG="XmlPullParsing";
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
			    
		            
		            myHelpz.SetRMNAccountBal(newoutputrecharge);
					
					accountbal.setVisibility(View.VISIBLE);
					accountbal.setText("Bal: Rs." + myHelpz.GetRMNAccountBal());
			           
			        break;
		        
		        }
		        

		    }
		       
  }
	
public void addListenerOnSpinnerItemSelection() {
		
		operatorSpinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());
	  }
	
	
	 public String[] getAllOperators() {
	        String[] strResponse1 = null;
	        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			
			if(pref.getString("user_sessionMOM", "test").equals("MOM"))
				
				{
	        
	    
				try{
				 String[] strOperators = new String[] {"Select Service Provider","AIRTEL DIGITAL" , "BIG TV" , "DISH" ,"SUN DIRECT" ,"TATA SKY"
			    		 ,"VIDEOCON DTH"};
				 
				 ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				   			android.R.layout.simple_spinner_item, strOperators);
				   			dataAdapter
				   			.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				   			operatorSpinner.setAdapter(dataAdapter);		
				}
				catch (Exception ex) {
					String[] strResponse = { "NO ITEM TO DISPLAY" };
					strResponse1 = strResponse;
				}
				}
			else if(pref.getString("user_sessionMOM", "test").equals("PBX"))
			{
				 HttpClient httpclient = new DefaultHttpClient();
					HttpPost httppost = new HttpPost("http://180.179.67.76/MobAppS/PbxMobApp.ashx");
				try {

					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
					nameValuePairs.add(new BasicNameValuePair("OT","2"));
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
					
					ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				   			android.R.layout.simple_spinner_item, strResponse1);
				   			dataAdapter
				   			.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				   			operatorSpinner.setAdapter(dataAdapter);	

				} catch (Exception ex) {
					String[] strResponse = { "NO ITEM TO DISPLAY" };
					strResponse1 = strResponse;
				}
			}
			else{
				Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
			}


	        return strResponse1;
	    }

	 
	 private String Get_OperatorID(String strOperatorName) {
		 SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			
			if(pref.getString("user_sessionMOM", "test").equals("MOM"))
				
				{
		
				
				if(strOperatorName.equals("AIRTEL DIGITAL"))
	            {
	            	
	            	
	            	return "21";
	            }
	            else  if(strOperatorName.equals("BIG TV"))
	            {
	            	
	            	return "27";
	            }
	            else  if(strOperatorName.equals("DISH"))
	            {
	            	
	            	return "7";
	            }
	            else  if(strOperatorName.equals("SUN DIRECT"))
	            {
	            
	            	return "14";
	            }
	            else  if(strOperatorName.equals("TATA SKY"))
	            {
	            	
	            	return "24";
	            }
	            else  if(strOperatorName.equals("VIDEOCON DTH"))
	            {
	            	
	            	return "18";
	            }
	            
	            else{
	            	return "0";
	            }
				}
			else 	if(pref.getString("user_sessionMOM", "test").equals("PBX"))
			{
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"http://180.179.67.76/MobAppS/PbxMobApp.ashx");
				try {
					
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
					nameValuePairs.add(new BasicNameValuePair("SN", strOperatorName.toString()));
					nameValuePairs.add(new BasicNameValuePair("Service", "OSN"));
					nameValuePairs.add(new BasicNameValuePair("OT", "2"));
					
					
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
			else{
				return "0";
			}
	    }
	

	 
	 
	 
	public void rechargeLogin(View v) {
		if(this.fillData()){
		
			sendPost();

		}
	}
	
	private int validate() {
		try {
			myHelpz.SetMyRechargeOperator(operatorSpinner.getSelectedItem()
					.toString());
		} catch (Exception ex) {

		}

		String Operatorid = (Get_OperatorID(myHelpz.GetMyRechargeOperator()
				.toString()));
		
		if ((Operatorid.equals("21")) || (Operatorid.equals("24")) || (Operatorid.equals("ADG")) || (Operatorid.equals("TSK"))) {
		if (operatorSpinner.getSelectedItemPosition() < 1) {
			return 1;
		} else if (numField_SubscriberId.getText().toString().length() < 10) {
			return 2;
		} else if (numField_SubscriberId.getText().toString().length() > 10) {
			return 2;
		} else if (amountField.getText().toString().length() == 0) {
			return 3;

		} else if (Integer.parseInt(amountField.getText().toString()) < 100) {
			return 3;
		} else if (Integer.parseInt(amountField.getText().toString()) > 15000) {
			return 3;
		} else if (CustomerNumber.getText().toString().length() < 10) {
			return 4;
		} else {
			return 0;
		}
	}
		else if((Operatorid.equals("7")) || (Operatorid.equals("14")) || (Operatorid.equals("DSH")) || (Operatorid.equals("SUN"))){
			if (operatorSpinner.getSelectedItemPosition() < 1) {
				return 1;
			} else if (numField_SubscriberId.getText().toString().length() < 11) {
				return 2;
			} else if (numField_SubscriberId.getText().toString().length() > 11) {
				return 2;
			}
			else if (amountField.getText().toString().length() == 0) {
				return 3;

			} else if (Integer.parseInt(amountField.getText().toString()) < 10) {
				return 3;
			} else if (Integer.parseInt(amountField.getText().toString()) > 15000) {
				return 3;
			} else if (CustomerNumber.getText().toString().length() < 10) {
				return 4;
			} else {
				return 0;
			}
		}
		
		else if((Operatorid.equals("27")) || (Operatorid.equals("BIG")))
		{
			if (operatorSpinner.getSelectedItemPosition() < 1) {
				return 1;
			} else if (numField_SubscriberId.getText().toString().length() < 12) {
				return 2;
			} else if (numField_SubscriberId.getText().toString().length() > 12) {
				return 2;
			}
			else if (amountField.getText().toString().length() == 0) {
				return 3;

			} else if (Integer.parseInt(amountField.getText().toString()) < 25) {
				return 3;
			} else if (Integer.parseInt(amountField.getText().toString()) > 15000) {
				return 3;
			} else if (CustomerNumber.getText().toString().length() < 10) {
				return 4;
			} else {
				return 0;
			}
		} else if((Operatorid.equals("18")) || (Operatorid.equals("D2H")))
		{
			if (operatorSpinner.getSelectedItemPosition() < 1) {
				return 1;
			} else if (numField_SubscriberId.getText().toString().length() < 1) {
				return 2;
			} else if (numField_SubscriberId.getText().toString().length() > 10) {
				return 2;
			}
			else if (amountField.getText().toString().length() == 0) {
				return 3;

			} else if (Integer.parseInt(amountField.getText().toString()) < 150) {
				return 3;
			} else if (Integer.parseInt(amountField.getText().toString()) > 15000) {
				return 3;
			} else if (CustomerNumber.getText().toString().length() < 10) {
				return 4;
			} else {
				return 0;
			}
		}
		else{
			if (operatorSpinner.getSelectedItemPosition() < 1) {
				return 1;
			} else if (numField_SubscriberId.getText().toString().length() < 1) {
				return 2;
			}  else if (amountField.getText().toString().length() == 0) {
				return 3;

			} else if (CustomerNumber.getText().toString().length() < 10) {
				return 4;
			} else {
				return 0;
			}
			
			
		}
	}
 
	
	 

	
	
	private void rechargecheck() {
		// TODO Auto-generated method stub
		//	if (this.response_status.equals("1")) {
			this.passButton.setVisibility(View.GONE);
			//this.tv_secpass.setVisibility(View.GONE);
			this.passField.setVisibility(View.GONE);
			this.backButton.setVisibility(View.GONE);
			this.responseText.setVisibility(View.GONE);
			this.numField_SubscriberId.setVisibility(View.VISIBLE);
			this.postButton.setVisibility(View.VISIBLE);
			this.operatorSpinner.setVisibility(View.VISIBLE);
			this.secondback.setVisibility(View.VISIBLE);
			this.amountField.setVisibility(View.VISIBLE);
			this.CustomerNumber.setVisibility(View.VISIBLE);
		
		
			
			//this.operatorSpinner1.setVisibility(View.VISIBLE);
			
		//}
		/*if(this.response_status.equals("0")){
			this.responseText.setVisibility(View.VISIBLE);
			responseText.setText("Incorrect Pass/Missing Params");	
		}
		if(this.response_status.equals("l")){
			startActivity(myintent1);
		}*/
	}
	
	private void newrechargecheck(){
		
		this.numField_SubscriberId.setVisibility(View.GONE);
		this.postButton.setVisibility(View.GONE);
		this.operatorSpinner.setVisibility(View.GONE);
		this.amountField.setVisibility(View.GONE);
	
		this.image.setVisibility(View.VISIBLE);
		this.responseText.setVisibility(View.VISIBLE);
		this.newButton.setVisibility(View.VISIBLE);
	
		this.responseText.setText(this.newoutputrecharge);
//////////		Toast.makeText(HomeActivity1.this, this.newoutputrecharge, Toast.LENGTH_SHORT).show();
		
	}

	/*public void rechargeData(View v) {
		//flag="0";
		this.dialog = new ProgressDialog(this);
		this.dialog.setMessage("Connecting..");
		this.dialog.show();
		this.dialog.setCancelable(false);
		GetRechargeTask task = new GetRechargeTask();
		task.execute();
	}*/
	public void newRecharge(View v) {
		this.resetLayout();
	}
	private void resetLayout() {
		AccountBalPost();
		tablelayout.setVisibility(View.VISIBLE);
		this.numField_SubscriberId.setText("");
		this.amountField.setText("");
		this.operatorSpinner.setSelection(0);
		this.CustomerNumber.setText("");
		this.numField_SubscriberId.setVisibility(View.VISIBLE);
		this.postButton.setVisibility(View.VISIBLE);
		this.operatorSpinner.setVisibility(View.VISIBLE);
		this.amountField.setVisibility(View.VISIBLE);
		this.CustomerNumber.setVisibility(View.VISIBLE);
		this.secondback.setVisibility(View.VISIBLE);
		this.newButton.setVisibility(View.GONE);
	    this.responseText.setVisibility(View.GONE);
	    this.responseText1.setVisibility(View.GONE);
	}

	public void backpost(View v) {
		
		//myintent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(reintent);
		this.finish();

	}

	public void Backpost1(View v) {
		
		//myintent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(reintent);
		this.finish();
	}
	private void rechargePost() {
		String[] strResponse1 = null;
		
			 Helpz myHelpez = new Helpz();
		try{
		   
		    myHelpez.SetMyRechargeMobileNumber(numField_SubscriberId.getText().toString());
		    myHelpez.SetMyRechargeAmount(amountField.getText().toString());
		    myHelpez.SetCustomerMobileNumber(CustomerNumber.getText().toString());
            myHelpez.SetMyRechargeOperator(operatorSpinner.getSelectedItem().toString());

            if (responseCheckbox.isChecked()== true ) {
                myHelpez.SetMyRechargeType("0");
            }else {
                myHelpez.SetMyRechargeType("0");
            }
		}
		catch (Exception ex) {
        }
		
SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		
		if(pref.getString("user_sessionMOM", "test").equals("MOM"))
			
			{
		 try {
           
             HttpClient httpclient = new DefaultHttpClient();
    		// HttpPost httppost = new HttpPost("http://msvc.money-on-mobile.net/WebServiceV3Trans.asmx/DoDTHRecharge"); 
    		 HttpPost httppost = new HttpPost("http://msvc.money-on-mobile.net/WebServiceV3Trans.asmx/DoDTHRechargeV2"); 
    		
    		 List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(7);
             nameValuePairs.add(new BasicNameValuePair("strMobileNumber", myHelpez.GetMyRechargeMobileNumber()));
             nameValuePairs.add(new BasicNameValuePair("strRechargeAmount", myHelpez.GetMyRechargeAmount()));
             nameValuePairs.add(new BasicNameValuePair("strOperator", Get_OperatorID(myHelpez.GetMyRechargeOperator()).toString()));
             nameValuePairs.add(new BasicNameValuePair("intCustomerId", myHelpez.GetMyCustomerId()));
             nameValuePairs.add(new BasicNameValuePair("strAccessID", GlobalVariables.AccessId));
             nameValuePairs.add(new BasicNameValuePair("CompanyID", myHelpez.GetMyCompanyId()));
             nameValuePairs.add(new BasicNameValuePair("CustomerNumber", myHelpez.GetCustomerMobileNumber()));
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
    			Log.i("postData",Get_OperatorID(myHelpez.GetMyRechargeOperator()).toString());
    			/*this.numField.setVisibility(View.GONE);
    			this.postButton.setVisibility(View.GONE);
    			this.operatorSpinner.setVisibility(View.GONE);
    			this.amountField.setVisibility(View.GONE);
    			this.responseCheckbox.setVisibility(View.GONE);
    			this.image.setVisibility(View.VISIBLE);
    			this.responseText.setVisibility(View.VISIBLE);
    			this.newButton.setVisibility(View.VISIBLE);
    			image.setImageResource(R.drawable.success);
    			this.responseText.setText(check);
    			Toast.makeText(HomeActivity.this, check, Toast.LENGTH_SHORT).show();*/
    			
   			InputStream in = new ByteArrayInputStream(this.responseBody.getBytes("UTF-8"));
    			 new NewXmlPullParsing(in);	
			 
			   /* this.numField.setVisibility(View.GONE);
				this.postButton.setVisibility(View.GONE);
				this.operatorSpinner.setVisibility(View.GONE);
				this.amountField.setVisibility(View.GONE);
				this.responseCheckbox.setVisibility(View.GONE);
				this.image.setVisibility(View.VISIBLE);
				this.responseText.setVisibility(View.VISIBLE);
				this.newButton.setVisibility(View.VISIBLE);
				image.setImageResource(R.drawable.success);
				this.responseText.setText(check);
				Toast.makeText(HomeActivity.this, check, Toast.LENGTH_SHORT).show();*/

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
				HttpConnectionParams.setConnectionTimeout(httpParams,45000);
				HttpConnectionParams.setSoTimeout(httpParams, 45000);
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
								 + "\n" + "Balance:" + strResponse1[3].toString());
						sb.append("\n");
						sb.append("\n");

						String a = sb.toString();

				    numField_SubscriberId.setVisibility(View.GONE);
		    		postButton.setVisibility(View.GONE);
		    		operatorSpinner.setVisibility(View.GONE);
		    		amountField.setVisibility(View.GONE);
		    		CustomerNumber.setVisibility(View.GONE);
		    		secondback.setVisibility(View.GONE);
		    		tablelayout.setVisibility(View.VISIBLE);
		    	    responseText1.setVisibility(View.VISIBLE);
		    		newButton.setVisibility(View.VISIBLE);
		    	    responseText1.setText(a);
		    	  

				Log.i("postData", response.getStatusLine().toString());
				Log.i("info", responseBody);
				 }	
				 else{
					    numField_SubscriberId.setVisibility(View.GONE);
			    		postButton.setVisibility(View.GONE);
			    		operatorSpinner.setVisibility(View.GONE);
			    		amountField.setVisibility(View.GONE);
			    		CustomerNumber.setVisibility(View.GONE);
			    		secondback.setVisibility(View.GONE);
			    		tablelayout.setVisibility(View.VISIBLE);
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
		this.checkbox=responseCheckbox.isChecked();
		if(checkbox==true){
			Topup="on";
		}
		else{
			Topup="null";
		}
		//nameValuePairs.add(new BasicNameValuePair("session_id", this.session_id));
		nameValuePairs.add(new BasicNameValuePair("customer_num", ((EditText)findViewById(R.id.number)).getText().toString()));
		nameValuePairs.add(new BasicNameValuePair("amount", amountField.getText().toString()));
		nameValuePairs.add(new BasicNameValuePair("operator", operatorSpinner.getSelectedItem().toString()));
		//nameValuePairs.add(new BasicNameValuePair("operatorDTH", operatorSpinner1.getSelectedItem().toString()));
		nameValuePairs.add(new BasicNameValuePair("topup", this.Topup));

	}
	private void sendPost() {
		
		 Helpz myHelpz = new Helpz();
		try {

			HttpClient httpclient = new DefaultHttpClient();
			//HttpPost httppost = new HttpPost("http://msvc.money-on-mobile.net/WebServiceV3Client.asmx/checkVallidTpin?CustomerID=" + myHelpz.GetMyCustomerId() + "&strTPassword=" + passField.getText() + "&CompanyID=" + myHelpz.GetMyCompanyId() + "&strAccessID=" + GlobalVariables.AccessId.toString());
			
			HttpPost httppost = new HttpPost("http://msvc.money-on-mobile.net/WebServiceV3Client.asmx/checkVallidTpin");
			
		            // Add  password
		         
		 
		         EditText pword = (EditText)findViewById(R.id.repass);
		         String password = pword.getText().toString();
		          
		        
		            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
		            nameValuePairs.add(new BasicNameValuePair("CustomerID", myHelpz.GetMyCustomerId() ));
		            nameValuePairs.add(new BasicNameValuePair("strTPassword", password));
		            nameValuePairs.add(new BasicNameValuePair("CompanyID", myHelpz.GetMyCompanyId()));
		            nameValuePairs.add(new BasicNameValuePair("strAccessId", GlobalVariables.AccessId.toString()));
			
			
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
			 InputStream in = new ByteArrayInputStream(this.responseBody.getBytes("UTF-8"));
			 
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
		            String newoutput = output;
		            String[] strArrayResponse = Split(newoutput, "~");
		            int i = Integer.parseInt(strArrayResponse[0].toString());
		            switch ( Integer.parseInt(strArrayResponse[0].toString()) )
			          {
			            case 101:
			            	
			           	//Toast.makeText(MainActivity.this, "Sucessful Login", Toast.LENGTH_SHORT).show();
			//////////            	Toast.makeText(HomeActivity1.this, strArrayResponse[1].toString(), Toast.LENGTH_LONG).show();
			            	
			            	//startActivity(myintent1);
			            	rechargecheck();
			            	myintent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			            	//login();
			            	
		          //  Log.i("valuee==============================",""+output);
			        break;
		        default:
                
		     //////////   	Toast.makeText(HomeActivity1.this, strArrayResponse[1].toString(), Toast.LENGTH_LONG).show();
		        	responseText.setVisibility(View.VISIBLE);
		        	responseText.setText( strArrayResponse[1].toString());
		        	passField.setText("");
                
		            break;
		        }
		        

		    }
		       
        }
		    
		  
	 }
	 
	 
	 
	 public class NewXmlPullParsing {

		  protected XmlPullParser xmlpullparser;
		   String outputrecharge;
		  String TAG="XmlPullParsing";
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

		    void parseTag(int event){

		        switch (event) {

		        case XmlPullParser.START_DOCUMENT:
		            Log.i(TAG,"START_DOCUMENT");
		            break;

		        case XmlPullParser.END_DOCUMENT:
		            Log.i(TAG,"END_DOCUMENT");
		            break;
		        case XmlPullParser.START_TAG: 
		            Log.i(TAG,"START_TAG"+xmlpullparser.getName());
		            Log.i(TAG,"Attribute Name"+xmlpullparser.getAttributeValue(null,"category"));

		            break;

		        case XmlPullParser.END_TAG: 
		            Log.i(TAG,"END_TAG"+xmlpullparser.getName());

		            break;

		        case XmlPullParser.TEXT:
		            Log.i(TAG,"TEXT");
		            outputrecharge = xmlpullparser.getText();
		            String newoutputrecharge = outputrecharge;
		            			                
			/////////        Toast.makeText(HomeActivity1.this, newoutputrecharge, Toast.LENGTH_LONG).show();
			            	
			            	//newrechargecheck();
			        numField_SubscriberId.setVisibility(View.GONE);
		    		postButton.setVisibility(View.GONE);
		    		operatorSpinner.setVisibility(View.GONE);
		    		amountField.setVisibility(View.GONE);
		    		CustomerNumber.setVisibility(View.GONE);
		    		secondback.setVisibility(View.GONE);
		    		tablelayout.setVisibility(View.VISIBLE);
		    	    responseText1.setVisibility(View.VISIBLE);
		    		newButton.setVisibility(View.VISIBLE);
		    	    responseText1.setText(newoutputrecharge);
		    	  
			            	
		     
			        break;
		       
		        }
		        

		    }
		       
       }
		    
		  
	
	private boolean fillData() {
		// TODO Auto-generated method stub

		if(passField.getText().toString().trim().equals("")){
			this.responseText.setVisibility(View.VISIBLE);
			responseText.setText("Please enter your Password");
			return false;
		}
		else{
			nameValuePairs.add(new BasicNameValuePair("recharge_key", passField.getText().toString()));
			//nameValuePairs.add(new BasicNameValuePair("session_id", this.session_id));
			return true;
		}
	}
	private void parseResponse() {
		// TODO Auto-generated method stub
		this.pairs=this.responseBody.split("[|]");
		for (int i=0;i<pairs.length;i++){
			Log.i("postData", pairs[i]);
			String[] temp=pairs[i].split("=");
			if(temp.length==2){
				this.responseMap.put(temp[0].trim(), temp[1].trim());
			}
		}
	}
	private void setResponse() {
		// TODO Auto-generated method stub
		if (this.responseMap.containsKey("status")){
			this.response_status = this.responseMap.get("status");
			Log.i("postData", this.response_status);}
		else
			this.response_status = "2";

		if (this.responseMap.containsKey("message"))
			this.response_message = this.responseMap.get("message");
		else
			this.response_message = "Error in logging";
	}
//	@Override
//	public void onUserLeaveHint (){
//		this.finish();
//	}
	@Override
	public void onBackPressed() {
		startActivity(reintent);
		reintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		this.finish();
		return;
	}

	@Override
	public void onClick(View view) {
		 if (validate() == 0) {
			 secondback_responseText.setText(" ");
		//AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeActivity1.this);
			 AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeActivity1.this);
		 
        // Setting Dialog Title
			 alertDialog.setTitle("Confirm DTHRecharge...");
 
        // Setting Dialog Message
			 alertDialog.setMessage("SubscriberId:" + " " + numField_SubscriberId.getText().toString() + "\n" + "Operator:" + " " +  operatorSpinner.getSelectedItem().toString() +"\n" + "Amount:" + " " + "Rs." + " " + amountField.getText().toString() +"\n" + "Mobile Number:" + " " + CustomerNumber.getText().toString());
 
        // Setting Icon to Dialog
       //// alertDialog.setIcon(R.drawable.delete);
 
        // Setting Positive "Yes" Button
			 alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
            	((AlertDialog)dialog).getButton(AlertDialog.BUTTON1).setEnabled(false);
            	rechargePost();
                new GetLoginTask().onPostExecute("test");
            // Write your code here to invoke YES event
           
            
            }
        });
 
        // Setting Negative "NO" Button
			 alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            // Write your code here to invoke NO event
           
            dialog.cancel();
            
            }
        });
 
        // Showing Alert Message
			 alertDialog.show();
			
		
		// TODO Auto-generated method stub
		
	}
		 else {
             switch (validate()) {
                 case 1:
                	 secondback_responseText.setVisibility(View.VISIBLE);
                	 secondback_responseText.setText("Select Service Provider");
                	 operatorSpinner.setSelection(0);
                     numField_SubscriberId.setText("");
                     amountField.setText("");
                     CustomerNumber.setText("");
                     
                     break;

                 case 2:
                	 secondback_responseText.setVisibility(View.VISIBLE);
                	 secondback_responseText.setText("Invalid SubscriberId");
                	 numField_SubscriberId.setText("");
                     break;

                 case 3:
                	 secondback_responseText.setVisibility(View.VISIBLE);
                	 secondback_responseText.setText("Invalid Amount");
                     amountField.setText("");

                     break;
                 case 4:
                	 secondback_responseText.setVisibility(View.VISIBLE);
                	 secondback_responseText.setText("Invalid MobileNumber");
                	 CustomerNumber.setText("");
                     break;
             }
         }
		 
	 }
	
	 public class CustomOnItemSelectedListener implements OnItemSelectedListener {
		  
		  public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
			  numField_SubscriberId.setText("");
			  amountField.setText("");
			  CustomerNumber.setText("");
			  
		  }
			 
		  @Override
		  public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
		  }
		  }
	
}
