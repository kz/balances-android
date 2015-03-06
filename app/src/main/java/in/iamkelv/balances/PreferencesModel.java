package in.iamkelv.balances;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesModel {

    private SharedPreferences mSettings;
    private SharedPreferences.Editor mEditor;
    private static final String PREFS_NAME = "BalancesPrefs";

    public PreferencesModel(Context context) {
        this.mSettings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        this.mEditor = mSettings.edit();
    }

    public boolean getSetupState() {
        return mSettings.getBoolean("setup_state", false);
    }

    public boolean getAuthState() {
        return mSettings.getBoolean("auth_state", true);
    }

    public boolean getIndevState() {
        return mSettings.getBoolean("indev_state", false);
    }

    public String getUsername() {
        return mSettings.getString("username", "");
    }

    public String getPassword() {
        return mSettings.getString("password", "");
    }

    public boolean getNotificationState() {
        return mSettings.getBoolean("notification_state", false);
    }

    public int getNotificationHours() {
        return mSettings.getInt("notification_hours", 18);
    }

    public int getNotificationMinutes() {
        return mSettings.getInt("notification_minutes", 0);
    }

    public int getLunchThreshold() {
        return mSettings.getInt("lunch_threshold", 10);
    }

    public int getTuckThreshold() {
        return mSettings.getInt("tuck_threshold", 10);

    }

    public String getLunchBalance() {
        return mSettings.getString("lunch_balance", "");
    }

    public String getTuckBalance() {
        return mSettings.getString("tuck_balance", "");
    }

    public long getLastChecked() {
        return mSettings.getLong("last_checked", 0);
    }

    public void setSetupState(Boolean setupState) {
        mEditor.putBoolean("setup_state", setupState);
        mEditor.commit();
    }

    public void setAuthState(Boolean authState) {
        mEditor.putBoolean("auth_state", authState);
        mEditor.commit();
    }

    public void setIndevState(Boolean indevState) {
        mEditor.putBoolean("indev_state", indevState);
        mEditor.commit();
    }

    public void setUsername(String username) {
        mEditor.putString("username", username);
        mEditor.commit();
    }

    public void setPassword(String password) {
        mEditor.putString("password", password);
        mEditor.commit();
    }

    public void setNotificationState(Boolean notificationState) {
        mEditor.putBoolean("notification_state", notificationState);
        mEditor.commit();
    }

    public void setNotificationHours(int notificationHours) {
        mEditor.putInt("notification_hours", notificationHours);
        mEditor.commit();
    }

    public void setNotificationMinutes(int notificationMinutes) {
        mEditor.putInt("notification_minutes", notificationMinutes);
        mEditor.commit();
    }

    public void setLunchThreshold(int lunchThreshold) {
        mEditor.putInt("lunch_threshold", lunchThreshold);
        mEditor.commit();
    }

    public void setTuckThreshold(int tuckThreshold) {
        mEditor.putInt("tuck_threshold", tuckThreshold);
        mEditor.commit();
    }

    public void setLunchBalance(String lunchBalance) {
        mEditor.putString("lunch_balance", lunchBalance);
        mEditor.commit();
    }

    public void setTuckBalance(String tuckBalance) {
        mEditor.putString("tuck_balance", tuckBalance);
        mEditor.commit();
    }

    public void setLastChecked(long lastChecked) {
        mEditor.putLong("last_checked", lastChecked);
        mEditor.commit();
    }

}
