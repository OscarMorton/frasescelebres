package ml.oscarmorton.frasescelebres.fragments;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import ml.oscarmorton.frasescelebres.R;

public class OptionsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preference_options, rootKey);
    }
}