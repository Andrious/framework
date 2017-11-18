package com.andrioussolutions.frmwrk.settings;

import com.andrioussolutions.frmwrk.App;
import com.andrioussolutions.frmwrk.appController;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.Preference;
/**
 *  Copyright  2015  Andrious Solutions Ltd.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *
 *
 * Created  2015-03-13.
 */
public class appSettings implements SharedPreferences.OnSharedPreferenceChangeListener {



    private appSettings(appController controller) {

        mController = controller;

        mSettings = get(controller);

        // Run code the moment a preference is changed.
        mSettings.registerOnSharedPreferenceChangeListener(this);
    }



    public static void init(appController controller){

        if(mSettings != null){

            if(!(mController == controller)){

//                Log.e(PACKAGE_NAME, "Settings already initialized.");
            }
        }else{

            new appSettings(controller);
        }
    }



    // Assign a predefined listener
    private appSettings(appController controller, SharedPreferences.OnSharedPreferenceChangeListener listener) {

        this(controller);

        mPrefsListener = listener;
    }



    public static void init(appController controller, SharedPreferences.OnSharedPreferenceChangeListener listener){

        if(mSettings != null){

            if(!(mController == controller)){

//                Log.e(PACKAGE_NAME, "Settings already initialized.");
            }
        }else{

            new appSettings(controller, listener);
        }
    }



    // Here is the preference listener.
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

// Example to dynamically change the preference text summary.
//        if (key.equals(KEY_PREF_SYNC_CONN)) {
//            Preference connectionPref = findPreference(key);
//            // Set summary to be the user-description for the selected value
//            connectionPref.setSummary(sharedPreferences.getString(key, ""));
//        }

        if (mPrefsListener != null)  mPrefsListener.onSharedPreferenceChanged(sharedPreferences, key);

        mController.onSharedPreferenceChanged(sharedPreferences, key);
    }


    public static SharedPreferences get(Context context) {

        SharedPreferences settings = context
                .getSharedPreferences(PACKAGE_NAME + "_preferences", Context.MODE_PRIVATE);

        if (mResources == null) {

            mResources = context.getResources();
        }
        return settings;
    }


    static SharedPreferences.Editor getEditor() {

        return mSettings.edit();
    }


    static Preference findPreference(CharSequence key) {

        if (appPreferences.mPreferenceManager == null) {
            return null;
        }

        return appPreferences.mPreferenceManager.findPreference(key);
    }


    public static String get(String key, String defaultValue) {

        try {

            return mSettings.getString(key, defaultValue);

        } catch (RuntimeException ex) {

            return defaultValue;
        }
    }


    public static void set(String key, String value){

        getEditor().putString(key, value).apply();
    }


    public static void set(String key, boolean value){

        getEditor().putBoolean(key, value).apply();

    }

    // Value from the resource, Strings.xml
    public static String getResource(int key, String defaultValue) {

        if (mResources == null)
            return defaultValue;

        String string;

        try {

            string = mResources.getString(key);

        } catch (Resources.NotFoundException ex) {

            string = defaultValue;
        }

        return string;
    }


    public static int get(String key, int defaultValue) {

        try {

            return mSettings.getInt(key, defaultValue);

        } catch (RuntimeException ex) {

            return defaultValue;
        }
    }


    public static void set(String key, int value){

        getEditor().putInt(key, value).apply();
    }


    static long get(String key, long defaultValue) {

        try {

            return mSettings.getLong(key, defaultValue);

        } catch (RuntimeException ex) {

            return defaultValue;
        }
    }


    static float get(String key, float defaultValue) {
        try {

            return mSettings.getFloat(key, defaultValue);

        } catch (RuntimeException ex) {

            return defaultValue;
        }
    }


    public static boolean get(String key, boolean defaultValue) {

        try {

            return mSettings.getBoolean(key, defaultValue);

        } catch (RuntimeException ex) {

            return defaultValue;
        }
    }


    public static void onDestroy() {

        mController = null;

        mPrefsListener = null;
    }

   // @Keep
    private static final String TODOITEM = "item";

    static private SharedPreferences mSettings;

    static private appController  mController;

    static private SharedPreferences.OnSharedPreferenceChangeListener mPrefsListener;

    static private Resources mResources;

    // Important to KEEP up to date when access settings in STATIC environment.
    static final private String PACKAGE_NAME = App.getPackageName();
}