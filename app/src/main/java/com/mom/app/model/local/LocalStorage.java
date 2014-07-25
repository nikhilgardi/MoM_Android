package com.mom.app.model.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.mom.app.utils.MOMConstants;

/**
 * Created by vaibhavsinha on 7/5/14.
 */
public abstract class LocalStorage implements IStorage{

    protected static IStorage _instance;
    protected Context _context;

    public abstract void storeLocally(String psKey, boolean pbValue);

    public abstract boolean getBoolean(String psKey, boolean pbDefault);

    public abstract void storeLocally(String psKey, String psValue);

    public abstract String getString(String psKey, String psDefault);

    public abstract void storeLocally(String psKey, float pValue);

    public abstract float getFloat(String psKey, float pfDefault);
}
