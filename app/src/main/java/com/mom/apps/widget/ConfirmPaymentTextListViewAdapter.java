package com.mom.apps.widget;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mom.apps.R;
import com.mom.apps.model.pbxpl.ImpsConfirmPaymentResult;

import java.util.List;

/**
 * Created by akanksharaina
 */
public class ConfirmPaymentTextListViewAdapter extends ArrayAdapter {
    private Context context;
    private int layoutResourceId;
    private List<ImpsConfirmPaymentResult> data;

    public ConfirmPaymentTextListViewAdapter(Context context, int layoutResourceId, List<ImpsConfirmPaymentResult> data) {

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
        final ImpsConfirmPaymentResult item       = data.get(position);

        if (row == null) {
            Log.d("LIST_VIEW", "row null");
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row                 = inflater.inflate(layoutResourceId, parent, false);
            holder              = new ViewHolder();

            holder.errorMessage  = (TextView) row.findViewById(R.id.errorMessage);
            holder.txtRecieptId   = (TextView) row.findViewById(R.id.txtRecieptId);

            holder.txtTransactionId = (TextView) row.findViewById(R.id.txtTransactionId);
            row.setTag(holder);
        } else {
            holder              = (ViewHolder) row.getTag();
        }

//        row.setBackgroundColor(Color.BLUE);



        holder.errorMessage.setText("Message:" + "\n" +item.getErrorMessage());
        holder.txtTransactionId.setText("TransactionId:" + "\n" +String.valueOf(item.getTransactionId()));
        holder.txtRecieptId.setText("ReceiptId:" + "\n" +String.valueOf(item.getRecieptId()));


        return row;
    }

    static class ViewHolder {

        TextView errorMessage;
        TextView txtRecieptId;
        TextView txtTransactionId;
    }
}