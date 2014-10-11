package com.mom.app.activity;

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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.mom.app.R;
import com.mom.app.identifier.ActivityIdentifier;
import com.mom.app.identifier.IdentifierUtils;
import com.mom.app.identifier.PlatformIdentifier;
import com.mom.app.model.AsyncListener;
import com.mom.app.model.AsyncResult;
import com.mom.app.model.DataExImpl;
import com.mom.app.model.local.EphemeralStorage;
import com.mom.app.utils.AppConstants;

public class MobileRechargeActivity extends MOMActivityBase implements AsyncListener<String>{
    String _LOG         = AppConstants.LOG_PREFIX + "MOBILE";

    private EditText rechargeTargetPhone;
	private EditText amountField;

	Spinner operatorSpinner;
	RadioButton rbtnTopUp, rbtnValidity, rbtnSpecial;

	String responseBody;


	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mobile_recharge);
        _currentPlatform        = IdentifierUtils.getPlatformIdentifier(getApplicationContext());

        this.operatorSpinner    = (Spinner) findViewById(R.id.Operator);
		this.rechargeTargetPhone
                                = (EditText) findViewById(R.id.rechargeTargetPhone);

        this.amountField        = (EditText) findViewById(R.id.amount);
        this.rbtnTopUp          = (RadioButton) findViewById(R.id.rbtnTopUp);
		this.rbtnValidity       = (RadioButton) findViewById(R.id.rbtnValidity);
		this.rbtnSpecial        = (RadioButton) findViewById(R.id.rbtnSpecial);

        getAllOperators();

		addListenerOnSpinnerItemSelection();
        getProgressBar().setVisibility(View.GONE);
	}


    @Override
    public void onTaskSuccess(String result, DataExImpl.Methods callback) {
        Log.d(_LOG, "Called back");
        switch(callback){
            case RECHARGE_MOBILE:
                if(result == null){
                    Log.d(_LOG, "Obtained NULL recharge response");
                    showMessage(getResources().getString(R.string.error_recharge_failed));
                    return;
                }
                Log.d(_LOG, "Going to get new balance");
                getBalance();
                Log.d(_LOG, "Starting navigation to TxnMsg Activity");
                navigateToTransactionMessageActivity(ActivityIdentifier.MOBILE_RECHARGE, result);
            break;

            case MOBILE_RECHARGEPBX:
                if(result == null){
                    Log.d(_LOG, "Obtained NULL recharge response");
                    showMessage(getResources().getString(R.string.error_recharge_failed));
                    return;
                }
                Log.d(_LOG, "Going to get new balance");
                getBalance();
                Log.d(_LOG, "Starting navigation to TxnMsg Activity");
                navigateToTransactionMessageActivity(ActivityIdentifier.MOBILE_RECHARGEPBX , result);
                break;
        }

        getProgressBar().setVisibility(View.GONE);
    }

    @Override
    public void onTaskError(AsyncResult pResult, DataExImpl.Methods callback) {

    }

    @Override
    protected void showBalance(float pfBalance) {
    }

	public void addListenerOnSpinnerItemSelection() {

		operatorSpinner
				.setOnItemSelectedListener(new CustomOnItemSelectedListener());
	}

	public String[] getAllOperators() {
		String[] strResponse1 = null;
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		if (_currentPlatform == PlatformIdentifier.NEW)
		{
			try {
				String[] strOperators = new String[] {
						"Select Service Provider", "AIRCEL", "AIRTEL",
						"BSNL", "DATACOMM", "IDEA", "LOOP", "MOM CARD REFILL",
						"MTNL", "MTS", "QUE MOBILE", "RELIANCE CDMA",
						"RELIANCE GSM", "STEL", "TATA", "TATA DOCOMO",
						"TATA WALKY", "UNINOR", "VIRGIN", "VODAFONE" };

				ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
						this, android.R.layout.simple_spinner_item,
						strOperators);
				dataAdapter
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				operatorSpinner.setAdapter(dataAdapter);

			} catch (Exception ex) {
				String[] strResponse = { "NO ITEM TO DISPLAY" };
				strResponse1 = strResponse;
			}

		} else if (_currentPlatform == PlatformIdentifier.PBX) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://180.179.67.76/MobAppS/PbxMobApp.ashx");
			try {

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						2);
				nameValuePairs.add(new BasicNameValuePair("OT", "1"));
				nameValuePairs.add(new BasicNameValuePair("Service", "ON"));

				httppost.addHeader("ua", "android");
				final HttpParams httpParams = httpclient.getParams();
				HttpConnectionParams.setConnectionTimeout(httpParams, 15000);
				HttpConnectionParams.setSoTimeout(httpParams, 15000);
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				responseBody = EntityUtils.toString(entity);
				String strOperators = responseBody;
				Log.i("postData", response.getStatusLine().toString());
				Log.i("postData", this.responseBody);
				String[] strArrOperators = strOperators.split("\\|");

				strResponse1 = strArrOperators;

				ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
						this, android.R.layout.simple_spinner_item,
						strResponse1);
				dataAdapter
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				operatorSpinner.setAdapter(dataAdapter);

			} catch (Exception ex) {
				String[] strResponse = { "NO ITEM TO DISPLAY" };
				strResponse1 = strResponse;
			}
		} else {
			Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG)
					.show();
		}

		return strResponse1;

	}

	private String getOperatorId(String strOperatorName) {

		if (_currentPlatform == PlatformIdentifier.NEW)
		{
            String id      = AppConstants.OPERATOR_NEW.get(strOperatorName);
            if(id == null){
                return "-1";
            }

            return id;

		} else if (_currentPlatform == PlatformIdentifier.PBX) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://180.179.67.76/MobAppS/PbxMobApp.ashx");
			try {

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						3);
				nameValuePairs.add(new BasicNameValuePair("SN", strOperatorName));
				nameValuePairs.add(new BasicNameValuePair("Service", "OSN"));
				nameValuePairs.add(new BasicNameValuePair("OT", "1"));

				httppost.addHeader("ua", "android");

				final HttpParams httpParams = httpclient.getParams();
				HttpConnectionParams.setConnectionTimeout(httpParams, 15000);
				HttpConnectionParams.setSoTimeout(httpParams, 15000);
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				responseBody = EntityUtils.toString(entity);

                return responseBody;
			} catch (Exception ex) {
				return "-1";
			}
		} else {
			return "-1";
		}
	}


	public void validateAndRecharge(View view) {
        if (_currentPlatform == PlatformIdentifier.NEW) {
            if (operatorSpinner.getSelectedItemPosition() < 1) {
                showMessage(getResources().getString(R.string.prompt_select_operator));
                return;
            }

            String sOperator = operatorSpinner.getSelectedItem().toString();
            String sOperatorId = getOperatorId(sOperator);

            int nMinAmount = 10;
            int nMaxAmount = 10000;
            int nMinPhoneLength = 10;
            int nMaxPhoneLength = 10;
            int nExactPhoneLength = 10;

            int nPhoneLength = rechargeTargetPhone.getText().toString().length();
            int nAmount = 0;

            try {
                nAmount = Integer.parseInt(amountField.getText().toString());
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
                showMessage(getResources().getString(R.string.prompt_numbers_only_amount));
                return;
            }

            if (
                    sOperatorId.equals(AppConstants.OPERATOR_ID_TATA_WALKY) ||
                            sOperatorId.equals("TWT")
                    ) {

                nMinPhoneLength = 1;
                nMaxPhoneLength = 12;
            }

            if (nPhoneLength < nMinPhoneLength || nPhoneLength > nMaxPhoneLength) {
                if (nMinPhoneLength == nMaxPhoneLength) {
                    showMessage(String.format(getResources().getString(R.string.error_phone_length), nMinPhoneLength));
                } else {
                    showMessage(String.format(getResources().getString(R.string.error_phone_length_min_max), nMinPhoneLength, nMaxPhoneLength));
                }
                return;
            }

            if (nAmount < nMinAmount || nAmount > nMaxAmount) {
                showMessage(String.format(getResources().getString(R.string.error_amount_min_max), nMinAmount, nMaxAmount));
                return;
            }
        }

       confirmRecharge();
	}

	private void startRecharge() {
	/*	String[] strResponse1 = null;

        String sConsumerNumber          = rechargeTargetPhone.getText().toString();
        String sRechargeAmount          = amountField.getText().toString();
        String sOperatorID              = getOperatorId(operatorSpinner.getSelectedItem().toString());
        int nRechargeType               = 0;


        if (rbtnValidity.isChecked()) {
            nRechargeType               = 1;
        } else if (rbtnSpecial.isChecked()) {
            nRechargeType               = 2;
        }*/

        String sConsumerNumber          = rechargeTargetPhone.getText().toString();
        String sRechargeAmount          = amountField.getText().toString();

		if (_currentPlatform == PlatformIdentifier.NEW){
            String[] strResponse1 = null;


            String sOperatorID              = getOperatorId(operatorSpinner.getSelectedItem().toString());
            int nRechargeType               = 0;
            if (rbtnValidity.isChecked()) {
                nRechargeType               = 1;
            } else if (rbtnSpecial.isChecked()) {
                nRechargeType = 2;
            }
           getDataEx(this).rechargeMobile(sConsumerNumber, Double.parseDouble(sRechargeAmount), sOperatorID, nRechargeType);

        } else if (_currentPlatform == PlatformIdentifier.PBX){

            String psoperator = "AIR";
        //    getDataEx(this).getOperatorNames();
            getDataEx(this).rechargeMobilePBX(sConsumerNumber, psoperator , Double.parseDouble(sRechargeAmount));

			/*HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://180.179.67.76/MobAppS/PbxMobApp.ashx");
			try {

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						5);
				nameValuePairs.add(new BasicNameValuePair("CustMobile", sConsumerNumber));
				nameValuePairs.add(new BasicNameValuePair("Amount", sRechargeAmount));
				nameValuePairs.add(new BasicNameValuePair("OP", sOperatorID));
                String sUserMobile  = EphemeralStorage.getInstance(this).getString(AppConstants.LOGGED_IN_USERNAME, null);
				nameValuePairs.add(new BasicNameValuePair("RN", sUserMobile));
				nameValuePairs.add(new BasicNameValuePair("Service", "RM"));

				final HttpParams httpParams = httpclient.getParams();
				// HttpConnectionParams.setConnectionTimeout(httpParams, 15000);
				// HttpConnectionParams.setSoTimeout(httpParams, 15000);
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				responseBody = EntityUtils.toString(entity);


				if (responseBody.contains("~")) {
					strResponse1 = responseBody.split("~");

					StringBuilder sb = new StringBuilder();
					sb.append("StatusCode : " + strResponse1[0].toString()
							+ "\n" + "TransId : " + strResponse1[2].toString()
							+ "\n" + "Message:" + strResponse1[1].toString()
							+ "\n" + "Balance:" + strResponse1[3].toString());

					sb.append("\n");
					sb.append("\n");

					String a = sb.toString();

					showMessage(a);

					Log.i("postData~", response.getStatusLine().toString());
					Log.i("info", a);
				}else {
                    showMessage(responseBody);
				}
			} catch (Exception e) {
				Log.e("log_tagTESTabcd",
						"Error in http connection " + e.toString());

			}*/

		} else {
			Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG)
					.show();
		}

	}



	public void goBack(View view) {
        Intent intent = new Intent(this, DashboardActivity.class);
		startActivity(intent);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		this.finish();
		return;
	}


	public void confirmRecharge() {
        getProgressBar().setVisibility(View.VISIBLE);


			AlertDialog.Builder alertDialog = new AlertDialog.Builder(
					MobileRechargeActivity.this);

			// Setting Dialog Title
			alertDialog.setTitle("Confirm MobileRecharge...");

			// Setting Dialog Message
		/*	alertDialog.setMessage("Mobile Number:" + " "
					+ rechargeTargetPhone.getText().toString() + "\n" + "Operator:"
					+ " " + operatorSpinner.getSelectedItem().toString() + "\n"
					+ "Amount:" + " " + "Rs." + " "
					+ amountField.getText().toString());*/

        alertDialog.setMessage("Mobile Number:" + " "
                + rechargeTargetPhone.getText().toString() + "\n" + "Operator:"
                + " "+ "AIRTEL" + "\n"
                + "Amount:" + " " + "Rs." + " "
                + amountField.getText().toString());

			alertDialog.setPositiveButton("YES",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							((AlertDialog) dialog).getButton(
									AlertDialog.BUTTON1).setEnabled(false);
							startRecharge();
//							new GetLoginTask().onPostExecute("test");

						}
					});

			// Setting Negative "NO" Button
			alertDialog.setNegativeButton("NO",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which1) {

							dialog.cancel();

						}
					});

			alertDialog.show();
	}

	public class CustomOnItemSelectedListener implements OnItemSelectedListener {
		public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

            rechargeTargetPhone.setText("");
            amountField.setText("");

            rbtnTopUp.setChecked(false);
            rbtnValidity.setChecked(false);

            String sOperator    = operatorSpinner.getSelectedItem().toString();
            String sOperatorId  = getOperatorId(sOperator);

            if(
                sOperatorId.equals(AppConstants.OPERATOR_ID_BSNL) ||
                sOperatorId.equals(AppConstants.OPERATOR_ID_MTNL)
                ){

                rbtnTopUp.setVisibility(view.VISIBLE);
                rbtnValidity.setVisibility(view.VISIBLE);
                rbtnSpecial.setVisibility(view.GONE);
                rbtnTopUp.setChecked(true);
                rbtnValidity.setChecked(false);
            }else if(sOperatorId.equals(AppConstants.OPERATOR_ID_TATA_DOCOMO) ||
                    sOperatorId.equals(AppConstants.OPERATOR_ID_UNINOR)
                    ){

                rechargeTargetPhone.setText("");
                amountField.setText("");
                rbtnTopUp.setVisibility(view.VISIBLE);
                rbtnValidity.setVisibility(view.GONE);
                rbtnSpecial.setVisibility(view.VISIBLE);
                rbtnTopUp.setChecked(true);
                rbtnValidity.setChecked(false);
            }
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {

		}

	}

}