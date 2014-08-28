package com.mom.app.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.util.Log;


public class MiscUtils {
    static String _LOG  = AppConstants.LOG_PREFIX + "UTILS";

    public static String getHttpGetResponse(String psUrl){
        try {
            Log.i("MISC-Get", "Getting: " + psUrl);

            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(psUrl);
            HttpResponse response = client.execute(request);

            BufferedReader rd = new BufferedReader
                    (new InputStreamReader(response.getEntity().getContent()));

            StringBuffer bf = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                bf.append(line);
            }

            return bf.toString();
        }catch(IOException ioe){
            ioe.printStackTrace();
            Log.i("MISC", ioe.getMessage());
        }
        return null;
    }

	public static String getHttpPostResponse(String psUrl, List<NameValuePair> pParams){
		HttpClient httpclient 				= new DefaultHttpClient();

        Log.i("MISC", "Getting: " + psUrl);

		try {

            HttpPost httppost 				= new HttpPost(psUrl);
			final HttpParams httpParams 	= httppost.getParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, AppConstants.TIMEOUT_CONNECTION);
			HttpConnectionParams.setSoTimeout(httpParams, AppConstants.TIMEOUT_SOCKET);

			httppost.setEntity(new UrlEncodedFormEntity(pParams));

            Log.i("Misc", httppost.toString());
            Log.i("Misc", httppost.getURI().toString());

			// Execute HTTP Post Request

			HttpResponse response 			= httpclient.execute(httppost);
			HttpEntity entity 				= response.getEntity();
			String sResponse 				= EntityUtils.toString(entity);
			
			Log.i("postData", response.getStatusLine().toString());
			Log.i("postData", sResponse);
			
			return sResponse;
			
		} catch (Exception ex) {
			ex.printStackTrace();
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
