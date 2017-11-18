package com.andrioussolutions.utils;

import android.content.res.Resources;
import android.os.Build;

import java.util.Comparator;
import java.util.Currency;
import java.util.Locale;
import java.util.SortedMap;
import java.util.TreeMap;
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
 * Created  12/11/2016.
 */

public class Intl{


    private static SortedMap<Currency, Locale> currencyLocaleMap;

    private static String[] mCurrencyCodes;

    private static Locale mDefaultLocale;

    // TODO Yeah, I don't want this in a static clause. Call it only when it is need.
    static{

        currencyLocaleMap = new TreeMap<>(new Comparator<Currency>(){

            public int compare(Currency c1, Currency c2){

                return c1.getCurrencyCode().compareTo(c2.getCurrencyCode());
            }
        });

        for (Locale locale : Locale.getAvailableLocales()){

            try{

                Currency currency = Currency.getInstance(locale);

                if (!currencyLocaleMap.containsKey(currency)){

                    currencyLocaleMap.put(currency, locale);
                }

            }catch (IllegalArgumentException ex){
                // Skip such locales that invoke an error. They are no country or not supported by the device.
                continue;

            }catch (Exception ex){
                // Skip such locales that invoke an error. They are no country or not supported by the device.
                continue;
            }
        }
    }

    public static SortedMap<Currency, Locale> getCurrencyLocaleMap(){

        return currencyLocaleMap;
    }



    public static Locale getCurrencyLocale(String currencyCode){

        Locale location;

        try{

            Currency currency = Currency.getInstance(currencyCode);

            location = currencyLocaleMap.get(currency);

        }catch (Exception ex){

            location = getDefaultLocale();
        }

        return location;
    }



    public static String getCurrencySymbol(){

        return getCurrencySymbol(getCurrencyInstance(getDefaultLocale()).getCurrencyCode());
    }



    public static String getCurrencySymbol(Locale location){

        return getCurrencySymbol(getCurrencyInstance(location).getCurrencyCode());
    }



    public static String getCurrencySymbol(String currencyCode){

        String symbol;

        try{

            Currency currency = Currency.getInstance(currencyCode);

            symbol = currency.getSymbol(currencyLocaleMap.get(currency));

        }catch (Exception ex){

            symbol = "";
        }
        return symbol;
    }



    public static String[] getCurrencySymbols(){

        SortedMap<Currency, Locale> locales = Intl.getCurrencyLocaleMap();

        String[] currencies = new String[locales.values().toArray().length];

        int cnt = 0;

        for (Currency currency : locales.keySet()){

            currencies[cnt] = Intl.getCurrencySymbol(currency.getCurrencyCode());

            cnt++;
        }
        return currencies;
    }



    public static String getCurrencyCode(){

        return getCurrencyInstance(getDefaultLocale()).getCurrencyCode();
    }



    public static String[] getCurrencyCodes(){

        if (mCurrencyCodes != null){

            return mCurrencyCodes;
        }

        SortedMap<Currency, Locale> locales = Intl.getCurrencyLocaleMap();

        mCurrencyCodes = new String[locales.values().toArray().length];

        int cnt = 0;

        for (Currency currency : locales.keySet()){

            mCurrencyCodes[cnt] = currency.getCurrencyCode();

            cnt++;
        }
        return mCurrencyCodes;
    }



    public static Locale getDefaultLocale(){

        if (mDefaultLocale != null){

            return mDefaultLocale;
        }

        if (mDefaultLocale == null){

            String language = System.getProperty("user.language", "en");

            String region = System.getProperty("user.region", "US");

            String variant = System.getProperty("user.variant", "");

            mDefaultLocale = new Locale(language, region, variant);
        }

        if (mDefaultLocale == null){

            mDefaultLocale = Resources.getSystem().getConfiguration().locale;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){

            String languageTag = System.getProperty("user.locale", "");

            if (!languageTag.isEmpty()){

                mDefaultLocale = Locale.forLanguageTag(languageTag);
            }
        }

        return mDefaultLocale;
    }



    public static void onDestroy(){

        currencyLocaleMap = null;

        mCurrencyCodes = null;

        mDefaultLocale = null;
    }



    public static Currency getCurrencyInstance(Locale local){

        Currency currency;

        try{

            currency = Currency.getInstance(local);

        }catch (Exception ex){

            currency = Currency.getInstance("XXX");
        }

        return currency;
    }
}
