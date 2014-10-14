package com.mom.app.test.model.pbxpl;

import com.mom.app.model.AsyncListener;
import com.mom.app.test.model.MockAsyncDataEx;

/**
 * Created by vaibhavsinha on 10/13/14.
 */
public class PBXMockAsyncDataEx extends MockAsyncDataEx {

    public PBXMockAsyncDataEx() {
        super();
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
