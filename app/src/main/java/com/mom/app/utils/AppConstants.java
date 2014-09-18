package com.mom.app.utils;

import java.util.HashMap;

import com.mom.app.R;

public class AppConstants {
	public static final String APP_PREFERENCES		        = "AppPreferences";
    public static final String PACKAGE                      = "com.mom.app";
    public static final String LOG_PREFIX                   = "MOMAPP";
    public static final String UTF_8                        = "utf-8";
    public static final String USER_BALANCE                 = "userBalance";
    public static final String USER_LANGUAGE                = "userLanguage";
    public static final String KEY_LANGUAGE                 = PACKAGE + USER_LANGUAGE;
    public static final String ACTIVE_PLATFORM              = "activePlatform";

    /*
    Start: MoM Platform urls
     */
	public static final String URL_NEW_PLATFORM		        = "http://msvc.money-on-mobile.net/WebServiceV3Client.asmx";
	public static final String URL_NEW_PLATFORM_TXN	        = "http://msvc.money-on-mobile.net/WebServiceV3Trans.asmx";
    public static final String URL_NEW_PLATFORM_GET_BEST_BILL
                                                            = "http://180.179.67.72/bestpayments/billInquiry.ashx";

    public static final String URL_NEW_PLATFORM_GET_RELIANCE_BILL
                                                            = "http://180.179.67.72/RelianceEnergy/billEnquiry.ashx";
    public static final String URL_NEW_PLATFORM_GET_MGL_BILL
                                                            = "http://180.179.67.72/mgl/billInquiry.aspx";

	public static final String URL_NEW_PL_DETAILS	        = "http://180.179.67.72/nokiaservice/DetailsByUserRMNCompID.aspx";
	public static final String URL_NEW_PL_HISTORY	        = "http://180.179.67.72/nokiaservice/Lastfivetransactions.aspx";




    //Start: MoM Platform Methods
	public static final String SVC_NEW_METHOD_LOGIN	        = "/getLoggedIn";
	public static final String SVC_NEW_METHOD_CHECK_TPIN    = "/checkVallidTpin";
    public static final String SVC_NEW_METHOD_GET_BALANCE   = "/getBalanceByCustomerId";
	public static final String SVC_NEW_METHOD_RECHARGE_MOBILE
                                                            = "/DoMobRecharge";
    public static final String SVC_NEW_METHOD_RECHARGE_DTH  = "/DoDTHRechargeV2";
    public static final String SVC_NEW_METHOD_BILL_PAYMENT  = "/doBillPayment";
    public static final String SVC_NEW_METHOD_CHANGE_TPIN   = "/ChangeT_Pin";
    public static final String SVC_NEW_METHOD_CHANGE_MPIN   = "/ChangeM_Pin";
    //End: MoM Platform Methods

    //Start: PBX Platform Service
    public static final String SVC_PBX_CHECK_LOGIN          = "CL";
    public static final String SVC_PBX_CHANGE_PASSWORD      = "CP";
    public static final String SVC_PBX_CHECK_BALANCE        = "CB";
    //End: PBX Platform Service

    //Start: PBX Platform URLs
    public static final String URL_PBX_PLATFORM_SERVICE     = "http://test.pbx.momappworld.com/momspre/service.ashx";
    public static final String URL_PBX_PLATFORM_APP         = "http://test.pbx.momappworld.com/momspre/PbxMobApp.ashx";

    //End: PBX Platform URLs

    //Start: PBX Platform Methods

    //End: PBX Platform Methods

    //START: FONT RELATED
    public static final String FONT_DEFAULT_TEXT_STYLE      = "regular";

    //END: FONT RELATED

    public static final float ERROR_BALANCE                 = -1000000;

    //PIN RELATED
    public static final String M_PIN                        = "M-Pin";
    public static final String T_PIN                        = "T-Pin";
    //End get bill information

    //START: External Android constants
    public static String EXTERNAL_TEMP_IMAGES           = "temp_images";
    //END: External Android constants

    //Get Bill information parameter
    public static final String PARAM_NEW_ACCOUNT_NUMBER     = "AccountNumber";
    public static final String PARAM_NEW_CANUMBER           = "CANumber";

