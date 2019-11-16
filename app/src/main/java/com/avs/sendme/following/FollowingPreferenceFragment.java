package com.avs.sendme.following;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.avs.sendme.R;

public class FollowingPreferenceFragment extends PreferenceFragmentCompat {

    private final static String LOG_TAG = FollowingPreferenceFragment.class.getSimpleName();

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Add visualizer preferences, defined in the XML file in res->xml->preferences_squawker
        addPreferencesFromResource(R.xml.following_sendme);
    }
}
