package in.iamkelv.balances.wizards.setupwizard;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.dd.processbutton.iml.ActionProcessButton;
import com.google.gson.JsonObject;

import org.codepond.wizardroid.WizardStep;

import in.iamkelv.balances.PreferencesModel;
import in.iamkelv.balances.R;
import in.iamkelv.balances.WisePayService;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SetupWizardStepLogin extends WizardStep {

    // Member variables
    private String mUsername = "";
    private String mPassword = "";
    private final String ENDPOINT = "https://balances.iamkelv.in";

    // Methods

    public SetupWizardStepLogin() {
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }

        return isAvailable;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.setup_wizard_step_login, container, false);

        final EditText txtLoginUsername = (EditText) v.findViewById(R.id.txtLoginUsername);
        final EditText txtLoginPassword = (EditText) v.findViewById(R.id.txtLoginPassword);
        final ActionProcessButton btnSignIn = (ActionProcessButton) v.findViewById(R.id.btnSignIn);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
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
                            PreferencesModel preferences = new PreferencesModel(getActivity());
                            preferences.setUsername(mUsername);
                            preferences.setPassword(mPassword);

                            // Change view properties
                            btnSignIn.setProgress(100);
                            notifyCompleted();
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
                                new AlertDialog.Builder(getActivity())
                                        .setMessage("Error - " + jsonResponse.get("message").getAsString())
                                        .setCancelable(false)
                                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int id) {
                                            }
                                        }).create().show();
                            } catch (NullPointerException e) {
                                new AlertDialog.Builder(getActivity())
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
                    new AlertDialog.Builder(getActivity())
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

        return v;
    }
}