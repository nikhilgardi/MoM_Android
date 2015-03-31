package com.mom.apps.test;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.test.mock.MockContext;

/**
 * Created by vaibhavsinha on 10/27/14.
 */
public class MoMMockContext extends MockContext{
    private Context mDelegatedContext;
    private static final String PREFIX = "test.";

    public MoMMockContext(Context context) {
        mDelegatedContext = context;
    }

    @Override
    public String getPackageName(){
        return PREFIX;
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
    public Object getSystemService(String service){
        return mDelegatedContext.getSystemService(service);
    }
}