	public static final String PARAM_NEW_USER		        = "strUserRMN";
	public static final String PARAM_NEW_PWD		        = "strPassword";
	public static final String PARAM_NEW_RMN		        = "UserRMN";
	public static final String PARAM_NEW_COMPANY_ID	        = "CompanyID";
    public static final String PARAM_NEW_OPERATOR_ID        = "OperatorID";
	public static final String PARAM_NEW_STR_ACCESS_ID      = "strAccessID";
	public static final String PARAM_NEW_STR_ACCESS_ID_SMALL_D
                                                            = "strAccessId";
    public static final String PARAM_NEW_USER_ID            = "UserID";
    public static final String PARAM_NEW_CUSTOMER_ID        = "CustomerID";
    public static final String PARAM_NEW_MOBILE_NUMBER      = "MobileNumber";
    public static final String PARAM_NEW_STR_MOBILE_NUMBER  = "strMobileNumber";
    public static final String PARAM_NEW_RECHARGE_AMOUNT    = "strRechargeAmount";
    public static final String PARAM_NEW_OPERATOR           = "strOperator";
    public static final String PARAM_NEW_INT_RECHARGE_TYPE  = "intRechargeType";
    public static final String PARAM_NEW_INT_CUSTOMER_ID    = "intCustomerId";
    public static final String PARAM_NEW_CUSTOMER_NUMBER    = "CustomerNumber";
    public static final String PARAM_NEW_INT_OPERATOR_ID_BILL_PAY
                                                            = "intOperatorIDBillPay";
    public static final String PARAM_NEW_DEC_BILL_AMOUNT    = "DecBillAmount";
    public static final String PARAM_NEW_STR_CUSTOMER_NUMBER
                                                            = "strCustomerNumber";
    public static final String PARAM_NEW_COMPANY_ID_CAMEL_CASE
                                                            = "companyId";

    public static final String PARAM_NEW_ROLE_ID            = "RoleID";
    public static final String PARAM_NEW_USER_AUTH_ID       = "UserAuthID";
    public static final String PARAM_NEW_USER_WALLET_ID     = "UserWalletID";
    public static final String PARAM_NEW_USER_STATUS        = "UserStatus";

    public static final String PARAM_NEW_USER_FRANCH_ID     = "UserFranchID";
    public static final String PARAM_NEW_USER_MAST_DIST     = "UserMastDist";
    public static final String PARAM_NEW_USER_AREA_DIST     = "UserAreaDist";
    public static final String PARAM_NEW_USER_VAS01         = "UserVAS01";
    public static final String PARAM_NEW_USER_VAS02         = "UserVAS02";
    public static final String PARAM_NEW_TPIN               = "strTPassword";

    public static final String PARAM_NEW_RELIANCE_SBE_NBE   = "relianceSBENBE";
    public static final String PARAM_NEW_SPECIAL_OPERATOR   = "specialOperator";
    public static final String PARAM_NEW_SPECIAL_OPERATOR_NBE
                                                            = "specialOperatorNBE";
    public static final String PARAM_NEW_STR_PASSWORD       = "strPassword";
    public static final String PARAM_NEW_STR_NEW_PASSWORD   = "strNewPassword";


    //Start: PBX Platform Params
    public static final String PARAM_PBX_SERVICE            = "Service";
    public static final String PARAM_PBX_USERNAME           = "un";
    public static final String PARAM_PBX_PASSWORD           = "password";
    public static final String PARAM_PBX_OP                 = "OP";
    public static final String PARAM_PBX_NP                 = "NP";
    public static final String PARAM_PBX_RN                 = "RN";
    public static final String PARAM_PBX_TOKEN              = "Token";

    //End: PBX Platform Params
	public static final int TIMEOUT_CONNECTION		        = 15000;
	public static final int TIMEOUT_SOCKET			        = 45000;
	public static final int STANDARD_MOBILE_NUMBER_LENGTH   = 10;

	public static final String IS_LOGGED_IN = "isLoggedIn";
	public static final String LOGGED_IN_USERNAME	        = "username";
	
	public static final String NEW_PL_COMPANY_ID	        = "184";
    public static final int NEW_PL_LOGIN_SUCCESS            = 101;
    public static final int NEW_PL_TPIN_VERIFIED            = 101;

    public static final String INTENT_MESSAGE               = PACKAGE + "MESSAGE";
    public static final String INTENT_MESSAGE_DEST          = PACKAGE + "MESSAGE_DEST";
    public static final String INTENT_MESSAGE_ORIGIN        = PACKAGE + "MESSAGE_ORIGIN";

    public static final String OPERATOR_ID_AIRCEL           = "1";
    public static final String OPERATOR_ID_AIRTEL           = "2";
    public static final String OPERATOR_ID_BSNL             = "5";
    public static final String OPERATOR_ID_DATACOMM         = "33";
    public static final String OPERATOR_ID_IDEA             = "28";
    public static final String OPERATOR_ID_LOOP             = "9";
    public static final String OPERATOR_ID_MOM_CARD         = "46";
    public static final String OPERATOR_ID_MTNL             = "23";
    public static final String OPERATOR_ID_MTS              = "10";

