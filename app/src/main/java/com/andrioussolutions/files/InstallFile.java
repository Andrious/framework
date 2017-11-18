package com.andrioussolutions.files;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.UUID;
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
 * Created  2/13/2017.
 */

public class InstallFile{

    private static final String FILE_NAME = ".install";

    private static String sID = null;



    public synchronized static String id(Context context){

        if (sID != null){ return sID; }

        File installFile = new File(context.getFilesDir(), FILE_NAME);

        try{

            if (!installFile.exists()){

                writeInstallationFile(installFile);
            }

            sID = readInstallationFile(installFile);

        }catch (Exception e){

            sID = "";
        }

        return sID;
    }



    private static String readInstallationFile(File installFile) throws IOException{

        RandomAccessFile file = new RandomAccessFile(installFile, "r");

        byte[] bytes = new byte[(int) file.length()];

        file.readFully(bytes);

        file.close();

        return new String(bytes);
    }



    private static void writeInstallationFile(File installFile) throws IOException{

        FileOutputStream out = new FileOutputStream(installFile);

        String id = UUID.randomUUID().toString();

        out.write(id.getBytes());

        out.close();
    }
}
