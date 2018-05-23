package com.andrioussolutions.admob;

import com.google.android.gms.ads.AdListener;

import com.andrioussolutions.R;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
public abstract class AdMobFragment extends Fragment{

    private AdMob mAdMob;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        AdMod.setEnabled(appSettings.get("in-app ads", !App.inDebugger()));
        AdMob.setEnabled(true);

        mAdMob = new AdMob(getActivity(), R.string.admob_id);
    }

    @Override
    abstract public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState);



//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState){
//        super.onActivityCreated(savedInstanceState);
//
//
//    }



    @Override
    public void onStart(){
        super.onStart();

        mAdMob.loadAd(R.id.adView);
    }



    @Override
    public void onPause(){
        super.onPause();

        mAdMob.onPause();
    }



    @Override
    public void onResume(){
        super.onResume();

        mAdMob.onResume();
    }



    @Override
    public void onDestroy(){
        super.onDestroy();

        mAdMob.onDestroy();
    }



    public void keywords(@NonNull String keywords){

        mAdMob.keywords(keywords);
    }



    public void localLocation(){

        mAdMob.localLocation();
    }



    public void localLocation(boolean local){

        mAdMob.localLocation(local);
    }



    public void setAdListener(@NonNull AdListener listener){

        mAdMob.setAdListener(listener);
    }



    public void addAdListener(@NonNull AdListener listener){

        mAdMob.addAdListener(listener);
    }



    public void removeAdListener(@NonNull AdListener listener){

        mAdMob.removeAdListener(listener);
    }



    public void addOnAdClosed(@NonNull AdMob.OnAdClosedListener listener){

        mAdMob.addOnAdClosed(listener);
    }



    public void removeOnAdClosed(@NonNull AdMob.OnAdClosedListener listener){

        mAdMob.removeOnAdClosed(listener);
    }



    public void addOnAdFailedToLoad(@NonNull AdMob.OnAdFailedToLoadListener listener){

        mAdMob.addOnAdFailedToLoad(listener);
    }



    public void removeOnAdFailedToLoad(@NonNull AdMob.OnAdFailedToLoadListener listener){

        mAdMob.removeOnAdFailedToLoad(listener);
    }



    public void addOnAdLeftApplication(@NonNull AdMob.OnAdLeftApplicationListener listener){

        mAdMob.addOnAdLeftApplication(listener);
    }



    public void removeOnAdLeftApplication(@NonNull AdMob.OnAdLeftApplicationListener listener){

        mAdMob.removeOnAdLeftApplication(listener);
    }



    public void addOnAdOpened(@NonNull AdMob.OnAdOpenedListener listener){

        mAdMob.addOnAdOpened(listener);
    }



    public void removeOnAdOpened(@NonNull AdMob.OnAdOpenedListener listener){

        mAdMob.removeOnAdOpened(listener);
    }



    public void addOnAdLoaded(@NonNull AdMob.OnAdLoadedListener listener){

        mAdMob.addOnAdLoaded(listener);
    }



    public void removeOnAdLoaded(@NonNull AdMob.OnAdLoadedListener listener){

        mAdMob.removeOnAdLoaded(listener);
    }
}
