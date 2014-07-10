package com.mom.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.mom.app.identifier.ActivityIdentifier;
import com.mom.app.identifier.PlatformIdentifier;
import com.mom.app.model.AsyncListener;
import com.mom.app.model.DataExImpl;
import com.mom.app.model.IDataEx;
import com.mom.app.model.local.LocalStorage;
import com.mom.app.model.newpl.NewPLDataExImpl;
import com.mom.app.utils.MOMConstants;

/**
 * Created by vaibhavsinha on 7/9/14.
 */
public abstract class TransactionActivityBase extends Activity{
    protected abstract void showBalance(float pfBalance);

    public void getBalanceAsync(){
        Log.d("MAIN", "Getting Balance");
        IDataEx dataEx  = new NewPLDataExImpl(getApplicationContext(), new AsyncListener<Float>() {
            @Override
            public void onTaskComplete(Float result, DataExImpl.Methods callback) {
                LocalStorage.storeLocally(getApplicationContext(), MOMConstants.USER_BALANCE, result);
                showBalance(result);
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
