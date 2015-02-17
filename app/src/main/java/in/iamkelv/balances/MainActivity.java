package in.iamkelv.balances;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check if initial setup is required
        PreferencesModel preferences = new PreferencesModel(this);
        Boolean setupState = preferences.getSetupState();
        if (!setupState) {
            Intent setupIntent = new Intent(MainActivity.this, SetupActivity.class);
            MainActivity.this.startActivity(setupIntent);
            finish();
        }
    }
}
