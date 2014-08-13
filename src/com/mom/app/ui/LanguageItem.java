package com.mom.app.ui;

import android.content.Context;

import com.mom.app.R;
import com.mom.app.ui.fonts.TextStyleExtractor;
import com.mom.app.ui.fonts.bengali.BengaliTextStyleExtractor;
import com.mom.app.ui.fonts.english.EnglishTextStyleExtractor;
import com.mom.app.ui.fonts.gujarati.GujratiTextStyleExtractor;
import com.mom.app.ui.fonts.hindi.HindiTextStyleExtractor;
import com.mom.app.ui.fonts.malayalam.MalayalamTextStyleExtractor;
import com.mom.app.ui.fonts.oriya.OriyaTextStyleExtractor;
import com.mom.app.ui.fonts.tamil.TamilTextStyleExtractor;
import com.mom.app.ui.fonts.telugu.TeluguTextStyleExtractor;

import java.io.Serializable;
import java.util.ArrayList;

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
        list.add(getLanguage(context, R.string.language_bengali));
        list.add(getLanguage(context, R.string.language_english));
        list.add(getLanguage(context, R.string.language_gujarati));
        list.add(getLanguage(context, R.string.language_hindi));
        list.add(getLanguage(context, R.string.language_malayalam));
        list.add(getLanguage(context, R.string.language_oriya));
        list.add(getLanguage(context, R.string.language_tamil));
        list.add(getLanguage(context, R.string.language_telugu));


        return list.toArray(new LanguageItem[0]);
    }

    public static LanguageItem getLanguage(Context context, int pnResourceId){
        switch (pnResourceId){
            case R.string.language_bengali:
                return new LanguageItem(
                        context.getResources().getString(R.string.language_bengali_code),
                        R.string.language_bengali,
                        context.getResources().getString(R.string.language_bengali)
                );
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
            case R.string.language_hindi:
                return new LanguageItem(
                        context.getResources().getString(R.string.language_hindi_code),
                        R.string.language_hindi,
                        context.getResources().getString(R.string.language_hindi)
                );
            case R.string.language_malayalam:
                return new LanguageItem(
                        context.getResources().getString(R.string.language_malayalam_code),
                        R.string.language_malayalam,
                        context.getResources().getString(R.string.language_malayalam)
                );
            case R.string.language_oriya:
                return new LanguageItem(
                        context.getResources().getString(R.string.language_oriya_code),
                        R.string.language_oriya,
                        context.getResources().getString(R.string.language_oriya)
                );
            case R.string.language_tamil:
                return new LanguageItem(
                        context.getResources().getString(R.string.language_tamil_code),
                        R.string.language_tamil,
                        context.getResources().getString(R.string.language_tamil)
                );
            case R.string.language_telugu:
                return new LanguageItem(
                        context.getResources().getString(R.string.language_telugu_code),
                        R.string.language_telugu,
                        context.getResources().getString(R.string.language_telugu)
                );
        }

        return null;
    }

    public static TextStyleExtractor getTextStyleExtractor(int pnResourceId){
        switch (pnResourceId){
            case R.string.language_bengali:
                return BengaliTextStyleExtractor.getInstance();
            case R.string.language_english:
                return EnglishTextStyleExtractor.getInstance();
            case R.string.language_gujarati:
                return GujratiTextStyleExtractor.getInstance();
            case R.string.language_hindi:
                return HindiTextStyleExtractor.getInstance();
            case R.string.language_malayalam:
                return MalayalamTextStyleExtractor.getInstance();
            case R.string.language_oriya:
                return OriyaTextStyleExtractor.getInstance();
            case R.string.language_tamil:
                return TamilTextStyleExtractor.getInstance();
            case R.string.language_telugu:
                return TeluguTextStyleExtractor.getInstance();
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
