package com.mom.app.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mom.app.R;
import com.mom.app.activity.ListActivity;
import com.mom.app.error.MOMException;
import com.mom.app.identifier.PlatformIdentifier;
import com.mom.app.identifier.TransactionType;
import com.mom.app.model.AsyncListener;
import com.mom.app.model.AsyncResult;
import com.mom.app.model.DataExImpl;
import com.mom.app.model.IDataEx;
import com.mom.app.model.Operator;
import com.mom.app.model.local.EphemeralStorage;
import com.mom.app.ui.TransactionRequest;
import com.mom.app.utils.AppConstants;
import com.mom.app.utils.DataProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link android.app.Fragment} subclass.
 *
 */
public class BookComplaintFragment extends FragmentBase {

    String _LOG     = AppConstants.LOG_PREFIX + "Book Complaint";

    private EditText _etTransactionId;
    private EditText _etAmount;
    private EditText _etComment;
    private EditText _etOccasion;
    private EditText _etDescription;
    private EditText _etSentTo;
    private EditText _etSentFrom;
    private EditText _etEmailId;
    TextView _tvBalance;
    TextView _tvlist;
    Spinner _spOperator;
    RadioButton _rbtnPrint, _rbtnEmail, _rbtnSMS;
    Button _rechargeBtn;
    EditText _verifyTPin;
    Button _btnSubmit , _btnCancel;
    ListView listView ;
    private ArrayAdapter<String> listAdapter ;


    @Override
    public void goToLogin() {
        super.goToLogin();
    }

    public BookComplaintFragment() {
        // Required empty public constructor
    }


    public static BookComplaintFragment newInstance(PlatformIdentifier currentPlatform){
        BookComplaintFragment fragment   = new BookComplaintFragment();
        Bundle bundle                       = new Bundle();
        bundle.putSerializable(AppConstants.ACTIVE_PLATFORM, currentPlatform);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showProgress(false);
        Log.d(_LOG, "onCreate");
        _currentPlatform        = (PlatformIdentifier) getArguments().getSerializable(AppConstants.ACTIVE_PLATFORM);
        Log.d(_LOG, "Returning from onCreate");



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(_LOG, "onCreateView");
        View view                   = inflater.inflate(R.layout.activity_book_complaint, null, false);

        setCurrentPlatform();

        this._spOperator      = (Spinner) view.findViewById(R.id.Operator);
        this._etTransactionId = (EditText) view.findViewById(R.id.transactionID);
        this._etComment       = (EditText) view.findViewById(R.id.comment);
        this._etAmount        = (EditText) view.findViewById(R.id.amount);

       _btnSubmit             = (Button) view.findViewById(R.id.btn_Submit);
        _btnCancel            = (Button) view.findViewById(R.id.btnCancel);

       getComplaintType();
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

    private void getComplaintType() {

        IDataEx dataEx = getDataEx(new AsyncListener<String>() {
            @Override
            public void onTaskSuccess(String result, DataExImpl.Methods pMethod) {
                Log.e(_LOG, "GetComplaintType: " + result);


                showProgress(false);
                switch (pMethod) {
                    case GET_COMPLAINT_TYPE:
                        Log.i(_LOG, "Check result: " + result);


                        if (TextUtils.isEmpty(result)) {
                            Log.i(_LOG, "Response Empty");


                            return;

                        }
                 try {
                      String[] sComplaintType = result.split("~");


                      ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                      android.R.layout.simple_spinner_item, sComplaintType);
                      dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                      _spOperator.setAdapter(dataAdapter);
                       }
                  catch (Exception ex) {
                     String[] sComplaintType = {"NO ITEM TO DISPLAY"};
                      ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                              android.R.layout.simple_spinner_item, sComplaintType);
                      dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                      _spOperator.setAdapter(dataAdapter);
                    }

                }
            }

            @Override
            public void onTaskError(AsyncResult pResult, DataExImpl.Methods callback) {
                Log.e(_LOG, "Error obtaining Complaint Type Operator");

            }
        });


