package com.mom.app.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.mom.app.ui.fonts.TypeFaceManager;

/**
 * Created by vaibhavsinha on 8/12/14.
 */
public class CustomTextView extends TextView {
    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
//        TypeFaceManager.getInstance().applyTypeface(this, context, attrs);
        Typeface typeface   = TypeFaceManager.getInstance().getTypefaceForCurrentLocale(context);
        if(typeface != null){
            setTypeface(typeface);
        }
    }

    /**
     * Convenience method in case I need to change the font from code as well.
     * @param textStyle
     */
    public void setTextStyle(ITextStyle textStyle) {
        TypeFaceManager.getInstance().applyTypeface(this, textStyle);
    }
}
