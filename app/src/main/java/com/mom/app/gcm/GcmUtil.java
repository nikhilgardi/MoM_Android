package com.mom.app.gcm;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.mom.app.model.AsyncDataEx;
import com.mom.app.model.local.PersistentStorage;
import com.mom.app.utils.AppConstants;
import com.mom.app.utils.MiscUtils;

import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by vaibhavsinha on 9/25/14.
 */
public class GcmUtil {
    static String _LOG = AppConstants.LOG_PREFIX + "GCM";

    static GcmUtil _instance;

    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";

//    Context context;
    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    String regid;
    String SENDER_ID = "706186800699";
    Context _context;

    private GcmUtil(Context context){
        _context = context;
        gcm = GoogleCloudMessaging.getInstance(_context);
    }

    public static GcmUtil getInstance(Context context){
        if(_instance == null){
            _instance   = new GcmUtil(context);
        }

        return _instance;
    }

    public void registerDevice(){
        regid = getRegistrationId();

        if (regid.isEmpty()) {
            Log.d(_LOG, "Device not registered. Registering...");
            registerInBackground();
            Log.d(_LOG, "Registration sent");
        }else{
            Log.d(_LOG, "Device already registered. regid: " + regid);
        }
    }

    /**
     * Stores the registration ID and the app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param regId registration ID
     */
    private void storeRegistrationId(String regId) {
        int appVersion = getAppVersion(_context);
        Log.i(_LOG, "Saving regId on app version " + appVersion);
        PersistentStorage.getInstance(_context).storeString(PROPERTY_REG_ID, regId);
        PersistentStorage.getInstance(_context).storeInt(PROPERTY_APP_VERSION, appVersion);
    }

    /**
     * Gets the current registration ID for application on GCM service, if there is one.
     * <p>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     *         registration ID.
     */
    public String getRegistrationId() {
        String registrationId = PersistentStorage.getInstance(_context).getString(
                PROPERTY_REG_ID, ""
        );

        if (TextUtils.isEmpty(registrationId)) {
            Log.i(_LOG, "Registration not found.");
            return "";
        }

        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = PersistentStorage.getInstance(_context).getInt(
                PROPERTY_APP_VERSION, Integer.MIN_VALUE
        );

        int currentVersion = getAppVersion(_context);
        if (registeredVersion != currentVersion) {
            Log.i(_LOG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    /**
     * Registers the application with GCM servers asynchronously.
     * <p>
     * Stores the registration ID and the app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(_context);
                    }

                    Log.d(_LOG, "Registering on server " + SENDER_ID);

                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;

                    sendRegistrationIdToBackend(regid);

                    // Persist the regID - no need to register again.
                    storeRegistrationId(regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                Log.d(_LOG, msg);
            }
        }.execute(null, null, null);
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }


    /**
     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP or CCS to send
     * messages to your app. Not needed for this demo since the device sends upstream messages
     * to a server that echoes back the message using the 'from' address in the message.
     */
    private void sendRegistrationIdToBackend(String id){

        Log.d(_LOG, "Sending reg id to server");
        String response = MiscUtils.callHttpMethod(
                AsyncDataEx.HttpMethod.GET,
                "http://v7-dot-momone-gateway.appspot.com/register",
                new BasicNameValuePair("regId", id));

        Log.d(_LOG, "Saved regid, response: " + response);
    }
}
