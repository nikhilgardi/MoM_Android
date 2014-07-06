package com.mom.app.model.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.mom.app.utils.MOMConstants;

/**
 * Created by vaibhavsinha on 7/5/14.
 */
public class LocalStorage {

    public static void storeLocally(Context context, String psKey, boolean pbValue){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefEditor = pref.edit();
        prefEditor.putBoolean(psKey, pbValue);
        prefEditor.commit();
    }

    public static boolean getBoolean(Context context, String psKey){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getBoolean(psKey, false);
    }

    public static void storeLocally(Context context, String psKey, String psValue){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefEditor = pref.edit();
        prefEditor.putString(psKey, psValue);
        prefEditor.commit();
    }

    public static String getString(Context context, String psKey){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getString(psKey, null);
    }
}
