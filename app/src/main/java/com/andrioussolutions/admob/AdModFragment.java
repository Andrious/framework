package com.andrioussolutions.admob;

import com.google.android.gms.ads.AdListener;

import com.andrioussolutions.R;
import com.andrioussolutions.frmwrk.App;
import com.andrioussolutions.frmwrk.settings.appSettings;

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
public abstract class AdModFragment extends Fragment{

    private AdMod mAdMod;




    @Override
    abstract public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState);




    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        AdMod.setEnabled(appSettings.get("in-app ads", App.inDebugger()));

        mAdMod = new AdMod(getActivity(), R.string.admob_id);
    }




    @Override
    public void onStart(){
        super.onStart();

        mAdMod.loadAd(R.id.adView);
    }




    @Override
    public void onPause(){
        super.onPause();

        mAdMod.onPause();
    }




    @Override
    public void onResume(){
        super.onResume();

        mAdMod.onResume();
    }




    @Override
    public void onDestroy(){
        super.onDestroy();

        mAdMod.onDestroy();
    }




    public void keywords(@NonNull String keywords){

        mAdMod.keywords(keywords);
    }




    public void localLocation(){

        mAdMod.localLocation();
    }




    public void setAdListener(@NonNull AdListener listener){

        mAdMod.setAdListener(listener);
    }




    public void addAdListener(@NonNull AdListener listener){

        mAdMod.addAdListener(listener);
    }




    public void removeAdListener(@NonNull AdListener listener){

        mAdMod.removeAdListener(listener);
    }




    public void addOnAdClosed(@NonNull AdMod.OnAdClosedListener listener){

        mAdMod.addOnAdClosed(listener);
    }




    public void removeOnAdClosed(@NonNull AdMod.OnAdClosedListener listener){

        mAdMod.removeOnAdClosed(listener);
    }
}
