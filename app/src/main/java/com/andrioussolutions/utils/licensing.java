package com.andrioussolutions.utils;

import com.google.android.vending.licensing.AESObfuscator;
import com.google.android.vending.licensing.LicenseChecker;
import com.google.android.vending.licensing.LicenseCheckerCallback;
import com.google.android.vending.licensing.ServerManagedPolicy;

import com.andrioussolutions.frmwrk.App;
import com.andrioussolutions.frmwrk.appAnalytics;
import com.andrioussolutions.frmwrk.appFireBaseDB;
import com.andrioussolutions.frmwrk.message.appMessaging;
import com.andrioussolutions.frmwrk.settings.appSettings;

import android.content.Context;
import android.support.annotation.Keep;
/**
 * Copyright  2016  Andrious Solutions Ltd.
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
 * Created  12/8/2016.
 */

public class licensing{

    public licensing(Context context, String publicKey){

        this(context);

        mPublicKey = publicKey;
    }




    public licensing(Context context){

        mContext = context;

        mLogEvent = new appFireBaseDB("Licencing");
    }




    public licensing(Context context, String publicKey, LicenseCheckerCallback callback){

        this(context, callback);

        mPublicKey = publicKey;
    }




    public licensing(Context context, LicenseCheckerCallback callback){

        this(context);

        mCallback = callback;
    }




    public boolean check(){

        boolean checked = true;

        LicenseCheckerCallback callback;

        if (mCallback == null){

            callback = new LicenseCallback();
        }else{

            callback = mCallback;
        }

        if (mPublicKey == null){

            return false;
        }

        try{

            checking(mPublicKey, callback);

        }catch (Exception ex){

            checked = false;
        }

        return checked;
    }




    @Keep
    private void checking(String publicKey, LicenseCheckerCallback callback){

        if (mChecker == null){

//            AESObfuscator ob = new AESObfuscator(
//                    new byte[]{-73, 95, 70, -126, -103, -57, 14, -46, 51, 88, -5,
//                            -60, 77, -88, -63, -13, -1, 82, -4, 9},
//                    App.getPackageName(), App.getAndroidID());

            // Construct the LicenseChecker with a policy.
            mChecker = new LicenseChecker(mContext.getApplicationContext(),
                    new ServerManagedPolicy(mContext, new AESObfuscator(
                            new byte[]{-73, 95, 70, -126, -103, -57, 14, -46, 51, 88, -5,
                                    -60, 77, -88, -63, -13, -1, 82, -4, 9},
                            App.getPackageName(), App.getAndroidID())), publicKey);
        }

        // Run the default local callback to determine the status
        mChecker.checkAccess(new LicenseCallback());

        // Next run the initial callback if one.
        if (mCallback != null){

            // Not the same object
            if (!mCallback.equals(callback)){

                mChecker.checkAccess(mCallback);
            }
        }

        mChecker.checkAccess(callback);
    }




    public boolean check(String publicKey){

        boolean checked = true;

        LicenseCheckerCallback callback;

        // reset with this new publicKey
        mChecker = null;

        if (mCallback == null){

            callback = new LicenseCallback();
        }else{

            callback = mCallback;
        }

        try{

            checking(publicKey, callback);

        }catch (Exception ex){

            checked = false;
        }

        return checked;
    }




    public boolean check(LicenseCheckerCallback callback){

        boolean checked = true;

        if (mPublicKey == null){

            return false;
        }

        try{

            checking(mPublicKey, callback);

        }catch (Exception ex){

            checked = false;
        }

        return checked;
    }




    public boolean check(String publicKey, LicenseCheckerCallback callback){

        boolean checked = true;

        // reset with this new publicKey
        mChecker = null;

        if (mCallback == null){

            mCallback = callback;
        }

        try{

            checking(publicKey, callback);

        }catch (Exception ex){

            checked = false;
        }

        return checked;
    }




    public String status(){

        return mStatus;
    }




    public int reason(){

        return mReason;
    }




    public boolean  ERROR_INVALID_PACKAGE_NAME(){

     return mReason == LicenseCheckerCallback.ERROR_INVALID_PACKAGE_NAME;
    }




    public boolean  ERROR_NON_MATCHING_UID(){

        return mReason == LicenseCheckerCallback.ERROR_NON_MATCHING_UID;
    }




    public boolean  ERROR_NOT_MARKET_MANAGED(){

        return mReason == LicenseCheckerCallback.ERROR_NOT_MARKET_MANAGED;
    }




