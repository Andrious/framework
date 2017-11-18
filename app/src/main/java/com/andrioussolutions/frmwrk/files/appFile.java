package com.andrioussolutions.frmwrk.files;

import com.gtfp.errorhandler.ErrorHandler;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
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
 * Created  3/8/2017.
 */

public class appFile{

    private static Context mContext;



    private appFile(){

    }



    public static void onCreate(@NonNull Context context){

        mContext = context;
    }



    public static String read(String fileName){

        String string = "";

        FileInputStream in = FileInputStream(fileName);

        if(in == null) return string;

        InputStreamReader reader = new InputStreamReader(in);

        BufferedReader buffer = new BufferedReader(reader);

        StringBuilder sb = new StringBuilder();

        try{

            while ((string = buffer.readLine()) != null){

                sb.append(string);

                sb.append('\n');
            }

            string = new String(sb);

        }catch (Exception ex){

            string = "";

            ErrorHandler.logError(ex);
        }finally{

            try{ reader.close(); }catch(Exception ex){}
        }

        return string;
    }





    public static FileInputStream FileInputStream(String fileName){

        FileInputStream input = null;

        if (fileName == null || fileName.isEmpty()){ return input; }

        String name = fileName.trim();

        try{

            input = mContext.openFileInput(name);

        }catch (Exception ex){

            input = null;
        }

        return input;
    }





    public static boolean write(String fileName, String data, int mode){

        if (fileName == null || fileName.isEmpty()){ return false; }

        String name = fileName.trim();

        boolean write = true;

        FileOutputStream fos;

        try{

            fos = mContext.openFileOutput(name, mode);

            fos.write(data.getBytes());

            fos.close();

        }catch (Exception ex){

            write = false;

            ErrorHandler.logError(ex);
        }

        return write;
    }



    public static boolean write(String fileName, String data){

        return write(fileName, data, Context.MODE_PRIVATE);
    }



    public static boolean append(String fileName, String data){

        return write(fileName, data, Context.MODE_APPEND);
    }




    public static void onDestroy(){

        mContext = null;
    }
}
