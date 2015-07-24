package com.mom.app;

        import android.app.Application;
        import android.content.Context;

        import com.crashlytics.android.Crashlytics;
        import com.mom.app.model.local.PersistentStorage;
        import com.mom.app.ui.LanguageItem;
        import com.mom.app.ui.fonts.TypeFaceManager;
        import com.mom.app.utils.AppConstants;
        import io.fabric.sdk.android.Fabric;

/**
 * Created by vaibhavsinha on 8/12/14.
 */
public class MoMApp extends Application {

    private static Context sContext;
    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        Fabric.with(this, new Crashlytics());
        int nStoreLanguage  = PersistentStorage.getInstance(this).getInt(AppConstants.USER_LANGUAGE, -1);
        if(nStoreLanguage != -1) {
            TypeFaceManager.addTextStyleExtractor(LanguageItem.getTextStyleExtractor(nStoreLanguage));
        }
    }

    public static Context getContext() {
        return sContext;
    }
}
