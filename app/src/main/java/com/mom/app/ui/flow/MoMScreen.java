package com.mom.app.ui.flow;

import com.mom.app.R;

import java.io.Serializable;

/**
 * Created by vaibhavsinha on 7/27/14.
 */
public enum MoMScreen implements Serializable{
//    DASHBOARD(50, R.string.action_dashboard, R.drawable.dashboard_transparent, R.drawable.dashboard_transparent),
//    MOBILE_RECHARGE(100, R.string.action_mobileRecharge, R.drawable.mobile, R.drawable.mobile_transparent),
//    DTH_RECHARGE(200, R.string.action_dthRecharge, R.drawable.dth, R.drawable.dth_transparent),
//    BILL_PAYMENT(300, R.string.action_billPayment, R.drawable.bill, R.drawable.bill_transparent),
//    UTILITY_BILL_PAYMENT(350, R.string.action_utilityBill, R.drawable.bill, R.drawable.bill_transparent),
//    BALANCE_TRANSFER(400, R.string.action_balanceTransfer, R.drawable.balance_transfer, R.drawable.balance_transfer_transparent),
//    IMPS(450,R.string.action_imps, R.drawable.balance_transfer, R.drawable.balance_transfer_transparent),
//    LIC(500, R.string.action_lIC,R.drawable.lic_large ,R.drawable.lic_large),
//    HISTORY(600, R.string.action_history, R.drawable.history, R.drawable.history_transparent),
//    SETTINGS(700, R.string.action_settings, R.drawable.settings, R.drawable.settings_transparent),
//    CHANGE_MPIN(750, R.string.action_changeMpin, R.drawable.mobile, R.drawable.mobile_transparent),
//    CHANGE_TPIN(800, R.string.action_changeTpin, R.drawable.telephone, R.drawable.telephone_transparent),
//    CHANGE_PASSWORD(850, R.string.action_changePassword, R.drawable.phone, R.drawable.phone_transparent),
//
//    LOGOUT(1100, R.string.action_logout, R.drawable.logout, R.drawable.logout_transparent);
    DASHBOARD(50, R.string.action_dashboard, R.drawable.dashboard_transparent, R.drawable.dashboard_transparent),
    MOBILE_RECHARGE(100, R.string.action_mobileRecharge, R.drawable.mtopupnew, R.drawable.mtopupnew),
    DTH_RECHARGE(150, R.string.action_dthRecharge, R.drawable.dthnew, R.drawable.dthnew),
    BILL_PAYMENT(200, R.string.action_billPayment, R.drawable.billpaymentnew, R.drawable.billpaymentnew),
    UTILITY_BILL_PAYMENT(250, R.string.action_utilityBill, R.drawable.utilitybillnew, R.drawable.utilitybillnew),
    BALANCE_TRANSFER(300, R.string.action_balanceTransfer, R.drawable.balance_trnsfernew, R.drawable.balance_trnsfernew),
    IMPS(350,R.string.action_imps, R.drawable.mtransfernew, R.drawable.mtransfernew),
    LIC(400, R.string.action_lIC,R.drawable.licnew ,R.drawable.lic_large),
    HISTORY(450, R.string.action_history, R.drawable.historynew, R.drawable.historynew),
    GIFT_VOUCHER(500, R.string.action_giftVoucher, R.drawable.gift, R.drawable.gift),
    BUS_TICKETING(550, R.string.action_busTicketing, R.drawable.bus, R.drawable.bus),
    RAIL_TICKETING(600, R.string.action_railTicketing, R.drawable.rail, R.drawable.rail),
    AIR_TICKETING(650, R.string.action_airTicketing, R.drawable.air, R.drawable.air),
    WALLET_UPDATE(700, R.string.action_walletUpdate, R.drawable.walletupdate, R.drawable.walletupdate),
    BOOK_COMPLAINT(750, R.string.action_bookComplaint, R.drawable.bookcomplaint, R.drawable.bookcomplaint),
    NEW_WITH_US(800, R.string.action_newWithUs, R.drawable.newwithus, R.drawable.newwithus),
    CONTACT_US(850, R.string.action_contactUs, R.drawable.contactus, R.drawable.contactus),
    SETTINGS(900, R.string.action_settings, R.drawable.settingnew, R.drawable.settingnew),
    CHANGE_MPIN(950, R.string.action_changeMpin, R.drawable.change_mpinnew, R.drawable.change_mpinnew),
    CHANGE_TPIN(1000, R.string.action_changeTpin, R.drawable.change_tpinnew, R.drawable.change_tpinnew),
    CHANGE_PASSWORD(1050, R.string.action_changePassword, R.drawable.change_paswrdnew, R.drawable.change_paswrdnew),
//    BOOK_COMPLAINT(1100, R.string.action_bookComplaint, R.drawable.bookcomplaint, R.drawable.bookcomplaint),
//    NEW_WITH_US(1150, R.string.action_newWithUs, R.drawable.newwithus, R.drawable.newwithus),
//    CONTACT_US(1400, R.string.action_contactUs, R.drawable.contactus, R.drawable.contactus),
    LOGOUT(1100, R.string.action_logout, R.drawable.logoutnew, R.drawable.logoutnew);


    public Integer titleResId;
    public Integer id;
    public Integer drawableId;
    public Integer drawableTransparentId;
    /*
    This string is populated using the 'context' from an external class.
     */
    public String screenTitle   = null;

    private MoMScreen(int id, int titleStrResId, int drawableId, int drawableTransparentId){
        this.id         = id;
        this.titleResId = titleStrResId;
        this.drawableId = drawableId;
        this.drawableTransparentId = drawableTransparentId;
    }

    public static MoMScreen getScreenFromId(int id){
        for(MoMScreen screen:MoMScreen.values()){
            if(screen.id == id){
                return screen;
            }
        }
        return null;
    }

    public String toString(){
        return screenTitle;
    }
}
