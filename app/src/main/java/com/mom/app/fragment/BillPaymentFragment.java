package com.mom.app.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.text.ParseException;

import com.mom.app.R;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by vaibhavsinha on 10/8/14.
 */
public class BillPaymentFragment extends FragmentBase implements AsyncListener<TransactionRequest> {

    private String _LOG = AppConstants.LOG_PREFIX + "BILL PAY";

    Button _btnGetBillAmount;
    private EditText _etSubscriberId;
    private EditText _etAmount;
    private EditText _etCustomerNumber;
     EditText et_dueDate ,et_sdCode , et_sop ,et_fsa ,et_acMonth;
    private TextView _billMsgDisplay;
    private Button _btnPay;
    private int day;
    private int month;
    private int year;
    private Calendar cal;
    String formatDueDate;

    TransactionRequest transrequest = new TransactionRequest();
    Spinner _spOperator;

    public static BillPaymentFragment newInstance(PlatformIdentifier currentPlatform){
        BillPaymentFragment fragment        = new BillPaymentFragment();
        Bundle bundle                       = new Bundle();
        bundle.putSerializable(AppConstants.ACTIVE_PLATFORM, currentPlatform);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _currentPlatform        = (PlatformIdentifier) getArguments().getSerializable(AppConstants.ACTIVE_PLATFORM);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_bill_payment, null, false);

        _btnGetBillAmount   = (Button) view.findViewById(R.id.btnGetBillAmount);
        _billMsgDisplay     = (TextView) view.findViewById(R.id.msgDisplay);
        _spOperator         = (Spinner) view.findViewById(R.id.Operator);
        _spOperator.setOnItemSelectedListener(new OperatorSelectedListener());

        _etSubscriberId     = (EditText) view.findViewById(R.id.subscriberId);
        _etAmount           = (EditText) view.findViewById(R.id.amount);
        _etCustomerNumber   = (EditText) view.findViewById(R.id.number);
        _btnPay             = (Button) view.findViewById(R.id.btnPay);

