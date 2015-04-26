package com.mom.app.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.mom.app.R;
import com.mom.app.identifier.IdentifierUtils;
import com.mom.app.identifier.PlatformIdentifier;
import com.mom.app.model.AsyncListener;
import com.mom.app.model.AsyncResult;
import com.mom.app.model.DataExImpl;
import com.mom.app.model.Transaction;
import com.mom.app.utils.AppConstants;
import com.mom.app.widget.TextListViewAdapter;

import java.util.ArrayList;

/**
 * Created by vaibhavsinha on 10/14/14.
 */
public class TransactionHistoryFragment extends FragmentBase implements AsyncListener<ArrayList<Transaction>> {
    private TextView _titleView;
    private ListView _listView;
    private TextView _tvNoTrans;

    public static TransactionHistoryFragment newInstance(PlatformIdentifier currentPlatform){
        TransactionHistoryFragment fragment        = new TransactionHistoryFragment();
        Bundle bundle                       = new Bundle();
        bundle.putSerializable(AppConstants.ACTIVE_PLATFORM, currentPlatform);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_transaction_history, null, false);

        _currentPlatform        = IdentifierUtils.getPlatformIdentifier(getActivity());
        _titleView              = (TextView) view.findViewById(R.id.transactionHistoryHeader);
        _listView               = (ListView) view.findViewById(R.id.listView);
        _tvNoTrans              = (TextView) view.findViewById(R.id.noTransactionsMsg);
        Log.d("HISTORY", "onCreate: calling getHistory");
        getHistory();
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _currentPlatform        = (PlatformIdentifier) getArguments().getSerializable(AppConstants.ACTIVE_PLATFORM);
    }

    private void initListView(ArrayList<Transaction> list){
        Log.d("HISTORY", "Obtained result: " + list);

        if(list == null || list.size() < 1) {
            setNoTransactionHistoryMessage();
            return;
        }

        Log.d("HISTORY", "Creating list with " + list.size() + " transactions");

        TextListViewAdapter adapter                = new TextListViewAdapter(
                getActivity(),
                R.layout.listview_transaction_history,
                list
        );

        _titleView.setText(String.format(getResources().getString(R.string.title_activity_transaction_history_count), list.size()));
        _listView.setVisibility(View.VISIBLE);
        _listView.setAdapter(adapter);
        Log.d("HISTORY", "ListView created");
    }

    private void setNoTransactionHistoryMessage(){
        _titleView.setText(getResources().getString(R.string.title_activity_transaction_history));

        _tvNoTrans.setVisibility(View.VISIBLE);
    }

    @Override
    public void onTaskSuccess(ArrayList<Transaction> result, DataExImpl.Methods callback) {
        Log.d("HISTORY" , "Result: " + result);
        initListView(result);
        showProgress(false);
        showBalance();
    }

    @Override
    public void onTaskError(AsyncResult pResult, DataExImpl.Methods callback) {
        Log.e(_LOG, "Error getting history");
        showBalance();
    }



    public void getHistory(){
        showProgress(true);
//        showBalance();
        getDataEx(this).getTransactionHistory();
    }

}

