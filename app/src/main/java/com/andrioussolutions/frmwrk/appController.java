package com.andrioussolutions.frmwrk;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.vending.licensing.LicenseCheckerCallback;
import com.google.firebase.crash.FirebaseCrash;

import com.andrioussolutions.frmwrk.db.dbDataRecords;
import com.andrioussolutions.frmwrk.settings.appPreferences;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.util.Log;
import android.view.MenuItem;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.security.cert.Certificate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
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
 * *
 *
 *
 * Created  2015-03-10.
 */

abstract public class appController extends appActivity{

    protected App mApp;

    protected appView mAppView;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient mClient;

    private appModel mAppModel;

    private appPreferences mPreferences;

    private dbDataRecords mdbRecords;

    private FragmentManager mFragMngr;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        mdbRecords = setRecords();

        mAppModel = new appModel(this, mdbRecords);

        // A data table is specified. If not, then there is no database in this application.
        if (!mAppModel.dbName().isEmpty() && !mAppModel.open()){

            //TODO This is not working fully. Closing the app.
            finish();
        }

        setPreferences(setPreferences());

        mApp = new App(this);

        App.setPreferences(getPreferences());

        mFragMngr = getFragmentManager();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mClient = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        mAppView = setView();
  }

    abstract protected appView setView();

    abstract protected dbDataRecords setRecords();

    abstract protected appPreferences setPreferences();

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        return mAppView.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart(){
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mClient.connect();

        // Use justCreated() here for that one special case
        // Use wasStopped() here
        // Use wasRecreated() here

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "appController Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://" + this.getPackageName() + "/http/host/path")
        );
        AppIndex.AppIndexApi.start(mClient, viewAction);

        mAppView.onStart();
    }

    /*
     * Called after onStart()
     *
     */
    protected void onResume(){
        super.onResume();
        mAppView.onResume();
    }

    protected void onPause(){
        super.onPause();
        mAppView.onPause();
    }

    /*
     * Only fired after a 'stopped' state.
     *
     */
    @Override
    protected void onRestart(){
        super.onRestart();

        mAppView.onRestart();

        mAppModel.onRestart();
    }

    /*
     * The system retains the Activity instance in system memory when it is stopped,
     * Here, CPU-intensive work to save data to a database.
     * The system might destroy the instance if it needs to recover system memory.
     * The system might even kill your app process without calling onDestroy()
     * Even if the system destroys the activity, the state of the View objects is still retained in a Bundle
     *
     */
    @Override
    protected void onStop(){
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "appController Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://" + this.getPackageName() + "/http/host/path")
        );
        AppIndex.AppIndexApi.end(mClient, viewAction);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mClient.disconnect();

        mAppView.onStop();

        mAppModel.onStop();
    }

    /*
 * Called after onPause()
 * Override to save additional data about your activity's state.
 * The system might destroy this activity while waiting in the back stack.
 *
 */
    @Override
    protected void onSaveInstanceState(Bundle outState){
        // Save the user's interface state
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(outState);

        mAppView.onSaveInstanceState(outState);
    }

    /*
     * Most apps don't need to implement this method because local class references are destroyed
     * with the activity and your activity should perform most cleanup during onPause() and onStop().
     * However, if your activity includes background threads that you created during onCreate()
     * or other long-running resources that could potentially leak memory if not properly closed,
     * you should kill them during onDestroy().
     *
     * You might call finish() from within onCreate() to destroy the activity.
     * In that case, the system immediately calls onDestroy() without calling
     * any of the other lifecycle methods.
     *
     */
    @Override
    protected void onDestroy(){
        super.onDestroy();

        // Application object
        mApp.onDestroy();

        mApp = null;

        mAppView.onDestroy();

        mAppView = null;

        mAppModel.onDestroy();

        mAppModel = null;

        mPreferences.onDestroy();

        mPreferences = null;

        mFragMngr = null;
    }

    public LicenseCheckerCallback LicenseCallback(){
        return new LicenseCheckerCallback(){

            @Override
            public void allow(int reason){

                int breakpoint = 0;
            }

            @Override
            public void dontAllow(int reason){

                int breakpoint = 0;
            }

            @Override
            public void applicationError(int errorCode){}
        };
    }

    public boolean open(){

        return mAppModel.open();
    }

    public void close(){

        mAppModel.close();
    }

    public appView getView(){

        return mAppView;
    }

    protected appModel getModel(){

        return mAppModel;
    }

    public appPreferences getPreferences(){

        return mPreferences;
    }

    public void setPreferences(appPreferences pref){

        mPreferences = pref;
    }

    // Called the moment when an applications preference setting is changed.
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key){

        return;
    }


    // Called the moment when an application preference setting is clicked.
    // Override if you want to.
    @Override
    public boolean onPreferenceClick(Preference preference){

        return true;
    }

    // Called when a application preference setting is changed.
    // Override if you want to.
    @Override
    public  boolean onPreferenceChange(Preference preference, Object newValue){

        return mAppView.onPreferenceChange(preference, newValue);
    }


    // Show 'deleted' records or not.
    public boolean showDeleted(){

        return mAppModel.showDeleted();
    }

    // If the manifest has the debug set.
    public final boolean DebugFlagSet(){

        return 0 != (this.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE);
    }

    public void logOnFire(String message){

        // Don't if in the IDE.
        if (App.inDebugger()){

            return;
        }

//        String deviceID = App.getDeviceID();
//
//        // Don't if it's my phone
//        if(deviceID.equals("ffffffff-c14e-f01a-1494-532f35918ecc")){
//
//            return;
//        }

        if (!App.NoConnectivity().isEmpty()){

            return;
        }

        FirebaseCrash.report(new Throwable(message));
    }

    private boolean createDataFile(File file){

        try{

            // Can't delete the old one?? Use it!
            if (file.exists() && !file.delete()){

                return true;
            }

            if (!file.createNewFile()){

                return false;
            }

//            CSVWriter csvWrite = new CSVWriter(new FileWriter(file), ',', '"', '\n');
//            CSVWriter csvWrite = new CSVWriter(new FileWriter(file), ',', ' ', '\n');

            CSVWriter csvWrite = new CSVWriter(new FileWriter(file), CSVWriter.DEFAULT_SEPARATOR,
                    CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, "\r\n");

            Cursor curCSV = mAppModel.getRecs();

            csvWrite.writeNext(curCSV.getColumnNames());

            String[] delimit = {","};

            int columnCount = curCSV.getColumnCount();

            String[] arrStr = new String[columnCount];

            while (curCSV.moveToNext()){

                csvWrite.writeNext(delimit);

                for (int i = 0; i < columnCount; i++){

                    arrStr[i] = curCSV.getString(i);
                }

                csvWrite.writeNext(arrStr);
            }

            // Catch IOException
            csvWrite.close();

            curCSV.close();

            return true;

        }catch (Exception ex){

            Log.e(App.getPackageName(), ex.getMessage(), ex);

            return false;
        }
    }

    private boolean exportData(){

        File file = exportDataFile();

        return createDataFile(file);
    }

    private boolean importData(){

        if (!open()){

            return false;
        }

        try{

            CSVReader reader = new CSVReader(new FileReader(exportDataFile()));

            String[] columnLine;

            columnLine = reader.readNext();

            if (columnLine == null){
                return false;
            }

            String[] dataLine;

            while ((dataLine = reader.readNext()) != null){

                for (int i = 0; i < dataLine.length; i++){

                    mAppModel.importRec(columnLine[i], dataLine[i]);
                }

                mAppModel.importRec();
            }

        }catch (Exception ex){

            Log.e(this.getClass().getSimpleName(), ex.getMessage(), ex);

            return false;
        }
        return true;
    }

    // The application's  private directory.
    private File appDir(){

        return App.getFilesDir(this);
    }

    private File exportDir(){

        File exportDir = new File(Environment.getExternalStorageDirectory(), "");

        if (!exportDir.exists() && !exportDir.mkdirs()){

            exportDir = this.getExternalCacheDir();
        }

        return exportDir;
    }

    private File exportDataFile(){

        File exportDir = exportDir();

        // TODO Maybe append today's date on the file name.
        return new File(exportDir, "workingmemory.csv");
    }

    public String appVersion(){

        Context context = this.getApplicationContext();

        PackageManager packageManager = context.getPackageManager();

        String packageName = context.getPackageName();

        String version;

        try{

            version = packageManager.getPackageInfo(packageName, 0).versionName;

        }catch (PackageManager.NameNotFoundException ex){

            version = "not available";
        }

        return version;
    }


    public boolean runPreference(appPreferences pref){

        final OnBackPressedListener listener = pref.OnBackPressedListener();

        // if there was a listener assigned to this preference object.
        if(listener != null){

            addOnBackPressedListener(new OnBackPressedListener() {

                public void onBackPressed(){

                    listener.onBackPressed();

                    removeOnBackPressedListener(this);
                }
            });
        }

        return runFragment(pref, android.R.id.content);
    }


    public boolean runFragment(Fragment newFragment, int layoutID){

        try{

            // Check to see it is not already open.
            String fragName = newFragment.getClass().getSimpleName();

            if (!mFragMngr.popBackStackImmediate(fragName, 0)
                    && mFragMngr.findFragmentByTag(fragName) == null){

                FragmentTransaction ft = mFragMngr
                        .beginTransaction()
                        .addToBackStack(fragName)
                        .replace(layoutID, newFragment, fragName);

                ft.commit();
            }

            return true;

        }catch (RuntimeException ex){

            return false;
        }
    }

    public Method getPMmethod(){

        Method mth;

        try{

            mth = getClass().getMethod(App.getPM());

        }catch (Exception ex){

            mth = null;
        }
        return mth;
    }

    // Return the Package Manager
    // Help gets the Signature
    public Object getPM(){

        Object obj;

        try{

            obj = getPMmethod().invoke(this);

        }catch (Exception ex){

            obj = null;
        }
        return obj;
    }

    // Returns Application's Signature
    public int getSignature(){

        return ((PackageInfo) mAppView.getPI()).signatures[0].hashCode();
    }

    // Return the Application's certification
    public Certificate[] GetApplicationCertifications(){

        InputStream is = null;

        try{

            JarFile jf = new JarFile(getApplicationInfo().sourceDir);

            JarEntry je = jf.getJarEntry("class.dex");

            is = jf.getInputStream(je);

            while ((is.read() != -1)){}

            return je.getCertificates();

        }catch (Exception ex){
        }finally{
            if (is != null){try{is.close();}catch (Exception ex){}}
        }
        return null;
    }

    public boolean licenced(){

        return mAppView.licenced();
    }

    public boolean licenceError(){

        return mAppView.licenceError();
    }
}
