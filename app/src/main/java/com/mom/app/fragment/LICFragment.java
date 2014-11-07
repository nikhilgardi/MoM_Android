package com.mom.app.fragment;

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
import com.mom.app.identifier.PlatformIdentifier;
import com.mom.app.model.AsyncListener;
import com.mom.app.model.AsyncResult;
import com.mom.app.model.DataExImpl;
import com.mom.app.model.pbxpl.lic.LicLifeResponse;
import com.mom.app.ui.TransactionRequest;
import com.mom.app.utils.AppConstants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by vaibhavsinha on 10/14/14.
 */
public class LICFragment extends FragmentBase implements AsyncListener<TransactionRequest<LicLifeResponse>> {
    String _LOG         = AppConstants.LOG_PREFIX + "LIC";

    Button _btnGetPremium, _btnPay , _btnCancel , _btnGetAnotherPremium;

    private EditText _etLIC , _etCustMobileNumber;

    private TextView _tvPremiumAmount , _tvPlaceHolder, _tvStatus , _tvReceiptId , _tvFromDate , _tvToDate;

    private TableLayout _tblPolicyDetails;
    private TableLayout _tblPaymentConfirmation;


    private TransactionRequest<LicLifeResponse> _lastRequest;

    public static final String PymtTrsfrStatusSuccessCode = "S" ;

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


        _etLIC.setText("806000021");

        _btnGetPremium = (Button) view.findViewById(R.id.btnRegister);
        _btnPay                 = (Button) view.findViewById(R.id.btnPay);
        _btnCancel                = (Button) view.findViewById(R.id.btnCancel);
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
              //  confirmRecharge();
            getLicPayment(_lastRequest) ;

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
                _tblPaymentConfirmation.setVisibility(View.GONE);

            }
        });

        return view;
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
                break;
            case PAY_LIC:
                showMessage(getResources().getString(R.string.error_could_not_pay));
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
        _btnCancel.setVisibility(View.VISIBLE);
    }

    private void showPaymentConfirmation(TransactionRequest<LicLifeResponse> result){
        if(result == null){
            Log.d(_LOG, "Obtained NULL  response");
            showMessage(getResources().getString(R.string.lic_failed_msg_default));
            return;
        }

        _etLIC.setEnabled(true);
        _etCustMobileNumber.setEnabled(true);

        _tblPolicyDetails.setVisibility(View.GONE);
        _tblPaymentConfirmation.setVisibility(View.VISIBLE);

        _btnPay.setVisibility(View.GONE);
        _btnCancel.setVisibility(View.GONE);

        _btnGetAnotherPremium.setVisibility(View.VISIBLE);

        _tvStatus.setTextColor(getActivity().getResources().getColor(R.color.green));
        _tvStatus.setText(R.string.lic_success_msg_default);
        _tvReceiptId.setText(result.getCustom().getTransReceiptID());
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
        _etCustMobileNumber.setEnabled(false);
        _etLIC.setEnabled(false);

        _lastRequest    = null;
        getDataEx(this).lic(policyNumber);
        showProgress(true);
    }
    private void getLicPayment(TransactionRequest<LicLifeResponse>  _lastRequest)
   {
       String policyNumber           = _etLIC.getText().toString();
       String CustomerMobNo          = _etCustMobileNumber.getText().toString();
        hideMessage();
        _etLIC.setText(null);

        getDataEx(this).licPayment(_lastRequest , CustomerMobNo ,  policyNumber);
        showProgress(true);
    }

}
