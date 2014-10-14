package com.mom.app.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mom.app.R;
import com.mom.app.error.MOMException;
import com.mom.app.identifier.IdentifierUtils;
import com.mom.app.identifier.PlatformIdentifier;
import com.mom.app.model.AsyncListener;
import com.mom.app.model.AsyncResult;
import com.mom.app.model.DataExImpl;
import com.mom.app.model.IDataEx;
import com.mom.app.model.local.EphemeralStorage;
import com.mom.app.model.mompl.MoMPLDataExImpl;
import com.mom.app.model.pbxpl.PBXPLDataExImpl;
import com.mom.app.ui.IFragmentListener;
import com.mom.app.utils.AppConstants;

import java.text.DecimalFormat;

/**
 * Created by vaibhavsinha on 7/28/14.
 */
public abstract class FragmentBase extends Fragment {
    String _LOG     = AppConstants.LOG_PREFIX + "BASE FRAG";

    protected PlatformIdentifier _currentPlatform;
    IDataEx _dataEx     = null;
    ProgressBar _pb;
    TextView tvMsgDisplay;
    IFragmentListener _callbackListener;
    Button _backBtn;


    protected abstract void showBalance(float pfBalance);

    protected void setCurrentPlatform() {
        _currentPlatform    = IdentifierUtils.getPlatformIdentifier(getActivity());
    }

    public TextView getMessageTextView(){
        if(tvMsgDisplay == null){
            tvMsgDisplay    = (TextView) getActivity().findViewById(R.id.msgDisplay);
        }
        return tvMsgDisplay;
    }

    public ProgressBar getProgressBar(View view){
        if(_pb == null){
            _pb			= (ProgressBar)view.findViewById(R.id.progressBar);
        }
        return _pb;
    }

    public void showMessage(String psMsg){
        TextView response	= getMessageTextView();

        response.setVisibility(View.VISIBLE);
        response.setText(psMsg);
    }

    public void receiveMessage(Bundle bundle) throws MOMException{

    }

    @Override
    public void onAttach(Activity activity) {
        Log.d(_LOG, "onAttach");
        super.onAttach(activity);
        try {
            _callbackListener = (IFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement IFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    protected boolean focusAndShowSoftInput(View view){
        if(view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            InputMethodManager imm = (InputMethodManager)
                    getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);


            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
            return true;
        }

        return false;
    }

    protected void hideSoftInput(View view){
        InputMethodManager imm = (InputMethodManager)
                getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);


        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public IDataEx getDataEx(AsyncListener pListener){
        if(_dataEx == null){
            if(_currentPlatform == PlatformIdentifier.NEW){
                _dataEx     = new MoMPLDataExImpl(getActivity().getApplicationContext(), pListener);
            }else{
                _dataEx     = new PBXPLDataExImpl(getActivity().getApplicationContext(), pListener);
            }
        }

        return _dataEx;
    }

    protected void showBalance(TextView tv){
        float balance         = EphemeralStorage.getInstance(
                getActivity()
        ).getFloat(AppConstants.USER_BALANCE, AppConstants.ERROR_BALANCE);

        showBalance(tv, balance);
    }

    protected void showBalance(TextView tv, Float balance){
        String sBal         = null;
        if(balance == AppConstants.ERROR_BALANCE){
            sBal            = getString(R.string.error_getting_balance);
            return;
        }else {
            DecimalFormat df = new DecimalFormat("#,###,###,##0.00");
            sBal = df.format(balance);
        }

        tv.setText("Balance: " + getResources().getString(R.string.Rupee) + sBal);
    }

    public void getBalanceAsync(){
        final Context context   = getActivity();
        Log.d(_LOG, "Getting Balance");
        IDataEx dataEx  = new MoMPLDataExImpl(getActivity().getApplicationContext(), new AsyncListener<Float>() {
            @Override
            public void onTaskSuccess(Float result, DataExImpl.Methods callback) {
                EphemeralStorage.getInstance(context).storeFloat(AppConstants.USER_BALANCE, result);
                showBalance(result);
            }

            @Override
            public void onTaskError(AsyncResult pResult, DataExImpl.Methods callback) {

            }
        });
        Log.d(_LOG, "DataEx instance created");
        dataEx.getBalance();
        Log.d(_LOG, "getBalance called");
    }

    protected void setupBackListener(){
        if(_backBtn == null){
            return;
        }

        _backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });
    }

    protected void goBack(){
        getFragmentManager().popBackStack();
    }
}