    public static final String OPERATOR_ID_QUE              = "72";
    public static final String OPERATOR_ID_RELIANCE_CDMA    = "12";
    public static final String OPERATOR_ID_RELIANCE_GSM     = "32";
    public static final String OPERATOR_ID_STEL             = "29";
    public static final String OPERATOR_ID_TATA             = "15";

    public static final String OPERATOR_ID_TATA_DOCOMO      = "16";
    public static final String OPERATOR_ID_TATA_WALKY       = "37";
    public static final String OPERATOR_ID_UNINOR           = "30";
    public static final String OPERATOR_ID_VIRGIN           = "19";
    public static final String OPERATOR_ID_VODAFONE         = "25";

    public static final String OPERATOR_ID_AIRTEL_DIGITAL   = "21";
    public static final String OPERATOR_ID_BIG_TV           = "27";
    public static final String OPERATOR_ID_DISH             = "7";
    public static final String OPERATOR_ID_SUN_DIRECT       = "14";
    public static final String OPERATOR_ID_TATA_SKY         = "24";

    public static final String OPERATOR_ID_VIDEOCON_DTH     = "18";
    public static final String OPERATOR_ID_AIRCEL_BILL      = "39";
    public static final String OPERATOR_ID_AIRTEL_BILL      = "3";
    public static final String OPERATOR_ID_AIRTEL_LAND_LINE = "68";
    public static final String OPERATOR_ID_BESCOM_BANGALURU = "61";
    public static final String OPERATOR_ID_BEST_ELECTRICITY = "55";
    public static final String OPERATOR_ID_BSES_RAJDHANI    = "58";
    public static final String OPERATOR_ID_BSNL_BILL_PAY    = "6";
    public static final String OPERATOR_ID_CELLONE_BILL_PAY = "56";
    public static final String OPERATOR_ID_CESC_LIMITED     = "57";

    public static final String OPERATOR_ID_CESCOM_MYSORE    = "62";
    public static final String OPERATOR_ID_DHBVN_HARYANA    = "59";
    public static final String OPERATOR_ID_IDEA_BILL        = "35";
    public static final String OPERATOR_ID_INDRAPRASTHA_GAS = "63";
    public static final String OPERATOR_ID_MAHANAGAR_GAS    = "45";
    public static final String OPERATOR_ID_NBE              = "49";
    public static final String OPERATOR_ID_RELIANCE_BILL_GSM
                                                            = "36";
    public static final String OPERATOR_ID_RELIANCE_BILL_CDMA
                                                            = "11";
    public static final String OPERATOR_ID_RELIANCE_ENERGY  = "51";
    public static final String OPERATOR_ID_SBE              = "50";
    public static final String OPERATOR_ID_TATA_BILL        = "42";
    public static final String OPERATOR_ID_TATA_POWER_DELHI = "67";
    public static final String OPERATOR_ID_TIKONA_BILL      = "44";
    public static final String OPERATOR_ID_UHBVN_HARYANA    = "60";
    public static final String OPERATOR_ID_VODAFONE_BILL    = "20";



    public static HashMap<String, String> OPERATOR_NEW     = new HashMap<String, String>();

