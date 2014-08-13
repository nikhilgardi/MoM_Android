package com.mom.app.model.local;

import android.content.Context;

import java.io.Serializable;

/**
 * Created by vaibhavsinha on 7/5/14.
 */
public abstract class LocalStorage implements IStorage{

    protected static IStorage _instance;
    protected Context _context;

    public abstract void storeBoolean(String psKey, boolean pbValue);

    public abstract boolean getBoolean(String psKey, boolean pbDefault);

    public abstract void storeString(String psKey, String psValue);

    public abstract String getString(String psKey, String psDefault);

    public abstract void storeFloat(String psKey, float pValue);

    public abstract float getFloat(String psKey, float pfDefault);

    public abstract int getInt(String psKey, int pnDefault);

    public abstract void storeInt(String psKey, int pnValue);
}
