package com.mom.app.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mom.app.R;
import com.mom.app.error.MOMException;
import com.mom.app.identifier.ActivityIdentifier;
import com.mom.app.identifier.IdentifierUtils;
import com.mom.app.identifier.PlatformIdentifier;
import com.mom.app.model.AsyncListener;
import com.mom.app.model.AsyncResult;
import com.mom.app.model.DataExImpl;
import com.mom.app.model.IDataEx;
import com.mom.app.model.local.EphemeralStorage;
import com.mom.app.model.mompl.MoMPLDataExImpl;
import com.mom.app.model.pbxpl.PBXPLDataExImpl;
import com.mom.app.utils.AppConstants;

import java.text.DecimalFormat;

/**
 * Created by vaibhavsinha on 7/9/14.
 */
public abstract class MOMActivityBase extends Activity{
    String _LOG         = AppConstants.LOG_PREFIX + "BASE";

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
        return getDataEx(pListener, null);
    }

    public IDataEx getDataEx(AsyncListener pListener, DataExImpl.Methods method){
        try {
            if (_dataEx == null) {
                if (_currentPlatform == PlatformIdentifier.MOM) {
                    _dataEx = new MoMPLDataExImpl(getApplicationContext(), pListener);
                } else {
                    _dataEx = new PBXPLDataExImpl(getApplicationContext(), method, pListener);
                }
            }
        }catch(MOMException me){
            if(me.getCode() == AsyncResult.CODE.NOT_LOGGED_IN){
                goToLogin();
            }

            Log.e(_LOG, "Error in getting dataex object", me);
        }

        return _dataEx;
    }

    public void goToLogin(){
        Intent intent           = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
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
//        Intent intent = new Intent(this, DashboardActivity.class);
//        startActivity(intent);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        this.finish();
//        return;
    }

    protected void showBalance(TextView tv){
        float balance         = EphemeralStorage.getInstance(this).getFloat(AppConstants.USER_BALANCE, AppConstants.ERROR_BALANCE);
        showBalance(tv, balance);
    }

    protected void showBalance(TextView tv, Float balance){
        String sBal         = null;
        if(balance == AppConstants.ERROR_BALANCE){
            sBal            = getString(R.string.error_getting_balance);
            return;
        }else {
            DecimalFormat df = new DecimalFormat("#,###,###,##0.00");
            sBal = df.format(balance);
        }

        tv.setText("Balance: " + getResources().getString(R.string.Rupee) + sBal);
    }

    public void getBalance(){
        final Context context   = this;
        Log.d(_LOG, "Getting Balance");
        getDataEx(new AsyncListener<Float>() {
            @Override
            public void onTaskSuccess(Float result, DataExImpl.Methods callback) {
                EphemeralStorage.getInstance(context).storeFloat(AppConstants.USER_BALANCE, result);
                showBalance(result);
            }

            @Override
            public void onTaskError(AsyncResult pResult, DataExImpl.Methods callback) {

            }
        }).getBalance();
        Log.d(_LOG, "getBalance called");
    }

    public void navigateToTransactionMessageActivity(ActivityIdentifier pSendingActivity, String psMsg){
        Log.d(_LOG, "Going to show response: " + psMsg);

        Intent intent   = new Intent(this, TransactionMessageActivity.class);
        intent.putExtra(AppConstants.INTENT_MESSAGE, psMsg);
        intent.putExtra(AppConstants.INTENT_MESSAGE_ORIGIN, pSendingActivity);
        startActivity(intent);
        finish();
    }
}
