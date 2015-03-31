package com.mom.apps.ui.fonts;

import android.util.Log;

import com.mom.apps.ui.ITextStyle;
import com.mom.apps.utils.AppConstants;

/**
 * Created by vaibhavsinha on 8/12/14.
 */
public abstract class TextStyleExtractor {
    String _LOG         = AppConstants.LOG_PREFIX + "TEXT_STYLE";

    public abstract ITextStyle[] getTextStyles();

    public ITextStyle getTextStyle(String textStyleName) {
        ITextStyle[] styles  = getTextStyles();
        Log.d(_LOG, "Styles: " + (styles == null ? "Null" : styles.length));
        for (ITextStyle textStyle : getTextStyles()) {
            Log.d(_LOG, "Style: " + textStyle);
            if (textStyle.getName().equals(textStyleName)) {
                return textStyle;
            }
        }
        return null;
    }
}