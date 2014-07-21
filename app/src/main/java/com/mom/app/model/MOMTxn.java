package com.mom.app.model;

import com.mom.app.error.MOMException;

import java.util.Date;

/**
 * Created by vaibhavsinha on 7/14/14.
 */
public class MOMTxn {
    public String transactionDate;
    public String subscriberId;
    public String transactionId;
    public String operator;
    public String amount;
    public String status;

    public MOMTxn(String sIntegratedValues, String sSeparator) throws MOMException{
        String[] sArr       = sIntegratedValues.split(sSeparator);

        if(sArr.length < 6){
            throw new MOMException();
        }

        transactionDate     = sArr[0];
        transactionId       = sArr[1];
        subscriberId        = sArr[2];
        amount              = sArr[3];
        operator            = sArr[4];
        status              = sArr[5];

    }
}
