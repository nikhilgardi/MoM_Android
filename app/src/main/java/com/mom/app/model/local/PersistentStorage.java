package com.mom.app.model.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by vaibhavsinha on 7/25/14.
 */
public class PersistentStorage extends LocalStorage {

    SharedPreferences _pref             = null;
    SharedPreferences.Editor _prefEditor = null;

    private PersistentStorage(Context context){
        _context    = context;
        _pref = PreferenceManager.getDefaultSharedPreferences(_context);
        _prefEditor = _pref.edit();
    }

    public static IStorage getInstance(Context context){
        if(_instance == null){
            _instance       = new PersistentStorage(context);
        }

        return _instance;
    }

    @Override
    public void clear(){
        _pref.edit().clear();
    }

    @Override
    public void storeLocally(String psKey, boolean pbValue){
        _prefEditor.putBoolean(psKey, pbValue);
        _prefEditor.commit();
    }

    @Override
    public boolean getBoolean(String psKey, boolean pbDefault){
        return _pref.getBoolean(psKey, pbDefault);
    }

    @Override
    public void storeLocally(String psKey, String psValue){
        _prefEditor.putString(psKey, psValue);
        _prefEditor.commit();
    }

    @Override
    public String getString(String psKey, String psDefault){
        return _pref.getString(psKey, null);
    }

    @Override
    public void storeLocally(String psKey, float pValue){
        _prefEditor.putFloat(psKey, pValue);
        _prefEditor.commit();
    }

    @Override
    public float getFloat(String psKey, float pfDefault) {
        return _pref.getFloat(psKey, pfDefault);
    }
}