        et_dueDate = (EditText) view.findViewById(R.id.DueDate);
        et_sdCode = (EditText) view.findViewById(R.id.SDCode);
        et_sop = (EditText) view.findViewById(R.id.SOP);
        et_fsa = (EditText) view.findViewById(R.id.FSA);
        et_acMonth = (EditText) view.findViewById(R.id.ACMonth);

        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);



        _btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateAndPay(view);
            }
        });

        et_dueDate.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    showDatePicker();

                }
                return false;

            }
        });

        _btnGetBillAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getBillAmount(view);
            }
        });

        getAllOperators();

        return view;
    }

    @Override
    public void onTaskSuccess(TransactionRequest result, DataExImpl.Methods callback) {
        Log.d(_LOG, "Called back");
        switch(callback){
            case PAY_BILL:
                if(result == null){
                    Log.d(_LOG, "Obtained NULL bill payment response");
                    showMessage(getResources().getString(R.string.error_recharge_failed));
                    return;
                }

                Log.d(_LOG, "Starting navigation to TxnMsg Activity");
//                navigateToTransactionMessageActivity(ActivityIdentifier.BILL_PAYMENT, result);
                break;
        }

        taskCompleted(result);
    }

    @Override
    public void onTaskError(AsyncResult pResult, DataExImpl.Methods callback) {

    }

    @Override
    protected void showBalance(float pfBalance) {

    }


    public void getAllOperators() {
        List<Operator> operatorList = null;

        if (_currentPlatform == PlatformIdentifier.MOM){
            operatorList    = DataProvider.getMoMPlatformBillPayOperators();
        } else if (_currentPlatform == PlatformIdentifier.PBX) {
            operatorList    = DataProvider.getPBXPlatformBillPayOperators();
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "Error", Toast.LENGTH_LONG)
                    .show();
        }

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



    private String getOperatorId(String strOperatorName) {

        if (_currentPlatform == PlatformIdentifier.MOM)
        {
            Operator operator               = (Operator) _spOperator.getSelectedItem();
            String id = operator.getCode();
          //  String id      = AppConstants.OPERATOR_NEW.get(operator);
            if(id == null){
                return "-1";
            }
            Log.i("OperatorBillMOM Payment", id);
            return id;

        } else if (_currentPlatform == PlatformIdentifier.PBX) {
           // String id      = AppConstants.OPERATOR_PBX.get(strOperatorName);
            Operator operator               = (Operator) _spOperator.getSelectedItem();
            String id = operator.getCode();
            if(id == null){
                return "-1";
            }
            Log.i("OperatorBill Payment", id);
            return id;
        }
        return "-1";
    }

    public void showBillMessage(String psMsg){
        _billMsgDisplay.setVisibility(View.VISIBLE);
        _billMsgDisplay.setText(psMsg);
    }

    public void getBillAmount(View view){
        Log.d(_LOG, "Get Bill Amount called");
        String sSubscriberId            = _etSubscriberId.getText().toString();
        Operator operator               = (Operator) _spOperator.getSelectedItem();

        IDataEx dataEx                  = getDataEx(new AsyncListener<Float>() {
            @Override
            public void onTaskSuccess(Float result, DataExImpl.Methods callback) {
                Log.e(_LOG, "Obtainined bill amount: " + result);
                int bill                = Math.round(result);
                _etAmount.setText(String.valueOf(bill));
//                _etAmount.setBackgroundColor(getResources().getColor(R.color.green));
            }

            @Override
            public void onTaskError(AsyncResult pResult, DataExImpl.Methods callback) {
                Log.e(_LOG, "Error obtaining bill amount");
//                _etAmount.setBackgroundColor(getResources().getColor(R.color.red));
                showBillMessage(getResources().getString(R.string.error_obtaining_bill_amount));
            }
        });

        TransactionRequest request  = new TransactionRequest();
        request.setOperator(operator);
        request.setConsumerId(sSubscriberId);
        dataEx.getBillAmount(request);
        Log.d(_LOG, "Get Bill Amount finished");
    }


    public void validateAndPay(View view) {
        if (_spOperator.getSelectedItemPosition() < 1){
            showMessage(getResources().getString(R.string.prompt_select_operator));
            return;
        }

        String sOperator        = _spOperator.getSelectedItem().toString();
        Log.i("sOperator" , sOperator);
        String sOperatorId      = getOperatorId(sOperator);
        Log.i("sOperatorId" , sOperatorId);

        int nMinAmount          = 10;
        int nMaxAmount          = 10000;
        int nMinLength          = 10;
        int nMaxLength          = 10;

        int nCustomerNumberLength
                = _etCustomerNumber.getText().toString().trim().length();
        int nAmount             = 0;

        try {
            nAmount             = Integer.parseInt(_etAmount.getText().toString().trim());
        }catch (NumberFormatException nfe){
            nfe.printStackTrace();
            showMessage(getResources().getString(R.string.prompt_numbers_only_amount));
            return;
        }

        if(
                sOperatorId.equals(AppConstants.OPERATOR_ID_AIRTEL_BILL) ||
                        sOperatorId.equals("BAI")
                ){

            nMinAmount      = 50;
            nMinLength      = 10;
            nMaxLength      = 11;
        }else if(
                        sOperatorId.equals(AppConstants.OPERATOR_ID_AIRCEL_BILL) ||
                        sOperatorId.equals(AppConstants.OPERATOR_ID_BSNL_BILL_PAY) ||
                        sOperatorId.equals(AppConstants.OPERATOR_ID_IDEA_BILL) ||
                        sOperatorId.equals(AppConstants.OPERATOR_ID_RELIANCE_BILL_GSM) ||
                        sOperatorId.equals(AppConstants.OPERATOR_ID_RELIANCE_BILL_CDMA) ||
                        sOperatorId.equals(AppConstants.OPERATOR_ID_TATA_BILL) ||
                        sOperatorId.equals(AppConstants.OPERATOR_ID_VODAFONE_BILL) ||
                        sOperatorId.equals("BAC") ||
                        sOperatorId.equals("BLL") ||
                        sOperatorId.equals("BID") ||
                        sOperatorId.equals("BRG") ||
                        sOperatorId.equals("BRC") ||
                        sOperatorId.equals("BTA") ||
                        sOperatorId.equals("BVO") ||
                        sOperatorId.equals("BRC")
                ){

            nMinAmount      = 50;
        }

        if(nCustomerNumberLength < nMinLength || nCustomerNumberLength > nMaxLength){
            if(nMinLength == nMaxLength){
                showMessage(String.format(getResources().getString(R.string.error_phone_length), nMinLength));
            }else{
                showMessage(String.format(getResources().getString(R.string.error_phone_length_min_max), nMinLength, nMaxLength));
            }
            return;
        }

        if(nAmount < nMinAmount || nAmount > nMaxAmount){
            showMessage(String.format(getResources().getString(R.string.error_amount_min_max), nMinAmount, nMaxAmount));
            return;
        }

        if(
                AppConstants.OPERATOR_ID_RELIANCE_ENERGY.equals(sOperatorId) ||
                        AppConstants.OPERATOR_ID_BEST_ELECTRICITY.equals(sOperatorId) ||
                        AppConstants.OPERATOR_ID_MAHANAGAR_GAS.equals(sOperatorId)){

            String sSubscriberId          = _etSubscriberId.getText().toString().trim();

            if("".equals(sSubscriberId)) {
                showMessage(getResources().getString(R.string.error_subscriber_id_length));
                return;
            }
        }


        confirmPayment();
    }

    private void startPayment() {
        showMessage(null);
        String sSubscriberId            = _etSubscriberId.getText().toString();
        String sAmount                  = _etAmount.getText().toString();
        Operator operator               = (Operator) _spOperator.getSelectedItem();
        String sCustomerNumber          = _etCustomerNumber.getText().toString().trim();
        String sCustomerName            = "";
        String sDueDate = et_dueDate.getText().toString();
        Log.i("Check" , sDueDate);
        int nRechargeType               = 0;


        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date dueDate = dateFormat.parse(sDueDate);
            Log.i("FormatDate" , dueDate.toString());
            dateFormat = new SimpleDateFormat("dd/MM/yy");
            formatDueDate = dateFormat.format(dueDate);
            Log.i("FormatDate1" , formatDueDate);


        }
        catch (ParseException e) {

        }

