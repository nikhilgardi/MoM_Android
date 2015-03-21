package com.mom.app.fragment;


import android.app.AlertDialog;
import android.app.FragmentManager;
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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mom.app.R;
import com.mom.app.error.MOMException;
import com.mom.app.identifier.PBXOperatorDataType;
import com.mom.app.identifier.PlatformIdentifier;
import com.mom.app.identifier.TransactionType;
import com.mom.app.model.AsyncListener;
import com.mom.app.model.AsyncResult;
import com.mom.app.model.DataExImpl;
import com.mom.app.model.IDataEx;
import com.mom.app.model.Operator;
import com.mom.app.model.pbxpl.PBXPLDataExImpl;
import com.mom.app.model.pbxpl.PaymentResponse;
import com.mom.app.ui.TransactionRequest;
import com.mom.app.utils.AppConstants;
import com.mom.app.utils.DataProvider;

import java.util.List;

/**
 * A simple {@link android.app.Fragment} subclass.
 *
 */
public class MobileRechargeFragment extends FragmentBase implements AsyncListener<TransactionRequest<String>>{

    String _LOG     = AppConstants.LOG_PREFIX + "MOB_RECHARGE";

    private EditText _etTargetPhone;
    private EditText _etAmount;

    Spinner _spOperator;
    RadioButton _rbtnTopUp, _rbtnValidity, _rbtnSpecial;
    Button _rechargeBtn;
    EditText _verifyTPin;
    Button _btnSubmit;


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
        showProgress(false);
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
        this._verifyTPin  = (EditText) view.findViewById(R.id.verifyTpin);
       _btnSubmit         = (Button) view.findViewById(R.id.btnSubmit);
        _rechargeBtn      = (Button) view.findViewById(R.id.btnRecharge);

        if(_currentPlatform == PlatformIdentifier.MOM)
        {
            _verifyTPin.setVisibility(View.VISIBLE);
            _btnSubmit.setVisibility(View.VISIBLE);
        }
        else if(_currentPlatform == PlatformIdentifier.PBX)
        {
            _spOperator.setVisibility(View.VISIBLE);
            _etTargetPhone.setVisibility(View.VISIBLE);
            _etAmount.setVisibility(View.VISIBLE);
            _rechargeBtn.setVisibility(View.VISIBLE);
        }

