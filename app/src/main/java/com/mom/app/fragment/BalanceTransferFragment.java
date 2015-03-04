package com.mom.app.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.mom.app.R;

import com.mom.app.identifier.PlatformIdentifier;
import com.mom.app.identifier.TransactionType;
import com.mom.app.model.AsyncListener;
import com.mom.app.model.AsyncResult;
import com.mom.app.model.DataExImpl;
import com.mom.app.ui.TransactionRequest;
import com.mom.app.utils.AppConstants;


public class BalanceTransferFragment extends FragmentBase implements AsyncListener<TransactionRequest> {
    String _LOG         = AppConstants.LOG_PREFIX + "BALANCE_TRANSFER";

    private EditText _etPayTo;
    private EditText _etAmount;

    public static BalanceTransferFragment newInstance(PlatformIdentifier currentPlatform){
        BalanceTransferFragment fragment        = new BalanceTransferFragment();
        Bundle bundle                       = new Bundle();
        bundle.putSerializable(AppConstants.ACTIVE_PLATFORM, currentPlatform);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_balance_transfer, null, false);

        _etPayTo            = (EditText) view.findViewById(R.id.payTo);
        _etAmount           = (EditText) view.findViewById(R.id.amount);
        Button btnPay       = (Button) view.findViewById(R.id.btnPay);

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateAndTransfer();
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
    public void onTaskSuccess(TransactionRequest result, DataExImpl.Methods callback) {
        Log.d(_LOG, "Called back");
        switch(callback){
            case BALANCE_TRANSFER:
                if(result == null){
                    Log.d(_LOG, "Obtained NULL  response");
                    showMessage(getResources().getString(R.string.error_transfer_failed));
                    return;
                }
                Log.d(_LOG, "Going to get new balance");
                Log.d(_LOG, "Starting navigation to TxnMsg Activity");

//                navigateToTransactionMessageActivity(ActivityIdentifier.BALANCE_TRANSFER, result);
                Log.i("ResultBalanceTransfer" , result.getRemoteResponse()+ result.getResponseCode());
                Log.i("ResultBalanceTransferData" , result.getRemoteResponse());
                showProgress(false);
                showMessage(result.getRemoteResponse());

                showBalance();

                break;
        }

       // taskCompleted(result);
    }

    @Override
    public void onTaskError(AsyncResult pResult, DataExImpl.Methods callback) {

    }

    public void validateAndTransfer() {
        int nMinAmount          = 10;

        int nMinPhoneLength     = 10;
        int nMaxPhoneLength     = 10;

        int nPhoneLength        = _etPayTo.getText().toString().length();
        int nAmount             = 0;

        try {
            nAmount             = Integer.parseInt(_etAmount.getText().toString());
        }catch (NumberFormatException nfe){
            nfe.printStackTrace();
            showMessage(getResources().getString(R.string.prompt_numbers_only_amount));
            return;
        }



        if(nPhoneLength < nMinPhoneLength || nPhoneLength > nMaxPhoneLength){
            if(nMinPhoneLength == nMaxPhoneLength) {
                showMessage(String.format(getResources().getString(R.string.error_phone_length), nMinPhoneLength));
            }else{
                showMessage(String.format(getResources().getString(R.string.error_phone_length_min_max), nMinPhoneLength, nMaxPhoneLength));
            }
            return;
        }

        if(nAmount < nMinAmount ){
            showMessage(String.format(getResources().getString(R.string.error_amount_min), nMinAmount));
            return;
        }

        confirmTransfer();
    }

    private void startTransfer() {
        showMessage(null);
        String sPayTo           = _etPayTo.getText().toString();
        String sAmount          = _etAmount.getText().toString();

        TransactionRequest request  = new TransactionRequest(
                getActivity().getString(TransactionType.BALANCE_TRANSFER.transactionTypeStringId),
                sPayTo,
                Float.parseFloat(sAmount)
        );

        getDataEx(this).balanceTransfer(request, sPayTo);
        _etAmount.setText(null);
        _etPayTo.setText(null);
        showProgress(true);
      //  updateAsyncQueue(request);
    }


    public void confirmTransfer() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        // Setting Dialog Title
        alertDialog.setTitle(R.string.confirm_balance_transfer);

        // Setting Dialog Message
        alertDialog.setMessage(getResources().getString(R.string.transfer) +
                " " + "Rs. "
                + _etAmount.getText().toString() + " : " +  _etPayTo.getText().toString());


        alertDialog.setPositiveButton(R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ((AlertDialog) dialog).getButton(
                                AlertDialog.BUTTON1).setEnabled(false);
                        startTransfer();
//							new GetLoginTask().onPostExecute("test");

                    }
                });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton(R.string.no,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which1) {

                        dialog.cancel();

                    }
                });

        alertDialog.show();
    }
}
