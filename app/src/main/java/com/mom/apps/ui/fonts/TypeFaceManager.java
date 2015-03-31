package com.mom.apps.ui.fonts;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.mom.apps.model.local.PersistentStorage;
import com.mom.apps.ui.ITextStyle;
import com.mom.apps.ui.LanguageItem;
import com.mom.apps.utils.AppConstants;

import java.util.HashMap;
import java.util.HashSet;


/**
 * Utility class used to apply a custom {@link Typeface} to a {@link TextView} subclass.
 */
public class TypeFaceManager {
    static String _LOG = AppConstants.LOG_PREFIX + "TYPE_FACE_MGR";
    private static Typeface _currentTypeface        = null;
    private static final TypeFaceManager INSTANCE   = new TypeFaceManager();

    public static TypeFaceManager getInstance() {
        return INSTANCE;
    }

    public static void addTextStyleExtractor(TextStyleExtractor textStyleExtractor) {
        if(textStyleExtractor == null){
            Log.e(_LOG, "Null TextStyleExtractor sent");
            return;
        }

        INSTANCE.mTextStyleExtractors.add(textStyleExtractor);
    }

    private final HashMap<ITextStyle, Typeface> mTypefaces = new HashMap<ITextStyle, Typeface>();
    private final HashSet<TextStyleExtractor> mTextStyleExtractors = new HashSet<TextStyleExtractor>();

    private TypeFaceManager() {
        // Singleton
    }

    /**
     * Method called from the {@TypefaceTextView} constructor to
     * apply a custom {@link com.mom.apps.ui.ITextStyle} defined in the application theme.
     *
     * @param textView the {@link TextView} to have the {@link com.mom.apps.ui.ITextStyle} applied
     * @param context  the {@link Context} of the {@link TextView}
     * @param attrs    the {@link AttributeSet} of the {@link TextView}
     */
    public void applyTypeface(TextView textView, Context context, AttributeSet attrs) {
        final TypedArray styleValues = context.obtainStyledAttributes(attrs, new int[]{android.R.attr.fontFamily});
        final String fontFamily = styleValues.getString(0);

        if (!TextUtils.isEmpty(fontFamily)) {
            for (TextStyleExtractor extractor : mTextStyleExtractors) {
                final ITextStyle textStyle = extractor.getTextStyle(fontFamily);
                if (textStyle != null) {
                    applyTypeface(textView, textStyle);
                    break;
                }
            }
        }
        styleValues.recycle();
    }

    /**
     * Method called from code to apply a custom {@link com.mom.apps.ui.ITextStyle}.
     *
     * @param textView  the {@link TextView} to have the {@link com.mom.apps.ui.ITextStyle} applied
     * @param textStyle the {@link com.mom.apps.ui.ITextStyle} to be applied
     */
    public void applyTypeface(TextView textView, ITextStyle textStyle) {
        final Typeface typeface = getTypeface(textView.getContext(), textStyle);
        if (typeface != null) {
            textView.setTypeface(typeface);
        }
    }

    /**
     * Apply a {@link Typeface} for a given {@link com.mom.apps.ui.ITextStyle}.
     *
     * @param context   the {@link Context} of the {@link TextView}
     * @param textStyle the {@link com.mom.apps.ui.ITextStyle} to be applied
     * @return the {@link Typeface} corresponding to the {@link com.mom.apps.ui.ITextStyle}, if defined
     */
    private Typeface getTypeface(Context context, ITextStyle textStyle) {
        if (textStyle == null) {
            throw new IllegalArgumentException("Param 'textStyle' can't be null.");
        }
        if (mTypefaces.containsKey(textStyle)) {
            return mTypefaces.get(textStyle);
        }

        final Typeface typeface = Typeface.createFromAsset(context.getAssets(), textStyle.getFontName());
        if (typeface == null) {
            throw new RuntimeException("Can't create Typeface for font '" + textStyle.getFontName() + "'");
        }

        mTypefaces.put(textStyle, typeface);
        return typeface;
    }

    public Typeface getTypefaceForCurrentLocale(Context context){
        if(_currentTypeface != null){
//            Log.d(_LOG, "Found typeface, returning: " + _currentTypeface);
            return _currentTypeface;
        }

        int nLanguageId         = PersistentStorage.getInstance(context).getInt(AppConstants.USER_LANGUAGE, -1);
        TextStyleExtractor extractor    = LanguageItem.getTextStyleExtractor(nLanguageId);
        if(extractor == null){
            Log.d(_LOG, "No extractor found, returning null");
            return null;
        }
        ITextStyle textStyle    = extractor.getTextStyle(AppConstants.FONT_DEFAULT_TEXT_STYLE);
        if(textStyle == null){
            Log.d(_LOG, "No textStyle found, returning null");
            return null;
        }

        Log.d(_LOG, "Returning typeface for " + textStyle.getName());
        _currentTypeface    = getTypeface(context, textStyle);
        return _currentTypeface;
    }
}