package com.mom.app.model;

import android.content.Context;

import com.mom.app.identifier.PinType;
import com.mom.app.utils.ConnectionUtil;

import java.util.HashMap;

public abstract class DataExImpl implements IDataEx{

    /*
    Default urls are of the New Platform. The actual Urls are to be set in the constructor
    of the respective platform implementations.
     */

    protected static String SOAP_ADDRESS        = "http://msvc.money-on-mobile.net/WebServiceV3Client.asmx";

    protected Context _applicationContext;
    protected AsyncListener _listener;
    protected boolean _connected;

    public enum Methods{
        LOGIN,
        VERIFY_TPIN,
        GET_BALANCE,
        RECHARGE_MOBILE,
        RECHARGE_DTH,
        PAY_BILL,
        GET_BILL_AMOUNT,
        TRANSACTION_HISTORY,
        CHANGE_PIN,
        CHECK_PLATFORM_DETAILS,
        GET_OPERATOR_NAMES,
        CHANGE_PASSWORD,
        BALANCE_TRANSFER;
    }

    public void checkConnectivity(Context context){
        _connected      = ConnectionUtil.checkConnectivity(context);
        if(!_connected){
            ConnectionUtil.showConnectionWarning(context);
        }
    }

}
