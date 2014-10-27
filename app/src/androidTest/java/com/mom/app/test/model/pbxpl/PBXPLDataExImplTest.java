package com.mom.app.test.model.pbxpl;

import android.test.mock.MockContext;

import com.mom.app.error.MOMException;
import com.mom.app.model.AsyncListener;
import com.mom.app.model.AsyncResult;
import com.mom.app.model.DataExImpl;
import com.mom.app.model.pbxpl.PBXPLDataExImpl;
import com.mom.app.test.model.MockAsyncDataEx;

import junit.framework.TestCase;

/**
 * Created by vaibhavsinha on 10/13/14.
 */
public class PBXPLDataExImplTest extends TestCase implements AsyncListener{
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

    public void testGetAllOperatorNames() throws MOMException{
        PBXPLDataExImpl dataEx = PBXPLDataExImpl.getInstance(new MockContext(), this, DataExImpl.Methods.GET_OPERATOR_NAMES);
        dataEx.getOperatorNames();
    }
}
