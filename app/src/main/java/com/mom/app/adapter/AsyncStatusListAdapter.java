package com.mom.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.util.LongSparseArray;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mom.app.R;
import com.mom.app.ui.TransactionRequest;
import com.mom.app.utils.AppConstants;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by vaibhavsinha on 10/9/14.
 */
public class AsyncStatusListAdapter extends ArrayAdapter<TransactionRequest> {
    String _LOG     = AppConstants.LOG_PREFIX + "ASYNC_ADAP";

    LongSparseArray<TransactionRequest> _idAsyncMap;

    public AsyncStatusListAdapter(Context context, int resource, LongSparseArray<TransactionRequest> list) {
        super(context, resource);
        if(list == null){
            throw new IllegalArgumentException("cannot send null list to init");
        }

        _idAsyncMap = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row        = convertView;
        ViewHolder holder;
        final TransactionRequest asyncTask         = getItem(position);

        if(row == null){
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            row                     = inflater.inflate(R.layout.async_progress_item, parent, false);
            holder                  = new ViewHolder();

            holder.tvDate           = (TextView) row.findViewById(R.id.dateStarted);
            holder.tvAmount         = (TextView) row.findViewById(R.id.amount);
            holder.tvDescription    = (TextView) row.findViewById(R.id.asyncTaskDescription);

            holder.progressBar      = (ProgressBar) row.findViewById(R.id.progressBar);
            holder.doneIndicator    = (ImageView) row.findViewById(R.id.doneIndicator);
            row.setTag(holder);
        }else{
            holder              = (ViewHolder) row.getTag();
        }

        holder.tvDate.setText(DateUtils.formatSameDayTime(
                asyncTask.getDateStarted().getTime(),
                new Date().getTime(),
                DateFormat.SHORT,
                DateFormat.SHORT
        ));

        holder.tvDescription.setText(asyncTask.getDescription());

        DecimalFormat df = new DecimalFormat("#,###,###,##0");
        String amount   = df.format(asyncTask.getAmount());
        holder.tvAmount.setText(getContext().getResources().getString(R.string.Rupee) + amount);

        if(asyncTask.isCompleted()){
            holder.progressBar.setVisibility(View.GONE);

            switch (asyncTask.getStatus()){
                case SUCCESSFUL:
                    holder.tvDescription.setTextColor(getContext().getResources().getColor(R.color.green));
                    holder.doneIndicator.setImageResource(R.drawable.tick);
                    break;
                case FAILED:
                    holder.tvDescription.setTextColor(getContext().getResources().getColor(R.color.red));
                    holder.doneIndicator.setImageResource(R.drawable.cross);
                    break;
                case PENDING:
                    holder.tvDescription.setTextColor(getContext().getResources().getColor(R.color.amber));
                    holder.doneIndicator.setImageResource(R.drawable.pending);
                    break;
            }

            holder.doneIndicator.setVisibility(View.VISIBLE);
        }

        return row;
    }

    public boolean contains(TransactionRequest request){
        if(_idAsyncMap == null){
            throw new IllegalStateException("This map can't be null here");
        }

        if(_idAsyncMap.get(request.id) != null){
            return true;
        }

        return false;
    }

    public TransactionRequest getTransactionRequest(String txnId){
        if(_idAsyncMap == null){
            throw new IllegalStateException("This map can't be null here");
        }

        if(TextUtils.isEmpty(txnId)){
            return null;
        }
        Long id         = null;

        try{
            id          = Long.valueOf(txnId);
        }catch(NumberFormatException nfe){
            Log.e(_LOG, "Error parsing id", nfe);
            return null;
        }

        return _idAsyncMap.get(id);
    }

    public void addTransaction(TransactionRequest request){
        _idAsyncMap.put(request.id, request);
    }

    public void updateCompletedTransaction(String sId, int code){
        try{
            Long id     = Long.decode(sId);
            TransactionRequest request  = _idAsyncMap.get(id);
            if(request != null){
                request.setCompleted(true);
                request.setDateCompleted(new Date());
                TransactionRequest.RequestStatus status = TransactionRequest.RequestStatus.getStatus(code);
                if(status == null){
                    Log.e(_LOG, "Could not resolve status code: " + code);
                    return;
                }

                request.setStatus(status);
            }
        }catch(Exception e){
            Log.e(_LOG, "Exception: ", e);
        }
    }

    @Override
    public TransactionRequest getItem(int position) {
        return _idAsyncMap.valueAt(position);
    }

    static class ViewHolder{
        TextView tvDate;
        TextView tvDescription;
        TextView tvAmount;

        ProgressBar progressBar;
        ImageView doneIndicator;
    }
}
