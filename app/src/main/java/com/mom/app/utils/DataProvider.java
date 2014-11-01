package com.mom.app.utils;

import android.app.Activity;
import android.content.Context;

import com.mom.app.identifier.PlatformIdentifier;
import com.mom.app.model.Operator;
import com.mom.app.ui.flow.MoMScreen;
import com.mom.app.widget.holder.ImageItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vaibhavsinha on 7/21/14.
 */
public class DataProvider {

    public static ArrayList<ImageItem<MoMScreen>> getScreens(Context context, PlatformIdentifier platform, boolean showingDashboard) {
        final ArrayList<ImageItem<MoMScreen>> imageItems = new ArrayList<ImageItem<MoMScreen>>();
        if(!showingDashboard) {
            imageItems.add(
                    new ImageItem<MoMScreen>(
                            MoMScreen.DASHBOARD,
                            MoMScreen.DASHBOARD.id,
                            MoMScreen.DASHBOARD.drawableId,
                            MoMScreen.DASHBOARD.drawableTransparentId,
                            context.getResources().getString(MoMScreen.DASHBOARD.titleResId),
                            false
                    )
            );
        }
        imageItems.add(
                new ImageItem<MoMScreen>(
                        MoMScreen.MOBILE_RECHARGE,
                        MoMScreen.MOBILE_RECHARGE.id,
                        MoMScreen.MOBILE_RECHARGE.drawableId,
                        MoMScreen.MOBILE_RECHARGE.drawableTransparentId,
                        context.getResources().getString(MoMScreen.MOBILE_RECHARGE.titleResId),
                        false
                )
        );

        imageItems.add(
                new ImageItem<MoMScreen>(
                        MoMScreen.DTH_RECHARGE,
                        MoMScreen.DTH_RECHARGE.id,
                        MoMScreen.DTH_RECHARGE.drawableId,
                        MoMScreen.DTH_RECHARGE.drawableTransparentId,
                        context.getResources().getString(MoMScreen.DTH_RECHARGE.titleResId),
                        false
                )
        );

        imageItems.add(
                new ImageItem<MoMScreen>(
                        MoMScreen.BILL_PAYMENT,
                        MoMScreen.BILL_PAYMENT.id,
                        MoMScreen.BILL_PAYMENT.drawableId,
                        MoMScreen.BILL_PAYMENT.drawableTransparentId,
                        context.getResources().getString(MoMScreen.BILL_PAYMENT.titleResId),
                        false
                )
        );

        if(platform == PlatformIdentifier.MOM){
            imageItems.add(
                    new ImageItem<MoMScreen>(
                            MoMScreen.BALANCE_TRANSFER,
                            MoMScreen.BALANCE_TRANSFER.id,
                            MoMScreen.BALANCE_TRANSFER.drawableId,
                            MoMScreen.BALANCE_TRANSFER.drawableTransparentId,
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
                        MoMScreen.HISTORY.drawableTransparentId,
                        context.getResources().getString(MoMScreen.HISTORY.titleResId),
                        false
                )
        );

        if(!showingDashboard || platform == PlatformIdentifier.PBX) {
            imageItems.add(
                    new ImageItem<MoMScreen>(
                            MoMScreen.SETTINGS,
                            MoMScreen.SETTINGS.id,
                            MoMScreen.SETTINGS.drawableId,
                            MoMScreen.SETTINGS.drawableTransparentId,
                            context.getResources().getString(MoMScreen.SETTINGS.titleResId),
                            false
                    )
            );
        }

        imageItems.add(
                new ImageItem<MoMScreen>(
                        MoMScreen.LIC,
                        MoMScreen.LIC.id,
                        MoMScreen.LIC.drawableId,
                        MoMScreen.LIC.drawableTransparentId,
                        context.getResources().getString(MoMScreen.LIC.titleResId),
                        false
                )
        );

        imageItems.add(
                new ImageItem<MoMScreen>(
                        MoMScreen.LOGOUT,
                        MoMScreen.LOGOUT.id,
                        MoMScreen.LOGOUT.drawableId,
                        MoMScreen.LOGOUT.drawableTransparentId,
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
                        MoMScreen.CHANGE_MPIN.drawableTransparentId,
                        activity.getResources().getString(MoMScreen.CHANGE_MPIN.titleResId),
                        false
                )
        );

        imageItems.add(
                new ImageItem<MoMScreen>(
                        MoMScreen.CHANGE_TPIN,
                        MoMScreen.CHANGE_TPIN.id,
                        MoMScreen.CHANGE_TPIN.drawableId,
                        MoMScreen.CHANGE_TPIN.drawableTransparentId,
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
                        MoMScreen.CHANGE_PASSWORD.drawableTransparentId,
                        activity.getResources().getString(MoMScreen.CHANGE_PASSWORD.titleResId),
                        false
                )
        );


        return imageItems;
    }

    public static List<Operator> getMoMPlatformMobileOperators(){
        String[] operatorNames  = {"AIRCEL", "AIRTEL",
                "BSNL", "DATACOMM", "IDEA", "LOOP", "MOM CARD REFILL",
                "MTNL", "MTS", "QUE MOBILE", "RELIANCE CDMA",
                "RELIANCE GSM", "STEL", "TATA", "TATA DOCOMO",
                "TATA WALKY", "UNINOR", "VIRGIN", "VODAFONE"};

        return getOperators(operatorNames);
    }

    public static List<Operator> getOperators(String[] operatorNames){
        List<Operator> operators    = new ArrayList<Operator>();

        for(String opName:operatorNames){
            Operator operator   = new Operator();
            operator.name       = opName;
            operator.code       = AppConstants.OPERATOR_NEW.get(opName);
            operators.add(operator);
        }

        return operators;
    }

    public static List<Operator> getMoMPlatformDTHOperators(){
        String[] operatorNames  = {"AIRTEL DIGITAL" , "BIG TV" , "DISH",
                "SUN DIRECT" ,"TATA SKY", "VIDEOCON DTH"};

        return getOperators(operatorNames);
    }

    public static List<Operator> getMoMPlatformBillPayOperators(){
        String[] operatorNames  = {"AIRCEL BILL" , "AIRTEL BILL", "AIRTEL LAND LINE",
                "BESCOM BANGALURU", "BEST ELECTRICITY BILL" ,"BSES RAJDHANI" ,"BSNL BILL PAY" ,
                "CELLONE BILL PAY"," CESC LIMITED" ,"CESCOM MYSORE",
                "DHBVN HARYANA", "IDEA BILL" ,"INDRAPRASTH GAS" , "MAHANAGAR GAS BILL",
                "NORTH BIHAR ELECTRICITY", "RELIANCE BILL GSM" ,"RELIANCE CDMA BILL",
                "RELIANCE ENERGY BILL", "SOUTH BIHAR ELECTRICITY","TATA BILL",
                "TATA POWER DELHI", "TIKONA BILL PAYMENT","UHBVN HARYANA","VODAFONE BILL"};

        return getOperators(operatorNames);
    }
}
