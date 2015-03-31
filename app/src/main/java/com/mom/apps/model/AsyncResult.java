package com.mom.apps.model;

/**
 * Created by vaibhavsinha on 7/10/14.
 */
public class AsyncResult {
    public enum CODE{
        SUCCESS, GENERAL_FAILURE, INVALID_PARAMETERS,
        NOT_LOGGED_IN;
    }

    public CODE code;
    public String message;

    public AsyncResult(CODE pCode){
        this.code       = pCode;
        this.message    = "";
    }

    public AsyncResult(CODE pCode, String psMsg){
        this.code      = pCode;
        this.message   = psMsg;
    }
}
