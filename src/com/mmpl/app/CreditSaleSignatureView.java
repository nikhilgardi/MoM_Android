package com.mmpl.app;

import java.io.StringReader;
import java.io.ByteArrayOutputStream;
import java.util.Hashtable;

import org.mswipe.sdk.MSwiperController;
import org.mswipe.sdk.Controller.DESede_BC;
import org.xmlpull.v1.XmlPullParser;

import com.mmpl.app.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Xml;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CreditSaleSignatureView extends Activity 
{	
	ProgressDialog progressdialog  =null;
	SignatureView signatureView =null;
	Intent reintent = new Intent();
	
 	String authCode = "AUTH NO: 2332D2";
 	String rrno = "RR NO: 000058628946";
 	String date = "DATE: 12 Feb 2001  AMT:121111.00";
 	String amt = "";
 	String lstFrDgts= "LAST FOUR DIGITS: XXXXXXXXXXXX3456";

	String mStandId  ="";
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creditsalesingnature);
        //this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Intent intent = getIntent();
    	
        mStandId = getIntent().getStringExtra("mStandId");
     	authCode = getIntent().getStringExtra("authCode");
     	rrno = getIntent().getStringExtra("rrno");
     	date = getIntent().getStringExtra("date");
     	lstFrDgts= getIntent().getStringExtra("lstFrDgts");
     	amt = getIntent().getStringExtra("amt");
     	
        initViews();
      
     }
    
    public void initViews()
    {
		((TextView)findViewById(R.id.topbar_LBL_heading)).setText("Signature");

    	LinearLayout lnrReciept = (LinearLayout)findViewById(R.id.creditsale_LNR_redceiptdetails);
    	final RecieptView recieptView = new RecieptView(this);
    	
    	recieptView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
    	lnrReciept.addView(recieptView);
    	
    	signatureView = (SignatureView)findViewById(R.id.creditsale_View_signature);
 
      	Button btnSubmit = (Button) findViewById(R.id.creditsale_BTN_submitsignature);
    	btnSubmit.setOnClickListener(new OnClickListener() 
    	{
			@Override
			public void onClick(View arg0) 
			{
				
					if(signatureView.signatureDrawn)
					{
						
						processSignature();
					}	else{
						
						ConstantSample.showDialog(CreditSaleSignatureView.this, "Card Sale", "You have sign for processing the card sale " );
					}
			}
		});
  	
    	Button btnClear = (Button) findViewById(R.id.creditsale_BTN_clear);
    	btnClear.setOnClickListener(new OnClickListener() 
    	{
			@Override
			public void onClick(View arg0) 
			{
				
				signatureView.clear();
				
			}
		});
    	
    }
    
    public void processSignature()
    {
		String referenceId = ConstantSample.mReferenceId;
		String password = ConstantSample.mPassword;
		
	
		MSwiperController swiperController = new MSwiperController(CreditSaleSignatureView.this);
		String[] results = swiperController.processSignature(signatureHandler, referenceId, password, 
				ConstantSample.mAuthCode, ConstantSample.mRRNO, ConstantSample.mDateTime, ConstantSample.mAmount, 
				ConstantSample.mLast4Digits, ConstantSample.mStandId, signatureView.mBitmap);
		
		  if(results[0].startsWith("error")){
				
				
			  ConstantSample.showDialog(this, "Card Sale", "Error in calling the card sale method " + results[1]);
		  }else{
				progressdialog = ProgressDialog.show(this, "","Please wait...", true);
				progressdialog.show();
		  }
    }
    
    private class RecieptView extends View {
   	 
    	public Bitmap mBitmapReciept = null;
    	public Paint mPaintReciept = null;
    

 		// CONSTRUCTOR
 		public RecieptView(Context context) {
 			super(context);
 			setFocusable(true); 	
 	    	mPaintReciept = new Paint();
 	    	mPaintReciept.setTextSize(10);
 			// smooth's out the edges of what is being drawn
 	    	mPaintReciept.setAntiAlias(true);
 	    	mBitmapReciept = Bitmap.createBitmap(450, 110, Bitmap.Config.ARGB_8888); // this creates a MUTABLE bitmap
 	    	Canvas canvas = new Canvas(mBitmapReciept);

 	    	Rect bounds = new Rect();

 	    	String authCodeRecipt = "AUTH NO: " + authCode;
 	    	String rrnoRecipt = "RR NO: " + rrno;
 	    	String dateAmtRecipt = "DATE: " + date + "  AMT: " + amt + "  LAST FOUR DIGITS: " + lstFrDgts;;
 	    	//String lstFrDgtsRecipt= "LAST FOUR DIGITS: " + lstFrDgts;
 	    	mPaintReciept.getTextBounds(authCodeRecipt, 0, authCodeRecipt.length(), bounds);
 	 	    
 	    	canvas.drawText(authCodeRecipt, 2, 15, mPaintReciept);
 	    	canvas.drawText(rrnoRecipt, 2 + bounds.width() + 25 , 15, mPaintReciept);
 	    	canvas.drawText(dateAmtRecipt, 2, 15+(bounds.height()* 1) + 5 , mPaintReciept);
   
 		}
 		
 	    @Override
 	    protected void onSizeChanged(int width, int height, int oldw, int oldh) 
 	    {

 	    	super.onSizeChanged(width, height, oldw, oldh);
 	    }

 		@Override
 		protected void onDraw(Canvas canvas) 
 		{
 			super.onDraw(canvas);
 			canvas.drawBitmap(mBitmapReciept, 0, 0, mPaintReciept);
 	
 		}
  
 	}
   
	public Handler signatureHandler = new Handler() 
    {
		public void handleMessage(android.os.Message msg) 
		{
			
			progressdialog.dismiss();

			progressdialog.dismiss();
			Bundle bundle = msg.getData();
			String[] results = bundle.getStringArray("HttpResponse");
			for (int ictr = 0; ictr < results.length; ictr++) {
				System.out.println( results[ictr]);
			}
			
			if(!results[0].startsWith("error"))
			{	
    
				
				
			}else{
				
			}
			AlertDialog.Builder builder = new AlertDialog.Builder(CreditSaleSignatureView.this);
			builder.setTitle("CardSale API");
			builder.setMessage(results[1]);
			builder.setNegativeButton("ok", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					reintent = new Intent(CreditSaleSignatureView.this, MSwipeAndroidSDKListActivity1.class);
					startActivity(reintent);
					dialog.dismiss();
			        finish();
				}
			});
			builder.setCancelable(false);
			builder.create().show();
          
    
		}
	};
	
	@Override
	public void onUserLeaveHint() {
		this.finish();
	}
  public void onBackPressed() {
		
		reintent = new Intent(CreditSaleSignatureView.this , MSwipeAndroidSDKListActivity1.class);
		startActivity(reintent);
			
		}
	
}
