package com.mom.app.model.xml;

import android.content.Context;
import android.util.Log;

import com.mom.app.model.local.EphemeralStorage;
import com.mom.app.utils.AppConstants;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
* Created by AkankshaRaina
*/
    public class PullParserCustomerRegistrationHandler {


    protected XmlPullParser xmlpullparser1;

        String TAG = "XmlPullParsing";
        int event;
        String text=null;


        private String customerRegistrationIMPSStatus;
        private String customerRegistrationErrorMessage;
        private int registerCustomerID;


        protected Context _applicationContext;

    public volatile boolean parsingComplete = true;

        public PullParserCustomerRegistrationHandler(InputStream is) {


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
                            if(name.equals("RegistrationStatus")){
                                customerRegistrationIMPSStatus = text;
                                EphemeralStorage.getInstance(_applicationContext).storeString(AppConstants.PARAM_NEW_MOM_REGISTER_CUSTOMER_REGISTRATION_STATUS, customerRegistrationIMPSStatus);
                            }
                            else if(name.equals("RegisteredCustomerID")){
                                registerCustomerID = Integer.parseInt(text);
                                EphemeralStorage.getInstance(_applicationContext).storeInt(AppConstants.PARAM_NEW_MOM_REGISTER_CUSTOMER_CUSTOMER_ID, registerCustomerID);
                            }
                            else if(name.equals("Message")){
                                customerRegistrationErrorMessage = text;
                                EphemeralStorage.getInstance(_applicationContext).storeString(AppConstants.PARAM_NEW_MOM_REGISTER_CUSTOMER_ERROR_MSG, customerRegistrationErrorMessage);
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