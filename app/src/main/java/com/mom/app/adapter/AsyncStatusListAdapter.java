package com.mom.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mom.app.R;
import com.mom.app.ui.AsyncProgressView;

import java.util.ArrayList;

/**
 * Created by vaibhavsinha on 10/9/14.
 */
public class AsyncStatusListAdapter extends ArrayAdapter<AsyncProgressView> {

    ArrayList<AsyncProgressView> _listAsyncTasks;


    public AsyncStatusListAdapter(Context context, int resource, ArrayList<AsyncProgressView> list) {
        super(context, resource);
        _listAsyncTasks     = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row        = convertView;
        ViewHolder holder;
        final AsyncProgressView asyncTask         = _listAsyncTasks.get(position);

        if(row == null){
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            row                 = inflater.inflate(R.layout.async_progress_item, parent, false);
            holder              = new ViewHolder();

            holder.textView     = (TextView) row.findViewById(R.id.asyncTaskDescription);
            holder.progressBar  = (ProgressBar) row.findViewById(R.id.progressBar);

            row.setTag(holder);
        }else{
            holder              = (ViewHolder) row.getTag();
        }

        holder.textView.setText(asyncTask.getDescription());

        if(!asyncTask.isSuccessful()){
            holder.textView.setTextColor(getContext().getResources().getColor(R.color.red));
        }

        if(asyncTask.isCompleted()){
            holder.progressBar.setVisibility(View.GONE);
        }

        return row;
    }

    static class ViewHolder{
        TextView textView;
        ProgressBar progressBar;
    }
}
