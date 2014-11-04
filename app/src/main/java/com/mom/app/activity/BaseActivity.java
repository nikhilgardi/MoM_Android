package com.mom.app.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.LongSparseArray;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.mom.app.R;
import com.mom.app.adapter.AsyncStatusListAdapter;
import com.mom.app.adapter.DrawerAdapter;
import com.mom.app.error.MOMException;
import com.mom.app.fragment.BalanceTransferFragment;
import com.mom.app.fragment.BillPaymentFragment;
import com.mom.app.fragment.DTHRechargeFragment;
import com.mom.app.fragment.DashboardFragment;
import com.mom.app.fragment.FragmentBase;
import com.mom.app.fragment.LICFragment;
import com.mom.app.fragment.MobileRechargeFragment;
import com.mom.app.fragment.SettingsFragment;
import com.mom.app.fragment.TransactionHistoryFragment;
import com.mom.app.identifier.IdentifierUtils;
import com.mom.app.identifier.PlatformIdentifier;
import com.mom.app.model.GcmTransactionMessage;
import com.mom.app.model.Operator;
import com.mom.app.model.local.EphemeralStorage;
import com.mom.app.ui.TransactionRequest;
import com.mom.app.ui.IFragmentListener;
import com.mom.app.ui.flow.MoMScreen;
import com.mom.app.utils.AppConstants;
import com.mom.app.utils.DataProvider;
import com.mom.app.widget.holder.ImageItem;

import java.util.ArrayList;
import java.util.HashMap;

public class BaseActivity extends ActionBarActivity implements IFragmentListener{

    String _LOG = AppConstants.LOG_PREFIX + "BASE ACT";

    DrawerLayout _drawerLayout;
    ProgressBar _progressBar;
    ListView _drawerList;
    ListView _asyncStatusList;
    AsyncStatusListAdapter _asyncAdapter;

    ActionBarDrawerToggle _drawerToggle;

    PlatformIdentifier _currentPlatform;

    ArrayList<ImageItem<MoMScreen>> _menuItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        _asyncStatusList    = (ListView) findViewById(R.id.asyncStatusList);
        _progressBar        = (ProgressBar) findViewById(R.id.progressBarActivityBase);
        _currentPlatform    = IdentifierUtils.getPlatformIdentifier(this);

        /**
         * REMOVE THIS!
         */
        if(_currentPlatform == null){
//            _currentPlatform    = PlatformIdentifier.PBX;
            _currentPlatform    = PlatformIdentifier.MOM;
            EphemeralStorage.getInstance(this).storeObject(AppConstants.ACTIVE_PLATFORM, _currentPlatform);
        }

        setupDrawerMenu();
        setupActionBar();
        setupAsyncProgressList();

