package com.mom.app.test.model;

import com.mom.app.model.AsyncListener;
import com.mom.app.model.AsyncResult;
import com.mom.app.model.DataExImpl;

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
