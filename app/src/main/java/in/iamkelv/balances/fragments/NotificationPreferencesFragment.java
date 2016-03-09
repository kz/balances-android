package in.iamkelv.balances.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.iamkelv.balances.R;

public class NotificationPreferencesFragment extends PreferenceFragment {


    public NotificationPreferencesFragment() {
    }


    public static NotificationPreferencesFragment newInstance() {
        NotificationPreferencesFragment fragment = new NotificationPreferencesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.notification_preferences);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notification_preferences, container, false);
    }
}
