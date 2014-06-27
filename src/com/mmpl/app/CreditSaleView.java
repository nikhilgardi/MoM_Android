package com.mmpl.app;



import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
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
import org.mswipe.sdk.MSwiperController;
import org.mswipe.sdk.Controller.Constants;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Xml;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bbpos.swiper.SwiperController;
import com.bbpos.swiper.SwiperController.DecodeResult;
import com.bbpos.swiper.SwiperController.SwiperControllerState;
import com.bbpos.swiper.SwiperController.SwiperStateChangedListener;
import com.mmpl.app.R;




public class CreditSaleView extends Activity 
{
	private final static String INTENT_ACTION_CALL_STATE = "com.bbpos.swiper.CALL_STATE";
	private SwiperController swiperController;
	private SwiperStateChangedListener stateChangedListener;
	
	private IncomingCallServiceReceiver incomingCallServiceReceiver;
	ProgressDialog progressdialog  =null;
	Intent reintent = new Intent();
	
	String mAmexSecurityCode = "";
	
    int mCurrentScreen=0;
    //fields for card sale screen
    EditText mTxtCreditAmount=null;
    EditText mTxtLast4Digits=null;
    EditText mTxtPhoneNum=null;
    EditText mTxtEmail=null;
    //EditText mTxtNotes=null;

    //fields for card details
    String mSno="";
    String mCreditCardNo="";
    String mCardHolderName="";
    String mExpiryDate="";
    String mPaddingInfo="";
    String mEncTrackData="";
       
    //for the swiper menu
    TextView lblAmtMsg = null;
    TextView txtProgMsg = null;
 
    Button mBtnSwipe = null; 
    Button mBtnSwipeOk = null;
    
       
    ViewFlipper mViewFlipper = null;
    
    String responseBody,output;
    
	/** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
    	setContentView(R.layout.creditsale);
		initViews();
		
		startCallStateService();
    	
		stateChangedListener = new StateChangedListener();
		swiperController = new SwiperController(this.getApplicationContext(), stateChangedListener);
		//swiperController.setDetectDeviceChange(true); // default: false
		//swiperController.setChargeUpTime(1.0); 		// default: 1.2s
		swiperController.setSwipeTimeout(-1);  			// no Timeout
		swiperController.setFskRequired(false); 		// default: true
		
       
    }
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(swiperController!=null)
			swiperController.deleteSwiper();
		endCallStateService();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			System.out.println("In the back key capture funcation the mCurrent screen is " + mCurrentScreen);
			if(mCurrentScreen==0 || mCurrentScreen ==1)
			{
				if(swiperController!=null)
					swiperController.deleteSwiper();
				endCallStateService();
				if(mCurrentScreen == 0)
				{
					//finish();
					reintent = new Intent(CreditSaleView.this, MSwipeAndroidSDKListActivity1.class);
					startActivity(reintent);
					
				}else{
					mViewFlipper.showPrevious();
					mCurrentScreen =0;

				}
			}else{
				if(mCurrentScreen==2) // on the card details
				{	
					mViewFlipper.showPrevious();
					mViewFlipper.showPrevious();
					mCurrentScreen =0;
				
				
				}else{
					mViewFlipper.showPrevious();
					mCurrentScreen --;
				
				}
			}
			//onBackPressed();
			return true;
		}else{
			return super.onKeyDown(keyCode, event);
		}
		
	}
	
	
	private void initViews() 
	{
		mViewFlipper = (ViewFlipper) findViewById(R.id.creditsale_VFL_content);
		((TextView)findViewById(R.id.topbar_LBL_heading)).setText("Card Sale");
		
		
//The actions are for the amount details		
        mTxtEmail = (EditText) findViewById(R.id.creditsale_TXT_email);
        mTxtPhoneNum = (EditText)findViewById(R.id.creditsale_TXT_mobileno);
        mTxtCreditAmount= (EditText) findViewById(R.id.creditsale_TXT_amount);
		mTxtLast4Digits= (EditText) findViewById(R.id.creditsale_TXT_cardfourdigits);
		
		//mTxtLast4Digits.setText("3456");
		//mTxtEmail.setText("");
		//mTxtPhoneNum.setText("9885135489");
		
		Button btnNext = (Button) findViewById(R.id.creditsale_BTN_amt_next);
		btnNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				///*
				lblAmtMsg.setText("Total amount of this transaction \nis Rs. "+ mTxtCreditAmount.getText().toString());
        		mCurrentScreen = mCurrentScreen +1;
            	mViewFlipper.showNext();
				//*/
				
