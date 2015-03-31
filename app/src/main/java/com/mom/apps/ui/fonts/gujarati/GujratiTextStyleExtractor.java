package com.mom.apps.ui.fonts.gujarati;

import android.util.Log;

import com.mom.apps.ui.ITextStyle;
import com.mom.apps.ui.fonts.TextStyleExtractor;
import com.mom.apps.utils.AppConstants;

/**
 * Created by vaibhavsinha on 8/12/14.
 */
public class GujratiTextStyleExtractor extends TextStyleExtractor {
    String _LOG = AppConstants.LOG_PREFIX + "FONT_EX";

    private static final GujratiTextStyleExtractor INSTANCE = new GujratiTextStyleExtractor();

    public static TextStyleExtractor getInstance() {
        return INSTANCE;
    }

    @Override
    public ITextStyle[] getTextStyles() {
        Log.d(_LOG, "Extracting");
        ITextStyle[] styles  = GujaratiTextStyle.values();
        Log.d(_LOG, "Values: " + (styles == null ? "null" : styles.length));
        return styles;
    }
}
