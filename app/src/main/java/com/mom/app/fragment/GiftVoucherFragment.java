package com.mom.app.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
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
import android.print.PrintManager;

import com.mom.app.R;
import com.mom.app.activity.Helper;
import com.mom.app.activity.ListActivity;
import com.mom.app.error.MOMException;
import com.mom.app.identifier.PlatformIdentifier;
import com.mom.app.identifier.TransactionType;
import com.mom.app.model.AsyncListener;
import com.mom.app.model.AsyncResult;
import com.mom.app.model.DataExImpl;
import com.mom.app.model.Operator;
import com.mom.app.model.local.EphemeralStorage;
import com.mom.app.ui.TransactionRequest;
import com.mom.app.utils.AppConstants;
import com.mom.app.utils.DataProvider;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link android.app.Fragment} subclass.
 *
 */
public class GiftVoucherFragment extends FragmentBase implements AsyncListener<TransactionRequest<String>>{

    String _LOG     = AppConstants.LOG_PREFIX + "GiftVoucher";

    private EditText _etTargetPhone;
    private EditText _etAmount;
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
    Button _btnSubmit;
    ListView listView ;
    private ArrayAdapter<String> listAdapter ;


    @Override
    public void goToLogin() {
        super.goToLogin();
    }

    public GiftVoucherFragment() {
        // Required empty public constructor
    }


    public static GiftVoucherFragment newInstance(PlatformIdentifier currentPlatform){
        GiftVoucherFragment fragment   = new GiftVoucherFragment();
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
        View view                   = inflater.inflate(R.layout.activity_gift_voucher, null, false);

        setCurrentPlatform();

        this._spOperator    = (Spinner) view.findViewById(R.id.Operator);
        this._etTargetPhone = (EditText) view.findViewById(R.id.rechargeTargetPhone);
        this._etAmount      = (EditText) view.findViewById(R.id.amount);
        this._etOccasion    = (EditText) view.findViewById(R.id.occasion);
        this._etDescription = (EditText) view.findViewById(R.id.description);
        this._etSentTo      = (EditText) view.findViewById(R.id.sentTo);
        this._etSentFrom    = (EditText) view.findViewById(R.id.sentFrom);
        this._etEmailId     = (EditText) view.findViewById(R.id.emailId);
        this._rbtnPrint     = (RadioButton) view.findViewById(R.id.rbtnPrint);
        this._rbtnEmail     = (RadioButton) view.findViewById(R.id.rbtnEmail);
        this._rbtnSMS       = (RadioButton) view.findViewById(R.id.rbtnSMS);
        this._verifyTPin    = (EditText) view.findViewById(R.id.verifyTpin);
        this._tvlist        = (TextView) view.findViewById(R.id.rowTextView);
       _btnSubmit           = (Button) view.findViewById(R.id.btnSubmit);
        _rechargeBtn        = (Button) view.findViewById(R.id.btnRecharge);



        _rbtnPrint.setChecked(true);

        _rbtnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _etTargetPhone.setVisibility(View.GONE);
                _etEmailId.setVisibility(View.GONE);
                showMessage(null);
            }
        });
       _rbtnEmail.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
//               Toast.makeText(getActivity().getApplicationContext(),"working",Toast.LENGTH_LONG).show();
               _etEmailId.setVisibility(View.VISIBLE);
               _etTargetPhone.setVisibility(View.GONE);
           }
       });



       _rbtnSMS.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
