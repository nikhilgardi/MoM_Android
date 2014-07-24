package com.mom.app.widget;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.mom.app.R;

import com.mom.app.widget.holder.ImageItem;

import java.util.ArrayList;

/**
 * Created by vaibhavsinha on 7/14/14.
 */
public class ImageTextViewAdapter extends ArrayAdapter{
    private Context context;
    private int layoutResourceId;
    private ArrayList<ImageItem> data;

    public ImageTextViewAdapter(Context context, int layoutResourceId, ArrayList<ImageItem> data) {

        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row                = convertView;
        ViewHolder holder       = null;
        final ImageItem item          = data.get(position);

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row                 = inflater.inflate(layoutResourceId, parent, false);
            row.setBackground(context.getResources().getDrawable(R.drawable.rounded_rect));

            holder              = new ViewHolder();
            holder.imageTitle   = (TextView) row.findViewById(R.id.text);
            ImageView imgView   = (ImageView) row.findViewById(R.id.image);
            holder.image        = imgView;
            row.setTag(holder);
        } else {
            holder              = (ViewHolder) row.getTag();
        }


        holder.imageTitle.setText(item.getTitle());
        holder.image.setImageBitmap(item.getImage());
        Log.d("ADAPTER", position + " selected = " + item.getSelected());
        if(item.getSelected()) {
            row.setBackgroundColor(row.getResources().getColor(R.color.row_selected));
            row.getBackground().setAlpha(128);
        }
        return row;
    }

    static class ViewHolder {
        TextView imageTitle;
        ImageView image;
    }
}
