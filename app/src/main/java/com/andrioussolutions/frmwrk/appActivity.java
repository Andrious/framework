package com.andrioussolutions.frmwrk;

import com.andrioussolutions.R;
import com.andrioussolutions.frmwrk.files.appFile;
import com.andrioussolutions.frmwrk.google.appGooglePlay;
import com.andrioussolutions.frmwrk.message.appMessaging;
import com.andrioussolutions.frmwrk.ui.appNotifications;

//import com.andrious.errorhandler.firebase.ErrorHandler;
import com.gtfp.errorhandler.ErrorHandler;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.support.annotation.Keep;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.HashSet;
import java.util.Set;
/**
 *  Copyright  2017  Andrious Solutions Ltd.
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
 *     Created 3/10/2017
 */
public abstract class appActivity extends Activity
        implements SharedPreferences.OnSharedPreferenceChangeListener,
        Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener{

    protected static String PACKAGE_NAME = "";

    protected static String INTENT_PACKAGE_NAME = "";

    private Boolean mJustCreated = true;

    private Boolean mRecreated = false;

    private Boolean mWasPaused = false;

    private Boolean mWasStopped = false;

    private Boolean mWasOutsideCall = false;

    private LayoutInflater mInflator;

    private String mPath;

    private Intent mIntent;

    private Set<OnCreateViewListener> mOnCreateViewListeners;

    private InputMethodManager mInputMnger;

    Set<OnBackPressedListener> mBackPressedListeners;



    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        ErrorHandler.toCatch(this);

        appAnalytics.init(this);

        appGooglePlay.onCreate(this);

        appMessaging.onCreate(this);

        appNotifications.onCreate(this);

        appFile.onCreate(this);


//        try{
//
//            // Set locale to the device's locale
//            Locale.setDefault(Resources.getSystem().getConfiguration().locale);
//
//        }catch (Exception ex){
//
//            ErrorHandler.logError(ex);
//        }

        // SPECIAL CASE: Most times savedInstanceState is null.
        // Check whether we're recreating a previously destroyed instance
        // Use wasRecreated() here
        mRecreated = savedInstanceState != null;

        mJustCreated = savedInstanceState == null;

        if (PACKAGE_NAME.equals("")){
            PACKAGE_NAME = this.getPackageName();
        }

        if (INTENT_PACKAGE_NAME.equals("")){

            // Get the message from the intent
            Intent intent = this.getIntent();

            INTENT_PACKAGE_NAME = intent.getComponent().getPackageName();
        }

        // Has this been called by another app?
        mWasOutsideCall = !INTENT_PACKAGE_NAME.equals(PACKAGE_NAME);

// getActionBar() creates  mContentParent = generateLayout(mDecor) which is premature.
//        // Make sure we're running on Honeycomb or higher to use ActionBar APIs
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//
//            // For the main activity, make sure the app icon in the action bar
//            // does not behave as a button
//            ActionBar actionBar = getActionBar();
//
//            if(actionBar != null){
//
//               actionBar.setHomeButtonEnabled(false);
//            }
//        }

        mOnCreateViewListeners = new HashSet<>();

        // Used in creating custom views.
        mInflator = LayoutInflater.from(this);

//        mInflator.setFactory(new LayoutFactory(this));

        mPath = this.getComponentName().getClassName();

        mPath = mPath.substring(0, mPath.lastIndexOf(".") + 1);
    }



    @Override
    protected void onStart(){

        super.onStart();
    }



    /*
     * Called after onStart()
     *
     */
    @Override
    protected void onResume(){

        super.onResume();

        // The Activity property is set: this.mResumed = true;
        // Use justCreated() here for that one special case
        // Use wasPaused() here
        // Use wasStopped() here
        // Use wasRecreated() here
    }



    /**
     * Called when activity resume is complete (after {@link #onResume} has
     * been called). Applications will generally not implement this method;
     * it is intended for system classes to do final setup after application
     * resume code has run.
     * /
     *
     *
     * /*
     * When paused, the Activity instance is still kept as a resident in memory.
     * Allows you to stop ongoing and 'resource hog' actions that should not continue while paused.
     * Release system resources.
     * Good time possibly to save any information that should be
     * but do avoid performing CPU-intensive work here such as writing to a database.
     */
    @Override
    protected void onPause(){

        super.onPause();

        mJustCreated = false;
        mWasStopped = false;
        mWasPaused = true;
    }



    /*
     * Only fired after a 'stopped' state.
     *
     */
    @Override
    protected void onRestart(){

        super.onRestart();
    }



    /*
     * The system retains the Activity instance in system memory when it is stopped,
     * Here, CPU-intensive work to save data to a database.
     * The system might destroy the instance if it needs to recover system memory.
     * The system might even kill your app process without calling onDestroy()
     * Even if the system destroys the activity, the state of the View objects is still retained in a Bundle
     *
     */
    @Override
    protected void onStop(){

        super.onStop();

        // Important to note it's no longer paused.
        mWasPaused = false;
        mWasStopped = true;
    }



    /*
     * Most apps don't need to implement this method because local class references are destroyed
     * with the activity and your activity should perform most cleanup during onPause() and onStop().
     * However, if your activity includes background threads that you created during onCreate()
     * or other long-running resources that could potentially leak memory if not properly closed,
     * you should kill them during onDestroy().
     *
     * You might call finish() from within onCreate() to destroy the activity.
     * In that case, the system immediately calls onDestroy() without calling
     * any of the other lifecycle methods.
     *
     */
    @Override
    protected void onDestroy(){

        super.onDestroy();

        appAnalytics.onDestroy();

        appGooglePlay.onDestroy();

        appMessaging.onDestroy();

        appNotifications.onDestroy();

        appFile.onDestroy();

        // May be coming straight from onCreate()
        // Use wasPaused() here
        // Use wasRecreated() here
        mInflator = null;

        mPath = null;

        mIntent = null;

        mInputMnger = null;
    }


    public void showSoftKeyboard(View view) {

        if (view.requestFocus()) {

            if (mInputMnger == null) {

                mInputMnger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            }

            mInputMnger.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }


    /*
    This allows for custom views to be used in XML files without specifying
    the class' full package name (i.e. directory path).
     */
    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs){

        View view;

//        if (mInflator == null){
//
//            mInflator = LayoutInflater.from(context);
//
//            mPrefix = ((Activity) context).getComponentName().getClassName();
//
//            mPrefix = mPrefix.substring(0, mPrefix.lastIndexOf(".")+1);
//        }

        // Don't bother if 'a path' os already specified.
        if (name.indexOf('.') > -1){

            return null;
        }

        try{

            view = mInflator.createView(name, mPath, attrs);

            for (OnCreateViewListener listener : mOnCreateViewListeners){

                listener.onCreateView(view);
            }

        }catch (ClassNotFoundException e){

            view = null;
        }

        return view;
    }



    /*
     * Called after onStart() and before  onPostCreate(Bundle)
     *
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){

        super.onRestoreInstanceState(savedInstanceState);
    }



    /*
     * Called when activity start-up is complete (after onStart() and onRestoreInstanceState(Bundle) have been called).
     * Applications will generally not implement this method; it is intended for system classes to do
     * final initialization after application code has run.
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState){

        super.onPostCreate(savedInstanceState);

    }



    @Override
    public void onContentChanged(){

        super.onContentChanged();
    }



    @Override
    // Called when the activity is re-launched while at the top of the activity stack instead
    // of a new instance of the activity being started.
    protected void onNewIntent(Intent intent){

        super.onNewIntent(intent);

    }



    // Return true to show the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        if (!super.onCreateOptionsMenu(menu)){

            return false;
        }

        this.getMenuInflater().inflate(R.menu.options_menu, menu);

        return true;
    }



    @Override
    public boolean onContextItemSelected(MenuItem item){

        if (super.onContextItemSelected(item)){ return true; }

        return true;
    }



    // Return the results from another Activity
    // Called when a startActivityForResult() is called from this Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){

    }



    abstract public boolean onPreferenceChange(Preference preference, Object newValue);



    // TODO Put this in a framework as a standard method.
    private boolean hideActionBar(){

        ActionBar bar = this.getActionBar();

        if (bar != null){

            bar.hide();
        }

        return bar != null;
    }



    public boolean runActivity(Class<?> cls){

        if (mIntent == null){

            mIntent = new Intent(this, cls);

//             mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
//            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        }else{

            mIntent = mIntent.setClass(this, cls);
        }

        try{

            this.startActivity(mIntent);

            return true;

        }catch (RuntimeException ex){

            return false;
        }
    }


    public void addOnBackPressedListener(OnBackPressedListener listener) {

        if (mBackPressedListeners == null) {

            mBackPressedListeners = new HashSet<>();
        }

        mBackPressedListeners.add(listener);
    }


    public void removeOnBackPressedListener(OnBackPressedListener listener) {

        if (mBackPressedListeners != null) {

            mBackPressedListeners.remove(listener);
        }
    }


    @Override
    public void onBackPressed(){

        super.onBackPressed();

        if (mBackPressedListeners != null){

            for (OnBackPressedListener  listener : mBackPressedListeners) {

                listener.onBackPressed();
            }
        }
    }



    public final Boolean justCreated(){

        return mJustCreated;
    }



    public final Boolean wasStopped(){

        return mWasStopped;
    }



    public final Boolean wasPaused(){

        return mWasPaused;
    }



    public final Boolean wasRecreated(){

        return mRecreated;
    }



    public final Boolean wasCalledOutside(){

        return mWasOutsideCall;
    }



    public void setOnCreateViewListener(OnCreateViewListener listener){

        mOnCreateViewListeners.add(listener);
    }



    public void removeOnCreateViewListener(OnCreateViewListener listener){

        mOnCreateViewListeners.remove(listener);
    }

    public interface OnCreateViewListener{

        /*
This allows for custom views to be used in XML files without specifying
the class' full package name (i.e. directory path).
*/
        void onCreateView(View view);
    }

    // Allows code to run when the back button is pressed.
    @Keep
    public interface OnBackPressedListener{

         void onBackPressed();
    }

}
