package in.iamkelv.balances.wizards.setupwizard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;

import org.codepond.wizardroid.WizardStep;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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


            }
        });

        return v;
    }

    public void authUser() {

    }

}
