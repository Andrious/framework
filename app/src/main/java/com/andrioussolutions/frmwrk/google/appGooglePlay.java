package com.andrioussolutions.frmwrk.google;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import com.gtfp.errorhandler.ErrorHandler;

import android.app.Activity;
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
 * Created  3/6/2017.
 */

public class appGooglePlay{

    private static Activity mActivity;

    private static GoogleApiAvailability mGoogleApiAvailability;




    private appGooglePlay(){

    }




    public static void onCreate(Activity activity){

        if (mActivity == null){

            mActivity = activity;

            mGoogleApiAvailability = GoogleApiAvailability.getInstance();
        }
    }




    public static boolean isAvailable(){

        if (mActivity == null){

            ErrorHandler.logError("GooglePlay.onCreate() was not called!");

            return false;
        }else{

            return mGoogleApiAvailability.isGooglePlayServicesAvailable(mActivity) ==
                    ConnectionResult.SUCCESS;
        }
    }




    public static boolean makeAvailable(){

        if (mActivity == null){

            ErrorHandler.logError("'GooglePlay.onCreate()' was not called!");

            return false;
        }

        boolean available = isAvailable();

        if (!available){

            mGoogleApiAvailability.makeGooglePlayServicesAvailable(mActivity);
        }

        return available;
    }




    public static void onDestroy(){

        mActivity = null;

        mGoogleApiAvailability = null;
    }
}
