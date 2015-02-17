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
        Boolean setupState = mSettings.getBoolean("setup_state", false);
        return setupState;
    }

    public boolean getAuthState() {
        Boolean authState = mSettings.getBoolean("auth_state", true);
        return authState;
    }

    public String getUsername() {
        String username = mSettings.getString("username", "");
        return username;
    }

    public String getPassword() {
        String password = mSettings.getString("password", "");
        return password;
    }

    public void setSetupState(Boolean setupState) {
        mEditor.putBoolean("setup_state", setupState);
        mEditor.commit();
    }

    public void setAuthState(Boolean authState) {
        mEditor.putBoolean("auth_state", authState);
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

}
