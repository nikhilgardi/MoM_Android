package com.mom.app.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.mom.app.R;
import com.mom.app.widget.holder.ImageItem;

import java.util.ArrayList;

/**
 * Created by vaibhavsinha on 7/21/14.
 */
public class DataProvider {
    public static ArrayList getDashboard(Activity activity) {
        final ArrayList imageItems = new ArrayList();
        // retrieve String drawable array
//        TypedArray imgs = activity.getResources().obtainTypedArray(R.array.mood_images);
        Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.phone);
        imageItems.add(new ImageItem(bitmap, "Mobile Recharge", false));

        bitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.dth);
        imageItems.add(new ImageItem(bitmap, "DTH Recharge", false));

        bitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.bill);
        imageItems.add(new ImageItem(bitmap, "Bill Payment", false));

        bitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.history);
        imageItems.add(new ImageItem(bitmap, "History", false));

        bitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.settings);
        imageItems.add(new ImageItem(bitmap, "Settings", false));

        bitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.logout);
        imageItems.add(new ImageItem(bitmap, "Logout", false));

        return imageItems;
    }

    public static ArrayList getOperators(Activity activity) {
        final ArrayList imageItems = new ArrayList();
        // retrieve String drawable array
//        TypedArray imgs = activity.getResources().obtainTypedArray(R.array.mood_images);
        Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.airtel);
        imageItems.add(new ImageItem(bitmap, "Aircel", false));

        bitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.airtel);
        imageItems.add(new ImageItem(bitmap, "Airtel", false));

        bitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.airtel);
        imageItems.add(new ImageItem(bitmap, "BSNL", false));

        bitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.airtel);
        imageItems.add(new ImageItem(bitmap, "Datacomm", false));

        bitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.airtel);
        imageItems.add(new ImageItem(bitmap, "IDEA", false));

        bitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.airtel);
        imageItems.add(new ImageItem(bitmap, "LOOP", false));


        return imageItems;
    }
}
