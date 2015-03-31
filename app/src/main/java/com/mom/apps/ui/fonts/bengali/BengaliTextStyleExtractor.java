package com.mom.apps.ui.fonts.bengali;


import android.util.Log;

import com.mom.apps.ui.ITextStyle;
import com.mom.apps.ui.fonts.TextStyleExtractor;
import com.mom.apps.utils.AppConstants;

/**
 * Created by vaibhavsinha on 8/13/14.
 */
public class BengaliTextStyleExtractor extends TextStyleExtractor {
    String _LOG = AppConstants.LOG_PREFIX + "FONT_EX";

    private static final BengaliTextStyleExtractor INSTANCE = new BengaliTextStyleExtractor();

    public static TextStyleExtractor getInstance() {
        return INSTANCE;
    }

    @Override
    public ITextStyle[] getTextStyles() {
        Log.d(_LOG, "Extracting");
        ITextStyle[] styles  = BengaliTextStyle.values();
        Log.d(_LOG, "Values: " + (styles == null ? "null" : styles.length));
        return styles;
    }
}
