package com.mom.app.ui.fonts;

import android.util.Log;

import com.mom.app.ui.ITextStyle;
import com.mom.app.utils.AppConstants;

/**
 * Created by vaibhavsinha on 8/13/14.
 */
public class EnglishTextStyleExtractor extends TextStyleExtractor{
    String _LOG = AppConstants.LOG_PREFIX + "ENGLISH_EX";

    private static final EnglishTextStyleExtractor INSTANCE = new EnglishTextStyleExtractor();

    public static TextStyleExtractor getInstance() {
        return INSTANCE;
    }

    @Override
    public ITextStyle[] getTextStyles() {
        Log.d(_LOG, "Extracting");
        ITextStyle[] styles  = EnglishTextStyle.values();
        Log.d(_LOG, "Values: " + (styles == null ? "null" : styles.length));
        return styles;
    }
}
