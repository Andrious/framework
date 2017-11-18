package com.andrioussolutions.frmwrk.db;

import com.andrioussolutions.frmwrk.appModel;

import android.content.ContentValues;
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
abstract public class dbRecordsImp implements dbRecValues {

    protected dbHelper mDbHelper;

    protected ContentValues mRecValues;


     public void setDbHelper(dbHelper helper){

        mDbHelper = helper;

        mRecValues = helper.recValues();
    }


    abstract public String dbName();


    public String dbFile(){
        String file = dbName();
        return file.isEmpty() ? "" :  file + ".db";
    }


    abstract public int dbVersion();


     abstract public ContentValues bindRecValues(appModel.dataItem itemToDo);


     abstract public void bindRecValues(String columnName, String value);


     abstract public boolean ifNewRec();


    abstract public String DBKEY_FIELD();

    // Ported to the file, create.sql
    abstract public String DATABASE_CREATE();

    // SQL statement used to upgrade the database.
    abstract  public String ALTER_TABLE();


    public String DATABASE_NAME(){

        return dbName();
    }


    public String DATABASE_FILE(){

        return dbFile();
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
