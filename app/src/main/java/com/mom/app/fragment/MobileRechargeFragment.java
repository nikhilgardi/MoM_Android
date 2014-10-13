package com.mom.app.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mom.app.R;
import com.mom.app.error.MOMException;
import com.mom.app.identifier.ActivityIdentifier;
import com.mom.app.identifier.IdentifierUtils;
import com.mom.app.identifier.PlatformIdentifier;
import com.mom.app.model.AsyncListener;
import com.mom.app.model.AsyncResult;
import com.mom.app.model.DataExImpl;
import com.mom.app.model.local.EphemeralStorage;
import com.mom.app.ui.IFragmentListener;
import com.mom.app.utils.AppConstants;

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
import java.util.List;

/**
 * A simple {@link android.app.Fragment} subclass.
 *
 */
public class MobileRechargeFragment extends FragmentBase implements AsyncListener<String>{
    static final String NAV_BUTTONS_TAG     = "momNavButtonsTag";

    String _LOG     = AppConstants.LOG_PREFIX + "CREATE FRAG";

    private EditText rechargeTargetPhone;
    private EditText amountField;

    Spinner operatorSpinner;
    RadioButton rbtnTopUp, rbtnValidity, rbtnSpecial;

    String responseBody;

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

        _currentPlatform        = IdentifierUtils.getPlatformIdentifier(getActivity().getApplicationContext());

        this.operatorSpinner    = (Spinner) view.findViewById(R.id.Operator);
        this.rechargeTargetPhone
                = (EditText) view.findViewById(R.id.rechargeTargetPhone);

        this.amountField        = (EditText) view.findViewById(R.id.amount);
        this.rbtnTopUp          = (RadioButton) view.findViewById(R.id.rbtnTopUp);
        this.rbtnValidity       = (RadioButton) view.findViewById(R.id.rbtnValidity);
        this.rbtnSpecial        = (RadioButton) view.findViewById(R.id.rbtnSpecial);

        getAllOperators();

        addListenerOnSpinnerItemSelection();

