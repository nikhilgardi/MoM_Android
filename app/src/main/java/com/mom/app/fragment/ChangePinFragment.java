package com.mom.app.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.mom.app.R;
import com.mom.app.identifier.PinType;
import com.mom.app.identifier.PlatformIdentifier;
import com.mom.app.model.AsyncListener;
import com.mom.app.model.AsyncResult;
import com.mom.app.model.DataExImpl;
import com.mom.app.ui.TransactionRequest;
import com.mom.app.utils.AppConstants;

/**
 * Created by Akanksha Raina on 03/12/2014.
 */
public class ChangePinFragment extends FragmentBase implements AsyncListener<TransactionRequest> {
    String _LOG             = AppConstants.LOG_PREFIX + "CHANGE PIN";

    private PinType _pinType;

    private EditText _oldPin;
    private EditText _newPin;
    private EditText _newPinConfirm;

    Button _submitButton;

    public static ChangePinFragment newInstance(PlatformIdentifier currentPlatform , PinType pinType){
        ChangePinFragment fragment    = new ChangePinFragment();
        Bundle bundle                   = new Bundle();
        bundle.putSerializable(AppConstants.ACTIVE_PLATFORM, currentPlatform);
        bundle.putSerializable(AppConstants.PIN , pinType);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _currentPlatform        = (PlatformIdentifier) getArguments().getSerializable(AppConstants.ACTIVE_PLATFORM);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(_LOG, "onCreateView");
        View view = inflater.inflate(R.layout.activity_m_pin, null, false);
        _pinType            = (PinType) getArguments().getSerializable(AppConstants.PIN);
            switch (_pinType)
            {
               case T_PIN:

                view = inflater.inflate(R.layout.activity_t_pin, null, false);

                    break;
            }

        _oldPin             = (EditText)view.findViewById(R.id.oldPin);
        _newPin             = (EditText) view.findViewById(R.id.newPin);
        _newPinConfirm      = (EditText)view.findViewById(R.id.newPinConfirm);
        _submitButton     = (Button) view.findViewById(R.id.btnSubmit);

        Log.i(_LOG, "started onCreate");


        _submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePIN(view);
            }
        });


        return view;
    }



    @Override
    public void onTaskSuccess(TransactionRequest result, DataExImpl.Methods callback) {
        Log.d(_LOG, "Called back");
        switch(callback){
            case CHANGE_PIN:
                if(result == null){
                    Log.d(_LOG, "Obtained NULL recharge response");
                    showMessage(getResources().getString(R.string.error_pin_confirm_mismatch));
                    return;
                }
                Log.d(_LOG, "Starting navigation to TxnMsg Activity");

                break;
        }
        Log.i("ResultChange" , result.getRemoteResponse()+ result.getResponseCode());
        showMessage(result.getRemoteResponse());
    }

    @Override
    public void onTaskError(AsyncResult pResult, DataExImpl.Methods callback) {

    }

    @Override
    protected void showBalance(float pfBalance) {
    }

    private int validate() {

        if (_oldPin.getText().length() <= 8) {
            if (_oldPin.getText().length() >= 4) {
                if (_newPin.getText().length() == 8)
                {

                    if (_newPin.getText().toString().equals(_newPinConfirm.getText().toString()))
                    {
                        if(!_oldPin.getText().toString().equals(_newPin.getText().toString()))
                        {
                            return 0;
                        }
                        else
                            return 5;
                    }
                    else
                        return 4;
                }
                else
                    return 3;
            } else
                return 2;
        } else
            return 1;
    }



   public void changePIN(View view) {
       String sOldPin = _oldPin.getText().toString();
       String sNewPin = _newPin.getText().toString();
       String sNewPinConfirm = _newPinConfirm.getText().toString();
       String sRequiredError = getResources().getString(R.string.error_field_required);
       int ValidationResult = validate();
       if (ValidationResult == 0) {
           getDataEx(this).changePin(_pinType, sOldPin, sNewPin);
       }
       else {

           switch (ValidationResult) {

               case 1:
                   if(_pinType.equals(PinType.M_PIN)) {


                       //	responseText.setText("Enter Correct Old MPIN");
                       _oldPin.setError(getResources().getString(R.string.error_oldMpin));

                   }
                   else if (_pinType.equals(PinType.T_PIN)){
                       _oldPin.setError(getResources().getString(R.string.error_oldTpin));

                   }
                   _oldPin.setText("");
                   _newPin.setText("");
                   _newPinConfirm.setText("");
                   break;

               case 2:

                   if(_pinType.equals(PinType.M_PIN)) {

                   //	responseText.setText("Enter Correct Old MPIN");
                   _oldPin.setError(getResources().getString(R.string.error_oldMpin));

                   }
                   else if (_pinType.equals(PinType.T_PIN)) {
                       _oldPin.setError(getResources().getString(R.string.error_oldTpin));


                   }
                   _oldPin.setText("");
                   _newPin.setText("");
                   _newPinConfirm.setText("");

                   break;

               case 3:
                   if(_pinType.equals(PinType.M_PIN)) {

                       //	responseText.setText("The new MPIN should be 8 numeric characters only");
                       _newPin.setError(getResources().getString(R.string.error_newMpin));

                   }
                   else if(_pinType.equals(PinType.T_PIN))
                   {
                       _newPin.setError(getResources().getString(R.string.error_newTpin));
                       _newPin.setText("");
                       _newPinConfirm.setText("");
                   }

                   _newPin.setText("");
                   _newPinConfirm.setText("");
                   break;

               case 4:

                if(_pinType.equals(PinType.M_PIN)) {
                    //	responseText.setText("New MPIN and Confirm Password doesnot match");
                    _newPin.setError(getResources().getString(R.string.error_Mpin_matching));
                    _newPinConfirm.setError(getResources().getString(R.string.error_Mpin_matching));

                }
                   else if (_pinType.equals(PinType.T_PIN)){
                    _newPin.setError(getResources().getString(R.string.error_Tpin_matching));
                    _newPinConfirm.setError(getResources().getString(R.string.error_Tpin_matching));

                }
                   _newPin.setText("");
                   _newPinConfirm.setText("");
                   break;


               case 5:

                   if(_pinType.equals(PinType.M_PIN)) {
                       //	responseText.setText("Old MPIN and New MPIN cannot be same");
                       _newPin.setError(getResources().getString(R.string.validate_oldnewMpin));

                   }
                   else if (_pinType.equals(PinType.T_PIN)){
                       _newPin.setError(getResources().getString(R.string.validate_oldnewTpin));

                   }
                   _oldPin.setText("");
                   _newPin.setText("");
                   _newPinConfirm.setText("");
                   break;

           }
       }




    }






    public void goBack(View view) {
        getFragmentManager().popBackStack();
    }



}
