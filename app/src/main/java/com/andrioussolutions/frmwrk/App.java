package com.andrioussolutions.frmwrk;


import com.google.android.vending.licensing.AESObfuscator;

import com.andrioussolutions.frmwrk.settings.appPreferences;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Debug;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Base64;

import java.io.File;
import java.util.UUID;
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
 *
 *
 * Created  2016-03-09.
 */
// A top-level Java class mimicking static class behavior
public final class App{

    private static appController mController;

    private static String PACKAGE_NAME = "package.name.unknown";

    private static File FILES_DIR;

    private static String LABEL;

    private static appPreferences mPreferences;

    private static ConnectivityManager mConnectMngr;

    private static String PM = new String(Base64.decode("Z2V0UGFja2FnZU1hbmFnZXI=\n", 0));

    private static String PI = new String(Base64.decode("Z2V0UGFja2FnZUluZm8=\n", 0));


    App(appController controller){

        mController = controller;
    }

    public static appController getController(){

        return mController;
    }

    public static Context getContext(){

        return mController;
    }

    public static String setPackageName(){

        if (PACKAGE_NAME.equals("package.name.unknown")){

            PACKAGE_NAME = mController.getPackageName();
        }

        return PACKAGE_NAME;
    }

    public static File getFilesDir(Context pContext){

        if (pContext == null){
            pContext = mController;
        }

        if (FILES_DIR == null){
            FILES_DIR = pContext.getFilesDir();
        }

        return FILES_DIR;
    }

    public static File getFilesDir(){

        if (FILES_DIR == null){

            return new File(Environment.getExternalStorageDirectory(), "");
        }else{

            return FILES_DIR;
        }
    }

    public static String setLabel(Context pContext){

        if (pContext == null){
            pContext = mController;
        }

        PackageManager lPackageManager = mController.getPackageManager();

        ApplicationInfo lApplicationInfo = null;

        try{

            lApplicationInfo = lPackageManager
                    .getApplicationInfo(mController.getApplicationInfo().packageName, 0);

        }catch (final PackageManager.NameNotFoundException e){
        }

        LABEL = (String) (lApplicationInfo != null ? lPackageManager
                .getApplicationLabel(lApplicationInfo) : "Unknown");

        return LABEL;
    }

    public static String getLabel(Context pContext){

        if (LABEL == null){
            LABEL = setLabel(pContext);
        }

        return LABEL;
    }

    public static String getLabel(){

        if (LABEL == null){
            return "Unknown";
        }

        return LABEL;
    }

    public static String getPackageName(){

        return App.setPackageName();
    }

    public static appPreferences getPreferences(){

        return mPreferences;
    }

    public static void setPreferences(appPreferences pref){

        mPreferences = pref;
    }

    //  Requires: <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    public static boolean isOnline(){

        //  This app has been destroyed??
        if (mController == null){ return false; }

        if (mConnectMngr == null){

            mConnectMngr =
                    (ConnectivityManager) mController.getSystemService(Context.CONNECTIVITY_SERVICE);
        }

        NetworkInfo netInfo = mConnectMngr.getActiveNetworkInfo();

        return netInfo != null && netInfo.isConnected();
    }

    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean inAirplaneMode(){

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1){

            return Settings.System.getInt(mController.getContentResolver(),
                    Settings.System.AIRPLANE_MODE_ON, 0) != 0;

        }else{

            return Settings.Global.getInt(mController.getContentResolver(),
                    Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
        }
    }

    public static String NoConnectivity(){

        String connectivity = "";

        if (mConnectMngr == null){

            mConnectMngr =
                    (ConnectivityManager) mController.getSystemService(Context.CONNECTIVITY_SERVICE);
        }

        NetworkInfo netInfo = mConnectMngr.getActiveNetworkInfo();

        if (netInfo == null){

            if (inAirplaneMode()){

                connectivity = "Airplane Mode";

            }else{

                connectivity = "No connectivity exists";
            }
        }else if (!netInfo.isAvailable()){

            connectivity = "No Connectivity available";
        }else if (!netInfo.isConnected()){

            connectivity = "No Connectivity exists";
        }else if (!netInfo.isConnectedOrConnecting()){

            connectivity = "Not Connecting";
        }

        return connectivity;
    }

    public static boolean inDebugger(){

        return Debug.isDebuggerConnected();
    }

    // <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    public static String getDeviceID(){

        final TelephonyManager tm = (TelephonyManager) mController
                .getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, androidId;

        tmDevice = "" + tm.getDeviceId();

        tmSerial = "" + tm.getSimSerialNumber();

        androidId = "" + android.provider.Settings.Secure.getString(mController.getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(),
                ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());

        return deviceUuid.toString();
    }

    public static String getAndroidID(){

        return Settings.Secure.getString(mController.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static AESObfuscator obfuscator(){
        return new AESObfuscator(
                new byte[]{-73, 95, 70, -126, -103, -57, 14, -46, 51, 88, -5,
                        -60, 77, -88, -63, -13, -1, 82, -4, 9},
                getPackageName(), getAndroidID());
    }

    // Return the Package Manager reference in 64bit code.
    public static String getPM(){

        return PM;
    }

    // Package Info.
    public static String getPI(){

        return PI;
    }



    public static void onDestroy(){

        mController = null;

        FILES_DIR = null;

        mPreferences = null;

        mConnectMngr = null;
    }
}