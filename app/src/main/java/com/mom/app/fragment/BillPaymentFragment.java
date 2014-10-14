package com.mom.app.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mom.app.R;
import com.mom.app.identifier.ActivityIdentifier;
import com.mom.app.identifier.PlatformIdentifier;
import com.mom.app.model.AsyncListener;
import com.mom.app.model.AsyncResult;
import com.mom.app.model.DataExImpl;
import com.mom.app.model.IDataEx;
import com.mom.app.model.Operator;
import com.mom.app.model.local.EphemeralStorage;
import com.mom.app.model.mompl.MoMPLDataExImpl;
import com.mom.app.utils.AppConstants;
import com.mom.app.utils.DataProvider;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by vaibhavsinha on 10/8/14.
 */
public class BillPaymentFragment extends FragmentBase implements AsyncListener<String> {

    private String _LOG = AppConstants.LOG_PREFIX + "BILL PAY";

    Button _getBillAmount;
    private EditText _etSubscriberId;
    private EditText amountField;
    private EditText _etCustomerNumber;
    private TextView _billMsgDisplay;

    Spinner operatorSpinner;

    public static BillPaymentFragment newInstance(PlatformIdentifier currentPlatform){
        BillPaymentFragment fragment        = new BillPaymentFragment();
        Bundle bundle                       = new Bundle();
        bundle.putSerializable(AppConstants.ACTIVE_PLATFORM, currentPlatform);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _currentPlatform        = (PlatformIdentifier) getArguments().getSerializable(AppConstants.ACTIVE_PLATFORM);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_bill_payment, null, false);

        _getBillAmount          = (Button) view.findViewById(R.id.btnGetBillAmount);
        _billMsgDisplay         = (TextView) view.findViewById(R.id.msgDisplay);
        this.operatorSpinner    = (Spinner) view.findViewById(R.id.Operator);
        this.operatorSpinner.setOnItemSelectedListener(new OperatorSelectedListener());

        this._etSubscriberId    = (EditText) view.findViewById(R.id.subscriberId);
        this.amountField        = (EditText) view.findViewById(R.id.amount);
        this._etCustomerNumber  = (EditText) view.findViewById(R.id.number);

