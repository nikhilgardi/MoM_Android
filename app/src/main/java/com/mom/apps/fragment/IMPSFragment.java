package com.mom.apps.fragment;

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
import com.mom.apps.R;
import com.mom.apps.identifier.IdentifierUtils;
import com.mom.apps.identifier.PlatformIdentifier;
import com.mom.apps.identifier.TransactionType;
import com.mom.apps.model.AsyncListener;
import com.mom.apps.model.AsyncResult;
import com.mom.apps.model.DataExImpl;
import com.mom.apps.model.IDataEx;
import com.mom.apps.model.local.EphemeralStorage;
import com.mom.apps.model.pbxpl.BankNameResult;
import com.mom.apps.model.pbxpl.BeneficiaryResult;
import com.mom.apps.model.pbxpl.BranchNameResult;
import com.mom.apps.model.pbxpl.CityNameResult;
import com.mom.apps.model.pbxpl.ImpsAddBeneficiaryResult;
import com.mom.apps.model.pbxpl.ImpsBeneficiaryDetailsResult;
import com.mom.apps.model.pbxpl.ImpsCheckKYCResult;
import com.mom.apps.model.pbxpl.ImpsConfirmPaymentResult;
import com.mom.apps.model.pbxpl.ImpsCreateCustomerResult;
import com.mom.apps.model.pbxpl.ImpsCustomerRegistrationResult;
import com.mom.apps.model.pbxpl.ImpsPaymentProcessResult;
import com.mom.apps.model.pbxpl.ImpsVerifyPaymentResult;
import com.mom.apps.model.pbxpl.ImpsVerifyProcessResult;
import com.mom.apps.model.pbxpl.StateNameResult;
import com.mom.apps.ui.TransactionRequest;
import com.mom.apps.utils.AppConstants;
import com.mom.apps.widget.ConfirmPaymentTextListViewAdapter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IMPSFragment extends FragmentBase implements AsyncListener<TransactionRequest> {

    private String _LOG = AppConstants.LOG_PREFIX + "IMPS";

    Button _btnGetBillAmount;
    private EditText _etSubscriberId;
    private EditText _etAmount;
    private EditText _etTxnDescription;
    private EditText _etAvailableLimit;
    private EditText _etConsumerNumber;
    private EditText _etIFSC_Code;
    private EditText _etBeneficiaryMobile_Number;
    private TextView _tvGetIFSC_List;
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
    Spinner _spOperator;
    Spinner _spOperator1;
    Spinner _spOperator2;
    Spinner _spOperator3;
    Spinner _spOperator4;
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
        _etTxnDescription           = (EditText) view.findViewById(R.id.txnDescription);
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
        _tvVerified                 = (TextView) view.findViewById(R.id.Verified);
        _tv_availableLimit          = (TextView) view.findViewById(R.id.tv_availableLimit);
        _tv_operator                = (TextView) view.findViewById(R.id.tv_operator);
        _tv_beneficiary_name        = (TextView) view.findViewById(R.id.tv_beneficiary_name);
        _tv_account_number          = (TextView) view.findViewById(R.id.tv_account_number);
        _tv_IFSC_Code               = (TextView) view.findViewById(R.id.tv_IFSC_Code);
        _tv_BeneficiaryMobile_number= (TextView) view.findViewById(R.id.tv_BeneficiaryMobile_number);
        _tv_amount                  = (TextView) view.findViewById(R.id.tv_amount);
        _tv_txnDescription          = (TextView) view.findViewById(R.id.tv_txnDescription);
        _tv_ProcessingFees          = (TextView) view.findViewById(R.id.tv_ProcessingFees);
        _tv_AmountPayable           = (TextView) view.findViewById(R.id.tv_AmountPayable);
        _tv_OTP                     = (TextView) view.findViewById(R.id.tv_OTP);
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

        _etBeneficiaryName.setEnabled(true);
        _etAccountNumber.setEnabled(true);
        _etIFSC_Code.setEnabled(true);
        _etBeneficiaryMobile_Number.setEnabled(true);
        _btnSubmit.setVisibility(view.VISIBLE);
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
                getIMPSVerifyPayment();
            }
        });
        _btnPaymentPaySubmit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                _btnPaymentPaySubmit.setEnabled(false);
                showProgress(false);
                _btnNext.setVisibility(View.GONE);

                _tvVerified.setVisibility(View.GONE);
                getIMPSConfirmPayment();
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


                createConsumerRegistration();

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
                _btnSubmit.setVisibility(View.VISIBLE);
                _etConsumerNumber.setVisibility(View.VISIBLE);

                _etAvailableLimit.setVisibility(View.GONE);
                _spOperator.setVisibility(View.GONE);
                _tvVerified.setVisibility(View.GONE);
                _tv_availableLimit.setVisibility(View.GONE);
                _tv_operator.setVisibility(View.GONE);
                _tv_beneficiary_name.setVisibility(View.GONE);
                _tv_account_number.setVisibility(View.GONE);
                _tv_consumerNumber.setVisibility(View.GONE);
                _tv_IFSC_Code.setVisibility(View.GONE);
                _tv_BeneficiaryMobile_number.setVisibility(View.GONE);
                _etBeneficiaryName.setVisibility(View.GONE);
                _etAccountNumber.setVisibility(View.GONE);
                _etIFSC_Code.setVisibility(View.GONE);
                _tvGetIFSC_List.setVisibility(View.GONE);
                _etBeneficiaryMobile_Number.setVisibility(View.GONE);
                _OperatorPayFrom.setVisibility(View.GONE);
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

                         // setupIMPSTransferView();
                if ((isVerified == true)) {
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
                    _OperatorPayFrom.setVisibility(View.GONE);
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

                    Toast.makeText(getActivity().getApplicationContext(), "true", Toast.LENGTH_LONG).show();
                }
                else if(isVerified == false){
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
                    _OperatorPayFrom.setVisibility(View.GONE);
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
                    Toast.makeText(getActivity().getApplicationContext(), "false", Toast.LENGTH_LONG).show();
                }

            }


        });

        _btnPaymentPayCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                _etAmount.setEnabled(true);
                _etConsumerNumber.setVisibility(View.VISIBLE);
                _etAvailableLimit.setVisibility(View.VISIBLE);
                _spOperator.setVisibility(View.VISIBLE);
                _etBeneficiaryName.setVisibility(View.VISIBLE);
                _etAccountNumber.setVisibility(View.VISIBLE);
                _etIFSC_Code.setVisibility(View.VISIBLE);
                _etTxnDescription.setVisibility(View.VISIBLE);
                _etBeneficiaryMobile_Number.setVisibility(View.VISIBLE);
                _OperatorPayFrom.setVisibility(View.GONE);
                _etAmount.setVisibility(View.VISIBLE);
                _rbPay.setVisibility(View.VISIBLE);
                _tvGetIFSC_List.setVisibility(View.VISIBLE);
                _btnNext.setVisibility(View.VISIBLE);
                _btnCancel.setVisibility(View.VISIBLE);
                _tvVerified.setVisibility(View.VISIBLE);
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
                _tv_amount.setVisibility(View.GONE);
                _tv_txnDescription.setVisibility(View.GONE);
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

            }

        });
                return view;
    }


    public void setIMPSVerifyView() {

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
        _OperatorPayFrom.setVisibility(View.GONE);
        _etAmount.setVisibility(View.GONE);
        _etProcessingFees.setVisibility(View.GONE);
        _etTxnDescription.setVisibility(View.GONE);
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

        alert.setTitle("IFSC Code");
        alert.setView(layout);

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                showProgress(false);
            }
        });

        alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                _etIFSC_Code.setText(sBranchIfscCode);
                showProgress(false);


            }
        });

        alert.show();
    }


    public void addListenerOnSpinnerItemSelection() {
        _spOperator
                .setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }

    private void consumerRegistration() {
    //    showMessage(null);

        String sConsumerNumber = _etConsumerNumber.getText().toString().trim();

        TransactionRequest<ImpsCustomerRegistrationResult> request = new TransactionRequest<ImpsCustomerRegistrationResult>(

                getActivity().getString(TransactionType.IMPS_CUSTOMER_REGISTRATION.transactionTypeStringId),
                sConsumerNumber
        );


        getDataEx(this).registerIMPSCustomer(request);




    }

    private void getBeneficiaryList() {


        String sConsumerNumber = _etConsumerNumber.getText().toString().trim();

        IDataEx dataEx = getDataEx(this);


        TransactionRequest<List<BeneficiaryResult>> request = new TransactionRequest<List<BeneficiaryResult>>(
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

    private void getBankName() {


        IDataEx dataEx = getDataEx(this);


        TransactionRequest<List<BankNameResult>> request = new TransactionRequest<List<BankNameResult>>(
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


        TransactionRequest<List<StateNameResult>> request = new TransactionRequest<List<StateNameResult>>(
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


        TransactionRequest<List<CityNameResult>> request = new TransactionRequest<List<CityNameResult>>(
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


        TransactionRequest<List<BranchNameResult>> request = new TransactionRequest<List<BranchNameResult>>(
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
        TransactionRequest<ImpsCreateCustomerResult> request = new TransactionRequest<ImpsCreateCustomerResult>(

                getActivity().getString(TransactionType.IMPS_CREATE_CUSTOMER_REGISTRATION.transactionTypeStringId),
                sConsumerNumber, sConsumerName, sConsumerDOB, sConsumerEmailAddress
        );

        getDataEx(this).impsCustomerRegistration(request);

    }

    private void checkKYC() {
        showMessage(null);
        String sConsumerNumber = _etConsumerNumber.getText().toString().trim();
        TransactionRequest<ImpsCheckKYCResult> request = new TransactionRequest<ImpsCheckKYCResult>(

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



        TransactionRequest<ImpsAddBeneficiaryResult> request = new TransactionRequest<ImpsAddBeneficiaryResult>(
                getActivity().getString(TransactionType.IMPS_ADD_BENEFICIARY.transactionTypeStringId),
                sBeneficiaryName, sAccountNumber, sIfscCode, sBeneficiaryMobNo, null
        );

        getDataEx(this).impsAddBeneficiary(request);

    }


    private void getIMPSBeneficiaryDetails(String sBeneficiaryName) {

        TransactionRequest<ImpsBeneficiaryDetailsResult> request = new TransactionRequest<ImpsBeneficiaryDetailsResult>(
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
                String sTxnNarration  = _etTxnDescription.getText().toString();
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
        _etAmount.setText("");
        return;
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

            createNewBeneficiary();
        } else if((_spOperator.getSelectedItemPosition()!= 0)&& (_rbPay.isChecked()== true)) {
           // todo enable
            _btnNext.setEnabled(false);
            getIMPSPaymentProcess();

            _tvVerified.setVisibility(View.GONE);

        }
        else if((_spOperator.getSelectedItemPosition()!= 0)&& (_rbVerify.isChecked()== true)) {
            _btnNext.setEnabled(false);
            getIMPSVerifyProcess();
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
        } else {
            hideMessage();
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
        _OperatorPayFrom.setVisibility(View.VISIBLE);
        _etAmount.setVisibility(View.GONE);
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
    public void hideCustomRegistrationFields() {
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
        _OperatorPayFrom.setVisibility(View.GONE);
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

        _etBeneficiaryName.setVisibility(View.VISIBLE);
        _etAccountNumber.setVisibility(View.VISIBLE);
        _etIFSC_Code.setVisibility(View.VISIBLE);
        _tvGetIFSC_List.setVisibility(View.VISIBLE);
        _etBeneficiaryMobile_Number.setVisibility(View.VISIBLE);
        _OperatorPayFrom.setVisibility(View.GONE);
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

      _etBeneficiaryName.setVisibility(View.VISIBLE);
      _etAccountNumber.setVisibility(View.VISIBLE);
      _etIFSC_Code.setVisibility(View.VISIBLE);
      _tvGetIFSC_List.setVisibility(View.VISIBLE);
      _etBeneficiaryMobile_Number.setVisibility(View.VISIBLE);
      _OperatorPayFrom.setVisibility(View.GONE);
      _etTxnDescription.setVisibility(View.VISIBLE);
      _etAmount.setVisibility(View.VISIBLE);
      _rbPay.setVisibility(View.VISIBLE);
      _rbVerify.setVisibility(View.VISIBLE);
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
      _tv_amount.setVisibility(View.GONE);
      _tv_txnDescription.setVisibility(View.GONE);
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
        _OperatorPayFrom.setVisibility(View.GONE);
        _etTxnDescription.setVisibility(View.GONE);
        _etAmount.setVisibility(View.GONE);
        _rbPay.setVisibility(View.VISIBLE);
        _rbVerify.setVisibility(View.VISIBLE);
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
        _OperatorPayFrom.setVisibility(View.GONE);
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
        _btnNext.setVisibility(View.GONE);
        _btnCancel.setVisibility(View.GONE);
        _tv_txnDescription.setVisibility(View.GONE);
        _tv_consumerNumber.setVisibility(View.GONE);
        _tv_availableLimit.setVisibility(View.GONE);
        _tv_operator.setVisibility(View.GONE);

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
        _etConsumerNumber.setVisibility(View.GONE);
        _etAvailableLimit.setVisibility(View.GONE);
        _spOperator.setVisibility(View.GONE);
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
        _tv_availableLimit.setVisibility(View.VISIBLE);
        _tv_operator.setVisibility(View.VISIBLE);
        _tv_beneficiary_name.setVisibility(View.VISIBLE);
        _tv_account_number.setVisibility(View.VISIBLE);
        _tv_IFSC_Code.setVisibility(View.VISIBLE);
        _tv_BeneficiaryMobile_number.setVisibility(View.VISIBLE);
        _tv_amount.setVisibility(View.VISIBLE);
        _tv_txnDescription.setVisibility(View.GONE);
        _tv_ProcessingFees.setVisibility(View.VISIBLE);
        _tv_AmountPayable.setVisibility(View.VISIBLE);
        _tv_OTP.setVisibility(View.VISIBLE);
        _tv_consumerNumber.setVisibility(View.GONE);
        _tv_availableLimit.setVisibility(View.GONE);
        _tv_operator.setVisibility(View.GONE);

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

        _etBeneficiaryName.setVisibility(View.VISIBLE);
        _etAccountNumber.setVisibility(View.VISIBLE);
        _etIFSC_Code.setVisibility(View.VISIBLE);
        _tvGetIFSC_List.setVisibility(View.VISIBLE);
        _etBeneficiaryMobile_Number.setVisibility(View.VISIBLE);
        _OperatorPayFrom.setVisibility(View.GONE);
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
        _etBeneficiaryName.setVisibility(View.GONE);
        _etAccountNumber.setVisibility(View.GONE);
        _etIFSC_Code.setVisibility(View.GONE);
        _etTxnDescription.setVisibility(View.GONE);

        _etBeneficiaryMobile_Number.setVisibility(View.GONE);
        _OperatorPayFrom.setVisibility(View.GONE);
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

        _tv_availableLimit.setVisibility(View.GONE);
        _tv_operator.setVisibility(View.GONE);
        _tv_beneficiary_name.setVisibility(View.GONE);
        _tv_account_number.setVisibility(View.GONE);
        _tv_IFSC_Code.setVisibility(View.GONE);
        _tv_BeneficiaryMobile_number.setVisibility(View.GONE);
        _tv_amount.setVisibility(View.GONE);
        _tv_txnDescription.setVisibility(View.GONE);
        _tv_ProcessingFees.setVisibility(View.GONE);
        _tv_AmountPayable.setVisibility(View.GONE);
        _tv_OTP.setVisibility(View.GONE);


    }


    @Override
    public void onTaskSuccess(TransactionRequest result, DataExImpl.Methods callback) {
        Log.d(_LOG, "In onTaskSuccess");

        switch (callback) {
            case IMPS_CUSTOMER_REGISTRATION:

                showProgress(false);
                Log.i(_LOG + "IMPS_CUSTOMER_REGISTRATION", result.getRemoteResponse() + result.getResponseCode());

                if (!(result.getCustom() instanceof ImpsCustomerRegistrationResult)) {
                    Log.d(_LOG, "response is in incorrect format!");
                    showMessage(getActivity().getString(R.string.error_imps_customer_registration));
                    return;
                }

                ImpsCustomerRegistrationResult registrationResult = (ImpsCustomerRegistrationResult) result.getCustom();


                showProgress(false);
                if (registrationResult.isRegistered && registrationResult.isIMPSServiceAllowed) {
                    Log.d(_LOG, "Both true");
                    checkKYC();
                    getBeneficiaryList();
                    setupIMPSTransferView();

                } else if (!registrationResult.isRegistered && !registrationResult.isIMPSServiceAllowed) {
                    Log.d(_LOG, "Both false");
                    showCustomRegistrationFields();
                } else if (!registrationResult.isRegistered && registrationResult.isIMPSServiceAllowed) {
                    Log.d(_LOG, "not registered but service allowed");
                    showCustomRegistrationFields();
                } else if (registrationResult.isRegistered && !registrationResult.isIMPSServiceAllowed) {
                    Log.d(_LOG, "customer already registered but imps not allowed");
                    showMessage("You are not allowed to perform IMPS");
                }

                break;

            case IMPS_BENEFICIARY_LIST:
                Log.d(_LOG, "onTaskSuccess done_Beneficiary");

                if(result.getCustom() == null){
                     Log.d(_LOG, "response is in incorrect format!");
                    showMessage("Couldn't fetch BeneficiaryList.");
                    showProgress(false);
                    return;
                }


                showProgress(false);
                List<BeneficiaryResult> beneficiaryResultsList = null;
                beneficiaryResultsList = (List<BeneficiaryResult>) result.getCustom();



                beneficiaryResultsList.add(0, new BeneficiaryResult("", getActivity().getString(R.string.prompt_spinner_select_NewBeneficiary)));
                ArrayAdapter<BeneficiaryResult> dataAdapter = new ArrayAdapter<BeneficiaryResult>(
                        getActivity(), android.R.layout.simple_spinner_item,
                        beneficiaryResultsList
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

                        } else   if (_spOperator.getSelectedItemPosition() != 0)   {
                            _rbPay.setVisibility(View.VISIBLE);
                            _rbVerify.setVisibility(View.VISIBLE);
                            _rbPay.setChecked(true);


                            BeneficiaryResult beneficiaryResult = (BeneficiaryResult) _spOperator.getSelectedItem();

                            String sBeneficiaryId = beneficiaryResult.id;
                            String sBeneficiaryName = beneficiaryResult.name;

                            EphemeralStorage.getInstance(getActivity()).storeString(
                                    AppConstants.PARAM_PBX_IMPS_BENEFICIARY_ACCOUNT_NAME,
                                    beneficiaryResult.name);
                            EphemeralStorage.getInstance(getActivity()).storeString(
                                    AppConstants.PARAM_PBX_IMPS_BENEFICIARY_ID,
                                    beneficiaryResult.id);
                            //  showMessage(sBeneficiaryId);
                            _etAmount.setText(null);
                            _etTxnDescription.setText(null);
                            _etAmount.setVisibility(View.VISIBLE);
                            _etTxnDescription.setVisibility(View.VISIBLE);

                            _rbVerify.setChecked(false);
                            getIMPSBeneficiaryDetails(sBeneficiaryName);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });



                break;

            case IMPS_CREATE_CUSTOMER_REGISTRATION:
                Log.d(_LOG, "onTaskSuccess done_CreateCustomer");


                if(result.getCustom() == null){
                    Log.d(_LOG, "response is in incorrect format!");
                    showMessage("Couldn't Create Customer.");
                    showProgress(false);
                    return;
                }


                ImpsCreateCustomerResult impsCreateCustomerResult = (ImpsCreateCustomerResult) result.getCustom();

                showMessage(impsCreateCustomerResult.ErrorMessage);
                break;

            case IMPS_CHECK_KYC:
                Log.d(_LOG, "onTaskSuccess done_CreateCustomer");

                if(result.getCustom() == null){
                    Log.d(_LOG, "response is in incorrect format!");
                    showMessage(getResources().getString(R.string.error_imps_kyc));
                    showProgress(false);
                    return;
                }

                ImpsCheckKYCResult impsCheckKYCResult = (ImpsCheckKYCResult) result.getCustom();


                _etAvailableLimit.setText(String.valueOf(impsCheckKYCResult.AvailableLimit));
                break;

            case IMPS_BENEFICIARY_DETAILS:
                Log.d(_LOG, "onTaskSuccess done_BeneficiaryDetails");

                showProgress(false);
                ImpsBeneficiaryDetailsResult impsBeneficiaryDetailsResult = (ImpsBeneficiaryDetailsResult) result.getCustom();
                if(impsBeneficiaryDetailsResult.IsBeneficiaryVerified)
                {
                    _rbPay.setChecked(true);
                    _tvVerified.setVisibility(View.VISIBLE);
                    _rbVerify.setVisibility(View.GONE);
                }
                else if(!impsBeneficiaryDetailsResult.IsBeneficiaryVerified){
                    _tvVerified.setVisibility(View.GONE);
                    _rbVerify.setVisibility(View.VISIBLE);
                }

                _etBeneficiaryName.setText(impsBeneficiaryDetailsResult.BeneficiaryName);
                _etBeneficiaryName.setEnabled(false);
                _etAccountNumber.setText(impsBeneficiaryDetailsResult.AccountNumber);
                _etAccountNumber.setEnabled(false);
                _etIFSC_Code.setText(impsBeneficiaryDetailsResult.IFSCCode);
                _etIFSC_Code.setEnabled(false);
                _etBeneficiaryMobile_Number.setText(impsBeneficiaryDetailsResult.BeneficiaryMobileNumber);
                _etBeneficiaryMobile_Number.setEnabled(false);
                break;

            case IMPS_ADD_BENEFICIARY:

                Log.d(_LOG, "onTaskSuccess done_AddBeneficiary");


                ImpsAddBeneficiaryResult impsAddBeneficiaryResult = (ImpsAddBeneficiaryResult) result.getCustom();
                if (impsAddBeneficiaryResult.RegistrationStatus) {
                    getBeneficiaryList();
                }
                showMessage(impsAddBeneficiaryResult.ErrorMessage);

                Toast.makeText(getActivity().getApplicationContext(), impsAddBeneficiaryResult.ErrorMessage, Toast.LENGTH_LONG).show();

                break;


            case IMPS_BANK_NAME_LIST:
                Log.d(_LOG, "onTaskSuccess done_BankNameList");

                List<BankNameResult> bankNameResultList = null;
                bankNameResultList = (List<BankNameResult>) result.getCustom();


                ArrayAdapter<BankNameResult> dataAdapterBankList = new ArrayAdapter<BankNameResult>(
                        getActivity(), android.R.layout.simple_spinner_item,
                        bankNameResultList
                );

                dataAdapterBankList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                _spOperator1.setAdapter(dataAdapterBankList);
                _spOperator1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        getStateName(_spOperator1.getSelectedItem().toString());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });


                break;

            case IMPS_STATE_NAME:
                Log.d(_LOG, "onTaskSuccess done_StateNameList");


                List<StateNameResult> stateNameResultList = null;
                stateNameResultList = (List<StateNameResult>) result.getCustom();


                ArrayAdapter<StateNameResult> dataAdapterStateList = new ArrayAdapter<StateNameResult>(
                        getActivity(), android.R.layout.simple_spinner_item,
                        stateNameResultList
                );

                dataAdapterStateList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                _spOperator2.setAdapter(dataAdapterStateList);
                _spOperator2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        getCityName(_spOperator1.getSelectedItem().toString(), _spOperator2.getSelectedItem().toString());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                break;

            case IMPS_CITY_NAME:
                Log.d(_LOG, "onTaskSuccess done_CityNameList");


                List<CityNameResult> cityNameResultList = null;
                cityNameResultList = (List<CityNameResult>) result.getCustom();


                ArrayAdapter<CityNameResult> dataAdapterCityList = new ArrayAdapter<CityNameResult>(
                        getActivity(), android.R.layout.simple_spinner_item,
                        cityNameResultList
                );

                dataAdapterCityList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                _spOperator3.setAdapter(dataAdapterCityList);
                _spOperator3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
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
                List<BranchNameResult> branchNameResultList = null;
                branchNameResultList = (List<BranchNameResult>) result.getCustom();



                ArrayAdapter<BranchNameResult> dataAdapterBranchList = new ArrayAdapter<BranchNameResult>(
                        getActivity(), android.R.layout.simple_spinner_item,
                        branchNameResultList
                );

                dataAdapterBranchList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                _spOperator4.setAdapter(dataAdapterBranchList);
                _spOperator4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        BranchNameResult branchNameResult = (BranchNameResult) _spOperator4.getSelectedItem();
                        sBranchIfscCode = branchNameResult.IFSCCode;
               //         Toast.makeText(getActivity().getApplicationContext(), sBranchIfscCode, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });


                break;

            case IMPS_VERIFY_PROCESS:


                Log.i(_LOG + "IMPS_VERIFY_PROCESS", result.getRemoteResponse() + result.getResponseCode());

                if(result.getCustom()== null){
                    showMessage("Couldn't process");
                }

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
                }
                else{
                    showMessage("Couldn't process");
                }

                showProgress(false);
                break;

            case IMPS_VERIFY_PAYMENT:
                Log.i(_LOG + "IMPS_VERIFY_PAYMENT", result.getRemoteResponse() + result.getResponseCode());


                ImpsVerifyPaymentResult impsVerifyPaymentResult = (ImpsVerifyPaymentResult) result.getCustom();

               showMessage("Message: "+" " +impsVerifyPaymentResult.errorMessage +"\n"
                       + "Receipt ID: " + impsVerifyPaymentResult.recieptId +"\n"
                       + "Transaction ID:" + impsVerifyPaymentResult.transactionId +"\n"
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
                    int processFees = 10;
                    int amountPayable = amount+processFees;
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
                Log.i(_LOG + "IMPS_VERIFY_PAYMENT", result.getRemoteResponse() + result.getResponseCode());

                List<ImpsConfirmPaymentResult> impsConfirmPaymentResultList = null;
                impsConfirmPaymentResultList = (List<ImpsConfirmPaymentResult>) result.getCustom();


                showMessage(null);


                Log.d("HISTORY", "Obtained result: " + impsConfirmPaymentResultList);

                if(impsConfirmPaymentResultList == null || impsConfirmPaymentResultList.size() < 1) {
                    setNoPaymentHistoryMessage();
                    return;
                }

                Log.d("HISTORY", "Creating list with " + impsConfirmPaymentResultList.size() + " transactions");

                ConfirmPaymentTextListViewAdapter adapter = new ConfirmPaymentTextListViewAdapter(
                        getActivity(),
                        R.layout.listview_impsconfirm_payment,
                        impsConfirmPaymentResultList
                );

                _titleView.setText(String.format(getResources().getString(R.string.title_activity_transaction_history_count), impsConfirmPaymentResultList.size()));

                _listView.setAdapter(adapter);
                _listView.setVisibility(View.VISIBLE);

                Log.d("HISTORY", "ListView created");

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
                    int processFees = 10;
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
                    int processFees = 10;
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
                showMessage(getActivity().getString(R.string.error_imps_customer_creation));
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


