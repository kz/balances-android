package in.iamkelv.balances.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.iamkelv.balances.R;
import in.iamkelv.balances.helpers.Helpers;
import in.iamkelv.balances.models.AuthResponse;
import in.iamkelv.balances.services.ApiEndpointInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SetupAccountActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.nextButton)
    ImageButton nextButton;
    @Bind(R.id.prevButton)
    ImageButton prevButton;
    @Bind(R.id.signInButton)
    Button signInButton;
    @Bind(R.id.usernameEditText)
    EditText usernameEditText;
    @Bind(R.id.passwordEditText)
    EditText passwordEditText;

    ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_account);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        // Disable next button
        nextButton.setEnabled(false);
        nextButton.setAlpha((float) 0.3);

        // Initialise progress dialog
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Signing in...");
        mProgressDialog.setMessage("Please wait.");
        mProgressDialog.setCancelable(false);

    }

    public void onSignInButtonClick() {
        mProgressDialog.show();
        attemptAuthentication(usernameEditText.getText().toString(),
                passwordEditText.getText().toString());
    }

    public void onNextButtonClick() {
        Intent nextActivityIntent = new Intent(this, SetupNotificationsActivity.class);
        startActivity(nextActivityIntent);
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

    public void onPrevButtonClick() {
        onBackPressed();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }

    private void attemptAuthentication(String username, String password) {
        String baseUrl = getString(R.string.app_base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiEndpointInterface apiService = retrofit.create(ApiEndpointInterface.class);

        Call<AuthResponse> call = apiService.authUser(username, password);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                int statusCode = response.code();
                if (response.isSuccessful() && statusCode == 200) {
                    updateViewOnSuccessfulLogin();
                    updatePreferencesOnSuccessfulLogin();
                } else {
                    Snackbar.make(findViewById(R.id.relativeLayout),
                            Helpers.getErrorTextFromStatusCode(statusCode), Snackbar.LENGTH_LONG)
                            .show();
                }

                mProgressDialog.hide();
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
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

    private void updatePreferencesOnSuccessfulLogin() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(getString(R.string.preferences_username_key), usernameEditText.getText().toString());
        editor.putString(getString(R.string.preferences_password_key), passwordEditText.getText().toString());
        editor.apply();
    }

    private void updateViewOnSuccessfulLogin() {
        signInButton.setEnabled(false);
        signInButton.setText(R.string.setup_account_signed_in);

        getWindow().getDecorView().clearFocus();

        usernameEditText.setEnabled(false);
        passwordEditText.setEnabled(false);

        nextButton.setAlpha((float) 1);
        nextButton.setEnabled(true);
    }

    @OnClick({R.id.signInButton, R.id.nextButton, R.id.prevButton})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signInButton:
                onSignInButtonClick();
                break;
            case R.id.nextButton:
                onNextButtonClick();
                break;
            case R.id.prevButton:
                onPrevButtonClick();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
