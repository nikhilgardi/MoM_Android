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

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

/**
* Created by AkankshaRaina
*/
    public class PullParserHandlerTest {
    protected XmlPullParser xmlpullparser1;

    HashMap<String, String> hashMapBeneficiary = new HashMap<String, String>();


    private static final String ns = null;



    /** This method read each country in the xml data and add it to List */
    public  PullParserHandlerTest(InputStream is)
            throws XmlPullParserException,IOException{
        XmlPullParser parser=null;
        XmlPullParserFactory factory = null;
        try {
            factory = XmlPullParserFactory.newInstance();
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        factory.setNamespaceAware(true);
        try {
            parser = factory.newPullParser();
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        try {
            parser.setInput(is, "UTF-8");
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        parser.require(XmlPullParser.START_TAG, ns, "GetBeneficiaryListResult ");

        while(parser.next() != XmlPullParser.END_TAG){
            if(parser.getEventType() != XmlPullParser.START_TAG){
                continue;
            }

            String name = parser.getName();
            if(name.equals("GetBeneficiaryListResult")){
                readBeneficiaryList(parser);
            }
            else{
                skip(parser);
            }
        }

    }



    public  String getBeneficiaryId(String param) {
        return hashMapBeneficiary.get(param);

    }

    /** This method read a country and returns its corresponding HashMap construct */
    private void readBeneficiaryList(XmlPullParser parser)
            throws XmlPullParserException, IOException{

        parser.require(XmlPullParser.START_TAG, ns, "GetBeneficiaryListResult");


        String BeneficiaryID="";
        String BeneficiaryAccountAlias="";
        String currencyCode="";
        String currency="";
        hashMapBeneficiary = new HashMap<String, String>();
        while(parser.next() != XmlPullParser.END_TAG){
            if(parser.getEventType() != XmlPullParser.START_TAG){
                continue;
            }
            BeneficiaryID="";
            BeneficiaryAccountAlias="";
            String name = parser.getName();

            if(name.equals("BeneficiaryID")){
                BeneficiaryID = readBeneficiaryId(parser);
            }else if(name.equals("BeneficiaryAccountAlias")) {
                BeneficiaryAccountAlias = readBeneficiaryAlias(parser);
            }else{
                skip(parser);
            }

            if ( !BeneficiaryID.equals("") && !BeneficiaryAccountAlias.equals("")){
                hashMapBeneficiary.put(BeneficiaryAccountAlias,BeneficiaryID) ;

            }
        }
    }

    /** Process language tag in the xml data */
    private String readBeneficiaryId(XmlPullParser parser)
            throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "BeneficiaryID");
        String language = readText(parser);
        Log.e("ID" , language);
        return language;
    }
    private String readBeneficiaryAlias(XmlPullParser parser)
            throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "BeneficiaryAccountAlias");
        String language = readText(parser);
        Log.e("Alias" , language);
        return language;
    }
    /** Process Capital tag in the xml data */
    private void readCapital(XmlPullParser parser)
            throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "BeneficiaryAccountAlias");
        parser.nextTag();
    }

    /** Process Currency tag in the xml data */
    private String readCurrency(XmlPullParser parser)
            throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "currency");
        String currency = readText(parser);
        return currency;
    }

    /** Getting Text from an element */
    private String readText(XmlPullParser parser)
            throws IOException, XmlPullParserException{
        String result = "";
        if(parser.next()==XmlPullParser.TEXT){
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser)
            throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}