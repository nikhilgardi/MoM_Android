
package com.mom.app.activity;


import com.mom.app.R;
import com.mom.app.identifier.ActivityIdentifier;
import com.mom.app.identifier.IdentifierUtils;
import com.mom.app.identifier.PlatformIdentifier;
import com.mom.app.model.local.EphemeralStorage;
import com.mom.app.ui.flow.MoMScreen;
import com.mom.app.utils.AppConstants;
import com.mom.app.utils.DataProvider;
import com.mom.app.widget.ImageTextViewAdapter;
import com.mom.app.widget.holder.ImageItem;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

public class DashboardActivity extends MOMActivityBase{
    String _LOG         = AppConstants.LOG_PREFIX + "DASHBOARD";

    private PlatformIdentifier _currentPlatform;
    GridView gridView;
    ImageTextViewAdapter gridViewAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Log.i("MainActivity", "started onCreate");

        _currentPlatform    = IdentifierUtils.getPlatformIdentifier(getApplicationContext());

//        String[] values     = null;
        getBalance();


//        if (_currentPlatform == PlatformIdentifier.NEW)
//        {
//            values = new String[]{"Mobile Recharge", "DTH Recharge", "Bill Payment", "Card Sale", "History", "Settings"};
//        } else {
//            values = new String[]{"Mobile Recharge", "DTH Recharge", "Bill Payment", "Utility Bill Payment", "History", "Settings"};
//        }

        gridView            = (GridView) findViewById(R.id.gridView);

     //   gridViewAdapter     = new ImageTextViewAdapter<MoMScreen>(this, R.layout.grid_cell, DataProvider.getScreens(this , _currentPlatform));

        gridViewAdapter     = new ImageTextViewAdapter<MoMScreen>(this, R.layout.grid_cell, DataProvider.getScreens(this, _currentPlatform));


        gridView.setAdapter(gridViewAdapter);
//        gridView.setNumColumns(2);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                Log.d(_LOG, "Clicked " + position);
                ImageItem item  = (ImageItem) gridViewAdapter.getItem(position);
                if(item == null){
                    Log.e(_LOG, "No click target found, returning.");
                    return;
                }

                Log.d(_LOG, "Altering selection to " + !item.getSelected());

                item.setSelected(!item.getSelected());

                Log.d(_LOG, "Going to selected activity");
                nextActivity(item);
            }
        });
    }

    @Override
    protected void showBalance(float pfBalance) {

    }

    private void goToNext(Class nextActivity, ActivityIdentifier ultimateDestination){
        Intent intent       = new Intent(this, nextActivity);

        if(ultimateDestination != null){
            intent.putExtra(AppConstants.INTENT_MESSAGE_DEST, ultimateDestination);
            intent.putExtra(AppConstants.INTENT_MESSAGE_ORIGIN, ActivityIdentifier.DASHBOARD);
        }

        startActivity(intent);
    }
    private void nextActivity(ImageItem<MoMScreen> item){
        ActivityIdentifier ultimateDestination      = null;
        Class nextActivity                          = null;
        boolean isVerificationNeeded                = true;

        switch (item.getItem()){
            case MOBILE_RECHARGE:
                Log.d(_LOG, "Starting Mobile Recharge");
                ultimateDestination     = ActivityIdentifier.MOBILE_RECHARGE;
                nextActivity            = MobileRechargeActivity.class;
                break;
            case DTH_RECHARGE:
                Log.d(_LOG, "Starting DTH Recharge");
                ultimateDestination     = ActivityIdentifier.DTH_RECHARGE;
                nextActivity            = DTHRechargeActivity.class;
                break;
            case BILL_PAYMENT:
                Log.d(_LOG, "Starting Bill Payment");
                ultimateDestination     = ActivityIdentifier.BILL_PAYMENT;
                nextActivity            = BillPaymentActivity.class;
                break;
            case BALANCE_TRANSFER:
                Log.d(_LOG, "Starting Balance Transfer Payment");
                ultimateDestination     = ActivityIdentifier.BALANCE_TRANSFER;
                nextActivity            = BalanceTransferActivity.class;
                break;
            case HISTORY:
                Log.d(_LOG, "Starting Transaction History Activity");
                ultimateDestination     = ActivityIdentifier.TRANSACTION_HISTORY;
                nextActivity            = TransactionHistoryActivity.class;
                isVerificationNeeded    = false;
                break;
            case SETTINGS:
                Log.d(_LOG, "Starting Settings Activity");
                ultimateDestination     = ActivityIdentifier.SETTINGS;
                nextActivity            = SettingsActivity.class;
                isVerificationNeeded    = false;
                break;
            case LOGOUT:
                Log.d(_LOG, "Logout called");
                logout();
                break;
        }

        goToNext(nextActivity, null);

        if(_currentPlatform == PlatformIdentifier.PBX){
            goToNext(nextActivity, null);
        }else{
            if(isVerificationNeeded) {
                goToNext(VerifyTPinActivity.class, ultimateDestination);
            }else{
                goToNext(nextActivity, null);
            }
        }
    }

    public void logout(){
        this.finish();
        EphemeralStorage.getInstance(this).clear();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            System.out.println("KEYCODE_BACK");
            showDialog("BACK");
            return true;
        }
        return false;
    }

    void showDialog(String the_key){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(DashboardActivity.this);
        final Intent intentMain = new Intent(this, DashboardActivity.class);

        alertDialog.setMessage("You have pressed the BACK  button. Would you like to exit the app?")
                .setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        logout();
                        finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialog.create();
        alert.setTitle("Message");
        alert.show();
    }
}
	