//        SharedPreferences pref = PreferenceManager
//                .getDefaultSharedPreferences(getApplicationContext());
        HashMap<String, String> map     = new HashMap<String, String>();

        if (_currentPlatform == PlatformIdentifier.MOM){
            map.put(AppConstants.PARAM_NEW_RELIANCE_SBE_NBE, "");
            map.put(AppConstants.PARAM_NEW_SPECIAL_OPERATOR_NBE, "");
        } else if (_currentPlatform == PlatformIdentifier.PBX){

        }

        TransactionRequest request  = new TransactionRequest(
                getActivity().getString(TransactionType.BILL_PAYMENT.transactionTypeStringId),
                sSubscriberId,
                sCustomerNumber,
                Float.parseFloat(sAmount),
                operator
        );

        getDataEx(this).payBill(request,sCustomerName,formatDueDate, map );

        _etAmount.setText(null);
        _etSubscriberId.setText(null);
        _etCustomerNumber.setText(null);
        _spOperator.setSelection(0);
        showProgress(true);
    }



    public void confirmPayment() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        // Setting Dialog Title
        alertDialog.setTitle("Confirm MobileRecharge...");

        String sMsg     = "Operator: "
                + _spOperator.getSelectedItem() + "\n"
                + "Amount:" + " " + "Rs." + " "
                + _etAmount.getText() + "\n"
                + "Phone: " + _etCustomerNumber.getText();

        String sOperator                = _spOperator.getSelectedItem().toString();

        String sOperatorId              = getOperatorId(sOperator);
        Log.i("AlertOperatorId" , sOperatorId);

        if(
                AppConstants.OPERATOR_ID_RELIANCE_ENERGY.equals(sOperatorId) ||
                        AppConstants.OPERATOR_ID_BEST_ELECTRICITY.equals(sOperatorId) ||
                        AppConstants.OPERATOR_ID_MAHANAGAR_GAS.equals(sOperatorId) ||
                        AppConstants.OPERATOR_ID_BSES_RAJDHANI.equals(sOperatorId) ||
                        AppConstants.OPERATOR_ID_BESCOM_BANGALURU.equals(sOperatorId)
                ){

            sMsg        = getResources().getString(R.string.lblConsumerNumber) + ": "
                    + _etSubscriberId.getText() + "\n" + sMsg;
        }

        // Setting Dialog Message
        alertDialog.setMessage(sMsg);

        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ((AlertDialog) dialog).getButton(
                                AlertDialog.BUTTON1).setEnabled(false);
                        startPayment();

                    }
                });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which1) {

                        dialog.cancel();

                    }
                });

        alertDialog.show();
    }


    private void showDatePicker() {
        DatepickerFragment date = new DatepickerFragment();

        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);

        date.setCallBack(ondate);
        date.show(getFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {

            showDate(arg1 , arg2 + 1, arg3);
        }
    };

    private void showDate(int year , int month, int day) {
        et_dueDate.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));

   }



    public void showRetrieveBillFields(){
        _etSubscriberId.setVisibility(View.VISIBLE);
        _btnGetBillAmount.setVisibility(View.VISIBLE);

        String sOperator                = _spOperator.getSelectedItem().toString();
        Log.d(_LOG, "Operator selected: " + sOperator);
        String sOperatorId              = getOperatorId(sOperator);
        Log.d(_LOG, "Operator selectedID: " + sOperatorId);

        if(AppConstants.OPERATOR_ID_BSES_RAJDHANI.equals(sOperatorId)||
           AppConstants.OPERATOR_ID_BESCOM_BANGALURU.equals(sOperatorId) ||
           AppConstants.OPERATOR_ID_CESCOM_MYSORE.equals(sOperatorId)||
           AppConstants.OPERATOR_ID_DHBVN_HARYANA.equals(sOperatorId)||
           AppConstants.OPERATOR_ID_INDRAPRASTHA_GAS.equals(sOperatorId)){
            _btnGetBillAmount.setVisibility(View.GONE);
            et_dueDate.setVisibility(View.GONE);
        }
        else if( AppConstants.OPERATOR_ID_DELHI_JAL_BOARD.equals(sOperatorId)){
            _btnGetBillAmount.setVisibility(View.GONE);
            et_dueDate.setVisibility(View.VISIBLE);
        }
    }

    public void hideRetrieveBillFields(){
//        _etAmount.setBackgroundColor(getResources().getColor(R.color.white));
        _etSubscriberId.setText("");
        _etSubscriberId.setVisibility(View.GONE);
        _btnGetBillAmount.setVisibility(View.GONE);
    }

    public class OperatorSelectedListener implements AdapterView.OnItemSelectedListener{
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }


        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            String sConsumerNumber          = getResources().getString(R.string.lblConsumerNumber);
            String sAccountNumber           = getResources().getString(R.string.lblAccountNumber);
            String sMobileNumber            = getResources().getString(R.string.lblCustomerMobile);
            String sCANumber                = getResources().getString(R.string.lblCANumber);
            String sCRNIDNumber             = getResources().getString(R.string.lblCRNIDNumber);
            String sBPNumber                = getResources().getString(R.string.lblBPNumber);
            String sKNumber                 = getResources().getString(R.string.lblKNumber);
            String sHintDisplay             = sConsumerNumber;

            showBillMessage("");

            String sOperator                = _spOperator.getSelectedItem().toString();

            Log.d(_LOG, "Operator selected: " + sOperator);
            String sOperatorId              = getOperatorId(sOperator);

            if(sOperatorId == null){
                Log.d(_LOG, "Could not find operator id, doing nothing");
                return;
            }

            hideRetrieveBillFields();

            if(AppConstants.OPERATOR_ID_NBE.equals(sOperatorId) || AppConstants.OPERATOR_ID_SBE.equals(sOperatorId)){
                sHintDisplay                = sConsumerNumber;
            }else if(
                            AppConstants.OPERATOR_ID_BESCOM_BANGALURU.equals(sOperatorId)||
                            AppConstants.OPERATOR_ID_CESCOM_MYSORE.equals(sOperatorId) ){

                sHintDisplay                = sAccountNumber;

                showRetrieveBillFields();
            }

            else if( AppConstants.OPERATOR_ID_RELIANCE_ENERGY.equals(sOperatorId) ||
                    AppConstants.OPERATOR_ID_BEST_ELECTRICITY.equals(sOperatorId)){
                sHintDisplay                = sConsumerNumber;
                showRetrieveBillFields();
            }

            else if( AppConstants.OPERATOR_ID_MAHANAGAR_GAS.equals(sOperatorId)){
                sHintDisplay                = sCANumber;
                showRetrieveBillFields();
            }

            else if(  AppConstants.OPERATOR_ID_BSES_RAJDHANI.equals(sOperatorId) ){
                sHintDisplay              = sCRNIDNumber;
                showRetrieveBillFields();
            }


            else if(  AppConstants.OPERATOR_ID_INDRAPRASTHA_GAS.equals(sOperatorId) ){
                sHintDisplay              = sBPNumber;
                showRetrieveBillFields();
            }

            else if(  AppConstants.OPERATOR_ID_DHBVN_HARYANA.equals(sOperatorId) ){
                sHintDisplay              = sKNumber;
                showRetrieveBillFields();
            }
            else if(  AppConstants.OPERATOR_ID_DELHI_JAL_BOARD.equals(sOperatorId) ){
                sHintDisplay              = sKNumber;
                showRetrieveBillFields();
            }

            _etSubscriberId.setHint(sHintDisplay);
        }
    }
}