//               Toast.makeText(getActivity().getApplicationContext(),"workingSMS",Toast.LENGTH_LONG).show();
               _etTargetPhone.setVisibility(View.VISIBLE);
               _etEmailId.setVisibility(View.GONE);
           }
       });





        _rechargeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateAndRecharge(view);
            }
        });


        try {
            getAllOperators();
        }catch(MOMException me){
            showMessage("Error getting operator list!");
        }

        addListenerOnSpinnerItemSelection();
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




    @Override
    public void onTaskSuccess(TransactionRequest<String> result, DataExImpl.Methods callback) {
        showProgress(false);
        Log.d(_LOG, "Called back: " + result);

        if(result == null){
            Log.e(_LOG, "Something wrong. Received null TransactionRequest object");
            showMessage(getResources().getString(R.string.error_recharge_failed));
            return;
        }

        switch(callback){

                case GIFT_VOUCHER:

                Log.e("Showbalance" , "method called");

                    Log.i("ResultChangeMobile" , result.getRemoteResponse());
                    String[] strArrayResponse = result.getRemoteResponse().split("~");
                    try {

                        if(strArrayResponse.length < 7){
                         throw new MOMException();
                    }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    EphemeralStorage.getInstance(getActivity()).storeString(AppConstants.PARAM_FLIP_KART_STATUS, strArrayResponse[0]);
                    EphemeralStorage.getInstance(getActivity()).storeString(AppConstants.PARAM_FLIP_KART_TRANSACTION_ID, strArrayResponse[1]);
                    EphemeralStorage.getInstance(getActivity()).storeString(AppConstants.PARAM_FLIP_KART_MOBILENUMBER, strArrayResponse[2]);
                    EphemeralStorage.getInstance(getActivity()).storeString(AppConstants.PARAM_FLIP_KART_AMOUNT, strArrayResponse[3]);
                    EphemeralStorage.getInstance(getActivity()).storeString(AppConstants.PARAM_FLIP_KART_VOUCHER, strArrayResponse[4]);
                    EphemeralStorage.getInstance(getActivity()).storeString(AppConstants.PARAM_FLIP_KART_PIN, strArrayResponse[5]);
                    EphemeralStorage.getInstance(getActivity()).storeString(AppConstants.PARAM_FLIP_KART_MESSAGE, strArrayResponse[6]);

                    showProgress(false);

//                   showMessageResponse(EphemeralStorage.getInstance(getActivity()).getString(AppConstants.PARAM_FLIP_KART_MESSAGE, null));
//                   _spOperator.setVisibility(View.GONE);
//                    _etOccasion.setVisibility(View.GONE);
//                    _etDescription.setVisibility(View.GONE);
//                    _etSentTo.setVisibility(View.GONE);
//                    _etSentFrom.setVisibility(View.GONE);
//                    _etAmount.setVisibility(View.GONE);
//                    _etTargetPhone.setVisibility(View.GONE);
//                    _etEmailId.setVisibility(View.GONE);
//                    _rbtnPrint.setVisibility(View.GONE);
//                    _rbtnSMS.setVisibility(View.GONE);
//                    _rbtnEmail.setVisibility(View.GONE);
//                    _rechargeBtn.setVisibility(View.GONE);
                  showBalance();

                  Log.i("GiftVoucher" , result.getRemoteResponse());
                    Intent  intent = new Intent(getActivity() , ListActivity.class);
                    startActivity(intent);
                    break;
        }
    }

    @Override
    public void onTaskError(AsyncResult pResult, DataExImpl.Methods callback) {
        Log.e(_LOG, "Error in recharging");
        showMessage(getResources().getString(R.string.error_recharge_failed));
    }
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, AbsListView.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
    @Override
    public void receiveMessage(Bundle bundle) throws MOMException{
        Log.d(_LOG, "Received message in sub-fragment");

    }

    public void addListenerOnSpinnerItemSelection() {
        _spOperator
                .setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }

    public void getAllOperators() throws MOMException{
        List<Operator> operatorList = null;

        if (_currentPlatform == PlatformIdentifier.MOM){
            operatorList    = DataProvider.getGiftVoucherOperators();
        } else if (_currentPlatform == PlatformIdentifier.PBX) {
            operatorList    = DataProvider.getGiftVoucherOperators();
        }
        else if (_currentPlatform == PlatformIdentifier.B2C) {
            operatorList    = DataProvider.getGiftVoucherOperators();
        }else {
            Toast.makeText(getActivity().getApplicationContext(), "Error", Toast.LENGTH_LONG)
                    .show();
        }

        showOperators(operatorList);
    }

    private void showOperators(List<Operator> operatorList){
        if(operatorList == null){
            Log.e(_LOG, "No operators!");
            return;
        }

        operatorList.add(0, new Operator("", getActivity().getString(R.string.prompt_spinner_select_operator)));


        ArrayAdapter<Operator> dataAdapter = new ArrayAdapter<Operator>(
                getActivity(), android.R.layout.simple_spinner_item,
                operatorList
        );

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _spOperator.setAdapter(dataAdapter);
    }

    public void validateAndRecharge(View view) {

        int nMinAmount          = 50;
        int nMaxAmount          = 50000;
        int nMinLength          = 10;
        int nMaxLength          = 10;
        int nConsumerNumberLength
                = _etTargetPhone.getText().toString().trim().length();

//        float balance         = EphemeralStorage.getInstance(getActivity().getApplicationContext()).getFloat(AppConstants.RMN_BALANCE, AppConstants.ERROR_BALANCE);
        if (_spOperator.getSelectedItemPosition() < 1){
            showMessage(getResources().getString(R.string.prompt_select_operator));
            return;
        }
        if ("".equals(_etOccasion.getText().toString().trim())) {
            showMessage(getResources().getString(R.string.prompt_TxnDescription));
            _etOccasion.setText("");
            return;
        }
        if ("".equals(_etDescription.getText().toString().trim())) {
            showMessage(getResources().getString(R.string.prompt_TxnDescription));
            _etDescription.setText("");
            return;
        }
        if ("".equals(_etSentTo.getText().toString().trim())) {
            showMessage(getResources().getString(R.string.prompt_TxnDescription));
            _etSentTo.setText("");
            return;
        }
        if ("".equals(_etSentFrom.getText().toString().trim())) {
            showMessage(getResources().getString(R.string.prompt_TxnDescription));
            _etSentFrom.setText("");
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
        float balance         = EphemeralStorage.getInstance(getActivity().getApplicationContext()).getFloat(AppConstants.RMN_BALANCE, AppConstants.ERROR_BALANCE);
//        if (Float.valueOf(_etAmount.getText().toString().trim()) > balance) {
//            Log.e("BalVal" , String.valueOf(balance));
//            showMessage(getResources().getString(R.string.Imps_failed_Amount));
//            return;
//        }

        if(_rbtnEmail.isChecked()) {
            if ("".equals(_etEmailId.getText().toString().trim())) {
                showMessage(getResources().getString(R.string.lblEmailId));
                _etEmailId.setText("");
                return;
            }
            if ((isEmailValid(_etEmailId.getText().toString())) == 0) {
                showMessage(getResources().getString(R.string.prompt_Validity_Email_Address));
                return;
            }
        }
            if(_rbtnSMS.isChecked()){

                if (nConsumerNumberLength < nMinLength || nConsumerNumberLength > nMaxLength) {
                    if (nMinLength == nMaxLength) {
                        showMessage(String.format(getResources().getString(R.string.error_phone_length), nMinLength));
                    } else {
                        showMessage(String.format(getResources().getString(R.string.error_phone_length_min_max), nMinLength, nMaxLength));
                    }
                    _etTargetPhone.setText("");
                    return;
                }
            }



        confirmRecharge();
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
        String sConsumerNumber = EphemeralStorage.getInstance(getActivity()).getString(AppConstants.PARAM_NEW_MOBILE_NUMBER, null);
        Log.e("Number", sConsumerNumber);
        if (_rbtnEmail.isChecked() || _rbtnPrint.isChecked()) {
            showDialog(
                    getResources().getString(R.string.AlertDialog_BillPayment),
                    getResources().getString(R.string.Lbl_MobileNumber) + " "
                            + sConsumerNumber + "\n" + getResources().getString(R.string.Lbl_Operator)
                            + " " + _spOperator.getSelectedItem().toString() + "\n"
                            + getResources().getString(R.string.Lbl_Amount) + " "
                            + _etAmount.getText().toString(),
                    getResources().getString(R.string.Dialog_Yes),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ((AlertDialog) dialog).getButton(
                                    AlertDialog.BUTTON1).setEnabled(false);
                            startRecharge();
//							new GetLoginTask().onPostExecute("test");

                        }
                    },
                    getResources().getString(R.string.Dialog_No),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which1) {
                            dialog.cancel();
                        }
                    }
            );
        } else {
            showDialog(
                    getResources().getString(R.string.AlertDialog_BillPayment),
                    getResources().getString(R.string.Lbl_MobileNumber) + " "
                            + _etTargetPhone.getText().toString() + "\n" + getResources().getString(R.string.Lbl_Operator)
                            + " " + _spOperator.getSelectedItem().toString() + "\n"
                            + getResources().getString(R.string.Lbl_Amount) + " "
                            + _etAmount.getText().toString(),
                    getResources().getString(R.string.Dialog_Yes),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ((AlertDialog) dialog).getButton(
                                    AlertDialog.BUTTON1).setEnabled(false);
                            startRecharge();
//							new GetLoginTask().onPostExecute("test");

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

    private void startRecharge() {
        showMessage(null);
        Operator operator               = (Operator) _spOperator.getSelectedItem();
        String sDescription            = _etDescription.getText().toString();
        String sOccasion               = _etOccasion.getText().toString();
        String sSentTo                 = _etSentTo.getText().toString();
        String sSentFrom               = _etSentFrom.getText().toString();
        String sEmailId                = _etEmailId.getText().toString();
        String sConsumerNumber          = _etTargetPhone.getText().toString();
        String sRechargeAmount          = _etAmount.getText().toString();


        int nRechargeType               = 0;
        int nDeliveryMethod             = 3;

        if (_rbtnEmail.isChecked()) {
            nDeliveryMethod             = 2;
            sConsumerNumber             = EphemeralStorage.getInstance(getActivity()).getString(AppConstants.PARAM_NEW_MOBILE_NUMBER, null);

        } else if (_rbtnPrint.isChecked()) {
            nDeliveryMethod             = 1;
            sEmailId                    ="NA";
            sConsumerNumber             = EphemeralStorage.getInstance(getActivity()).getString(AppConstants.PARAM_NEW_MOBILE_NUMBER, null);

        }
        else if (_rbtnSMS.isChecked()) {
            nDeliveryMethod             = 3;
            sEmailId                    ="NA";
        }

        Log.e("params" ,operator + sDescription + sOccasion + sSentTo + sSentFrom + sEmailId + sConsumerNumber + sRechargeAmount);

        getDataEx(this).giftVoucher(operator , sDescription ,sOccasion ,sSentTo
                ,sSentFrom ,sEmailId ,sConsumerNumber ,sRechargeAmount ,nRechargeType ,nDeliveryMethod);
        showProgress(true);
//
//        getDataEx(this).giftVoucher(operator , sDescription ,sOccasion ,sSentTo
//                ,sSentFrom  ,sConsumerNumber ,sRechargeAmount ,nRechargeType ,nDeliveryMethod);

        //showProgress(true);


        _etTargetPhone.setText(null);
        _etAmount.setText(null);
        _spOperator.setSelection(0);
        _etOccasion.setText(null);
        _etDescription.setText(null);
        _etSentTo.setText(null);
        _etSentFrom.setText(null);
        _etEmailId.setText(null);
        _etTargetPhone.setText(null);
        _rbtnPrint.setChecked(true);
        if(_rbtnPrint.isChecked()){
            _etTargetPhone.setVisibility(View.GONE);
            _etEmailId.setVisibility(View.GONE);
        }
    }


    public class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

            _etTargetPhone.setText("");
            _etAmount.setText("");
            showMessage(null);


            _rbtnPrint.setChecked(true);
            _rbtnEmail.setChecked(false);
            _rbtnSMS.setChecked(false);

            Operator operator   = (Operator) _spOperator.getSelectedItem();
            String sOperatorId  = operator.code;

            if (
                    sOperatorId.equals(AppConstants.OPERATOR_ID_BSNL) ||
                            sOperatorId.equals(AppConstants.OPERATOR_ID_MTNL)
                    ) {

                _rbtnPrint.setVisibility(View.VISIBLE);
                _rbtnEmail.setVisibility(View.VISIBLE);
                _rbtnSMS.setVisibility(View.GONE);
                _rbtnPrint.setChecked(true);
                _rbtnEmail.setChecked(false);
            } else if (sOperatorId.equals(AppConstants.OPERATOR_ID_TATA_DOCOMO) ||
                    sOperatorId.equals(AppConstants.OPERATOR_ID_UNINOR)
                    ) {

                _etTargetPhone.setText("");
                _etAmount.setText("");
                _rbtnPrint.setVisibility(View.VISIBLE);
                _rbtnEmail.setVisibility(View.GONE);
                _rbtnSMS.setVisibility(View.VISIBLE);
                _rbtnPrint.setChecked(true);
                _rbtnEmail.setChecked(false);
            }

            else if (sOperatorId.equals(AppConstants.OPERATOR_ID_DATACOMM)) {

                _etTargetPhone.setText("");
                _etAmount.setText("");
                _rbtnPrint.setVisibility(View.VISIBLE);
                _rbtnEmail.setVisibility(View.GONE);
                _rbtnSMS.setVisibility(View.VISIBLE);
                _rbtnPrint.setChecked(true);
                _rbtnSMS.setChecked(false);

            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {

        }
    }

    private void showEditDialog() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        MyDialogFragment editNameDialog = new MyDialogFragment();
        editNameDialog.show(fm, "fragment_edit_name");
    }


        public void onFinishEditDialog(String inputText) {
        showMessage(inputText);
    }

    private void showAlert() {

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        LinearLayout layout = new LinearLayout(getActivity());
        TextView tvMessage = new TextView(getActivity());
        final EditText input = new EditText(getActivity().getApplicationContext());
        alert.setView(input);




        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(input);


        alert.setTitle("T-Pin");
        alert.setView(layout);

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String value = input.getText().toString();

                  TransactionRequest request  = new TransactionRequest(
                        getActivity().getString(TransactionType.MOM_T_PIN.transactionTypeStringId),
                        null );
              _dataEx.verifyTPin(value);
                Toast.makeText(getActivity().getApplicationContext(), value, Toast.LENGTH_LONG)
                        .show();

                showProgress(false);


            }
        });

        alert.show();
    }



}