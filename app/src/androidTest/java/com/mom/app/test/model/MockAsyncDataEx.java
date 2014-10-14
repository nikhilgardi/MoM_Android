package com.mom.app.test.model;

import android.os.AsyncTask;

import com.mom.app.model.AsyncDataEx;
import com.mom.app.model.AsyncListener;
import com.mom.app.model.DataExImpl;

import org.apache.http.NameValuePair;

/**
 * Created by vaibhavsinha on 7/9/14.
 */
public abstract class MockAsyncDataEx  extends AsyncDataEx{

    public MockAsyncDataEx(){
        super();
    }

    public abstract String getAllOperatorNames();

    @Override
    protected String doInBackground(NameValuePair... nameValuePairs) {
        switch(_callbackData){
            case GET_OPERATOR_NAMES:
                return getAllOperatorNames();
        }

        return null;
    }


}
