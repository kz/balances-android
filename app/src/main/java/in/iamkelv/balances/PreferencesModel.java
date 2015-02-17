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

    public void setUsername(String username) {
        mEditor.putString("username", username);
        mEditor.commit();
    }

    public void setPassword(String password) {
        mEditor.putString("password", password);
        mEditor.commit();
    }

}
