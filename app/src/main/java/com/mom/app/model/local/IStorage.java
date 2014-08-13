package com.mom.app.model.local;

import android.content.Context;

import java.io.Serializable;

/**
 * Created by vaibhavsinha on 7/25/14.
 */
public interface IStorage {

    void storeBoolean(String psKey, boolean pbValue);
    boolean getBoolean(String psKey, boolean pbDefault);
    void storeString(String psKey, String psValue);
    String getString(String psKey, String psDefault);
    void storeFloat(String psKey, float pValue);
    float getFloat(String psKey, float pfDefault);
    void storeInt(String psKey, int pnValue);
    int getInt(String psKey, int pnDefault);
    void clear();
}
