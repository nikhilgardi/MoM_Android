package com.mom.app.ui.fonts.oriya;
import com.mom.app.ui.ITextStyle;

/**
 * Created by vaibhavsinha on 8/13/14.
 */
public enum OriyaTextStyle implements ITextStyle {
    NORMAL("oriya_regular", "custom_fonts/Lohit-Oriya.ttf");

    private String mName;
    private String mFontName;

    private OriyaTextStyle(String name, String fontName){
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