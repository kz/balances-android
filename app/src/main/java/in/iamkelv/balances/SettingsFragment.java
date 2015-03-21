package in.iamkelv.balances;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);

        Preference resetPreference = findPreference("reset");
        resetPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick (Preference pref){
                PreferencesModel mPreferences = new PreferencesModel(getActivity());
                mPreferences.clearPreferences();
                Intent setupIntent = new Intent(getActivity(), SetupActivity.class);
                getActivity().startActivity(setupIntent);
                getActivity().finish();
                return true;
            }
        });


    }

}
