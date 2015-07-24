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
    public class PullParserBeneficiaryDetailsHandler {
    HashMap<String, String> hashMapBeneficiary = new HashMap<String, String>();
    List<String> list = new ArrayList<String>();
    int count=0;

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
        private String customerRegistrationIMPSStatus;
        private String customerRegistrationErrorMessage;
          private String GetBeneficiaryListResult;
        private int code;
        private String customerRegistrationStatus;
        private int customerID;
        private int registerCustomerID;
        private String impsAllowed;
        private String message;
        private String availableLimit;
        private Boolean isFullKYC;
        private String beneficiaryId;
        private String beneficiaryAccountAlias;
        private String beneficiaryAccountName;
        private String beneficiaryAccountNumber;
        private String beneficiaryIFSCCode;
        private String beneficiaryMobileNumber;
        private Boolean beneficiaryVerificationStatus;
        protected Context _applicationContext;

    public volatile boolean parsingComplete = true;

        public PullParserBeneficiaryDetailsHandler(InputStream is) {


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
                        if(name.equals("BeneficiaryID")){
                            beneficiaryId = text;
                            EphemeralStorage.getInstance(_applicationContext).storeString(AppConstants.PARAM_NEW_MOM_IMPS_BENEFICIARY_ID, beneficiaryId);
                        }
                        else if(name.equals("BeneficiaryAccountAlias")){
                            beneficiaryAccountAlias = text;
                            EphemeralStorage.getInstance(_applicationContext).storeString(AppConstants.PARAM_NEW_MOM_IMPS_BENEFICIARY_ACCOUNT_ALIAS, beneficiaryAccountAlias);
                        }
                        else if(name.equals("BeneficiaryAccountName")){
                            beneficiaryAccountName = text;
                            EphemeralStorage.getInstance(_applicationContext).storeString(AppConstants.PARAM_NEW_MOM_IMPS_BENEFICIARY_ACCOUNT_NAME, beneficiaryAccountName);
                        }
                        else if(name.equals("BeneficiaryAccountNumber")){
                            beneficiaryAccountNumber = text;
                            EphemeralStorage.getInstance(_applicationContext).storeString(AppConstants.PARAM_NEW_MOM_IMPS_BENEFICIARY_ACCOUNT_NUMBER, beneficiaryAccountNumber);
                        }
                        else if(name.equals("BeneficiaryMobileNumber")){
                            beneficiaryMobileNumber = text;
                            EphemeralStorage.getInstance(_applicationContext).storeString(AppConstants.PARAM_NEW_MOM_IMPS_BENEFICIARY_MOBILE_NUMBER, beneficiaryMobileNumber);
                            Log.i("BeneficiaryMobileNumber" , beneficiaryMobileNumber);
                        }
                        else if(name.equals("BeneficiaryIFSCCode")){
                            beneficiaryIFSCCode = text;
                            EphemeralStorage.getInstance(_applicationContext).storeString(AppConstants.PARAM_NEW_MOM_IMPS_BENEFICIARY_IFSC_CODE, beneficiaryIFSCCode);
                        }

                        else if(name.equals("BeneficiaryVerificationStatus")){
                            beneficiaryVerificationStatus = Boolean.valueOf(text);
                            EphemeralStorage.getInstance(_applicationContext).storeBoolean(AppConstants.PARAM_NEW_MOM_IMPS_BENEFICIARY_VERIFICATION_STATUS, beneficiaryVerificationStatus);
                        }
                            break;
                    }


                    event = xmlpullparser1.next();



                }
//                EphemeralStorage.getInstance(_applicationContext).storeBeneficiaryList(AppConstants.PARAM_NEW_MOM_REGISTER_GET_BENEFICIARY_LIST_RESULT, hashMapBeneficiary);
//                EphemeralStorage.getInstance(_applicationContext).storeBeneficiaryListDetail(AppConstants.PARAM_NEW_MOM_REGISTER_GET_BENEFICIARY_LIST_RESULT_DETAIL, list);
                parsingComplete = false;
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

    }