package com.mom.app.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.mom.app.R;
import com.mom.app.activity.WebViewActivity;
import com.mom.app.error.MOMException;
import com.mom.app.identifier.PlatformIdentifier;
import com.mom.app.model.local.EphemeralStorage;
import com.mom.app.utils.AppConstants;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WalletUpdateFragment extends FragmentBase {

    String _LOG     = AppConstants.LOG_PREFIX + "WalletUpdate";

    private EditText _etTargetName;
    private EditText _etAmount;

    private EditText _etEmailId;

    Button _btnSubmit , _btnCancel;


    @Override
    public void goToLogin() {
        super.goToLogin();
    }

    public WalletUpdateFragment() {
        // Required empty public constructor
    }


    public static WalletUpdateFragment newInstance(PlatformIdentifier currentPlatform){
        WalletUpdateFragment fragment   = new WalletUpdateFragment();
        Bundle bundle                       = new Bundle();
        bundle.putSerializable(AppConstants.ACTIVE_PLATFORM, currentPlatform);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        showProgress(false);
        Log.d(_LOG, "onCreate");
        _currentPlatform        = (PlatformIdentifier) getArguments().getSerializable(AppConstants.ACTIVE_PLATFORM);
        Log.d(_LOG, "Returning from onCreate");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(_LOG, "onCreateView");
        View view                   = inflater.inflate(R.layout.activity_wallet_update, null, false);

        setCurrentPlatform();


        this._etTargetName = (EditText) view.findViewById(R.id.TargetName);
        this._etAmount      = (EditText) view.findViewById(R.id.amount);
        this._etEmailId     = (EditText) view.findViewById(R.id.emailId);
        _btnSubmit           = (Button) view.findViewById(R.id.btn_Submit);
        _btnCancel          = (Button) view.findViewById(R.id.btnCancel);

       _btnSubmit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               validate(view);
           }
       });
        _btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFragment(DashboardFragment.newInstance(_currentPlatform));
            }
        });

        return view;
    }


    private MenuItem balItem = null;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_activity_actions, menu);
        setHasOptionsMenu(true);
        balItem = menu.findItem(R.id.balance);
        MenuItemCompat.setShowAsAction(balItem, MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
        MenuItemCompat.getActionView(balItem);
        super.onCreateOptionsMenu(menu,inflater);
    }


    public void showFragment(Fragment fragment){
        showFragment(fragment, R.id.contentFrame);
    }

    public void showFragment(Fragment fragment, int containerResourceId){
        if(fragment == null){
            Log.e(_LOG, "No fragment sent to show. Doing nothing.");
            return;
        }

        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Log.d(_LOG, "Adding fragment");
        transaction.replace(containerResourceId, fragment);
        Log.d(_LOG, "Fragment added");

        transaction.commit();
    }



    public void validate(View view) {

       //int nMinAmount          = 10;
       int nMinAmount          = 1;
       int nMaxAmount          = 10000;


        if("".equals(_etTargetName.getText().toString())){
            showMessage(getResources().getString(R.string.prompt_Validity_name));
            _etTargetName.setText("");
            return;
        }
        if ((isEmailValid(_etEmailId.getText().toString()))== 0){
            showMessage(getResources().getString(R.string.prompt_Validity_Email_Address));
            _etEmailId.setText("");
            return;
        }
        if ("".equals(_etAmount.getText().toString().trim())) {
            showMessage(getResources().getString(R.string.prompt_numbers_only_amount));
            _etAmount.setText("");
            return;
        }

        if (Integer.parseInt(_etAmount.getText().toString().trim()) < nMinAmount
                || Integer.parseInt(_etAmount.getText().toString().trim()) > nMaxAmount) {
            showMessage(String.format(getResources().getString(R.string.error_amount_min_max), nMinAmount, nMaxAmount));
            _etAmount.setText("");
            return;
        }

        confirmRecharge();
    }
    private void startPayment() {
        Intent myintent = new Intent(getActivity() , WebViewActivity.class);
        myintent.putExtra(AppConstants.PARAM_NEW_STR_NAME, _etTargetName.getText().toString());
        myintent.putExtra(AppConstants.PARAM_NEW_STR_EMAIL, _etEmailId.getText().toString());
        myintent.putExtra(AppConstants.PARAM_NEW_STR_AMOUNT, _etAmount.getText().toString());
        myintent.putExtra(AppConstants.PARAM_NEW_STR_MOBILE_NUMBER_PAY_U, EphemeralStorage.getInstance(getActivity()).getString(AppConstants.LOGGED_IN_USERNAME, null));
        startActivity(myintent);



        _etTargetName.setText(null);
        _etEmailId.setText(null);
        _etAmount.setText(null);
        showMessage(null);
        showBalance();

    }


        public static int isEmailValid(String email) {
        int isValid = 0;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,3}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = 1;

        }
        return isValid;
    }

    public void confirmRecharge() {
        showDialog(
                getResources().getString(R.string.AlertDialog_BillPayment),

                        getResources().getString(R.string.Lbl_Amount) + " "
                        + _etAmount.getText().toString(),
                getResources().getString(R.string.Dialog_Yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ((AlertDialog) dialog).getButton(
                                AlertDialog.BUTTON1).setEnabled(false);
                        startPayment();

                    }
                },
                getResources().getString(R.string.Dialog_No),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which1) {
                        dialog.cancel();
                    }
                }
        );
    }

}