        if (savedInstanceState == null) {
//            showMobileRecharge();
            showDashboard();
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(
                messageReceiver, new IntentFilter(AppConstants.GCM_INTENT)
        );
    }

    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String jsonReceived     = intent.getStringExtra(AppConstants.PARAM_GCM_PAYLOAD);
            if(TextUtils.isEmpty(jsonReceived)){
                Log.w(_LOG, "Did not receive any json payload");
                return;
            }
            try{
                Gson gson                       = new Gson();
                GcmTransactionMessage message   = gson.fromJson(
                        jsonReceived, GcmTransactionMessage.class
                );

                if(!TextUtils.isEmpty(message.getOriginTxnId())){

                }
            }catch(Exception e){
                Log.e(_LOG, "Error parsing json", e);
            }

        }
    };


    private void setupDrawerMenu(){
        if(_currentPlatform == null){
            throw new IllegalStateException("platform should have been identified by now");
        }

        _menuItems = DataProvider.getScreens(this, _currentPlatform, false);

        _drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        _drawerList = (ListView) findViewById(R.id.leftDrawer);

        DrawerAdapter<MoMScreen> adapter   = new DrawerAdapter<MoMScreen>(this, R.layout.drawer_list_item, _menuItems);

        // Set the adapter for the list view
        _drawerList.setAdapter(adapter);
        // Set the list's click listener
        _drawerList.setOnItemClickListener(new DrawerItemClickListener());

        Log.d(_LOG, "ActionBar set");
        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        _drawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                _drawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                Log.d(_LOG, "onDrawerClosed");
//                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                Log.d(_LOG, "onDrawerOpened");
//                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        Log.d(_LOG, "setting listener");

        _drawerLayout.setDrawerListener(_drawerToggle);
    }

    private void setupActionBar(){
        if(getActionBar() == null){
            return;
        }

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayShowTitleEnabled(true);
        getActionBar().setTitle(R.string.money_on_mobile);
        getActionBar().setDisplayUseLogoEnabled(true);
    }

    private void setupAsyncProgressList(){
        if(_asyncAdapter == null){
            _asyncAdapter       = new AsyncStatusListAdapter(
                    this,
                    R.layout.async_progress_item,
                    new LongSparseArray<TransactionRequest>()
            );
        }

        _asyncStatusList.setAdapter(_asyncAdapter);
//        addToAsyncList(new TransactionRequest("Recharge", "9810012345", "398923", 20f, new Operator("ab", "airtel")));
    }

    private void addToAsyncList(TransactionRequest transactionRequest){
        /**
         * For some reason, notifyDataset has no effect unless add/remove methods are called.
         */
        if(!_asyncAdapter.contains(transactionRequest)) {
            _asyncAdapter.add(transactionRequest);
        }else{
            _asyncAdapter.remove(transactionRequest);
            _asyncAdapter.add(transactionRequest);
        }

        _asyncAdapter.addTransaction(transactionRequest);
        _asyncAdapter.notifyDataSetChanged();
        _asyncStatusList.setVisibility(View.VISIBLE);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show){
        _progressBar.setVisibility(show ? View.VISIBLE : View.INVISIBLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            _progressBar.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    _progressBar.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.base, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (_drawerToggle.onOptionsItemSelected(item)) {
            Log.d(_LOG, "click handled by drawer, returning...");
            return true;
        }

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected boolean onPrepareOptionsPanel(View view, Menu menu) {
        Log.d(_LOG, "onPrepareOptionsPanel");
        return super.onPrepareOptionsPanel(view, menu);
    }

    @Override
    public void processMessage(Bundle bundle) {
        int showProgressBarCode        = bundle.getInt(AppConstants.BUNDLE_PROGRESS, AppConstants.DEFAULT_INT);
        if(showProgressBarCode != AppConstants.DEFAULT_INT){
            if(showProgressBarCode == 0){
                showProgress(false);
            }else{
                showProgress(true);
            }
            Log.d(_LOG, "Showing progress bar: " + showProgressBarCode);
            return;
        }

        FragmentBase fragmentBase = (FragmentBase) getFragmentManager().findFragmentById(R.id.contentFrame);

        if(fragmentBase == null){
            Log.d(_LOG, "No fragment found to pass the message");
            return;
        }

        if(messageIntercepted(bundle)){
            Log.d(_LOG, "Message intercepted. Not being forwarded to attached fragment");
            return;
        }

        Log.d(_LOG, "Passing the message to sub fragment");

        try {
            fragmentBase.receiveMessage(bundle);
        }catch (MOMException me){
            me.printStackTrace();
        }
    }

    private boolean messageIntercepted(Bundle bundle){
        MoMScreen nextScreen            = (MoMScreen) bundle.getSerializable(
                AppConstants.BUNDLE_NEXT_SCREEN
        );

        if(nextScreen != null){
            return showScreen(nextScreen);
        }

        return processTransactionMessage(bundle);

    }

    private boolean processTransactionMessage(Bundle bundle){
        TransactionRequest transaction  = (TransactionRequest) bundle.getSerializable(
                AppConstants.BUNDLE_TRANSACTION_REQUEST
        );

        if(transaction == null){
            return false;
        }

        /**
         * If we're here, the transaction was probably sent via GCM to update the asynclist
         * or this is a new transaction. In either case, the view needs to be updated and
         * the new object should be added or the old one replaced with this new one (GCM case).
         */

        addToAsyncList(transaction);
        return true;
    }

    private boolean showScreen(MoMScreen screen){
        switch (screen){
            case DASHBOARD:
                showFragment(DashboardFragment.newInstance(_currentPlatform));
                break;
            case MOBILE_RECHARGE:
                Log.d(_LOG, "mobile recharge selected");
                showFragment(MobileRechargeFragment.newInstance(_currentPlatform));
                break;
            case DTH_RECHARGE:
                Log.d(_LOG, "show dth recharge selected");
                showFragment(DTHRechargeFragment.newInstance(_currentPlatform));
                break;
            case BILL_PAYMENT:
                showFragment(BillPaymentFragment.newInstance(_currentPlatform));
                break;
            case LIC:
                Log.d(_LOG, "lic selected");
                showFragment(LICFragment.newInstance(_currentPlatform));
                break;
            case BALANCE_TRANSFER:
                showFragment(BalanceTransferFragment.newInstance(_currentPlatform));
                break;
            case HISTORY:
                showFragment(TransactionHistoryFragment.newInstance(_currentPlatform));
                break;
            case SETTINGS:
                showFragment(SettingsFragment.newInstance(_currentPlatform));
                break;
            case LOGOUT:
                confirmLogout();
                break;
        }

        return false;
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.d(_LOG, "Item clicked: " + position);
            selectItem(position);

        }
    }

    public void showFragment(Fragment fragment){
        showFragment(fragment, R.id.contentFrame);
    }

    public void showFragment(Fragment fragment, int containerResourceId){
        if(fragment == null){
            Log.e(_LOG, "No fragment sent to show. Doing nothing.");
            return;
        }

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Log.d(_LOG, "Adding fragment");
        transaction.replace(containerResourceId, fragment);
        Log.d(_LOG, "Fragment added");
//        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void selectItem(int position) {
        if(position > _menuItems.size()){
            Log.e(_LOG, "Selected Menu item exceeds the menu item array length!");
            return;
        }

        ImageItem screen    = _menuItems.get(position);

        // update selected item and title, then close the drawer
        Log.d(_LOG, "select item called");
        _drawerList.setItemChecked(position, true);
//        setTitle(screen.screenTitle);
        _drawerLayout.closeDrawer(_drawerList);
        Log.d(_LOG, "select item finished");

        MoMScreen appScreen   = MoMScreen.getScreenFromId(screen.getId());

        showScreen(appScreen);

        Log.d(_LOG, "exiting selectItem");
    }

    private void showMobileRecharge(){
        showFragment(MobileRechargeFragment.newInstance(_currentPlatform));
    }

    private void showDashboard(){
        showFragment(DashboardFragment.newInstance(_currentPlatform));
    }

    void confirmLogout(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        final Context context     = this;

        alertDialog.setMessage(R.string.confirm_logout)
                .setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        EphemeralStorage.getInstance(context).clear();
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
