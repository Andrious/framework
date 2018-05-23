package com.andrioussolutions.admob;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import com.andrioussolutions.frmwrk.App;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import java.util.HashSet;

import static android.content.Context.LOCATION_SERVICE;
/**
 * Copyright (C) 2017  Andrious Solutions Ltd.
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
 * Created  01 Jul 2017
 */
public class AdMob{

    private static final String TEST_ADMOD_ID = "ca-app-pub-3940256099942544/6300978111";

    private static boolean mEnabled;

    private AdView mAdView;

    private AdRequest mAdRequest;

    private AdRequest.Builder mAdBuilder;

    private boolean mTestMode = false;

    private AdWrapper mAdWrapper;

    private HashSet<AdListener> mAdListeners = new HashSet<>();

    private HashSet<OnAdClosedListener> mOnAdClosedListeners = new HashSet<>();

    private HashSet<OnAdFailedToLoadListener> mOnAdFailedToLoadListeners = new HashSet<>();

    private HashSet<OnAdLeftApplicationListener> mOnAdLeftApplicationListeners = new HashSet<>();

    private HashSet<OnAdOpenedListener> mOnAdOpenedListeners = new HashSet<>();

    private HashSet<OnAdLoadedListener> mOnAdLoadedListeners = new HashSet<>();

    private boolean mLocalLocation;

    private LocationManager mLocationManager;

    private String mKeywords;

    private Location mLocation;




    public AdMob(Context context){

        // Initialize the Mobile Ads SDK.
        this(context, TEST_ADMOD_ID);
    }




    public AdMob(Context context, String AdModID){

        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(context, AdModID);

        mLocationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

        mTestMode = AdModID.equals(TEST_ADMOD_ID);

        mAdWrapper = new AdWrapper();
    }




    public AdMob(Context context, String AdModID, @IdRes int ResId){

        this(context, AdModID);

        setAdView(ResId);
    }




    public AdMob(Context context, @StringRes int AdModID){

        this(context, App.getController().getResources().getString(AdModID));
    }




    public AdMob(Context context, @StringRes int AdModID, @IdRes int ResId){

        this(context, AdModID);

        setAdView(ResId);
    }




    public static boolean isEnabled(){

        return mEnabled;
    }




    public static boolean setEnabled(boolean enable){

        mEnabled = enable;

        return mEnabled;
    }




    public AdView setAdView(@IdRes int ResId){

        if (mAdView == null){ mAdView = (AdView) App.getController().findViewById(ResId); }

        return mAdView;
    }




    public AdView setAdView(AdView view){

        if (mAdView == null){ mAdView = view; }

        return mAdView;
    }




    public AdView getAdView(){

        return mAdView;
    }




    public AdView getAdView(@IdRes int ResId){

        AdView ad = (AdView) App.getController().findViewById(ResId);

        return ad;
    }




    public AdRequest getAdRequest(){

        if (mAdRequest != null){

            return mAdRequest;
        }

        mAdRequest = getAdBuilder().build();

        return mAdRequest;
    }




    public void loadAd(){

        if (mAdView == null) return;

        mAdView.loadAd(getAdRequest());

        mAdWrapper.setAdListener();
    }




    public void loadAd(@IdRes int ResId){

        if (!isEnabled()) return;

        // No need to load again and again if done once already.
        if (mAdView == null){

            setAdView(ResId);

            loadAd();
        }
    }




    public void loadAd(@IdRes int ResId, AdModListener listener){

        loadAd(ResId);

        setAdListener(listener);
    }




    public void loadAd(@IdRes int ResId, AdListener listener){

        loadAd(ResId);

        setAdListener(listener);
    }




    public void loadAd(AdRequest request){

        if (mAdView != null){ mAdView.loadAd(request); }
    }




    public void loadAd(@IdRes int ResId, AdRequest request){

        loadAd(ResId);

        loadAd(request);
    }




    public void setAdListener(AdModListener listener){

        if (mAdView != null){ mAdView.setAdListener(listener); }
    }




    public void setAdListener(AdListener listener){

        if (mAdView != null){ mAdWrapper.setAdListener(listener); }
    }




    public void addAdListener(@NonNull AdListener listener){

        mAdListeners.add(listener);
    }




    public void removeAdListener(@NonNull AdListener listener){

        mAdListeners.remove(listener);
    }




    public void addOnAdClosed(@NonNull OnAdClosedListener listener){

        mOnAdClosedListeners.add(listener);
    }




