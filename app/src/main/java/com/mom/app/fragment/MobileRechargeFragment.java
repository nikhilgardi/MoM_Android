package com.mom.app.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.mom.app.R;
import com.mom.app.error.MOMException;
import com.mom.app.identifier.PlatformIdentifier;
import com.mom.app.identifier.TransactionType;
import com.mom.app.model.AsyncListener;
import com.mom.app.model.AsyncResult;
import com.mom.app.model.DataExImpl;
import com.mom.app.model.Operator;
import com.mom.app.model.pbxpl.PaymentResponse;
import com.mom.app.ui.TransactionRequest;
import com.mom.app.utils.AppConstants;
import com.mom.app.utils.DataProvider;

import java.util.List;

/**
 * A simple {@link android.app.Fragment} subclass.
 *
 */
public class MobileRechargeFragment extends FragmentBase implements AsyncListener<TransactionRequest<PaymentResponse>>{

    String _LOG     = AppConstants.LOG_PREFIX + "MOB_RECHARGE";

    private EditText _etTargetPhone;
    private EditText _etAmount;

    Spinner _spOperator;
    RadioButton _rbtnTopUp, _rbtnValidity, _rbtnSpecial;
    Button _rechargeBtn;


    public MobileRechargeFragment() {
        // Required empty public constructor
    }

