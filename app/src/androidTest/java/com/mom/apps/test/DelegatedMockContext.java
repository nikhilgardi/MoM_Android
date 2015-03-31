package com.mom.apps.test;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.test.mock.MockContext;

/**
 * Created by vaibhavsinha on 11/1/14.
 */

public class DelegatedMockContext extends MockContext {

    private Context mDelegatedContext;
    private static final String PREFIX = "test.";

    public DelegatedMockContext(Context context) {
        mDelegatedContext = context;
    }

    @Override
    public String getPackageName(){
        return "com.mom.app";
    }

    @Override
    public SharedPreferences getSharedPreferences(String name, int mode) {
        return mDelegatedContext.getSharedPreferences(name, mode);
    }

    @Override
    public Resources getResources(){
        return mDelegatedContext.getResources();
    }

    @Override
    public Object getSystemService(String name) {
        return mDelegatedContext.getSystemService(name);
    }

    @Override
    public PackageManager getPackageManager() {
        return mDelegatedContext.getPackageManager();
    }
}