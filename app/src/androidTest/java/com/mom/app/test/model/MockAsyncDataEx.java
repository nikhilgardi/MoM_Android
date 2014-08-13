package com.mom.app.test.model;

import android.os.AsyncTask;

import com.mom.app.model.AsyncListener;
import com.mom.app.model.DataExImpl;

import org.apache.http.NameValuePair;

/**
 * Created by vaibhavsinha on 7/9/14.
 */
public class MockAsyncDataEx  extends AsyncTask<NameValuePair, Integer, String>  {
    AsyncListener _callback;
    String _url;
    DataExImpl.Methods _callbackData;

    public MockAsyncDataEx(AsyncListener pListener, String psUrl, DataExImpl.Methods pCallbackData){
        this._url			= psUrl;
        this._callback		= pListener;
        this._callbackData  = pCallbackData;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

    @Override
    protected String doInBackground(NameValuePair... nameValuePairs) {
        return null;
    }
}