    public static MobileRechargeFragment newInstance(PlatformIdentifier currentPlatform){
        MobileRechargeFragment fragment   = new MobileRechargeFragment();
        Bundle bundle                       = new Bundle();
        bundle.putSerializable(AppConstants.ACTIVE_PLATFORM, currentPlatform);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(_LOG, "onCreate");
        _currentPlatform        = (PlatformIdentifier) getArguments().getSerializable(AppConstants.ACTIVE_PLATFORM);
        Log.d(_LOG, "Returning from onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(_LOG, "onCreateView");
        View view                   = inflater.inflate(R.layout.activity_mobile_recharge, null, false);

        setCurrentPlatform();

        this._spOperator = (Spinner) view.findViewById(R.id.Operator);
        this._etTargetPhone
                = (EditText) view.findViewById(R.id.rechargeTargetPhone);

        this._etAmount = (EditText) view.findViewById(R.id.amount);
        this._rbtnTopUp = (RadioButton) view.findViewById(R.id.rbtnTopUp);
        this._rbtnValidity = (RadioButton) view.findViewById(R.id.rbtnValidity);
        this._rbtnSpecial = (RadioButton) view.findViewById(R.id.rbtnSpecial);
        _rechargeBtn            = (Button) view.findViewById(R.id.btnRecharge);

        _rechargeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateAndRecharge(view);
            }
        });

        _backBtn                = (Button) view.findViewById(R.id.btnBack);
        setupBackListener();

        getAllOperators();

        addListenerOnSpinnerItemSelection();
        return view;
    }


    @Override
    public void onTaskSuccess(TransactionRequest<PaymentResponse> result, DataExImpl.Methods callback) {
        showProgress(false);
        Log.d(_LOG, "Called back: " + result);

        if(result == null){
            Log.e(_LOG, "Something wrong. Received null TransactionRequest object");
            showMessage(getResources().getString(R.string.error_recharge_failed));
            return;
        }

        switch(callback){
            case RECHARGE_MOBILE:
                taskCompleted(result);
                Log.i("MobileRecharge" , result.getRemoteResponse());
                break;
        }
    }

    @Override
    public void onTaskError(AsyncResult pResult, DataExImpl.Methods callback) {
        Log.e(_LOG, "Error in recharging");
        showMessage(getResources().getString(R.string.error_recharge_failed));
    }

    @Override
    public void receiveMessage(Bundle bundle) throws MOMException{
        Log.d(_LOG, "Received message in sub-fragment");

    }

    @Override
    protected void showBalance(float pfBalance) {
    }

    public void addListenerOnSpinnerItemSelection() {
        _spOperator
                .setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }

    public void getAllOperators() {
        List<Operator> operatorList = null;

        if (_currentPlatform == PlatformIdentifier.MOM){
            operatorList    = DataProvider.getMoMPlatformMobileOperators();
        } else if (_currentPlatform == PlatformIdentifier.PBX) {
            operatorList    = DataProvider.getMoMPlatformMobileOperators();
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "Error", Toast.LENGTH_LONG)
                    .show();
        }

        if(operatorList == null){
            Log.e(_LOG, "No operators!");
            return;
        }

        operatorList.add(0, new Operator("", getActivity().getString(R.string.prompt_spinner_select_operator)));

        ArrayAdapter<Operator> dataAdapter = new ArrayAdapter<Operator>(
                getActivity(), android.R.layout.simple_spinner_item,
                operatorList
        );

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _spOperator.setAdapter(dataAdapter);
    }


    public void validateAndRecharge(View view) {
        if (_spOperator.getSelectedItemPosition() < 1){
            showMessage(getResources().getString(R.string.prompt_select_operator));
            return;
        }

        Operator operator               = (Operator) _spOperator.getSelectedItem();

        int nMinAmount          = 10;
        int nMaxAmount          = 10000;
        int nMinPhoneLength     = 10;
        int nMaxPhoneLength     = 10;
        int nExactPhoneLength   = 10;

        int nPhoneLength        = _etTargetPhone.getText().toString().length();
        int nAmount             = 0;

        try {
            nAmount             = Integer.parseInt(_etAmount.getText().toString());
        }catch (NumberFormatException nfe){
            nfe.printStackTrace();
            showMessage(getResources().getString(R.string.prompt_numbers_only_amount));
            return;
        }

        if(
                operator.code.equals(AppConstants.OPERATOR_ID_TATA_WALKY) ||
                        operator.code.equals("TWT")
                ){

            nMinPhoneLength     = 1;
            nMaxPhoneLength     = 12;
        }

        if(nPhoneLength < nMinPhoneLength || nPhoneLength > nMaxPhoneLength){
            if(nMinPhoneLength == nMaxPhoneLength) {
                showMessage(String.format(getResources().getString(R.string.error_phone_length), nMinPhoneLength));
            }else{
                showMessage(String.format(getResources().getString(R.string.error_phone_length_min_max), nMinPhoneLength, nMaxPhoneLength));
            }
            return;
        }

        if(nAmount < nMinAmount || nAmount > nMaxAmount){
            showMessage(String.format(getResources().getString(R.string.error_amount_min_max), nMinAmount, nMaxAmount));
            return;
        }

        confirmRecharge();
    }


    public void confirmRecharge() {
        showDialog(
                "Confirm Mobile Recharge",
                "Mobile Number:" + " "
                        + _etTargetPhone.getText().toString() + "\n" + "Operator:"
                        + " " + _spOperator.getSelectedItem().toString() + "\n"
                        + "Amount:" + " " + "Rs." + " "
                        + _etAmount.getText().toString(),
                "YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ((AlertDialog) dialog).getButton(
                                AlertDialog.BUTTON1).setEnabled(false);
                        startRecharge();
//							new GetLoginTask().onPostExecute("test");

                    }
                },
                "NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which1) {
                        dialog.cancel();
                    }
                }
        );
    }

    private void startRecharge() {
        showMessage(null);
        String sConsumerNumber          = _etTargetPhone.getText().toString();
        String sRechargeAmount          = _etAmount.getText().toString();
        Operator operator               = (Operator) _spOperator.getSelectedItem();

        int nRechargeType               = 0;

        if (_rbtnValidity.isChecked()) {
            nRechargeType               = 1;
        } else if (_rbtnSpecial.isChecked()) {
            nRechargeType               = 2;
        }

        TransactionRequest<PaymentResponse> request = new TransactionRequest<PaymentResponse>(
                getActivity().getResources().getString(TransactionType.MOBILE.transactionTypeStringId),
                sConsumerNumber,
                sConsumerNumber,
                Integer.parseInt(sRechargeAmount),
                operator
        );

        updateAsyncQueue(request);
        getDataEx(this).rechargeMobile(request, nRechargeType);


        showProgress(true);


        _etTargetPhone.setText(null);
        _etAmount.setText(null);
        _spOperator.setSelection(0);
    }


    public class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

            _etTargetPhone.setText("");
            _etAmount.setText("");

            _rbtnTopUp.setChecked(false);
            _rbtnValidity.setChecked(false);

            Operator operator   = (Operator) _spOperator.getSelectedItem();
            String sOperatorId  = operator.code;

            if (
                    sOperatorId.equals(AppConstants.OPERATOR_ID_BSNL) ||
                            sOperatorId.equals(AppConstants.OPERATOR_ID_MTNL)
                    ) {

                _rbtnTopUp.setVisibility(view.VISIBLE);
                _rbtnValidity.setVisibility(view.VISIBLE);
                _rbtnSpecial.setVisibility(view.GONE);
                _rbtnTopUp.setChecked(true);
                _rbtnValidity.setChecked(false);
            } else if (sOperatorId.equals(AppConstants.OPERATOR_ID_TATA_DOCOMO) ||
                    sOperatorId.equals(AppConstants.OPERATOR_ID_UNINOR)
                    ) {

                _etTargetPhone.setText("");
                _etAmount.setText("");
                _rbtnTopUp.setVisibility(view.VISIBLE);
                _rbtnValidity.setVisibility(view.GONE);
                _rbtnSpecial.setVisibility(view.VISIBLE);
                _rbtnTopUp.setChecked(true);
                _rbtnValidity.setChecked(false);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {

        }
    }
}