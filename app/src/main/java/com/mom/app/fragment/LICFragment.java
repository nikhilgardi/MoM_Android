package com.mom.app.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;

import com.mom.app.R;
import com.mom.app.activity.BaseActivity;
import com.mom.app.identifier.PlatformIdentifier;
import com.mom.app.model.AsyncListener;
import com.mom.app.model.AsyncResult;
import com.mom.app.model.DataExImpl;
import com.mom.app.model.local.EphemeralStorage;
import com.mom.app.model.pbxpl.lic.LicLifeResponse;
import com.mom.app.ui.TransactionRequest;
import com.mom.app.ui.flow.MoMScreen;
import com.mom.app.utils.AppConstants;
import com.mom.app.utils.DataProvider;
import com.mom.app.widget.holder.ImageItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class LICFragment extends FragmentBase implements AsyncListener<TransactionRequest<LicLifeResponse>> {
    String _LOG         = AppConstants.LOG_PREFIX + "LIC";

    Button _btnGetPremium, _btnPay , _btnPaymentCancel , _btnPolicyDetailsCancel , _btnGetAnotherPremium;

    private EditText _etLIC , _etCustMobileNumber;

    private TextView _tvPremiumAmount , _tvPlaceHolder, _tvStatus , _tvReceiptId , _tvFromDate , _tvToDate;

    private TableLayout _tblPolicyDetails;
    private TableLayout _tblPaymentConfirmation;


    private TransactionRequest<LicLifeResponse> _lastRequest;

    public static final String PymtTrsfrStatusSuccessCode = "S" ;
    public static final String PymtTrsfrStatusErrorCode3  = "E3" ;
    public static final String PymtTrsfrStatusErrorCode4  = "E4" ;
    public static final String PymtTrsfrStatusErrorCode5  = "E5" ;
    public static final String PymtTrsfrStatusErrorCode6  = "E6" ;
    public static final String PymtTrsfrStatusErrorCode7  = "E7" ;
    public static final String PymtTrsfrStatusErrorCode8  = "E8" ;
    public static final String PymtTrsfrStatusErrorCode9  = "E9" ;
    public static final String PymtTrsfrStatusErrorCode10 = "E10" ;
    public static final String PymtTrsfrStatusErrorCode11 = "E11" ;
    ArrayList<ImageItem<MoMScreen>> _menuItems;

    public static LICFragment newInstance(PlatformIdentifier currentPlatform){
        LICFragment fragment        = new LICFragment();
        Bundle bundle                       = new Bundle();
        bundle.putSerializable(AppConstants.ACTIVE_PLATFORM, currentPlatform);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view               = inflater.inflate(R.layout.activity_lic, null, false);

        _etLIC                  = (EditText) view.findViewById(R.id.lic);
        _etCustMobileNumber     = (EditText) view.findViewById(R.id.CustomerNumber);

        _tvPremiumAmount        = (TextView) view.findViewById(R.id.premiumAmount);
        _tvPlaceHolder          = (TextView) view.findViewById(R.id.policyHolder);
        _tvStatus               = (TextView) view.findViewById(R.id.status);
        _tvReceiptId            = (TextView) view.findViewById(R.id.receiptNo);
        _tvFromDate             = (TextView) view.findViewById(R.id.fromDate);
        _tvToDate               = (TextView) view.findViewById(R.id.toDate);

        _tblPolicyDetails       = (TableLayout) view.findViewById(R.id.policyDetails);
        _tblPaymentConfirmation = (TableLayout) view.findViewById(R.id.paymentConfirmation);


      //  _etLIC.setText("806000021");

        _btnGetPremium = (Button) view.findViewById(R.id.btnRegister);
        _btnPay                 = (Button) view.findViewById(R.id.btnPay);
        _btnPaymentCancel                = (Button) view.findViewById(R.id.btnPaymentCancel);
        _btnPolicyDetailsCancel     =   (Button) view.findViewById(R.id.btnPolicyDetailsCancel);
        _btnGetAnotherPremium   = (Button) view.findViewById(R.id.btnGetAnotherPremium);

        _btnGetPremium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateAndGetAmount();

            }
        });

        _btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            _btnPay.setEnabled(false);
                confirmRecharge();
           // getLicPayment(_lastRequest) ;

            }
        });

        _btnPolicyDetailsCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                _btnPolicyDetailsCancel.setEnabled(false);
                showFragment(DashboardFragment.newInstance(_currentPlatform));
                // getLicPayment(_lastRequest) ;

            }
        });
        _btnGetAnotherPremium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                _btnGetAnotherPremium.setVisibility(View.GONE);

                _etLIC.setVisibility(View.VISIBLE);
                _etLIC.setText("806000021");
                _etCustMobileNumber.setVisibility(View.VISIBLE);
                _btnGetPremium.setVisibility(View.VISIBLE);
                _btnPay.setEnabled(true);
                _tblPolicyDetails.setVisibility(View.GONE);
                _btnPolicyDetailsCancel.setVisibility(View.GONE);
                _tblPaymentConfirmation.setVisibility(View.GONE);

            }
        });

        _btnPaymentCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                _btnGetAnotherPremium.setVisibility(View.GONE);

                _etLIC.setVisibility(View.VISIBLE);
                _etLIC.setEnabled(true);
                _etCustMobileNumber.setEnabled(true);
                _etCustMobileNumber.setVisibility(View.VISIBLE);
                _btnGetPremium.setVisibility(View.VISIBLE);
                _btnPolicyDetailsCancel.setVisibility(View.VISIBLE);
                _btnPay.setEnabled(true);
                _btnPay.setVisibility(View.GONE);
                _btnPaymentCancel.setVisibility(View.GONE);
                _tblPolicyDetails.setVisibility(View.GONE);
                _tblPaymentConfirmation.setVisibility(View.GONE);
                showMessage(null);


            }
        });


        return view;
    }


    public void showFragment(Fragment fragment){
        showFragment(fragment, R.id.contentFrame);
    }

    public void showFragment(Fragment fragment, int containerResourceId){
        if(fragment == null){
            Log.e(_LOG, "No fragment sent to show. Doing nothing.");
            return;
        }

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Log.d(_LOG, "Adding fragment");
        transaction.replace(containerResourceId, fragment);
        Log.d(_LOG, "Fragment added");
//        transaction.addToBackStack(null);
        transaction.commit();
    }
    public void confirmRecharge() {
        showDialog(
                "Confirm Confirmation Collected",
                "PolicyNumber:" + " "
                        + _etLIC.getText().toString() + "\n"
                        + "Amount:" + " " + "Rs." + " "
                        +_tvPremiumAmount.getText().toString() ,
                "YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ((AlertDialog) dialog).getButton(
                                AlertDialog.BUTTON1).setEnabled(false);
                        getLicPayment(_lastRequest) ;
//							new GetLoginTask().onPostExecute("test");

                    }
                },
                "NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which1) {
                        dialog.cancel();
                        _btnPay.setEnabled(true);
                    }
                }


        );
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _currentPlatform        = (PlatformIdentifier) getArguments().getSerializable(AppConstants.ACTIVE_PLATFORM);
    }

    @Override
    public void onTaskSuccess(TransactionRequest<LicLifeResponse> result, DataExImpl.Methods callback) {
        Log.d(_LOG, "Called back: " + result);

        showProgress(false);

        switch(callback){
            case LIC:
                showPolicyDetails(result);
                break;

                case PAY_LIC:
                    showPaymentConfirmation(result);
                break;
        }
    }

    @Override
    public void onTaskError(AsyncResult pResult, DataExImpl.Methods callback) {
        switch (callback){
            case LIC:
                showMessage(getResources().getString(R.string.error_invalid_policy_number));
                _etLIC.setVisibility(View.GONE);
                _etCustMobileNumber.setVisibility(View.GONE);
                _btnGetPremium.setVisibility(View.GONE);
                _btnPolicyDetailsCancel.setVisibility(View.GONE);
                _btnPaymentCancel.setVisibility(View.VISIBLE);
                _etCustMobileNumber.setEnabled(true);
                _etLIC.setEnabled(true);
                break;
            case PAY_LIC:
                showMessage(getResources().getString(R.string.error_could_not_pay));
                _etLIC.getText().toString();
                _btnPay.setEnabled(true);
                break;
        }

        showProgress(false);

    }

    private void showPolicyDetails(TransactionRequest<LicLifeResponse> result){


        if(result == null){
            Log.d(_LOG, "Obtained NULL  response");
            showMessage(getResources().getString(R.string.error_invalid_policy_number));
            return;
        }

        _etLIC.setVisibility(View.GONE);
        _etCustMobileNumber.setVisibility(View.GONE);

        _tblPolicyDetails.setVisibility(View.VISIBLE);

        _tvPremiumAmount.setText(String.valueOf(result.getCustom().getTransInvAmount()));


        _tvPlaceHolder.setText(result.getCustom().getOLife().getParty().getFullName());

        _lastRequest    = result;

        String fromDate = result.getCustom().getOLife().getPolicy().getFrUnpaidPremiumDate();
        String toDate = result.getCustom().getOLife().getPolicy().getToUnpaidPremiumDate();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        try {
            Date FromUnpaidDate = dateFormat.parse(fromDate);
            Date ToUnpaidDate = dateFormat.parse(toDate);
            dateFormat = new SimpleDateFormat("MM/yyyy");
            String fromDateLic = dateFormat.format(FromUnpaidDate);
            String toDateLic = dateFormat.format(ToUnpaidDate);
            _tvFromDate.setText(fromDateLic);
            _tvToDate.setText(toDateLic);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        _btnGetPremium.setVisibility(View.GONE);
        _btnGetAnotherPremium.setVisibility(View.GONE);

        _btnPay.setVisibility(View.VISIBLE);
        _btnPaymentCancel.setVisibility(View.VISIBLE);
        _btnPolicyDetailsCancel.setVisibility(View.GONE);
    }

    private void showPaymentConfirmation(TransactionRequest<LicLifeResponse> result) {
        if (result == null) {
            Log.d(_LOG, "Obtained NULL  response");
            showMessage(getResources().getString(R.string.lic_failed_msg_default));
            return;
        }
        if (result.getCustom().getPymtTrsfrStatus().equals(PymtTrsfrStatusSuccessCode)) {
            _etLIC.setEnabled(true);
            _etCustMobileNumber.setEnabled(true);

            _tblPolicyDetails.setVisibility(View.GONE);
            _tblPaymentConfirmation.setVisibility(View.VISIBLE);

            _btnPay.setVisibility(View.GONE);
            _btnPaymentCancel.setVisibility(View.GONE);

            _btnGetAnotherPremium.setVisibility(View.VISIBLE);

            _tvStatus.setTextColor(getActivity().getResources().getColor(R.color.green));
            _tvStatus.setText(R.string.lic_success_msg_default);
            _tvReceiptId.setText(result.getCustom().getTransReceiptID());
        } else if (result.getCustom().getPymtTrsfrStatus().equals(PymtTrsfrStatusErrorCode3)
                || result.getCustom().getPymtTrsfrStatus().equals(PymtTrsfrStatusErrorCode4)
                || result.getCustom().getPymtTrsfrStatus().equals(PymtTrsfrStatusErrorCode5)
                || result.getCustom().getPymtTrsfrStatus().equals(PymtTrsfrStatusErrorCode6)
                || result.getCustom().getPymtTrsfrStatus().equals(PymtTrsfrStatusErrorCode7)
                || result.getCustom().getPymtTrsfrStatus().equals(PymtTrsfrStatusErrorCode8)
                || result.getCustom().getPymtTrsfrStatus().equals(PymtTrsfrStatusErrorCode9)
                || result.getCustom().getPymtTrsfrStatus().equals(PymtTrsfrStatusErrorCode10)
                || result.getCustom().getPymtTrsfrStatus().equals(PymtTrsfrStatusErrorCode11)) {
            _btnPay.setVisibility(View.GONE);
            _btnPaymentCancel.setVisibility(View.GONE);
            _btnGetAnotherPremium.setVisibility(View.VISIBLE);
            _tblPolicyDetails.setVisibility(View.GONE);
            _tblPaymentConfirmation.setVisibility(View.VISIBLE);
        }

        else{
            _btnPay.setVisibility(View.GONE);
            _btnPaymentCancel.setVisibility(View.GONE);
            _btnGetAnotherPremium.setVisibility(View.VISIBLE);
            _tblPolicyDetails.setVisibility(View.GONE);
            _tblPaymentConfirmation.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void showBalance(float pfBalance) {
    }

    public void validateAndGetAmount() {
        String policyNumber           = _etLIC.getText().toString();
        if(TextUtils.isEmpty(policyNumber)){
            showMessage(getActivity().getResources().getString(R.string.error_invalid_policy_number));
            return;
        }

        getPremiumAmount(policyNumber);
    }

    private void getPremiumAmount(String policyNumber) {
        hideMessage();

       // _etLIC.setText(null);
       // _etCustMobileNumber.setText(null);

        _etCustMobileNumber.setEnabled(true);
        _etLIC.setEnabled(true);


        _lastRequest    = null;
        getDataEx(this).lic(policyNumber);
        showProgress(true);
    }
    private void getLicPayment(TransactionRequest<LicLifeResponse>  _lastRequest)
   {
       String policyNumber           = _etLIC.getText().toString();
       String CustomerMobNo          = _etCustMobileNumber.getText().toString();
       hideMessage();
       // _etLIC.setText(null);

        getDataEx(this).licPayment(_lastRequest , CustomerMobNo ,  policyNumber);
        showProgress(true);
    }

}
