package com.mom.app.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.mom.app.R;
import com.mom.app.error.MOMException;
import com.mom.app.identifier.PlatformIdentifier;
import com.mom.app.identifier.TransactionType;
import com.mom.app.model.AsyncListener;
import com.mom.app.model.AsyncResult;
import com.mom.app.model.DataExImpl;
import com.mom.app.model.pbxpl.PBXPLDataExImpl;
import com.mom.app.model.pbxpl.lic.LicLifeResponse;
import com.mom.app.ui.TransactionRequest;
import com.mom.app.utils.AppConstants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by vaibhavsinha on 10/14/14.
 */
public class LICFragment extends FragmentBase implements AsyncListener<TransactionRequest<LicLifeResponse>> {
    String _LOG         = AppConstants.LOG_PREFIX + "LIC";
    TableLayout _tableLayout , _tableLayout1;
    Button _btnRegister , _btnPay , _btnCancel , _btnGetAnotherPremium;

    private EditText _etLIC , _etCustMobileNumber;
    private TextView _tvPremiumAmount , _tvPlaceHolder, _tvStatus , _tvReceiptId , _tvFromDate , _tvToDate;
    private TransactionRequest<LicLifeResponse> _lastRequest;

    public static final String PymtTrsfrStatusSuccessCode = "S" ;
    public static final String PymtTrsfrStatusErrorCode3  = "E3" ;
    public static final String PymtTrsfrStatusErrorCode4  = "E4" ;
    public static final String PymtTrsfrStatusErrorCode5  = "E5" ;
    public static final String PymtTrsfrStatusErrorCode6  = "E6" ;
    public static final String PymtTrsfrStatusErrorCode7  = "E7" ;
    public static final String PymtTrsfrStatusErrorCode8  = "E8" ;
    public static final String PymtTrsfrStatusErrorCode9  = "E9" ;
    public static final String PymtTrsfrStatusErrorCode10 = "E10" ;
    public static final String PymtTrsfrStatusErrorCode11 = "E11" ;

    public static LICFragment newInstance(PlatformIdentifier currentPlatform){
        LICFragment fragment        = new LICFragment();
        Bundle bundle                       = new Bundle();
        bundle.putSerializable(AppConstants.ACTIVE_PLATFORM, currentPlatform);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_lic, null, false);

        _etLIC                  = (EditText) view.findViewById(R.id.lic);
        _etCustMobileNumber     = (EditText) view.findViewById(R.id.CustomerNumber);
        _tvPremiumAmount        = (TextView) view.findViewById(R.id.premiumAmount);
        _tvPlaceHolder          = (TextView) view.findViewById(R.id.policyHolder);
        _tvStatus               = (TextView) view.findViewById(R.id.status);
        _tvReceiptId            = (TextView) view.findViewById(R.id.receiptNo);
        _tvFromDate             = (TextView) view.findViewById(R.id.fromDate);
        _tvToDate               = (TextView) view.findViewById(R.id.toDate);
        _tableLayout            = (TableLayout) view.findViewById(R.id.tableLayout);
        _tableLayout1           = (TableLayout) view.findViewById(R.id.tableLayout1);

        _etLIC.setText("806000021");

        _btnRegister            = (Button) view.findViewById(R.id.btnRegister);
        _btnPay                 = (Button) view.findViewById(R.id.btnPay);
        _btnCancel                = (Button) view.findViewById(R.id.btnCancel);
        _btnGetAnotherPremium   = (Button) view.findViewById(R.id.btnGetAnotherPremium);

