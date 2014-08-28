package com.mom.app.ui.fonts.telugu;

import com.mom.app.ui.ITextStyle;

/**
 * Created by vaibhavsinha on 8/13/14.
 */
public enum TeluguTextStyle implements ITextStyle {
    NORMAL("regular", "custom_fonts/Lohit-Telugu.ttf");

    private String mName;
    private String mFontName;

    private TeluguTextStyle(String name, String fontName){
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