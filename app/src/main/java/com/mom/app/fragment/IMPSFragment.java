package com.mom.app.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mom.app.R;
import com.mom.app.identifier.PlatformIdentifier;
import com.mom.app.identifier.TransactionType;
import com.mom.app.model.AsyncListener;
import com.mom.app.model.AsyncResult;
import com.mom.app.model.DataExImpl;
import com.mom.app.model.IDataEx;
import com.mom.app.model.pbxpl.BeneficiaryResult;
import com.mom.app.model.pbxpl.ImpsCustomerRegistrationResult;
import com.mom.app.ui.TransactionRequest;
import com.mom.app.utils.AppConstants;

import java.util.Calendar;
<<<<<<< HEAD
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
=======
>>>>>>> 198c7f77c4fc6efc3e62827ca177688d5171d5c7
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IMPSFragment extends FragmentBase implements AsyncListener<TransactionRequest> {

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

    public static IMPSFragment newInstance(PlatformIdentifier currentPlatform) {
        IMPSFragment fragment = new IMPSFragment();
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

        _btnSubmit                  = (Button) view.findViewById(R.id.BTN_Submit);
        _btnNext                    = (Button) view.findViewById(R.id.btn_Submit);
        _btnCancel                  = (Button) view.findViewById(R.id.btnCancel);
        _btnOTPResend               = (Button) view.findViewById(R.id.btn_OTPResend);
        _btnNewRecharge             = (Button) view.findViewById(R.id._btn_newRecharge);
        _btnGetBillAmount           = (Button) view.findViewById(R.id.btnGetBillAmount);
        _btnImpsCreateCustomer      = (Button) view.findViewById(R.id.btn_impsCreateCustomer);
        _btnImpsCancel              = (Button) view.findViewById(R.id.btn_ImpsCancel);
        _billMsgDisplay             = (TextView) view.findViewById(R.id.msgDisplay);
        _spOperator                 = (Spinner) view.findViewById(R.id.Operator);
        _splOperatorNBE             = (Spinner) view.findViewById(R.id.Spl_OperatorNBE);
        _OperatorPayFrom            = (Spinner) view.findViewById(R.id.Operator_PayFrom);
        _etSubscriberId             = (EditText) view.findViewById(R.id.subscriberId);
        _etAmount                   = (EditText) view.findViewById(R.id.amount);
        _etConsumerNumber           = (EditText) view.findViewById(R.id.ConsumerNumber);
        _etBeneficiaryMobile_Number = (EditText) view.findViewById(R.id.BeneficiaryMobile_number);
        _etAvailableLimit           = (EditText) view.findViewById(R.id.availableLimit);
        _etIFSC_Code                = (EditText) view.findViewById(R.id.IFSC_Code);
        _etProcessingFees           = (EditText) view.findViewById(R.id.ProcessingFees);
        _etAmountPayable            = (EditText) view.findViewById(R.id.AmountPayable);
        _etOTP                      = (EditText) view.findViewById(R.id.OTP);
        _etConsumerName             = (EditText) view.findViewById(R.id.et_name);
        _etDob                      = (EditText) view.findViewById(R.id.et_dob);
        _etEmailAddress             = (EditText) view.findViewById(R.id.et_emailId);
        _tvGetIFSC_List             = (TextView) view.findViewById(R.id.GetIFSC_List);
        _btnPay                     = (Button) view.findViewById(R.id.btn_Pay);
        _etDueDate                  = (EditText) view.findViewById(R.id.DueDate);
        _etSdCode                   = (EditText) view.findViewById(R.id.SDCode);
        _etSop                      = (EditText) view.findViewById(R.id.SOP);
        _etFsa                      = (EditText) view.findViewById(R.id.FSA);
        _etAcMonth                  = (EditText) view.findViewById(R.id.ACMonth);
        _etBeneficiaryName          = (EditText) view.findViewById(R.id.beneficiary_name);
        _etLastName                 = (EditText) view.findViewById(R.id.last_name);
        _etAccountNumber            = (EditText) view.findViewById(R.id.account_number);
        _rbPay                      = (RadioButton) view.findViewById(R.id.rbtnPay);
        _rbVerify                   = (RadioButton) view.findViewById(R.id.rbtn_Verify);
        cal                         = Calendar.getInstance();
        day                         = cal.get(Calendar.DAY_OF_MONTH);
        month                       = cal.get(Calendar.MONTH);
        year                        = cal.get(Calendar.YEAR);

        _btnGetBillAmount.setVisibility(View.GONE);
        _splOperatorNBE.setVisibility(View.GONE);
<<<<<<< HEAD
=======

>>>>>>> 198c7f77c4fc6efc3e62827ca177688d5171d5c7
        _tvGetIFSC_List.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(), "Its working", Toast.LENGTH_LONG).show();
            }
        });

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
<<<<<<< HEAD


