package com.andrioussolutions.db;

import com.google.android.gms.tasks.Tasks;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.andrioussolutions.frmwrk.appView;
import com.gtfp.errorhandler.ErrorHandler;

import android.content.ContentValues;
import android.content.Context;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
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
 * Created  2/18/2017.
 */

public class dbFirebase{

    private static dbFirebase mThis;

    private static appView mAppView;

    private static FirebaseDatabase mDatabase;

    private static FirebaseApp mApp;

    private static ArrayList<HashMap<String, String>> mDataArrayList;

    private Context mContext;

    private ContentValues mRecValues;



    private dbFirebase(Context context){

        mContext = context;

        // Stores the record's contents.
        mRecValues = new ContentValues();
    }



    public static dbFirebase getInstance(appView mVc){

        if (mThis == null){

            mAppView = mVc;

            mThis = new dbFirebase(mVc.getContext());

            mDatabase = FirebaseDatabase.getInstance();

            mApp = mDatabase.getApp();
        }

        return mThis;
    }



    private static ArrayList<HashMap<String, Object>> getRecList(DataSnapshot snapshot){

        ArrayList<HashMap<String, Object>> list = new ArrayList<>();

        if (snapshot == null){

            return list;
        }

        Tasks task;

        DesiredField annotation;

        Field[] fields = Tasks.class.getDeclaredFields();

        Method[] methods = Tasks.class.getDeclaredMethods();

        for (DataSnapshot shot : snapshot.getChildren()){

            try{

                task = shot.getValue(Tasks.class);

            }catch (Exception ex){

                ErrorHandler.logError(ex);

                continue;
            }

            HashMap<String, Object> row = new HashMap<>();

            row.put("recKeyID", shot.getKey());

            for (int i = 0; i < fields.length; i++){

                annotation = fields[i].getAnnotation(DesiredField.class);

                if (annotation == null){

                    continue;
                }

                try{

                    switch (methods[i].getReturnType().getName()){
                        case "long":

                            row.put(fields[i].getName(),
                                    Long.valueOf(methods[i].invoke(task).toString()));

                            break;

                        case "int":

                            row.put(fields[i].getName(),
                                    Integer.valueOf(methods[i].invoke(task).toString()));

                            break;
                        default:

                            row.put(fields[i].getName(), methods[i].invoke(task));
                    }
                }catch (Exception ex){

                    ErrorHandler.logError(ex);
                }
            }

            list.add(row);
        }

        return list;
    }



    public void goOnline(){

        mDatabase.goOnline();
    }



    public void goOffline(){

        mDatabase.getReference().goOffline();

        mDatabase.goOffline();
    }



    public FirebaseApp getApp(){

        return mDatabase.getApp();
    }



    public DatabaseReference getReference(){

        return mDatabase.getReference();
    }



    public DatabaseReference getReference(String table){

        return mDatabase.getReference(table);
    }



    public ArrayList<HashMap<String, String>> getDataArrayList(){

        return mDataArrayList;
    }



//    public ArrayList<HashMap<String, String>> recArrayList(DataSnapshot snapshot,
//            dbFields fieldsObj){
//
//        ArrayList<HashMap<String, String>> list = new ArrayList<>();
//
//        if (snapshot == null){
//
//            return list;
//        }
//
//        if (fieldsObj == null){
//
//            return list;
//        }
//
//        DesiredField annotation;
//
//        Field[] fields = fieldsObj.getDeclaredFields();
//
//        Method[] methods = fieldsObj.getDeclaredMethods();
//
//        dbFields fldObj;
//
//        String value;
//
//        String fldName;
//
//        long number;
//
//        int integer;
//
//        boolean skip = false;
//
//        for (DataSnapshot shot : snapshot.getChildren()){
//
//            try{
//
//                fldObj = shot.getValue(fieldsObj.getClass());
//
//            }catch (Exception ex){
//
//                ErrorHandler.logError(ex);
//
//                continue;
//            }
//
//            HashMap<String, String> row = new HashMap<>();
//
//            row.put("recKeyID", shot.getKey());
//
//            for (int i = 0; i < fields.length; i++){
//
//                annotation = fields[i].getAnnotation(DesiredField.class);
//
//                if (annotation == null){
//
//                    continue;
//                }
//
//                try{
//
//                    switch (methods[i].getReturnType().getName()){
//                        case "long":
//
//                            number = (Long) methods[i].invoke(fldObj);
//
//                            value = Long.toString(number);
//
//                            break;
//
//                        case "int":
//
//                            integer = (Integer) methods[i].invoke(fldObj);
//
//                            value = String.valueOf(integer);
//
//                            break;
//                        default:
//
//                            value = (String) methods[i].invoke(fldObj);
//                    }
//
//                    fldName = fields[i].getName();
//
//                    if (!fieldsObj.showDeleted() && fldName.equals("Deleted") && value.equals("1")){
//
//                        skip = true;
//
//                        break;
//                    }
//                }catch (Exception ex){
//
//                    ErrorHandler.logError(ex);
//
//                    continue;
//                }
//
//                row.put(fldName, value);
//            }
//
//            if (skip){
//
//                skip = false;
//
//                continue;
//            }
//
//            list.add(row);
//        }
//
//        return list;
//    }



    public ArrayList<HashMap<String, Object>> recArrayList(DataSnapshot snapshot){

        ArrayList<HashMap<String, Object>> list = new ArrayList<>();

        if (snapshot == null){

            return list;
        }

        // This is awesome! No middle man!
        Object fieldsObj = new Object();

        HashMap fldObj;

        for (DataSnapshot shot : snapshot.getChildren()){

            try{

                fldObj = (HashMap)shot.getValue(fieldsObj.getClass());

            }catch (Exception ex){

                ErrorHandler.logError(ex);

                continue;
            }

            fldObj.put("recKeyID", shot.getKey());

            list.add(fldObj);
        }

        return list;
    }



    public void onDestroy(){

        goOffline();

        mAppView = null;

        mThis = null;

        mContext = null;

        mDatabase = null;

        mApp = null;
    }

    @Target(value = ElementType.FIELD)
    @Retention(value = RetentionPolicy.RUNTIME)
    public @interface DesiredField{}

}