    public void removeOnAdClosed(@NonNull OnAdClosedListener listener){

        mOnAdClosedListeners.remove(listener);
    }



    public void addOnAdFailedToLoad(@NonNull OnAdFailedToLoadListener listener){

        mOnAdFailedToLoadListeners.add(listener);
    }



    public void removeOnAdFailedToLoad(@NonNull OnAdFailedToLoadListener listener){

        mOnAdFailedToLoadListeners.remove(listener);
    }



    public void addOnAdLeftApplication(@NonNull OnAdLeftApplicationListener listener){

        mOnAdLeftApplicationListeners.add(listener);
    }



    public void removeOnAdLeftApplication(@NonNull OnAdLeftApplicationListener listener){

        mOnAdLeftApplicationListeners.remove(listener);
    }



    public void addOnAdOpened(@NonNull OnAdOpenedListener listener){

        mOnAdOpenedListeners.add(listener);
    }




    public void removeOnAdOpened(@NonNull OnAdOpenedListener listener){

        mOnAdOpenedListeners.remove(listener);
    }



    public void addOnAdLoaded(@NonNull OnAdLoadedListener listener){

        mOnAdLoadedListeners.add(listener);
    }



    public void removeOnAdLoaded(@NonNull OnAdLoadedListener listener){

        mOnAdLoadedListeners.remove(listener);
    }



    private AdRequest.Builder getAdBuilder(){

        Location location = null;

        if (mAdBuilder != null){

            return mAdBuilder;
        }

        mAdBuilder = new AdRequest.Builder();

        if (mKeywords != null){

            // trim any spaces.
            String[] keywords = mKeywords.trim().split("\\s*,\\s*");

            for (String keyword : keywords){

                mAdBuilder.addKeyword(keyword);
            }
        }

        // Local location takes priority
        if (mLocation != null && !mLocalLocation){

            mAdBuilder.setLocation(mLocation);
        }

        if (mLocalLocation){

            try{

//                location = mLocationManager.getLastKnownLocation(
//                        mLocationManager.getBestProvider(new Criteria(), false));

                location = mLocationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

                // Don't have permission
            }catch (SecurityException ex){

                location = null;

            }catch (Exception ex){

                location = null;
            }

            if (location != null){

                mAdBuilder.setLocation(location);
            }
        }

        if (mTestMode){

            mAdBuilder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR);
        }

