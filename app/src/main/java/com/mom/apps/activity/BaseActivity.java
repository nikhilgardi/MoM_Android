package com.mom.apps.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.LongSparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mom.apps.R;
import com.mom.apps.adapter.AsyncStatusListAdapter;
import com.mom.apps.adapter.DrawerAdapter;
import com.mom.apps.error.MOMException;
import com.mom.apps.fragment.BalanceTransferFragment;
import com.mom.apps.fragment.BillPaymentFragment;
import com.mom.apps.fragment.ChangePinFragment;
import com.mom.apps.fragment.DTHRechargeFragment;
import com.mom.apps.fragment.DashboardFragment;
import com.mom.apps.fragment.FragmentBase;
import com.mom.apps.fragment.IMPSFragment;
import com.mom.apps.fragment.LICFragment;
import com.mom.apps.fragment.MobileRechargeFragment;
import com.mom.apps.fragment.SettingsFragment;
import com.mom.apps.fragment.TransactionHistoryFragment;
import com.mom.apps.fragment.UtilityBillPaymentFragment;
import com.mom.apps.identifier.IdentifierUtils;
import com.mom.apps.identifier.MessageCategory;
import com.mom.apps.identifier.PinType;
import com.mom.apps.identifier.PlatformIdentifier;
import com.mom.apps.model.AsyncListener;
import com.mom.apps.model.AsyncResult;
import com.mom.apps.model.DataExImpl;
import com.mom.apps.model.GcmTransactionMessage;
import com.mom.apps.model.IDataEx;
import com.mom.apps.model.local.EphemeralStorage;
import com.mom.apps.model.mompl.MoMPLDataExImpl;
import com.mom.apps.model.pbxpl.PBXPLDataExImpl;
import com.mom.apps.ui.TransactionRequest;
import com.mom.apps.ui.IFragmentListener;
import com.mom.apps.ui.flow.MoMScreen;
import com.mom.apps.utils.AppConstants;
import com.mom.apps.utils.DataProvider;
import com.mom.apps.widget.holder.ImageItem;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class BaseActivity extends ActionBarActivity implements IFragmentListener{

    String _LOG = AppConstants.LOG_PREFIX + "BASE ACT";

    DrawerLayout _drawerLayout;
    ProgressBar _progressBar;
    TextView _tvBalance;
    ListView _drawerList;
    ListView _asyncStatusList;
    AsyncStatusListAdapter _asyncAdapter;
    TextView _tvAppMessage;

    ActionBarDrawerToggle _drawerToggle;

    PlatformIdentifier _currentPlatform;
    PinType _pPinType;

    MoMScreen _currentScreen;
    AsyncListener asyncListener;

    ArrayList<ImageItem<MoMScreen>> _menuItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        _asyncStatusList    = (ListView) findViewById(R.id.asyncStatusList);

        _progressBar        = (ProgressBar) findViewById(R.id.progressBarActivityBase);
        _tvAppMessage       = (TextView) findViewById(R.id.appMessage);
        _currentPlatform    = IdentifierUtils.getPlatformIdentifier(this);

        if(_currentPlatform == null){
            throw new IllegalStateException("Platform should not have been null here!");
        }

        setupDrawerMenu();
        setupActionBar();
        setupAsyncProgressList();

        if (savedInstanceState == null) {
            showDashboard();
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(
                messageReceiver, new IntentFilter(AppConstants.INTENT_GCM)
        );
//        getWindow().setBackgroundDrawable(
//                getResources().getDrawable(R.drawable.appsbg));
      getBalance();

    }

    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Log.d(_LOG, "BroadcastReceiver: Received message");
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
                    TransactionRequest request  = _asyncAdapter.getTransactionRequest(message.getOriginTxnId());
                    updateAsyncList(request, message.getStatus());
                }
            }catch(Exception e){
                Log.e(_LOG, "Error parsing json", e);
            }

        }
    };

    private BroadcastReceiver _networkMessageReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            Log.d(_LOG, "NetworkReceiver: Received message");
            boolean isConnected     = intent.getBooleanExtra(AppConstants.NETWORK_STATUS_CONNECTED, true);
            if(isConnected){
                showAppMessage(false, -1);
            }else {
                showAppMessage(true, R.string.error_no_internet);
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(this).registerReceiver(
                _networkMessageReceiver, new IntentFilter(AppConstants.INTENT_NETWORK)
        );

        if(_currentScreen == MoMScreen.IMPS){

            _asyncStatusList.setVisibility(View.GONE);
        }else{
            _asyncStatusList.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(_networkMessageReceiver);
    }

    private void showAppMessage(boolean show, int idMsg){
        if(!show){
            _tvAppMessage.setText(null);
            _tvAppMessage.setVisibility(View.GONE);
            return;
        }

        _tvAppMessage.setText(idMsg);
        _tvAppMessage.setVisibility(View.VISIBLE);
    }

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
                getBalance();
//                getActionBar().setTitle(mTitle);
                //invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                supportInvalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                Log.d(_LOG, "onDrawerOpened");
                getBalance();
//                getActionBar().setTitle(mDrawerTitle);
                //invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                supportInvalidateOptionsMenu();
            }
        };

        Log.d(_LOG, "setting listener");

        _drawerLayout.setDrawerListener(_drawerToggle);
    }

    private void setupActionBar(){
        if(getSupportActionBar() == null){
            return;
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setTitle(R.string.money_on_mobile);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
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
        _asyncStatusList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(_LOG, "Clicked");
                TransactionRequest request = (TransactionRequest)parent.getItemAtPosition(position);
                if(request == null){
                    Log.d(_LOG, "Did not find transaction!");
                    throw new IllegalArgumentException("Cannot have a list item without a transaction");
                }


            }
        });
    }

   // private void getBalance() {
        protected void getBalance() {
        showProgress(true);

            AsyncListener<Float> listener = new AsyncListener<Float>() {
                @Override
                public void onTaskSuccess(Float result, DataExImpl.Methods callback) {
                    Log.d(_LOG, "Got balance: " + result);
                    showProgress(false);

                    EphemeralStorage.getInstance(getApplicationContext()).storeFloat(
                            AppConstants.USER_BALANCE, result
                    );

                    showBalance(_tvBalance, result);
                }

                @Override
                public void onTaskError(AsyncResult pResult, DataExImpl.Methods callback) {
                    Log.e(_LOG, "Error retrieving balance");
                    showProgress(false);
                }
            };


            Log.d(_LOG, "Going to fetch balance");
            getDataEx(listener).getBalance();


    }

    protected void showBalance(TextView tv){
        float balance         = EphemeralStorage.getInstance(
                this
        ).getFloat(AppConstants.USER_BALANCE, AppConstants.ERROR_BALANCE);

        showBalance(tv, balance);
    }
    String sBal         = null;
    protected void showBalance(TextView tv, Float balance){


        if(balance == AppConstants.ERROR_BALANCE){
            sBal            = getString(R.string.error_getting_balance);
            return;
        }

        else {
            try {
                if (balItem!=null) {
                    //  DecimalFormat df = new DecimalFormat("#,###,###,##0.00");
                    DecimalFormat df = new DecimalFormat("#0.00");
                    Log.e("Bal1", String.valueOf(balance));
                    sBal = df.format(balance);
                    Log.e("Bal", sBal);
                    balItem.setTitle("Balance: " + getResources().getString(R.string.Rupee) + sBal);
                    //  balItem.setTitle("Balance: "  + sBal);
                    Log.d(_LOG, sBal);
                }
                else{
                    Log.e("Balance Error" , String.valueOf(balance));
                }
            }
            catch(Exception ex){
                Log.e("Exception" , ex.toString());
                Intent intent 						= new Intent(BaseActivity.this, LoginActivity.class);
                Log.i(_LOG, "Finishing LoginActivity");

                startActivity(intent);
            }
        }

       // tv.setText("Balance: " + getResources().getString(R.string.Rupee) + sBal);

    }

    public IDataEx getDataEx(AsyncListener<?> listener){
        IDataEx dataEx;

        try {
            if (_currentPlatform == PlatformIdentifier.MOM) {
                dataEx = new MoMPLDataExImpl(getApplicationContext(), listener);
            }
            else if(_currentPlatform == PlatformIdentifier.B2C){
                dataEx = new MoMPLDataExImpl(getApplicationContext(), listener);
            }

            else {
                dataEx = new PBXPLDataExImpl(getApplicationContext(), DataExImpl.Methods.LOGIN, listener);
            }

            return dataEx;
        }catch(MOMException me){
            Log.e(_LOG, "Error getting dataex", me);
        }

        return null;
    }

    private void updateAsyncList(TransactionRequest request, Integer status){
        if(request == null){
            Log.w(_LOG, "Invalid transaction for update");
            return;
        }

        if(request.setStatus(status)){
            request.setCompleted(true);
            Log.d(_LOG, "Found status, setting to: " + request.getStatus());
            addToAsyncList(request);
        }
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
 //      getMenuInflater().inflate(R.menu.base, menu);
//
//        _tvBalance = new TextView(this);
//
//        _tvBalance.setTextColor(getResources().getColor(R.color.app_secondary));
//        _tvBalance.setPadding(5, 0, 5, 0);
//        _tvBalance.setTextSize(14);
//
//       menu.add("balance").setActionView(_tvBalance).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//
//        return super.onCreateOptionsMenu(menu);
//    }

private   MenuItem balItem = null;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_actions, menu);
        balItem = menu.findItem(R.id.balance);
        MenuItemCompat.setShowAsAction(balItem , MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
         MenuItemCompat.getActionView(balItem); //


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


            return;
        }

        MessageCategory category = (MessageCategory) bundle.getSerializable(AppConstants.BUNDLE_MESSAGE_CATEGORY);

        if(category != null) {

            switch (category) {
                case GET_AND_SHOW_BALANCE:

                    getBalance();
                    return;
            }
        }

        FragmentBase fragmentBase = (FragmentBase) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

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
                if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                    _asyncStatusList.setVisibility(View.    GONE);
                }else{
                    _asyncStatusList.setVisibility(View.VISIBLE);
                }
                showFragment(DashboardFragment.newInstance(_currentPlatform));
                break;
            case MOBILE_RECHARGE:
                Log.d(_LOG, "mobile recharge selected");
