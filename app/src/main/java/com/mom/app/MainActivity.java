
package com.mom.app;


import com.mom.app.activity.VerifyTPinActivity;
import com.mom.app.identifier.ActivityIdentifier;
import com.mom.app.identifier.PlatformIdentifier;
import com.mom.app.model.AsyncListener;
import com.mom.app.model.DataExImpl;
import com.mom.app.model.IDataEx;
import com.mom.app.model.local.LocalStorage;
import com.mom.app.model.newpl.NewPLDataExImpl;
import com.mom.app.utils.MOMConstants;


import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends ListActivity implements AsyncListener{
    private String responseBody;
    private String session_id;
//    private TextView responsetxt;
//    Helpz myHelpz = new Helpz();
//    Intent myintent = new Intent();
//    Intent myintent1 = new Intent();
//    Intent myintent2 = new Intent();
//    Intent myintent3 = new Intent();
//    Intent myintent4 = new Intent();
//    Intent myintent5 = new Intent();
//    Intent myintent6 = new Intent();
//    Intent intentMain = new Intent();
//    Intent swipeintent = new Intent();

//    WebView wv;
//    ListView lv;
//    Button save, logout, relogout, passchng, repasschng, back;
//    EditText op, np, np1;
//    TextView res;
//    String url, check, output;
//    String error = "2";
//    String flag = "2";
//    HashMap<String, String> responseMap = new HashMap<String, String>();
//    String pairs[];
//    ProgressDialog dialog;
//    Intent myintentlogin = new Intent();
//    Intent reintent = new Intent();
//    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
//    ArrayList<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>(2);

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("MainActivity", "started onCreate");
        //String[] values = new String[] { "My Info", "Change Recharge Password", "Change Password", "Recharge Logout","Logout"};
//        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        String sActivePL    = LocalStorage.getString(getApplicationContext(), MOMConstants.ACTIVE_PLATFORM);
        String[] values     = null;

        if (PlatformIdentifier.NEW.toString().equals(sActivePL))
        {
            getBalance();
            values = new String[]{"Mobile Recharge", "DTH Recharge", "Bill Payment", "Card Sale", "History", "Settings"};
        } else {
            values = new String[]{"Mobile Recharge", "DTH Recharge", "Bill Payment", "Utility Bill Payment", "History", "Settings"};

        }

        setContentView(R.layout.main);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.row_layout, R.id.label, values);
        setListAdapter(adapter);

//        wv = (WebView) findViewById(R.id.resp);
//        lv = (ListView) findViewById(android.R.id.list);

//        this.res = (TextView) findViewById(R.id.response);

//        this.back = (Button) findViewById(R.id.button6);

//        intentMain = new Intent(this, MainActivity.class);
        getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.appsbg));
//        this.responsetxt = (TextView) findViewById(R.id.textView1);
//        SaveSessionData();
        //	rechargePost();
