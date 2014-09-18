
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

//        if (_currentPlatform == PlatformIdentifier.NEW)
//        {
//            values = new String[]{"Mobile Recharge", "DTH Recharge", "Bill Payment", "Card Sale", "History", "Settings"};
//        } else {
//            values = new String[]{"Mobile Recharge", "DTH Recharge", "Bill Payment", "Utility Bill Payment", "History", "Settings"};
//        }

        gridView            = (GridView) findViewById(R.id.gridView);
        gridViewAdapter     = new ImageTextViewAdapter(this, R.layout.grid_cell, DataProvider.getScreens(this));

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

//                if(item.getSelected()) {
//                    v.setBackgroundColor(getResources().getColor(R.color.row_selected));
//                }else{
//                    v.setBackgroundColor(Color.TRANSPARENT);
//                }

                Log.d(_LOG, "Going to selected activity");
                nextActivity(item.getTitle());

            }
        });
    }

    @Override
    protected void showBalance(float pfBalance) {

    }

    private void nextActivity(String item){
        Intent intent   = null;
        if (item.equals("Mobile Recharge")) {
            Log.d(_LOG, "Starting Mobile Recharge");
            intent = new Intent(this, VerifyTPinActivity.class);
            intent.putExtra(AppConstants.INTENT_MESSAGE_DEST, ActivityIdentifier.MOBILE_RECHARGE);
            intent.putExtra(AppConstants.INTENT_MESSAGE_ORIGIN, ActivityIdentifier.DASHBOARD);
            startActivity(intent);
            Log.d(_LOG, "Started Mobile Recharge");
            return;
        }else if (item.equals("DTH Recharge")) {
            Log.d(_LOG, "Starting DTH Recharge");
            intent = new Intent(this, VerifyTPinActivity.class);
            intent.putExtra(AppConstants.INTENT_MESSAGE_DEST, ActivityIdentifier.DTH_RECHARGE);
            intent.putExtra(AppConstants.INTENT_MESSAGE_ORIGIN, ActivityIdentifier.DASHBOARD);
            startActivity(intent);
            Log.d(_LOG, "Started DTH Recharge");
            return;

        }else if (item.equals("Bill Payment")) {
            Log.d(_LOG, "Starting Bill Payment");
            intent = new Intent(this, VerifyTPinActivity.class);
            intent.putExtra(AppConstants.INTENT_MESSAGE_DEST, ActivityIdentifier.BILL_PAYMENT);
            intent.putExtra(AppConstants.INTENT_MESSAGE_ORIGIN, ActivityIdentifier.DASHBOARD);
            startActivity(intent);
            Log.d(_LOG, "Started Bill Payment");
            return;
        }else if (item.equals("History")) {

            Log.d(_LOG, "Starting Transaction History Activity");
            intent = new Intent(this, TransactionHistoryActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            Log.d(_LOG, "Started Transaction History Activity");

        }else if (item.equals("Settings")) {
            Log.d(_LOG, "Starting Settings Activity");
            intent = new Intent(DashboardActivity.this, SettingsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            Log.d(_LOG, "Started Settings Activity");
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
	
