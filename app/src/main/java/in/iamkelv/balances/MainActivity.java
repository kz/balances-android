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
import android.widget.Button;
import android.widget.TextView;

import com.dd.processbutton.iml.ActionProcessButton;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends Activity {

    private final String ENDPOINT = "https://balances.iamkelv.in";
    private ActionProcessButton mBtnCheckBalances;
    private Button mBtnOpenWisePayWebsite;
    private Button mBtnSettings;
    private TextView mTxtLunchBalance;
    private TextView mTxtTuckBalance;
    private TextView mTxtLastChecked;
    private PreferencesModel mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check if initial setup is required
        mPreferences = new PreferencesModel(this);
        Boolean setupState = mPreferences.getSetupState();
        if (!setupState) {
            Intent setupIntent = new Intent(MainActivity.this, SetupActivity.class);
            MainActivity.this.startActivity(setupIntent);
            finish();
        }

        mBtnCheckBalances = (ActionProcessButton) findViewById(R.id.btnCheckBalances);
        mBtnOpenWisePayWebsite = (Button) findViewById(R.id.btnOpenWisePayWebsite);
        mBtnSettings = (Button) findViewById(R.id.btnSettings);
        mTxtLunchBalance = (TextView) findViewById(R.id.txtLunchBalance);
        mTxtTuckBalance = (TextView) findViewById(R.id.txtTuckBalance);
        mTxtLastChecked = (TextView) findViewById(R.id.txtLastChecked);

        mBtnCheckBalances.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBalances();
            }
        });

        mBtnOpenWisePayWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mBtnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void checkBalances() {
        if (isNetworkAvailable()) {
            mBtnCheckBalances.setEnabled(false);
            mBtnCheckBalances.setMode(ActionProcessButton.Mode.ENDLESS);
            mBtnCheckBalances.setProgress(1);

            // Assign variables
            String mUsername = mPreferences.getUsername();
            String mPassword = mPreferences.getPassword();

            // Send auth request
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setEndpoint(ENDPOINT)
                    .build();
            WisePayService service = restAdapter.create(WisePayService.class);

            Callback callback = new Callback() {
                @Override
                public void success(Object o, Response response) {
                    // Update balances
                    LinkedTreeMap<String, String> balancesa = (LinkedTreeMap) o;
                    Object balancesb = (Object) o.get("balances");

                    // Update last checked

                    // Change view properties
                    mBtnCheckBalances.setProgress(0);
                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    // Change view properties
                    mBtnCheckBalances.setProgress(0);
                    mBtnCheckBalances.setEnabled(true);

                    JsonObject jsonResponse = (JsonObject) retrofitError.getBodyAs(JsonObject.class);

                    try {
                        new AlertDialog.Builder(MainActivity.this)
                                .setMessage("Error - " + jsonResponse.get("message").getAsString())
                                .setCancelable(false)
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                    }
                                }).create().show();
                    } catch (NullPointerException e) {
                        new AlertDialog.Builder(MainActivity.this)
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

            service.checkBalances(mUsername, mPassword, callback);

        } else {
            new AlertDialog.Builder(MainActivity.this)
                    .setMessage("There is no internet connection. Please connect to the internet and try again.")
                    .setCancelable(false)
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    }).create().show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }

        return isAvailable;
    }
}
