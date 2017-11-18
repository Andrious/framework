package com.andrioussolutions.frmwrk;

import com.andrioussolutions.frmwrk.db.dbDataRecords;
import com.andrioussolutions.frmwrk.db.dbHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
/**
 *  Copyright  2015  Andrious Solutions Ltd.
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
 * Created  2015-02-12.
 */
public class appModel{

    // Variable to hold the database instance
    private dbHelper mDBHelper;

    // Variable to hold the database records class
    private dbDataRecords mDataRecords;

    // An insert of a record will produce the rowid assigned the last record inserted.
    private long mLastRowID = 0;

    private boolean mShowDeleted;


    public appModel(Context controller, dbDataRecords recs){

        mDataRecords = recs;

        // There's no database to work with anyway.
        if (mDataRecords.dbFile().isEmpty()) {

            return;
        }

        try{

            mDBHelper = new dbHelper(controller, recs, recs.dbFile(), null, recs.dbVersion(), null);

        }catch (IllegalArgumentException ex){

            mDBHelper = null;
        }
    }


    // Return the name of the data table.
    public String dbName(){

        return  mDataRecords.dbName();
    }



    public boolean open(){

        boolean opened = mDBHelper != null && mDBHelper.open().isOpen();

        if (opened){

            opened = mDBHelper.createCurrentRecs();

            if (!opened){

                mDBHelper.close();
            }
        }

        return opened;
    }


    public boolean isOpen(){

        return mDBHelper != null && mDBHelper.isOpen();
    }


    public void close(){

        if (mDBHelper != null){

            mDBHelper.close();
        }
    }


    public long getLastRowID(){

        if (mLastRowID == 0){

            mLastRowID = mDBHelper.getLastRowID();
        }

        return mLastRowID;
    }


    public boolean save(dataItem itemToDo){

        return mDBHelper.save(itemToDo);
    }


    public boolean delete(dataItem itemToDo){

        itemToDo.isDeleted(true);

        return delete(itemToDo.getId());
    }


    public boolean delete(long id){

        return mDBHelper.markRec(id);
    }


    public boolean trueDelete(dataItem itemToDo){

        return mDBHelper.deleteRec(itemToDo.getId()) > 0;
    }


    public boolean showDeleted(){

        return mShowDeleted;
    }


    private boolean showDeleted(boolean showDeleted){

        mShowDeleted = showDeleted;

        return mShowDeleted;
    }

//    ArrayList<dataItem> ToDoList() {
//
//        // Determine if deleted records are to be displayed.
//        mDBHelper.showDeleted(showDeleted(appSettings.getBoolean("show_deleted", false)));
//
//        return mDBHelper.ToDoList();
//    }


    // Returns the database
    public SQLiteDatabase getDatabase(){

        return mDBHelper.getDatabase();
    }



    // Returns the records
    public Cursor getRecs(){

        return mDBHelper.getRecs();
    }



    public Cursor getCurrentRecs(){

        return mDBHelper.getCurrentRecs();
    }



    public Cursor getRecs(String whereClause){

        return mDBHelper.getRecs(whereClause);
    }



    public Cursor getDeletedRecs(){

        return mDBHelper.getDeletedRecs();
    }



    public void importRec(String columnName, String value){

        mDBHelper.bindRecValues(columnName, value);
    }



    boolean importRec(){

        return mDBHelper.importRec();
    }



    public boolean insertIfNewRec(){

        boolean newRec = mDBHelper.ifNewRec();

        if (newRec){

            newRec = mDBHelper.importRec();
        }

        return newRec;
    }



    public dbHelper getDBHelper(){

        return mDBHelper;
    }



    public dbDataRecords getDataRec(){

        return mDataRecords;
    }



    public dbDataRecords newRec(){

       return mDataRecords.newRec();
    }



    // The App might get destroyed with calling onDestroy, and so take no chances.
    protected void onStop(){

        // close the db connection...
        close();
    }



    protected void onRestart(){

        // db likely closed.
        open();
    }



    protected void onDestroy(){

        if(mDataRecords != null){

            mDataRecords.onDestroy();

            mDataRecords = null;
        }

        if (mDBHelper != null){

            mDBHelper.onDestroy();

            mDBHelper = null;
        }
    }


    abstract public class dataItem{

        private long mId = -1; // unique ID for the alarm, only one alarm can be set for each item.

        private boolean mNewItem = false;

        private boolean mDeleted = false;


        abstract public String toString();


        public long getId(){

            return mId;
        }


        public void setId(long id){

            mId = id;
        }


        public void setId(String id){

            mId = Integer.parseInt(id);
        }


        public boolean newItem(boolean newItem){

            mNewItem = newItem;

            return mNewItem;
        }

        public boolean newItem(){

            return mNewItem;
        }


        public boolean isDeleted(){

            return mDeleted;
        }


        public boolean isDeleted(boolean deleted){

            mDeleted = deleted;

            return mDeleted;
        }

    }
}
