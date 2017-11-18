package com.andrioussolutions.frmwrk.settings;

import com.andrioussolutions.R;
import com.andrioussolutions.frmwrk.appActivity;
import com.andrioussolutions.frmwrk.appController;
import com.andrioussolutions.frmwrk.appView;
import com.andrioussolutions.utils.dialog;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

import java.util.HashSet;
import java.util.Set;
/**
 * Copyright  2017  Andrious Solutions Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 *
 * Created  8/13/2016.
 */
abstract public class appPreferences extends PreferenceFragment
        implements Preference.OnPreferenceClickListener
        , Preference.OnPreferenceChangeListener
        , SharedPreferences.OnSharedPreferenceChangeListener
        , dialog.DialogBoxListener{

    protected static PreferenceManager mPreferenceManager;

    protected static PreferenceScreen mPreferenceScreen;

    //    @Keep
    static int mPosition;

    Preference mPreferenceClicked;

    private int mREQUEST_CODE;

    //    @Keep
    private CharSequence mSummary;

    private dialog mDialogue;

    private appController mController;

    private appView mAppView;

    // Should be initialized right off the hop.
    private Set<OnDestroyListener> mOnDestroyListeners = new HashSet<>();
    //    super.onActivityResult(requestCode, resultCode, data);

//        void SoundSettingsResult(Intent data){
//
//            Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
//
//            if (uri == null) return;
//
//            ToDoItem itemToDo = getItemToDo(data);
//
//            if (itemToDo != null) {
//
//                itemToDo.setSound(uri);
//
//                return;
//            }
//
//            if (!mSoundPreference.shouldCommit()) {
//
//               return;
//            }
//
//            SharedPreferences.Editor editor = mSoundPreference.getEditor();
//
//            editor.putString(mSoundPreference.getKey(), uri.toString());
//
//            editor.apply();
//        }

    private appActivity.OnBackPressedListener mBackPressedListener;




    /** Sets up the action bar for an {@link PreferenceScreen} */
    public static void initializeActionBar(PreferenceScreen parentScreen,
            PreferenceScreen preferenceScreen){

        final Dialog dialog = preferenceScreen.getDialog();

        if (dialog == null){

            return;
        }

        // Inialize the action bar
        ActionBar bar = dialog.getActionBar();

        if (bar != null){

            bar.setDisplayHomeAsUpEnabled(true);
        }
        // Apply custom home button area click listener to close the PreferenceScreen because PreferenceScreens are dialogs which swallow
        // events instead of passing to the activity
        // Related Issue: https://code.google.com/p/android/issues/detail?id=4611
        View homeBtn = dialog.findViewById(android.R.id.home);

        if (homeBtn != null){

            View.OnClickListener dismissDialogClickListener = new View.OnClickListener(){

                @Override
                public void onClick(View v){

                    dialog.cancel();
                }
            };

            // Prepare yourselves for some hacky programming
            ViewParent homeBtnContainer = homeBtn.getParent();

            // The home button is an ImageView inside a FrameLayout
            if (homeBtnContainer instanceof FrameLayout){

                ViewGroup containerParent = (ViewGroup) homeBtnContainer.getParent();

                if (containerParent instanceof LinearLayout){

//                    View view = (View) containerParent.getParent();

//                        if (view bar.getCustomView() ){

//                        }else {

                    // This view also contains the title text, set the whole view as clickable
                    containerParent
                            .setOnClickListener(dismissDialogClickListener);
//                        }
                }else{

                    // Just set it on the home button
                    ((FrameLayout) homeBtnContainer)
                            .setOnClickListener(dismissDialogClickListener);
                }
            }else{

                // The 'If all else fails' default case
                homeBtn.setOnClickListener(dismissDialogClickListener);
            }
        }
    }




    public static Preference openPreference(String prefKey){

//            Preference pref = findPreference(mPreferenceScreen, prefKey);
//
//            if (pref != null) {
//
//                mPreferenceScreen.onItemClick(null, null, mPosition, 0);
//            }

        final ListAdapter listAdapter = mPreferenceScreen.getRootAdapter();

        String key;

        Preference pref = null;

        // Note, this just goes through the top level of preferences
        for (int cnt = 0; cnt < listAdapter.getCount(); ++cnt){

            Object item = listAdapter.getItem(cnt);

            if (!(item instanceof Preference)){

                continue;
            }

            pref = ((Preference) item);

            key = pref.getKey();

            if (key != null && key.equals(prefKey)){

                // Preference has a *hidden* performClick()
                mPreferenceScreen.onItemClick(null, null, cnt, 0);
                break;
            }
        }

        return pref;
    }




    static Preference findPreference(PreferenceGroup group, String key){

        mPosition = -1;

        return PreferenceFind(group, key);
    }




    //    @Keep
    private static Preference PreferenceFind(PreferenceGroup group, String key){

        final int preferenceCount = group.getPreferenceCount();

        for (int i = 0; i < preferenceCount; i++){

            final Preference preference = group.getPreference(i);

            final String curKey = preference.getKey();

            if (curKey != null && curKey.equals(key)){

                mPosition++;

                return preference;
            }

            if (preference instanceof PreferenceGroup){

                mPosition++;

                Preference returnedPreference = PreferenceFind(
                        (PreferenceGroup) preference, key);

                if (returnedPreference != null){

                    return returnedPreference;
                }
            }
        }

        return null;
    }




    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);

        mPreferenceManager = getPreferenceManager();

        mPreferenceScreen = getPreferenceScreen();

        initPreferences(mPreferenceScreen);

        //       setPreferenceListeners();

        // Run code the moment a preference is changed.
