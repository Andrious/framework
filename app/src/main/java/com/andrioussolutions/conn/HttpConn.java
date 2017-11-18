package com.andrioussolutions.conn;

import com.andrioussolutions.frmwrk.App;
import com.andrioussolutions.frmwrk.settings.appSettings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
/**
 * Copyright  2017  Andrious Solutions Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 *
 * Created 1/9/2017.
 */

public class HttpConn{


    private static String mErrorMsg = "";




    public static InputStreamReader urlStream(String url){

        InputStreamReader input = null;

        try{

            input = getInputStream(url);

        }catch (Exception ex){

            collectError(url, ex);
        }
        return input;
    }




    private static InputStreamReader getInputStream(String urlString) throws IOException{

        HttpURLConnection conn = urlConnect(urlString);

        return new InputStreamReader(conn.getInputStream());
    }




    public static String urlResponse(String url){

        String response;

        try{

            response = getInputString(url);

        }catch (Exception ex){

            response = "";

            collectError(url, ex);
        }
        return response;
    }




    private static String getInputString(String urlString) throws IOException{

        HttpURLConnection conn = urlConnect(urlString);

        return streamToString(conn.getInputStream());
    }




    private static HttpURLConnection urlConnect(String urlString) throws IOException{

        URL url = new URL(urlString);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");

        conn.setRequestProperty("Accept", "application/json");

        int response = conn.getResponseCode();

        if (response != HttpURLConnection.HTTP_OK){

            if (!handleResponse(response, conn)){

                throw new RuntimeException("Failed : HTTP error code : " + response);
            }
        }

        return conn;
    }




    private static String streamToString(InputStream is) throws IOException{

        StringBuilder sb = new StringBuilder();

        BufferedReader rd = new BufferedReader(new InputStreamReader(is));

        String line;

        while ((line = rd.readLine()) != null){

            sb.append(line);
        }
        return sb.toString();
    }




    public static String getURL(String key, String defaultURL){

        String urlString = appSettings.get(key, defaultURL);

        return urlString;
    }




    private static boolean handleResponse(int response, HttpURLConnection conn){

        if (response == HttpURLConnection.HTTP_OK){

            return true;
        }

        if (response == HttpURLConnection.HTTP_MOVED_TEMP
                || response == HttpURLConnection.HTTP_MOVED_PERM
                || response == HttpURLConnection.HTTP_SEE_OTHER){

            String newURL = conn.getHeaderField("Location");

            conn.disconnect();

            try{

                // get redirect url from "location" header field and open a new connection again
                conn = urlConnect(newURL);

                return true;

            }catch (IOException ex){

                throw new RuntimeException(ex);
            }
        }

        // Nothing to be done. Can't go any further.
        return false;
    }




    private static void collectError(Exception ex){

        mErrorMsg = App.NoConnectivity();

        if (mErrorMsg.isEmpty()){

            String msg = ex.getMessage();

            int index;

            if (msg == null){

                index = 0;
            }else{

                index = msg.indexOf(":");
            }

            if (index > 0){

                mErrorMsg = msg.substring(0, index);

                if (mErrorMsg.indexOf("resolve") > 0 || mErrorMsg.indexOf("failed to connect") > 0){

                    mErrorMsg += "\nTry a different Exchange";
                }else{

                    mErrorMsg = msg;
                }
            }else{

                mErrorMsg = "Connection Timeout!"; //ex.getMessage();
            }
        }
    }




    private static void collectError(String msg, Exception ex){

        collectError(ex);

        String line = msg.trim();

        if (!line.isEmpty()){

            mErrorMsg = line + "\n" + mErrorMsg;
        }
    }




    // Determine if an error had occured.
    public static boolean hasError(){

        return !mErrorMsg.isEmpty();
    }




    // Remove the recorded error.
    public static String clearError(){

        return getError();
    }




    // Get the last error message
    public static String getError(){

        String msg = mErrorMsg;

        mErrorMsg = "";

        return msg;
    }




    // Get the last error message
    public static String setError(String error){

        if (error == null || error.isEmpty()) return "";

        mErrorMsg = error.trim();

        return mErrorMsg;
    }




    public static void onDestroy(){

    }
}
