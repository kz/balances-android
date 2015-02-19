package in.iamkelv.balances;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.dd.processbutton.iml.ActionProcessButton;
import com.google.gson.JsonObject;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class ReauthActivity extends ActionBarActivity {

    // Member variables
    private String mUsername = "";
    private String mPassword = "";
    EditText txtLoginUsername;
    EditText txtLoginPassword;
    ActionProcessButton btnSignIn;
    PreferencesModel preferences;

    private final String ENDPOINT = "https://balances.iamkelv.in";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reauth);

        txtLoginUsername = (EditText) findViewById(R.id.txtLoginUsername);
        txtLoginPassword = (EditText) findViewById(R.id.txtLoginPassword);
        btnSignIn = (ActionProcessButton) findViewById(R.id.btnSignIn);
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

                    // Assign variables - TODO: Add check for empty username/password
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reauth, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
