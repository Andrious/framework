package com.andrioussolutions.ui;

import com.andrioussolutions.frmwrk.settings.appSettings;

import android.content.Context;
import android.preference.Preference;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
/**
 * Copyright (C) 2017 Andrious Solutions Ltd.
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
 * Created  27 Jun 2017
 */
public class SpinnerObj{

    private HashMap<String, String> mMap = new HashMap<>();

    private String mSettingsKey;

    private ArrayList<String> mItems = new ArrayList<>();

    private String mItem;

    private ArrayList<String> mValues = new ArrayList<>();

    private String mValue;

    private android.widget.Spinner mSpinner;

    // Prevents a automated selection event.
    private int mPos;




    public SpinnerObj(String settingsKey, String items, String values){

        initSpinner(settingsKey, items, values);
    }




    public SpinnerObj(String settingsKey, String items){

        this(settingsKey, items, null);
    }




    public SpinnerObj(String settingsKey, Map<String, String> map){

        Set<String> keySet = map.keySet();

        String items = "", values = "";

        for (String key : keySet){

            String value = map.get(key);

            items = items + ',' + key.trim();

            values = values + ',' + value.trim();
        }

        if (keySet.size() == 0){

            items = values = " ";
        }

        initSpinner(settingsKey, items.substring(1), values.substring(1));
    }




    public SpinnerObj(String settingsKey, String[] items, String[] values){

        mSettingsKey = settingsKey;

        Collections.addAll(mItems, items);

        Collections.addAll(mValues, values);

        // Put the data items into a map.
        for (int cnt = 0; cnt < mItems.size(); cnt++){

            mMap.put(mItems.get(cnt), mValues.get(cnt));
        }
    }




    private void initSpinner(String settingsKey, String items, String values){

        mSettingsKey = settingsKey;

        add(items, values, mItems, mValues, mMap);

//        // trim any spaces.
//        String[] itemArray = items.trim().split("\\s*,\\s*");
//
//        Collections.addAll(mItems, itemArray);
//
//        if (itemArray.length == 0){
//
//            mItems.add(" ");
//
//            mValues = new String[1];
//
//            mValues[0] = "  ";
//        }else{
//
//            if (values == null){
//
//                mValues = new String[itemArray.length];
//
//                System.arraycopy(itemArray, 0, mValues, 0, itemArray.length);
//            }else{
//
//                // trim any spaces.
//                mValues = values.trim().split("\\s*,\\s*");
//            }
//        }
//
//        // Put the data items into a map.
//        for (int cnt = 0; cnt < mItems.size(); cnt++){
//
//            mMap.put(mItems.get(cnt), mValues[cnt]);
//        }
    }




    private void add(String items, String values, ArrayList<String> itemsList,
            ArrayList<String> ValuesArray,
            HashMap<String, String> map){

        // trim any spaces.
        String[] itemArray = items.trim().split("\\s*,\\s*");

        Collections.addAll(itemsList, itemArray);

        if (itemArray.length == 0){

            itemsList.add(" ");

            ValuesArray = new ArrayList<>();

            ValuesArray.add(" ");
        }else{

            if (values == null){

                Collections.addAll(ValuesArray, itemArray);
            }else{

                String[] valueArray = values.trim().split("\\s*,\\s*");

                if (valueArray.length == 0){

                    valueArray = itemArray;
                }

                // trim any spaces.
                Collections.addAll(ValuesArray, valueArray);
            }
        }

        // Put the data items into a map.
        for (int cnt = 0; cnt < itemsList.size(); cnt++){

            map.put(itemsList.get(cnt), ValuesArray.get(cnt));
        }
    }




    public void init(Context context, android.widget.Spinner spinner){

        mSpinner = spinner;

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, mItems);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinner.setAdapter(adapter);

        mItem = appSettings.get(mSettingsKey, "");

        if (mItem.isEmpty()){

            mPos = 0;

            mItem = mItems.get(mPos);

            mValue = mValues.get(mPos);
        }else{

            mPos = mItems.indexOf(mItem);

            // item not found in list of items.
            if (mPos < 0 || mPos >= mItems.size()){

                mPos = 0;

                mItem = mItems.get(mPos);

                mValue = mValues.get(mPos);
            }else{

                mValue = mValues.get(mPos);
            }
        }

