package com.andrioussolutions.frmwrk.settings;

import com.andrioussolutions.R;
import com.andrioussolutions.frmwrk.App;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
/**
 *  Copyright  2015  Andrious Solutions Ltd.
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
 * Created  2015-03-07.
 */
public class SettingsActivity extends AppCompatActivity  implements DialogInterface.OnCancelListener {

    private Intent mIntent;

    private boolean mItemPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mIntent = getIntent();

        setContentView(R.layout.preferences);

        // However, if we're being restored from a previous state,
        // then we don't need to do anything and should return or else
        // we could end up with overlapping fragments.
        if (savedInstanceState != null) {

            return;
        }

        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, App.getPreferences())
                .commit();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
//
                // This will close this activity
//                finish();
//
//                return true;

                return false;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        String key = mIntent.getStringExtra("Preference");

        mItemPreference = key != null && !key.isEmpty();

        // If the event item preference settings was clicked.
        if (mItemPreference) {

            // Open a particular preference
            Preference pref = appPreferences.openPreference(key);

//            Preference pref = appSettings.findPreference(key);

            if (pref != null && pref instanceof PreferenceScreen) {

                // Assign onCancel()
                ((PreferenceScreen) pref).getDialog().setOnCancelListener(this);
            }
        }
    }


    // Close the activity if looking at a particular preference.
    @Override
    public void onBackPressed() {

        // We're viewing an event item's preference
        if (mItemPreference) {

            setResult(-1, mIntent);

            finish();
        } else {

            super.onBackPressed();
        }
    }

    // Close the settings activity
    public void onCancel(DialogInterface dialog) {

        setResult(-1, mIntent);

        finish();
    }
}
