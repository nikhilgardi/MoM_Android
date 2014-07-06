package com.mom.app.model;

import org.apache.http.NameValuePair;

import java.util.List;

/**
 * Created by vaibhavsinha on 7/5/14.
 */
public interface IDataEx {
    public double getBalance();
    public void login(NameValuePair...params);
}
