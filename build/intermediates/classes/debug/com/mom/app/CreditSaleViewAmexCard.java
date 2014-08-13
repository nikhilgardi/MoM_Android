package com.mom.app;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

public class CreditSaleViewAmexCard extends Activity 
{
	public EditText mTxtSecurity = null;
	Intent reintent = new Intent();
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
    	setContentView(R.layout.creditsaleamexcard);
    	
		initViews();
    }
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	

	
	private void initViews() 
	{
		mTxtSecurity = (EditText) findViewById(R.id.creditsale_TXT_amexsecuritykey);
	

        Button btnCardDetailsNext = (Button)findViewById(R.id.creditsale_BTN_creditdetails_next);
        btnCardDetailsNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
			 	
	           if(mTxtSecurity.getText().toString().length() <4)
                {

   					ConstantSample.showDialog(CreditSaleViewAmexCard.this, "Amex Card", "Invalid Amex Card security code.");
                    mTxtSecurity.requestFocus();
                    
                }else{
        		    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    	            imm.hideSoftInputFromWindow(CreditSaleViewAmexCard.this.getCurrentFocus().getWindowToken(), 
    	            		InputMethodManager.HIDE_NOT_ALWAYS);


					Intent returnIntent = new Intent();
			    	returnIntent.putExtra("action", 2);
			    	returnIntent.putExtra("securitycode", mTxtSecurity.getText().toString());
	
			    	setResult(RESULT_OK,returnIntent);    	
			    	finish();
                }

			}
		} );

		
	}	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) 
		{
		 	Intent returnIntent = new Intent();
	    	returnIntent.putExtra("action", 0);
	    	returnIntent.putExtra("securitycode", mTxtSecurity.getText().toString());

	    	setResult(RESULT_OK,returnIntent);    	
	    	finish();

		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	public void onUserLeaveHint() {
		this.finish();
	}
	public void onBackPressed() {
		
		reintent = new Intent(CreditSaleViewAmexCard.this , MSwipeAndroidSDKListActivity1.class);
		startActivity(reintent);
			
		} 
}