				/*
                mSno = "10741011200005";
                mSno = "10710308120423";
                
                System.out.println("The serial no is "+ mSno);
                //mCreditCardNo="1234567890123456";
                mCreditCardNo="3434567890123456"; // for amex card
                mCardHolderName="Manish Patel";
                mExpiryDate="0617";
                mPaddingInfo="12345";
                mEncTrackData="EEA35AA88062E17B7030E7718ECDF6F9BF2266058F52EF274877E1BD178AE5BB4877E1BD178AE5BB";
                mCurrentScreen = mCurrentScreen +1;
                mViewFlipper.showNext(); // this should be disabled 
                showCreditDetailsScreen();
                */
                    
			}
		});
//The actions are for the Swiper		
		//lblProgressMsg = (TextView) findViewById(R.id.creditsale_LBL_lblprogmsg);
		//lblRecordingPause =(TextView) findViewById(R.id.creditsale_LBL_lblrecordpause);
		lblAmtMsg = (TextView) findViewById(R.id.creditsale_LBL_swipe_amtmsg);
		
		txtProgMsg = (TextView) findViewById(R.id.creditsale_EDT_swipe_progmsg);
		mBtnSwipeOk = (Button) findViewById(R.id.creditsale_BTN_swipe_ok);
        
        mBtnSwipe = (Button) findViewById(R.id.creditsale_BTN_swipe);
        mBtnSwipe.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) 
			{
		        Button btnSwipe = (Button) arg0;
	        	mBtnSwipe.setVisibility(View.INVISIBLE);
	        	//txtProgMsg.setVisibility(View.VISIBLE);
	        	txtProgMsg.setTextColor(Color.BLUE);
	        	txtProgMsg.setText("Please wait initializing the swiper");
	        	mBtnSwipeOk.setVisibility(View.VISIBLE);
	        	swiperController.startSwiper();

				
			}
		});
 
        mBtnSwipeOk = (Button) findViewById(R.id.creditsale_BTN_swipe_ok);
        mBtnSwipeOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) 
			{
		        Button btnSwipe = (Button) arg0;
	        	try{
	        		
	        	}catch(Exception ex){
	        		swiperController.stopSwiper();
	        	}
	        	
	        	mBtnSwipe.setVisibility(View.VISIBLE);
	        	//txtProgMsg.setVisibility(View.GONE);
	        	txtProgMsg.setText("");
	        	mBtnSwipeOk.setVisibility(View.INVISIBLE);
	        	try{
	        		swiperController.stopSwiper();
	        	}catch(Exception ex){}
				
			}
		});
        
//the actions for submitting the card sale details 
        Button btnSubmitCardDetails = (Button) findViewById(R.id.creditsale_BTN_carddetails_submit);
        btnSubmitCardDetails.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				processCardTransaction();
			}
		});
        
        
        Button btnBack = (Button) findViewById(R.id.login_BTN_back);
      	btnBack.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				reintent = new Intent(CreditSaleView.this, MSwipeAndroidSDKListActivity1.class);
				reintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(reintent);
			   finish();
	
			}
		});
		
		
	}