=======
>>>>>>> 198c7f77c4fc6efc3e62827ca177688d5171d5c7
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
<<<<<<< HEAD
                int nMinPhoneLength = 10;
                int nMaxPhoneLength = 10;
                int nPhoneLength = _etConsumerNumber.getText().toString().length();
                if (nPhoneLength < nMinPhoneLength || nPhoneLength > nMaxPhoneLength) {
                    if (nMinPhoneLength == nMaxPhoneLength) {
                        _etConsumerNumber.setText("");
                        showMessage(String.format(getResources().getString(R.string.error_phone_length), nMinPhoneLength));
                    } else {
                        _etConsumerNumber.setText("");
                        showMessage(String.format(getResources().getString(R.string.error_phone_length_min_max), nMinPhoneLength, nMaxPhoneLength));
                    }
                    return;
                }

                consumerRegistrationStatus();

                }
=======
                int nMinPhoneLength     = 10;
                int nMaxPhoneLength     = 10;
                int nPhoneLength        = _etConsumerNumber.getText().toString().length();
                if(nPhoneLength < nMinPhoneLength || nPhoneLength > nMaxPhoneLength){
                    _etConsumerNumber.setText("");
                    showMessage(String.format(getResources().getString(R.string.error_phone_length_min_max), nMinPhoneLength, nMaxPhoneLength));
                    return;
                }

                consumerRegistration();
            }
>>>>>>> 198c7f77c4fc6efc3e62827ca177688d5171d5c7
        });

        _btnImpsCreateCustomer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

