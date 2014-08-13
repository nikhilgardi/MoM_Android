package com.mom.app.ui.fonts.tamil;

import android.util.Log;

import com.mom.app.ui.ITextStyle;
import com.mom.app.ui.fonts.TextStyleExtractor;
import com.mom.app.utils.AppConstants;

/**
 * Created by vaibhavsinha on 8/13/14.
 */
public class TamilTextStyleExtractor extends TextStyleExtractor {
    String _LOG = AppConstants.LOG_PREFIX + "FONT_EX";

    private static final TamilTextStyleExtractor INSTANCE = new TamilTextStyleExtractor();

    public static TextStyleExtractor getInstance() {
        return INSTANCE;
    }

    @Override
    public ITextStyle[] getTextStyles() {
        Log.d(_LOG, "Extracting");
        ITextStyle[] styles  = TamilTextStyle.values();
        Log.d(_LOG, "Values: " + (styles == null ? "null" : styles.length));
        return styles;
    }
}
