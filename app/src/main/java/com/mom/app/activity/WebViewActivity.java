package com.mom.app.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mom.app.R;
import com.mom.app.fragment.DashboardFragment;
import com.mom.app.fragment.WalletUpdateFragment;
import com.mom.app.identifier.PlatformIdentifier;
import com.mom.app.model.local.EphemeralStorage;
import com.mom.app.model.pbxpl.ResponseBase;
import com.mom.app.utils.AppConstants;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.lang.reflect.Type;

public class WebViewActivity extends ActionBarActivity {
    String _LOG     = AppConstants.LOG_PREFIX + "WebViewActivity";
    private WebView wv1;
    protected PlatformIdentifier _currentPlatform;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_web_view);
        wv1               =(WebView) findViewById(R.id.pageInfo);

        Intent intent = getIntent();
        String sName = intent.getStringExtra(AppConstants.PARAM_NEW_STR_NAME);
        String sEmail = intent.getStringExtra(AppConstants.PARAM_NEW_STR_EMAIL);
        String sAmount = intent.getStringExtra(AppConstants.PARAM_NEW_STR_AMOUNT);
        String sMobNo = intent.getStringExtra(AppConstants.PARAM_NEW_STR_MOBILE_NUMBER_PAY_U);
        try {
            String url = "http://utilities.money-on-mobile.net/payu_web/UpdateWallet.aspx?Name=" + sName + "&Mobile=" + sMobNo + "&Amount=" + sAmount + "&Email=" + sEmail + "";
            Log.i("PayUParams", url);
            wv1.getSettings().setLoadsImagesAutomatically(true);
            wv1.getSettings().setJavaScriptEnabled(true);
            wv1.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            wv1.setWebViewClient(new WebViewSampleClient());
            wv1.loadUrl(url);
        }
        catch(Exception ex){
            Log.e("Error" , ex.toString());
            Toast.makeText(this, R.string.error_transfer_failed_payU, Toast.LENGTH_LONG).show();


        }
    }

    private class WebViewSampleClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

        }
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);


        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            Log.e("ErrorLog"," Error occured while loading the web page at Url");
            Toast.makeText(WebViewActivity.this, "Error occured,please check Network connectivity", Toast.LENGTH_SHORT).show();
        }
    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if(wv1 != null) {
//            wv1.stopLoading();
//            wv1.onPause(); //pauses background threads, stops playing sound
//            wv1.pauseTimers(); //pauses the WebViewCore
//        }
//    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_web_view, menu);





        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
