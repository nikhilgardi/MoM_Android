package com.mom.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mom.app.R;
import com.mom.app.identifier.ActivityIdentifier;
import com.mom.app.identifier.IdentifierUtils;
import com.mom.app.utils.AppConstants;

public class TransactionMessageActivity extends Activity {

    ActivityIdentifier _originActivity  = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_message);

        TextView textView   = (TextView)findViewById(R.id.confirmationMessage);

        String message      = getIntent().getStringExtra(AppConstants.INTENT_MESSAGE);
        /*
        Not using getParentActivityIntent to maintain minSdk level of 8
         */
        _originActivity     = (ActivityIdentifier) getIntent().getSerializableExtra(AppConstants.INTENT_MESSAGE_ORIGIN);

        if(_originActivity == ActivityIdentifier.BILL_PAYMENT){
            Button btnRecharge  = (Button) findViewById(R.id.btnNewRecharge);
            btnRecharge.setText(getResources().getString(R.string.btn_new_bill_pay));
        }

        textView.setText(message);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.transaction_confirmation, menu);
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

    public void goToRecharge(View view){
        Intent intent   = new Intent(this, IdentifierUtils.getActivityClass(_originActivity));
        startActivity(intent);
        finish();
    }

    public void goToMain(View view){
//        Intent intent   = new Intent(this, DashboardActivity.class);
//        startActivity(intent);
//        finish();
    }
}
