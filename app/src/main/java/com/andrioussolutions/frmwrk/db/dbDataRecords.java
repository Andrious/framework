package com.andrioussolutions.frmwrk.db;


import com.andrioussolutions.frmwrk.appModel;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
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
 * Created  8/10/2016.
 */
abstract public class dbDataRecords  implements dbDataRecordsInt{

    protected dbHelper mDbHelper;

    public static ContentValues VALUES;

    private long mId = -1; // unique ID for the alarm, only one alarm can be set for each item.

    private boolean mNewItem = false;

    private boolean mDeleted = false;

    private boolean mUseScript = false;



     public void setDbHelper(dbHelper helper){

        mDbHelper = helper;
    }

    public dbHelper getDbHelper(){

        return mDbHelper;
    }

    abstract public String dbName();

    // Should be greater than 0
    abstract public int dbVersion();


     abstract public ContentValues bindRecValues(appModel.dataItem itemToDo);


     abstract public void bindRecValues(String columnName, String value);


     abstract public boolean ifNewRec();



    // Called in getDatabaseLocked(boolean writable) when a database is being opened.
    public void onConfigure(dbHelper helper){

    }



    // Called in getWritableDatabase() when a database has just been opened..
    public void onOpen(dbHelper helper){

    }


    abstract public String DBKEY_FIELD();

    // Ported to the file, create.sql
    abstract public String DATABASE_CREATE();

    // SQL statement used to upgrade the database.
    abstract  public String ALTER_TABLE();

    // the method to be invoked when database corruption is detected.
    public void onCorruption(SQLiteDatabase db){

    }



    // Supply a new instance.
   abstract public dbDataRecords newRec();



    public long getId(){

        Object value = VALUES.get(DBKEY_FIELD());

        long id;

        if (value == null){

            id = -1;
        }else{

            id = ((Number) value).longValue();
        }

        return id;
    }


    public void setId(long id){

        mId = id;

        VALUES.put(DBKEY_FIELD(), id);
    }


    public void setId(String id){

        setId(Long.parseLong(id));
    }


    public boolean newItem(boolean newItem){

        mNewItem = newItem;

        return mNewItem;
    }


    public boolean newItem(){

        return mNewItem;
    }



    public boolean save(){

        boolean save = mDbHelper != null;

        if (save){

            save = mDbHelper.save(this);
        }

        return save;
    }



    public boolean isDeleted(){

        return mDeleted;
    }


    public boolean isDeleted(boolean deleted){

        mDeleted = deleted;

        return mDeleted;
    }


    public String DATABASE_NAME(){

        return dbName();
    }


    public String DATABASE_FILE(){

        return dbFile();
    }


    public String dbFile(){

        String file = dbName();
        return file.isEmpty() ? "" :  file + ".db";
    }


    public boolean useScript(){

        return mUseScript;
    }


    public boolean useScript(boolean use){

        mUseScript = use;

        return use;
    }


    public String SELECT_ALL(){

        return "SELECT " + DBKEY_FIELD() + " AS _id, * FROM " + DATABASE_NAME();
        //  + " ORDER BY ToDoDateTimeEpoch ASC"
    }


    public String DROP_TABLE(){

        return  "DROP TABLE IF EXISTS " + DATABASE_NAME();
    }


    public String CREATE_NOT_DELETED(){

        return  "CREATE TEMP VIEW IF NOT EXISTS temp.notdeleted AS SELECT " + DBKEY_FIELD()
                + " AS _id, * FROM " + DATABASE_NAME() + " WHERE deleted = 0";
    }


    public String CREATE_DELETED(){

        return  "CREATE TEMP VIEW IF NOT EXISTS temp.deleted AS SELECT " + DBKEY_FIELD()
                + " AS _id, * FROM " + DATABASE_NAME() + " WHERE deleted = 1";
    }


    public String DROP_DELETED(){

        return "DROP VIEW IF EXISTS temp.deleted";
    }


    public String SELECT_NOT_DELETED(){

        return "SELECT " + DBKEY_FIELD()+" AS _id, * FROM temp.notdeleted";
    }


    public String SELECT_DELETED(){

        return "SELECT " + DBKEY_FIELD() + " AS _id, * FROM temp.deleted";
    }


    public void onDestroy(){

        mDbHelper = null;
    }
}
