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
import com.mom.app.external.ImageLoader;
import com.mom.app.utils.AppConstants;
import com.mom.app.widget.holder.ImageItem;

import java.util.ArrayList;


/**
 * Created by vaibhavsinha on 7/14/14.
 */
public class ImageTextViewAdapter<T> extends ArrayAdapter{
    static String _LOG = AppConstants.LOG_PREFIX + "ITV_ADAPTER";

    private Context context;
    private int layoutResourceId;
    private ArrayList<ImageItem<T>> data;
    private boolean _isGridView     = true;

    public ImageTextViewAdapter(Context context, int layoutResourceId, ArrayList<ImageItem<T>> data) {

        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    public ImageTextViewAdapter(Context context, int layoutResourceId, ArrayList<ImageItem<T>> data, boolean isGridView) {

        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context    = context;
        this.data       = data;
        _isGridView     = isGridView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row                = convertView;
        ViewHolder holder       = null;
        final ImageItem item          = data.get(position);

        Log.d(_LOG, "Getting drawer view");

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
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

        if(item.getImageUrl() != null){
            ImageLoader imageLoader = new ImageLoader(getContext());
            imageLoader.displayImage(item.getImageUrl(), loader, holder.image);
        }else{
            holder.image.setImageResource(item.getDrawableId());
        }

        Log.d(_LOG, position + " selected = " + item.getSelected());

//        if(item.getSelected()) {
//            if(_isGridView){
//                if(holder != null && holder.selectedImage != null) {
//                    holder.selectedImage.setVisibility(View.VISIBLE);
//                }
//            }else {
//                row.setBackgroundColor(row.getResources().getColor(R.color.row_selected));
//                row.getBackground().setAlpha(128);
//            }
//        }

        return row;
    }

    static class ViewHolder {
        TextView imageTitle;
        ImageView image;
        ImageView selectedImage;
    }
}
