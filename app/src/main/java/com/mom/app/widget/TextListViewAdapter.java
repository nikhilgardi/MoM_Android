package com.mom.app.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mom.app.R;
import com.mom.app.model.MOMTxn;

import java.util.ArrayList;

/**
 * Created by vaibhavsinha on 7/14/14.
 */
public class TextListViewAdapter extends ArrayAdapter {
    private Context context;
    private int layoutResourceId;
    private ArrayList<MOMTxn> data;

    public TextListViewAdapter(Context context, int layoutResourceId, ArrayList<MOMTxn> data) {

        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("LIST_VIEW", "Creating listView");

        View row                = convertView;
        ViewHolder holder       = null;
        final MOMTxn item       = data.get(position);

        if (row == null) {
            Log.d("LIST_VIEW", "row null");
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row                 = inflater.inflate(layoutResourceId, parent, false);
            holder              = new ViewHolder();
            holder.header       = (TextView) row.findViewById(R.id.txtHeader);
            holder.txtDateTime  = (TextView) row.findViewById(R.id.txtDateTime);
            holder.txtOperator  = (TextView) row.findViewById(R.id.txtOperator);
            holder.txtStatus    = (TextView) row.findViewById(R.id.txtStatus);
            row.setTag(holder);
        } else {
            holder              = (ViewHolder) row.getTag();
        }

//        row.setBackgroundColor(Color.BLUE);

        Log.d("LIST_VIEW", "Setting data in row: " + position + ", " + item.transactionDate);
        holder.header.setText(String.valueOf(position + 1));
        holder.txtDateTime.setText(item.transactionDate);
        holder.txtOperator.setText(item.operator + " (" + item.subscriberId + ")");
        holder.txtStatus.setText(
                context.getResources().getString(R.string.Rupee) + item.amount + ", " + item.status
        );

        return row;
    }

    static class ViewHolder {
        TextView header;
        TextView txtDateTime;
        TextView txtOperator;
        TextView txtStatus;
    }
}