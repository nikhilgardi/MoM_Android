package com.mom.app.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mom.app.R;
import com.mom.app.error.MOMException;
import com.mom.app.fragment.DTHRechargeFragment;
import com.mom.app.fragment.FragmentBase;
import com.mom.app.fragment.MobileRechargeFragment;
import com.mom.app.identifier.PlatformIdentifier;
import com.mom.app.ui.IFragmentListener;
import com.mom.app.ui.flow.MoMScreen;
import com.mom.app.utils.AppConstants;
import com.mom.app.utils.DataProvider;
import com.mom.app.widget.ImageTextViewAdapter;
import com.mom.app.widget.holder.ImageItem;

import java.util.ArrayList;

public class BaseActivity extends ActionBarActivity implements IFragmentListener{

    String _LOG = AppConstants.LOG_PREFIX + "BASE ACT";

    DrawerLayout _drawerLayout;
    ListView _drawerList;
    PlatformIdentifier _currentPlatform;

    ArrayList<ImageItem<MoMScreen>> _menuItems;
    ImageTextViewAdapter _imageTextAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        /*
        THIS IS HARD CODED. ONLY USED FOR TESTING OF PBX PLATFORM. REMOVE!!!
         */

        _currentPlatform = PlatformIdentifier.PBX;

        setupDrawerMenu();

        setupActionBar();

        if (savedInstanceState == null) {
            selectItem(0);
        }
    }

    private void setupDrawerMenu(){
        if(_currentPlatform == null){
            throw new IllegalStateException("platform should have been identified by now");
        }

        _menuItems = DataProvider.getScreens(this, _currentPlatform);

        _drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        _drawerList = (ListView) findViewById(R.id.leftDrawer);

        _imageTextAdapter   = new ImageTextViewAdapter<MoMScreen>(this, R.layout.drawer_list_item, _menuItems);

        // Set the adapter for the list view
        _drawerList.setAdapter(_imageTextAdapter);
        // Set the list's click listener
        _drawerList.setOnItemClickListener(new DrawerItemClickListener());

        Log.d(_LOG, "ActionBar set");
        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(
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

        _drawerLayout.setDrawerListener(drawerToggle);
    }

    private void setupActionBar(){
        if(getActionBar() == null){
            return;
        }

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayShowTitleEnabled(true);
        getActionBar().setTitle(R.string.money_on_mobile);
        getActionBar().setDisplayUseLogoEnabled(false);
        getActionBar().setIcon(R.color.transparent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.base, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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
        transaction.addToBackStack(null);
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

        /*
        Hard coded position 'case' needs to change.
         */
        switch (appScreen){
            case MOBILE_RECHARGE:
                Log.d(_LOG, "mobile recharge selected");
                showMobileRecharge();
                break;
            case DTH_RECHARGE:
                Log.d(_LOG, "show dth recharge selected");
                showDTHRecharge();
                break;
            default:
                Log.d(_LOG, "No selection. Showing dashboard");
                showDashboard();
        }

        Log.d(_LOG, "exiting selectItem");
    }

    private void showMobileRecharge(){
        showFragment(MobileRechargeFragment.newInstance());
    }

    private void showDTHRecharge(){
        showFragment(DTHRechargeFragment.newInstance());
    }

    private void showDashboard(){

    }
}
