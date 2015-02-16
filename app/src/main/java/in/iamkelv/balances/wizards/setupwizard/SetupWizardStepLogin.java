package in.iamkelv.balances.wizards.setupwizard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;

import org.apache.http.HttpStatus;
import org.codepond.wizardroid.WizardStep;
import org.json.JSONException;
import org.json.JSONObject;
import com.loopj.android.http.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

import in.iamkelv.balances.R;

public class SetupWizardStepLogin extends WizardStep {

    // Member variables
    private String mUsername = "";
    private String mPassword = "";
    private String mBaseUrl = "https://balances.iamkelv.in/";

    // Methods

    public SetupWizardStepLogin() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.setup_wizard_step_login, container, false);

        final EditText txtUsername = (EditText) v.findViewById(R.id.txtLoginUsername);
        final EditText txtPassword = (EditText) v.findViewById(R.id.txtLoginPassword);
        final ActionProcessButton btnSignIn = (ActionProcessButton) v.findViewById(R.id.btnSignIn);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Change view properties
                btnSignIn.setEnabled(false);
                btnSignIn.setMode(ActionProcessButton.Mode.ENDLESS);
                btnSignIn.setProgress(1);

                // Assign variables
                try {
                    mUsername = URLEncoder.encode(txtUsername.getText().toString(), "UTF-8");
                    mPassword = URLEncoder.encode(txtPassword.getText().toString(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    Toast.makeText(getActivity(), "An unexpected error has occurred.", Toast.LENGTH_SHORT).show();
                }

                // Authenticate user
                authUser();
            }
        });

        return v;
    }

    public void authUser() {
        AuthUserTask authUserTask = new AuthUserTask();
        authUserTask.execute();
    }

    public String getBalances() {
        return "";
    }

    private class AuthUserTask extends AsyncTask<Object, Void, String> {

        @Override
        protected String doInBackground(Object... params) {

            try {
                String postParameters = "username=" + mUsername + "&password=" + mPassword;

                // Connect to URL
                URL authUrl = new URL(mBaseUrl + "auth");
                HttpsURLConnection connection = (HttpsURLConnection) authUrl.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                connection.setDoInput(true);
                connection.setDoOutput(true);

                connection.setFixedLengthStreamingMode(postParameters.getBytes().length);
                PrintWriter out = new PrintWriter(connection.getOutputStream());
                out.print(postParameters);
                out.close();

                connection.connect();

                // Get JSON data from connection
                int status = connection.getResponseCode();
                InputStream inputStream;
                if(status >= HttpStatus.SC_BAD_REQUEST) {
                    inputStream = connection.getErrorStream();
                } else {
                    inputStream = connection.getInputStream();
                }
                Reader reader = new InputStreamReader(inputStream);
                int contentLength = connection.getContentLength();
                char[] charArray = new char[contentLength];
                reader.read(charArray);
                String responseData = new String(charArray);

                JSONObject jsonResponse = new JSONObject(responseData);

                // Check for errors
                String error = jsonResponse.getString("error");

                if (error.equals("true")) {
                    String message = jsonResponse.getString("message");
                    throw new Exception(message);
                } else if (error.equals("false")) {
                    return "true";
                } else {
                    throw new Exception("unknown");
                }
            } catch (MalformedURLException e) {
                Log.e(SetupWizardStepLogin.class.getSimpleName(), "Exception caught: ", e);
                return "An unknown error has occurred.";
            } catch (JSONException e) {
                Log.e(SetupWizardStepLogin.class.getSimpleName(), "Exception caught: ", e);
                return "An unknown error has occurred.";
            } catch (IOException e) {
                Log.e(SetupWizardStepLogin.class.getSimpleName(), "Exception caught: ", e);
                return "An unknown error has occurred.";
            } catch (Exception e) {
                Log.e(SetupWizardStepLogin.class.getSimpleName(), "Exception caught: ", e);
                if (e.getMessage().equals("unknown")) {
                    return "An unknown error has occurred.";
                } else {
                    return e.getMessage();
                }
            }

        }

        @Override
        protected void onPostExecute(String result) {
            new AlertDialog.Builder(getActivity())
                .setMessage(result)
                .setCancelable(false)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                }).create().show();
        }
    }

}
