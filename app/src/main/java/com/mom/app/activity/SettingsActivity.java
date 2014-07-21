package com.mom.app.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mom.app.R;
import com.mom.app.identifier.IdentifierUtils;
import com.mom.app.identifier.PinType;
import com.mom.app.identifier.PlatformIdentifier;
import com.mom.app.utils.MOMConstants;

public class SettingsActivity extends ListActivity {

    private PlatformIdentifier _currentPlatform;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        _currentPlatform    = IdentifierUtils.getPlatformIdentifier(getApplicationContext());

        String[] values     = new String[]{"Change M-Pin", "Change T-Pin"};

        setContentView(R.layout.activity_settings);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.row_layout, R.id.label, values);
        setListAdapter(adapter);
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
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String item = (String) getListAdapter().getItem(position);
        Intent intent   = null;

        Log.d("LIST_CLICKED", "Going to start activity");

        intent = new Intent(this, ChangePINActivity.class);

        if (item.equals("Change M-Pin")) {
            Log.d("LIST_CLICKED", "Starting Change M-Pin");
            intent.putExtra(MOMConstants.INTENT_MESSAGE, PinType.M_PIN);
        }else if (item.equals("Change T-Pin")) {
            Log.d("LIST_CLICKED", "Starting Change T-Pin");
            intent.putExtra(MOMConstants.INTENT_MESSAGE, PinType.T_PIN);
        }

        startActivity(intent);
        Log.d("LIST_CLICKED", "Started Change Pin");
    }
}