        mSpinner.setSelection(mPos);
    }




    public void setOnItemSelectedListener(final AdapterView.OnItemSelectedListener listener){

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){

                if (mPos == pos){

                    return;
                }

                mPos = pos;

                mItem = mItems.get(pos);

                mValue = mValues.get(pos);

                appSettings.set(mSettingsKey, mItem);

                listener.onItemSelected(parent, view, pos, id);
            }




            @Override
            public void onNothingSelected(AdapterView<?> parent){}
        });
    }




    public android.widget.Spinner spinner(){

        return mSpinner;
    }




    public String getItem(){

        return mItem;
    }




    public String setItem(String item){

        setValue(item);

        return mItem;
    }




    public ArrayList<String> getItems(){

        return mItems;
    }




    public String getValue(){

        return mValue;
    }




    public String setValue(String item){

        if (item == null || item.isEmpty()){

            return mValue;
        }

        String value = mMap.get(item);

        if (value != null){

            mItem = item;

            // So not to fire the selection routine.
            mPos = mItems.indexOf(mItem);

            mValue = value;
        }

        return mValue;
    }




    public String getValue(String item){

        if (item == null || item.isEmpty()){

            return "";
        }

        String value = mMap.get(item);

        if (value == null){

            value = "";
        }

        return value;
    }




    public String[] getValues(){

        String[] values = new String[mValues.size()];

        values = mValues.toArray(values);

        return values;
    }




    public ArrayList<String> getValuesList(){

        return mValues;
    }




    public void update(String items, String values){

        mItems.clear();

        mValues.clear();

        mMap.clear();

        add(items, values);
    }




    public void add(Map<String, String> map){

        Set<String> keySet = map.keySet();

        String items = "", values = "";

        for (String key : keySet){

            String value = map.get(key);

            items = items + ',' + key.trim();

            values = values + ',' + value.trim();
        }

        add(items, values);
    }




    public void add(CharSequence items, CharSequence values){

        add(items.toString(), values.toString());
    }




    public void add(String items, String values){

        HashMap<String, String> map = new HashMap<>();

        ArrayList<String> itemsList = new ArrayList<>();

        ArrayList<String> valuesList = new ArrayList<>();

        add(items, values, itemsList, valuesList, map);

        // Updates the dropdown!
        mItems.addAll(itemsList);

        mValues.addAll(valuesList);

        for (int cnt = 0; cnt < itemsList.size(); cnt++){

            mMap.put(itemsList.get(cnt), valuesList.get(cnt));
        }
    }




    public void remove(@NonNull CharSequence values){

        remove(values.toString());
    }




    public void remove(@NonNull String values){

        String[] valueArray = values.trim().split("\\s*,\\s*");

        for (String value : valueArray){

            for (int cnt = 0; cnt < mValues.size(); cnt++){

                if (mValues.get(cnt).equals(value)){

                    mMap.remove(mItems.get(cnt));

                    mItems.remove(cnt);

                    mValues.remove(cnt);

                    break;
                }
            }
        }
    }




    public void edit(Map<String, String> map){

        Set<String> keySet = map.keySet();

        String items = "", values = "";

        for (String key : keySet){

            String value = map.get(key);

            items = items + ',' + key.trim();

            values = values + ',' + value.trim();
        }

        edit(items, values);
    }




    public void edit(CharSequence items, CharSequence values){

        edit(items.toString(), values.toString());
    }




    public boolean edit(@NonNull String items, @NonNull String values){

        boolean edit = true;

        String[] itemArray = items.trim().split("\\s*,\\s*");

        String[] valueArray = values.trim().split("\\s*,\\s*");

        for (int cnt = 0; cnt < itemArray.length; cnt++){

            int idx = mItems.indexOf(itemArray[cnt]);

            if (idx < 0){

                edit = false;

                break;
            }

            mValues.set(idx, valueArray[cnt]);
        }

        if(edit){

            mMap.clear();

            for (int cnt = 0; cnt < mItems.size(); cnt++){

                mMap.put(mItems.get(cnt), mValues.get(cnt));
            }
        }

        return edit;
    }




    public String setSelection(String item){

        setItem(item);

        setSelection();

        return getValue();
    }




    public String setSelection(){

        String item = getItem();

        int idx = mItems.indexOf(item);

        if (idx < 0 || idx >= mItems.size()){

            idx = 0;
        }

        mSpinner.setSelection(idx);

        return getValue();
    }



     @Keep
    public interface UpdateListener{

        void update(String items, String values);
    }


    public interface OnAddListener{

        void onAdd(Preference pref);
    }

    public class OnNewPref implements OnAddListener{

        public void onAdd(Preference pref){

            CharSequence item = pref.getTitle();

            CharSequence value = pref.getSummary();

            mItems.add(item.toString());

        }
    }
}
