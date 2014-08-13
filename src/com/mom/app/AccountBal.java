package com.mom.app;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.util.Log;

public class AccountBal {
	private String responseBody;
	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	
	
	
	private class GetLoginTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			
			
			return responseBody;
		}

		@Override
		protected void onPostExecute(String result) {
			try{
				 HttpClient httpclient = new DefaultHttpClient();

					
					HttpPost httppost = new HttpPost(
							"http://61.16.219.11/aadhar/Authentication.asmx/AuthenticateSingleFingerApp");
				
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
				nameValuePairs.add(new BasicNameValuePair("AadharNumber", "12345"));
				nameValuePairs.add(new BasicNameValuePair("BiometricsData", "RKSCAyyy"));
				nameValuePairs.add(new BasicNameValuePair("FingerPosition", "LEFT_THUMB"));
				nameValuePairs.add(new BasicNameValuePair("RemittanceAmount", "10"));
				final HttpParams httpParams = httpclient.getParams();
				HttpConnectionParams.setConnectionTimeout(httpParams, 15000);
				HttpConnectionParams.setSoTimeout(httpParams, 45000);
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request

				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				responseBody= EntityUtils.toString(entity);
				String check = responseBody;
				Log.i("postData", response.getStatusLine().toString());
				Log.i("postData", responseBody);
				InputStream in = new ByteArrayInputStream(	responseBody.getBytes("UTF-8"));
					//new XmlPullParsing(in);
			}
				catch (Exception ex) {
					ex.printStackTrace();
				
					Log.e("AccountBal", ex.getMessage());
				}
			}
		}
	
}
