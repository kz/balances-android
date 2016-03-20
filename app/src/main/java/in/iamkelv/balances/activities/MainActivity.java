package in.iamkelv.balances.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.iamkelv.balances.R;
import in.iamkelv.balances.helpers.Helpers;
import in.iamkelv.balances.models.AccountResponse;
import in.iamkelv.balances.services.ApiEndpointInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.lunchAmountTextView)
    TextView lunchAmountTextView;
    @Bind(R.id.tuckAmountTextView)
    TextView tuckAmountTextView;
    @Bind(R.id.lastCheckedTextView)
    TextView lastCheckedTextView;
    @Bind(R.id.lastCheckedValueTextView)
    TextView lastCheckedValueTextView;
    @Bind(R.id.refreshFab)
    FloatingActionButton refreshFab;

    SharedPreferences mSettings;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSettings = PreferenceManager.getDefaultSharedPreferences(this);
        ensureAuthenticatedAndSetupComplete();

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Initialise progress dialog
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Fetching data");
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);

        updateBalancesFromPreferences();
    }

    private void ensureAuthenticatedAndSetupComplete() {
        boolean isSetupComplete = mSettings.getBoolean(getString(R.string.preferences_is_setup_complete_key), false);
        if (!isSetupComplete) {
            Intent setupActivityIntent = new Intent(this, SetupWelcomeActivity.class);
            startActivity(setupActivityIntent);
            finish();
        }
    }

    @OnClick({R.id.refreshFab})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.refreshFab:
                onRefreshFabClick();
                break;
        }
    }

    private void onRefreshFabClick() {
        mProgressDialog.show();
        updateData();
    }

    private void updateData() {
        String baseUrl = getString(R.string.app_base_url);
        String username = mSettings.getString(getString(R.string.preferences_username_key), null);
        String password = mSettings.getString(getString(R.string.preferences_password_key), null);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiEndpointInterface apiService = retrofit.create(ApiEndpointInterface.class);

        Call<AccountResponse> call = apiService.checkBalancesAndPurchases(username, password);
        call.enqueue(new Callback<AccountResponse>() {
            @Override
            public void onResponse(Call<AccountResponse> call, Response<AccountResponse> response) {
                int statusCode = response.code();
                if (response.isSuccessful() && statusCode == 200) {
                    AccountResponse accountResponse = response.body();
                    updatePreferencesOnSuccessfulUpdate(accountResponse);
                    updateViewOnSuccessfulUpdate(accountResponse);
                } else if (statusCode == 401) {
                    // Re-authenticate the user
                    mSettings.edit()
                            .putBoolean(getString(R.string.preferences_reauthentication_key), true)
                            .apply();

                    // TODO: Add re-authentication activity
                } else {
                    Snackbar.make(findViewById(R.id.coordinatorLayout),
                            Helpers.getErrorTextFromStatusCode(statusCode), Snackbar.LENGTH_LONG)
                            .show();
                }

                mProgressDialog.hide();
            }

            @Override
            public void onFailure(Call<AccountResponse> call, Throwable t) {
                if (!isNetworkAvailable()) {
                    Snackbar.make(findViewById(R.id.relativeLayout),
                            "No network connection", Snackbar.LENGTH_LONG)
                            .show();
                } else {
                    Snackbar.make(findViewById(R.id.relativeLayout),
                            "An error has occurred. Please try again later.", Snackbar.LENGTH_LONG)
                            .show();
                }
                mProgressDialog.hide();
            }
        });
    }

    private void updatePreferencesOnSuccessfulUpdate(AccountResponse data) {
        SharedPreferences.Editor editor = mSettings.edit();

        // Update balances
        Double lunch = ((double) data.getBalances().getLunch()) / 100;
        Double tuck = ((double) data.getBalances().getTuck()) / 100;
        editor.putString(getString(R.string.preferences_lunch_key), String.format("%.2f", lunch));
        editor.putString(getString(R.string.preferences_tuck_key), String.format("%.2f", tuck));

        // Update last checked
        long lastChecked = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String strLastChecked = simpleDateFormat.format(lastChecked);
        editor.putString(getString(R.string.preferences_last_checked_key), strLastChecked);
        editor.commit();
    }

    private void updateViewOnSuccessfulUpdate(AccountResponse data) {
        updateBalancesFromPreferences();
    }

    private void updateBalancesFromPreferences() {
        String lunch = mSettings.getString(getString(R.string.preferences_lunch_key), getString(R.string.main_default_value));
        String tuck = mSettings.getString(getString(R.string.preferences_tuck_key), getString(R.string.main_default_value));
        String lastChecked = mSettings.getString(getString(R.string.preferences_last_checked_key), getString(R.string.main_default_value));

        if (!(lunch.equals(getString(R.string.main_default_value)) || tuck.equals(getString(R.string.main_default_value)))) {
            lunchAmountTextView.setText(String.format(getString(R.string.main_price_value), lunch));
            tuckAmountTextView.setText(String.format(getString(R.string.main_price_value), tuck));
            lastCheckedValueTextView.setText(lastChecked);
        }
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
