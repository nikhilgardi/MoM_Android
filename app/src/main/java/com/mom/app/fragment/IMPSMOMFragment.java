package com.mom.app.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mom.app.R;
import com.mom.app.identifier.IdentifierUtils;
import com.mom.app.identifier.PlatformIdentifier;
import com.mom.app.identifier.TransactionType;
import com.mom.app.model.AsyncListener;
import com.mom.app.model.AsyncResult;
import com.mom.app.model.DataExImpl;
import com.mom.app.model.IDataEx;
import com.mom.app.model.local.EphemeralStorage;
import com.mom.app.model.pbxpl.BankNameResult;
import com.mom.app.model.pbxpl.BeneficiaryResult;
import com.mom.app.model.pbxpl.BranchNameResult;
import com.mom.app.model.pbxpl.CityNameResult;
import com.mom.app.model.pbxpl.ImpsAddBeneficiaryResult;
import com.mom.app.model.pbxpl.ImpsAuthenticationResult;
import com.mom.app.model.pbxpl.ImpsBeneficiaryDetailsResult;
import com.mom.app.model.pbxpl.ImpsCheckKYCResult;
import com.mom.app.model.pbxpl.ImpsConfirmPaymentResult;
import com.mom.app.model.pbxpl.ImpsCreateCustomerResult;
import com.mom.app.model.pbxpl.ImpsCustomerRegistrationResult;
import com.mom.app.model.pbxpl.ImpsPaymentProcessResult;
import com.mom.app.model.pbxpl.ImpsVerifyPaymentResult;
import com.mom.app.model.pbxpl.ImpsVerifyProcessResult;
import com.mom.app.model.pbxpl.StateNameResult;
import com.mom.app.ui.TransactionRequest;
import com.mom.app.utils.AppConstants;
import com.mom.app.widget.ConfirmPaymentTextListViewAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IMPSMOMFragment extends FragmentBase implements AsyncListener<TransactionRequest> {

    private String _LOG = AppConstants.LOG_PREFIX + "MOMIMPS";

    Button _btnGetBillAmount;
    private EditText _etSubscriberId;
    private EditText _etAmount;
    private EditText _etTxnDescription;
    private EditText _etAvailableLimit;
    private EditText _etConsumerNumber;
    private EditText _etIFSC_Code;
    private EditText _etBeneficiaryMobile_Number;
    private TextView _tvGetIFSC_List;
    private TextView _tvFullKYC;
    private TextView _tvNILLKYC;
    private TextView _tvVerified;
    private TextView _lblBeneficiaryName;
    private TextView _lblAccountNumber;
    private TextView _lblIFSCCode;
    private TextView _lblBeneficiaryMobileNumber;
    private TextView _tvBeneficiaryName;
    private TextView _tvNoTrans , _titleView;
    private TextView _tvAccountNumber;
    private TextView _tvIFSCCode;
    private TextView _tvBeneficiaryMobileNumber;
    private TextView _tv_availableLimit;
    private TextView _tv_operator;
    private TextView _tv_beneficiary_name;
    private TextView _tv_account_number;
    private TextView _tv_IFSC_Code;
    private TextView _tv_BeneficiaryMobile_number;
    private TextView _tv_Spinner_PayFrom;
    private TextView _tv_amount;
    private TextView _tv_txnDescription;
    private TextView _tv_ProcessingFees;
    private TextView _tv_AmountPayable;
    private TextView _tv_OTP;
    private TextView _tv_consumerNumber;
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
    private Button _btnVerifySubmit;
    private Button _btnVerifyCancel;
    private Button _btnPaymentPaySubmit;
    private Button _btnPaymentPayCancel;
    private Button _btnVerifyPaySubmit;
    private Button _btnVerifyPayCancel;
    private Button _btnOTPResend;
    private Button _btnNewRecharge;
    private Button _btnImpsCreateCustomer;
    private Button _btnImpsCancel;
    private ListView _listView;
    private int day;
    private int month;
    private int year;
    private Calendar cal;
    private RadioGroup _rGrp;
    private RadioButton _rbPay, _rbVerify;
    String formatDueDate;
    String sBranchIfscCode;
    Spinner _spPayFrom;
    Spinner _spOperator;
    Spinner _spOperator1;
    Spinner _spOperator2;
    Spinner _spOperator3;
    Spinner _spOperator4;

    Spinner _splOperatorNBE;
    private Boolean isAgeValid = true;




    public static IMPSMOMFragment newInstance(PlatformIdentifier currentPlatform) {
        IMPSMOMFragment fragment = new IMPSMOMFragment();
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
        setCurrentPlatform();
        _currentPlatform            = IdentifierUtils.getPlatformIdentifier(getActivity());
        _titleView                  = (TextView) view.findViewById(R.id.transactionHistoryHeader);
        _listView                   = (ListView) view.findViewById(R.id.listView);
        _tvNoTrans                  = (TextView) view.findViewById(R.id.noTransactionsMsg);
        _btnSubmit                  = (Button) view.findViewById(R.id.BTN_Submit);
        _btnNext                    = (Button) view.findViewById(R.id.btn_Submit);
        _btnCancel                  = (Button) view.findViewById(R.id.btnCancel);
        _btnVerifySubmit            = (Button) view.findViewById(R.id.btn_VerifySubmit);
        _btnVerifyCancel            = (Button) view.findViewById(R.id.btnVerifyCancel);
        _btnVerifyPaySubmit         = (Button) view.findViewById(R.id.btn_VerifyPaySubmit);
        _btnVerifyPayCancel         = (Button) view.findViewById(R.id.btn_VerifyPayCancel);
        _btnPaymentPaySubmit        = (Button) view.findViewById(R.id.btn_PaymentPaySubmit);
        _btnPaymentPayCancel        = (Button) view.findViewById(R.id.btn_PaymentPayCancel);
        _btnOTPResend               = (Button) view.findViewById(R.id.btn_IPINResend);
        _btnNewRecharge             = (Button) view.findViewById(R.id._btn_newRecharge);
        _btnGetBillAmount           = (Button) view.findViewById(R.id.btnGetBillAmount);
        _btnImpsCreateCustomer      = (Button) view.findViewById(R.id.btn_impsCreateCustomer);
        _btnImpsCancel              = (Button) view.findViewById(R.id.btn_ImpsCancel);
        _billMsgDisplay             = (TextView) view.findViewById(R.id.msgDisplay);
        _spOperator                 = (Spinner) view.findViewById(R.id.Operator);
        _spPayFrom                  = (Spinner) view.findViewById(R.id.spinner_PayFrom);
        _splOperatorNBE             = (Spinner) view.findViewById(R.id.Spl_OperatorNBE);

        _etSubscriberId             = (EditText) view.findViewById(R.id.subscriberId);
        _etAmount                   = (EditText) view.findViewById(R.id.amount);
        _etTxnDescription           = (EditText) view.findViewById(R.id.txnDescription);
        _etConsumerNumber           = (EditText) view.findViewById(R.id.ConsumerNumber);
        _etBeneficiaryMobile_Number = (EditText) view.findViewById(R.id.BeneficiaryMobile_number);
        _etAvailableLimit           = (EditText) view.findViewById(R.id.availableLimit);
        _etIFSC_Code                = (EditText) view.findViewById(R.id.IFSC_Code);
        _etProcessingFees           = (EditText) view.findViewById(R.id.ProcessingFees);
        _etAmountPayable            = (EditText) view.findViewById(R.id.AmountPayable);
        _etOTP                      = (EditText) view.findViewById(R.id.IPin);
        _etConsumerName             = (EditText) view.findViewById(R.id.et_name);
        _etDob                      = (EditText) view.findViewById(R.id.et_dob);
        _etEmailAddress             = (EditText) view.findViewById(R.id.et_emailId);
        _tvGetIFSC_List             = (TextView) view.findViewById(R.id.GetIFSC_List);
        _tvFullKYC                  = (TextView) view.findViewById(R.id.GetFullKYC);
        _tvNILLKYC                  = (TextView) view.findViewById(R.id.GetNillKYC);
        _tvVerified                 = (TextView) view.findViewById(R.id.Verified);
        _tv_availableLimit          = (TextView) view.findViewById(R.id.tv_availableLimit);
        _tv_operator                = (TextView) view.findViewById(R.id.tv_operator);
        _tv_beneficiary_name        = (TextView) view.findViewById(R.id.tv_beneficiary_name);
        _tv_account_number          = (TextView) view.findViewById(R.id.tv_account_number);
        _tv_IFSC_Code               = (TextView) view.findViewById(R.id.tv_IFSC_Code);
        _tv_BeneficiaryMobile_number= (TextView) view.findViewById(R.id.tv_BeneficiaryMobile_number);
        _tv_Spinner_PayFrom         = (TextView) view.findViewById(R.id.tv_spinner_PayFrom);
        _tv_amount                  = (TextView) view.findViewById(R.id.tv_amount);
        _tv_txnDescription          = (TextView) view.findViewById(R.id.tv_txnDescription);
        _tv_ProcessingFees          = (TextView) view.findViewById(R.id.tv_ProcessingFees);
        _tv_AmountPayable           = (TextView) view.findViewById(R.id.tv_AmountPayable);
        _tv_OTP                     = (TextView) view.findViewById(R.id.tv_IPin);
        _tv_consumerNumber          = (TextView) view.findViewById(R.id.tv_consumerNumber);
        _btnPay                     = (Button) view.findViewById(R.id.btn_Pay);
        _etDueDate                  = (EditText) view.findViewById(R.id.DueDate);
        _etSdCode                   = (EditText) view.findViewById(R.id.SDCode);
        _etSop                      = (EditText) view.findViewById(R.id.SOP);
        _etFsa                      = (EditText) view.findViewById(R.id.FSA);
        _etAcMonth                  = (EditText) view.findViewById(R.id.ACMonth);
        _etBeneficiaryName          = (EditText) view.findViewById(R.id.beneficiary_name);
        _etLastName                 = (EditText) view.findViewById(R.id.last_name);
        _etAccountNumber            = (EditText) view.findViewById(R.id.account_number);
        _rGrp                       = (RadioGroup) view.findViewById(R.id.radioGroup);
        _rbPay                      = (RadioButton) view.findViewById(R.id.rbtnPay);
        _rbVerify                   = (RadioButton) view.findViewById(R.id.rbtn_Verify);
         cal                        = Calendar.getInstance();
        day                         = cal.get(Calendar.DAY_OF_MONTH);
        month                       = cal.get(Calendar.MONTH);
        year                        = cal.get(Calendar.YEAR);
        impsAuthentication();
        _etBeneficiaryName.setEnabled(true);
        _etAccountNumber.setEnabled(true);
        _etIFSC_Code.setEnabled(true);
        _etBeneficiaryMobile_Number.setEnabled(true);
       // _btnSubmit.setVisibility(view.VISIBLE);
        _titleView.setVisibility(view.GONE);


        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        System.out.println("Current time => "+c.getTime());
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String formattedDate = df.format(c.getTime());
        Log.i("Date Time: ", formattedDate);

        _rbPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _rbPay.setChecked(true);
                if((_spOperator.getSelectedItemPosition()== 0) && (_rbPay.isChecked())){

                    _etBeneficiaryName.setEnabled(true);
                    _etAccountNumber.setEnabled(true);
                    _etIFSC_Code.setEnabled(true);
                    _etBeneficiaryMobile_Number.setEnabled(true);

                    _etTxnDescription.setText(null);
                    _etAmount.setText(null);
                    _tv_amount.setVisibility(View.VISIBLE);
                    _tv_txnDescription.setVisibility(View.VISIBLE);
                    _etAmount.setVisibility(View.VISIBLE);
                    _etTxnDescription.setVisibility(View.VISIBLE);
                    setupIMPSTransferView();

                }
               else  if((_spOperator.getSelectedItemPosition()!= 0) && (_rbPay.isChecked())){
                    _etBeneficiaryName.setEnabled(true);
                    _etAccountNumber.setEnabled(true);
                    _etIFSC_Code.setEnabled(true);
                    _etBeneficiaryMobile_Number.setEnabled(true);
                    _etTxnDescription.setText(null);
                    _etAmount.setText(null);
                    _etAmount.setEnabled(true);
                    _tv_amount.setVisibility(View.VISIBLE);
                    _tv_txnDescription.setVisibility(View.VISIBLE);
                    _etAmount.setVisibility(View.VISIBLE);
                    _etTxnDescription.setVisibility(View.VISIBLE);
                    setupIMPSTransferPayView();

                }

               }
        });

        _rbVerify.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                _rbVerify.setChecked(true);
                if ((_spOperator.getSelectedItemPosition() == 0) && (_rbVerify.isChecked())) {
                    _etBeneficiaryName.setEnabled(true);
                    _etAccountNumber.setEnabled(true);
                    _etIFSC_Code.setEnabled(true);
                    _etBeneficiaryMobile_Number.setEnabled(true);


                }

                      setIMPSVerifyView();
                       }
        });

        _tvGetIFSC_List.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showAlert();

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
                showMessage(null);
                _listView.setVisibility(View.GONE);
                _etConsumerNumber.setText(null);
                _etConsumerNumber.setEnabled(true);
                _btnNewRecharge.setVisibility(View.GONE);
                _etAmount.setVisibility(View.GONE);
                _etProcessingFees.setVisibility(View.GONE);
                _etAmountPayable.setVisibility(View.GONE);
                _btnSubmit.setVisibility(View.VISIBLE);
                _etConsumerNumber.setVisibility(View.VISIBLE);

            }
        });

        _btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgress(false);
                showMessage(null);
                _btnVerifyPaySubmit.setEnabled(true);
                _btnPaymentPaySubmit.setEnabled(true);
                validate(view);
            }
        });


        _btnVerifyPaySubmit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                _btnVerifyPaySubmit.setEnabled(false);
                showProgress(false);
                if ("".equals(_etOTP.getText().toString().trim())) {
                    showMessage(getResources().getString(R.string.error_otp));

                    return;
                }
              //  getIMPSVerifyPayment();
                 getMOMIMPSVerifyPayment();
            }
        });
        _btnPaymentPaySubmit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(_etOTP.getText().toString().equals("")){
                    showMessage(getResources().getString(R.string.error_otp));
                    return;
                }

                _btnPaymentPaySubmit.setEnabled(false);
                showProgress(false);
                _btnNext.setVisibility(View.GONE);

                _tvVerified.setVisibility(View.GONE);
             //   getIMPSConfirmPayment();
                if(_spPayFrom.getSelectedItemPosition()!=0){
                    getMOMIMPSPaymentByRefund();
                }
                else {
                    getMOMIMPSConfirmPayment();
                }
            }
        });

        _btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                int nMinPhoneLength = 10;
                int nMaxPhoneLength = 10;
                int nPhoneLength = _etConsumerNumber.getText().toString().length();
                if (nPhoneLength < nMinPhoneLength || nPhoneLength > nMaxPhoneLength)
                {
                    if(nMinPhoneLength == nMaxPhoneLength){
                        showMessage(String.format(getResources().getString(R.string.error_phone_length), nMinPhoneLength));
                    }else{
                        showMessage(String.format(getResources().getString(R.string.error_phone_length_min_max), nMinPhoneLength, nMaxPhoneLength));
                    }
                    return;
                }

                _etConsumerNumber.setEnabled(false);
                _etAvailableLimit.setEnabled(false);


                showProgress(true);
                consumerRegistration();

            }

        });

        _btnImpsCreateCustomer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                validateCustomerCreation(view);


            }

        });

        _btnImpsCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                hideCustomRegistrationFields();

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



        _btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                showMessage(null);
                _etConsumerNumber.setText(null);
                _etConsumerNumber.setEnabled(true);
                _btnSubmit.setEnabled(true);
                _btnSubmit.setVisibility(View.VISIBLE);
                _etConsumerNumber.setVisibility(View.VISIBLE);

                _etAvailableLimit.setVisibility(View.GONE);
                _spOperator.setVisibility(View.GONE);
                _spPayFrom.setVisibility(View.GONE);
                _tvVerified.setVisibility(View.GONE);
                _tv_availableLimit.setVisibility(View.GONE);
                _tv_operator.setVisibility(View.GONE);
                _tv_beneficiary_name.setVisibility(View.GONE);
                _tv_account_number.setVisibility(View.GONE);
                _tv_consumerNumber.setVisibility(View.GONE);
                _tv_amount.setVisibility(View.GONE);
                _tv_txnDescription.setVisibility(View.GONE);
                _tv_IFSC_Code.setVisibility(View.GONE);
                _tv_BeneficiaryMobile_number.setVisibility(View.GONE);
                _tv_Spinner_PayFrom.setVisibility(View.GONE);
                _etBeneficiaryName.setVisibility(View.GONE);
                _etAccountNumber.setVisibility(View.GONE);
                _etIFSC_Code.setVisibility(View.GONE);
                _tvGetIFSC_List.setVisibility(View.GONE);
                _tvFullKYC.setVisibility(View.GONE);
                _tvNILLKYC.setVisibility(View.GONE);
                _etBeneficiaryMobile_Number.setVisibility(View.GONE);

                _etTxnDescription.setVisibility(View.GONE);
                _etAmount.setVisibility(View.GONE);
                _rbPay.setVisibility(View.GONE);
                _rbVerify.setVisibility(View.GONE);
                _btnNext.setVisibility(View.GONE);
                _btnCancel.setVisibility(View.GONE);
                _btnVerifySubmit.setVisibility(View.GONE);
                _btnVerifyCancel.setVisibility(View.GONE);


            }

        });
