package com.mom.app.widget.holder;

import android.graphics.Bitmap;

/**
 * Created by vaibhavsinha on 7/14/14.
 */
public class ImageItem {
    private Bitmap image;
    private String title;
    private boolean selected;

    public ImageItem(Bitmap image, String title, boolean pbSelected) {
        super();
        this.image      = image;
        this.title      = title;
        this.selected   = pbSelected;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean getSelected(){
        return selected;
    }

    public void setSelected(boolean pbSelected){
        this.selected   = pbSelected;
    }
}
