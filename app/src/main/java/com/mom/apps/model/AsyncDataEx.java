package com.mom.apps.model;

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
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;

import com.mom.apps.ui.TransactionRequest;
import com.mom.apps.utils.AppConstants;


public class AsyncDataEx extends AsyncTask<NameValuePair, Integer, String>{
    String _LOG             = AppConstants.LOG_PREFIX + "ASYNC_DATAEX";
    public static enum HttpMethod{GET, POST};
    HttpMethod _httpMethod;
	AsyncListener<TransactionRequest> _callback;
	String _url;
    TransactionRequest _request;
	protected DataExImpl.Methods _callbackData;


    public AsyncDataEx(AsyncListener<TransactionRequest> pListener){
        _callback           = pListener;
    }

    public AsyncDataEx(
            AsyncListener<TransactionRequest> pListener,
            TransactionRequest request,
            String psUrl,
            DataExImpl.Methods pCallbackData,
            HttpMethod method
    ){
        this._url			= psUrl;
        _request            = request;
        this._callback		= pListener;
        this._callbackData  = pCallbackData;
        this._httpMethod    = method;
    }

	public AsyncDataEx(
            AsyncListener<TransactionRequest> pListener,
            TransactionRequest request,
            String psUrl,
            DataExImpl.Methods pCallbackData
    ){
		_url			= psUrl;
		_callback		= pListener;
        _request        = request;
        _callbackData  = pCallbackData;
        _httpMethod    = HttpMethod.POST;
	}

    public void setListener(AsyncListener listener){
        _callback   = listener;
    }

    public void setUrl(String url){
        _url            = url;
    }

    public void setCallerMethod(DataExImpl.Methods method){
        _callbackData   = method;
    }

	@Override
	protected String doInBackground(NameValuePair...pList) {
		return postData(Arrays.asList(pList));
	}

	protected void onPostExecute(String result){

        Log.d(_LOG, "Called onPostExecute of listener, calling listener");
        if(_request != null) {
            _request.setRemoteResponse(result);
        }
        _callback.onTaskSuccess(_request, _callbackData);
        Log.d(_LOG, "Called onTaskSuccess of listener: " + result);
	}
	
	protected void onProgressUpdate(Integer... progress){

	}


    @Override
    protected void onCancelled() {
        super.onCancelled();


       Log.e("\n\n\n**************AsynTaskCancelled*************" , "test");
DataExImpl._listener = null;

    }

    public String postData(List<NameValuePair> pParams) {
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();

		try {
            HttpRequestBase httpReq         = null;

            if(_httpMethod == HttpMethod.GET){
                Log.d(_LOG, "Going to make GET request");
                String params   = URLEncodedUtils.format(pParams, AppConstants.UTF_8);
                _url            = _url + "?" + params;
                Log.d(_LOG, "Calling: " + _url);
                httpReq         = new HttpGet(_url);
            }else{
                Log.d(_LOG, "Going to make POST request");
                httpReq         = new HttpPost(_url);
                ((HttpPost)httpReq).setEntity(new UrlEncodedFormEntity(pParams));
            }

            Log.d(_LOG, "Starting request");

           // SystemClock.sleep(3000);

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httpReq);
			HttpEntity entity 				= response.getEntity();
			String sResponse 				= EntityUtils.toString(entity);
            Log.d(_LOG, "Response: " + sResponse);
            Log.d(_LOG , _url);
            if(_request != null){
                Log.d(_LOG, "TransactionRequest.id: " + _request.getId());
            }else{
                Log.w(_LOG, "TransactionRequest is null");
            }

            Log.d(_LOG , pParams.toString());
            return sResponse;
		}catch (Exception e) {
            e.printStackTrace();
            Log.i("Async", e.getMessage());
		}
        return null;
	}


}