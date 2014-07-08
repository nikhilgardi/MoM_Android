package com.mom.app.identifier;

import android.util.Log;

import com.mom.app.HomeActivity;
import com.mom.app.LoginActivity;
import com.mom.app.MainActivity;

/**
 * Created by vaibhavsinha on 7/7/14.
 */
public class IdentifierUtils {
    public static Class getActivityClass(ActivityIdentifier pIdentifier){
        if(pIdentifier == null){
            Log.e("Activity Identifier", "null activity identifier sent!");
            return MainActivity.class;
        }

        switch (pIdentifier){
            case LOGIN:
                return LoginActivity.class;
            case MAIN:
                return MainActivity.class;
            case HOME:
                return HomeActivity.class;
            default:
                return MainActivity.class;
        }
    }
}
