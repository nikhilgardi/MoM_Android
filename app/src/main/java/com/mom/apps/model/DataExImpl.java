package com.mom.apps.model;

import android.content.Context;

import com.mom.apps.utils.ConnectionUtil;

public abstract class DataExImpl implements IDataEx{

    /*
    Default urls are of the New Platform. The actual Urls are to be set in the constructor
    of the respective platform implementations.
     */


    protected Context _applicationContext;
    public static AsyncListener _listener;
    protected boolean _connected;


    public enum Methods{
        LOGIN,
        VERIFY_TPIN,
        GET_BALANCE,
        SIGN_UP_ENCRYPT_DATA,
        SIGN_UP_CONSUMER,
        RECHARGE_MOBILE,
        RECHARGE_DTH,
        PAY_BILL,
        UTILITY_BILL_PAY,
        IMPS_CUSTOMER_REGISTRATION,
        IMPS_CUSTOMER_REGISTRATION_STATUS,
        IMPS_BENEFICIARY_LIST,
        IMPS_CREATE_CUSTOMER_REGISTRATION,
        IMPS_AUTHENTICATION,
        IMPS_AUTHENTICATION_MOM,
        IMPS_CHECK_KYC,
        IMPS_ADD_BENEFICIARY,
        IMPS_BENEFICIARY_DETAILS,
        IMPS_BANK_NAME_LIST,
        IMPS_STATE_NAME,
        IMPS_CITY_NAME,
        IMPS_BRANCH_NAME,
        IMPS_VERIFY_PROCESS,
        IMPS_VERIFY_PAYMENT,
        IMPS_PAYMENT_PROCESS,
        IMPS_CONFIRM_PAYMENT,
        IMPS_MOM_SUBMIT_PROCESS,
        IMPS_MOM_CONFIRM_PROCESS,
        IMPS_MOM_SUBMIT_PAY_PROCESS,
        IMPS_MOM_CONFIRM_PAY_PROCESS,
        MOM_T_PIN,
        GET_BILL_AMOUNT,
        TRANSACTION_HISTORY,
        CHANGE_PIN,
        CHECK_PLATFORM_DETAILS,
        GET_OPERATOR_NAMES,
        CHANGE_PASSWORD,
        BALANCE_TRANSFER,
        LIC,
        PAY_LIC;
    }

    public void checkConnectivity(Context context){
        _connected      = ConnectionUtil.checkConnectivity(context);
        if(!_connected){
            ConnectionUtil.showConnectionWarning(context);
        }
    }

    protected AsyncListener getCallbackListener(){
        return _listener;
    }

    public void setListener(AsyncListener listener){
        _listener   = listener;
    }
}
