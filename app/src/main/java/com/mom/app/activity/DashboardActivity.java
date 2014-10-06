
package com.mom.app.activity;


import com.mom.app.R;
import com.mom.app.identifier.ActivityIdentifier;
import com.mom.app.identifier.IdentifierUtils;
import com.mom.app.identifier.PlatformIdentifier;
import com.mom.app.model.local.EphemeralStorage;
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
        if (_currentPlatform == PlatformIdentifier.PBX) {
            String jsonstring1 = EphemeralStorage.getInstance(this).getString(AppConstants.PARAM_PBX_USERID, null);
            Log.i("ResultLogin1 Dash", jsonstring1);
            String jsonstring2 = EphemeralStorage.getInstance(this).getString(AppConstants.PARAM_PBX_RMN, null);
            Log.i("ResultLogin2 Dash", jsonstring2);
            String jsonstring3 = EphemeralStorage.getInstance(this).getString(AppConstants.PARAM_PBX_NAME, null);
            Log.i("ResultLogin3 Dash", jsonstring3);

            String jsonstring4 = EphemeralStorage.getInstance(this).getString(AppConstants.PARAM_PBX_TOKEN, null);
            Log.i("ResultLogin4 Dash", jsonstring4);

            String jsonstring5 = EphemeralStorage.getInstance(this).getString(AppConstants.PARAM_PBX_USERTYPE, null);
            Log.i("ResultLogin5 Dash", jsonstring5);

            String jsonstring6 = EphemeralStorage.getInstance(this).getString(AppConstants.PARAM_PBX_USERNAMELOGIN, null);
            Log.i("ResultLogin6 Dash", jsonstring6);

        }

//        if (_currentPlatform == PlatformIdentifier.NEW)
//        {
//            values = new String[]{"Mobile Recharge", "DTH Recharge", "Bill Payment", "Card Sale", "History", "Settings"};
//        } else {
//            values = new String[]{"Mobile Recharge", "DTH Recharge", "Bill Payment", "Utility Bill Payment", "History", "Settings"};
//        }

        gridView            = (GridView) findViewById(R.id.gridView);
        gridViewAdapter     = new ImageTextViewAdapter(this, R.layout.grid_cell, DataProvider.getScreens(this, _currentPlatform));

        gridView.setAdapter(gridViewAdapter);
        gridView.setNumColumns(2);


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
                nextActivity(item.getTitle());
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
    private void nextActivity(String item){
        ActivityIdentifier ultimateDestination      = null;
        Class nextActivity                          = null;

        if (item.equals("Mobile Recharge")) {
            Log.d(_LOG, "Starting Mobile Recharge");
            ultimateDestination     = ActivityIdentifier.MOBILE_RECHARGE;
            nextActivity            = VerifyTPinActivity.class;
        }else if (item.equals("DTH Recharge")) {
            Log.d(_LOG, "Starting DTH Recharge");
            ultimateDestination     = ActivityIdentifier.DTH_RECHARGE;
            nextActivity            = DTHRechargeActivity.class;

        }else if (item.equals("Bill Payment")) {
            Log.d(_LOG, "Starting Bill Payment");
            ultimateDestination     = ActivityIdentifier.BILL_PAYMENT;
            nextActivity            = BillPaymentActivity.class;
        }else if (item.equals("History")) {

            Log.d(_LOG, "Starting Transaction History Activity");
            ultimateDestination     = ActivityIdentifier.TRANSACTION_HISTORY;
            nextActivity            = TransactionHistoryActivity.class;
           // nextActivity            = VerifyTPinActivity.class;
        }else if (item.equals("Settings")) {
            Log.d(_LOG, "Starting Settings Activity");
            ultimateDestination     = ActivityIdentifier.SETTINGS;
            nextActivity            = SettingsActivity.class;
        }

        if(_currentPlatform == PlatformIdentifier.PBX){
            goToNext(nextActivity, null);
        }

        else{
            goToNext(VerifyTPinActivity.class, ultimateDestination);
        }
    }


    public void logout(View v) {
        logout();
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
	
