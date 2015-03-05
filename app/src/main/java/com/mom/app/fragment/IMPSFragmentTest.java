package com.mom.app.fragment;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mom.app.R;
import com.mom.app.error.MOMException;
import com.mom.app.identifier.PlatformIdentifier;
import com.mom.app.identifier.TransactionType;
import com.mom.app.model.AsyncListener;
import com.mom.app.model.AsyncResult;
import com.mom.app.model.Beneficiary;
import com.mom.app.model.DataExImpl;
import com.mom.app.model.IDataEx;
import com.mom.app.model.Operator;
import com.mom.app.model.Transaction;
import com.mom.app.model.local.EphemeralStorage;
import com.mom.app.model.pbxpl.PBXPLDataExImpl;
import com.mom.app.ui.TransactionRequest;
import com.mom.app.utils.AppConstants;
import com.mom.app.utils.DataProvider;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by vaibhavsinha on 10/8/14.
 */
public class IMPSFragmentTest extends FragmentBase implements AsyncListener<TransactionRequest>  {

    private String _LOG = AppConstants.LOG_PREFIX + "BILL PAY";

    Button _btnGetBillAmount;
    private EditText _etSubscriberId;
    private EditText _etAmount;
    private EditText _etAvailableLimit;
    private EditText _etConsumerNumber;
    private EditText _etIFSC_Code;
    private EditText _etBeneficiaryMobile_Number;
    private TextView _tvGetIFSC_List;
    private EditText _etDueDate;
    private EditText _etSdCode;
    private EditText _etSop;
    private EditText _etFsa;
    private EditText _etAcMonth;
    private EditText _etBeneficiaryName;
    private EditText _etConsumerName;
    private EditText _etDob;
    private EditText _etEmailAddress;
    private EditText _etAccountNumber;
    private EditText _etProcessingFees;
    private EditText _etAmountPayable;
    private EditText _etOTP;
    private EditText _etLastName;
    private TextView _billMsgDisplay;
    private Button _btnPay;
    private Button _btnSubmit;
    private Button _btnNext;
    private Button _btnCancel;
    private Button _btnOTPResend;
    private Button _btnNewRecharge;
    private Button _btnImpsCreateCustomer;
    private Button _btnImpsCancel;
    private int day;
    private int month;
    private int year;
    private Calendar cal;
    private RadioButton _rbPay, _rbVerify;
    String formatDueDate;
    Spinner _spOperator;
    Spinner _OperatorPayFrom;
    Spinner _splOperatorNBE;

