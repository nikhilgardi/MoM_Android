package com.mom.app.model.local;


import android.content.Context;

import java.util.HashMap;

/**
 * Created by vaibhavsinha on 7/25/14.
 */
public class EphemeralStorage extends LocalStorage {
    HashMap map = new HashMap();
    private EphemeralStorage(Context context){
        _context    = context;
    }

    public static IStorage getInstance(Context context){
        if(_instance == null){
            _instance       = new EphemeralStorage(context);
        }

        return _instance;
    }

    @Override
    public void clear(){
        map.clear();
    }
    @Override
    public void storeBoolean(String psKey, boolean pbValue){
        map.put(psKey, pbValue);
    }

    @Override
    public boolean getBoolean(String psKey, boolean pbDefault){
        Object val = map.get(psKey);
        return val == null ? pbDefault : (Boolean) val;
    }

    @Override
    public void storeString(String psKey, String psValue){
        map.put(psKey, psValue);
    }

    @Override
    public String getString(String psKey, String psDefault){
        Object val = map.get(psKey);
        return val == null ? psDefault : (String) val;
    }

    @Override
    public void storeFloat(String psKey, float pValue){
        map.put(psKey, pValue);
    }

    @Override
    public float getFloat(String psKey, float pfDefault){
        Object val = map.get(psKey);
        return val == null ? pfDefault : (Float) val;
    }

    @Override
    public int getInt(String psKey, int pnDefault) {
        Object val = map.get(psKey);
        return val == null ? pnDefault : (Integer) val;
    }

    @Override
    public void storeInt(String psKey, int pnValue) {
        map.put(psKey, pnValue);
    }

    @Override
    public void storeObject(String psKey, Object obj) {
        map.put(psKey, obj);
    }

    @Override
    public Object getObject(String psKey, Object pDefault) {
        Object val = map.get(psKey);
        return val == null ? pDefault : (Integer) val;
    }
}
