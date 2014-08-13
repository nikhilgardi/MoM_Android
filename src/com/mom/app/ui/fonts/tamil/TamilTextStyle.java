package com.mom.app.ui.fonts.tamil;

import com.mom.app.ui.ITextStyle;

/**
 * Created by vaibhavsinha on 8/13/14.
 */
public enum TamilTextStyle implements ITextStyle {
    NORMAL("regular", "custom_fonts/Lohit-Tamil.ttf");

    private String mName;
    private String mFontName;

    TamilTextStyle(String name, String fontName){
        mName       = name;
        mFontName   = fontName;
    }

    @Override
    public String getFontName() {
        return mFontName;
    }

    @Override
    public String getName() {
        return mName;
    }

}