    public static IMPSFragmentTest newInstance(PlatformIdentifier currentPlatform) {
        IMPSFragmentTest fragment = new IMPSFragmentTest();
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
        View view = inflater.inflate(R.layout.activity_imps, null, false);

        _btnSubmit        = (Button) view.findViewById(R.id.BTN_Submit);
        _btnNext          = (Button) view.findViewById(R.id.btn_Submit);
        _btnCancel        = (Button) view.findViewById(R.id.btnCancel);
        _btnOTPResend     = (Button) view.findViewById(R.id.btn_OTPResend);
        _btnNewRecharge  = (Button) view.findViewById(R.id._btn_newRecharge);
        _btnGetBillAmount = (Button) view.findViewById(R.id.btnGetBillAmount);
        _btnImpsCreateCustomer = (Button) view.findViewById(R.id.btn_impsCreateCustomer);
        _btnImpsCancel     = (Button) view.findViewById(R.id.btn_ImpsCancel);
        _billMsgDisplay = (TextView) view.findViewById(R.id.msgDisplay);
        _spOperator = (Spinner) view.findViewById(R.id.Operator);
        _splOperatorNBE = (Spinner) view.findViewById(R.id.Spl_OperatorNBE);
        _OperatorPayFrom = (Spinner) view.findViewById(R.id.Operator_PayFrom);
        _spOperator.setOnItemSelectedListener(new OperatorSelectedListener());
        _etSubscriberId = (EditText) view.findViewById(R.id.subscriberId);
        _etAmount = (EditText) view.findViewById(R.id.amount);
        _etConsumerNumber = (EditText) view.findViewById(R.id.ConsumerNumber);
        _etBeneficiaryMobile_Number  = (EditText) view.findViewById(R.id.BeneficiaryMobile_number);
        _etAvailableLimit = (EditText) view.findViewById(R.id.availableLimit);
        _etIFSC_Code      = (EditText) view.findViewById(R.id.IFSC_Code);
        _etProcessingFees = (EditText) view.findViewById(R.id.ProcessingFees);
        _etAmountPayable  = (EditText) view.findViewById(R.id.AmountPayable);
        _etOTP            = (EditText) view.findViewById(R.id.OTP);
        _etConsumerName   = (EditText) view.findViewById(R.id.et_name);
        _etDob            = (EditText) view.findViewById(R.id.et_dob);
        _etEmailAddress   = (EditText) view.findViewById(R.id.et_emailId);
        _tvGetIFSC_List   = (TextView) view.findViewById(R.id.GetIFSC_List);
        _btnPay = (Button) view.findViewById(R.id.btn_Pay);
        _etDueDate = (EditText) view.findViewById(R.id.DueDate);
        _etSdCode = (EditText) view.findViewById(R.id.SDCode);
        _etSop = (EditText) view.findViewById(R.id.SOP);
        _etFsa = (EditText) view.findViewById(R.id.FSA);
        _etAcMonth = (EditText) view.findViewById(R.id.ACMonth);
        _etBeneficiaryName = (EditText) view.findViewById(R.id.beneficiary_name);
        _etLastName = (EditText) view.findViewById(R.id.last_name);
        _etAccountNumber = (EditText) view.findViewById(R.id.account_number);
        _rbPay = (RadioButton) view.findViewById(R.id.rbtnPay);
        _rbVerify = (RadioButton) view.findViewById(R.id.rbtn_Verify);
        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);

        _btnGetBillAmount.setVisibility(View.GONE);
        _splOperatorNBE.setVisibility(View.GONE);


