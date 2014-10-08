package com.mom.app.ui.flow;

import com.mom.app.R;

import java.io.Serializable;

/**
 * Created by vaibhavsinha on 7/27/14.
 */
public enum MoMScreen implements Serializable{
    MOBILE_RECHARGE(100, R.string.action_mobileRecharge, R.drawable.phone),
    DTH_RECHARGE(200, R.string.action_dthRecharge, R.drawable.dth),
    BILL_PAYMENT(300, R.string.action_billPayment, R.drawable.bill),
    BALANCE_TRANSFER(350, R.string.action_balanceTransfer, R.drawable.balance_transfer),
    HISTORY(400, R.string.action_history, R.drawable.history),
    SETTINGS(500, R.string.action_settings, R.drawable.settings),
    CHANGE_MPIN(600, R.string.action_changeMpin, R.drawable.phone),
    CHANGE_TPIN(650, R.string.action_changeTpin, R.drawable.telephone),
    LOGOUT(1100, R.string.action_logout, R.drawable.logout);

    public Integer titleResId;
    public Integer id;
    public Integer drawableId;
    /*
    This string is populated using the 'context' from an external class.
     */
    public String screenTitle   = null;

    private MoMScreen(int id, int titleStrResId, int drawableId){
        this.id         = id;
        this.titleResId = titleStrResId;
        this.drawableId = drawableId;
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
