package com.andrioussolutions.frmwrk;

import com.google.firebase.analytics.FirebaseAnalytics;

import com.andrioussolutions.files.InstallFile;

import android.app.Activity;
import android.os.Bundle;
import android.provider.Settings.Secure;
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
 * Created  2/12/2017.
 */

public class appAnalytics{

    private static FirebaseAnalytics mFirebaseAnalytics;

    private static String mUserID = "";



    public static void init(Activity activity){

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(activity);

        if (mFirebaseAnalytics == null){ return; }

        String userId = Secure.getString(activity.getContentResolver(), Secure.ANDROID_ID);

        if(userId == null){

            userId = InstallFile.id(activity);
        }

        if (userId != null && !userId.isEmpty()){

            setUserID(userId);
        }
    }




    public static void logEvent(String event, Bundle bundle){

        if (mFirebaseAnalytics == null){ return; }

        mFirebaseAnalytics.logEvent(event, bundle);
    }




    public static void logEvent(String event, String description){

        if (mFirebaseAnalytics == null){ return; }

        Bundle bundle = new Bundle();

        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, description);

        mFirebaseAnalytics.logEvent(event, bundle);
    }



    public static void logEvent(String event, String param, String description){

        if (mFirebaseAnalytics == null){ return; }

        Bundle bundle = new Bundle();

        bundle.putString(param, description);

        mFirebaseAnalytics.logEvent(event, bundle);
    }




    public static boolean setUserProperty(String property, String value){

        if (property == null || value == null){ return false; }

        if (mFirebaseAnalytics == null){ return false; }

        boolean set = true;

        try{

            mFirebaseAnalytics.setUserProperty(property, value);

        }catch (Exception ex){

            set = false;
        }

        return set;
    }




    public static boolean setUserID(String id){

        if (id == null){ return false; }

        if (mFirebaseAnalytics == null){ return false; }

        boolean set = true;

        try{

            mFirebaseAnalytics.setUserId(id);

            mUserID = id;

        }catch (Exception ex){

            set = false;
        }

        return set;
    }




    public static String getUserID(){

        return mUserID;
    }




    public static void onDestroy(){

        mFirebaseAnalytics = null;
    }
}