        _tvGetIFSC_List.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(),"Its working",Toast.LENGTH_LONG).show();
            }
        });

        try {
            getAllOperators();
        }catch(MOMException me){
            showMessage("Error getting operator list!");
        }

        _btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _btnNewRecharge.setVisibility(View.VISIBLE);
                _spOperator.setVisibility(View.GONE);
                _etAvailableLimit.setVisibility(View.GONE);
                _etBeneficiaryName.setVisibility(View.GONE);
                _etAccountNumber.setVisibility(View.GONE);
                _etIFSC_Code.setVisibility(View.GONE);
                _etBeneficiaryMobile_Number.setVisibility(View.GONE);
                _OperatorPayFrom.setVisibility(View.GONE);
                _etAmount.setVisibility(View.GONE);
                _etProcessingFees.setVisibility(View.GONE);
                _etAmountPayable.setVisibility(View.GONE);
                _etOTP.setVisibility(View.GONE);
                _btnOTPResend.setVisibility(View.GONE);
                _btnPay.setVisibility(View.GONE);




            }
        });

        _btnNewRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _btnNewRecharge.setVisibility(View.GONE);
                _btnSubmit.setVisibility(View.VISIBLE);
                _etConsumerNumber.setVisibility(View.VISIBLE);

            }
        });

        _btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showRetrieveBillNextFields();
            }
        });

        _btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int nMinPhoneLength     = 10;
                int nMaxPhoneLength     = 10;
                int nPhoneLength        = _etConsumerNumber.getText().toString().length();
                if(nPhoneLength < nMinPhoneLength || nPhoneLength > nMaxPhoneLength){
                    if(nMinPhoneLength == nMaxPhoneLength) {
                        _etConsumerNumber.setText("");
                        showMessage(String.format(getResources().getString(R.string.error_phone_length), nMinPhoneLength));
                    }else{
                        _etConsumerNumber.setText("");
                        showMessage(String.format(getResources().getString(R.string.error_phone_length_min_max), nMinPhoneLength, nMaxPhoneLength));
                    }
                    return;
                }

                consumerRegistration();


              //  showRetrieveBillFields();

            }
        });

        _btnImpsCreateCustomer.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                createConsumerRegistration();
            }
        });




        _etDob.setOnTouchListener(new View.OnTouchListener() {

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



        return view;
    }


   /* @Override
    public void onTaskSuccess(TransactionRequest result, DataExImpl.Methods callback) {
        Log.d(_LOG, "Called back");
        switch (callback) {
            case IMPS_CUSTOMER_REGISTRATION:
                if (result == null) {
                    Log.d(_LOG, "Obtained NULL bill payment response");
                    showMessage(getResources().getString(R.string.error_recharge_failed));
                    return;
                }
                showBalance();
                Log.d(_LOG, "Starting navigation to TxnMsg Activity");
//                navigateToTransactionMessageActivity(ActivityIdentifier.BILL_PAYMENT, result);
                break;
        }
        Log.i("ResultChange" , result.getRemoteResponse()+ result.getResponseCode());
        showMessage(result.getRemoteResponse());
       Boolean isReg=  EphemeralStorage.getInstance(getActivity().getApplicationContext()).getBoolean(
               AppConstants.PARAM_PBX_IMPS_ISREGISTERED, false);
        Boolean isImpsServiceAllowed =  EphemeralStorage.getInstance(getActivity().getApplicationContext()).getBoolean(
                AppConstants.PARAM_PBX_IMPS_SERVICEALLOWED, false);

       System.out.println(isReg + "test");

        if((isReg== true )&&( isImpsServiceAllowed == true)){
            System.out.println(isReg+"true");
            Toast.makeText(getActivity().getApplicationContext(),"true",Toast.LENGTH_LONG).show();
            showRetrieveBillFields();
            getBeneficiaryList();
        }
        else if((isReg == false)&&( isImpsServiceAllowed == false)) {
            System.out.println(isReg+"False");
            Toast.makeText(getActivity().getApplicationContext(),"false",Toast.LENGTH_LONG).show();
            showCustomRegistrationFields();
        }
       // taskCompleted(result);
    }

    @Override
    public void onTaskError(AsyncResult pResult, DataExImpl.Methods callback) {

    }*/

//    @Override
//    protected void showBalance(float pfBalance) {
//
//    }

//    public void addListenerOnSpinnerItemSelection() {
//        _spOperator
//                .setOnItemSelectedListener(new IMPSFragment().CustomOnItemSelectedListener());
//    }

    public void getAllOperators() throws MOMException {
        List<Operator> operatorList = null;

        if (_currentPlatform == PlatformIdentifier.MOM){
            operatorList    = DataProvider.getMoMPlatformMobileOperators();
        } else if (_currentPlatform == PlatformIdentifier.PBX) {
            operatorList    = DataProvider.getPBXPlatformMobileOperators();
        } else {
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

        operatorList.add(0, new Operator("", getActivity().getString(R.string.prompt_spinner_select_NewBeneficiary)));


        ArrayAdapter<Operator> dataAdapter = new ArrayAdapter<Operator>(
                getActivity(), android.R.layout.simple_spinner_item,
                operatorList
        );

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _spOperator.setAdapter(dataAdapter);
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

    private void consumerRegistration() {
        showMessage(null);

        String sConsumerNumber = _etConsumerNumber.getText().toString().trim();

//        IDataEx dataEx = getDataEx(new AsyncListener<Object>()
//        {
//            public void onTaskSuccess(Object result, DataExImpl.Methods callback) {
//                Log.d(_LOG, "Called back");
//                switch (callback) {
//                    case IMPS_CUSTOMER_REGISTRATION:
//                        if (result == null) {
//                            Log.d(_LOG, "Obtained NULL bill payment response");
//                            showMessage(getResources().getString(R.string.error_recharge_failed));
//                            return;
//                        }
//                       showProgress(false);
//                        showBalance();
//                        Log.d(_LOG, "Starting navigation to TxnMsg Activity");
////                navigateToTransactionMessageActivity(ActivityIdentifier.BILL_PAYMENT, result);
//                        break;
//
//
//                }
////                Log.i("ResultChange", result.getRemoteResponse() + result.getResponseCode());
////                showMessage(result.getRemoteResponse());
//                Boolean isReg = EphemeralStorage.getInstance(getActivity().getApplicationContext()).getBoolean(
//                        AppConstants.PARAM_PBX_IMPS_ISREGISTERED, false);
//                Boolean isImpsServiceAllowed = EphemeralStorage.getInstance(getActivity().getApplicationContext()).getBoolean(
//                        AppConstants.PARAM_PBX_IMPS_SERVICEALLOWED, false);
//
//                System.out.println(isReg + "test");
//
//                if ((isReg == true) && (isImpsServiceAllowed == true)) {
//                    System.out.println(isReg + "true");
//                    Toast.makeText(getActivity().getApplicationContext(), "true", Toast.LENGTH_LONG).show();
//                    showRetrieveBillFields();
//                    getBeneficiaryList();
//                } else if ((isReg == false) && (isImpsServiceAllowed == false)) {
//                    System.out.println(isReg + "False");
//                    Toast.makeText(getActivity().getApplicationContext(), "false", Toast.LENGTH_LONG).show();
//                    showCustomRegistrationFields();
//                }
//                // taskCompleted(result);
//            }
//
//            @Override
//            public void onTaskError(AsyncResult pResult, DataExImpl.Methods callback) {
//
//            }
//        });

        TransactionRequest request  = new TransactionRequest(
                getActivity().getString(TransactionType.IMPS_CUSTOMER_REGISTRATION.transactionTypeStringId),
                sConsumerNumber
                       );

      // getDataEx(this).impsCustomerRegistration(sConsumerNumber);

        showProgress(true);
}

    private void getBeneficiaryList() {
        showMessage(null);


        String sConsumerNumber = _etConsumerNumber.getText().toString().trim();
        getDataEx(this).impsBeneficiaryList(sConsumerNumber);
        return;
//        IDataEx dataEx = getDataEx(new AsyncListener<Object> ()
//         {
//
//            public void onTaskSuccess(Object result, DataExImpl.Methods callback) {
//
//                switch (callback){
//                    case IMPS_BENEFICIARY_LIST:
//
//                        ArrayList<Beneficiary> list     = new ArrayList<Beneficiary>();
//                        list = (ArrayList<Beneficiary>) result;
//                        Log.i(_LOG, "Check Response123: " + list);
//
//
//                    break;
//                }
//                showProgress(false);
//
//            }
//
//            @Override
//            public void onTaskError(AsyncResult pResult, DataExImpl.Methods callback) {
//
//            }
//        });



//        try {
//
//
//            dataEx.impsBeneficiaryList(sConsumerNumber);
//            showProgress(true);
//        }catch(Exception me){
//            Log.e(_LOG, "Error getting dataex", me);
//
//        }



    }

 private void createConsumerRegistration(){
     showMessage(null);
 }



    private void startPayment() {
        showMessage(null);
        String sSubscriberId            = _etSubscriberId.getText().toString();
        String sAmount                  = _etAmount.getText().toString();
        Operator operator               = (Operator) _spOperator.getSelectedItem();
        String sCustomerNumber          = _etConsumerNumber.getText().toString().trim();
        String sAccountNumber           = _etConsumerNumber.getText().toString();
      //  String sCustomerName            = _etFirstName.getText().toString();
        String sDueDate                 = _etDueDate.getText().toString();
        String sACMonth                 = _etAcMonth.getText().toString();

        String sBill;

        Log.i("Check" , sDueDate);
        int nRechargeType               = 0;

        sBill = null;

        if (_rbPay.isChecked() == true) {

          sBill ="Y";

        } else if (_rbVerify.isChecked() == true) {

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

        } else if (_currentPlatform == PlatformIdentifier.PBX){
            map.put(AppConstants.PARAM_PBX_DUE_DATE,formatDueDate );
            map.put(AppConstants.PARAM_PBX_AC_MONTH,sACMonth );
            map.put(AppConstants.PARAM_PBX_ACCOUNT_NUMBER , sAccountNumber);
        }


        TransactionRequest request  = new TransactionRequest(
                getActivity().getString(TransactionType.UTILITY_BILL_PAYMENT.transactionTypeStringId),
                sSubscriberId,
                sCustomerNumber,
                Float.parseFloat(sAmount),
                operator
        );

     //   getDataEx(this).utilityPayBill(request,sCustomerName,map );
        _etAmount.setText(null);
        _etSubscriberId.setText(null);
        _etConsumerNumber.setText(null);
        _spOperator.setSelection(0);
        showProgress(true);
    }



  /*  public void confirmPayment() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        // Setting Dialog Title
        alertDialog.setTitle("Confirm Payment...");
        String sMsg     = "Operator: "
                + _spOperator.getSelectedItem() + "\n"
                + "Amount:" + " " + "Rs." + " "
                + _etAmount.getText() + "\n"
                + "Phone: " + _etConsumerNumber.getText();

        String sOperator                = _spOperator.getSelectedItem().toString();

//        Log.i("AlertOperatorId", sOperatorId);

        if(

                sOperatorId.equals("CES")

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
    }*/


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
    public void showRetrieveBillNextFields(){

        _etConsumerNumber.setVisibility(View.GONE);
        _etConsumerName.setVisibility(View.VISIBLE);
        _etDob.setVisibility(View.VISIBLE);
        _etEmailAddress.setVisibility(View.VISIBLE);
        _btnImpsCreateCustomer.setVisibility(View.VISIBLE);
        _btnImpsCancel.setVisibility(View.VISIBLE);
        _etAvailableLimit.setVisibility(View.GONE);
        _tvGetIFSC_List.setVisibility(View.GONE);
        _rbPay.setVisibility(View.GONE);
        _rbVerify.setVisibility(View.GONE);
        _btnNext.setVisibility(View.GONE);
        _btnCancel.setVisibility(View.GONE);
        _spOperator.setVisibility(View.VISIBLE);
        _etBeneficiaryName.setVisibility(View.VISIBLE);
        _etAccountNumber.setVisibility(View.VISIBLE);
        _etIFSC_Code.setVisibility(View.VISIBLE);
        _etBeneficiaryMobile_Number.setVisibility(View.VISIBLE);
        _OperatorPayFrom.setVisibility(View.VISIBLE);
        _etAmount.setVisibility(View.VISIBLE);
        _etProcessingFees.setVisibility(View.VISIBLE);
        _etAmountPayable.setVisibility(View.VISIBLE);
        _etOTP.setVisibility(View.VISIBLE);
        _btnOTPResend.setVisibility(View.VISIBLE);
        _btnPay.setVisibility(View.VISIBLE);

    }
    public void showCustomRegistrationFields() {

        _etConsumerNumber.setVisibility(View.VISIBLE);
        _etConsumerName.setVisibility(View.VISIBLE);
        _etDob.setVisibility(View.VISIBLE);
        _etEmailAddress.setVisibility(View.VISIBLE);
        _btnImpsCreateCustomer.setVisibility(View.VISIBLE);
        _btnImpsCancel.setVisibility(View.VISIBLE);

        _btnSubmit.setVisibility(View.GONE);

        _etAvailableLimit.setVisibility(View.GONE);
        _spOperator.setVisibility(View.GONE);
        _etBeneficiaryName.setVisibility(View.GONE);
        _etAccountNumber.setVisibility(View.GONE);
        _etIFSC_Code.setVisibility(View.GONE);
        _tvGetIFSC_List.setVisibility(View.GONE);
        _etBeneficiaryMobile_Number.setVisibility(View.GONE);
        _OperatorPayFrom.setVisibility(View.GONE);
        _etAmount.setVisibility(View.GONE);
        _rbPay.setVisibility(View.GONE);
        _rbVerify.setVisibility(View.GONE);
        _btnNext.setVisibility(View.GONE);
        _btnCancel.setVisibility(View.GONE);
    }

    public void showRetrieveBillFields(){


        _btnSubmit.setVisibility(View.GONE);
        _etConsumerNumber.setVisibility(View.VISIBLE);

        _etAvailableLimit.setVisibility(View.VISIBLE);
        _spOperator.setVisibility(View.VISIBLE);
        _etBeneficiaryName.setVisibility(View.VISIBLE);
        _etAccountNumber.setVisibility(View.VISIBLE);
        _etIFSC_Code.setVisibility(View.VISIBLE);
        _tvGetIFSC_List.setVisibility(View.VISIBLE);
        _etBeneficiaryMobile_Number.setVisibility(View.VISIBLE);
        _OperatorPayFrom.setVisibility(View.VISIBLE);
        _etAmount.setVisibility(View.VISIBLE);
        _rbPay.setVisibility(View.VISIBLE);
        _rbVerify.setVisibility(View.VISIBLE);
        _btnNext.setVisibility(View.VISIBLE);
        _btnCancel.setVisibility(View.VISIBLE);




    //    String sOperatorId              = getOperatorId(sOperator);
      //  Log.d(_LOG, "Operator selectedID: " + sOperatorId);

  /* if(sOperatorId.equals("CES") ){

            _etDueDate.setText("");
            _etAcMonth.setText("");
            _btnGetBillAmount.setVisibility(View.GONE);
            _etSdCode.setVisibility(View.GONE);
            _etSop.setVisibility(View.GONE);
            _etFsa.setVisibility(View.GONE);
            //_etFirstName.setVisibility(View.GONE);
            _etLastName.setVisibility(View.GONE);
            _splOperatorNBE.setVisibility(View.GONE);
         //   _splOperatorSBE.setVisibility(View.GONE);
            _etDueDate.setVisibility(View.VISIBLE);
            _etAcMonth.setVisibility(View.VISIBLE);
            // _rb.setVisibility(View.VISIBLE);
            // _rb1.setVisibility(View.VISIBLE);
        }*/

    }

    public void hideRetrieveBillFields(){
        _etSubscriberId.setVisibility(View.GONE);
        _btnGetBillAmount.setVisibility(View.GONE);
//        _btnSubmit.setVisibility(View.GONE);
//        _etCustomerNumber.setVisibility(View.GONE);
//
//        _etAvailableLimit.setVisibility(View.GONE);
//        _spOperator.setVisibility(View.GONE);
//        _etBeneficiaryName.setVisibility(View.GONE);
//        _etAccountNumber.setVisibility(View.GONE);
//        _etIFSC_Code.setVisibility(View.GONE);
//        _tvGetIFSC_List.setVisibility(View.GONE);
//        _etBeneficiaryMobile_Number.setVisibility(View.GONE);
//        _OperatorPayFrom.setVisibility(View.GONE);
//        _etAmount.setVisibility(View.GONE);
//        _rbPay.setVisibility(View.GONE);
//        _rbVerify.setVisibility(View.GONE);
//        _btnNext.setVisibility(View.GONE);
//        _btnCancel.setVisibility(View.GONE);
//
//        _btnGetBillAmount.setVisibility(View.GONE);

    }
    private void validateNBE_SBE( View view) {




        String sOperator        = _spOperator.getSelectedItem().toString();
        Log.i("sOperator", sOperator);
      //  String sOperatorId      = getOperatorId(sOperator);
     //   Log.i("sOperatorId", sOperatorId);
        int nAmount             = 0;
        int nMinAmount          = 10;
        int nMaxAmount          = 20000;
        int nMinLength          = 10;
        int nMaxLength          = 10;

        int nCustomerNumberLength
                = _etConsumerNumber.getText().toString().trim().length();

        if (_spOperator.getSelectedItemPosition() < 1){
            showMessage(getResources().getString(R.string.prompt_select_operator));
            return;
        }

//        if(sOperatorId.equals("CES"))
//        {
//
//            if (_etSubscriberId.getText().toString().length() == 0) {
//                showMessage(getResources().getString(R.string.prompt_Validity_consumer_number));
//                return ;
//
//            } else if (nCustomerNumberLength < nMinLength || nCustomerNumberLength > nMaxLength)
//            {
//                if(nMinLength == nMaxLength){
//                    showMessage(String.format(getResources().getString(R.string.error_phone_length), nMinLength));
//                }else{
//                    showMessage(String.format(getResources().getString(R.string.error_phone_length_min_max), nMinLength, nMaxLength));
//                }
//                return;
//            }
//            else if("".equals(_etAmount.getText().toString().trim())){
//                showMessage(getResources().getString(R.string.prompt_numbers_only_amount));
//                return;
//            }
//            else if(Integer.parseInt(_etAmount.getText().toString().trim()) < nMinAmount
//                    || Integer.parseInt(_etAmount.getText().toString().trim()) > nMaxAmount){
//            showMessage(String.format(getResources().getString(R.string.error_amount_min_max), nMinAmount, nMaxAmount));
//            return;
//        }
//
//
//            else if(_etDueDate.getText().toString().length() == 0){
//
//                showMessage(getResources().getString(R.string.error_Invalid_Date));
//                return;
//            } else if (_etAcMonth.getText().toString().length() == 0) {
//                showMessage(getResources().getString(R.string.error_AccountMonth));
//                return;
//            }else if ((_rbPay.isChecked() == false) && (_rbVerify.isChecked() == false)) {
//                showMessage(getResources().getString(R.string.error_RadioButton));
//                return;
//            }
//
//        }



        //confirmPayment();

    }

    @Override
    public void onTaskSuccess(TransactionRequest result, DataExImpl.Methods callback) {
        Log.d(_LOG, "Called back");
        switch (callback) {
            case IMPS_CUSTOMER_REGISTRATION:
                if (result == null) {
                    Log.d(_LOG, "Obtained NULL bill payment response");
                    showMessage(getResources().getString(R.string.error_recharge_failed));
                    return;
                }
                showProgress(false);
                showBalance();
                Log.d(_LOG, "Starting navigation to TxnMsg Activity");
//                navigateToTransactionMessageActivity(ActivityIdentifier.BILL_PAYMENT, result);
                break;


        }
//                Log.i("ResultChange", result.getRemoteResponse() + result.getResponseCode());
//                showMessage(result.getRemoteResponse());
        Boolean isReg = EphemeralStorage.getInstance(getActivity().getApplicationContext()).getBoolean(
                AppConstants.PARAM_PBX_IMPS_ISREGISTERED, false);
        Boolean isImpsServiceAllowed = EphemeralStorage.getInstance(getActivity().getApplicationContext()).getBoolean(
                AppConstants.PARAM_PBX_IMPS_SERVICEALLOWED, false);

        System.out.println(isReg + "test");

        if ((isReg == true) && (isImpsServiceAllowed == true)) {
            System.out.println(isReg + "true");
            Toast.makeText(getActivity().getApplicationContext(), "true", Toast.LENGTH_LONG).show();
            showRetrieveBillFields();
            getBeneficiaryList();
        } else if ((isReg == false) && (isImpsServiceAllowed == false)) {
            System.out.println(isReg + "False");
            Toast.makeText(getActivity().getApplicationContext(), "false", Toast.LENGTH_LONG).show();
            showCustomRegistrationFields();
        }
        // taskCompleted(result);
    }


    @Override
    public void onTaskError(AsyncResult pResult, DataExImpl.Methods callback) {

    }

    public class OperatorSelectedListener implements AdapterView.OnItemSelectedListener{
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }


        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            String sConsumerNumber          = getResources().getString(R.string.lblConsumerNumber);

            String sHintDisplay             = sConsumerNumber;

            showBillMessage("");

            String sOperator                = _spOperator.getSelectedItem().toString();

            Log.d(_LOG, "Operator selected: " + sOperator);
      //      String sOperatorId              = getOperatorId(sOperator);
//            Log.d(_LOG, "Operator selectedId: " + sOperatorId);
//
//            if(sOperatorId == null){
//                Log.d(_LOG, "Could not find operator id, doing nothing");
//                return;
//            }
//
//            hideRetrieveBillFields();
//
//                 if( sOperatorId.equals("CES")){
//                sHintDisplay                = sConsumerNumber;
//                showRetrieveBillFields();
//            }




            _etSubscriberId.setHint(sHintDisplay);
        }
    }

   }
