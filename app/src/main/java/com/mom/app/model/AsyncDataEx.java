package com.mom.app.model;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.mom.app.identifier.PlatformIdentifier;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;


public class AsyncDataEx extends AsyncTask<NameValuePair, Integer, String>{
	AsyncListener _callback;
	String _url;
	DataExImpl.Methods _callbackData;

	public AsyncDataEx(AsyncListener pListener, String psUrl, DataExImpl.Methods pCallbackData){
		this._url			= psUrl;
		this._callback		= pListener;
        this._callbackData  = pCallbackData;
	}
	
	@Override
	protected String doInBackground(NameValuePair...pList) {
		postData(Arrays.asList(pList));
		return null;
	}

	protected void onPostExecute(Double result){

	}
	
	protected void onProgressUpdate(Integer... progress){

	}

	public void postData(List<NameValuePair> pParams) {
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(_url);

		try {
			httppost.setEntity(new UrlEncodedFormEntity(pParams));

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity 				= response.getEntity();
			String sResponse 				= EntityUtils.toString(entity);
            Log.d("AsyncDataEx", "Response: " + sResponse);

			_callback.onTaskComplete(sResponse, _callbackData);
            Log.d("AsyncDataEx", "Called onTaskComplete of listener");
		}catch (Exception e) {
            e.printStackTrace();
            Log.i("Async", e.getMessage());

		}
	}
}