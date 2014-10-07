package com.mom.app.model.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.mom.app.R;
import com.mom.app.utils.MiscUtils;

import java.io.Serializable;

/**
 * Created by vaibhavsinha on 7/25/14.
 */
public class PersistentStorage extends LocalStorage {

    SharedPreferences _pref             = null;
    SharedPreferences.Editor _prefEditor = null;

    private PersistentStorage(Context context){
        _context    = context;
        _pref       = _context.getSharedPreferences(
                "com.mom.app.pref",
                Context.MODE_PRIVATE
        );
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
    public void storeBoolean(String psKey, boolean pbValue){
        _prefEditor.putBoolean(psKey, pbValue);
        _prefEditor.commit();
    }

    @Override
    public boolean getBoolean(String psKey, boolean pbDefault){
        return _pref.getBoolean(psKey, pbDefault);
    }

    @Override
    public void storeString(String psKey, String psValue){
        _prefEditor.putString(psKey, psValue);
        _prefEditor.commit();
    }

    @Override
    public String getString(String psKey, String psDefault){
        return _pref.getString(psKey, psDefault);
    }

    @Override
    public void storeFloat(String psKey, float pValue){
        _prefEditor.putFloat(psKey, pValue);
        _prefEditor.commit();
    }

    @Override
    public float getFloat(String psKey, float pfDefault) {
        return _pref.getFloat(psKey, pfDefault);
    }

    @Override
    public int getInt(String psKey, int pnDefault) {
        return _pref.getInt(psKey, pnDefault);
    }

    @Override
    public void storeInt(String psKey, int pnValue) {
        _prefEditor.putInt(psKey, pnValue);
        _prefEditor.commit();
    }

    @Override
    public void storeObject(String psKey, Object obj) {
        storeString(psKey, MiscUtils.toJson(obj));
    }

    @Override
    public Object getObject(String psKey, Object pDefault) {
        String sObj = getString(psKey, null);

        if(sObj == null || pDefault == null){
            return null;
        }

        return MiscUtils.fromJson(sObj, pDefault.getClass());
    }
}
