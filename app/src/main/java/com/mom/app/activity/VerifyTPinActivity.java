package com.mom.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mom.app.R;
import com.mom.app.identifier.ActivityIdentifier;
import com.mom.app.identifier.IdentifierUtils;
import com.mom.app.model.AsyncListener;
import com.mom.app.model.AsyncResult;
import com.mom.app.model.DataExImpl;
import com.mom.app.model.IDataEx;
import com.mom.app.model.newpl.NewPLDataExImpl;
import com.mom.app.utils.MOMConstants;

public class VerifyTPinActivity extends MOMActivityBase implements AsyncListener<String>{

    ActivityIdentifier _destinationActivity         = null;
    ActivityIdentifier _originActivity              = null;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_tpin);
        Intent intent = getIntent();
        _destinationActivity  = (ActivityIdentifier) intent.getSerializableExtra(MOMConstants.INTENT_MESSAGE_DEST);
        _originActivity  = (ActivityIdentifier) intent.getSerializableExtra(MOMConstants.INTENT_MESSAGE_ORIGIN);

        getProgressBar().setVisibility(View.GONE);
    }

    public void setOriginDestinationActivity(ActivityIdentifier pOrigin, ActivityIdentifier pDestination){
        this._originActivity        = pOrigin;
        this._destinationActivity   = pDestination;
    }

    @Override
    protected void showBalance(float pfBalance) {

    }

    @Override
    public void onTaskSuccess(String result, DataExImpl.Methods callback) {
        getProgressBar().setVisibility(View.GONE);
        Log.d("TPIN_COMPLETE", "Result: " + result);

        if(result == null || "".equals(result.trim())){
            Log.w("TPIN_COMPLETE", "Empty verification result!");
            showMessage(getResources().getString(R.string.tpin_incorrect));
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

    @Override
    public void onTaskError(AsyncResult pResult, DataExImpl.Methods callback) {

    }

    public void verifyTPin(View view){
        getProgressBar().setVisibility(View.VISIBLE);
        Log.d("TPIN", "Verifying TPIN");
        getMessageTextView().setVisibility(View.GONE);
        EditText tpinTxt    = (EditText)findViewById(R.id.tpinTxt);
        String sTPin        = tpinTxt.getText().toString();

        if("".equals(sTPin.trim())){
            showMessage(getResources().getString(R.string.prompt_enter_tpin));
            return;
        }

        IDataEx dataEx  = new NewPLDataExImpl(getApplicationContext(), this);
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

}