        _getBillAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getBillAmount(view);
            }
        });

        getAllOperators();

        getProgressBar(view).setVisibility(View.GONE);

        return view;
    }

    @Override
    public void onTaskSuccess(String result, DataExImpl.Methods callback) {
        Log.d(_LOG, "Called back");
        switch(callback){
            case PAY_BILL:
                if(result == null){
                    Log.d(_LOG, "Obtained NULL bill payment response");
                    showMessage(getResources().getString(R.string.error_recharge_failed));
                    return;
                }
                Log.d(_LOG, "Going to get new balance");
                getBalanceAsync();
                Log.d(_LOG, "Starting navigation to TxnMsg Activity");
//                navigateToTransactionMessageActivity(ActivityIdentifier.BILL_PAYMENT, result);
                break;
        }

        _pb.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onTaskError(AsyncResult pResult, DataExImpl.Methods callback) {

    }

    @Override
    protected void showBalance(float pfBalance) {

    }


    public void getAllOperators() {
        List<Operator> operatorList = null;

        if (_currentPlatform == PlatformIdentifier.NEW){
            operatorList    = DataProvider.getMoMPlatformBillPayOperators();
        } else if (_currentPlatform == PlatformIdentifier.PBX) {

        } else {
            Toast.makeText(getActivity().getApplicationContext(), "Error", Toast.LENGTH_LONG)
                    .show();
        }

        if(operatorList == null){
            Log.e(_LOG, "No operators!");
            return;
        }

        ArrayAdapter<Operator> dataAdapter = new ArrayAdapter<Operator>(
                getActivity(), android.R.layout.simple_spinner_item,
                operatorList
        );

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        operatorSpinner.setAdapter(dataAdapter);
    }



    private String getOperatorId(String strOperatorName) {

        if (_currentPlatform == PlatformIdentifier.NEW)
        {
            String id      = AppConstants.OPERATOR_NEW.get(strOperatorName);
            if(id == null){
                return "-1";
            }

            return id;

        } else if (_currentPlatform == PlatformIdentifier.PBX) {

        }
        return "-1";
    }

    public void showBillMessage(String psMsg){
        _billMsgDisplay.setVisibility(View.VISIBLE);
        _billMsgDisplay.setText(psMsg);
    }

    public void getBillAmount(View view){
        Log.d(_LOG, "Get Bill Amount called");
        String sSubscriberId            = _etSubscriberId.getText().toString();
        String sOperatorID              = getOperatorId(operatorSpinner.getSelectedItem().toString());

        IDataEx dataEx                  = getDataEx(new AsyncListener<Float>() {
            @Override
            public void onTaskSuccess(Float result, DataExImpl.Methods callback) {
                Log.e(_LOG, "Obtainined bill amount: " + result);
                int bill                = Math.round(result);
                amountField.setText(String.valueOf(bill));
                amountField.setBackgroundColor(getResources().getColor(R.color.green));
            }

            @Override
            public void onTaskError(AsyncResult pResult, DataExImpl.Methods callback) {
                Log.e(_LOG, "Error obtaining bill amount");
                amountField.setBackgroundColor(getResources().getColor(R.color.red));
                showBillMessage(getResources().getString(R.string.error_obtaining_bill_amount));
            }
        });

        dataEx.getBillAmount(sOperatorID, sSubscriberId);
        Log.d(_LOG, "Get Bill Amount finished");
    }


    public void validateAndPay(View view) {
        if (operatorSpinner.getSelectedItemPosition() < 1){
            showMessage(getResources().getString(R.string.prompt_select_operator));
            return;
        }

        String sOperator        = operatorSpinner.getSelectedItem().toString();
        String sOperatorId      = getOperatorId(sOperator);

        int nMinAmount          = 10;
        int nMaxAmount          = 10000;
        int nMinLength          = 10;
        int nMaxLength          = 10;

        int nCustomerNumberLength
                = _etCustomerNumber.getText().toString().trim().length();
        int nAmount             = 0;

        try {
            nAmount             = Integer.parseInt(amountField.getText().toString().trim());
        }catch (NumberFormatException nfe){
            nfe.printStackTrace();
            showMessage(getResources().getString(R.string.prompt_numbers_only_amount));
            return;
        }

        if(
                sOperatorId.equals(AppConstants.OPERATOR_ID_AIRTEL_BILL) ||
                        sOperatorId.equals("BAI")
                ){

            nMinAmount      = 50;
            nMinLength      = 10;
            nMaxLength      = 11;
        }else if(
                sOperatorId.equals(AppConstants.OPERATOR_ID_AIRCEL_BILL) ||
                        sOperatorId.equals(AppConstants.OPERATOR_ID_BSNL_BILL_PAY) ||
                        sOperatorId.equals(AppConstants.OPERATOR_ID_IDEA_BILL) ||
                        sOperatorId.equals(AppConstants.OPERATOR_ID_RELIANCE_BILL_GSM) ||
                        sOperatorId.equals(AppConstants.OPERATOR_ID_RELIANCE_BILL_CDMA) ||
                        sOperatorId.equals(AppConstants.OPERATOR_ID_TATA_BILL) ||
                        sOperatorId.equals(AppConstants.OPERATOR_ID_VODAFONE_BILL) ||
                        sOperatorId.equals("BAC") ||
                        sOperatorId.equals("BLL") ||
                        sOperatorId.equals("BID") ||
                        sOperatorId.equals("BRG") ||
                        sOperatorId.equals("BRC") ||
                        sOperatorId.equals("BTA") ||
                        sOperatorId.equals("BVO") ||
                        sOperatorId.equals("BRC")
                ){

            nMinAmount      = 50;
        }

        if(nCustomerNumberLength < nMinLength || nCustomerNumberLength > nMaxLength){
            if(nMinLength == nMaxLength){
                showMessage(String.format(getResources().getString(R.string.error_phone_length), nMinLength));
            }else{
                showMessage(String.format(getResources().getString(R.string.error_phone_length_min_max), nMinLength, nMaxLength));
            }
            return;
        }

        if(nAmount < nMinAmount || nAmount > nMaxAmount){
            showMessage(String.format(getResources().getString(R.string.error_amount_min_max), nMinAmount, nMaxAmount));
            return;
        }

        if(
                AppConstants.OPERATOR_ID_RELIANCE_ENERGY.equals(sOperatorId) ||
                        AppConstants.OPERATOR_ID_BEST_ELECTRICITY.equals(sOperatorId) ||
                        AppConstants.OPERATOR_ID_MAHANAGAR_GAS.equals(sOperatorId)){

            String sSubscriberId          = _etSubscriberId.getText().toString().trim();

            if("".equals(sSubscriberId)) {
                showMessage(getResources().getString(R.string.error_subscriber_id_length));
                return;
            }
        }


        confirmPayment();
    }

    private void startPayment() {
        _pb.setVisibility(View.VISIBLE);


        String sSubscriberId            = _etSubscriberId.getText().toString();
        String sRechargeAmount          = amountField.getText().toString();
        String sOperatorID              = getOperatorId(operatorSpinner.getSelectedItem().toString());
        String sCustomerNumber          = _etCustomerNumber.getText().toString().trim();
        String sCustomerName            = "";

        int nRechargeType               = 0;

//        SharedPreferences pref = PreferenceManager
//                .getDefaultSharedPreferences(getApplicationContext());

        if (_currentPlatform == PlatformIdentifier.NEW){
            HashMap<String, String> map     = new HashMap<String, String>();

            map.put(AppConstants.PARAM_NEW_RELIANCE_SBE_NBE, "");
            map.put(AppConstants.PARAM_NEW_SPECIAL_OPERATOR_NBE, "");

            getDataEx(this).payBill(sSubscriberId, Double.parseDouble(sRechargeAmount), sOperatorID, sCustomerNumber, sCustomerName, map);

        } else if (_currentPlatform == PlatformIdentifier.PBX){


        }

    }



    public void confirmPayment() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        // Setting Dialog Title
        alertDialog.setTitle("Confirm MobileRecharge...");

        String sMsg     = "Operator: "
                + operatorSpinner.getSelectedItem() + "\n"
                + "Amount:" + " " + "Rs." + " "
                + amountField.getText() + "\n"
                + "Phone: " + _etCustomerNumber.getText();

        String sOperator                = operatorSpinner.getSelectedItem().toString();

        String sOperatorId              = getOperatorId(sOperator);

        if(
                AppConstants.OPERATOR_ID_RELIANCE_ENERGY.equals(sOperatorId) ||
                        AppConstants.OPERATOR_ID_BEST_ELECTRICITY.equals(sOperatorId) ||
                        AppConstants.OPERATOR_ID_MAHANAGAR_GAS.equals(sOperatorId)
                ){

            sMsg        = getResources().getString(R.string.lblConsumerNumber) + ": "
                    + _etSubscriberId.getText() + "\n" + sMsg;
        }

        // Setting Dialog Message
        alertDialog.setMessage(sMsg);

        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ((AlertDialog) dialog).getButton(
                                AlertDialog.BUTTON1).setEnabled(false);
                        startPayment();

                    }
                });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which1) {

                        dialog.cancel();

                    }
                });

        alertDialog.show();
    }

    public void showRetrieveBillFields(){
        _etSubscriberId.setVisibility(View.VISIBLE);
        _getBillAmount.setVisibility(View.VISIBLE);
    }

    public void hideRetrieveBillFields(){
        amountField.setBackgroundColor(getResources().getColor(R.color.white));
        _etSubscriberId.setText("");
        _etSubscriberId.setVisibility(View.GONE);
        _getBillAmount.setVisibility(View.GONE);
    }

    public class OperatorSelectedListener implements AdapterView.OnItemSelectedListener{
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            String sConsumerNumber          = getResources().getString(R.string.lblConsumerNumber);
            String sAccountNumber           = getResources().getString(R.string.lblAccountNumber);
            String sMobileNumber            = getResources().getString(R.string.lblCustomerMobile);

            String sHintDisplay             = sConsumerNumber;

            showBillMessage("");

            String sOperator                = operatorSpinner.getSelectedItem().toString();

            Log.d(_LOG, "Operator selected: " + sOperator);
            String sOperatorId              = getOperatorId(sOperator);

            if(sOperatorId == null){
                Log.d(_LOG, "Could not find operator id, doing nothing");
                return;
            }

            hideRetrieveBillFields();

            if(AppConstants.OPERATOR_ID_NBE.equals(sOperatorId) || AppConstants.OPERATOR_ID_SBE.equals(sOperatorId)){
                sHintDisplay                = sConsumerNumber;
            }else if(
                    AppConstants.OPERATOR_ID_RELIANCE_ENERGY.equals(sOperatorId) ||
                            AppConstants.OPERATOR_ID_BEST_ELECTRICITY.equals(sOperatorId) ||
                            AppConstants.OPERATOR_ID_MAHANAGAR_GAS.equals(sOperatorId)){

                sHintDisplay                = sAccountNumber;

                showRetrieveBillFields();
            }

            _etSubscriberId.setHint(sHintDisplay);
        }
    }
}
