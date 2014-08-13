package com.mom.app.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import com.mom.app.ui.fonts.TypeFaceManager;

/**
 * Created by vaibhavsinha on 8/13/14.
 */
public class CustomButton extends Button {
    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface typeface   = TypeFaceManager.getInstance().getTypefaceForCurrentLocale(context);
        if(typeface != null){
            setTypeface(typeface);
        }
    }
}
