package in.iamkelv.balances.wizards.setupwizard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.codepond.wizardroid.WizardStep;

import in.iamkelv.balances.R;

public class SetupWizardStepNotifications extends WizardStep {

    public SetupWizardStepNotifications() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.setup_wizard_step_notifications, container, false);
        return v;
    }
}