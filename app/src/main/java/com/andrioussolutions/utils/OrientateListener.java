package com.andrioussolutions.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.util.DisplayMetrics;
import android.view.OrientationEventListener;
import android.view.Surface;
/**
 *  Copyright  2016  Andrious Solutions Ltd.
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
 * Created  10/16/2016.
 */
public class OrientateListener extends OrientationEventListener{

    private static final int THRESHOLD = 40;

    public static final int PORTRAIT = 0;

    public static final int LANDSCAPE = 270;

    public static final int REVERSE_PORTRAIT = 180;

    public static final int REVERSE_LANDSCAPE = 90;

    private int lastRotatedTo = 0;

    private int mLastScreenOrientation = 0;

     private Context mContext = null;

    public OrientateListener(Context context){
        super(context);

        mContext = context;

    }



    @Override
    public void onOrientationChanged(int orientation) {

        getRotation(orientation);
    }



    private int getRotation(int orientation){

        int newRotateTo = lastRotatedTo;

        if(orientation >= 360 + PORTRAIT - THRESHOLD && orientation < 360 || orientation >= 0 && orientation <= PORTRAIT + THRESHOLD)

            newRotateTo = 0;

        else if(orientation >= LANDSCAPE - THRESHOLD && orientation <= LANDSCAPE + THRESHOLD)

            newRotateTo = 90;

        else if(orientation >= REVERSE_PORTRAIT - THRESHOLD && orientation <= REVERSE_PORTRAIT + THRESHOLD)

            newRotateTo = 180;

        else if(orientation >= REVERSE_LANDSCAPE - THRESHOLD && orientation <= REVERSE_LANDSCAPE + THRESHOLD)

            newRotateTo = -90;

        if(newRotateTo != lastRotatedTo) {

            lastRotatedTo = newRotateTo;
        }

        return newRotateTo;
    }


    public int getLastRotation(){

        return lastRotatedTo;
    }


    private boolean isLandscape(int orientation){

        return orientation >= (90 - THRESHOLD) && orientation <= (90 + THRESHOLD);
    }



    private boolean isPortrait(int orientation){

        return (orientation >= (360 - THRESHOLD) && orientation <= 360) || (orientation >= 0 && orientation <= THRESHOLD);
    }


    public int getScreenOrientation() {

        int rotation = ((Activity) mContext).getWindowManager().getDefaultDisplay().getRotation();

        DisplayMetrics dm = new DisplayMetrics();

        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;

        int height = dm.heightPixels;

        int orientation;

        // if the device's natural orientation is portrait:
        if ((rotation == Surface.ROTATION_0
                || rotation == Surface.ROTATION_180) && height > width ||
                (rotation == Surface.ROTATION_90
                        || rotation == Surface.ROTATION_270) && width > height) {

            switch(rotation) {
                case Surface.ROTATION_0:

                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;

                case Surface.ROTATION_90:

                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;

                case Surface.ROTATION_180:

                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                    break;

                case Surface.ROTATION_270:

                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                    break;

                default:

                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
            }
        }
        // if the device's natural orientation is landscape or if the device
        // is square:
        else {

            switch(rotation) {

                case Surface.ROTATION_0:

                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;

                case Surface.ROTATION_90:

                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;

                case Surface.ROTATION_180:

                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                    break;

                case Surface.ROTATION_270:

                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                    break;

                default:

                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
            }
        }

        mLastScreenOrientation = orientation;

        return orientation;
    }



    public int getLastScreenOrientation() {

        return mLastScreenOrientation;
    }


    public boolean hasScreenOrientation(){

        int lastOrientation = getLastScreenOrientation();

        return getScreenOrientation() != lastOrientation;
    }
}
