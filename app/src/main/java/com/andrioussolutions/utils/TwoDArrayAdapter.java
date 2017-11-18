package com.andrioussolutions.utils;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;
/**
 * Copyright (C) 2017  Andrious Solutions Ltd.
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
 * Created  23 Jun 2017
 */
public class TwoDArrayAdapter<T> extends ArrayAdapter<T>{




    public TwoDArrayAdapter(@NonNull Context context, @LayoutRes int resource,  @NonNull List<T> objects){

        super(context, resource, objects);


    }




    @Override
    public View getView(int position, @Nullable View convertView, @Nullable ViewGroup parent) {

          return super.getView(position, convertView, parent);
    }




    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        return super.getDropDownView(position, convertView, parent);
    }
}
