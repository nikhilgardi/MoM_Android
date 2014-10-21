package com.mom.app.identifier;

import android.content.Context;
import android.util.Log;


import com.mom.app.activity.BaseActivity;
import com.mom.app.activity.LoginActivity;
import com.mom.app.model.local.EphemeralStorage;
import com.mom.app.utils.AppConstants;

/**
 * Created by vaibhavsinha on 7/7/14.
 */
public class IdentifierUtils {
    public static Class getActivityClass(ActivityIdentifier pIdentifier){
        if(pIdentifier == null){
            Log.e("Activity Identifier", "null activity identifier sent!");
            return BaseActivity.class;
        }

        switch (pIdentifier){
            case LOGIN:
                return LoginActivity.class;
            default:
                return BaseActivity.class;
        }
    }

    public static PlatformIdentifier getPlatformIdentifier(Context context){
        PlatformIdentifier platform = (PlatformIdentifier) EphemeralStorage.getInstance(context).getObject(
                AppConstants.ACTIVE_PLATFORM, null
        );

        return platform;
    }
}
