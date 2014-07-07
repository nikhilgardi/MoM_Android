package com.mom.app.activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.mom.app.R;
import com.mom.app.identifier.ActivityIdentifier;
import com.mom.app.identifier.IdentifierUtils;
import com.mom.app.model.AsyncListener;
import com.mom.app.model.DataExImpl;
import com.mom.app.model.IDataEx;
import com.mom.app.model.local.LocalStorage;
import com.mom.app.model.newpl.NewPLDataExImpl;
import com.mom.app.utils.MOMConstants;

import org.apache.http.message.BasicNameValuePair;

public class VerifyTPinActivity extends ActionBarActivity implements AsyncListener{

    ActivityIdentifier _destinationActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_tpin);
        Intent intent = getIntent();
        _destinationActivity  = (ActivityIdentifier) intent.getSerializableExtra(MOMConstants.INTENT_MESSAGE);
    }

    @Override
    public void onTaskComplete(String result, DataExImpl.Methods callback) {
        Log.d("TPIN_COMPLETE", "Result: " + result);

        if("".equals(result.trim())){
            Log.w("TPIN_COMPLETE", "Empty verification result!");
            return;
        }

        boolean bVerified   = Boolean.valueOf(result);
        if(!bVerified){
            showMessage(getResources().getString(R.string.tpin_incorrect));
            return;
        }

        Log.d("TPIN_COMPLETE", "Sending back to: " + _destinationActivity);

        startActivity(new Intent(this, IdentifierUtils.getActivityClass(_destinationActivity)));
    }

    public void showMessage(String psMsg){
        TextView txtView    = (TextView)findViewById(R.id.msgDisplay);
        txtView.setText(psMsg);
    }

    public void verifyTPin(){

        Log.d("TPIN", "Verifying TPIN");

        EditText tpinTxt    = (EditText)findViewById(R.id.tpinTxt);
        String sTPin        = tpinTxt.getText().toString();

        if("".equals(sTPin.trim())){
            showMessage(getResources().getString(R.string.tpin_required));
            return;
        }

        IDataEx dataEx  = new NewPLDataExImpl(this, getApplicationContext());
        Log.d("TPIN", "DataEx instance created");

        dataEx.verifyTPin(sTPin);
        Log.d("TPIN", "verifyTPin called");
    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.verify_tpin, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
