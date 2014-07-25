package com.mom.app.identifier;

import android.content.Context;
import android.util.Log;

import com.mom.app.activity.BillPaymentActivity;
import com.mom.app.activity.DTHRechargeActivity;
import com.mom.app.activity.DashboardActivity;
import com.mom.app.activity.MobileRechargeActivity;
import com.mom.app.activity.LoginActivity;
import com.mom.app.model.local.EphemeralStorage;
import com.mom.app.model.local.LocalStorage;
import com.mom.app.utils.MOMConstants;

/**
 * Created by vaibhavsinha on 7/7/14.
 */
public class IdentifierUtils {
    public static Class getActivityClass(ActivityIdentifier pIdentifier){
        if(pIdentifier == null){
            Log.e("Activity Identifier", "null activity identifier sent!");
            return DashboardActivity.class;
        }

        switch (pIdentifier){
            case LOGIN:
                return LoginActivity.class;
            case DASHBOARD:
                return DashboardActivity.class;
            case MOBILE_RECHARGE:
                return MobileRechargeActivity.class;
            case DTH_RECHARGE:
                return DTHRechargeActivity.class;
            case BILL_PAYMENT:
                return BillPaymentActivity.class;
            default:
                return DashboardActivity.class;
        }
    }

    public static PlatformIdentifier getPlatformIdentifier(Context context){
        String sPlatform        = EphemeralStorage.getInstance(context).getString(MOMConstants.ACTIVE_PLATFORM, null);
        if(PlatformIdentifier.PBX.toString().equals(sPlatform)){
            return PlatformIdentifier.PBX;
        }

        return PlatformIdentifier.NEW;
    }
}
