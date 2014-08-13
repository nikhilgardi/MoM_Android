/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Satish N
 */



package com.mom.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;


public class ConstantSample {
    
    
    
    public static String mUserId="";
    public static String mReferenceId="";
    public static String mPassword="";
    public static String mFirstName="";
    public static String NotFirstTimeLogin="";
    
    public static String mLast4Digits = "";
    
    public static String mStandId = "";
    public static String mAuthCode = "";
    public static String mRRNO = "";
    public static String mDateTime = "";
    public static String mAmount= "";
    public static String test= "abc";
    
    
	
	

    
    
    public static void showDialog(Context context, String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("ok", new OnClickListener() {
                public void onClick(DialogInterface dialog, int arg1) {
               
                   
                	dialog.dismiss();
                	//dialog.cancel();
                	
             
                	//Toast.makeText(ConstantSample.this, text, duration)
                    
                }});
        
        builder.setCancelable(false);
       builder.create().show();
    }

}
