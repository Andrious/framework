package com.andrioussolutions.frmwrk;

import com.google.android.vending.licensing.LicenseCheckerCallback;

import com.andrioussolutions.frmwrk.settings.appSettings;
import com.andrioussolutions.utils.licensing;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.Preference;
import android.support.annotation.IdRes;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.lang.reflect.Method;

import static com.andrioussolutions.frmwrk.App.getPackageName;
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
 * Created  2015-02-07.
 */

abstract public class appView{

    private static final String ALARM_ID = "id";

    private static boolean mClearNotification;

    private static boolean mSyncData;

    private static licensing mLicensing;

    private static boolean mLicenced;

    private final int REQUEST_CODE = 1;

    private final int GOOGLE_REQUEST = 2;

    private appController mController;

    private Activity mActivity;

    private Context mContext;

    private Intent mIntent;

    private int mToDoListID = -1;

    private appSettings mAppSettings;

    private MenuItem mMenuItem;



    public appView(appController controller){

        this((Context) controller);

        mActivity = (Activity) controller;

        mController = controller;

        appSettings.init(controller);

        mLicensing = new licensing(mContext,
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmipHoRWP89NazmT5spv9Dx4YtOIzVfR/7nXZcrO0tRhepzECE211wKqHDZdN28pjDdjQdHp9ZBqb0+z+Joq20QtAhAp2Lpjadl9hI87n+yM94WjIOSd4bCjRLuOMLDPD6HS6ADp7M88OTK1p2A4I/kGR3SAePq48+mKIOwK5cR6/8sFD5DpeSxOPcYxtGF4BWs+sdAbXV8/+6a98m+ffYn5bW0WWDxwMgdbd4N8Wq2MDIONrh7EqhakAW06tBZh+d9M4YIbCp32cSFnXg9/ygUjTgfHCuPon9TsHca3yzUoIrRPrMmfp5WS76wfk+dLmAvzztbafxZuRhlSMEYzMqwIDAQAB",
                mController.LicenseCallback());

        appFireBaseDB.onCreate(this);
    }



    // This is called when there is not activity available.
    public appView(Context context){

        mContext = context;

        // Provide this App's label to a 'global' Static reference.
        App.setLabel(context);
    }



    // Used by external components.
    public static boolean showDialogue(){

        return appSettings.get("popup_notification", true);
    }



    // Used by external components.
    public static boolean clearNotification(){

        return mClearNotification;
    }



    public static boolean licenced(){

//        return (BuildConfig.BUILD_TYPE.equals("debug") && App.inDebugger()) ||  mLicensing.licenced();

        if (mLicenced){ return true; }

        // In case the internet is out and yet already licenced.
        boolean licenced = appSettings.get("licenced", false);

        //  Check for a licence if already licenced.
        if (licenced){

            mLicenced = true;
        }else{

            licenced = mLicensing.licenced();

            if (licenced){

                mLicenced = true;

                appSettings.set("licenced", true);
            }
        }

        return licenced;
    }



    public static boolean notLicenced(){

        return mLicensing.notLicenced();
    }




    public static boolean hasLicencing(){

        return mLicensing.hasLicencing();
    }




    public static boolean licenceError(){

        String status = new String(Base64.decode(mLicensing.status(), 0));

        return status.startsWith("ERROR");
    }



    abstract public void onCreate(Bundle savedInstanceState);



    public appController getController(){

        return mController;
    }



    public Activity getActivity(){

        return mActivity;
    }



    public Context getContext(){

        return mContext;
    }



    public View findViewById(@IdRes int id){

        return mController.findViewById(id);
    }



    public View findViewById(@IdRes int id, Bundle savedInstanceState){

        View view = mController.findViewById(id);

        if (view == null){

            view = new TextView(getContext());
        }

        if (savedInstanceState != null){

            String value = savedInstanceState.getString(Integer.toString(id));

            if (value != null){

                ((TextView) view).setText(value);
            }
        }
        return view;
    }

    // Help gets Signature



    public Method getPImethod(){

        Method mth;

        try{

            mth = mController.getPM().getClass()
                    .getMethod(App.getPI(), String.class, int.class);

        }catch (Exception ex){

            mth = null;
        }
        return mth;
    }


    public boolean runActivity(Class<?> cls){

        if (mIntent == null){

            mIntent = new Intent(mController, cls);
       }else{

            mIntent = mIntent.setClass(mController, cls);
        }

        try{

            mController.startActivity(mIntent);

            return true;

        }catch (Exception ex){

            return false;
        }
    }


    // Return the Package Info.
    // Get Signature
    public Object getPI(){

        Object obj;

        try{

            obj = getPImethod()
                    .invoke(mController.getPM(), getPackageName(), PackageManager.GET_SIGNATURES);

        }catch (Exception ex){

            obj = null;
        }
        return obj;
    }



    // Called by the Controller.
    // Override to implement a menu
    public boolean onOptionsItemSelected(MenuItem item){

        // false so to fall through and continue the selected item.
        return false;
    }



    // May be called by the Controller
    // Override to implement
    // Called when a application preference setting is changed.
    public boolean onPreferenceChange(Preference preference, Object newValue){

        return true;
    }


     // May be called to run some code after a fragment is closed.
    protected void addOnBackPressedListener(appActivity.OnBackPressedListener listener){

         getController().addOnBackPressedListener(listener);
    }


    public void check(final LicenseCheckerCallback callback){

        // Perform off the UI thread.
        new Thread(){

            @Override
            public void run(){

                mLicensing.check(callback);
            }
        }.start();
    }



    public void check(final String pubKey, final LicenseCheckerCallback callback){

        // Perform off the UI thread.
        new Thread(){

            @Override
            public void run(){

                mLicensing.check(pubKey, callback);
            }
        }.start();
    }



    /*
  * Called after onPause()
  * Override to save additional data about your activity's state.
  * The system might destroy this activity while waiting in the back stack.
  *
  */
    // Used by Subclass
    public void onSaveInstanceState(Bundle outState){

    }



    // Used by Subclass
    protected void onStart(){

    }



    /*
     * Called after onStart()
     *
     */
    // Used by Subclass
    protected void onResume(){

    }



    // Used by Subclass
    protected void onPause(){

    }



    // Used by Subclass
    protected void onRestart(){

    }



    /*
     * The system retains the Activity instance in system memory when it is stopped,
     * Here, CPU-intensive work to save data to a database.
     * The system might destroy the instance if it needs to recover system memory.
     * The system might even kill your app process without calling onDestroy()
     * Even if the system destroys the activity, the state of the View objects is still retained in a Bundle
     *
     */
    // Used by Subclass
    protected void onStop(){

    }



    protected void onDestroy(){

        mController = null;

        mContext = null;


        appSettings.onDestroy();

//        if (mAppSettings != null){
//
//            mAppSettings.onDestroy();
//
//            mAppSettings = null;
//        }

        mLicensing.onDestroy();

        appFireBaseDB.onDestroy();
    }
}
