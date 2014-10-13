package com.mom.app.utils;

import android.app.Activity;
import android.content.Context;

import com.mom.app.identifier.PlatformIdentifier;
import com.mom.app.ui.flow.MoMScreen;
import com.mom.app.widget.holder.ImageItem;

import java.util.ArrayList;

/**
 * Created by vaibhavsinha on 7/21/14.
 */
public class DataProvider {

    public static ArrayList<ImageItem<MoMScreen>> getScreens(Context context, PlatformIdentifier platform) {
        final ArrayList<ImageItem<MoMScreen>> imageItems = new ArrayList<ImageItem<MoMScreen>>();

        imageItems.add(
                new ImageItem<MoMScreen>(
                        MoMScreen.MOBILE_RECHARGE,
                        MoMScreen.MOBILE_RECHARGE.id,
                        MoMScreen.MOBILE_RECHARGE.drawableId,
                        context.getResources().getString(MoMScreen.MOBILE_RECHARGE.titleResId),
                        false
                )
        );

        imageItems.add(
                new ImageItem<MoMScreen>(
                        MoMScreen.DTH_RECHARGE,
                        MoMScreen.DTH_RECHARGE.id,
                        MoMScreen.DTH_RECHARGE.drawableId,
                        context.getResources().getString(MoMScreen.DTH_RECHARGE.titleResId),
                        false
                )
        );

        imageItems.add(
                new ImageItem<MoMScreen>(
                        MoMScreen.BILL_PAYMENT,
                        MoMScreen.BILL_PAYMENT.id,
                        MoMScreen.BILL_PAYMENT.drawableId,
                        context.getResources().getString(MoMScreen.BILL_PAYMENT.titleResId),
                        false
                )
        );

        if(platform == PlatformIdentifier.NEW){
            imageItems.add(
                    new ImageItem<MoMScreen>(
                            MoMScreen.BALANCE_TRANSFER,
                            MoMScreen.BALANCE_TRANSFER.id,
                            MoMScreen.BALANCE_TRANSFER.drawableId,
                            context.getResources().getString(MoMScreen.BALANCE_TRANSFER.titleResId),
                            false
                    )
            );
        }

        imageItems.add(
                new ImageItem<MoMScreen>(
                        MoMScreen.HISTORY,
                        MoMScreen.HISTORY.id,
                        MoMScreen.HISTORY.drawableId,
                        context.getResources().getString(MoMScreen.HISTORY.titleResId),
                        false
                )
        );
        imageItems.add(
                new ImageItem<MoMScreen>(
                        MoMScreen.SETTINGS,
                        MoMScreen.SETTINGS.id,
                        MoMScreen.SETTINGS.drawableId,
                        context.getResources().getString(MoMScreen.SETTINGS.titleResId),
                        false
                )
        );
        imageItems.add(
                new ImageItem<MoMScreen>(
                        MoMScreen.LOGOUT,
                        MoMScreen.LOGOUT.id,
                        MoMScreen.LOGOUT.drawableId,
                        context.getResources().getString(MoMScreen.LOGOUT.titleResId),
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

    public static ArrayList<ImageItem<MoMScreen>> getPBXSettingsScreens(Activity activity) {
        final ArrayList<ImageItem<MoMScreen>> imageItems = new ArrayList<ImageItem<MoMScreen>>();

        imageItems.add(
                new ImageItem<MoMScreen>(
                        MoMScreen.CHANGE_PASSWORD,
                        MoMScreen.CHANGE_PASSWORD.id,
                        MoMScreen.CHANGE_PASSWORD.drawableId,
                        activity.getResources().getString(MoMScreen.CHANGE_PASSWORD.titleResId),
                        false
                )
        );


        return imageItems;
    }
}
