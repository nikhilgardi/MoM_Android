package com.mom.app.ui.fonts.oriya;

import android.util.Log;

import com.mom.app.ui.ITextStyle;
import com.mom.app.ui.fonts.TextStyleExtractor;
import com.mom.app.utils.AppConstants;

/**
 * Created by vaibhavsinha on 8/13/14.
 */
public class OriyaTextStyleExtractor extends TextStyleExtractor {
    String _LOG = AppConstants.LOG_PREFIX + "ENGLISH_EX";

    private static final OriyaTextStyleExtractor INSTANCE = new OriyaTextStyleExtractor();

    public static TextStyleExtractor getInstance() {
        return INSTANCE;
    }

    @Override
    public ITextStyle[] getTextStyles() {
        Log.d(_LOG, "Extracting");
        ITextStyle[] styles  = OriyaTextStyle.values();
        Log.d(_LOG, "Values: " + (styles == null ? "null" : styles.length));
        return styles;
    }
}
