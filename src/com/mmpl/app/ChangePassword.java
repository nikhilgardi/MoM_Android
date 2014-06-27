package com.mmpl.app;

import org.mswipe.sdk.MSwiperController;
 


import com.mmpl.app.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ChangePassword extends Activity 
{
	ProgressDialog progressdialog  =null;	

	//fields for credit sale screen
    EditText mTxtPassword=null;
    EditText mTxtNewPassword=null;
    EditText mTxtReTypeNewPassword=null;
	    Intent reintent = new Intent();
    
	/** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
    	setContentView(R.layout.changepassword);
		initViews();

	
    }
	
	@Override
	public void onResume()
	{

		super.onResume();
		
	}

	private void initViews() 
	{
		((TextView)findViewById(R.id.topbar_LBL_heading)).setText("Change Password");
		mTxtPassword = (EditText) findViewById(R.id.changepassword_TXT_password);
		mTxtNewPassword = (EditText) findViewById(R.id.changepassword_TXT_newpassword);
		mTxtReTypeNewPassword = (EditText) findViewById(R.id.changepassword_TXT_retypepassword);
		
		Button submit = (Button) findViewById(R.id.changepassword_BTN_submit);
		submit.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
			    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			    //imm.hideSoftInputFromWindow(tabHost.getApplicationWindowToken(), 0);
			    imm.hideSoftInputFromWindow(ChangePassword.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			    checkPassword();
				
			}
		});
/*
		Button menu= (Button) findViewById(R.id.changepassword_BTN_menu);
		menu.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				finish();
				
			}
		});
*/
		  Button btnBack = (Button) findViewById(R.id.changepassword_BTN_back);
	      	btnBack.setOnClickListener(new OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					reintent = new Intent(ChangePassword.this, MSwipeAndroidSDKListActivity1.class);
					startActivity(reintent);
				   finish();
		
				}
			});
			
	}
	
	public void checkPassword()
	{
		String aReferenceId = ConstantSample.mReferenceId; 
		String password = mTxtPassword.getText().toString();
		String newpassword = mTxtNewPassword.getText().toString();
		
		    
		  MSwiperController swiperController = new MSwiperController(this);
		  String[] results = swiperController.changeUserPassword(changePasswordHandler, aReferenceId,password,newpassword);
			
		  if(results[0].startsWith("error")){
				
			  //System.out.println("Error in calling the login method " + results[1]);
			  ConstantSample.showDialog(this, "Change Passowrd API", results[1]);
			  mTxtPassword.setText("");
			  mTxtNewPassword.setText("");
			  mTxtReTypeNewPassword.setText("");
			  
		  }else{
			progressdialog = ProgressDialog.show(this, "","Please wait...", true);
			progressdialog.show();
		  }

		
	}
	
	
	public Handler changePasswordHandler = new Handler() 
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
				
				ConstantSample.mPassword = mTxtPassword.getText().toString();
				ConstantSample.showDialog(ChangePassword.this, "Login API", results[1]);
				  mTxtPassword.setText("");
				  mTxtNewPassword.setText("");
				  mTxtReTypeNewPassword.setText("");
				  
			}else{
				ConstantSample.showDialog(ChangePassword.this, "Login API", results[1]);
				  mTxtPassword.setText("");
				  mTxtNewPassword.setText("");
				  mTxtReTypeNewPassword.setText("");
				  
			}
			
		}
	};
//	
//	@Override
//	public void onUserLeaveHint() {
//		this.finish();
//	}

public void onBackPressed() {
		
	reintent = new Intent(ChangePassword.this , MSwipeAndroidSDKListActivity1.class);
	reintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
	startActivity(reintent);
	finish();
		
	} 
}
