package com.mom.app.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mom.app.R;
import com.mom.app.error.MOMException;
import com.mom.app.identifier.PlatformIdentifier;
import com.mom.app.identifier.TransactionType;
import com.mom.app.model.AsyncListener;
import com.mom.app.model.AsyncResult;
import com.mom.app.model.DataExImpl;
import com.mom.app.model.pbxpl.PBXPLDataExImpl;
import com.mom.app.ui.TransactionRequest;
import com.mom.app.utils.AppConstants;

/**
 * Created by vaibhavsinha on 10/14/14.
 */
public class LICFragment extends FragmentBase implements AsyncListener<TransactionRequest> {
    String _LOG         = AppConstants.LOG_PREFIX + "LIC";

    private EditText _etLIC;
    private TextView _tvPremiumAmount;

    public static LICFragment newInstance(PlatformIdentifier currentPlatform){
        LICFragment fragment        = new LICFragment();
        Bundle bundle                       = new Bundle();
        bundle.putSerializable(AppConstants.ACTIVE_PLATFORM, currentPlatform);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_lic, null, false);

        _etLIC                  = (EditText) view.findViewById(R.id.lic);
        _tvPremiumAmount        = (TextView) view.findViewById(R.id.premiumAmount);

        _etLIC.setText("806000021");

        Button btnregister      = (Button) view.findViewById(R.id.btnRegister);

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateAndGetAmount();
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
    public void onTaskSuccess(TransactionRequest result, DataExImpl.Methods callback) {
        Log.d(_LOG, "Called back");
        showProgress(false);

        switch(callback){
            case LIC:
                if(result == null){
                    Log.d(_LOG, "Obtained NULL  response");
                    showMessage(getResources().getString(R.string.error_invalid_policy_number));
                    return;
                }

                Log.d(_LOG, "Obtained: " + result);
                _tvPremiumAmount.setText(String.valueOf(result.getAmount()));
                break;
        }
    }

    @Override
    public void onTaskError(AsyncResult pResult, DataExImpl.Methods callback) {

    }

    @Override
    protected void showBalance(float pfBalance) {
    }

    private void showPolicy(){

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
        showMessage(null);
        _etLIC.setText(null);

        getDataEx(this).lic(policyNumber);
        showProgress(true);
    }
}