    static {
        OPERATOR_NEW.put("AIRCEL", AppConstants.OPERATOR_ID_AIRCEL);
        OPERATOR_NEW.put("AIRTEL", AppConstants.OPERATOR_ID_AIRTEL);
        OPERATOR_NEW.put("BSNL", AppConstants.OPERATOR_ID_BSNL);
        OPERATOR_NEW.put("DATACOMM", AppConstants.OPERATOR_ID_DATACOMM);
        OPERATOR_NEW.put("IDEA", AppConstants.OPERATOR_ID_IDEA);
        OPERATOR_NEW.put("LOOP", AppConstants.OPERATOR_ID_LOOP);

        OPERATOR_NEW.put("MOM CARD REFILL", AppConstants.OPERATOR_ID_MOM_CARD);
        OPERATOR_NEW.put("MTNL", AppConstants.OPERATOR_ID_MTNL);
        OPERATOR_NEW.put("MTS", AppConstants.OPERATOR_ID_MTS);

        OPERATOR_NEW.put("QUE MOBILE", AppConstants.OPERATOR_ID_QUE);
        OPERATOR_NEW.put("RELIANCE CDMA", AppConstants.OPERATOR_ID_RELIANCE_CDMA);
        OPERATOR_NEW.put("RELIANCE GSM", AppConstants.OPERATOR_ID_RELIANCE_GSM);
        OPERATOR_NEW.put("STEL", AppConstants.OPERATOR_ID_STEL);
        OPERATOR_NEW.put("TATA", AppConstants.OPERATOR_ID_TATA);
        OPERATOR_NEW.put("TATA DOCOMO", AppConstants.OPERATOR_ID_TATA_DOCOMO);
        OPERATOR_NEW.put("TATA WALKY", AppConstants.OPERATOR_ID_TATA_WALKY);
        OPERATOR_NEW.put("UNINOR", AppConstants.OPERATOR_ID_UNINOR);

        OPERATOR_NEW.put("VIRGIN", AppConstants.OPERATOR_ID_VIRGIN);
        OPERATOR_NEW.put("VODAFONE", AppConstants.OPERATOR_ID_VODAFONE);

        OPERATOR_NEW.put("AIRTEL DIGITAL", AppConstants.OPERATOR_ID_AIRTEL_DIGITAL);
        OPERATOR_NEW.put("BIG TV", AppConstants.OPERATOR_ID_BIG_TV);
        OPERATOR_NEW.put("DISH", AppConstants.OPERATOR_ID_DISH);
        OPERATOR_NEW.put("SUN DIRECT", AppConstants.OPERATOR_ID_SUN_DIRECT);

        OPERATOR_NEW.put("TATA SKY", AppConstants.OPERATOR_ID_TATA_SKY);
        OPERATOR_NEW.put("VIDEOCON DTH", AppConstants.OPERATOR_ID_VIDEOCON_DTH);

        //Bill Payment
        OPERATOR_NEW.put("AIRCEL BILL", AppConstants.OPERATOR_ID_AIRCEL_BILL);
        OPERATOR_NEW.put("AIRTEL BILL", AppConstants.OPERATOR_ID_AIRTEL_BILL);
        OPERATOR_NEW.put("AIRTEL LAND LINE", AppConstants.OPERATOR_ID_AIRTEL_LAND_LINE);

        OPERATOR_NEW.put("BESCOM BANGALURU", AppConstants.OPERATOR_ID_BESCOM_BANGALURU);
        OPERATOR_NEW.put("BEST ELECTRICITY BILL", AppConstants.OPERATOR_ID_BEST_ELECTRICITY);
        OPERATOR_NEW.put("BSES RAJDHANI", AppConstants.OPERATOR_ID_BSES_RAJDHANI);
        OPERATOR_NEW.put("BSNL BILL PAY", AppConstants.OPERATOR_ID_BSNL_BILL_PAY);
        OPERATOR_NEW.put("CELLONE BILL PAY", AppConstants.OPERATOR_ID_CELLONE_BILL_PAY);
        OPERATOR_NEW.put("CESC LIMITED", AppConstants.OPERATOR_ID_CESC_LIMITED);
        OPERATOR_NEW.put("CESCOM MYSORE", AppConstants.OPERATOR_ID_CESCOM_MYSORE);
        OPERATOR_NEW.put("DHBVN HARYANA", AppConstants.OPERATOR_ID_DHBVN_HARYANA);
        OPERATOR_NEW.put("IDEA BILL", AppConstants.OPERATOR_ID_IDEA_BILL);
        OPERATOR_NEW.put("INDRAPRASTH GAS", AppConstants.OPERATOR_ID_INDRAPRASTHA_GAS);
        OPERATOR_NEW.put("MAHANAGAR GAS BILL", AppConstants.OPERATOR_ID_MAHANAGAR_GAS);
        OPERATOR_NEW.put("NORTH BIHAR ELECTRICITY", AppConstants.OPERATOR_ID_NBE);
        OPERATOR_NEW.put("RELIANCE BILL GSM", AppConstants.OPERATOR_ID_RELIANCE_BILL_GSM);
        OPERATOR_NEW.put("RELIANCE CDMA BILL", AppConstants.OPERATOR_ID_RELIANCE_BILL_CDMA);
        OPERATOR_NEW.put("RELIANCE ENERGY BILL", AppConstants.OPERATOR_ID_RELIANCE_ENERGY);
        OPERATOR_NEW.put("SOUTH BIHAR ELECTRICITY", AppConstants.OPERATOR_ID_SBE);
        OPERATOR_NEW.put("TATA BILL", AppConstants.OPERATOR_ID_TATA_BILL);
        OPERATOR_NEW.put("TATA POWER DELHI", AppConstants.OPERATOR_ID_TATA_POWER_DELHI);
        OPERATOR_NEW.put("TIKONA BILL PAYMENT", AppConstants.OPERATOR_ID_TIKONA_BILL);
        OPERATOR_NEW.put("UHBVN HARYANA", AppConstants.OPERATOR_ID_UHBVN_HARYANA);
        OPERATOR_NEW.put("VODAFONE BILL", AppConstants.OPERATOR_ID_VODAFONE_BILL);
    }

}
