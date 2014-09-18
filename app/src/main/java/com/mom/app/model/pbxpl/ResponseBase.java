package com.mom.app.model.pbxpl;

/**
 * Created by vaibhavsinha on 9/16/14.
 */
public class ResponseBase<T> {
    public int status;
    public String message;
    public ResponseData<T> response;
}
