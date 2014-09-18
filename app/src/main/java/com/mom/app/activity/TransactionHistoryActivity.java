package com.mom.app.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.mom.app.R;
import com.mom.app.error.MOMException;
import com.mom.app.identifier.IdentifierUtils;
import com.mom.app.model.AsyncListener;
import com.mom.app.model.AsyncResult;
import com.mom.app.model.DataExImpl;
import com.mom.app.model.Transaction;
import com.mom.app.widget.TextListViewAdapter;

import java.util.ArrayList;

public class TransactionHistoryActivity extends MOMActivityBase implements AsyncListener<ArrayList<Transaction>>{
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

    private void initListView(ArrayList<Transaction> list){
        Log.d("HISTORY", "Obtained result: " + list);

        if(list == null || list.size() < 1) {
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
    public void onTaskSuccess(ArrayList<Transaction> result, DataExImpl.Methods callback) {
        Log.d("HISTORY" , "Result: " + result);
        initListView(result);
    }

    @Override
    public void onTaskError(AsyncResult pResult, DataExImpl.Methods callback) {
        Log.e(_LOG, "Error getting history");
    }


    @Override
    protected void showBalance(float pfBalance) {

    }

    public void getHistory(){
        getDataEx(this).getTransactionHistory();
    }

}
