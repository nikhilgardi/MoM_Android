package com.mom.apps.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

import com.mom.apps.ui.fonts.TypeFaceManager;

/**
 * Created by vaibhavsinha on 8/13/14.
 */
public class CustomEditText extends EditText {
    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        Typeface typeface   = TypeFaceManager.getInstance().getTypefaceForCurrentLocale(context);
        if(typeface != null){
            Log.d("EditText", "Setting custom typeface " + typeface);
            setTypeface(typeface);
        }
    }
}