//        new GetLoginTask().onPostExecute("test");


    }

    @Override
    public void onTaskComplete(String result, DataExImpl.Methods pMethod) {
        Log.d("TASK_C_MAIN", "Called back");
        switch(pMethod){
            case GET_BALANCE:
                Log.d("TASK_C_MAIN", "Balance returned: " + result);
                TextView balanceTxtView = (TextView) findViewById(R.id.textView1);
                if(balanceTxtView != null){
                    balanceTxtView.setVisibility(View.VISIBLE);
                    balanceTxtView.setText("Balance: " + result);
                    Log.d("TASK_C_MAIN", "Balance TextView set");
                }
            break;
        }
    }

    public void getBalance(){
        Log.d("MAIN", "Getting Balance");
        IDataEx dataEx  = new NewPLDataExImpl(this, getApplicationContext());
        Log.d("MAIN", "DataEx instance created");
        dataEx.getBalance();
        Log.d("MAIN", "getBalance called");
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String item = (String) getListAdapter().getItem(position);
        Intent intent   = null;
        Log.d("LIST_CLICKED", "Going to start activity");
        if (item.equals("Mobile Recharge")) {
            Log.d("LIST_CLICKED", "Starting Mobile Recharge");
            intent = new Intent(this, VerifyTPinActivity.class);
            intent.putExtra(MOMConstants.INTENT_MESSAGE_DEST, ActivityIdentifier.HOME);
            intent.putExtra(MOMConstants.INTENT_MESSAGE_ORIGIN, ActivityIdentifier.MAIN);
            startActivity(intent);
            Log.d("LIST_CLICKED", "Started Mobile Recharge");
            return;
//            intent = new Intent(MainActivity.this, HomeActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//            finish();

        }else if (item.equals("DTH Recharge")) {
            intent = new Intent(MainActivity.this, HomeActivity1.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();

        }else if (item.equals("Bill Payment")) {
            intent = new Intent(MainActivity.this, HomeActivity2.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }else if (item.equals("Card Sale")) {
            intent = new Intent(MainActivity.this, MSwipeAndroidSDKListActivity1.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();

        }else if (item.equals("Utility Bill Payment")) {
            intent = new Intent(MainActivity.this, HomeBillActivity_PBX.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();

        }else if (item.equals("History")) {

            intent = new Intent(MainActivity.this, HistoryActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }else if (item.equals("Settings")) {
            intent = new Intent(MainActivity.this, InfoActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    public void logout(View v) {
//		Helpz myhelp = new Helpz();
        //SharedPreferences appSettings = getSharedPreferences(LoginActivity.PREFERENCE_FILENAME, MODE_PRIVATE);
        //session_id = appSettings.getString("user_session","-1");
        //url="https://recharge123.com/mobileapp/logout.php";
        //	this.sendpost(url);
        //	SharedPreferences.Editor prefEditor = appSettings.edit();
        //	prefEditor.putString("user_session","null");
        //	prefEditor.commit();
        this.finish();
        Helpz.SetLogoutVariable(false);
        LocalStorage.storeLocally(getApplicationContext(), MOMConstants.PREF_IS_LOGGED_IN, false);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

        //session_id = appSettings.getString("user_session","-1");
        //Log.i("logout",session_id );
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            System.out.println("KEYCODE_BACK");
            showDialog("BACK");
            return true;
        }
        return false;
    }

    void showDialog(String the_key){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        final Intent intentMain = new Intent(this, MainActivity.class);

        alertDialog.setMessage("You have pressed the BACK  button. Would you like to exit the app?")
                .setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Helpz myhelp = new Helpz();
                        myhelp.SetLogoutVariable(false);
                        intentMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialog.create();
        alert.setTitle("Message");
        alert.show();
    }

//    private class GetLoginTask extends AsyncTask<Void, Void, String> {
//
//        @Override
//        protected String doInBackground(Void... params) {
//            return responseBody;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//
//            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//
//            if (pref.getString("user_sessionMOM", "test").equals("MOM"))
//
//            {
//                responsetxt.setVisibility(View.VISIBLE);
//                responsetxt.setText("Bal: Rs." + myHelpz.GetRMNAccountBal().toString());
//                Log.i("postDataAccntMOM", myHelpz.GetRMNAccountBal().toString());
//
//            } else if (pref.getString("user_sessionMOM", "test").equals("PBX")) {
//
//
//                responsetxt.setVisibility(View.VISIBLE);
//                responsetxt.setText("Bal: Rs." + myHelpz.GetRMNAccountBal().toString());
//                Log.i("postDataAccntPBX", myHelpz.GetRMNAccountBal().toString());
//            } else {
//                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
//            }
//        }
//    }

//    public class XmlPullParsing {
//
//        protected XmlPullParser xmlpullparser1;
//        String output1;
//        String TAG = "XmlPullParsing";
//
//        public XmlPullParsing(InputStream is) {
//
//
//            XmlPullParserFactory factory = null;
//            try {
//                factory = XmlPullParserFactory.newInstance();
//            } catch (XmlPullParserException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//            factory.setNamespaceAware(true);
//            try {
//                xmlpullparser1 = factory.newPullParser();
//            } catch (XmlPullParserException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//
//
//            try {
//                xmlpullparser1.setInput(is, "UTF-8");
//            } catch (XmlPullParserException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//
//
//            int eventType = 0;
//            try {
//                eventType = xmlpullparser1.getEventType();
//            } catch (XmlPullParserException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//            while (eventType != XmlPullParser.END_DOCUMENT) {
//
//                parseTag(eventType);
//                try {
//                    eventType = xmlpullparser1.next();
//                } catch (XmlPullParserException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//
//
//        }
//
//        void parseTag(int event) {
//
//            switch (event) {
//
//                case XmlPullParser.START_DOCUMENT:
//                    Log.i(TAG, "START_DOCUMENT");
//                    break;
//
//                case XmlPullParser.END_DOCUMENT:
//                    Log.i(TAG, "END_DOCUMENT");
//                    break;
//                case XmlPullParser.START_TAG:
//                    Log.i(TAG, "START_TAG" + xmlpullparser1.getName());
//                    Log.i(TAG, "Attribute Name" + xmlpullparser1.getAttributeValue(null, "category"));
//
//                    break;
//
//                case XmlPullParser.END_TAG:
//                    Log.i(TAG, "END_TAG" + xmlpullparser1.getName());
//
//                    break;
//
//                case XmlPullParser.TEXT:
//                    Log.i(TAG, "TEXT");
//                    output = xmlpullparser1.getText();
//                    String newoutputrecharge = output;
//
//                    //////////        Toast.makeText(InfoActivity.this, newoutputrecharge, Toast.LENGTH_LONG).show();
//
//                    responsetxt.setVisibility(View.VISIBLE);
//                    responsetxt.setText("Bal: Rs. " + newoutputrecharge);
//                    //response.setText("Bal: Rs. 100000000");
//                    Helpz.SetRMNAccountBal(newoutputrecharge);
//                    break;
//
//            }
//
//
//        }
//
//    }


//    public boolean SaveSessionData() {
//        try {
//            Helpz myHelp = new Helpz();
//            myHelp.SetLogoutVariable(true);
//            return true;
//        } catch (Exception ex) {
//            return false;
//        }
//
//    }


	
//	@Override
//	public void onDestroy() {
//		super.onDestroy();
//		}
//	@Override
//	public void onUserLeaveHint() {
//		//super.onUserLeaveHint();
////		Intent intent =new Intent(this,MainActivity.class);
////		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////		startActivity(intent);
//        this.finish();
//		
//	}
	
	
	/*public void onBackPressed() {
		
		this.finish();
		
	}*/
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event)
//	{
//	    if ((keyCode == KeyEvent.KEYCODE_BACK))
//	    {
//	    	
//	        return true;
//	        
//	    }
//	    return super.onKeyDown(keyCode, event);
//	   
//	}

	
	/*public boolean onKeyDown(int keyCode, KeyEvent event)  {
	    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.ECLAIR
	        && keyCode == KeyEvent.KEYCODE_BACK
	    && event.getRepeatCount() == 0) 
	    {
	        onBackPressed();
	    }
	    return super.onKeyDown(keyCode, event);
	}
	 
	@Override
	public void onBackPressed() {
	    // do what you want here
		finish();
	    return;
	}*/
}
	
