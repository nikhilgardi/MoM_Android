package com.mom.app.test.model.pbxpl;

import android.test.AndroidTestCase;

import com.mom.app.error.MOMException;
import com.mom.app.model.AsyncListener;
import com.mom.app.model.AsyncResult;
import com.mom.app.model.DataExImpl;
import com.mom.app.model.pbxpl.PBXPLDataExImpl;
import com.mom.app.test.DelegatedMockContext;

/**
 * Created by vaibhavsinha on 10/13/14.
 */
public class PBXPLDataExImplTest extends AndroidTestCase implements AsyncListener{
    @Override
    public void onTaskSuccess(Object result, DataExImpl.Methods callback) {
        switch (callback){
            case GET_OPERATOR_NAMES:

                break;
            case LIC:

                break;
        }
    }

    @Override
    public void onTaskError(AsyncResult pResult, DataExImpl.Methods callback) {

    }

    public void testGetAllOperatorNames() throws MOMException{
//        PBXPLDataExImpl dataEx = PBXPLDataExImpl.getInstance(
//                new DelegatedMockContext(getContext()),
//                this,
//                DataExImpl.Methods.GET_OPERATOR_NAMES
//        );
//
//        dataEx.getOperatorNames();
    }

    public void testLICResponse() throws MOMException{
        String resp     = "{\"code\":0,\"data\":{\"something\":\"got it\", \"TXLife\":{\"TXLifeResponse\":{\"TransRefGUID\":\"2014MOM5555525202724\",\"TransInvGUID\":\"LICI2012000000002445\",\"TransInvAmount\":\"201.0\",\"MaxOccurs\":\"1\",\"TransType\":\"M\",\"TransMode\":\"Invoice\",\"TransExeDate\":\"2014-11-01\",\"TransExeTime\":\"18:26:12\",\"OLife\":{\"Party\":{\"FullName\":\"Yella Mangathayaru\",\"Address\":{\"Address1\":null,\"Address2\":null,\"Address3\":null}},\"Policy\":{\"PolicyNo\":\"806000021\",\"ShortName\":\"Yella Mangathayaru\",\"ServiceBranch\":\"R080\",\"AgencyCode\":null,\"InstalmentPremium\":\"100.0\",\"PaymentMode\":\"1\",\"FrUnpaidPremiumDate\":\"27/09/2014\",\"ToUnpaidPremiumDate\":\"27/10/2014\",\"InstalmentNo\":\"2\",\"TotalPremium\":\"200.0\",\"LateFee\":\"1.0\",\"ServiceTax\":\"0.0\",\"EducationCess\":\"0.0\",\"TotalAmount\":\"201.0\",\"Plan\":null,\"Term\":null,\"NextUnpaidPremiumDate\":\"27/11/2014\"}}}}}}";
        PBXPLDataExImpl dataEx = PBXPLDataExImpl.getInstance(new DelegatedMockContext(getContext()), this, DataExImpl.Methods.LOGIN);

        Float amount    = dataEx.getPremiumAmount(resp);
    }
}
