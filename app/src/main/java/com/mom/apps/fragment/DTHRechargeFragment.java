package com.mom.apps.fragment;

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

import com.mom.apps.R;
import com.mom.apps.identifier.PlatformIdentifier;
import com.mom.apps.identifier.TransactionType;
import com.mom.apps.model.AsyncListener;
import com.mom.apps.model.AsyncResult;
import com.mom.apps.model.DataExImpl;
import com.mom.apps.model.IDataEx;
import com.mom.apps.model.Operator;
import com.mom.apps.ui.TransactionRequest;
import com.mom.apps.utils.AppConstants;
import com.mom.apps.utils.DataProvider;

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
    EditText _verifyTPin;
    Button _btnSubmit;

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
        this._verifyTPin  = (EditText) view.findViewById(R.id.verifyTpin);
        _btnSubmit         = (Button) view.findViewById(R.id.btnSubmit);
        if(_currentPlatform == PlatformIdentifier.MOM)
        {
            _verifyTPin.setVisibility(View.VISIBLE);
            _btnSubmit.setVisibility(View.VISIBLE);
        }
        else if(_currentPlatform == PlatformIdentifier.B2C)
        {
            _verifyTPin.setVisibility(View.VISIBLE);
            _btnSubmit.setVisibility(View.VISIBLE);
        }
        else if(_currentPlatform == PlatformIdentifier.PBX)
        {
            _spOperator.setVisibility(View.VISIBLE);
            _etSubscriberId.setVisibility(View.VISIBLE);
            _etCustomerNumber.setVisibility(View.VISIBLE);
            _etAmount.setVisibility(View.VISIBLE);
            _rechargeButton.setVisibility(View.VISIBLE);
        }

        _btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getVerifyTpin();
            }
        });
        _rechargeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateAndRecharge(view);
            }
        });

        getAllOperators();
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
                        _etSubscriberId.setVisibility(View.VISIBLE);
                        _etCustomerNumber.setVisibility(View.VISIBLE);
                        _etAmount.setVisibility(View.VISIBLE);
                        _rechargeButton.setVisibility(View.VISIBLE);
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

//                new android.os.Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        showBalance();
//                    }
//                },2000) ;

                showBalance();
                break;
        }
        
        taskCompleted(result);
    }

    @Override
    public void onTaskError(AsyncResult pResult, DataExImpl.Methods callback) {

    }

    public void getAllOperators() {
        List<Operator> operatorList = null;

        if (_currentPlatform == PlatformIdentifier.MOM){
            operatorList    = DataProvider.getMoMPlatformDTHOperators();
        } else if (_currentPlatform == PlatformIdentifier.B2C) {
            operatorList    = DataProvider.getMoMPlatformDTHOperators();
        }
        else if (_currentPlatform == PlatformIdentifier.PBX) {
            operatorList    = DataProvider.getPBXPlatformDTHOperators();
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


        String sCustomerNumber          = _etCustomerNumber.getText().toString().trim();

        if("".equals(sCustomerNumber)) {
            showMessage(getResources().getString(R.string.error_customer_phone_required));
            return;
        }else if(sCustomerNumber.length() != AppConstants.STANDARD_MOBILE_NUMBER_LENGTH){
            showMessage(String.format(getResources().getString(R.string.error_phone_length), AppConstants.STANDARD_MOBILE_NUMBER_LENGTH));
            return;
        }
        try {
            nAmount             = Integer.parseInt(_etAmount.getText().toString());
        }catch (NumberFormatException nfe){
            nfe.printStackTrace();
            showMessage(getResources().getString(R.string.prompt_numbers_only_amount));
            return;
        }
        if(nAmount < nMinAmount || nAmount > nMaxAmount){
            showMessage(String.format(getResources().getString(R.string.error_amount_min_max), nMinAmount, nMaxAmount));
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
Log.i("Params" , sSubscriberId +"/n" + sRechargeAmount +"/n"+ operator+ "/n" +  sCustomerNumber);
        TransactionRequest request      = new TransactionRequest(
                getActivity().getString(TransactionType.DTH.transactionTypeStringId),
                sSubscriberId,
                sCustomerNumber,
                Float.parseFloat(sRechargeAmount),
                operator
        );

        if (_currentPlatform == PlatformIdentifier.MOM){

            getDataEx(this).rechargeDTH(request);

        }else if (_currentPlatform == PlatformIdentifier.B2C){
            getDataEx(this).rechargeDTH(request);
        }
        else if (_currentPlatform == PlatformIdentifier.PBX){
            getDataEx(this).rechargeDTH(request);
        }

        _etSubscriberId.setText(null);
        _etAmount.setText(null);
        _etCustomerNumber.setText(null);
        _spOperator.setSelection(0);

        showProgress(true);
        updateAsyncQueue(request);
    }



    public void goBack(View view) {
        getActivity().getSupportFragmentManager().popBackStack();
    }


    public void confirmRecharge() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        // Setting Dialog Title
        alertDialog.setTitle(getResources().getString(R.string.AlertDialog_DTHRecharge));

        // Setting Dialog Message
        alertDialog.setMessage(getResources().getString(R.string.Lbl_SubscriberID) + " "
                + _etSubscriberId.getText().toString() + "\n" + getResources().getString(R.string.Lbl_Operator)
                + " " + _spOperator.getSelectedItem().toString() + "\n"
                + getResources().getString(R.string.Lbl_Amount) + " " + "Rs." + " "
                + _etAmount.getText().toString());

        alertDialog.setPositiveButton(getResources().getString(R.string.Dialog_Yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ((AlertDialog) dialog).getButton(
                                AlertDialog.BUTTON1).setEnabled(false);
                        startRecharge();
//							new GetLoginTask().onPostExecute("test");

                    }
                });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton(getResources().getString(R.string.Dialog_No),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which1) {

                        dialog.cancel();

                    }
                });

        alertDialog.show();
    }
}
