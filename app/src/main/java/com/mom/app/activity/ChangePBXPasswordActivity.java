package com.mom.app.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.mom.app.R;
import com.mom.app.model.AsyncListener;
import com.mom.app.model.AsyncResult;
import com.mom.app.model.DataExImpl;
import com.mom.app.utils.AppConstants;

public class ChangePBXPasswordActivity extends MOMActivityBase  implements AsyncListener<String>{
    String _LOG             = AppConstants.LOG_PREFIX + "CHANGE PASSWORD";



    private EditText _oldPassword;
    private EditText _newPassword;
    private EditText _newPasswordConfirm;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pbxpassword);

        Log.i(_LOG, "started onCreate");

      //  _pinType            = (PinType) getIntent().getSerializableExtra(AppConstants.INTENT_MESSAGE);
        _oldPassword             = (EditText)findViewById(R.id.oldPin);
        _newPassword             = (EditText)findViewById(R.id.newPin);
        _newPasswordConfirm      = (EditText)findViewById(R.id.newPinConfirm);
    }

    @Override
    protected void showBalance(float pfBalance) {

    }

    @Override
    public void onTaskSuccess(String result, DataExImpl.Methods callback) {
        Log.d(_LOG, "Change Password callback, result: " + result);

        if(callback != DataExImpl.Methods.CHANGE_PASSWORD){
            Log.d(_LOG, "Callback not for CHANGE_PASSWORD, called for: " + callback);
            return;
        }
        Log.d(_LOG, "Displaying message to user");
        showMessage(result);
        }

//    @Override
//    public void onTaskSuccess(String result, DataExImpl.Methods callback) {
//        Log.d(_LOG, "Called back");
//        switch(callback){
//            case CHANGE_PASSWORD:
//                if(result == null){
//                    Log.d(_LOG, "Callback not for CHANGE_PASSWORD, called for: " + callback);
//                    showMessage(getResources().getString(R.string.error_recharge_failed));
//                    return;
//                }
//                Log.d(_LOG, "Going to get new balance");
//                getBalance();
//                Log.d(_LOG, "Starting navigation to TxnMsg Activity");
//                showMessage(result);
//                Log.i("Changepwd" , result);
//               // navigateToTransactionMessageActivity(ActivityIdentifier.CHANGE_PASSWORD, result);
//                break;
//        }
//
//
//    }




    @Override
    public void onTaskError(AsyncResult pResult, DataExImpl.Methods callback) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.change_pbxpassword, menu);
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

    public void changePIN(View view){
        String sOldPin          = _oldPassword.getText().toString();
        String sNewPin          = _newPassword.getText().toString();
        String sNewPinConfirm   = _newPasswordConfirm.getText().toString();
        String sRequiredError   = getResources().getString(R.string.error_field_required);

        if("".equals(sOldPin)){
            _oldPassword.setError(sRequiredError);
            return;
        }
        if("".equals(sNewPin)){
            _newPassword.setError(sRequiredError);
            return;
        }
        if("".equals(sNewPinConfirm)){
            _newPasswordConfirm.setError(sRequiredError);
            return;
        }

        if(!sNewPin.equals(sNewPinConfirm)){
            _newPasswordConfirm.setError(getResources().getString(R.string.error_pin_confirm_mismatch));
            return;
        }

        getDataEx(this).changePassword( sOldPin, sNewPin);

    }
}
