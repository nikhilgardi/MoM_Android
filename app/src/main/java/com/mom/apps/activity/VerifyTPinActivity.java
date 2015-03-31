package com.mom.apps.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.mom.apps.R;
import com.mom.apps.identifier.ActivityIdentifier;
import com.mom.apps.identifier.IdentifierUtils;
import com.mom.apps.model.AsyncListener;
import com.mom.apps.model.AsyncResult;
import com.mom.apps.model.DataExImpl;
import com.mom.apps.model.IDataEx;
import com.mom.apps.model.mompl.MoMPLDataExImpl;
import com.mom.apps.utils.AppConstants;

public class VerifyTPinActivity extends MOMActivityBase implements AsyncListener<String>{
    String _LOG             = AppConstants.LOG_PREFIX + "VERIFY TPIN";
    
            ActivityIdentifier _destinationActivity         = null;
    ActivityIdentifier _originActivity              = null;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_tpin);
        Intent intent = getIntent();
        _destinationActivity  = (ActivityIdentifier) intent.getSerializableExtra(AppConstants.INTENT_MESSAGE_DEST);
        _originActivity  = (ActivityIdentifier) intent.getSerializableExtra(AppConstants.INTENT_MESSAGE_ORIGIN);

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
        Log.d(_LOG, "Result: " + result);

        if(result == null || "".equals(result.trim())){
            Log.w(_LOG, "Empty verification result!");
            showMessage(getResources().getString(R.string.tpin_incorrect));
            return;
        }

        boolean bVerified   = Boolean.valueOf(result);

        if(!bVerified){
            showMessage(getResources().getString(R.string.tpin_incorrect));
            return;
        }

        Log.d(_LOG, "Sending back to: " + _destinationActivity);

        startActivity(new Intent(this, IdentifierUtils.getActivityClass(_destinationActivity)));
    }

    @Override
    public void onTaskError(AsyncResult pResult, DataExImpl.Methods callback) {

    }

    public void verifyTPin(View view){
        getProgressBar().setVisibility(View.VISIBLE);
        Log.d(_LOG, "Verifying TPIN");
        getMessageTextView().setVisibility(View.GONE);
        EditText tpinTxt    = (EditText)findViewById(R.id.tpinTxt);
        String sTPin        = tpinTxt.getText().toString();

        if("".equals(sTPin.trim())){
            showMessage(getResources().getString(R.string.prompt_enter_tpin));
            return;
        }

        IDataEx dataEx  = new MoMPLDataExImpl(getApplicationContext(), this);
        Log.d(_LOG, "DataEx instance created");

        dataEx.verifyTPin(sTPin);
        Log.d(_LOG, "verifyTPin called");
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
