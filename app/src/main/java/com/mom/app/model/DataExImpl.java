package com.mom.app.model;

import android.content.Context;
import android.util.Log;

import java.io.InputStream;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public abstract class DataExImpl implements IDataEx{

    /*
    Default urls are of the New Platform. The actual Urls are to be set in the constructor
    of the respective platform implementations.
     */

    protected static String WSDL_TARGET_NAMESPACE = "http://localhost:4471/";

    protected static String SOAP_ADDRESS = "http://msvc.money-on-mobile.net/WebServiceV3Client.asmx";

    protected Context _applicationContext;
    protected AsyncListener _listener;

    public enum Methods{
        LOGIN;
    }

    public abstract double getBalance();
    public abstract void login(NameValuePair...param);


    public SoapObject getResponse(String psMethod, HashMap<String, String> pParamsMap){
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, psMethod);

        if(pParamsMap != null){
            for(String key:pParamsMap.keySet()){
                request.addProperty(getProperty(key, pParamsMap.get(key)));
            }
        }

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);

        try{
            Log.i("DataExImpl", "Calling service");
            httpTransport.call(SOAP_ADDRESS + "/" + psMethod, envelope);
            Log.i("DataExImpl", "Called service: " + envelope.bodyIn);

            if (envelope.bodyIn instanceof SoapObject) { // SoapObject = SUCCESS
                SoapObject soapObject = (SoapObject) envelope.bodyIn;
                Log.i("DataExImpl", "Obtained response: " + soapObject);
                return soapObject;
            } else if (envelope.bodyIn instanceof SoapFault) { // SoapFault = FAILURE
                SoapFault soapFault = (SoapFault) envelope.bodyIn;
                Log.i("DataExImpl", "Obtained Error: " + soapFault);
                Log.i("DataExImpl", "Dump: " + httpTransport.responseDump);
                throw new Exception(soapFault.getMessage());
            }

        }catch (Exception exception){
            exception.printStackTrace();
        }
        return null;
    }

    public PropertyInfo getProperty(String psName, String psValue){
        PropertyInfo property = new PropertyInfo();
        property.setNamespace(WSDL_TARGET_NAMESPACE);
        property.setName(psName);
        property.setValue(psValue);

        return property;
    }
}
