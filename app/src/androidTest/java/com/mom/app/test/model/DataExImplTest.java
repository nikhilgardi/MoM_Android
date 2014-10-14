package com.mom.app.test.model;

import android.test.mock.MockContext;

import com.mom.app.model.AsyncDataEx;
import com.mom.app.model.AsyncListener;
import com.mom.app.model.AsyncResult;
import com.mom.app.model.DataExImpl;
import com.mom.app.model.pbxpl.Balance;
import com.mom.app.model.pbxpl.PBXPLDataExImpl;

import static junit.framework.Assert.assertEquals;

/**
 * Created by vaibhavsinha on 7/9/14.
 */
public class DataExImplTest implements AsyncListener{

    @Override
    public void onTaskSuccess(Object result, DataExImpl.Methods callback) {
        switch (callback){
            case GET_OPERATOR_NAMES:

                break;
        }
    }

    @Override
    public void onTaskError(AsyncResult pResult, DataExImpl.Methods callback) {

    }

    public void testExtractJSONBalance(){


    }

}
