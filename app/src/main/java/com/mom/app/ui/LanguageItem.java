package com.mom.app.ui;

import android.content.Context;

import java.io.Serializable;
import java.util.ArrayList;
import com.mom.app.R;
import com.mom.app.ui.fonts.EnglishTextStyleExtractor;
import com.mom.app.ui.fonts.GujratiTextStyleExtractor;
import com.mom.app.ui.fonts.TextStyleExtractor;

/**
 * Created by vaibhavsinha on 8/13/14.
 */
public class LanguageItem implements Serializable{
    public String code;
    public String name;
    public int resourceId;


    public LanguageItem(int resourceId){
        this.resourceId = resourceId;
    }

    public LanguageItem(String code, int resourceId, String name){
        this.code       = code;
        this.name       = name;
        this.resourceId = resourceId;
    }

    public String toString(){
        return name;
    }

    public static LanguageItem getDefault(Context context){
        return new LanguageItem(
                context.getResources().getString(R.string.language_english_code),
                R.string.language_english,
                context.getResources().getString(R.string.language_english)
        );
    }

    public static LanguageItem[] getLanguages(Context context){
        ArrayList<LanguageItem> list    = new ArrayList<LanguageItem>();
        list.add(getLanguage(context, R.string.language_english));
        list.add(getLanguage(context, R.string.language_gujarati));

        return list.toArray(new LanguageItem[0]);
    }

    public static LanguageItem getLanguage(Context context, int pnResourceId){
        switch (pnResourceId){
            case R.string.language_english:
                return new LanguageItem(
                        context.getResources().getString(R.string.language_english_code),
                        R.string.language_english,
                        context.getResources().getString(R.string.language_english)
                );
            case R.string.language_gujarati:
                return new LanguageItem(
                        context.getResources().getString(R.string.language_gujarati_code),
                        R.string.language_gujarati,
                        context.getResources().getString(R.string.language_gujarati)
                );
        }

        return null;
    }

    public static TextStyleExtractor getTextStyleExtractor(int pnResourceId){
        switch (pnResourceId){
            case R.string.language_english:
                return EnglishTextStyleExtractor.getInstance();
            case R.string.language_gujarati:
                return GujratiTextStyleExtractor.getInstance();
        }

        return null;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof LanguageItem){
            return (((LanguageItem)o).resourceId    == this.resourceId);
        }
        return false;
    }
}
