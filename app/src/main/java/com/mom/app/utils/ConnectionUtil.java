package com.mom.app.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.mom.app.R;
import com.mom.app.activity.DashboardActivity;
import com.mom.app.activity.LoginActivity;

/**
 * Created by vaibhavsinha on 7/25/14.
 */
public class ConnectionUtil {
    public static boolean checkConnectivity(Context context){
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

    public static void showConnectionWarning(final Context context){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialog.setTitle(context.getResources().getString(R.string.error_no_internet_title));

        // Setting Dialog Message
        alertDialog.setMessage(context.getResources().getString(R.string.error_no_internet));

        alertDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ((AlertDialog) dialog).getButton(
                                AlertDialog.BUTTON_NEUTRAL).setEnabled(false);
                        Intent intent   = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    }
                });


        alertDialog.show();
    }
}
