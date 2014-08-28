package com.mmpl.app;


import java.util.Vector;
import org.mswipe.sdk.MSwiperController;

import com.mmpl.app.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LastTrxStatus extends Activity 
{
	ProgressDialog progressdialog  =null;
	
	Vector lastTrxData = new Vector();
    Intent reintent = new Intent();
	/** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
    	setContentView(R.layout.lasttrxstatus);
	  for (int ictr = 0; ictr < 12; ictr++) 
	  {
	      lastTrxData.addElement("");
	      
	  }

   }

	@Override
	public void onResume(){

		super.onResume();
		getLastTrxStatus();
		

	}

	private void initViews() 
	{	
		try{
		TextView lblStatus = (TextView) findViewById(R.id.lsttrxstatus_LBL_TxStatus);
		lblStatus.setText((String) lastTrxData.elementAt(10));
		if(lastTrxData.elementAt(10).toString().equalsIgnoreCase("approved"))
		{
			lblStatus.setTextColor(Color.rgb(0,176,80));
			
		}else{
			lblStatus.setTextColor(Color.rgb(255,0,0));
		}
		TextView lblTxtDateTime = (TextView) findViewById(R.id.lsttrxstatus_LBL_TxDateTime);
		lblTxtDateTime.setText((String) lastTrxData.elementAt(0));
		

		TextView lblName = (TextView) findViewById(R.id.lsttrxstatus_LBL_CardholderName);
		lblName.setText((String) lastTrxData.elementAt(1));
		
		TextView lblNum = (TextView) findViewById(R.id.lsttrxstatus_LBL_CreditCardNum);
		lblNum.setText("" + (String) lastTrxData.elementAt(2));
		
		TextView lblamt = (TextView) findViewById(R.id.lsttrxstatus_LBL_AmtRs);
		lblamt.setText((String) lastTrxData.elementAt(3));
		
		TextView lblType = (TextView) findViewById(R.id.lsttrxstatus_LBL_TypeofTx);
		lblType.setText((String) lastTrxData.elementAt(4));
		lblType.setText("testing");
		

		TextView lblStan = (TextView) findViewById(R.id.lsttrxstatus_LBL_StanID);
		lblStan.setText((String) lastTrxData.elementAt(6));

		TextView lblVoucher = (TextView) findViewById(R.id.lsttrxstatus_LBL_Voucher);
		lblVoucher.setText((String) lastTrxData.elementAt(7));

		TextView lblAuthNo = (TextView) findViewById(R.id.lsttrxstatus_LBL_AuthNo);
		lblAuthNo.setText((String) lastTrxData.elementAt(8));

		TextView lblRRNo = (TextView) findViewById(R.id.lsttrxstatus_LBL_RRNo);
		lblRRNo.setText((String) lastTrxData.elementAt(9));

		TextView lbltmsg = (TextView) findViewById(R.id.lsttrxstatus_TXT_TerminalMessage);
		lbltmsg.setText((String) lastTrxData.elementAt(11));
		
		EditText lblnotes = (EditText) findViewById(R.id.lsttrxstatus_TXT_Notes);
		lblnotes.setText((String) lastTrxData.elementAt(5));
		
		

		}catch(Exception ex) {
			ex.printStackTrace();
		}

	}

	public void getLastTrxStatus() 
	{
		String aReferenceId = ConstantSample.mReferenceId; 
		String password = ConstantSample.mPassword;
		  
		    
		  MSwiperController swiperController = new MSwiperController(this);
		  String[] results = swiperController.getLastTrxStatus(lastTrxStatusHandler, aReferenceId,password);
	
 if(results[0].startsWith("error")){
				
			  //System.out.println("Error in calling the login method " + results[1]);
			  ConstantSample.showDialog(this, "Last Trs Status API", results[1]);
			  
		  }
		 else{
			progressdialog = ProgressDialog.show(this, "","Please wait...", true);
			progressdialog.show();
		 }
  

	}

	
	public Handler lastTrxStatusHandler = new Handler() 
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
				
			//	ConstantSample.showDialog(LastTrxStatus.this, "Login API", results[1]);
				
				

				
				int iposS = results[1].indexOf("</TrxDate>");
				char[] strArray = results[1].toCharArray();
				String extrsStr = "";
				iposS--;
				while (strArray[iposS] != '>' && iposS >= 0) {
					extrsStr = strArray[iposS] + extrsStr;
					iposS--;
				}
				
				TextView lblTxtDateTime = (TextView) findViewById(R.id.lsttrxstatus_LBL_TxDateTime);
				lblTxtDateTime.setText(extrsStr);
				
				
				iposS = results[1].indexOf("</CardHolderName>");
				strArray = results[1].toCharArray();
				extrsStr = "";
				iposS--;
				while (strArray[iposS] != '>' && iposS >= 0) {
					extrsStr = strArray[iposS] + extrsStr;
					iposS--;
				}
				
				TextView lblName = (TextView) findViewById(R.id.lsttrxstatus_LBL_CardholderName);
				lblName.setText(extrsStr);
				
				
				
				iposS = results[1].indexOf("</CardLastFourDigits>");
				strArray = results[1].toCharArray();
				extrsStr = "";
				iposS--;
				while (strArray[iposS] != '>' && iposS >= 0) {
					extrsStr = strArray[iposS] + extrsStr;
					iposS--;
				}
				TextView lblNum = (TextView) findViewById(R.id.lsttrxstatus_LBL_CreditCardNum);
				lblNum.setText(extrsStr);
				
				
				
				iposS = results[1].indexOf("</TrxAmount>");
				strArray = results[1].toCharArray();
				extrsStr = "";
				iposS--;
				while (strArray[iposS] != '>' && iposS >= 0) {
					extrsStr = strArray[iposS] + extrsStr;
					iposS--;
				}
				
				TextView lblamt = (TextView) findViewById(R.id.lsttrxstatus_LBL_AmtRs);
				lblamt.setText(extrsStr);
				
				
				
				 iposS = results[1].indexOf("</StanNo>");
				 strArray = results[1].toCharArray();
				 extrsStr = "";
				iposS--;
				while (strArray[iposS] != '>' && iposS >= 0) {
					extrsStr = strArray[iposS] + extrsStr;
					iposS--;
				}
				TextView lblStan = (TextView) findViewById(R.id.lsttrxstatus_LBL_StanID);
				lblStan.setText(extrsStr);
				
				
				
				iposS = results[1].indexOf("</TrxType>");
				strArray = results[1].toCharArray();
				extrsStr = "";
				iposS--;
				while (strArray[iposS] != '>' && iposS >= 0) {
					extrsStr = strArray[iposS] + extrsStr;
					iposS--;
				}
				TextView lblType = (TextView) findViewById(R.id.lsttrxstatus_LBL_TypeofTx);
				lblType.setText(extrsStr);

				iposS = results[1].indexOf("</AuthNo>");
				strArray = results[1].toCharArray();
				extrsStr = "";
				iposS--;
				while (strArray[iposS] != '>' && iposS >= 0) {
					extrsStr = strArray[iposS] + extrsStr;
					iposS--;
				}
				TextView lblAuthNo = (TextView) findViewById(R.id.lsttrxstatus_LBL_AuthNo);
				lblAuthNo.setText(extrsStr);

				iposS = results[1].indexOf("</RRNo>");
				strArray = results[1].toCharArray();
				extrsStr = "";
				iposS--;
				while (strArray[iposS] != '>' && iposS >= 0) {
					extrsStr = strArray[iposS] + extrsStr;
					iposS--;
				}
				TextView lblRRNo = (TextView) findViewById(R.id.lsttrxstatus_LBL_RRNo);
				lblRRNo.setText(extrsStr);

				iposS = results[1].indexOf("</VoucherNo>");
				strArray = results[1].toCharArray();
				extrsStr = "";
				iposS--;
				while (strArray[iposS] != '>' && iposS >= 0) {
					extrsStr = strArray[iposS] + extrsStr;
					iposS--;
				}
				TextView lblVoucher = (TextView) findViewById(R.id.lsttrxstatus_LBL_Voucher);
				lblVoucher.setText(extrsStr);

				iposS = results[1].indexOf("</TerminalMessage>");
				strArray = results[1].toCharArray();
				extrsStr = "";
				iposS--;
				while (strArray[iposS] != '>' && iposS >= 0) {
					extrsStr = strArray[iposS] + extrsStr;
					iposS--;
				}
				TextView lbltmsg = (TextView) findViewById(R.id.lsttrxstatus_TXT_TerminalMessage);
				lbltmsg.setText(extrsStr);
	
	
	
				
				
								
			}else{
				ConstantSample.showDialog(LastTrxStatus.this, "Login API", results[1]);
			}

  
	                
			
		}
	};
	
//	@Override
//	public void onUserLeaveHint() {
//		this.finish();
//	}
public void onBackPressed() {
		
		reintent = new Intent(LastTrxStatus.this , MSwipeAndroidSDKListActivity1.class);
		reintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(reintent);
			finish();
		}  
	

}
