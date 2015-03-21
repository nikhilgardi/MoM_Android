package com.mom.app.utils;

import android.app.Activity;
import android.content.Context;

import com.mom.app.identifier.PlatformIdentifier;
import com.mom.app.model.Operator;
import com.mom.app.model.local.EphemeralStorage;
import com.mom.app.ui.flow.MoMScreen;
import com.mom.app.widget.holder.ImageItem;

import java.util.ArrayList;
import java.util.HashMap;
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

        if( platform == PlatformIdentifier.PBX){
            imageItems.add(
                    new ImageItem<MoMScreen>(
                            MoMScreen.UTILITY_BILL_PAYMENT,
                            MoMScreen.UTILITY_BILL_PAYMENT.id,
                            MoMScreen.UTILITY_BILL_PAYMENT.drawableId,
                            MoMScreen.UTILITY_BILL_PAYMENT.drawableTransparentId,
                            context.getResources().getString(MoMScreen.UTILITY_BILL_PAYMENT.titleResId),
                            false
                    )
            );
        }

        if(platform == PlatformIdentifier.MOM) {
            if((EphemeralStorage.getInstance(context).getString(AppConstants.PARAM_NEW_ROLE_ID, null).equals("5"))
            ||(EphemeralStorage.getInstance(context).getString(AppConstants.PARAM_NEW_ROLE_ID, null).equals("11"))||
                    (EphemeralStorage.getInstance(context).getString(AppConstants.PARAM_NEW_ROLE_ID, null).equals("3"))){
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
        }
        else if (platform == PlatformIdentifier.PBX) {
//            if ((EphemeralStorage.getInstance(context).getString(AppConstants.PARAM_PBX_USERTYPE, null).equals("2"))
//                    || (EphemeralStorage.getInstance(context).getString(AppConstants.PARAM_PBX_USERTYPE, null).equals("4")) ||
//                    (EphemeralStorage.getInstance(context).getString(AppConstants.PARAM_PBX_USERTYPE, null).equals("8"))) {
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

           // }
        }

        imageItems.add(
                new ImageItem<MoMScreen>(
                        MoMScreen.IMPS,
                        MoMScreen.IMPS.id,
                        MoMScreen.IMPS.drawableId,
                        MoMScreen.IMPS.drawableTransparentId,
                        context.getResources().getString(MoMScreen.IMPS.titleResId),
                        false
                )
        );

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

     //   if(!showingDashboard || platform == PlatformIdentifier.PBX) {
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
      //  }




        int isLic = EphemeralStorage.getInstance(context).getInt(AppConstants.PARAM_IsLIC , -1);
        if((isLic)== 0){
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
       }


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



    public static List<Operator> getOperators(HashMap<String, String> map, String[] operatorCodes){
        List<Operator> operators    = new ArrayList<Operator>();

        for(String op:operatorCodes){
            Operator operator   = new Operator();
            operator.code       = op;
            operator.name       = map.get(op);

            operators.add(operator);
        }

        return operators;
    }

    public static List<Operator> getMoMPlatformMobileOperators(){
        String[] operatorNames  = {AppConstants.OPERATOR_ID_AIRCEL, AppConstants.OPERATOR_ID_AIRTEL,
                AppConstants.OPERATOR_ID_BSNL, AppConstants.OPERATOR_ID_DATACOMM, AppConstants.OPERATOR_ID_IDEA,
                AppConstants.OPERATOR_ID_LOOP, AppConstants.OPERATOR_ID_MOM_CARD,
                AppConstants.OPERATOR_ID_MTNL, AppConstants.OPERATOR_ID_MTS, AppConstants.OPERATOR_ID_QUE,
                AppConstants.OPERATOR_ID_RELIANCE_CDMA,
                AppConstants.OPERATOR_ID_RELIANCE_GSM, AppConstants.OPERATOR_ID_STEL,
                AppConstants.OPERATOR_ID_TATA, AppConstants.OPERATOR_ID_TATA_DOCOMO,
                AppConstants.OPERATOR_ID_TATA_WALKY, AppConstants.OPERATOR_ID_UNINOR,
                AppConstants.OPERATOR_ID_VIRGIN, AppConstants.OPERATOR_ID_VODAFONE};

        return getOperators(AppConstants.OPERATOR_NEW, operatorNames);
    }

    public static List<Operator> getMoMPlatformDTHOperators(){
        String[] operatorNames  = {AppConstants.OPERATOR_ID_AIRTEL_DIGITAL , AppConstants.OPERATOR_ID_BIG_TV ,
                AppConstants.OPERATOR_ID_DISH,
                AppConstants.OPERATOR_ID_SUN_DIRECT, AppConstants.OPERATOR_ID_TATA_SKY, AppConstants.OPERATOR_ID_VIDEOCON_DTH};

        return getOperators(AppConstants.OPERATOR_NEW, operatorNames);
    }

    public static List<Operator> getMoMPlatformBillPayOperators(){
        String[] operatorNames  = {AppConstants.OPERATOR_ID_AIRCEL_BILL , AppConstants.OPERATOR_ID_AIRTEL_BILL,
                AppConstants.OPERATOR_ID_AIRTEL_LAND_LINE,
                AppConstants.OPERATOR_ID_BESCOM_BANGALURU, AppConstants.OPERATOR_ID_BEST_ELECTRICITY,
                AppConstants.OPERATOR_ID_BSES_RAJDHANI,  AppConstants.OPERATOR_ID_BSES_YAMUNA,
                AppConstants.OPERATOR_ID_BSNL_BILL_PAY,
                AppConstants.OPERATOR_ID_CELLONE_BILL_PAY, AppConstants.OPERATOR_ID_CESC_LIMITED,
                AppConstants.OPERATOR_ID_CESCOM_MYSORE,
                AppConstants.OPERATOR_ID_DHBVN_HARYANA, AppConstants.OPERATOR_ID_DELHI_JAL_BOARD,
                AppConstants.OPERATOR_ID_IDEA_BILL,
                AppConstants.OPERATOR_ID_INDRAPRASTHA_GAS, AppConstants.OPERATOR_ID_MAHANAGAR_GAS,
                AppConstants.OPERATOR_ID_NBE, AppConstants.OPERATOR_ID_RELIANCE_BILL_GSM,
                AppConstants.OPERATOR_ID_RELIANCE_BILL_CDMA,
                AppConstants.OPERATOR_ID_RELIANCE_ENERGY, AppConstants.OPERATOR_ID_SBE,
                AppConstants.OPERATOR_ID_TATA_BILL, AppConstants.OPERATOR_ID_TATA_POWER_DELHI,
                AppConstants.OPERATOR_ID_TIKONA_BILL, AppConstants.OPERATOR_ID_UHBVN_HARYANA,
                AppConstants.OPERATOR_ID_VODAFONE_BILL};

        return getOperators(AppConstants.OPERATOR_NEW, operatorNames);
    }

    public static List<Operator> getPBXPlatformMobileOperators(){
        String[] operatorNames  = {
                "CEL", "AIR", "BST", "BSV", "DTC", "IDE",
                "ACT", "MOM", "MTS", "REL", "RGM", "TID",
                "DCM", "SDC", "TWT", "UNI", "UNS", "VIN",
                "VIR", "VOD"
        };

        return getOperators(AppConstants.OPERATOR_PBX, operatorNames);
    }

    public static List<Operator> getPBXPlatformBillPayOperators(){
        String[] operatorNames  = {"BAI", "BLA", "BLL", "BID", "BRC", "BRG", "BTA", "BVO"};

        return getOperators(AppConstants.OPERATOR_PBX, operatorNames);
    }

    public static List<Operator> getPBXPlatformDTHOperators(){
        String[] operatorNames  = {"ADG", "BIG", "SUN", "TSK", "D2H"};

        return getOperators(AppConstants.OPERATOR_PBX, operatorNames);
    }

    public static List<Operator> getPBXPlatformUtilityBillPayOperators(){
        String[] operatorNames  = {"CES"};

        return getOperators(AppConstants.OPERATOR_PBX, operatorNames);
    }
}
