package com.andrioussolutions.frmwrk.db;

import com.gtfp.errorhandler.ErrorHandler;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
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
 * Created 3/5/2017.
 */


public class dbSQLiteHelper{


    public dbSQLiteHelper(@NonNull Context context, @NonNull String dbName, @NonNull int dbVersion, @NonNull String tableDef){

        if (dbName == null || dbName.isEmpty()){

            mDBName = "table";
        }else{

            mDBName = dbName.trim();
        }

        if(dbVersion >= 0){

            mDBVersion = 1;
        }else{

            mDBVersion = dbVersion;
        }

        if(tableDef == null || tableDef.isEmpty()){

            mSchema = "";
        }else{

            mSchema = tableDef.trim();
        }

//        mSQLDrop = "DROP TABLE IF EXISTS " + mDBName;

        mSQLHelper = new SQLHelper(context);
    }




    public dbSQLiteHelper open(){

        mSQLHelper.open();

        return this;
    }




    public boolean isOpen(){

        return mSQLHelper.isOpen();
    }




    public SQLiteDatabase getDatabase(){

        return mSQLHelper.getDatabase();
    }




    public dbSQLiteHelper close(){

        mSQLHelper.close();

        return this;
    }




    public String getName(){

        return mDBName;
    }




    public int getVersion(){

        return mDBVersion;
    }




    public void setOnOpenListener(OnOpenListener listener){

        mOnOpenListeners.add(listener);
    }




    public void setOnConfigureListener(OnConfigureListener listener){

        mOnConfigureListeners.add(listener);
    }




    private class SQLHelper extends SQLiteOpenHelper{



        SQLHelper(Context context){

            super(context,  mDBName + ".db", null, mDBVersion);

            mAssetManager = context.getAssets();
        }



        @Override
        public void onCreate(SQLiteDatabase db){

            try{

                db.execSQL("CREATE TABLE IF NOT EXISTS " + mDBName + mSchema);

            }catch (Exception ex){

                ErrorHandler.logError(ex);
            }
        }




        @Override
        public void onConfigure(SQLiteDatabase db){

            if (mOnConfigureListeners != null){

                for (OnConfigureListener listener : mOnConfigureListeners){

                    listener.onConfigure(db);
                }
            }
        }




        @Override
        public void onOpen(SQLiteDatabase db){

            if (mOnOpenListeners != null){

                for (OnOpenListener listener : mOnOpenListeners){

                    listener.onOpen(db);
                }
            }
        }




        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

            setTheGrade(db, oldVersion, newVersion);
        }



        public boolean currentRecs(){

            try{

                mDB.execSQL("SELECT  * FROM " + mDBName);

            }catch (SQLException ex){

                return false;
            }

            return true;
        }



        public SQLHelper open(){

            try{

                if (!isOpen()){

                    mDB = getWritableDatabase();
                }

            }catch (SQLException ex){

                if (mDB != null && mDB.isOpen()){

                    mDB.close();
                }

                if (mDB != null){

                    mDB = null;
                }
            }

            return this;
        }



        public boolean isOpen(){

            return mDB != null && mDB.isOpen();
        }



        public void close(){

            super.close();

            if (mDB != null){

                mDB.close();
            }

            // It's good to lose the reference here with the connection closed.
            mDB = null;

            if (mResultSet != null){

                mResultSet.close();
            }
            // This resource too. It could be huge!
            mResultSet = null;
        }



        public SQLiteDatabase getDatabase(){

            return mDB;
        }



        public Cursor runQuery(String sqlStmt){

            Cursor records;

            try{

                records = mDB.rawQuery(sqlStmt, null);

            }catch (RuntimeException ex){

                // If something goes wrong, return an empty cursor.
                records = new MatrixCursor(new String[]{"empty"});
            }

            return records;
        }



        private void setTheGrade(SQLiteDatabase db, int oldVersion, int newVersion){

            try{

                for (String sqlFile : dbSQLParser.list(SQL_DIR, mAssetManager)){

                    if (sqlFile.startsWith(UPGRADEFILE_PREFIX)){

                        int fileVersion = Integer.parseInt(
                                sqlFile.substring(UPGRADEFILE_PREFIX.length(),
                                        sqlFile.length() - UPGRADEFILE_SUFFIX.length()));

                        if (fileVersion > oldVersion && fileVersion <= newVersion){

                            execSqlFile(sqlFile, db);
                        }
                    }
                }
            }catch (IOException ex){

                ErrorHandler.logError(ex);

            }catch (RuntimeException ex){

                ErrorHandler.logError(ex);

                // Be sure to throw the exception back up to be handled there as well.
                throw ex;
            }
        }



        private void execSqlFile(String sqlFile, SQLiteDatabase db)
                throws SQLException, IOException{

            int line = 0;

            for (String sqlInstruction : dbSQLParser
                    .parseSqlFile(SQL_DIR + "/" + sqlFile, mAssetManager)){

                line = line + 1;

                if (line == 1 && sqlInstruction.substring(0, 7).equals("ATTACH ")){

                    db.endTransaction();
                }

                db.execSQL(sqlInstruction);
            }
        }

        private AssetManager mAssetManager;

    }

    public interface OnOpenListener{

        void onOpen(SQLiteDatabase db);
    }

    public interface OnConfigureListener{

        void onConfigure(SQLiteDatabase db);
    }

    private static final String SQL_DIR = "sql";

    private static final String UPGRADEFILE_PREFIX = "upgrade-";

    private static final String UPGRADEFILE_SUFFIX = ".sql";

    private final String mDBName;

    private final String mSchema;

    private int mDBVersion;

    private SQLiteDatabase mDB;

    private Cursor mResultSet;

    private SQLHelper mSQLHelper;

    private Set<OnOpenListener> mOnOpenListeners = new HashSet<>();

    private Set<OnConfigureListener> mOnConfigureListeners = new HashSet<>();
}