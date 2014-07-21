package com.mom.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mom.app.R;
import com.mom.app.error.MOMException;
import com.mom.app.identifier.IdentifierUtils;
import com.mom.app.identifier.PlatformIdentifier;
import com.mom.app.model.AsyncListener;
import com.mom.app.model.AsyncResult;
import com.mom.app.model.DataExImpl;
import com.mom.app.model.IDataEx;
import com.mom.app.model.MOMTxn;
import com.mom.app.model.newpl.NewPLDataExImpl;
import com.mom.app.model.pbxpl.PBXPLDataExImpl;
import com.mom.app.widget.TextListViewAdapter;

import java.util.ArrayList;

public class TransactionHistoryActivity extends MOMActivityBase implements AsyncListener<String>{
    private TextView _titleView;
    private ListView _listView;
    private TextListViewAdapter _adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);

        _currentPlatform        = IdentifierUtils.getPlatformIdentifier(getApplicationContext());
        _titleView              = (TextView) findViewById(R.id.transactionHistoryHeader);
        _listView               = (ListView) findViewById(R.id.listView);

        Log.d("HISTORY", "onCreate: calling getHistory");
        getHistory();

    }

    private void initListView(String result){
        if(result == null || "".equals(result.trim())){
            setNoTransactionHistoryMessage();
            return;
        }

        Log.d("HISTORY", "Obtained result: " + result);
        String[] sArrTxn        = result.split("\\|");
        ArrayList<MOMTxn> list  = new ArrayList<MOMTxn>();

        Log.d("HISTORY", "Number of transactions: " + sArrTxn.length);

        try {
            for (String txt : sArrTxn) {
                MOMTxn txn = new MOMTxn(txt, "~");
                list.add(txn);
            }
        }catch(MOMException me){
            me.printStackTrace();
        }

        if(list.size() < 1) {
            setNoTransactionHistoryMessage();
            return;
        }

        Log.d("HISTORY", "Creating list with " + list.size() + " transactions");

        _adapter                = new TextListViewAdapter(this, R.layout.listview_transaction_history, list);

        _titleView.setText(String.format(getResources().getString(R.string.title_activity_transaction_history_count), list.size()));
        _listView.setVisibility(View.VISIBLE);
        _listView.setAdapter(_adapter);
        Log.d("HISTORY", "ListView created");
    }

    private void setNoTransactionHistoryMessage(){
        _titleView.setText(getResources().getString(R.string.title_activity_transaction_history));
        TextView tvNoTrans  = (TextView) findViewById(R.id.noTransactionsMsg);
        tvNoTrans.setVisibility(View.VISIBLE);
    }

    @Override
    public void onTaskSuccess(String result, DataExImpl.Methods callback) {
        Log.d("HISTORY" , "Result: " + result);
        initListView(result);
//        initListView("1/2/14~123~9810921333~100~AIRTEL~Success|1/2/14~123~9810921222~100~VODAFONE~Success|1/2/14~123~9810921222~100~IDEA~Success|1/2/14~123~9810921222~100~AIRCEL~Success");
    }

    @Override
    public void onTaskError(AsyncResult pResult, DataExImpl.Methods callback) {

    }


    @Override
    protected void showBalance(float pfBalance) {

    }

    public void getHistory(){
        getDataEx(this).getTransactionHistory();
    }

}