        return view;
    }


    @Override
    public void onTaskSuccess(String result, DataExImpl.Methods callback) {
        Log.d(_LOG, "Called back");
        switch(callback){
            case RECHARGE_MOBILE:
                if(result == null){
                    Log.d(_LOG, "Obtained NULL recharge response");
                    showMessage(getResources().getString(R.string.error_recharge_failed));
                    return;
                }
                Log.d(_LOG, "Going to get new balance");
                getBalanceAsync();
                Log.d(_LOG, "Starting navigation to TxnMsg Activity");
//                navigateToTransactionMessageActivity(ActivityIdentifier.MOBILE_RECHARGE, result);
                break;
        }

        getProgressBar().setVisibility(View.GONE);
    }

    @Override
    public void onTaskError(AsyncResult pResult, DataExImpl.Methods callback) {

    }

    @Override
    public void receiveMessage(Bundle bundle) throws MOMException{
        Log.d(_LOG, "Received message in sub-fragment");

    }

    @Override
    protected void showBalance(float pfBalance) {
    }

    public Fragment getAttachedFragment(int containerResId){
        FragmentManager manager     = getFragmentManager();
        Fragment attachedFragment   = manager.findFragmentById(containerResId);

        return attachedFragment;
    }

    public void addListenerOnSpinnerItemSelection() {
        operatorSpinner
                .setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }

    public void getAllOperators() {

        if (_currentPlatform == PlatformIdentifier.NEW)
        {
            try {
                String[] strOperators = new String[] {
                        "Select Service Provider", "AIRCEL", "AIRTEL",
                        "BSNL", "DATACOMM", "IDEA", "LOOP", "MOM CARD REFILL",
                        "MTNL", "MTS", "QUE MOBILE", "RELIANCE CDMA",
                        "RELIANCE GSM", "STEL", "TATA", "TATA DOCOMO",
                        "TATA WALKY", "UNINOR", "VIRGIN", "VODAFONE" };

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
                        getActivity(), android.R.layout.simple_spinner_item,
                        strOperators);
                dataAdapter
                        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                operatorSpinner.setAdapter(dataAdapter);

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } else if (_currentPlatform == PlatformIdentifier.PBX) {

        } else {
            Toast.makeText(getActivity().getApplicationContext(), "Error", Toast.LENGTH_LONG)
                    .show();
        }
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

        return "";
    }

    public void validateAndRecharge(View view) {
        if (operatorSpinner.getSelectedItemPosition() < 1){
            showMessage(getResources().getString(R.string.prompt_select_operator));
            return;
        }

        String sOperator        = operatorSpinner.getSelectedItem().toString();
        String sOperatorId      = getOperatorId(sOperator);

        int nMinAmount          = 10;
        int nMaxAmount          = 10000;
        int nMinPhoneLength     = 10;
        int nMaxPhoneLength     = 10;
        int nExactPhoneLength   = 10;

        int nPhoneLength        = rechargeTargetPhone.getText().toString().length();
        int nAmount             = 0;

        try {
            nAmount             = Integer.parseInt(amountField.getText().toString());
        }catch (NumberFormatException nfe){
            nfe.printStackTrace();
            showMessage(getResources().getString(R.string.prompt_numbers_only_amount));
            return;
        }

        if(
                sOperatorId.equals(AppConstants.OPERATOR_ID_TATA_WALKY) ||
                        sOperatorId.equals("TWT")
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
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        // Setting Dialog Title
        alertDialog.setTitle("Confirm MobileRecharge...");

        // Setting Dialog Message
        alertDialog.setMessage("Mobile Number:" + " "
                + rechargeTargetPhone.getText().toString() + "\n" + "Operator:"
                + " " + operatorSpinner.getSelectedItem().toString() + "\n"
                + "Amount:" + " " + "Rs." + " "
                + amountField.getText().toString());

        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ((AlertDialog) dialog).getButton(
                                AlertDialog.BUTTON1).setEnabled(false);
                        startRecharge();
//							new GetLoginTask().onPostExecute("test");

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

    private void startRecharge() {
        String[] strResponse1 = null;

        String sConsumerNumber          = rechargeTargetPhone.getText().toString();
        String sRechargeAmount          = amountField.getText().toString();
        String sOperatorID              = getOperatorId(operatorSpinner.getSelectedItem().toString());
        int nRechargeType               = 0;

        if (rbtnValidity.isChecked()) {
            nRechargeType               = 1;
        } else if (rbtnSpecial.isChecked()) {
            nRechargeType               = 2;
        }

        if (_currentPlatform == PlatformIdentifier.NEW){
            getDataEx(this).rechargeMobile(sConsumerNumber, Double.parseDouble(sRechargeAmount), sOperatorID, nRechargeType);
        } else if (_currentPlatform == PlatformIdentifier.PBX){


        } else {
            Toast.makeText(getActivity().getApplicationContext(), "Error", Toast.LENGTH_LONG)
                    .show();
        }

    }

    public class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

            rechargeTargetPhone.setText("");
            amountField.setText("");

            rbtnTopUp.setChecked(false);
            rbtnValidity.setChecked(false);

            String sOperator = operatorSpinner.getSelectedItem().toString();
            String sOperatorId = getOperatorId(sOperator);

            if (
                    sOperatorId.equals(AppConstants.OPERATOR_ID_BSNL) ||
                            sOperatorId.equals(AppConstants.OPERATOR_ID_MTNL)
                    ) {

                rbtnTopUp.setVisibility(view.VISIBLE);
                rbtnValidity.setVisibility(view.VISIBLE);
                rbtnSpecial.setVisibility(view.GONE);
                rbtnTopUp.setChecked(true);
                rbtnValidity.setChecked(false);
            } else if (sOperatorId.equals(AppConstants.OPERATOR_ID_TATA_DOCOMO) ||
                    sOperatorId.equals(AppConstants.OPERATOR_ID_UNINOR)
                    ) {

                rechargeTargetPhone.setText("");
                amountField.setText("");
                rbtnTopUp.setVisibility(view.VISIBLE);
                rbtnValidity.setVisibility(view.GONE);
                rbtnSpecial.setVisibility(view.VISIBLE);
                rbtnTopUp.setChecked(true);
                rbtnValidity.setChecked(false);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {

        }
    }
}