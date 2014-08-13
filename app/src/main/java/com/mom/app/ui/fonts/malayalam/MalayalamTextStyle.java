package com.mom.app.ui.fonts.malayalam;

import com.mom.app.ui.ITextStyle;

/**
 * Created by vaibhavsinha on 8/13/14.
 */
public enum MalayalamTextStyle implements ITextStyle {
    NORMAL("malayalam_regular", "custom_fonts/Lohit-Malayalam.ttf");

    private String mName;
    private String mFontName;

    private MalayalamTextStyle(String name, String fontName){
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