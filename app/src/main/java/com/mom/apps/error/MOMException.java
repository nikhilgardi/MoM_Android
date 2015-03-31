package com.mom.apps.error;

import com.mom.apps.model.AsyncResult;

/**
 * Created by vaibhavsinha on 7/7/14.
 */
public class MOMException extends Exception {
    private AsyncResult.CODE code   = AsyncResult.CODE.GENERAL_FAILURE;

    public MOMException(String psMsg){
        super(psMsg);
    }

    public MOMException() {

    }

    public MOMException(AsyncResult.CODE code){
        this.code = code;
    }

    public AsyncResult.CODE getCode(){
        return code;
    }
}
