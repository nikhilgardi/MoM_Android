package com.mom.app.ui.fonts.gujarati;

import com.mom.app.ui.ITextStyle;

/**
 * Created by vaibhavsinha on 8/12/14.
 */
public enum GujaratiTextStyle implements ITextStyle {
    NORMAL("gujrati_regular", "custom_fonts/Lohit-Gujarati.ttf");

    private String mName;
    private String mFontName;

    GujaratiTextStyle(String name, String fontName){
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
