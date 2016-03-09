package in.iamkelv.balances.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import in.iamkelv.balances.R;
import in.iamkelv.balances.fragments.NotificationPreferencesFragment;

public class SetupNotificationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_notifications);
        getFragmentManager().beginTransaction().replace(R.id.relativeLayout, new NotificationPreferencesFragment()).commit();
    }
}
