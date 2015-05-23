package com.mom.app.model.xml;

import android.content.Context;

import com.mom.app.model.local.EphemeralStorage;
import com.mom.app.utils.AppConstants;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;

/**
* Created by AkankshaRaina
*/
    public class PullParserHandler {

        protected XmlPullParser xmlpullparser1;
        String output1;
        String TAG = "XmlPullParsing";
        int event;
        String text=null;
        private String PostingStatus ;
        private String sessionID ;
        private String errorMessage ;
        private String statusCode;
        private String errorCode;
        private String receiptID;
        private String currentBalance;
        private String registrationStatus;
    protected Context _applicationContext;

    public volatile boolean parsingComplete = true;

        public PullParserHandler(InputStream is) {


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

        void parseTag(int event) {
            try {
                event = xmlpullparser1.getEventType();
                while (event != XmlPullParser.END_DOCUMENT) {
                    String name=xmlpullparser1.getName();
                    switch (event){
                        case XmlPullParser.START_TAG:
                            break;
                        case XmlPullParser.TEXT:
                            text = xmlpullparser1.getText();
                            break;

                        case XmlPullParser.END_TAG:
                            if(name.equals("PostingStatus")){
                                PostingStatus = text;

                             EphemeralStorage.getInstance(_applicationContext).storeString(AppConstants.PARAM_NEW_SUBMIT_POSTING_STATUS, PostingStatus);

                            }
                            else if(name.equals("SessionID")){
                                sessionID = text;
                                EphemeralStorage.getInstance(_applicationContext).storeString(AppConstants.PARAM_NEW_SUBMIT_SESSION_ID, sessionID);
                               // Log.i("SessionID" , sessionID);
                            }
                            else if(name.equals("ErrorMessage")){
                                errorMessage = text;
                                EphemeralStorage.getInstance(_applicationContext).storeString(AppConstants.PARAM_NEW_SUBMIT_ERROR_MSG, errorMessage);
                              //  Log.i("ErrorMessage" , errorMessage);
                            }
                            else if(name.equals("StatusCode")){
                                statusCode = text;

                                EphemeralStorage.getInstance(_applicationContext).storeString(AppConstants.PARAM_NEW_SUBMIT_STATUS_CODE, statusCode);
                               // Log.i("StatusCode_Confirm" , statusCode);
                            }
                            else if(name.equals("ErrorCode")){
                                errorCode = text;
                                EphemeralStorage.getInstance(_applicationContext).storeString(AppConstants.PARAM_NEW_SUBMIT_ERROR_CODE, errorCode);
                               // Log.i("ErrorCode_Confirm" , errorCode);
                            }
                            else if(name.equals("ReceiptID")){
                                receiptID = text;
                                EphemeralStorage.getInstance(_applicationContext).storeString(AppConstants.PARAM_NEW_SUBMIT_RECEIPT_ID, receiptID);
                              //  Log.i("ReceiptID_Confirm" , receiptID);
                            }

                            else if(name.equals("CurrentBalance")){
                                currentBalance = text;
                                EphemeralStorage.getInstance(_applicationContext).storeString(AppConstants.PARAM_NEW_SUBMIT_CURRENT_BALANCE, currentBalance);
                               // Log.i("SessionID_Confirm" , sessionID);
                            }
                            else if(name.equals("RegistrationStatus")){
                                registrationStatus = text;
                                EphemeralStorage.getInstance(_applicationContext).storeString(AppConstants.PARAM_NEW_SIGNUP_STATUS, registrationStatus);
                            }
                            else{
                            }


                            break;
                    }


                    event = xmlpullparser1.next();



                }
                parsingComplete = false;
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

    }