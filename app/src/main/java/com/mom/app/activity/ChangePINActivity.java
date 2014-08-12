package com.mom.app.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.mom.app.R;
import com.mom.app.identifier.PinType;
import com.mom.app.model.AsyncListener;
import com.mom.app.model.AsyncResult;
import com.mom.app.model.DataExImpl;
import com.mom.app.utils.AppConstants;

public class ChangePINActivity extends MOMActivityBase  implements AsyncListener<String>{
    String _LOG             = AppConstants.LOG_PREFIX + "CHANGE PIN";
    
    private PinType _pinType;

    private EditText _oldPin;
    private EditText _newPin;
    private EditText _newPinConfirm;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pin);

        Log.i(_LOG, "started onCreate");

        _pinType            = (PinType) getIntent().getSerializableExtra(AppConstants.INTENT_MESSAGE);
        _oldPin             = (EditText)findViewById(R.id.oldPin);
        _newPin             = (EditText)findViewById(R.id.newPin);
        _newPinConfirm      = (EditText)findViewById(R.id.newPinConfirm);
    }

    @Override
    protected void showBalance(float pfBalance) {

    }

    @Override
    public void onTaskSuccess(String result, DataExImpl.Methods callback) {
        Log.d(_LOG, "Change Pin callback, result: " + result);

        if(callback != DataExImpl.Methods.CHANGE_PIN){
            Log.d(_LOG, "Callback not for CHANGE_PIN, called for: " + callback);
            return;
        }
        Log.d(_LOG, "Displaying message to user");
        showMessage(result);
    }

    @Override
    public void onTaskError(AsyncResult pResult, DataExImpl.Methods callback) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.change_pin, menu);
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
        String sOldPin          = _oldPin.getText().toString();
        String sNewPin          = _newPin.getText().toString();
        String sNewPinConfirm   = _newPinConfirm.getText().toString();
        String sRequiredError   = getResources().getString(R.string.error_field_required);

        if("".equals(sOldPin)){
            _oldPin.setError(sRequiredError);
            return;
        }
        if("".equals(sNewPin)){
            _newPin.setError(sRequiredError);
            return;
        }
        if("".equals(sNewPinConfirm)){
            _newPinConfirm.setError(sRequiredError);
            return;
        }

        if(!sNewPin.equals(sNewPinConfirm)){
            _newPinConfirm.setError(getResources().getString(R.string.error_pin_confirm_mismatch));
            return;
        }

        getDataEx(this).changePin(_pinType, sOldPin, sNewPin);

    }
}