//
        _btnVerifyPayCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
        Boolean isVerified = EphemeralStorage.getInstance(getActivity().getApplicationContext()).getBoolean(
                        AppConstants.PARAM_PBX_IMPS_ISBENEFICIAYSTATUS, false);
                   showMessage(null);
                         // setupIMPSTransferView();
                if ((isVerified == true)) {
                    _btnNext.setEnabled(true);
                    _tvVerified.setVisibility(View.VISIBLE);
                    _etAmount.setEnabled(true);
                    _etBeneficiaryName.setEnabled(true);
                    _etAccountNumber.setEnabled(true);
                    _etIFSC_Code.setEnabled(true);
                    _etBeneficiaryMobile_Number.setEnabled(true);
                    _btnSubmit.setVisibility(View.GONE);
                    _etConsumerNumber.setVisibility(View.VISIBLE);
                    _etAvailableLimit.setVisibility(View.VISIBLE);
                    _spOperator.setVisibility(View.VISIBLE);

                    _etBeneficiaryName.setVisibility(View.VISIBLE);
                    _etAccountNumber.setVisibility(View.VISIBLE);
                    _etIFSC_Code.setVisibility(View.VISIBLE);
                    _tvGetIFSC_List.setVisibility(View.VISIBLE);
                    _etBeneficiaryMobile_Number.setVisibility(View.VISIBLE);

                    _etTxnDescription.setVisibility(View.VISIBLE);
                    _etAmount.setVisibility(View.VISIBLE);
                    _rbPay.setVisibility(View.VISIBLE);
                    _rbVerify.setVisibility(View.GONE);
                    _btnNext.setVisibility(View.VISIBLE);
                    _btnCancel.setVisibility(View.VISIBLE);
                    _btnVerifySubmit.setVisibility(View.GONE);
                    _btnVerifyCancel.setVisibility(View.GONE);
                    _btnVerifyPaySubmit.setVisibility(View.GONE);
                    _btnVerifyPayCancel.setVisibility(View.GONE);
                    _etAmountPayable.setVisibility(View.GONE);
                    _etOTP.setVisibility(View.GONE);
                    _tv_availableLimit.setVisibility(View.VISIBLE);
                    _tv_operator.setVisibility(View.VISIBLE);
                    _tv_beneficiary_name.setVisibility(View.VISIBLE);
                    _tv_account_number.setVisibility(View.VISIBLE);
                    _tv_IFSC_Code.setVisibility(View.VISIBLE);
                    _tv_BeneficiaryMobile_number.setVisibility(View.VISIBLE);

                    _tv_consumerNumber.setVisibility(View.VISIBLE);
                    _tv_amount.setVisibility(View.VISIBLE);
                    _tv_txnDescription.setVisibility(View.VISIBLE);
                    _tv_ProcessingFees.setVisibility(View.GONE);
                    _tv_AmountPayable.setVisibility(View.GONE);
                    _tv_OTP.setVisibility(View.GONE);
                    _btnOTPResend.setVisibility(View.GONE);

                   // Toast.makeText(getActivity().getApplicationContext(), "true", Toast.LENGTH_LONG).show();
                }
                else if(isVerified == false){
                    _btnNext.setEnabled(true);
                    _tvVerified.setVisibility(View.GONE);
                    _etAmount.setEnabled(true);
                    _etBeneficiaryName.setEnabled(true);
                    _etAccountNumber.setEnabled(true);
                    _etIFSC_Code.setEnabled(true);
                    _etBeneficiaryMobile_Number.setEnabled(true);
                    _btnSubmit.setVisibility(View.GONE);
                    _etConsumerNumber.setVisibility(View.VISIBLE);
                    _etAvailableLimit.setVisibility(View.VISIBLE);
                    _spOperator.setVisibility(View.VISIBLE);
                    _etBeneficiaryName.setVisibility(View.VISIBLE);
                    _etAccountNumber.setVisibility(View.VISIBLE);
                    _etIFSC_Code.setVisibility(View.VISIBLE);
                    _tvGetIFSC_List.setVisibility(View.VISIBLE);
                    _etBeneficiaryMobile_Number.setVisibility(View.VISIBLE);

                    _etTxnDescription.setVisibility(View.GONE);
                    _etAmount.setVisibility(View.GONE);
                    _rbPay.setVisibility(View.GONE);
                    _rbVerify.setVisibility(View.VISIBLE);
                    _btnNext.setVisibility(View.VISIBLE);
                    _btnCancel.setVisibility(View.VISIBLE);
                    _btnVerifySubmit.setVisibility(View.GONE);
                    _btnVerifyCancel.setVisibility(View.GONE);
                    _btnVerifyPaySubmit.setVisibility(View.GONE);
                    _btnVerifyPayCancel.setVisibility(View.GONE);
                    _etAmountPayable.setVisibility(View.GONE);
                    _etOTP.setVisibility(View.GONE);
                    _tv_availableLimit.setVisibility(View.VISIBLE);
                    _tv_operator.setVisibility(View.VISIBLE);
                    _tv_beneficiary_name.setVisibility(View.VISIBLE);
                    _tv_account_number.setVisibility(View.VISIBLE);
                    _tv_IFSC_Code.setVisibility(View.VISIBLE);
                    _tv_BeneficiaryMobile_number.setVisibility(View.VISIBLE);
                    _tv_consumerNumber.setVisibility(View.VISIBLE);
                    _tv_amount.setVisibility(View.GONE);
                    _tv_txnDescription.setVisibility(View.GONE);
                    _tv_ProcessingFees.setVisibility(View.GONE);
                    _tv_AmountPayable.setVisibility(View.GONE);
                    _tv_OTP.setVisibility(View.GONE);
                    _btnOTPResend.setVisibility(View.GONE);
                 //   Toast.makeText(getActivity().getApplicationContext(), "false", Toast.LENGTH_LONG).show();
                }

            }


        });

        _btnPaymentPayCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                 showMessage(null);
                _etAmount.setEnabled(true);
                _btnNext.setEnabled(true);
                _etConsumerNumber.setVisibility(View.VISIBLE);
                _etAvailableLimit.setVisibility(View.VISIBLE);
                _spOperator.setVisibility(View.VISIBLE);
                _spPayFrom.setVisibility(View.VISIBLE);
                _etBeneficiaryName.setVisibility(View.VISIBLE);
                _etAccountNumber.setVisibility(View.VISIBLE);
                _etIFSC_Code.setVisibility(View.VISIBLE);
                _etTxnDescription.setVisibility(View.VISIBLE);
                _etBeneficiaryMobile_Number.setVisibility(View.VISIBLE);

                _etAmount.setVisibility(View.VISIBLE);
                _rbPay.setVisibility(View.VISIBLE);
                _tvGetIFSC_List.setVisibility(View.VISIBLE);
                _btnNext.setVisibility(View.VISIBLE);
                _btnCancel.setVisibility(View.VISIBLE);
                _tvVerified.setVisibility(View.VISIBLE);
                _rbVerify.setVisibility(View.GONE);
                _btnVerifySubmit.setVisibility(View.GONE);
                _btnVerifyCancel.setVisibility(View.GONE);
   //new updatedUI
                _tv_consumerNumber.setVisibility(View.VISIBLE);
                _tv_amount.setVisibility(View.VISIBLE);
                _tv_availableLimit.setVisibility(View.VISIBLE);
                _tv_txnDescription.setVisibility(View.VISIBLE);
                _tv_ProcessingFees.setVisibility(View.GONE);

                _tv_operator.setVisibility(View.VISIBLE);
                _tv_beneficiary_name.setVisibility(View.VISIBLE);
                _tv_account_number.setVisibility(View.VISIBLE);
                _tv_IFSC_Code.setVisibility(View.VISIBLE);
                _tv_BeneficiaryMobile_number.setVisibility(View.VISIBLE);
                _tv_Spinner_PayFrom.setVisibility(View.VISIBLE);
                _tv_amount.setVisibility(View.VISIBLE);
                _tv_txnDescription.setVisibility(View.VISIBLE);
                _tv_ProcessingFees.setVisibility(View.GONE);
                _tv_AmountPayable.setVisibility(View.GONE);
                _tv_OTP.setVisibility(View.GONE);
                _btnPaymentPaySubmit.setVisibility(View.GONE);
                _btnPaymentPayCancel.setVisibility(View.GONE);
                _btnVerifyPayCancel.setVisibility(View.GONE);
                _btnVerifyPaySubmit.setVisibility(View.GONE);
                _etProcessingFees.setVisibility(View.GONE);
                _etAmountPayable.setVisibility(View.GONE);
                _etOTP.setVisibility(View.GONE);
                _btnOTPResend.setVisibility(View.GONE);

            }

        });

        _btnOTPResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               resetIPin();
            }
        });
                return view;
    }
    public  void validateCustomerCreation( View view) {
        if (_etConsumerName.getText().toString().length() == 0) {
            showMessage(getResources().getString(R.string.prompt_Validity_Name));
            return ;
        }else if (_etDob.getText().toString().length() == 0) {
            showMessage(getResources().getString(R.string.prompt_Validity_DOB));
            return ;
        }else if (_etDob.getText().toString().length()!= 0 && !isAgeValid ) {
            showMessage(getResources().getString(R.string.prompt_Validity_DOB_Age));
            return ;
        }else if ((isEmailValid(_etEmailAddress.getText().toString()))== 0) {
            showMessage(getResources().getString(R.string.prompt_Validity_Email_Address));
            return ;
        }


        createConsumerRegistration();
    }


    public void setIMPSVerifyView() {

        _btnSubmit.setVisibility(View.GONE);
        _etConsumerNumber.setVisibility(View.VISIBLE);
        _etAvailableLimit.setVisibility(View.VISIBLE);
        _spOperator.setVisibility(View.VISIBLE);

        _etBeneficiaryName.setVisibility(View.VISIBLE);
        _etAccountNumber.setVisibility(View.VISIBLE);
        _etIFSC_Code.setVisibility(View.VISIBLE);
        _tv_consumerNumber.setVisibility(View.VISIBLE);
        _tv_availableLimit.setVisibility(View.VISIBLE);
        _tv_beneficiary_name.setVisibility(View.VISIBLE);
        _tv_account_number.setVisibility(View.VISIBLE);
        _tv_IFSC_Code.setVisibility(View.VISIBLE);
        _tv_BeneficiaryMobile_number.setVisibility(View.VISIBLE);

        _tvGetIFSC_List.setVisibility(View.VISIBLE);
        _etBeneficiaryMobile_Number.setVisibility(View.VISIBLE);
        _etTxnDescription.setVisibility(View.GONE);

        _etAmount.setVisibility(View.GONE);
        _etProcessingFees.setVisibility(View.GONE);
        _etTxnDescription.setVisibility(View.GONE);
        _spPayFrom.setVisibility(View.GONE);
        _tv_Spinner_PayFrom.setVisibility(View.GONE);
        _tv_amount.setVisibility(View.GONE);
        _tv_ProcessingFees.setVisibility(View.GONE);
        _tv_txnDescription.setVisibility(View.GONE);
        _rbPay.setVisibility(View.VISIBLE);
        _rbVerify.setVisibility(View.VISIBLE);
        _btnNext.setVisibility(View.VISIBLE);
        _btnCancel.setVisibility(View.VISIBLE);
        _btnVerifySubmit.setVisibility(View.GONE);
        _btnVerifyCancel.setVisibility(View.GONE);

    }

    private void showAlert() {
        //EphemeralStorage.getInstance(getActivity()).getBranchList("");
        getBankName();
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        LinearLayout layout = new LinearLayout(getActivity());
        TextView tvMessage = new TextView(getActivity());
        final EditText etInput = new EditText(getActivity());
        _spOperator1 = new Spinner(getActivity());
        _spOperator2 = new Spinner(getActivity());
        _spOperator3 = new Spinner(getActivity());
        _spOperator4 = new Spinner(getActivity());


        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(_spOperator1);
        layout.addView(_spOperator2);
        layout.addView(_spOperator3);
        layout.addView(_spOperator4);

        alert.setTitle(R.string.tvIFSCCode);
        alert.setView(layout);

        alert.setNegativeButton( getResources().getString(R.string.Dialog_No), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                showProgress(false);
            }
        });

        alert.setPositiveButton( getResources().getString(R.string.Dialog_Yes), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {


                if ((_spOperator1.getSelectedItemPosition() < 1) || (_spOperator2.getSelectedItemPosition() < 1)
                ||(_spOperator3.getSelectedItemPosition() < 1) || (_spOperator4.getSelectedItemPosition() < 1)){

                    sBranchIfscCode = null;
                }
                _etIFSC_Code.setText(sBranchIfscCode);
                showProgress(false);


            }
        });

        alert.show();
    }

    private void resetIPin() {
        //    showMessage(null);

        String sCustomerID = String.valueOf(EphemeralStorage.getInstance(getActivity()).getInt(AppConstants.PARAM_NEW_MOM_IMPS_CUSTOMER_STATUS_CUSTOMER_ID, -1));

        TransactionRequest request = new TransactionRequest(

                getActivity().getString(TransactionType.IMPS_RESET_IPIN.transactionTypeStringId),
               null
        );


        getDataEx(this).resetIPIN(request , sCustomerID);




    }


    public void addListenerOnSpinnerItemSelection() {
        _spOperator
                .setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }

    private void consumerRegistration() {
    //    showMessage(null);

        String sConsumerNumber = _etConsumerNumber.getText().toString().trim();

        TransactionRequest request = new TransactionRequest(

                getActivity().getString(TransactionType.IMPS_CUSTOMER_REGISTRATION.transactionTypeStringId),
                sConsumerNumber
        );


        getDataEx(this).registerIMPSCustomer(request);




    }

    private void impsAuthentication() {

        if(_currentPlatform == PlatformIdentifier.PBX) {
            TransactionRequest<ImpsAuthenticationResult> request = new TransactionRequest<ImpsAuthenticationResult>(

                    getActivity().getString(TransactionType.IMPS_AUTHENTICATION.transactionTypeStringId),
                    null
            );


            getDataEx(this).impsAuthentication(request);

        }
        else if(_currentPlatform == PlatformIdentifier.MOM)
        {

            TransactionRequest  request = new TransactionRequest (

                    getActivity().getString(TransactionType.IMPS_AUTHENTICATION_MOM.transactionTypeStringId),
                    null
            );
            getDataEx(this).impsAuthentication(request);
//            _etConsumerNumber.setVisibility(View.VISIBLE);
//            _btnSubmit.setVisibility(View.VISIBLE);
        }


    }

    private void getBeneficiaryList() {


        String sConsumerNumber = _etConsumerNumber.getText().toString().trim();

        IDataEx dataEx = getDataEx(this);


//        TransactionRequest<List<BeneficiaryResult>> request = new TransactionRequest<List<BeneficiaryResult>>(
//                getActivity().getString(TransactionType.IMPS_BENEFICIARY_LIST.transactionTypeStringId),
//                sConsumerNumber
//        );
        TransactionRequest  request = new TransactionRequest(
                getActivity().getString(TransactionType.IMPS_BENEFICIARY_LIST.transactionTypeStringId),
                sConsumerNumber
        );


        try {
            dataEx.getIMPSBeneficiaryList(request);
            showProgress(true);
        } catch (Exception me) {
            Log.e(_LOG, "Error getting dataex", me);

        }
    }

    private void getRefundDetails() {




        IDataEx dataEx = getDataEx(this);


        TransactionRequest  request = new TransactionRequest(
                getActivity().getString(TransactionType.IMPS_REFUND_DETAILS.transactionTypeStringId),
                null
        );


        try {
            dataEx.impsRefundDetails(request);
            showProgress(true);
        } catch (Exception me) {
            Log.e(_LOG, "Error getting dataex", me);

        }
    }

    private void getBankName() {


        IDataEx dataEx = getDataEx(this);


//        TransactionRequest<List<BankNameResult>> request = new TransactionRequest<List<BankNameResult>>(
//                getActivity().getString(TransactionType.IMPS_BANK_NAME_LIST.transactionTypeStringId),
//                null
//        );

        TransactionRequest  request = new TransactionRequest(
                getActivity().getString(TransactionType.IMPS_BANK_NAME_LIST.transactionTypeStringId),
                null
        );


        try {
            dataEx.getIMPSBankName(request);
            showProgress(true);
        } catch (Exception me) {
            Log.e(_LOG, "Error getting dataex", me);

        }
    }


    private void getStateName(String sBankName) {


        IDataEx dataEx = getDataEx(this);


//        TransactionRequest<List<StateNameResult>> request = new TransactionRequest<List<StateNameResult>>(
//                getActivity().getString(TransactionType.IMPS_STATE_NAME.transactionTypeStringId),
//                null
//        );
        TransactionRequest request = new TransactionRequest(
                getActivity().getString(TransactionType.IMPS_STATE_NAME.transactionTypeStringId),
                null
        );



        try {
            dataEx.getIMPSStateName(request, sBankName);
            showProgress(true);
        } catch (Exception me) {
            Log.e(_LOG, "Error getting dataex", me);

        }
    }

    private void getCityName(String sBankName, String sStateName) {


        IDataEx dataEx = getDataEx(this);


        TransactionRequest request = new TransactionRequest(
                getActivity().getString(TransactionType.IMPS_CITY_NAME.transactionTypeStringId),
                null
        );


        try {
            dataEx.getIMPSCityName(request, sBankName, sStateName);
            showProgress(true);
        } catch (Exception me) {
            Log.e(_LOG, "Error getting dataex", me);

        }
    }

    private void getBranchName(String sBankName, String sStateName, String sCityName) {


        IDataEx dataEx = getDataEx(this);


        TransactionRequest  request = new TransactionRequest(
                getActivity().getString(TransactionType.IMPS_BRANCH_NAME.transactionTypeStringId),
                null
        );


        try {
            dataEx.getIMPSBranchName(request, sBankName, sStateName, sCityName);
            showProgress(true);
        } catch (Exception me) {
            Log.e(_LOG, "Error getting dataex", me);

        }
    }


    private int validate() {
        if (_etConsumerNumber.getText().toString().length() < 10) {
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



    private void setNoPaymentHistoryMessage(){
        _titleView.setText(getResources().getString(R.string.title_activity_transaction_history));

        _tvNoTrans.setVisibility(View.VISIBLE);
    }


    private void createConsumerRegistration() {
        showMessage(null);
        String sConsumerNumber = _etConsumerNumber.getText().toString().trim();
        String sConsumerName = _etConsumerName.getText().toString().trim();
        String sConsumerDOB = _etDob.getText().toString();
        String sConsumerEmailAddress = _etEmailAddress.getText().toString();
        TransactionRequest request = new TransactionRequest(

                getActivity().getString(TransactionType.IMPS_CREATE_CUSTOMER_REGISTRATION.transactionTypeStringId),
                sConsumerNumber, sConsumerName, sConsumerDOB, sConsumerEmailAddress
        );

        getDataEx(this).impsCustomerRegistration(request,sConsumerNumber);

    }

    private void checkKYC() {
        showMessage(null);
        String sConsumerNumber = _etConsumerNumber.getText().toString().trim();
//        TransactionRequest<ImpsCheckKYCResult> request = new TransactionRequest<ImpsCheckKYCResult>(
//
//                getActivity().getString(TransactionType.IMPS_CHECK_KYC.transactionTypeStringId),
//                null
//        );
        TransactionRequest  request = new TransactionRequest(

                getActivity().getString(TransactionType.IMPS_CHECK_KYC.transactionTypeStringId),
                null
        );
        getDataEx(this).impsCheckKYC(request, sConsumerNumber);

    }

    private void createNewBeneficiary() {
     //   showMessage(null);
        String sBeneficiaryName = _etBeneficiaryName.getText().toString();
        String sAccountNumber = _etAccountNumber.getText().toString();
        String sIfscCode = _etIFSC_Code.getText().toString();
        String sBeneficiaryMobNo = _etBeneficiaryMobile_Number.getText().toString();



//        TransactionRequest<ImpsAddBeneficiaryResult> request = new TransactionRequest<ImpsAddBeneficiaryResult>(
//                getActivity().getString(TransactionType.IMPS_ADD_BENEFICIARY.transactionTypeStringId),
//                sBeneficiaryName, sAccountNumber, sIfscCode, sBeneficiaryMobNo, null
//        );

        TransactionRequest request = new TransactionRequest(
                getActivity().getString(TransactionType.IMPS_ADD_BENEFICIARY.transactionTypeStringId),
                sBeneficiaryName, sAccountNumber, sIfscCode, sBeneficiaryMobNo, null
        );

        getDataEx(this).impsAddBeneficiary(request);

    }


    private void getIMPSBeneficiaryDetails(String sBeneficiaryName) {

//        TransactionRequest<ImpsBeneficiaryDetailsResult> request = new TransactionRequest<ImpsBeneficiaryDetailsResult>(
//                getActivity().getString(TransactionType.IMPS_BENEFICIARY_DETAILS.transactionTypeStringId),
//                null
//
//        );

        TransactionRequest request = new TransactionRequest(
                getActivity().getString(TransactionType.IMPS_BENEFICIARY_DETAILS.transactionTypeStringId),
                null

        );

        try {
            getDataEx(this).impsBeneficiaryDetails(request, sBeneficiaryName);

            showProgress(true);

        } catch (Exception me) {
            Log.e(_LOG, "Error getting dataex", me);

        }

    }

    private void getIMPSVerifyProcess() {



        try {
            if (_currentPlatform == PlatformIdentifier.PBX) {
                TransactionRequest<ImpsVerifyProcessResult> request = new TransactionRequest<ImpsVerifyProcessResult>(
                        getActivity().getString(TransactionType.IMPS_VERIFY_PROCESS.transactionTypeStringId),
                        null

                );
                getDataEx(this).impsVerifyProcess(request);
            }
            else if(_currentPlatform == PlatformIdentifier.MOM){
                String sAmount = "1";
                String sTxnNarration  = "test";
                TransactionRequest request  = new TransactionRequest(
                        getActivity().getString(TransactionType.IMPS_MOM_SUBMIT_PROCESS.transactionTypeStringId),
                        null

                );
                getDataEx(this).impsMomPaymentProcess(request, sAmount, sTxnNarration);
            }
            showProgress(true);

        } catch (Exception me) {
            Log.e(_LOG, "Error getting dataex", me);

        }

    }

    private void getMOMIMPSServiceCharge() {
        try {
            String sAmount  = _etAmount.getText().toString();
            TransactionRequest request  = new TransactionRequest(
                    getActivity().getString(TransactionType.IMPS_MOM_SEVICE_CHARGE_PROCESS.transactionTypeStringId),
                    null

            );
            getDataEx(this).impsMomIMPSServiceCharge(request, sAmount);

        } catch (Exception me) {
            Log.e(_LOG, "Error getting dataex", me);

        }
    }
    private void getMOMIMPSVerifyServiceCharge() {
        try {
            String sAmount  = "1";
            TransactionRequest request  = new TransactionRequest(
                    getActivity().getString(TransactionType.IMPS_MOM_SEVICE_CHARGE_PROCESS.transactionTypeStringId),
                    null

            );
            getDataEx(this).impsMomIMPSServiceCharge(request, sAmount);

        } catch (Exception me) {
            Log.e(_LOG, "Error getting dataex", me);

        }
    }
    private void getIMPSVerifyPayment() {

        String sCustomerNumber= _etConsumerNumber.getText().toString();

        Log.i("1" ,sCustomerNumber);
        String sOTP           = _etOTP.getText().toString();
        Log.i("2" ,sOTP);
        String sAccountNumber = _etAccountNumber.getText().toString();
        Log.i("3" ,sAccountNumber);
        String sIFSCCode      = _etIFSC_Code.getText().toString();
        Log.i("4" ,sIFSCCode);



        try {
            if(_currentPlatform == PlatformIdentifier.PBX) {
                TransactionRequest<ImpsVerifyPaymentResult> request = new TransactionRequest<ImpsVerifyPaymentResult>(
                        getActivity().getString(TransactionType.IMPS_VERIFY_PAYMENT.transactionTypeStringId),
                        null

                );
                getDataEx(this).impsVerifyPayment(request, sOTP, sAccountNumber, sIFSCCode, sCustomerNumber);
            }
            else if(_currentPlatform == PlatformIdentifier.MOM){
                TransactionRequest request  = new TransactionRequest(
                        getActivity().getString(TransactionType.IMPS_MOM_CONFIRM_PROCESS.transactionTypeStringId),
                        null

                );

                getDataEx(this).impsMomConfirmProcess(request, sOTP, sAccountNumber, sIFSCCode, sCustomerNumber);

            }
            showProgress(true);

        } catch (Exception me) {
            Log.e(_LOG, "Error getting dataex", me);

        }

    }
    private void getIMPSPaymentProcess() {
        String sAmount        = _etAmount.getText().toString();
        String sTxnNarration  = _etTxnDescription.getText().toString();


        try {
            if (_currentPlatform == PlatformIdentifier.MOM) {
                TransactionRequest request  = new TransactionRequest(
                        getActivity().getString(TransactionType.IMPS_MOM_SUBMIT_PROCESS.transactionTypeStringId),
                        null

                );
                getDataEx(this).impsMomPaymentProcess(request, sAmount, sTxnNarration);
            }
            else if (_currentPlatform == PlatformIdentifier.PBX) {
                TransactionRequest<ImpsPaymentProcessResult> request = new TransactionRequest<ImpsPaymentProcessResult>(

                        getActivity().getString(TransactionType.IMPS_PAYMENT_PROCESS.transactionTypeStringId),
                        null
                );
                getDataEx(this).impsPaymentProcess(request, sAmount, sTxnNarration);
            }

            showProgress(true);

        } catch (Exception me) {
            Log.e(_LOG, "Error getting dataex", me);

        }

    }

    private void getIMPSConfirmPayment() {

        String sCustomerNumber= _etConsumerNumber.getText().toString();
        String sFormattedCustNumber = sCustomerNumber.substring(sCustomerNumber.length() - 3);
        Log.i("CustomerNumber", sFormattedCustNumber);
        Log.i("11" ,sCustomerNumber);
        String sOTP           = _etOTP.getText().toString();
        Log.i("22" ,sOTP);
        String sAccountNumber = _etAccountNumber.getText().toString();
        Log.i("33" ,sAccountNumber);
        String sIFSCCode      = _etIFSC_Code.getText().toString();
        Log.i("44" ,sIFSCCode);
        String sAmount        = _etAmount.getText().toString();



        try {

            if (_currentPlatform == PlatformIdentifier.PBX){
                TransactionRequest<List<ImpsConfirmPaymentResult>> request = new TransactionRequest<List<ImpsConfirmPaymentResult>>(
                        getActivity().getString(TransactionType.IMPS_CONFIRM_PAYMENT.transactionTypeStringId),
                        null

                );
                getDataEx(this).impsConfirmPayment(request, sOTP, sAccountNumber, sIFSCCode, sCustomerNumber, sAmount);
              }

            else if(_currentPlatform == PlatformIdentifier.MOM){
                TransactionRequest request  = new TransactionRequest(
                        getActivity().getString(TransactionType.IMPS_MOM_CONFIRM_PROCESS.transactionTypeStringId),
                        null

                );
                Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                System.out.println("Current time => "+c.getTime());
                DateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                String formattedDate = df.format(c.getTime());
                String sClientTxnID = formattedDate+sFormattedCustNumber;

               getDataEx(this).impsMomConfirmProcess(request, sOTP, sAccountNumber, sClientTxnID, sCustomerNumber);

            }
            showProgress(true);

        } catch (Exception me) {
            Log.e(_LOG, "Error getting dataex", me);

        }

    }

    private void getMOMIMPSConfirmPayment() {

        String sCustomerNumber= _etConsumerNumber.getText().toString();
        String sFormattedCustNumber = sCustomerNumber.substring(sCustomerNumber.length() - 3);
        Log.i("CustomerNumber", sFormattedCustNumber);
        Log.i("11" ,sCustomerNumber);
        String sIPin           = _etOTP.getText().toString();
        Log.i("22" ,sIPin);
        String sAccountNumber = _etAccountNumber.getText().toString();
        Log.i("33" ,sAccountNumber);
        String sIFSCCode      = _etIFSC_Code.getText().toString();
        Log.i("44" ,sIFSCCode);
        String sAmount        = _etAmount.getText().toString();
        String sTxnNarration  = _etTxnDescription.getText().toString();
        String CustomerID = String.valueOf(EphemeralStorage.getInstance(getActivity()).getInt(AppConstants.PARAM_NEW_MOM_IMPS_CUSTOMER_STATUS_CUSTOMER_ID, -1));
        Log.e("CustomerId" , CustomerID);



        try {

            if (_currentPlatform == PlatformIdentifier.PBX){
                TransactionRequest<List<ImpsConfirmPaymentResult>> request = new TransactionRequest<List<ImpsConfirmPaymentResult>>(
                        getActivity().getString(TransactionType.IMPS_CONFIRM_PAYMENT.transactionTypeStringId),
                        null

                );
                getDataEx(this).impsConfirmPayment(request, sIPin, sAccountNumber, sIFSCCode, sCustomerNumber, sAmount);
            }

            else if(_currentPlatform == PlatformIdentifier.MOM){
                TransactionRequest request  = new TransactionRequest(
                        getActivity().getString(TransactionType.IMPS_MOM_CONFIRM_PROCESS.transactionTypeStringId),
                        null

                );
                Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                System.out.println("Current time => "+c.getTime());
                DateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                String formattedDate = df.format(c.getTime());
                String sClientTxnID = formattedDate+sFormattedCustNumber;


                getDataEx(this).impsMomConfirmPaymentProcess(request, sAmount ,sTxnNarration ,
                        sIPin ,  sClientTxnID);

            }
            showProgress(true);

        } catch (Exception me) {
            Log.e(_LOG, "Error getting dataex", me);

        }

    }

    private void getMOMIMPSPaymentByRefund() {

        String sCustomerNumber= _etConsumerNumber.getText().toString();
        String sFormattedCustNumber = sCustomerNumber.substring(sCustomerNumber.length() - 3);
        Log.i("CustomerNumber", sFormattedCustNumber);
        Log.i("11" ,sCustomerNumber);
        String sIPin           = _etOTP.getText().toString();
        Log.i("22" ,sIPin);
        String sAccountNumber = _etAccountNumber.getText().toString();
        Log.i("33" ,sAccountNumber);
        String sIFSCCode      = _etIFSC_Code.getText().toString();
        Log.i("44" ,sIFSCCode);
        String sReceiptId     = EphemeralStorage.getInstance(getActivity()).getString(AppConstants.PARAM_MOM_IMPS_REFUND_RECEIPT_ID_RESPONSE , null);
        Log.i("ReceiptID" ,sReceiptId);
        String sTxnNarration  = _etTxnDescription.getText().toString();
        String CustomerID = String.valueOf(EphemeralStorage.getInstance(getActivity()).getInt(AppConstants.PARAM_NEW_MOM_IMPS_CUSTOMER_STATUS_CUSTOMER_ID, -1));
        Log.e("CustomerId" , CustomerID);



        try {



                TransactionRequest request  = new TransactionRequest(
                        getActivity().getString(TransactionType.IMPS_MOM_CONFIRM_PROCESS.transactionTypeStringId),
                        null

                );
                Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                System.out.println("Current time => "+c.getTime());
                DateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                String formattedDate = df.format(c.getTime());
                String sClientTxnID = formattedDate+sFormattedCustNumber;


                getDataEx(this).impsMomConfirmPaymentProcessByRefund(request, sReceiptId ,sTxnNarration ,
                        sIPin ,  sClientTxnID);


            showProgress(true);

        } catch (Exception me) {
            Log.e(_LOG, "Error getting dataex", me);

        }

    }

    private void getMOMIMPSVerifyPayment() {

        String sCustomerNumber= _etConsumerNumber.getText().toString();
        String sFormattedCustNumber = sCustomerNumber.substring(sCustomerNumber.length() - 3);
        Log.i("CustomerNumber", sFormattedCustNumber);
        Log.i("1" ,sCustomerNumber);
        String sOTP           = _etOTP.getText().toString();
        Log.i("2" ,sOTP);
        String sAccountNumber = _etAccountNumber.getText().toString();
        Log.i("3" ,sAccountNumber);
        String sIFSCCode      = _etIFSC_Code.getText().toString();
        Log.i("4" ,sIFSCCode);
        String sAmount = "1";
        Log.i("5" ,sAmount);
        String sTxnNarration  = "test";
        Log.i("6" ,sTxnNarration);
        String sIPin           = _etOTP.getText().toString();
        Log.i("22" ,sIPin);
        String CustomerID = String.valueOf(EphemeralStorage.getInstance(getActivity()).getInt(AppConstants.PARAM_NEW_MOM_IMPS_CUSTOMER_STATUS_CUSTOMER_ID, -1));
        Log.e("CustomerId" , CustomerID);





        try {


            TransactionRequest request  = new TransactionRequest(
                    getActivity().getString(TransactionType.IMPS_MOM_CONFIRM_PROCESS.transactionTypeStringId),
                    null

            );
            Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            System.out.println("Current time => "+c.getTime());
            DateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String formattedDate = df.format(c.getTime());
            String sClientTxnID = formattedDate+sFormattedCustNumber;


            getDataEx(this).impsMomConfirmPaymentProcess(request, sAmount ,sTxnNarration ,
                    sIPin ,  sClientTxnID);

        } catch (Exception me) {
            Log.e(_LOG, "Error getting dataex", me);

        }

    }
    private void validate( View view) {


        int nAmount             = 0;
        int nMinAmount          = 10;
        int nMaxAmount          = 25000;
        int nMinLength          = 10;
        int nMaxLength          = 10;
        int nMinName            = 6;
        int nMaxName            = 25;
        int nMinAccountLength   = 4;
        int nMaxAccountlength   = 30;
        int nMinIFSClength      = 11;
        int nMaxIFSClength      = 11;
        float nAvailableLimit   =Float.valueOf(_etAvailableLimit.getText().toString());




        int nCustomerNumberLength
                = _etConsumerNumber.getText().toString().trim().length();
        int nBeneficiaryNameLength
                = _etBeneficiaryName.getText().toString().trim().length();
        int nAccountLength
                = _etAccountNumber.getText().toString().trim().length();
        int nIFSCLength
                = _etIFSC_Code.getText().toString().trim().length();
        int nBeneficiaryNumberLength
                = _etBeneficiaryMobile_Number.getText().toString().trim().length();

        if(((_spOperator.getSelectedItemPosition()== 0)&& (_rbPay.isChecked()))|| (_spOperator.getSelectedItemPosition()== 0)
        ||((_spOperator.getSelectedItemPosition()== 0) &&(_rbVerify.isChecked()))){
            if (nBeneficiaryNameLength < nMinName || nBeneficiaryNameLength > nMaxName) {

                if (nMinName == nMaxName) {

                    showMessage(String.format(getResources().getString(R.string.error_beneficiary_name_length), nMinName));
                } else {
                    showMessage(String.format(getResources().getString(R.string.error_beneficiary_name_min_max), nMinName, nMaxName));
                }
                _etBeneficiaryName.setText("");
                return;
            }


            if (nAccountLength < nMinAccountLength || nAccountLength > nMaxAccountlength) {
                if (nMinAccountLength == nMaxAccountlength) {
                    showMessage(String.format(getResources().getString(R.string.error_AccountNumber_length), nMinAccountLength));
                } else {
                    showMessage(String.format(getResources().getString(R.string.error_AccountNumber_length_min_max), nMinAccountLength, nMaxAccountlength));
                }
                _etAccountNumber.setText("");
                return;
            }
            if ("".equals(_etIFSC_Code.getText().toString().trim())) {
                showMessage(getResources().getString(R.string.prompt_numbers_only_IFSCCode));
                return;
            }

            if (nIFSCLength < nMinIFSClength || nIFSCLength > nMaxIFSClength) {
                if (nMinIFSClength == nMaxIFSClength) {
                    showMessage(String.format(getResources().getString(R.string.error_IFSCCODE_length), nMinIFSClength));
                } else {
                    showMessage(String.format(getResources().getString(R.string.error_IFSCCODE_length_min_max), nMinIFSClength, nMaxIFSClength));
                }
                _etIFSC_Code.setText("");
                return;
            }
            if (nBeneficiaryNumberLength < nMinLength || nBeneficiaryNumberLength > nMaxLength) {
                if (nMinLength == nMaxLength) {
                    showMessage(String.format(getResources().getString(R.string.error_phone_length), nMinLength));
                } else {
                    showMessage(String.format(getResources().getString(R.string.error_phone_length_min_max), nMinLength, nMaxLength));
                }
                _etBeneficiaryMobile_Number.setText("");
                return;
            }
        }

           if((_rbPay.isChecked()) && (_spOperator.getSelectedItemPosition()!= 0))

           {


           if (nBeneficiaryNameLength < nMinName || nBeneficiaryNameLength > nMaxName) {

             if (nMinName == nMaxName) {

            showMessage(String.format(getResources().getString(R.string.error_beneficiary_name_length), nMinName));
           } else {
            showMessage(String.format(getResources().getString(R.string.error_beneficiary_name_min_max), nMinName, nMaxName));
          }
            _etBeneficiaryName.setText("");
            return;
          }

         if (nAccountLength < nMinAccountLength || nAccountLength > nMaxAccountlength) {
          if (nMinAccountLength == nMaxAccountlength) {
            showMessage(String.format(getResources().getString(R.string.error_AccountNumber_length), nMinAccountLength));
          } else {
            showMessage(String.format(getResources().getString(R.string.error_AccountNumber_length_min_max), nMinAccountLength, nMaxAccountlength));
          }
           _etAccountNumber.setText("");
           return;
         }
         if ("".equals(_etIFSC_Code.getText().toString().trim())) {
          showMessage(getResources().getString(R.string.prompt_numbers_only_IFSCCode));
          return;
        }

    if (nIFSCLength < nMinIFSClength || nIFSCLength > nMaxIFSClength) {
        if (nMinIFSClength == nMaxIFSClength) {
            showMessage(String.format(getResources().getString(R.string.error_IFSCCODE_length), nMinIFSClength));
        } else {
            showMessage(String.format(getResources().getString(R.string.error_IFSCCODE_length_min_max), nMinIFSClength, nMaxIFSClength));
        }
        _etIFSC_Code.setText("");
        return;
    }
    if (nBeneficiaryNumberLength < nMinLength || nBeneficiaryNumberLength > nMaxLength) {
        if (nMinLength == nMaxLength) {
            showMessage(String.format(getResources().getString(R.string.error_phone_length), nMinLength));
        } else {
            showMessage(String.format(getResources().getString(R.string.error_phone_length_min_max), nMinLength, nMaxLength));
        }
        _etBeneficiaryMobile_Number.setText("");
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
        if(_spPayFrom.getSelectedItemPosition()!=0){
            _etAmount.setEnabled(false);
        }
        else {
            _etAmount.setText("");
        }
        return;
    }
//    if(Integer.parseInt(_etAmount.getText().toString().trim()) > nAvailableLimit ){
//        showMessage(getResources().getString(R.string.lic_failed_policyDetails_msg_default));
//       return;
//    }
               if(Float.valueOf(_etAmount.getText().toString().trim()) > nAvailableLimit ){
                   showMessage(getResources().getString(R.string.lic_failed_policyDetails_msg_default));
                   return;
               }
    float balance         = EphemeralStorage.getInstance(getActivity().getApplicationContext()).getFloat(AppConstants.RMN_BALANCE, AppConstants.ERROR_BALANCE);
               if(_currentPlatform == PlatformIdentifier.PBX) {
//                   float bal = Math.abs(balance);
                   Log.e("BalVal" , String.valueOf(balance*(-1)));
                   if(balance*(-1)<Float.valueOf(_etAmount.getText().toString().trim()) ){

                       showMessage(getResources().getString(R.string.Imps_failed_Amount));
                       return;
                   }
               }
               else {
                   if (Float.valueOf(_etAmount.getText().toString().trim()) > balance) {
                       Log.e("BalVal" , String.valueOf(balance));
                       showMessage(getResources().getString(R.string.Imps_failed_Amount));
                       return;
                   }
               }

    if ("".equals(_etTxnDescription.getText().toString().trim())) {
        showMessage(getResources().getString(R.string.prompt_TxnDescription));
        _etTxnDescription.setText("");
        return;
    }
}

        else if((_rbVerify.isChecked())&& (_spOperator.getSelectedItemPosition()!= 0)){
    if (nBeneficiaryNameLength < nMinName || nBeneficiaryNameLength > nMaxName) {

        if (nMinName == nMaxName) {

            showMessage(String.format(getResources().getString(R.string.error_beneficiary_name_length), nMinName));
        } else {
            showMessage(String.format(getResources().getString(R.string.error_beneficiary_name_min_max), nMinName, nMaxName));
        }
        _etBeneficiaryName.setText("");
        return;
    }

    if (nAccountLength < nMinAccountLength || nAccountLength > nMaxAccountlength) {
        if (nMinAccountLength == nMaxAccountlength) {
            showMessage(String.format(getResources().getString(R.string.error_AccountNumber_length), nMinAccountLength));
        } else {
            showMessage(String.format(getResources().getString(R.string.error_AccountNumber_length_min_max), nMinAccountLength, nMaxAccountlength));
        }
        _etAccountNumber.setText("");
        return;
    }
    if ("".equals(_etIFSC_Code.getText().toString().trim())) {
        showMessage(getResources().getString(R.string.prompt_numbers_only_IFSCCode));
        return;
    }

    if (nIFSCLength < nMinIFSClength || nIFSCLength > nMaxIFSClength) {
        if (nMinIFSClength == nMaxIFSClength) {
            showMessage(String.format(getResources().getString(R.string.error_IFSCCODE_length), nMinIFSClength));
        } else {
            showMessage(String.format(getResources().getString(R.string.error_IFSCCODE_length_min_max), nMinIFSClength, nMaxIFSClength));
        }
        _etIFSC_Code.setText("");
        return;
    }
    if (nBeneficiaryNumberLength < nMinLength || nBeneficiaryNumberLength > nMaxLength) {
        if (nMinLength == nMaxLength) {
            showMessage(String.format(getResources().getString(R.string.error_phone_length), nMinLength));
        } else {
            showMessage(String.format(getResources().getString(R.string.error_phone_length_min_max), nMinLength, nMaxLength));
        }
        _etBeneficiaryMobile_Number.setText("");
        return;
    }

        }

       // String sBeneficiarySpinner = _spOperator.getSelectedItem().toString();

        if (_spOperator.getSelectedItemPosition() == 0) {
            _btnNext.setEnabled(false);
            _tvVerified.setVisibility(View.GONE);
            _tv_amount.setVisibility(View.GONE);
            _tv_txnDescription.setVisibility(View.GONE);
            _etAmount.setVisibility(View.GONE);
            _etTxnDescription.setVisibility(View.GONE);

            createNewBeneficiary();
        } else if((_spOperator.getSelectedItemPosition()!= 0)&& (_rbPay.isChecked()== true)) {
           // todo enable
            _btnNext.setEnabled(false);

            getMOMIMPSServiceCharge();
           //  getIMPSPaymentProcess();


            _tvVerified.setVisibility(View.GONE);

        }
        else if((_spOperator.getSelectedItemPosition()!= 0)&& (_rbVerify.isChecked()== true)) {
            _btnNext.setEnabled(false);
           // getMOMIMPSVerifyServiceCharge();
           // getIMPSVerifyProcess();

            _etAmountPayable.setVisibility(View.GONE);
            _etOTP.setVisibility(View.VISIBLE);
            _etOTP.setText(null);

            //hideRetrieveBillFields();
            showVerifyPaymentFields();
            _tvVerified.setVisibility(View.GONE);


        }
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
        date.show(getActivity().getSupportFragmentManager(), "Date Picker");
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

        Calendar userAge = new GregorianCalendar(year, month, day);
        Calendar minAdultAge = new GregorianCalendar();
        minAdultAge.add(Calendar.YEAR, -18);


        if (minAdultAge.before(userAge)) {

            showMessage(getResources().getString(R.string.prompt_Validity_DOB_Age));
            isAgeValid= false;

        } else {
            hideMessage();

            isAgeValid= true;
        }

    }


    public void showRetrieveBillNextFields() {


        _etConsumerNumber.setVisibility(View.GONE);
        _etConsumerName.setVisibility(View.GONE);
        _etDob.setVisibility(View.GONE);
        _etEmailAddress.setVisibility(View.GONE);
        _btnImpsCreateCustomer.setVisibility(View.GONE);
        _btnImpsCancel.setVisibility(View.GONE);
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

        _etAmount.setVisibility(View.GONE);
        _etProcessingFees.setVisibility(View.VISIBLE);
        _etAmountPayable.setVisibility(View.VISIBLE);
        _etOTP.setVisibility(View.VISIBLE);
        _btnOTPResend.setVisibility(View.VISIBLE);
        _btnPay.setVisibility(View.VISIBLE);
    }

    public void showCustomRegistrationFields() {
        _etConsumerName.setText("");
        _etDob.setText("");
        _etEmailAddress.setText("");

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

        _etAmount.setVisibility(View.GONE);
        _rbPay.setVisibility(View.GONE);
        _rbVerify.setVisibility(View.GONE);
        _btnNext.setVisibility(View.GONE);
        _btnCancel.setVisibility(View.GONE);
    }
    public void hideCustomRegistrationFields() {
         showMessage(null);
        _etConsumerNumber.setEnabled(true);
        _etConsumerNumber.setVisibility(View.VISIBLE);
        _etConsumerName.setVisibility(View.GONE);
        _etDob.setVisibility(View.GONE);
        _etEmailAddress.setVisibility(View.GONE);
        _btnImpsCreateCustomer.setVisibility(View.GONE);
        _btnImpsCancel.setVisibility(View.GONE);

        _btnSubmit.setVisibility(View.VISIBLE);

        _etAvailableLimit.setVisibility(View.GONE);
        _spOperator.setVisibility(View.GONE);
        _etBeneficiaryName.setVisibility(View.GONE);
        _etAccountNumber.setVisibility(View.GONE);
        _etIFSC_Code.setVisibility(View.GONE);
        _tvGetIFSC_List.setVisibility(View.GONE);
        _etBeneficiaryMobile_Number.setVisibility(View.GONE);

        _etAmount.setVisibility(View.GONE);
        _rbPay.setVisibility(View.GONE);
        _rbVerify.setVisibility(View.GONE);
        _btnNext.setVisibility(View.GONE);
        _btnCancel.setVisibility(View.GONE);
    }

    public void setupIMPSPaymentCancelTransferView() {

        _etAmount.setEnabled(true);
        _btnSubmit.setVisibility(View.GONE);
        _etConsumerNumber.setVisibility(View.VISIBLE);

        _etAvailableLimit.setVisibility(View.VISIBLE);
        _spOperator.setVisibility(View.VISIBLE);
        _spPayFrom.setVisibility(View.VISIBLE);
        _etBeneficiaryName.setVisibility(View.VISIBLE);
        _etAccountNumber.setVisibility(View.VISIBLE);
        _etIFSC_Code.setVisibility(View.VISIBLE);
        _tvGetIFSC_List.setVisibility(View.VISIBLE);
        _etBeneficiaryMobile_Number.setVisibility(View.VISIBLE);

        _etTxnDescription.setVisibility(View.VISIBLE);
        _etAmount.setVisibility(View.VISIBLE);
        _rbPay.setVisibility(View.VISIBLE);
        _rbVerify.setVisibility(View.GONE);
        _btnNext.setVisibility(View.VISIBLE);
        _btnCancel.setVisibility(View.VISIBLE);
        _btnVerifySubmit.setVisibility(View.GONE);
        _btnVerifyCancel.setVisibility(View.GONE);

        _tv_availableLimit.setVisibility(View.GONE);
        _tv_operator.setVisibility(View.GONE);
        _tv_beneficiary_name.setVisibility(View.GONE);
        _tv_account_number.setVisibility(View.GONE);
        _tv_IFSC_Code.setVisibility(View.GONE);
        _tv_BeneficiaryMobile_number.setVisibility(View.GONE);
        //_tv_Spinner_PayFrom.setVisibility(View.GONE);
        _tv_amount.setVisibility(View.GONE);
        _tv_txnDescription.setVisibility(View.GONE);
        _tv_ProcessingFees.setVisibility(View.GONE);
        _tv_AmountPayable.setVisibility(View.GONE);
        _tv_OTP.setVisibility(View.GONE);

    }
  public void  setupIMPSTransferPayView(){
      _etAmount.setEnabled(true);
      _btnSubmit.setVisibility(View.GONE);
      _etConsumerNumber.setVisibility(View.VISIBLE);

      _etAvailableLimit.setVisibility(View.VISIBLE);
      _spOperator.setVisibility(View.VISIBLE);
       _spPayFrom.setVisibility(View.VISIBLE);
      _etBeneficiaryName.setVisibility(View.VISIBLE);
      _etAccountNumber.setVisibility(View.VISIBLE);
      _etIFSC_Code.setVisibility(View.VISIBLE);
      _tvGetIFSC_List.setVisibility(View.VISIBLE);
      _etBeneficiaryMobile_Number.setVisibility(View.VISIBLE);

      _etTxnDescription.setVisibility(View.VISIBLE);
      _etAmount.setVisibility(View.VISIBLE);
      _rbPay.setVisibility(View.VISIBLE);
      _rbVerify.setVisibility(View.VISIBLE);
      _btnNext.setVisibility(View.VISIBLE);
      _btnCancel.setVisibility(View.VISIBLE);
      _btnVerifySubmit.setVisibility(View.GONE);
      _btnVerifyCancel.setVisibility(View.GONE);

      _tv_availableLimit.setVisibility(View.VISIBLE);
      _tv_operator.setVisibility(View.VISIBLE);
      _tv_beneficiary_name.setVisibility(View.VISIBLE);
      _tv_account_number.setVisibility(View.VISIBLE);
      _tv_IFSC_Code.setVisibility(View.VISIBLE);
      _tv_BeneficiaryMobile_number.setVisibility(View.VISIBLE);
      _tv_Spinner_PayFrom.setVisibility(View.VISIBLE);
      _tv_amount.setVisibility(View.VISIBLE);
      _tv_txnDescription.setVisibility(View.VISIBLE);
      _tv_ProcessingFees.setVisibility(View.GONE);
      _tv_AmountPayable.setVisibility(View.GONE);
      _tv_OTP.setVisibility(View.GONE);
    }

    public void setupIMPSTransferView() {


        _etAmount.setEnabled(true);
        _btnSubmit.setVisibility(View.GONE);
        _etConsumerNumber.setVisibility(View.VISIBLE);

        _etAvailableLimit.setVisibility(View.VISIBLE);
        _spOperator.setVisibility(View.VISIBLE);

        _etBeneficiaryName.setVisibility(View.VISIBLE);
        _etAccountNumber.setVisibility(View.VISIBLE);
        _etIFSC_Code.setVisibility(View.VISIBLE);
        _tvGetIFSC_List.setVisibility(View.VISIBLE);
        _etBeneficiaryMobile_Number.setVisibility(View.VISIBLE);

        _etTxnDescription.setVisibility(View.GONE);
        _etAmount.setVisibility(View.GONE);
        _rbPay.setVisibility(View.VISIBLE);
        _rbVerify.setVisibility(View.VISIBLE);
        _btnNext.setVisibility(View.VISIBLE);
        _btnCancel.setVisibility(View.VISIBLE);
        _btnVerifySubmit.setVisibility(View.GONE);
        _btnVerifyCancel.setVisibility(View.GONE);
        _tv_consumerNumber.setVisibility(View.VISIBLE);
        _tv_availableLimit.setVisibility(View.VISIBLE);
        _tv_operator.setVisibility(View.VISIBLE);
        _tv_beneficiary_name.setVisibility(View.VISIBLE);
        _tv_account_number.setVisibility(View.VISIBLE);
        _tv_IFSC_Code.setVisibility(View.VISIBLE);
        _tv_BeneficiaryMobile_number.setVisibility(View.VISIBLE);

        _tv_amount.setVisibility(View.GONE);
        _tv_txnDescription.setVisibility(View.GONE);
        _tv_ProcessingFees.setVisibility(View.GONE);
        _tv_AmountPayable.setVisibility(View.GONE);
        _tv_OTP.setVisibility(View.GONE);

    }



    public void setupIMPSVerifyView() {


        _btnSubmit.setVisibility(View.GONE);
        _etConsumerNumber.setVisibility(View.VISIBLE);
        _etAvailableLimit.setVisibility(View.VISIBLE);
        _spOperator.setVisibility(View.VISIBLE);
        _etBeneficiaryName.setVisibility(View.VISIBLE);
        _etAccountNumber.setVisibility(View.VISIBLE);
        _etIFSC_Code.setVisibility(View.VISIBLE);
        _tvGetIFSC_List.setVisibility(View.VISIBLE);
        _etBeneficiaryMobile_Number.setVisibility(View.VISIBLE);

        _etAmount.setVisibility(View.GONE);
        _rbPay.setVisibility(View.VISIBLE);
        _rbVerify.setVisibility(View.VISIBLE);
        _btnNext.setVisibility(View.VISIBLE);
        _btnCancel.setVisibility(View.VISIBLE);
        _etTxnDescription.setVisibility(View.GONE);
        _btnVerifySubmit.setVisibility(View.VISIBLE);
        _btnVerifyCancel.setVisibility(View.VISIBLE);
        _tv_AmountPayable.setVisibility(View.GONE);
        _tv_OTP.setVisibility(View.GONE);
        _btnVerifySubmit.setVisibility(View.GONE);
        _btnVerifyCancel.setVisibility(View.GONE);

    }


    public void showPaymentFields(){
        _etBeneficiaryName.setEnabled(false);
        _etAccountNumber.setEnabled(false);
        _etIFSC_Code.setEnabled(false);
        _etBeneficiaryMobile_Number.setEnabled(false);
        _etAmountPayable.setEnabled(false);

        _tv_availableLimit.setVisibility(View.VISIBLE);
        _tv_operator.setVisibility(View.VISIBLE);
        _tv_beneficiary_name.setVisibility(View.VISIBLE);
        _tv_account_number.setVisibility(View.VISIBLE);
        _tv_IFSC_Code.setVisibility(View.VISIBLE);
        _tv_BeneficiaryMobile_number.setVisibility(View.VISIBLE);
       _tv_Spinner_PayFrom.setVisibility(View.VISIBLE);
        _tv_AmountPayable.setVisibility(View.VISIBLE);
        _tv_OTP.setVisibility(View.VISIBLE);
        _tv_amount.setVisibility(View.VISIBLE);
        _etBeneficiaryName.setVisibility(View.VISIBLE);
        _etAccountNumber.setVisibility(View.VISIBLE);
        _etIFSC_Code.setVisibility(View.VISIBLE);
        _etBeneficiaryMobile_Number.setVisibility(View.VISIBLE);
        _etOTP.setVisibility(View.VISIBLE);
        _etAmountPayable.setVisibility(View.VISIBLE);

        _btnVerifyPaySubmit.setVisibility(View.VISIBLE);
        _btnVerifyPayCancel.setVisibility(View.VISIBLE);
        _etTxnDescription.setVisibility(View.GONE);
        _tvGetIFSC_List.setVisibility(View.GONE);
        _rbPay.setVisibility(View.GONE);
        _rbVerify.setVisibility(View.GONE);
        _spOperator.setVisibility(View.GONE);
        _spPayFrom.setVisibility(View.GONE);
        _etConsumerNumber.setVisibility(View.GONE);
        _etAvailableLimit.setVisibility(View.GONE);
        _spOperator.setVisibility(View.GONE);
        _btnNext.setVisibility(View.GONE);
        _btnCancel.setVisibility(View.GONE);
        _tv_txnDescription.setVisibility(View.GONE);
        _tv_consumerNumber.setVisibility(View.GONE);
        _tv_availableLimit.setVisibility(View.GONE);
        _tv_operator.setVisibility(View.GONE);

    }
    public void showVerifyPaymentFields(){
        _etBeneficiaryName.setEnabled(false);
        _etAccountNumber.setEnabled(false);
        _etIFSC_Code.setEnabled(false);
        _etBeneficiaryMobile_Number.setEnabled(false);
        _etAmountPayable.setEnabled(false);
        _etAmountPayable.setText("10");
        _tv_availableLimit.setVisibility(View.VISIBLE);
        _tv_operator.setVisibility(View.VISIBLE);
        _tv_beneficiary_name.setVisibility(View.VISIBLE);
        _tv_account_number.setVisibility(View.VISIBLE);
        _tv_IFSC_Code.setVisibility(View.VISIBLE);
        _tv_BeneficiaryMobile_number.setVisibility(View.VISIBLE);

        _tv_AmountPayable.setVisibility(View.VISIBLE);
        _tv_OTP.setVisibility(View.VISIBLE);
        _tv_amount.setVisibility(View.GONE);
        _etBeneficiaryName.setVisibility(View.VISIBLE);
        _etAccountNumber.setVisibility(View.VISIBLE);
        _etIFSC_Code.setVisibility(View.VISIBLE);
        _etBeneficiaryMobile_Number.setVisibility(View.VISIBLE);
        _etOTP.setVisibility(View.VISIBLE);
        _etAmountPayable.setVisibility(View.VISIBLE);
        _btnOTPResend.setVisibility(View.VISIBLE);
        _btnVerifyPaySubmit.setVisibility(View.VISIBLE);
        _btnVerifyPayCancel.setVisibility(View.VISIBLE);
        _etTxnDescription.setVisibility(View.GONE);
        _tvGetIFSC_List.setVisibility(View.GONE);
        _rbPay.setVisibility(View.GONE);
        _rbVerify.setVisibility(View.GONE);
        _spOperator.setVisibility(View.GONE);

        _etConsumerNumber.setVisibility(View.GONE);
        _etAvailableLimit.setVisibility(View.GONE);
        _spOperator.setVisibility(View.GONE);
        _spPayFrom.setVisibility(View.GONE);
        _btnNext.setVisibility(View.GONE);
        _btnCancel.setVisibility(View.GONE);
        _tv_txnDescription.setVisibility(View.GONE);
        _tv_consumerNumber.setVisibility(View.GONE);
        _tv_availableLimit.setVisibility(View.GONE);
        _tv_operator.setVisibility(View.GONE);
        _tv_Spinner_PayFrom.setVisibility(View.GONE);

    }

    public void showPaymentProcessFields(){
        _etBeneficiaryName.setVisibility(View.VISIBLE);
        _etAccountNumber.setVisibility(View.VISIBLE);
        _etIFSC_Code.setVisibility(View.VISIBLE);
        _etBeneficiaryMobile_Number.setVisibility(View.VISIBLE);
        _etOTP.setVisibility(View.VISIBLE);
        _btnVerifyPaySubmit.setVisibility(View.GONE);
        _btnVerifyPayCancel.setVisibility(View.GONE);
        _etAmountPayable.setVisibility(View.VISIBLE);
        _etProcessingFees.setVisibility(View.VISIBLE);
        _etAmount.setVisibility(View.VISIBLE);
        _etOTP.setVisibility(View.VISIBLE);

        _etTxnDescription.setVisibility(View.GONE);

    }
    public void showConfirmPaymentFields(){
        int amount = Integer.parseInt(_etAmount.getText().toString());
      //  int processFees = 10;
        int processFees =  EphemeralStorage.getInstance(getActivity()).getInt(AppConstants.PARAM_NEW_IMPS_SERVICE_CHARGE, -1);
        int amountPayable = amount+processFees;
        _etBeneficiaryName.setVisibility(View.VISIBLE);
        _etAccountNumber.setVisibility(View.VISIBLE);
        _etIFSC_Code.setVisibility(View.VISIBLE);
        _etBeneficiaryMobile_Number.setVisibility(View.VISIBLE);
        _etAmountPayable.setVisibility(View.VISIBLE);
        _etProcessingFees.setVisibility(View.VISIBLE);
        _etAmount.setVisibility(View.VISIBLE);
        _btnOTPResend.setVisibility(View.VISIBLE);
        _etProcessingFees.setText(String.valueOf(processFees));

        _etAmountPayable.setText(String.valueOf(amountPayable));
        _etOTP.setVisibility(View.VISIBLE);
        _etConsumerNumber.setVisibility(View.GONE);
        _etAvailableLimit.setVisibility(View.GONE);
        _spOperator.setVisibility(View.GONE);
        _spPayFrom.setVisibility(View.GONE);
        _etTxnDescription.setVisibility(View.GONE);
        _btnSubmit.setVisibility(View.GONE);
        _btnCancel.setVisibility(View.GONE);
        _btnVerifyPaySubmit.setVisibility(View.GONE);
        _btnVerifyPayCancel.setVisibility(View.GONE);
        _btnNext.setVisibility(View.GONE);
        _btnCancel.setVisibility(View.GONE);
        _rbPay.setVisibility(View.GONE);
        _rbVerify.setVisibility(View.GONE);
        _btnPaymentPaySubmit.setVisibility(View.VISIBLE);
        _btnPaymentPayCancel.setVisibility(View.VISIBLE);


        _tvGetIFSC_List.setVisibility(View.GONE);
        //_tv_availableLimit.setVisibility(View.VISIBLE);
        //_tv_operator.setVisibility(View.VISIBLE);
        _tv_beneficiary_name.setVisibility(View.VISIBLE);
        _tv_account_number.setVisibility(View.VISIBLE);
        _tv_IFSC_Code.setVisibility(View.VISIBLE);
        _tv_BeneficiaryMobile_number.setVisibility(View.VISIBLE);
        _tv_Spinner_PayFrom.setVisibility(View.GONE);
        _tv_amount.setVisibility(View.VISIBLE);
        _tv_txnDescription.setVisibility(View.GONE);
        _tv_ProcessingFees.setVisibility(View.VISIBLE);
        _tv_AmountPayable.setVisibility(View.VISIBLE);
        _tv_OTP.setVisibility(View.VISIBLE);
        _tv_consumerNumber.setVisibility(View.GONE);
        _tv_availableLimit.setVisibility(View.GONE);
        _tv_operator.setVisibility(View.GONE);
        _tvFullKYC.setVisibility(View.GONE);
        _tvNILLKYC.setVisibility(View.GONE);

    }


    public void showPBXPaymentSubmit(){
        int amount = Integer.parseInt(_etAmount.getText().toString());
        int processFees = 10;
        int amountPayable = amount+processFees;
        _etBeneficiaryName.setVisibility(View.VISIBLE);
        _etAccountNumber.setVisibility(View.VISIBLE);
        _etIFSC_Code.setVisibility(View.VISIBLE);
        _etBeneficiaryMobile_Number.setVisibility(View.VISIBLE);
        _etAmountPayable.setVisibility(View.VISIBLE);
        _etProcessingFees.setVisibility(View.VISIBLE);
        _etAmount.setVisibility(View.VISIBLE);

        _etProcessingFees.setText(String.valueOf(processFees));

        _etAmountPayable.setText(String.valueOf(amountPayable));
        _etOTP.setVisibility(View.VISIBLE);
        _btnSubmit.setVisibility(View.GONE);
        _btnCancel.setVisibility(View.GONE);
        _btnVerifyPaySubmit.setVisibility(View.GONE);
        _btnVerifyPayCancel.setVisibility(View.GONE);
        _btnNext.setVisibility(View.GONE);
        _btnCancel.setVisibility(View.GONE);
        _btnPaymentPaySubmit.setVisibility(View.VISIBLE);
        _btnPaymentPayCancel.setVisibility(View.VISIBLE);



        _tv_availableLimit.setVisibility(View.VISIBLE);
        _tv_operator.setVisibility(View.VISIBLE);
        _tv_beneficiary_name.setVisibility(View.VISIBLE);
        _tv_account_number.setVisibility(View.VISIBLE);
        _tv_IFSC_Code.setVisibility(View.VISIBLE);
        _tv_BeneficiaryMobile_number.setVisibility(View.VISIBLE);
        _tv_Spinner_PayFrom.setVisibility(View.VISIBLE);
        _tv_amount.setVisibility(View.VISIBLE);
        _tv_txnDescription.setVisibility(View.GONE);
        _tv_ProcessingFees.setVisibility(View.VISIBLE);
        _tv_AmountPayable.setVisibility(View.VISIBLE);
        _tv_OTP.setVisibility(View.VISIBLE);

    }

    public void showPBXBackSubmit(){
        _etAmount.setEnabled(true);
        _btnSubmit.setVisibility(View.GONE);
        _etConsumerNumber.setVisibility(View.VISIBLE);

        _etAvailableLimit.setVisibility(View.VISIBLE);
        _spOperator.setVisibility(View.VISIBLE);
        _spPayFrom.setVisibility(View.VISIBLE);
        _etBeneficiaryName.setVisibility(View.VISIBLE);
        _etAccountNumber.setVisibility(View.VISIBLE);
        _etIFSC_Code.setVisibility(View.VISIBLE);
        _tvGetIFSC_List.setVisibility(View.VISIBLE);
        _etBeneficiaryMobile_Number.setVisibility(View.VISIBLE);

        _etTxnDescription.setVisibility(View.VISIBLE);
        _etAmount.setVisibility(View.VISIBLE);
        _rbPay.setVisibility(View.VISIBLE);
        _rbVerify.setVisibility(View.GONE);
        _btnNext.setVisibility(View.VISIBLE);
        _btnCancel.setVisibility(View.VISIBLE);
        _btnVerifySubmit.setVisibility(View.GONE);
        _btnVerifyCancel.setVisibility(View.GONE);

        _tv_availableLimit.setVisibility(View.GONE);
        _tv_operator.setVisibility(View.GONE);
        _tv_beneficiary_name.setVisibility(View.GONE);
        _tv_account_number.setVisibility(View.GONE);
        _tv_IFSC_Code.setVisibility(View.GONE);
        _tv_BeneficiaryMobile_number.setVisibility(View.GONE);
        _tv_Spinner_PayFrom.setVisibility(View.GONE);
        _tv_amount.setVisibility(View.GONE);
        _tv_txnDescription.setVisibility(View.GONE);
        _tv_ProcessingFees.setVisibility(View.GONE);
        _tv_AmountPayable.setVisibility(View.GONE);
        _tv_OTP.setVisibility(View.GONE);

        _etConsumerNumber.setVisibility(View.VISIBLE);
        _etAvailableLimit.setVisibility(View.VISIBLE);
        _spOperator.setVisibility(View.VISIBLE);
        _etBeneficiaryName.setVisibility(View.VISIBLE);
        _etAccountNumber.setVisibility(View.VISIBLE);
        _etIFSC_Code.setVisibility(View.VISIBLE);
        _etBeneficiaryMobile_Number.setVisibility(View.VISIBLE);

        _etAmount.setVisibility(View.VISIBLE);

        _btnNext.setVisibility(View.VISIBLE);
        _btnCancel.setVisibility(View.VISIBLE);

        _etAmountPayable.setVisibility(View.GONE);
        _etProcessingFees.setVisibility(View.GONE);
        _etOTP.setVisibility(View.GONE);
        _btnSubmit.setVisibility(View.GONE);
        _btnCancel.setVisibility(View.GONE);
        _btnVerifyPaySubmit.setVisibility(View.GONE);
        _btnVerifyPayCancel.setVisibility(View.GONE);

        _btnPaymentPaySubmit.setVisibility(View.GONE);
        _btnPaymentPayCancel.setVisibility(View.GONE);



        _tv_availableLimit.setVisibility(View.GONE);
        _tv_operator.setVisibility(View.GONE);
        _tv_beneficiary_name.setVisibility(View.GONE);
        _tv_account_number.setVisibility(View.GONE);
        _tv_IFSC_Code.setVisibility(View.GONE);
        _tv_BeneficiaryMobile_number.setVisibility(View.GONE);
        _tv_Spinner_PayFrom.setVisibility(View.GONE);
        _tv_amount.setVisibility(View.GONE);
        _tv_txnDescription.setVisibility(View.GONE);
        _tv_ProcessingFees.setVisibility(View.GONE);
        _tv_AmountPayable.setVisibility(View.GONE);
        _tv_OTP.setVisibility(View.GONE);

    }
    public void hideRetrieveBillFields() {

        _etConsumerNumber.setVisibility(View.GONE);
        _etAvailableLimit.setVisibility(View.GONE);
        _spOperator.setVisibility(View.GONE);
        _spPayFrom.setVisibility(View.GONE);
        _etBeneficiaryName.setVisibility(View.GONE);
        _etAccountNumber.setVisibility(View.GONE);
        _etIFSC_Code.setVisibility(View.GONE);
        _etTxnDescription.setVisibility(View.GONE);

        _etBeneficiaryMobile_Number.setVisibility(View.GONE);

        _etAmount.setVisibility(View.GONE);
        _rbPay.setVisibility(View.GONE);
        _rbVerify.setVisibility(View.GONE);
        _btnVerifySubmit.setVisibility(View.GONE);
        _btnVerifyCancel.setVisibility(View.GONE);
        _tv_amount.setVisibility(View.GONE);
        _tv_txnDescription.setVisibility(View.GONE);
        _tv_ProcessingFees.setVisibility(View.GONE);


        _tv_availableLimit.setVisibility(View.GONE);
        _tv_operator.setVisibility(View.GONE);
        _tv_beneficiary_name.setVisibility(View.GONE);
        _tv_account_number.setVisibility(View.GONE);
        _tv_IFSC_Code.setVisibility(View.GONE);
        _tv_BeneficiaryMobile_number.setVisibility(View.GONE);
        _tv_Spinner_PayFrom.setVisibility(View.GONE);
        _tv_amount.setVisibility(View.GONE);
        _tv_txnDescription.setVisibility(View.GONE);
        _tv_ProcessingFees.setVisibility(View.GONE);
        _tv_AmountPayable.setVisibility(View.GONE);
        _tv_OTP.setVisibility(View.GONE);

    }

    public void hideVerifyPaymentFields(){
        _etOTP.setVisibility(View.GONE);
        _etAmountPayable.setVisibility(View.GONE);
        _btnVerifyPaySubmit.setVisibility(View.GONE);
        _btnVerifyPayCancel.setVisibility(View.GONE);
        _btnPaymentPaySubmit.setVisibility(View.GONE);
        _btnPaymentPayCancel.setVisibility(View.GONE);
        _etProcessingFees.setVisibility(View.GONE);
        _etAmount.setVisibility(View.GONE);
        _etAmountPayable.setVisibility(View.GONE);
        _etTxnDescription.setVisibility(View.GONE);
        _btnNext.setVisibility(View.GONE);
        _tvVerified.setVisibility(View.GONE);
        _btnVerifyPaySubmit.setVisibility(View.GONE);
        _btnVerifyPayCancel.setVisibility(View.GONE);
        _etBeneficiaryName.setVisibility(View.GONE);
        _etAccountNumber.setVisibility(View.GONE);
        _etIFSC_Code.setVisibility(View.GONE);
        _etBeneficiaryMobile_Number.setVisibility(View.GONE);
        _btnNewRecharge.setVisibility(View.VISIBLE);
        _tvFullKYC.setVisibility(View.GONE);
        _tvNILLKYC.setVisibility(View.GONE);
        _tv_availableLimit.setVisibility(View.GONE);
        _tv_operator.setVisibility(View.GONE);
        _tv_beneficiary_name.setVisibility(View.GONE);
        _tv_account_number.setVisibility(View.GONE);
        _tv_IFSC_Code.setVisibility(View.GONE);
        _tv_BeneficiaryMobile_number.setVisibility(View.GONE);
        _tv_Spinner_PayFrom.setVisibility(View.GONE);
        _tv_amount.setVisibility(View.GONE);
        _tv_txnDescription.setVisibility(View.GONE);
        _tv_ProcessingFees.setVisibility(View.GONE);
        _tv_AmountPayable.setVisibility(View.GONE);
        _tv_OTP.setVisibility(View.GONE);
        _btnOTPResend.setVisibility(View.GONE);


    }


    @Override
    public void onTaskSuccess(TransactionRequest result, DataExImpl.Methods callback) {
        Log.d(_LOG, "In onTaskSuccess");

        switch (callback) {

            case IMPS_AUTHENTICATION:
                Log.d(_LOG, "onTaskSuccess done_ImpsAuthentication");
                showProgress(false);

                if (result.getCustom() == null) {
                    Log.d(_LOG, "response is in incorrect format!");
                    showMessage("Couldn't Authenticate.Try Again Later");
                    showProgress(false);
                    return;
                }


                ImpsAuthenticationResult impsAuthenticationResult = (ImpsAuthenticationResult) result.getCustom();
                if (impsAuthenticationResult.isIMPSActive && impsAuthenticationResult.isIMPSServiceAllowed) {
                    Log.d(_LOG, "Both true");
                   _etConsumerNumber.setVisibility(View.VISIBLE);
                    _btnSubmit.setVisibility(View.VISIBLE);


                }

                else if (!impsAuthenticationResult.isIMPSServiceAllowed) {
                    Log.d(_LOG, "You are not authorised to perform IMPS");
                    showMessage("You are not authorised to perform IMPS");
                }

                else if (impsAuthenticationResult.isIMPSServiceAllowed && !impsAuthenticationResult.isIMPSActive) {
                    Log.d(_LOG, "Service Temporary Unavailable.Please try Later");
                    showMessage("Service Temporary Unavailable.Please try Later");
                }

               // showMessage(String.valueOf(impsAuthenticationResult.isIMPSActive));
                break;

            case IMPS_AUTHENTICATION_MOM:
                Log.d(_LOG, "onTaskSuccess done_ImpsAuthentication");


                if (result.getRemoteResponse() == null) {
                    Log.d(_LOG, "response is in incorrect format!");
                    showMessage("Couldn't Authenticate.Try Again Later");
                    showProgress(false);
                    return;
                }

               if(result.getRemoteResponse().equals("true")){
                   Log.d(_LOG, "true");
                   _etConsumerNumber.setVisibility(View.VISIBLE);
                   _btnSubmit.setVisibility(View.VISIBLE);
               }
               else if (result.getRemoteResponse().equals("false")){
                   Log.d(_LOG, "False");
                   showMessage("You are not authorised to perform IMPS");
               }

                   break;

            case IMPS_CUSTOMER_REGISTRATION:

                showProgress(false);
              //  Log.i(_LOG + "IMPS_CUSTOMER_REGISTRATION", result.getRemoteResponse() + result.getResponseCode());
                Log.i(_LOG + "IMPS_CUSTOMER_REGISTRATION", result.getRemoteResponse());
                String isIMPSAuthenticate = EphemeralStorage.getInstance(getActivity()).getString(AppConstants.PARAM_NEW_MOM_CUSTOMER_STATUS_IS_IMPS_AUTHORISED, null);;
                if(result.getRemoteResponse().equals("true") && isIMPSAuthenticate.equals("true") )  {
                    Log.d(_LOG, "Both true");
                    checkKYC();
                    getBeneficiaryList();
                    getRefundDetails();
                    setupIMPSTransferView();
                }
                else if(result.getRemoteResponse().equals("false") && isIMPSAuthenticate.equals("false")){
                    Log.d(_LOG, "Both false");
                    showCustomRegistrationFields();
                }
                else if(result.getRemoteResponse().equals("false") && isIMPSAuthenticate.equals("true")){
                    Log.d(_LOG, "not registered but service allowed");
                    showCustomRegistrationFields();
                }
                else if(result.getRemoteResponse().equals("true") && isIMPSAuthenticate.equals("false")){
                    Log.d(_LOG, "customer already registered but imps not allowed");
                    showMessage("You are not allowed to perform IMPS");
                    _etConsumerNumber.setEnabled(true);
                }
//                if (!(result.getCustom() instanceof ImpsCustomerRegistrationResult)) {
//                    Log.d(_LOG, "response is in incorrect format!");
//                    showMessage(getActivity().getString(R.string.error_imps_customer_registration));
//                    _etConsumerNumber.setEnabled(true);
//                    return;
//                }
//
//                ImpsCustomerRegistrationResult registrationResult = (ImpsCustomerRegistrationResult) result.getCustom();
//
//
//                showProgress(false);
//                if (registrationResult.isRegistered && registrationResult.isIMPSServiceAllowed) {
//                    Log.d(_LOG, "Both true");
//                    checkKYC();
//                    getBeneficiaryList();
//                    setupIMPSTransferView();
//
//                } else if (!registrationResult.isRegistered && !registrationResult.isIMPSServiceAllowed) {
//                    Log.d(_LOG, "Both false");
//                    showCustomRegistrationFields();
//                } else if (!registrationResult.isRegistered && registrationResult.isIMPSServiceAllowed) {
//                    Log.d(_LOG, "not registered but service allowed");
//                    showCustomRegistrationFields();
//                } else if (registrationResult.isRegistered && !registrationResult.isIMPSServiceAllowed) {
//                    Log.d(_LOG, "customer already registered but imps not allowed");
//                    showMessage("You are not allowed to perform IMPS");
//                }

                break;

            case IMPS_BENEFICIARY_LIST:
                Log.d(_LOG, "onTaskSuccess done_Beneficiary");

                if (result == null) {
                    Log.d(_LOG, "response is in incorrect format!");
                    showMessage("Couldn't fetch BeneficiaryList.");
                    showProgress(false);
                    return;
                }


                showProgress(false);

                EphemeralStorage.getInstance(getActivity()).getBeneficiaryListDetail();
                List<String> beneficiarylistDetail =   EphemeralStorage.getInstance(getActivity()).getBeneficiaryListDetail();

//                List<BeneficiaryResult> beneficiaryResultsList = null;
//                beneficiaryResultsList = (List<BeneficiaryResult>) result.getCustom();
//
//
//                beneficiaryResultsList.add(0, new BeneficiaryResult("", getActivity().getString(R.string.prompt_spinner_select_NewBeneficiary)));
//                ArrayAdapter<BeneficiaryResult> dataAdapter = new ArrayAdapter<BeneficiaryResult>(
//                        getActivity(), android.R.layout.simple_spinner_item,
//                        beneficiaryResultsList
//                );
                ArrayAdapter dataAdapter = new ArrayAdapter (
                        getActivity(), android.R.layout.simple_spinner_item,
                        beneficiarylistDetail

                );

                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                _spOperator.setAdapter(dataAdapter);
                _spOperator.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        showMessage(null);
                        _btnNext.setEnabled(true);
                        if (_spOperator.getSelectedItemPosition() == 0) {
                            _etBeneficiaryName.setEnabled(true);
                            _etAccountNumber.setEnabled(true);
                            _etIFSC_Code.setEnabled(true);
                            _etBeneficiaryMobile_Number.setEnabled(true);
                            _etBeneficiaryName.setText(null);
                            _etAccountNumber.setText(null);
                            _etIFSC_Code.setText(null);
                            _etBeneficiaryMobile_Number.setText(null);
                            _etAmount.setText(null);
                            _etTxnDescription.setText(null);


                            _rbPay.setVisibility(View.VISIBLE);
                            _rbVerify.setVisibility(View.VISIBLE);
                            _tvVerified.setVisibility(View.GONE);

                        } else if (_spOperator.getSelectedItemPosition() != 0) {
                            _rbPay.setVisibility(View.VISIBLE);
                            _rbVerify.setVisibility(View.VISIBLE);
                            _rbPay.setChecked(true);


//                            BeneficiaryResult beneficiaryResult = (BeneficiaryResult) _spOperator.getSelectedItem();
//
//                            String sBeneficiaryId = beneficiaryResult.id;
//                            String sBeneficiaryName = beneficiaryResult.name;
//
//                            EphemeralStorage.getInstance(getActivity()).storeString(
//                                    AppConstants.PARAM_PBX_IMPS_BENEFICIARY_ACCOUNT_NAME,
//                                    beneficiaryResult.name);
//                            EphemeralStorage.getInstance(getActivity()).storeString(
//                                    AppConstants.PARAM_PBX_IMPS_BENEFICIARY_ID,
//                                    beneficiaryResult.id);
                            //  showMessage(sBeneficiaryId);
                            _etAmount.setText(null);
                            _etTxnDescription.setText(null);
                            _spPayFrom.setSelection(0);
                            _tv_amount.setVisibility(View.VISIBLE);
                            _tv_txnDescription.setVisibility(View.VISIBLE);
                            _tv_Spinner_PayFrom.setVisibility(View.VISIBLE);
                            _etAmount.setVisibility(View.VISIBLE);
                            _etTxnDescription.setVisibility(View.VISIBLE);
                            _spPayFrom.setVisibility(View.VISIBLE);

                            _rbVerify.setChecked(false);
                            String sBenneficiaryAlias =  EphemeralStorage.getInstance(getActivity()).getBeneficiaryList(_spOperator.getSelectedItem().toString());
                            String sBenneficiaryAliasName =_spOperator.getSelectedItem().toString();
                            Log.e("BenefiaciaryAlias" , sBenneficiaryAliasName);
                            getIMPSBeneficiaryDetails(sBenneficiaryAliasName);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });


                break;



            case IMPS_REFUND_DETAILS:
                Log.d(_LOG, "onTaskSuccess done_RefundDetails");

                if (result == null) {
                    Log.d(_LOG, "response is in incorrect format!");
                    showMessage("Couldn't fetch Refund Details.");
                    showProgress(false);
                    return;
                }


                showProgress(false);

                EphemeralStorage.getInstance(getActivity()).getRefundTransactionDescriptionList();
                List<String> refundListDetail =   EphemeralStorage.getInstance(getActivity()).getRefundTransactionDescriptionList();


                ArrayAdapter refundDataAdapter = new ArrayAdapter (
                        getActivity(), android.R.layout.simple_spinner_item,
                        refundListDetail

                );

                refundDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                _spPayFrom.setAdapter(refundDataAdapter);
                _spPayFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        showMessage(null);
                        if(_spPayFrom.getSelectedItemPosition()!= 0){
                            String sRefundAmount = EphemeralStorage.getInstance(getActivity()).getRefundMapAmount(_spPayFrom.getSelectedItem().toString());
                            Log.e("RefundAmount" ,EphemeralStorage.getInstance(getActivity()).getRefundMapAmount(_spPayFrom.getSelectedItem().toString()));
                            Log.e("REfundReceiptId",EphemeralStorage.getInstance(getActivity()).getRefundReceiptIdMap(_spPayFrom.getSelectedItem().toString()));
                            String sRefundReceiptID = EphemeralStorage.getInstance(getActivity()).getRefundReceiptIdMap(_spPayFrom.getSelectedItem().toString());
                            EphemeralStorage.getInstance(getActivity()).storeString(AppConstants.PARAM_MOM_IMPS_REFUND_RECEIPT_ID_RESPONSE , sRefundReceiptID);
                            double d = Double.parseDouble(sRefundAmount);
                            Log.e("ValueRefundAmount" , String.valueOf(d));
                            int refundAmount = ((int) d);
                            Log.e("ValueIntRefundAmountNew" , String.valueOf(refundAmount));
                            _etAmount.setText(String.valueOf(refundAmount));
                            _etAmount.setEnabled(false);

                        }
                        else{
                            _etAmount.setText(null);
                            _etAmount.setEnabled(true);
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });


                break;

            case IMPS_CREATE_CUSTOMER_REGISTRATION:
                Log.d(_LOG, "onTaskSuccess done_CreateCustomer");

                showProgress(false);
//                if (result.getCustom() == null) {
//                    Log.d(_LOG, "response is in incorrect format!");
//                    showMessage("Couldn't Create Customer.");
//                    showProgress(false);
//                    return;
//                }

                String registrationStatus = result.getRemoteResponse();
                Log.e("Registration REsponse" , registrationStatus);
//                ImpsCreateCustomerResult impsCreateCustomerResult = (ImpsCreateCustomerResult) result.getCustom();
//
//                showMessage(impsCreateCustomerResult.ErrorMessage);
//                if(impsCreateCustomerResult.RegistrationStatus){
//                    _etConsumerName.setVisibility(View.GONE);
//                    _etDob.setVisibility(View.GONE);
//                    _etEmailAddress.setVisibility(View.GONE);
//                    _btnImpsCreateCustomer.setVisibility(View.GONE);
//                    _btnImpsCancel.setVisibility(View.GONE);
//                    _btnSubmit.setVisibility(View.VISIBLE);
//                    _etConsumerNumber.setEnabled(true);
//
//                }

                showMessage(registrationStatus);

                if( EphemeralStorage.getInstance(getActivity()).getString(AppConstants.PARAM_NEW_MOM_REGISTER_CUSTOMER_REGISTRATION_STATUS , null).equals("true")){
                    _etConsumerName.setVisibility(View.GONE);
                    _etDob.setVisibility(View.GONE);
                    _etEmailAddress.setVisibility(View.GONE);
                    _btnImpsCreateCustomer.setVisibility(View.GONE);
                    _btnImpsCancel.setVisibility(View.GONE);
                    _btnSubmit.setVisibility(View.VISIBLE);
                    _etConsumerNumber.setEnabled(true);

                }
                break;

            case IMPS_CHECK_KYC:
                Log.d(_LOG, "onTaskSuccess done_CreateCustomer");
                Log.i("AvailableLimit" , result.getRemoteResponse());


                if (result.getRemoteResponse() == null) {
                    Log.d(_LOG, "response is in incorrect format!");
                    showMessage(getResources().getString(R.string.error_imps_kyc));
                    showProgress(false);
                    return;
                }


              Boolean kycStatus = EphemeralStorage.getInstance(getActivity()).getBoolean(AppConstants.PARAM_NEW_MOM_CHECK_KYC_isFullKYC , false);
                Log.i("KycStatus" , String.valueOf(kycStatus));
                _etAvailableLimit.setText(result.getRemoteResponse());
                 if( kycStatus){
                     _tvFullKYC.setVisibility(View.VISIBLE);
                 }
                else {
                     _tvNILLKYC.setVisibility(View.VISIBLE);
                 }
                break;

            case IMPS_RESET_IPIN:
                Log.i(_LOG + "IMPS_MOM_RESET_IPIN", result.getRemoteResponse());
                showProgress(false);
                  if(result.getRemoteResponse().equals("true")){
                        Toast.makeText(getActivity(),"Your IPIN has been Reset",Toast.LENGTH_LONG).show();
                }
                else{
                      Toast.makeText(getActivity(),"Your IPIN has not been Reset.Please try Later",Toast.LENGTH_LONG).show();
                  }
               // showMessage(result.getRemoteResponse());

                break;

            case IMPS_BENEFICIARY_DETAILS:
                Log.d(_LOG, "onTaskSuccess done_BeneficiaryDetails");

                showProgress(false);

                Boolean isBeneficiaryVerified = EphemeralStorage.getInstance(getActivity()).getBoolean(AppConstants.PARAM_NEW_MOM_IMPS_BENEFICIARY_VERIFICATION_STATUS, false);

                if(isBeneficiaryVerified){
                    _rbPay.setChecked(true);
                    _tvVerified.setVisibility(View.VISIBLE);
                    _rbVerify.setVisibility(View.GONE);
                }

                else if (!isBeneficiaryVerified) {
                    _tvVerified.setVisibility(View.GONE);
                    _rbVerify.setVisibility(View.VISIBLE);
                }
                String sBeneficiaryName = EphemeralStorage.getInstance(getActivity()).getString(AppConstants.PARAM_NEW_MOM_IMPS_BENEFICIARY_ACCOUNT_NAME,null);
                Log.e("BeneficiaryName" , sBeneficiaryName);
                String sBeneficiaryAccountNumber = EphemeralStorage.getInstance(getActivity()).getString(AppConstants.PARAM_NEW_MOM_IMPS_BENEFICIARY_ACCOUNT_NUMBER, null);
                Log.e("BeneficiaryAccountNumber" , sBeneficiaryAccountNumber);
                String sBeneficiaryIFSCCode  = EphemeralStorage.getInstance(getActivity()).getString(AppConstants.PARAM_NEW_MOM_IMPS_BENEFICIARY_IFSC_CODE, null);
                Log.e("BeneficiaryIFSCCode" , sBeneficiaryIFSCCode);
                String sBeneficiaryMobileNumber = EphemeralStorage.getInstance(getActivity()).getString(AppConstants.PARAM_NEW_MOM_IMPS_BENEFICIARY_MOBILE_NUMBER, null);
                Log.e("BeneficiaryMobileNumber" , sBeneficiaryMobileNumber);
                _etBeneficiaryName.setText(sBeneficiaryName);
                _etBeneficiaryName.setEnabled(false);
                _etAccountNumber.setText(sBeneficiaryAccountNumber);
                _etAccountNumber.setEnabled(false);
                _etIFSC_Code.setText(sBeneficiaryIFSCCode);
                _etIFSC_Code.setEnabled(false);
                _etBeneficiaryMobile_Number.setText(sBeneficiaryMobileNumber);
                _etBeneficiaryMobile_Number.setEnabled(false);
                break;

            case IMPS_ADD_BENEFICIARY:
                if (result.getRemoteResponse() == null) {
                    Log.d(_LOG, "response is in incorrect format!");
                    showMessage(getResources().getString(R.string.error_imps_add_beneficiary));
                    showProgress(false);
                    return;
                }
                Log.d(_LOG, "onTaskSuccess done_AddBeneficiary");
                showMessage(result.getRemoteResponse());
                Toast.makeText(getActivity().getApplicationContext(), result.getRemoteResponse(), Toast.LENGTH_LONG).show();

               Boolean registrationBeneficiaryStatus = EphemeralStorage.getInstance(getActivity()).getBoolean(AppConstants.PARAM_NEW_MOM_IMPS_ADD_BENEFICIARY_REGISTRATION_STATUS , false);

                if (registrationBeneficiaryStatus) {
                    getBeneficiaryList();
                }
                else{
                   _btnNext.setEnabled(true);
                }


                break;


            case IMPS_BANK_NAME_LIST:
                Log.d(_LOG, "onTaskSuccess done_BankNameList");

                List<String> bankNameResultList = EphemeralStorage.getInstance(getActivity()).getBankList();



                ArrayAdapter dataAdapterBankList = new ArrayAdapter(
                        getActivity(), android.R.layout.simple_spinner_item,
                        bankNameResultList
                );

                dataAdapterBankList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                _spOperator1.setAdapter(dataAdapterBankList);
                _spOperator1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                            _spOperator2.setAdapter(null);
                            _spOperator3.setAdapter(null);
                            _spOperator4.setAdapter(null);
                      //  EphemeralStorage.getInstance(getActivity()).getBranchList("");

                            getStateName(_spOperator1.getSelectedItem().toString());

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });


                break;

            case IMPS_STATE_NAME:
                Log.d(_LOG, "onTaskSuccess done_StateNameList");


                List<String> stateNameResultList =  EphemeralStorage.getInstance(getActivity()).getStateList();



                ArrayAdapter dataAdapterStateList = new ArrayAdapter(
                        getActivity(), android.R.layout.simple_spinner_item,
                        stateNameResultList
                );

                dataAdapterStateList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                _spOperator2.setAdapter(dataAdapterStateList);
                _spOperator2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        _spOperator3.setAdapter(null);
                        _spOperator4.setAdapter(null);
                     //   EphemeralStorage.getInstance(getActivity()).getBranchList("");
                        getCityName(_spOperator1.getSelectedItem().toString(), _spOperator2.getSelectedItem().toString());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                break;

            case IMPS_CITY_NAME:
                Log.d(_LOG, "onTaskSuccess done_CityNameList");


                List cityNameResultList = EphemeralStorage.getInstance(getActivity()).getCityList();



                ArrayAdapter dataAdapterCityList = new ArrayAdapter(
                        getActivity(), android.R.layout.simple_spinner_item,
                        cityNameResultList
                );

                dataAdapterCityList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                _spOperator3.setAdapter(dataAdapterCityList);
                _spOperator3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        _spOperator4.setAdapter(null);
                      //  EphemeralStorage.getInstance(getActivity()).getBranchList("");
                        getBranchName(_spOperator1.getSelectedItem().toString(), _spOperator2.getSelectedItem().toString()
                                , _spOperator3.getSelectedItem().toString());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                break;

            case IMPS_BRANCH_NAME:
                Log.d(_LOG, "onTaskSuccess done_BranchNameList");
