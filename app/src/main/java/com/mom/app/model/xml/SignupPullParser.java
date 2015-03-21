package com.mom.app.model.xml;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.mom.app.R;
import com.mom.app.model.DataExImpl;
import com.mom.app.utils.AppConstants;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Pbx12 on 2/5/2015.
 */
public class SignupPullParser {

    protected XmlPullParser xmlpullparser;
    String output1;
    String TAG = "XmlPullParsing";
    int event;
    String text=null;
    private String registrationStatus ;
    public volatile boolean parsingComplete = true;
    private String registeredCustomerID;
    private String errorMessage ;

    public SignupPullParser(InputStream is , DataExImpl.Methods callback) {


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
        try {
            event = xmlpullparser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                String name=xmlpullparser.getName();
                switch (event){
                    case XmlPullParser.START_TAG:
                        break;
                    case XmlPullParser.TEXT:
                        text = xmlpullparser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if(name.equals("PostingStatus")){
                            registrationStatus = text;
                            Log.i("REG", registrationStatus);


                        }
                        else if(name.equals("SessionID")){
                            registeredCustomerID = text;

                        }
                        else if(name.equals("ErrorMessage")){
                            errorMessage = text;

                        }
                        else{
                        }


                        break;
                }

               // if(registrationStatus.equals("true"))
                if("true".equals(registrationStatus)){
                System.out.println(registrationStatus);
                }
                else if("false".equals(registrationStatus)){


                }
                event = xmlpullparser.next();



            }
            parsingComplete = false;
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}

