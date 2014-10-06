package com.mom.app.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.mom.app.R;
import com.mom.app.identifier.IdentifierUtils;
import com.mom.app.identifier.PlatformIdentifier;
import com.mom.app.utils.AppConstants;
import com.mom.app.utils.DataProvider;
import com.mom.app.widget.ImageTextViewAdapter;
import com.mom.app.widget.holder.ImageItem;

public class SettingsActivity extends MOMActivityBase {
    String _LOG             = AppConstants.LOG_PREFIX + "SETTINGS";
    
    private PlatformIdentifier _currentPlatform;
    GridView gridView;
    ImageTextViewAdapter gridViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        _currentPlatform    = IdentifierUtils.getPlatformIdentifier(getApplicationContext());

        gridView            = (GridView) findViewById(R.id.gridView);
        gridViewAdapter     = new ImageTextViewAdapter(this, R.layout.row_grid, DataProvider.getScreens(this, _currentPlatform));

        gridView.setAdapter(gridViewAdapter);
        gridView.setNumColumns(2);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                Log.d(_LOG, "Clicked " + position);
                ImageItem item = (ImageItem) gridViewAdapter.getItem(position);
                if (item == null) {
                    Log.e(_LOG, "No click target found, returning.");
                    return;
                }
                Log.d(_LOG, "Altering selection to " + !item.getSelected());

                item.setSelected(!item.getSelected());

                if (item.getSelected()) {
                    v.setBackgroundColor(getResources().getColor(R.color.row_selected));
                } else {
                    v.setBackgroundColor(Color.TRANSPARENT);
                }

                Log.d(_LOG, "Going to selected activity");
                nextActivity(item.getTitle());
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings, menu);
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
    protected void showBalance(float pfBalance) {

    }

    private void nextActivity(String item) {
        Intent intent = null;
        if (item.equals("Change M-Pin")) {
            Log.d(_LOG, "Starting Change M-Pin");
            intent      = new Intent(this, ChangePINActivity.class);
            startActivity(intent);
            Log.d(_LOG, "Started Mobile Recharge");
            return;
        } else if (item.equals("Change T-Pin")) {
            Log.d(_LOG, "Starting Change T_Pin");
            intent = new Intent(this, ChangePINActivity.class);

            startActivity(intent);
            Log.d(_LOG, "Started DTH Recharge");
            return;

        }
    }
}
