
package com.mom.app.activity;


import com.mom.app.Helpz;
import com.mom.app.HistoryActivity;
import com.mom.app.HomeActivity2;
import com.mom.app.HomeBillActivity_PBX;
import com.mom.app.InfoActivity;
import com.mom.app.MSwipeAndroidSDKListActivity1;
import com.mom.app.R;
import com.mom.app.identifier.ActivityIdentifier;
import com.mom.app.identifier.IdentifierUtils;
import com.mom.app.identifier.PlatformIdentifier;
import com.mom.app.model.AsyncListener;
import com.mom.app.model.DataExImpl;
import com.mom.app.model.IDataEx;
import com.mom.app.model.local.LocalStorage;
import com.mom.app.model.newpl.NewPLDataExImpl;
import com.mom.app.model.pbxpl.PBXPLDataExImpl;
import com.mom.app.utils.MOMConstants;


import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.Locale;

public class DashboardActivity extends ListActivity implements AsyncListener<Float>{

    private PlatformIdentifier _currentPlatform;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("MainActivity", "started onCreate");

        _currentPlatform    = IdentifierUtils.getPlatformIdentifier(getApplicationContext());

        String[] values     = null;

        if (_currentPlatform == PlatformIdentifier.NEW)
        {
            getBalance();
            values = new String[]{"Mobile Recharge", "DTH Recharge", "Bill Payment", "Card Sale", "History", "Settings"};
        } else {
            getBalancePBX();
            values = new String[]{"Mobile Recharge", "DTH Recharge", "Bill Payment", "Utility Bill Payment", "History", "Settings"};

        }

        setContentView(R.layout.activity_dashboard);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.row_layout, R.id.label, values);
        setListAdapter(adapter);
//        getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.appsbg));
    }

    @Override
    public void onTaskComplete(Float result, DataExImpl.Methods pMethod) {
        Log.d("TASK_C_MAIN", "Called back");
        switch(pMethod){
            case GET_BALANCE:
                Log.d("TASK_C_MAIN", "Balance returned: " + result);
                TextView balanceTxtView = (TextView) findViewById(R.id.textView1);
                if(balanceTxtView != null){
                    balanceTxtView.setVisibility(View.VISIBLE);

                    Locale lIndia       = new Locale("en", "IN");
                    NumberFormat form   = NumberFormat.getCurrencyInstance(lIndia);
                    String sBalance      = form.format(result);
                    balanceTxtView.setText("Balance: " + sBalance);
                    Log.d("TASK_C_MAIN", "Balance TextView set");
                }

                LocalStorage.storeLocally(getApplicationContext(), MOMConstants.USER_BALANCE, result);
            break;
        }
    }

    public void getBalance(){
        Log.d("MAIN", "Getting Balance");
        IDataEx dataEx  = new NewPLDataExImpl(getApplicationContext(), this);
        Log.d("MAIN", "DataEx instance created");
        dataEx.getBalance();
        Log.d("MAIN", "getBalance called");
    }

    public void getBalancePBX(){
        Log.d("MAIN", "Getting BalancePBX");
        IDataEx dataEx  = new PBXPLDataExImpl(this,getApplicationContext());
        Log.d("MAIN", "DataEx instance created");
        dataEx.getBalance();
        Log.d("MAIN", "getBalancePBX called");
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String item = (String) getListAdapter().getItem(position);
        Intent intent   = null;
        Log.d("LIST_CLICKED", "Going to start activity");
        if (item.equals("Mobile Recharge")) {
            Log.d("LIST_CLICKED", "Starting Mobile Recharge");
            intent = new Intent(this, VerifyTPinActivity.class);
            intent.putExtra(MOMConstants.INTENT_MESSAGE_DEST, ActivityIdentifier.MOBILE_RECHARGE);
            intent.putExtra(MOMConstants.INTENT_MESSAGE_ORIGIN, ActivityIdentifier.DASHBOARD);
            startActivity(intent);
            Log.d("LIST_CLICKED", "Started Mobile Recharge");
            return;
        }else if (item.equals("DTH Recharge")) {
            Log.d("LIST_CLICKED", "Starting DTH Recharge");
            intent = new Intent(this, VerifyTPinActivity.class);
            intent.putExtra(MOMConstants.INTENT_MESSAGE_DEST, ActivityIdentifier.DTH_RECHARGE);
            intent.putExtra(MOMConstants.INTENT_MESSAGE_ORIGIN, ActivityIdentifier.DASHBOARD);
            startActivity(intent);
            Log.d("LIST_CLICKED", "Started DTH Recharge");
            return;

        }else if (item.equals("Bill Payment")) {
            intent = new Intent(DashboardActivity.this, HomeActivity2.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }else if (item.equals("Card Sale")) {
            intent = new Intent(DashboardActivity.this, MSwipeAndroidSDKListActivity1.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();

        }else if (item.equals("Utility Bill Payment")) {
            intent = new Intent(DashboardActivity.this, HomeBillActivity_PBX.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();

        }else if (item.equals("History")) {

            intent = new Intent(DashboardActivity.this, HistoryActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }else if (item.equals("Settings")) {
            intent = new Intent(DashboardActivity.this, InfoActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    public void logout(View v) {

        this.finish();
        Helpz.SetLogoutVariable(false);
        LocalStorage.storeLocally(getApplicationContext(), MOMConstants.PREF_IS_LOGGED_IN, false);
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
                        dialog.cancel();
                        Helpz myhelp = new Helpz();
                        myhelp.SetLogoutVariable(false);
                        intentMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
	
