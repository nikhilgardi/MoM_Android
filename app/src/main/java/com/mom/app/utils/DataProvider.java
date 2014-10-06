package com.mom.app.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.mom.app.R;
import com.mom.app.identifier.PlatformIdentifier;
import com.mom.app.ui.flow.MoMScreen;
import com.mom.app.widget.holder.ImageItem;

import java.util.ArrayList;

/**
 * Created by vaibhavsinha on 7/21/14.
 */
public class DataProvider {
    public static ArrayList getScreens(Activity activity, PlatformIdentifier platform) {
        final ArrayList imageItems = new ArrayList();

        imageItems.add(
                new ImageItem(
                        MoMScreen.MOBILE_RECHARGE.id,
                        MoMScreen.MOBILE_RECHARGE.drawableId,
                        activity.getResources().getString(MoMScreen.MOBILE_RECHARGE.titleResId),
                        false
                )
        );

        imageItems.add(
                new ImageItem(
                        MoMScreen.DTH_RECHARGE.id,
                        MoMScreen.DTH_RECHARGE.drawableId,
                        activity.getResources().getString(MoMScreen.DTH_RECHARGE.titleResId),
                        false
                )
        );

        imageItems.add(
                new ImageItem(
                        MoMScreen.BILL_PAYMENT.id,
                        MoMScreen.BILL_PAYMENT.drawableId,
                        activity.getResources().getString(MoMScreen.BILL_PAYMENT.titleResId),
                        false
                )
        );

        if(platform == PlatformIdentifier.NEW){
            imageItems.add(
                    new ImageItem(
                            MoMScreen.RETAILER_PAYMENT.id,
                            MoMScreen.RETAILER_PAYMENT.drawableId,
                            activity.getResources().getString(MoMScreen.RETAILER_PAYMENT.titleResId),
                            false
                    )
            );
        }

        imageItems.add(
                new ImageItem(
                        MoMScreen.HISTORY.id,
                        MoMScreen.HISTORY.drawableId,
                        activity.getResources().getString(MoMScreen.HISTORY.titleResId),
                        false
                )
        );
        imageItems.add(
                new ImageItem(
                        MoMScreen.SETTINGS.id,
                        MoMScreen.SETTINGS.drawableId,
                        activity.getResources().getString(MoMScreen.SETTINGS.titleResId),
                        false
                )
        );
        imageItems.add(
                new ImageItem(
                        MoMScreen.LOGOUT.id,
                        MoMScreen.LOGOUT.drawableId,
                        activity.getResources().getString(MoMScreen.LOGOUT.titleResId),
                        false
                )
        );

        return imageItems;
    }
}
