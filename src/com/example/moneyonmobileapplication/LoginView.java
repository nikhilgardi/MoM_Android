package com.example.moneyonmobileapplication;

import org.mswipe.sdk.MSwiperController;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class LoginView extends Activity {
	
	TextView mTxtUsername = null;
	TextView mTxtPassword = null;
	TextView responseText = null;
	TextView responseText1 = null;
	TextView responseText2 = null;
	CheckBox mChkRememberMe = null;
	ProgressDialog progressdialog  =null;	
	Intent reintent = new Intent();
	
  
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_activity);

        initViews();
        
    }

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	
	private void initViews() 
	{
		//mTxtUsername = (TextView) findViewById(R.id.login_TXT_merchantid);
		mTxtPassword = (TextView) findViewById(R.id.login_TXT_merchantpassword);
		responseText =(TextView)findViewById(R.id.responseText);
		responseText1 =(TextView)findViewById(R.id.responseText1);
		responseText2 =(TextView)findViewById(R.id.responseText2);
	  	
		//mTxtPassword.setText("123456");
		//mTxtUsername.setText("9769634306");
		
		//mTxtPassword.setText("mswipe");
		//mTxtUsername.setText("9702338976");
		
        Button btnLogin = (Button) findViewById(R.id.login_BTN_signin);
 		btnLogin.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
		
				
			    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	            //imm.hideSoftInputFromWindow(tabHost.getApplicationWindowToken(), 0);
	            imm.hideSoftInputFromWindow(LoginView.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	            merchantAuthentication();
				
	
			}
		});
 		
      	Button btnBack = (Button) findViewById(R.id.login_BTN_back);
      	btnBack.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				reintent = new Intent(LoginView.this, MSwipeAndroidSDKListActivity1.class);
				startActivity(reintent);
			   finish();
	
			}
		});
		
		
	}	
  
	public void merchantAuthentication()
	{
		Helpz myHelpez = new Helpz();
		//String userId = mTxtUsername.getText().toString(); 
		String password = mTxtPassword.getText().toString();
		    
		  MSwiperController swiperController = new MSwiperController(this);
		//  String[] results = swiperController.userAuthentication(loginHandler, myHelpez.GetMyRechargeMobileNumber(),password);
	String[] results = swiperController.userAuthentication(loginHandler, "9702338976",password);
		
		  if(results[0].startsWith("error")){
			  //System.out.println("Error in calling the login method " + results[1]);
			  ConstantSample.showDialog(this, "Login API", results[1]);
			  Log.i("postData", results[1]);
			  
			  Toast.makeText(LoginView.this, results[1], Toast.LENGTH_LONG).show();
			  
		  }else{
			progressdialog = ProgressDialog.show(this, "","Please wait...", true);
			progressdialog.show();
		  }
				
		
	}

	public Handler loginHandler = new Handler() 
    {
		public void handleMessage(android.os.Message msg) 
		{
			progressdialog.dismiss();
			Bundle bundle = msg.getData();
			String[] results = bundle.getStringArray("HttpResponse");
			//for (int ictr = 0; ictr < results.length; ictr++) {
			//	System.out.println( results[ictr]);
			//}
			if(!results[0].startsWith("error"))
			{	
			
				
				System.out.println( results[1]);
				
				Log.i("postData", results[1]);
			  //  Toast.makeText(LoginView.this, results[1], Toast.LENGTH_LONG).show();
				 
				int iposS=results[1].indexOf("</NotFirstTimeLogin>");
				char[] strArray = results[1].toCharArray();
				String extrsStr = "";
				iposS--;
				while(strArray[iposS] !='>' && iposS >=0)
				{
					extrsStr = strArray[iposS]  + extrsStr;
					iposS--;
				}
				ConstantSample.NotFirstTimeLogin = extrsStr;
				System.out.println("nor first time login  is " + ConstantSample.NotFirstTimeLogin);
				
				
				Log.i("postData1", results[1]);
			   // Toast.makeText(LoginView.this, extrsStr, Toast.LENGTH_LONG).show();
                responseText.setVisibility(View.VISIBLE);
                responseText.setText("Login Successful" + "\n" + "NotFirstTimeLogin : " + extrsStr);
			    
			    						
				iposS= results[1].indexOf("</ReferenceId>");
				extrsStr="";
				iposS--;
				while(strArray[iposS] !='>' && iposS >=0)
				{
					extrsStr = strArray[iposS]  + extrsStr;
					iposS--;
				}
				
				ConstantSample.mReferenceId = extrsStr;
				System.out.println("reference id is " + ConstantSample.mReferenceId);
				
				Log.i("postData2", results[1]);
			  //  Toast.makeText(LoginView.this, extrsStr, Toast.LENGTH_LONG).show();
			    
			    responseText2.setVisibility(View.VISIBLE);
                responseText2.setText("Reference id is : " + extrsStr);
			    
			    
			    
			    
			    iposS=results[1].indexOf("</FirstName>");
				char[] strArrayName = results[1].toCharArray();
				 extrsStr = "";
				iposS--;
				while(strArray[iposS] !='>' && iposS >=0)
				{
					extrsStr = strArray[iposS]  + extrsStr;
					iposS--;
				}
				
				
				ConstantSample.mFirstName = extrsStr;
				System.out.println("First name is " + ConstantSample.mReferenceId);
				//Toast.makeText(LoginView.this, extrsStr, Toast.LENGTH_LONG).show();
				responseText1.setVisibility(View.VISIBLE);
                responseText1.setText("First name is : " + extrsStr);
				
				
				
				ConstantSample.mPassword = mTxtPassword.getText().toString();
				if(ConstantSample.NotFirstTimeLogin.equals("false"))
				{
					ConstantSample.showDialog(LoginView.this, "Login API", "Your loggin in for first time,have to change the password");
				}else{
					//ConstantSample.showDialog(LoginView.this, "Login API", results[1]);
					
				}
			}else{
				ConstantSample.showDialog(LoginView.this, "Login API", results[1]);
			}

			//get the reference id this has to be sent to all the other api
			
		}
	};
	
	@Override
	public void onUserLeaveHint() {
		this.finish();
		
	}
public void onBackPressed() {
		
		reintent = new Intent(LoginView.this, MSwipeAndroidSDKListActivity1.class);
		startActivity(reintent);
		return;
	}

}
