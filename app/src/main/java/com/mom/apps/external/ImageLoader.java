package com.mom.apps.external;

/**
 * Created by vaibhavsinha on 8/11/14.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;


import com.mom.apps.R;
import com.mom.apps.cache.FileCache;
import com.mom.apps.cache.MemoryCache;
import com.mom.apps.utils.MiscUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageLoader {

    MemoryCache mMemoryCache                    = new MemoryCache();
    FileCache mFileCache;
    private Map<ImageView, String> mImageViews  = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());

    ExecutorService mExecutorService;

    int mStubId                                 = R.drawable.ic_launcher;

    public ImageLoader(Context context){
        mFileCache          = new FileCache(context);
        mExecutorService    = Executors.newFixedThreadPool(5);
    }

    public void displayImage(String url, int loader, ImageView imageView)
    {
        mStubId         = loader;
        mImageViews.put(imageView, url);
        Bitmap bitmap   = mMemoryCache.get(url);
        if(bitmap!=null)
            imageView.setImageBitmap(bitmap);
        else
        {
            queuePhoto(url, imageView);
            imageView.setImageResource(loader);
        }
    }

    private void queuePhoto(String url, ImageView imageView)
    {
        PhotoToLoad p=new PhotoToLoad(url, imageView);
        mExecutorService.submit(new PhotosLoader(p));
    }

    private Bitmap getBitmap(String url)
    {
        File f= mFileCache.getFile(url);

        //from SD cache
        Bitmap b = decodeFile(f);
        if(b!=null)
            return b;

        //from web
        try {
            Bitmap bitmap=null;
            URL imageUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)imageUrl.openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(true);
            InputStream is=conn.getInputStream();
            OutputStream os = new FileOutputStream(f);
            MiscUtils.copyStream(is, os);
            os.close();
            bitmap = decodeFile(f);
            return bitmap;
        } catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    //decodes image and scales it to reduce memory consumption
    private Bitmap decodeFile(File f){
        try {
            //decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);

            //Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE=70;
            int width_tmp=o.outWidth, height_tmp=o.outHeight;
            int scale=1;
            while(true){
                if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
                    break;
                width_tmp/=2;
                height_tmp/=2;
                scale*=2;
            }

            //decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {}
        return null;
    }

    //Task for the queue
    private class PhotoToLoad
    {
        public String url;
        public ImageView imageView;
        public PhotoToLoad(String u, ImageView i){
            url=u;
            imageView=i;
        }
    }

    class PhotosLoader implements Runnable {
        PhotoToLoad photoToLoad;
        PhotosLoader(PhotoToLoad photoToLoad){
            this.photoToLoad=photoToLoad;
        }

        @Override
        public void run() {
            if(imageViewReused(photoToLoad))
                return;
            Bitmap bmp=getBitmap(photoToLoad.url);
            mMemoryCache.put(photoToLoad.url, bmp);
            if(imageViewReused(photoToLoad))
                return;
            BitmapDisplayer bd=new BitmapDisplayer(bmp, photoToLoad);
            Activity a=(Activity)photoToLoad.imageView.getContext();
            a.runOnUiThread(bd);
        }
    }

    boolean imageViewReused(PhotoToLoad photoToLoad){
        String tag= mImageViews.get(photoToLoad.imageView);
        if(tag==null || !tag.equals(photoToLoad.url))
            return true;
        return false;
    }

    //Used to display bitmap in the UI thread
    class BitmapDisplayer implements Runnable
    {
        Bitmap bitmap;
        PhotoToLoad photoToLoad;
        public BitmapDisplayer(Bitmap b, PhotoToLoad p){bitmap=b;photoToLoad=p;}
        public void run()
        {
            if(imageViewReused(photoToLoad))
                return;
            if(bitmap!=null)
                photoToLoad.imageView.setImageBitmap(bitmap);
            else
                photoToLoad.imageView.setImageResource(mStubId);
        }
    }

    public void clearCache() {
        mMemoryCache.clear();
        mFileCache.clear();
    }

}