//                if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
//                    _asyncStatusList.setVisibility(View.    GONE);
//                }else{
//                    _asyncStatusList.setVisibility(View.VISIBLE);
//                }
                showFragment(MobileRechargeFragment.newInstance(_currentPlatform));
                break;
            case DTH_RECHARGE:
                Log.d(_LOG, "show dth recharge selected");
                if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                    _asyncStatusList.setVisibility(View.    GONE);
                }else{
                    _asyncStatusList.setVisibility(View.VISIBLE);
                }
                showFragment(DTHRechargeFragment.newInstance(_currentPlatform));
                break;
            case BILL_PAYMENT:
//                if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
//                    _asyncStatusList.setVisibility(View.    GONE);
//                }else{
//                    _asyncStatusList.setVisibility(View.VISIBLE);
//                }
                showFragment(BillPaymentFragment.newInstance(_currentPlatform));
                break;

            case UTILITY_BILL_PAYMENT:
//                if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
//                    _asyncStatusList.setVisibility(View.    GONE);
//                }else{
//                    _asyncStatusList.setVisibility(View.VISIBLE);
//                }
                showFragment(UtilityBillPaymentFragment.newInstance(_currentPlatform));
                break;
            case LIC:
                Log.d(_LOG, "lic selected");
//                if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
//                    _asyncStatusList.setVisibility(View.    GONE);
//                }else{
//                    _asyncStatusList.setVisibility(View.VISIBLE);
//                }
                showFragment(LICFragment.newInstance(_currentPlatform));
                break;
            case BALANCE_TRANSFER:
