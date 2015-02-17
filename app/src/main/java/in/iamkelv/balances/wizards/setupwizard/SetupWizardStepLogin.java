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
import android.widget.EditText;

import com.dd.processbutton.iml.ActionProcessButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.codepond.wizardroid.WizardStep;
import org.json.JSONException;
import org.json.JSONObject;

import in.iamkelv.balances.R;

public class SetupWizardStepLogin extends WizardStep {

    // Member variables
    private String mUsername = "";
    private String mPassword = "";

    private String mBaseUrl = "https://balances.iamkelv.in/";

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

                    // Create parameters
                    RequestParams params = new RequestParams();
                    params.put("username", mUsername);
                    params.put("password", mPassword);
                    // Create headers
                    Header[] header = {
                            new BasicHeader("Content-Type", "application/x-www-form-urlencoded")
                    };
                    // Create client
                    AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
                    client.post(null, mBaseUrl + "auth", header, params, "application/x-www-form-urlencoded", new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            // Change view properties
                            btnSignIn.setProgress(100);
                            notifyCompleted();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject errorResponse) {
                            // Change view properties
                            btnSignIn.setProgress(0);
                            btnSignIn.setEnabled(true);
                            txtLoginUsername.setFocusableInTouchMode(true);
                            txtLoginUsername.setFocusable(true);
                            txtLoginPassword.setFocusableInTouchMode(true);
                            txtLoginPassword.setFocusable(true);

                            // Display alert dialog
                            try {
                                new AlertDialog.Builder(getActivity())
                                        .setMessage("Error - " + errorResponse.getString("message"))
                                        .setCancelable(false)
                                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int id) {
                                            }
                                        }).create().show();
                            } catch (JSONException e1) {
                                new AlertDialog.Builder(getActivity())
                                        .setMessage("Error - " + "An unknown error has occurred.")
                                        .setCancelable(false)
                                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int id) {
                                            }
                                        }).create().show();
                            }
                        }

                    });
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