        dataEx.getComplaintType();
        Log.d(_LOG, "GetComplaintType Called");


        showProgress(true);


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
//        transaction.addToBackStack(null);
        transaction.commit();
    }
    public void validate(View view) {

        if (_spOperator.getSelectedItemPosition() < 1){
            showMessage(getResources().getString(R.string.prompt_select_complaint_type));
            return;
        }

        if("".equals(_etComment.getText().toString().trim()))
        {
            showMessage(getResources().getString(R.string.prompt_TxnDescription));
            _etComment.setText("");
            return;
        }
        confirmRecharge();
    }



    public void confirmRecharge() {
        String sConsumerNumber = EphemeralStorage.getInstance(getActivity()).getString(AppConstants.PARAM_NEW_MOBILE_NUMBER, null);
        Log.e("Number", sConsumerNumber);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        // Setting Dialog Title
        alertDialog.setTitle(getResources().getString(R.string.AlertDialog_Booking_Complaint));
        String sMsg = null;
        if("".equals(_etTransactionId.getText().toString())) {
            int sTransactionID = 0;
           sMsg = getResources().getString(R.string.lblTransactionID) + " "
                    + sTransactionID + "\n" + getResources().getString(R.string.Lbl_ComplaintType)
                    + " " + _spOperator.getSelectedItem().toString();

        }
        else{
          sMsg =  getResources().getString(R.string.lblTransactionID) + " "
                        + _etTransactionId.getText().toString() + "\n" + getResources().getString(R.string.Lbl_ComplaintType)
                        + " " + _spOperator.getSelectedItem().toString();
                    }
        alertDialog.setMessage(sMsg);


        alertDialog.setPositiveButton(getResources().getString(R.string.Dialog_Yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ((AlertDialog) dialog).getButton(
                                AlertDialog.BUTTON1).setEnabled(false);
                        startRecharge();

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

    private void startRecharge() {



        IDataEx dataEx = getDataEx(new AsyncListener<String>() {
            @Override
            public void onTaskSuccess(String result, DataExImpl.Methods pMethod) {
                Log.e(_LOG, "GetBookedComplaint: " + result);


                showProgress(false);
                switch (pMethod) {
                    case GET_BOOK_COMPLAINT:
                        Log.i(_LOG, "Check result: " + result);
                        if (result == null || ("".equals(result))) {
                            Log.d(_LOG, "Obtained NULL GetBookComplaint response");
                            showMessage(getResources().getString(R.string.error_could_not_pay));
                            return;
                        }

                        String[] sComplaintType = result.split("~");
                        showMessageResponse(sComplaintType[2].toString() + "\n" +
                                             getResources().getString(R.string.Lbl_Ticket_no) + sComplaintType[1].toString());

                        break;

                }

            }

            @Override
            public void onTaskError(AsyncResult pResult, DataExImpl.Methods callback) {
                Log.e(_LOG, "Error obtaining Complaint Response");

            }
        });

        showMessage(null);
        String operator                =  _spOperator.getSelectedItem().toString();
        int sTransactionId = 0;
        String sComment                =_etComment.getText().toString().trim();

       if("".equals(_etTransactionId.getText().toString()) || (_etTransactionId.getText().toString().equals(null))){
           sTransactionId = 0;
       }
        else
       {
            sTransactionId             = Integer.parseInt(_etTransactionId.getText().toString().trim());
           Log.e("TxnId" , String.valueOf(sTransactionId));
       }


        Log.e("paramsBookedComplaint" ,operator + sTransactionId + sComment);

        dataEx.bookComplaint(operator , sTransactionId ,sComment);
        showProgress(true);
        _etTransactionId.setText(null);
        _etComment.setText(null);
        _spOperator.setSelection(0);


    }







}