package com.mom.app.test.model.pbxpl;

import com.mom.app.model.AsyncListener;
import com.mom.app.model.AsyncResult;
import com.mom.app.model.DataExImpl;
import com.mom.app.test.model.MockAsyncDataEx;
import com.mom.app.ui.TransactionRequest;

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
