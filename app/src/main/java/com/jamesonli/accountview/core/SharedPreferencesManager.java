package com.jamesonli.accountview.core;

import android.content.Context;
import android.content.SharedPreferences;
import com.jamesonli.accountview.R;

/**
 * Created by james on 11/21/15.
 */
public class SharedPreferencesManager {

    /** keys **/
    public static final String USER_ID = "userid";
    /** end keys **/

    private static SharedPreferencesManager mPrefManager;

    private final SharedPreferences mSharedPreferences;

    public static SharedPreferencesManager getInstance(Context context) {
        if(mPrefManager == null) {
            mPrefManager = new SharedPreferencesManager(context);
        }

        return mPrefManager;
    }

    private SharedPreferencesManager(Context context) {
        mSharedPreferences = context.getSharedPreferences(context.getString(R.string.shared_pref_file), Context.MODE_PRIVATE);
    }

    public String getString(String key, String defaultValue) {
        return mSharedPreferences.getString(key, defaultValue);
    }

    public void setString(String key, String value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public boolean valueExists(String key) {
        return mSharedPreferences.contains(key);
    }
}
