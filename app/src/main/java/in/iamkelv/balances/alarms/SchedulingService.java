package in.iamkelv.balances.alarms;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import in.iamkelv.balances.R;
import in.iamkelv.balances.activities.MainActivity;
import in.iamkelv.balances.models.Balances;
import in.iamkelv.balances.models.BalancesDeserializer;
import in.iamkelv.balances.models.PreferencesModel;
import in.iamkelv.balances.models.WisePayService;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

public class SchedulingService extends IntentService {

    private final String ENDPOINT = "https://balances.iamkelv.in";
    private PreferencesModel mPreferences;
    private NotificationManager mNotificationManager;
    public static final int NOTIFICATION_ID = 1;
    NotificationCompat.Builder builder;


    public SchedulingService() {
        super("SchedulingService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("Balances", "Starting notification check");
        mPreferences = new PreferencesModel(this);
        if (isNetworkAvailable()) {
            checkBalances();
        } else {
            Log.e("Balances", "Network Unavailable");
        }
        Log.i("Balances", "Notification check complete");

        AlarmReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(String title, String message) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle(title)
                        .setContentText(message);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }



    private void checkBalances() {

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
                int lunchBalance = lunch.intValue();
                int tuckBalance= tuck.intValue();
                lunch /= 100;
                tuck /= 100;
                String strLunch = String.format("%.2f",lunch);
                String strTuck = String.format("%.2f",tuck);

                // Update preferences
                mPreferences.setLunchBalance(strLunch);
                mPreferences.setTuckBalance(strTuck);
                mPreferences.setLastChecked(System.currentTimeMillis());

                int lunchThreshold = mPreferences.getLunchThreshold() * 100;
                int tuckThreshold = mPreferences.getTuckThreshold() * 100;

                if (lunchBalance < lunchThreshold && tuckBalance < tuckThreshold) {
                    sendNotification("Balances Alert", "Your lunch and tuck balances are low");
                } else if (lunchBalance < lunchThreshold) {
                    sendNotification("Balances Alert", "Your lunch balance is low");
                } else if (tuckBalance < tuckThreshold) {
                    sendNotification("Balances Alert", "Your tuck balance is low");
                }
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.e("BALANCES", "Failed callback");
                // Check for authentication error
                if (retrofitError.getResponse().getStatus() == 401) {
                    mPreferences.setAuthState(false);
                    sendNotification("Balances - Error", "Your WisePay login details are incorrect. Tap here to fix.");
                } else {

                    JsonObject jsonResponse = (JsonObject) retrofitError.getBodyAs(JsonObject.class);

                    try {
                        sendNotification("Balances - Error", jsonResponse.get("message").getAsString());
                    } catch (NullPointerException e) {
                        sendNotification("Balances - Error", "An unknown error occurred while checking your balances.");
                    }

                }
            }
        };

        service.checkBalances(mUsername, mPassword, callback);
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