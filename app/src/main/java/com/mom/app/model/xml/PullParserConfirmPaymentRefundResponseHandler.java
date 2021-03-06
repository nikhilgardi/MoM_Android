package com.mom.app.model.xml;

import android.content.Context;

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
    public class PullParserConfirmPaymentRefundResponseHandler {
    HashMap<String, String> hashMapBeneficiary = new HashMap<String, String>();
    List<String> list = new ArrayList<String>();
    int count=0;

    protected XmlPullParser xmlpullparser1;
        String output1;
        String TAG = "XmlPullParsing";
        int event;
        String text=null;

        private String clientTransactionID ;
        private String transactionAmount;
        private String errorMessage ;
        private String receiptID ;
        private String paymentStatus;
        private String errorCode;

        protected Context _applicationContext;

    public volatile boolean parsingComplete = true;

        public PullParserConfirmPaymentRefundResponseHandler(InputStream is) {


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
                String BeneficiaryID="",BeneficiaryAccountAlias="";
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
                        if(name.equals("PaymentStatus")){
                            paymentStatus = text;
                            EphemeralStorage.getInstance(_applicationContext).storeString(AppConstants.PARAM_NEW_MOM_IMPS_CONFIRM_PAYMENT_STATUS_BY_REFUND, paymentStatus);
                        }
                        else if(name.equals("ReceiptID")){
                            receiptID = text;
                            EphemeralStorage.getInstance(_applicationContext).storeString(AppConstants.PARAM_NEW_MOM_IMPS_CONFIRM_RECEIPT_ID_BY_REFUND, receiptID);
                        }
                        else if(name.equals("ErrorCode")){
                            errorCode = text;
                            EphemeralStorage.getInstance(_applicationContext).storeString(AppConstants.PARAM_NEW_MOM_IMPS_CONFIRM_PAYMENT_ERROR_CODE_BY_REFUND, errorCode);
                        }
                        else if(name.equals("ErrorMessage")){
                            errorMessage = text;
                            EphemeralStorage.getInstance(_applicationContext).storeString(AppConstants.PARAM_NEW_MOM_IMPS_CONFIRM_PAYMENT_ERROR_MESSAGE_BY_REFUND, errorMessage);
                        }
                        else if(name.equals("ClientTransactionID")){
                            clientTransactionID = text;
                            EphemeralStorage.getInstance(_applicationContext).storeString(AppConstants.PARAM_NEW_MOM_IMPS_CONFIRM_CLIENT_TXN_ID_BY_REFUND, clientTransactionID);
                        }
                        else if(name.equals("TransactionAmount")){
                            transactionAmount = text;
                            EphemeralStorage.getInstance(_applicationContext).storeString(AppConstants.PARAM_NEW_MOM_IMPS_CONFIRM_TxnAMOUNT_ID_BY_REFUND, transactionAmount);
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