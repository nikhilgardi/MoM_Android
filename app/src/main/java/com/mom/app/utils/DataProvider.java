package com.mom.app.utils;

import android.app.Activity;

import com.mom.app.identifier.PlatformIdentifier;
import com.mom.app.ui.flow.MoMScreen;
import com.mom.app.widget.holder.ImageItem;

import java.util.ArrayList;

/**
 * Created by vaibhavsinha on 7/21/14.
 */
public class DataProvider {


//        public static ArrayList getScreens(Activity activity, PlatformIdentifier platform) {
//        final ArrayList imageItems = new ArrayList();


    public static ArrayList<ImageItem<MoMScreen>> getScreens(Activity activity, PlatformIdentifier platform) {
        final ArrayList<ImageItem<MoMScreen>> imageItems = new ArrayList<ImageItem<MoMScreen>>();


        imageItems.add(
                new ImageItem<MoMScreen>(
                        MoMScreen.MOBILE_RECHARGE,
                        MoMScreen.MOBILE_RECHARGE.id,
                        MoMScreen.MOBILE_RECHARGE.drawableId,
                        activity.getResources().getString(MoMScreen.MOBILE_RECHARGE.titleResId),
                        false
                )
        );

        imageItems.add(
                new ImageItem<MoMScreen>(
                        MoMScreen.DTH_RECHARGE,
                        MoMScreen.DTH_RECHARGE.id,
                        MoMScreen.DTH_RECHARGE.drawableId,
                        activity.getResources().getString(MoMScreen.DTH_RECHARGE.titleResId),
                        false
                )
        );

        imageItems.add(
                new ImageItem<MoMScreen>(
                        MoMScreen.BILL_PAYMENT,
                        MoMScreen.BILL_PAYMENT.id,
                        MoMScreen.BILL_PAYMENT.drawableId,
                        activity.getResources().getString(MoMScreen.BILL_PAYMENT.titleResId),
                        false
                )
        );

        if(platform == PlatformIdentifier.NEW){
            imageItems.add(
                    new ImageItem<MoMScreen>(

//                            MoMScreen.RETAILER_PAYMENT,
//                            MoMScreen.RETAILER_PAYMENT.id,
//                            MoMScreen.RETAILER_PAYMENT.drawableId,
//                            activity.getResources().getString(MoMScreen.RETAILER_PAYMENT.titleResId),

                            MoMScreen.BALANCE_TRANSFER,
                            MoMScreen.BALANCE_TRANSFER.id,
                            MoMScreen.BALANCE_TRANSFER.drawableId,
                            activity.getResources().getString(MoMScreen.BALANCE_TRANSFER.titleResId),

                            false
                    )
            );
        }

        imageItems.add(
                new ImageItem<MoMScreen>(
                        MoMScreen.HISTORY,
                        MoMScreen.HISTORY.id,
                        MoMScreen.HISTORY.drawableId,
                        activity.getResources().getString(MoMScreen.HISTORY.titleResId),
                        false
                )
        );
        imageItems.add(
                new ImageItem<MoMScreen>(
                        MoMScreen.SETTINGS,
                        MoMScreen.SETTINGS.id,
                        MoMScreen.SETTINGS.drawableId,
                        activity.getResources().getString(MoMScreen.SETTINGS.titleResId),
                        false
                )
        );
        imageItems.add(
                new ImageItem<MoMScreen>(
                        MoMScreen.LOGOUT,
                        MoMScreen.LOGOUT.id,
                        MoMScreen.LOGOUT.drawableId,
                        activity.getResources().getString(MoMScreen.LOGOUT.titleResId),
                        false
                )
        );

        return imageItems;
    }

    public static ArrayList<ImageItem<MoMScreen>> getSettingsScreens(Activity activity) {
        final ArrayList<ImageItem<MoMScreen>> imageItems = new ArrayList<ImageItem<MoMScreen>>();

        imageItems.add(
                new ImageItem<MoMScreen>(
                        MoMScreen.CHANGE_MPIN,
                        MoMScreen.CHANGE_MPIN.id,
                        MoMScreen.CHANGE_MPIN.drawableId,
                        activity.getResources().getString(MoMScreen.CHANGE_MPIN.titleResId),
                        false
                )
        );

        imageItems.add(
                new ImageItem<MoMScreen>(
                        MoMScreen.CHANGE_TPIN,
                        MoMScreen.CHANGE_TPIN.id,
                        MoMScreen.CHANGE_TPIN.drawableId,
                        activity.getResources().getString(MoMScreen.CHANGE_TPIN.titleResId),
                        false
                )
        );

        return imageItems;
    }
}
