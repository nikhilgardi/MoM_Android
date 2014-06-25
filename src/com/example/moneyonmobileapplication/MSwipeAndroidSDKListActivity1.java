package com.example.moneyonmobileapplication;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
	import android.app.ListActivity;
	import android.content.Intent;
	import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
	import android.view.View;
	import android.widget.AdapterView;
	import android.widget.AdapterView.OnItemClickListener;
	import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


	public class MSwipeAndroidSDKListActivity1 extends Activity {
		Intent myintenttest = new Intent();
		private TextView response;
		String output ,check ,responseBody;
		/** Called when the activity is first created. */
		@Override
	    public void onCreate(Bundle savedInstanceState) 
	    {
	        super.onCreate(savedInstanceState);
	    	setContentView(R.layout.mswipersdklist1);
	    	this.response = (TextView) findViewById(R.id.textView1);
			initViews();
			
		
	    }
		
		@Override
		public void onDestroy() {
			super.onDestroy();
			}
		
		private void AccountBalPost(){
			 
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
//					error="0";
					Log.i("postData", response.getStatusLine().toString());
					Log.i("info", responseBody);
					
					InputStream in = new ByteArrayInputStream(responseBody.getBytes("UTF-8"));
				    new XmlPullParsing(in);	
				 
				} catch (Exception e) {
					Log.e("log_tag", "Error in http connection " + e.toString());
					responseBody= "Timeout|Error in Http Connection";
//					error="1";
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
				    
				         response.setVisibility(View.VISIBLE);
				         response.setText("Bal: Rs. " + newoutputrecharge);
				         //response.setText("Bal: Rs. 100000000");
				        
				           
				        break;
			        
			        }
			        

			    }
			       
	    }
		
		// -----------------------------------------------------------------------
		// Private classes
		// -----------------------------------------------------------------------
		
		private void initViews() 
		{
			AccountBalPost();
			ListView listView = (ListView) findViewById(R.id.mswipelist);
			String[] values = new String[] { "Login", "CreditSale", "Signature","Last Transaction Status", "Change password"};
			
			////String[] values = new String[] { "CreditSale API", "Signature API","Last Transaction Status", "Change password"};	
			
			
			
			// First paramenter - Conte
			// Second parameter - Layout for the row
			// Third parameter - ID of the View to which the data is written
			// Forth - the Array of data
	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.row_layout, R.id.label , values);

	
			// Assign adapter to ListView
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
			
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) 
				{
					if(arg2 == 0)
					{
						Intent intent =new Intent(MSwipeAndroidSDKListActivity1.this, LoginView.class);
						startActivity(intent);
					}else if (arg2 == 1){
									
						if(ConstantSample.mReferenceId.length()>0)
						{	
							Intent intent = new Intent(MSwipeAndroidSDKListActivity1.this, CreditSaleView.class);
							startActivity(intent);
						}else{
							ConstantSample.showDialog(MSwipeAndroidSDKListActivity1.this, "Login API", "Please login first");
						}
					}else if (arg2 == 2){
						
						Intent intent = new Intent(MSwipeAndroidSDKListActivity1.this, CreditSaleSignatureView.class);
				////////		
						/*
						ConstantSample.mReferenceId = "9100000288";
						ConstantSample.mPassword = "mswipe";
						
						ConstantSample.mStandId = "18751";
						ConstantSample.mRRNO= "000058628946";			
						ConstantSample.mAuthCode = "048110";
						ConstantSample.mDateTime= "29 Nov 2012";

						ConstantSample.mLast4Digits = "3456";
						ConstantSample.mAmount= "5678";
						*/
						
						
						if(ConstantSample.NotFirstTimeLogin.equals("false"))
						{
							ConstantSample.showDialog(MSwipeAndroidSDKListActivity1.this, "Login API", "You loggin in for first time have to change the password");
						}else{
							if(ConstantSample.mReferenceId.length()>0)
							{	

								intent.putExtra("mStandId", ConstantSample.mStandId);
								intent.putExtra("rrno", ConstantSample.mRRNO);			
								intent.putExtra("authCode", ConstantSample.mAuthCode);
								intent.putExtra("date", ConstantSample.mDateTime);
			
								intent.putExtra("lstFrDgts",ConstantSample.mLast4Digits );
								intent.putExtra("amt", ConstantSample.mAmount);
			
								startActivity(intent);
							}else{
								ConstantSample.showDialog(MSwipeAndroidSDKListActivity1.this, "Login API", "Please login first");
							}
						}
				}else if (arg2 == 3){
						
						Intent intent = new Intent(MSwipeAndroidSDKListActivity1.this, LastTrxStatus.class);
						
						
					if(ConstantSample.NotFirstTimeLogin.equals("false"))
						{
							ConstantSample.showDialog(MSwipeAndroidSDKListActivity1.this, "Login API", "You loggin in for first time have to change the password");
						}else{
							if(ConstantSample.mReferenceId.length()>0)
							{	

								startActivity(intent);
							}else{
								ConstantSample.showDialog(MSwipeAndroidSDKListActivity1.this, "Login API", "Please login first");
							}

						}
					
				}else if (arg2 == 4){
					
						Intent intent = new Intent(MSwipeAndroidSDKListActivity1.this, ChangePassword.class);
						
						if(ConstantSample.mReferenceId.length()>0)
						{	

							startActivity(intent);
						}else{
							ConstantSample.showDialog(MSwipeAndroidSDKListActivity1.this, "Login API", "Please login first");
						}

				}
						
				}
			});
		}
		
		
		
		public void onBackPressed() {
			
			myintenttest = new Intent(MSwipeAndroidSDKListActivity1.this, MainActivity1.class);
			startActivity(myintenttest);
			return;
			
		}
		
		@Override
		public void onUserLeaveHint() {
			this.finish();
		}
		/*	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
		{
		    if ((keyCode == KeyEvent.KEYCODE_BACK))
		    {
		        finish();
		    }
		    return super.onKeyDown(keyCode, event);
		}*/
	}