<<<<<<< HEAD
                if (validate() == 0) {
                    createConsumerRegistration();
                    Log.i(_LOG, "Async Registration request sent");
                } else {
                    switch (validate()) {


                        case 1:

                            showMessage(getResources().getString(R.string.prompt_Validity_mobile_number));
                            _etConsumerNumber.setText("");
                            break;
=======
        _etDob.setOnTouchListener(new View.OnTouchListener() {
>>>>>>> 198c7f77c4fc6efc3e62827ca177688d5171d5c7


<<<<<<< HEAD
                        case 2:


                            showMessage(getString(R.string.prompt_Validity_Name));
                            _etConsumerName.setText("");
                            break;

                        case 3:


                            showMessage(getString(R.string.prompt_Validity_DOB));
                            _etDob.setText("");
                            break;

                        case 4:


                            showMessage(getString(R.string.prompt_Validity_Email_Address));
                            _etEmailAddress.setText("");
                            break;


                    }
                }
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


        return view;
    }


    private void consumerRegistrationStatus() {
        showMessage(null);

        String sConsumerNumber = _etConsumerNumber.getText().toString().trim();
        IDataEx dataEx = getDataEx(new AsyncListener<TransactionRequest>() {
            public void onTaskSuccess(TransactionRequest result, DataExImpl.Methods callback) {
                Log.d(_LOG, "Called back");
                switch (callback) {
                    case IMPS_CUSTOMER_REGISTRATION_STATUS:
                        if (result == null) {
                            Log.d(_LOG, "Obtained NULL bill payment response");
                            showMessage(getResources().getString(R.string.error_recharge_failed));
                            return;
                        }
                        showBalance();
                        Log.d(_LOG, "Starting navigation to TxnMsg Activity");

                        break;
                }
                Log.i("ResultChange", result.getRemoteResponse() + result.getResponseCode());
                showMessage(result.getRemoteResponse());
                Boolean isReg = EphemeralStorage.getInstance(getActivity().getApplicationContext()).getBoolean(
                        AppConstants.PARAM_PBX_IMPS_ISREGISTERED, false);
                Boolean isImpsServiceAllowed = EphemeralStorage.getInstance(getActivity().getApplicationContext()).getBoolean(
                        AppConstants.PARAM_PBX_IMPS_SERVICEALLOWED, false);

                System.out.println(isReg + "test");

                if ((isReg == true) && (isImpsServiceAllowed == true)) {
                    System.out.println(isReg + "true");
                    Toast.makeText(getActivity().getApplicationContext(), "true", Toast.LENGTH_LONG).show();
                    hideMessage();
                    showRetrieveBillFields();
                    //getBeneficiaryList();
                } else if ((isReg == false) && (isImpsServiceAllowed == false)) {
//                    System.out.println(isReg + "False");
//                    Toast.makeText(getActivity().getApplicationContext(), "false", Toast.LENGTH_LONG).show();
                    hideMessage();
                    showCustomRegistrationFields();
                } else if ((isReg == false) && (isImpsServiceAllowed == true)) {
//                    System.out.println(isReg + "False");
//                    Toast.makeText(getActivity().getApplicationContext(), "false", Toast.LENGTH_LONG).show();
                    showCustomRegistrationFields();
                } else if ((isReg == true) && (isImpsServiceAllowed == false)) {
//                    System.out.println(isImpsServiceAllowed + "False");
                    hideMessage();
//                    Toast.makeText(getActivity().getApplicationContext(), "false", Toast.LENGTH_LONG).show();
                    showMessage("You are not allowed to perform IMPS");
                }

            }

            @Override
            public void onTaskError(AsyncResult pResult, DataExImpl.Methods callback) {

            }
        });

        TransactionRequest request = new TransactionRequest(
=======
                }
                return false;
            }
        });

        return view;
    }

    private void consumerRegistration() {
        showMessage(null);

        String sConsumerNumber = _etConsumerNumber.getText().toString().trim();

        TransactionRequest<ImpsCustomerRegistrationResult> request  = new TransactionRequest<ImpsCustomerRegistrationResult>(
>>>>>>> 198c7f77c4fc6efc3e62827ca177688d5171d5c7
                getActivity().getString(TransactionType.IMPS_CUSTOMER_REGISTRATION.transactionTypeStringId),
                sConsumerNumber
        );

<<<<<<< HEAD
        getDataEx(this).impsCustomerRegistrationStatus(sConsumerNumber);
=======
        getDataEx(this).registerIMPSCustomer(request);
>>>>>>> 198c7f77c4fc6efc3e62827ca177688d5171d5c7

        showProgress(true);
    }

    private void getBeneficiaryList() {
        showMessage(null);

        String sConsumerNumber = _etConsumerNumber.getText().toString().trim();
<<<<<<< HEAD
        IDataEx dataEx         = getDataEx(new AsyncListener<ArrayList<Transaction>>() {

            public void onTaskSuccess(ArrayList<Transaction> result, DataExImpl.Methods callback) {
                switch (callback) {
                    case IMPS_BENEFICIARY_LIST:
                        Log.i(_LOG, "Check Response: " + result);

                }

            }

            @Override
            public void onTaskError(AsyncResult pResult, DataExImpl.Methods callback) {

            }
        });
=======
>>>>>>> 198c7f77c4fc6efc3e62827ca177688d5171d5c7

        IDataEx dataEx = getDataEx(this);

<<<<<<< HEAD
=======
        TransactionRequest<List<BeneficiaryResult>> request  = new TransactionRequest<List<BeneficiaryResult>>(
                getActivity().getString(TransactionType.IMPS_BENEFICIARY_LIST.transactionTypeStringId),
                sConsumerNumber
        );

>>>>>>> 198c7f77c4fc6efc3e62827ca177688d5171d5c7
        try {
            dataEx.getIMPSBeneficiaryList(request);
            showProgress(true);
        } catch (Exception me) {
            Log.e(_LOG, "Error getting dataex", me);

        }
<<<<<<< HEAD


    }

    private int validate() {
        if        (_etConsumerNumber.getText().toString().length() < 10) {
            return 1;
        } else if (_etConsumerNumber.getText().toString().length() > 10) {
            return 1;
        } else if (_etConsumerName.getText().toString().length() == 0) {
            return 2;
        } else if (_etDob.getText().toString().length() == 0) {
            return 3;
        } else if ((isEmailValid(_etEmailAddress.getText().toString())) == 0) {
            return 4;
        } else {
            return 0;
        }
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

    private void createConsumerRegistration() {
        showMessage(null);
        String sConsumerNumber = _etConsumerNumber.getText().toString().trim();
        String sConsumerName   = _etConsumerName.getText().toString().trim();
        String sConsumerDOB    = _etDob.getText().toString();
        String sConsumerEmailAddress = _etEmailAddress.getText().toString();

        getDataEx(this).impsCustomerRegistration(sConsumerNumber, sConsumerName, sConsumerDOB, sConsumerEmailAddress);

    }


=======
    }

    private void createConsumerRegistration(){
     showMessage(null);
 }

