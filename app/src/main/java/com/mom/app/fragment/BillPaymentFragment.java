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
import android.widget.RadioButton;
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
    private EditText _etDueDate;
    private EditText _etSdCode;
    private EditText _etSop;
    private EditText _etFsa;
    private EditText _etAcMonth;
    private EditText _etFirstName;
    private EditText _etLastName;
    private EditText _etAccountNumber;
    private TextView _billMsgDisplay;
    private Button _btnPay;
    private int day;
    private int month;
    private int year;
    private Calendar cal;
    private RadioButton _rb, _rb1;
    String formatDueDate;
    Spinner _spOperator;
    Spinner _splOperatorSBE;
    Spinner _splOperatorNBE;

    public static BillPaymentFragment newInstance(PlatformIdentifier currentPlatform) {
        BillPaymentFragment fragment = new BillPaymentFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(AppConstants.ACTIVE_PLATFORM, currentPlatform);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _currentPlatform = (PlatformIdentifier) getArguments().getSerializable(AppConstants.ACTIVE_PLATFORM);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_bill_payment, null, false);

        _btnGetBillAmount = (Button) view.findViewById(R.id.btnGetBillAmount);
        _billMsgDisplay = (TextView) view.findViewById(R.id.msgDisplay);
        _spOperator = (Spinner) view.findViewById(R.id.Operator);
        _splOperatorNBE = (Spinner) view.findViewById(R.id.Spl_OperatorNBE);
        _splOperatorSBE = (Spinner) view.findViewById(R.id.Spl_OperatorSBE);
        _spOperator.setOnItemSelectedListener(new OperatorSelectedListener());
        _etSubscriberId = (EditText) view.findViewById(R.id.subscriberId);
        _etAmount = (EditText) view.findViewById(R.id.amount);
        _etCustomerNumber = (EditText) view.findViewById(R.id.number);
        _btnPay = (Button) view.findViewById(R.id.btnPay);
        _etDueDate = (EditText) view.findViewById(R.id.DueDate);
        _etSdCode = (EditText) view.findViewById(R.id.SDCode);
        _etSop = (EditText) view.findViewById(R.id.SOP);
        _etFsa = (EditText) view.findViewById(R.id.FSA);
        _etAcMonth = (EditText) view.findViewById(R.id.ACMonth);
        _etFirstName = (EditText) view.findViewById(R.id.first_name);
        _etLastName = (EditText) view.findViewById(R.id.last_name);
        _etAccountNumber = (EditText) view.findViewById(R.id.spl_accountnumber);
        _rb = (RadioButton) view.findViewById(R.id.RadioButton01);
        _rb1 = (RadioButton) view.findViewById(R.id.RadioButton02);
        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);


        _btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               validateNBE_SBE(view);

            }
        });

        _etDueDate.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
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
        switch (callback) {
            case PAY_BILL:
                if (result == null) {
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

        if (_currentPlatform == PlatformIdentifier.MOM) {
            operatorList = DataProvider.getMoMPlatformBillPayOperators();
        } else if (_currentPlatform == PlatformIdentifier.PBX) {
            operatorList = DataProvider.getPBXPlatformBillPayOperators();
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "Error", Toast.LENGTH_LONG)
                    .show();
        }

        if (operatorList == null) {
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

        if (_currentPlatform == PlatformIdentifier.MOM) {
            Operator operator = (Operator) _spOperator.getSelectedItem();
            String id = operator.getCode();
            //  String id      = AppConstants.OPERATOR_NEW.get(operator);
            if (id == null) {
                return "-1";
            }
            Log.i("OperatorBillMOM Payment", id);
            return id;

        } else if (_currentPlatform == PlatformIdentifier.PBX) {
            // String id      = AppConstants.OPERATOR_PBX.get(strOperatorName);
            Operator operator = (Operator) _spOperator.getSelectedItem();
            String id = operator.getCode();
            if (id == null) {
                return "-1";
            }
            Log.i("OperatorBill Payment", id);
            return id;
        }
        return "-1";
    }

    public void showBillMessage(String psMsg) {
        _billMsgDisplay.setVisibility(View.VISIBLE);
        _billMsgDisplay.setText(psMsg);
    }

    public void getBillAmount(View view) {
        Log.d(_LOG, "Get Bill Amount called");
        String sSubscriberId = _etSubscriberId.getText().toString();
        Operator operator = (Operator) _spOperator.getSelectedItem();

        IDataEx dataEx = getDataEx(new AsyncListener<Float>() {
            @Override
            public void onTaskSuccess(Float result, DataExImpl.Methods callback) {
                Log.e(_LOG, "Obtainined bill amount: " + result);
                int bill = Math.round(result);
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

        TransactionRequest request = new TransactionRequest();
        request.setOperator(operator);
        request.setConsumerId(sSubscriberId);
        dataEx.getBillAmount(request);
        Log.d(_LOG, "Get Bill Amount finished");
    }




    private void startPayment() {
        showMessage(null);
        String sSubscriberId            = _etSubscriberId.getText().toString();
        String sAmount                  = _etAmount.getText().toString();
        Operator operator               = (Operator) _spOperator.getSelectedItem();
        String sCustomerNumber          = _etCustomerNumber.getText().toString().trim();
        String sCustomerName            = _etFirstName.getText().toString();
        String sDueDate                 = _etDueDate.getText().toString();
        String sACMonth                 = _etAcMonth.getText().toString();
        String sSDCode                  = _etSdCode.getText().toString();
        String sSOP                     = _etSop.getText().toString();
        String sFSA                     = _etFsa.getText().toString();
        String sOperatorNBE             = _splOperatorNBE.getSelectedItem().toString();
        String sOperatorSBE             = _splOperatorSBE.getSelectedItem().toString();
        String sAccountNumber           = _etAccountNumber.getText().toString();
        String sBill;

        Log.i("Check" , sDueDate);
        int nRechargeType               = 0;

        sBill = null;

        if (_rb.isChecked() == true) {

          sBill ="Y";

        } else if (_rb1.isChecked() == true) {

            sBill ="Z";
        } else {
            sBill ="Y";
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date dueDate = dateFormat.parse(sDueDate);
            Log.i("LOGDate" , dueDate.toString());
            dateFormat = new SimpleDateFormat("dd/MM/yy");
            formatDueDate = dateFormat.format(dueDate);
            Log.i("LOGFormatDate" ,  formatDueDate);


        }
        catch (ParseException e) {

        }

        HashMap<String, String> map     = new HashMap<String, String>();

        if (_currentPlatform == PlatformIdentifier.MOM){
            map.put(AppConstants.PARAM_NEW_DUE_DATE,formatDueDate );
            map.put(AppConstants.PARAM_NEW_AC_MONTH,sACMonth );
            map.put(AppConstants.PARAM_NEW_SD_CODE,sSDCode );
            map.put(AppConstants.PARAM_NEW_SOP,sSOP );
            map.put(AppConstants.PARAM_NEW_FSA,sFSA );
            map.put(AppConstants.PARAM_NEW_STUBTYPE ,sBill );
            map.put(AppConstants.PARAM_NEW_SPECIAL_OPERATOR_NBE ,sOperatorNBE);
            map.put(AppConstants.PARAM_NEW_SPECIAL_OPERATOR_SBE , sOperatorSBE);
            map.put(AppConstants.PARAM_NEW_ACCOUNT_NUMBER , sAccountNumber);
        } else if (_currentPlatform == PlatformIdentifier.PBX){

        }


        TransactionRequest request  = new TransactionRequest(
                getActivity().getString(TransactionType.BILL_PAYMENT.transactionTypeStringId),
                sSubscriberId,
                sCustomerNumber,
                Float.parseFloat(sAmount),
                operator
        );

        getDataEx(this).payBill(request,sCustomerName,map );

        _etAmount.setText(null);
        _etSubscriberId.setText(null);
        _etCustomerNumber.setText(null);
        _spOperator.setSelection(0);
        showProgress(true);
    }



    public void confirmPayment() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        // Setting Dialog Title
        alertDialog.setTitle("Confirm Payment...");
        String sMsg     = "Operator: "
                + _spOperator.getSelectedItem() + "\n"
                + "Amount:" + " " + "Rs." + " "
                + _etAmount.getText() + "\n"
                + "Phone: " + _etCustomerNumber.getText();

        String sOperator                = _spOperator.getSelectedItem().toString();

        String sOperatorId              = getOperatorId(sOperator);
        Log.i("AlertOperatorId", sOperatorId);

        if(
                AppConstants.OPERATOR_ID_RELIANCE_ENERGY.equals(sOperatorId) ||
                        AppConstants.OPERATOR_ID_BEST_ELECTRICITY.equals(sOperatorId) ||
                        AppConstants.OPERATOR_ID_MAHANAGAR_GAS.equals(sOperatorId) ||
                        AppConstants.OPERATOR_ID_BSES_RAJDHANI.equals(sOperatorId) ||
                        AppConstants.OPERATOR_ID_RELIANCE_ENERGY.equals(sOperatorId) ||
                        AppConstants.OPERATOR_ID_BEST_ELECTRICITY.equals(sOperatorId)||
                        AppConstants.OPERATOR_ID_CESC_LIMITED.equals(sOperatorId)

                ){

            sMsg        = getResources().getString(R.string.lblConsumerNumber) + ": "
                    + _etSubscriberId.getText() + "\n" + sMsg;
        }

       else if(AppConstants.OPERATOR_ID_NBE.equals(sOperatorId) ||
                AppConstants.OPERATOR_ID_SBE.equals(sOperatorId)||
                AppConstants.OPERATOR_ID_BESCOM_BANGALURU.equals(sOperatorId)||
                AppConstants.OPERATOR_ID_CESCOM_MYSORE.equals(sOperatorId)||
                AppConstants.OPERATOR_ID_UHBVN_HARYANA.equals(sOperatorId) ){
            sMsg        = getResources().getString(R.string.lblAccountNumber) + ": "
                    + _etSubscriberId.getText() + "\n" + sMsg;

        }


        else if( AppConstants.OPERATOR_ID_MAHANAGAR_GAS.equals(sOperatorId)||
                 AppConstants.OPERATOR_ID_TATA_POWER_DELHI.equals(sOperatorId)){
            sMsg        = getResources().getString(R.string.lblCANumber) + ": "
                    + _etSubscriberId.getText() + "\n" + sMsg;

        }

        else if(  AppConstants.OPERATOR_ID_BSES_RAJDHANI.equals(sOperatorId)||
                  AppConstants.OPERATOR_ID_BSES_YAMUNA.equals(sOperatorId) ){
            sMsg        = getResources().getString(R.string.lblCRNIDNumber) + ": "
                    + _etSubscriberId.getText() + "\n" + sMsg;

        }


        else if(  AppConstants.OPERATOR_ID_INDRAPRASTHA_GAS.equals(sOperatorId) ){
            sMsg        = getResources().getString(R.string.lblBPNumber) + ": "
                    + _etSubscriberId.getText() + "\n" + sMsg;

        }

        else if(  AppConstants.OPERATOR_ID_DHBVN_HARYANA.equals(sOperatorId)||
                AppConstants.OPERATOR_ID_DELHI_JAL_BOARD.equals(sOperatorId)){
            sMsg        = getResources().getString(R.string.lblKNumber) + ": "
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
        _etDueDate.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));

   }



    public void showRetrieveBillFields(){
        _etSubscriberId.setVisibility(View.VISIBLE);
        _btnGetBillAmount.setVisibility(View.VISIBLE);

        String sOperator                = _spOperator.getSelectedItem().toString();
        Log.d(_LOG, "Operator selected: " + sOperator);
        String sOperatorId              = getOperatorId(sOperator);
        Log.d(_LOG, "Operator selectedID: " + sOperatorId);

        if(sOperatorId.equals(AppConstants.OPERATOR_ID_BSES_RAJDHANI)||
                sOperatorId.equals(AppConstants.OPERATOR_ID_BESCOM_BANGALURU) ||
                sOperatorId.equals(AppConstants.OPERATOR_ID_CESCOM_MYSORE)||
                sOperatorId.equals(AppConstants.OPERATOR_ID_DHBVN_HARYANA)||
                sOperatorId.equals(AppConstants.OPERATOR_ID_TATA_POWER_DELHI)||
                sOperatorId.equals(AppConstants.OPERATOR_ID_INDRAPRASTHA_GAS)){
            _btnGetBillAmount.setVisibility(View.GONE);
            _etDueDate.setVisibility(View.GONE);
            _etAcMonth.setVisibility(View.GONE);
            _rb.setVisibility(View.GONE);
            _rb1.setVisibility(View.GONE);
            _etSdCode.setVisibility(View.GONE);
            _etSop.setVisibility(View.GONE);
            _etFsa.setVisibility(View.GONE);
            _etFirstName.setVisibility(View.GONE);
            _etLastName.setVisibility(View.GONE);
           // _etAccountNumber.setVisibility(View.GONE);
            _splOperatorNBE.setVisibility(View.GONE);
            _splOperatorSBE.setVisibility(View.GONE);
        }

        else if(sOperatorId.equals(AppConstants.OPERATOR_ID_NBE)){
            _splOperatorNBE.setSelection(0);
            _etFirstName.setText("");
            _etLastName.setText("");
            _etSubscriberId.setText("");

            _btnGetBillAmount.setVisibility(View.GONE);
            _etAcMonth.setVisibility(View.GONE);
            _rb.setVisibility(View.GONE);
            _rb1.setVisibility(View.GONE);
            _etSdCode.setVisibility(View.GONE);
            _etSop.setVisibility(View.GONE);
            _etFsa.setVisibility(View.GONE);
            _etDueDate.setVisibility(View.GONE);
            _splOperatorNBE.setVisibility(View.VISIBLE);
            _splOperatorSBE.setVisibility(View.GONE);
            _etFirstName.setVisibility(View.VISIBLE);
            _etLastName.setVisibility(View.VISIBLE);
            _etSubscriberId.setVisibility(View.VISIBLE);
        }

        else if(sOperatorId.equals(AppConstants.OPERATOR_ID_SBE)){
            _splOperatorSBE.setSelection(0);
            _etFirstName.setText("");
            _etLastName.setText("");
            _etSubscriberId.setText("");

            _btnGetBillAmount.setVisibility(View.GONE);
            _etAcMonth.setVisibility(View.GONE);
            _rb.setVisibility(View.GONE);
            _rb1.setVisibility(View.GONE);
            _etSdCode.setVisibility(View.GONE);
            _etSop.setVisibility(View.GONE);
            _etFsa.setVisibility(View.GONE);
            _etDueDate.setVisibility(View.GONE);
            _splOperatorNBE.setVisibility(View.GONE);
            _splOperatorSBE.setVisibility(View.VISIBLE);
            _etFirstName.setVisibility(View.VISIBLE);
            _etLastName.setVisibility(View.VISIBLE);
            _etSubscriberId.setVisibility(View.VISIBLE);
        }
        else if( sOperatorId.equals(AppConstants.OPERATOR_ID_DELHI_JAL_BOARD)){
            _etDueDate.setText("");
            _btnGetBillAmount.setVisibility(View.GONE);
            _etAcMonth.setVisibility(View.GONE);
            _rb.setVisibility(View.GONE);
            _rb1.setVisibility(View.GONE);
            _etSdCode.setVisibility(View.GONE);
            _etSop.setVisibility(View.GONE);
            _etFsa.setVisibility(View.GONE);
            _etFirstName.setVisibility(View.GONE);
            _etLastName.setVisibility(View.GONE);
           // _etAccountNumber.setVisibility(View.GONE);
            _splOperatorNBE.setVisibility(View.GONE);
            _splOperatorSBE.setVisibility(View.GONE);
            _etDueDate.setVisibility(View.VISIBLE);
        }
        else if(sOperatorId.equals(AppConstants.OPERATOR_ID_CESC_LIMITED) ){

            _etDueDate.setText("");
            _etAcMonth.setText("");
            _btnGetBillAmount.setVisibility(View.GONE);
            _etSdCode.setVisibility(View.GONE);
            _etSop.setVisibility(View.GONE);
            _etFsa.setVisibility(View.GONE);
            _etFirstName.setVisibility(View.GONE);
            _etLastName.setVisibility(View.GONE);
            //_etAccountNumber.setVisibility(View.GONE);
            _splOperatorNBE.setVisibility(View.GONE);
            _splOperatorSBE.setVisibility(View.GONE);
            _etDueDate.setVisibility(View.VISIBLE);
            _etAcMonth.setVisibility(View.VISIBLE);
             _rb.setVisibility(View.VISIBLE);
             _rb1.setVisibility(View.VISIBLE);
        }

        else if(sOperatorId.equals(AppConstants.OPERATOR_ID_UHBVN_HARYANA)){
            _btnGetBillAmount.setVisibility(View.GONE);
            _etDueDate.setVisibility(View.GONE);
            _etAcMonth.setVisibility(View.GONE);
            _rb.setVisibility(View.GONE);
            _rb1.setVisibility(View.GONE);
            _etFirstName.setVisibility(View.GONE);
            _etLastName.setVisibility(View.GONE);
           // _etAccountNumber.setVisibility(View.GONE);
            _splOperatorNBE.setVisibility(View.GONE);
            _splOperatorSBE.setVisibility(View.GONE);
            _etSdCode.setVisibility(View.VISIBLE);
            _etSop.setVisibility(View.VISIBLE);
            _etFsa.setVisibility(View.VISIBLE);
        }
    }

    public void hideRetrieveBillFields(){
//        _etAmount.setBackgroundColor(getResources().getColor(R.color.white));
        _etSubscriberId.setText("");
        _etSubscriberId.setVisibility(View.GONE);
        _btnGetBillAmount.setVisibility(View.GONE);
        _etDueDate.setVisibility(View.GONE);
        _etAcMonth.setVisibility(View.GONE);
        _rb.setVisibility(View.GONE);
        _rb1.setVisibility(View.GONE);
        _etSdCode.setVisibility(View.GONE);
        _etSop.setVisibility(View.GONE);
        _etFsa.setVisibility(View.GONE);
        _etFirstName.setVisibility(View.GONE);
        _etLastName.setVisibility(View.GONE);
        _etAccountNumber.setVisibility(View.GONE);
        _splOperatorNBE.setVisibility(View.GONE);
        _splOperatorSBE.setVisibility(View.GONE);
    }
    private void validateNBE_SBE( View view) {




        String sOperator        = _spOperator.getSelectedItem().toString();
        Log.i("sOperator" , sOperator);
        String sOperatorId      = getOperatorId(sOperator);
        Log.i("sOperatorId" , sOperatorId);
        int nAmount             = 0;
        int nMinAmount          = 10;
        int nMaxAmount          = 20000;
        int nMinLength          = 10;
        int nMaxLength          = 10;

        int nCustomerNumberLength
                = _etCustomerNumber.getText().toString().trim().length();

        if (_spOperator.getSelectedItemPosition() < 1){
            showMessage(getResources().getString(R.string.prompt_select_operator));
            return;
        }

        if (AppConstants.OPERATOR_ID_NBE.equals(sOperatorId))

        {

            if (_splOperatorNBE.getSelectedItemPosition() < 1) {
                showMessage(getResources().getString(R.string.prompt_Validity_north_ServiceProvider));
                return ;
            } else if ("".equals(_etFirstName.getText().toString())) {
                showMessage(getResources().getString(R.string.lbl_FirstName));
                return ;

            } else if ("".equals(_etLastName.getText().toString())) {
                showMessage(getResources().getString(R.string.lbl_LastName));
                return ;

            } else if (_etSubscriberId.getText().toString().length() == 0) {
                showMessage(getResources().getString(R.string.lbl_AccountNumber));
                return ;
            }
            else   if(nCustomerNumberLength < nMinLength || nCustomerNumberLength > nMaxLength){
                if(nMinLength == nMaxLength){
                    showMessage(String.format(getResources().getString(R.string.error_phone_length), nMinLength));
                }else{
                    showMessage(String.format(getResources().getString(R.string.error_phone_length_min_max), nMinLength, nMaxLength));
                }
                return;
            }
            else if("".equals(_etAmount.getText().toString().trim())){
                showMessage(getResources().getString(R.string.prompt_numbers_only_amount));
                return;
            }
            else if(Integer.parseInt(_etAmount.getText().toString().trim()) < nMinAmount
                    || Integer.parseInt(_etAmount.getText().toString().trim()) > nMaxAmount){
                showMessage(String.format(getResources().getString(R.string.error_amount_min_max), nMinAmount, nMaxAmount));
                return;
            }


        } else if (AppConstants.OPERATOR_ID_SBE.equals(sOperatorId)) {
            if (_splOperatorSBE.getSelectedItemPosition() < 1) {
                showMessage(getResources().getString(R.string.prompt_Validity_south_ServiceProvider));
                return ;
            } else if ("".equals(_etFirstName.getText().toString())) {
                showMessage(getResources().getString(R.string.lbl_FirstName));
                return ;

            } else if ("".equals(_etLastName.getText().toString())) {
                showMessage(getResources().getString(R.string.lbl_LastName));
                return ;

            } else if (_etSubscriberId.getText().toString().length() == 0) {
                showMessage(getResources().getString(R.string.lbl_AccountNumber));
                return ;
            }
            else   if(nCustomerNumberLength < nMinLength || nCustomerNumberLength > nMaxLength){
                if(nMinLength == nMaxLength){
                    showMessage(String.format(getResources().getString(R.string.error_phone_length), nMinLength));

                }else{
                    showMessage(String.format(getResources().getString(R.string.error_phone_length_min_max), nMinLength, nMaxLength));
                }
                return;
            }  else if("".equals(_etAmount.getText().toString().trim())){
                showMessage(getResources().getString(R.string.prompt_numbers_only_amount));
                return;
            }
            else if(Integer.parseInt(_etAmount.getText().toString().trim()) < nMinAmount
                    || Integer.parseInt(_etAmount.getText().toString().trim()) > nMaxAmount){
                showMessage(String.format(getResources().getString(R.string.error_amount_min_max), nMinAmount, nMaxAmount));
                return;
            }


        }
        else if(AppConstants.OPERATOR_ID_BESCOM_BANGALURU.equals(sOperatorId)
                ||AppConstants.OPERATOR_ID_CESCOM_MYSORE.equals(sOperatorId)
               ){

            if (_etSubscriberId.getText().toString().length() == 0) {
                showMessage(getResources().getString(R.string.lbl_AccountNumber));
                return ;
            }
            else if(nCustomerNumberLength < nMinLength || nCustomerNumberLength > nMaxLength){
                if(nMinLength == nMaxLength){
                    showMessage(String.format(getResources().getString(R.string.error_phone_length), nMinLength));
                }else{
                    showMessage(String.format(getResources().getString(R.string.error_phone_length_min_max), nMinLength, nMaxLength));
                }
                return;
            }  else if("".equals(_etAmount.getText().toString().trim())){
                showMessage(getResources().getString(R.string.prompt_numbers_only_amount));
                return;
            }
            else if(Integer.parseInt(_etAmount.getText().toString().trim()) < nMinAmount
                    || Integer.parseInt(_etAmount.getText().toString().trim()) > nMaxAmount){
                showMessage(String.format(getResources().getString(R.string.error_amount_min_max), nMinAmount, nMaxAmount));
                return;
            }


        }
        else if(AppConstants.OPERATOR_ID_UHBVN_HARYANA.equals(sOperatorId) ){

            if (_etSubscriberId.getText().toString().length() == 0) {
                showMessage(getResources().getString(R.string.lbl_AccountNumber));
                return ;
            }
            else if(nCustomerNumberLength < nMinLength || nCustomerNumberLength > nMaxLength){
                if(nMinLength == nMaxLength){
                    showMessage(String.format(getResources().getString(R.string.error_phone_length), nMinLength));
                }else{
                    showMessage(String.format(getResources().getString(R.string.error_phone_length_min_max), nMinLength, nMaxLength));
                }
                return;
            } else if("".equals(_etAmount.getText().toString().trim())){
                showMessage(getResources().getString(R.string.prompt_numbers_only_amount));
                return;
            }
            else if(Integer.parseInt(_etAmount.getText().toString().trim()) < nMinAmount
                    || Integer.parseInt(_etAmount.getText().toString().trim()) > nMaxAmount){
                showMessage(String.format(getResources().getString(R.string.error_amount_min_max), nMinAmount, nMaxAmount));
                return;
            }
            else if (_etSdCode.getText().toString().length() == 0) {
                showMessage(getResources().getString(R.string.error_SDCode));
                return;
            }
            else if (_etSop.getText().toString().length() == 0) {
                showMessage(getResources().getString(R.string.error_SOP));
                return;
            }
            else if (_etFsa.getText().toString().length() == 0) {
                showMessage(getResources().getString(R.string.error_FSA));
                return;
            }


        }

        else   if(AppConstants.OPERATOR_ID_RELIANCE_ENERGY.equals(sOperatorId)
                ||AppConstants.OPERATOR_ID_BEST_ELECTRICITY.equals(sOperatorId)
                 )
        {
            String sSubscriberId          = _etSubscriberId.getText().toString().trim();


            if("".equals(sSubscriberId)) {
                showMessage(getResources().getString(R.string.prompt_Validity_consumer_number));
                return;
            }
            else if(nCustomerNumberLength < nMinLength || nCustomerNumberLength > nMaxLength){
                if(nMinLength == nMaxLength){
                    showMessage(String.format(getResources().getString(R.string.error_phone_length), nMinLength));
                }else{
                    showMessage(String.format(getResources().getString(R.string.error_phone_length_min_max), nMinLength, nMaxLength));
                }
                return;
            }

            else if("".equals(_etAmount.getText().toString().trim())){
                showMessage(getResources().getString(R.string.prompt_numbers_only_amount));
                return;
            }
            else if(Integer.parseInt(_etAmount.getText().toString().trim()) < nMinAmount
                    || Integer.parseInt(_etAmount.getText().toString().trim()) > nMaxAmount){
                showMessage(String.format(getResources().getString(R.string.error_amount_min_max), nMinAmount, nMaxAmount));
                return;
            }
            else{

            }

        }

        else if(AppConstants.OPERATOR_ID_MAHANAGAR_GAS.equals(sOperatorId)
                ||AppConstants.OPERATOR_ID_TATA_POWER_DELHI.equals(sOperatorId) )
        {
            if (_etSubscriberId.getText().toString().length() == 0) {
                showMessage(getResources().getString(R.string.prompt_Validity_CA_number));
                return;
            }
            else if(nCustomerNumberLength < nMinLength || nCustomerNumberLength > nMaxLength){
                if(nMinLength == nMaxLength){
                    showMessage(String.format(getResources().getString(R.string.error_phone_length), nMinLength));
                }else{
                    showMessage(String.format(getResources().getString(R.string.error_phone_length_min_max), nMinLength, nMaxLength));
                }
                return;
            }

            else if("".equals(_etAmount.getText().toString().trim())){
                showMessage(getResources().getString(R.string.prompt_numbers_only_amount));
                return;
            }
            else if(Integer.parseInt(_etAmount.getText().toString().trim()) < nMinAmount
                    || Integer.parseInt(_etAmount.getText().toString().trim()) > nMaxAmount){
                showMessage(String.format(getResources().getString(R.string.error_amount_min_max), nMinAmount, nMaxAmount));
                return;
            }

        }
        else if(AppConstants.OPERATOR_ID_BSES_YAMUNA.equals(sOperatorId)
                ||AppConstants.OPERATOR_ID_BSES_RAJDHANI.equals(sOperatorId) )
        {
            if (_etSubscriberId.getText().toString().length() == 0) {
                showMessage(getResources().getString(R.string.prompt_Validity_CRNID_number));
                return;
            }
            else if(nCustomerNumberLength < nMinLength || nCustomerNumberLength > nMaxLength){
                if(nMinLength == nMaxLength){
                    showMessage(String.format(getResources().getString(R.string.error_phone_length), nMinLength));
                }else{
                    showMessage(String.format(getResources().getString(R.string.error_phone_length_min_max), nMinLength, nMaxLength));
                }
                return;
            }

            else if("".equals(_etAmount.getText().toString().trim())){
                showMessage(getResources().getString(R.string.prompt_numbers_only_amount));
                return;
            }
            else if(Integer.parseInt(_etAmount.getText().toString().trim()) < nMinAmount
                    || Integer.parseInt(_etAmount.getText().toString().trim()) > nMaxAmount){
                showMessage(String.format(getResources().getString(R.string.error_amount_min_max), nMinAmount, nMaxAmount));
                return;
            }

        }
        else if(AppConstants.OPERATOR_ID_DELHI_JAL_BOARD.equals(sOperatorId) )
            {
            if (_etSubscriberId.getText().toString().length() == 0) {
                showMessage(getResources().getString(R.string.prompt_Validity_K_number));
                return;
            }
            else if(nCustomerNumberLength < nMinLength || nCustomerNumberLength > nMaxLength){
                if(nMinLength == nMaxLength){
                    showMessage(String.format(getResources().getString(R.string.error_phone_length), nMinLength));
                }else{
                    showMessage(String.format(getResources().getString(R.string.error_phone_length_min_max), nMinLength, nMaxLength));
                }
                return;
            }

            else if("".equals(_etAmount.getText().toString().trim())){
                showMessage(getResources().getString(R.string.prompt_numbers_only_amount));
                return;
            }
            else if(Integer.parseInt(_etAmount.getText().toString().trim()) < nMinAmount
                    || Integer.parseInt(_etAmount.getText().toString().trim()) > nMaxAmount){
                showMessage(String.format(getResources().getString(R.string.error_amount_min_max), nMinAmount, nMaxAmount));
                return;
            }

            else if(_etDueDate.getText().toString().length() == 0){
                showMessage(getResources().getString(R.string.error_Invalid_Date));
                return;

            }

        }

        else if(AppConstants.OPERATOR_ID_DHBVN_HARYANA.equals(sOperatorId) )
        {
            if (_etSubscriberId.getText().toString().length() == 0) {
                showMessage(getResources().getString(R.string.prompt_Validity_K_number));
                return;
            }
            else if(nCustomerNumberLength < nMinLength || nCustomerNumberLength > nMaxLength){
                if(nMinLength == nMaxLength){
                    showMessage(String.format(getResources().getString(R.string.error_phone_length), nMinLength));
                }else{
                    showMessage(String.format(getResources().getString(R.string.error_phone_length_min_max), nMinLength, nMaxLength));
                }
                return;
            }

            else if("".equals(_etAmount.getText().toString().trim())){
                showMessage(getResources().getString(R.string.prompt_numbers_only_amount));
                return;
            }
            else if(Integer.parseInt(_etAmount.getText().toString().trim()) < nMinAmount
                    || Integer.parseInt(_etAmount.getText().toString().trim()) > nMaxAmount){
                showMessage(String.format(getResources().getString(R.string.error_amount_min_max), nMinAmount, nMaxAmount));
                return;
            }


        }
        else if(AppConstants.OPERATOR_ID_INDRAPRASTHA_GAS.equals(sOperatorId))
        {
            if (_etSubscriberId.getText().toString().length() == 0)
            {
                showMessage(getResources().getString(R.string.prompt_Validity_BP_number));
                return;
            }
            else if(nCustomerNumberLength < nMinLength || nCustomerNumberLength > nMaxLength)
            {
                if(nMinLength == nMaxLength){
                    showMessage(String.format(getResources().getString(R.string.error_phone_length), nMinLength));
                }else{
                    showMessage(String.format(getResources().getString(R.string.error_phone_length_min_max), nMinLength, nMaxLength));
                }
                return;
            }

            else if("".equals(_etAmount.getText().toString().trim())){
                showMessage(getResources().getString(R.string.prompt_numbers_only_amount));
                return;
            }
            else if(Integer.parseInt(_etAmount.getText().toString().trim()) < nMinAmount
                    || Integer.parseInt(_etAmount.getText().toString().trim()) > nMaxAmount){
                showMessage(String.format(getResources().getString(R.string.error_amount_min_max), nMinAmount, nMaxAmount));
                return;
            }

        }
        else if(AppConstants.OPERATOR_ID_CESC_LIMITED.equals(sOperatorId))
        {

            if (_etSubscriberId.getText().toString().length() == 0) {
                showMessage(getResources().getString(R.string.prompt_Validity_consumer_number));
                return ;

            } else if (nCustomerNumberLength < nMinLength || nCustomerNumberLength > nMaxLength)
            {
                if(nMinLength == nMaxLength){
                    showMessage(String.format(getResources().getString(R.string.error_phone_length), nMinLength));
                }else{
                    showMessage(String.format(getResources().getString(R.string.error_phone_length_min_max), nMinLength, nMaxLength));
                }
                return;
            }
            else if("".equals(_etAmount.getText().toString().trim())){
                showMessage(getResources().getString(R.string.prompt_numbers_only_amount));
                return;
            }
            else if(Integer.parseInt(_etAmount.getText().toString().trim()) < nMinAmount
                    || Integer.parseInt(_etAmount.getText().toString().trim()) > nMaxAmount){
            showMessage(String.format(getResources().getString(R.string.error_amount_min_max), nMinAmount, nMaxAmount));
            return;
        }


            else if(_etDueDate.getText().toString().length() == 0){

                showMessage(getResources().getString(R.string.error_Invalid_Date));
                return;
            } else if (_etAcMonth.getText().toString().length() == 0) {
                showMessage(getResources().getString(R.string.error_AccountMonth));
                return;
            }else if ((_rb.isChecked() == false) && (_rb1.isChecked() == false)) {
                showMessage(getResources().getString(R.string.error_RadioButton));
                return;
            }

        }

        else if(
                sOperatorId.equals(AppConstants.OPERATOR_ID_AIRCEL_BILL) ||
                        sOperatorId.equals(AppConstants.OPERATOR_ID_AIRTEL_BILL)||
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
            nMaxAmount      = 500000;
            if(nCustomerNumberLength < nMinLength || nCustomerNumberLength > nMaxLength){
                if(nMinLength == nMaxLength){
                    showMessage(String.format(getResources().getString(R.string.error_phone_length), nMinLength));
                }else{
                    showMessage(String.format(getResources().getString(R.string.error_phone_length_min_max), nMinLength, nMaxLength));
                }
                return;
            }

            else if("".equals(_etAmount.getText().toString().trim())){
                showMessage(getResources().getString(R.string.prompt_numbers_only_amount));
                return;
            }
            else if(Integer.parseInt(_etAmount.getText().toString().trim()) < nMinAmount
                    || Integer.parseInt(_etAmount.getText().toString().trim()) > nMaxAmount){
                showMessage(String.format(getResources().getString(R.string.error_amount_min_max), nMinAmount, nMaxAmount));
                return;
            }

        }


        confirmPayment();

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
            Log.d(_LOG, "Operator selectedId: " + sOperatorId);
            if(sOperatorId == null){
                Log.d(_LOG, "Could not find operator id, doing nothing");
                return;
            }

            hideRetrieveBillFields();

            if(AppConstants.OPERATOR_ID_NBE.equals(sOperatorId) || AppConstants.OPERATOR_ID_SBE.equals(sOperatorId)){
                sHintDisplay                = sAccountNumber;
               showRetrieveBillFields();
            }else if(
                            AppConstants.OPERATOR_ID_BESCOM_BANGALURU.equals(sOperatorId)||
                            AppConstants.OPERATOR_ID_CESCOM_MYSORE.equals(sOperatorId)||
                            AppConstants.OPERATOR_ID_UHBVN_HARYANA.equals(sOperatorId) ){

                sHintDisplay                = sAccountNumber;

                showRetrieveBillFields();
            }

            else if( AppConstants.OPERATOR_ID_RELIANCE_ENERGY.equals(sOperatorId) ||
                    AppConstants.OPERATOR_ID_BEST_ELECTRICITY.equals(sOperatorId)||
                    AppConstants.OPERATOR_ID_CESC_LIMITED.equals(sOperatorId)){
                sHintDisplay                = sConsumerNumber;
                showRetrieveBillFields();
            }

            else if( AppConstants.OPERATOR_ID_MAHANAGAR_GAS.equals(sOperatorId)
                    ||  AppConstants.OPERATOR_ID_TATA_POWER_DELHI.equals(sOperatorId)){
                sHintDisplay                = sCANumber;
                showRetrieveBillFields();
            }

            else if(  AppConstants.OPERATOR_ID_BSES_RAJDHANI.equals(sOperatorId)
                    || AppConstants.OPERATOR_ID_BSES_YAMUNA.equals(sOperatorId) ){
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