//
                List<String> branchNameResultList =  EphemeralStorage.getInstance(getActivity()).getBranchListDetail();



                ArrayAdapter  dataAdapterBranchList = new ArrayAdapter(
                        getActivity(), android.R.layout.simple_spinner_item,
                        branchNameResultList
                );

                dataAdapterBranchList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                _spOperator4.setAdapter(dataAdapterBranchList);
                _spOperator4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                           String ifscCode = EphemeralStorage.getInstance(getActivity()).getBranchList(_spOperator4.getSelectedItem().toString());
                            sBranchIfscCode = ifscCode;
                            //         Toast.makeText(getActivity().getApplicationContext(), sBranchIfscCode, Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });


                break;


            case IMPS_MOM_SEVICE_CHARGE_PROCESS:
                Log.i(_LOG + "IMPS_MOM_SEVICE_CHARGE_PROCESS", result.getRemoteResponse());

                String serviceCharge = result.getRemoteResponse();
                double d = Double.parseDouble(serviceCharge);
                Log.e("ValueNew" , String.valueOf(d));
                int service = ((int) d);
                Log.e("ValueIntNew" , String.valueOf(service));
                EphemeralStorage.getInstance(getActivity()).storeInt(AppConstants.PARAM_NEW_IMPS_SERVICE_CHARGE, service);
                Log.d(_LOG, "Starting navigation to TxnMsg Activity");
                if (_rbPay.isChecked()) {

                    Log.d(_LOG, "true");
                    _etAmountPayable.setVisibility(View.GONE);

                    _etOTP.setVisibility(View.VISIBLE);
                    _etOTP.setText(null);
                    int amount = Integer.parseInt(_etAmount.getText().toString());
                    //  int processFees = 10;
                    int processFees =  EphemeralStorage.getInstance(getActivity()).getInt(AppConstants.PARAM_NEW_IMPS_SERVICE_CHARGE, -1);
                    Log.e("_rbPayProcessFee" , String.valueOf(processFees));
                    int amountPayable = amount+processFees;
                    _etProcessingFees.setText(String.valueOf(processFees));
                    _etAmountPayable.setText(String.valueOf(amountPayable));
                    _etAmount.setEnabled(false);
                    _etProcessingFees.setEnabled(false);
                    _etAmountPayable.setEnabled(false);

                    showConfirmPaymentFields();


                }
                else if ((_rbVerify.isChecked())) {
                    Log.d(_LOG, "true");
                    _etAmountPayable.setVisibility(View.GONE);

                    _etOTP.setVisibility(View.VISIBLE);
                    _etOTP.setText(null);

                    //hideRetrieveBillFields();
                    showVerifyPaymentFields();
                }
                break;

            case IMPS_VERIFY_PROCESS:


                Log.i(_LOG + "IMPS_VERIFY_PROCESS", result.getRemoteResponse() + result.getResponseCode());



                ImpsVerifyProcessResult impsVerifyProcessResult = (ImpsVerifyProcessResult) result.getCustom();


                if (impsVerifyProcessResult.PostingStatus) {
                    Log.d(_LOG, "true");
                    _etAmountPayable.setVisibility(View.VISIBLE);
                    _etOTP.setText(null);


                    showVerifyPaymentFields();


                } else if (!impsVerifyProcessResult.PostingStatus) {
                    Log.d(_LOG, "Both false");
                    setupIMPSTransferView();
                    showMessage(impsVerifyProcessResult.ErrorMessage);
                } else {
                    showMessage("Couldn't process");
                }

                showProgress(false);
                break;

            case IMPS_VERIFY_PAYMENT:

                if (result.getCustom() == null) {
                    showProgress(false);
                    showMessage(getResources().getString(R.string.lic_failed_msg_default));
                    hideVerifyPaymentFields();
                    return;
                }

                if(result.getResponseCode()== -1){
                    showProgress(false);
                    hideVerifyPaymentFields();
                    showMessage(getActivity().getString(R.string.lic_failed_msg_default));
                    //  Toast.makeText(getActivity().getApplicationContext(),String.valueOf(result.getResponseCode()) , Toast.LENGTH_LONG).show();
                    return;
                }

                    showBalance();
                    Log.i(_LOG + "IMPS_VERIFY_PAYMENT", result.getRemoteResponse() + result.getResponseCode());


                    ImpsVerifyPaymentResult impsVerifyPaymentResult = (ImpsVerifyPaymentResult) result.getCustom();

                    showMessage("Message: " + " " + impsVerifyPaymentResult.errorMessage + "\n"
                                    + "Receipt ID: " + impsVerifyPaymentResult.recieptId + "\n"
                                    + "Transaction ID:" + impsVerifyPaymentResult.transactionId + "\n"
                    );
                    hideVerifyPaymentFields();
                    _etOTP.setText(null);

                showProgress(false);

                break;

            case IMPS_PAYMENT_PROCESS:

                Log.i(_LOG + "IMPS_PAYMENT_PROCESS", result.getRemoteResponse() + result.getResponseCode());

                ImpsPaymentProcessResult impsPaymentProcessResult = (ImpsPaymentProcessResult) result.getCustom();

                showProgress(false);

                if (impsPaymentProcessResult.PostingStatus) {
                    Log.d(_LOG, "true");

                    _etOTP.setText(null);
                    int amount = Integer.parseInt(_etAmount.getText().toString());
                  //  int processFees = 10;
                    int processFees =  EphemeralStorage.getInstance(getActivity()).getInt(AppConstants.PARAM_NEW_IMPS_SERVICE_CHARGE, -1);
                    int amountPayable = amount + processFees;
                    _etProcessingFees.setText(String.valueOf(processFees));
                    _etAmountPayable.setText(String.valueOf(amountPayable));
                    _etAmount.setEnabled(false);
                    _etProcessingFees.setEnabled(false);
                    _etAmountPayable.setEnabled(false);


                    showConfirmPaymentFields();


                } else if (!impsPaymentProcessResult.PostingStatus) {
                    Log.d(_LOG, "Both false");

                    showMessage(impsPaymentProcessResult.ErrorMessage);
                }
                break;

            case IMPS_CONFIRM_PAYMENT:

                if (result.getCustom() == null) {
                    showMessage(getResources().getString(R.string.lic_failed_msg_default));
                    showProgress(false);
                    hideVerifyPaymentFields();
                    return;
                }

                   if(result.getResponseCode()== -1){
                      showProgress(false);
                      hideVerifyPaymentFields();
                      showMessage("Transaction Under Process");
                    //  Toast.makeText(getActivity().getApplicationContext(),String.valueOf(result.getResponseCode()) , Toast.LENGTH_LONG).show();
                      return;
                  }

