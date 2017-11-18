package com.andrioussolutions.utils;

import android.util.Patterns;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 *  Copyright  2016  Andrious Solutions Ltd.
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
 * Created 11/16/2016.
 */

public class uri{

    public static String getDomainName(String url){

        URI uri;

        try{

            uri = new URI(url);

        }catch(URISyntaxException ex){

            uri = null;
        }

        String domain;

        if(uri == null){

            domain = "";
        }else{

            domain = uri.getHost();

            if(domain == null){

                domain = uri.toString();

                if (domain != null && !domain.isEmpty()){

                    Pattern p = Patterns.TOP_LEVEL_DOMAIN;

                    Matcher m = p.matcher(domain);

                    if (m.find()){ // Find each match in turn

                        domain = m.group(0); // Access a submatch groupthis.
                    }
                }
            }

            if(domain == null){

                domain = "";

            }else if (domain.startsWith("www.")){

                domain = domain.substring(4);
            }
        }

        return domain;
    }



}
