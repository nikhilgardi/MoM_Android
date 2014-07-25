package com.mom.app.model.local;

import android.content.Context;

/**
 * Created by vaibhavsinha on 7/25/14.
 */
public interface IStorage {

    void storeLocally(String psKey, boolean pbValue);
    boolean getBoolean(String psKey, boolean pbDefault);
    void storeLocally(String psKey, String psValue);
    String getString(String psKey, String psDefault);
    void storeLocally(String psKey, float pValue);
    float getFloat(String psKey, float pfDefault);
    void clear();
}
