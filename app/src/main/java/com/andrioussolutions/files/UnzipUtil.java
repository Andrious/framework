package com.andrioussolutions.files;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
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
 * Created  09/10/2010.
 */
public class UnzipUtil
{
    private String zipFile;
    private String location;

    public UnzipUtil(String zipFile, String location)
    {
        this.zipFile = zipFile;
        this.location = location;

        dirChecker("");
    }

    public void unzip()
    {
        try
        {
            FileInputStream fin = new FileInputStream(zipFile);
            ZipInputStream zin = new ZipInputStream(fin);
            ZipEntry ze;
            while ((ze = zin.getNextEntry()) != null)
            {
                Log.v("Decompress", "Unzipping " + ze.getName());

                if(ze.isDirectory())
                {
                    dirChecker(ze.getName());
                }
                else
                {
                    FileOutputStream fout = new FileOutputStream(location + ze.getName());

                    byte[] buffer = new byte[8192];
                    int len;
                    while ((len = zin.read(buffer)) != -1)
                    {
                        fout.write(buffer, 0, len);
                    }
                    fout.close();

                    zin.closeEntry();

                }

            }
            zin.close();
        }
        catch(Exception e)
        {
            Log.e("Decompress", "unzip", e);
        }

    }

    private void dirChecker(String dir)
    {
        File f = new File(location + dir);
        if(!f.isDirectory())
        {
            f.mkdirs();
        }
    }
}
