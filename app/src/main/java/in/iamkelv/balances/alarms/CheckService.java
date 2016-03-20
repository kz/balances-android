package in.iamkelv.balances.alarms;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.text.SimpleDateFormat;

import in.iamkelv.balances.R;
import in.iamkelv.balances.activities.MainActivity;
import in.iamkelv.balances.api.BalancesResponse;
import in.iamkelv.balances.api.ApiEndpointInterface;
import in.iamkelv.balances.helpers.Helpers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CheckService extends IntentService {

    private Context mContext;
    public static final int NOTIFICATION_ID = 1;

    public CheckService() {
        super("balances-service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mContext = this;

        Log.i("Balances", "Starting notification check");

        if (isNetworkAvailable()) {
            checkBalances();
        } else {
            Log.e("Balances", "Network unavailable");
        }
        Log.i("Balances", "Notification check complete");

        AlarmReceiver.completeWakefulIntent(intent);
    }

    private void checkBalances() {
        String baseUrl = getString(R.string.app_base_url);
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        String username = settings.getString(getString(R.string.preferences_username_key), null);
        String password = settings.getString(getString(R.string.preferences_password_key), null);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiEndpointInterface apiService = retrofit.create(ApiEndpointInterface.class);

        Call<BalancesResponse> call = apiService.checkBalances(username, password);
        call.enqueue(new Callback<BalancesResponse>() {
            @Override
            public void onResponse(Call<BalancesResponse> call, Response<BalancesResponse> response) {
                int statusCode = response.code();
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mContext);

                if (response.isSuccessful() && statusCode == 200) {
                    Log.d("Balances", "Balances successfully retrieved");
                    BalancesResponse balancesResponse = response.body();

                    // Retrieve lunch and tuck values from data
                    int lunch = balancesResponse.getBalances().getLunch();
                    int tuck = balancesResponse.getBalances().getTuck();

                    // Retrieve threshold values from SharedPreferences
                    String lunchStr = settings.getString(getString(R.string.preferences_notifications_lunch_key),
                            getString(R.string.preferences_notifications_lunch_default));
                    String tuckStr = settings.getString(getString(R.string.preferences_notifications_tuck_key),
                            getString(R.string.preferences_notifications_tuck_default));

                    int lunchThreshold = Integer.valueOf(lunchStr) * 100;
                    int tuckThreshold = Integer.valueOf(tuckStr) * 100;

                    if (lunch < lunchThreshold && tuck < tuckThreshold) {
                        sendNotification("Balances Alert", "Your lunch and tuck balances are low");
                    } else if (lunch < lunchThreshold) {
                        sendNotification("Balances Alert", "Your lunch balance is low");
                    } else if (tuck < tuckThreshold) {
                        sendNotification("Balances Alert", "Your tuck balance is low");
                    }

                    // Update balances
                    Double lunchPreferenceValue = ((double) lunch) / 100;
                    Double tuckPreferenceValue = ((double) tuck) / 100;
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString(getString(R.string.preferences_lunch_key), String.format("%.2f", lunchPreferenceValue));
                    editor.putString(getString(R.string.preferences_tuck_key), String.format("%.2f", tuckPreferenceValue));

                    // Update last checked
                    long lastChecked = System.currentTimeMillis();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    String strLastChecked = simpleDateFormat.format(lastChecked);
                    editor.putString(getString(R.string.preferences_last_checked_key), strLastChecked);

                    editor.commit();

                    Log.d("Balances", "Balances successfully updated");
                } else if (statusCode == 401) {
                    // Re-authenticate the user
                    settings.edit()
                            .putBoolean(getString(R.string.preferences_reauthentication_key), true)
                            .commit();
                    sendNotification("Balances - Error", "Your WisePay login details are incorrect. Tap here to fix.");
                } else {
                    Log.e("Balances", "Failure due to unsuccessful response");

                }
            }

            @Override
            public void onFailure(Call<BalancesResponse> call, Throwable t) {
                if (!isNetworkAvailable()) {
                    Log.e("Balances", "Failure due to unavailable network");
                } else {
                    Log.e("Balances", "Failure due to unknown cause");
                    sendNotification("Balances - Error", "An unknown error occurred while checking your balances.");
                }
            }
        });
    }

    private void sendNotification(String title, String message) {
        NotificationManager notificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_notification)
                        .setContentTitle(title)
                        .setContentText(message);

        builder.setContentIntent(contentIntent);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
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
