package com.andrioussolutions.frmwrk.settings;

import android.content.Context;
import android.util.AttributeSet;
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
 * Created  2015-04-12.
 */
public class RingtonePreference extends android.preference.RingtonePreference {

    private String mRingtone;

    public RingtonePreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public RingtonePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RingtonePreference(Context context) {
        super(context, null);
    }

    public void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {

        super.onSetInitialValue(restorePersistedValue, defaultValue);

        mRingtone = (String) defaultValue;
    }

    protected String getPersistedString(String defaultReturnValue) {

        if (!shouldPersist()) {

            return mRingtone == null ? defaultReturnValue : mRingtone;
        }else {

            return appSettings.get(getKey(), defaultReturnValue);
        }
    }
}