        _btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateAndGetAmount();

            }
        });

        _btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            _btnPay.setEnabled(false);
              //  confirmRecharge();
            getLicPayment(_lastRequest) ;

            }
        });


        _btnGetAnotherPremium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _tableLayout.setVisibility(View.GONE);
                _tableLayout.findViewById(R.id.tableRowFromDate).setVisibility(View.GONE);
                _tableLayout.findViewById(R.id.tableRowToDate).setVisibility(View.GONE);
                _btnGetAnotherPremium.setVisibility(View.GONE);
                _etLIC.setVisibility(View.VISIBLE);
                _etLIC.setText("806000021");
                _etCustMobileNumber.setVisibility(View.VISIBLE);
                _btnRegister.setVisibility(View.VISIBLE);
                _btnPay.setEnabled(true);

            }
        });

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _currentPlatform        = (PlatformIdentifier) getArguments().getSerializable(AppConstants.ACTIVE_PLATFORM);
    }

    @Override
    public void onTaskSuccess(TransactionRequest<LicLifeResponse> result, DataExImpl.Methods callback) {
        Log.d(_LOG, "Called back");
        showProgress(false);

        switch(callback){
            case LIC:
                if(result == null){
                    Log.d(_LOG, "Obtained NULL  response");
                    showMessage(getResources().getString(R.string.error_invalid_policy_number));
                    return;
                }
                Log.d(_LOG, "Obtained: " + result);
                _etLIC.setVisibility(View.GONE);
                _etCustMobileNumber.setVisibility(View.GONE);
                _btnRegister.setVisibility(View.GONE);
                _btnGetAnotherPremium.setVisibility(View.GONE);
                _tableLayout.setVisibility(View.VISIBLE);
                _tableLayout.findViewById(R.id.tableRowStatus).setVisibility(View.GONE);
                _tableLayout.findViewById(R.id.tableRowReceipt).setVisibility(View.GONE);
                _tableLayout.findViewById(R.id.tableRowPolicyHolder).setVisibility(View.VISIBLE);
                _tableLayout.findViewById(R.id.tableRowPremiumAmount).setVisibility(View.VISIBLE);
                _tableLayout.findViewById(R.id.tableRowFromDate).setVisibility(View.VISIBLE);
                _tableLayout.findViewById(R.id.tableRowToDate).setVisibility(View.VISIBLE);
                _btnPay.setVisibility(View.VISIBLE);
                _btnCancel.setVisibility(View.VISIBLE);
                _tvPremiumAmount.setText(String.valueOf(result.getCustom().getTransInvAmount()));
                _tvPlaceHolder.setText(result.getCustom().getOLife().getParty().getFullName());
                _lastRequest    = result;
                String fromDate = result.getCustom().getOLife().getPolicy().getFrUnpaidPremiumDate();
                String toDate = result.getCustom().getOLife().getPolicy().getToUnpaidPremiumDate();

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                try{
                    Date FromUnpaidDate = dateFormat.parse(fromDate);
                    Date ToUnpaidDate = dateFormat.parse(toDate);
                    dateFormat = new SimpleDateFormat("MM/yyyy");
                    String fromDateLic = dateFormat.format(FromUnpaidDate);
                    String toDateLic   = dateFormat.format(ToUnpaidDate);
                    _tvFromDate.setText(fromDateLic);
                    _tvToDate.setText(toDateLic);
                 }catch (ParseException e) {
                    e.printStackTrace();

        }
                break;

                case PAY_LIC:
                    if(result == null){
                        Log.d(_LOG, "Obtained NULL  response");
                        showMessage(getResources().getString(R.string.error_invalid_policy_number));
                        return;
                    }

                    if(result.getCustom().getPymtTrsfrStatus().equals(PymtTrsfrStatusSuccessCode)) {
                        _btnPay.setVisibility(View.GONE);
                        _btnCancel.setVisibility(View.GONE);
                        _tableLayout.setVisibility(View.VISIBLE);
                        _tableLayout.findViewById(R.id.tableRowFromDate).setVisibility(View.GONE);
                        _tableLayout.findViewById(R.id.tableRowToDate).setVisibility(View.GONE);
                        _tableLayout.findViewById(R.id.tableRowPolicyHolder).setVisibility(View.GONE);
                        _tableLayout.findViewById(R.id.tableRowPremiumAmount).setVisibility(View.GONE);
                        _tableLayout.findViewById(R.id.tableRowStatus).setVisibility(View.VISIBLE);
                        _tableLayout.findViewById(R.id.tableRowReceipt).setVisibility(View.VISIBLE);
                        _btnGetAnotherPremium.setVisibility(View.VISIBLE);
                        _tvStatus.setText(R.string.lic_success_msg_default);
                        _tvStatus.setTextColor(getActivity().getResources().getColor(R.color.green));
                        _tvReceiptId.setText(result.getCustom().getTransReceiptID());
                    }
                       else if(result.getCustom().getPymtTrsfrStatus().equals(PymtTrsfrStatusErrorCode3)
                            || result.getCustom().getPymtTrsfrStatus().equals(PymtTrsfrStatusErrorCode4)
                            || result.getCustom().getPymtTrsfrStatus().equals(PymtTrsfrStatusErrorCode5)
                            || result.getCustom().getPymtTrsfrStatus().equals(PymtTrsfrStatusErrorCode6)
                            || result.getCustom().getPymtTrsfrStatus().equals(PymtTrsfrStatusErrorCode7)
                            || result.getCustom().getPymtTrsfrStatus().equals(PymtTrsfrStatusErrorCode8)
                            || result.getCustom().getPymtTrsfrStatus().equals(PymtTrsfrStatusErrorCode9)
                            || result.getCustom().getPymtTrsfrStatus().equals(PymtTrsfrStatusErrorCode10)
                            || result.getCustom().getPymtTrsfrStatus().equals(PymtTrsfrStatusErrorCode11))
                    {
                        _btnPay.setVisibility(View.GONE);
                        _btnCancel.setVisibility(View.GONE);
                        _btnGetAnotherPremium.setVisibility(View.VISIBLE);
                        _tableLayout.findViewById(R.id.tableRowFromDate).setVisibility(View.GONE);
                        _tableLayout.findViewById(R.id.tableRowToDate).setVisibility(View.GONE);
                        _tableLayout.findViewById(R.id.tableRowPolicyHolder).setVisibility(View.GONE);
                        _tableLayout.findViewById(R.id.tableRowPremiumAmount).setVisibility(View.GONE);
                        _tableLayout.findViewById(R.id.tableRowStatus).setVisibility(View.VISIBLE);
                     //   _tableLayout.findViewById(R.id.tableRowReceipt).setVisibility(View.VISIBLE);
                        _tvStatus.setText(R.string.lic_failed_msg_default);
                        _tvStatus.setTextColor(getActivity().getResources().getColor(R.color.red));
                      //  _tvReceiptId.setText(result.getCustom().getTransReceiptID());
                        }
                        else{

                        }


                break;
        }
    }

    @Override
    public void onTaskError(AsyncResult pResult, DataExImpl.Methods callback) {
        showMessage(getResources().getString(R.string.error_invalid_policy_number));
        _etLIC.setVisibility(View.GONE);
        _etCustMobileNumber.setVisibility(View.GONE);
        _btnRegister.setVisibility(View.GONE);
        showProgress(false);

    }

    @Override
    protected void showBalance(float pfBalance) {
    }

    private void showPolicy(){

    }

    public void confirmRecharge() {
        showDialog(
                "Confirm ",
                "Policy Number:" + " "
                        + _etLIC.getText().toString() + "\n" + "Customer Mob No:"
                        + " " + _etCustMobileNumber.getText() + "\n"
                        + "Amount:" + " " + "Rs." + " "
                        + String.valueOf(_lastRequest.getCustom().getTransInvAmount()) ,
                "YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ((AlertDialog) dialog).getButton(
                                AlertDialog.BUTTON1).setEnabled(false);
                         getLicPayment(_lastRequest) ;


                    }
                },
                "NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which1) {
                        dialog.cancel();
                    }
                }
        );
    }
    public void validateAndGetAmount() {
        String policyNumber           = _etLIC.getText().toString();
        if(TextUtils.isEmpty(policyNumber)){
            showMessage(getActivity().getResources().getString(R.string.error_invalid_policy_number));
            return;
        }

        getPremiumAmount(policyNumber);
    }

    private void getPremiumAmount(String policyNumber) {
        hideMessage();
       // _etLIC.setText(null);
       // _etCustMobileNumber.setText(null);
        _lastRequest    = null;
        getDataEx(this).lic(policyNumber);
        showProgress(true);
    }
    private void getLicPayment(TransactionRequest<LicLifeResponse>  _lastRequest)
   {
       String policyNumber           = _etLIC.getText().toString();
       String CustomerMobNo          = _etCustMobileNumber.getText().toString();
        hideMessage();
        _etLIC.setText(null);

        getDataEx(this).licPayment(_lastRequest , CustomerMobNo ,  policyNumber);
        showProgress(true);
    }

}
