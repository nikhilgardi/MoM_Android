package com.mom.app.model.xml;

import android.content.Intent;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Toast;

import com.mom.app.R;
import com.mom.app.utils.AppConstants;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by vaibhavsinha on 7/6/14.
 */
public class PullParser {
    protected XmlPullParser xmlpullparser;
    public volatile boolean parsingComplete = true;
    String TAG = "XmlPullParsing";
    private String registrationStatus ;
    private String registeredCustomerID;
    private String errorMessage ;
    private boolean _signUpParsing;

    public PullParser(InputStream inputStream) {
        this(inputStream, false);
    }

    public PullParser(InputStream inputStream, boolean signUpParsing) {
        _signUpParsing = signUpParsing;
        XmlPullParserFactory factory = null;
        try {
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);

            xmlpullparser = factory.newPullParser();
            xmlpullparser.setInput(inputStream, "UTF-8");

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    public String getTextResponse() {
        try {
            int eventType = xmlpullparser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if(_signUpParsing){
                  //  parseTag(eventType);
                    parseXMLAndStoreIt(xmlpullparser);

                    return null;
                }
                if (eventType == XmlPullParser.TEXT) {
                    return xmlpullparser.getText();

                }

                eventType = xmlpullparser.next();
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    void parseTag(int event) {
        try {
            String text = null;
            event = xmlpullparser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                String name = xmlpullparser.getName();
                switch (event){
                    case XmlPullParser.START_TAG:
                        break;
                    case XmlPullParser.TEXT:
                        text = xmlpullparser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if(name.equals("RegistrationStatus")){
                            registrationStatus = xmlpullparser.getText();

                        }
                        else if(name.equals("RegisteredCustomerID")){
                            registeredCustomerID = text;

                        }
                        else if(name.equals("ErrorMessage")){
                            errorMessage = text;

                        }

                        break;
                }

                event = xmlpullparser.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void parseXMLAndStoreIt(XmlPullParser myParser) {
        int event;
        String text=null;
        try {
            event = myParser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                String name=myParser.getName();
                switch (event){
                    case XmlPullParser.START_TAG:
                        break;
                    case XmlPullParser.TEXT:
                        text = myParser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if(name.equals("RegistrationStatus")){
                            registrationStatus = text;

                        }
                        else if(name.equals("RegisteredCustomerID")){
                            registeredCustomerID = text;
                        }
                        else if(name.equals("ErrorMessage")){
                            errorMessage = text;
                        }

                        else{
                        }
                        break;
                }

                event = myParser.next();
                if(registrationStatus.equals("true")){
                    Log.i("RegStatus" , registrationStatus);
                }
            }
            parsingComplete = false;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getRegistrationStatus() {
        Log.i("GetRegStatus" , registrationStatus);
        return registrationStatus;
    }

    public String getErrorMessage() {
        Log.i("GetErrorMsg" , errorMessage);
        return errorMessage;
    }
}
