package com.mom.apps.ui.fonts.hindi;
import com.mom.apps.ui.ITextStyle;

/**
 * Created by vaibhavsinha on 8/13/14.
 */
public enum HindiTextStyle implements ITextStyle {
    NORMAL("regular", "custom_fonts/Lohit-Devanagari.ttf");

    private String mName;
    private String mFontName;

    private HindiTextStyle(String name, String fontName){
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