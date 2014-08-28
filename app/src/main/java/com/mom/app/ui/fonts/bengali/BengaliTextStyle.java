package com.mom.app.ui.fonts.bengali;

import com.mom.app.ui.ITextStyle;

/**
 * Created by vaibhavsinha on 8/13/14.
 */
public enum BengaliTextStyle implements ITextStyle {
    NORMAL("regular", "custom_fonts/Roboto-Regular.ttf");

    private String mName;
    private String mFontName;

    private BengaliTextStyle(String name, String fontName){
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
