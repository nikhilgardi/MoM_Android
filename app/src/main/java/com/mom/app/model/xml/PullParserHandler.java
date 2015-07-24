package com.mom.app.model.xml;

import android.content.Context;
import android.util.Log;

import com.mom.app.MoMApp;
import com.mom.app.R;
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
    public class PullParserHandler  {
    HashMap<String, String> hashMapBeneficiary = new HashMap<String, String>();
    HashMap<String, String> hashMapBranch = new HashMap<String, String>();

    List<String> list = new ArrayList<String>();
    List<String> bankList  = new ArrayList<String>();
    List<String> stateList = new ArrayList<String>();
    List<String> cityList  = new ArrayList<String>();
    List<String> branchList  = new ArrayList<String>();
//    int count=0,bankCount=0;
//    int stateCount =0;
//    int cityCount =0;
//    int branchCount =0;

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
        private String bankName;
        private String stateName;
        private String cityName;

        private String GetBeneficiaryListResult;
        private int code;
        private String customerRegistrationStatus;

        private int customerID;
        private int registerCustomerID;
        private String impsAllowed;
        private String message;
        private String availableLimit;
        private Boolean isFullKYC;

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
                String BeneficiaryID="",BeneficiaryAccountAlias="";
                String BranchName="",IFSCCode="";
                int count=0,bankCount=0;
                int stateCount =0;
                int cityCount =0;
                int branchCount =0;

                if ( count==0)
                {
                   // list.add("--Select Beneficiary--");
                    list.add("--"+MoMApp.getContext().getResources().getString(R.string.prompt_spinner_select_Beneficiary)+"--");
                    Log.e("SelectBeneficiary" ,"--"+MoMApp.getContext().getResources().getString(R.string.prompt_spinner_select_Beneficiary)+"--");
                    hashMapBeneficiary.put("--Select Beneficiary--","-1");
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
                            if(name.equals("PostingStatus")){
                                PostingStatus = text;

                             EphemeralStorage.getInstance(_applicationContext).storeString(AppConstants.PARAM_NEW_SUBMIT_POSTING_STATUS, PostingStatus);

                            }
                           else if (name.equals("BeneficiaryID")) {
                                BeneficiaryID = text;
                                Log.e("ID", BeneficiaryID);
                            }

                            else if (name.equals("BeneficiaryAccountAlias")) {
                                BeneficiaryAccountAlias =text;
                                Log.e("Alias", BeneficiaryAccountAlias);
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

                            else if(name.equals("Code")){
                                code = Integer.parseInt(text);
                                EphemeralStorage.getInstance(_applicationContext).storeInt(AppConstants.PARAM_NEW_FORGOT_PASSWORD_CODE, code);
                                Log.e("Code", String.valueOf(code));
                            }
                            else if(name.equals("Message")){
                                message = text;
                                EphemeralStorage.getInstance(_applicationContext).storeString(AppConstants.PARAM_NEW_FORGOT_PASSWORD_MESSAGE, message);
                            }
                            else if(name.equals("IsRegistered")){
                                customerRegistrationStatus = text;
                                EphemeralStorage.getInstance(_applicationContext).storeString(AppConstants.PARAM_NEW_MOM_CUSTOMER_STATUS_IS_REGISTERED, customerRegistrationStatus);
                            }
                            else if(name.equals("CustomerID")){
                                customerID = Integer.parseInt(text);
                                EphemeralStorage.getInstance(_applicationContext).storeInt(AppConstants.PARAM_NEW_MOM_IMPS_CUSTOMER_STATUS_CUSTOMER_ID, customerID);
                            }
                            else if(name.equals("IsIMPSServiceAllowed")){
                                impsAllowed = text;
                                EphemeralStorage.getInstance(_applicationContext).storeString(AppConstants.PARAM_NEW_MOM_CUSTOMER_STATUS_IS_IMPS_AUTHORISED, impsAllowed);
                            }
                            else if(name.equals("AvailableLimit")){
                                availableLimit = text;
                                EphemeralStorage.getInstance(_applicationContext).storeString(AppConstants.PARAM_NEW_MOM_CHECK_KYC_AVAILABLE_LIMIT, availableLimit);
                            }
                            else if(name.equals("isFullKYC")){
                                isFullKYC = Boolean.valueOf(text);
                                EphemeralStorage.getInstance(_applicationContext).storeBoolean(AppConstants.PARAM_NEW_MOM_CHECK_KYC_isFullKYC, isFullKYC);
                            }
//                             else if(name.equals("RegistrationStatus")){
//                                customerRegistrationIMPSStatus = text;
//                                EphemeralStorage.getInstance(_applicationContext).storeString(AppConstants.PARAM_NEW_MOM_REGISTER_CUSTOMER_REGISTRATION_STATUS, customerRegistrationIMPSStatus);
//                            }
//                            else if(name.equals("RegisteredCustomerID")){
//                                registerCustomerID = Integer.parseInt(text);
//                                EphemeralStorage.getInstance(_applicationContext).storeInt(AppConstants.PARAM_NEW_MOM_REGISTER_CUSTOMER_CUSTOMER_ID, registerCustomerID);
//                            }
//                            else if(name.equals("Message")){
//                                customerRegistrationErrorMessage = text;
//                                EphemeralStorage.getInstance(_applicationContext).storeString(AppConstants.PARAM_NEW_MOM_REGISTER_CUSTOMER_ERROR_MSG, customerRegistrationErrorMessage);
//                            }

                            else if(name.equals("BankName")){
                                if(bankCount==0) {
                                  //  bankList.add("--Select Bank--");
                                    bankList.add("--"+MoMApp.getContext().getResources().getString((R.string.prompt_spinner_select_Bank))+"--");
                                  Log.e("SelectBank", "--"+MoMApp.getContext().getResources().getString((R.string.prompt_spinner_select_Bank))+"--");

                                    bankCount=1;
                                }
                                    bankName = text;
                                    bankList.add(bankName);

                                }
                            else if(name.equals("StateName")){
                                if(stateCount==0) {
                                   // stateList.add("--Select State--");
                                    stateList.add("--"+MoMApp.getContext().getResources().getString(R.string.prompt_spinner_select_State)+"--");
                                    Log.e("SelectState","--"+MoMApp.getContext().getResources().getString(R.string.prompt_spinner_select_State)+"--");
                                    stateCount=1;
                                }
                                stateName = text;
                                stateList.add(stateName);

                            }
                            else if(name.equals("CityName")){
                                if(cityCount==0) {
                                   // cityList.add("--Select City--");
                                    cityList.add("--"+MoMApp.getContext().getResources().getString(R.string.prompt_spinner_select_City)+"--");
                                    Log.e("SelectCity","--"+MoMApp.getContext().getResources().getString(R.string.prompt_spinner_select_City)+"--");
                                    cityCount=1;
                                }
                                cityName = text;
                                cityList.add(cityName);

                            }
                            else if(name.equals("BranchName")){

                                BranchName = text;

                            }
                            else if(name.equals("IFSCCode")){

                                IFSCCode = text;

                            }

                            else{
                            }

                            if ( !BeneficiaryID.equals("") && !BeneficiaryAccountAlias.equals("")){
//                                if ( count==0)
//                                {
//                                    list.add("--Select Beneficiary--");
//                                    hashMapBeneficiary.put("--Select Beneficiary--","-1");
//                                    count=1;
//                                }
                                hashMapBeneficiary.put(BeneficiaryAccountAlias,BeneficiaryID) ;

                                list.add(BeneficiaryAccountAlias);
                                Log.e("BeneficiaryTEst" , BeneficiaryAccountAlias);
                                BeneficiaryID="";BeneficiaryAccountAlias="";
                            }


                            if ( !BranchName.equals("") && !IFSCCode.equals("")){
                                if ( branchCount==0)
                                {
                                  //  branchList.add("--Select Branch--");
                                    branchList.add("--"+MoMApp.getContext().getResources().getString(R.string.prompt_spinner_select_Branch)+"--");
                                    Log.e("SelectBranch","--"+MoMApp.getContext().getResources().getString(R.string.prompt_spinner_select_Branch)+"--");
                                    hashMapBranch.put("--Select Branch--","-1");
                                    branchCount=1;
                                }
                                hashMapBranch.put(BranchName,IFSCCode) ;

                                branchList.add(BranchName);
                                Log.e("BranchTEst" , BranchName);
                                BranchName="";IFSCCode="";
                            }



                            break;
                    }


                    event = xmlpullparser1.next();



                }
                EphemeralStorage.getInstance(_applicationContext).storeBeneficiaryList(AppConstants.PARAM_NEW_MOM_REGISTER_GET_BENEFICIARY_LIST_RESULT, hashMapBeneficiary);
                EphemeralStorage.getInstance(_applicationContext).storeBeneficiaryListDetail(AppConstants.PARAM_NEW_MOM_REGISTER_GET_BENEFICIARY_LIST_RESULT_DETAIL, list);
                EphemeralStorage.getInstance(_applicationContext).storeBankList(AppConstants.PARAM_NEW_MOM_REGISTER_IFSC_BANK_NAMES_LIST, bankList);
                EphemeralStorage.getInstance(_applicationContext).storeStateList(AppConstants.PARAM_NEW_MOM_REGISTER_IFSC_STATE_NAMES_LIST, stateList);
                EphemeralStorage.getInstance(_applicationContext).storeCityList(AppConstants.PARAM_NEW_MOM_REGISTER_IFSC_CITY_NAMES_LIST, cityList);
                EphemeralStorage.getInstance(_applicationContext).storeBranchList(AppConstants.PARAM_NEW_MOM_REGISTER_GET_BRANCH_LIST_RESULT, hashMapBranch);
                EphemeralStorage.getInstance(_applicationContext).storeBranchListDetail(AppConstants.PARAM_NEW_MOM_REGISTER_GET_BRANCH_LIST_RESULT_DETAIL, branchList);
                parsingComplete = false;
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

    }