package com.mom.app.widget.holder;

import android.graphics.Bitmap;

/**
 * Created by vaibhavsinha on 7/14/14.
 */
public class ImageItem {
    private Integer id;
    private Integer drawableId;
    private String imageUrl;

    private String title;
    private Boolean selected;

    public ImageItem(int id, int imageId, String title, boolean pbSelected) {
        super();
        this.id         = id;
        this.drawableId = imageId;
        this.title      = title;
        this.selected   = pbSelected;
    }

    public ImageItem(int id, String imageUrl, String title, boolean pbSelected){
        super();
        this.id         = id;
        this.imageUrl   = imageUrl;
        this.title      = title;
        this.selected   = pbSelected;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(Integer drawableId) {
        this.drawableId = drawableId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public void setSelected(Boolean pbSelected){
        this.selected   = pbSelected;
    }
}
