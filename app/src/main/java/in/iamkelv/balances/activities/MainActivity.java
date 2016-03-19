package in.iamkelv.balances.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.github.jorgecastilloprz.FABProgressCircle;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.iamkelv.balances.R;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.lunchAmountTextView)
    TextView lunchAmountTextView;
    @Bind(R.id.tuckAmountTextView)
    TextView tuckAmountTextView;
    @Bind(R.id.lastUpdatedTextView)
    TextView lastUpdatedTextView;
    @Bind(R.id.refreshFab)
    FloatingActionButton refreshFab;
    @Bind(R.id.fabProgressCircle)
    FABProgressCircle fabProgressCircle;

    SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mSettings = PreferenceManager.getDefaultSharedPreferences(this);
        ensureAuthenticatedAndSetupComplete();
    }

    private void ensureAuthenticatedAndSetupComplete() {
        boolean isSetupComplete = mSettings.getBoolean(getString(R.string.preferences_is_setup_complete_key), false);
        if (!isSetupComplete) {
            Intent setupActivityIntent = new Intent(this, SetupWelcomeActivity.class);
            startActivity(setupActivityIntent);
            finish();
        }
    }

    @OnClick(R.id.refreshFab)
    public void onClick() {
        fabProgressCircle.show();
    }
}
