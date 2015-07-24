package com.mom.app.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.mom.app.R;
import com.mom.app.adapter.PrintDocAdapter;
import com.mom.app.fragment.GiftVoucherFragment;
import com.mom.app.fragment.MobileRechargeFragment;
import com.mom.app.identifier.IdentifierUtils;
import com.mom.app.identifier.PlatformIdentifier;
import com.mom.app.model.local.EphemeralStorage;
import com.mom.app.ui.IFragmentListener;
import com.mom.app.utils.AppConstants;

public class ListActivity extends Activity{
    ListView listView ;
    Button btn_back;
    private ArrayAdapter<String> listAdapter ;
    private PlatformIdentifier _currentPlatform;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        this.listView       = (ListView) findViewById(R.id.mobile_list);
        btn_back            = (Button) findViewById(R.id.btnBack);
        _currentPlatform    = IdentifierUtils.getPlatformIdentifier(this);

        if(_currentPlatform == null){
            throw new IllegalStateException("Platform should not have been null here!");
        }

        String[] values = new String[] { "Status" + ": "+ "   " +  EphemeralStorage.getInstance(this).getString(AppConstants.PARAM_FLIP_KART_MESSAGE, null),
                "Transaction Id" + ": "+ "   " +  EphemeralStorage.getInstance(this).getString(AppConstants.PARAM_FLIP_KART_TRANSACTION_ID, null),
                "Mobile Number" + ": "+ "   " +  EphemeralStorage.getInstance(this).getString(AppConstants.PARAM_FLIP_KART_MOBILENUMBER, null),
                "Amount" + ": "+ "   " +  EphemeralStorage.getInstance(this).getString(AppConstants.PARAM_FLIP_KART_AMOUNT, null),
                "Voucher" + ": "+ "   " +  EphemeralStorage.getInstance(this).getString(AppConstants.PARAM_FLIP_KART_VOUCHER, null),
                "Pin" + ": "+ "   " +  EphemeralStorage.getInstance(this).getString(AppConstants.PARAM_FLIP_KART_PIN, null),
                "Message" + ": "+ "   " +  EphemeralStorage.getInstance(this).getString(AppConstants.PARAM_FLIP_KART_MESSAGE, null),

                    };
                    ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, values);


                  listView.setAdapter(adapter);


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                FragmentManager manager = getSupportFragmentManager();
//                FragmentTransaction transaction = manager.beginTransaction();
//                transaction.replace(R.id.contentFrame ,GiftVoucherFragment.newInstance(_currentPlatform));
//                transaction.commit();


            }
        });
    }



    @TargetApi(19)
    private void doPrint() {
        // Get a PrintManager instance
        PrintManager printManager = (PrintManager) getApplication()
                .getSystemService(Context.PRINT_SERVICE);

        // Set job name, which will be displayed in the print queue
        String jobName = getApplication().getString(R.string.app_name) + " Document";

        // Start a print job, passing in a PrintDocumentAdapter implementation
        // to handle the generation of a print document

        printManager.print(jobName, new PrintDocAdapter(), null); //
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
