package in.iamkelv.balances;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dd.processbutton.iml.ActionProcessButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

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
        if (!mPreferences.getSetupState()) {
            Intent setupIntent = new Intent(MainActivity.this, SetupActivity.class);
            MainActivity.this.startActivity(setupIntent);
            finish();
        } else if (!mPreferences.getAuthState()) {
            Intent reauthIntent = new Intent(MainActivity.this, ReauthActivity.class);
            MainActivity.this.startActivity(reauthIntent);
            finish();
        } else if (!mPreferences.getIndevState()) { // Check if app is still indev - TODO - Remove once notifications fixed
            new AlertDialog.Builder(MainActivity.this)
                    .setMessage("Scheduled notifications is still in development and will be available in the next update. In the meantime, you\'ll have to open this app to check your balances.")
                    .setCancelable(false)
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    }).create().show();
            mPreferences.setIndevState(true);
        }

        mBtnCheckBalances = (ActionProcessButton) findViewById(R.id.btnCheckBalances);
        mBtnOpenWisePayWebsite = (Button) findViewById(R.id.btnOpenWisePayWebsite);
        mBtnSettings = (Button) findViewById(R.id.btnSettings);
        mTxtLunchBalance = (TextView) findViewById(R.id.txtLunchBalance);
        mTxtTuckBalance = (TextView) findViewById(R.id.txtTuckBalance);
        mTxtLastChecked = (TextView) findViewById(R.id.txtLastCheckedDateTime);

        mBtnCheckBalances.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBalances();
            }
        });

        mBtnOpenWisePayWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uriWisePay = Uri.parse("https://www.wisepay.co.uk/store/generic/template.asp?ACT=nav&mID=24022");
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriWisePay);
                startActivity(launchBrowser);

            }
        });

        mBtnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
                MainActivity.this.startActivity(settingsIntent);
            }
        });

        // Populate balances
        String strLunch = mPreferences.getLunchBalance();
        String strTuck = mPreferences.getTuckBalance();
        long lastChecked = mPreferences.getLastChecked();

        if (strLunch.equals("")) {
            mTxtLunchBalance.setText(getString(R.string.unknown));
        } else {
            mTxtLunchBalance.setText(getString(R.string.pound_sign) + strLunch);
        }

        if (strTuck.equals("")) {
            mTxtTuckBalance.setText(getString(R.string.unknown));
        } else {
            mTxtTuckBalance.setText(getString(R.string.pound_sign) + strTuck);
        }

        if (lastChecked == 0) {
            mTxtLastChecked.setText(R.string.unknown);
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String strLastChecked = simpleDateFormat.format(lastChecked);
            mTxtLastChecked.setText(strLastChecked);
        }

    }

    private void checkBalances() {
        if (isNetworkAvailable()) {
            mBtnCheckBalances.setEnabled(false);
            mBtnCheckBalances.setMode(ActionProcessButton.Mode.ENDLESS);
            mBtnCheckBalances.setProgress(1);

            // Assign variables
            String mUsername = mPreferences.getUsername();
            String mPassword = mPreferences.getPassword();

            // Set type adapter
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Balances.class, new BalancesDeserializer())
                    .create();

            // Send balance request
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(ENDPOINT)
                    .setConverter(new GsonConverter(gson))
                    .build();
            WisePayService service = restAdapter.create(WisePayService.class);

            Callback<Balances> callback = new Callback<Balances>() {
                @Override
                public void success(Balances balances, Response response) {
                    // Get balances
                    Double lunch = balances.lunch;
                    Double tuck = balances.tuck;
                    lunch /= 100;
                    tuck /= 100;
                    String strLunch = String.format("%.2f",lunch);
                    String strTuck = String.format("%.2f",tuck);

                    // Update balances
                    mTxtLunchBalance.setText(getString(R.string.pound_sign) + strLunch);
                    mTxtTuckBalance.setText(getString(R.string.pound_sign) + strTuck);

                    // Update last checked
                    long lastChecked = System.currentTimeMillis();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    String strLastChecked = simpleDateFormat.format(lastChecked);
                    mTxtLastChecked.setText(strLastChecked);

                    // Update preferences
                    mPreferences.setLunchBalance(strLunch);
                    mPreferences.setTuckBalance(strTuck);
                    mPreferences.setLastChecked(lastChecked);

                    // Change view properties
                    mBtnCheckBalances.setProgress(0);
                    mBtnCheckBalances.setEnabled(true);
                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    // Check for authentication error
                    if (retrofitError.getResponse().getStatus() == 401) {
                        mPreferences.setAuthState(false);
                        Intent reauthIntent = new Intent(MainActivity.this, ReauthActivity.class);
                        MainActivity.this.startActivity(reauthIntent);
                        finish();
                    } else {

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
