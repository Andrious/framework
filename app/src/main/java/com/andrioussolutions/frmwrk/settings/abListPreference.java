package com.andrioussolutions.frmwrk.settings;

import android.content.DialogInterface;
import android.preference.Preference;
import android.preference.PreferenceScreen;
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
 * Created  2/11/2017.
 */

public abstract class abListPreference  implements  Preference.OnPreferenceClickListener,Preference.OnPreferenceChangeListener,DialogInterface.OnDismissListener{

    protected PreferenceScreen mScreen;

    protected String mTitle;

    protected String mKeySuffix;


    public abListPreference(PreferenceScreen screen, String title, String keySuffix){

        mScreen = screen;

        screen.setOnPreferenceClickListener(this);

        screen.setTitle(title);

        mTitle = title;

        mKeySuffix = keySuffix;
    }

    @Override
    public boolean onPreferenceClick(Preference preference){

        mScreen.getDialog().setOnDismissListener(this);

        // false allows you to fall through
        return false;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue){

        // true to fall through
        return true;
    }

    @Override
    public abstract void onDismiss(DialogInterface dialog);

}
