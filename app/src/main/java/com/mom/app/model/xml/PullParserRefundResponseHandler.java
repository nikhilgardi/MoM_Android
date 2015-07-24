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
    public class PullParserRefundResponseHandler {
    HashMap<String, String> hashMapRefundReceiptID = new HashMap<String, String>();
    HashMap<String, String> hashMapRefundAmount = new HashMap<String, String>();
    List<String> refundTxnDescList = new ArrayList<String>();

    protected XmlPullParser xmlpullparser1;
        String output1;
        String TAG = "XmlPullParsing";
        int event;
        String text=null;

        private String receiptID;
        private String transactionDescription;
        private String refundAmount;


        protected Context _applicationContext;

    public volatile boolean parsingComplete = true;

        public PullParserRefundResponseHandler(InputStream is) {


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
                receiptID="";transactionDescription="";
                refundAmount="";
                int count=0;

                if ( count==0)
                {
                    refundTxnDescList.add("--Retailer Payment--");
                    hashMapRefundReceiptID.put("--Retailer Payment--","-1");
                    hashMapRefundAmount.put("--Retailer Payment--","-1");
                    count=1;
                }


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

                          if(name.equals("ReceiptID")){
                                receiptID = text;
                               // EphemeralStorage.getInstance(_applicationContext).storeString(AppConstants.PARAM_NEW_MOM_IMPS_REFUND_RECEIPT_ID, receiptID);
                              Log.e("ReceiptID" , receiptID);

                            }

                            else if(name.equals("TransactionDescription")){
                                transactionDescription = text;
                               // EphemeralStorage.getInstance(_applicationContext).storeString(AppConstants.PARAM_NEW_MOM_IMPS_REFUND_TRANSACTION_DESCRIPTION, transactionDescription);
                              Log.e("TransactionDescription" , transactionDescription);
                            }
                            else if(name.equals("RefundAmount")){
                                refundAmount = text;
                               // EphemeralStorage.getInstance(_applicationContext).storeString(AppConstants.PARAM_NEW_MOM_IMPS_REFUND_AMOUNT, refundAmount);
                              Log.e("RefundAmount" , refundAmount);
                          }

                            else{
                            }

                            if ( !receiptID.equals("") && !transactionDescription.equals("")&& !refundAmount.equals("")){

                                hashMapRefundReceiptID.put(transactionDescription ,receiptID) ;
                                hashMapRefundAmount.put(transactionDescription ,refundAmount) ;


                                refundTxnDescList.add(transactionDescription);
                                Log.e("RefundTest" , transactionDescription);
                                receiptID="";transactionDescription="";refundAmount="";
                            }

                            break;
                    }


                    event = xmlpullparser1.next();



                }
                EphemeralStorage.getInstance(_applicationContext).storeRefundReceiptIdMap(AppConstants.PARAM_NEW_MOM_REFUND_RECEIPT_ID_MAP, hashMapRefundReceiptID);
                EphemeralStorage.getInstance(_applicationContext).storeRefundAmountMap(AppConstants.PARAM_NEW_MOM_REFUND_AMOUNT_MAP, hashMapRefundAmount);
                EphemeralStorage.getInstance(_applicationContext).storeRefundTransactionDescriptionList(AppConstants.PARAM_NEW_MOM_REFUND_TRANSACTION_DESCRIPTION_LIST, refundTxnDescList);

                parsingComplete = false;
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

    }