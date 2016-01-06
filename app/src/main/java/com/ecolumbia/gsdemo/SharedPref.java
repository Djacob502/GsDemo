package com.ecolumbia.gsdemo;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Toast;

/**
 * Created by DaveJacob on 12/29/2015.
 */
public class SharedPref {
    private static final String PREFERENCE_FILE_NAME = "com.ecolumbia.gsdemo.preferences_file_key";

    public static void saveToPreferences(Context context, String preferenceName, Boolean value) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.putBoolean(preferenceName, value);
        editor.commit();
    }

    public static Boolean readFromPreferences(Context context, String preferenceName, Boolean defaultValue) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        boolean value = sharedPref.getBoolean(preferenceName, defaultValue);
        return value;
    }
}
