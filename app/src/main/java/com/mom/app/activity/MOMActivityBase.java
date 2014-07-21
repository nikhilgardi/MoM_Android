package com.mom.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mom.app.R;
import com.mom.app.identifier.ActivityIdentifier;
import com.mom.app.identifier.IdentifierUtils;
import com.mom.app.identifier.PlatformIdentifier;
import com.mom.app.model.AsyncListener;
import com.mom.app.model.AsyncResult;
import com.mom.app.model.DataExImpl;
import com.mom.app.model.IDataEx;
import com.mom.app.model.local.LocalStorage;
import com.mom.app.model.newpl.NewPLDataExImpl;
import com.mom.app.model.pbxpl.PBXPLDataExImpl;
import com.mom.app.utils.MOMConstants;

import java.text.DecimalFormat;

/**
 * Created by vaibhavsinha on 7/9/14.
 */
public abstract class MOMActivityBase extends Activity{
    PlatformIdentifier _currentPlatform;
    IDataEx _dataEx     = null;
    ProgressBar _pb;
    TextView tvMsgDisplay;

    protected abstract void showBalance(float pfBalance);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _currentPlatform        = IdentifierUtils.getPlatformIdentifier(getApplicationContext());
    }

    public void setDataEx(IDataEx pDataEx){
        this._dataEx    = pDataEx;
    }

    public IDataEx getDataEx(AsyncListener pListener){
        if(_dataEx == null){
            if(_currentPlatform == PlatformIdentifier.NEW){
                _dataEx     = new NewPLDataExImpl(getApplicationContext(), pListener);
            }else{
                _dataEx     = new PBXPLDataExImpl(pListener, getApplicationContext());
            }
        }

        return _dataEx;
    }

    public void showMessage(String psMsg){
        TextView response	= getMessageTextView();

        response.setVisibility(View.VISIBLE);
        response.setText(psMsg);
    }

    public TextView getMessageTextView(){
        if(tvMsgDisplay == null){
            tvMsgDisplay    = (TextView) findViewById(R.id.msgDisplay);
        }
        return tvMsgDisplay;
    }

    public ProgressBar getProgressBar(){
        if(_pb == null){
            _pb			= (ProgressBar)findViewById(R.id.progressBar);
        }
        return _pb;
    }

    public void goBack(View view) {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.finish();
        return;
    }

    protected void showBalance(TextView tv){
        float balance         = LocalStorage.getFloat(getApplicationContext(), MOMConstants.USER_BALANCE);
        showBalance(tv, balance);
    }

    protected void showBalance(TextView tv, Float balance){
        DecimalFormat df   = new DecimalFormat( "#,###,###,##0.00" );
        String sBal         = df.format(balance);
        tv.setText(sBal);
    }

    public void getBalanceAsync(){
        Log.d("MAIN", "Getting Balance");
        IDataEx dataEx  = new NewPLDataExImpl(getApplicationContext(), new AsyncListener<Float>() {
            @Override
            public void onTaskSuccess(Float result, DataExImpl.Methods callback) {
                LocalStorage.storeLocally(getApplicationContext(), MOMConstants.USER_BALANCE, result);
                showBalance(result);
            }

            @Override
            public void onTaskError(AsyncResult pResult, DataExImpl.Methods callback) {

            }
        });
        Log.d("MAIN", "DataEx instance created");
        dataEx.getBalance();
        Log.d("MAIN", "getBalance called");
    }

    public void navigateToTransactionMessageActivity(ActivityIdentifier pSendingActivity, String psMsg){
        Log.d("NAVIGATE_TO_TXN_MSG", "Going to show response: " + psMsg);

        Intent intent   = new Intent(this, TransactionMessageActivity.class);
        intent.putExtra(MOMConstants.INTENT_MESSAGE, psMsg);
        intent.putExtra(MOMConstants.INTENT_MESSAGE_ORIGIN, pSendingActivity);
        startActivity(intent);
        finish();
    }
}
