package com.mom.app.fragment;

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

import com.mom.app.R;
import com.mom.app.activity.LoginActivity;
import com.mom.app.error.MOMException;
import com.mom.app.identifier.IdentifierUtils;
import com.mom.app.identifier.MessageCategory;
import com.mom.app.identifier.PlatformIdentifier;
import com.mom.app.model.AsyncListener;
import com.mom.app.model.IDataEx;
import com.mom.app.model.b2cpl.B2CPLDataExImpl;
import com.mom.app.model.mompl.MoMPLDataExImpl;
import com.mom.app.model.pbxpl.PBXPLDataExImpl;
import com.mom.app.ui.IFragmentListener;
import com.mom.app.ui.TransactionRequest;
import com.mom.app.utils.AppConstants;

/**
 * Created by vaibhavsinha on 7/28/14.
 */
public abstract class FragmentBase extends Fragment{
    String _LOG     = AppConstants.LOG_PREFIX + "BASE FRAG";

    protected PlatformIdentifier _currentPlatform;
    IDataEx _dataEx     = null;
    TextView tvMsgDisplay;
    TextView tvResponseDisplay;
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
    public TextView getMsgTextView(){
        if(tvResponseDisplay == null){
            tvResponseDisplay    = (TextView) getActivity().findViewById(R.id.msgDisplayResponse);
           // tvResponseDisplay.requestFocus();
        }
        return tvResponseDisplay;
    }
    public void cancelAsynTaskFromFrag(){
        if(_dataEx!=null){
            Log.e("TestAsync" , "Working");
            _dataEx.cancelAsynctASK();
        }
    }

    public void showMessage(String psMsg){
        TextView response	= getMessageTextView();

        response.setVisibility(View.VISIBLE);
        response.requestFocus();
        response.setText(psMsg);
    }
    public void showMessageResponse(String psMsg){
        TextView response	= getMsgTextView();

        response.setVisibility(View.VISIBLE);
        response.requestFocus();
        response.setText(psMsg);
    }

    public void hideMessage(){
        getMessageTextView().setVisibility(View.GONE);

    }
//    public void hideMessageResponse(){
//
//        getMsgTextView().setVisibility(View.GONE);
//    }
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

            //THE FIRST IF CODE WILL SET THE PLATFORM, UNFORTUNATELY IT IS HOLDING THE REFERENCE OF
            //LISTENER AS WELL, AND THERE WAS NO WAY TO UPDATE REFERENCE TO LISTENER,
            //SO WHEN IN SAME FRAGMENT U HAVE 2 ASYNC TASK LIKE, THE SECOND REFERNCE SHALL CALL LISTENER
            //OF FIRST INIT, THE ELSE CONDITION WITLL ADDRESS THE ISSUE BY UPDATING THE LISTENER REFERENCES
            //THIS IS FIX, IN FUTURE YOU MAY CONSIDER SOME RE-DESIGN
            //ALSO ANY NEW PLATFORM SHOULD EXTEND THE ABSTRACT DataExImpl , THAN IT IS SAFE
            //ALSO IT IS UNSAFE TO HAVE 2 NETWORK THREDS RUNNING IN SAME FRAGMENT PARALLELY
            //THE USER INTERFACE SHOULD ENSURE THAT, ELSE THIS WILL FAIL
            //FOR THIS REASON THE BALANCE REFERESH IS MOVED TO ACTIVITY LOGIC, AS IT IS SEPERATE
            //AND BALANCE UI BELONG TO BASEACTIVITY, LET IT HANDLE ITS JOB:
            //:::::


//            if (_dataEx == null) {

                if (_currentPlatform == PlatformIdentifier.MOM) {
                    _dataEx = new MoMPLDataExImpl(getActivity(), pListener);
                }else if (_currentPlatform == PlatformIdentifier.B2C) {
                    _dataEx = new B2CPLDataExImpl(getActivity(), pListener);
                }else {
                    _dataEx = new PBXPLDataExImpl(getActivity(), null, pListener);
                }

            }
// else{
//                //CODE MOHIT
//                Log.i("getDataEx","update reference to listener start");
//                ((DataExImpl)_dataEx).set_listener(pListener);
//                Log.i("getDataEx","update reference to listener ends");
//            }
////            _dataEx.se
//=======
////            }
//>>>>>>> 4ef65a11a9fe718373ef5774812be13a03de950d
        catch(MOMException me){
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
        //Testing
        updateAsyncQueue(result);
    }

    protected void updateAsyncQueue(TransactionRequest request){
        Bundle bundle = new Bundle();
        bundle.putSerializable(AppConstants.BUNDLE_TRANSACTION_REQUEST, request);
//        Toast.makeText(getActivity().getApplicationContext(), R.string.added_to_queue, Toast.LENGTH_SHORT)
//                .show();

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