//        mSettings.registerOnSharedPreferenceChangeListener(this);

        mDialogue = new dialog(getActivity());

        if (mDialogue != null){

            mDialogue.setDialogBoxListener(this);
        }

        mOnDestroyListeners = new HashSet<>();

        int id = appPreferences.class.hashCode();

        mREQUEST_CODE = (id < 0) ? -id : id;
    }




    protected void initPreferences(PreferenceScreen screen){

        PreferenceCategory prefCategory;
        Preference pref;

        for (int index = 0; index < screen.getPreferenceCount(); index++){

            pref = screen.getPreference(index);

            if (!(pref instanceof PreferenceCategory)){

                if (pref instanceof PreferenceScreen){

                    setupPreferenceScreen((PreferenceScreen) pref);

                    initPreferences((PreferenceScreen) pref);
                }else{

                    setupPreference(pref);
                }

                continue;
            }

            prefCategory = (PreferenceCategory) pref;

            for (int cnt = 0; cnt < prefCategory.getPreferenceCount(); cnt++){

                pref = prefCategory.getPreference(cnt);

                if (pref instanceof PreferenceScreen){

                    setupPreferenceScreen((PreferenceScreen) pref);

                    initPreferences((PreferenceScreen) pref);
                }else{

                    setupPreference(pref);
                }
            }
        }
    }




    abstract protected void setupPreferenceScreen(PreferenceScreen screen);




    abstract protected void setupPreference(Preference preference);




    // Here is the preference listener.
    // Override if you want to.
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key){

        int breakpoint = 0;
    }




    // Override if you want to.
    @Override
    public boolean onPreferenceClick(Preference preference){

        // false if you want it to continue through
        return false;
    }




    // Set by setOnPreferenceChangeListener
    // A hook to  the Controller of the application.
    // Override if you what to.
    public boolean onPreferenceChange(Preference preference, Object newValue){

        try{

            return (getController()).onPreferenceChange(preference, newValue);

        }catch (Exception ex){

            return false;
        }
    }




    private void setPreferenceListeners(PreferenceScreen screen){

        PreferenceCategory prefCategory;
        Preference pref;

        int screenCount = screen.getPreferenceCount();

        for (int index = 0; index < screenCount; index++){

            pref = screen.getPreference(index);

            if (!(pref instanceof PreferenceCategory)){

                pref.setOnPreferenceChangeListener(this);

                pref.setOnPreferenceClickListener(this);

                if (pref instanceof PreferenceScreen){

                    setPreferenceListeners((PreferenceScreen) pref);
                }

                continue;
            }

            prefCategory = (PreferenceCategory) pref;

            int prefCount = prefCategory.getPreferenceCount();

            for (int cnt = 0; cnt < prefCount; cnt++){

                pref = prefCategory.getPreference(cnt);

                pref.setOnPreferenceChangeListener(this);

                pref.setOnPreferenceClickListener(this);

                if (pref instanceof PreferenceScreen){

                    setPreferenceListeners((PreferenceScreen) pref);
                }
            }
        }
    }




    abstract public boolean setItemPreferences(Intent intent, Preference preference,
            Object newValue);




    // Return the results from another Activity
    @Override
    abstract public void onActivityResult(int requestCode, int resultCode, Intent data);




    // TODO Called when asking the user to delete records, etc.
    public void dialogResult(boolean result){

        if (result){

            onPreferenceClick(mPreferenceClicked);
        }

        // Important to null this now.
        mPreferenceClicked = null;
    }




    /*
        Called immediately after {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
        has returned, but before any saved state has been restored in to the view.
   */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){

//        setPreferenceListeners(mPreferenceScreen);
    }




    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
            Preference preference){
        super.onPreferenceTreeClick(preferenceScreen, preference);

        // If the user has clicked on a preference screen, set up the action bar
        if (preference instanceof PreferenceScreen){

            initializeActionBar(preferenceScreen, (PreferenceScreen) preference);
        }

        // Important to return false to continue the preferences.
        return false;
    }




    public void addOnBackPressedListener(appActivity.OnBackPressedListener listener){

        mBackPressedListener = listener;
    }




    public appActivity.OnBackPressedListener OnBackPressedListener(){

        return mBackPressedListener;
    }




    protected appController getController() throws Exception{

        if (mController != null) return mController;

        mController = (appController) getActivity();

        return mController;
    }




    protected appView getAppView() throws Exception{

        if (mAppView != null) return mAppView;

        appController controller = getController();

        mAppView = controller.getView();

        return mAppView;
    }




    public void setOnDestroyListener(OnDestroyListener listener){

        mOnDestroyListeners.add(listener);
    }




    public void removeOnDestroyListener(OnDestroyListener listener){

        mOnDestroyListeners.remove(listener);
    }




    public boolean licenced(){

        boolean licenced;

        try{

            licenced = getController().licenced();

        }catch (Exception ex){

            licenced = true;
        }

        return licenced;
    }




    public boolean licenceError(){

        boolean licenceError;

        try{

            licenceError = getController().licenceError();

        }catch (Exception ex){

            licenceError = true;

        }

        return licenceError;
    }




    private void onDestroy(PreferenceScreen screen){

        Preference pref;

        PreferenceCategory prefCategory;

        CharSequence title, summary;

        for (int index = 0; index < screen.getPreferenceCount(); index++){

            pref = screen.getPreference(index);

            if (!(pref instanceof PreferenceCategory)){

                if (pref instanceof PreferenceScreen){

                    for (OnDestroyListener listener : mOnDestroyListeners){

                        listener.onDestroy((PreferenceScreen) pref);
                    }

                    onDestroy((PreferenceScreen) pref);
                }
                continue;
            }

            prefCategory = (PreferenceCategory) pref;

            for (int cnt = 0; cnt < prefCategory.getPreferenceCount(); cnt++){

                pref = prefCategory.getPreference(cnt);

                if (pref instanceof PreferenceScreen){

                    for (OnDestroyListener listener : mOnDestroyListeners){

                        listener.onDestroy((PreferenceScreen) pref);
                    }

                    onDestroy((PreferenceScreen) pref);
                }
            }
        }
    }




    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        getView().setBackgroundColor(Color.BLACK);
        getView().setClickable(true);
    }




    @Override
    public void onPause(){

        super.onPause();
    }




    @Override
    public void onDestroy(){

        if (mPreferenceManager != null){

            super.onDestroy();
        }

        if(mOnDestroyListeners != null){

            onDestroy(mPreferenceScreen);

            mOnDestroyListeners = null;
        }

        if (mDialogue != null){

            mDialogue.onDestroy();

            mDialogue = null;
        }

        mPreferenceClicked = null;

        mPreferenceManager = null;

        mPreferenceScreen = null;

        mController = null;

        mAppView = null;
    }

    public interface OnDestroyListener{

        void onDestroy(PreferenceScreen screen);
    }
}