//                if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
//                    _asyncStatusList.setVisibility(View.    GONE);
//                }else{
//                    _asyncStatusList.setVisibility(View.VISIBLE);
//                }
                showFragment(BalanceTransferFragment.newInstance(_currentPlatform));
                break;
            case IMPS:
//                if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
//                    _asyncStatusList.setVisibility(View.    GONE);
//                }else{
//                    _asyncStatusList.setVisibility(View.VISIBLE);
//                }
                showFragment(IMPSFragment.newInstance(_currentPlatform));

              break;

            case CHANGE_MPIN:
                if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                    _asyncStatusList.setVisibility(View.    GONE);
                }else{
                    _asyncStatusList.setVisibility(View.VISIBLE);
                }
                showFragment(ChangePinFragment.newInstance(_currentPlatform, PinType.M_PIN));
                break;
            case CHANGE_TPIN:
//                if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
//                    _asyncStatusList.setVisibility(View.    GONE);
//                }else{
//                    _asyncStatusList.setVisibility(View.VISIBLE);
//                }
                showFragment(ChangePinFragment.newInstance(_currentPlatform, PinType.T_PIN));
                break;
            case CHANGE_PASSWORD:
//                if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
//                    _asyncStatusList.setVisibility(View.    GONE);
//                }else{
//                    _asyncStatusList.setVisibility(View.VISIBLE);
//                }
                showFragment(ChangePinFragment.newInstance(_currentPlatform, PinType.PBX_CHANGE_PASSWORD));
                break;
            case HISTORY:
                if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                    _asyncStatusList.setVisibility(View.    GONE);
                }else{
                    _asyncStatusList.setVisibility(View.VISIBLE);
                }
                showFragment(TransactionHistoryFragment.newInstance(_currentPlatform));
                break;
            case SETTINGS:
                if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                    _asyncStatusList.setVisibility(View.    GONE);
                }else{
                    _asyncStatusList.setVisibility(View.VISIBLE);
                }
                showFragment(SettingsFragment.newInstance(_currentPlatform));
                break;
            case LOGOUT:

                if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                    _asyncStatusList.setVisibility(View.    GONE);
                }else{
                    _asyncStatusList.setVisibility(View.VISIBLE);
                }
                confirmLogout();
                break;

        }

        _currentScreen  = screen;
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
       _progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
       Log.d(_LOG, "Back pressed");
       FragmentManager fragMgr = getSupportFragmentManager();
       FragmentBase fb= (FragmentBase)fragMgr.findFragmentById(R.id.contentFrame);
        fb.cancelAsynTaskFromFrag();
        showProgress(false);
        if(_currentScreen != MoMScreen.DASHBOARD){
            showScreen(MoMScreen.DASHBOARD);
        }else{
            confirmLogout();
        }
        if(_currentScreen == MoMScreen.IMPS){

            _asyncStatusList.setVisibility(View.GONE);
        }else{
            _asyncStatusList.setVisibility(View.VISIBLE);

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

        FragmentManager manager = getSupportFragmentManager();
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
                .setPositiveButton(R.string.btn_Ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        EphemeralStorage.getInstance(context).clear();
                        finish();
                    }
                })
                .setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialog.create();
        alert.setTitle("Message");
        alert.show();
    }
}