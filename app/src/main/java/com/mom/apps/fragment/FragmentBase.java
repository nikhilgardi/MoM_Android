package com.mom.apps.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.mom.apps.R;
import com.mom.apps.activity.LoginActivity;
import com.mom.apps.error.MOMException;
import com.mom.apps.identifier.IdentifierUtils;
import com.mom.apps.identifier.MessageCategory;
import com.mom.apps.identifier.PlatformIdentifier;
import com.mom.apps.model.AsyncListener;
import com.mom.apps.model.IDataEx;
import com.mom.apps.model.b2cpl.B2CPLDataExImpl;
import com.mom.apps.model.mompl.MoMPLDataExImpl;
import com.mom.apps.model.pbxpl.PBXPLDataExImpl;
import com.mom.apps.ui.IFragmentListener;
import com.mom.apps.ui.TransactionRequest;
import com.mom.apps.utils.AppConstants;

/**
 * Created by vaibhavsinha on 7/28/14.
 */
public abstract class FragmentBase extends Fragment{
    String _LOG     = AppConstants.LOG_PREFIX + "BASE FRAG";

    protected PlatformIdentifier _currentPlatform;
    IDataEx _dataEx     = null;
    TextView tvMsgDisplay;
    IFragmentListener _callbackListener;

    protected void showBalance(){
        Bundle bundle       = new Bundle();
        bundle.putSerializable(AppConstants.BUNDLE_MESSAGE_CATEGORY, MessageCategory.GET_AND_SHOW_BALANCE);
        _callbackListener.processMessage(bundle);
    }

    protected void setCurrentPlatform() {
        _currentPlatform    = IdentifierUtils.getPlatformIdentifier(getActivity());
    }

    protected void showProgress(boolean show){
        Bundle bundle   = new Bundle();
        if(show) {
            bundle.putInt(AppConstants.BUNDLE_PROGRESS, 1);
        }else{
            bundle.putInt(AppConstants.BUNDLE_PROGRESS, 0);
        }

        if(_callbackListener != null){
            _callbackListener.processMessage(bundle);
        }
    }

    public TextView getMessageTextView(){
        if(tvMsgDisplay == null){
            tvMsgDisplay    = (TextView) getActivity().findViewById(R.id.msgDisplay);
            tvMsgDisplay.requestFocus();
        }
        return tvMsgDisplay;
    }
    public void cancelAsynTaskFromFrag(){
        if(_dataEx!=null){
            _dataEx.cancelAsynctASK();
        }
    }

    public void showMessage(String psMsg){
        TextView response	= getMessageTextView();

        response.setVisibility(View.VISIBLE);
      // response.requestFocus();
        response.setText(psMsg);
    }

    public void hideMessage(){
        getMessageTextView().setVisibility(View.GONE);
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
        try {
//            if (_dataEx == null) {
                if (_currentPlatform == PlatformIdentifier.MOM) {
                    _dataEx = new MoMPLDataExImpl(getActivity(), pListener);
                }else if (_currentPlatform == PlatformIdentifier.B2C) {
                    _dataEx = new B2CPLDataExImpl(getActivity(), pListener);
                }else {
                    _dataEx = new PBXPLDataExImpl(getActivity(), null, pListener);
                }
//            }
        }catch(MOMException me){
            Log.w(_LOG, "Logged out", me);
            goToLogin();
            return null;
        }
        return _dataEx;
    }

    public void goToLogin(){
        Intent intent           = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }


    protected void goBack(){
        getActivity().getSupportFragmentManager().popBackStack();
    }

    protected void taskCompleted(TransactionRequest result){
        showProgress(false);
        updateAsyncQueue(result);
    }

    protected void updateAsyncQueue(TransactionRequest request){
        Bundle bundle = new Bundle();
        bundle.putSerializable(AppConstants.BUNDLE_TRANSACTION_REQUEST, request);
        Toast.makeText(getActivity().getApplicationContext(), R.string.added_to_queue, Toast.LENGTH_SHORT)
                .show();

        _callbackListener.processMessage(bundle);
    }

    public void showDialog(
            String title,
            String message,
            String yes,
            DialogInterface.OnClickListener yesListener,
            String no,
            DialogInterface.OnClickListener noListener
    ) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        alertDialog.setPositiveButton(yes, yesListener);

        if(noListener != null) {
            // Setting Negative "NO" Button
            alertDialog.setNegativeButton(no, noListener);
        }

        alertDialog.show();
    }
}
