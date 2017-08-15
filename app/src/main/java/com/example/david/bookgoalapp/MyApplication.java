package com.example.david.bookgoalapp;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import java.util.Locale;

/**
 * Created by David on 15-Aug-17.
 */

public class MyApplication extends Application
{
    /**
     * App language. iw = hebrew, en = english.
     * Also change with app:menu_labels_position="right" in floating menu in activity_main.xml file
     */
    public static final String mlang = "iw";

    @Override
    public void onCreate()
    {
        //TODO:: why caldroid doesn't chnage it's language??
        updateLanguage(this);
        super.onCreate();
    }

    public static void updateLanguage(Context ctx)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        String lang = prefs.getString("locale_override", mlang);
        updateLanguage(ctx, lang);
    }

    public static void updateLanguage(Context ctx, String lang)
    {
        Configuration cfg = new Configuration();
        if (!TextUtils.isEmpty(lang))
            cfg.locale = new Locale(lang);
        else
            cfg.locale = Locale.getDefault();

        ctx.getResources().updateConfiguration(cfg, null);
    }
}
