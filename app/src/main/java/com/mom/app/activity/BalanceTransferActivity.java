package com.mom.app.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mom.app.R;
import com.mom.app.identifier.ActivityIdentifier;
import com.mom.app.identifier.PlatformIdentifier;
import com.mom.app.model.AsyncListener;
import com.mom.app.model.AsyncResult;
import com.mom.app.model.DataExImpl;
import com.mom.app.utils.AppConstants;

public class BalanceTransferActivity extends MOMActivityBase implements AsyncListener<String>{
    String _LOG         = AppConstants.LOG_PREFIX + "BALANCE_TRANSFER";

    private EditText _etPayTo;
    private EditText _etAmount;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_transfer);

        _etPayTo            = (EditText) findViewById(R.id.payTo);
        _etAmount           = (EditText) findViewById(R.id.amount);
        getProgressBar().setVisibility(View.GONE);
    }

    @Override
    public void onTaskSuccess(String result, DataExImpl.Methods callback) {
        Log.d(_LOG, "Called back");
        switch(callback){
            case BALANCE_TRANSFER:
                if(result == null){
                    Log.d(_LOG, "Obtained NULL  response");
                    showMessage(getResources().getString(R.string.error_transfer_failed));
                    return;
                }
                Log.d(_LOG, "Going to get new balance");
                getBalance();
                Log.d(_LOG, "Starting navigation to TxnMsg Activity");
                navigateToTransactionMessageActivity(ActivityIdentifier.BALANCE_TRANSFER, result);
                break;
        }

        getProgressBar().setVisibility(View.GONE);
    }

    @Override
    public void onTaskError(AsyncResult pResult, DataExImpl.Methods callback) {

    }

    @Override
    protected void showBalance(float pfBalance) {
    }

    public void validateAndTransfer(View view) {
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

        String sPayTo           = _etPayTo.getText().toString();
        String sAmount          = _etAmount.getText().toString();


        if (_currentPlatform == PlatformIdentifier.NEW){
            getDataEx(this).retailerpayment(sPayTo,Double.parseDouble(sAmount));
        }else {
            getDataEx(this).retailerpayment(sPayTo,Double.parseDouble(sAmount));
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG)
                    .show();
        }

    }



    public void goBack(View view) {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.finish();
        return;
    }


    public void confirmTransfer() {
        getProgressBar().setVisibility(View.VISIBLE);


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                BalanceTransferActivity.this);

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