//                   else{


                showBalance();
                List<ImpsConfirmPaymentResult> impsConfirmPaymentResultList = null;

                impsConfirmPaymentResultList = (List<ImpsConfirmPaymentResult>) result.getCustom();
//                if(impsConfirmPaymentResultList.equals(null)){
//                    Toast.makeText(getActivity().getApplicationContext(),"TestError" , Toast.LENGTH_LONG).show();
//                }

                showMessage(null);


                Log.d("IMPS_CONFIRM_PAYMENT", "Obtained result: " + impsConfirmPaymentResultList);

                if (impsConfirmPaymentResultList == null || impsConfirmPaymentResultList.size() < 1 || impsConfirmPaymentResultList.contains("[null]")) {
                    setNoPaymentHistoryMessage();
                    return;
                }

                Log.d("IMPS_CONFIRM_PAYMENT", "Creating list with " + impsConfirmPaymentResultList.size() + " transactions");

                ConfirmPaymentTextListViewAdapter adapter = new ConfirmPaymentTextListViewAdapter(
                        getActivity(),
                        R.layout.listview_impsconfirm_payment,
                        impsConfirmPaymentResultList
                );

                _titleView.setText(String.format(getResources().getString(R.string.title_activity_transaction_history_count), impsConfirmPaymentResultList.size()));

                _listView.setAdapter(adapter);
                _listView.setVisibility(View.VISIBLE);

                Log.d("HISTORY", "ListView created");
