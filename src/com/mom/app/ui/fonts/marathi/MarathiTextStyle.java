package com.mom.app.ui.fonts.marathi;

import com.mom.app.ui.ITextStyle;

/**
 * Created by vaibhavsinha on 8/13/14.
 */
public enum MarathiTextStyle implements ITextStyle {
    NORMAL("regular", "custom_fonts/Lohit-Marathi.ttf");

    private String mName;
    private String mFontName;

    private MarathiTextStyle(String name, String fontName){
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