        return mAdBuilder;
    }




    public void keywords(@NonNull String keywords){

        mKeywords = keywords;
    }




    public void setLocation(@NonNull Location location){

        mLocation = location;
    }




    public void localLocation(@NonNull boolean local){

        mLocalLocation = local;
    }




    public void localLocation(){

        localLocation(true);
    }




    public void pause(){

        if (mAdView != null){ mAdView.pause(); }
    }




    public void onPause(){

        pause();
    }




    public void resume(){

        if (mAdView != null){ mAdView.resume(); }
    }




    public void onResume(){

        resume();
    }




    public void destroy(){

        mAdWrapper = null;

        if (mAdView != null){ mAdView.destroy(); }

        mAdListeners = null;
        mOnAdClosedListeners = null;
        mOnAdFailedToLoadListeners = null;
        mOnAdLeftApplicationListeners = null;
        mOnAdOpenedListeners = null;
        mOnAdLoadedListeners = null;
    }




    public void onDestroy(){

        destroy();
    }




    // Interface to run code when the ad is closed.
    public interface OnAdClosedListener{

        void onAdClosed();
    }


    // Interface to run code when an ad request fails.
    public interface OnAdFailedToLoadListener{

        void onAdFailedToLoad(int var1);
    }


    // Interface to run code when the user has left the app.
    // This method is invoked after onAdOpened(),
    public interface OnAdLeftApplicationListener{

        void onAdLeftApplication();
    }


    // Interface to run code when an ad opens an overlay that covers the screen.
    public interface OnAdOpenedListener{

        void onAdOpened();
    }


    // Interface to run code when an ad finishes loading.
    public interface OnAdLoadedListener{

        void onAdLoaded();
    }

    // Stuff that has to run within the App when dealing with AdMods.
    private static class AppAdModListener extends AdModListener{


        AppAdModListener(){

        }




        @Override
        public void onAdLoaded(){

            int breakpoint = 0;
        }




        @Override
        public void onAdFailedToLoad(int i){

            int breakpoint = 0;
        }




        @Override
        public void onAdOpened(){

            int breakpoint = 0;
        }




        @Override
        public void onAdClosed(){

            int breakpoint = 0;
        }




        @Override
        public void onAdLeftApplication(){

            int breakpoint = 0;
        }
    }

    public static class AdModListener extends AdListener{

        public AdModListener(){

        }




        @Override
        public void onAdLoaded(){

            // Code to be executed when an ad finishes loading.
            // The onAdLoaded() method is executed when an ad has finished loading.
            // If you want to delay adding the AdView to your activity or fragment until you're sure an ad will be loaded, for example, you can do so here.
            // If you're using a third-party analytics package to track impressions, this is also where you can place the call to record them.
        }




        @Override
        public void onAdFailedToLoad(int i){

            // Code to be executed when an ad request fails.
                /*
                The onAdFailedToLoad() method is the only one that includes a parameter.
                The errorCode parameter indicates what type of failure occurred.
                The possible values are defined as constants in the AdRequest class:
                 ERROR_CODE_INTERNAL_ERROR - Something happened internally; for instance, an invalid response was received from the ad server.
                 ERROR_CODE_INVALID_REQUEST - The ad request was invalid; for instance, the ad unit ID was incorrect.
                 ERROR_CODE_NETWORK_ERROR - The ad request was unsuccessful due to network connectivity.
                 ERROR_CODE_NO_FILL - The ad request was successful, but no ad was returned due to lack of ad inventory.
                 */
        }




        @Override
        public void onAdOpened(){

            // Code to be executed when an ad opens an overlay that covers the screen.
            //  If you're using an analytics package to track clickthroughs, this is a good place to record one.
        }




        @Override
        public void onAdClosed(){

            // Code to be executed when when the user is about to return
            // to the app after tapping on an ad.
            // When a user returns to the app after viewing an ad's destination URL, this method is invoked.
            // Your app can use it to resume suspended activities or perform any other work necessary to make itself ready for interaction.
            // See the AdMob AdListener example for an implementation of the ad listener methods in the Android API Demo app.
        }




        @Override
        public void onAdLeftApplication(){

            // Code to be executed when the user has left the app.
            // This method is invoked after onAdOpened(),
            // when a user click opens another app (such as the Google Play),
            // backgrounding the current app.
            // You get credit for a click-through!
        }
    }

    private class AdWrapper{

        private AdListener mListener;




        void setAdListener(AdListener listener){

            mListener = listener;
        }




        void setAdListener(){

            if (mAdView == null){

                return;
            }

            mAdView.setAdListener(new AppAdModListener(){

                @Override
                public void onAdLoaded(){
                    super.onAdLoaded();

                    if (mListener != null){ mListener.onAdLoaded(); }

                    for (AdListener listener : mAdListeners){

                        listener.onAdLoaded();
                    }

                    for (OnAdLoadedListener listener : mOnAdLoadedListeners){

                        listener.onAdLoaded();
                    }                    
                }




                @Override
                public void onAdFailedToLoad(int error){
                    super.onAdFailedToLoad(error);

                    if (mListener != null){ mListener.onAdFailedToLoad(error); }

                    for (AdListener listener : mAdListeners){

                        listener.onAdFailedToLoad(error);
                    }

                    for (OnAdFailedToLoadListener listener : mOnAdFailedToLoadListeners){

                        listener.onAdFailedToLoad(error);
                    }
                }




                @Override
                public void onAdOpened(){
                    super.onAdOpened();

                    if (mListener != null){ mListener.onAdOpened(); }

                    for (AdListener listener : mAdListeners){

                        listener.onAdOpened();
                    }

                    for (OnAdOpenedListener listener : mOnAdOpenedListeners){

                        listener.onAdOpened();
                    }
                }




                @Override
                public void onAdClosed(){
                    super.onAdClosed();

                    if (mListener != null){ mListener.onAdClosed(); }

                    for (AdListener listener : mAdListeners){

                        listener.onAdClosed();
                    }

                    for (OnAdClosedListener listener : mOnAdClosedListeners){

                        listener.onAdClosed();
                    }
                }




                @Override
                public void onAdLeftApplication(){
                    super.onAdLeftApplication();

                    if (mListener != null){ mListener.onAdLeftApplication(); }

                    for (AdListener listener : mAdListeners){

                        listener.onAdLeftApplication();
                    }

                    for (OnAdLeftApplicationListener listener : mOnAdLeftApplicationListeners){

                        listener.onAdLeftApplication();
                    }
                }
            });
        }
    }
}