//            }
                showProgress(false);
                hideVerifyPaymentFields();
                break;


            case IMPS_MOM_SUBMIT_PROCESS:
                //showMessage(null);
                _etOTP.setText(null);

                Log.i(_LOG + "IMPS_MOM_SUBMIT_PROCESS", result.getRemoteResponse());


                showProgress(false);

                if ((result.getRemoteResponse().equals("true")) && (_rbPay.isChecked())) {
                    Log.d(_LOG, "true");
                    _etAmountPayable.setVisibility(View.GONE);

                    _etOTP.setVisibility(View.VISIBLE);
                    _etOTP.setText(null);
                    int amount = Integer.parseInt(_etAmount.getText().toString());
                  //  int processFees = 10;
                    int processFees =  EphemeralStorage.getInstance(getActivity()).getInt(AppConstants.PARAM_NEW_IMPS_SERVICE_CHARGE, -1);
                    int amountPayable = amount+processFees;
                    _etProcessingFees.setText(String.valueOf(processFees));
                    _etAmountPayable.setText(String.valueOf(amountPayable));
                    _etAmount.setEnabled(false);
                    _etProcessingFees.setEnabled(false);
                    _etAmountPayable.setEnabled(false);

                    showConfirmPaymentFields();


                }
                else if ((result.getRemoteResponse().equals("true")) && (_rbVerify.isChecked())) {
                    Log.d(_LOG, "true");
                _etAmountPayable.setVisibility(View.GONE);

                _etOTP.setVisibility(View.VISIBLE);
                _etOTP.setText(null);

                //hideRetrieveBillFields();
                showVerifyPaymentFields();
        }
                else {

                    showMessage("Couldn't fetch the response.Try again");
                }
                break;


            case IMPS_MOM_CONFIRM_PROCESS:
                Log.i(_LOG + "IMPS_MOM_CONFIRM_PROCESS", result.getRemoteResponse());
                showProgress(false);
                showBalance();
                showMessage(result.getRemoteResponse());
                hideVerifyPaymentFields();
                break;
            case IMPS_MOM_CONFIRM_PROCESS_ByREFUND:
                Log.i(_LOG + "IMPS_MOM_CONFIRM_PROCESS_BY_REFUND", result.getRemoteResponse());
                showProgress(false);
                showBalance();
                showMessage(result.getRemoteResponse());
                hideVerifyPaymentFields();
                break;


            case IMPS_MOM_SUBMIT_PAY_PROCESS:
                showMessage(null);
                _etOTP.setText(null);


                Log.i(_LOG + "IMPS_MOM_SUBMIT_PROCESS", result.getRemoteResponse());

                showProgress(false);

                if (result.getRemoteResponse().equals("true")) {
                    Log.d(_LOG, "true");
                    _etAmountPayable.setVisibility(View.GONE);

                    _etOTP.setVisibility(View.VISIBLE);
                    _etOTP.setText(null);
                    int amount = Integer.parseInt(_etAmount.getText().toString());
                    //int processFees = 10;
                    int processFees =  EphemeralStorage.getInstance(getActivity()).getInt(AppConstants.PARAM_NEW_IMPS_SERVICE_CHARGE, -1);
                    int amountPayable = amount+processFees;
                    _etProcessingFees.setText(String.valueOf(processFees));
                    _etAmountPayable.setText(String.valueOf(amountPayable));
                    _etAmount.setEnabled(false);
                    _etProcessingFees.setEnabled(false);
                    _etAmountPayable.setEnabled(false);
                    //hideRetrieveBillFields();
                    showVerifyPaymentFields();

                }
                else {

                    showMessage("Couldn't fetch the response.Try again");
                }
                break;

