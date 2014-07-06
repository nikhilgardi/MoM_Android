package com.mom.app.model.xml;

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

    String TAG = "XmlPullParsing";

    public PullParser(InputStream inputStream){
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
                if(eventType == XmlPullParser.TEXT) {
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
}