    public boolean  ERROR_CHECK_IN_PROGRESS(){

        return mReason == LicenseCheckerCallback.ERROR_CHECK_IN_PROGRESS;
    }




    public boolean  ERROR_INVALID_PUBLIC_KEY(){

        return mReason == LicenseCheckerCallback.ERROR_INVALID_PUBLIC_KEY;
    }




    public boolean  ERROR_MISSING_PERMISSION(){

        return mReason == LicenseCheckerCallback.ERROR_MISSING_PERMISSION;
    }




    public boolean ERROR_SERVER_FAILURE(){

        return mReason == ERROR_SERVER_FAILURE;
    }




    // For the Americans
    public boolean licensed(){

        return licenced();
    }




    // For the British.
    public boolean licenced(){

        return status().equals("TElDRU5TRUQ=");
    }




    // For the Americans
    public boolean notLicensed(){

        return notLicenced();
    }




    // For the British.`
    public boolean notLicenced(){

        return status().equals("Tk9UX0xJQ0VOU0VE");
    }




    // Licencing is not implemented.
    public boolean hasLicencing(){

        return !ERROR_SERVER_FAILURE();
    }




    // For the Americans
    public boolean hasLicensing(){

        return hasLicencing();
    }




    public void onDestroy(){

        mContext = null;

        if (mChecker != null){

            mChecker.onDestroy();
        }
    }




    private class LicenseCallback implements LicenseCheckerCallback{




        @Override
        public void allow(int reason){

//            if (isFinishing()){
//                return; // Don't update UI if Activity is finishing.
//            }

            mReason = reason;

            // LICENSED
            mStatus = "TElDRU5TRUQ=";
        }




        @Override
        public void dontAllow(int reason){

//            if (isFinishing()){
//                return; // Don't update UI if Activity is finishing.
//            }

            mReason = reason;

            // NOT_LICENSED
            mStatus = "Tk9UX0xJQ0VOU0VE";

            //  Set as Not licensed, but not able to determine either way.
            // Licensing may not even be implemented.
            if(reason == ERROR_SERVER_FAILURE){

                return;
            }

            if (appSettings.get("licenced", false)){

                appAnalytics.logEvent("licenced_yet_not_allowed", Integer.toString(reason));

                mLogEvent.insert("licenced_yet_not_allowed", Integer.toString(reason));
            }else{

                if (App.isOnline()){

                    appAnalytics.logEvent("not_licenced", Integer.toString(reason));

                    mLogEvent
                            .add("token", appMessaging.getToken())
                            .insert("not_licenced", Integer.toString(reason));
                }
            }
        }




        @Override
        public void applicationError(int errorCode){

            switch (errorCode){
                case ERROR_INVALID_PACKAGE_NAME:

                    mStatus = "RVJST1JfSU5WQUxJRF9QQUNLQUdFX05BTUU=";

                    break;
                case ERROR_NON_MATCHING_UID:

                    mStatus = "RVJST1JfTk9OX01BVENISU5HX1VJRA==";

                    break;
                case ERROR_NOT_MARKET_MANAGED:

                    mStatus = "RVJST1JfTk9UX01BUktFVF9NQU5BR0VE";

                    break;
                case ERROR_CHECK_IN_PROGRESS:

                    mStatus = "RVJST1JfQ0hFQ0tfSU5fUFJPR1JFU1M=";

                    break;
                case ERROR_INVALID_PUBLIC_KEY:

                    mStatus = "RVJST1JfSU5WQUxJRF9QVUJMSUNfS0VZ";

                    break;
                case ERROR_MISSING_PERMISSION:

                    mStatus = "RVJST1JfTUlTU0lOR19QRVJNSVNTSU9O";

                    break;
                default:

                    //ERROR_UNKNOWN
                    mStatus = "RVJST1JfVU5LTk9XTg==";
            }

            if (appSettings.get("licenced", false)){

                appAnalytics.logEvent("licence_error", "licenced yet " + mStatus);

                mLogEvent.insert("licence_error", "licenced yet " + mStatus);
            }
        }
    }

    private static final int ERROR_SERVER_FAILURE = 291;

    private Context mContext;

    private LicenseChecker mChecker;

    private LicenseCheckerCallback mCallback;

    private String mPublicKey;

    private appFireBaseDB mLogEvent;

    // NOT_KNOWN
    private String mStatus = "Tk9UX0tOT1dO";

    private int mReason = 0;
}
