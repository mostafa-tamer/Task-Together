package com.mostafatamer.tasktogether.lib;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author Mostafa Tamer
 */
public class SharedPreferencesHelper {
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor sharedPreferencesEditor;

    public SharedPreferencesHelper(Context context, String sharedName) {
        this.sharedPreferences = context.getSharedPreferences(sharedName, Context.MODE_PRIVATE);
        this.sharedPreferencesEditor = sharedPreferences.edit();
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, null);
    }

    public boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public void setValue(String key, String value) {
        sharedPreferencesEditor.putString(key, value);
        sharedPreferencesEditor.apply();
    }

    public void setValue(String key, boolean value) {
        sharedPreferencesEditor.putBoolean(key, value);
        sharedPreferencesEditor.apply();
    }
}
