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

/**
 * Created by vaibhavsinha on 10/14/14.
 */
public class LICFragment extends FragmentBase implements AsyncListener<TransactionRequest> {
    String _LOG         = AppConstants.LOG_PREFIX + "LIC";

    private EditText _etLIC;


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

        _etLIC            = (EditText) view.findViewById(R.id.lic);

        Button btnregister       = (Button) view.findViewById(R.id.btnRegister);

        btnregister.setOnClickListener(new View.OnClickListener() {
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
            case LIC:
                if(result == null){
                    Log.d(_LOG, "Obtained NULL  response");
                    showMessage(getResources().getString(R.string.error_transfer_failed));
                    return;
                }
                Log.d(_LOG, "Going to get new balance");
                Log.d(_LOG, "Starting navigation to TxnMsg Activity");
//                navigateToTransactionMessageActivity(ActivityIdentifier.BALANCE_TRANSFER, result);
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

    public void validateAndTransfer() {
//        int nMinAmount          = 10;
//
//        int nMinPhoneLength     = 10;
//        int nMaxPhoneLength     = 10;

        int nPhoneLength        = _etLIC.getText().toString().length();




//        if(nPhoneLength < nMinPhoneLength || nPhoneLength > nMaxPhoneLength){
//            if(nMinPhoneLength == nMaxPhoneLength) {
//                showMessage(String.format(getResources().getString(R.string.error_phone_length), nMinPhoneLength));
//            }else{
//                showMessage(String.format(getResources().getString(R.string.error_phone_length_min_max), nMinPhoneLength, nMaxPhoneLength));
//            }
//            return;
//        }



        confirmTransfer();
    }

    private void startTransfer() {
        showMessage(null);
        String slic           = _etLIC.getText().toString();


        TransactionRequest request  = new TransactionRequest(
                getActivity().getString(TransactionType.LIC.transactionTypeStringId),
                slic );

        getDataEx(this).lic( slic);

        _etLIC.setText(null);

        showProgress(true);
       // updateAsyncQueue(request);
    }


    public void confirmTransfer() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        // Setting Dialog Title
        alertDialog.setTitle(R.string.confirm_balance_transfer);

        // Setting Dialog Message
        alertDialog.setMessage(getResources().getString(R.string.transfer) +
                 " : " +  _etLIC.getText().toString());


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
