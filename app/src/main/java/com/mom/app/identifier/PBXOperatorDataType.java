package com.mom.app.identifier;

/**
 * Created by vaibhavsinha on 12/23/14.
 */
public enum PBXOperatorDataType {
    TOPUP(1), DTH(2), BILL_PAYMENT(3), UBP(4);

    public int code;

    private PBXOperatorDataType(int code){
        this.code = code;
    }
}
