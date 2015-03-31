package com.mom.apps.identifier;

import android.content.Context;
import android.util.Log;


import com.mom.apps.activity.BaseActivity;
import com.mom.apps.activity.LoginActivity;
import com.mom.apps.model.local.EphemeralStorage;
import com.mom.apps.utils.AppConstants;

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
