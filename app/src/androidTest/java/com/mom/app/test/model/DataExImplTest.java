package com.mom.app.test.model;

import com.mom.app.model.pbxpl.Balance;
import com.mom.app.model.pbxpl.PBXPLDataExImpl;

import static junit.framework.Assert.assertEquals;

/**
 * Created by vaibhavsinha on 7/9/14.
 */
public class DataExImplTest {

    public void testExtractJSONBalance(){
        Balance balance = new PBXPLDataExImpl().extractJSONBalance();
        assertEquals(balance.balance, 10);
    }
}
