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

    protected static String WSDL_TARGET_NAMESPACE = "http://localhost:4471/";

    protected static String SOAP_ADDRESS = "http://msvc.money-on-mobile.net/WebServiceV3Client.asmx";

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
        BALANCE_TRANSFER;

    }

    public void checkConnectivity(Context context){
        _connected      = ConnectionUtil.checkConnectivity(context);
        if(!_connected){
            ConnectionUtil.showConnectionWarning(context);
        }
    }

    public abstract void getBalance();
    public abstract void login(String userName, String password);
    public abstract void verifyTPin(String psTPin);
    public abstract void rechargeMobile(
                                    String psConsumerNumber,
                                    double pdAmount,
                                    String psOperator,
                                    int pnRechargeType
    );

    public abstract void rechargeDTH(
                                    String psSubscriberId,
                                    double pdAmount,
                                    String psOperator,
                                    String psCustomerMobile
    );

    public abstract void payBill(
                                String psSubscriberId,
                                double pdAmount,
                                String psOperatorId,
                                String psCustomerMobile,
                                String psConsumerName,
                                HashMap<String, String> psExtraParamsMap
    );

    public abstract void getBillAmount(String psOperatorId, String psSubscriberId);

    public abstract void getTransactionHistory();
    public abstract void changePin(PinType pinType, String psOldPin, String psNewPin);

    public abstract void retailerpayment( String psConsumerNumber,double pdAmount );

}
