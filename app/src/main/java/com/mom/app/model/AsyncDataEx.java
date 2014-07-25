package com.mom.app.model;

import java.net.URI;
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
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.util.Log;

import com.mom.app.utils.MOMConstants;


public class AsyncDataEx extends AsyncTask<NameValuePair, Integer, String>{
    String _LOG             = "ASYNC_DATAEX";
    public static enum HttpMethod{GET, POST};
    HttpMethod _httpMethod;
	AsyncListener _callback;
	String _url;
	DataExImpl.Methods _callbackData;

    public AsyncDataEx(AsyncListener pListener, String psUrl, DataExImpl.Methods pCallbackData, HttpMethod method){
        this._url			= psUrl;
        this._callback		= pListener;
        this._callbackData  = pCallbackData;
        this._httpMethod    = method;
    }

	public AsyncDataEx(AsyncListener pListener, String psUrl, DataExImpl.Methods pCallbackData){
		this._url			= psUrl;
		this._callback		= pListener;
        this._callbackData  = pCallbackData;
	}
	
	@Override
	protected String doInBackground(NameValuePair...pList) {
		return postData(Arrays.asList(pList));
	}

	protected void onPostExecute(String result){
        Log.d(_LOG, "Called onPostExecute of listener, calling listener");
        _callback.onTaskSuccess(result, _callbackData);
        Log.d(_LOG, "Called onTaskSuccess of listener");
	}
	
	protected void onProgressUpdate(Integer... progress){

	}

	public String postData(List<NameValuePair> pParams) {
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();

		try {
            HttpRequestBase httpReq         = null;

            if(_httpMethod == HttpMethod.GET){
                Log.d(_LOG, "Going to make GET request");
                String params   = URLEncodedUtils.format(pParams, MOMConstants.UTF_8);
                _url            = _url + "?" + params;
                Log.d(_LOG, "Calling: " + _url);
                httpReq         = new HttpGet(_url);
            }else{
                Log.d(_LOG, "Going to make POST request");
                httpReq         = new HttpPost(_url);
                ((HttpPost)httpReq).setEntity(new UrlEncodedFormEntity(pParams));
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


}