       _btnSubmit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               getVerifyTpin();
           }
       });
        _rechargeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateAndRecharge(view);
            }
        });


        try {
            getAllOperators();
        }catch(MOMException me){
            showMessage("Error getting operator list!");
        }

        addListenerOnSpinnerItemSelection();
        return view;
    }

    private void getVerifyTpin() {

        IDataEx dataEx = getDataEx(new AsyncListener<Integer>() {
            @Override
            public void onTaskSuccess(Integer result, DataExImpl.Methods callback) {
                Log.e(_LOG, "VerifyTpin: " + result);

                switch(result)
                {
                    case 101:
                        _spOperator.setVisibility(View.VISIBLE);
                        _etTargetPhone.setVisibility(View.VISIBLE);
                        _etAmount.setVisibility(View.VISIBLE);
                        _rechargeBtn.setVisibility(View.VISIBLE);
                        _verifyTPin.setVisibility(View.GONE);
                        _btnSubmit.setVisibility(View.GONE);
                        break;

                    default:
                       showMessage(getResources().getString(R.string.error_invalid_t_pin));
                        _verifyTPin.setText(null);



                }
                showProgress(false);

            }

            @Override
            public void onTaskError(AsyncResult pResult, DataExImpl.Methods callback) {
                Log.e(_LOG, "Error obtaining bill amount");

            }
        });
        showMessage(null);
        String sTpin          = _verifyTPin.getText().toString();


        TransactionRequest request = new TransactionRequest();
        request.setTpin(sTpin);

        dataEx.verifyTPin(sTpin);
        Log.d(_LOG, "Get Bill Amount finished");


        showProgress(true);


        _etTargetPhone.setText(null);
        _etAmount.setText(null);
        _spOperator.setSelection(0);
    }


    @Override
    public void onTaskSuccess(TransactionRequest<String> result, DataExImpl.Methods callback) {
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

                showBalance();
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

    public void addListenerOnSpinnerItemSelection() {
        _spOperator
                .setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }

    public void getAllOperators() throws MOMException{
        List<Operator> operatorList = null;

        if (_currentPlatform == PlatformIdentifier.MOM){
            operatorList    = DataProvider.getMoMPlatformMobileOperators();
        } else if (_currentPlatform == PlatformIdentifier.PBX) {
            operatorList    = DataProvider.getPBXPlatformMobileOperators();
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "Error", Toast.LENGTH_LONG)
                    .show();
    }

        showOperators(operatorList);
    }

    private void showOperators(List<Operator> operatorList){
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

            _rbtnTopUp.setVisibility(View.GONE);
            _rbtnValidity.setVisibility(View.GONE);
            _rbtnSpecial.setVisibility(View.GONE);
            _rbtnTopUp.setChecked(false);
            _rbtnValidity.setChecked(false);

            Operator operator   = (Operator) _spOperator.getSelectedItem();
            String sOperatorId  = operator.code;

            if (
                    sOperatorId.equals(AppConstants.OPERATOR_ID_BSNL) ||
                            sOperatorId.equals(AppConstants.OPERATOR_ID_MTNL)
                    ) {

                _rbtnTopUp.setVisibility(View.VISIBLE);
                _rbtnValidity.setVisibility(View.VISIBLE);
                _rbtnSpecial.setVisibility(View.GONE);
                _rbtnTopUp.setChecked(true);
                _rbtnValidity.setChecked(false);
            } else if (sOperatorId.equals(AppConstants.OPERATOR_ID_TATA_DOCOMO) ||
                    sOperatorId.equals(AppConstants.OPERATOR_ID_UNINOR)
                    ) {

                _etTargetPhone.setText("");
                _etAmount.setText("");
                _rbtnTopUp.setVisibility(View.VISIBLE);
                _rbtnValidity.setVisibility(View.GONE);
                _rbtnSpecial.setVisibility(View.VISIBLE);
                _rbtnTopUp.setChecked(true);
                _rbtnValidity.setChecked(false);
            }

            else if (sOperatorId.equals(AppConstants.OPERATOR_ID_DATACOMM)) {

                _etTargetPhone.setText("");
                _etAmount.setText("");
                _rbtnTopUp.setVisibility(View.VISIBLE);
                _rbtnValidity.setVisibility(View.GONE);
                _rbtnSpecial.setVisibility(View.VISIBLE);
                _rbtnTopUp.setChecked(true);
                _rbtnSpecial.setChecked(false);

            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {

        }
    }

    private void showEditDialog() {
        FragmentManager fm = getActivity().getFragmentManager();
        MyDialogFragment editNameDialog = new MyDialogFragment();
        editNameDialog.show(fm, "fragment_edit_name");
    }


        public void onFinishEditDialog(String inputText) {
        showMessage(inputText);
    }

    private void showAlert() {

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        LinearLayout layout = new LinearLayout(getActivity());
        TextView tvMessage = new TextView(getActivity());
        final EditText input = new EditText(getActivity().getApplicationContext());
        alert.setView(input);




        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(input);


        alert.setTitle("T-Pin");
        alert.setView(layout);

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String value = input.getText().toString();

                  TransactionRequest request  = new TransactionRequest(
                        getActivity().getString(TransactionType.MOM_T_PIN.transactionTypeStringId),
                        null );
              _dataEx.verifyTPin(value);
                Toast.makeText(getActivity().getApplicationContext(), value, Toast.LENGTH_LONG)
                        .show();

                showProgress(false);


            }
        });

        alert.show();
    }

//    protected void showInputDialog() {
//
//        // get prompts.xml view
//        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
//        View promptView = layoutInflater.inflate(R.layout.input_dialog, null);
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
//        alertDialogBuilder.setView(promptView);
//
//        final EditText editText = (EditText) promptView.findViewById(R);
//        // setup a dialog window
//        alertDialogBuilder.setCancelable(false)
//                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        resultText.setText("Hello, " + editText.getText());
//                    }
//                })
//                .setNegativeButton("Cancel",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                            }
//                        });
//
//        // create an alert dialog
//        AlertDialog alert = alertDialogBuilder.create();
//        alert.show();
//    }
}