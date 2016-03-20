package in.iamkelv.balances.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.iamkelv.balances.R;
import in.iamkelv.balances.adapters.PurchasesAdapter;
import in.iamkelv.balances.adapters.SectionedPurchasesRecyclerViewAdapter;
import in.iamkelv.balances.api.AccountResponse;
import in.iamkelv.balances.api.ApiEndpointInterface;
import in.iamkelv.balances.api.Purchase;
import in.iamkelv.balances.decorators.DividerItemDecoration;
import in.iamkelv.balances.helpers.Helpers;
import in.iamkelv.balances.models.DbPurchase;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.lunchAmountTextView)
    TextView lunchAmountTextView;
    @Bind(R.id.tuckAmountTextView)
    TextView tuckAmountTextView;
    @Bind(R.id.lastCheckedTextView)
    TextView lastCheckedTextView;
    @Bind(R.id.lastCheckedValueTextView)
    TextView lastCheckedValueTextView;
    @Bind(R.id.refreshFab)
    FloatingActionButton refreshFab;
    @Bind(R.id.purchasesRecyclerView)
    RecyclerView purchasesRecyclerView;

    SharedPreferences mSettings;
    ProgressDialog mProgressDialog;
    PurchasesAdapter mPurchasesAdapter;
    SectionedPurchasesRecyclerViewAdapter mSectionedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: Remove Stetho
        Stetho.initializeWithDefaults(this);
        JodaTimeAndroid.init(this);

        mSettings = PreferenceManager.getDefaultSharedPreferences(this);
        ensureAuthenticatedAndSetupComplete();

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Initialise progress dialog
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Fetching data");
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);

        updateBalancesFromPreferences();
        createRecyclerViewFromDatabase();
    }

    private void ensureAuthenticatedAndSetupComplete() {
        boolean isSetupComplete = mSettings.getBoolean(getString(R.string.preferences_is_setup_complete_key), false);
        if (!isSetupComplete) {
            Intent setupActivityIntent = new Intent(this, SetupWelcomeActivity.class);
            startActivity(setupActivityIntent);
            finish();
        }
    }

    @OnClick({R.id.refreshFab})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.refreshFab:
                onRefreshFabClick();
                break;
        }
    }

    private void onRefreshFabClick() {
        mProgressDialog.show();

        updateData();
    }

    private void updateBalancesFromPreferences() {
        String lunch = mSettings.getString(getString(R.string.preferences_lunch_key), getString(R.string.main_default_value));
        String tuck = mSettings.getString(getString(R.string.preferences_tuck_key), getString(R.string.main_default_value));
        String lastChecked = mSettings.getString(getString(R.string.preferences_last_checked_key), getString(R.string.main_default_value));

        if (!(lunch.equals(getString(R.string.main_default_value)) || tuck.equals(getString(R.string.main_default_value)))) {
            lunchAmountTextView.setText(String.format(getString(R.string.main_price_value), lunch));
            tuckAmountTextView.setText(String.format(getString(R.string.main_price_value), tuck));
            lastCheckedValueTextView.setText(lastChecked);
        }
    }

    private void createRecyclerViewFromDatabase() {

        TreeMap<Long, List<DbPurchase>> sectionedDbPurchases = getSectionedDbPurchaseFromDatabase();
        List<DbPurchase> purchases = new ArrayList<>();
        List<SectionedPurchasesRecyclerViewAdapter.Section> sections = new ArrayList<>();

        int lenCount = 0;
        for (Map.Entry<Long, List<DbPurchase>> dbPurchaseSection : sectionedDbPurchases.entrySet()) {
            String sectionTitle = convertDateTimestampToString(dbPurchaseSection.getKey());
            sections.add(new SectionedPurchasesRecyclerViewAdapter.Section(lenCount, sectionTitle));

            for (DbPurchase dbPurchase : dbPurchaseSection.getValue()) {
                purchases.add(dbPurchase);
            }

            lenCount += dbPurchaseSection.getValue().size();
        }

        mPurchasesAdapter = new PurchasesAdapter(this, purchases);

        SectionedPurchasesRecyclerViewAdapter.Section[] dummy = new SectionedPurchasesRecyclerViewAdapter.Section[sections.size()];
        mSectionedAdapter = new
                SectionedPurchasesRecyclerViewAdapter(this, R.layout.section, R.id.section_text, mPurchasesAdapter);
        mSectionedAdapter.setSections(sections.toArray(dummy));

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        purchasesRecyclerView.addItemDecoration(itemDecoration);
        purchasesRecyclerView.setAdapter(mSectionedAdapter);
        purchasesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void updateRecyclerViewFromDatabase() {
        TreeMap<Long, List<DbPurchase>> sectionedDbPurchases = getSectionedDbPurchaseFromDatabase();
        List<DbPurchase> purchases = new ArrayList<>();
        List<SectionedPurchasesRecyclerViewAdapter.Section> sections = new ArrayList<>();

        int lenCount = 0;
        for (Map.Entry<Long, List<DbPurchase>> dbPurchaseSection : sectionedDbPurchases.entrySet()) {
            String sectionTitle = convertDateTimestampToString(dbPurchaseSection.getKey());
            sections.add(new SectionedPurchasesRecyclerViewAdapter.Section(lenCount, sectionTitle));

            for (DbPurchase dbPurchase : dbPurchaseSection.getValue()) {
                purchases.add(dbPurchase);
            }

            lenCount += dbPurchaseSection.getValue().size();
        }

        mPurchasesAdapter.swap(purchases);

        SectionedPurchasesRecyclerViewAdapter.Section[] dummy = new SectionedPurchasesRecyclerViewAdapter.Section[sections.size()];
        mSectionedAdapter = new
                SectionedPurchasesRecyclerViewAdapter(this, R.layout.section, R.id.section_text, mPurchasesAdapter);
        mSectionedAdapter.setSections(sections.toArray(dummy));
    }

    private void updateData() {
        String baseUrl = getString(R.string.app_base_url);
        String username = mSettings.getString(getString(R.string.preferences_username_key), null);
        String password = mSettings.getString(getString(R.string.preferences_password_key), null);

        // TODO: Remove Stetho
        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        ApiEndpointInterface apiService = retrofit.create(ApiEndpointInterface.class);

        Call<AccountResponse> call = apiService.checkBalancesAndPurchases(username, password);
        call.enqueue(new Callback<AccountResponse>() {
            @Override
            public void onResponse(Call<AccountResponse> call, Response<AccountResponse> response) {
                int statusCode = response.code();
                if (response.isSuccessful() && statusCode == 200) {
                    AccountResponse accountResponse = response.body();
                    updatePreferencesOnSuccessfulUpdate(accountResponse);
                    updateDatabaseOnSuccessfulUpdate(accountResponse);
                    updateViewOnSuccessfulUpdate(accountResponse);
                } else if (statusCode == 401) {
                    // Re-authenticate the user
                    mSettings.edit()
                            .putBoolean(getString(R.string.preferences_reauthentication_key), true)
                            .apply();

                    // TODO: Add re-authentication activity
                } else {
                    Snackbar.make(findViewById(R.id.coordinatorLayout),
                            Helpers.getErrorTextFromStatusCode(statusCode), Snackbar.LENGTH_LONG)
                            .show();
                }

                mProgressDialog.hide();
            }

            @Override
            public void onFailure(Call<AccountResponse> call, Throwable t) {
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

    private void updateDatabaseOnSuccessfulUpdate(AccountResponse data) {
        List<Purchase> purchases = data.getPurchases();
        DbPurchase.deleteAll(DbPurchase.class);
        for (Purchase purchase : purchases) {
            try {
                DbPurchase dbPurchase = new DbPurchase(purchase.getItem(), purchase.getDateAsTimestamp(), purchase.getTime(), purchase.getPrice());
                dbPurchase.save();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private void updatePreferencesOnSuccessfulUpdate(AccountResponse data) {
        SharedPreferences.Editor editor = mSettings.edit();

        // Update balances
        Double lunch = ((double) data.getBalances().getLunch()) / 100;
        Double tuck = ((double) data.getBalances().getTuck()) / 100;
        editor.putString(getString(R.string.preferences_lunch_key), String.format("%.2f", lunch));
        editor.putString(getString(R.string.preferences_tuck_key), String.format("%.2f", tuck));

        // Update last checked
        long lastChecked = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String strLastChecked = simpleDateFormat.format(lastChecked);
        editor.putString(getString(R.string.preferences_last_checked_key), strLastChecked);
        editor.commit();
    }

    private void updateViewOnSuccessfulUpdate(AccountResponse data) {
        updateBalancesFromPreferences();
        updateRecyclerViewFromDatabase();
    }

    private TreeMap<Long, List<DbPurchase>> getSectionedDbPurchaseFromDatabase() {
        // Retrieve purchases from database
        List<DbPurchase> dbPurchases = DbPurchase.listAll(DbPurchase.class);
        TreeMap<Long, List<DbPurchase>> sectionedDbPurchases = new TreeMap<>(Collections.reverseOrder());

        for (DbPurchase dbPurchase : dbPurchases) {
            long date = dbPurchase.getDate();
            if (sectionedDbPurchases.containsKey(date)) {
                sectionedDbPurchases.get(date).add(dbPurchase);
            } else {
                List<DbPurchase> newList = new ArrayList<>();
                newList.add(dbPurchase);

                sectionedDbPurchases.put(date, newList);
            }
        }

        return sectionedDbPurchases;
    }

    private String convertDateTimestampToString(long timestamp) {
        DateTime dateTime = new DateTime(timestamp);

        return String.format("%s %s %s",
                dateTime.dayOfWeek().getAsShortText(),
                dateTime.dayOfMonth().getAsText(),
                dateTime.monthOfYear().getAsText());
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
