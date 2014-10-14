package com.mom.app.widget.holder;

import android.graphics.Bitmap;

/**
 * Created by vaibhavsinha on 7/14/14.
 */
public class ImageItem<T> {
    private T item;

    private Integer id;
    private Integer drawableId;
    private Integer transparentDrawableId;
    private String imageUrl;

    private String title;
    private Boolean selected;

    public ImageItem(T item, int id, int imageId, int transparentDrawableId, String title, boolean pbSelected) {
        super();
        this.item       = item;
        this.id         = id;
        this.drawableId = imageId;
        this.transparentDrawableId = transparentDrawableId;
        this.title      = title;
        this.selected   = pbSelected;
    }


    public T getItem() {
        return item;
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

    public Integer getTransparentDrawableId() {
        return transparentDrawableId;
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
