package com.mom.app.adapter;

/**
 * Created by vaibhavsinha on 10/14/14.
 */

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mom.app.R;
import com.mom.app.utils.AppConstants;
import com.mom.app.widget.holder.ImageItem;

import java.util.ArrayList;



/**
 * Created by vaibhavsinha on 7/14/14.
 */
public class DrawerAdapter<T> extends ArrayAdapter {
    static String _LOG = AppConstants.LOG_PREFIX + "DRWR_ADAPTER";

    private Context context;
    private int layoutResourceId;
    private ArrayList<ImageItem<T>> data;

    public DrawerAdapter(Context context, int layoutResourceId, ArrayList<ImageItem<T>> data) {

        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    public DrawerAdapter(Context context, int layoutResourceId, ArrayList<ImageItem<T>> data, boolean isGridView) {

        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context    = context;
        this.data       = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row                = convertView;
        ViewHolder holder       = null;
        final ImageItem item          = data.get(position);

//        Log.d(_LOG, "Getting drawer view");

        if (row == null) {
            LayoutInflater inflater = ((ActionBarActivity) context).getLayoutInflater();
            row                 = inflater.inflate(layoutResourceId, parent, false);
            holder              = new ViewHolder();
            holder.imageTitle   = (TextView) row.findViewById(R.id.text);
            ImageView imgView   = (ImageView) row.findViewById(R.id.image);
            holder.image        = imgView;
            holder.selectedImage    = (ImageView) row.findViewById(R.id.imgSelected);
            row.setTag(holder);

        } else {
            holder              = (ViewHolder) row.getTag();
        }

        int loader              = R.drawable.failure;
        holder.imageTitle.setText(item.getTitle());

        holder.image.setImageResource(item.getTransparentDrawableId());

//        Log.d(_LOG, position + " selected = " + item.getSelected());

        return row;
    }


    static class ViewHolder {

        TextView imageTitle;
        ImageView image;
        ImageView selectedImage;
    }
}