>>>>>>> 198c7f77c4fc6efc3e62827ca177688d5171d5c7
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

            showDate(arg1, arg2 + 1, arg3);
        }
    };

    private void showDate(int year, int month, int day) {

        _etDob.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
<<<<<<< HEAD
        Calendar userAge = new GregorianCalendar(year, month, day);
        Calendar minAdultAge = new GregorianCalendar();
        minAdultAge.add(Calendar.YEAR, -18);


        if (minAdultAge.before(userAge)) {

            showMessage(getResources().getString(R.string.prompt_Validity_DOB_Age));
        } else {
            hideMessage();
        }

    }

    public void showRetrieveBillNextFields() {
=======
    }

    public void showRetrieveBillNextFields(){
>>>>>>> 198c7f77c4fc6efc3e62827ca177688d5171d5c7

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

<<<<<<< HEAD
    public void showRetrieveBillFields() {


=======
    public void setupIMPSTransferView(){
>>>>>>> 198c7f77c4fc6efc3e62827ca177688d5171d5c7
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
<<<<<<< HEAD


=======
>>>>>>> 198c7f77c4fc6efc3e62827ca177688d5171d5c7
    }

    public void hideRetrieveBillFields() {
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


    @Override
    public void onTaskSuccess(TransactionRequest result, DataExImpl.Methods callback) {
        Log.d(_LOG, "In onTaskSuccess");

        switch (callback){
            case IMPS_CUSTOMER_REGISTRATION:
                Log.i(_LOG, result.getRemoteResponse() + result.getResponseCode());

                if(!(result.getCustom() instanceof ImpsCustomerRegistrationResult)){
                    Log.d(_LOG, "response is in incorrect format!");
                    showMessage(getActivity().getString(R.string.error_imps_customer_registration));
                    return;
                }

                ImpsCustomerRegistrationResult registrationResult   = (ImpsCustomerRegistrationResult) result.getCustom();

                showMessage(result.getRemoteResponse());

                if (registrationResult.isRegistered && registrationResult.isIMPSServiceAllowed) {
                    Log.d(_LOG, "Both true");
                    setupIMPSTransferView();
                    //getBeneficiaryList();
                } else if (!registrationResult.isRegistered && !registrationResult.isIMPSServiceAllowed) {
                    Log.d(_LOG, "Both false");
                    showCustomRegistrationFields();
                }else if (!registrationResult.isRegistered && registrationResult.isIMPSServiceAllowed) {
                    Log.d(_LOG, "not registered but service allowed");
                    showCustomRegistrationFields();
                }else if (registrationResult.isRegistered && !registrationResult.isIMPSServiceAllowed) {
                    Log.d(_LOG, "customer already registered but imps not allowed");
                    showMessage("You are not allowed to perform IMPS");
                }
                break;
            case IMPS_BENEFICIARY_LIST:
                break;
        }

<<<<<<< HEAD
=======
        Log.d(_LOG, "onTaskSuccess done");
    }

    @Override
    public void onTaskError(AsyncResult pResult, DataExImpl.Methods callback) {
        Log.d(_LOG, "In onTaskError");
        switch (callback){
            case IMPS_CUSTOMER_REGISTRATION:
                showMessage(getActivity().getString(R.string.error_imps_customer_registration));
                break;
            case IMPS_BENEFICIARY_LIST:
                break;
        }
        Log.d(_LOG, "onTaskError done");
    }
>>>>>>> 198c7f77c4fc6efc3e62827ca177688d5171d5c7
}
