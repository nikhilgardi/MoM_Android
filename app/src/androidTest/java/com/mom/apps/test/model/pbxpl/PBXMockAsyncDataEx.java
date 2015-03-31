package com.mom.apps.test.model.pbxpl;

import com.mom.apps.model.AsyncListener;
import com.mom.apps.model.AsyncResult;
import com.mom.apps.model.DataExImpl;
import com.mom.apps.test.model.MockAsyncDataEx;
import com.mom.apps.ui.TransactionRequest;

/**
 * Created by vaibhavsinha on 10/13/14.
 */
public class PBXMockAsyncDataEx extends MockAsyncDataEx {

    public PBXMockAsyncDataEx() {
        super(new AsyncListener<TransactionRequest>() {
            @Override
            public void onTaskSuccess(TransactionRequest result, DataExImpl.Methods callback) {

            }

            @Override
            public void onTaskError(AsyncResult pResult, DataExImpl.Methods callback) {

            }
        });
    }

    public String getAllOperatorNames(){
        return "{\n" +
                "\"code\":0,\n" +
                "\"data\":\n" +
                "[\n" +
                "\n" +
                "{\"code\":\"CEL\",\"name\":\"AIRCEL\"},\n" +
                "{\"code\":\"AIR\",\"name\":\"AIRTEL\"},\n" +
                "{\"code\":\"BST\",\"name\":\"BSNL\"}\n" +
                "]\n" +
                "}\n";
    }
}
