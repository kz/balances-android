package in.iamkelv.balances;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.dd.processbutton.FlatButton;
import com.dd.processbutton.iml.ActionProcessButton;
import com.google.gson.JsonObject;

import in.iamkelv.balances.model.WisePayService;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class ReauthActivity extends Activity {

    // Member variables
    private String mUsername = "";
    private String mPassword = "";
    EditText txtLoginUsername;
    EditText txtLoginPassword;
    ActionProcessButton btnSignIn;
    PreferencesModel preferences;
    FlatButton btnProceedToApp;

    private final String ENDPOINT = "https://balances.iamkelv.in";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reauth);

        txtLoginUsername = (EditText) findViewById(R.id.txtLoginUsername);
        txtLoginPassword = (EditText) findViewById(R.id.txtLoginPassword);
        btnSignIn = (ActionProcessButton) findViewById(R.id.btnSignIn);
        btnProceedToApp = (FlatButton) findViewById(R.id.btnProceedToApp);
        preferences = new PreferencesModel(this);

        if (!preferences.getUsername().equals("")) {
            txtLoginUsername.setText(preferences.getUsername());
        }

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (NullPointerException ignored) {
                }
                if (isNetworkAvailable()) {
                    // Change view properties
                    txtLoginUsername.setFocusable(false);
                    txtLoginPassword.setFocusable(false);
                    btnSignIn.setEnabled(false);
                    btnSignIn.setMode(ActionProcessButton.Mode.ENDLESS);
                    btnSignIn.setProgress(1);

                    // Assign variables
                    mUsername = txtLoginUsername.getText().toString();
                    mPassword = txtLoginPassword.getText().toString();

                    // Send auth request
                    RestAdapter restAdapter = new RestAdapter.Builder()
                            .setEndpoint(ENDPOINT)
                            .build();
                    WisePayService service = restAdapter.create(WisePayService.class);

                    Callback callback = new Callback() {
                        @Override
                        public void success(Object o, Response response) {
                            // Save credentials to preferences
                            preferences.setUsername(mUsername);
                            preferences.setPassword(mPassword);

                            // Change view properties
                            btnSignIn.setProgress(100);
                            fadeIn(btnProceedToApp);
                        }

                        @Override
                        public void failure(RetrofitError retrofitError) {
                            // Change view properties
                            btnSignIn.setProgress(0);
                            btnSignIn.setEnabled(true);
                            txtLoginUsername.setFocusableInTouchMode(true);
                            txtLoginUsername.setFocusable(true);
                            txtLoginPassword.setFocusableInTouchMode(true);
                            txtLoginPassword.setFocusable(true);

                            JsonObject jsonResponse = (JsonObject) retrofitError.getBodyAs(JsonObject.class);

                            try {
                                new AlertDialog.Builder(ReauthActivity.this)
                                        .setMessage("Error - " + jsonResponse.get("message").getAsString())
                                        .setCancelable(false)
                                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int id) {
                                            }
                                        }).create().show();
                            } catch (NullPointerException e) {
                                new AlertDialog.Builder(ReauthActivity.this)
                                        .setMessage("Error - An unknown error has occurred.")
                                        .setCancelable(false)
                                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int id) {
                                            }
                                        }).create().show();
                            }
                        }
                    };

                    service.authUser(mUsername, mPassword, callback);

                } else {
                    new AlertDialog.Builder(ReauthActivity.this)
                            .setMessage("There is no internet connection. Please connect to the internet and try again.")
                            .setCancelable(false)
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            }).create().show();
                }
            }
        });

        btnProceedToApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences.setAuthState(true);
                Intent mainIntent= new Intent(ReauthActivity.this, MainActivity.class);
                ReauthActivity.this.startActivity(mainIntent);
                finish();
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }

        return isAvailable;
    }

    private void fadeIn(final View view) {
        final AlphaAnimation fadeOutAnimation = new AlphaAnimation(0.0F,  1.0F);
        fadeOutAnimation.setDuration(100);
        fadeOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }
        });
        view.startAnimation(fadeOutAnimation);
    }

}
