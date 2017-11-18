package com.andrioussolutions.frmwrk;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import com.andrioussolutions.db.dbFirebase;
import com.andrioussolutions.frmwrk.auth.appAuth;
import com.gtfp.errorhandler.ErrorHandler;

import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
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
 * Created  2/23/2017.
 */

public class appFireBaseDB{

    private static final String TABLE_NAME = "Table";

    private static appView mAppView;

    private static dbFirebase mDB;

    private static AuthListener mAuthListener;

    private static AuthFailure mFailListener;


    private String mTableName;

    private DatabaseReference mDBRef;

    private HashSet<ValueEventListener> mEventListeners;

    private dataChange mDataChangeListener;

    private boolean mUseDataChangeListener;

    private HashMap<String, Object> mRecords;



    public appFireBaseDB(@NonNull String tableName){

        String name = tableName.trim();

        if (name.isEmpty()){

            name = TABLE_NAME;
        }

        mTableName = name;

        mEventListeners = new HashSet<>();

        mRecords = new HashMap<>();

        mUseDataChangeListener = false;
    }



    public appFireBaseDB(@NonNull String tableName, @NonNull ValueEventListener listener){

        this(tableName);

        mEventListeners.add(listener);
    }



    public appFireBaseDB(@NonNull appView view, @NonNull String tableName){

        this(tableName);

        onCreate(view);
    }



    static void onCreate(@NonNull appView view){

        if (mAppView == null){

            mAppView = view;

            mAuthListener = new AuthListener();

            mFailListener = new AuthFailure();
        }
    }



    public static void onDestroy(){

        mAppView = null;

        mAuthListener = null;

        mFailListener = null;

        appAuth.onDestroy();

        if (mDB != null){

            // goOffline() is called in here.
            mDB.onDestroy();

            mDB = null;
        }
    }



    public appFireBaseDB useListener(){

        mUseDataChangeListener = true;

        if (mDBRef != null && mDataChangeListener != null){

            mDBRef.addValueEventListener(mDataChangeListener);
        }

        return this;
    }



    public appFireBaseDB useListener(@NonNull ValueEventListener listener){

        addValueEventListener(listener);

        return this;
    }



    public appFireBaseDB removeListener(){

        mUseDataChangeListener = false;

        if (mDBRef != null && mDataChangeListener != null){

            mDBRef.removeEventListener(mDataChangeListener);
        }

        return this;
    }



    public appFireBaseDB addValueEventListener(@NonNull ValueEventListener listener){

        mEventListeners.add(listener);

        return this;
    }



    public appFireBaseDB removeEventListener(@NonNull ValueEventListener listener){

        mEventListeners.remove(listener);

        if (mDBRef != null){

            mDBRef.removeEventListener(listener);
        }

        return this;
    }



    public appFireBaseDB update(@NonNull String description, @NonNull String data){

        mUseDataChangeListener = true;

        return this;
    }



    public appFireBaseDB add(@NonNull String description, @NonNull String data){

        mRecords.put(description, data);

        return this;
    }



    public void insert(@NonNull String tableName, @NonNull String description,
            @NonNull String data){

        String saveTable = mTableName;

        insert(description, data);

        mTableName = saveTable;
    }



    public void insert(@NonNull String description, @NonNull String data){

        if (description == null || data == null){ return; }

        mRecords.put(description, data);

        insert();
    }



    public void insert(){

        HashMap<String, Object> rec = new HashMap<>();

        rec.putAll(mRecords);

        // Don't want any duplicates.
        mRecords.clear();

        insert(rec);
    }



    public void insert(@NonNull final HashMap<String, Object> dataRec){

        if (mAppView == null){

            ErrorHandler.logError("appLog Class not initialized with AppView object!");

            return;
        }

        //        if (dataRec == null){ return; }

        getOnline();

        appAuth.logon(mAppView.getActivity(), new OnSuccessListener<AuthResult>(){

            // When successfully authenticated.
            public void onSuccess(AuthResult result){

                if (open()){

                    String userID = appAnalytics.getUserID();

                    HashMap<String, Object> rec = new HashMap<>();

                    rec.putAll(dataRec);

                    rec.put("UserID", userID);

                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.US);

                    rec.put("DateTime", df.format(new Date()));

                    rec.put("Country", Locale.getDefault().getDisplayCountry());

                    insertRec(rec);

                    close();
                }
            }
        });
    }



    private void getOnline(){

        // Don't contact Firebase until you have to.
        if (mDB == null){

            mDB = dbFirebase.getInstance(mAppView);

            appAuth.onCreate(); //(mAuthListener);

            // Important now to be 'online' for the duration of the app's lifecycle.
            mDB.goOnline();
        }
    }



    private boolean open(){

        try{

            if (mDBRef == null){

                String tableName = appAnalytics.getUserID();

                int length = tableName.length();

                int cnt = length < 16 ? length : 16;

                tableName = mTableName + "_" + tableName.substring(length - cnt, length);

                mDBRef = mDB.getReference().child(mTableName).child(tableName);

                mDataChangeListener = new dataChange();

                if (mUseDataChangeListener){

                    mDBRef.addValueEventListener(mDataChangeListener);
                }

                for (ValueEventListener listener : mEventListeners){

                    // TODO I wonder if they're stored in a HashSet, and that this actually works.
                    mDBRef.addValueEventListener(listener);
                }
            }
        }catch (Exception ex){

            mDBRef = null;

            ErrorHandler.logError(ex);
        }
        return mDBRef != null;
    }



    private String insertRec(HashMap<String, Object> rec){

        String key;

        try{

            key = mDBRef.push().getKey();

            Map<String, Object> childUpdates = new HashMap<>();

            childUpdates.put(key, rec);

            mDataChangeListener.mIgnoreTrigger = true;

            mDBRef.updateChildren(childUpdates);

        }catch (Exception ex){

            mDataChangeListener.mIgnoreTrigger = false;

            key = "";
        }

        return key;
    }



    private void close(){

        mDBRef.removeEventListener(mDataChangeListener);

        for (ValueEventListener listener : mEventListeners){

            mDBRef.removeEventListener(listener);

            mEventListeners.remove(listener);
        }

        mDBRef = null;

        mEventListeners = new HashSet<>();
    }

    private static class AuthListener implements FirebaseAuth.AuthStateListener{

        private static boolean mFirstCall = true;



        public void onAuthStateChanged(@NonNull FirebaseAuth auth){

            if (mFirstCall){

                mFirstCall = false;

                return;
            }

            // No Internet connection
            if (!App.NoConnectivity().isEmpty()){

                return;
            }

            FirebaseUser user = auth.getCurrentUser();

            // Sign in didn't fail
            if (user != null){

                // Makes this a one time trigger
                auth.removeAuthStateListener(this);
            }
        }
    }


    private static class AuthFailure implements OnFailureListener{

        public void onFailure(@NonNull Exception ex){

            if (ex.getMessage().contains("unreachable host")){

                // No Internet connection
                int breakpoint = 0;
            }else{

            }
        }
    }


    private class dataChange implements ValueEventListener{

        boolean mIgnoreTrigger = false;

        private ArrayList<HashMap<String, Object>> mDataArrayList;



        private dataChange(){

        }



        @Override
        public void onDataChange(DataSnapshot snapshot){

            if (snapshot == null){

                return;
            }

            // If these were my own changes ignore.
            if (mIgnoreTrigger){

                mIgnoreTrigger = false;

                return;
            }

            mDataArrayList = mDB.recArrayList(snapshot);
        }



        @Override
        public void onCancelled(DatabaseError databaseError){

//            String error = databaseError.getMessage();
        }
    }
}
