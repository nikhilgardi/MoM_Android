package com.mom.app.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.util.Log;

import com.mom.app.model.AsyncDataEx;


public class MiscUtils {
    static String _LOG  = AppConstants.LOG_PREFIX + "UTILS";

    public static String callHttpMethod(AsyncDataEx.HttpMethod method, String url, NameValuePair...pParams) {
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();

        try {
            HttpRequestBase httpReq         = null;
            ArrayList<NameValuePair> list   = new ArrayList<NameValuePair>(Arrays.asList(pParams));

            if(method == AsyncDataEx.HttpMethod.GET){
                Log.d(_LOG, "Going to make GET request");
                String params   = URLEncodedUtils.format(list, AppConstants.UTF_8);
                url            = url + "?" + params;
                Log.d(_LOG, "Calling: " + url);
                httpReq         = new HttpGet(url);
            }else{
                Log.d(_LOG, "Going to make POST request");
                httpReq         = new HttpPost(url);
                ((HttpPost)httpReq).setEntity(new UrlEncodedFormEntity(list));
            }

            Log.d(_LOG, "Starting request");

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httpReq);
            HttpEntity entity 				= response.getEntity();
            String sResponse 				= EntityUtils.toString(entity);
            Log.d("AsyncDataEx", "Response: " + sResponse);
            return sResponse;
        }catch (Exception e) {
            e.printStackTrace();
            Log.i("Async", e.getMessage());
        }
        return null;
    }

    public static void copyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];

            for(;;)
            {
                int count       = is.read(bytes, 0, buffer_size);

                if(count == -1) {
                    break;
                }

                os.write(bytes, 0, count);
            }
        }catch(Exception ex){
            Log.e(_LOG, "Error copying stream", ex);
        }
    }
}