//test//
	
	public void processCardTransaction()
	{
		String referenceId = ConstantSample.mReferenceId;
		String password = ConstantSample.mPassword;
		
		double aAmount = Double.parseDouble(mTxtCreditAmount.getText().toString());
		
		String aCardExpDate = mExpiryDate;
		
		String aCardLstFrDgts = "";
        int ilen=mCreditCardNo.length();
        if(ilen>=4)
        	aCardLstFrDgts=mCreditCardNo.substring(ilen-4, ilen);
        else
        	aCardLstFrDgts=mCreditCardNo;

       if(mAmexSecurityCode.length()>0)
    	   aCardLstFrDgts = aCardLstFrDgts + "|" + mAmexSecurityCode;

		String aCardFrstFrDgts = "";
       if(ilen>=4)
    	   aCardFrstFrDgts=mCreditCardNo.substring(0, 4);
       else
    	   aCardFrstFrDgts=mCreditCardNo;
		
        String aSwiperSrNo = mSno;
		String aCardHldrNm = mCardHolderName;
		String aPaddingInfo = mPaddingInfo;
		String aEncTrckData = mEncTrackData;
		
		String aMerchantBillNo = "";
		String aCardHldrMob = mTxtPhoneNum.getText().toString();
		String aCardHldrEmail = mTxtEmail.getText().toString();
		String aNotes = "";

		System.out.println(referenceId +  " " + password+  " " +  aSwiperSrNo +  " " + 
				aMerchantBillNo +  " " +  aCardHldrNm +  " " + aCardLstFrDgts+  " " + 
				aCardExpDate+  " " +  aPaddingInfo+  " " +  aEncTrckData+  " " +  aAmount+  " " + 
				aCardHldrMob+  " " +  aCardHldrEmail+  " " +  aNotes+  " " +  aCardFrstFrDgts);
		MSwiperController swiperController = new MSwiperController(CreditSaleView.this);
		String[] results = swiperController.processCreditSale(creditsaleHandler,
				referenceId, password, aSwiperSrNo,
				aMerchantBillNo, aCardHldrNm, aCardLstFrDgts,
				aCardExpDate, aPaddingInfo, aEncTrckData, aAmount,
				aCardHldrMob, aCardHldrEmail, aNotes, aCardFrstFrDgts);
		
		  if(results[0].startsWith("error")){
				
				
			  ConstantSample.showDialog(this, "Card Sale", "Error in calling the card sale method " + results[1]);
		  }else{
				progressdialog = ProgressDialog.show(this, "","Please wait...", true);
				progressdialog.show();
		  }

	}
	
	public Handler creditsaleHandler = new Handler() 
    {
		 public volatile boolean parsingComplete = true;
		public void handleMessage(android.os.Message msg) 
		{
			progressdialog.dismiss();
			Bundle bundle = msg.getData();
			String[] results = bundle.getStringArray("HttpResponse");
			for (int ictr = 0; ictr < results.length; ictr++) {
				System.out.println( results[ictr]);
			}
			
			if(!results[0].startsWith("error"))
			{	
				
				/*String message = null;
    			XmlPullParser parser = Xml.newPullParser();
    			try 
    			{
    				parser.setInput(new StringReader(results[1]));
    				boolean done = false;
    				int eventType = parser.getEventType();
    				while ( !done) 
    				{
    					switch (eventType) 
    					{
    						case XmlPullParser.START_TAG:
    							String name = parser.getName();
    							if ("stanId".equalsIgnoreCase(name)) 
    								ConstantSample.mStandId = parser.nextText();
    							else if ("RRNO".equalsIgnoreCase(name)) 	
    								ConstantSample.mRRNO = parser.nextText();
    							else if ("AuthCode".equalsIgnoreCase(name)) 	
    								ConstantSample.mAuthCode = parser.nextText();
    							else if ("ReceiptDate".equalsIgnoreCase(name)) 	
    								ConstantSample.mDateTime = parser.nextText();
    							Toast.makeText(CreditSaleView.this,ConstantSample.mDateTime , Toast.LENGTH_LONG).show();
    							
    	                        break;
    						case XmlPullParser.END_DOCUMENT: 
    							done = true;
    							break;
    						}
    					if(eventType != XmlPullParser.END_DOCUMENT)
    						eventType = parser.next();
    				}
    				LoadTransferPost();
    			} catch (Exception e) {
    				e.printStackTrace(); // ignore the error messages
    			} */
				
				
				String message = null;
				String text = null;
    			XmlPullParser parser = Xml.newPullParser();
    			try 
    			{
    				parser.setInput(new StringReader(results[1]));
    				boolean done = false;
    				int eventType = parser.getEventType();
    				 while (eventType != XmlPullParser.END_DOCUMENT) {
    			            String name=parser.getName();
    			            switch (eventType){
    			               case XmlPullParser.START_TAG:
    			            	   if ("stanId".equalsIgnoreCase(name)) 
       								ConstantSample.mStandId = parser.nextText();
       							else if ("RRNO".equalsIgnoreCase(name)) 	
       								ConstantSample.mRRNO = parser.nextText();
       							else if ("AuthCode".equalsIgnoreCase(name)) 	
       								ConstantSample.mAuthCode = parser.nextText();
       							else if ("ReceiptDate".equalsIgnoreCase(name)) 	
       								ConstantSample.mDateTime = parser.nextText();
    			               break;
    			               
    			               case XmlPullParser.TEXT:
    			               text = parser.getText();
    			            
    			               break;

    			               case XmlPullParser.END_TAG:
    			                  if(name.equals("displayMsg")){
    			                	  message = text;
    			                	 
    			                	AlertDialog.Builder builder = new AlertDialog.Builder(CreditSaleView.this);
    			      				builder.setTitle("CardSale API");
    			      				builder.setMessage(message);
    			      			    builder.setNegativeButton("ok", new DialogInterface.OnClickListener() {
    			      					
    			      					@Override
    			      					public void onClick(DialogInterface dialog, int which) {
    			      						// TODO Auto-generated method stub
    			      						reintent = new Intent(CreditSaleView.this, MSwipeAndroidSDKListActivity1.class);
    			      						startActivity(reintent);
    			      						dialog.dismiss();
    			      				        finish();
    			      					}
    			      				});
    			      				builder.setCancelable(false);
    			      				builder.create().show();
    			                	  
    			              
    			                	  
    			                  }
    			                  
    			                  else{
    			                	  
    			                	  
    			                  }
    			                  break;
    			                  }		 
    			                  eventType = parser.next(); 

    			              }
    			                 parsingComplete = false;
    				LoadTransferPost();
    			} catch (Exception e) {
    				e.printStackTrace(); // ignore the error messages
    			} 
				ConstantSample.mAmount = mTxtCreditAmount.getText().toString();
				
				String aCardLstFrDgts = "";
		        int ilen=mCreditCardNo.length();
		        if(ilen>=4)
		        	aCardLstFrDgts=mCreditCardNo.substring(ilen-4, ilen);
		        else
		        	aCardLstFrDgts=mCreditCardNo;
				ConstantSample.mLast4Digits = aCardLstFrDgts;
				
				
			}
			else{
				AlertDialog.Builder builder = new AlertDialog.Builder(CreditSaleView.this);
				builder.setTitle("CardSale API");
				builder.setMessage(results[1]);
				builder.setMessage(results[1]);
			    builder.setNegativeButton("ok", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						reintent = new Intent(CreditSaleView.this, MSwipeAndroidSDKListActivity1.class);
						startActivity(reintent);
						dialog.dismiss();
				        finish();
					}
				});
				builder.setCancelable(false);
				builder.create().show();

			}
		/*	AlertDialog.Builder builder = new AlertDialog.Builder(CreditSaleView.this);
			builder.setTitle("CardSale API");
			builder.setMessage(results[1]);
			builder.setMessage(results[1]);
		    builder.setNegativeButton("ok", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					reintent = new Intent(CreditSaleView.this, MSwipeAndroidSDKListActivity1.class);
					startActivity(reintent);
					dialog.dismiss();
			        finish();
				}
			});
			builder.setCancelable(false);
			builder.create().show();*/

			
		}
	};
	
	
	
	

	private void LoadTransferPost(){
		 
		 Helpz myHelpz = new Helpz();
			try {
			 	 
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(6);
	            nameValuePairs.add(new BasicNameValuePair("payer","182971"));
	            nameValuePairs.add(new BasicNameValuePair("Payee",myHelpz.GetMyRechargeMobileNumber() ));
	           	nameValuePairs.add(new BasicNameValuePair("TransferAmount", mTxtCreditAmount.getText().toString()));
	            nameValuePairs.add(new BasicNameValuePair("Transvaltype", "Refilled"));  
				nameValuePairs.add(new BasicNameValuePair("strAccessID",GlobalVariables.AccessId));
				nameValuePairs.add(new BasicNameValuePair("CompanyID", myHelpz.GetMyCompanyId()));
				HttpClient httpclient = new DefaultHttpClient();
				
				HttpPost httppost = new HttpPost("http://msvc.money-on-mobile.net/WebServiceV3Trans.asmx/doBalanceTransfer");
				httppost.addHeader("ua", "android");
				final HttpParams httpParams = httpclient.getParams();
				HttpConnectionParams.setConnectionTimeout(httpParams, 15000);
				HttpConnectionParams.setSoTimeout(httpParams, 15000);
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				responseBody = EntityUtils.toString(entity);
			String	 check = responseBody;
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
			//////		responseText.setVisibility(View.VISIBLE);
			///////		responseText.setText(strArrayResponse[0].toString());
										
						Toast.makeText(CreditSaleView.this,strArrayResponse[1].toString(), Toast.LENGTH_SHORT).show();
					
					
					break;
				default:

		///////			responseText.setVisibility(View.VISIBLE);
		///////			responseText.setText(strArrayResponse[1].toString());
					Toast.makeText(CreditSaleView.this,strArrayResponse[1].toString(), Toast.LENGTH_SHORT).show();
					

					break;
				}

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

	
	public void showCreditDetailsScreen()
	{
		 String tempString="";
         int ilen=mCreditCardNo.length();
         if(ilen>=4)
             tempString=mCreditCardNo.substring(ilen-4, ilen);
         else
             tempString=mCreditCardNo;
         

        	 
             String tempFirst2Digits="";
             if(ilen>=2)
                 tempFirst2Digits=mCreditCardNo.substring(0, 2);
             else
                 tempFirst2Digits=mCreditCardNo;
             System.out.println("The first two digits are " + tempFirst2Digits);
             mAmexSecurityCode = "";
             if(tempFirst2Digits.equals("34") || tempFirst2Digits.equals("37"))
             {
                 System.out.println("The first two digits are " + tempFirst2Digits);
                 
                 Intent intent = new Intent(this,CreditSaleViewAmexCard.class);
                 startActivityForResult(intent, 1);

                 
             }else{
            	 
	        	 ((TextView)findViewById(R.id.creditsale_LBL_CardHolderName)).setText("  "+ mCardHolderName);
	        	 String tempString1="";
	             int ilen1=mCreditCardNo.length();
	             if(ilen1>=4)
	                 tempString1=mCreditCardNo.substring(ilen1-4, ilen1);
	             else
	                 tempString1=mCreditCardNo;
	 
	        	 ((TextView)findViewById(R.id.creditsale_LBL_CardNo)).setText("  " + "************"+tempString1);
	        	 ((TextView)findViewById(R.id.creditsale_LBL_ExpiryDate)).setText("  " + mExpiryDate);
	         	 ((TextView)findViewById(R.id.creditsale_LBL_AmtRs)).setText("  Rs." + mTxtCreditAmount.getText().toString());
	             mCurrentScreen = mCurrentScreen +1;
	        	 mViewFlipper.showNext();
             }
         

	}
	
	// When the Intention has been fulfilled Android will notify us through this   
    // method  
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{  
		
		switch(requestCode) {
	    	case 1: 
	            if (resultCode == RESULT_OK) {
	                int action = data.getIntExtra("action",0);
	                mAmexSecurityCode = data.getStringExtra("securitycode");
	                System.out.println("The Amex security code is " + mAmexSecurityCode);
	                System.out.println("the action code is " + action);
	                if(action == 0) // when back in pressed on amex screen action is set to 0
	                {
	                	mViewFlipper.showPrevious();
	                	mCurrentScreen =0;
	                	System.out.println("moving to the previous screen");
	                } else if (action ==2){ //on pressing next on amex screen action is set to 2
	                	
	                 mViewFlipper.showNext();
	                 mCurrentScreen ++;
	   	        	 ((TextView)findViewById(R.id.creditsale_LBL_CardHolderName)).setText(mCardHolderName);
		        	 String tempString1="";
		             int ilen1=mCreditCardNo.length();
		             if(ilen1>=4)
		                 tempString1=mCreditCardNo.substring(ilen1-4, ilen1);
		             else
		                 tempString1=mCreditCardNo;
		 
		        	 ((TextView)findViewById(R.id.creditsale_LBL_CardNo)).setText("************"+tempString1);
		        	 
		        	 ((TextView)findViewById(R.id.creditsale_LBL_ExpiryDate)).setText(mExpiryDate);
		        	 
		     
		         	 ((TextView)findViewById(R.id.creditsale_LBL_AmtRs)).setText(mTxtCreditAmount.getText().toString());

	                }
	                break;
	            }
		}    
	}  

	
	// -----------------------------------------------------------------------
	// Swiper API
	// -----------------------------------------------------------------------
	
	private class StateChangedListener implements SwiperStateChangedListener {

		@Override
		public void onCardSwipeDetected() {
			//outMsg.setText("Reading card data...");
		}
		
		@Override
		public void onDecodeCompleted(String encTrack, String ksn, String formatID,
				String maskedPAN, String cardHolderName, String expDate, String partialTrack) {
			
			/*outMsg.setText("Decode Completed");
			outMsg2.setText(encTrack + "\n"  + ksn + "\n" + formatID + "\n" +
							maskedPAN + "\n" + cardHolderName + "\n" + 
							expDate + "\n" + partialTrack);
							*/
	        mSno=ksn;
	        mCreditCardNo=maskedPAN.trim();
	        mPaddingInfo=partialTrack;
	        mEncTrackData=encTrack;
	        mCardHolderName=cardHolderName;

	        
		    if(mCardHolderName.length() <=0){
	            mCardHolderName = "XXXX-XXXX";
			}else{
				int pos = mCardHolderName.indexOf("^");
				if(pos>0)
				{
					try{
						mCardHolderName = mCardHolderName.substring(0, pos);
					}catch(Exception ex){
						
					}
				}
				
			}
              
                
	        String tempString=expDate.trim();
	        if(tempString.length()==5)
	        {
	            mExpiryDate=tempString.substring(3,5);
	            mExpiryDate=mExpiryDate+tempString.substring(0,2);
	            
	        }else if(tempString.length()==4){
	            mExpiryDate=tempString.substring(2,4);
	            mExpiryDate=mExpiryDate+tempString.substring(0,2);
	        }else{
	            mExpiryDate=tempString;
	        }

	        //switchToScreen(mCurrentScreen+1);
	        showCreditDetailsScreen();
			
		}

		@Override
		public void onDecodeError(DecodeResult decodeResult) {
			String msg;
			if (decodeResult == DecodeResult.DECODE_SWIPE_FAIL) {
				msg = ("Bad Swipe");
			} else if (decodeResult == DecodeResult.DECODE_CRC_ERROR) {
				msg = ("CRC Error");
			} else if (decodeResult == DecodeResult.DECODE_COMM_ERROR) {
				msg = ("Communication Error");
			} else {
				msg = ("Unknown Decode Error");
			}
	
			
			txtProgMsg.setTextColor(Color.RED);
			txtProgMsg.setText(msg );
			mBtnSwipe.setVisibility(View.VISIBLE);
			mBtnSwipeOk.setVisibility(View.INVISIBLE);

		}

		@Override
		public void onError(String message) {
			//outMsg.setText(message);
			//lblProgressMsg.setText(message);
	        //lblRecordingPause.setText("Please try again");
	        //mBtnSwipe.setText("Swipe");
			
			txtProgMsg.setTextColor(Color.RED);
			if(message.startsWith("Record"))
				message = "No swipe detected,";
			txtProgMsg.setText(message + "\nplease try again.");

			mBtnSwipe.setVisibility(View.VISIBLE);
			mBtnSwipeOk.setVisibility(View.INVISIBLE);


		}

		@Override
		public void onGetKsnCompleted(String ksn) {
			//outMsg.setText("ksn: " + ksn); // + " [" + end + "ms]");
		}
		
		@Override
		public void onInterrupted() {
			
			//outMsg.setText("Interrupted");
			//lblProgressMsg.setText("Interrupted");
	        //lblRecordingPause.setText("Please try again");
	        //mBtnSwipe.setText("Swipe");
			txtProgMsg.setTextColor(Color.RED);
			txtProgMsg.setText("Interrupted, please try again.");
			mBtnSwipe.setVisibility(View.VISIBLE);
			mBtnSwipeOk.setVisibility(View.INVISIBLE);


		}

		@Override
		public void onNoDeviceDetected() {
			//outMsg.setText("Swiper Not Detected");
			//lblProgressMsg.setText("Swiper Not Detected");
	        //lblRecordingPause.setText("Please try again");
	        //mBtnSwipe.setText("Swipe");
			txtProgMsg.setTextColor(Color.RED);
			txtProgMsg.setText("Swiper not detected,\nplease plug in the swiper.");
			mBtnSwipe.setVisibility(View.VISIBLE);
			mBtnSwipeOk.setVisibility(View.INVISIBLE);


		}		

		@Override
		public void onTimeout() {
			//outMsg.setText("Timeout");
			//lblProgressMsg.setText("Timeout");
	        //lblRecordingPause.setText("Please try again");
	        //mBtnSwipe.setText("Swipe");
			txtProgMsg.setTextColor(Color.RED);
			txtProgMsg.setText("Timeout, please try again.");
			mBtnSwipe.setVisibility(View.VISIBLE);
			mBtnSwipeOk.setVisibility(View.INVISIBLE);

			
		}

		@Override
		public void onWaitingForCardSwipe() {
			//outMsg.setText("Waiting card swipe...");
			//lblProgressMsg.setText("Please swipe the card");
	        //lblRecordingPause.setText("");

			txtProgMsg.setText("Please swipe the card");

		}
		
		@Override
		public void onWaitingForDevice() {
			//outMsg.setText("Waiting for device...");
			
		}

		@Override
		public void onDecodingStart() {
			//outMsg.setText("Decoding card data...");
			//lblProgressMsg.setText("Please wait");
	        //lblRecordingPause.setText("Processing Swipe");

			txtProgMsg.setText("Please wait,\nprocessing swipe.");


		}

		@Override
		public void onDevicePlugged() {
			//outMsg.setText("Swiper plugged");
		}

		@Override
		public void onDeviceUnplugged() {
			//outMsg.setText("Swiper unplugged!");
			txtProgMsg.setTextColor(Color.RED);
			txtProgMsg.setText("Swiper unplugged!");
			mBtnSwipe.setVisibility(View.VISIBLE);
			mBtnSwipeOk.setVisibility(View.INVISIBLE);

			
		}

		@Override
		public void onSwiperHere(boolean arg0) {
			// TODO Auto-generated method stub
			
		}



	}	


	private void startCallStateService() {
		startService(new Intent(INTENT_ACTION_CALL_STATE));
		if (incomingCallServiceReceiver == null) {
			incomingCallServiceReceiver = new IncomingCallServiceReceiver();
			IntentFilter intentFilter = new IntentFilter();
			intentFilter.addAction(SwiperCallStateService.INTENT_ACTION_INCOMING_CALL);
			this.registerReceiver(incomingCallServiceReceiver, intentFilter);
		}
	}
	
	private void endCallStateService() {
		stopService(new Intent(INTENT_ACTION_CALL_STATE));
		if (incomingCallServiceReceiver != null) {
			this.unregisterReceiver(incomingCallServiceReceiver);
			incomingCallServiceReceiver = null;
		}
	}
	
	// -----------------------------------------------------------------------
	// Inner classes
	// -----------------------------------------------------------------------
	
	private class IncomingCallServiceReceiver extends BroadcastReceiver {		
		@Override
		public void onReceive(Context context, Intent intent) {
			
			if (intent.getAction().equals(SwiperCallStateService.INTENT_ACTION_INCOMING_CALL)) {
				
				try {
					if (swiperController.getSwiperState() != SwiperControllerState.STATE_IDLE) {
						//mBtnSwipe.setText("Swipe");
						if(mBtnSwipeOk.isShown())
						{
							swiperController.stopSwiper();

							mBtnSwipe.setVisibility(View.VISIBLE);
						
							mBtnSwipeOk.setVisibility(View.INVISIBLE);
							//txtProgMsg.setVisibility(View.GONE);
							txtProgMsg.setText("");
						}
					}
				}
				catch (IllegalStateException ex) {
					ex.printStackTrace();
				}
			}			
		} // end-of onReceive		
	}
	
//	@Override
//	public void onUserLeaveHint() {
//		this.finish();
//	}
	/*public void onBackPressed() {
		
		reintent = new Intent(CreditSaleView.this, MSwipeAndroidSDKListActivity1.class);
		startActivity(reintent);
		
	}*/
	

}


