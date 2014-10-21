package com.mom.app.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mom.app.R;
import com.mom.app.identifier.PlatformIdentifier;
import com.mom.app.identifier.TransactionType;
import com.mom.app.model.AsyncListener;
import com.mom.app.model.AsyncResult;
import com.mom.app.model.DataExImpl;
import com.mom.app.model.Operator;
import com.mom.app.ui.TransactionRequest;
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
import java.util.List;

/**
 * Created by vaibhavsinha on 10/4/14.
 */
public class DTHRechargeFragment extends FragmentBase implements AsyncListener<TransactionRequest> {
    String _LOG         = AppConstants.LOG_PREFIX + "DTH RECHARGE";

    private EditText _etSubscriberId;
    private EditText _etAmount;
    private EditText _etCustomerNumber;

    Spinner _spOperator;

//    String responseBody;
    Button _rechargeButton;

    public static DTHRechargeFragment newInstance(PlatformIdentifier currentPlatform){
        DTHRechargeFragment fragment    = new DTHRechargeFragment();
        Bundle bundle                   = new Bundle();
        bundle.putSerializable(AppConstants.ACTIVE_PLATFORM, currentPlatform);
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
        View view = inflater.inflate(R.layout.activity_dth_recharge, null, false);

        _spOperator = (Spinner) view.findViewById(R.id.Operator);
        _etSubscriberId    = (EditText) view.findViewById(R.id.subscriberId);
        _etAmount = (EditText) view.findViewById(R.id.amount);
        _etCustomerNumber  = (EditText) view.findViewById(R.id.number);
        _rechargeButton     = (Button) view.findViewById(R.id.btnRecharge);

        _rechargeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateAndRecharge(view);
            }
        });

        getAllOperators();
        return view;
    }



    @Override
    public void onTaskSuccess(TransactionRequest result, DataExImpl.Methods callback) {
        Log.d(_LOG, "Called back");
        switch(callback){
            case RECHARGE_DTH:
                if(result == null){
                    Log.d(_LOG, "Obtained NULL recharge response");
                    showMessage(getResources().getString(R.string.error_recharge_failed));
                    return;
                }
                Log.d(_LOG, "Going to get new balance");
                getBalanceAsync();
                Log.d(_LOG, "Starting navigation to TxnMsg Activity");

                break;
        }

        taskCompleted(result);
    }

    @Override
    public void onTaskError(AsyncResult pResult, DataExImpl.Methods callback) {

    }

    @Override
    protected void showBalance(float pfBalance) {
    }


    public void getAllOperators() {
        List<Operator> operatorList = null;

        if (_currentPlatform == PlatformIdentifier.MOM){
            operatorList    = DataProvider.getMoMPlatformDTHOperators();
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

        operatorList.add(0, new Operator("", getActivity().getString(R.string.prompt_spinner_select_operator)));

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _spOperator.setAdapter(dataAdapter);
    }


//    private String getOperatorId(String strOperatorName) {
//
//        if (_currentPlatform == PlatformIdentifier.MOM)
//        {
//            String id      = AppConstants.OPERATOR_NEW.get(strOperatorName);
//            if(id == null){
//                return "-1";
//            }
//
//            return id;
//
//        } else if (_currentPlatform == PlatformIdentifier.PBX) {
//            HttpClient httpclient = new DefaultHttpClient();
//            HttpPost httppost = new HttpPost(
//                    "http://180.179.67.76/MobAppS/PbxMobApp.ashx");
//            try {
//
//                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
//                        3);
//                nameValuePairs.add(new BasicNameValuePair("SN", strOperatorName));
//                nameValuePairs.add(new BasicNameValuePair("Service", "OSN"));
//                nameValuePairs.add(new BasicNameValuePair("OT", "1"));
//
//                httppost.addHeader("ua", "android");
//
//                final HttpParams httpParams = httpclient.getParams();
//                HttpConnectionParams.setConnectionTimeout(httpParams, 15000);
//                HttpConnectionParams.setSoTimeout(httpParams, 15000);
//                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//                HttpResponse response = httpclient.execute(httppost);
//                HttpEntity entity = response.getEntity();
//                responseBody = EntityUtils.toString(entity);
//
//                return responseBody;
//            } catch (Exception ex) {
//                return "-1";
//            }
//        } else {
//            return "-1";
//        }
//    }


    public void validateAndRecharge(View view) {
        if (_spOperator.getSelectedItemPosition() < 1){
            showMessage(getResources().getString(R.string.prompt_select_operator));
            return;
        }

        Operator operator       = (Operator) _spOperator.getSelectedItem();

        int nMinAmount          = 100;
        int nMaxAmount          = 15000;
        int nMinLength          = 10;
        int nMaxLength          = 10;

        int nSubscriberLength   = _etSubscriberId.getText().toString().length();
        int nAmount             = 0;

        try {
            nAmount             = Integer.parseInt(_etAmount.getText().toString());
        }catch (NumberFormatException nfe){
            nfe.printStackTrace();
            showMessage(getResources().getString(R.string.prompt_numbers_only_amount));
            return;
        }

        if(
                operator.code.equals(AppConstants.OPERATOR_ID_DISH) ||
                        operator.code.equals(AppConstants.OPERATOR_ID_SUN_DIRECT) ||
                        operator.code.equals("DSH") ||
                        operator.code.equals("SUN")
                ){

            nMinLength      = 11;
            nMaxLength      = 11;
            nMinAmount      = 10;
        }else if(
                operator.code.equals(AppConstants.OPERATOR_ID_BIG_TV) ||
                        operator.code.equals("BIG")
                ){

            nMinLength      = 12;
            nMaxLength      = 12;
            nMinAmount      = 25;
        }else if(
                operator.code.equals(AppConstants.OPERATOR_ID_VIDEOCON_DTH) ||
                operator.code.equals("D2H")
                ){

            nMinLength      = 1;
            nMaxLength      = 10;
            nMinAmount      = 150;
        }

        if(nSubscriberLength < nMinLength || nSubscriberLength > nMaxLength){
            if(nMinLength == nMaxLength){
                showMessage(String.format(getResources().getString(R.string.error_subscriber_id_length), nMinLength));
            }else{
                showMessage(String.format(getResources().getString(R.string.error_subscriber_id_min_max), nMinLength, nMaxLength));
            }
            return;
        }

        if(nAmount < nMinAmount || nAmount > nMaxAmount){
            showMessage(String.format(getResources().getString(R.string.error_amount_min_max), nMinAmount, nMaxAmount));
            return;
        }

        String sCustomerNumber          = _etCustomerNumber.getText().toString().trim();

        if("".equals(sCustomerNumber)) {
            showMessage(getResources().getString(R.string.error_customer_phone_required));
            return;
        }else if(sCustomerNumber.length() != AppConstants.STANDARD_MOBILE_NUMBER_LENGTH){
            showMessage(String.format(getResources().getString(R.string.error_phone_length), AppConstants.STANDARD_MOBILE_NUMBER_LENGTH));
            return;
        }

        confirmRecharge();
    }

    private void startRecharge() {
        showMessage(null);
        String sSubscriberId            = _etSubscriberId.getText().toString();
        String sRechargeAmount          = _etAmount.getText().toString();
        Operator operator               = (Operator) _spOperator.getSelectedItem();
        String sCustomerNumber          = _etCustomerNumber.getText().toString().trim();

        TransactionRequest request      = new TransactionRequest(
                getActivity().getString(TransactionType.DTH.transactionTypeStringId),
                sSubscriberId,
                sCustomerNumber,
                Float.parseFloat(sRechargeAmount),
                operator
        );

        if (_currentPlatform == PlatformIdentifier.MOM){

            getDataEx(this).rechargeDTH(request);

        } else if (_currentPlatform == PlatformIdentifier.PBX){

        }

        _etSubscriberId.setText(null);
        _etAmount.setText(null);
        _etCustomerNumber.setText(null);
        _spOperator.setSelection(0);

        showProgress(true);
        updateAsyncQueue(request);
    }



    public void goBack(View view) {
        getFragmentManager().popBackStack();
    }


    public void confirmRecharge() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        // Setting Dialog Title
        alertDialog.setTitle("Confirm DTH Recharge...");

        // Setting Dialog Message
        alertDialog.setMessage("Subscriber ID:" + " "
                + _etSubscriberId.getText().toString() + "\n" + "Operator:"
                + " " + _spOperator.getSelectedItem().toString() + "\n"
                + "Amount:" + " " + "Rs." + " "
                + _etAmount.getText().toString());

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
}