//            case IMPS_MOM_CONFIRM_PAY_PROCESS:
//                Log.i(_LOG + "IMPS_MOM_CONFIRM_PROCESS", result.getRemoteResponse());
//                showProgress(false);
//                showMessage(result.getRemoteResponse());
//                hideVerifyPaymentFields();
//                break;



        }


        Log.d(_LOG, "onTaskSuccess done");
    }

    @Override
    public void onTaskError(AsyncResult pResult, DataExImpl.Methods callback) {
        Log.d(_LOG, "In onTaskError");
        switch (callback) {
            case IMPS_CUSTOMER_REGISTRATION:

                showMessage(getActivity().getString(R.string.error_imps_customer_registration));
                break;
            case IMPS_BENEFICIARY_LIST:
                showMessage(getActivity().getString(R.string.error_imps_beneficiary_List));
                break;

            case IMPS_CREATE_CUSTOMER_REGISTRATION:
              //  showMessage(getActivity().getString(R.string.error_imps_customer_creation));
                break;

            case IMPS_BENEFICIARY_DETAILS:
                showMessage(getActivity().getString(R.string.error_imps_beneficiary_Details));
                break;

            case IMPS_ADD_BENEFICIARY:
                showMessage(getActivity().getString(R.string.error_imps_add_beneficiary));
                break;

            case IMPS_BANK_NAME_LIST:
                showMessage(getActivity().getString(R.string.error_imps_bank_List));

                break;

            case IMPS_STATE_NAME:
                showMessage(getActivity().getString(R.string.error_imps_state_List));
                break;

            case IMPS_CITY_NAME:
                showMessage(getActivity().getString(R.string.error_imps_city_List));
                break;
            case IMPS_BRANCH_NAME:
                showMessage(getActivity().getString(R.string.error_imps_branch_List));
                break;

            case IMPS_VERIFY_PROCESS:
                showMessage(getActivity().getString(R.string.error_tryAgain));
                break;
            case IMPS_VERIFY_PAYMENT:
                showMessage(getActivity().getString(R.string.error_tryAgain));
                break;
            case IMPS_PAYMENT_PROCESS:
                showMessage(getActivity().getString(R.string.error_tryAgain));
                break;
            case IMPS_CONFIRM_PAYMENT:
                showMessage(getActivity().getString(R.string.error_tryAgain));
                break;
            case IMPS_MOM_SUBMIT_PROCESS:
                showMessage(getActivity().getString(R.string.error_tryAgain));
                break;
            case IMPS_MOM_CONFIRM_PROCESS:
                showMessage(getActivity().getString(R.string.error_tryAgain));
                break;
            case IMPS_MOM_SUBMIT_PAY_PROCESS:
                break;
            case IMPS_MOM_CONFIRM_PAY_PROCESS:
                break;
            case IMPS_AUTHENTICATION:
                break;
            case IMPS_AUTHENTICATION_MOM:
                break;
        }
        showProgress(false);
        Log.d(_LOG, "onTaskError done");
    }


    public class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

            BeneficiaryResult beneficiaryResult = (BeneficiaryResult) _spOperator.getSelectedItem();
            String sBeneficiaryId = beneficiaryResult.id;
            String sBeneficiaryName = beneficiaryResult.name;
            EphemeralStorage.getInstance(getActivity()).storeString(
                    AppConstants.PARAM_PBX_IMPS_BENEFICIARY_ACCOUNT_NAME,
                    beneficiaryResult.name);
            EphemeralStorage.getInstance(getActivity()).storeString(
                    AppConstants.PARAM_PBX_IMPS_BENEFICIARY_ID,
                    beneficiaryResult.id);
            showMessage(sBeneficiaryId);
            getIMPSBeneficiaryDetails(sBeneficiaryName);
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {

        }
    }




}


