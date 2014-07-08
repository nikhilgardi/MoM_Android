package com.mom.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mom.app.R;
import com.mom.app.error.MOMException;
import com.mom.app.identifier.ActivityIdentifier;
import com.mom.app.identifier.IdentifierUtils;
import com.mom.app.model.AsyncListener;
import com.mom.app.model.DataExImpl;
import com.mom.app.model.IDataEx;
import com.mom.app.model.local.LocalStorage;
import com.mom.app.model.newpl.NewPLDataExImpl;
import com.mom.app.utils.MOMConstants;

import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Text;

public class VerifyTPinActivity extends Activity implements AsyncListener{

    ActivityIdentifier _destinationActivity         = null;
    ActivityIdentifier _originActivity              = null;
    TextView _messageTextView                       = null;
    private ProgressBar _pb                         = null;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_tpin);
        Intent intent = getIntent();
        _destinationActivity  = (ActivityIdentifier) intent.getSerializableExtra(MOMConstants.INTENT_MESSAGE_DEST);
        _originActivity  = (ActivityIdentifier) intent.getSerializableExtra(MOMConstants.INTENT_MESSAGE_ORIGIN);

        getProgressBar().setVisibility(View.GONE);
    }

    public ProgressBar getProgressBar(){
        if(_pb == null){
            _pb			= (ProgressBar)findViewById(R.id.progressBar);
        }
        return _pb;
    }

    @Override
    public void onTaskComplete(String result, DataExImpl.Methods callback) {
        getProgressBar().setVisibility(View.GONE);
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
        TextView textView       = getMessageTextView();
        textView.setVisibility(View.VISIBLE);
        textView.setText(psMsg);
    }

    public TextView getMessageTextView(){
        if(_messageTextView == null){
            _messageTextView    = (TextView)findViewById(R.id.msgDisplay);
        }
        return _messageTextView;
    }

    public void verifyTPin(View view){
        getProgressBar().setVisibility(View.VISIBLE);
        Log.d("TPIN", "Verifying TPIN");
        getMessageTextView().setVisibility(View.GONE);
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

    public void goBack(View view){
        Intent intent = new Intent(this, IdentifierUtils.getActivityClass(_originActivity));
        /*
        The next method requires API level 16 and above so not using.
         */
//        Intent intent = getParentActivityIntent();
        startActivity(intent);
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
